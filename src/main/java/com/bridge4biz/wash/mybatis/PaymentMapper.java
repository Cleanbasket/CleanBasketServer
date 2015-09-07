package com.bridge4biz.wash.mybatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.bridge4biz.wash.service.PaymentResult;

public interface PaymentMapper {
	@Insert("INSERT INTO payment (uid, type, bid, authDate, cardName, rdate) VALUES (#{uid}, #{type}, #{bid}, #{authDate}, #{cardName}, NOW()) ON DUPLICATE KEY UPDATE type = #{type}, bid = #{bid}, authDate = #{authDate}, cardName = #{cardName}, rdate = NOW()")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "pid", before = false, resultType = Integer.class)	
	Boolean addPayment(@Param("uid") int uid, @Param("type") int type, @Param("bid") String bid, @Param("authDate") String authDate, @Param("cardName") String cardName);


	@Delete("DELETE FROM payment WHERE uid = #{uid}")
	Boolean removePayment(@Param("uid") Integer uid);

	@Select("SELECT * FROM payment WHERE uid = #{uid}")
	PaymentResult getPaymentInfo(@Param("oid") Integer oid, @Param("uid") Integer uid);

	@Insert("INSERT INTO payment_result (oid, uid, code, msg, tid, authDate, rdate) VALUES (#{oid}, #{uid}, #{resultCode}, #{resultMsg}, #{TID}, #{AuthDate}, NOW())")
	Boolean addPaymentResult(@Param("oid") int oid, @Param("uid") int uid, @Param("resultCode") String resultCode, @Param("resultMsg") String resultMsg, @Param("TID") String TID, @Param("AuthDate") String AuthDate);

	@Insert("INSERT INTO payment_cancel (oid, uid, code, msg, cancelAmt, cancelDate, cancelTime, cancelNum, rdate) VALUES (#{oid}, #{uid}, #{resultCode}, #{resultMsg}, #{cancelAmt}, #{cancelDate}, #{cancelTime}, #{cancelNum}, NOW())")
	Boolean addCancelResult(@Param("oid") int oid, @Param("uid") int uid, @Param("resultCode") String resultCode, @Param("resultMsg") String resultMsg, @Param("cancelAmt") String cancelAmt, @Param("cancelDate") String cancelDate, @Param("cancelTime") String cancelTime, @Param("cancelNum") String cancelNum);
	
	@Select("SELECT bid FROM payment WHERE uid = #{uid}")
	String getBillKey(@Param("uid") int uid);
	
	@Update("UPDATE orders SET payment_method = #{payment_method} WHERE oid = #{oid}")
	Boolean setUpdatePaymentStatus(@Param("oid") Integer oid, @Param("payment_method") Integer payment_method);
	
	@Select("SELECT email FROM user WHERE uid = #{uid} LIMIT 1")
	String getEmailFromOrder(@Param("uid") Integer uid);

	@Select("SELECT tid FROM payment_result WHERE oid = #{oid} LIMIT 1")
	String getTID(@Param("oid") int oid);
}
