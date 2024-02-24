package com.time.jwt;

import java.io.IOException;

import org.springdoc.api.ErrorMessage;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		Integer exception = (Integer) request.getAttribute("exception");
		try {
			if (exception == null) {
				setResponse(response, JwtErrorCode.ACCESS_DENIED);
			}else if(exception==JwtErrorCode.TOKEN_EXPIRED.getCode()){
				setResponse(response, JwtErrorCode.TOKEN_EXPIRED);
			}else if(exception==JwtErrorCode.WRONG_TYPE_TOKEN.getCode()) {
				setResponse(response,JwtErrorCode.WRONG_TYPE_TOKEN);
			}else if(exception== JwtErrorCode.UNKNOWN_ERROR.getCode()) {
				setResponse(response,JwtErrorCode.UNKNOWN_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 한글 출력을 위해 getWriter() 사용
	private void setResponse(HttpServletResponse response, JwtErrorCode jwtErrorCode)
			throws RuntimeException, IOException, JSONException {
		JSONObject responseJson = new JSONObject();
		responseJson.put("message", jwtErrorCode.getMessage());
		responseJson.put("status", jwtErrorCode.getStatus());
		responseJson.put("code", jwtErrorCode.getCode());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().print(responseJson);
	}
}
