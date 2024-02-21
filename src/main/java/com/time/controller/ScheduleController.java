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
import com.time.response.schedule.ResCalendar;
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
		schduleSRV.scheduleInsert(reqData);
		return null;
	}
	
	@GetMapping("/auth/calendar")
	public ResponseEntity<ResCalendar> calendarChk(@Parameter(name = "date", description = "검색할 연도-달",example = "2023-01")@RequestParam(name="date") String date){
		ResCalendar result=schduleSRV.calendarChk(date);
		return ResponseEntity.ok(result);
	}

}
