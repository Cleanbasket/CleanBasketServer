package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

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
import com.bridge4biz.wash.data.PickupStateData;
import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.service.Address;
import com.bridge4biz.wash.service.AppInfo;
import com.bridge4biz.wash.service.Area;
import com.bridge4biz.wash.service.AreaDate;
import com.bridge4biz.wash.service.AuthUser;
import com.bridge4biz.wash.service.Category;
import com.bridge4biz.wash.service.Coupon;
import com.bridge4biz.wash.service.Deliverer;
import com.bridge4biz.wash.service.DelivererInfo;
import com.bridge4biz.wash.service.DelivererWork;
import com.bridge4biz.wash.service.District;
import com.bridge4biz.wash.service.Feedback;
import com.bridge4biz.wash.service.Item;
import com.bridge4biz.wash.service.ItemCode;
import com.bridge4biz.wash.service.ItemInfo;
import com.bridge4biz.wash.service.Member;
import com.bridge4biz.wash.service.MemberInfo;
import com.bridge4biz.wash.service.MemberOrderInfo;
import com.bridge4biz.wash.service.Notice;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.service.OrderItem;
import com.bridge4biz.wash.service.OrderItemInfo;
import com.bridge4biz.wash.service.PickupTime;

public interface MybatisMapper {
	@Select("SELECT 1")
	Integer selectTest();

	@Update("UPDATE areacode SET areacode = '50' WHERE acid = 1")
	Boolean transactionTest();

	@Select("SHOW PROCESSLIST")
	ArrayList<Map<String, Object>> getProcessList();

	@Select("SELECT uid FROM user WHERE email = #{email}")
	Integer getUid(@Param("email") String email);

	@Select("SELECT authority FROM user WHERE email = #{email}")
	String getAuthority(@Param("email") String email);

	@Select("SELECT COUNT(*) FROM user WHERE email = #{email}")
	Integer emailCheck(@Param("email") String email);

	@Select("SELECT COUNT(*) FROM user WHERE email = #{email} AND enabled = 1")
	Integer emailCheckIsEnabled(@Param("email") String email);

	@Select("SELECT COUNT(*) FROM user WHERE email = #{email} AND password = SHA(#{password})")
	Integer accountCheck(@Param("email") String email, @Param("password") String password);

	@Select("SELECT COUNT(*) FROM user WHERE email = #{email} AND password = #{password}")
	Integer accountNewCheck(@Param("email") String email, @Param("password") String password);
	
	@Select("SELECT uid, email, phone FROM user WHERE email = #{email}")
	Member getMember(@Param("email") String email);

	@Select("SELECT uid, email, name, phone, img, birthday FROM user WHERE uid = #{uid}")
	Deliverer getDeliverer(@Param("uid") Integer uid);

	@Select("SELECT uid, email, name, phone, img, birthday FROM user WHERE authority = 'ROLE_DELIVERER' AND enabled = 1")
	ArrayList<Deliverer> getDelivererAll();

	@Select("SELECT phone FROM user WHERE uid = #{uid}")
	String getPhone(@Param("uid") Integer uid);

	@Select("SELECT phone FROM orders WHERE uid = #{oid} LIMIT 1")
	String getPhoneFromOrder(@Param("oid") Integer oid);

	@Select("SELECT address FROM orders WHERE oid = #{oid}")
	String getAddressForOrderId(@Param("oid") Integer oid);

	
	@Select("SELECT * FROM address WHERE adrid = #{adrid} AND uid = #{uid}")
	Address getAddressForSingle(@Param("adrid") Integer adrid, @Param("uid") Integer uid);

	@Select("SELECT * FROM address WHERE uid = #{uid}")
	ArrayList<Address> getAddress(@Param("uid") Integer uid);


	@Select("SELECT COUNT(*) FROM address where uid = #{uid}")
	Integer getNumberOfAddressByUid(@Param("uid") Integer uid);

	@Select("SELECT adrid FROM address where uid = #{uid} AND type = 0")
	Integer getAddressByUid(@Param("uid") Integer uid);
	
	
	@Select("SELECT * FROM coupon_code WHERE kind = 0")
	ArrayList<CouponCodeData> getOrderCoupon();

	@Select("SELECT coupon_code FROM coupon_code WHERE kind = #{kind}")
	Integer getCouponCode(@Param("kind") Integer kind);

	@Select("SELECT COUNT(*) FROM coupon WHERE coupon_code = #{coupon_code} AND uid = #{uid}")
	Integer getCouponIssueCheck(@Param("coupon_code") Integer coupon_code, @Param("uid") Integer uid);

	@Select("SELECT serial_number FROM coupon WHERE coupon_code = #{coupon_code} AND uid = #{uid} AND serial_number is not NULL")
	String getSerialNumberIssueCheck(@Param("coupon_code") Integer coupon_code, @Param("uid") Integer uid);

	
	
	@Select("SELECT A.*, IF(B.item_code IS NULL, 0, A.item_code) AS info FROM item_code A LEFT JOIN item_info B ON A.item_code = B.item_code")
	ArrayList<ItemCode> getItemCode();

	@Select("SELECT cpid, name, descr, type, kind, infinite, A.value, img, A.start_date, A.end_date, A.rdate FROM coupon AS A INNER JOIN coupon_code AS B ON A.coupon_code = B.coupon_code WHERE (uid = #{uid} AND used = 0 AND enabled = 1 AND A.end_date > NOW()) OR (uid = #{uid} AND used = 0 AND enabled = 1 AND infinite = 1)")
	ArrayList<Coupon> getAvailableCoupon(@Param("uid") Integer uid);

	@Select("SELECT oid, pickup_man, dropoff_man, order_number, state, phone, address, addr_number, addr_building, addr_remainder, memo, price, dropoff_price, pickup_date, dropoff_date, rdate FROM orders WHERE uid = #{uid} AND state != 4")
	ArrayList<Order> getOrder(@Param("uid") Integer uid);

	@Select("SELECT oid, pickup_man, dropoff_man, order_number, state, phone, address, addr_number, addr_building, addr_remainder, memo, price, dropoff_price, pickup_date, dropoff_date, payment_method, rdate FROM orders WHERE uid = #{uid} ORDER BY oid DESC")
	ArrayList<Order> getAllOrder(@Param("uid") Integer uid);
	
	@Select("SELECT oid, pickup_man, dropoff_man, order_number, state, phone, address, addr_number, addr_building, addr_remainder, memo, price, dropoff_price, pickup_date, dropoff_date, rdate FROM orders WHERE uid = #{uid} AND  state != 4 ORDER BY oid DESC LIMIT 1")
	ArrayList<Order> getRecentOrder(@Param("uid") Integer uid);
	
	@Select("SELECT oid, uid, order_number, state, phone, address, addr_number, addr_building, addr_remainder, note, memo, price, dropoff_price, pickup_date, dropoff_date, payment_method, rdate FROM orders WHERE (state = 1 AND pickup_man = #{uid}) OR (state = 2 AND rdate >= CURDATE() AND pickup_man = #{uid}) ORDER BY state ASC, pickup_date ASC, oid ASC")
	ArrayList<DelivererWork> getPickupRequest(@Param("uid") Integer uid);

	@Select("SELECT oid, uid, order_number, state, phone, address, addr_number, addr_building, addr_remainder, note, memo, price, dropoff_price, pickup_date, dropoff_date, payment_method, rdate FROM orders WHERE (state = 3 AND dropoff_man = #{uid}) OR (state = 4 AND rdate >= CURDATE() AND dropoff_man = #{uid}) ORDER BY state ASC, dropoff_date ASC, oid ASC")
	ArrayList<DelivererWork> getDeliveryRequest(@Param("uid") Integer uid);

	@Select("SELECT itid, oid, A.item_code, name, descr, A.price, count, img, A.rdate FROM item AS A INNER JOIN item_code AS B ON A.item_code = B.item_code WHERE oid = #{oid}")
	ArrayList<Item> getItem(@Param("oid") Integer oid);

	@Select("SELECT cpid, name, descr, type, kind, infinite, A.value, img, A.start_date, A.end_date, A.rdate FROM coupon AS A INNER JOIN coupon_code AS B ON A.coupon_code = B.coupon_code WHERE oid = #{oid} AND uid = #{uid}")
	ArrayList<Coupon> getCoupon(@Param("oid") Integer oid, @Param("uid") Integer uid);

	@Select("SELECT note FROM orders WHERE oid = #{oid} AND uid = #{uid}")
	String getNote(@Param("oid") Integer oid, @Param("uid") Integer uid);

	@Select("SELECT state FROM orders WHERE oid = #{oid} AND uid = #{uid}")
	Integer getOrderState(@Param("oid") Integer oid, @Param("uid") Integer uid);

//	@Select("SELECT oid, A.uid, order_number, email, address, addr_number, addr_building, addr_remainder, A.phone, price, payment_method, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid ORDER BY oid DESC")
	@Select("SELECT oid, A.uid, order_number, email, address, addr_number, addr_building, addr_remainder, A.phone, pickup_date, dropoff_date, price, payment_method, memo, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid ORDER BY oid DESC LIMIT 500")
	ArrayList<OrderStateData> getOrderStateData();

	@Select("SELECT oid, A.uid, order_number, email, address, addr_number, addr_building, addr_remainder, A.phone, pickup_date, dropoff_date, price, payment_method, memo, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid WHERE order_number LIKE #{search} OR email LIKE #{search} ORDER BY oid DESC")
	ArrayList<OrderStateData> getOrderStateDataSearch(@Param("search") String search);

	@Select("SELECT COUNT(*) FROM orders WHERE state = 4")
	Integer getOrderStateCompleteCount();

	@Select("SELECT COUNT(*) FROM orders WHERE state != 4")
	Integer getOrderStateIncompleteCount();

	@Select("SELECT oid, A.uid, order_number, pickup_date, email, address, addr_number, addr_building, addr_remainder, A.phone, price, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid WHERE state = 0 OR state = 1 ORDER BY state ASC, pickup_date ASC")
	ArrayList<PickupStateData> getPickupStateData();

	@Select("SELECT COUNT(*) FROM orders WHERE state = 1 AND rdate >= CURDATE()")
	Integer getPickupStateCompleteCount();

	@Select("SELECT COUNT(*) FROM orders WHERE state = 0")
	Integer getPickupStateIncompleteCount();

	@Select("SELECT oid, A.uid, order_number, dropoff_date, email, address, addr_number, addr_building, addr_remainder, A.phone, price, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid WHERE state = 2 OR state = 3 ORDER BY state ASC, dropoff_date ASC")
	ArrayList<DropoffStateData> getDropoffStateData();

	@Select("SELECT COUNT(*) FROM orders WHERE state = 3 AND rdate >= CURDATE()")
	Integer getDropoffStateCompleteCount();

	@Select("SELECT COUNT(*) FROM orders WHERE state = 2")
	Integer getDropoffStateIncompleteCount();

	@Select("SELECT SUM(count) FROM item WHERE oid = #{oid}")
	Integer getSumCountForItem(@Param("oid") Integer oid);

	
	
	
	@Select("SELECT @X:=@X+1 AS rownum, A.uid, B.address, A.email, A.phone, avg as avgPrice, sum as accruePrice, count, A.rdate FROM (SELECT @X:=0) R, user A INNER JOIN (SELECT uid, CONCAT(address, ' ', addr_number, ' ', addr_building, ' ', addr_remainder) as address, AVG(price) as avg, SUM(price) as sum, COUNT(*) as count FROM orders GROUP BY uid) B ON A.uid = B.uid WHERE authority = 'ROLE_MEMBER' ORDER BY count DESC")
	ArrayList<MemberInfo> getMemberInfo();

//	@Select("SELECT @X:=@X+1 AS rownum, A.* FROM (SELECT @X:=0) R, user A WHERE authority = 'ROLE_MEMBER' AND email LIKE #{search}")
//	@Select("SELECT @X:=@X+1 AS rownum, A.uid, B.address, A.email, A.phone, avg as avgPrice, sum as accruePrice, count, A.rdate FROM (SELECT @X:=0) R, user A INNER JOIN (SELECT uid, CONCAT(address, ' ', addr_number, ' ', addr_building, ' ', addr_remainder) as address, AVG(price) as avg, SUM(price) as sum, COUNT(*) as count FROM orders GROUP BY uid) B ON A.uid = B.uid WHERE authority = 'ROLE_MEMBER' ORDER BY count DESC")
	@Select("SELECT @X:=@X+1 AS rownum, A.uid, B.address, A.email, A.phone, avg as avgPrice, sum as accruePrice, count, A.rdate FROM (SELECT @X:=0) R, user A INNER JOIN (SELECT uid, CONCAT(address, ' ', addr_number, ' ', addr_building, ' ', addr_remainder) as address, AVG(price) as avg, SUM(price) as sum, COUNT(*) as count FROM orders GROUP BY uid) B ON A.uid = B.uid  WHERE authority = 'ROLE_MEMBER' AND email LIKE #{search} ORDER BY count DESC")
	ArrayList<MemberInfo> getMemberInfoSearch(@Param("search") String search);

	@Select("SELECT @X:=@X+1 AS rownum, A.* FROM (SELECT @X:=0) R, user A WHERE authority = 'ROLE_MEMBER' AND uid = #{uid}")
	ArrayList<MemberInfo> getMemberInfoAppend(@Param("uid") Integer uid);

	@Select("SELECT @X:=@X+1 AS rownum, A.* FROM (SELECT @X:=0) R, user A WHERE authority = 'ROLE_MEMBER' AND email LIKE #{search} AND uid = #{uid}")
	ArrayList<MemberInfo> getMemberInfoAppendSearch(@Param("search") String search, @Param("uid") Integer uid);

	@Select("SELECT @X:=@X+1 AS rownum, A.* FROM (SELECT @X:=0) R, user A WHERE authority = 'ROLE_DELIVERER'")
	ArrayList<DelivererInfo> getDelivererInfo();

	@Select("SELECT oid, A.uid, order_number, pickup_date, dropoff_date, address, addr_number, addr_building, addr_remainder, price, dropoff_price, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid WHERE A.uid = #{uid}")
	ArrayList<MemberOrderInfo> getMemberOrderInfo(@Param("uid") Integer uid);

	@Select("SELECT SUM(price) FROM orders WHERE uid = #{uid}")
	Integer getTotalPrice(@Param("uid") Integer uid);

	@Select("SELECT SUM(price) FROM orders WHERE uid = #{uid} AND state = 4 AND rdate > DATE_ADD(NOW(), INTERVAL -6 MONTH)")
	Integer getTotalPriceBySixMonth(@Param("uid") Integer uid);
	
	@Select("SELECT price FROM item_code WHERE item_code = #{item_code}")
	Integer getItemPrice(@Param("item_code") Integer item_code);

	@Select("SELECT name FROM item_code WHERE item_code = #{item_code}")
	String getItemName(@Param("item_code") Integer item_code);
	
	@Select("SELECT value FROM coupon_code WHERE coupon_code = #{coupon_code}")
	Integer getCouponValue(@Param("coupon_code") Integer coupon_code);

	@Select("SELECT uid FROM coupon WHERE serial_number = #{serial_number}")
	Integer getCouponSerialNumberExist(@Param("serial_number") String serial_number);

	@Select("SELECT COUNT(*) FROM coupon WHERE coupon_code = #{coupon_code} AND serial_number = #{serial_number} AND enabled = 1")
	Integer getRecommendationCouponEnabledCount(@Param("coupon_code") Integer coupon_code, @Param("serial_number") String serial_number);

	@Select("SELECT value FROM coupon WHERE cpid = #{cpid} AND uid = #{uid}")
	Integer getCouponPrice(@Param("uid") Integer uid, @Param("cpid") Integer cpid);

	@Select("SELECT oid FROM orders order by oid DESC LIMIT 1")
	Integer getLatestOrderId();
	

	@Select("SELECT oid FROM orders WHERE uid = #{uid} order by oid DESC LIMIT 1")
	Integer getLatestOrderIdByUid(@Param("uid") Integer uid);
	
	
	@Select("SELECT * FROM areacode")
	ArrayList<Area> getAvailableAreaDatas();

	@Select("SELECT acid FROM areacode WHERE area = #{area} LIMIT 1")
	Integer getAcidWithAreaData(@Param("area") String area);
	
	@Select("SELECT area FROM areacode")
	ArrayList<String> getAvailableArea();
	
	@Select("SELECT area_date FROM area_date WHERE acid = #{acid}")
	ArrayList<String> getAvailableAreaDateDatas(@Param("acid") Integer acid);
	
	@Select("SELECT * FROM area_date WHERE acid = #{acid}")
	ArrayList<AreaDate> getAvailableAreaDate(@Param("acid") Integer acid);
	
	@Select("SELECT phone FROM area_alarm WHERE acid = #{acid}")
	ArrayList<String> getPhones(@Param("acid") Integer acid);
	
	@Select("SELECT * FROM orders WHERE oid = #{oid}")
	Order getOrderForSingle(@Param("oid") Integer oid);
	
	@Insert("INSERT INTO user (email, password, name, phone, img, birthday, enabled, authority, rdate) VALUES(#{email}, SHA(#{password}), #{name}, #{phone}, #{img}, #{birthday}, #{enabled}, #{authority}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "uid", before = false, resultType = Integer.class)
	Boolean addUser(UserData userData);

	@Insert("INSERT INTO user (email, password, name, phone, img, birthday, enabled, authority, rdate) VALUES(#{email}, SHA(#{password}), #{phone}, #{img}, #{birthday}, #{enabled}, #{authority}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "uid", before = false, resultType = Integer.class)
	Boolean registerUser(UserData userData);

	
	
	@Insert("INSERT INTO address (uid, type, address, addr_number, addr_building, addr_remainder, rdate) VALUES(#{uid}, #{type}, #{address}, #{addr_number}, #{addr_building}, #{addr_remainder}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "adrid", before = false, resultType = Integer.class)
	Boolean addAddress(AddressData addressData);


	@Insert("INSERT INTO address (uid, type, address, addr_number, addr_building, addr_remainder, rdate) VALUES(#{uid}, #{type}, #{address}, #{addr_number}, #{addr_building}, #{addr_remainder}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "adrid", before = false, resultType = Integer.class)
	Boolean addAddress2(Address addressData);

	
	@Insert("INSERT INTO orders (uid, adrid, phone, address, addr_number, addr_building, addr_remainder, memo, price, dropoff_price, pickup_date, dropoff_date, rdate) VALUES(#{uid}, #{adrid}, #{phone}, #{address}, #{addr_number}, #{addr_building}, #{addr_remainder}, #{memo}, #{price}, #{dropoff_price}, #{pickup_date}, #{dropoff_date}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "oid", before = false, resultType = Integer.class)
	Boolean addOrder(OrderData orderData);

	@Insert("INSERT INTO orders (uid, adrid, phone, address, addr_number, addr_building, addr_remainder, memo, price, dropoff_price, pickup_date, dropoff_date, rdate) VALUES(#{uid}, #{adrid}, #{phone}, #{address}, #{addr_number}, #{addr_building}, #{addr_remainder}, #{memo}, #{price}, #{dropoff_price}, #{pickup_date}, #{dropoff_date}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "oid", before = false, resultType = Integer.class)
	Boolean addNewOrder(Order order);	
	
	@Insert("INSERT INTO item (oid, item_code, price, count, rdate) VALUES(#{oid}, #{item_code}, #{price}, #{count}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "itid", before = false, resultType = Integer.class)
	Boolean addItem(ItemData itemData);

	@Insert("INSERT INTO coupon (uid, oid, coupon_code, value, used, start_date, end_date, rdate) VALUES(#{uid}, #{oid}, #{coupon_code}, #{value}, #{used}, #{start_date}, #{end_date}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "cpid", before = false, resultType = Integer.class)
	Boolean addCoupon(CouponData couponData);

	@Insert("INSERT INTO coupon (uid, oid, coupon_code, value, used, start_date, end_date, rdate, serial_number, enabled) VALUES(#{uid}, #{oid}, #{coupon_code}, #{value}, #{used}, #{start_date}, #{end_date}, NOW(), #{serial_number}, #{enabled})")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "cpid", before = false, resultType = Integer.class)
	Boolean addRecommendationCoupon(CouponData couponData);

	@Insert("INSERT INTO areacode (areacode, area) VALUES(#{areacode}, #{area})")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "acid", before = false, resultType = Integer.class)
	Boolean insertArea(AreaData areaData);
	
	@Insert("INSERT INTO area_date (acid, area_date) VALUES(#{acid}, #{area_date})")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "adid", before = false, resultType = Integer.class)
	Boolean insertAreaDate(AreaDateData areaDateData);

	@Insert("INSERT INTO area_alarm (acid, phone) VALUES(#{acid}, #{phone})")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "aaid", before = false, resultType = Integer.class)
	Boolean insertAreaAlarm(AreaAlarmData areaAlarmData);
	
	@Update("UPDATE user SET password = SHA(#{password}) WHERE email = #{email}")
	Boolean updatePassword(@Param("email") String email, @Param("password") String password);

	@Update("UPDATE user SET img = #{img} WHERE uid = #{uid}")
	Boolean updateImageForUser(UserData userData);

	@Update("UPDATE user SET phone = #{phone} WHERE uid = #{uid}")
	Boolean updatePhone(UserData userData);

	@Update("UPDATE user SET phone = #{phone} WHERE email = #{email}")
	Boolean updatePhoneByEmail(UserData userData);

	@Update("UPDATE coupon SET oid = #{oid}, used = 1 WHERE cpid = #{cpid} AND uid = #{uid}")
	Boolean updateCoupon(@Param("uid") Integer uid, @Param("oid") Integer oid, @Param("cpid") Integer cpid);

	@Update("UPDATE coupon SET used = 0 WHERE oid = #{oid} AND uid = #{uid}")
	Boolean updateCouponUsedCancel(@Param("oid") Integer oid, @Param("uid") Integer uid);

	@Update("UPDATE orders SET order_number = #{order_number} WHERE oid = #{oid} AND uid = #{uid}")
	Boolean updateOrderNumber(OrderData orderData);


	@Update("UPDATE orders SET order_number = #{order_number} WHERE oid = #{oid} AND uid = #{uid}")
	Boolean updateNewOrderNumber(Order orderData);
	
	
	@Update("UPDATE address SET address = #{address}, addr_number = #{addr_number}, addr_building = #{addr_building}, addr_remainder = #{addr_remainder} WHERE type = #{type} AND uid = #{uid}")
	Boolean updateMemberAddress(Address address);

	@Update("UPDATE orders SET state = 2, note = #{note}, rdate = NOW() WHERE oid = #{oid}")
	Boolean updatePickupRequestComplete(@Param("oid") Integer oid, @Param("note") String note);

	@Update("UPDATE orders SET state = 4, note = #{note}, payment_method = #{payment_method}, rdate = NOW() WHERE oid = #{oid}")
	Boolean updateDeliveryRequestComplete(@Param("oid") Integer oid, @Param("note") String note, @Param("payment_method") String payment_method);

	@Update("UPDATE orders SET state = 4, note = #{note}, rdate = NOW() WHERE oid = #{oid}")
	Boolean updateDeliveryRequest(@Param("oid") Integer oid, @Param("note") String note);
	
	@Update("UPDATE orders SET address = #{address}, addr_number = #{addr_number}, addr_building = #{addr_building}, addr_remainder = #{addr_remainder} WHERE (uid = #{uid} AND state = 0) OR (uid = #{uid} AND state = 2")
	Boolean updateOrderAddress(Address address);

	@Update("UPDATE orders SET state = 1, pickup_man = #{uid}, rdate = NOW() WHERE oid = #{oid}")
	Boolean setPickupMan(PickupStateData pickupStateData);

	@Update("UPDATE orders SET state = 3, dropoff_man = #{uid}, rdate = NOW() WHERE oid = #{oid}")
	Boolean setDropoffMan(DropoffStateData dropoffStateData);

	@Update("UPDATE orders SET note = #{note} WHERE oid = #{oid} AND uid = #{uid}")
	Boolean updateNote(@Param("oid") Integer oid, @Param("uid") Integer uid, @Param("note") String note);

	@Update("UPDATE user SET enabled = #{enabled} WHERE uid = #{uid}")
	Boolean updateDelivererEnabled(@Param("uid") Integer uid, @Param("enabled") Boolean enabled);

	@Update("UPDATE coupon SET serial_number = #{serial_number} WHERE uid = #{uid} AND coupon_code = #{coupon_code} AND serial_number is NULL")
	Boolean updateRecommendationCoupon(@Param("coupon_code") Integer coupon_code, @Param("uid") Integer uid, @Param("serial_number") String serial_number);

	@Update("UPDATE coupon SET enabled = 1 WHERE coupon_code = #{coupon_code} AND serial_number = #{serial_number}")
	Boolean updateRecommendationCouponEnable(@Param("coupon_code") Integer coupon_code, @Param("serial_number") String serial_number);

	@Update("UPDATE orders SET pickup_date = #{pickup_date}, dropoff_date = #{dropoff_date} WHERE oid = #{oid}")
	Boolean updateOrderDateTime(Order order);



	@Delete("DELETE FROM mileage WHERE oid = #{oid} AND uid = #{uid} AND type = #{type}")
	Boolean deleteMileageUsedCancel(@Param("oid") Integer oid, @Param("uid") Integer uid, @Param("type") Integer type);

	@Delete("DELETE FROM orders WHERE oid = #{oid} AND uid = #{uid}")
	Boolean delOrder(@Param("oid") Integer oid, @Param("uid") Integer uid);
	
	@Delete("DELETE FROM areacode WHERE acid = #{acid}")
	Boolean delArea(@Param("acid") Integer acid);
	
	@Delete("DELETE FROM area_date WHERE acid = #{acid}")
	Boolean delAllAreaDate(@Param("acid") Integer acid);
	
	@Delete("DELETE FROM area_alarm WHERE acid = #{acid}")
	Boolean delAllAreaAlarm(@Param("acid") Integer acid);
	
	@Delete("DELETE FROM area_date WHERE adid = #{adid}")
	Boolean delAreaDate(@Param("adid") Integer adid);
	
	@Delete("DELETE FROM area_alarm WHERE adid = #{aaid}")
	Boolean delAreaAlarm(@Param("aaid") Integer aaid);
	
	

	@Select("SELECT regid FROM gcm WHERE uid = #{uid}")
	String getRegid(@Param("uid") Integer uid);

	@Select("SELECT COUNT(*) FROM gcm WHERE regid = #{regid}")
	Integer getCanonicalRegidCount(@Param("regid") String canonicalRegId);

	@Select("SELECT uid FROM gcm WHERE regid = #{regid}")
	Integer getUidFromGcm(@Param("regid") String regid);
	
	@Insert("INSERT INTO gcm (uid, regid, rdate) VALUES(#{uid}, #{regid}, NOW()) ON DUPLICATE KEY UPDATE regid = #{regid}, rdate = NOW()")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "gcmid", before = false, resultType = Integer.class)
	Boolean updateRegid(@Param("uid") Integer uid, @Param("regid") String regid);
	
	@Delete("UPDATE gcm SET regid = null WHERE regid = #{regid}")
	Boolean clearAllRegid(@Param("regid") String regid);

	@Delete("UPDATE gcm SET regid = null WHERE uid = #{uid}")
	Boolean clearRegid(@Param("uid") Integer uid);
	
	
	
	@Select("SELECT * FROM order_item")
	ArrayList<OrderItem> getOrderItems();

	@Select("SELECT * FROM category")
	ArrayList<Category> getCategory();

	@Select("SELECT * FROM app_info ORDER BY aiid DESC LIMIT 1")
	AppInfo getAppInfo();

	@Select("SELECT * FROM notice ORDER BY nid DESC")
	ArrayList<Notice> getNotice();

	@Select("SELECT mileage FROM auth_user WHERE uid = #{uid}")
	Integer getMileage(@Param("uid") Integer uid);

	@Select("SELECT count(*) FROM mileage WHERE uid = #{uid} AND oid = #{oid}")
	Integer checkMileage(@Param("oid") Integer oid, @Param("uid") Integer uid);
	
	@Select("SELECT mileage FROM mileage WHERE oid = #{oid} AND type = 1 LIMIT 1")
	Integer getMileageByOid(@Param("oid") Integer oid);
	
	@Select("SELECT price FROM sale ORDER BY sid DESC LIMIT 1")
	Integer getSale();

	@Insert("INSERT INTO mileage (uid, oid, type, mileage, rdate) VALUES (#{uid}, #{oid}, #{type}, #{mileage}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "mid", before = false, resultType = Integer.class)	
	Boolean addUseOfMileage(@Param("uid") Integer uid, @Param("oid") Integer oid, @Param("type") Integer type, @Param("mileage") Integer mileage);
	
	@Insert("INSERT INTO auth_user (uid, code, email, phone, mileage, total, user_class, agent, rdate) VALUES (#{uid}, #{code}, #{email}, #{phone}, #{mileage}, #{total}, #{user_class}, #{agent}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "auid", before = false, resultType = Integer.class)
	Boolean addAuthUser(AuthUser authUser);

	@Select("SELECT COUNT(*) FROM orders WHERE uid = #{uid}")
	Integer getCountOfOrder(@Param("uid") Integer uid);

	@Select("SELECT SUM(price) FROM orders WHERE uid = #{uid} AND state = 4")
	Integer getTotalGrossOfUser(@Param("uid") Integer uid);

	@Select("SELECT COUNT(*) FROM auth_user WHERE uid = #{uid}")
	Integer isAuthUser(@Param("uid") Integer uid);

	@Select("SELECT * FROM auth_user WHERE uid = #{uid}")
	AuthUser getAuthUser(@Param("uid") Integer uid);

	
	@Insert("INSERT INTO orders (uid, adrid, phone, address, addr_number, addr_building, addr_remainder, memo, price, payment_method, dropoff_price, pickup_date, dropoff_date, rdate) VALUES(#{uid}, #{adrid}, #{phone}, #{address}, #{addr_number}, #{addr_building}, #{addr_remainder}, #{memo}, #{price}, #{payment_method}, #{dropoff_price}, #{pickup_date}, #{dropoff_date}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "oid", before = false, resultType = Integer.class)
	Boolean insertOrder(Order order);

	@Insert("INSERT INTO feedback (uid, oid, rate, time, quality, kindness, memo, rdate) VALUES(#{uid}, #{oid}, #{rate}, #{time}, #{quality}, #{kindness}, #{memo}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "fid", before = false, resultType = Integer.class)
	Boolean addRate(Feedback feedback);

	@Insert("INSERT INTO code (uid, code, rdate) VALUES(#{uid}, #{value}, NOW()) ON DUPLICATE KEY UPDATE code = #{value}, rdate = NOW()")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "cid", before = false, resultType = Integer.class)
	Boolean generateCode(@Param("value") String value, @Param("uid") Integer uid);


	
	@Select("SELECT rdate FROM code WHERE uid = #{uid}")
	Date getAuthorizationDate(@Param("uid") Integer uid);
	
	@Select("SELECT code FROM code WHERE uid = #{uid}")
	String getAuthorizationCode(@Param("uid") Integer uid);
	
	@Select("SELECT uid FROM code WHERE code = #{code}")
	Integer getAuthorizationCodeExist(@Param("code") String code);

	@Delete("DELETE FROM item WHERE oid = #{oid}")
	Boolean deleteOrderItem(@Param("oid") Integer oid);
	
	@Update("UPDATE orders SET price = #{price}, dropoff_price = #{dropoff_price} WHERE oid = #{oid}")
	Boolean updateOrderData(Order order);

	@Select("SELECT COUNT(*) FROM feedback WHERE oid = #{oid}")
	Integer selectRate(@Param("oid") Integer oid);

	@Select("SELECT user_class FROM auth_user WHERE uid = #{uid}")
	Integer getAccumulationRateByUser(@Param("uid") Integer uid);


	@Select("SELECT total FROM auth_user WHERE uid = #{uid}")
	Integer getTotalByUser(@Param("uid") Integer uid);
	

	@Select("SELECT mileage FROM auth_user WHERE uid = #{uid}")
	Integer getMileageByUser(@Param("uid") Integer uid);
	

	@Select("SELECT mileage FROM mileage WHERE oid = #{oid} AND uid = #{uid} AND type = #{type}")
	Integer selectMileage(@Param("oid") Integer oid, @Param("uid") Integer uid, @Param("type") Integer type);

	
	@Update("UPDATE auth_user SET total = #{total} WHERE uid = #{uid}")
	Boolean updateTotalByUser(@Param("uid") Integer uid, @Param("total") int total);
	
	@Update("UPDATE auth_user SET user_class = #{user_class} WHERE uid = #{uid}")
	Boolean updateUserClass(@Param("uid") Integer uid, @Param("user_class") int user_class);


	
	@Update("UPDATE auth_user SET mileage = #{mileage} WHERE uid = #{uid}")
	Boolean updateMileageByUser(@Param("uid") Integer uid, @Param("mileage") int mileage);

	@Select("SELECT COUNT(*) FROM auth_user WHERE email = #{email}")
	Integer selectEmailAuthUser(@Param("email") String email);

	@Select("SELECT COUNT(*) FROM auth_user WHERE phone = #{phone}")
	Integer selectPhoneAuthUser(@Param("phone") String phone);

	@Select("SELECT * FROM district_code")
	ArrayList<District> getDistricts();
	

	@Select("SELECT dcid FROM district_code WHERE area = #{dist} LIMIT 1")
	Integer getDistrictsHolidayDate(@Param("area") String area);
	
	@Select("SELECT COUNT(*) FROM district_code WHERE city= #{city} AND district = #{district} AND dong is null")
	Integer isAvailableDistrictWithNull(District object);

	@Select("SELECT COUNT(*) FROM district_code WHERE #{city} = city AND #{district} = district AND #{dong} = dong")
	Integer isAvailableDistrict(District object);
	


	@Select("SELECT dcid FROM district_code WHERE #{city} = city AND #{district} = district AND dong = #{dong}")
	Integer getDistrictId(District object);

	@Select("SELECT dcid FROM district_code WHERE #{city} = city AND #{district} = district AND dong is null")
	Integer getDistrictIdWithNull(District object);
	

	@Select("SELECT holiday FROM district_date WHERE dcid = #{dcid}")
	ArrayList<String> getAvailableDistrictDateDatas(@Param("dcid") Integer dcid);
	
	@Select("SELECT phone FROM district_alarm WHERE dcid = #{dcid}")
	ArrayList<String> getDistrictPhones(@Param("dcid") Integer dcid);
	
	
	@Select("SELECT price FROM orders WHERE oid = #{oid} AND uid = #{uid}")
	int getOrderPrice(@Param("oid") int oid, @Param("uid") int uid);

	@Select("SELECT * FROM orders WHERE phone = #{phone} ORDER BY oid DESC")
	ArrayList<Order> getOrderByPhone(@Param("phone") String phone);

	@Select("SELECT * FROM item_info WHERE item_code = #{info}")
	OrderItemInfo getItemInfo(@Param("info") Integer info);

	@Select("SELECT datetime, type FROM pickuptime_type WHERE datetime >= NOW()")
	ArrayList<PickupTime> getPickupTime();

	@Select("SELECT COUNT(*) FROM dropofftime WHERE datetime > #{dropoffTime}")
	Integer getDropoffTime(@Param("dropoffTime") String dropoffTime);

	@Insert("INSERT INTO pickuptime_type (datetime, type, rdate) VALUES (#{datetime}, #{type}, NOW())")
	Boolean addPickupTime(@Param("datetime") String datetime, @Param("type") Integer type);	

	@Insert("INSERT INTO dropofftime (datetime, rdate) VALUES (#{datetime}, NOW())")
	Boolean addDropoffTime(@Param("datetime") String datetime);	
}
