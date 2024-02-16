package com.time.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.time.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
	
	
}
