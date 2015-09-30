package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import com.bridge4biz.wash.service.DashBoardData;

public class DashBoardDAO {
	private static final Logger log = LoggerFactory.getLogger(DashBoardDAO.class);		

	private DashBoardMapper dashBoardMapper;
	
	public DashBoardDAO() {
		
	}

	@Autowired
	private DashBoardDAO(DashBoardMapper dashBoardMapper, PlatformTransactionManager platformTransactionManager) {
		this.dashBoardMapper = dashBoardMapper;
	}
	
	public ArrayList<DashBoardData> getDailyPickup(int limit) {
		return dashBoardMapper.getDailyPickUp(limit);
	}
}
