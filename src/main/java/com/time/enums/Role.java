package com.time.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
	
	
	USER("USER","일반 회원"),
	ADMIN("ADMIN","관리자 아이디");
	
	private String type;
	private String name;
	
	
}
