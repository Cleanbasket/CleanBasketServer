<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>배달관리</title>
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
				<a class="navbar-brand" href="../admin/dropoff" onfocus="this.blur()">CleanBasket</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="../admin/order" onfocus="this.blur()">주문현황</a></li>
					<li><a href="../admin/pickup" onfocus="this.blur()">수거관리</a></li>
					<li class="active"><a href="../admin/dropoff" onfocus="this.blur()">배달관리</a></li>
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
				배달관리
				<ul style="float: right;" class="list-inline">
					<li>배정완료 주문<span id="complete-count" class="badge state-count">0</span></li>
					<li>미배정 주문<span id="incomplete-count" class="badge state-count">0</span></li>
				</ul>
			</div>
			<div class="panel-body">
				<table id="dropoff" class="table table-hover">
					<thead>
						<tr>
							<th>주문번호</th>
							<th>배달 예정일</th>
							<th>이메일</th>
							<th>주소</th>
							<th>연락처</th>
							<th>수량</th>
							<th>가격</th>
							<th>배정하기</th>
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
	<script data-jui="#dropoff" data-tpl="row" type="text/template">
		<tr>
			<td><!= order_number !></td>
			<td><!= dropoff_date !></td>
			<td><!= email !></td>
			<td><!= address !></td>
			<td><!= phone !></td>
			<td><!= count !></td>
			<td><!= price !></td>
			<td><!= assign !></td>
		</tr>
	</script>
	<script data-jui="#dropoff" data-tpl="none" type="text/template">
		<tr>
			<td colspan="8" class="none">데이터가 없습니다.</td>
		</tr>
	</script>
	<script>
		var constant = new Constant();

		jui.ready(function(ui, uix, _) {
			dropoff = uix.table("#dropoff", {
				animate : true
			});
		});

		$(window).ready(function() {
			socket = webSocketIO();
			socket.on('message', function(msg) {
				receiveMessage(msg);
			});
			getDropoffState();
		});

		function receiveMessage(msg) {
			switch (msg.constant) {
			case constant.PUSH_ASSIGN_DROPOFF:
				getDropoffState();
				break;
			case constant.PUSH_DROPOFF_COMPLETE:
				getDropoffState();
				break;
			case constant.PUSH_PICKUP_COMPLETE:
				getDropoffState();
				break;
			}
		}

		function sendMessage(msg) {
			socket.emit('message', msg);
		}

		function getDropoffState() {
			$.ajax({
				type : 'POST',
				url : '../admin/dropoff',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				success : function(json) {
					setDropoffState(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function setButtonEvent() {

			var unassign = $('.unassign').unbind('click');
			unassign.click(function() {

				var clicked = $(this);

				$.ajax({
					type : 'POST',
					url : '../admin/deliverer',
					dataType : 'json',
					contentType : "application/json",
					async : true,
					success : function(json) {
						var deliverer = JSON.parse(json.data);

						var picker = $('<select />', {
							'class' : 'form-control',
							id : 'select_box'
						});

						for (var i = 0; i < deliverer.length; i++) {
							var text = deliverer[i].name + ', ' + deliverer[i].phone + ', ' + deliverer[i].email;
							picker.append($('<option />').text(text).val(deliverer[i].uid));
						}

						bootbox.dialog({
							title : "배달자 배정",
							message : picker,
							buttons : {
								ok : {
									label : "확인",
									className : "btn-success",
									callback : function() {
										var uid = $("#select_box option:selected").val();
										var oid = clicked.attr('id')
										setDropoffMan(uid, oid);
									}
								},
								cancel : {
									label : "취소",
									className : "btn-default"
								}
							}
						});
					},
					error : function(request, status, error) {
						console.log(request.responseText);
						errorCheck(request.responseText);
					}
				});

			});

			var assign = $('.assign').unbind('click');
			assign.click(function() {

			});
		}

		function setDropoffMan(uid, oid) {
			$.ajax({
				type : 'POST',
				url : '../admin/dropoff/assign',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					uid : uid,
					oid : oid
				}),
				success : function(json) {
					switch (json.constant) {
					case constant.SUCCESS:
						getDropoffState();
						sendMessage({
							constant : constant.PUSH_ASSIGN_DROPOFF,
							uid : uid,
							oid : oid
						});
						break;
					case constant.ERROR:
						var data = {
							msg : "<strong>처리 중 오류가 발생하였습니다.",
							color : "danger"
						};
						notify.add(data);
						break;
					}
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function setDropoffState(json) {
			console.log(json.data);
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
				case 2:
					data.stateData[i]['assign'] = '<a href="" onclick="return false" id="' + data.stateData[i].oid + '" onfocus="this.blur()" class="unassign">배정</a>';
					break;
				case 3:
					data.stateData[i]['assign'] = '<a href="" onclick="return false" id="' + data.stateData[i].oid + '" onfocus="this.blur()" class="assign">완료</a>';
					break;
				}
			}
			dropoff.update(data.stateData);
			setButtonEvent();
		}
	</script>
</body>
</html>