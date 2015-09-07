package com.bridge4biz.wash.data;

import java.util.ArrayList;

import com.bridge4biz.wash.service.Item;

public class OrderData {
	public Integer oid = 0;
	public Integer uid = 0;
	public Integer pickup_man = 0;
	public Integer dropoff_man = 0;
	public Integer adrid = 0;
	public String order_number = "";
	public Integer state = 0;
	public String phone = "";
	public String address = "";
	public String addr_number = "";
	public String addr_building = "";
	public String addr_remainder = "";
	public String note = "";
	public String memo = "";
	public Integer price = 0;
	public Integer payment_method = 0;
	public Integer dropoff_price = 0;
	public String pickup_date = "";
	public String dropoff_date = "";
	public String rdate = "";
	public ArrayList<Item> item = null;
	public ArrayList<Integer> cpid = null;
}