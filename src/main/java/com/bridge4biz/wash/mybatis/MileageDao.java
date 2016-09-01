package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import com.bridge4biz.wash.service.Promotion;
import com.bridge4biz.wash.service.PromotionResult;

public class MileageDao {
	private MileageMapper mileageMapper;
	private MybatisMapper mapper;

	public MileageDao() {

	}

	@Autowired
	private MileageDao(MybatisMapper mapper, MileageMapper meilageMapper,
			PlatformTransactionManager platformTransactionManager) {
		this.mapper = mapper;
		this.mileageMapper = meilageMapper;
	}

	public void addMileage(int uid, int oid, int mileage) {
		if (mapper.isAuthUser(uid) == 0)
			return;

		int totalMileage = mileageMapper.getMileage(uid);
		mileageMapper.addUseOfMileage(uid, oid, 0, mileage);
		mileageMapper.updateMileageByUser(uid, totalMileage + mileage);
	}

	public void delMileage(int uid, int oid, int mileage) {
		if (mapper.isAuthUser(uid) == 0)
			return;

		int totalMileage = mileageMapper.getMileage(uid);

		mileageMapper.addUseOfMileage(uid, oid, 1, mileage);
		mileageMapper.updateMileageByUser(uid, totalMileage - mileage);
	}

	public Integer getMileage(int uid) {
		if (mapper.isAuthUser(uid) == 0)
			return 0;

		return getMileageByUid(uid);
	}
	
	private int getMileageByUid(int uid) {
		return mileageMapper.getMileage(uid);
	}

	public Integer getMileageByOid(int oid) {
		return mileageMapper.getMileageByOid(oid);
	}

	public int getMileageFromOrder(int oid, int uid, int type) {
		Integer mileageInOrder = mileageMapper.selectMileage(oid, uid, type);
		if (mileageInOrder != null) {
			return mileageInOrder;
		}
		return 0;
	}

	public void updateMileage(int uid, int mileage) {
		mileageMapper.updateMileageByUser(uid, mileage);
	}

	public void deleteMileageByOrderCancel(int oid, int uid, int type) {
		mileageMapper.deleteMileageUsedCancel(oid, uid, type);
	}

	public int checkMileage(int oid, int uid) {
		return mileageMapper.checkMileage(oid, uid);
	}

	public void addUseOfMileage(Integer uid, Integer oid, Integer type, Integer mileage) {
		mileageMapper.addUseOfMileage(uid, oid, type, mileage);
	}

	public Promotion getPromotionByCode(String code) {
		ArrayList<Promotion> result = mileageMapper.getPromotionByCode(code);
		if (result.size() > 0 && result != null) {
			return result.get(0);
		}
		return null;
	}
	
	public ArrayList<Promotion> getPromotionsByUid(Integer uid) {
		ArrayList<Promotion> promotions = mileageMapper.getPromotionsByUid(uid);
		if (promotions.size() > 0 && promotions != null) {
			return promotions;
		}
		return null;
	}
	
	public boolean isPromotionAlreadyUse(int uid, int promotionId) {
		return mileageMapper.checkPromotion(uid, promotionId) != 0 ? true :false;
	}
	
	public boolean addPromotionResult(int uid, int promotionId) {
		return mileageMapper.addPromotionResult(uid, promotionId);
	}

}
