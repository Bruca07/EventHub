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
            .requestMatchers(
                "/",
                "/index.html",
                "/login.html",
                "/signup.html",
                "/events.html",
                "/event-detail.html",
                "/my-bookings.html",        
                "/organizer-events.html",   
                "/admin.html",             
                "/feedback.html",
                "/profile.html",         
                "/favicon.ico",
                "/error",
                "/css/**",
                "/js/**",
                "/*.css",
                "/*.js"
            ).permitAll()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/events/*/book").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/events/**").hasAuthority("ROLE_ORGANIZER")
            .requestMatchers(HttpMethod.PUT, "/api/events/**").hasAuthority("ROLE_ORGANIZER")
            .requestMatchers(HttpMethod.GET, "/api/venues/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/feedBacks/**").permitAll() 
            .requestMatchers(HttpMethod.POST, "/api/feedBacks/**").authenticated() 
            .requestMatchers(HttpMethod.PUT, "/api/feedBacks/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/feedBacks/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/venues/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/venues/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/venues/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/speakers/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/speakers/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/speakers/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/speakers/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers("/api/organizer/**").hasAuthority("ROLE_ORGANIZER")
            .requestMatchers(HttpMethod.DELETE, "/api/tickets/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/tags/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/tags/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/tags/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/tags/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/tickets/event/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/tickets/my").authenticated()
            .anyRequest().authenticated()
        )
        .httpBasic(basic -> basic
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(401);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized\"}");
            })
        )
   .sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
);

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