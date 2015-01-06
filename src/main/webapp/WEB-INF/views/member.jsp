<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>회원관리</title>
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
				<a class="navbar-brand" href="../admin/member" onfocus="this.blur()">CleanBasket</a>
			</div>
			<div class="navbar-collapse collapse">
				<div class="col-sm-4 col-md-5">
					<div class="navbar-form">
						<div class="input-group">
							<input type="text" id="search" class="form-control" placeholder="Search">
							<div class="input-group-btn">
								<button class="btn btn-default btn-search" type="button" onfocus="this.blur()" onclick="memberInfoSearch(); return false;">검색</button>
							</div>
						</div>
					</div>
				</div>
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href="../admin/order" onfocus="this.blur()">주문현황</a>
					</li>
					<li>
						<a href="../admin/pickup" onfocus="this.blur()">수거관리</a>
					</li>
					<li>
						<a href="../admin/dropoff" onfocus="this.blur()">배달관리</a>
					</li>
					<li class="active">
						<a href="../admin/member" onfocus="this.blur()">회원관리</a>
					</li>
					<li>
						<a href="../admin/deliverer" onfocus="this.blur()">배달자관리</a>
					</li>
					<li>
						<a href="../logout" onfocus="this.blur()">로그아웃</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">회원관리</div>
			<div class="panel-body">
				<table id="member" class="table table-hover">
					<thead class="member_thead">
						<tr>
							<th>순번</th>
							<th>이메일</th>
							<th>연락처</th>
							<th>누적주문금액</th>
							<th>주문내역</th>
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
	<script data-jui="#member" data-tpl="row" type="text/template">
		<tr>
			<td><!= rownum !></td>
			<td><!= email !></td>
			<td><!= phone !></td>
			<td><!= accruePrice !></td>
			<td><!= orderInfo !></td>
		</tr>
	</script>
	<script data-jui="#member" data-tpl="none" type="text/template">
		<tr>
			<td colspan="5" class="none">회원정보가 없습니다.</td>
		</tr>
	</script>
	<script data-jui="#detail" data-tpl="none" type="text/template">
		<tr>
			<td colspan="8" class="none">주문내역이 없습니다.</td>
		</tr>
	</script>
	<script data-jui="#member" data-tpl="expand" type="text/template">
		<div class="panel panel-default table_panel">
			<div id="detail-heading" class="panel-heading">주문내역</div>
 	 			<div class="panel-body">
					<table id="detail" class="table table-hover">
						<thead>
							<tr>
								<th>주문번호</th>
								<th>수거 예정일</th>
								<th>배달 예정일</th>
								<th>주소</th>
								<th>가격</th>
								<th>품목</th>
								<th>쿠폰</th>
								<th>비고</th>
							</tr>
						</thead>
					<tbody></tbody>
					</table>
					<div class="row" style="text-align: center">
						<ul id="paging" class="pagination pagination-sm"></ul>
					</div>
				</div>
			</div>
		</div>
	</script>
	<script data-jui="#detail" data-tpl="row" type="text/template">
		<tr>
			<td><!= order_number !></td>
			<td><!= pickup_date !></td>
			<td><!= dropoff_date !></td>
			<td><!= address !></td>
			<td><!= price !></td>
			<td><!= item !></td>
			<td><!= coupon !></td>
			<td><!= note !></td>
		</tr>
	</script>
	<script data-jui="#paging" data-tpl="pages" type="text/template">
		<li class="prev"><a href="#">&laquo;</a></li>
			<! for(var i = 0; i < pages.length; i++) { !>
				<li class="page"><a href="#"><!= pages[i] !></a></li>
			<! } !>
		<li class="next"><a href="#">&raquo;</a></li>
	</script>
	<script>
		var constant = new Constant();

		var global_uix = null;
		var global_ui = null;

		jui.ready(function(ui, uix, _) {
			global_ui = ui;
			global_uix = uix;
			member = uix.table("#member", {
				animate : true,
				expand : true,
				expandEvent : false,
				event : {
					colresize : function() {
						memberResize();
					}
				}
			});
			memberResize();
		});

		$(window).ready(function() {
			socket = webSocketIO();
			socket.on('message', function(msg) {
				receiveMessage(msg);
			});
			$('#search').keypress(function(event) {
				if (event.keyCode == 13) {
					memberInfoSearch();
				}
			});
			getMemberInfo();
		});

		function receiveMessage(msg) {
			switch (msg.constant) {
			case constant.PUSH_MEMBER_JOIN:
				getMemberInfoAppend($('#search').val(), msg.uid);
				break;
			case constant.PUSH_ORDER_ADD:
				var data_uid = $('#detail-heading').attr('data-uid');
				if (data_uid == msg.uid) {
					getDetailInfo(msg.uid);
				}
				break;
			case constant.PUSH_ORDER_CANCEL:
				var data_uid = $('#detail-heading').attr('data-uid');
				if (data_uid == msg.uid) {
					getDetailInfo(msg.uid);
				}
				break;
			}
		}

		function getMemberInfoAppend(search, uid) {
			if (search == undefined) {
				search = "";
			}
			$.ajax({
				type : 'POST',
				url : '../admin/member/append',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					search : search,
					uid : uid
				}),
				success : function(json) {
					setMemberInfoAppend(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function getMemberInfo(search) {
			if (search == undefined) {
				search = "";
			}
			$.ajax({
				type : 'POST',
				url : '../admin/member',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					search : search
				}),
				success : function(json) {
					setMemberInfo(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function getDetailInfo(uid) {
			$.ajax({
				type : 'POST',
				url : '../admin/member/order',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					uid : uid
				}),
				success : function(json) {
					setDetailInfo(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function memberInfoSearch() {
			getMemberInfo($('#search').val());
		}

		function setMemberButtonEvent(data) {
			var order_link = $('.order_link').unbind('click');
			order_link.click(function() {
				var clicked = $(this);
				var key = Number(clicked.closest('tr').children('td:eq(0)').text());
				var expand = member.getExpand();
				if (expand == null || key != expand.data.rownum) {

					var index = key - 1;
					member.showExpand(index);
					clicked.closest('tbody').children().removeClass('active');
					clicked.closest('tr').addClass('active');
					$('#detail-heading').text(clicked.attr("data-email") + " 회원님의 주문내역").css('font-weight', 'bold').attr('data-uid', clicked.attr('id'));

					detail = global_uix.xtable("#detail", {
						animate : true,
						buffer : "page",
						bufferCount : 12,
						event : {
							colresize : function() {
								detailResize();
							}
						}
					});

					paging = global_ui.paging("#paging", {
						pageCount : 12,
						screenCount : 9,
						event : {
							page : function(pNo) {
								detail.page(pNo);
							}
						}
					});

					$('.pagination').css('margin', '0px').css('margin-top', '12px');
					detailResize();

					getDetailInfo(clicked.attr('id'));

				} else {
					clicked.closest('tr').removeClass('active');
					member.hideExpand();
				}
			});
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
						oid : clicked.attr('id'),
						uid : clicked.attr('data-uid')
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
						var result = total_price + Number(clicked.attr('data-dropoff-price')) - Number(clicked.attr('data-coupon'));
						list_group.append('<a class="list-group-item sum"><h6 class="list-group-item-heading">총 금액 : ' + total_price + '원 (' + total_count
								+ '개)</h6><h6 class="list-group-item-heading">배송료 : ' + clicked.attr('data-dropoff-price') + '원</h6><h6 class="list-group-item-heading">쿠폰할인 : -'
								+ clicked.attr('data-coupon') + '원</h6></h6><h6 class="list-group-item-heading">종합 : ' + result + '원</h6></a>');
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
						oid : clicked.attr('id'),
						uid : clicked.attr('data-uid')
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
						errorCheck(request.responseText);
					}
				});
			});

			var detail_note_link = $('.detail_note_link').unbind('click');
			detail_note_link.click(function() {
				var clicked = $(this);
				var textarea = $('<textarea id="note-editer" class="form-control" rows="10" style="resize: none;" disabled></textarea>');
				bootbox.dialog({
					title : "비고를 작성 및 확인합니다.",
					message : textarea,
					buttons : {
						ok : {
							label : "확인",
							className : "btn-success",
							callback : function() {
								updateOrderNote(clicked.attr('id'), clicked.attr('data-uid'), textarea.text());
							}
						},
						cancel : {
							label : "취소",
							className : "btn-default"
						}
					}
				});

				$.ajax({
					type : 'POST',
					url : '../admin/member/note',
					dataType : 'json',
					contentType : "application/json",
					async : true,
					data : JSON.stringify({
						oid : clicked.attr('id'),
						uid : clicked.attr('data-uid')
					}),
					success : function(json) {
						var note = JSON.parse(json.data);
						textarea.text(note);
					},
					error : function(request, status, error) {
						console.log(request.responseText);
						errorCheck(request.responseText);
					},
					complete : function() {
						textarea.removeAttr('disabled');
					}
				});
			});

		}

		function updateOrderNote(oid, uid, note) {
			$.ajax({
				type : 'POST',
				url : '../admin/member/note/update',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					oid : oid,
					uid : uid,
					note : note
				}),
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function setMemberInfo(json) {
			var data = JSON.parse(json.data);
			var memberInfoSize = data.length;
			for (var i = 0; i < memberInfoSize; i++) {
				data[i]['orderInfo'] = '<a href="" onclick="return false" id="' + data[i].uid + '" data-email="' + data[i].email + '" onfocus="this.blur()" class="order_link">주문내역 확인</a>';
			}
			member.update(data);
			setMemberButtonEvent();
		}

		function setMemberInfoAppend(json) {
			var data = JSON.parse(json.data);
			var memberInfoSize = data.length;
			for (var i = 0; i < memberInfoSize; i++) {
				data[i].rownum = member.count() + 1;
				data[i]['orderInfo'] = '<a href="" onclick="return false" id="' + data[i].uid + '" data-email="' + data[i].email + '" onfocus="this.blur()" class="order_link">주문내역 확인</a>';
			}
			member.append(data);
			setMemberButtonEvent();
		}

		function setDetailInfo(json) {
			var data = JSON.parse(json.data);
			var detailInfoSize = data.length;
			var tempAddress = null;
			for (var i = 0; i < detailInfoSize; i++) {
				if (data[i].addr_number != "") {
					tempAddress = data[i].address + " " + data[i].addr_number;
				} else {
					tempAddress = data[i].address + " " + data[i].addr_building;
				}
				var couponSize = data[i].coupon.length;
				var coupon = 0;
				for (var j = 0; j < couponSize; j++) {
					if (data[i].coupon[j].type == 0) {
						coupon += data[i].coupon[j].value;
					} else {
						coupon += data[i].coupon[j].value;
					}
				}
				data[i].address = '<a target="_blank" onfocus="this.blur()" href="http://map.naver.com/?query=' + tempAddress + '">' + tempAddress + " " + data[i].addr_remainder + '</a>';
				data[i]['item'] = '<a href="" onclick="return false" id="' + data[i].oid + '" data-uid="' + data[i].uid + '" data-coupon="' + coupon + '" data-dropoff-price="' + data[i].dropoff_price
						+ '" onfocus="this.blur()" class="detail_item_link">품목확인</a>';
				data[i]['coupon'] = '<a href="" onclick="return false" id="' + data[i].oid + '" data-uid="' + data[i].uid + '" onfocus="this.blur()" class="detail_coupon_link">쿠폰확인</a>';
				data[i]['note'] = '<a href="" onclick="return false" id="' + data[i].oid + '" data-uid="' + data[i].uid + '" onfocus="this.blur()" class="detail_note_link">비고확인</a>';
			}
			detail.update(data);
			paging.reload(detail.count());
			setDetailButtonEvent();
		}

		function memberResize() {
			var th = $('.member_thead').find('th');
			th.eq(0).css('width', '10%');
			th.eq(1).css('width', '30%');
			th.eq(2).css('width', '20%');
			th.eq(3).css('width', '20%');
			th.eq(4).css('width', '20%');
		}

		function detailResize() {
			var head_th = $('.head').find('th');
			head_th.eq(0).css('width', '11%');
			head_th.eq(1).css('width', '12%');
			head_th.eq(2).css('width', '12%');
			head_th.eq(3).css('width', '40%');
			head_th.eq(4).css('width', '7%');
			head_th.eq(5).css('width', '6%');
			head_th.eq(6).css('width', '6%');
			head_th.eq(7).css('width', '6%');

			var body_th = $('.body').find('th');
			body_th.eq(0).css('width', '11%');
			body_th.eq(1).css('width', '12%');
			body_th.eq(2).css('width', '12%');
			body_th.eq(3).css('width', '40%');
			body_th.eq(4).css('width', '7%');
			body_th.eq(5).css('width', '6%');
			body_th.eq(6).css('width', '6%');
			head_th.eq(7).css('width', '6%');
		}
	</script>
</body>
</html>