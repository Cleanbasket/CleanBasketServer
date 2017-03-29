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
			var buttonTypes = ["Daily", "Weekly", "Monthly"];
			var type = "Daily"

			var body = d3.select("body");
			body.append("h3")
				.text("Number of Pickup");
						
			var buttons = 
				body.append("div")
				.attr("class", "buttons-container")
				.selectAll("div").data(buttonTypes)
				.enter().append("div")
				.text(function(d) { return d; })
				.attr("class", function(d) {
					if(d == type)
						return "button selected";
					else
						return "button";
				})
			
			body.append("div")
				.attr("class", "clearfix");
			
			var margin = {top: 30, right: 0, bottom: 0, left: 100};
			var width = 960 - margin.left - margin.right,
				height = 450 - margin.top - margin.bottom,
				translateText = "translate(" + margin.left + ", " + margin.top + ")";
			
			var svg = body.append("svg")
				.attr("width", width + margin.left + margin.right)
				.attr("height", height + margin.top + margin.bottom)
				.append("g")
				.attr("transform", translateText);
			var barGroup = svg.append("g")
				.attr("class", "bar");
			var firstGroup = svg.append("g")
				.attr("class", "first");
			var barLabelGroup = svg.append("g")
				.attr("class", "bar-label")

			var x = d3.scale.ordinal()
				.domain(data.map(function(element) { return element.date }))
				.rangeBands([0, width], 0.2);

			var y = d3.scale.linear()
				.domain([0, d3.max(data, function(element) { return element.data })])
				.range([0, height]);

			var yForAxis = d3.scale.linear()
				.domain([0, d3.max(data, function(element) { return element.data })])
				.range([height, 0]);
				
			var xAxis = d3.svg.axis()
				.scale(x)
				.orient("bottom");

			var yAxis = d3.svg.axis()
				.scale(yForAxis)
				.orient("left");

			svg.append("g")
				.call(xAxis)
				.attr("class", "x axis")
				.attr("transform", "translate(0, " + height + ")")
			  .selectAll("text")
			  	.attr("y", 0)
			    .attr("x", 9)
			    .attr("dy", ".35em")
			    .attr("transform", "rotate(-90)")
			    .style("text-anchor", "start");
			  
			svg.append("g")
				.call(yAxis)
				.attr("class", "y axis");
			
			barGroup.selectAll("rect")
				.data(data)
			  .enter().append("rect")
				.attr("height", function(d) { return y(d.data) })
				.attr("width", x.rangeBand())
				.attr("x", function(d) { return x(d.date) })	
				.attr("y", function(d) { return height - y(d.data) })
				.attr("fill", "#83dbd1")
			    .on("mouseover", function() {
					d3.select(this)
						.attr("fill", "orange")
				})
				.on("mouseout", function() {
					d3.select(this)
						.attr("fill", "#83dbd1");
				})
			  .append("title")
				.text(function(d) {
					return "Pickup : " + d.data;
				});	
			
			firstGroup.selectAll("rect")
				.data(data)
			  .enter().append("rect")
				.attr("height", function(d) { return y(d.data2) })
				.attr("width", x.rangeBand() * 0.8)
				.attr("x", function(d, i) { return x(d.date) })	
				.attr("y", function(d, i) { return height - y(d.data2) })
				.attr("fill", "#7FBCE8")
			    .on("mouseover", function() {
			    	d3.select(this)
		    			.attr("fill", "orange");
				})
			    .on("mouseout", function() {
				d3.select(this)
					.attr("fill", "#7FBCE8");
				})
			  .append("title")
				.text(function(d) {
					return "New Pickup : " + d.data2;
				});
			  
			body.append("div")
				.attr("class", "clearfix");
			
			var callback = function(type, popData) {
				x = d3.scale.ordinal()
					.domain(popData.map(function(element) { return element.date }))
					.rangeBands([0, width], 0.2);

				y = d3.scale.linear()
					.domain([0, d3.max(popData, function(element) { return element.data })])
					.range([0, height]);
				
				yForAxis = d3.scale.linear()
					.domain([0, d3.max(popData, function(element) { return element.data })])
					.range([height, 0]);
				
				xAxis = d3.svg.axis()
					.scale(x)
					.orient("bottom");

				yAxis = d3.svg.axis()
					.scale(yForAxis)
					.orient("left");
				
				svg.selectAll("g.x.axis")
					.call(xAxis)
				  .selectAll("text")
				  	.attr("y", 0)
				    .attr("x", 9)
				    .attr("dy", ".35em")
					.attr("transform", "rotate(-90)")
				    .style("text-anchor", "start");
				
				svg.selectAll("g.y.axis")
					.call(yAxis);
				
				if (type == "d" || type == "w") {
					barGroup.selectAll("rect").data(popData).enter().append("rect").attr("fill", "#83dbd1")
							.on("mouseover", function() {
								d3.select(this)
									.attr("fill", "orange")
							})
							.on("mouseout", function() {
								d3.select(this)
									.attr("fill", "#83dbd1");
							})
							.append("title")
							.text(function(d) {
								return "Pickup : " + d.data;
							});	
					
					firstGroup.selectAll("rect").data(popData).enter().append("rect").attr("fill", "#7FBCE8")
							.on("mouseover", function() {
								d3.select(this)
									.attr("fill", "orange")
							})
							.on("mouseout", function() {
								d3.select(this)
									.attr("fill", "#7FBCE8");
							})
							.append("title")
							.text(function(d) {
								return "Pickup : " + d.data2;
							});	
				}
				
				barGroup.selectAll("rect")
					.data(popData)
					.transition()
					.duration(500)
					.attr("height", function(d) { return y(d.data) })
					.attr("width", x.rangeBand())
					.attr("x", function(d, i) { return x(d.date) })	
					.attr("y", function(d, i) { return height - y(d.data) })
					.attr("fill", "#83dbd1")
				  .select("title")
					.text(function(d) {
						return "Pickup : " + d.data;
					});	
				  
				firstGroup.selectAll("rect")
					.data(popData)
					.transition()
					.duration(500)
					.attr("height", function(d) { return y(d.data2) })
					.attr("width", x.rangeBand() * 0.8)
					.attr("x", function(d, i) { return x(d.date) })	
					.attr("y", function(d, i) { return height - y(d.data2) })
					.attr("fill", "#7FBCE8")
				  .select("title")
					.text(function(d) {
						return "Pickup : " + d.data2;
					});	
				
				if (type == "m") {
					barGroup.selectAll("rect").data(popData).exit().remove();
					firstGroup.selectAll("rect").data(popData).exit().remove();
				}
			}
			
			buttons.on("click", function(d) {
				d3.select(".selected")
					.classed("selected", false);
				d3.select(this)
					.classed("selected", true);
				
				type = d;
								
				if (d == "Daily") 
					popData = getData("d", callback);
				else if (d == "Weekly")
					popData = getData("w", callback);
				else if (d == "Monthly")
					popData = getData("m", callback);
			});
		}
		
		$(window).ready(function() {
			getDailyPickup();
		});
		
		function getDailyPickup() {
			$.ajax({
				type : 'GET',
				url : '../dash/pickup/d/30',
				dataType : 'json',
				async : true,
				success : function(json) {
					var data = JSON.parse(json.data);
					data.reverse();
					drawDailyPickup(data);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
				}
			});
		}
		
		function getData(type, callback) {
			$.ajax({
				type : 'GET',
				url : '../dash/pickup/' + type + '/30',
				dataType : 'json',
				async : true,
				success : function(json) {
					var data = JSON.parse(json.data);
					data.reverse();
					callback(type, data);
				},
				error : function(request, status, error) {
					console.log(request.responseText);
				}
			});
		}
	</script>
</body>
</html>