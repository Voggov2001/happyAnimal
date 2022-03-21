package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.UserRqDto;
import com.coderiders.happyanimal.model.dto.UserRsDto;
import com.coderiders.happyanimal.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final UserRepository repository;
    private static final String ERROR_MESSAGE_BAD_REQUEST = "Пользователь не найден";

    @Autowired
    public UserMapper(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserRsDto> mapUserListToDtoList(List<User> users) {
        return users.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public User mapToUser(UserRsDto dto) {
        return repository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_BAD_REQUEST));
    }

    public User mapToUser(UserRqDto dto) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(dto, User.class);
    }

    public UserRsDto mapToResponseDto(User user) {
        var modelMapper = new ModelMapper();
        return modelMapper.map(user, UserRsDto.class);
    }
}
