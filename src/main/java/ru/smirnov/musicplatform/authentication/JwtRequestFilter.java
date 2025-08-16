package ru.smirnov.musicplatform.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.smirnov.musicplatform.service.AccountService;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AccountService accountService;
    private final TokenUtils tokenUtils;

    @Autowired
    public JwtRequestFilter(AccountService accountService, TokenUtils tokenUtils) {
        this.accountService = accountService;
        this.tokenUtils = tokenUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                username = tokenUtils.extractUsername(jwtToken);
            } catch (Exception e) {
                logger.error("Cannot extract username from JWT token: {}");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = accountService.loadUserByUsername(username);

            // ограничение на использование в заголовке (для общения с API) только access-токена
            if (!this.tokenUtils.tokenPresentsType(jwtToken, JwtToken.ACCESS_TOKEN)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "For communicating with API access-token is required");
                return;
            }

            if (tokenUtils.validateJwtToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
