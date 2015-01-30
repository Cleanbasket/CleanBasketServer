<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>CleanBasket</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href="./favicon.ico">
<link rel="stylesheet" href="./resources/cleanbasket/bootstrap.min.css">
<link rel="stylesheet" href="./resources/cleanbasket/non-responsive.css">
<link rel="stylesheet" href="./resources/cleanbasket/animate.min.css">
<link rel="stylesheet" href="./resources/cleanbasket/datepicker3.css">
<link rel="stylesheet" href="./resources/cleanbasket/bootstrap-select.css">
<link rel="stylesheet" href="./resources/cleanbasket/common.css">
</head>
<body>
	<header>
		<div class="navbar navbar-default navbar-fixed-top">
			<span class="nav-left">
				<button id="menu-button" type="button"></button>
			</span>
			<span class="nav-right">
				<button id="account-button" type="button" class="logout-state"></button>
				<a target="_blank" href="https://play.google.com/store/apps/details?id=com.bridge4biz.laundry" id="googleplay-button" type="button"></a>
				<a target="_blank" href="https://itunes.apple.com/kr/app/keulinbaseukes/id933165319?mt=8" id="appstore-button" type="button"></a>
			</span>
		</div>
	</header>
	<section>
		<article id="header"></article>
		<article id="intro"></article>
		<article id="qna"></article>
		<article id="order">
			<div id="order-title">주문하기</div>
			<div id="order-content">
				<div id="order-pickup-date">
					<div class="sub-title">수거일</div>
					<div class="input-group order-bar">
						<span style="width: 10%;" class="input-group-addon basic">
							<i class="icon-date"></i>
						</span>
						<input style="width: 100%;" class="input-group-addon date basic" readonly="readonly">
						<span style="width: 10%;" class="input-group-addon basic">
							<i class="icon-time"></i>
						</span>
						<select class="order-select-pickup">
						</select>
					</div>
				</div>
				<div id="order-dropoff-date">
					<div class="sub-title">배달일</div>
					<div class="input-group order-bar">
						<span style="width: 10%;" class="input-group-addon basic">
							<i class="icon-date"></i>
						</span>
						<input style="width: 100%;" class="input-group-addon date basic" readonly="readonly">
						<span style="width: 10%;" class="input-group-addon basic">
							<i class="icon-time"></i>
						</span>
						<select class="order-select-dropoff">
							<option value="10">10:00 ~ 11:00</option>
							<option value="11">11:00 ~ 12:00</option>
							<option value="12">12:00 ~ 13:00</option>
							<option value="13">13:00 ~ 14:00</option>
							<option value="14">14:00 ~ 15:00</option>
							<option value="15">15:00 ~ 16:00</option>
							<option value="16">16:00 ~ 17:00</option>
							<option value="17">17:00 ~ 18:00</option>
							<option value="18">18:00 ~ 19:00</option>
							<option value="19">19:00 ~ 20:00</option>
							<option value="20">20:00 ~ 21:00</option>
							<option value="21">21:00 ~ 22:00</option>
							<option value="22">22:00 ~ 23:00</option>
							<option value="23">23:00 ~ 24:00</option>
						</select>
					</div>
				</div>
				<div id="order-address">
					<div class="sub-title">주소</div>
					<div class="input-group order-bar order-select-bar">
						<span style="width: 75px;" class="input-group-addon basic">
							<i class="icon-place"></i>
						</span>
						<select class="order-select-address">
						</select>
					</div>
					<div style="padding-top: 10px;" class="order-bar">
						<span id="order-address-text" style="width: 750px; padding-left: 12px !important; text-align: left; border-radius: 4px;" class="input-group-addon basic"></span>
					</div>
				</div>
				<div id="order-phone">
					<div class="sub-title">연락처</div>
					<div class="order-bar">
						<span id="order-phone-text" style="width: 130px; border-radius: 4px;" class="input-group-addon basic"></span>
					</div>
				</div>
				<div id="order-ready" style="text-align: center;">
					<button id="button-order-ready" type="button" class="btn btn-info btn-lg">주문하기!!</button>
				</div>
			</div>
		</article>
		<article id="order-state">
			<div id="order-state-title">진행상태</div>
			<div id="order-state-content">
				<button id="buttton-order-history" class="btn btn-info">주문내역 / 취소</button>
				<div id="deliverer-area">
					<div id="deliverer-pic"></div>
					<div id="deliverer-info">
						<div id="deliverer-name">미지정</div>
						<div id="deliverer-date" style="margin-top: 5px;"></div>
					</div>
				</div>
			</div>
		</article>
		<article id="price-list">
			<div id="price-list-title">가격표</div>
			<div id="price-list-content">
				<div style="margin-bottom: 0px;" class="panel panel-default">
					<div style="font-size: 12px; color: #52c8b5; padding: 10px 16px 13px;" class="panel-heading">
						<div style="margin-bottom: 5px;">최소주문 : 10,000원</div>
						<div style="margin-bottom: 5px;">배달비 : 2,000원 (단, 20,000원 이상 주문시 무료)</div>
						<div>*특수재질은 추가비용 2,000원이 발생할 수 있습니다.</div>
					</div>
					<div class="panel-body">
						<ul style="margin-bottom: 0px;" class="list-group">
							<li class="list-group-item">
								<span style="color: #555">와이셔츠</span>
								<span style="color: #52c8b5; float: right;">2,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">정장(한벌)</span>
								<span style="color: #52c8b5; float: right;">6,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">바지/스커트</span>
								<span style="color: #52c8b5; float: right;">3,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">블라우스</span>
								<span style="color: #52c8b5; float: right;">4,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">원피스</span>
								<span style="color: #52c8b5; float: right;">7,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">코트</span>
								<span style="color: #52c8b5; float: right;">8,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">정상(상의)</span>
								<span style="color: #52c8b5; float: right;">3,500원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">정장(하의)</span>
								<span style="color: #52c8b5; float: right;">3,500원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">점퍼</span>
								<span style="color: #52c8b5; float: right;">7,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">자켓</span>
								<span style="color: #52c8b5; float: right;">4,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">스웨터</span>
								<span style="color: #52c8b5; float: right;">4,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">교복(한벌)</span>
								<span style="color: #52c8b5; float: right;">5,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">가죽점퍼(Short)</span>
								<span style="color: #52c8b5; float: right;">30,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">가죽점퍼(Long)</span>
								<span style="color: #52c8b5; float: right;">40,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">아웃도어 & 골프웨어</span>
								<span style="color: #52c8b5; float: right;">10,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">가디건 / 조끼</span>
								<span style="color: #52c8b5; float: right;">4,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">한복</span>
								<span style="color: #52c8b5; float: right;">15,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">넥타이 / 스카프 / 목도리</span>
								<span style="color: #52c8b5; float: right;">1,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">티셔츠</span>
								<span style="color: #52c8b5; float: right;">3,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">추가비용(실크, 마, 울, 캐쉬미어)</span>
								<span style="color: #52c8b5; float: right;">2,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">이불(더블)</span>
								<span style="color: #52c8b5; float: right;">15,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">이불(싱글)</span>
								<span style="color: #52c8b5; float: right;">10,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">이불(퀸)</span>
								<span style="color: #52c8b5; float: right;">18,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">이불 커버</span>
								<span style="color: #52c8b5; float: right;">8,000원</span>
							</li>
							<li class="list-group-item">
								<span style="color: #555">인조무스탕</span>
								<span style="color: #52c8b5; float: right;">15,000원</span>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</article>
		<article id="account">
			<div id="account-title">개인설정</div>
			<div id="account-content">
				<div id="account-privacy">
					<div class="sub-title">개인정보</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">이메일</span>
						<span id="account-email" style="width: 100%;" class="input-group-addon basic"></span>
					</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">연락처</span>
						<button id="account-phone" style="width: 100%;" class="input-group-addon btn btn-default basic"></button>
					</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">비밀번호</span>
						<button id="account-password" style="width: 100%;" class="input-group-addon btn btn-default basic">변경하기</button>
					</div>
				</div>
				<div id="account-address">
					<div style="margin-top: 40px;" class="sub-title">주소설정</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">집</span>
						<button id="address-home" style="width: 100%;" class="input-group-addon btn btn-default basic"></button>
					</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">회사</span>
						<button id="address-company" style="width: 100%;" class="input-group-addon btn btn-default basic"></button>
					</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">장소1</span>
						<button id="address-etc1" style="width: 100%;" class="input-group-addon btn btn-default basic"></button>
					</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">장소2</span>
						<button id="address-etc2" style="width: 100%;" class="input-group-addon btn btn-default basic"></button>
					</div>
					<div class="input-group account-bar">
						<span style="width: 20%;" class="input-group-addon basic">장소3</span>
						<button id="address-etc3" style="width: 100%;" class="input-group-addon btn btn-default basic"></button>
					</div>
				</div>
			</div>
		</article>
		<article id="coupon">
			<div id="coupon-title">쿠폰</div>
			<div id="coupon-content">
				<div class="sub-title">쿠폰발급</div>
				<div style="margin-bottom: 40px; margin-top: 20px;">
					<input id="coupon-issue-text" type="text" class="form-control" placeholder="쿠폰코드를 입력하세요.">
					<button id="button-coupon-issue" class="btn btn-info" type="button">쿠폰발급</button>
				</div>
				<div class="sub-title">쿠폰목록</div>
				<div style="margin-bottom: 0px; margin-top: 20px; padding: 15px; background-color: #fff;" class="well">
					<div id="coupon-list"></div>
					<div id="coupon-empty">보유중인 쿠폰이 없습니다.</div>
				</div>
			</div>
		</article>
	</section>
	<footer id="footer" class="footer">
		<div>
			<p>고객센터 : 070-7552-1385 (운영시간 10:00~24:00) &nbsp;E-mail : help@cleanbasket.co.kr</p>
			<p>(주)워시앱 코리아 &nbsp;대표 : 임수일 &nbsp;사업자번호 : 220-88-86968</p>
			<p>주소 : 서울 강남구 봉은사로24길 9 대암빌딩 104</p>
			<p>Copyright Washapp Korea in Yeoksam</p>
		</div>
	</footer>
	<script src="./resources/cleanbasket/jquery.min.js"></script>
	<script src="./resources/cleanbasket/bootstrap.min.js"></script>
	<script src="./resources/cleanbasket/jui.min.js"></script>
	<script src="./resources/cleanbasket/jquery.anchor.js"></script>
	<script src="./resources/cleanbasket/bootstrap-datepicker.js"></script>
	<script src="./resources/cleanbasket/locales/bootstrap-datepicker.kr.js"></script>
	<script src="./resources/cleanbasket/bootstrap-select.js"></script>
	<script src="./resources/cleanbasket/common.js"></script>
	<script>
		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
		ga('create', 'UA-55279233-2', 'auto');
		ga('send', 'pageview');
	</script>
</body>
</html>
