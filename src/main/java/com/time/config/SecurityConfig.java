package com.time.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	 @Bean
	 public PasswordEncoder passwordEncoder(){
	     return new BCryptPasswordEncoder();
	 }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrfConfig) -> csrfConfig.disable()
				)
		.headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()
				)
		).authorizeHttpRequests((authorizeRequests)->
			authorizeRequests
				.requestMatchers("/**").permitAll()
				.anyRequest().authenticated()
				);
		http.formLogin( (formLogin) ->
			formLogin.disable()
		);
		http.logout(login -> 
			login.disable()
		);
		
		return http.build();
	}

}
