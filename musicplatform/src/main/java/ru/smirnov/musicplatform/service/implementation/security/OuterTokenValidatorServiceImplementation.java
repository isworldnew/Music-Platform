package ru.smirnov.musicplatform.service.implementation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.smirnov.dtoregistry.AuthenticationResponse;
import ru.smirnov.dtoregistry.OuterTokenRequest;
import ru.smirnov.musicplatform.authentication.TokenUtils;
import ru.smirnov.musicplatform.exception.ForbiddenException;
import ru.smirnov.musicplatform.service.abstraction.security.OuterTokenValidatorService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OuterTokenValidatorServiceImplementation implements OuterTokenValidatorService {

    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    @Autowired
    public OuterTokenValidatorServiceImplementation(UserDetailsService userDetailsService, TokenUtils tokenUtils) {
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    /*
    @Override
    public UsernamePasswordAuthenticationToken validateOuterToken(OuterTokenRequest dto) {

        String jwtToken = dto.getJwtToken();
        String username = null;
        try {
            username = this.tokenUtils.extractUsername(jwtToken);
        }
        catch (ExpiredJwtException eje) {
            throw new ForbiddenException("Token has been expired");
        }
        catch (Exception e) {
            throw new ForbiddenException("Invalid token: unable to extract username from token");
        }

        UsernamePasswordAuthenticationToken authentication = null;

        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (tokenUtils.validateJwtToken(jwtToken, userDetails)) {

                Claims claims = tokenUtils.extractAllClaims(jwtToken);
                List<SimpleGrantedAuthority> authorities = ((List<Map<String, String>>) claims.get("authorities")).stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                        .collect(Collectors.toList());

                authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);
            }

            if (!userDetails.isEnabled()) {
                throw new ForbiddenException("Account became DISABLED after generating this token");
            }
        }
        catch (Exception e) {
            throw new ForbiddenException("Invalid token: unable to load data by username or validation fail");
        }

        return authentication;
    }
    */
    /*
    // работало
    @Override
    public AuthenticationResponse validateOuterToken(OuterTokenRequest dto) {

        String jwtToken = dto.getJwtToken();
        String username = null;

        try {
            username = this.tokenUtils.extractUsername(jwtToken);
        } catch (ExpiredJwtException eje) {
            throw new ForbiddenException("Token has been expired");
        } catch (Exception e) {
            throw new ForbiddenException("Invalid token: unable to extract username from token");
        }

        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (!tokenUtils.validateJwtToken(jwtToken, userDetails)) {
                throw new ForbiddenException("Invalid token: validation failed");
            }

            if (!userDetails.isEnabled()) {
                throw new ForbiddenException("Account became DISABLED after generating this token");
            }

            Claims claims = tokenUtils.extractAllClaims(jwtToken);
            List<String> authorities = ((List<Map<String, String>>) claims.get("authorities")).stream()
                    .map(authority -> authority.get("authority"))
                    .collect(Collectors.toList());

            return new AuthenticationResponse(userDetails.getUsername(), authorities);

        } catch (Exception e) {
            throw new ForbiddenException("Invalid token: unable to load data by username or validation fail");
        }
    }
    */
    @Override
    public AuthenticationResponse validateOuterToken(String jwtToken) {

        String username = null;

        try {
            username = this.tokenUtils.extractUsername(jwtToken);
        } catch (ExpiredJwtException eje) {
            throw new ForbiddenException("Token has been expired");
        } catch (Exception e) {
            throw new ForbiddenException("Invalid token: unable to extract username from token");
        }

        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (!tokenUtils.validateJwtToken(jwtToken, userDetails)) {
                throw new ForbiddenException("Invalid token: validation failed");
            }

            if (!userDetails.isEnabled()) {
                throw new ForbiddenException("Account became DISABLED after generating this token");
            }

            Claims claims = tokenUtils.extractAllClaims(jwtToken);
            List<String> authorities = ((List<Map<String, String>>) claims.get("authorities")).stream()
                    .map(authority -> authority.get("authority"))
                    .collect(Collectors.toList());

            return new AuthenticationResponse(userDetails.getUsername(), authorities);

        } catch (Exception e) {
            throw new ForbiddenException("Invalid token: unable to load data by username or validation fail");
        }
    }

}
