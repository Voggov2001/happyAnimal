package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.exceptions.UnAuthorizedException;
import com.coderiders.happyanimal.mapper.UserMapper;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.AuthenticationRsDto;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.security.AuthenticationDto;
import com.coderiders.happyanimal.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserRepository userRepository,
                                    JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationRsDto> authenticate(@RequestBody AuthenticationDto authenticationDto) {
        try {
            String login = authenticationDto.getLogin();
            String password = authenticationDto.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            User user = userRepository.findByLogin(login).orElseThrow(
                    () -> new NotFoundException("???????????????????????? ?? ?????????????? " + login + " ???? ????????????"));
            String token = jwtTokenProvider.createToken(login, user.getUserRole().name());
            var created = userMapper.mapToAuthenticationRsDto(user, token);
            var url = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(created.getId())
                    .toUri();
            return ResponseEntity.created(url).body(created);
        } catch (AuthenticationException e) {
            throw new UnAuthorizedException(e.getLocalizedMessage());
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(httpServletRequest, httpServletResponse, null);
    }
}
