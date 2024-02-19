package com.time.jwt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 요청일 경우
	private final JWTUtil jwtUtil;

	@Getter
	@Setter
	private static class ReqLoginData { // form 로그인 / json 로그인 방식에따라 데이터 받아오는 방식이 달라서 만든객체
		private String username;
		private String password;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ReqLoginData loginData=null;
		
		
		if(request.getContentType().equals(CONTENT_TYPE)) {  // 시큐리티는 form아니면 obtain에서 못가져온다..
			ObjectMapper objectMapper = new ObjectMapper();
			try (InputStream inputStream = request.getInputStream()) {
				loginData = objectMapper.readValue(inputStream, ReqLoginData.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			loginData.setUsername(obtainUsername(request));
			loginData.setPassword(obtainPassword(request));
		}
		
		
		log.info("로그인 사용자 이메일 : {} 패스워드 : {}", loginData.getUsername(), loginData.getPassword());
		// 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야한다.
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword(), null);

		// token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
	}

	// 로그인 성공시 실행하는 메소드 ( 여기서 JWT를 발급하면된다 )
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

		String username = customUserDetails.getUsername();
		Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();

		String role = auth.getAuthority();

		String token = jwtUtil.createJwt(username, role, 2000 * 2000 * 30L);

		// HTTP인증 방식은 RFC7235정의에 따라 인증방식 : 타입 인증토큰 < 형태를 가져야함
		// 예시 : Authorization: Bearer 인증토큰문자열
		response.addHeader("Authorization", "Bearer " + token);

		log.info("로그인 성공하였습니다");
	}

	// 로그인 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.error("로그인에 실패하였습니다");
		// 로그인 실패시 401 응답코드 발생
		response.setStatus(401);

	}

}
