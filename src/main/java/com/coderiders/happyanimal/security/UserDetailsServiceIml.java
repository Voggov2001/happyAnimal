package com.coderiders.happyanimal.security;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.UserMapper;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceIml implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final String NOT_FOUND_EXCEPTION = "Пользователь с данным логином не найден";

    @Autowired
    public UserDetailsServiceIml(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_EXCEPTION));
        return null;
    }
}
