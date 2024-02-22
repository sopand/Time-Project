package com.time.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
		System.out.println("췍췍"+localDate);
		switch (type.getType()) {
			case "END" -> { // 선택날짜의 23:59:59로 변환
				  LocalDateTime endOfDay = localDate.atStartOfDay().plusDays(1).minusSeconds(1);
				return  Timestamp.valueOf(endOfDay);
			
			}
			case "NEXT_DAY_END" -> { // 다음날자의 23:59:59로 변환
				LocalDateTime nextDayEnd = localDate.plusDays(2).atStartOfDay().minusSeconds(1);
				return Timestamp.valueOf(nextDayEnd);
			}
			default -> {  // 선택날짜를 Timestamp로 변환
				return Timestamp.valueOf(localDate.atStartOfDay());
			}
		}

	}

}
