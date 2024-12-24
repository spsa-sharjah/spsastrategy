package com.spsa.strategy.config;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	public static boolean isapiauthorized(String url, List<String> authorizedapis) {
		for (String authapi : authorizedapis)
			if (url.contains(authapi))
				return true;
		return false;
	}

	public static String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

    public static String generateUniqueString(String name) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = now.format(formatter);
        return name + "_" + formattedDateTime;
    }
    
    public static int concertStringtoInteger(String strnbr) {
    	try {
    		return Integer.parseInt(strnbr);
    	}catch (Exception e) {
		}
    	return 1;
    }
    
    public static String generateId(String key) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_ID);
        String dateTime = now.format(formatter);
        String uuid = UUID.randomUUID().toString();
        String id = key + dateTime + "-" + uuid;
        return id;
    }
    
    public static Date convertStringToDate(String datestr, String format) {

    	if (datestr == null || datestr.equals(""))
    		return null;
    	if (format == null)
    		format = Constants.DATE_FORMAT;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(datestr);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
