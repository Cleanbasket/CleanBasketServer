package com.bridge4biz.wash.data;

public class CouponData {
	public Integer cpid = 0;
	public Integer uid = 0;
	public Integer oid = null;
	public Integer coupon_code = 0;
	public Integer value = 0;
	public Boolean used = false;
	public String start_date = "";
	public String end_date = "";
	public String rdate = "";
	public String serial_number = "";
	public Boolean enabled = true;

	public CouponData() {

	}

	public CouponData(Integer uid, Integer coupon_code, Integer value, String start_date, String end_date) {
		this.uid = uid;
		this.coupon_code = coupon_code;
		this.value = value;
		this.start_date = start_date;
		this.end_date = end_date;
	}

	public CouponData(Integer uid, Integer coupon_code, Integer value, String start_date, String end_date, String serial_number, Boolean enabled) {
		this.uid = uid;
		this.coupon_code = coupon_code;
		this.value = value;
		this.start_date = start_date;
		this.end_date = end_date;
		this.serial_number = serial_number;
		this.enabled = enabled;
	}
}