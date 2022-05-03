package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.exceptions.BadRequestException;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.AuthenticationRsDto;
import com.coderiders.happyanimal.model.dto.UserRqDto;
import com.coderiders.happyanimal.model.dto.UserRsDto;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.security.MyUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class UserMapper {
    private final UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private static final String ERROR_MESSAGE_BAD_REQUEST = "Пользователь не найден";

    @Autowired
    public UserMapper(UserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    public User mapToUser(UserRqDto dto) {
        if (repository.findByLogin(dto.getLogin()).isPresent()){
            throw new BadRequestException("Пользователь с таким логином уже существует");
        }
        var modelMapper = new ModelMapper();
        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }

    public UserRsDto mapToResponseDto(User user) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(user, UserRsDto.class);
    }

    public MyUserDetails mapToMyUserDetails(User user) {
        return MyUserDetails.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .userRole(user.getUserRole())
                .isActive(user.isActive())
                .build();
    }

    public AuthenticationRsDto mapToAuthenticationRsDto(User user, String token) {
        return AuthenticationRsDto.builder()
                .id(user.getId())
                .userRole(user.getUserRole())
                .token(token)
                .build();
    }
}
