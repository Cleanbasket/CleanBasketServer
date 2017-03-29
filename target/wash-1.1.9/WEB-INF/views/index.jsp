<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<%
  // 헤더에서 스마트폰 여부확인 후 리다이렉트
  String browser = request.getHeader("User-Agent"); // 브라우저 구해오기

  if (browser.indexOf("Android") > 0 || browser.indexOf("iPhone") > 0){
    response.sendRedirect("m_main"); // 모바일 버전
  } else {
    response.sendRedirect("main"); // 데스크탑 버전
  }

%>