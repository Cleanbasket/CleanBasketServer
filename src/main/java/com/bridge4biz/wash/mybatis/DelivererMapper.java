package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import com.bridge4biz.wash.data.ItemData;
import com.bridge4biz.wash.service.Item;
import org.apache.ibatis.annotations.*;

import com.bridge4biz.wash.service.Order;

public interface DelivererMapper {
	@Select("SELECT pickup_man, dropoff_man, oid, A.uid, order_number, email, address, addr_number, addr_building, addr_remainder, A.phone, pickup_date, dropoff_date, price, A.payment_method, note, memo, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid ORDER BY oid DESC LIMIT 10")
	ArrayList<Order> getRecentOrderInit();
	
	@Select("SELECT pickup_man, dropoff_man, oid, A.uid, order_number, email, address, addr_number, addr_building, addr_remainder, A.phone, pickup_date, dropoff_date, price, A.payment_method, note, memo, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid WHERE oid < #{oid} ORDER BY oid DESC LIMIT 20")
	ArrayList<Order> getRecentOrder(@Param("oid") Integer oid);
	
	@Select("SELECT pickup_man, dropoff_man, oid, A.uid, order_number, pickup_date, dropoff_date, email, address, addr_number, addr_building, addr_remainder, A.phone, price, A.payment_method, note, memo, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid WHERE state = 0 OR state = 1 ORDER BY state ASC, pickup_date ASC")
	ArrayList<Order> getPickupData();

	@Select("SELECT pickup_man, dropoff_man, oid, A.uid, order_number, pickup_date, dropoff_date, email, address, addr_number, addr_building, addr_remainder, A.phone, price, A.payment_method, note, memo, state, A.rdate FROM orders AS A INNER JOIN user AS B ON A.uid = B.uid WHERE state = 2 OR state = 3 ORDER BY state ASC, dropoff_date ASC")
	ArrayList<Order> getDropoffData();
	
	@Update("UPDATE orders SET state = 1, pickup_man = #{uid}, rdate = NOW() WHERE oid = #{oid}")
	Boolean setPickupMan(Order order);

	@Update("UPDATE orders SET state = 3, dropoff_man = #{uid}, rdate = NOW() WHERE oid = #{oid}")
	Boolean setDropoffMan(Order order);

	@Update("UPDATE orders SET price = #{price} WHERE oid = #{oid}")
	Boolean updateOrderTotal(Order order);

	@Update("UPDATE orders SET pickup_man = null, state = 0 WHERE oid = #{oid}")
	Boolean cancelPickupAssign(Order order);
	
	@Update("UPDATE orders SET dropoff_man = null, state = 2 WHERE oid = #{oid}")
	Boolean cancelDropoffAssign(Order order);

	@Delete("DELETE FROM item WHERE oid = #{oid}")
	Boolean deleteItemList(@Param("oid") Integer oid);

	@Insert("INSERT INTO item (oid, item_code, price, count, rdate) VALUES(#{oid}, #{item_code}, #{price}, #{count}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "itid", before = false, resultType = Integer.class)
	Boolean updateItems(ItemData itemData);

}
