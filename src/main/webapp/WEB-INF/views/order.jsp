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
					<li><a href="../admin/area" onfocus="this.blur()">지역관리</a></li>																									
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
					<thead class="order_thead">
						<tr>
							<th>주문번호</th>
							<th>이메일</th>
							<th>주소</th>
							<th>연락처</th>
							<th>수거일자</th>
							<th>배달일자</th>
							<th>가격</th>							
							<th>수량</th>		
							<th>품목</th>																			
							<th>쿠폰내역</th>
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
			<td><!= pickup_date !></td>
			<td><!= dropoff_date !></td>
			<td><!= price !></td>
			<td><!= count !></td>
			<td><!= item !></td>
			<td><!= orderInfo !></td>
			<td><!= state !></td>
		</tr>
	</script>
	<script data-jui="#order" data-tpl="none" type="text/template">
		<tr>
			<td colspan="10" class="none">데이터가 없습니다.</td>
		</tr>
	</script>
	<script data-jui="#order" data-tpl="expand" type="text/template">
		<div class="memo">
			<label class="text text-small">메모</label>		
			<label class="text text-small"><!= memo !></label>
		</div>
	</script>

	<script>
		var constant = new Constant();

		var global_uix = null;
		var global_ui = null;
		
		jui.ready(function(ui, uix, _) {
			order = uix.table("#order", {
				animate : true,
				expand : true
			});
			
			orderResize();
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
			startRefresher();
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
				data.stateData[i]['orderInfo'] = '<a href="" onclick="return false" id="' + data.stateData[i].uid + '" data-email="' + data.stateData[i].email + '" oid="' + data.stateData[i].oid + '" onfocus="this.blur()" class="detail_coupon_link">쿠폰내역</a>';
				data.stateData[i]['item'] = '<a href="" onclick="return false" id="' + data.stateData[i].uid + '" data-dropoff-price="' + data.stateData[i].dropoff_price + '" data-email="' + data.stateData[i].email + '" oid="' + data.stateData[i].oid + '" onfocus="this.blur()" class="detail_item_link">품목</a>';
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
			setDetailButtonEvent();
		}
		
		function setDetailButtonEvent() {
			var detail_item_link = $('.detail_item_link').unbind('click');
			detail_item_link.click(function() {
				var clicked = $(this);
				var list_group = $('<div />', {
					'class' : 'list-group'
				});
				bootbox.dialog({
					title : "품목 내용을 확인합니다.",
					message : list_group,
					buttons : {
						ok : {
							label : "확인",
							className : "btn-success"
						}
					}
				});

				$.ajax({
					type : 'POST',
					url : '../admin/member/item',
					dataType : 'json',
					contentType : "application/json",
					async : true,
					data : JSON.stringify({
						oid : clicked.attr('oid'),
						uid : clicked.attr('id')
					}),
					success : function(json) {
						var data = JSON.parse(json.data);
						var datalength = data.length;
						var heading = null;
						var price = 0;
						var total_price = 0;
						var total_count = 0;
						for (var i = 0; i < datalength; i++) {
							heading = '[' + data[i].price + '] ' + data[i].name + ' ' + data[i].count + '개';
							price = data[i].price * data[i].count;
							total_price += price;
							total_count += data[i].count;
							list_group.append('<a class="list-group-item"><h6 class="list-group-item-heading">' + heading + '</h6><p class="list-group-item-text">금액: ' + price + '원</p></a>');
						}
					},
					error : function(request, status, error) {
						console.log(request.responseText);
						errorCheck(request.responseText);
					}
				});

			});
			
			var detail_coupon_link = $('.detail_coupon_link').unbind('click');
			detail_coupon_link.click(function() {
				var clicked = $(this);
				var list_group = $('<div />', {
					'class' : 'list-group'
				});
				bootbox.dialog({
					title : "쿠폰 사용내역을 확인합니다.",
					message : list_group,
					buttons : {
						ok : {
							label : "확인",
							className : "btn-success"
						}
					}
				});

				$.ajax({
					type : 'POST',
					url : '../admin/member/coupon',
					dataType : 'json',
					contentType : "application/json",
					async : true,
					data : JSON.stringify({
						oid : clicked.attr('oid'),
						uid : clicked.attr('id')
					}),
					success : function(json) {
						var data = JSON.parse(json.data);
						var datalength = data.length;
						var discount_price = 0;
						if (datalength != 0) {
							for (var i = 0; i < datalength; i++) {
								if (data[i].type == 0) {
									discount_price = data[i].value;
								} else {
									discount_price = data[i].value;
								}
								list_group.append('<a class="list-group-item"><h6 class="list-group-item-heading">쿠폰이름 : ' + data[i].name + '</h6><p class="list-group-item-text">할인금액: '
										+ discount_price + '원</p></a>');
							}
						} else {
							list_group.append('사용된 쿠폰이 없습니다.');
						}
					},
					error : function(request, status, error) {
						console.log(request.responseText);
					}
				});
			});
		}
		
		function startRefresher() {
			$(window).ready(function() {
				setInterval(function() {
					$.ajax({
						url : '../admin/refresh',
						async : true,
						success : function(json) {
							var data = JSON.parse(json);
							var num = $("#order td")[1];
							var oid = num.outerText.split('-')[1];
							if(parseInt(data.data) > parseInt(oid)) {
								playSound();
							};
						},
						error : function(request, status, error) {
							console.log(request.responseText);
						}
					});
				}, 30000);
			});
		}
		
		function playSound() {
			var snd = new Audio('../resources/sounds/alarm.mp3'); // buffers automatically when created
			snd.play();
		}
		
		function orderResize() {
			var th = $('.order_thead').find('th');
			th.eq(0).css('width', '10%');
			th.eq(1).css('width', '10%');
			th.eq(2).css('width', '30%');
			th.eq(3).css('width', '10%');
			th.eq(4).css('width', '7%');
			th.eq(5).css('width', '7%');
			th.eq(6).css('width', '5%');
			th.eq(7).css('width', '4%');
			th.eq(8).css('width', '5%');
			th.eq(9).css('width', '6%');
			th.eq(10).css('width', '6%');
		}
	</script>
</body>
</html>