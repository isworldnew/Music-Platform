package ru.smirnov.demandservice.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.smirnov.dtoregistry.AuthenticationResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /*
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/authentication/validate";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            OuterTokenRequest dto = new OuterTokenRequest();
            dto.setJwtToken(jwtToken);

            // Создаем HttpEntity с телом запроса и заголовками
            HttpEntity<OuterTokenRequest> entity = new HttpEntity<>(dto, headers);

            try {
                // Выполняем POST-запрос
                ResponseEntity<AuthenticationResponse> serviceResponse = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        AuthenticationResponse.class
                );

                AuthenticationResponse authenticationResponse = serviceResponse.getBody();
                // вот тут нужно вытащить нужные данные и добавить их в контекст
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);


                System.out.println("Response Status: " + serviceResponse.getStatusCode());
                System.out.println("Response Body: " + serviceResponse.getBody());
            } catch (Exception e) {
                System.err.println("Error occurred: " + e.getMessage());
            }
        }



        filterChain.doFilter(request, response);
    }
    */
    /*
    // работало
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            System.out.println(jwtToken);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/authentication/validate";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            OuterTokenRequest dto = new OuterTokenRequest();
            dto.setJwtToken(jwtToken);

            HttpEntity<OuterTokenRequest> entity = new HttpEntity<>(dto, headers);

            try {
                ResponseEntity<AuthenticationResponse> serviceResponse = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        AuthenticationResponse.class
                );

                AuthenticationResponse authenticationResponse = serviceResponse.getBody();

                if (authenticationResponse != null) {
                    String username = authenticationResponse.getUsername();
                    List<String> authorities = authenticationResponse.getAuthorities();

                    List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            grantedAuthorities
                    );

                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(upat);

                    System.out.println("Response Status: " + serviceResponse.getStatusCode());
                    System.out.println("Response Body: " + serviceResponse.getBody());
                }

            } catch (Exception e) {
                System.err.println("Error occurred: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
    */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // Убираем "Bearer " из заголовка
            System.out.println("Extracted JWT Token: " + jwtToken);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/authentication/validate";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwtToken); // Передаем токен в заголовке
            headers.add("Content-Type", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<AuthenticationResponse> serviceResponse = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        AuthenticationResponse.class
                );

                AuthenticationResponse authenticationResponse = serviceResponse.getBody();

                if (authenticationResponse != null) {
                    String username = authenticationResponse.getUsername();
                    List<String> authorities = authenticationResponse.getAuthorities();

                    List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            grantedAuthorities
                    );

                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(upat);

                    System.out.println("Response Status: " + serviceResponse.getStatusCode());
                    System.out.println("Response Body: " + serviceResponse.getBody());
                }

            } catch (Exception e) {
                System.err.println("Error occurred: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
