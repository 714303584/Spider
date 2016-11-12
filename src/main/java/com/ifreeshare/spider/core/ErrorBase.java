package com.ifreeshare.spider.core;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to define the error code。
 * At the beginning of the 1 is a request parameter error 
 * @author zhuss
 * @date 2016-10-28 - 3:57:29
 */
public class ErrorBase {
	
	//Input parameter format  error code 
	public static final int DATA_I_TYPE_ERROR = 10001;
	
	//Output parameter format  error code 
	public static final int DATA_O_TYPE_ERROR = 10002;
	
	//Registered user name has been occupied
	public static final int USER_ALREADY_EXISTS = 10004;
	
	//ErrorCode mapping Message
	public static Map<Integer, String> codeAndMessage = new HashMap<Integer, String>();
	
	static{
		codeAndMessage.put(DATA_I_TYPE_ERROR, "This parameter format is not supported ");

		codeAndMessage.put(DATA_O_TYPE_ERROR, "This parameter format is not supported ");
		
		codeAndMessage.put(USER_ALREADY_EXISTS, "Registered user name has been occupied ");
	}
	

	/**
	 * Obtain error information by error code 
	 * @param errorCode
	 * @return ErrorMessage
	 */
	public static String getErrorMesage(int errorCode) {
		return codeAndMessage.get(errorCode);
	}

}
