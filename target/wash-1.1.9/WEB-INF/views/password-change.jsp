<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<div class="modal fade" id="passwordModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog change-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					<strong>비밀번호 변경</strong>
				</h4>
			</div>
			<div class="modal-body">
				<input style="margin-bottom: 10px;" type="password" id="password-change" class="form-control" placeholder="비밀번호 (6자리 이상 10자리 이하)">
				<input type="password" id="password-change2" class="form-control" placeholder="비밀번호 확인">
			</div>
			<div class="modal-footer change-footer">
				<button type="button" id="button-password-change" class="btn btn-info">확인</button>
			</div>
		</div>
	</div>
</div>