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
<link rel="stylesheet" href="../resources/dashboard.css">
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
					<li><a href="../admin/area" onfocus="this.blur()">지역관리</a></li>																																																																											
					<li class="active"><a href="../admin/dashboard" onfocus="this.blur()">대시보드</a></li>						
					<li><a href="../logout" onfocus="this.blur()">로그아웃</a></li>
				</ul>
			</div>
		</div>
	</div>
	
	<div class="daily-pickup"></div>
	
	<script src="../resources/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js" charset="utf-8"></script>

	<script>
		function drawDailyPickup(data) {
			var body = d3.select("body");
			body.append("h2")
				.text("Daily Number of Pick-up");
			body.append("div")
				.attr("class", "top-label age-label")
				.append("p")
				.text("Date");
			body.append("div")
				.attr("class", "top-label")
				.append("p")
				.text("Count");
			body.append("div")
				.attr("class", "clearfix");
			
			var margin = {top: 30, right: 0, bottom: 0, left: 100};
			var width = 500 - margin.left - margin.right,
				height = 450 - margin.top - margin.bottom,
				translateText = "translate(" + margin.left + ", " + margin.top + ")";
			
			var svg = body.append("svg")
				.attr("width", width + margin.left + margin.right)
				.attr("height", height + margin.top + margin.bottom)
				.append("g")
				.attr("transform", translateText);
			var barGroup = svg.append("g")
				.attr("class", "bar");
			var barLabelGroup = svg.append("g")
				.attr("class", "bar-label")
			
			var x = d3.scale.linear()
				.domain([0, d3.max(data, function(element) { return element.data })])
				.range([0, width]);
			
			var y = d3.scale.ordinal()
				.domain(data.map(function(element) { return element.date } ))
				.rangeBands([0, height], 0.2);
			
			d3.max(data, function(element) { return element.value })
			
			var xAxis = d3.svg.axis()
				.scale(x)
				.orient("top")
				.ticks(5);
			
			svg.append("g")
				.call(xAxis)
				.attr("class", "axis");
			
			var yAxis = d3.svg.axis()
				.scale(y)
				.orient("left");

			svg.append("g")
				.call(yAxis)
				.attr("class", "axis");
			
			barGroup.selectAll("rect")
				.data(data)
				.enter().append("rect")
				.attr("height", y.rangeBand())
				.attr("width", function(d) { return x(d.data) })
				.attr("y", function(d, i) { return y(d.date) })	
		}
		
		$(window).ready(function() {
			getDailyPickup();
		});
		
		function getDailyPickup() {
			$.ajax({
				type : 'GET',
				url : '../dash/daily/pickup/30',
				dataType : 'json',
				async : true,
				success : function(json) {
					var data = JSON.parse(json.data);
					data.sort
					console.log(data)
					drawDailyPickup(data)
				},
				error : function(request, status, error) {
					console.log(request.responseText);
					errorCheck(request.responseText);
				}
			});
		}
	</script>
	
</body>
</html>