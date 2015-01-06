package com.bridge4biz.wash.service;

import java.util.ArrayList;

public class MemberOrderInfo {
	public Integer oid = 0;
	public Integer uid = 0;
	public String order_number = "";
	public String pickup_date = "";
	public String dropoff_date = "";
	public String address = "";
	public String addr_number = "";
	public String addr_building = "";
	public String addr_remainder = "";
	public Integer price = 0;
	public Integer dropoff_price = 0;
	public String rdate = "";
	public ArrayList<Coupon> coupon = null;
}
