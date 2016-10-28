package com.bridge4biz.wash;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.util.Constant;

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

	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/main")
	public String main() {
		return "pages/main";
	}

	@RequestMapping(value = "/sub01")
	public String sub01() {
		return "pages/sub01";
	}

	@RequestMapping(value = "/sub02")
	public String sub02() {
		return "pages/sub02";
	}

	@RequestMapping(value = "/sub03")
	public String sub03() {
		return "pages/sub03";
	}

	@RequestMapping(value = "/sub04")
	public String sub04() {
		return "pages/sub04";
	}

	@RequestMapping(value = "/sub0101")
	public String sub0101() {
		return "pages/sub0101";
	}

	@RequestMapping(value = "/sub0102")
	public String sub0102() {
		return "pages/sub0102";
	}

	@RequestMapping(value = "/sub0103")
	public String sub0103() {
		return "pages/sub0103";
	}

	@RequestMapping(value = "/sub0104")
	public String sub0104() {
		return "pages/sub0104";
	}

	@RequestMapping(value = "/sub0105")
	public String sub0105() {
		return "pages/sub0105";
	}

	@RequestMapping(value = "/sub0106")
	public String sub0106() {
		return "pages/sub0106";
	}

	@RequestMapping(value = "/sub0107")
	public String sub0107() {
		return "pages/sub0107";
	}

	@RequestMapping(value = "/sub0108")
	public String sub0108() {
		return "pages/sub0108";
	}
	

	@RequestMapping(value = "/sub0109")
	public String sub0109() {
		return "pages/sub0109";
	}
	
	@RequestMapping(value = "/sub02ask")
	public String sub02ask() {
		return "pages/sub02ask";
	}

	@RequestMapping(value = "/m_main")
	public String mobileMain() {
		return "pages/m_main";
	}

	@RequestMapping(value = "/m_sub04")
	public String mobileSub04() {
		return "pages/m_sub04";
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
		return "index";
		// }
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