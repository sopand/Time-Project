package com.time.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeStampType {
	
	START_DAY("START"),
	END_DAY("END"),
	NEXT_DAY_END("NEXT_DAY_END");
	private String type;

}
