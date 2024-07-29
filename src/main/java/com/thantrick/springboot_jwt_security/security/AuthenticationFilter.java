package com.thantrick.springboot_jwt_security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thantrick.springboot_jwt_security.SpringApplicationContext;
import com.thantrick.springboot_jwt_security.model.UserLoginRequestModel;
import com.thantrick.springboot_jwt_security.repository.UserRepository;
import com.thantrick.springboot_jwt_security.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

/**
 * Created on 30-07-2024
 *
 * @author : Rithesh Nagaraj
 * @project : SpringBoot_JWT_Security
 */

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            UserLoginRequestModel credentials = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword(), new ArrayList<>());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.TOKEN_SECRET.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        String userName = ((User) authResult.getPrincipal()).getUsername();
        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(Date.from(Instant.now().plusMillis(SecurityConstants.EXPIRATION_TIME)))
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();

        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX +" "+ token);

        /*  In case of delete an user /update an user then we need UserId -> We can add userId in response header  */

        /*  We have access to email (userName) in this class - we can call getUserByEmail() of UserService.
            But we can not access userService in this class. Because AuthenticationFilter class is not a spring bean.
            So we can create a class which extends 'ApplicationContextAware' interface
                    ----> Refer SpringApplicationContext class          */

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
        response.addHeader("userId", userService.getUserByEmail(userName).getUserId());
    }


}
