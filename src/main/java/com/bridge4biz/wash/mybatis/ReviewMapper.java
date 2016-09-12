package com.bridge4biz.wash.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

public interface ReviewMapper {
	
	@Insert("INSERT INTO feedback(uid, oid, rate, kindness, memo, rdate, time, quality) VALUES(#{uid}, #{oid}, #{rate}, #{kindness}, #{memo}, NOW(), 0, 0)")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "fid", before = false, resultType = Integer.class)	
	Boolean addReview(@Param("uid") int uid, @Param("oid") int oid, @Param("rate") int rate, @Param("kindness") int kindness, @Param("memo") String memo);

	@Select("SELECT count(*) FROM feedback WHERE oid = #{oid} AND kindness = #{kindness}")
	Integer getCountOfReview(@Param("oid") int oid, @Param("kindness") int kindness);
	
}
