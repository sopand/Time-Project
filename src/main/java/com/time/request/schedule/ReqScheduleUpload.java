package com.time.request.schedule;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Text파일 업로드 요청데이터")
@Data
public class ReqScheduleUpload {
	
	
	@Schema(description = "업로드할 텍스트 파일")
	private MultipartFile file;
	

	
}
