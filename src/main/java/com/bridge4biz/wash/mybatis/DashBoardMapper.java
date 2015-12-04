package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.bridge4biz.wash.service.DashBoardData;

public interface DashBoardMapper {
	@Select("SELECT a.date, a.data, b.data as data2 FROM (SELECT DATE_FORMAT(pickup_date, '%Y-%m-%d') AS date, COUNT(*) as data FROM orders GROUP BY DATE_FORMAT(pickup_date, '%Y-%m-%d')) a LEFT JOIN (SELECT DATE_FORMAT(pickup_date, '%Y-%m-%d') AS date, COUNT(*) as data FROM orders a RIGHT JOIN (SELECT uid, COUNT(*) as count FROM orders GROUP BY uid HAVING COUNT(*) = 1) b ON a.uid = b.uid GROUP BY DATE_FORMAT(pickup_date, '%Y-%m-%d')) b ON a.date = b.date ORDER BY DATE_FORMAT(a.date, '%Y-%m-%d') DESC LIMIT #{limit};")
	ArrayList<DashBoardData> getDailyPickUp(@Param("limit") int limit);
	
	@Select("SELECT DATE_FORMAT(a.pickup_date, '%Y-%m-%d') as date, a.data, b.data as data2 FROM (SELECT pickup_date, CONCAT(YEAR(pickup_date), '/', WEEK(pickup_date)) AS date, COUNT(*) as data FROM orders GROUP BY CONCAT(YEAR(pickup_date), '/', WEEK(pickup_date))) a LEFT JOIN (SELECT a.pickup_date, CONCAT(YEAR(pickup_date), '/', WEEK(pickup_date)) AS date, COUNT(*) as data FROM orders a RIGHT JOIN (SELECT uid, COUNT(*) as count FROM orders GROUP BY uid HAVING COUNT(*) = 1) b ON a.uid = b.uid GROUP BY CONCAT(YEAR(pickup_date), '/', WEEK(pickup_date))) b ON a.date = b.date ORDER BY date DESC LIMIT #{limit};")
	ArrayList<DashBoardData> getWeeklyPickup(@Param("limit") int limit);
	
	@Select("SELECT a.date, a.data, b.data as data2 FROM (SELECT DATE_FORMAT(pickup_date, '%Y-%m') AS date, COUNT(*) as data FROM orders GROUP BY DATE_FORMAT(pickup_date, '%Y-%m')) a LEFT JOIN (SELECT DATE_FORMAT(pickup_date, '%Y-%m') AS date, COUNT(*) as data FROM orders a RIGHT JOIN (SELECT uid, COUNT(*) as count FROM orders GROUP BY uid HAVING COUNT(*) = 1) b ON a.uid = b.uid GROUP BY DATE_FORMAT(pickup_date, '%Y-%m')) b ON a.date = b.date ORDER BY date DESC LIMIT #{limit};")
	ArrayList<DashBoardData> getMonthlyPickup(@Param("limit") int limit);
}