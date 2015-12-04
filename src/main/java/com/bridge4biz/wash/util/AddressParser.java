package com.bridge4biz.wash.util;

import com.bridge4biz.wash.service.District;

public class AddressParser {
	public static District makeDistrict(String address) {
		address = address.replaceAll(",", "");
		String[] districts = address.split(" ");
		String city = "";
		String district = "";
		String dong = "";
		
		if (districts.length == 0) return new District();
		
		if (checkEnglish(districts[0])) {
			if (districts.length > 2)
				city = districts[2];
			dong = districts[0];
		}
		else {
			city = districts[0];
			if (districts.length > 2)
				dong = districts[2];
		}
		
		if (districts.length > 1)
			district = districts[1];

		return new District(city, district, dong);
	}
	
	private static boolean checkEnglish(String str) {
		for(int i = 0; i < str.length(); i++) {
			if(Character.getType(str.charAt(i)) == 5) {
				return false;
			}
		}
		
		return true;
	}
}
