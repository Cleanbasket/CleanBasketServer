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

	public String getCity() {
		return city;
	}

	public String getDistrict() {
		return district;
	}

	public String getDong() {
		return dong;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}
}
