package com.grigorev.srm.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static com.grigorev.srm.security.CustomHttpHeaders.ACCESS_TOKEN;
import static com.grigorev.srm.security.CustomHttpHeaders.REFRESH_TOKEN;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtConfig jwtConfig;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getExpirationAfterDays())))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(jwtConfig.getExpirationAfterWeeks())))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();

        response.addHeader(ACCESS_TOKEN, jwtConfig.getPrefix() + accessToken);
        response.addHeader(REFRESH_TOKEN, jwtConfig.getPrefix() + refreshToken);
    }
}
