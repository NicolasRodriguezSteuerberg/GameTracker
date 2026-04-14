package com.nsteuerberg.gametracker.auth.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nsteuerberg.gametracker.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    private final static Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Quiteamos el inicio 'Bearer '
        final String token = authHeader.substring(7);

        try {
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(
                    jwtService.extractUserId(decodedJWT),
                    null,
                    jwtService.extractAuthorities(decodedJWT)
            ));
            SecurityContextHolder.setContext(context);
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException ex) {
            handleError(response, HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "Expired Token");
        } catch (JWTVerificationException ex) {
            handleError(response, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Invalid JWT Token or incorrect sign");
        } catch (Exception ex) {
            handleError(response, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Internal error when processing JWT token");
        }

    }

    private void handleError(HttpServletResponse response, HttpStatus status, String error, String message) throws IOException {
        SecurityContextHolder.clearContext();
        logger.warn("{}: {}", error, message);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> payload = new HashMap<>();
        payload.put("error", error);
        payload.put("message", message);

        objectMapper.writeValue(response.getWriter(), payload);
    }
}
