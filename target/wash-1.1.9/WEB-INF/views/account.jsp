<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<div class="modal fade" id="accountModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog login-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					<strong>크린바스켓 로그인</strong>
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-signin">
					<input type="text" id="logid" class="form-control" placeholder="이메일">
					<input type="password" id="logpw" class="form-control" placeholder="비밀번호">
					<div style="float: right !important;">
						<button type="button" id="button-login" class="btn btn-info">로그인</button>
						<button type="button" id="button-join" class="btn btn-info">회원가입</button>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="javascript:findPassword();">비밀번호를 잊으셨나요?</a>
			</div>
		</div>
	</div>
</div>