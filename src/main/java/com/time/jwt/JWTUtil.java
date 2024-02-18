package com.time.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {
	
	// String문자열 키는 JWT에서 사용하지 않음, 그래서 프로퍼티의 key를 기반으로 SecretKey라는 객체 키를 생성
	private SecretKey secretKey;
	
	public JWTUtil (@Value("${jwt.secret}")String secret) {
		secretKey=new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
	}
	
	// 검증을 진행할 3개의 메서드 Role,name,expired
	public String getUsername(String token) {
		// verifyWith = 우리 서버의 key와 맞는지 매칭 
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username",String.class);
	}
	
	public String getRole(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role",String.class);
	}
	
	public Boolean isExpired(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
	}
	
	
	// JWT토큰을 생성하는 메서드
	
	public String createJwt(String username,String role,Long expiredMs) {
		return Jwts.builder()
				.claim("username", username)
				.claim("role", role)
				.issuedAt(new Date(System.currentTimeMillis()))  // 토큰의 생성시간
				.expiration(new Date(System.currentTimeMillis()+expiredMs)) // 토큰의 만료시간
				.signWith(secretKey)  // 암호화 키 
				.compact();
	}
	
	
	


}
