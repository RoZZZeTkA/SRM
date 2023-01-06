package com.grigorev.srm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtConfig jwtConfig;

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtConfig jwtConfig) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig), JwtAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/test/hello", "/security/refresh").permitAll()
                .antMatchers("/test/**").hasRole("USER")
                .anyRequest()
                .authenticated();

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }

}
