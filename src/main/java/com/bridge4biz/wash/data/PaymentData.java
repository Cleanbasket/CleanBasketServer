package com.bridge4biz.wash.data;

public class PaymentData {
	public String CardNo = "";
	public String ExpMonth = "";
	public String ExpYear = "";
	public String IDNo = "";
	public String CardPw = "";
	public String SUB_ID = "";
	
	public PaymentData() {

	}

	public PaymentData(String cardNo, String expMonth, String expYear, String iDNo, String cardPw, String sUB_ID) {
		super();
		CardNo = cardNo;
		ExpMonth = expMonth;
		ExpYear = expYear;
		IDNo = iDNo;
		CardPw = cardPw;
		SUB_ID = sUB_ID;
	}

	public String getCardNo() {
		return CardNo;
	}

	public String getExpMonth() {
		return ExpMonth;
	}

	public String getExpYear() {
		return ExpYear;
	}

	public String getIDNo() {
		return IDNo;
	}

	public String getCardPw() {
		return CardPw;
	}

	public String getSUB_ID() {
		return SUB_ID;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public void setExpMonth(String expMonth) {
		ExpMonth = expMonth;
	}

	public void setExpYear(String expYear) {
		ExpYear = expYear;
	}

	public void setIDNo(String iDNo) {
		IDNo = iDNo;
	}

	public void setCardPw(String cardPw) {
		CardPw = cardPw;
	}

	public void setSUB_ID(String sUB_ID) {
		SUB_ID = sUB_ID;
	}
}
