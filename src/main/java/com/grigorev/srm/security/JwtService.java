package com.grigorev.srm.security;

import com.grigorev.srm.entity.User;
import com.grigorev.srm.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static com.grigorev.srm.security.CustomHttpHeaders.ACCESS_TOKEN;
import static com.grigorev.srm.security.CustomHttpHeaders.REFRESH_TOKEN;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;

    private final UserService userService;

    @Autowired
    public JwtService(JwtConfig jwtConfig, UserService userService) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    public void refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenHeader = request.getHeader(REFRESH_TOKEN);
        if (refreshTokenHeader == null || !refreshTokenHeader.startsWith(jwtConfig.getPrefix())) {
            throw new IllegalStateException("Refresh token is not valid");
        }
        String refreshToken = refreshTokenHeader.substring(jwtConfig.getPrefix().length());
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .parseClaimsJws(refreshToken);
        Claims body = claimsJws.getBody();
        String username = body.getSubject();
        User user = userService.findByUsername(username);
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities =
                Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim("authorities", simpleGrantedAuthorities)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getExpirationAfterDays())))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();
        refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(jwtConfig.getExpirationAfterWeeks())))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .compact();

        response.addHeader(ACCESS_TOKEN, jwtConfig.getPrefix() + accessToken);
        response.addHeader(REFRESH_TOKEN, jwtConfig.getPrefix() + refreshToken);
    }
}
