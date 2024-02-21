package com.time.dslrepository;

import java.util.List;

public interface ScheduleCustomRepository {
	
	
	List<Integer>  findCalendarSchedule(Long memberSid,String date);

}
