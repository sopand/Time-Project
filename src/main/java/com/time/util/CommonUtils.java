package com.time.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

import com.time.enums.TimeStampType;
import com.time.exception.CustomException;
import com.time.exception.ErrorCode;
import com.time.jwt.JwtErrorCode;
import com.time.response.ResResult;

import jakarta.servlet.http.HttpServletResponse;

public final class CommonUtils {

	public static ResResult isSaveSuccessful(Long sid, String message) {

		if (sid == null)
			throw new CustomException(ErrorCode.DATA_INSERT_FAILED);

		return ResResult.builder().success(true).message(message).statusCode(200).build();
	}

	public static ResResult isPostSuccessful(Long sid, String message) {

		if (sid == null)
			throw new CustomException(ErrorCode.DATA_MODIFY_FAILED);

		return ResResult.builder().success(true).message(message).statusCode(200).build();
	}

	public static Timestamp stringToTimeStamp(String date, TimeStampType type) throws Exception {
		LocalDate localDate = LocalDate.parse(date);
		
		switch (type.getType()) {
			case "END" -> { // 선택날짜의 23:59:59로 변환
				LocalDateTime endOfDay = localDate.atStartOfDay().plusDays(1).minusSeconds(1);
				return Timestamp.valueOf(endOfDay);
	
			}
			case "NEXT_DAY_END" -> { // 다음날자의 23:59:59로 변환
				LocalDateTime nextDayEnd = localDate.plusDays(2).atStartOfDay().minusSeconds(1);
				return Timestamp.valueOf(nextDayEnd);
			}
			default -> { // 선택날짜를 Timestamp로 변환
				return Timestamp.valueOf(localDate.atStartOfDay());
			}
		}

	}

	public static Timestamp stringToTimeStampFormat(String date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

		// LocalDateTime 객체를 Timestamp 객체로 변환
		return Timestamp.valueOf(dateTime);
	}


	public static void responseAuthenticationError(HttpServletResponse response, JwtErrorCode jwtErrorCode)
			throws Exception {
		JSONObject responseJson = new JSONObject();
		responseJson.put("message", jwtErrorCode.getMessage());
		responseJson.put("status", jwtErrorCode.getStatus());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().print(responseJson);
	}
	public static void responseAuthenticationError(HttpServletResponse response, String message,int status)
			throws Exception {
		JSONObject responseJson = new JSONObject();
		responseJson.put("message", message);
		responseJson.put("status", status);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().print(responseJson);
	}
}
