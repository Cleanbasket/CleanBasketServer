package com.bridge4biz.wash;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bridge4biz.wash.data.OrderData;
import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.service.Address;
import com.bridge4biz.wash.service.Member;
import com.bridge4biz.wash.util.Constant;
import com.bridge4biz.wash.util.EmailData;
import com.bridge4biz.wash.util.EmailService;
import com.bridge4biz.wash.util.PushMessage;
import com.bridge4biz.wash.util.SocketIO;
import com.google.gson.Gson;

@Controller
public class AppController {

	@Autowired
	private MybatisDAO dao;

	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "/member/join")
	@ResponseBody
	public Constant memberJoin(@RequestBody UserData userData, Constant constant) {
		userData.authority = "ROLE_MEMBER";
		userData.enabled = true;
		Integer value = dao.addUserForMember(userData);
		switch (value) {
		case Constant.SUCCESS:
			SocketIO.broadCast(new PushMessage(Constant.PUSH_MEMBER_JOIN, userData.uid, 0));
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
	@RequestMapping(value = "/member/order")
	@ResponseBody
	public Constant memberOrder(Constant constant, Gson gson, Authentication auth) {
		return constant.setConstant(Constant.SUCCESS, "일반회원 주문정보 가져오기 성공 : SUCCESS", gson.toJson(dao.getOrder(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/member/order/add")
	@ResponseBody
	public Constant addMemberOrder(Constant constant, @RequestBody OrderData orderData, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.addOrder(orderData, uid);
		if (value == Constant.SUCCESS) {
			SocketIO.broadCast(new PushMessage(Constant.PUSH_ORDER_ADD, uid, orderData.oid));
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS", orderData.order_number);
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/member/order/del")
	@ResponseBody
	public Constant delMemberOrder(Constant constant, @RequestBody OrderData orderData, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		Integer value = dao.delOrder(orderData, uid);
		if (value == Constant.SUCCESS) {
			SocketIO.broadCast(new PushMessage(Constant.PUSH_ORDER_CANCEL, uid, orderData.oid));
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문취소 성공 : SUCCESS");
		} else if (value == Constant.IMPOSSIBLE) {
			return constant.setConstant(Constant.IMPOSSIBLE, "주문취소가 불가능한 상태가 입니다. : IMPOSSIBLE");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문취소 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/member/coupon")
	@ResponseBody
	public Constant memberCoupon(Constant constant, Gson gson, Authentication auth) {
		return constant.setConstant(Constant.SUCCESS, "일반회원 쿠폰 가져오기 성공 : SUCCESS", gson.toJson(dao.getAvailableCoupon(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/member/coupon/issue")
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
	@RequestMapping(value = "/member/address")
	@ResponseBody
	public Constant memberAddress(Constant constant, Gson gson, Authentication auth) {
		return constant.setConstant(Constant.SUCCESS, "일반회원 주소 가져오기 성공 : SUCCESS", gson.toJson(dao.getAddress(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/member", consumes = { "application/json" })
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
	@RequestMapping(value = "/member/address/update")
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
	@RequestMapping(value = "/member/phone/update")
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
	@RequestMapping(value = "/member/password/update")
	@ResponseBody
	public Constant updateMemberPassword(Constant constant, Authentication auth, @RequestBody UserData userData) {
		userData.email = auth.getName();
		Boolean success = dao.updateMemberPasswrod(userData);
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 비밀번호 변경 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "일반회원 비밀번호 변경 실패 : ERROR");
		}
	}

	@Secured("ROLE_MEMBER")
	@RequestMapping(value = "/recommendation")
	@ResponseBody
	public Constant recommendation(Constant constant, Authentication auth) {
		Integer uid = dao.getUid(auth.getName());
		StringBuilder serial_number = new StringBuilder();
		Boolean success = dao.addRecommendationCoupon(uid, serial_number);
		if (success == null) { 
			return constant.setConstant(Constant.DUPLICATION, "이미 발급되어있습니다. : DUPLICATION", "coupon/" + serial_number);
		} else if (success) {
			return constant.setConstant(Constant.SUCCESS, "쿠폰지급 준비 성공 : SUCCESS", "coupon/" + serial_number);
		} else {
			return constant.setConstant(Constant.ERROR, "쿠폰지급  준비 에러 : ERROR");
		}
	}

	@RequestMapping(value = "/deliverer/join")
	@ResponseBody
	public Constant delivererJoin(HttpServletRequest request, UserData user, Constant constant, @RequestParam(value = "file") MultipartFile file) {
		user.authority = "ROLE_DELIVERER";
		user.enabled = false;
		user.setDeliverer(request);
		Integer value = dao.addUserForDeliverer(user, file);
		switch (value) {
		case Constant.SUCCESS:
			SocketIO.broadCast(new PushMessage(Constant.PUSH_DELIVERER_JOIN, user.uid, 0));
			constant.setConstant(Constant.SUCCESS, "배달자 회원가입 성공 : SUCCESS");
			break;
		case Constant.ERROR:
			constant.setConstant(Constant.ERROR, "배달자 회원가입 실패 : ERROR");
			break;
		case Constant.IMAGE_WRITE_ERROR:
			constant.setConstant(Constant.IMAGE_WRITE_ERROR, "배달자 이미지 쓰기 실패 : IMAGE_WRITE_ERROR");
			break;
		case Constant.ACCOUNT_DUPLICATION:
			constant.setConstant(Constant.ACCOUNT_DUPLICATION, "배달자 이메일 중복 : ACCOUNT_DUPLICATION");
			break;
		}
		return constant;
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/deliverer/pickup")
	@ResponseBody
	public Constant delivererPickup(Constant constant, Authentication auth, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "수거요청 정보 가져오기 성공 : SUCCESS", gson.toJson(dao.getPickupRequest(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/deliverer/pickup/complete")
	@ResponseBody
	public Constant delivererPickupComplete(Constant constant, Authentication auth, @RequestBody Map<String, String> data) {
		Boolean success = dao.updatePickupRequestComplete(Integer.parseInt(data.get("oid")), data.get("note"));
		if (success) {
			SocketIO.broadCast(new PushMessage(Constant.PUSH_PICKUP_COMPLETE, 0, Integer.parseInt(data.get("oid"))));
			return constant.setConstant(Constant.SUCCESS, "수거완료 처리 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "수거완료 처리 실패 : ERROR");
		}
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/deliverer/dropoff")
	@ResponseBody
	public Constant delivererDropOff(Constant constant, Authentication auth, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "배달요청 정보 가져오기 성공 : SUCCESS", gson.toJson(dao.getDeliveryRequest(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/deliverer/dropoff/complete")
	@ResponseBody
	public Constant delivererDropOffComplete(Constant constant, Authentication auth, @RequestBody Map<String, String> data) {
		Boolean success = dao.updateDeliveryRequestComplete(Integer.parseInt(data.get("oid")), data.get("note"));
		if (success) {
			SocketIO.broadCast(new PushMessage(Constant.PUSH_DROPOFF_COMPLETE, 0, Integer.parseInt(data.get("oid"))));
			return constant.setConstant(Constant.SUCCESS, "배달완료 처리 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "배달완료 처리 실패 : ERROR");
		}
	}

	@RequestMapping(value = "/password/inquiry")
	@ResponseBody
	public Constant passwordInquiry(Constant constant, EmailData email, @RequestBody UserData userData) {
		email.setFrom("password@cleanbasket.co.kr");
		email.setFromPersonal("비밀번호고객지원");
		email.setSubject("CleanBasket 임시비밀번호 입니다.");
		Integer result = dao.passwordInquiry(userData, emailService, email);
		switch (result) {
		case 0:
			constant.setConstant(Constant.ERROR, "서버 오류 : ERROR");
			break;
		case 1:
			constant.setConstant(Constant.SUCCESS, "성공 : SUCCESS");
			break;
		case 2:
			constant.setConstant(Constant.ERROR, "이메일 전송에러 : ERROR");
			break;
		case 3:
			constant.setConstant(Constant.EMAIL_ERROR, "존재하지 않는 이메일주소 : EMAIL_ERROR");
			break;
		}
		return constant;
	}

	@Secured({ "ROLE_DELIVERER", "ROLE_MEMBER" })
	@RequestMapping(value = "/item/code")
	@ResponseBody
	public Constant itemCode(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "아이템 코드 가져오기 성공 : SUCCESS", gson.toJson(dao.getItemCode()));
	}

	@Secured({ "ROLE_DELIVERER", "ROLE_MEMBER" })
	@RequestMapping(value = "/regid")
	@ResponseBody
	public Constant regid(@RequestBody Map<String, String> data, Constant constant, Authentication auth, Gson gson) {
		String regid = data.get("regid");
		Boolean success = dao.updateRegid(dao.getUid(auth.getName()), regid);
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "REGID 수정 성공 : SUCCESS", gson.toJson(dao.getRegid(dao.getUid(auth.getName()))));
		} else {
			return constant.setConstant(Constant.ERROR, "REGID 수정 실패 : ERROR");
		}
	}

	@Secured({ "ROLE_DELIVERER", "ROLE_MEMBER" })
	@RequestMapping(value = "/auth/check")
	@ResponseBody
	public Constant authCheck(Constant constant, Authentication auth, Gson gson) {
		Integer uid = dao.getUid(auth.getName());
		dao.clearRegid(uid);
		return constant.setConstant(Constant.SESSION_VALID, "로그인되어 있는 상태입니다. : SESSION_VALID", gson.toJson(uid));
	}

	@RequestMapping(value = "/expired")
	@ResponseBody
	public Constant expired(Constant constant) {
		return constant.setConstant(Constant.SESSION_EXPIRED, "인증 정보가 더 이상 유효하지 않습니다. : SESSION_EXPIRED");
	}

	@RequestMapping(value = "/logout/success")
	@ResponseBody
	public Constant logoutSuccess(Constant constant, HttpServletResponse response, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		Cookie[] cookieArray = { new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE", null), new Cookie("JSESSIONID", null) };
		for (Cookie cookie : cookieArray) {
			cookie.setMaxAge(0);
			cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request.getContextPath() : "/");
			response.addCookie(cookie);
		}
		return constant.setConstant(Constant.SUCCESS, "로그아웃이 성공하였습니다. : SUCCESS");
	}

	@RequestMapping(value = "/version")
	public String version() {
		return "version";
	}

	@RequestMapping(value = "/hours")
	@ResponseBody
	public Constant hours(Constant constant, Gson gson) {
		SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.KOREA);
		SimpleDateFormat month = new SimpleDateFormat("MM", Locale.KOREA);
		SimpleDateFormat day = new SimpleDateFormat("dd", Locale.KOREA);
		SimpleDateFormat hours = new SimpleDateFormat("HH", Locale.KOREA);
		HashMap<String, String> datetime = new HashMap<String, String>();
		datetime.put("year", year.format(new Date()));
		datetime.put("month", month.format(new Date()));
		datetime.put("day", day.format(new Date()));
		datetime.put("hours", hours.format(new Date()));
		return constant.setConstant(Constant.SUCCESS, "서버시간정보입니다.", gson.toJson(datetime));
	}
}
