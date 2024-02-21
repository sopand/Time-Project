package com.time.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.time.request.schedule.ReqScheduleUpload;
import com.time.response.ResResult;
import com.time.response.schedule.ResCalendarCheckList;
import com.time.response.schedule.ResDaySchedule;
import com.time.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
	
	private final ScheduleService schduleSRV;
	
	@PostMapping(value="/auth/upload",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "텍스트 파일로 일정표 등록")
	@ApiResponse(responseCode = "200",description = "일정 등록 성공")
	@ApiResponse(responseCode = "400",description = "일정 등록 실패")
	public  ResponseEntity<ResResult> scheduleUpload(@ModelAttribute ReqScheduleUpload reqData) throws Exception{
		ResResult result=schduleSRV.scheduleInsert(reqData);
		return ResponseEntity.status(result.getStatusCode()).body(result);
	}
	@Operation(summary = "달력 - 해당 달의 일정 등록된날짜 체크")
	@ApiResponse(responseCode = "200",description = "일정 불러오기 성공")
	@GetMapping("/auth/day/upload/check")
	public ResponseEntity<ResCalendarCheckList> dayUploadCheck(@Parameter(name = "date", description = "검색할 연도-달",example = "2023-01")@RequestParam(name="date") String date){
		ResCalendarCheckList result=schduleSRV.dayUploadCheck(date);
		return ResponseEntity.ok(result);
	}
	@Operation(summary = "해당 날짜의 일정보기")
	@ApiResponse(responseCode = "200",description = "일정 불러오기 성공")
	@GetMapping("/auth/day/list")
	public ResponseEntity<ResDaySchedule> findDaySchedule(@Parameter(name = "date", description = "검색할 연도-달-날짜",example = "2023-06-10")@RequestParam(name="date") String date) throws Exception{
		ResDaySchedule result=schduleSRV.calendarByDay(date);
		return ResponseEntity.ok(result);
	}
	

}
