package com.bridge4biz.wash.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeCheck {
	public static boolean isTooEarly(String datetime) {
		Calendar now = Calendar.getInstance();
		Calendar pickUpDateTime = Calendar.getInstance();
		
		pickUpDateTime.setTime(getDate(datetime));
		
		long gap = pickUpDateTime.getTimeInMillis() - now.getTimeInMillis();
		long minutes = (gap / 1000) / 60;
 
		if (minutes < 104) 
			return true;
		
		return false;
	}
	
	public static boolean isTooLate(String datetime) {
		Calendar pickUpDateTime = Calendar.getInstance();
		
		pickUpDateTime.setTime(getDate(datetime));

		int hour = pickUpDateTime.get(Calendar.HOUR_OF_DAY);
		int minute = pickUpDateTime.get(Calendar.MINUTE);
	
		if (hour > Constant.OPERATION_END_HOUR && minute > Constant.OPERATION_END_MINUTE)
			return true;
		
		if (hour < Constant.OPERATION_START_HOUR)
			return true;
		
		return false;
	}
	
    public static String getStringDateTime(Date date) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

        return transFormat.format(date);
    }
    
    public static Date getDate(String string) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;

        try {
            date = transFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
