<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>주문현황</title>
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
				<a class="navbar-brand" href="../admin/order" onfocus="this.blur()">CleanBasket</a>
			</div>
			<div class="navbar-collapse collapse">
				<div class="col-sm-4 col-md-5">
					<div class="navbar-form">
						<div class="input-group">
							<input type="text" id="search" class="form-control" placeholder="Search">
							<div class="input-group-btn">
								<button class="btn btn-default btn-search" type="button" onfocus="this.blur()" onclick="orderStateSearch(); return false;">검색</button>
							</div>
						</div>
					</div>
				</div>
				<ul class="nav navbar-nav navbar-right">
					<li class="active"><a href="../admin/order" onfocus="this.blur()">주문현황</a></li>
					<li><a href="../admin/pickup" onfocus="this.blur()">수거관리</a></li>
					<li><a href="../admin/dropoff" onfocus="this.blur()">배달관리</a></li>
					<li><a href="../admin/member" onfocus="this.blur()">회원관리</a></li>
					<li><a href="../admin/deliverer" onfocus="this.blur()">배달자관리</a></li>
					<li><a href="../logout" onfocus="this.blur()">로그아웃</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">
				주문현황
				<ul style="float: right;" class="list-inline">
					<li>주문 완료<span id="complete-count" class="badge state-count">0</span></li>
					<li>주문 미완료<span id="incomplete-count" class="badge state-count">0</span></li>
				</ul>
			</div>
			<div class="panel-body">
				<table id="order" class="table table-hover">
					<thead>
						<tr>
							<th>주문번호</th>
							<th>이메일</th>
							<th>주소</th>
							<th>연락처</th>
							<th>수량</th>
							<th>가격</th>
							<th>주문현황</th>
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
	<script data-jui="#order" data-tpl="row" type="text/template">
		<tr>
			<td><!= order_number !></td>
			<td><!= email !></td>
			<td><!= address !></td>
			<td><!= phone !></td>
			<td><!= count !></td>
			<td><!= price !></td>
			<td><!= state !></td>
		</tr>
	</script>
	<script data-jui="#order" data-tpl="none" type="text/template">
		<tr>
			<td colspan="7" class="none">데이터가 없습니다.</td>
		</tr>
	</script>
	<script>
		var constant = new Constant();

		jui.ready(function(ui, uix, _) {
			order = uix.table("#order", {
				animate : true
			});
		});

		$(window).ready(function() {
			socket = webSocketIO();
			socket.on('message', function(msg) {
				receiveMessage(msg);
			});
			$('#search').keypress(function(event) {
				if (event.keyCode == 13) {
					orderStateSearch();
				}
			});
			getOrderState();
		});

		function receiveMessage(msg) {
			switch (msg.constant) {
			case constant.PUSH_ORDER_ADD:
				getPickupState($('#search').val());
				break;
			case constant.PUSH_ORDER_CANCEL:
				getPickupState($('#search').val());
				break;
			case constant.PUSH_ASSIGN_PICKUP:
				getPickupState($('#search').val());
				break;
			case constant.PUSH_ASSIGN_DROPOFF:
				getPickupState($('#search').val());
				break;
			case constant.PUSH_DROPOFF_COMPLETE:
				getPickupState($('#search').val());
				break;
			case constant.PUSH_PICKUP_COMPLETE:
				getPickupState($('#search').val());
				break;
			}
		}

		function getOrderState(search) {
			if (search == undefined) {
				search = "";
			}
			$.ajax({
				type : 'POST',
				url : '../admin/order',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					search : search
				}),
				success : function(json) {
					setOrderState(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function orderStateSearch() {
			getOrderState($('#search').val());
		}

		function setOrderState(json) {
			var data = JSON.parse(json.data);
			$('#complete-count').text(data.complete);
			$('#incomplete-count').text(data.incomplete);
			var stateDataSize = data.stateData.length;
			var tempState = 0;
			var tempAddress = null;
			for (var i = 0; i < stateDataSize; i++) {
				if (data.stateData[i].addr_number != "") {
					tempAddress = data.stateData[i].address + " " + data.stateData[i].addr_number;
				} else {
					tempAddress = data.stateData[i].address + " " + data.stateData[i].addr_building;
				}
				data.stateData[i].address = '<a target="_blank" onfocus="this.blur()" href="http://map.naver.com/?query=' + tempAddress + '">' + tempAddress + " " + data.stateData[i].addr_remainder
						+ '</a>';
				tempState = data.stateData[i].state;
				switch (tempState) {
				case 0:
					data.stateData[i].state = "수거요청"
					break;
				case 1:
					data.stateData[i].state = "수거요청"
					break;
				case 2:
					data.stateData[i].state = "수거완료"
					break;
				case 3:
					data.stateData[i].state = "배달대기"
					break;
				case 4:
					data.stateData[i].state = "배달완료"
					break;
				}
			}
			order.update(data.stateData);
		}
	</script>
</body>
</html>