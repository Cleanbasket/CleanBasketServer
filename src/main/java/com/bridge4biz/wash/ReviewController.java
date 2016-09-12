package com.bridge4biz.wash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.data.Review;
import com.bridge4biz.wash.mybatis.ReviewDAO;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	ReviewDAO reviewDao;

	@Secured({ "ROLE_MEMBER" })
	@RequestMapping(method = RequestMethod.POST, value = "/add")
	@ResponseBody
	public Constant addReview(Constant constant, Authentication auth, Gson gson, Review review) {
		if (reviewDao.getCountOfReview(review.oid, review.kindness) > 0) {
			return constant.setConstant(Constant.IMPOSSIBLE, "이미 등록된 리뷰입니다.");
		}else if (reviewDao.addReview(review.uid, review.oid, review.rate, review.kindness, review.memo)) {
			return constant.setConstant(Constant.SUCCESS, "Review 등록 성공");
		}
		return constant.setConstant(Constant.ERROR, "Review 등록 실패");
	}

}
