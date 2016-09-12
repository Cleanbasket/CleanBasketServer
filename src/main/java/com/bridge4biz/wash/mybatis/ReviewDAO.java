package com.bridge4biz.wash.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public class ReviewDAO {
	private ReviewMapper reviewMapper;

	public ReviewDAO() {
	}

	@Autowired
	private ReviewDAO(ReviewMapper reviewMapper, PlatformTransactionManager platformTransactionManager) {
		this.reviewMapper = reviewMapper;
	}

	public boolean addReview(int uid, int oid, int rate, int kindness, String memo) {
		return reviewMapper.addReview(uid, oid, rate, kindness, memo);
	}

	public Integer getCountOfReview(int oid, int kindness) {
		return reviewMapper.getCountOfReview(oid, kindness);
	}

}
