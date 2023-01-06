package com.grigorev.srm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/security")
public class JwtController {

    private final JwtService jwtService;

    @Autowired
    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        jwtService.refreshTokens(request, response);
        return ResponseEntity.ok().build();
//        return new ResponseEntity<>(new Object(), HttpStatus.OK);
    }
}
