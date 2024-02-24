package com.time.jwt;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
			
		Exception exception = (Exception)request.getAttribute("exception");
		try {
			if (exception == null) {
				setResponse(response, JwtErrorCode.ACCESS_DENIED);
			}else if(exception instanceof ExpiredJwtException){
				setResponse(response, JwtErrorCode.TOKEN_EXPIRED);
			}else if(exception.getCause() instanceof JwtException){
				setResponse(response,exception.getCause().getMessage());
			}else {
				setResponse(response, JwtErrorCode.ACCESS_DENIED);
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
		
		private void setResponse(HttpServletResponse response, String message)
				throws RuntimeException, IOException, JSONException {
			JSONObject responseJson = new JSONObject();
			responseJson.put("message",message);
			responseJson.put("status", 401);
			responseJson.put("code", 401);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.getWriter().print(responseJson);
		}

}
