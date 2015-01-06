<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<div class="modal fade" id="calcModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog order-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					<strong>세탁품목선택</strong>
				</h4>
			</div>
			<div class="modal-body order-body">
				<div id="item-selecter" class="noselect">
					<div id="item-title">와이셔츠</div>
					<span id="item-left"></span>
					<span id="item-content"></span>
					<span id="item-right"></span>
				</div>
				<div id="item-bar-padding">
					<div class="input-group item-bar">
						<span style="width: 38px;" class="input-group-addon basic-label">가격</span>
						<span id="order-price" style="width: 184px; border-radius: 4px 0px 0px 4px;" class="input-group-addon basic">2000원</span>
						<span id="order-item-sub" style="width: 40px; text-align: right;" class="input-group-addon basic noselect">
							<i class="icon-sub"></i>
						</span>
						<span id="order-item-count" style="width: 40px;" class="input-group-addon basic">0</span>
						<span id="order-item-add" style="width: 40px; text-align: left;" class="input-group-addon basic noselect">
							<i class="icon-add"></i>
						</span>
					</div>
					<div style="margin-top: 10px;" class="input-group item-bar">
						<span style="width: 38px;" class="input-group-addon basic-label">합계</span>
						<span id="order-sum-price" style="width: 184px; border-radius: 4px;" class="input-group-addon basic">
							<span id="item-price">0원</span>
							<span id="dropoff-price">+2000원(배송)</span>
						</span>
						<button id="order-coupon-apply" class="btn btn-default unused" type="button">쿠폰적용</button>
					</div>
					<div style="margin-top: 10px;" class="input-group item-bar">
						<span style="width: 38px;" class="input-group-addon basic-label">총계</span>
						<span id="order-total-price" style="width: 184px; border-radius: 4px;" class="input-group-addon basic">2000원</span>
						<span style="width: 95px; display: inline-block;"></span>
					</div>
					<div style="margin-top: 10px;" class="input-group item-bar">
						<input id="order-memo" class="input-group-addon basic" placeholder="추가요청사항">
					</div>
				</div>
			</div>
			<div class="modal-footer order-footer">
				<button id="button-order-decide" class="btn btn-info" type="button">주문확정</button>
			</div>
		</div>
	</div>
</div>