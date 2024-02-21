package com.time.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.time.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	@Query("SELECT sc FROM Schedule sc JOIN FETCH sc.member m WHERE m.memberSid= :memberSid AND sc.startDate <= :date AND sc.endDate >= :date")
	List<Schedule> selectTimeBetween(@Param("memberSid") Long memberSid, @Param("date") Timestamp date);

	@Query("SELECT sc FROM Schedule sc JOIN FETCH sc.member m WHERE m.memberSid= :memberSid AND sc.startDate <= :date  ORDER BY sc.startDate desc LIMIT 1")
	Schedule selectByBeforeTime(@Param("memberSid") Long memberSid, @Param("date") Timestamp date);
	
	List<Schedule> findByStartDateBetween(Timestamp startDate,Timestamp endDate);
	
}
