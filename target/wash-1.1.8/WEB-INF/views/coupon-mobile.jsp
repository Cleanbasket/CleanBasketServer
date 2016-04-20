<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>CleanBasket Coupon</title>
<meta property="og:url" content="http://www.cleanbasket.co.kr/coupon/${serial_number}" />
<meta property="og:title" content="CleanBasket Coupon" />
<meta property="og:description" content="친구추천 2000원 할인쿠폰" />
<meta property="og:image" content="http://www.cleanbasket.co.kr/coupon.jpg" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href="../favicon.ico">
<link rel="stylesheet" href="../resources/bootstrap.min.css">
<link rel="stylesheet" href="../resources/animate.min.css">
<body>
	<div style="margin-top: 25px; text-align: center;">
		<img src="../resources/images/logo.png">
	</div>
	<div style="margin: 25px; margin-bottom: 0px;" class="panel panel-default">
		<div style="text-align: center;" class="panel-heading">친구추천 2000원 할인쿠폰</div>
		<div style="text-align: center;" class="panel-body">
			<div style="margin-bottom: 10px;">아래 할인코드를 복사하여 사용하세요.</div>
			<h3 style="color: #5bc0de;">${serial_number}</h3>
		</div>
	</div>
	<div style="margin: 25px; margin-bottom: 0px;">
		<a class="btn btn-info btn-lg btn-block" target="_blank" href="https://play.google.com/store/apps/details?id=com.bridge4biz.laundry">할인쿠폰 사용하기</a>
	</div>
</body>
</html>