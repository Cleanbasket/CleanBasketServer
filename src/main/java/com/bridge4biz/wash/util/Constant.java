package com.bridge4biz.wash.util;

public class Constant {
	public int constant = 0;
	public String message = "";
	public String data = "";
	
	public static final int SESSION_EXPIRED = 0;
	public static final int SUCCESS = 1;
	public static final int ERROR = 2;
	public static final int EMAIL_ERROR = 3;
	public static final int PASSWORD_ERROR = 4;
	public static final int ACCOUNT_VALID = 5;
	public static final int ACCOUNT_INVALID = 6;
	public static final int ACCOUNT_ENABLED = 8;
	public static final int ACCOUNT_DISABLED = 9;
	public static final int ROLE_ADMIN = 10;
	public static final int ROLE_DELIVERER = 11;
	public static final int ROLE_MEMBER = 12;
	public static final int ROLE_INVALID = 13;
	public static final int IMAGE_WRITE_ERROR = 14;
	public static final int IMPOSSIBLE = 15;
	public static final int ACCOUNT_DUPLICATION = 16;
	public static final int SESSION_VALID = 17;
	public static final int ADDRESS_UNAVAILABLE = 18;
	public static final int DATE_UNAVAILABLE = 19;
	public static final int AUTH_CODE_INVALID = 20;
	public static final int AUTH_CODE_TIME = 21;
	public static final int DUPLICATION_FEEDBACK = 22;
	public static final int TOO_EARLY_TIME = 23;
    public static final int TOO_LATE_TIME = 24;

	public static final int PUSH_ASSIGN_PICKUP = 100;
	public static final int PUSH_ASSIGN_DROPOFF = 101;
	public static final int PUSH_SOON_PICKUP = 102;
	public static final int PUSH_SOON_DROPOFF = 103;

	public static final int PUSH_ORDER_ADD = 200;
	public static final int PUSH_ORDER_CANCEL = 201;
	public static final int PUSH_PICKUP_COMPLETE = 202;
	public static final int PUSH_DROPOFF_COMPLETE = 203;
	public static final int PUSH_MEMBER_JOIN = 204;
	public static final int PUSH_DELIVERER_JOIN = 205;
	public static final int PUSH_CHANGE_ACCOUNT_ENABLED = 206;

	public static final int DUPLICATION = 207;
	public static final int INVALID = 208;

	public static final String PATH = "/var/lib/tomcat7/webapps/";

	
	public static final int OPERATION_START_HOUR = 10;
	public static final int OPERATION_END_HOUR = 23;
	public static final int OPERATION_END_MINUTE = 30;

	public Constant() {

	}

	public Constant(int constant, String message, String data) {
		this.constant = constant;
		this.message = message;
		this.data = data;
	}

	public Constant(int constant, String message) {
		this.constant = constant;
		this.message = message;
	}

	public Constant setConstant(int constant, String message, String data) {
		this.constant = constant;
		this.message = message;
		this.data = data;
		return this;
	}

	public Constant setConstant(int constant, String message) {
		this.constant = constant;
		this.message = message;
		return this;
	}
}
