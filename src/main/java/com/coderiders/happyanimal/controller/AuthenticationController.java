package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.security.AuthenticationDto;
import com.coderiders.happyanimal.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserRepository userRepository,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity authenticate(@RequestBody AuthenticationDto authenticationDto) {
        String login = authenticationDto.getLogin();
        String password = authenticationDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        User user = userRepository.findByLogin(login).orElseThrow(
                () -> new NotFoundException("Пользователь с логином " + login + " не найден"));
        String token = jwtTokenProvider.createToken(login, user.getUserRole().name());
        return ResponseEntity.ok("login - " + login + "\ntoken - " + token);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(httpServletRequest, httpServletResponse, null);
    }
}
