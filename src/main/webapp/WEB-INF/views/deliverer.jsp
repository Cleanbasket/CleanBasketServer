<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>배달자관리</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="../favicon.ico">
<link rel="stylesheet" href="../resources/bootstrap.min.css">
<link rel="stylesheet" href="../resources/animate.min.css">
<link rel="stylesheet" href="../resources/common.css">
</head>
<body>
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="../admin/deliverer" onfocus="this.blur()">CleanBasket</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="../admin/order" onfocus="this.blur()">주문현황</a></li>
					<li><a href="../admin/pickup" onfocus="this.blur()">수거관리</a></li>
					<li><a href="../admin/dropoff" onfocus="this.blur()">배달관리</a></li>
					<li><a href="../admin/member" onfocus="this.blur()">회원관리</a></li>
					<li class="active"><a href="../admin/deliverer" onfocus="this.blur()">배달자관리</a></li>
					<li><a href="../admin/area" onfocus="this.blur()">지역관리</a></li>															
					<li><a href="../logout" onfocus="this.blur()">로그아웃</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">배달자 목록</div>
			<div class="panel-body">
				<table id="deliverer" class="table table-hover">
					<thead class="thead">
						<tr>
							<th>순번</th>
							<th>이메일</th>
							<th>이름</th>
							<th>생년월일</th>
							<th>연락처</th>
							<th>사진</th>
							<th>계정상태</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	<script src="../resources/jquery.min.js"></script>
	<script src="../resources/bootstrap.min.js"></script>
	<script src="../resources/bootbox.min.js"></script>
	<script src="../resources/jui.min.js"></script>
	<script src="../resources/socket.io.js"></script>
	<script src="../resources/common.js"></script>
	<script data-jui="#deliverer" data-tpl="row" type="text/template">
		<tr>
			<td><!= rownum !></td>
			<td><!= email !></td>
			<td><!= name !></td>
			<td><!= birthday !></td>
			<td><!= phone !></td>
			<td><!= img !></td>
			<td><!= enabled !></td>
		</tr>
	</script>
	<script data-jui="#deliverer" data-tpl="none" type="text/template">
		<tr>
			<td colspan="7" class="none">데이터가 없습니다.</td>
		</tr>
	</script>
	<script>
		var constant = new Constant();

		jui.ready(function(ui, uix, _) {
			deliverer = uix.table("#deliverer", {
				animate : true,
				event : {
					colresize : function() {
						theadResize();
					}
				}
			});
			theadResize();
		});

		$(window).ready(function() {
			socket = webSocketIO();
			socket.on('message', function(msg) {
				receiveMessage(msg);
			});
			getDeliverer();
		});

		function receiveMessage(msg) {
			switch (msg.constant) {
			case constant.PUSH_CHANGE_ACCOUNT_ENABLED:
				getDeliverer();
				break;
			case constant.PUSH_DELIVERER_JOIN:
				getDeliverer();
				break;
			}
		}

		function sendMessage(msg) {
			try {
				socket.emit('message', msg);
			} catch (e) {
				alert(e);
			}
		}

		function getDeliverer() {
			$.ajax({
				type : 'POST',
				url : '../admin/deliverer/manage',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				success : function(json) {
					setDeliverer(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function setButtonEvent() {
			var account = $("a[class^='account']").unbind('click');
			account.click(function() {
				var clicked = $(this);
				if (clicked.hasClass('account-enabled')) {
					$.ajax({
						type : 'POST',
						url : '../admin/deliverer/enabled/update',
						dataType : 'json',
						contentType : "application/json",
						async : true,
						data : JSON.stringify({
							uid : clicked.attr('id'),
							enabled : false
						}),
						success : function(json) {
							switch (json.constant) {
							case constant.SUCCESS:
								buttonDisable(clicked);
								sendMessage({
									constant : constant.PUSH_CHANGE_ACCOUNT_ENABLED,
									uid : clicked.attr('id')
								});
								break;
							}
						},
						error : function(request, status, error) {
							console.log(request.responseText);
							errorCheck(request.responseText);
						}
					});
				} else {
					$.ajax({
						type : 'POST',
						url : '../admin/deliverer/enabled/update',
						dataType : 'json',
						contentType : "application/json",
						async : true,
						data : JSON.stringify({
							uid : clicked.attr('id'),
							enabled : true
						}),
						success : function(json) {
							switch (json.constant) {
							case constant.SUCCESS:
								buttonEnable(clicked);
								sendMessage({
									constant : constant.PUSH_CHANGE_ACCOUNT_ENABLED,
									uid : clicked.attr('id')
								});
								break;
							}
						},
						error : function(request, status, error) {
							console.log(request.responseText);
							errorCheck(request.responseText);
						}
					});
				}
			});
		}

		function setDeliverer(json) {
			var data = JSON.parse(json.data);
			var dataSize = data.length;
			var img = null;
			var enabled = false;
			for (var i = 0; i < dataSize; i++) {
				img = data[i].img;
				data[i].img = '<a href="../' + img + '" target="_blank" onfocus="this.blur()">사진확인</a>';
				enabled = data[i].enabled;
				if (enabled) {
					data[i].enabled = '<a href="" onclick="return false" onfocus="this.blur()" id="' + data[i].uid + '" title="사용가능한 상태이며 클릭하면 비활성화 할 수 있습니다." class="account-enabled">활성화 상태</a>';
				} else {
					data[i].enabled = '<a href="" onclick="return false" onfocus="this.blur()" id="' + data[i].uid + '" title="사용할 수 없는 상태이며 클릭하면 활성화 할 수 있습니다." class="account-disabled">비활성화 상태</a>';
				}
			}
			deliverer.update(data);
			setButtonEvent();
		}

		function buttonEnable(clicked) {
			clicked.removeClass('account-disabled');
			clicked.addClass('account-enabled');
			clicked.attr('title', '사용가능한 상태이며 클릭하면 비활성화 할 수 있습니다.');
			clicked.text('활성화 상태');
		}

		function buttonDisable(clicked) {
			clicked.removeClass('account-enabled');
			clicked.addClass('account-disabled');
			clicked.attr('title', '사용할 수 없는 상태이며 클릭하면 활성화 할 수 있습니다.');
			clicked.text('비활성화 상태');
		}

		function theadResize() {
			var th = $('.thead').find('th');
			th.eq(0).css('width', '5%');
			th.eq(1).css('width', '17%');
			th.eq(2).css('width', '17%');
			th.eq(3).css('width', '17%');
			th.eq(4).css('width', '17%');
			th.eq(5).css('width', '17%');
			th.eq(6).css('width', '22%');
		}
	</script>
</body>
</html>