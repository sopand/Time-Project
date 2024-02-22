package com.time.request.schedule;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReqScheduleCreate {
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "일정 시작 시간", example = "2023-07-10 00:00:00")
	@NotNull(message = "시작 시간이 없습니다")
	private String startDate;
	
	@NotNull(message = "종료 시간이 없습니다")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "일정 종료 시간",example = "2023-07-10 03:10:00")
	private String endDate;
	
	
	@Schema(description = "실행한 일 대분류",example = "운동")
	@NotBlank(message = "실행한 일의 대분류를 입력해주셔야합니다.")
	private String firstDepth;
	
	@Schema(description = "실행한 일 소분류",example = "헬스")
	private String secondDepth;
	
	@Schema(description = "메모" ,example = "등운동 ,팔운동 끝")
	private String memo;
	
}
