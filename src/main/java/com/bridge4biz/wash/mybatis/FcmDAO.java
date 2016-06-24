package com.bridge4biz.wash.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;

public class FcmDAO {

	private FcmMapper fcmMapper;

	public FcmDAO(){}

	@Autowired
	private FcmDAO(FcmMapper mapper, PlatformTransactionManager platformTransactionManager) {
		this.fcmMapper = mapper;
	}

	public Boolean updateRegid(Integer uid, String regid) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		try {
			fcmMapper.updateRegid(uid, regid);
		} catch (DuplicateKeyException duplicateKeyException) {
			fcmMapper.clearAllRegid(regid);
			fcmMapper.updateRegid(uid, regid);
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return false;
		}
		// platformTransactionManager.commit(status);
		return true;
	}

	public Boolean clearRegid(Integer uid) {
		return fcmMapper.clearRegid(uid);
	}

	public Boolean clearAllRegid(String regid) {
		try {
			fcmMapper.clearAllRegid(regid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getRegid(Integer uid) {
		return fcmMapper.getRegid(uid);
	}
}
