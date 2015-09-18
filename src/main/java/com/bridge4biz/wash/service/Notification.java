package com.bridge4biz.wash.service;

public class Notification {
    public static final int EVENT_ALARM = 0;
    public static final int MESSAGE_ALARM = 1;
    public static final int PICKUP_ALARM = 2;
    public static final int DROPOFF_ALARM = 3;
    public static final int COUPON_ALARM = 4;
    public static final int FEEDBACK_ALARM = 5;
    public static final int MODIFY_ALARM = 6;
    public static final int MILEAGE_ALARM = 7;
    public static final int PAYMENT_ALARM = 8;
    public static final int PAYMENT_CANCEL_ALARM = 9;

	public int nid;
	public int uid;
	public int oid;
	public int type;
	public String message;
	public String notification;
	public int value;
	public String date;
	public String rdate;
}

