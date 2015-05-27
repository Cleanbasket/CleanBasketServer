package com.bridge4biz.wash.service;

public class District {
	public Integer dcid = 0;
	public String city = "";
	public String district = "";	
	public String dong = "";
	
	public District() {
		
	}
	
	public District(String city, String district, String dong) {
		this.city = city;
		this.district = district;
		this.dong = dong;
	}
}
