package com.time.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.time.entity.Member;
import com.time.enums.FlagYN;
import com.time.enums.Role;
import com.time.exception.CustomException;
import com.time.exception.ErrorCode;
import com.time.repository.MemberRepository;
import com.time.request.member.ReqSignUp;
import com.time.response.ResResult;
import com.time.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final PasswordEncoder passwordEncoder;

	private final MemberRepository memberRepository;

	public ResResult signUp(ReqSignUp reqData) {

		// 이미 존재하는 닉네임일 경우 에러발생
		if (memberRepository.existsByEmail(reqData.getEmail()))
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);

		Member saveMember = Member.builder().email(reqData.getEmail())
				.password(passwordEncoder.encode(reqData.getPassword())).name(reqData.getName()).role(Role.USER)
				.delYn(FlagYN.N).build();
		memberRepository.save(saveMember);

		// SAVE 성공여부 체크를 위한 static 메서드
		return CommonUtils.isSaveSuccessful(saveMember.getMemberSid(), "회원가입이 완료 되었습니다");

	}

}
