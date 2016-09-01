package com.bridge4biz.wash;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.mybatis.MileageDao;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.service.Promotion;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
public class MileageController {
	@Autowired
	private MybatisDAO dao;

	@Autowired
	private MileageDao mileageDao;

	@RequestMapping(method = RequestMethod.POST, value = "/member/promotion/add")
	@ResponseBody
	public Constant addPromotionCode(@RequestBody Map<String,String> data, Authentication auth, Constant constant) {
		String code = data.get("code");
		Promotion promotion = mileageDao.getPromotionByCode(code);
		
		int uid = dao.getUid(auth.getName());
		if (promotion == null) {
			return constant.setConstant(Constant.ERROR, "존재하지 않는 프로모션입니다.");
		} else if (mileageDao.isPromotionAlreadyUse(uid, promotion.getPromotion_id())){
			return constant.setConstant(Constant.IMPOSSIBLE, "이미 사용한 코드입니다.");
		}else {
			int nowMileage = mileageDao.getMileage(uid);
			int promotionMileage = promotion.getMileage();
			mileageDao.updateMileage(uid, nowMileage + promotionMileage); 
			mileageDao.addPromotionResult(uid, promotion.getPromotion_id());
			return constant.setConstant(Constant.SUCCESS, "프로모션 코드 등록 성공");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/member/promotion")
	@ResponseBody
	public Constant getPromotionResults(Authentication auth, Gson gson, Constant constant) {
		int uid = dao.getUid(auth.getName());
		ArrayList<Promotion> promotions = mileageDao.getPromotionsByUid(uid);
		
		if(promotions == null) {
			return constant.setConstant(Constant.ERROR, "등록된 프로모션 코드가 없습니다.");
		}else {
			return constant.setConstant(Constant.SUCCESS, "프로모션 코드 가져오기 성공.", gson.toJson(promotions));
		}	
	}

}
