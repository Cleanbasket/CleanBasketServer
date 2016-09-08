package com.bridge4biz.wash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bridge4biz.wash.data.ItemData;
import com.bridge4biz.wash.mybatis.PaymentDAO;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import com.bridge4biz.wash.fcm.FcmPushMessage;
import com.bridge4biz.wash.mybatis.DelivererDAO;
import com.bridge4biz.wash.mybatis.FcmDAO;
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
	private FcmDAO fcmDAO;

	@Autowired
	private PaymentDAO paymentDao;
	
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
		Integer uid = dao.getUid(auth.getName());
		Boolean success = dao.updatePickupRequestComplete(Integer.parseInt(data.get("oid")), data.get("note"));
		if (success) {
			FcmPushMessage.sendGradeNotification(fcmDAO.getRegid(uid));
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
	public Constant delivererDropOffComplete(Constant constant, Authentication auth, @RequestBody Order order, Gson gson) {
		Integer payment_method;
		Boolean success = false;
		Integer uid = dao.getUid(auth.getName());
		Integer price = order.price;
		Integer value = null;
		String gson_test = null;
		
		if (order.payment_method != null) {
			payment_method = order.payment_method;
			success = dao.updateDeliveryRequestComplete(uid, order.oid, order.note, payment_method);
			value = delivererDAO.paymentChangePrice(order);

			if (order.payment_method == 3){
				gson_test = gson.toJson(paymentDao.triggerPayment(order.oid, order.uid));
			}

		} else {
			success = null;
		}
		
		if (success && value == Constant.SUCCESS) {
			FcmPushMessage.sendGradeNotification(fcmDAO.getRegid(uid));
			return constant.setConstant(Constant.SUCCESS, "배달완료 처리 성공 : SUCCESS", gson_test);
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
	@RequestMapping(method=RequestMethod.POST, value = "/item/delete")
	@ResponseBody
	public Constant deleteItemOfOrder(Constant constant, @RequestBody ItemData itemData, Authentication auth) {

		Integer value = delivererDAO.deleteItems(itemData);

		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "아이템 수정 성공 : SUCCESS");
		}
		else {
			return constant.setConstant(Constant.ERROR, "아이템 수정 실패 : ERROR");
		}
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/item/update")
	@ResponseBody
	public Constant modifyItemOfOrder(Constant constant, @RequestBody ArrayList<ItemData> itemData, Authentication auth, Gson gson) {

		Order value = delivererDAO.modifyOrderItem(itemData);

		if (value != null) {
			return constant.setConstant(Constant.SUCCESS, "아이템 수정 성공 : SUCCESS", gson.toJson(value));
		}
		else {
			return constant.setConstant(Constant.ERROR, "아이템 수정 실패 : ERROR");
		}
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/order/update")
	@ResponseBody
	public Constant updateOrder(Constant constant, @RequestBody Order order, Authentication auth) {

		String name = dao.getDeliverer(dao.getUid(auth.getName()));

		String pre_pickup_date = delivererDAO.getOrderByOid(String.valueOf(order.oid)).pickup_date;
		String pre_dropoff_date = delivererDAO.getOrderByOid(String.valueOf(order.oid)).dropoff_date;

		Integer value = delivererDAO.updateOrder(order);

		if (value == Constant.SUCCESS) {

			String state = "";
			JSONObject subObject1 = new JSONObject();

			if(pre_pickup_date.equals(order.pickup_date)){

				subObject1.put("title", "변경이력");
				subObject1.put("description", pre_dropoff_date + "에서 " + order.dropoff_date + "로 변경되었습니다.");

				state = "배달";

			} else if(pre_dropoff_date.equals(order.dropoff_date)) {

				subObject1.put("title", "변경이력");
				subObject1.put("description", pre_pickup_date + "에서 " + order.pickup_date + "로 변경되었습니다.");

				state = "수거";
			}

			JSONObject subObject = new JSONObject();
			subObject.put("title", "주문번호");
			subObject.put("description", order.order_number);

			JSONArray jsonArray = new JSONArray();
			jsonArray.add(subObject);
			jsonArray.add(subObject1);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("connectColor", "#FF0066");
			jsonObject.put("body", "[시간변경] " + name + "가 "+ state +"시간을 변경했습니다.");
			jsonObject.put("connectInfo", jsonArray);



			URL obj = null;

			try {
				obj = new URL("https://wh.jandi.com/connect-api/webhook/11486269/75f086fdd098cdac2ce7cf6191a9b352");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Accept", "application/vnd.tosslab.jandi-v2+json");
				con.setRequestProperty("Content-Type", "application/json");
				con.setDoOutput(true);

				OutputStream os = con.getOutputStream();
				os.write(jsonObject.toJSONString().getBytes());
				os.flush();
				os.close();

				String response;

				int responseCode = con.getResponseCode();

				if(responseCode == HttpURLConnection.HTTP_OK) {

					InputStream is = null;
					ByteArrayOutputStream baos = null;
					is = con.getInputStream();
					baos = new ByteArrayOutputStream();
					byte[] byteBuffer = new byte[1024];
					byte[] byteData = null;
					int nLength = 0;
					while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
						baos.write(byteBuffer, 0, nLength);
					}
					byteData = baos.toByteArray();

				}


			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return constant.setConstant(Constant.SUCCESS, "아이템 수정 성공 : SUCCESS");
		}
		else {
			return constant.setConstant(Constant.ERROR, "아이템 수정 실패 : ERROR");
		}
	}


	@Secured("ROLE_DELIVERER")
	@RequestMapping(method=RequestMethod.POST, value = "/payment", consumes = { "application/json" })
	@ResponseBody
	public Constant triggerPayment(Constant constant, Gson gson, @RequestBody Order order) {
		int oid = Integer.valueOf(order.oid);
		int uid = Integer.valueOf(order.uid);

		Integer value = delivererDAO.paymentChangePrice(order);

		if (value == Constant.SUCCESS) {
			return constant.setConstant(Constant.SUCCESS, "결제 성공", gson.toJson(paymentDao.triggerPayment(oid, uid)));
		} else {
			return constant.setConstant(Constant.ERROR, "결제 실패 : ERROR");
		}
	}
}
