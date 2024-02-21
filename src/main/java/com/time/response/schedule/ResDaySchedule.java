package com.time.response.schedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class ResDaySchedule {
	
	
	@Schema(description = "해당 일자의 일정 목록")
	List<DTO> scheduleList=new ArrayList<>();
	
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	@Getter
	@Schema(name = "ResSelectBoardInnerDTO")
	public static class DTO {

		@Schema(description = " 일정 시퀀스")
		private Long scheduleSid;

		@Schema(description = "일정 대분류")
		private String firstDepth;


		@Schema(description = "일정 소분류")
		private String secondDepth;
		
		@Schema(description = "일정 메모")
		private String memo;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@Schema(description = "일정 시작 시간")
		private Timestamp startDate;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@Schema(description = "일정 종료 시간")
		private Timestamp endDate;

	}

	
	
}
