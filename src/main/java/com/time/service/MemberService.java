package com.time.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.time.entity.Member;
import com.time.exception.CustomException;
import com.time.exception.ErrorCode;
import com.time.repository.MemberRepository;
import com.time.request.member.ReqSignUp;
import com.time.response.ResResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final MemberRepository memberRepository;
	
	
	public ResResult signUp(ReqSignUp reqData) {
		Member member=Member.builder()
				.email(reqData.getEmail())
				.password(passwordEncoder.encode(reqData.getPassword()))
				.name(reqData.getName())
				.build();
		memberRepository.save(member);
		
		if(member.getMemberSid()==null) {
			throw new CustomException(ErrorCode.DATA_INSERT_FAILED);
		}
		
		return ResResult.builder()
				.success(true)
				.message("회원가입이 완료되었습니다.")
				.statusCode(200)
				.build();
		
	}
	
	

}
