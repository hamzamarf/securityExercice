package com.example.securityExercice.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.securityExercice.Service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtF jwtfilter;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception
    { http
        .csrf(csrf->csrf.disable())
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login").permitAll()
        .requestMatchers("/files/**").permitAll() // plus besoin de rôle
       .anyRequest().authenticated()
)
        .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
}
@Bean 
public AuthenticationProvider authenticationProvider()
{DaoAuthenticationProvider provider =new DaoAuthenticationProvider();
    provider.setPasswordEncoder( passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    return provider;
}
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception 
{ return config.getAuthenticationManager(           );}

@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // toutes les routes
                        .allowedOrigins("http://localhost:5173") // ton front React
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
