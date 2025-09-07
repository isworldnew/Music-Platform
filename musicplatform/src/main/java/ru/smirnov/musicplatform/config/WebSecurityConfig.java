package ru.smirnov.musicplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.smirnov.musicplatform.authentication.JwtRequestFilter;
import ru.smirnov.musicplatform.authentication.JwtToken;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAP = new DaoAuthenticationProvider(userDetailsService);
        daoAP.setPasswordEncoder(this.bCryptPasswordEncoder);
        return daoAP;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/authentication/login", "/error").permitAll()
                        .requestMatchers("/users/registration").permitAll()
                        .requestMatchers("/authentication/validate").hasAnyAuthority(JwtToken.ACCESS_TOKEN.name())

                        .requestMatchers(HttpMethod.GET, "/artists/{id}").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/tracks/{id}/listen").hasAnyAuthority(JwtToken.ACCESS_TOKEN.name(), "ROLE_ANONYMOUS")

                        .requestMatchers("/tracks/search").hasAnyAuthority(JwtToken.ACCESS_TOKEN.name(), "ROLE_ANONYMOUS") //.anonymous()
                        .requestMatchers("/albums/search").hasAnyAuthority(JwtToken.ACCESS_TOKEN.name(), "ROLE_ANONYMOUS")
                        .requestMatchers("/playlists/search").hasAnyAuthority(JwtToken.ACCESS_TOKEN.name(), "ROLE_ANONYMOUS")
                        .requestMatchers("/charts/search").hasAnyAuthority(JwtToken.ACCESS_TOKEN.name(), "ROLE_ANONYMOUS")

                        .requestMatchers("/artists/search").permitAll()

                        .requestMatchers("/authentication/refresh").hasAuthority(JwtToken.REFRESH_TOKEN.name())


                        .anyRequest().hasAuthority(JwtToken.ACCESS_TOKEN.name())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(this.daoAuthenticationProvider())
                .addFilterBefore(this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
