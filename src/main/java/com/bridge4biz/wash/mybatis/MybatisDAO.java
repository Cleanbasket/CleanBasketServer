package com.bridge4biz.wash.mybatis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;
import com.bridge4biz.wash.data.AddressData;
import com.bridge4biz.wash.data.AreaAlarmData;
import com.bridge4biz.wash.data.AreaData;
import com.bridge4biz.wash.data.AreaDateData;
import com.bridge4biz.wash.data.CouponCodeData;
import com.bridge4biz.wash.data.CouponData;
import com.bridge4biz.wash.data.DropoffStateData;
import com.bridge4biz.wash.data.ItemData;
import com.bridge4biz.wash.data.OrderData;
import com.bridge4biz.wash.data.OrderStateData;
import com.bridge4biz.wash.data.PassData;
import com.bridge4biz.wash.data.PickupStateData;
import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.gcm.Message;
import com.bridge4biz.wash.gcm.MulticastResult;
import com.bridge4biz.wash.gcm.Result;
import com.bridge4biz.wash.gcm.Sender;
import com.bridge4biz.wash.service.Address;
import com.bridge4biz.wash.service.AppInfo;
import com.bridge4biz.wash.service.Area;
import com.bridge4biz.wash.service.AreaDate;
import com.bridge4biz.wash.service.AuthUser;
import com.bridge4biz.wash.service.Coupon;
import com.bridge4biz.wash.service.Deliverer;
import com.bridge4biz.wash.service.DelivererInfo;
import com.bridge4biz.wash.service.DelivererWork;
import com.bridge4biz.wash.service.District;
import com.bridge4biz.wash.service.DropoffState;
import com.bridge4biz.wash.service.Feedback;
import com.bridge4biz.wash.service.Item;
import com.bridge4biz.wash.service.ItemCode;
import com.bridge4biz.wash.service.ItemInfo;
import com.bridge4biz.wash.service.Member;
import com.bridge4biz.wash.service.MemberInfo;
import com.bridge4biz.wash.service.MemberOrderInfo;
import com.bridge4biz.wash.service.Notice;
import com.bridge4biz.wash.service.Notification;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.service.OrderState;
import com.bridge4biz.wash.service.PickupState;
import com.bridge4biz.wash.sms.SendSMS;
import com.bridge4biz.wash.sms.Set;
import com.bridge4biz.wash.util.Constant;
import com.bridge4biz.wash.util.EmailData;
import com.bridge4biz.wash.util.EmailService;
import com.bridge4biz.wash.util.RandomNumber;

public class MybatisDAO {
	private static final Logger log = LoggerFactory.getLogger(MybatisDAO.class);		

	private MybatisMapper mapper;
		
	public MybatisDAO() {
		
	}

	@Autowired
	private MybatisDAO(MybatisMapper mapper, PlatformTransactionManager platformTransactionManager) {
		this.mapper = mapper;
	}

	public Integer selectTest() {
		try {
			return mapper.selectTest();
		} catch (Exception e) {
			return 0;
		}
	}

	public Integer getUid(String email) {
		return mapper.getUid(email);
	}

	public String getAuthority(String email) {
		return mapper.getAuthority(email);
	}
	
	public Integer loginFailureCheck(String email, String password) {
		try {
			if (mapper.emailCheck(email) == 0) {
				return Constant.EMAIL_ERROR;
			} else if (mapper.accountCheck(email, password) == 0) {
				return Constant.PASSWORD_ERROR;
			} else if (mapper.emailCheckIsEnabled(email) == 0) {
				return Constant.ACCOUNT_DISABLED;
			} else {
				return Constant.ACCOUNT_VALID;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.ERROR;
		}
	}

	public Integer accountCheck(String email, String password) {
		if (mapper.emailCheck(email) == 0) {
			return Constant.EMAIL_ERROR;
		} else if (mapper.accountCheck(email, password) == 0) {
			return Constant.PASSWORD_ERROR;
		} else {
			return Constant.ACCOUNT_VALID;
		}
	}

	public Boolean updatePassword(String email, String password) {
		try {
			return mapper.updatePassword(email, password);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean updateRegid(Integer uid, String regid) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		try {
			mapper.updateRegid(uid, regid);
		} catch (DuplicateKeyException duplicateKeyException) {
			mapper.clearAllRegid(regid);
			mapper.updateRegid(uid, regid);
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return false;
		}
		// platformTransactionManager.commit(status);
		return true;
	}

	public Boolean clearRegid(Integer uid) {
		return mapper.clearRegid(uid);
	}

	public Boolean clearAllRegid(String regid) {
		try {
			mapper.clearAllRegid(regid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getRegid(Integer uid) {
		return mapper.getRegid(uid);
	}

	public Integer getSale() {
		return mapper.getSale();
	}
	
	public Integer addUserForMember(UserData userData) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		if (mapper.getUid(userData.email) != null) {
			return Constant.ACCOUNT_DUPLICATION;
		}
		try {
			mapper.addUser(userData);
			mapper.addAddress(new AddressData(userData.uid, 0, userData.address, userData.addr_number, userData.addr_building, userData.addr_remainder));
			for (int i = 1; i < 5; i++) {
				mapper.addAddress(new AddressData(userData.uid, i, "", "", "", ""));
			}
			
			ArrayList<CouponCodeData> codeDatas = mapper.getOrderCoupon();
			for (CouponCodeData data : codeDatas) {
				mapper.addCoupon(new CouponData(userData.uid, data.coupon_code, data.value, null, null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}
		// platformTransactionManager.commit(status);
		return Constant.SUCCESS;
	}

	public Integer registerUserForMember(UserData userData) {
		if (mapper.getUid(userData.email) != null) {
			return Constant.ACCOUNT_DUPLICATION;
		}
		try {
			mapper.addUser(userData);

			
			
		} catch (Exception e) {
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}
		// platformTransactionManager.commit(status);
		return Constant.SUCCESS;
	}
	
	
	private boolean checkEnglish(String str) {
		for(int i = 0; i < str.length(); i++) {
			if(Character.getType(str.charAt(i)) == 5) {
				return false;
			}
		}
		
		return true;
	}
	
	private District makeDistrict(String address) {
		address = address.replaceAll(",", "");
		String[] districts = address.split(" ");
		String city;
		String dong;
		
		if (checkEnglish(districts[0])) {
			city = districts[2];
			dong = districts[0];
		}
		else {
			city = districts[0];
			dong = districts[2];
		}
		
		String district = districts[1];

		
		return new District(city, district, dong);
	}
	
	public Integer addNewOrder(Order order, Integer uid) {
		if (priceCheck(order, uid) == false) {
			return Constant.ERROR;
		}
		
		try {
			order.uid = uid;
			String address = order.address;

			District districtObject = makeDistrict(address);
			
			Integer dcid = 0;
			
			if (mapper.isAvailableDistrictWithNull(districtObject) == 0) {
				if (mapper.isAvailableDistrict(districtObject) == 0)
					return Constant.ADDRESS_UNAVAILABLE;
				else dcid = mapper.getDistrictId(districtObject);
			} else 
				dcid = mapper.getDistrictIdWithNull(districtObject);
			
			if(checkUnavailableDistricDate(dcid, order.pickup_date, order.dropoff_date)) {
				return Constant.DATE_UNAVAILABLE;
			}			
			
			if(mapper.getNumberOfAddressByUid(uid) == 0) {
				mapper.addAddress(new AddressData(uid, order.address, order.addr_building));
			} else {
				mapper.updateMemberAddress(new Address(order.uid, order.address, order.addr_building));
			}

			Integer adrid = mapper.getAddressByUid(uid);
			order.adrid = adrid;
			order.uid = uid;
			
			if(!mapper.insertOrder(order)) {
				return Constant.ERROR;
			}
			
			Integer oid = mapper.getLatestOrderId();
			
			order.order_number = new SimpleDateFormat("yyMMdd").format(new Date()) + "-" + order.oid;
			mapper.updateNewOrderNumber(order);
			
			Integer price = 0;

			for (Item item : order.item) {
				price = (int) (mapper.getItemPrice(item.item_code) * (1 - item.discount_rate));
				mapper.addItem(new ItemData(oid, item.item_code, price, item.count));
			}

			for (Coupon coupon : order.coupon) {
				mapper.updateCoupon(order.uid, oid, coupon.cpid);
			}

			if (order.mileage > 0) 
				delMileage(uid, oid, order.mileage);
			
			ArrayList<String> phones = mapper.getDistrictPhones(dcid);
			
			sendNewSMS(order, order.address + " " + order.addr_building, phones);
		} catch (Exception e) {
			e.printStackTrace();
			
			return Constant.ERROR;
		}
		
		return Constant.SUCCESS;
	}
	
	public void addMileage(int uid, int oid, int mileage) {		
		if (mapper.isAuthUser(uid) == 0)
			return;
		
		int totalMileage = mapper.getMileage(uid);
		mapper.addUseOfMileage(uid, oid, 0, mileage);
		mapper.updateMileageByUser(uid, totalMileage + mileage);
	}
	

	public void delMileage(int uid, int oid, int mileage) {
		if (mapper.isAuthUser(uid) == 0)
			return;
		
		int totalMileage = mapper.getMileage(uid);

		mapper.addUseOfMileage(uid, oid, 1, mileage);
		mapper.updateMileageByUser(uid, totalMileage - mileage);
	}
	
	
	public void addTotal(int uid, int price) {
		if (mapper.isAuthUser(uid) == 0)
			return;
		
		int total = mapper.getTotalByUser(uid);
		
		mapper.updateTotalByUser(uid, total + price);
		updateUser(uid);
	}
	
	public void delTotal(int uid, int price) {
		if (mapper.isAuthUser(uid) == 0)
			return;
		
		int total = mapper.getTotalByUser(uid);
		
		mapper.updateTotalByUser(uid, total - price);
		updateUser(uid);
	}
	
	public float getAccumulationRate(int uid) {
		switch (mapper.getAccumulationRateByUser(uid)) {
			case 0:
				return (float) 0.01;
				
			case 1:
				return (float) 0.02;
				
			case 2:
				return (float) 0.03;
				
			case 3:
				return (float) 0.04;
		}
		
		return mapper.getAccumulationRateByUser(uid);
	}
	
	public void updateUser(int uid) {
		if (mapper.getTotalPriceBySixMonth(uid) == null)
			return;
		
		int total = mapper.getTotalPriceBySixMonth(uid);
				
		if (total >= 150000 && total < 300000) {
			mapper.updateUserClass(uid, 1);
		} else if (total >= 300000 && total < 500000) {
			mapper.updateUserClass(uid, 2);
		} else if (total >= 500000) {
			mapper.updateUserClass(uid, 3);
		} 
	}
	
	public Integer addOrder(OrderData orderData, Integer uid) {
		if (priceCheck(orderData, uid) == false) {
			return Constant.ERROR;
		}
		
		String fullAddr;
		
		try {
			orderData.uid = uid;
			
			String district;
		
			Address address = mapper.getAddressForSingle(orderData.adrid, orderData.uid);
			district = address.address;
			
			orderData.address = address.address;
			orderData.addr_number = address.addr_number;
			orderData.addr_building = address.addr_building;
			orderData.addr_remainder = address.addr_remainder;
			
			fullAddr = address.address + " " + address.addr_number + " " + address.addr_building + " " + address.addr_remainder;
			
			String[] districts = district.split(" ");
			String addr = districts[0] + " " + districts[1];
			
			// 서비스 가능 지역인지 확인합니다
			ArrayList<String> phones;
			
			ArrayList<String> areaDatas = mapper.getAvailableArea();
			if(!areaDatas.contains(addr)) {
				if(mapper.isAvailableDistrict(new District(districts[0], districts[1], districts[2])) == 0)
					return Constant.ADDRESS_UNAVAILABLE;
				else {
					Integer dcid = mapper.getDistrictId(new District(districts[0], districts[1], districts[2]));
					phones = mapper.getDistrictPhones(dcid);
				}
			} else {
				Integer acid = mapper.getAcidWithAreaData(addr);
				phones = mapper.getPhones(acid);
			}
			
			Integer acid = mapper.getAcidWithAreaData(addr);
			
			if (checkUnavailableDate(acid, orderData.pickup_date, orderData.dropoff_date)) {
				return Constant.DATE_UNAVAILABLE;
			}
						
			mapper.addOrder(orderData);
			orderData.order_number = new SimpleDateFormat("yyMMdd").format(new Date()) + "-" + orderData.oid;
			
			mapper.updateOrderNumber(orderData);
			Integer price = 0;
			
			sendSMS(orderData, fullAddr, phones);
			
			for (Item item : orderData.item) {
				price = mapper.getItemPrice(item.item_code);
				mapper.addItem(new ItemData(orderData.oid, item.item_code, price, item.count));
			}
			
			for (Integer cpid : orderData.cpid) {
				mapper.updateCoupon(orderData.uid, orderData.oid, cpid);
			}
		} catch (Exception e) {
			e.printStackTrace();

			return Constant.ERROR;
		}
		
		return Constant.SUCCESS;
	}

	private void sendSMS(OrderData orderData, String fullAddr, ArrayList<String> phones) {			
		Set set = new Set();
		set.setTo(phones.toArray(new String[phones.size()])); // 받는사람 번호
		set.setFrom("07075521385"); // 보내는 사람 번호
		
		String stuff = "주문번호:" + orderData.order_number + "/수거:" + orderData.pickup_date + "/배달:" + orderData.dropoff_date + "/주소:" + fullAddr + "/연락처:" + orderData.phone + "/총:" + orderData.price + "/품목:";
		
		if(stuff.length() > 80) {
			set.setType("LMS");
		}
		
		for(Item item : orderData.item) {
			String name = mapper.getItemName(item.item_code);
			stuff = stuff + name + "=" + item.count + ",";
		}
		
		if(!orderData.memo.equals(""))
			stuff = stuff + "/메모:" + orderData.memo;
	
		set.setText(stuff); // 문자내용 SMS(90바이트), LMS(장문 2,000바이트), MMS(장문+이미지)
	
		new SendSMS(set).run();
	}

	private void sendNewSMS(Order order, String fullAddr, ArrayList<String> phones) {			
		Set set = new Set();
		set.setTo(phones.toArray(new String[phones.size()])); // 받는사람 번호
		set.setFrom("07075521385"); // 보내는 사람 번호
		
		String stuff = "주문번호:" + order.order_number + "/수거:" + order.pickup_date + "/배달:" + order.dropoff_date + "/주소:" + fullAddr + "/연락처:" + order.phone + "/총:" + order.price + "/품목:";
		
		if(stuff.length() > 80) {
			set.setType("LMS");
		}
		
		for(Item item : order.item) {
			String name = mapper.getItemName(item.item_code);
			stuff = stuff + name + "=" + item.count + ",";
		}
		
		if(!order.memo.equals(""))
			stuff = stuff + "/메모:" + order.memo;
	
		set.setText(stuff); // 문자내용 SMS(90바이트), LMS(장문 2,000바이트), MMS(장문+이미지)
	
		new SendSMS(set).run();
	}
	
	private Boolean checkUnavailableDate(Integer acid, String pickup_date, String dropoff_date) {
		// 오늘이 수거배달 제한일인지 확인합니다
		ArrayList<String> areaDateDatas = mapper.getAvailableAreaDateDatas(acid);
		
		try {
			String pickup_date_split = pickup_date.split(" ")[0];
			String dropoff_date_split = dropoff_date.split(" ")[0];
	
			for(String areaDateData : areaDateDatas) {			
				if(areaDateData.startsWith(pickup_date_split) || areaDateData.startsWith(dropoff_date_split))
					return true;
			}
		} catch(Exception e) {
			return false;
		}
		
		return false;
	}

	private Boolean checkUnavailableDistricDate(Integer dcid, String pickup_date, String dropoff_date) {
		// 오늘이 수거배달 제한일인지 확인합니다
		ArrayList<String> areaDateDatas = mapper.getAvailableDistrictDateDatas(dcid);
		
		try {
			String pickup_date_split = pickup_date.split(" ")[0];
			String dropoff_date_split = dropoff_date.split(" ")[0];
	
			for(String areaDateData : areaDateDatas) {			
				if(areaDateData.startsWith(pickup_date_split) || areaDateData.startsWith(dropoff_date_split))
					return true;
			}
		} catch(Exception e) {
			return false;
		}
		
		return false;
	}
		
	private Boolean priceCheck(OrderData orderData, Integer uid) {
		Integer sumPrice = 0;
		Integer couponPrice = 0;
		Integer dropoffPrice = 2000;
		for (Item item : orderData.item) {
			sumPrice += mapper.getItemPrice(item.item_code) * item.count;
		}
		if (sumPrice >= 20000) {
			dropoffPrice = 0;
		}
		if (orderData.cpid.size() != 0) {
			couponPrice = mapper.getCouponPrice(uid, orderData.cpid.get(0));
			if (couponPrice == null) {
				return false;
			} else if (sumPrice < 10000) {
				return false;
			}
		}
		if (!String.valueOf(orderData.dropoff_price).equals(String.valueOf(dropoffPrice))) {
			return false;
		}
		if (!String.valueOf(orderData.price).equals(String.valueOf(sumPrice + dropoffPrice - couponPrice))) {
			return false;
		}
		
		return true;
	}

	public Boolean priceCheck(Order orderData, Integer uid) {
		Integer sumPrice = 0;
		Integer couponPrice = 0;
		Integer dropoffPrice = 2000;
		Integer mileage = orderData.mileage;
		
		try {
			for (Item item : orderData.item) {
				Integer item_price;
				item_price = (int) (mapper.getItemPrice(item.item_code) * (1 - item.discount_rate));
				sumPrice += item_price * item.count;
			}

			if (sumPrice >= 20000) {
				dropoffPrice = 0;
			}

			if (orderData.coupon.size() != 0) {
				couponPrice = mapper.getCouponPrice(uid, orderData.coupon.get(0).cpid);
				if (couponPrice == null) {
					return false;
				} else if (sumPrice < 10000) {
					return false;
				}
			}

			if (mapper.isAuthUser(uid) > 0) {
				if (mileage > mapper.getMileage(uid)) 
					return false;
			}

			if (!String.valueOf(orderData.dropoff_price).equals(String.valueOf(dropoffPrice))) {
				return false;
			}

			if (!String.valueOf(orderData.price).equals(String.valueOf(sumPrice + dropoffPrice - couponPrice - mileage))) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public Integer delOrder(OrderData orderData, Integer uid) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		orderData.uid = uid;
		try {
			Integer state = mapper.getOrderState(orderData.oid, uid);
			if (state == null) {
				return Constant.ERROR;
			} else if (state >= 1) {
				return Constant.IMPOSSIBLE;
			} else {
				if (mapper.selectMileage(orderData.oid, uid, 1) != null) {
					int addMileage = mapper.selectMileage(orderData.oid, uid, 1);
					int totalMileage = mapper.getMileage(uid);
					mapper.updateMileageByUser(uid, totalMileage + addMileage);
					mapper.deleteMileageUsedCancel(orderData.oid, uid, 1);
				}
				
				sendDeleteSMS(orderData, uid);

				mapper.updateCouponUsedCancel(orderData.oid, uid);
				mapper.delOrder(orderData.oid, uid);				
			}
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}

		return Constant.SUCCESS;
	}

	
	public Integer delNewOrder(Order orderData, Integer uid) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		orderData.uid = uid;
		try {
			Integer state = mapper.getOrderState(orderData.oid, uid);
			if (state == null) {
				return Constant.ERROR;
			} else if (state >= 2) {
				return Constant.IMPOSSIBLE;
			} else {
				if (mapper.selectMileage(orderData.oid, uid, 1) != null) {
					int addMileage = mapper.selectMileage(orderData.oid, uid, 1);
					int totalMileage = mapper.getMileage(uid);
					mapper.updateMileageByUser(uid, totalMileage + addMileage);
					mapper.deleteMileageUsedCancel(orderData.oid, uid, 1);
				}
				
				sendNewDeleteSMS(orderData, uid);
				
				mapper.updateCouponUsedCancel(orderData.oid, uid);
				mapper.delOrder(orderData.oid, uid);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}

		return Constant.SUCCESS;
	}

	
	private void addPush(int uid, int oid, String msg, int value, int type) {
		Sender sender = new Sender("AIzaSyClOmdKk3R8N1-gAoifS2gBijqMf4wjLGI");
		String regId = mapper.getRegid(uid);
		Message message = new Message.Builder()
		.addData("oid", String.valueOf(oid))
		.addData("uid", String.valueOf(uid))
		.addData("message", msg)
		.addData("type", String.valueOf(type))
		.addData("value", String.valueOf(value)).build();
		List<String> list = new ArrayList<String>();
		list.add(regId);
		MulticastResult multiResult;
		try {
			multiResult = sender.send(message, list, 3);
			if (multiResult != null) {
				List<Result> resultList = multiResult.getResults();
				for (Result result : resultList) {
					log.debug("Result : " + result.getMessageId());
				}
		}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendNewDeleteSMS(Order order, Integer uid) {
		String address = mapper.getAddressForOrderId(order.oid);
		District districtObject = makeDistrict(address);

		int dcid = 0;
		
		if (mapper.isAvailableDistrictWithNull(districtObject) == 0) {
			if (mapper.isAvailableDistrict(districtObject) != 0) {
				dcid = mapper.getDistrictId(districtObject);
			}
		} else 
			dcid = mapper.getDistrictIdWithNull(districtObject);
		
		ArrayList<String> phones = mapper.getDistrictPhones(dcid);
				
		OrderData orderData = mapper.getOrderForSingle(order.oid);
		
		Set set = new Set();
		set.setTo(phones.toArray(new String[phones.size()])); // 받는사람 번호
		set.setFrom("07075521385"); // 보내는 사람 번호
		
		String stuff = "[주문취소]주문번호:" + orderData.order_number + "/수거:" + orderData.pickup_date + "/배달:" + orderData.dropoff_date;
	
		set.setText(stuff); // 문자내용 SMS(90바이트), LMS(장문 2,000바이트), MMS(장문+이미지)	
		
		new SendSMS(set).run();
	}
	
	private void sendDeleteSMS(OrderData orderData, Integer uid) {
		OrderData order = mapper.getOrderForSingle(orderData.oid);
		String district = order.address;
		String[] districts = district.split(" ");
		String addr = districts[0] + " " + districts[1];
		
		Integer acid = mapper.getAcidWithAreaData(addr);		
		ArrayList<String> phones = mapper.getPhones(acid);
				
		Set set = new Set();
		set.setTo(phones.toArray(new String[phones.size()])); // 받는사람 번호
		set.setFrom("07075521385"); // 보내는 사람 번호
		
		String stuff = "[주문취소]주문번호:" + order.order_number + "/수거:" + order.pickup_date + "/배달:" + order.dropoff_date;
	
		set.setText(stuff); // 문자내용 SMS(90바이트), LMS(장문 2,000바이트), MMS(장문+이미지)	
		
		new SendSMS(set).run();
	}

	public Member getMember(String email) {
		Member member = mapper.getMember(email);
		member.address = mapper.getAddress(member.uid);
		return member;
	}

	public ArrayList<ItemCode> getItemCode() {
		return mapper.getItemCode();
	}

	public ItemInfo getItemInfo() {
		ItemInfo itemInfo = new ItemInfo();
		itemInfo.categories = mapper.getCategory();
		itemInfo.orderItems = mapper.getItemCode();
		
		return itemInfo;
	}

	public Integer getMileage(int uid) {
		if (mapper.isAuthUser(uid) == 0)
			return 0;
		
		return mapper.getMileage(uid);
	}
	
	public ArrayList<Coupon> getAvailableCoupon(Integer uid) {
		return mapper.getAvailableCoupon(uid);
	}

	public ArrayList<Order> getOrder(Integer uid) {
		ArrayList<Order> orders = mapper.getOrder(uid);
		for (Order order : orders) {
			order.pickupInfo = mapper.getDeliverer(order.pickup_man);
			order.dropoffInfo = mapper.getDeliverer(order.dropoff_man);
			order.item = mapper.getItem(order.oid);
			order.coupon = mapper.getCoupon(order.oid, uid);
			
			if (mapper.selectMileage(order.oid, uid, 1) != null) {
				int mileage = mapper.selectMileage(order.oid, uid, 1);
				if (mileage > 0)
					order.mileage = mileage;
			}
		}
		
		return orders;
	}

	public ArrayList<Item> getItem(Integer oid) {
		return mapper.getItem(oid);
	}

	public ArrayList<Coupon> getCoupon(Integer oid, Integer uid) {
		return mapper.getCoupon(oid, uid);
	}

	public String getNote(Integer oid, Integer uid) {
		String note = mapper.getNote(oid, uid);
		note = note == null ? "" : note;
		
		return note;
	}

	public Boolean updateNote(Integer oid, Integer uid, String note) {
		try {
			mapper.updateNote(oid, uid, note);
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}

	public ArrayList<Address> getAddress(Integer uid) {
		return mapper.getAddress(uid);
	}

	public Boolean updateMemberPhone(UserData userData) {
		try {
			return mapper.updatePhoneByEmail(userData);
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}

	public Boolean updateMemberPassword(UserData userData) {
		try {
			return mapper.updatePassword(userData.email, userData.password);
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	public Boolean updateMemberNewPassword(PassData passData) {
		try {
			if (mapper.accountCheck(passData.email, passData.current_password) == 0)
				return false;
			return mapper.updatePassword(passData.email, passData.password);
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}

	public Integer passwordInquiry(UserData userData, EmailService emailService, EmailData email) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		try {
			if (mapper.getUid(userData.email) == null) {
				return 3;
			}
			userData.password = RandomNumber.GetPassword(5);
			mapper.updatePassword(userData.email, userData.password);
			email.setReceiver(userData.email);
			email.setContent(userData.password);
			if (!emailService.sendEmail(email)) {
				return 2;
			}
			// platformTransactionManager.commit(status);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Boolean updateMemberAddress(Address address, Integer uid) {
		try {
			address.uid = uid;
			return mapper.updateMemberAddress(address);
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}

	public ArrayList<DelivererWork> getPickupRequest(Integer delivererUid) {
		ArrayList<DelivererWork> delivererWorkLists = mapper.getPickupRequest(delivererUid);
		for (DelivererWork delivererWorkList : delivererWorkLists) {
			delivererWorkList.item = mapper.getItem(delivererWorkList.oid);
			delivererWorkList.coupon = mapper.getCoupon(delivererWorkList.oid, delivererWorkList.uid);
		}
		return delivererWorkLists;
	}
	
	public ArrayList<DelivererWork> getDeliveryRequest(Integer delivererUid) {
		ArrayList<DelivererWork> delivererWorkLists = mapper.getDeliveryRequest(delivererUid);
		for (DelivererWork delivererWorkList : delivererWorkLists) {
			delivererWorkList.item = mapper.getItem(delivererWorkList.oid);
			delivererWorkList.coupon = mapper.getCoupon(delivererWorkList.oid, delivererWorkList.uid);
		}
		return delivererWorkLists;
	}

	public Boolean updatePickupRequestComplete(Integer oid, String note) {
		try {
			return mapper.updatePickupRequestComplete(oid, note);
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}

	public Boolean updateDeliveryRequestComplete(Integer uid, int oid, String note, String payment_method) {
		try {
			OrderData orderData = getOrderByOrderId(oid);
			int userId = orderData.uid;
									
			if (mapper.isAuthUser(userId) == 1 && mapper.checkMileage(uid, oid) == 0) {
				if (mapper.selectRate(oid) == 0)
					addPush(userId, oid, null, 0, Notification.FEEDBACK_ALARM);
				
				addMileage(userId, oid, (int) (orderData.price * getAccumulationRate(userId)));
				addTotal(userId, orderData.price);
				addPush(userId, oid, null, (int) (orderData.price * getAccumulationRate(userId)), Notification.MILEAGE_ALARM);
			}
			
			return mapper.updateDeliveryRequestComplete(oid, note, payment_method);
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}

	private OrderData getOrderByOrderId(int oid) {
		return mapper.getOrderForSingle(oid);
	}

	public Boolean addRecommendationCoupon(Integer uid, StringBuilder serial_number) {
		try {
			Integer coupon_code = mapper.getCouponCode(1);
			String random = null;
			while (true) {
				random = RandomNumber.GetRandom(8);
				if (mapper.getCouponSerialNumberExist(random) == null) {
					break;
				}
			}
			if (mapper.getCouponIssueCheck(coupon_code, uid) != 0) {
				String serial_number_now = mapper.getSerialNumberIssueCheck(coupon_code, uid);
				if (serial_number_now == null) {
					mapper.updateRecommendationCoupon(coupon_code, uid, random);
					serial_number.append(random);
				} else {
					serial_number.append(serial_number_now);
				}
				return null;
			}
			mapper.addRecommendationCoupon(new CouponData(uid, coupon_code, mapper.getCouponValue(coupon_code), null, null, random, false));
			serial_number.append(random);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Integer recommendationCouponIssue(Integer uid, String serial_number) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		try {
			Integer coupon_code = mapper.getCouponCode(1);
			Integer serialNumberUid = mapper.getCouponSerialNumberExist(serial_number);
			if (serialNumberUid != null) {
				if (serialNumberUid != uid) {
					if (mapper.getCouponIssueCheck(coupon_code, uid) != 0) {
						return 2;
					}
					mapper.addRecommendationCoupon(new CouponData(uid, coupon_code, mapper.getCouponValue(coupon_code), null, null, null, true));
					// platformTransactionManager.commit(status);
					return 1;
				} else {
					if (mapper.getRecommendationCouponEnabledCount(coupon_code, serial_number) == 0) {
						mapper.updateRecommendationCouponEnable(coupon_code, serial_number);
						// platformTransactionManager.commit(status);
						return 1;
					}
					return 2;
				}
			} else {
				return 3;
			}
		} catch (Exception e) {
			// platformTransactionManager.rollback(status);
			e.printStackTrace();
			return 0;
		}
	}

	public Boolean couponSerialNumberCheck(String serial_number) {
		if (mapper.getCouponSerialNumberExist(serial_number) != null) {
			return true;
		}
		return false;
	}

	public OrderState getOrderState() {
		OrderState orderState = new OrderState();
		orderState.stateData = mapper.getOrderStateData();
		for (OrderStateData data : orderState.stateData) {
			data.count = mapper.getSumCountForItem(data.oid);
		}
		orderState.complete = mapper.getOrderStateCompleteCount();
		orderState.incomplete = mapper.getOrderStateIncompleteCount();
		return orderState;
	}

	public OrderState getOrderStateSearch(String search) {
		OrderState orderState = new OrderState();
		orderState.stateData = mapper.getOrderStateDataSearch("%" + search + "%");
		Integer complete = 0;
		Integer incomplete = 0;
		for (OrderStateData data : orderState.stateData) {
			data.count = mapper.getSumCountForItem(data.oid);
			if (data.state == 4) {
				complete++;
			} else if (data.state != 4) {
				incomplete++;
			}
		}
		orderState.complete = complete;
		orderState.incomplete = incomplete;
		return orderState;
	}

	public PickupState getPickupState() {
		PickupState pickupState = new PickupState();
		pickupState.stateData = mapper.getPickupStateData();
		for (PickupStateData data : pickupState.stateData) {
			data.count = mapper.getSumCountForItem(data.oid);
		}
		pickupState.complete = mapper.getPickupStateCompleteCount();
		pickupState.incomplete = mapper.getPickupStateIncompleteCount();
		return pickupState;
	}

	public Boolean setPickupMan(PickupStateData pickupStateData) {
		return mapper.setPickupMan(pickupStateData);
	}

	public DropoffState getDropoffState() {
		DropoffState dropoffState = new DropoffState();
		dropoffState.stateData = mapper.getDropoffStateData();
		for (DropoffStateData data : dropoffState.stateData) {
			data.count = mapper.getSumCountForItem(data.oid);
		}
		dropoffState.complete = mapper.getDropoffStateCompleteCount();
		dropoffState.incomplete = mapper.getDropoffStateIncompleteCount();
		return dropoffState;
	}

	public Boolean setDropoffMan(DropoffStateData dropoffStateData) {
		return mapper.setDropoffMan(dropoffStateData);
	}

	public ArrayList<Deliverer> getDelivererAll() {
		return mapper.getDelivererAll();
	}

	public ArrayList<MemberInfo> getMemberInfo() {
		ArrayList<MemberInfo> memberInfos = mapper.getMemberInfo();
		for (MemberInfo memberInfo : memberInfos) {
			Integer accruePrice = mapper.getTotalPrice(memberInfo.uid);
			memberInfo.accruePrice = accruePrice != null ? accruePrice : 0;
		}
		return memberInfos;
	}

	public ArrayList<MemberInfo> getMemberInfoSearch(String search) {
		ArrayList<MemberInfo> memberInfos = mapper.getMemberInfoSearch("%" + search + "%");
		for (MemberInfo memberInfo : memberInfos) {
			Integer accruePrice = mapper.getTotalPrice(memberInfo.uid);
			memberInfo.accruePrice = accruePrice != null ? accruePrice : 0;
		}
		return memberInfos;
	}

	public ArrayList<MemberInfo> getMemberInfoAppend(Integer uid) {
		ArrayList<MemberInfo> memberInfos = mapper.getMemberInfoAppend(uid);
		for (MemberInfo memberInfo : memberInfos) {
			Integer accruePrice = mapper.getTotalPrice(memberInfo.uid);
			memberInfo.accruePrice = accruePrice != null ? accruePrice : 0;
		}
		return memberInfos;
	}

	public ArrayList<MemberInfo> getMemberInfoAppendSearch(String search, Integer uid) {
		ArrayList<MemberInfo> memberInfos = mapper.getMemberInfoAppendSearch("%" + search + "%", uid);
		for (MemberInfo memberInfo : memberInfos) {
			Integer accruePrice = mapper.getTotalPrice(memberInfo.uid);
			memberInfo.accruePrice = accruePrice != null ? accruePrice : 0;
		}
		return memberInfos;
	}

	public ArrayList<DelivererInfo> getDelivererInfo() {
		return mapper.getDelivererInfo();
	}

	public Boolean updateDelivererEnabled(Integer uid, Boolean enabled) {
		try {
			mapper.updateDelivererEnabled(uid, enabled);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ArrayList<MemberOrderInfo> getMemberOrderInfo(Integer uid) {
		ArrayList<MemberOrderInfo> memberOrderInfos = mapper.getMemberOrderInfo(uid);
		for (MemberOrderInfo memberOrderInfo : memberOrderInfos) {
			memberOrderInfo.coupon = mapper.getCoupon(memberOrderInfo.oid, uid);
		}
		return memberOrderInfos;
	}

	public Integer getLatestOrderId() {
		return mapper.getLatestOrderId();
	}
	
	public Integer getLatestOrderIdByUid(int uid) {
		return mapper.getLatestOrderIdByUid(uid);
	}
	
	public Boolean insertArea(String areacode, String area) {
		return mapper.insertArea(new AreaData(areacode, area));
	}
	
	public Boolean insertAreaDate(Integer acid, String area_date) {
		return mapper.insertAreaDate(new AreaDateData(acid, area_date));
	}
	
	public ArrayList<Area> getAvailableAreaDatas() {
		ArrayList<Area> areaDatas = mapper.getAvailableAreaDatas();
		
		return areaDatas;
	}
	
	public ArrayList<String> getAvailableAreaDateDatas(Integer acid) {
		ArrayList<String> areaDateDatas = mapper.getAvailableAreaDateDatas(acid);
		
		return areaDateDatas;
	}
	
	public ArrayList<AreaDate> getAvailableAreaDate(Integer acid) {
		ArrayList<AreaDate> areaDateDatas = mapper.getAvailableAreaDate(acid);
		
		return areaDateDatas;
	}
	
	public Boolean deleteArea(int acid) {		
		mapper.delAllAreaDate(acid); 
		mapper.delAllAreaAlarm(acid);
		
		return mapper.delArea(acid);
	}

	public Boolean deleteAreaDate(int adid) {
		return mapper.delAreaDate(adid);
	}

	public Boolean insertAreaAlarm(int acid, String phone) {
		return mapper.insertAreaAlarm(new AreaAlarmData(acid, phone));
	}

	public AppInfo getAppInfo() {
		return mapper.getAppInfo();
	}

	public ArrayList<Notice> getNotice() {
		return mapper.getNotice();
	}

	public Integer addAuthUser(AuthUser authUser, Integer uid) {
		try {
			String getCode = mapper.getAuthorizationCode(uid);
			Date codeDate = mapper.getAuthorizationDate(uid);
			
			if ((System.currentTimeMillis() - codeDate.getTime()) >= 180000)	
				return Constant.AUTH_CODE_TIME;
				
			if (!authUser.code.equals(getCode))
				return Constant.AUTH_CODE_INVALID;
			
			if (mapper.selectEmailAuthUser(authUser.email) > 0)
				return Constant.ACCOUNT_DUPLICATION;

			if (mapper.selectPhoneAuthUser(authUser.phone) > 0)
				return Constant.DUPLICATION;
			
			String random = null;
			while (true) {
				random = RandomNumber.GetRandom(5);
				if (mapper.getAuthorizationCodeExist(random) == null) {
					break;
				}
			}
			
			authUser.code = random;
			
			if (mapper.getCountOfOrder(uid) > 0 && null != mapper.getTotalGrossOfUser(uid)) {
				int initialMileage = (int) (mapper.getTotalGrossOfUser(uid) * 0.01);				
				authUser.total = mapper.getTotalGrossOfUser(uid);
				authUser.mileage = initialMileage;
				mapper.addUseOfMileage(uid, null, 0, initialMileage);
			}
			else 
				authUser.total = 0;
						
			authUser.uid = uid;
			
			if (mapper.addAuthUser(authUser))  {
				addCouponForRegister(uid);
				
				updateUser(uid);
				
				return Constant.SUCCESS;
			}
			else
				return Constant.ERROR;
		} catch (Exception e) {
			return Constant.ERROR;
		}
	}
	
//	public Integer addAuthUserWithRegister(AuthUser authUser, Integer uid) {
//		try {
//			if (mapper.addAuthUser(authUser))  {
//				addCouponForRegister(uid);
//				
//				updateUser(uid);
//				
//				return Constant.SUCCESS;
//			}
//			else
//				return Constant.ERROR;
//		} catch (Exception e) {
//			return Constant.ERROR;
//		}
//	}

	private void addCouponForRegister(int uid) { 
		ArrayList<CouponCodeData> codeDatas = mapper.getOrderCoupon();
		for (CouponCodeData data : codeDatas) {
			mapper.addCoupon(new CouponData(uid, data.coupon_code, data.value, null, null));
			addPush(uid, 0, null, data.value, Notification.COUPON_ALARM);
		}
	}
	
	public AuthUser isAuthUser(Integer uid) {		
		return mapper.getAuthUser(uid);
	}

	public Integer addRate(Feedback feedback, Integer uid) {
		feedback.uid = uid;
		
		try {
			mapper.addRate(feedback);
		} catch (Exception e) {
			return Constant.ERROR;
		}
		
		return Constant.SUCCESS;
	}

	public Boolean getAuthorizationCode(Integer uid, String phone) {
		String code = RandomNumber.GetAuthCode(4);
		Boolean success = mapper.generateCode(code, uid);
		
		if (success) {
			sendCodeSMS(code, phone);
			return true;
		}
		
		return false;
	}
	
	private void sendCodeSMS(String code, String phone) {			
		Set set = new Set();
		set.setTo(phone); // 받는사람 번호
		set.setFrom("07075521385"); // 보내는 사람 번호
		set.setText("Clean Basket에서 보낸 인증 번호 [" + code + "]"); 
				
		new SendSMS(set).run();
	}

	public Integer modifyOrderItem(Order order, Integer uid) {
		System.out.println(order.price + " / " + order.dropoff_price);
		
		if (order.state > 1)
			return Constant.ERROR;
		
		try {			
			if (!mapper.deleteOrderItem(order.oid))
				return Constant.ERROR;
			
			for (Item item : order.item) {
				int price = mapper.getItemPrice(item.item_code);
				mapper.addItem(new ItemData(order.oid, item.item_code, price, item.count));
			}
			
			ArrayList<Coupon> coupons = mapper.getCoupon(order.oid, uid);
			for (Coupon c : coupons) {
				order.price = order.price - c.value;
			}
			
			if (mapper.isAuthUser(uid) > 0) {
				int mileage = mapper.getMileageByOid(uid, order.oid);
				order.price = order.price - mileage;
			}
			
			if (!mapper.updateOrderData(order))
				return Constant.ERROR;
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return Constant.ERROR;
		}
		
		return Constant.SUCCESS;
	}

	public Integer modifyOrderDate(Order order, Integer uid) {		
		if (!mapper.updateOrderDateTime(order))
			return Constant.ERROR;
		
		sendModifyDateSMS(order);
		
		return Constant.SUCCESS;
	}
	
	private void sendModifyDateSMS(Order order) {
		String address = mapper.getAddressForOrderId(order.oid);
		District districtObject = makeDistrict(address);

		int dcid = 0;
		
		if (mapper.isAvailableDistrictWithNull(districtObject) == 0) {
			if (mapper.isAvailableDistrict(districtObject) != 0) {
				dcid = mapper.getDistrictId(districtObject);
			}
		} else 
			dcid = mapper.getDistrictIdWithNull(districtObject);
		
		ArrayList<String> phones = mapper.getDistrictPhones(dcid);
				
		Set set = new Set();
		set.setTo(phones.toArray(new String[phones.size()])); // 받는사람 번호
		set.setFrom("07075521385"); // 보내는 사람 번호
		
		String stuff = "[주문변경]주문번호:" + order.order_number + "/수거:" + order.pickup_date + "/배달:" + order.dropoff_date;
	
		set.setText(stuff); // 문자내용 SMS(90바이트), LMS(장문 2,000바이트), MMS(장문+이미지)	
		
		new SendSMS(set).run();
	}

	private void sendConfirmSMS(int oid) {
		String address = mapper.getAddressForOrderId(oid);
		District districtObject = makeDistrict(address);

		OrderData order = mapper.getOrderForSingle(oid);
		
		int dcid = 0;
		
		if (mapper.isAvailableDistrictWithNull(districtObject) == 0) {
			if (mapper.isAvailableDistrict(districtObject) != 0) {
				dcid = mapper.getDistrictId(districtObject);
			}
		} else 
			dcid = mapper.getDistrictIdWithNull(districtObject);
		
		ArrayList<String> phones = mapper.getDistrictPhones(dcid);
				
		Set set = new Set();
		set.setTo(phones.toArray(new String[phones.size()])); // 받는사람 번호
		set.setFrom("07075521385"); // 보내는 사람 번호
		
		String stuff = "[주문시간확정]주문번호:" + order.order_number + "/수거:" + order.pickup_date + "/배달:" + order.dropoff_date;
	
		set.setText(stuff); // 문자내용 SMS(90바이트), LMS(장문 2,000바이트), MMS(장문+이미지)	
		
		new SendSMS(set).run();
	}
	
	public Integer confirmRate(int oid) {		
		if (mapper.selectRate(oid) > 0)
			return Constant.DUPLICATION_FEEDBACK;
		
		return Constant.SUCCESS;
	}

	public Integer confirmOrder(Integer oid) {
		sendConfirmSMS(oid);
		
		return Constant.SUCCESS;
	}

	public ArrayList<District> getDistricts() {
		return mapper.getDistricts();
	}

}
