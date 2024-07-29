package com.thantrick.springboot_jwt_security.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created on 30-07-2024
 *
 * @author : Rithesh Nagaraj
 * @project : SpringBoot_JWT_Security
 */

/*      This 'BasicAuthenticationFilter' class in SpringSecurity
        process HTTP Requests with basic Authorization headers
        and puts the result in Spring Security Context holder     */

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request){

        String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);
        String token = authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");

        byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.TOKEN_SECRET.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        Jwt<Header, Claims> jwt = jwtParser.parse(token);
        String subject = jwt.getBody().getSubject();

        if(subject == null)
            return null;

        return new UsernamePasswordAuthenticationToken(subject, null, new ArrayList<>());
    }

}
