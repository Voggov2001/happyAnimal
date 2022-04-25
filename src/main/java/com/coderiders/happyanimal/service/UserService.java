package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.UserMapper;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.UserRqDto;
import com.coderiders.happyanimal.model.dto.UserRsDto;
import com.coderiders.happyanimal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String ERROR_MESSAGE_NOT_FOUND = "Пользователь не найден";

    @Autowired
    public UserService(UserRepository userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.userMapper = mapper;
    }

    @Transactional
    public UserRsDto saveUser(UserRqDto userRqDto) {
        var user = userMapper.mapToUser(userRqDto);
        userRepository.save(user);
        return userMapper.mapToResponseDto(user);
    }

    @Transactional
    public Page<UserRsDto> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::mapToResponseDto);
    }

    @Transactional
    public UserRsDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND));
        return userMapper.mapToResponseDto(user);
    }

    @Transactional
    public Page<UserRsDto> getByName(String name, Pageable pageable) {
        Page<User> users = userRepository.getAllByNameContainsIgnoreCase(name, pageable);
        return users.map(userMapper::mapToResponseDto);
    }

    @Transactional
    public Page<UserRsDto> getAllByRole(UserRole role, Pageable pageable) {
        Page<User> users = userRepository.getAllByUserRole(role, pageable);
        return users.map(userMapper::mapToResponseDto);
    }
}
