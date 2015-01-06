package com.bridge4biz.wash.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.util.SocketIO;

public class ServletContextLifeCycle implements ServletContextListener {

	@Autowired
	private MybatisDAO dao;

	@Override
	public void contextInitialized(ServletContextEvent sEvent) {
		WebApplicationContextUtils.getRequiredWebApplicationContext(sEvent.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
		dao.selectTest();
		new SocketIO();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sEvent) {
		SocketIO.stop();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver d = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(d);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}