package com.time.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.time.entity.Member;
import com.time.enums.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter { // 요청에 대해 1번만 작동하는 필터

	private final JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Request에서 Authorization 헤더를 찾음
		String authorization = request.getHeader("Authorization");
		
		//Authorization 헤더 검증   
		if(authorization == null || !authorization.startsWith("Bearer ")) { // null OR 접두사에 이상이 있을경우
			log.error("인증 토큰이 없거나 데이터가 변조되었습니다."); 
			filterChain.doFilter(request, response);
			return;
		}
		log.info("인증 진행중");
		
		//Bearer 부분 제거 후 순수 토큰만 획득
		String token = authorization.split(" ")[1];
		
		//토큰 소멸시간 검증
		if(jwtUtil.isExpired(token)) {
			log.error("토큰 인증시간이 만료되었습니다 재로그인해주세요.");
			filterChain.doFilter(request, response);
			return;
		}
		
		//토큰에서 username과 role 획득
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);
		
		// Member Entity생성하여 값 set
		Member memberData = Member.builder()
				.email(username)
				.role(Role.fromString(role))
				.password("adsadasd")
				.build();
		
		//UserDetail에 회원정보 객체담기
		CustomUserDetails customUserDetails = new CustomUserDetails(memberData);
		
		//스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        
        filterChain.doFilter(request, response);
		
	}
}
