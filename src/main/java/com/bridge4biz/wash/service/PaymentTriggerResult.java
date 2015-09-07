package com.bridge4biz.wash.service;

public class PaymentTriggerResult {
	public String resultCode = "";
	public String resultMsg = "";
	public String TID = "";
	public String AuthDate = "";
	
	public PaymentTriggerResult(String resultCode, String resultMsg, String TID, String authDate) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.TID = TID;
		this.AuthDate = authDate;
	}
	
	public PaymentTriggerResult(String resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public String getTID() {
		return TID;
	}

	public String getAuthDate() {
		return AuthDate;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public void setTID(String tID) {
		TID = tID;
	}

	public void setAuthDate(String authDate) {
		AuthDate = authDate;
	}
}
