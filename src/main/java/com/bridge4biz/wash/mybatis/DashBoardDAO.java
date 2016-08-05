package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import com.bridge4biz.wash.service.DashBoardData;
import com.bridge4biz.wash.service.MapData;

public class DashBoardDAO {
	private static final Logger log = LoggerFactory.getLogger(DashBoardDAO.class);		

	private DashBoardMapper dashBoardMapper;
	
	public DashBoardDAO() {
		
	}

	@Autowired
	private DashBoardDAO(DashBoardMapper dashBoardMapper, PlatformTransactionManager platformTransactionManager) {
		this.dashBoardMapper = dashBoardMapper;
	}
	
	public ArrayList<DashBoardData> getBoardDatas(String type, int limit) {
		ArrayList<DashBoardData> dashBoardDatas = null;
		
		if (type.equals("d")) 
			dashBoardDatas = dashBoardMapper.getDailyPickUp(limit);
		else if (type.equals("w")) 
			dashBoardDatas = dashBoardMapper.getWeeklyPickup(limit);
		else if (type.equals("m"))
			dashBoardDatas = dashBoardMapper.getMonthlyPickup(limit);

		return dashBoardDatas;
	}
	
	public ArrayList<MapData> getMapDatas(String type, String date) {
		ArrayList<MapData> mapDatas = null;
		
		if (type.equals("d")) 
			mapDatas = dashBoardMapper.getPickupNo(1, date);
		else if (type.equals("w")) 
			mapDatas = dashBoardMapper.getPickupNo(7, date);
		else if (type.equals("m"))
			mapDatas = dashBoardMapper.getPickupNo(30, date);
		else if (type.equals("s"))
			mapDatas = dashBoardMapper.getPickupNo(91, date);
		else if (type.equals("y"))
			mapDatas = dashBoardMapper.getPickupNo(365, date);
		
		return mapDatas;
	}
}
