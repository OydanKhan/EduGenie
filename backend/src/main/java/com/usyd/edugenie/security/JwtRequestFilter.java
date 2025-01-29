package com.usyd.edugenie.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.service.JwtTokenProvider;
import com.usyd.edugenie.service.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();

        if (requestPath.startsWith("/user/verify")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("authorization");
        final String prefix = "Bearer ";
        System.out.println("authorizationHeader: " + authorizationHeader);
        try {
            String token = authorizationHeader.substring(prefix.length());
            DecodedJWT decodedJwt = jwtTokenProvider.verifyJwtToken(token);
            String userEmail = jwtTokenProvider.getUserEmailFromDecoded(decodedJwt);
            Optional<Users> user = usersService.getUserByEmail(userEmail);
            System.out.println("user by email: " + user);
            if (user.isEmpty()) {
                throw new Exception();
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user.get(),
                    null,
                    Collections.emptyList()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Please Sign in");
        }

        filterChain.doFilter(request, response);
    }
}
