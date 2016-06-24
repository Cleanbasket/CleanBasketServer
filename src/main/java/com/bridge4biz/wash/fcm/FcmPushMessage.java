package com.bridge4biz.wash.fcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bridge4biz.wash.gcm.Message;
import com.bridge4biz.wash.gcm.Result;

public class FcmPushMessage {
	private static final Logger log = LoggerFactory.getLogger(FcmPushMessage.class);

	public static void addPush(String msg, String regId) {
		FcmSender sender = new FcmSender();

		Message message = new Message.Builder().addData("title", "테스트 입니다.").addData("text", "문자열 입니다.").build();
		List<String> list = new ArrayList<String>();
		list.add(regId);
		Result result;
		try {
			result = sender.send(message, regId, 3);
			log.debug("Result : " + result.getMessageId());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
