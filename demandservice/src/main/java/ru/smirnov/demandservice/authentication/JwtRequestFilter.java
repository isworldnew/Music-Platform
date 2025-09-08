package ru.smirnov.demandservice.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    @Autowired
    public JwtRequestFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = tokenUtils.parseClaims(token);

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("Token has expired");
            }


            String username = claims.getSubject();
            Long accountId = claims.get("accountId", Long.class);
            Long entityId = claims.get("entityId", Long.class);
            String role = claims.get("role", String.class);
            List<SimpleGrantedAuthority> authorities = ((List<Map<String, String>>) claims.get("authorities")).stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                    .collect(Collectors.toList());

            DataForToken tokenData = DataForToken.builder()
                    .username(username)
                    .accountId(accountId)
                    .entityId(entityId)
                    .role(role)
                    .authorities(authorities)
                    .enabled(true)
                    .build();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    tokenData, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"exceptionMessage\": \"" + "Invalid token: unable to extract data from token" + "\"}");
            return;
        }

        chain.doFilter(request, response);
    }

}