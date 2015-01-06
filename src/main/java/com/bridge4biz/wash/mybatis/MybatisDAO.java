package com.bridge4biz.wash.mybatis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.bridge4biz.wash.data.AddressData;
import com.bridge4biz.wash.data.CouponCodeData;
import com.bridge4biz.wash.data.CouponData;
import com.bridge4biz.wash.data.DropoffStateData;
import com.bridge4biz.wash.data.ItemData;
import com.bridge4biz.wash.data.OrderData;
import com.bridge4biz.wash.data.OrderStateData;
import com.bridge4biz.wash.data.PickupStateData;
import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.service.Address;
import com.bridge4biz.wash.service.Coupon;
import com.bridge4biz.wash.service.Deliverer;
import com.bridge4biz.wash.service.DelivererInfo;
import com.bridge4biz.wash.service.DelivererWork;
import com.bridge4biz.wash.service.DropoffState;
import com.bridge4biz.wash.service.Item;
import com.bridge4biz.wash.service.ItemCode;
import com.bridge4biz.wash.service.Member;
import com.bridge4biz.wash.service.MemberInfo;
import com.bridge4biz.wash.service.MemberOrderInfo;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.service.OrderState;
import com.bridge4biz.wash.service.PickupState;
import com.bridge4biz.wash.util.Constant;
import com.bridge4biz.wash.util.EmailData;
import com.bridge4biz.wash.util.EmailService;
import com.bridge4biz.wash.util.RandomNumber;
import com.google.gson.Gson;

public class MybatisDAO {

	private MybatisMapper mapper;
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private MybatisDAO(MybatisMapper mapper, PlatformTransactionManager platformTransactionManager) {
		this.mapper = mapper;
		this.platformTransactionManager = platformTransactionManager;
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
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
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

	public Integer addUserForMember(UserData userData) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
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

	public Integer addUserForDeliverer(UserData userData, MultipartFile file) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		if (mapper.getUid(userData.email) != null) {
			return Constant.ACCOUNT_DUPLICATION;
		}
		try {
			mapper.addUser(userData);
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}
		try {
			userData.img = "images/deliverer/" + userData.uid + ".jpg";
			File fileImage = new File(Constant.PATH + userData.img);
			BufferedImage buffer = ImageIO.read(file.getInputStream());
			ImageIO.write(buffer, "jpg", fileImage);
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.IMAGE_WRITE_ERROR;
		}
		try {
			mapper.updateImageForUser(userData);
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}
		// platformTransactionManager.commit(status);
		return Constant.SUCCESS;
	}

	public Integer addOrder(OrderData orderData, Integer uid) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);

		System.out.println(new Gson().toJson(orderData));

		if (priceCheck(orderData, uid) == false) {
			return Constant.ERROR;
		}
		
		try {
			orderData.uid = uid;
			Address address = mapper.getAddressForSingle(orderData.adrid, orderData.uid);
			orderData.address = address.address;
			orderData.addr_number = address.addr_number;
			orderData.addr_building = address.addr_building;
			orderData.addr_remainder = address.addr_remainder;
			mapper.addOrder(orderData);
			orderData.order_number = new SimpleDateFormat("yyMMdd").format(new Date()) + "-" + orderData.oid;
			mapper.updateOrderNumber(orderData);
			Integer price = 0;
			for (Item item : orderData.item) {
				price = mapper.getItemPrice(item.item_code);
				mapper.addItem(new ItemData(orderData.oid, item.item_code, price, item.count));
			}
			for (Integer cpid : orderData.cpid) {
				mapper.updateCoupon(orderData.uid, orderData.oid, cpid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}
		// platformTransactionManager.commit(status);
		return Constant.SUCCESS;
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

	public Integer delOrder(OrderData orderData, Integer uid) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
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
				mapper.updateCouponUsedCancel(orderData.oid, uid);
				mapper.delOrder(orderData.oid, uid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return Constant.ERROR;
		}
		// platformTransactionManager.commit(status);
		return Constant.SUCCESS;
	}

	public Member getMember(String email) {
		Member member = mapper.getMember(email);
		member.address = mapper.getAddress(member.uid);
		return member;
	}

	public ArrayList<ItemCode> getItemCode() {
		return mapper.getItemCode();
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

	public Boolean updateMemberPasswrod(UserData userData) {
		try {
			return mapper.updatePassword(userData.email, userData.password);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Integer passwordInquiry(UserData userData, EmailService emailService, EmailData email) {
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
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
				// platformTransactionManager.rollback(status);
				return 2;
			}
			// platformTransactionManager.commit(status);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
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

	public Boolean updateDeliveryRequestComplete(Integer oid, String note) {
		try {
			return mapper.updateDeliveryRequestComplete(oid, note);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
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

}
