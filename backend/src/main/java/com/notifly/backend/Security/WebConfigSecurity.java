package com.notifly.backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {

    @Bean
    public SecurityFilterChain securityFilerChain(HttpSecurity http)throws Exception{
        // Disable CSRF protection
        http.csrf(csrfToken->csrfToken.disable());

        //Basic actions are enabled
         http.httpBasic(Customizer.withDefaults());
        




        return http.build();
    }
   
    
}
