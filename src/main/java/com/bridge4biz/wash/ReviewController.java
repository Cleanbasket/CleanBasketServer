package com.bridge4biz.wash;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.mybatis.MileageDao;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.mybatis.ReviewDAO;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	MybatisDAO dao;
	
	@Autowired
	ReviewDAO reviewDao;
	
	@Autowired
	private MileageDao mileageDao;

	@Secured({ "ROLE_MEMBER" })
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	@ResponseBody
	public Constant addReview(Constant constant, Authentication auth, Gson gson, @RequestBody Map<String, String> data) {
		int oid = Integer.parseInt(data.get("oid"));
		int kindness = Integer.parseInt(data.get("kindness"));
		int uid = dao.getUid(auth.getName());
		int rate = Integer.parseInt(data.get("rate"));
		if (reviewDao.getCountOfReview(oid,kindness) > 0) {
			return constant.setConstant(Constant.IMPOSSIBLE, "이미 등록된 리뷰입니다.");
		}else if (reviewDao.addReview(uid, oid, rate, kindness, data.get("memo"))) {
			mileageDao.addMileage(uid, oid, 10);
			return constant.setConstant(Constant.SUCCESS, "Review 등록 성공");
		}
		return constant.setConstant(Constant.ERROR, "Review 등록 실패");
	}

}
