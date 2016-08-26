package com.bridge4biz.wash;

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

@Controller
public class MileageController {
	@Autowired
	private MybatisDAO dao;

	@Autowired
	private MileageDao mileageDao;

	@RequestMapping(method = RequestMethod.POST, value = "/member/promotion/add")
	@ResponseBody
	public Constant addPromotionCode(@RequestBody String promotionCode, Authentication auth, Constant constant) {
		Promotion promotion = mileageDao.getPromotionByCode(promotionCode);
		int uid = dao.getUid(auth.getName());
		if (promotion == null) {
			return constant.setConstant(Constant.ERROR, "존재하지 않는 프로모션입니다.");
		} else if (mileageDao.isPromotionAlreadyUse(uid, promotion.getPromotion_id())){
			return constant.setConstant(Constant.IMPOSSIBLE, "이미 사용한 코드입니다.");
		}else {
			int nowMileage = mileageDao.getMileageByUid(uid);
			int promotionMileage = promotion.getMileage();
			mileageDao.updateMileage(uid, nowMileage + promotionMileage); 
			mileageDao.addPromotionResult(uid, promotion.getPromotion_id());
			return constant.setConstant(Constant.SUCCESS, "프로모션 코드 등록 성공");
		}
	}

}
