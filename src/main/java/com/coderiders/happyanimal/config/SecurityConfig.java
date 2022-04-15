package com.coderiders.happyanimal.config;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.security.MyUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                createUserDetails("superAdmin", UserRole.SUPER_ADMIN.getAuthorities()),
                createUserDetails("employee", UserRole.EMPLOYEE.getAuthorities()),
                createUserDetails("admin", UserRole.ADMIN.getAuthorities()),
                createUserDetails("veterinarian", UserRole.VETERINARIAN.getAuthorities())
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private MyUserDetails createUserDetails(String username, Set<SimpleGrantedAuthority> authorities) {
        return MyUserDetails.builder()
                .username(username)
                .password(passwordEncoder().encode(username))
                .authorities(authorities)
                .isLocked(true)
                .build();
    }
}
