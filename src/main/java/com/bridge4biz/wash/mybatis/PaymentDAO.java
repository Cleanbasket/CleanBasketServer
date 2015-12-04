package com.bridge4biz.wash.mybatis;

import java.net.UnknownHostException;
import java.util.HashMap;

import kr.co.nicepay.module.lite.NicePayWebConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import com.bridge4biz.wash.data.PaymentData;
import com.bridge4biz.wash.service.Notification;
import com.bridge4biz.wash.service.OrderItemInfo;
import com.bridge4biz.wash.service.PaymentCancelResult;
import com.bridge4biz.wash.service.PaymentResult;
import com.bridge4biz.wash.service.PaymentTriggerResult;
import com.bridge4biz.wash.util.PushMessage;
import com.google.gson.JsonElement;

public class PaymentDAO {
	private static final Logger log = LoggerFactory.getLogger(PaymentDAO.class);		

	private MybatisMapper mapper;
	private PaymentMapper paymentMapper;
		
	public PaymentDAO() {
		
	}

	@Autowired
	private PaymentDAO(MybatisMapper mapper, PaymentMapper paymentMapper, PlatformTransactionManager platformTransactionManager) {
		this.mapper = mapper;
		this.paymentMapper = paymentMapper;
	}

	public NicePayWebConnector initNicePayWebConnector() {
		NicePayWebConnector connector = new NicePayWebConnector();
		connector.setLogHome("/var/lib/tomcat/logs");
		connector.setNicePayHome("/var/lib/tomcat/logs");
		
		return connector;
	}
	
	public PaymentResult getPaymentInfo(Integer uid) {
		PaymentResult payment = paymentMapper.getPaymentInfo(uid);
		
		if (payment == null) {
			payment = new PaymentResult("", "", "", "");
		}
		
		return payment; 
	}
	
	public PaymentResult addPayment(int uid, PaymentData paymentData) { 
		NicePayWebConnector connector = initNicePayWebConnector();
        connector.addRequestData("CardNo", paymentData.getCardNo());
        connector.addRequestData("ExpMonth", paymentData.getExpMonth());
        connector.addRequestData("ExpYear", paymentData.getExpYear());
        connector.addRequestData("IDNo", paymentData.getIDNo());
        connector.addRequestData("CardPw", paymentData.getCardPw());
        connector.addRequestData("SUB_ID", paymentData.getSUB_ID());
        connector.addRequestData("MID", "cleanbsk1m");
        connector.addRequestData("EncodeKey", "StY2m0Iz9APoJYsEo4f4joxytEwTqidenr3yaFsTR9y4Mz8ywSUQvRiAN9cvT9SbptcoZAOosK1PAzNjSI8Mew==");

        try {
			connector.addRequestData("MallIP", java.net.InetAddress.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e) {
			log.debug("Failure in get host address");
		}
        
        connector.addRequestData("actionType", "PY0");
        connector.addRequestData("PayMethod", "BILLKEY");

        try {
            connector.requestAction();
        } catch (Exception e) {
            return null;
        }

        if (connector.getResultData("ResultCode").equals("F100")) {
        	PaymentResult paymentResult = new PaymentResult(
        			connector.getResultData("BID"), 
        			connector.getResultData("AuthDate"), 
        			connector.getResultData("CardName"));
        	
        	// type 0 is Card
        	paymentMapper.addPayment(uid, 0, paymentResult.getBid(), paymentResult.getAuthDate(), paymentResult.getCardName());
        	
        	// 보안 이슈로 빌키는 전하지 않음
        	return new PaymentResult(
        			"", 
        			paymentResult.getAuthDate(), 
        			paymentResult.getCardName());
        } 
        else {
        	log.debug(connector.getResultData("ResultCode") + " / " + connector.getResultData("ResultMsg"));
        	
        	return new PaymentResult(
        			"", "", "",
        			connector.getResultData("ResultMsg"));
        }
	}

	public Boolean removePayment(Integer uid) {
		return paymentMapper.removePayment(uid);
	}
	
	public PaymentTriggerResult triggerPayment(int oid, int uid) {      
		NicePayWebConnector connector = initNicePayWebConnector();
        connector.addRequestData("MID", "cleanbsk1m");
        connector.addRequestData("EncodeKey", "StY2m0Iz9APoJYsEo4f4joxytEwTqidenr3yaFsTR9y4Mz8ywSUQvRiAN9cvT9SbptcoZAOosK1PAzNjSI8Mew==");
        connector.addRequestData("CardInterest", "0");
        connector.addRequestData("CardQuota", "0");

        try {
			connector.addRequestData("MallIP", java.net.InetAddress.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e) {
			log.debug("Failure in get host address");
		}
        
        connector.addRequestData("Moid", String.valueOf(oid));
        connector.addRequestData("actionType", "PY0");
        connector.addRequestData("PayMethod", "BILL");        
        connector.addRequestData("GoodsName", "세탁 서비스");
        connector.addRequestData("BuyerTel", mapper.getPhone(uid));
        connector.addRequestData("BuyerEmail", paymentMapper.getEmailFromOrder(uid));

        // 빌키를 디비에서 가져옴
        connector.addRequestData("BillKey", paymentMapper.getBillKey(uid));
        
        int price = mapper.getOrderPrice(oid, uid);
        connector.addRequestData("Amt", String.valueOf(price));

        try {
            connector.requestAction();
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }

        if (connector.getResultData("ResultCode").equals("3001")) {
        	PaymentTriggerResult ptr = new PaymentTriggerResult(
        			connector.getResultData("ResultCode"), 
        			connector.getResultData("ResultMsg"), 
        			connector.getResultData("TID"), 
        			connector.getResultData("AuthDate"));
        	
        	// type 0 is Card
        	paymentMapper.addPaymentResult(oid, uid, ptr.getResultCode(), ptr.getResultMsg(), ptr.getTID(), ptr.getAuthDate());
        	paymentMapper.setUpdatePaymentStatus(oid, 6);
        	
        	PushMessage.addPush(uid, oid, ptr.getTID(), price, Notification.PAYMENT_ALARM, mapper.getRegid(uid));
        	
        	// 보안 이슈로 빌키는 전하지 않음
        	return new PaymentTriggerResult(
        			connector.getResultData("ResultCode"), 
        			connector.getResultData("ResultMsg"));
        } 
        else {
        	log.debug(connector.getResultData("ResultCode") + " / " + connector.getResultData("ResultMsg"));
        	
        	return new PaymentTriggerResult(
        			connector.getResultData("ResultCode"), 
        			connector.getResultData("ResultMsg"));
        }
	}
	
	public PaymentTriggerResult cancelPayment(int oid, int uid, String price, String partialCancelCode) {     
		NicePayWebConnector connector = initNicePayWebConnector();
        connector.addRequestData("MID", "cleanbsk1m");
        connector.addRequestData("TID", paymentMapper.getTID(oid));
    	connector.addRequestData("PartialCancelCode", partialCancelCode);
        connector.addRequestData("actionType", "CL0");
        connector.addRequestData("CancelName", "운영자");
        connector.addRequestData("CancelMsg", "환불");
        connector.addRequestData("CancelPwd", "cleanb2015");
        
        if (partialCancelCode.equals("0")) {
            int orderPrice = mapper.getOrderPrice(oid, uid);
            connector.addRequestData("CancelAmt", String.valueOf(orderPrice));
        }
        else {
            connector.addRequestData("CancelAmt", price);
        }
        
        try {
			connector.addRequestData("CancelIP", java.net.InetAddress.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e) {
			log.debug("Failure in get host address");
		}
        
        try {
            connector.requestAction();
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        
        if (connector.getResultData("ResultCode").equals("2001")) {
        	PaymentCancelResult pcr = new PaymentCancelResult(
        			connector.getResultData("ResultCode"), 
        			connector.getResultData("ResultMsg"), 
        			connector.getResultData("CancelAmt"), 
        			connector.getResultData("CancelDate"), 
        			connector.getResultData("CancelTime"),
        			connector.getResultData("CancelNum"),
        			connector.getResultData("TID"));
        	
        	// type 0 is Card
        	paymentMapper.addCancelResult(oid, uid, pcr.getResultCode(), pcr.getResultMsg(), pcr.getCancelAmt(), pcr.getCancelDate(), pcr.getCancelTime(), pcr.getCancelNum(), pcr.getTID());
        	paymentMapper.setUpdatePaymentStatus(oid, 3);
        	
        	PushMessage.addPush(uid, oid, pcr.getTID(), Integer.parseInt(price), Notification.PAYMENT_CANCEL_ALARM, mapper.getRegid(uid));
        	
        	// 보안 이슈로 빌키는 전하지 않음
        	return new PaymentTriggerResult(
        			connector.getResultData("ResultCode"), 
        			connector.getResultData("ResultMsg"));
        } 
        else {
        	log.debug(connector.getResultData("ResultCode") + " / " + connector.getResultData("ResultMsg"));
        	
        	return new PaymentTriggerResult(
        			connector.getResultData("ResultCode"), 
        			connector.getResultData("ResultMsg"));
        }
	}

	public String getPaymentResult(int oid) {
		return paymentMapper.getTID(oid);
	}
}
