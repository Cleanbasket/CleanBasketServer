<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>비밀번호 변경</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="../favicon.ico">
<link rel="stylesheet" href="../resources/bootstrap.min.css">
<link rel="stylesheet" href="../resources/animate.min.css">
<link rel="stylesheet" href="../resources/common.css">
<style type="text/css">
#change {
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="form-signin">
			<h2 class="form-signin-heading">Password Change</h2>
			<input id="email" type="text" class="form-control" placeholder="Account" autofocus> <input id="password" type="password" class="form-control" placeholder="Password"> <input
				id="newpass1" type="password" class="form-control" placeholder="New Password1"> <input id="newpass2" type="password" class="form-control" placeholder="New Password2">
			<button class="btn btn-lg btn-primary btn-block" id="change" type="button" onfocus="this.blur()">변경</button>
			<button class="btn btn-lg btn-primary btn-block" type="button" onclick="location.href='../admin/login'" onfocus="this.blur()">되돌아가기</button>
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
			var change = $('#change');

			$('input').keypress(function(event) {
				if (event.keyCode == 13) {
					change.click();
				}
			});

			change.click(function() {
				var email = $('#email');
				var password = $('#password');
				var newpass1 = $('#newpass1');
				var newpass2 = $('#newpass2');

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

				if (!newpass1.val()) {
					var data = {
						msg : "<strong>첫번째 새로운 비밀번호를 입력해야합니다.",
						color : "info"
					};
					notify.add(data);
					newpass1.focus();
					return;
				}

				if (!newpass2.val()) {
					var data = {
						msg : "<strong>두번째 새로운 비밀번호를 입력해야합니다.",
						color : "info"
					};
					notify.add(data);
					newpass2.focus();
					return;
				}

				if (newpass1.val() != newpass2.val()) {
					var data = {
						msg : "<strong>새로운 비밀번호가 서로 일치하지 않습니다.",
						color : "info"
					};
					notify.add(data);
					newpass1.focus();
					return;
				}

				$.ajax({
					type : 'POST',
					url : '../passwd/update',
					contentType : "application/json",
					dataType : 'json',
					async : true,
					data : JSON.stringify({
						email : email.val(),
						password : password.val(),
						newpass : newpass1.val(),
					}),
					success : function(json) {

						switch (json.constant) {
						case constant.SUCCESS:
							location.href = '../admin/login';
							break;
						case constant.ERROR:
							var data = {
								msg : "<strong>처리 중 오류가 발생하였습니다.",
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
					}
				});
			});
		});
	</script>
</body>
</html>