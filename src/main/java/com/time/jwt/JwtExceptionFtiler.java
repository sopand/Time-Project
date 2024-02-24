package com.time.jwt;

import java.io.IOException;

import org.springdoc.api.ErrorMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFtiler extends OncePerRequestFilter{
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 response.setCharacterEncoding("utf-8");
		 
		 try{
	            filterChain.doFilter(request, response);
	        } catch (ExpiredJwtException e){
	            //만료 에러
	            request.setAttribute("exception", JwtErrorCode.TOKEN_EXPIRED.getCode());
	        } catch (MalformedJwtException e){
	            //변조 에러
	            request.setAttribute("exception", JwtErrorCode.WRONG_TYPE_TOKEN.getCode());
	        } catch (SignatureException e){
	            //형식, 길이 에러
	            request.setAttribute("exception", JwtErrorCode.WRONG_TYPE_TOKEN.getCode());
	        } catch (JwtException e ) {
	        	request.setAttribute("exception", JwtErrorCode.UNKNOWN_ERROR.getCode());
	        }
	}
}
