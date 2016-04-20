<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>로그인</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="../favicon.ico">
<link rel="stylesheet" href="../resources/bootstrap.min.css">
<link rel="stylesheet" href="../resources/animate.min.css">
<link rel="stylesheet" href="../resources/common.css">
<style type="text/css">
#login {
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="form-signin">
			<h2 class="form-signin-heading">CleanBasket</h2>
			<input id="email" type="text" class="form-control" placeholder="Account" autofocus>
			<input id="password" type="password" class="form-control" placeholder="Password">
			<button class="btn btn-lg btn-primary btn-block" id="login" type="button" onfocus="this.blur()">로그인</button>
			<button class="btn btn-lg btn-primary btn-block" type="button" onclick="location.href='../admin/passwd'" onfocus="this.blur()">비밀번호 변경</button>
		</div>
	</div>
	<div class="modal" id="pleaseWaitDialog" data-backdrop="static">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h6 class="modal-title">정보를 불러오는 중 입니다.</h6>
				</div>
				<div class="modal-body">
					<div class="progress progress-striped active">
						<div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%;">
							<span class="sr-only">100% Complete</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="../resources/jquery.min.js"></script>
	<script src="../resources/bootstrap.min.js"></script>
	<script src="../resources/bootbox.min.js"></script>
	<script src="../resources/jui.min.js"></script>
	<script src="../resources/common.js"></script>
	<script id="tpl_alarm" type="text/template">
		<div class="alert alert-<!= color !>" style="width: 100%;"><!= msg !></div>
	</script>
	<script>
		var constant = new Constant();
		var run = false;

		jui.ready(function(ui, uix, _) {

			notify = ui.notify("body", {
				position : "top",
				timeout : 2000,
				padding : 25,
				tpl : {
					alarm : $("#tpl_alarm").html()
				}
			});

		});

		$(window).load(function() {
			var login = $('#login');

			$('input').keypress(function(event) {
				if (event.keyCode == 13) {
					login.click();
				}
			});

			login.click(function() {
				if (run == false) {
					var email = $('#email');
					var password = $('#password');

					if (!email.val()) {
						var data = {
							msg : "<strong>아이디를 입력해야합니다.",
							color : "info"
						};
						if ($('.alert').length == 0) {
							notify.add(data);
						}
						email.focus();
						return;
					}

					if (!password.val()) {
						var data = {
							msg : "<strong>비밀번호를 입력해야합니다.",
							color : "info"
						};
						notify.add(data);
						password.focus();
						return;
					}

					var pleaseWaitDiv = $('#pleaseWaitDialog');

					$.ajax({
						type : 'POST',
						url : '../auth',
						dataType : 'json',
						async : true,
						data : {
							email : email.val(),
							password : password.val(),
							remember : false
						},
						beforeSend : function() {
							run = true;
							pleaseWaitDiv.modal('show');
						},
						success : function(json) {
							switch (json.constant) {
							case constant.SUCCESS:
								location.href = '../admin/order'
								break;
							case constant.ACCOUNT_DISABLED:
								var data = {
									msg : "<strong>계정이 비활성화 되어 있습니다. 관리자에게 문의하세요.",
									color : "danger"
								};
								notify.add(data);
								break;
							case constant.ERROR:
								var data = {
									msg : "<strong>서버에 오류가 발생하였습니다.",
									color : "danger"
								};
								notify.add(data);
								break;
							case constant.EMAIL_ERROR:
								var data = {
									msg : "<strong>아이디를 다시 확인하세요.",
									color : "danger"
								};
								notify.add(data);
								email.focus();
								break;
							case constant.PASSWORD_ERROR:
								var data = {
									msg : "<strong>비밀번호를 다시 확인하세요.",
									color : "danger"
								};
								notify.add(data);
								password.focus();
								break;
							}

						},
						error : function(request, status, error) {
							alert(request.responseText);
						},
						complete : function() {
							run = false;
							pleaseWaitDiv.modal('hide');
						},
					});
				}
			});
		});

		function loadingFix(cl) {
			var loaderObj = document.getElementById("canvasLoader");
			loaderObj.style.position = "absolute";
			loaderObj.style["top"] = cl.getDiameter() * -0.5 + "px";
			loaderObj.style["left"] = cl.getDiameter() * -0.5 + "px";
		}
	</script>
</body>
</html>