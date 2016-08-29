package com.bridge4biz.wash.mybatis;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.bridge4biz.wash.service.Promotion;
import com.bridge4biz.wash.service.PromotionResult;

public interface MileageMapper {

	@Select("SELECT mileage FROM auth_user WHERE uid = #{uid}")
	Integer getMileage(@Param("uid") Integer uid);

	@Select("SELECT count(*) FROM mileage WHERE uid = #{uid} AND oid = #{oid}")
	Integer checkMileage(@Param("oid") Integer oid, @Param("uid") Integer uid);

	@Select("SELECT mileage FROM mileage WHERE oid = #{oid} AND type = 1 LIMIT 1")
	Integer getMileageByOid(@Param("oid") Integer oid);

	@Insert("INSERT INTO mileage (uid, oid, type, mileage, rdate) VALUES (#{uid}, #{oid}, #{type}, #{mileage}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "mid", before = false, resultType = Integer.class)
	Boolean addUseOfMileage(@Param("uid") Integer uid, @Param("oid") Integer oid, @Param("type") Integer type,
			@Param("mileage") Integer mileage);

	@Select("SELECT mileage FROM auth_user WHERE uid = #{uid}")
	Integer getMileageByUser(@Param("uid") Integer uid);

	@Select("SELECT mileage FROM mileage WHERE oid = #{oid} AND uid = #{uid} AND type = #{type}")
	Integer selectMileage(@Param("oid") Integer oid, @Param("uid") Integer uid, @Param("type") Integer type);

	@Update("UPDATE auth_user SET mileage = #{mileage} WHERE uid = #{uid}")
	Boolean updateMileageByUser(@Param("uid") Integer uid, @Param("mileage") int mileage);

	@Delete("DELETE FROM mileage WHERE oid = #{oid} AND uid = #{uid} AND type = #{type}")
	Boolean deleteMileageUsedCancel(@Param("oid") Integer oid, @Param("uid") Integer uid, @Param("type") Integer type);

	@Select("SELECT * from promotion WHERE code = #{code}")
	ArrayList<Promotion>  getPromotionByCode(@Param("code") String code);
	
	@Select("SELECT * from promotion_result WHERE uid = #{uid}")
	ArrayList<PromotionResult> getPromotionResultsByUid(@Param("uid")Integer uid); 
	
	@Select("SELECT count(*) FROM promotion_result WHERE uid = #{uid} AND promotion_id = #{promotion_id}")
	Integer checkPromotion(@Param("uid") Integer uid, @Param("promotion_id") Integer promotionId);
	
	@Insert("INSERT INTO promotion_result (uid, promotion_id, used, enable) VALUES(#{uid}, #{promotion_id}, 1, 0)") 
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "prid", before = false, resultType = Integer.class)
	Boolean addPromotionResult(@Param("uid") Integer uid, @Param("promotion_id") Integer promotionId);
}
