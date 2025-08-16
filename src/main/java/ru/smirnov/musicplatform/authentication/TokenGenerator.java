package ru.smirnov.musicplatform.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.authentication.JwtResponseDto;
import ru.smirnov.musicplatform.dto.authentication.LoginRequestDto;
import ru.smirnov.musicplatform.dto.authentication.RefreshRequestDto;
import ru.smirnov.musicplatform.exception.InvalidRefreshTokenException;
import ru.smirnov.musicplatform.exception.RefreshTokenExpectedException;
import ru.smirnov.musicplatform.exception.RefreshTokenIsExpiredException;
import ru.smirnov.musicplatform.service.AccountService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenGenerator {

    @Value("${jwt.secret}")
    private String secret;

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;

    @Autowired
    public TokenGenerator(
            AccountService accountService,
            AuthenticationManager authenticationManager,
            TokenUtils tokenUtils
    ) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    public ResponseEntity<JwtResponseDto> createTokens(LoginRequestDto dto) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        }
        catch (DisabledException de) {
            throw new Exception("User is disabled", de);
        }
        catch (BadCredentialsException bce) {
            throw new Exception("Invalid credentials (username or password)", bce);
        }

        UserDetails dataForToken = this.accountService.loadUserByUsername(dto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtResponseDto(
                        this.generateJwtToken(dataForToken, JwtToken.ACCESS_TOKEN),
                        this.generateJwtToken(dataForToken, JwtToken.REFRESH_TOKEN)
                )
        );

    }

    public ResponseEntity<JwtResponseDto> refreshTokens(RefreshRequestDto dto) {

        if (this.tokenUtils.isTokenExpired(dto.getRefreshToken()))
            throw new RefreshTokenIsExpiredException("Refresh token has been expired");

        if (!this.tokenUtils.tokenPresentsType(dto.getRefreshToken(), JwtToken.REFRESH_TOKEN))
            throw new RefreshTokenExpectedException("To refresh tokens refresh-token is needed");

        UserDetails userDetails = this.accountService.loadUserByUsername(
                this.tokenUtils.extractUsername(dto.getRefreshToken())
        );

        if (!this.tokenUtils.validateJwtToken(dto.getRefreshToken(), userDetails))
            throw new InvalidRefreshTokenException("Invalid refresh-token (wrong username, etc.)");


        return ResponseEntity.status(HttpStatus.CREATED).body(
            new JwtResponseDto(
                    this.generateJwtToken(userDetails, JwtToken.ACCESS_TOKEN),
                    this.generateJwtToken(userDetails, JwtToken.REFRESH_TOKEN)
            )
        );

    }

    private String generateJwtToken(UserDetails userDetails, JwtToken jwtToken) {

        Map<String, Object> claims = new HashMap<>();

        // Дополнительные поля из DataForToken
        if (userDetails instanceof DataForToken dataForToken) {
            claims.put("accountId", dataForToken.getAccountId());
            claims.put("role", dataForToken.getRole());
            claims.put("entityId", dataForToken.getEntityId());
            claims.put("tokenType", jwtToken.name()); // для проверки того, что для общения
            // с API пользователь использует именно ACCESS_TOKEN, буду использовать это поле
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtToken.validityDuration()))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
