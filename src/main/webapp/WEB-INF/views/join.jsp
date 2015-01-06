<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<div class="modal fade" id="joinModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog join-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title">
					<strong>크린바스켓 회원가입</strong>
				</h4>
			</div>
			<div class="modal-body">
				<div class="form-signin">
					<input type="text" id="join-logid" class="form-control" placeholder="이메일 (최대 80자)">
					<input type="password" id="join-logpw" class="form-control" placeholder="비밀번호 (6자리 이상 10자리 이하)">
					<input type="password" id="join-logpw2" class="form-control" placeholder="비밀번호 확인">
					<input type="text" id="join-phone" class="form-control" placeholder="연락처 (선택사항)">
					<div id="join-address">주소 (선택사항)</div>
				</div>
				<div style="color: #555;">
					<div style="font-weight: bold; margin-bottom: 10px;">
						<span style="margin-left: 6px;">이용약관 / 개인정보 (필수)</span>
						<span id="agreement-all" class="noselect" style="float: right; font-weight: normal; cursor: pointer; margin-right: 10px;">
							<i class="icon-check"></i>모두동의
						</span>
					</div>
					<div style="height: 34px; background-color: #f5f5f5; border-radius: 4px; margin-bottom: 10px; padding-left: 12px; padding-top: 7px;">
						<a style="color: #555 !important; text-decoration: none;" target="_blank" href="./term-of-use">서비스 이용 약관에 동의</a>
						<span id="agreement-use" class="noselect" style="float: right; cursor: pointer; margin-right: 10px;">
							<i class="icon-check"></i>동의
						</span>
					</div>
					<div style="height: 34px; background-color: #f5f5f5; border-radius: 4px; padding-left: 12px; padding-top: 7px;">
						<a style="color: #555 !important; text-decoration: none;" target="_blank" href="./privacy">개인정보 수집 및 이용에 동의</a>
						<span id="agreement-privacy" class="noselect" style="float: right; cursor: pointer; margin-right: 10px;">
							<i class="icon-check"></i>동의
						</span>
					</div>
				</div>
			</div>
			<div class="modal-footer join-footer">
				<button type="button" id="button-join-ok" class="btn btn-info">가입하기</button>
			</div>
		</div>
	</div>
</div>