package com.bridge4biz.wash.util;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

	@Autowired
	protected JavaMailSender mailSender;

	public Boolean sendEmail(EmailData email) {
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			msg.setSubject(email.getSubject());
			msg.setText(email.getContent());
			msg.setFrom(new InternetAddress(email.getFrom(), email.getFromPersonal()));
			msg.setRecipient(RecipientType.TO, new InternetAddress(email.getReceiver()));
			mailSender.send(msg);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
