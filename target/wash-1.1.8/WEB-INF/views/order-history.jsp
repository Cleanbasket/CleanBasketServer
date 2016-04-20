<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<div class="modal fade" id="historyModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog history-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					<strong>세탁물 상세보기</strong>
				</h4>
			</div>
			<div style="height: 500px; overflow-y: auto;" class="modal-body history-body">
				<div class="panel-group">
					<div class="panel panel-default temp">
						<div class="panel-heading">
							<a class="history-title" data-toggle="collapse" href="#121009-1">
								<span>주문번호</span>
								<span class="order-number">141009-1</span>
							</a>
						</div>
						<div id="121009-1" class="panel-collapse collapse in history-content">
							<div class="panel-body">
								<div>
									<span class="history-label">주문가격</span>
									<span class="history-text">14000원 (배송 2000원)</span>
								</div>
								<div>
									<span class="history-label">주문품목</span>
									<span class="history-text">와이셔츠(6)</span>
								</div>
								<div>
									<span class="history-label">진행상태</span>
									<span class="history-text">주문완료</span>
								</div>
								<div>
									<span class="history-label">수거일시</span>
									<span class="history-text">2014-10-09 23~24시</span>
								</div>
								<div>
									<span class="history-label">배달일시</span>
									<span class="history-text">2014-10-09 23~24시</span>
								</div>
								<div class="history-deliverer-info">
									<span class="history-deliverer-pic"></span>
									<span class="history-deliverer-label">배달원 이름</span>
									<span class="history-deliverer-name">미지정</span>
								</div>
								<div>
									<a class="btn btn-info btn-mini btn-sm button-history-delete">주문취소</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>