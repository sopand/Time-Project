package com.time.util;

import com.time.exception.CustomException;
import com.time.exception.ErrorCode;
import com.time.response.ResResult;

public class CommonUtils {
	
	public static ResResult isSaveSuccessful(Long sid,String message) {
		
		if(sid==null) 
			throw new CustomException(ErrorCode.DATA_INSERT_FAILED);
		
		return ResResult.builder()
				.success(true)
				.message(message)
				.statusCode(200)
				.build();
		
		
	}

}
