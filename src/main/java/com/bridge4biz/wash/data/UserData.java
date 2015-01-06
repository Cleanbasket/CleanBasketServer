package com.bridge4biz.wash.data;

import javax.servlet.http.HttpServletRequest;

public class UserData {
	public Integer uid = 0;
	public String email = "";
	public String password = "";
	public String name = "";
	public String phone = "";
	public String address = "";
	public String addr_number = "";
	public String addr_building = "";
	public String addr_remainder = "";
	public String img = "";
	public String birthday = "";
	public Boolean enabled = false;
	public String authority = "";
	public String rdate = "";

	public void setDeliverer(HttpServletRequest request) {
		this.email = request.getParameter("email");
		this.password = request.getParameter("password");
		this.name = request.getParameter("name");
		this.phone = request.getParameter("phone");
		this.birthday = request.getParameter("birthday");
	}

	public UserData() {

	}

	public UserData(Integer uid, String phone) {
		this.uid = uid;
		this.phone = phone;
	}
}
