package com.time.response.schedule;

import java.sql.Timestamp;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class ResDaySchedule {
	
	
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	@Getter
	@Schema(name = "ResSelectBoardInnerDTO")
	public static class DTO {

		@Schema(description = "게시글 시퀀스아이디")
		private Long boardSid;

		@Schema(description = "게시글 제목")
		private String title;


		@Schema(description = "작성자 명")
		private String memberName;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@Schema(description = "일정 시작 시간")
		private Timestamp startDate;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@Schema(description = "일정 종료 시간")
		private Timestamp endDate;

	}

	
	
}
