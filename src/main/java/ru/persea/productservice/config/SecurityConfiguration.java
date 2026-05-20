package ru.persea.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) {
    //     http
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers(
    //                 "/swagger-ui/**",
    //                 "/swagger-ui.html"
    //             ).permitAll()
    //             .anyRequest().authenticated()
    //         )
    //         .oauth2ResourceServer(oauth2 -> oauth2
    //             .jwt(Customizer.withDefaults())
    //         );

    //     return http.build();
    // }
}
