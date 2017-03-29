<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<div class="modal fade" id="calcEtcModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog order-etc-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					<strong>세탁품목선택(기타)</strong>
				</h4>
			</div>
			<div style="height: 453px; padding-bottom: 0px; overflow-y: scroll;" class="modal-body">
				<ul id="order-etc-list" style="font-size: 12px;" class="list-group">
					<li class="list-group-item">
						<span id="order-etc-name">정장(상의)</span>
						<span style="float: right;">
							<span id="order-etc-price" style="margin-right: 5px;">3500원</span>
							<a id="order-etc-sub" style="margin-right: 5px;" class="noselect">
								<i class="icon-etc-sub"></i>
							</a>
							<span id="order-etc-count" style="margin-right: 5px;">0</span>
							<a id="order-etc-add" class="noselect">
								<i class="icon-etc-add"></i>
							</a>
						</span>
					</li>
				</ul>
			</div>
			<div class="modal-footer order-footer">
				<ul id="order-etc-sumprice" class="list-group">
					<li class="list-group-item">합계 : 0원</li>
				</ul>
				<button id="button-order-etc-ok" class="btn btn-info" data-dismiss="modal" type="button">확인</button>
			</div>
		</div>
	</div>
</div>