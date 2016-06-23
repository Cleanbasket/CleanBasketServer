package com.bridge4biz.wash.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bridge4biz.wash.gcm.Message;
import com.bridge4biz.wash.gcm.MulticastResult;
import com.bridge4biz.wash.gcm.Result;
import com.bridge4biz.wash.gcm.Sender;

import fcm.FcmSender;

public class PushMessage {
	private static final Logger log = LoggerFactory.getLogger(PushMessage.class);

	public static void addPush(int uid, int oid, String msg, int value, int type, String regId) {
		FcmSender sender = null;
		try {
			sender = new FcmSender();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message message = new Message.Builder().addData("oid", String.valueOf(oid)).addData("uid", String.valueOf(uid))
				.addData("message", msg).addData("type", String.valueOf(type)).addData("value", String.valueOf(value))
				.build();
		List<String> list = new ArrayList<String>();
		list.add(regId);
		MulticastResult multiResult;
		try {
			multiResult = sender.send(message, list, 3);
			if (multiResult != null) {
				List<Result> resultList = multiResult.getResults();
				for (Result result : resultList) {
					log.debug("Result : " + result.getMessageId());
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*
	public static void addPush(int uid, int oid, String msg, int value, int type, ArrayList<String> regIds) {
		FcmSender sender = new FcmSender();
		Message message = new Message.Builder().addData("oid", String.valueOf(oid)).addData("uid", String.valueOf(uid))
				.addData("message", msg).addData("type", String.valueOf(type)).addData("value", String.valueOf(value))
				.build();
		List<String> list = new ArrayList<String>();
		list.addAll(regIds);
		MulticastResult multiResult;
		try {
			multiResult = sender.send(message, list, 3);
			if (multiResult != null) {
				List<Result> resultList = multiResult.getResults();
				for (Result result : resultList) {
					log.debug("Result : " + result.getMessageId());
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
