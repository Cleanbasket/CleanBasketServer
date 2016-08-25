package com.bridge4biz.wash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.mybatis.PaymentDAO;
import com.bridge4biz.wash.util.Constant;

@Controller
public class MileageController {
	@Autowired
	private MybatisDAO dao;
	
	@Autowired
	private PaymentDAO paymentDao;
	
	@RequestMapping(method=RequestMethod.POST, value = "/member/promotion/add")
	@ResponseBody
	public Constant addPromotionCode(@RequestBody String promotionCode, Authentication auth, Constant constant) {
		
		return constant;
	}
	
}
