package com.bridge4biz.wash.service;

public class AuthUser {
	public int auid = 0;
	public int uid = 0;
	public String code = "";
	public String email = "";
	public String phone = "";
	public int mileage = 0;
	public int total = 0;
	public int user_class = 0;
	public String agent = "";
	
	public AuthUser(){}
	
	public AuthUser(int uid, String email, String phone){
		this.uid = uid;
		this.email = email;
		this.phone = phone;
	}
}
