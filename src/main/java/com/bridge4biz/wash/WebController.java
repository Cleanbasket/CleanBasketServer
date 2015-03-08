package com.bridge4biz.wash;

import java.net.InetAddress;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.data.DropoffStateData;
import com.bridge4biz.wash.data.PickupStateData;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
public class WebController {

	@Autowired
	private MybatisDAO dao;

	@RequestMapping(value = "/admin/login")
	public String login(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.equals("wash")) {
			return "redirect:../expired";
		} else {
			return "login";
		}
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletResponse response, HttpServletRequest request, Authentication auth) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.equals("wash")) {
			return "redirect:./logout/success";
		} else {
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
			return "redirect:./admin/login";
		}
	}

	@RequestMapping(value = "/logout", consumes = { "application/json" })
	public String logoutJson(Authentication auth) {
		return "redirect:./logout/success";
	}

	@RequestMapping(value = "/term-of-use")
	public String termOfUse() {
		return "term-of-use";
	}

	@RequestMapping(value = "/privacy")
	public String privacy() {
		return "privacy";
	}


	@RequestMapping(value = "/service-info")
	public String serviceInfo() {
		return "service-info";
	}

	
	@RequestMapping(value = "/menu")
	public String menu() {
		return "menu";
	}

	@RequestMapping(value = "/account")
	public String account() {
		return "account";
	}

	@RequestMapping(value = "/join")
	public String join() {
		return "join";
	}

	@RequestMapping(value = "/address")
	public String address() {
		return "address";
	}

	@RequestMapping(value = "/phone-change")
	public String phoneChange() {
		return "phone-change";
	}

	@RequestMapping(value = "/password-change")
	public String passwordChange() {
		return "password-change";
	}

	@RequestMapping(value = "/order/calc")
	public String orderCalc() {
		return "order-calculation";
	}

	@RequestMapping(value = "/order/calc/etc")
	public String orderCalcEtc() {
		return "order-calculation-etc";
	}

	@RequestMapping(value = "/order/history")
	public String orderHistory() {
		return "order-history";
	}

	@RequestMapping(value = "/order/coupon")
	public String orderCoupon() {
		return "order-coupon";
	}

	@RequestMapping(value = "/admin/passwd")
	public String passwd() {
		return "passwd";
	}

	@RequestMapping(value = "/passwd/update")
	@ResponseBody
	public Constant passwdUpdate(Constant constant, @RequestBody Map<String, String> data) {
		String email = data.get("email");
		Integer value = dao.accountCheck(email, data.get("password"));
		if (value == Constant.ACCOUNT_VALID) {
			Boolean success = dao.updatePassword(email, data.get("newpass"));
			if (success) {
				constant.setConstant(Constant.SUCCESS, "비밀번호 변경 성공. : SUCCESS");
			} else {
				constant.setConstant(Constant.ERROR, "비밀번호 변경 중 오류가 발생하였습니다. : ERROR");
			}
		} else if (value == Constant.EMAIL_ERROR) {
			constant.setConstant(Constant.EMAIL_ERROR, "이메일 불일치 : EMAIL_ERROR");
		} else {
			constant.setConstant(Constant.PASSWORD_ERROR, "비밀번호 불일치 : PASSWORD_ERROR");
		}
		return constant;
	}

	@RequestMapping(value = "/")
	public String home(SitePreference sitePreference, HttpServletRequest request) {
		if (sitePreference == SitePreference.MOBILE) {
			if (request.getHeader("User-Agent").matches("(?i).*android.*")) {
				return "redirect:intent://#Intent;scheme=cleanbasket;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=com.bridge4biz.laundry;end";
			} else {
				return "redirect:https://play.google.com/store/apps/details?id=com.bridge4biz.laundry";
			}
		} else {
			return "home";
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin")
	public String homeAdmin() {
		return "redirect:./admin/order";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/order")
	public String order() {
		return "order";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/pickup")
	public String pickup() {
		return "pickup";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/dropoff")
	public String delivery() {
		return "dropoff";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member")
	public String member() {
		return "member";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/deliverer")
	public String deliverer() {
		return "deliverer";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/area")
	public String area() {
		return "area";
	}	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/order", consumes = { "application/json" })
	@ResponseBody
	public Constant orderState(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		String search = data.get("search");
		if (search.isEmpty()) {
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getOrderState()));
		} else {
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getOrderStateSearch(search)));
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.GET, value = "/admin/area", consumes = { "application/json" })
	@ResponseBody
	public Constant getArea(Constant constant, Gson gson) {		
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getAvailableAreaDatas()));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/admin/area", consumes = { "application/json" })
	@ResponseBody
	public Constant insertArea(Constant constant, Gson gson, @RequestBody Map<String, String> data) {		
		String areacode = data.get("areacode");
		String area = data.get("area");
		Boolean success = dao.insertArea(areacode, area);
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");			
		}
		return constant;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.DELETE, value = "/admin/area", consumes = { "application/json" })
	@ResponseBody
	public Constant deleteArea(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		String acidStr = data.get("acid");
		int acid = Integer.parseInt(acidStr);
		
		Boolean success = dao.deleteArea(acid);
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");			
		}
		
		return constant;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.GET, value = "/admin/date")
	@ResponseBody
	public Constant getData(Constant constant, Gson gson, @RequestBody Map<String, String> data) {		
		String acidStr = data.get("acid");
		Integer acid = Integer.parseInt(acidStr);
		
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getAvailableAreaDate(acid)));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/admin/date", consumes = { "application/json" })
	@ResponseBody
	public Constant insertDate(Constant constant, Gson gson, @RequestBody Map<String, String> data) {		
		String acidStr = data.get("acid");
		String area_date = data.get("area_date");
		int acid = Integer.parseInt(acidStr);
		Boolean success = dao.insertAreaDate(acid, area_date);
		
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");			
		}
		
		return constant;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.DELETE, value = "/admin/date", consumes = { "application/json" })
	@ResponseBody
	public Constant deleteDate(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		String adidStr = data.get("adid");
		int adid = Integer.parseInt(adidStr);
		Boolean success = dao.deleteAreaDate(adid);
		
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");			
		}
		
		return constant;
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/admin/alarm", consumes = { "application/json" })
	@ResponseBody
	public Constant insertAlarm(Constant constant, Gson gson, @RequestBody Map<String, String> data) {		
		String acidStr = data.get("acid");
		String phone = data.get("phone");
		int acid = Integer.parseInt(acidStr);
		Boolean success = dao.insertAreaAlarm(acid, phone);
		
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");			
		}
		
		return constant;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/pickup", consumes = { "application/json" })
	@ResponseBody
	public Constant pickupState(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getPickupState()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/pickup/assign", consumes = { "application/json" })
	@ResponseBody
	public Constant pickupAssign(Constant constant, @RequestBody PickupStateData pickupStateData) {
		Boolean success = dao.setPickupMan(pickupStateData);
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");
		}
		return constant;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/dropoff", consumes = { "application/json" })
	@ResponseBody
	public Constant dropoffState(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDropoffState()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/dropoff/assign", consumes = { "application/json" })
	@ResponseBody
	public Constant dropoffAssign(Constant constant, @RequestBody DropoffStateData dropoffStateData) {
		Boolean success = dao.setDropoffMan(dropoffStateData);
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");
		}
		return constant;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member", consumes = { "application/json" })
	@ResponseBody
	public Constant member(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		String search = data.get("search");
		if (search.isEmpty()) {
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getMemberInfo()));
		} else {
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getMemberInfoSearch(search)));
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member/append", consumes = { "application/json" })
	@ResponseBody
	public Constant memberAppend(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		String search = data.get("search");
		String uidStr = data.get("uid");
		Integer uid = uidStr != null ? Integer.parseInt(uidStr) : 0;
		if (search.isEmpty()) {
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getMemberInfoAppend(uid)));
		} else {
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getMemberInfoAppendSearch(search, uid)));
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member/order", consumes = { "application/json" })
	@ResponseBody
	public Constant memberOrder(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getMemberOrderInfo(data.get("uid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member/item", consumes = { "application/json" })
	@ResponseBody
	public Constant memberItem(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getItem(data.get("oid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member/coupon", consumes = { "application/json" })
	@ResponseBody
	public Constant memberCoupon(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getCoupon(data.get("oid"), data.get("uid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member/note", consumes = { "application/json" })
	@ResponseBody
	public Constant memberNote(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getNote(data.get("oid"), data.get("uid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/member/note/update", consumes = { "application/json" })
	@ResponseBody
	public Constant memberNoteUpdate(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		Boolean success = dao.updateNote(Integer.parseInt(data.get("oid")), Integer.parseInt(data.get("uid")), data.get("note"));
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "");
		} else {
			return constant.setConstant(Constant.ERROR, "");
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/deliverer", consumes = { "application/json" })
	@ResponseBody
	public Constant deliverer(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDelivererAll()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/deliverer/manage", consumes = { "application/json" })
	@ResponseBody
	public Constant delivererManage(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDelivererInfo()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/deliverer/enabled/update", consumes = { "application/json" })
	@ResponseBody
	public Constant delivererEnabledUpdate(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		Boolean success = dao.updateDelivererEnabled(Integer.parseInt(data.get("uid")), Boolean.parseBoolean(data.get("enabled")));
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "");
		} else {
			return constant.setConstant(Constant.ERROR, "");
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/admin/refresh")
	@ResponseBody
	public Constant getLatestOrderId(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getLatestOrderId()));
	}
	
	@RequestMapping(value = "/coupon/{serial_number}")
	public String couponCode(Model model, SitePreference sitePreference, HttpServletRequest request, @PathVariable String serial_number) {
		if (serial_number != null) {
			if (dao.couponSerialNumberCheck(serial_number)) {
				model.addAttribute("serial_number", serial_number);
			} else {
				model.addAttribute("serial_number", "발행된 쿠폰코드가 없습니다.");
			}
		} else {
			model.addAttribute("serial_number", "발행된 쿠폰코드가 없습니다.");
		}
		if (sitePreference == SitePreference.MOBILE) {
			if (request.getHeader("User-Agent").matches("(?i).*android.*")) {
				return "coupon-android";
			} else {
				return "coupon-mobile";
			}
		} else {
			return "coupon-pc";
		}
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void backupSchedule() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (hostName.matches(".*qsh-2627.*")) {
			commandStart("/data/mysql-backup", "root", "clba2014!", "wash");
			commandStart("/data/tomcat-backup");
		}
	}

	public void commandStart(String... command) {
		Process process = null;
		try {
			process = new ProcessBuilder(command).start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != process) {
				IOUtils.closeQuietly(process.getInputStream());
				IOUtils.closeQuietly(process.getOutputStream());
				IOUtils.closeQuietly(process.getErrorStream());
				process.destroy();
			}
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/backup")
	@ResponseBody
	public String backup() {
		backupSchedule();
		return "SUCCESS";
	}

	@RequestMapping(value = "/refresh")
	@ResponseBody
	public String refresh() {
		return "SUCCESS";
	}

	@Scheduled(fixedDelay = 60000)
	public void databaseRefresh() {
		dao.selectTest();
	}
}