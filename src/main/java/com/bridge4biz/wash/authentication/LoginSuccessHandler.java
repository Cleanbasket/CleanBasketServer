package com.bridge4biz.wash.authentication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private MybatisDAO dao;
	private Gson gson = new Gson();
	private Constant constant = new Constant();

	@Autowired
	private LoginSuccessHandler(MybatisDAO dao) {
		this.dao = dao;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("uid", dao.getUid(auth.getName()));
		data.put("authority", dao.getAuthority(auth.getName()));
		constant.setConstant(Constant.SUCCESS, "로그인 성공 : SUCCESS", gson.toJson(data));
		String jsonString = new ObjectMapper().writeValueAsString(constant);
		response.addHeader("Content-Type", "text/html;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		out.write(jsonString.getBytes("UTF-8"));
	}
}
