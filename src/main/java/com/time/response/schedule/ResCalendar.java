package com.time.response.schedule;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResCalendar {
	
	@Schema(description = "일정이 등록되어있는 날짜")
	private List<Integer> calendarList; 
	
}
