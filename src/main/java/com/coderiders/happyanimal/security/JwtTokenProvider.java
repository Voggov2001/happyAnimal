package com.coderiders.happyanimal.security;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.exceptions.UnAuthorizedException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserDetailsServiceIml userDetailsServiceIml;

    private String secretKey = "proselyte";

    @Autowired
    public JwtTokenProvider(UserDetailsServiceIml userDetailsServiceIml) {
        this.userDetailsServiceIml = userDetailsServiceIml;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userName, String role) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", role);
        Date now = new Date();
        long validityInMilliseconds = 604800;
        Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UnAuthorizedException("Токен истек или поврежден");
        }
    }

    Authentication getAuthentication(String token) {
        MyUserDetails userDetails = userDetailsServiceIml.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserName(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public UserRole getUserRole(String token) {
        MyUserDetails userDetails = userDetailsServiceIml.loadUserByUsername(getUserName(token));
        return userDetails.getUserRole();
    }

    String resolveToken(HttpServletRequest request) {
        String authorisationHeader = "Authorization";
        return request.getHeader(authorisationHeader);
    }
}
