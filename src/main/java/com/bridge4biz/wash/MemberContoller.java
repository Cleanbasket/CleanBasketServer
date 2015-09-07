package com.bridge4biz.wash;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.data.OrderData;
import com.bridge4biz.wash.data.PassData;
import com.bridge4biz.wash.data.PaymentData;
import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.mybatis.PaymentDAO;
import com.bridge4biz.wash.service.Address;
import com.bridge4biz.wash.service.AuthUser;
import com.bridge4biz.wash.service.Feedback;
import com.bridge4biz.wash.service.Member;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.service.PaymentResult;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/member")
public class MemberContoller {

	@Autowired
	private MybatisDAO dao;
	
	@Autowired
	private PaymentDAO paymentDao;


	@RequestMapping(method=RequestMethod.POST, value = "/register")
	@ResponseBody
	public Constant memberNewJoin(@RequestBody UserData userData, Constant constant) {
		userData.authority = "ROLE_MEMBER";
		userData.enabled = true;
		Integer value = dao.registerUserForMember(userData);
		switch (value) {
		case Constant.SUCCESS:
			constant.setConstant(Constant.SUCCESS, "일반회원 가입 성공 : SUCCESS");
			break;
		case Constant.ERROR:
			constant.setConstant(Constant.ERROR, "일반회원 가입 실패 : ERROR");
			break;
		case Constant.ACCOUNT_DUPLICATION:
			constant.setConstant(Constant.ACCOUNT_DUPLICATION, "일반회원 이메일 중복 : ACCOUNT_DUPLICATION");
			break;
		}
		return constant;
	}
	
	@RequestMapping(value = "/join")
	@ResponseBody
	public Constant memberJoin(@RequestBody UserData userData, Constant constant) {
		userData.authority = "ROLE_MEMBER";
		userData.enabled = true;
		Integer value = dao.addUserForMember(userData);
		switch (value) {
		case Constant.SUCCESS:
			constant.setConstant(Constant.SUCCESS, "일반회원 가입 성공 : SUCCESS");
			break;
		case Constant.ERROR:
			constant.setConstant(Constant.ERROR, "일반회원 가입 실패 : ERROR");
			break;
		case Constant.ACCOUNT_DUPLICATION:
			constant.setConstant(Constant.ACCOUNT_DUPLICATION, "일반회원 이메일 중복 : ACCOUNT_DUPLICATION");
			break;
		}
		return constant;
	}
	

	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.POST, value = "/user")
	@ResponseBody
	public Constant registerAuthUser(Constant constant, Gson gson, Authentication auth, @RequestBody AuthUser authUser) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.addAuthUser(authUser, uid);
		switch (value) {
		case Constant.SUCCESS:
			constant.setConstant(Constant.SUCCESS, "인증회원 가입 성공 : SUCCESS");
			break;
		case Constant.ERROR:
			constant.setConstant(Constant.ERROR, "인증회원 가입 실패 : ERROR");
			break;
		case Constant.AUTH_CODE_TIME:
			constant.setConstant(Constant.AUTH_CODE_TIME, "인증회원 가입 실패 : ERROR");
			break;
		case Constant.AUTH_CODE_INVALID:
			constant.setConstant(Constant.AUTH_CODE_INVALID, "인증회원 가입 실패 : ERROR");
			break;
		case Constant.ACCOUNT_DUPLICATION:
			constant.setConstant(Constant.ACCOUNT_DUPLICATION, "인증회원 가입 실패 : ERROR");
			break;
		case Constant.DUPLICATION:
			constant.setConstant(Constant.DUPLICATION, "인증회원 가입 실패 : ERROR");
			break;
		}
		
		return constant;
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/order")
	@ResponseBody
	public Constant memberOrder(Constant constant, Gson gson, Authentication auth) {
		return constant.setConstant(Constant.SUCCESS, "일반회원 주문정보 가져오기 성공 : SUCCESS", gson.toJson(dao.getOrder(dao.getUid(auth.getName()))));
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/add/new")
	@ResponseBody
	public Constant addMemberOrder(Constant constant, @RequestBody Order order, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.addNewOrder(order, uid);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS", String.valueOf(dao.getLatestOrderIdByUid(uid)));
		} 
		else if (value == Constant.ADDRESS_UNAVAILABLE) {
			return constant.setConstant(Constant.ADDRESS_UNAVAILABLE, "서비스 지역이 아님 : ERROR");		
		}
		else if (value == Constant.DATE_UNAVAILABLE) {
			return constant.setConstant(Constant.DATE_UNAVAILABLE, "해당 수거배달일 서비스 안 함 : ERROR");
		}
		else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/modify")
	@ResponseBody
	public Constant modifyMemberOrderItem(Constant constant, @RequestBody Order order, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.modifyOrderItem(order, uid);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS");
		} 
		else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/date")
	@ResponseBody
	public Constant modifyMemberOrderDate(Constant constant, @RequestBody Order order, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.modifyOrderDate(order, uid);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS");
		} 
		else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}

	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/rate")
	@ResponseBody
	public Constant addRate(Constant constant, @RequestBody Feedback feedback, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.addRate(feedback, uid);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "피드백 성공 : SUCCESS");	
		}
		else {
			return constant.setConstant(Constant.ERROR, "피드백 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.GET, value = "/order/rate")
	@ResponseBody
	public Constant confirmRate(Constant constant, @RequestParam Integer oid) {
		Integer value = dao.confirmRate(oid);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "피드백 성공 : SUCCESS");	
		} else if (value == Constant.DUPLICATION_FEEDBACK) {
			return constant.setConstant(Constant.DUPLICATION_FEEDBACK, "피드백 중복 : DUPLICATION");
		}
		else {
			return constant.setConstant(Constant.ERROR, "피드백 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.GET, value = "/id")
	@ResponseBody
	public Constant getMember(Constant constant, Authentication auth) {
		return constant.setConstant(Constant.SUCCESS, "UID : SUCCESS", dao.getUid(auth.getName()) + "");	
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.GET, value = "/order/confirm")
	@ResponseBody
	public Constant confirmOrder(Constant constant, @RequestParam Integer oid) {
		Integer value = dao.confirmOrder(oid);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "확정 성공 : SUCCESS");	
		}
		else {
			return constant.setConstant(Constant.ERROR, "확정 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/order/add")
	@ResponseBody
	public Constant addMemberOrder(Constant constant, @RequestBody OrderData orderData, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.addOrder(orderData, uid);
		if (value == Constant.SUCCESS) {
//			SocketIO.broadCast(new PushMessage(Constant.PUSH_ORDER_ADD, uid, orderData.oid));
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS", orderData.order_number);
		} 
		else if (value == Constant.ADDRESS_UNAVAILABLE) {
			return constant.setConstant(Constant.ADDRESS_UNAVAILABLE, "서비스 지역이 아님 : ERROR");		
		}
		else if (value == Constant.DATE_UNAVAILABLE) {
			return constant.setConstant(Constant.DATE_UNAVAILABLE, "해당 수거배달일 서비스 안 함 : ERROR");
		}
		else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/order/del")
	@ResponseBody
	public Constant delMemberOrder(Constant constant, @RequestBody OrderData orderData, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.delOrder(orderData, uid);
		if (value == Constant.SUCCESS) {
//			SocketIO.broadCast(new PushMessage(Constant.PUSH_ORDER_CANCEL, uid, orderData.oid));
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문취소 성공 : SUCCESS");
		} else if (value == Constant.IMPOSSIBLE) {
			return constant.setConstant(Constant.IMPOSSIBLE, "주문취소가 불가능한 상태가 입니다. : IMPOSSIBLE");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문취소 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/order/del/new")
	@ResponseBody
	public Constant delMemberNewOrder(Constant constant, @RequestBody Order order, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.delNewOrder(order, uid);
		if (value == Constant.SUCCESS) {
//			SocketIO.broadCast(new PushMessage(Constant.PUSH_ORDER_CANCEL, uid, orderData.oid));
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문취소 성공 : SUCCESS");
		} else if (value == Constant.IMPOSSIBLE) {
			return constant.setConstant(Constant.IMPOSSIBLE, "주문취소가 불가능한 상태가 입니다. : IMPOSSIBLE");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문취소 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/coupon")
	@ResponseBody
	public Constant memberCoupon(Constant constant, Gson gson, Authentication auth) {
		return constant.setConstant(Constant.SUCCESS, "일반회원 쿠폰 가져오기 성공 : SUCCESS", gson.toJson(dao.getAvailableCoupon(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/coupon/issue")
	@ResponseBody
	public Constant memberCouponIssue(Constant constant, Gson gson, Authentication auth, @RequestBody Map<String, String> data) {
		Integer uid = dao.getUid(auth.getName());
		String serial_number = data.get("serial_number");
		Integer success = dao.recommendationCouponIssue(uid, serial_number);
		if (success == 1) {
			return constant.setConstant(Constant.SUCCESS, "추천하기 쿠폰 발급 성공 : SUCCESS");
		} else if (success == 0) {
			return constant.setConstant(Constant.ERROR, "추천하기 쿠폰 발급 오류 : ERROR");
		} else if (success == 2) {
			return constant.setConstant(Constant.DUPLICATION, "이미 발급되어있습니다. : DUPLICATION");
		} else {
			return constant.setConstant(Constant.INVALID, "유효하지 않은 쿠폰코드입니다. : INVALID");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/address")
	@ResponseBody
	public Constant memberAddress(Constant constant, Gson gson, Authentication auth) {
		return constant.setConstant(Constant.SUCCESS, "일반회원 주소 가져오기 성공 : SUCCESS", gson.toJson(dao.getAddress(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "", consumes = { "application/json" })
	@ResponseBody
	public Constant member(Constant constant, Authentication auth, Gson gson) {
		Member member = dao.getMember(auth.getName());
		if (member != null) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 정보 가져오기 성공 : SUCCESS", gson.toJson(member));
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 정보 가져오기 실패 : ERROR");
		}
	}


	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.GET, value = "/user")
	@ResponseBody
	public Constant getMember(Constant constant, Authentication auth, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "인증회원 정보 가져오기 성공 : SUCCESS", gson.toJson(dao.isAuthUser(dao.getUid(auth.getName()))));
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/address/update")
	@ResponseBody
	public Constant updateMemberAddress(Constant constant, Gson gson, Authentication auth, @RequestBody Address address) {
		Boolean success = dao.updateMemberAddress(address, dao.getUid(auth.getName()));
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 주소 변경 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 주소 변경 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/phone/update")
	@ResponseBody
	public Constant updateMemberPhone(Constant constant, Authentication auth, @RequestBody UserData userData) {
		userData.email = auth.getName();
		Boolean success = dao.updateMemberPhone(userData);
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 전화번호 변경 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 전화번호 변경 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/password/update")
	@ResponseBody
	public Constant updateMemberPassword(Constant constant, Authentication auth, @RequestBody UserData userData) {
		userData.email = auth.getName();
		Boolean success = dao.updateMemberPassword(userData);
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 비밀번호 변경 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 비밀번호 변경 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.POST, value = "/password/update/new")
	@ResponseBody
	public Constant updateMemberNewPassword(Constant constant, Authentication auth, @RequestBody PassData passData) {
		passData.email = auth.getName();
		Boolean success = dao.updateMemberNewPassword(passData);
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 비밀번호 변경 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 비밀번호 변경 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.POST, value = "/payment")
	@ResponseBody
	public Constant addPayment(Constant constant, Authentication auth, Gson gson, @RequestBody PaymentData paymentData) {
		Integer uid = dao.getUid(auth.getName());
		PaymentResult paymentResult = paymentDao.addPayment(uid, paymentData);
		if (paymentResult.resultMsg.equals("")) {
			return constant.setConstant(Constant.SUCCESS, "카드 등록 성공 : SUCCESS", gson.toJson(paymentResult));
		} else {
			return constant.setConstant(Constant.ERROR, "카드 등록 실패 : ERROR", gson.toJson(paymentResult));
		}
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.GET, value = "/payment")
	@ResponseBody
	public Constant removePayment(Constant constant, Authentication auth, Gson gson) {
		Integer uid = dao.getUid(auth.getName());
		Boolean success = paymentDao.removePayment(uid);
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "카드 삭제 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "카드 삭제 실패 : ERROR");
		}
	}
}
