package com.bridge4biz.wash.mybatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

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

}
