package ru.smirnov.musicplatform.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.smirnov.musicplatform.dto.authentication.JwtResponseDto;
import ru.smirnov.musicplatform.dto.authentication.LoginRequestDto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenGenerator {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    @Autowired
    public TokenGenerator(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    public ResponseEntity<JwtResponseDto> createTokens(LoginRequestDto dto) {

        UserDetails dataForToken = this.userDetailsService.loadUserByUsername(dto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtResponseDto(
                    this.generateJwtToken(dataForToken, JwtToken.ACCESS_TOKEN),
                    this.generateJwtToken(dataForToken, JwtToken.REFRESH_TOKEN)
                )
        );

    }

    public ResponseEntity<JwtResponseDto> refreshTokens() {

        // сначала случился затуп: "как же мне теперь добраться до токена, который в заголовке??
        // это что, мне теперь ещё и тут его получать и проверять??" а потом понял...
        // у меня же уже есть JwtRequestFilter, который уже в момент вызова эндпоинта проверит, что токен именно refresh
        // и что он валиден
        // было бы вообще отлично, если бы тут его можно было перевызвать...

        // ПРОВЕРЬ CONTEXT НА NULL

        UserDetails dataForToken = this.userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtResponseDto(
                        this.generateJwtToken(dataForToken, JwtToken.ACCESS_TOKEN),
                        this.generateJwtToken(dataForToken, JwtToken.REFRESH_TOKEN)
                )
        );

    }

    private String generateJwtToken(UserDetails userDetails, JwtToken jwtToken) {

        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof DataForToken dataForToken) {

            // прямо в authorities буду хранить тип токена, что автоматически "наделяет"
            // refresh-токен правом быть переданным в заголовке для эндпоинта /refresh
            dataForToken.addAuthority(new SimpleGrantedAuthority(jwtToken.name()));
            // правда, можно было бы добавлять эту authority только для refresh-токена

            claims.put("accountId", dataForToken.getAccountId());
            claims.put("role", dataForToken.getRole());
            claims.put("entityId", dataForToken.getEntityId());
        }

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtToken.validityDuration()))
                .signWith(this.tokenUtils.getSigningKey(), SignatureAlgorithm.ES256)
                .compact();
    }

}
