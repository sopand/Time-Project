package com.time.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.time.dslrepository.ScheduleCustomRepositoryImpl;
import com.time.entity.Member;
import com.time.entity.Schedule;
import com.time.exception.CustomException;
import com.time.exception.ErrorCode;
import com.time.repository.MemberRepository;
import com.time.repository.ScheduleRepository;
import com.time.request.schedule.ReqScheduleUpload;
import com.time.response.schedule.ResCalendar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	
	private final ScheduleCustomRepositoryImpl scheduleCustomRepositoryImpl;
	private final MemberRepository memberRepository;

	/* 오늘 하루 스케줄 등록하기 */
	@Transactional
	public void scheduleInsert(ReqScheduleUpload reqData) throws Exception {
		Member member = loginInformation();

		InputStreamReader input = new InputStreamReader(reqData.getFile().getInputStream()); // text 파일을 inputStream변환
		BufferedReader buffer = new BufferedReader(input); // 한줄씩읽어오기 위해 buffer로 변환
		
		int commaIndex = reqData.getFile().getOriginalFilename().indexOf("."); // .txt 지우기위해  콤마의 위치값 가져옴
		String date_str = reqData.getFile().getOriginalFilename().substring(0, commaIndex); // 날짜.txt파일의 날짜값 문자열받아오기
		String bufferTextLine = ""; // buffer의 readLine ( 한줄 데이터 ) 받아놓을 문자열

		StringBuffer sb = new StringBuffer(); // timestamp 날짜형식 맞추기위한 string buffer
		sb.append(date_str);
		sb.insert(4, "-");
		sb.insert(7, "-");
		
		while ((bufferTextLine = buffer.readLine()) != null) { // 한줄이 null이 아닐때까지 반복
			String[] textList = bufferTextLine.split(","); // ,단위로 끊어서 배열에 저장
			Timestamp start_date = Timestamp.valueOf(sb.toString() + " " + textList[0] + ":00"); // timeStamp 변환

			List<Schedule> time_valid = scheduleRepository.selectTimeBetween(member.getMemberSid(), start_date);
		              
			if (!time_valid.isEmpty()) { // 내가 지정한 시간대에 이미 일정이 존재하는지 체크
				throw new CustomException(ErrorCode.SCHEDULE_TIME_DUPLICATION);
			}
			Schedule beforeData = scheduleRepository.selectByBeforeTime(member.getMemberSid(), start_date); // 이전 데이터가
																											// 존재하는지 체크
			if (beforeData != null) {
				beforeData.endDateUpdate(start_date);
			}
			Schedule newSchedule = Schedule.builder().member(member).firstDepth(textList[1]).startDate(start_date)
					.build();
			switch (textList.length) {
				case 3 -> {
					newSchedule.secondDepthIn(textList[2]);
				}
				case 4 -> {
					newSchedule.secondDepthIn(textList[2]);
					newSchedule.memoIn(textList[3]);
				}
			}
			scheduleRepository.save(newSchedule);
		}
		buffer.close();
	}

	public Member loginInformation() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}
	
	
	public ResCalendar calendarChk(String date) {
		Member member =loginInformation();
		List<Integer> calendarList=scheduleCustomRepositoryImpl.findCalendarSchedule(member.getMemberSid(),date);
		return ResCalendar.builder()
				.calendarList(calendarList)
				.build();
	}

}
