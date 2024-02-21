package com.time.dslrepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.entity.QSchedule;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class ScheduleCustomRepositoryImpl implements ScheduleCustomRepository{

	private final JPAQueryFactory queryFactory;
	@Override
	public List<Integer> findCalendarSchedule(Long memberSid,String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		LocalDate localStartDate = YearMonth.parse(date, formatter).atDay(1);
		Timestamp timestampStartDate = Timestamp.valueOf(localStartDate.atStartOfDay());
		QSchedule schedule = QSchedule.schedule;
		return queryFactory
				.select(schedule.startDate.dayOfMonth().as("day")).from(schedule)
				.where(schedule.startDate.goe(timestampStartDate)
						.and(schedule.startDate.lt(Timestamp.valueOf(localStartDate.plusMonths(1).atStartOfDay()))))
				.groupBy(schedule.startDate.dayOfMonth())
				.fetch();
	}
}
