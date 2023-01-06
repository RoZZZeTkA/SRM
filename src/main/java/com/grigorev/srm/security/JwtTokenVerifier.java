package com.grigorev.srm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.grigorev.srm.security.CustomHttpHeaders.ACCESS_TOKEN;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessTokenHeader = request.getHeader(ACCESS_TOKEN);
        if (accessTokenHeader == null || !accessTokenHeader.startsWith(jwtConfig.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String accessToken = accessTokenHeader.substring(jwtConfig.getPrefix().length());
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                    .parseClaimsJws(accessToken);
            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                            .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                            .collect(Collectors.toSet());
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            response.setHeader("error", e.getMessage());
            throw new IllegalStateException("Access token is not valid");
        }
        filterChain.doFilter(request, response);
    }
}
