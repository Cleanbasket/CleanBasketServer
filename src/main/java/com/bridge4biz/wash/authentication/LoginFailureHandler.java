package com.bridge4biz.wash.authentication;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginFailureHandler implements AuthenticationFailureHandler {

	private MybatisDAO dao;
	private Constant constant = new Constant();

	@Autowired
	private LoginFailureHandler(MybatisDAO dao) {
		this.dao = dao;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		Integer reason = dao.loginFailureCheck(request.getParameter("email"), request.getParameter("password"));
		System.out.println(request.getParameter("email") + request.getParameter("password"));
		switch (reason) {
		case Constant.ACCOUNT_DISABLED:
			constant.setConstant(Constant.ACCOUNT_DISABLED, "계정정보가 비활성화 되어있습니다. 관리자에게 문의하세요. : ACCOUNT_DISABLED");
			break;
		case Constant.EMAIL_ERROR:
			constant.setConstant(Constant.EMAIL_ERROR, "이메일 불일치 : EMAIL_ERROR");
			break;
		case Constant.PASSWORD_ERROR:
			constant.setConstant(Constant.PASSWORD_ERROR, "비밀번호 불일치 : PASSWORD_ERROR");
			break;
		case Constant.ERROR:
			constant.setConstant(Constant.ERROR, "DB에러... : ERROR");
			break;
		}

		String jsonString = new ObjectMapper().writeValueAsString(constant);
		response.addHeader("Content-Type", "text/html;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		out.write(jsonString.getBytes("UTF-8"));
	}

}