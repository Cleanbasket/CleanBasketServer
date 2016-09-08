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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.mybatis.MileageDao;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.util.Constant;
import com.bridge4biz.wash.util.EmailData;
import com.bridge4biz.wash.util.EmailService;
import com.google.gson.Gson;

@RestController
public class AppController {

	@Autowired
	private MybatisDAO dao;

	@Autowired
	private EmailService emailService;
	
	@RequestMapping(method=RequestMethod.GET, value = "/item")
	@ResponseBody
	public Constant getItemList(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getItemInfo()));		
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/notice")
	@ResponseBody
	public Constant getNotice(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getNotice()));		
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.GET, value = "/mileage")
	@ResponseBody
	public Constant getMileage(Constant constant, Gson gson, Authentication auth) {
		MileageDao mileageDao = new MileageDao();
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(mileageDao.getMileage(dao.getUid(auth.getName()))));		
	}
	
	@Secured("ROLE_MEMBER")
	@RequestMapping(method=RequestMethod.GET, value = "/appinfo")
	@ResponseBody
	public Constant getAppInfo(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getAppInfo()));		
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/code")
	@ResponseBody
	public Constant getAuthorizationCode(Constant constant, Gson gson, Authentication auth, @RequestBody Order order) {
		String phone = order.phone;
		Boolean success = dao.getAuthorizationCode(dao.getUid(auth.getName()), phone);
		if(success)
			return constant.setConstant(Constant.SUCCESS, "인증코드 문자 전송 성공");
		
		return constant.setConstant(Constant.ERROR, "인증코드 문자 전송 실패");
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
	
	@RequestMapping(value = "/password/inquiry")
	@ResponseBody
	public Constant passwordInquiry(Constant constant, EmailData email, @RequestBody UserData userData) {
		email.setFrom("password@cleanbasket.co.kr");
		email.setFromPersonal("Password Support");
		email.setSubject("Clean Basket Temporary Password");
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

	@Secured({ "ROLE_MEMBER" })
	@RequestMapping(method=RequestMethod.GET, value = "/district")
	@ResponseBody
	public Constant getDistrict(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "아이템 코드 가져오기 성공 : SUCCESS", gson.toJson(dao.getDistricts()));
	}

	
	@Secured({ "ROLE_DELIVERER", "ROLE_MEMBER" })
	@RequestMapping(method=RequestMethod.POST, value = "/regid", consumes = { "application/json" })
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
