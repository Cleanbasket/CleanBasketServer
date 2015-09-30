package com.bridge4biz.wash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridge4biz.wash.mybatis.DashBoardDAO;
import com.bridge4biz.wash.util.Constant;
import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/dash")
public class DashBoardController {
	
	@Autowired
	private DashBoardDAO dao;
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/daily/pickup/{limit}")
	@ResponseBody
	public Constant getItemList(Constant constant, Gson gson, @PathVariable int limit) {
		return constant.setConstant(Constant.SUCCESS, "", gson.toJson(dao.getDailyPickup(limit)));		
	} 
}
