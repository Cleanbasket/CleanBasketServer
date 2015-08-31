package com.bridge4biz.wash.service;

public class PaymentResult {
	public String bid = "";
	public String authDate = "";
	public String cardName = "";
	public String resultMsg = "";
			
	public PaymentResult() {
		
	}

	public PaymentResult(String bid, String authDate, String cardName) {
		this.bid = bid;
		this.authDate = authDate;
		this.cardName = cardName;
	}

	public PaymentResult(String bid, String authDate, String cardName, String resultMsg) {
		this.bid = bid;
		this.authDate = authDate;
		this.cardName = cardName;
		this.resultMsg = resultMsg;
	}

	public String getBid() {
		return bid;
	}

	public String getAuthDate() {
		return authDate;
	}

	public String getCardName() {
		return cardName;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
}
