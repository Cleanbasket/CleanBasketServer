package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.util.Constant;

public class DelivererDAO {
	private static final Logger log = LoggerFactory.getLogger(MybatisDAO.class);		

	private MybatisMapper mapper;
	private DelivererMapper delivererMapper;
	
	public DelivererDAO() {
		
	}

	@Autowired
	private DelivererDAO(MybatisMapper mapper, DelivererMapper delivererMapper, PlatformTransactionManager platformTransactionManager) {
		this.mapper = mapper;
		this.delivererMapper = delivererMapper;
	}
	
	private Order additionalInfo(Order order) {
		order.item = mapper.getItem(order.oid);
		order.coupon = mapper.getCoupon(order.oid, order.uid);
		order.pickupInfo = mapper.getDeliverer(order.pickup_man);
		order.dropoffInfo = mapper.getDeliverer(order.dropoff_man);
		
		if (mapper.checkMileage(order.oid, order.uid) > 0)
			order.mileage = mapper.getMileageByOid(order.oid, order.uid);
		else
			order.mileage = 0;
		
		return order;	
	}
	
	public ArrayList<Order> getPickUpOrder() {
		ArrayList<Order> datas = delivererMapper.getPickupData();
		
		for (Order order : datas) {
			additionalInfo(order);
		}
		
		return datas;
	}

	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/pickup/assign", consumes = { "application/json" })
	@ResponseBody
	public Constant pickupAssign(Constant constant, @RequestBody Order order) {
		Boolean success = delivererMapper.setPickupMan(order);
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");
		}
		return constant;
	}
	
	public ArrayList<Order> getDropOffOrder() {
		ArrayList<Order> datas = delivererMapper.getDropoffData();
		
		for (Order order : datas) {
			additionalInfo(order);
		}
		
		return datas;
	}
	
	@Secured("ROLE_DELIVERER")
	@RequestMapping(value = "/pickup/assign", consumes = { "application/json" })
	@ResponseBody
	public Constant dropoffAssign(Constant constant, @RequestBody Order order) {
		Boolean success = delivererMapper.setPickupMan(order);
		if (success) {
			constant.setConstant(Constant.SUCCESS, "");
		} else {
			constant.setConstant(Constant.ERROR, "");
		}
		return constant;
	}
	
	public ArrayList<Order> getRecentOrder(Integer oid) {
		ArrayList<Order> datas;
		
		if (oid == 0) {
			datas = delivererMapper.getRecentOrderInit();
		}
		else
			datas = delivererMapper.getRecentOrder(oid);
		
		for (Order order : datas) {
			additionalInfo(order);
		}
		
		return datas;
	}

	public Integer modifyOrderDate(Order order) {
		if (!mapper.updateOrderDateTime(order))
			return Constant.ERROR;
				
		return Constant.SUCCESS;
	}
	
	public Integer modifyOrderTotal(Order order) {
		if (!delivererMapper.updateOrderTotal(order))
			return Constant.ERROR;
				
		return Constant.SUCCESS;
	}

	public Integer cancelAssign(Order order) {		
		if (order.state == 1)
			delivererMapper.cancelPickupAssign(order);
		else if (order.state == 3)
			delivererMapper.cancelDropoffAssign(order);
		else 
			return Constant.ERROR;

		return Constant.SUCCESS;
	}
}
