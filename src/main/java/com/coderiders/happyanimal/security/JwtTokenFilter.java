package com.coderiders.happyanimal.security;

import com.coderiders.happyanimal.controller.handler.ErrorDto;
import com.coderiders.happyanimal.exceptions.UnAuthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        if (token != null && token.matches("Bearer .*")) {
            var tokenArray = token.split(" ");
            token = tokenArray[1];
        }
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (UnAuthorizedException e) {
            SecurityContextHolder.clearContext();
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorDto errorDto = new ErrorDto(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getLocalizedMessage());
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.getWriter().write(objectMapper.writeValueAsString(errorDto));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
