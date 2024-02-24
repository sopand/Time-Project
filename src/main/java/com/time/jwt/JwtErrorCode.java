package com.time.jwt;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {
	
	SID_DONT_MATCH(HttpStatus.FORBIDDEN,100,"해당 데이터에 접근할 권한이 없습니다."),
	JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,101,"접근 권한이 없습니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,102,"토큰 시간이 만료되었습니다 재로그인해주세요"),
	WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED,103,"잘못된 토큰 정보입니다."),
	UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED,104,"인증토큰이 존재하지 않습니다."),
	UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED,105,"지원하지 않는 토큰입니다."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED,106,"알수 없는 이유로 요청이 거절되었습니다.");
	
	private HttpStatus status;
	
	private Integer code;
	
	private String message;
}
