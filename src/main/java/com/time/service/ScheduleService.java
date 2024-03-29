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
import com.time.enums.TimeStampType;
import com.time.exception.CustomException;
import com.time.exception.ErrorCode;
import com.time.repository.MemberRepository;
import com.time.repository.ScheduleRepository;
import com.time.request.schedule.ReqScheduleCreate;
import com.time.request.schedule.ReqScheduleUpload;
import com.time.response.ResResult;
import com.time.response.schedule.ResCalendarCheckList;
import com.time.response.schedule.ResDaySchedule;
import com.time.util.CommonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;

	private final ScheduleCustomRepositoryImpl scheduleCustomRepositoryImpl;
	private final MemberRepository memberRepository;

	public Member loginInformation() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	/* 오늘 하루 스케줄 등록하기 */
	@Transactional
	public ResResult scheduleInsert(ReqScheduleUpload reqData) throws Exception {
		Member member = loginInformation();

		for (int i = 0; i < reqData.getFile().size(); i++) {
			try (BufferedReader buffer = new BufferedReader(
					new InputStreamReader(reqData.getFile().get(i).getInputStream()))) {
				String bufferTextLine; // buffer의 readLine ( 한줄 데이터 ) 받아놓을 문자열

				String dayStr = getDateByFileName(reqData.getFile().get(i).getOriginalFilename());

				while ((bufferTextLine = buffer.readLine()) != null) { // 한줄이 null이 아닐때까지 반복

					String[] textList = bufferTextLine.split(","); // 메모장의 내용이 , 단위로 나뉘어져 있으므로 , 단위로 모든 내용을 배열에 분할
					Timestamp startDate = Timestamp.valueOf(dayStr + " " + textList[0] + ":00"); // timeStamp 변환

					scheduleValidation(member.getMemberSid(), startDate);
					beforeScheduleChk(member.getMemberSid(), startDate);

					Schedule newSchedule = Schedule.builder().member(member).firstDepth(textList[1])
							.startDate(startDate).build();
					
					switch (textList.length) {
						case 3 -> {
							newSchedule.secondDepthIn(textList[2]);
						}
						case 4 -> {
							newSchedule.secondDepthIn(textList[2]);
							newSchedule.memoIn(textList[3]);
						}
					}
					
					if (!buffer.ready()) { // 해당일의 마지막 일정일 경우 작동 ( 바로 뒷날에 데이터가 있다면 해당날짜의 첫번째 데이터의 startDate가 마지막일정의 endDate로 등록
						Timestamp nextDayLastTime = CommonUtils.stringToTimeStamp(dayStr, TimeStampType.NEXT_DAY_END);
						Timestamp currentLastTime = CommonUtils.stringToTimeStamp(dayStr, TimeStampType.END_DAY);
						Schedule nextDayStartDate = scheduleRepository.findByCurrentAndNextEndSchedule(currentLastTime,
								nextDayLastTime, member.getMemberSid());
						newSchedule.nextDateChkUpdate(nextDayStartDate);
					}
					scheduleRepository.save(newSchedule);
				}
			}
		}
		return CommonUtils.isSaveSuccessful(member.getMemberSid(),
				"총 " + reqData.getFile().size() + "개의 파일 업로드가 완료되었습니다.");
	}

	private void scheduleValidation(Long memberSid, Timestamp startDate) {
		Schedule time_valid = scheduleRepository.selectTimeBetween(memberSid, startDate);

		if (time_valid != null) { // 내가 지정한 시간대에 이미 일정이 존재하는지 체크
			throw new CustomException(ErrorCode.SCHEDULE_TIME_DUPLICATION);
		}

	}

	private void beforeScheduleChk(Long memberSid, Timestamp startDate) {
		Schedule beforeData = scheduleRepository.selectByBeforeTime(memberSid, startDate); // 해당 시간 이전데이터가 존재한다면
																							// endDate를 추가해줘야함
		if (beforeData != null) {
			beforeData.endDateUpdate(startDate); // 해당시간 이전의 일정에 endDate를 추가
		}
	}

	private String getDateByFileName(String fileName) { // 메모장 파일의 이름에서 날짜를 가져오는 메소드
		int commaIndex = fileName.indexOf(".");
		String textFileDate = fileName.substring(0, commaIndex);
		StringBuffer dateSetBuffer = new StringBuffer(); // timestamp 날짜형식 맞추기위한 string buffer
		dateSetBuffer.append(textFileDate); // 메모장이름 20230714를 .txt파일명을 제외하고 가져옴
		dateSetBuffer.insert(4, "-");
		dateSetBuffer.insert(7, "-");
		return dateSetBuffer.toString();
	}

	/**
	 * 해당 달의 각 요일별 등록된 일정여부 체크
	 * 
	 * @param date
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResCalendarCheckList dayUploadCheck(String date) {
		Member member = loginInformation();
		List<Integer> calendarList = scheduleCustomRepositoryImpl.findCalendarSchedule(member.getMemberSid(), date);
		return ResCalendarCheckList.builder().calendarList(calendarList).build();
	}

	/**
	 * 선택한 날의 등록한 일정목록보기
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public ResDaySchedule findByDaySchedule(String date) throws Exception {
		Member member = loginInformation();
		Timestamp start = CommonUtils.stringToTimeStamp(date, TimeStampType.CURRENT);
		Timestamp end = CommonUtils.stringToTimeStamp(date, TimeStampType.END_DAY);
		List<Schedule> schedules = scheduleRepository.findByStartDateBetweenAndMemberMemberSid(start, end,
				member.getMemberSid());
		ResDaySchedule result = new ResDaySchedule();
		for (Schedule data : schedules) {
			result.getScheduleList()
					.add(ResDaySchedule.DTO.builder().scheduleSid(data.getScheduleSid())
							.firstDepth(data.getFirstDepth()).secondDepth(data.getSecondDepth()).memo(data.getMemo())
							.startDate(data.getStartDate()).endDate(data.getEndDate()).build());
		}

		return result;
	}

	@Transactional
	public ResResult deleteSchedule(Long scheduleSid) {
		Schedule schedule = scheduleRepository.findById(scheduleSid)
				.orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
		scheduleRepository.delete(schedule);

		return CommonUtils.isPostSuccessful(scheduleSid, "일정의 삭제가 완료되었습니다.");

	}

	@Transactional
	public ResResult createSchedule(ReqScheduleCreate reqData) {
		Timestamp startDate = CommonUtils.stringToTimeStampFormat(reqData.getStartDate());
		Timestamp endDate = CommonUtils.stringToTimeStampFormat(reqData.getEndDate());
		Member member = loginInformation();
		Schedule newSchedule = Schedule.builder().firstDepth(reqData.getFirstDepth())
				.secondDepth(reqData.getSecondDepth()).member(member).memo(reqData.getMemo()).startDate(startDate)
				.endDate(endDate).build();
		scheduleRepository.save(newSchedule);

		scheduleValidation(member.getMemberSid(), startDate);
		beforeScheduleChk(member.getMemberSid(), startDate);
		return CommonUtils.isSaveSuccessful(newSchedule.getScheduleSid(), "일정 등록이 완료되었습니다.");

	}

}
