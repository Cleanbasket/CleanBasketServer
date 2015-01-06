<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<div class="modal fade" id="addressModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog join-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					<strong>주소입력</strong>
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-signin address-config">
					<div style="margin-bottom: 10px;">
						<span style="width: 60px; display: inline-block;">시/도</span>
						<select class="address-first">
							<option value="서울">서울</option>
						</select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 60px; display: inline-block;">구/군/시</span>
						<select class="address-second">
							<option value="강남구">강남구</option>
							<option value="강동구">강동구</option>
							<option value="강북구">강북구</option>
							<option value="강서구">강서구</option>
							<option value="관악구">관악구</option>
							<option value="광진구">광진구</option>
							<option value="구로구">구로구</option>
							<option value="금천구">금천구</option>
							<option value="노원구">노원구</option>
							<option value="도봉구">도봉구</option>
							<option value="동대문구">동대문구</option>
							<option value="동작구">동작구</option>
							<option value="마포구">마포구</option>
							<option value="서대문구">서대문구</option>
							<option value="서초구">서초구</option>
							<option value="성동구">성동구</option>
							<option value="성북구">성북구</option>
							<option value="송파구">송파구</option>
							<option value="양천구">양천구</option>
							<option value="영등포구">영등포구</option>
							<option value="용산구">용산구</option>
							<option value="은평구">은평구</option>
							<option value="종로구">종로구</option>
							<option value="중구">중구</option>
							<option value="중랑구">중랑구</option>
						</select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 60px; display: inline-block;">동/면/읍</span>
						<select class="address-third">
							<option value="개포1동">개포1동</option>
							<option value="개포2동">개포2동</option>
							<option value="개포4동">개포4동</option>
							<option value="개포동">개포동</option>
							<option value="논현1동">논현1동</option>
							<option value="논현2동">논현2동</option>
							<option value="논현동">논현동</option>
							<option value="대치1동">대치1동</option>
							<option value="대치2동">대치2동</option>
							<option value="대치4동">대치4동</option>
							<option value="대치동">대치동</option>
							<option value="도곡1동">도곡1동</option>
							<option value="도곡2동">도곡2동</option>
							<option value="도곡동">도곡동</option>
							<option value="삼성1동">삼성1동</option>
							<option value="삼성2동">삼성2동</option>
							<option value="삼성동">삼성동</option>
							<option value="세곡동">세곡동</option>
							<option value="수서동">수서동</option>
							<option value="신사동">신사동</option>
							<option value="압구정동">압구정동</option>
							<option value="역삼1동">역삼1동</option>
							<option value="역삼2동">역삼2동</option>
							<option value="역삼동">역삼동</option>
							<option value="율현동">율현동</option>
							<option value="일원1동">일원1동</option>
							<option value="일원2동">일원2동</option>
							<option value="일원동">일원동</option>
							<option value="일원본동">일원본동</option>
							<option value="자곡동">자곡동</option>
							<option value="청담동">청담동</option>
						</select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 60px; display: inline-block;">번지</span>
						<input style="width: 270px; display: inline-block;" type="text" id="address-number" class="form-control">
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 60px; display: inline-block;">건물명</span>
						<input style="width: 270px; display: inline-block;" type="text" id="address-building" class="form-control">
					</div>
					<div>
						<span style="width: 60px; display: inline-block;">나머지</span>
						<input style="width: 270px; display: inline-block;" type="text" id="address-remainder" class="form-control">
					</div>
				</div>
			</div>
			<div class="modal-footer join-footer address-footer">
				<button type="button" id="button-address-ok" class="btn btn-info">확인</button>
			</div>
		</div>
	</div>
</div>