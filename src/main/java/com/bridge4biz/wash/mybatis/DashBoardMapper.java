package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.bridge4biz.wash.service.DashBoardData;

public interface DashBoardMapper {
	@Select("SELECT DATE_FORMAT(pickup_date, '%Y-%m-%d') AS date, COUNT(*) as DATA FROM orders GROUP BY DATE_FORMAT(pickup_date, '%Y-%m-%d') ORDER BY DATE_FORMAT(pickup_date, '%Y-%m-%d') DESC LIMIT #{limit};")
	ArrayList<DashBoardData> getDailyPickUp(@Param("limit") int limit);
}