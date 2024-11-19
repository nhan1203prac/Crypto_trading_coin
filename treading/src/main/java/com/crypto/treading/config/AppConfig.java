package com.crypto.treading.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(Authorize->Authorize.requestMatchers("/api/**").authenticated()
				.anyRequest().permitAll()
				)
		.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
		.csrf(csrf->csrf.disable())
		.cors(cors->cors.configurationSource(corsConfigarutionSource()));
		
		return http.build();
	}

	private CorsConfigurationSource corsConfigarutionSource() {
		// TODO Auto-generated method stub	
		return new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				// TODO Auto-generated method stub
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
				cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));
				cfg.setAllowCredentials(true);
				cfg.setExposedHeaders(Arrays.asList("Authorization"));
				cfg.setAllowedHeaders(Collections.singletonList("*"));
				cfg.setMaxAge(3600L);
				return cfg;
			}
		};
	}
}
