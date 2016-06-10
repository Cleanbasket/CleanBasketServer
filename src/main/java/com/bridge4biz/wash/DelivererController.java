package com.bridge4biz.wash;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bridge4biz.wash.service.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bridge4biz.wash.data.DropoffStateData;
import com.bridge4biz.wash.data.PickupStateData;
import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.mybatis.DelivererDAO;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/deliverer")
public class DelivererController {
	@Autowired
	private MybatisDAO dao;
	
	@Autowired
	private DelivererDAO delivererDAO;
	
	@RequestMapping(value = "/join")
	@ResponseBody
	public Constant delivererJoin(HttpServletRequest request, UserData user, Constant constant, @RequestParam(value = "file") MultipartFile file) {
		user.authority = "ROLE_DELIVERER";
		user.enabled = false;
		user.setDeliverer(request);
		Integer value = delivererDAO.addUserForDeliverer(user, file);
		switch (value) {
			case Constant.SUCCESS:
	//			SocketIO.broadCast(new PushMessage(Constant.PUSH_DELIVERER_JOIN, user.uid, 0));
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
	@RequestMapping(value = "/order")
	@ResponseBody
	public Constant delivererOrder(Constant constant, Authentication auth, Gson gson, @RequestBody Map<String, String> data) {
		int oid = 0;
		
		if (!data.get("oid").equals("(null)"))
			oid = Integer.parseInt(data.get("oid"));
		
		return constant.setConstant(Constant.SUCCESS, "수거요청 정보 가져오기 성공 : SUCCESS", gson.toJson(delivererDAO.getRecentOrder(oid)));
	}
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/pickup")
	@ResponseBody
	public Constant delivererPickup(Constant constant, Authentication auth, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "수거요청 정보 가져오기 성공 : SUCCESS", gson.toJson(dao.getPickupRequest(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/pickup/complete")
	@ResponseBody
	public Constant delivererPickupComplete(Constant constant, Authentication auth, @RequestBody Map<String, String> data) {
		Boolean success = dao.updatePickupRequestComplete(Integer.parseInt(data.get("oid")), data.get("note"));
		if (success) {
//			SocketIO.broadCast(new PushMessage(Constant.PUSH_PICKUP_COMPLETE, 0, Integer.parseInt(data.get("oid"))));
			return constant.setConstant(Constant.SUCCESS, "수거완료 처리 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "수거완료 처리 실패 : ERROR");
		}
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/dropoff")
	@ResponseBody
	public Constant delivererDropOff(Constant constant, Authentication auth, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "배달요청 정보 가져오기 성공 : SUCCESS", gson.toJson(dao.getDeliveryRequest(dao.getUid(auth.getName()))));
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/dropoff/complete")
	@ResponseBody
	public Constant delivererDropOffComplete(Constant constant, Authentication auth, @RequestBody Map<String, String> data) {
		String payment_method;
		Boolean success = false;
		Integer uid = dao.getUid(auth.getName());
		
		if (data.containsKey("payment_method")) {
			payment_method = data.get("payment_method");
			success = dao.updateDeliveryRequestComplete(uid, Integer.parseInt(data.get("oid")), data.get("note"), payment_method);
		} else {
			success = dao.updateDeliveryRequestComplete(uid, Integer.parseInt(data.get("oid")), data.get("note"), null);
		}
		
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "배달완료 처리 성공 : SUCCESS");
		} else {
			return constant.setConstant(Constant.ERROR, "배달완료 처리 실패 : ERROR");
		}
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/order/pickup", consumes = { "application/json" })
	@ResponseBody
	public Constant pickupState(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(delivererDAO.getPickUpOrder()));
	}

	@Secured("ROLE_DELIVERER")
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

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/order/dropoff", consumes = { "application/json" })
	@ResponseBody
	public Constant dropoffState(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(delivererDAO.getDropOffOrder()));
	}

	@Secured("ROLE_DELIVERER")
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
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/assign/cancel")
	@ResponseBody
	public Constant assignCancel(Constant constant, @RequestBody Order order, Authentication auth) {
		Integer value = delivererDAO.cancelAssign(order);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS");
		} 
		else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/date")
	@ResponseBody
	public Constant modifyMemberOrderDate(Constant constant, @RequestBody Order order, Authentication auth) {
		Integer value = delivererDAO.modifyOrderDate(order);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS");
		} 
		else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/total")
	@ResponseBody
	public Constant modifyMemberOrderTotal(Constant constant, @RequestBody Order order, Authentication auth) {
		Integer value = delivererDAO.modifyOrderTotal(order);
		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "일반회원 주문 성공 : SUCCESS");
		} 
		else {
			return constant.setConstant(Constant.ERROR, "일반회원 주문 실패 : ERROR");
		}
	}
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/list")
	@ResponseBody
	public Constant delivererManage(Constant constant, Gson gson) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDelivererAll()));
	}
	
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.GET, value = "/order/{oid}")
	@ResponseBody
	public Constant getOrderByOid(Constant constant, Gson gson, @PathVariable String oid) {
		Order order = delivererDAO.getOrderByOid(oid);
		
		if (order != null)
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(order));
		else
			return constant.setConstant(Constant.ERROR, "");
	}
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/phone")
	@ResponseBody
	public Constant getOrderByPhone(Constant constant, Gson gson, @RequestBody Map<String, String> data) {
		String phone = data.get("phone");
		ArrayList<Order> orders = delivererDAO.getOrderByPhone(phone);
		
		if (orders != null)
			return constant.setConstant(Constant.SUCCESS, "", gson.toJson(orders));
		else
			return constant.setConstant(Constant.ERROR, "");
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/item/update")
	@ResponseBody
	public Constant modifyItemOfOrder(Constant constant, @RequestBody Order order, Authentication auth) {

		Integer value = delivererDAO.modifyOrderItem(order);

		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "아이템 수정 성공 : SUCCESS");
		}
		else {
			return constant.setConstant(Constant.ERROR, "아이템 수정 실패 : ERROR");
		}
	}
}
