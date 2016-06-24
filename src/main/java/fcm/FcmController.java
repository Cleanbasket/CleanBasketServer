package fcm;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@Controller
public class FcmController {

	@Autowired
	private FcmDAO fcmDao;

	@Autowired
	private MybatisDAO dao;

	@Secured({ "ROLE_DELIVERER", "ROLE_MEMBER" })
	@RequestMapping(method = RequestMethod.GET, value = "/fcm/test")
	@ResponseBody
	public Constant gcmTest(Constant constant, Authentication auth, Gson gson) {
		FcmPushMessage.addPush("TEST", fcmDao.getRegid(dao.getUid(auth.getName())));
		return constant.setConstant(Constant.SUCCESS, "", "");
	}

	@Secured({ "ROLE_DELIVERER", "ROLE_MEMBER" })
	@RequestMapping(method = RequestMethod.POST, value = "/fcm/regid", consumes = { "application/json" })
	@ResponseBody
	public Constant regid(@RequestBody Map<String, String> data, Constant constant, Authentication auth, Gson gson) {
		String regid = data.get("regid");
		Boolean success = fcmDao.updateRegid(dao.getUid(auth.getName()), regid);
		if (success) {
			return constant.setConstant(Constant.SUCCESS, "FCM REGID 수정 성공 : SUCCESS",
					gson.toJson(fcmDao.getRegid(dao.getUid(auth.getName()))));
		} else {
			return constant.setConstant(Constant.ERROR, "FCM REGID 수정 실패 : ERROR");
		}
	}
}
