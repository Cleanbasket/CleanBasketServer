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
				<a class="navbar-brand" href="../admin/area" onfocus="this.blur()">CleanBasket</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="../admin/order" onfocus="this.blur()">주문현황</a></li>
					<li><a href="../admin/pickup" onfocus="this.blur()">수거관리</a></li>
					<li><a href="../admin/dropoff" onfocus="this.blur()">배달관리</a></li>
					<li><a href="../admin/member" onfocus="this.blur()">회원관리</a></li>
					<li><a href="../admin/deliverer" onfocus="this.blur()">배달자관리</a></li>
					<li class="active"><a href="../admin/area" onfocus="this.blur()">지역관리</a></li>					
					<li><a href="../logout" onfocus="this.blur()">로그아웃</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">지역관리</div>
			<div class="panel-body">
				<table id="area" class="table table-hover">
					<thead class="area_thead">
						<tr>
							<th>No.</th>
							<th>지역코드</th>
							<th>지역</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div class="add_area">
		<div class="area-input-group">	
			<input type="text" id="areacode_text" class="input input-large input-rect" placeholder="지역코드">
			<input type="text" id="area_text" class="input input-large input-rect" placeholder="지역">
			<button class="btn btn-default" type="button" onfocus="this.blur()" onclick="addArea(); return false;">Add</button>
		</div>
	</div>
	<script src="../resources/jquery.min.js"></script>
	<script src="../resources/bootstrap.min.js"></script>
	<script src="../resources/bootbox.min.js"></script>
	<script src="../resources/jui.min.js"></script>
	<script src="../resources/socket.io.js"></script>
	<script src="../resources/common.js"></script>
	<script data-jui="#area" data-tpl="row" type="text/template">
		<tr>
			<td><!= acid !></td>
			<td><!= areacode !></td>
			<td><!= area !></td>
		</tr>
	</script>
	<script data-jui="#area" data-tpl="expand" type="text/template">
		<div class="panel panel-default table_panel">
			<div id="detail-heading" class="panel-heading">수거배달 제한일</div>
 	 			<div class="panel-body">
					<table id="areadate" class="table table-hover">
						<thead>
							<tr>
								<th>수거배달 제한일</th>
								<th>No.</th>
							</tr>
						</thead>
					<tbody></tbody>
					</table>
				</div>
				<div class="add_date_area">
					<div class="area-input-group">	
						<input type="text" id="area_date_text" class="input input-large input-rect" placeholder="제한일(yyyy-mm-dd)">
						<button class="btn btn-default" type="button" onfocus="this.blur()" onclick="addAreaDate(<!= acid !>); return false;">Add</button>
					</div>
				</div>
			</div>
		</div>
		<div class="add_date_area">
			<div class="area-input-group">	
				<input type="text" id="area_alarm_text" class="input input-large input-rect" placeholder="번호(010xxxxxxxx)">
				<button class="btn btn-default" type="button" onfocus="this.blur()" onclick="addAreaAlarm(<!= acid !>); return false;">Add</button>
			</div>
		</div>

		<div class="group">
			<a href="javascript:area.remove(<!= row.index !>); area.hideExpand(); deleteArea(<!= acid !>);" class="btn btn-black btn-small">Delete</a>
		</div>
	</script>	
	<script data-jui="#area" data-tpl="none" type="text/template">
		<tr>
			<td colspan="2" class="none">데이터가 없습니다.</td>
		</tr>
	</script>
	<script data-jui="#areadate" data-tpl="row" type="text/template">
		<tr>
			<td><!= area_date !></td>
			<td><!= acid !></td>
		</tr>
	</script>
	<script data-jui="#areadate" data-tpl="none" type="text/template">
		<tr>
			<td colspan="2" class="none">데이터가 없습니다.</td>
		</tr>
	</script>	
	<script>
		var constant = new Constant();

		var global_uix = null;
		var global_ui = null;
		
		jui.ready(function(ui, uix, _) {
			area = uix.table("#area", {
				animate : true,
				expand : true
			});
			
			getAreaData();
		});

		jui.ready(function(ui, uix, _) {
			areadate = uix.table("#areadate", {
				animate : true,
				expand : true
			});			
		});
		
		function getAreaData() {
			$.ajax({
				type : 'GET',
				url : '../admin/area',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				success : function(json) {
					setAreaInfo(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}

		function getAreaDateData(acid) {
			$.ajax({
				type : 'GET',
				url : '../admin/date',
				dataType : 'json',
				data : JSON.stringify({
					acid : acid
				}),
				contentType : "application/json",
				async : true,
				success : function(json) {
					setAreaDateInfo(json);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}
		
		function setAreaInfo(json) {
			var data = JSON.parse(json.data);
			var areaInfoSize = data.length;
			area.update(data);
		}
		
		function setAreaDateInfo(json) {
			var data = JSON.parse(json.data);
			var areaInfoSize = data.length;
			for (var i = 0; i < areaInfoSize; i++) {
				data[i]['delete_date_row'] = '<a href="" onclick="return false" adid="' + data[i].adid + '" onfocus="this.blur()" class="area_date_delete_link">삭제</a>';
			}
			areadate.update(data);
		}
		
		function addArea() {					
			$.ajax({
				type : 'POST',
				url : '../admin/area',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					areacode : $('#areacode_text').val(),
					area : $('#area_text').val()
				}),
				success : function(json) {
					area.reset;
					
					getAreaData()
				},
				error : function(request, status, error) {
					console.log(request.responseText);
				}
			});
		}
		
		function deleteArea(acid) {					
			$.ajax({
				type : 'DELETE',
				url : '../admin/area',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					acid : acid
				}),
				success : function(json) {
					console.log("success in")					
				},
				error : function(request, status, error) {
					console.log(request.responseText);
				}
			});
		}
		
		function addAreaDate(acid) {					
			$.ajax({
				type : 'POST',
				url : '../admin/date',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					acid : acid,
					area_date : $('#area_date_text').val()
				}),
				success : function(json) {
					area_date = $('#area_date_text').val()
					var rows = [];
					rows.push({ area_date: area_date, acid: acid });
					areadate.append(rows);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
				}
			});
		}
		
		function deleteAreaDate() {
			$.ajax({
				type : 'POST',
				url : '../admin/date',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					area_date : $('#area_date_text').val()
				}),
				success : function(json) {
					areacode : $('#areacode_text').val();
					area : $('#area_text').val();
					var rows = [];
					rows.push({ area_date: data.area_date });
					areadate.append(rows);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
				}
			});
		}
		
		function addAreaAlarm(acid) {					
			$.ajax({
				type : 'POST',
				url : '../admin/alarm',
				dataType : 'json',
				contentType : "application/json",
				async : true,
				data : JSON.stringify({
					acid : acid,
					phone : $('#area_alarm_text').val()
				}),
				success : function(json) {
					area_date = $('#area_alarm_text').val()
				},
				error : function(request, status, error) {
					console.log(request.responseText);
				}
			});
		}

	</script>
</body>
</html>