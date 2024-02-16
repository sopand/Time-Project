package com.time.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import com.time.repository.ScheduleRepository;
import com.time.request.schedule.ReqScheduleUpload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	
	private ScheduleRepository scheduleRepository;
	
	
	/* 오늘 하루 스케쥴 등록하기*/
	public void scheduleInsert(ReqScheduleUpload reqData) throws Exception {
		
		InputStreamReader input = new InputStreamReader(reqData.getFile().getInputStream()); // text 파일을 inputStream변환
		BufferedReader buffer = new BufferedReader(input); // 한줄씩읽어오기 위해 buffer로 변환
		int commaIndex=reqData.getFile().getOriginalFilename().indexOf("."); // .txt 지우기
		String date_str=reqData.getFile().getOriginalFilename().substring(0,commaIndex); // 날짜.txt파일의 날짜값 문자열받아오기
		String text_line=""; // buffer의 readLine ( 한줄 데이터 ) 받아놓을 문자열
		StringBuffer sb = new StringBuffer(); // timestamp 날짜형식 맞추기위한 string buffer
		sb.append(date_str);
		sb.insert(4,"-");
		sb.insert(7, "-");
//		while((text_line=buffer.readLine())!=null) { 	 //한줄이 null이 아닐때까지 반복
//				ReqUpload dbRequest=new ReqUpload();
//				String[] textList=text_line.split(",");  // ,단위로 끊어서 배열에 저장
//				Timestamp start_date= Timestamp.valueOf(sb.toString()+" "+textList[0]+":00"); // timeStamp  변환
//					
//				Map<String, Object> select_param=new HashMap<String, Object>();
//				select_param.put("writer_sid",1L );
//				select_param.put("date",start_date);
//				List<ScheduleVO> time_valid=schMapper.selectByTimeBetween(select_param);
//				if(!time_valid.isEmpty()) { // 내가 지정한 시간대 사이에 일정이 존재하는 지체크
//					throw new CustomException(ErrorCode.TEXT_TIME_DUPLICATION);
//				}
//				ScheduleVO beforeData=schMapper.selectBeforeTime(select_param); // 이전 데이터가 존재하는지 체크
//				if(beforeData!=null) { 
//					select_param.put("end_date",start_date);
//					select_param.put("schedule_sid", beforeData.getSchedule_sid());
//					schMapper.updateEndDate(select_param);  // 이전데이터가 존재하면 이전데이터의 endDate를 지금 startDate로 수정
//				}
//				dbRequest.setStart_date(start_date);
//				dbRequest.setDepth_1(textList[1]);
//				if(textList.length>2) {
//					dbRequest.setDepth_2(textList[2]);
//				}
//				if(textList.length==4) {
//					dbRequest.setMemo(textList[3]);
//				}
//				dbRequest.setWriter_sid(1L);
//				schMapper.scheduleInsert(dbRequest);
//		}
		buffer.close();
	}
	

}
