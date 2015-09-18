package com.bridge4biz.wash.service;

public class PaymentCancelResult {
	public String resultCode = "";
	public String resultMsg = "";
	public String cancelAmt = "";
	public String cancelDate = "";
	public String cancelTime = "";
	public String cancelNum = "";
	public String TID = "";
	
	public PaymentCancelResult(String resultCode, String resultMsg, String cancelAmt, String cancelDate, String cancelTime, String cancelNum, String TID) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.cancelAmt = cancelAmt;
		this.cancelDate = cancelDate;
		this.cancelTime = cancelTime;
		this.cancelNum = cancelNum;
		this.TID = TID;
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public String getCancelAmt() {
		return cancelAmt;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public String getCancelNum() {
		return cancelNum;
	}

	public String getTID() {
		return TID;
	}

	public void setTID(String tID) {
		TID = tID;
	}
}
