package com.bridge4biz.wash.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public 	class SendSMS extends Thread {
	private Set set;
	private Logger log = LoggerFactory.getLogger(SendSMS.class);		
	
	public SendSMS(Set set) {
		this.set = set;
	}
	
	@Override
	public void run() {
		super.run();
		
		Coolsms coolsms = new Coolsms();
		SendResult result = coolsms.send(this.set); // 보내기&전송결과받기

		if (result.getErrorString() == null) {
			/*
			 *  메시지 보내기 성공 및 전송결과 출력
			 */
			log.info("성공");			
			log.info(result.getGroup_id()); // 그룹아이디			
			log.info(result.getResult_code()); // 결과코드
			log.info(result.getResult_message());  // 결과 메시지
			log.info(result.getSuccessCount()); // 성공개수
			log.info(result.getErrorCount());  // 여러개 보낼시 오류난 메시지 수
		} else {
			/*
			 * 메시지 보내기 실패
			 */
			log.info("실패");
			log.info(result.getErrorString()); // 에러 메시지
		}	
	}
}