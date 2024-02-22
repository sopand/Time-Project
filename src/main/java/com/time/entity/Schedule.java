package com.time.entity;


import java.sql.Time;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tb_schedule")
@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_sid")
	private Long scheduleSid;
	
	
	// 해당 일정을 등록한 사용자 시퀀스
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_sid")
	private Member member;

	// 일정의 대분류 1번뎁스
	@Column(name = "first_depth")
	@NotNull
	private String firstDepth;

	// 일정의 소분류 상세 뎁스
	@Column(name = "second_depth")
	private String secondDepth;

	// 해당 일정에 대한 메모
	private String memo;
	
	// 일정의 시작시간
	@Column(name = "start_date")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp startDate;

	// 일정의 종료시간
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp endDate;
	
	
	public void secondDepthIn(String secondDepth) {
		this.secondDepth=secondDepth;
	}
	
	public void memoIn(String memo) {
		this.memo=memo;
	}
	
	
	public void endDateUpdate(Timestamp endDate) {
			this.endDate=endDate;
		
	}
	
	public void nextDateChkUpdate(Schedule schedule) {
		if(schedule!=null) {
			this.endDate=schedule.getStartDate();
		}
	}
}
