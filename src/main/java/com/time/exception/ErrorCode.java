package com.time.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"입력 정보 유효성 인증 실패"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저 정보를 찾을 수 없습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT,"해당 이메일은 이미 등록되어 있습니다."),
	DATA_INSERT_FAILED(HttpStatus.BAD_REQUEST,"알 수 없는 이유로 데이터 저장에 실패하였습니다 재시도해주세요."),
	SID_DONT_MATCH(HttpStatus.FORBIDDEN,"해당 데이터에 접근할 권한이 없습니다."),
	SCHEDULE_TIME_DUPLICATION(HttpStatus.CONFLICT,"기존 일정과 시간이 중복됩니다"),
	
	JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,"접근 권한이 없습니다."),
	TOKEN_ACCESS_TIME_OUT(HttpStatus.UNAUTHORIZED,"토큰 시간이 만료되었습니다 재로그인해주세요");
	
	
	private final HttpStatus status;
	private final String message;

}
