package com.academy.eventhub.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;


import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET,"/api/events/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/events/*/book").authenticated()
            .requestMatchers(HttpMethod.POST,"/api/events/**").hasRole("ORGANIZER")
            .requestMatchers(HttpMethod.PUT,"/api/events/**").hasRole("ORGANIZER")
            .requestMatchers(HttpMethod.GET,"/api/venues/**").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/venues/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/venues/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/venues/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/speakers/**").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/speakers/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/speakers/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/speakers/**").hasRole("ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/organizer/**").hasRole("ORGANIZER")
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
       .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            return http.build();
}

 @Bean
public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    manager.setUsersByUsernameQuery(
        "select username, password, enabled from users where username=?"
    );
    manager.setAuthoritiesByUsernameQuery(
        "select u.username, r.name from users u join role r on u.role_id = r.id where u.username=?"
    );
    return manager;
}


@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
}