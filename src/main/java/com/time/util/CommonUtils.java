package com.time.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import com.time.enums.TimeStampType;
import com.time.exception.CustomException;
import com.time.exception.ErrorCode;
import com.time.response.ResResult;

public class CommonUtils {

	public static ResResult isSaveSuccessful(Long sid, String message) {

		if (sid == null)
			throw new CustomException(ErrorCode.DATA_INSERT_FAILED);

		return ResResult.builder().success(true).message(message).statusCode(200).build();
	}

	public static Timestamp stringToTimeStamp(String date, TimeStampType type) throws Exception {
		LocalDate localDate = LocalDate.parse(date);
		switch (type.getType()) {
			case "END" -> { // 선택날짜의 다음날짜로 변환
				LocalDate nextDay = localDate.plusDays(1); // 다음 날짜 계산
				return  Timestamp.valueOf(nextDay.atStartOfDay());
			
			}
			default -> {  // 선택날짜를 Timestamp로 변환
				return Timestamp.valueOf(localDate.atStartOfDay());
			}
		}

	}

}
