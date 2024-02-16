package com.time.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"입력 정보 유효성 인증 실패"),
	NOT_LOGIN_USER(HttpStatus.BAD_REQUEST,"로그인 된 유저만 접근 가능합니다."),
	SESSION_USER_NOT_FOUND(HttpStatus.NOT_FOUND,"로그인 세션과 일치하는 유저정보가 존재하지 않습니다."),
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,"찾을 수 없는 게시글 정보입니다."),
	DATA_INSERT_FAILED(HttpStatus.BAD_REQUEST,"데이터 저장에 실패하였습니다."),
	SID_DONT_MATCH(HttpStatus.FORBIDDEN,"해당 데이터에 접근할 권한이 없습니다.");
	
	
	private final HttpStatus status;
	private final String message;

}
