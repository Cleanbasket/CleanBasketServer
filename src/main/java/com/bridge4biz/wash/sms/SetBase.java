package com.bridge4biz.wash.sms;


public class SetBase {
	private String api_key = "NCS54CAE83317A7F";
	private String api_secret = "C224DA7674B9848F475E9683B87A7632"; 
	private String base_url="https://api.coolsms.co.kr/sms/2/";
	
	public String getApiKey() {
		return api_key;
	}		
	public String getApiSecret() {
		return api_secret;
	}
	public String getBaseUrl() {
		return base_url;
	}
}
