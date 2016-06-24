package fcm;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

public interface FcmMapper {

	@Select("SELECT regid FROM gcm WHERE uid = #{uid}")
	String getRegid(@Param("uid") Integer uid);

	@Select("SELECT COUNT(*) FROM gcm WHERE regid = #{regid}")
	Integer getCanonicalRegidCount(@Param("regid") String canonicalRegId);

	@Select("SELECT uid FROM gcm WHERE regid = #{regid}")
	Integer getUidFromGcm(@Param("regid") String regid);

	@Insert("INSERT INTO fcm (uid, regid, rdate) VALUES(#{uid}, #{regid}, NOW())")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "fcmid", before = false, resultType = Integer.class)
	Boolean updateRegid(@Param("uid") Integer uid, @Param("regid") String regid);

	@Delete("UPDATE gcm SET regid = null WHERE regid = #{regid}")
	Boolean clearAllRegid(@Param("regid") String regid);

	@Delete("UPDATE gcm SET regid = null WHERE uid = #{uid}")
	Boolean clearRegid(@Param("uid") Integer uid);
}
