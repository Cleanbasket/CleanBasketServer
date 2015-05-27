package com.bridge4biz.wash.sms;

public 	class SendSMS extends Thread {
	private Set set;
	
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
			System.out.println("성공");			
			System.out.println(result.getGroup_id()); // 그룹아이디			
			System.out.println(result.getResult_code()); // 결과코드
			System.out.println(result.getResult_message());  // 결과 메시지
			System.out.println(result.getSuccessCount()); // 성공개수
			System.out.println(result.getErrorCount());  // 여러개 보낼시 오류난 메시지 수
		} else {
			/*
			 * 메시지 보내기 실패
			 */
			System.out.println("실패");
			System.out.println(result.getErrorString()); // 에러 메시지
		}	
	}
}