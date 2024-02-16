package com.time.request.schedule;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Text파일 업로드 요청데이터")
@Data
public class ReqScheduleUpload {
	
	
	@Schema(description = "업로드할 텍스트 파일")
	private MultipartFile file;
	
	@Schema(description = "일정 시퀀스")
	@Hidden
	private Long schedule_sid;
	
	@Schema(description = "작성자 시퀀스")
	@Hidden
	private Long writer_sid;
	
	@Schema(description = "일정 대분류")
	@Hidden
	private String depth_1;
	
	@Schema(description = "일정 소분류")
	@Hidden
	private String depth_2;
	
	@Schema(description = "메모")
	@Hidden
	private String memo;
	
	@Schema(description = "해당 일정을 실행한 시간")
	@Hidden
	private Timestamp start_date;
	
	@Schema(description = "해당 일정이 종료된 시간")
	@Hidden
	private Timestamp end_date;
}
