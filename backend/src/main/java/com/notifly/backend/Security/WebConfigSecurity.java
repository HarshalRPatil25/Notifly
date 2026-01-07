package com.notifly.backend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.notifly.backend.Security.JWT.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {

     @Autowired
    private JwtAuthFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilerChain(HttpSecurity http)throws Exception{
        // Disable CSRF protection
        http.csrf(csrfToken->csrfToken.disable());

        //Basic actions are enabled
         http.httpBasic(Customizer.withDefaults());
        

         //Authenticating all requests

         http.authorizeHttpRequests(request->request.
            requestMatchers("/api/user/**").authenticated()
            .requestMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated()
         );
         
         http.addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);




        return http.build();
    }
   
    
}
