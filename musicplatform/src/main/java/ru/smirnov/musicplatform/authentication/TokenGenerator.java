package ru.smirnov.musicplatform.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.smirnov.dtoregistry.dto.authentication.DataForToken;
import ru.smirnov.musicplatform.dto.authentication.JwtResponse;
import ru.smirnov.musicplatform.dto.authentication.LoginRequest;

import java.util.*;

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

    public ResponseEntity<JwtResponse> createTokens(LoginRequest dto) {

        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        }
        catch (UsernameNotFoundException unfe) {
            throw new UsernameNotFoundException("No account with such username", unfe);
        }
        catch (BadCredentialsException bce) {
            throw new BadCredentialsException("Bad credentials (username, password, etc.)", bce);
        }
        catch (DisabledException de) {
            throw new DisabledException("Account is disabled", de);
        }


        UserDetails dataForToken = this.userDetailsService.loadUserByUsername(dto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtResponse(
                    this.generateJwtToken(dataForToken, JwtToken.ACCESS_TOKEN),
                    this.generateJwtToken(dataForToken, JwtToken.REFRESH_TOKEN)
                )
        );

    }

    /*
    нам для того, чтобы попасть в этот контроллер (и этот метод), сначала нужно пройти цепочку фильтров

    сначала выполнится фильтр, он полностью проверит токен (не повреждён ли он, можно ли из него данные вытащить, не истёк ли срок действия)

    потом он его установит в контекст

    только затем из него мы возьмём authority (что это refresh-токен)

    и вот только после всех этих проверок, если всё ок, мы уже можем попасть сюда и сделать новый токен

    тут как бэ... даже проверять ничего не нужно - фильтр на себя всю валидацию забирает
    */
    public ResponseEntity<JwtResponse> refreshTokens() {

        UserDetails dataForToken = this.userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new JwtResponse(
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
            List<SimpleGrantedAuthority> authorities = new ArrayList<>(dataForToken.getAuthorities());
            authorities.add(new SimpleGrantedAuthority(jwtToken.name()));

            claims.put("accountId", dataForToken.getAccountId());
            claims.put("role", dataForToken.getRole());
            claims.put("entityId", dataForToken.getEntityId());
            claims.put("authorities", authorities);
        }

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtToken.validityDuration()))
                .signWith(this.tokenUtils.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
