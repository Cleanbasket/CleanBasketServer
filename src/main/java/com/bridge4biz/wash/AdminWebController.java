package com.bridge4biz.wash;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.data.DropoffStateData;
import com.bridge4biz.wash.data.PickupStateData;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.mybatis.PaymentDAO;
import com.bridge4biz.wash.service.PickupTime;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/admin")
public class AdminWebController {
	
	@Autowired
	private MybatisDAO dao;
	
	@Autowired
	private PaymentDAO paymentDao;

	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.equals("wash")) {
			return "redirect:../expired";
		} else {
			return "login";
		}
	}

	@RequestMapping(value = "/passwd")
	public String passwd() {
		return "passwd";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "")
	public String homeAdmin() {
		return "redirect:./admin/order";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/order")
	public String order() {
		return "order";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/pickup")
	public String pickup() {
		return "pickup";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/dropoff")
	public String delivery() {
		return "dropoff";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/member")
	public String member() {
		return "member";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/deliverer")
	public String deliverer() {
		return "deliverer";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/area")
	public String area() {
		return "area";
	}	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/dashboard")
	public String dashboard() {
		return "dashboard";
	}	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/order", consumes = { "application/json" })
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
	@RequestMapping(method=RequestMethod.GET, value = "/area", consumes = { "application/json" })
	@ResponseBody
	public Constant getArea(Constant constant, Gson gson) {		
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getAvailableAreaDatas()));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/area", consumes = { "application/json" })
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
	@RequestMapping(method=RequestMethod.DELETE, value = "/area", consumes = { "application/json" })
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
	@RequestMapping(method=RequestMethod.GET, value = "/date")
	@ResponseBody
	public Constant getData(Constant constant, Gson gson, @RequestBody Map<String, String> data) {		
		String acidStr = data.get("acid");
		Integer acid = Integer.parseInt(acidStr);
		
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getAvailableAreaDate(acid)));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/date", consumes = { "application/json" })
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
	@RequestMapping(method=RequestMethod.DELETE, value = "/date", consumes = { "application/json" })
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
	@RequestMapping(method=RequestMethod.POST, value = "/alarm", consumes = { "application/json" })
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
	@RequestMapping(value = "/pickup", consumes = { "application/json" })
	@ResponseBody
	public Constant pickupState(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getPickupState()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/pickup/assign", consumes = { "application/json" })
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
	@RequestMapping(value = "/dropoff", consumes = { "application/json" })
	@ResponseBody
	public Constant dropoffState(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDropoffState()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/dropoff/assign", consumes = { "application/json" })
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
	@RequestMapping(value = "/member", consumes = { "application/json" })
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
	@RequestMapping(value = "/member/append", consumes = { "application/json" })
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
	@RequestMapping(value = "/member/order", consumes = { "application/json" })
	@ResponseBody
	public Constant memberOrder(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getMemberOrderInfo(data.get("uid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/member/item", consumes = { "application/json" })
	@ResponseBody
	public Constant memberItem(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getItem(data.get("oid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/member/coupon", consumes = { "application/json" })
	@ResponseBody
	public Constant memberCoupon(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getCoupon(data.get("oid"), data.get("uid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/member/note", consumes = { "application/json" })
	@ResponseBody
	public Constant memberNote(Constant constant, Gson gson, @RequestBody Map<String, Integer> data) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getNote(data.get("oid"), data.get("uid"))));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/member/note/update", consumes = { "application/json" })
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
	@RequestMapping(value = "/deliverer", consumes = { "application/json" })
	@ResponseBody
	public Constant deliverer(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDelivererAll()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/deliverer/manage", consumes = { "application/json" })
	@ResponseBody
	public Constant delivererManage(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDelivererInfo()));
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/deliverer/enabled/update", consumes = { "application/json" })
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
	@RequestMapping(value = "/refresh")
	@ResponseBody
	public Constant getLatestOrderId(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getLatestOrderId()));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/payment", consumes = { "application/json" })
	@ResponseBody
	public Constant getPaymentInfo(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		int uid = Integer.valueOf(data.get("uid"));
		
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(paymentDao.getPaymentInfo(uid)));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/payment/trigger", consumes = { "application/json" })
	@ResponseBody
	public Constant triggerPayment(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		int oid = Integer.valueOf(data.get("oid"));
		int uid = Integer.valueOf(data.get("uid"));
		
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(paymentDao.triggerPayment(oid, uid)));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/payment/cancel", consumes = { "application/json" })
	@ResponseBody
	public Constant cancelPayment(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		int oid = Integer.valueOf(data.get("oid"));
		int uid = Integer.valueOf(data.get("uid"));
		String price = data.get("price");
		String partialCancelCode = data.get("code");
		
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(paymentDao.cancelPayment(oid, uid, price, partialCancelCode)));
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/pickup/date", consumes = { "application/json" })
	@ResponseBody
	public Constant addPickup(Constant constant, Gson gson, @RequestBody PickupTime data) {
		Boolean success = dao.addPickupType(data);
		
		if (success)
			return constant.setConstant(Constant.SUCCESS, "");
		else
			return constant.setConstant(Constant.ERROR, "");
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST, value = "/dropoff/date", consumes = { "application/json" })
	@ResponseBody
	public Constant addDropoff(Constant constant, Gson gson, @RequestBody PickupTime data) {
		Boolean success = dao.addDropoff(data);
		
		if (success)
			return constant.setConstant(Constant.SUCCESS, "");
		else
			return constant.setConstant(Constant.ERROR, "");
	}
}
