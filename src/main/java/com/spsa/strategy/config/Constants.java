package com.spsa.strategy.config;

import java.util.Random;

public class Constants {

	public static final String[] EXCLUDED_PATHS = {"/api/public/", "/api/notifications"};
	public static final String ADMIN_PATH = "/api/admin/";
	
	public static final String ACCESS_TOKEN_KEY = "access_token";

	public static final String TOKEN_TYPE_BEARER = "Bearer ";

	public static final String SUCCESS_KEY = "Success";

	public static final String SUBJECT = "SPSA Inspection - ";

	public static final String OTP_SMS_KEY = "OTP_SMS";

	public static final String OTP_SMS_MESSAG_KEY = "Hi this is your Inspection OTP, ";

	public static final String DEFAULT_LANG = "en";

	public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT_ID = "yyyyMMddHHmmss";
	public static final String AUTHORITY_KEY = "AUTH-";
	public static final String DEPARTMENT_KEY = "DEPT-";
	public static final String SECTION_KEY = "SECT-";
	public static final String EVIDENCE_KEY = "EVID";
	public static final String DEFAULT_WIDTH = "120px";
	public static final Integer DEFAULT_DEADLINE_NOTIFICATION_DAYS = 15;
	public static final int DEFAULT_ENDORSEMENT_DEADLINE_NBR_MONTH = 4;
	public static final String ALL = "all";
	
	
	public static String generateOTP(int length) {
		String numbers = "0123456789";
		Random rndm_method = new Random();
		char[] otp = new char[length];
		for (int i = 0; i < length; i++)
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		
		return new String(otp);
	}


	public static String returnOTPSMSMessage() {
		return OTP_SMS_MESSAG_KEY + generateOTP(6);
	}

}
