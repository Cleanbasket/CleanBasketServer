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

	@RequestMapping(value = "/logout")
	public String logout(HttpServletResponse response,
			HttpServletRequest request, Authentication auth) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.equals("wash")) {
			return "redirect:./logout/success";
		} else {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			Cookie[] cookieArray = {
					new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE", null),
					new Cookie("JSESSIONID", null) };
			for (Cookie cookie : cookieArray) {
				cookie.setMaxAge(0);
				cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request
						.getContextPath() : "/");
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

	@RequestMapping(value = "/payment-term-of-use")
	public String paymentTermOfUse() {
		return "payment-term-of-use";
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

	@RequestMapping(value = "/coupon/{serial_number}")
	public String couponCode(Model model, SitePreference sitePreference,
			HttpServletRequest request, @PathVariable String serial_number) {
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

	@RequestMapping(value = "/passwd/update")
	@ResponseBody
	public Constant passwdUpdate(Constant constant,
			@RequestBody Map<String, String> data) {
		String email = data.get("email");
		Integer value = dao.accountCheck(email, data.get("password"));
		if (value == Constant.ACCOUNT_VALID) {
			Boolean success = dao.updatePassword(email, data.get("newpass"));
			if (success) {
				constant.setConstant(Constant.SUCCESS, "비밀번호 변경 성공. : SUCCESS");
			} else {
				constant.setConstant(Constant.ERROR,
						"비밀번호 변경 중 오류가 발생하였습니다. : ERROR");
			}
		} else if (value == Constant.EMAIL_ERROR) {
			constant.setConstant(Constant.EMAIL_ERROR, "이메일 불일치 : EMAIL_ERROR");
		} else {
			constant.setConstant(Constant.PASSWORD_ERROR,
					"비밀번호 불일치 : PASSWORD_ERROR");
		}
		return constant;
	}

	@RequestMapping(value = "/")
	public String home(SitePreference sitePreference, HttpServletRequest request) {
		// if (sitePreference == SitePreference.MOBILE) {
		// if (request.getHeader("User-Agent").matches("(?i).*android.*")) {
		// return
		// "redirect:intent://#Intent;scheme=cleanbasket;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=com.bridge4biz.laundry;end";
		// } else {
		// return
		// "redirect:https://play.google.com/store/apps/details?id=com.bridge4biz.laundry";
		// }
		// } else {
		return "home";
		// }
	}

	// @Scheduled(cron = "0 0 0 * * *")
	// public void backupSchedule() {
	// String hostName = null;
	// try {
	// hostName = InetAddress.getLocalHost().getHostName();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if (hostName.matches(".*qsh-2627.*")) {
	// commandStart("/data/mysql-backup", "manager", "fastdel2015@", "wash");
	// commandStart("/data/tomcat-backup");
	// }
	// }
	//
	// public void commandStart(String... command) {
	// Process process = null;
	// try {
	// process = new ProcessBuilder(command).start();
	// process.waitFor();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (null != process) {
	// IOUtils.closeQuietly(process.getInputStream());
	// IOUtils.closeQuietly(process.getOutputStream());
	// IOUtils.closeQuietly(process.getErrorStream());
	// process.destroy();
	// }
	// }
	// }

	// @Secured("ROLE_ADMIN")
	// @RequestMapping(value = "/backup")
	// @ResponseBody
	// public String backup() {
	// backupSchedule();
	// return "SUCCESS";
	// }

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