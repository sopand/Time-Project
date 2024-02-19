package com.time.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.time.jwt.JWTFilter;
import com.time.jwt.JWTUtil;
import com.time.jwt.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	// AuthenticationManger를 불러올때 필요해서 생성자주입방식으로 받아옴
	private final AuthenticationConfiguration authenticationConfiguration;
	
	private final JWTUtil jwtUtil;
	
	
	
	// LoginFilter를 사용하기위해 Manager객체를 Bean 등록
	@Bean
	public AuthenticationManager getAuthManager(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
	}
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return web -> web.ignoring().requestMatchers(
	                                                 "/favicon.ico",
	                                                 "/swagger-ui/**",
	                                                 "/swagger-resources/**",
	                                                 "/v3/api-docs/**");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// CSRF disable
		http.csrf((csrfConfig) -> csrfConfig.disable())
//				.headers(
//						(headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
				// 경로별 인가 작업
				.authorizeHttpRequests(auth -> auth 
						.requestMatchers("/api/member/login","/","/api/member/signup").permitAll()
						.requestMatchers("/api/admin/**").hasAuthority("ADMIN")
						.anyRequest().authenticated());
		// http basic 인증 방식 disable
		http.httpBasic(auth -> auth.disable());
		// Form 로그인 disable
		http.formLogin((formLogin) -> formLogin.disable());
		
		
		
		// LoginFilter의 
		LoginFilter loginFilter = new LoginFilter(getAuthManager(authenticationConfiguration),jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/member/login");
		
		// 기본으로 설정되어있는 filter를 대체하기 위한것 UsernamePasswordAuthenticationFilter를  내가 만든 LoginFilter로 대체할것
		http.addFilterAt(loginFilter,UsernamePasswordAuthenticationFilter.class);
		//JWTFilter 등록
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
		// 세션 설정
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// logout disable
		http.logout(logout -> logout.disable());

		return http.build();
	}

}
