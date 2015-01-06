package com.bridge4biz.wash.util;

public class EmailData {
	private String subject;
	private String content;
	private String regdate;
	private String receiver;
	private String from;
	private String fromPersonal;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromPersonal() {
		return fromPersonal;
	}

	public void setFromPersonal(String fromPersonal) {
		this.fromPersonal = fromPersonal;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
}
