package com.time.jwt;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {
	
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,101,"토큰 시간이 만료되었습니다 재로그인해주세요"),
	WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED,102,"잘못된 토큰 정보입니다."),
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,103,"인증토큰이 존재하지 않습니다."),
	SERVER_ERROR(HttpStatus.UNAUTHORIZED,104,"서버의 문제로인해 요청이 실패하였습니다."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED,105,"알수 없는 이유로 요청이 거절되었습니다."),
	
	
	LOGIN_INFORMATION_DONT_MATCH(HttpStatus.UNAUTHORIZED,106,"로그인 정보가 일치하지 않습니다.")
	;
	
	private HttpStatus status;
	
	private Integer code;
	
	private String message;
}
