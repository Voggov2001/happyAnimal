package com.coderiders.happyanimal.config;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.security.JwtConfigurer;
import com.coderiders.happyanimal.security.MyUserDetails;
import com.coderiders.happyanimal.security.UserDetailsServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    private final UserDetailsServiceIml userDetailsServiceIml;
    private final JwtConfigurer jwtConfigurer;

    @Autowired
    public SecurityConfig(UserDetailsServiceIml userDetailsServiceIml, JwtConfigurer jwtConfigurer) {
        this.userDetailsServiceIml = userDetailsServiceIml;
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/auth/login").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .apply(jwtConfigurer);
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

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    private MyUserDetails createUserDetails(String username, Set<SimpleGrantedAuthority> authorities) {
        return MyUserDetails.builder()
                .username(username)
                .password(passwordEncoder().encode(username))
                .authorities(authorities)
                .isActive(true)
                .build();
    }
}
