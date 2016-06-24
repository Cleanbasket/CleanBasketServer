package com.bridge4biz.wash.fcm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;

public class FcmDAO {

	private FcmMapper mapper;

	public FcmDAO(){}
	
	@Autowired
	private FcmDAO(FcmMapper mapper, PlatformTransactionManager platformTransactionManager) {
		this.mapper = mapper;
	}

	public Boolean updateRegid(Integer uid, String regid) {
		// TransactionStatus status =
		// platformTransactionManager.getTransaction(paramTransactionDefinition);
		try {
			mapper.updateRegid(uid, regid);
		} catch (DuplicateKeyException duplicateKeyException) {
			mapper.clearAllRegid(regid);
			mapper.updateRegid(uid, regid);
		} catch (Exception e) {
			e.printStackTrace();
			// platformTransactionManager.rollback(status);
			return false;
		}
		// platformTransactionManager.commit(status);
		return true;
	}

	public Boolean clearRegid(Integer uid) {
		return mapper.clearRegid(uid);
	}

	public Boolean clearAllRegid(String regid) {
		try {
			mapper.clearAllRegid(regid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getRegid(Integer uid) {
		return mapper.getRegid(uid);
	}
}
