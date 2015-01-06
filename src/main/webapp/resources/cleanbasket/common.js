$(document).ready(function() {
	$(window).resize(function() {
		headerResize();
	}).resize();
	setDatePicker();
	setSelectBox();
	setDatetimeLimit();
	setMenuHtml();
	setAccountHtml();
	setOrderCalculationHtml();
	setAccountSettings();
	setOrderState();
	setCouponIssue();
	authCheck();
	refresh();
});

function refresh() {
	setInterval(function() {
		$.ajax({
			url : './refresh',
			async : true
		});
	}, 1200 * 1000);
}

var recommendationURL = null;

function getRecommendationURL() {
	$.ajax({
		type : 'POST',
		url : './recommendation',
		contentType : "application/json",
		dataType : 'json',
		async : true,
		success : function(json) {
			var constant = new Constant();
			if (json.constant == constant.SUCCESS || json.constant == constant.DUPLICATION) {
				recommendationURL = json.data;
			} else if (json.constant == constant.ERROR) {
				alert("서버에 오류가 발생하였습니다.");
			}
		},
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function facebookShare() {
	$(".popover").remove();
	var accountButton = $("#account-button");
	if (accountButton.hasClass("login-state")) {
		var width = 553;
		var height = 277;
		var top = (screen.availHeight - height) / 2;
		var left = (screen.availWidth - width) / 2;
		var popUrl = "https://www.facebook.com/sharer/sharer.php?u=" + encodeURIComponent("http://www.cleanbasket.co.kr/" + recommendationURL);
		console.log(popUrl);
		var popOption = 'height=' + height + ', width=' + width + ', top=' + top + ', left=' + left;
		window.open(popUrl, "CleanBasket Facebook", popOption);
	} else {
		accountButton.click();
	}
}

function setMenuHtml() {
	var body = $("body");
	var menuButton = $("#menu-button");

	getHtmlCode('./menu', function(html) {
		menuButton.click(function(e) {
			if ($(".popover").length == 0) {
				$("body").append(html);
				$(".anchorLink").anchorAnimate({
					offset : 90
				});
			} else {
				$(".popover").remove();
			}
		});
	});

	body.mousedown(function(e) {
		if (!$(e.target).is("#menu-button") && !$(e.target).is(".popover-content") && !$(e.target).is(".button")) {
			$(".popover").remove();
		}
	});
}

function setAccountHtml() {
	var accountButton = $("#account-button");
	getHtmlCode("./account", function(html) {
		accountButton.click(function(e) {
			if (accountButton.hasClass("logout-state")) {
				var body = $("body").append(html);
				var accountModal = $("#accountModal");
				accountModal.modal({
					backdrop : "static",
					keyboard : false
				});
				accountModal.modal("show");
				accountModal.on("shown.bs.modal", function() {
					$("body").css("overflow-y", "hidden");
					$("#logid").focus();
				});
				accountModal.on("hidden.bs.modal", function() {
					if ($("#joinModal").length == 0) {
						$("body").css("overflow-y", "auto");
					}
					$(this).remove();
				});
				$("#logid").keypress(function(event) {
					if (event.keyCode == 13) {
						$("#logpw").focus();
					}
				});
				$("#logpw").keypress(function(event) {
					if (event.keyCode == 13) {
						$("#button-login").click();
					}
				});
				$("#button-login").click(function() {
					loginProcess(accountModal);
				});
				$("#button-join").click(function() {
					setJoinHtml();
				});
			} else if (accountButton.hasClass("login-state")) {
				logoutProcess(false);
			}
		});
	});
}

function setJoinHtml() {
	getHtmlCode("./join", function(html) {
		var body = $("body").append(html);
		var joinModal = $("#joinModal");
		joinModal.modal({
			backdrop : "",
			keyboard : false
		});
		$("#accountModal").hide();
		joinModal.modal("show");
		joinModal.on("hidden.bs.modal", function() {
			$("#accountModal").show();
			$("#logid").focus();
			$("#addressModal").remove();
			$(this).remove();
		});
		joinModal.on("shown.bs.modal", function() {
			$("#join-logid").focus();
		});

		setAgreementButton();
		setJoinClickEvent();

		$("#join-address").click(function(e) {
			setAddressHtml();
		});

		$("#button-join-ok").click(function(e) {
			joinProcess();
		});
	});
}

function findPassword() {
	getHtmlCode("./account", function(html) {
		var passwordModal = $(html).attr("id", "passwordModal");
		var progressBar = createProgressBar().css("display", "none");
		passwordModal.find(".modal-footer").remove();
		passwordModal.find(".login-dialog").removeClass("login-dialog").addClass("password-dialog");
		passwordModal.find(".modal-title strong").text("비밀번호 찾기");
		passwordModal.find("#logpw").remove();
		passwordModal.find("#button-join").remove();
		passwordModal.find(".modal-body").append(progressBar);
		var email = passwordModal.find("#logid").attr("id", "email").attr("placeholder", "이메일 주소").css("display", "inline-block").css("width", 270);
		var button = passwordModal.find("#button-login").attr("id", "button-find-paassword").text("확인").css("display", "inline-block");
		var body = $("body").append(passwordModal);
		passwordModal.modal({
			backdrop : "",
			keyboard : false
		});
		$("#accountModal").hide();
		passwordModal.modal("show");
		passwordModal.on("hidden.bs.modal", function() {
			$("#accountModal").show();
			$("#logid").focus();
			$(this).remove();
		});
		button.click(function(e) {
			$.ajax({
				type : 'POST',
				url : './password/inquiry',
				contentType : "application/json",
				dataType : 'json',
				async : true,
				data : JSON.stringify({
					email : email.val()
				}),
				beforeSend : function() {
					progressBar.css("display", "block");
					$(".form-signin").css("display", "none");
				},
				success : function(json) {
					var constant = new Constant();
					if (json.constant == constant.SUCCESS) {
						alert("임시비밀번호가 발송되었습니다.");
					} else if (json.constant == constant.EMAIL_ERROR) {
						alert("이메일 주소가 존재하지 않습니다.");
					} else {
						alert("오류가 발생하였습니다.");
					}
				},
				error : function(request, status, error) {
					errorCheck(request.responseText);
				},
				complete : function() {
					progressBar.css("display", "none");
					$(".form-signin").css("display", "block");
				}
			});
		});
	});
}

function createProgressBar() {
	var progressBar = $('<div class="progress progress-striped active"></div>').css("margin-bottom", 0);
	progressBar.append('<div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"><span class="sr-only">100% Complete</span></div>');
	return progressBar;
}

var addressStorage = {
	addressFirst : "",
	addressSecond : "",
	addressThird : "",
	addressNumber : "",
	addressBuilding : "",
	addressRemainder : ""
};

var addressData = [
		[ "개포1동", "개포2동", "개포4동", "개포동", "논현1동", "논현2동", "논현동", "대치1동", "대치2동", "대치4동", "대치동", "도곡1동", "도곡2동", "도곡동", "삼성1동", "삼성2동", "삼성동", "세곡동", "수서동", "신사동", "압구정동", "역삼1동", "역삼2동", "역삼동", "율현동",
				"일원1동", "일원2동", "일원동", "일원본동", "자곡동", "청담동" ],
		[ "강일동", "고덕1동", "고덕2동", "길동", "둔촌1동", "둔촌2동", "둔촌동", "명일1동", "명일2동", "명일동", "상일동", "성내1동", "성내2동", "성내3동", "성내동", "암사1동", "암사2동", "암사3동", "암사동", "천호1동", "천호2동", "천호3동", "천호동" ],
		[ "미아동", "번1동", "번2동", "번3동", "번동", "삼각산동", "상암동", "송중동", "송천동", "수유1동", "수유2동", "수유3동", "수유동", "우이동", "인수동" ],
		[ "가양1동", "가양2동", "가양3동", "가양동", "개화동", "공항동", "과해동", "내발산동", "등촌1동", "등촌2동", "등촌3동", "등촌동", "마곡동", "방화1동", "방화2동", "방화3동", "방화동", "염창동", "오곡동", "오쇠동", "외발산동", "우장사동", "화곡1동", "화곡2동", "화곡3동",
				"화곡4동", "화곡6동", "화곡8동", "화곡동", "화곡본동" ],
		[ "낙성대동", "난곡동", "난향동", "남현동", "대학동", "미성동", "보라매동", "봉천동", "삼성동", "서림동", "서원동", "성현동", "신림동", "신사동", "신원동", "은천동", "인헌동", "조원동", "중앙동", "청룡동", "청림동", "행운동" ],
		[ "광장동", "구의1동", "구의2동", "구의3동", "구의동", "군자동", "능동", "자양1동", "자양2동", "자양3동", "자양4동", "자양동", "중곡1동", "중곡2동", "중곡3동", "중곡4동", "중곡동", "화양" ],
		[ "가리봉동", "개봉1동", "개봉2동", "개봉3동", "개봉동", "고척1동", "고척2동", "고척동", "구로1동", "구로2동", "구로3동", "구로4동", "구로5동", "구로동", "궁동", "신도림동", "오류1동", "오류2동", "오류동", "온수동", "천왕동", "항동" ],
		[ "가산동", "독산1동", "독산2동", "독산3동", "독산4동", "독산동", "시흥1동", "시흥2동", "시흥3동", "시흥4동", "시흥5동", "시흥" ],
		[ "공릉1동", "공릉2동", "공릉동", "상계10동", "상계1동", "상계2동", "상게3.4동", "상계5동", "상계6.7동", "상계8동", "상계9동", "상계동", "월계1동", "월계2동", "월계3동", "월계동", "중계1동", "중계2.3동", "중계4동", "중계동", "중계본동", "하계1동", "하계2동",
				"하계동" ],
		[ "도봉1동", "도봉2동", "도봉동", "방학1동", "방학2동", "방학3동", "방학동", "쌍문1동", "쌍문2동", "쌍문3동", "쌍문4동", "쌍문동", "창1동", "창2동", "창3동", "창4동", "창5동", "창동" ],
		[ "답십리1동", "답십리2동", "답십리동", "신설동", "용두동", "이문1동", "이문2동", "이문동", "장안1동", "장안2동", "장안동", "전농1동", "전농2동", "전농동", "제기동", "청량리동", "회기동", "휘경1동", "휘경2동", "휘경" ],
		[ "노량진1동", "노량진2동", "노량진동", "대방동", "동작동", "본동", "사당1동", "사당2동", "사당3동", "사당4동", "사당5동", "사당동", "상도1동", "상도2동", "상도3동", "상도4동", "상도동", "신대방1동", "신대방2동", "신대방동", "흑석" ],
		[ "공덕동", "구수동", "노고산동", "당인동", "대흥동", "도화동", "동교동", "마포동", "망원1동", "망원2동", "망원동", "상수동", "상암동", "서교동", "성산1동", "성산2동", "성산동", "신공덕동", "신수동", "신정동", "아현동", "연남동", "염리동", "용강동", "중동", "창전동",
				"토정동", "하중동", "합정동", "현석동" ],
		[ "남가좌1동", "남가좌2동", "남가좌동", "냉천동", "대신동", "대현동", "미근동", "봉원동", "북가좌1동", "북가좌2동", "북가좌동", "북아현동", "신촌동", "연희동", "영천동", "옥천동", "창천동", "천연동", "충정로2가", "충정로3가", "합동", "현저동", "홍은1동", "홍은2동",
				"홍은동", "홍제1동", "홍제2동", "홍제3동", "홍제동" ],
		[ "내곡동", "반포1동", "반포2동", "반포4동", "반포동", "반포본동", "방배1동", "방배2동", "방배3동", "방배4동", "방배동", "방배본동", "서초1동", "서초2동", "서초3동", "서초4동", "서초동", "신원동", "양재1동", "양재2동", "양재동", "염곡동", "우면동", "원지동", "잠원동" ],
		[ "금호동1가", "금호동2가", "금호동3가", "금호동4가", "도선동", "마장동", "사근동", "상왕십리동", "성수1가1동", "성수1가2동", "성수2가1동", "성수2가3동", "성수동1가", "성수동2가", "송정동", "옥수동", "왕십리2동", "용답동", "응봉동", "하왕십리동", "행당1동", "행당2동",
				"행당동", "홍익" ],
		[ "길음1동", "길음2동", "길음동", "돈암1동", "돈암2동", "돈암동", "동선동1가", "동선동2가", "동선동3가", "동선동4가", "동선동5가", "동소문동1가", "동소문동2가", "동소문동3가", "동소문동4가", "동소문동5가", "동소문동6가", "동소문동7가", "보문동1가", "보문동2가", "보문동3가",
				"보문동4가", "보문동5가", "보문동6가", "보문동7가", "삼선동1가", "삼선동2가", "삼선동3가", "삼선동4가", "삼선동5가", "상월곡동", "석관동", "성북동", "성북동1가", "안암동1가", "안암동2가", "안암동3가", "암암동4가", "안암동5가", "월곡1동", "월곡2동", "장위1동",
				"장위2동", "장위3동", "장위동", "정릉1동", "정릉2동", "정릉3동", "정릉4동", "정릉동", "종암동", "하월곡동" ],
		[ "가락1동", "가락2동", "가락동", "가락본동", "거여1동", "거여2동", "거여동", "마천1동", "마천2동", "마천동", "문정1동", "문정2동", "문정동", "방이1동", "방이2동", "방이동", "삼전동", "석촌동", "송파1동", "송파2동", "송파동", "신천동", "오금동", "오륜동", "잠실2동",
				"잠실3동", "잠실4동", "잠실6동", "잠실7동", "잠실동", "잠실본동", "장지동", "풍납1동", "푼납2동", "풍납동" ],
		[ "목1동", "목2동", "목3동", "목4동", "목5동", "목동", "신월1동", "신월2동", "신월3동", "신월4동", "신월5동", "신월6동", "신월7동", "신월동", "신정1동", "신정2동", "신정3동", "신정4동", "신정6동", "신정7동", "신정" ],
		[ "당산동", "당산동1가", "당산동2가", "당산동3가", "당산동4가", "당산동5가", "당산동6가", "대림1동", "대림2동", "대림3동", "대림동", "도림동", "문래동1가", "문래동2가", "문래동3가", "문래동4가", "문래동5가", "문래동6가", "신길1동", "신길3동", "신길4동", "신길5동",
				"신일6동", "신길7동", "신길동", "양평동", "양평동1가", "양평동2가", "양평동3가", "양평동4가", "양평동5가", "양평동6가", "양화동", "여의도동", "영등포동", "영등포동1가", "영등포동2가", "영등포동3가", "영등포동4가", "영등포동5가", "영등포동6가", "영등포동7가",
				"영등포동8가", "영등포본동" ],
		[ "갈월동", "남영동", "도원동", "동빙고동", "동자동", "문배동", "보광동", "산천동", "서계동", "서빙고동", "신계동", "신창동", "용문동", "용산동1가", "용산동2가", "용산동3가", "용산동4가", "용산동5가", "용산동6가", "원효로1가", "원효로2가", "원효로3가", "원효로4가",
				"이촌1동", "이촌2동", "이촌동", "이태원1동", "이태원2동", "이태원동", "주성동", "청암동", "청파동1가", "청파동2가", "청파동3가", "한강로1가", "한강로2가", "한강로3가", "한남동", "효창동", "후암동" ],
		[ "갈현1동", "갈현2동", "갈현동", "구산동", "녹번동", "대조동", "불광1동", "불광2동", "불광동", "수색동", "신사1동", "신사2동", "신사동", "역촌동", "응암1동", "응암2동", "응암3동", "응암동", "증산동", "진관동" ],
		[ "가회동", "견지동", "경운동", "계동", "공평동", "관수동", "관철동", "관훈동", "교남동", "교북동", "구기동", "궁정동", "권농동", "낙원동", "내수동", "내자동", "누상동", "누하동", "당주동", "도렴동", "돈의동", "동승동", "명륜1가", "명륜2가", "명륜3가", "명륜4가",
				"묘동", "무악동", "봉익동", "부암동", "사간동", "사직동", "삼청동", "서린동", "세종로", "소격동", "송월동", "송현동", "수송동", "숭인1동", "숭인2동", "숭인동", "신교동", "신문로1가", "신문로2가", "신영동", "안국동", "연건동", "연지동", "예지동", "옥인동",
				"와룡동", "운니동", "원남동", "원서동", "이화동", "익선동", "인사동", "인의동", "장사동", "재동", "적선동", "종로1가", "종로2가", "종로3가", "종로4가", "종로5가", "종로6가", "중학동", "창성동", "창신1동", "창신2동", "창신3동", "창신동", "청운동", "청진동",
				"체부동", "충신동", "통의동", "통인동", "팔판동", "평동", "평창동", "필운동", "행촌동", "혜화동", "홍지동", "홍파동", "화동", "효자동", "효제동", "훈정동" ],
		[ "광희동1가", "광희동2가", "남대문로1가", "남대문로2가", "남대문로3가", "남대문로4가", "남대문로5가", "남산동1가", "남산동2가", "남산동3가", "남창동", "남학동", "다동", "만리동1가", "만리동2가", "명동1가", "명동2가", "무교동", "무학동", "묵정동", "방산동", "봉래동1가",
				"봉래동2가", "북창동", "산림동", "삼각동", "서소문동", "소공동", "수표동", "수하동", "순화동", "신당1동", "신당2동", "신당3동", "신당4동", "신당5동", "신당6동", "신당동", "쌍림동", "예관동", "예장동", "오장동", "을지로1가", "을지로2가", "을지로3가",
				"을지로4가", "을지로5가", "을지로6가", "을지로7가", "의주로1가", "의주로2가", "인현동1가", "인현동2가", "입정동", "장교동", "장충동1가", "장충동2가", "저동1가", "저동2가", "정동", "주교동", "주자동", "중림동", "초동", "충무로1가", "충무로2가", "충무로3가",
				"충무로4가", "충무로5가", "충정로1가", "태평로1가", "태평로2가", "필동1가", "필동2가", "필동3가", "황학동", "회현동1가", "회현동2가", "회현동3가", "흥인동" ],
		[ "망우3동", "망우동", "망우본동", "면목2동", "면목3.8동", "면목4동", "면목5동", "면목7동", "면목동", "면목본동", "묵1동", "묵2동", "묵동", "상봉1동", "상봉2동", "상봉동", "신내1동", "신내2동", "신내동", "중화1동", "중화2동", "중화동" ] ];

function setAddressHtml() {
	getHtmlCode("./address", function(html) {
		var body = $("body").append(html);
		var addressModal = $("#addressModal");
		addressModal.modal({
			backdrop : "",
			keyboard : false
		});

		$("#joinModal").hide();

		$(".address-first").selectpicker({
			size : 5,
			width : 270
		});

		$(".address-second").selectpicker({
			size : 5,
			width : 270
		});

		$(".address-third").selectpicker({
			size : 5,
			width : 270
		});

		addressModal.modal("show");
		addressModal.on("hidden.bs.modal", function() {
			$("#joinModal").show();
			$(this).remove();
		});

		$("#button-address-ok").click(function() {
			setAddressText();
			var addressText = getAddressText();
			if (addressText != "") {
				$("#join-address").text(addressText);
			}

			addressModal.modal("hide");
		});

		var selected = $(".bootstrap-select.address-second").find("a");
		selected.click(function() {
			var index = selected.index(this);
			var select = $("select.address-third");
			select.find("option").remove();
			for (var i = 0; i < addressData[index].length; i++) {
				select.append("<option value='" + addressData[index][i] + "'>" + addressData[index][i] + "</option>");
			}
			$(".address-third").selectpicker('refresh');
			$(".address-third").selectpicker('refresh');
		});
	});
}

function getAddressText() {
	var text = "";
	if (addressStorage.addressBuilding == "") {
		text = addressStorage.addressFirst + " " + addressStorage.addressSecond + " " + addressStorage.addressThird + " " + addressStorage.addressNumber + " " + addressStorage.addressRemainder;
	} else if (addressStorage.addressNumber == "") {
		text = addressStorage.addressFirst + " " + addressStorage.addressSecond + " " + addressStorage.addressThird + " " + addressStorage.addressBuilding + " " + addressStorage.addressRemainder;
	} else {
		text = addressStorage.addressFirst + " " + addressStorage.addressSecond + " " + addressStorage.addressThird + " " + addressStorage.addressNumber + " " + addressStorage.addressBuilding + " "
				+ addressStorage.addressRemainder;
	}
	var textLength = addressStorage.addressNumber.length + addressStorage.addressBuilding.length + addressStorage.addressRemainder.length;
	if (textLength.length != 0) {
		return text;
	} else {
		return "주소 (선택사항)";
	}
}

function setAddressText() {
	addressStorage.addressFirst = $(".address-first").val();
	addressStorage.addressSecond = $(".address-second").val();
	addressStorage.addressThird = $(".address-third").val();
	addressStorage.addressNumber = $("#address-number").val();
	addressStorage.addressBuilding = $("#address-building").val();
	addressStorage.addressRemainder = $("#address-remainder").val();
}

function setJoinClickEvent() {
	$("#join-logid").keypress(function(event) {
		if (event.keyCode == 13) {
			$("#join-logpw").focus();
		}
	});

	$("#join-logpw").keypress(function(event) {
		if (event.keyCode == 13) {
			$("#join-logpw2").focus();
		}
	});

	$("#join-logpw2").keypress(function(event) {
		if (event.keyCode == 13) {
			$("#button-join-ok").click();
		}
	});

	$("#join-phone").keypress(function(event) {
		if (event.keyCode == 13) {
			$("#button-join-ok").click();
		}
	});
}

function setOrderState() {
	var accountButton = $("#account-button");
	$("#buttton-order-history").click(function(e) {
		if (accountButton.hasClass("login-state")) {
			getOrderInfo(function(json) {
				setOrderInfo();
				var constant = new Constant();

				if (json.constant = !constant.SUCCESS) {
					alert("서버 오류가 발생하였습니다.");
					return;
				}

				var deliverers = JSON.parse(json.data);

				if (deliverers.length != 0) {
					getHours(function(timeData) {
						getHtmlCode("./order/history", function(html) {
							var body = $("body").append(html);
							var historyModal = $("#historyModal");
							historyModal.modal({
								backdrop : "static",
								keyboard : false
							});
							historyModal.on("hidden.bs.modal", function() {
								setOrderInfo();
								$(this).remove();
							});

							var panelGroup = historyModal.find(".panel-group");
							var historyElement = historyModal.find(".panel.panel-default.temp").clone();
							historyModal.find(".panel.panel-default.temp").remove();
							var divWarp = $("<div />");

							for ( var i in deliverers) {
								var deliverer = deliverers[i];
								var history = historyElement.clone().removeClass("temp").addClass(replaceStr(deliverer.order_number));
								var historyTitle = history.find(".history-title").attr("href", "#" + deliverer.order_number);
								historyTitle.find(".order-number").text(deliverer.order_number);
								var historyContent = history.find(".panel-collapse.collapse.history-content").attr("id", deliverer.order_number);

								if (i != 0) {
									historyContent.removeClass("in");
								}

								var priceText = null;
								if (deliverer.coupon.length != 0) {
									var value = deliverer.coupon[0].value;
									priceText = deliverer.price + "원 (쿠폰 -" + value + "원) (배송 " + deliverer.dropoff_price + "원)";
								} else {
									priceText = deliverer.price + "원 (배송 " + deliverer.dropoff_price + "원)";
								}

								var itemArray = new Array();
								var itemText = null;
								for ( var i in deliverer.item) {
									itemArray.push(deliverer.item[i].name + "(" + deliverer.item[i].count + ")");
								}
								itemText = itemArray.join(", ");

								var historyDelivererPic = history.find(".history-deliverer-pic");
								var historyDelivererName = history.find(".history-deliverer-name");
								var stateText = null;
								switch (deliverer.state) {
								case 0:
									stateText = "주문완료";
									historyDelivererPic.css("background-image", 'url("./resources/cleanbasket/images/deliverer2.png")');
									historyDelivererPic.css("border-radius", 0);
									historyDelivererPic.css("-webkit-border-radius", 0);
									historyDelivererPic.css("-moz-border-radius", 0);
									historyDelivererName.text("미지정");
									break;
								case 1:
									stateText = "수거준비";
									historyDelivererPic.css("background-image", 'url("./' + deliverer.pickupInfo.img + '")');
									historyDelivererPic.css("border-radius", 50);
									historyDelivererPic.css("-webkit-border-radius", 50);
									historyDelivererPic.css("-moz-border-radius", 50);
									historyDelivererName.text(deliverer.pickupInfo.name);
									break;
								case 2:
									stateText = "수거완료/세탁중";
									historyDelivererPic.css("background-image", 'url("./' + deliverer.pickupInfo.img + '")');
									historyDelivererPic.css("border-radius", 50);
									historyDelivererPic.css("-webkit-border-radius", 50);
									historyDelivererPic.css("-moz-border-radius", 50);
									historyDelivererName.text(deliverer.pickupInfo.name);
									break;
								case 3:
									stateText = "배달준비";
									historyDelivererPic.css("background-image", 'url("./' + deliverer.pickupInfo.img + '")');
									historyDelivererPic.css("border-radius", 50);
									historyDelivererPic.css("-webkit-border-radius", 50);
									historyDelivererPic.css("-moz-border-radius", 50);
									historyDelivererName.text(deliverer.dropoffInfo.name);
									break;
								case 4:
									stateText = "배달완료";
									historyDelivererPic.css("background-image", 'url("./' + deliverer.pickupInfo.img + '")');
									historyDelivererPic.css("border-radius", 50);
									historyDelivererPic.css("-webkit-border-radius", 50);
									historyDelivererPic.css("-moz-border-radius", 50);
									historyDelivererName.text(deliverer.dropoffInfo.name);
									break;
								}

								var pickupDate = deliverer.pickup_date.split(" ");
								var pickupDateText = pickupDate[0] + " " + pickupDate[1].split(":")[0] + "~" + (Number(pickupDate[1].split(":")[0]) + 1) + "시";

								var dropoffDate = deliverer.dropoff_date.split(" ");
								var dropoffDateText = dropoffDate[0] + " " + dropoffDate[1].split(":")[0] + "~" + (Number(dropoffDate[1].split(":")[0]) + 1) + "시";

								var historyText = historyContent.find(".history-text");
								historyText.eq(0).text(priceText);
								historyText.eq(1).text(itemText);
								historyText.eq(2).text(stateText);
								historyText.eq(3).text(pickupDateText);
								historyText.eq(4).text(dropoffDateText);

								var buttonHistoryDelete = historyContent.find(".button-history-delete");
								var datetime = JSON.parse(timeData.data);
								var year = parseInt(datetime.year);
								var month = parseInt(datetime.month);
								var day = parseInt(datetime.day);
								var hours = parseInt(datetime.hours);

								if (pickupDate[0] == (year + "-" + month + "-" + day).toString() && hours >= (Number(pickupDate[1].split(":")[0]) - 1) && deliverer.state == 1) {
									buttonHistoryDelete.attr("href", "javascript:delOrder(" + deliverer.oid + ", " + replaceStr(deliverer.order_number) + ", false);")
								} else {
									buttonHistoryDelete.attr("href", "javascript:delOrder(" + deliverer.oid + ", " + replaceStr(deliverer.order_number) + ", true);")
								}

								divWarp.append(history);
							}

							panelGroup.append(divWarp.html());
							historyModal.modal("show");
						});
					});
				} else {
					setOrderInfo();
					alert("진행중인 세탁물이 없습니다.");
					return;
				}
			});
		} else {
			accountButton.click();
		}
	});
}

function delOrder(oid, order_number, able) {
	if (able) {
		$.ajax({
			type : 'POST',
			url : './member/order/del',
			contentType : "application/json",
			dataType : 'json',
			async : true,
			data : JSON.stringify({
				oid : oid
			}),
			success : function(json) {
				var constant = new Constant();
				switch (json.constant) {
				case constant.SUCCESS:
					alert("주문이 취소되었습니다.");
					$("#historyModal").find(".panel.panel-default." + order_number).remove();
					setOrderInfo();
					break;
				case constant.IMPOSSIBLE:
					alert("세탁물이 수거된 이후에는 취소가 불가능합니다.\n필요한 경우 고객센터로 연락바랍니다.");
					break;
				case constant.ERROR:
					alert("서버에 오류가 발생하였습니다.");
					break;
				}
			},
			error : function(request, status, error) {
				errorCheck(request.responseText);
			}
		});
	} else {
		alert("수거시간 전 1시간 이내는 취소가 불가능합니다.\n필요한 경우 고객센터로 연락바랍니다.");
	}
}

function setCouponIssue() {
	var accountButton = $("#account-button");
	$("#button-coupon-issue").click(function(e) {
		if (accountButton.hasClass("login-state")) {
			$.ajax({
				type : 'POST',
				url : './member/coupon/issue',
				contentType : "application/json",
				dataType : 'json',
				async : true,
				data : JSON.stringify({
					serial_number : $("#coupon-issue-text").val()
				}),
				success : function(json) {
					var constant = new Constant();
					switch (json.constant) {
					case constant.SUCCESS:
						alert("쿠폰이 발급되었습니다.");
						setCoupon();
						break;
					case constant.DUPLICATION:
						alert("더 이상 발급받을 수 없습니다.");
						break;
					case constant.INVALID:
						alert("유효하지 않은 쿠폰코드입니다.");
						break;
					case constant.ERROR:
						alert("서버에 오류가 발생하였습니다.");
						break;
					}
				},
				error : function(request, status, error) {
					errorCheck(request.responseText);
				}
			});
		} else {
			accountButton.click();
		}
	});
}

function joinProcess() {
	var joinLogid = $("#join-logid");
	var joinLogpw = $("#join-logpw");
	var joinLogpw2 = $("#join-logpw2");
	var phone = $("#join-phone");
	var agreement = $("#agreement-all");

	if (joinLogid.val() == "") {
		alert("이메일 주소를 입력해야 합니다.");
		joinLogid.focus();
		return;
	}

	if (joinLogid.val().length > 60) {
		alert("이메일 주소는 60자 이하로 입력해야합니다.");
		joinLogid.focus();
		return;
	}

	if (joinLogpw.val() == "") {
		alert("비밀번호를 입력해야합니다.");
		joinLogpw.focus();
		return;
	}

	if (joinLogpw.val().length < 6 || joinLogpw.val().length > 10) {
		alert("비밀번호는 6자리 이상 10자리 이하로 입력해야합니다.");
		joinLogpw.focus();
		return;
	}

	if (joinLogpw2.val() == "") {
		alert("재확인 비밀번호를 입력해야합니다.");
		joinLogpw2.focus();
		return;
	}

	if (joinLogpw.val() != joinLogpw2.val()) {
		alert("비밀번호가 동일하지 않습니다.");
		joinLogpw.focus();
		return;
	}

	if (!agreement.find("i").hasClass("active")) {
		alert("약관에 동의해야 합니다.");
		return;
	}

	var address = null;
	var textLength = addressStorage.addressNumber.length + addressStorage.addressBuilding.length + addressStorage.addressRemainder.length;
	if (textLength.length != 0) {
		address = addressStorage.addressFirst + " " + addressStorage.addressSecond + " " + addressStorage.addressThird;
	} else {
		address = "";
	}

	$.ajax({
		type : 'POST',
		url : './member/join',
		contentType : "application/json",
		dataType : 'json',
		async : true,
		data : JSON.stringify({
			email : joinLogid.val(),
			password : joinLogpw.val(),
			phone : phone.val(),
			address : address,
			addr_number : addressStorage.addressNumber,
			addr_building : addressStorage.addressBuilding,
			addr_remainder : addressStorage.addressRemainder
		}),
		success : function(json) {
			var constant = new Constant();
			switch (json.constant) {
			case constant.SUCCESS:
				alert("회원가입이 성공하였습니다.");
				$("#joinModal").modal("hide");
				break;
			case constant.ACCOUNT_DUPLICATION:
				alert("이메일 주소 중복입니다.");
				joinLogid.focus();
				break;
			case constant.ERROR:
				alert("서버에 오류가 발생하였습니다.");
				break;
			}
		},
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function setAgreementButton() {
	$("#agreement-all").click(function(e) {
		var i = $(this).find("i");
		i.toggleClass("active");
		if (i.hasClass("active")) {
			$("#agreement-use").find("i").addClass("active");
			$("#agreement-privacy").find("i").addClass("active");
		} else {
			$("#agreement-use").find("i").removeClass("active");
			$("#agreement-privacy").find("i").removeClass("active");
		}
	});

	$("#agreement-use").click(function(e) {
		var i = $(this).find("i");
		i.toggleClass("active");
		if (i.hasClass("active")) {
			if ($("#agreement-privacy").find("i").hasClass("active")) {
				$("#agreement-all").find("i").addClass("active");
			}
		} else {
			$("#agreement-all").find("i").removeClass("active");
		}
	});

	$("#agreement-privacy").click(function(e) {
		var i = $(this).find("i");
		i.toggleClass("active");
		if (i.hasClass("active")) {
			if ($("#agreement-use").find("i").hasClass("active")) {
				$("#agreement-all").find("i").addClass("active");
			}
		} else {
			$("#agreement-all").find("i").removeClass("active");
		}
	});
}

function setOrderCalculationHtml() {
	getHtmlCode("./order/calc", function(html) {
		$("#button-order-ready").click(function() {
			getItemData(function(item) {
				getCouponData(function(coupon) {
					var body = $("body").append(html);
					var calcModal = $("#calcModal");
					calcModal.modal({
						backdrop : "static",
						keyboard : false
					})
					calcModal.modal("show");
					calcModal.on("hidden.bs.modal", function() {
						itemSelect = 1;
						$("body").css("overflow-y", "auto");
						$(this).remove();
					});
					calcModal.on("shown.bs.modal", function() {
						$("body").css("overflow-y", "hidden");
					});
					orderCalculationProcess(calcModal, item, coupon);
				});
			});
		});
	});
}

function setOrderCalculationEtcHtml() {
	getHtmlCode("./order/calc/etc", function(html) {
		var body = $("body").append(html);
		var calcEtcModal = $("#calcEtcModal");
		var ul = $("#order-etc-list");
		var li = ul.find("li:eq(0)").clone();
		ul.find("li").remove();

		jQuery.each(items, function(key, val) {
			if (val.item_code >= 101) {
				var itemElement = li.clone();
				var name = itemElement.find("#order-etc-name");
				var price = itemElement.find("#order-etc-price");
				var count = itemElement.find("#order-etc-count");
				var add = itemElement.find("#order-etc-add");
				var sub = itemElement.find("#order-etc-sub");
				name.text(val.name);
				price.text(val.price);
				count.text(val.count);
				add.attr("href", "javascript:etcItemAdd(" + key + ");");
				sub.attr("href", "javascript:etcItemSub(" + key + ");");
				ul.append(itemElement);
			}
		});

		calcEtcModal.modal({
			backdrop : "",
			keyboard : false
		});

		calcEtcModal.modal("show");
		calcEtcModal.on("hidden.bs.modal", function() {
			var calcResult = priceCalculation();
			$("#order-item-count").text(countCalculationEtc());

			if (calcResult.coupon == 0) {
				$("#item-price").text(calcResult.price + "원").css("color", "#555");
				$("#dropoff-price").text(" +" + calcResult.dropoff + "원(배송)");
				$("#order-total-price").text((calcResult.price + calcResult.dropoff) + "원");
			} else {
				$("#item-price").text((calcResult.price - calcResult.coupon) + "원").css("color", "#52c8b5");
				$("#dropoff-price").text(" +" + calcResult.dropoff + "원(배송)");
				$("#order-total-price").text((calcResult.price + calcResult.dropoff - calcResult.coupon) + "원");
			}

			$("body").css("overflow-y", "hidden");
			$(this).remove();
		});
		calcEtcModal.on("shown.bs.modal", function() {
			$("body").css("overflow-y", "hidden");
			$("#order-etc-sumprice li:eq(0)").text("합계 : " + priceCalculationEtc() + "원");
		});
	});
}

function etcItemAdd(index) {
	items[index].count++;
	$("#etc-count-" + index).text(items[index].count);
	$("#order-etc-sumprice li:eq(0)").text("합계 : " + priceCalculationEtc() + "원");
	$("#order-etc-list li:eq(" + (index - 101) + ")").find("#order-etc-count").text(items[index].count);
}

function etcItemSub(index) {
	if (items[index].count > 0) {
		items[index].count--;
		$("#etc-count-" + index).text(items[index].count);
		$("#order-etc-sumprice li:eq(0)").text("합계 : " + priceCalculationEtc() + "원");
		$("#order-etc-list li:eq(" + (index - 101) + ")").find("#order-etc-count").text(items[index].count);
	}
}

var items = {};
var coupons = {};
var itemSelect = 1;
var itemImageURL = {
	1 : "./resources/cleanbasket/images/y-shirt.png",
	2 : "./resources/cleanbasket/images/suit.png",
	3 : "./resources/cleanbasket/images/pants.png",
	4 : "./resources/cleanbasket/images/blouse.png",
	5 : "./resources/cleanbasket/images/onepiece.png",
	6 : "./resources/cleanbasket/images/coat.png",
	7 : "./resources/cleanbasket/images/etc.png"
};

function orderCalculationProcess(calcModal, item, coupon) {
	var itemData = JSON.parse(item.data);
	var itemContent = $("#item-content");
	var itemTitle = $("#item-title");
	var price = $("#order-price");
	var itemPrice = $("#item-price");
	var dropoffPrice = $("#dropoff-price");
	var totalPrice = $("#order-total-price");
	var count = $("#order-item-count");

	for ( var i in itemData) {
		itemData[i]["count"] = 0;
		items[itemData[i].item_code] = itemData[i];
	}

	var couponData = JSON.parse(coupon.data);

	for ( var i in couponData) {
		couponData[i]["used"] = false;
		coupons[couponData[i].cpid] = couponData[i];
	}

	$("#item-left").click(function() {
		if (itemSelect > 1) {
			itemSelect--;
			itemContent.css("background-image", 'url("' + itemImageURL[itemSelect] + '")');
			itemContent.css("cursor", "default");
			itemTitle.text(items[itemSelect].name);
			price.text(items[itemSelect].price + "원");
			count.text(items[itemSelect].count);
		}
	});

	$("#item-right").click(function() {
		if (itemSelect < 7) {
			itemSelect++;
			itemContent.css("background-image", 'url("' + itemImageURL[itemSelect] + '")');
			if (itemSelect != 7) {
				itemTitle.text(items[itemSelect].name);
				price.text(items[itemSelect].price + "원");
				count.text(items[itemSelect].count);
			} else {
				itemContent.css("cursor", "pointer");
				itemTitle.text("기타");
				price.text("- 원");
				count.text(countCalculationEtc());
			}
		}
	});

	$("#order-item-add").click(function() {
		if (itemSelect != 7) {
			items[itemSelect].count++;
			count.text(items[itemSelect].count);
			var calcResult = priceCalculation();
			if (calcResult.coupon == 0) {
				itemPrice.text(calcResult.price + "원").css("color", "#555");
				dropoffPrice.text(" +" + calcResult.dropoff + "원(배송)");
				totalPrice.text((calcResult.price + calcResult.dropoff) + "원");
			} else {
				itemPrice.text((calcResult.price - calcResult.coupon) + "원").css("color", "#52c8b5");
				dropoffPrice.text(" +" + calcResult.dropoff + "원(배송)");
				totalPrice.text((calcResult.price + calcResult.dropoff - calcResult.coupon) + "원");
			}
		} else {
			setOrderCalculationEtcHtml();
		}
	});

	$("#order-item-sub").click(function() {
		if (itemSelect != 7) {
			if (items[itemSelect].count > 0) {
				items[itemSelect].count--;
				count.text(items[itemSelect].count);
				var calcResult = priceCalculation();
				if (calcResult.coupon == 0) {
					itemPrice.text(calcResult.price + "원").css("color", "#555");
					dropoffPrice.text(" +" + calcResult.dropoff + "원(배송)");
					totalPrice.text((calcResult.price + calcResult.dropoff) + "원");
				} else {
					itemPrice.text((calcResult.price - calcResult.coupon) + "원").css("color", "#52c8b5");
					dropoffPrice.text(" +" + calcResult.dropoff + "원(배송)");
					totalPrice.text((calcResult.price + calcResult.dropoff - calcResult.coupon) + "원");
				}
			}
		} else {
			setOrderCalculationEtcHtml();
		}
	});

	$("#item-content").click(function() {
		if (itemSelect == 7) {
			setOrderCalculationEtcHtml();
		}
	});

	$("#order-coupon-apply").click(function() {
		if ($(this).hasClass("unused")) {
			var clacData = priceCalculation();
			if (clacData.price < 10000) {
				alert("10000원 이상인 경우 쿠폰사용이 가능합니다.");
				return;
			} else {
				couponApply();
			}
		} else if ($(this).hasClass("used")) {
			couponCancel();
		}
	});

	$("#button-order-decide").click(function() {
		if ($("#order-address-text").text().indexOf("강남구") != -1 || $("#order-address-text").text().indexOf("서초구") != -1 || $("#order-address-text").text().indexOf("용산") != -1 || || $("#order-address-text").text().indexOf("마포구") != -1 || || $("#order-address-text").text().indexOf("성동구") != -1) {

			var clacData = priceCalculation();

			if (clacData.price < 10000) {
				alert("주문금액이 10000원 이상시 가능합니다.");
				return;
			}

			getUserInfo(function(json) {
				var data = JSON.parse(json.data);
				var pickupDay = dateCalc(pickupDate.datepicker("getDate"), $(".order-select-pickup"));
				var dropoffDay = dateCalc(dropoffDate.datepicker("getDate"), $(".order-select-dropoff"));

				if (data.phone == null || data.phone == "") {
					alert("개인설정에서 연락처를 설정해주세요.");
					return;
				}

				$.ajax({
					type : 'POST',
					url : './member/order/add',
					contentType : "application/json",
					dataType : 'json',
					async : true,
					data : JSON.stringify({
						adrid : $(".order-select-address").selectpicker('val'),
						phone : data.phone,
						memo : $("#order-memo").val(),
						price : clacData.price + clacData.dropoff - clacData.coupon,
						dropoff_price : clacData.dropoff,
						pickup_date : pickupDay,
						dropoff_date : dropoffDay,
						item : itemDataCalculation(),
						cpid : couponDataCalculation()
					}),
					success : function(json) {
						var constant = new Constant();
						if (json.constant == constant.SUCCESS) {
							alert("주문이 접수되었습니다.");
							setOrderInfo();
							setCoupon();
							calcModal.modal("hide");
						} else {
							alert("오류가 발생하였습니다.");
						}
					},
					error : function(request, status, error) {
						errorCheck(request.responseText);
					}
				});
			});
		} else {
			alert("강남구와 서초구 지역만 가능합니다.");
		}
	});
}

function couponApply() {
	getHtmlCode("./order/coupon", function(html) {
		$("#calcModal").hide();
		var body = $("body").append(html);
		var couponModal = $("#couponModal");
		couponModal.modal({
			backdrop : "",
			keyboard : false
		});
		couponModal.on("hidden.bs.modal", function() {
			$("#calcModal").show();
			couponModal.modal("hide");
		});

		var couponElement = couponModal.find("#temp").clone();
		couponModal.find("#temp").remove();
		var divWarp = $("<div />")

		if (Object.keys(coupons).length != 0) {
			jQuery.each(coupons, function(key, val) {
				var coupon = couponElement.clone().attr("id", key);
				if (val.kind == 0) {
					coupon.find(".coupon-text").text("가입감사 쿠폰. 2000원 할인권!");
				} else if (val.kind == 1) {
					coupon.find(".coupon-text").text("친구추천 쿠폰. 2000원 할인권!");
				}
				divWarp.append(coupon);
			});
		} else {
			divWarp.append($('<div style="font-size: 12px; text-align: center;">보유중인 쿠폰이 없습니다.</div>'));
		}

		couponModal.find(".modal-body").append(divWarp.html());
		couponModal.modal("show");

		couponModal.find(".btn-coupon").click(function(e) {
			var cpid = $(this).attr("id");
			coupons[cpid].used = true;
			var calcResult = priceCalculation();

			if (calcResult.coupon == 0) {
				$("#item-price").text(calcResult.price + "원").css("color", "#555");
				$("#order-total-price").text((calcResult.price + calcResult.dropoff) + "원");
			} else {
				$("#item-price").text((calcResult.price - calcResult.coupon) + "원").css("color", "#52c8b5");
				$("#order-total-price").text((calcResult.price + calcResult.dropoff - calcResult.coupon) + "원");
			}

			var button = $("#order-coupon-apply");
			button.addClass("used");
			button.removeClass("unused");
			button.text("쿠폰취소");

			couponModal.modal("hide");
		});
	});
}

function couponCancel() {
	var button = $("#order-coupon-apply");
	button.addClass("unused");
	button.removeClass("used");
	button.text("쿠폰적용");

	jQuery.each(coupons, function(key, val) {
		if (val.used) {
			coupons[key].used = false;
		}
	});

	var calcResult = priceCalculation();

	if (calcResult.coupon == 0) {
		$("#item-price").text(calcResult.price + "원").css("color", "#555");
		$("#order-total-price").text((calcResult.price + calcResult.dropoff) + "원");
	} else {
		$("#item-price").text((calcResult.price - calcResult.coupon) + "원").css("color", "#52c8b5");
		$("#order-total-price").text((calcResult.price + calcResult.dropoff - calcResult.coupon) + "원");
	}
}

function priceCalculation() {
	var result = {
		price : 0,
		dropoff : 2000,
		coupon : 0
	};
	jQuery.each(items, function(key, val) {
		if (val.count != 0) {
			result.price += val.price * val.count;
		}
	});
	jQuery.each(coupons, function(key, val) {
		if (val.used) {
			result.coupon = val.value;
		}
	});
	if (result.price >= 20000) {
		result.dropoff = 0;
	}
	return result;
}

function itemDataCalculation() {
	var result = [];
	jQuery.each(items, function(key, val) {
		if (val.count != 0) {
			var data = {
				item_code : val.item_code,
				count : val.count
			}
			result.push(data);
		}
	});
	return result;
}

function couponDataCalculation() {
	var result = [];
	jQuery.each(coupons, function(key, val) {
		if (val.used) {
			result.push(key);
		}
	});
	return result;
}

function priceCalculationEtc() {
	var price = 0;
	jQuery.each(items, function(key, val) {
		if (val.item_code >= 101 && val.count != 0) {
			price += val.price * val.count;
		}
	});
	return price;
}

function countCalculationEtc() {
	var count = 0;
	jQuery.each(items, function(key, val) {
		if (val.item_code >= 101 && val.count != 0) {
			count += val.count;
		}
	});
	return count;
}

function setAddressSettings(type) {
	getHtmlCode("./address", function(html) {
		var body = $("body").append(html);
		var addressModal = $("#addressModal");
		addressModal.modal({
			backdrop : "static",
			keyboard : false
		});

		$(".address-first").selectpicker({
			size : 5,
			width : 270
		});

		$(".address-second").selectpicker({
			size : 5,
			width : 270
		});

		$(".address-third").selectpicker({
			size : 5,
			width : 270
		});

		$(".address-footer").append('<button type="button" id="button-address-delete" class="btn btn-info">삭제</button>');

		addressModal.modal("show");
		addressModal.on("hidden.bs.modal", function() {
			$(this).remove();
		});

		$("#button-address-ok").click(function(e) {
			setAddressText();
			var addressText = getAddressText();
			if (addressText != "") {
				var address = addressStorage.addressFirst + " " + addressStorage.addressSecond + " " + addressStorage.addressThird;
				updateAddress(JSON.stringify({
					type : type,
					address : address,
					addr_number : addressStorage.addressNumber,
					addr_building : addressStorage.addressBuilding,
					addr_remainder : addressStorage.addressRemainder
				}), function(json) {
					var constant = new Constant();
					if (json.constant = constant.SUCCESS) {
						$("#addressModal").modal("hide");
						setUserInfo();
					} else {
						alert("오류가 발생하였습니다.");
					}
				});
			}
		});

		$("#button-address-delete").click(function(e) {
			updateAddress(JSON.stringify({
				type : type,
				address : "",
				addr_number : "",
				addr_building : "",
				addr_remainder : ""
			}), function(json) {
				var constant = new Constant();
				if (json.constant = constant.SUCCESS) {
					$("#addressModal").modal("hide");
					setUserInfo();
					switch (type) {
					case 0:
						$("#address-home").text("");
						break;
					case 1:
						$("#address-company").text("");
						break;
					case 2:
						$("#address-etc1").text("");
						break;
					case 3:
						$("#address-etc2").text("");
						break;
					case 4:
						$("#address-etc3").text("");
						break;
					}
				} else {
					alert("오류가 발생하였습니다.").text("");
				}
			});
		});

		var selected = $(".bootstrap-select.address-second").find("a");
		selected.click(function() {
			var index = selected.index(this);
			var select = $("select.address-third");
			select.find("option").remove();
			for (var i = 0; i < addressData[index].length; i++) {
				select.append("<option value='" + addressData[index][i] + "'>" + addressData[index][i] + "</option>");
			}
			$(".address-third").selectpicker('refresh');
			$(".address-third").selectpicker('refresh');
		});
	});
}

function setCoupon() {
	getCouponData(function(coupon) {
		var couponList = $("#coupon-list").empty();
		var couponEmpty = $("#coupon-empty");
		var divWarp = $("<div />");
		var couponData = JSON.parse(coupon.data);
		if (couponData.length != 0) {
			couponEmpty.css("display", "none");
			for ( var i in couponData) {
				if (couponData[i].kind == 0) {
					divWarp.append(createCouponElement("가입감사 쿠폰. 2000원 할인권!"));
				} else if (couponData[i].kind == 1) {
					divWarp.append(createCouponElement("친구추천 쿠폰. 2000원 할인권!"));
				}
			}
			couponList.append(divWarp.html());
		} else {
			couponEmpty.css("display", "block");
		}
	});
}

function createCouponElement(text) {
	var coupon = $('<span class="coupon normal-coupon"><span class="coupon-text">' + text + '</span></span>');
	return coupon;
}

function updateAddress(data, success) {
	$.ajax({
		type : 'POST',
		url : './member/address/update',
		contentType : "application/json",
		dataType : 'json',
		async : true,
		data : data,
		success : success,
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function setAccountSettings() {
	var accountButton = $("#account-button");

	$("#account-phone").click(function(e) {
		if (accountButton.hasClass("login-state")) {
			getHtmlCode("./phone-change", function(html) {
				var body = $("body").append(html);
				var phoneModal = $("#phoneModal");
				phoneModal.modal({
					backdrop : "static",
					keyboard : false
				});
				phoneModal.modal("show");
				phoneModal.on("hidden.bs.modal", function() {
					$(this).remove();
				});
				$("#button-phone-change").click(function(e) {
					var phoneChangeText = $("#phone-change").val();
					$.ajax({
						type : 'POST',
						url : './member/phone/update',
						contentType : "application/json",
						dataType : 'json',
						async : true,
						data : JSON.stringify({
							phone : phoneChangeText
						}),
						success : function(json) {
							var constant = new Constant();
							if (json.constant = constant.SUCCESS) {
								phoneModal.modal("hide");
								setUserInfo();
							} else {
								alert("오류가 발생하였습니다.");
							}
						},
						error : function(request, status, error) {
							errorCheck(request.responseText);
						}
					});
				});
			});
		} else {
			accountButton.click();
		}
	});

	$("#account-password").click(function(e) {
		if (accountButton.hasClass("login-state")) {
			getHtmlCode("./password-change", function(html) {
				var body = $("body").append(html);
				var passwordModal = $("#passwordModal");
				passwordModal.modal({
					backdrop : "static",
					keyboard : false
				});
				passwordModal.modal("show");
				passwordModal.on("hidden.bs.modal", function() {
					$(this).remove();
				});
				$("#button-password-change").click(function(e) {
					var passwordChange = $("#password-change");
					var passwordChange2 = $("#password-change2");

					if (passwordChange.val() == "") {
						alert("비밀번호를 입력해야합니다.");
						passwordChange.focus();
						return;
					}

					if (passwordChange.val().length < 6 || passwordChange.val().length > 10) {
						alert("비밀번호는 6자리 이상 10자리 이하로 입력해야합니다.");
						passwordChange.focus();
						return;
					}

					if (passwordChange2.val() == "") {
						alert("재확인 비밀번호를 입력해야합니다.");
						passwordChange2.focus();
						return;
					}

					if (passwordChange.val() != passwordChange2.val()) {
						alert("비밀번호가 동일하지 않습니다.");
						passwordChange.focus();
						return;
					}

					$.ajax({
						type : 'POST',
						url : './member/password/update',
						contentType : "application/json",
						dataType : 'json',
						async : true,
						data : JSON.stringify({
							password : passwordChange.val()
						}),
						success : function(json) {
							var constant = new Constant();
							if (json.constant = constant.SUCCESS) {
								alert("비밀번호가 변경되었습니다.");
								passwordModal.modal("hide");
								setUserInfo();
							} else {
								alert("오류가 발생하였습니다.");
							}
						},
						error : function(request, status, error) {
							errorCheck(request.responseText);
						}
					});
				});
			});
		} else {
			accountButton.click();
		}
	});

	$("#address-home").click(function(e) {
		var type = 0;
		if (accountButton.hasClass("login-state")) {
			setAddressSettings(type);
		} else {
			accountButton.click();
		}
	});

	$("#address-company").click(function(e) {
		var type = 1;
		if (accountButton.hasClass("login-state")) {
			setAddressSettings(type);
		} else {
			accountButton.click();
		}
	});

	$("#address-etc1").click(function(e) {
		var type = 2;
		if (accountButton.hasClass("login-state")) {
			setAddressSettings(type);
		} else {
			accountButton.click();
		}
	});

	$("#address-etc2").click(function(e) {
		var type = 3;
		if (accountButton.hasClass("login-state")) {
			setAddressSettings(type);
		} else {
			accountButton.click();
		}
	});

	$("#address-etc3").click(function(e) {
		var type = 4;
		if (accountButton.hasClass("login-state")) {
			setAddressSettings(type);
		} else {
			accountButton.click();
		}
	});
}

function loginProcess(accountModal) {
	var constant = new Constant();
	var logid = $("#logid");
	var logpw = $("#logpw");

	if (logid.val() == "") {
		alert("이메일을 입력해야 합니다.");
		logid.focus();
		return;
	}
	if (logpw.val() == "") {
		alert("비밀번호를 입력해야 합니다.");
		logpw.focus();
		return;
	}

	$.ajax({
		type : 'POST',
		url : './auth',
		dataType : 'json',
		async : true,
		data : {
			email : logid.val(),
			password : logpw.val(),
			remember : "false",
			wash : "true"
		},
		success : function(json) {
			switch (json.constant) {
			case constant.SUCCESS:
				var accountButton = $("#account-button");
				accountButton.removeClass("logout-state");
				accountButton.addClass("login-state");
				accountModal.modal("hide");
				getRecommendationURL();
				setUserInfo();
				setOrderInfo();
				setCoupon();
				break;
			case constant.ACCOUNT_DISABLED:
				alert("계정이 비활성화 되어 있습니다. 관리자에게 문의하세요.");
				break;
			case constant.ERROR:
				alert("서버에 오류가 발생하였습니다.");
				break;
			case constant.EMAIL_ERROR:
				alert("이메일을 다시 확인하세요.");
				logid.focus();
				break;
			case constant.PASSWORD_ERROR:
				alert("비밀번호를 다시 확인하세요.");
				logpw.focus();
				break;
			}
		},
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function logoutProcess(error) {
	$.ajax({
		type : 'POST',
		url : './logout',
		contentType : "application/json",
		dataType : 'json',
		async : true,
		success : function(json) {
			var accountButton = $("#account-button");
			accountButton.removeClass("login-state");
			accountButton.addClass("logout-state");
			logoutFlag = false;
			if (error) {
				accountButton.click();
			} else {
				location.href = "./";
			}
		},
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function getItemData(success) {
	$.ajax({
		type : 'POST',
		url : './item/code',
		dataType : 'json',
		async : true,
		success : success,
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function getCouponData(success) {
	$.ajax({
		type : 'POST',
		url : './member/coupon',
		dataType : 'json',
		async : true,
		success : success,
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function getHtmlCode(url, success) {
	$.ajax({
		type : 'POST',
		url : url,
		dataType : 'html',
		async : true,
		success : success
	});
};

function setDatePicker() {
	pickupDate = $('#order-pickup-date .date').datepicker({
		format : "D요일 mm/dd",
		language : "kr",
		keyboardNavigation : false,
		autoclose : true
	});

	dropoffDate = $('#order-dropoff-date .date').datepicker({
		format : "D요일 mm/dd",
		language : "kr",
		keyboardNavigation : false,
		autoclose : true
	});

	pickupDate.on("hide", function(e) {
		setDatetimeLimit();
		var dayAfter2 = pickupDate.datepicker("getDate");
		dayAfter2.setDate(dayAfter2.getDate() + 2);
		var dayAfter8 = pickupDate.datepicker("getDate");
		dayAfter8.setDate(dayAfter8.getDate() + 8);
		dropoffDate.datepicker("setStartDate", dayAfter2);
		dropoffDate.datepicker("setEndDate", dayAfter8);
		dropoffDate.datepicker("setDate", dayAfter2);
	});

	getHours(function(json) {
		var datetime = JSON.parse(json.data);
		var year = parseInt(datetime.year);
		var month = parseInt(datetime.month) - 1;
		var day = parseInt(datetime.day);
		var hours = parseInt(datetime.hours);

		var dayNow = new Date(year, month, day);
		if (hours >= 22) {
			dayNow.setDate(dayNow.getDate() + 1);
		}
		var dayAfter6 = new Date(year, month, day);
		dayAfter6.setDate(dayAfter6.getDate() + 6);
		pickupDate.datepicker("setStartDate", dayNow);
		pickupDate.datepicker("setEndDate", dayAfter6);
		pickupDate.datepicker("setDate", dayNow);

		var dayAfter2 = new Date(year, month, day);
		dayAfter2.setDate(dayAfter2.getDate() + 2);
		var dayAfter8 = new Date(year, month, day);
		dayAfter8.setDate(dayAfter8.getDate() + 8);
		dropoffDate.datepicker("setStartDate", dayAfter2);
		dropoffDate.datepicker("setEndDate", dayAfter8);
		dropoffDate.datepicker("setDate", dayAfter2);
	});
}

function setSelectBox() {
	$(".order-select-pickup").selectpicker({
		size : 5,
		width : 284
	});
	$(".order-select-dropoff").selectpicker({
		size : 5,
		width : 284
	});
	$(".order-select-address").selectpicker({
		size : 5,
		width : 120
	});
}

var hoursTextData = {
	10 : "10:00 ~ 11:00",
	11 : "11:00 ~ 12:00",
	12 : "12:00 ~ 13:00",
	13 : "13:00 ~ 14:00",
	14 : "14:00 ~ 15:00",
	15 : "15:00 ~ 16:00",
	16 : "16:00 ~ 17:00",
	17 : "17:00 ~ 18:00",
	18 : "18:00 ~ 19:00",
	19 : "19:00 ~ 20:00",
	20 : "20:00 ~ 21:00",
	21 : "21:00 ~ 22:00",
	22 : "22:00 ~ 23:00",
	23 : "23:00 ~ 24:00"
};

function setDatetimeLimit() {
	getHours(function(json) {
		var datetime = JSON.parse(json.data);
		var day = parseInt(datetime.day);
		var hours = parseInt(datetime.hours);

		var startPoint = 10;

		if (day == pickupDate.datepicker("getDate").getDate()) {
			if (hours >= 9) {
				startPoint = hours + 3;
			}
		}

		var select = $("select.order-select-pickup");
		select.find("option").remove();

		if (startPoint <= 23) {
			for (var i = startPoint; i <= 23; i++) {
				select.append("<option value='" + i + "'>" + hoursTextData[i] + "</option>");
			}
			$(".order-select-pickup").selectpicker('refresh');
			$(".order-select-pickup").selectpicker('refresh');
		}
	});
}

var logoutFlag = false;

function errorCheck(message) {
	if (message.indexOf("<!DOCTYPE html>") != -1) {
		if (logoutFlag == false) {
			logoutFlag = true;
			logoutProcess(true);
		}
	} else {
		console.log(message);
	}
}

function getHours(success) {
	$.ajax({
		type : 'POST',
		url : './hours',
		contentType : "application/json",
		dataType : 'json',
		async : true,
		success : success,
		error : function(request, status, error) {
			errorCheck(request.responseText);
		}
	});
}

function phoneFormat(num) {
	if (num != null && num != undefined) {
		num = replaceStr(num);
		if (num.length == 8) {
			return num.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/, "$1$2-$3");
		} else if (num.length < 8) {
			return num;
		} else {
			return num.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/, "$1-$2-$3");
		}
	} else {
		return "";
	}
}

function replaceStr(number) {
	return number.replace(/[^0-9]/g, '');
}

function getOrderInfo(success) {
	$.ajax({
		type : 'POST',
		url : './member/order',
		contentType : "application/json",
		dataType : 'json',
		async : true,
		success : success,
		error : function(request, status, error) {
			if (request.responseText.indexOf("<!DOCTYPE html>") != -1) {
				logoutProcess(false);
			} else {
				console.log(request.responseText);
			}
		}
	});
}

var orderData = null;
var orderStateImageURL = {
	"deliverer" : "./resources/cleanbasket/images/deliverer.png",
	"state" : "./resources/cleanbasket/images/state.png",
	0 : "./resources/cleanbasket/images/state0.png",
	1 : "./resources/cleanbasket/images/state1.png",
	2 : "./resources/cleanbasket/images/state2.png",
	3 : "./resources/cleanbasket/images/state3.png",
	4 : "./resources/cleanbasket/images/state4.png"
};

function getDelivererDate(text, datetime) {
	var date = datetime.split(" ")[0];
	var time = datetime.split(" ")[1];
	return text + date.split("-")[1] + "월 " + date.split("-")[2] + "일 " + time.split(":")[0] + "-" + (parseInt(time.split(":")[0]) + 1) + "시";
}

function setOrderInfo() {
	getOrderInfo(function(json) {
		var constant = new Constant();
		if (json.constant == constant.SUCCESS) {
			orderData = JSON.parse(json.data);
			var orderStateContent = $("#order-state-content");
			var delivererPic = $("#deliverer-pic");
			var delivererName = $("#deliverer-name");
			var delivererDate = $("#deliverer-date");
			if (orderData.length != 0) {
				var state = orderData[0].state;
				var pickupDate = orderData[0].pickup_date;
				var dropoffDate = orderData[0].dropoff_date;
				var pickupInfo = orderData[0].pickupInfo;
				var dropoffInfo = orderData[0].dropoffInfo;
				switch (state) {
				case 0:
					orderStateContent.css("background-image", 'url("' + orderStateImageURL[state] + '")');
					delivererPic.css("background-image", 'url("' + orderStateImageURL.deliverer + '")');
					delivererPic.css("border-radius", 0);
					delivererPic.css("-webkit-border-radius", 0);
					delivererPic.css("-moz-border-radius", 0);
					delivererName.text("미지정");
					delivererDate.text("");
					break;
				case 1:
					orderStateContent.css("background-image", 'url("' + orderStateImageURL[state] + '")');
					delivererPic.css("background-image", 'url("./' + pickupInfo.img + '")');
					delivererPic.css("border-radius", 50);
					delivererPic.css("-webkit-border-radius", 50);
					delivererPic.css("-moz-border-radius", 50);
					delivererName.text(pickupInfo.name);
					delivererDate.text(getDelivererDate("수거 ", pickupDate));
					break;
				case 2:
					orderStateContent.css("background-image", 'url("' + orderStateImageURL[state] + '")');
					delivererPic.css("background-image", 'url("./' + pickupInfo.img + '")');
					delivererPic.css("border-radius", 50);
					delivererPic.css("-webkit-border-radius", 50);
					delivererPic.css("-moz-border-radius", 50);
					delivererName.text(pickupInfo.name);
					delivererDate.text(getDelivererDate("수거 ", pickupDate));
					break;
				case 3:
					orderStateContent.css("background-image", 'url("' + orderStateImageURL[state] + '")');
					delivererPic.css("background-image", 'url("./' + dropoffInfo.img + '")');
					delivererPic.css("border-radius", 50);
					delivererPic.css("-webkit-border-radius", 50);
					delivererPic.css("-moz-border-radius", 50);
					delivererName.text(dropoffInfo.name);
					delivererDate.text(getDelivererDate("배달 ", dropoffDate));
					break;
				case 4:
					orderStateContent.css("background-image", 'url("' + orderStateImageURL[state] + '")');
					delivererPic.css("background-image", 'url("./' + dropoffInfo.img + '")');
					delivererPic.css("border-radius", 50);
					delivererPic.css("-webkit-border-radius", 50);
					delivererPic.css("-moz-border-radius", 50);
					delivererName.text(dropoffInfo.name);
					delivererDate.text(getDelivererDate("배달 ", dropoffDate));
					break;
				}
			} else {
				orderStateContent.css("background-image", 'url("' + orderStateImageURL.state + '")');
				delivererPic.css("background-image", 'url("' + orderStateImageURL.deliverer + '")');
				delivererPic.css("border-radius", 0);
				delivererPic.css("-webkit-border-radius", 0);
				delivererPic.css("-moz-border-radius", 0);
				delivererName.text("미지정");
				delivererDate.text("");
			}
		}
	});
}

function getUserInfo(success) {
	$.ajax({
		type : 'POST',
		url : './member',
		contentType : "application/json",
		dataType : 'json',
		async : true,
		success : success,
		error : function(request, status, error) {
			if (request.responseText.indexOf("<!DOCTYPE html>") != -1) {
				logoutProcess(false);
			} else {
				console.log(request.responseText);
			}
		}
	});
}

var userData = null;

function setUserInfo() {
	getUserInfo(function(json) {
		var constant = new Constant();
		if (json.constant == constant.SUCCESS) {
			userData = JSON.parse(json.data);
			var orderSelectAddress = $(".order-select-address");
			var select = $("select.order-select-address");
			select.find("option").remove();
			for ( var i in userData.address) {
				switch (userData.address[i].type) {
				case 0:
					select.append('<option value="' + userData.address[i].adrid + '">집</option>');
					$("#address-home").text(userData.address[i].address + " " + userData.address[i].addr_number + " " + userData.address[i].addr_building + " " + userData.address[i].addr_remainder);
					break;
				case 1:
					select.append('<option value="' + userData.address[i].adrid + '">회사</option>');
					$("#address-company")
							.text(userData.address[i].address + " " + userData.address[i].addr_number + " " + userData.address[i].addr_building + " " + userData.address[i].addr_remainder);
					break;
				case 2:
					select.append('<option value="' + userData.address[i].adrid + '">장소1</option>');
					$("#address-etc1").text(userData.address[i].address + " " + userData.address[i].addr_number + " " + userData.address[i].addr_building + " " + userData.address[i].addr_remainder);

					break;
				case 3:
					select.append('<option value="' + userData.address[i].adrid + '">장소2</option>');
					$("#address-etc2").text(userData.address[i].address + " " + userData.address[i].addr_number + " " + userData.address[i].addr_building + " " + userData.address[i].addr_remainder);

					break;
				case 4:
					select.append('<option value="' + userData.address[i].adrid + '">장소3</option>');
					$("#address-etc3").text(userData.address[i].address + " " + userData.address[i].addr_number + " " + userData.address[i].addr_building + " " + userData.address[i].addr_remainder);

					break;
				}
			}
			orderSelectAddress.selectpicker('refresh');
			orderSelectAddress.selectpicker('refresh');
			var selected = $(".bootstrap-select.order-select-address").find("a");
			for (var i = 0; i < selected.length; i++) {
				switch (i) {
				case 0:
					selected.eq(i).click(function() {
						var addr = userData.address[0].address;
						var addrNumber = userData.address[0].addr_number;
						var addrBuilding = userData.address[0].addr_building;
						var addrRemainder = userData.address[0].addr_remainder;
						$("#order-address-text").text(addr + " " + addrNumber + " " + addrBuilding + " " + addrRemainder);
					});
					break;
				case 1:
					selected.eq(i).click(function() {
						var addr = userData.address[1].address;
						var addrNumber = userData.address[1].addr_number;
						var addrBuilding = userData.address[1].addr_building;
						var addrRemainder = userData.address[1].addr_remainder;
						$("#order-address-text").text(addr + " " + addrNumber + " " + addrBuilding + " " + addrRemainder);
					});
					break;
				case 2:
					selected.eq(i).click(function() {
						var addr = userData.address[2].address;
						var addrNumber = userData.address[2].addr_number;
						var addrBuilding = userData.address[2].addr_building;
						var addrRemainder = userData.address[2].addr_remainder;
						$("#order-address-text").text(addr + " " + addrNumber + " " + addrBuilding + " " + addrRemainder);
					});
					break;
				case 3:
					selected.eq(i).click(function() {
						var addr = userData.address[3].address;
						var addrNumber = userData.address[3].addr_number;
						var addrBuilding = userData.address[3].addr_building;
						var addrRemainder = userData.address[3].addr_remainder;
						$("#order-address-text").text(addr + " " + addrNumber + " " + addrBuilding + " " + addrRemainder);
					});
					break;
				case 4:
					selected.eq(i).click(function() {
						var addr = userData.address[4].address;
						var addrNumber = userData.address[4].addr_number;
						var addrBuilding = userData.address[4].addr_building;
						var addrRemainder = userData.address[4].addr_remainder;
						$("#order-address-text").text(addr + " " + addrNumber + " " + addrBuilding + " " + addrRemainder);
					});
					break;
				}
			}
			if (selected.length != 0) {
				selected.eq(0).click();
			}
			$("#order-phone-text").text(userData.phone);
			$("#account-email").text(userData.email);
			$("#account-phone").text(userData.phone);
		}
	});
}

function authCheck() {
	$.ajax({
		type : 'POST',
		url : './auth/check',
		dataType : 'json',
		async : true,
		success : function(data) {
			var constant = new Constant();
			if (data.constant == constant.SESSION_VALID) {
				var accountButton = $("#account-button");
				accountButton.removeClass("logout-state");
				accountButton.addClass("login-state");
				getRecommendationURL();
				setUserInfo();
				setOrderInfo();
				setCoupon();
			}
		},
		error : function(request, status, error) {
			if (request.responseText.indexOf("Access is denied") != -1) {
				if (logoutFlag == false) {
					logoutFlag = true;
					logoutProcess(false);
				}
			}
		}
	});
}

function headerResize() {
	var windowHeight = $(window).height();
	var navHeight = $(".navbar").height();
	$("#header").css("height", windowHeight);
	$("#intro").css("height", windowHeight + navHeight * 2);
	$("#qna").css("height", windowHeight + navHeight * 4);
}

function dateCalc(date, time) {
	return date.getFullYear() + "-" + leadingZeros((date.getMonth() + 1), 2) + "-" + leadingZeros(date.getDate(), 2) + " " + time.selectpicker('val') + ":00:00";
}

function leadingZeros(n, digits) {
	var zero = '';
	n = n.toString();

	if (n.length < digits) {
		for (var i = 0; i < digits - n.length; i++)
			zero += '0';
	}
	return zero + n;
}

var Constant = function() {
	this.SESSION_EXPIRED = 0;
	this.SUCCESS = 1;
	this.ERROR = 2;
	this.EMAIL_ERROR = 3;
	this.PASSWORD_ERROR = 4;
	this.ACCOUNT_VALID = 5;
	this.ACCOUNT_INVALID = 6;
	this.ACCOUNT_ENABLED = 8;
	this.ACCOUNT_DISABLED = 9;
	this.ROLE_ADMIN = 10;
	this.ROLE_DELIVERER = 11;
	this.ROLE_MEMBER = 12;
	this.ROLE_INVALID = 13;
	this.IMAGE_WRITE_ERROR = 14;
	this.IMPOSSIBLE = 15;
	this.ACCOUNT_DUPLICATION = 16;
	this.SESSION_VALID = 17;
	this.PUSH_ASSIGN_PICKUP = 100;
	this.PUSH_ASSIGN_DROPOFF = 101;
	this.PUSH_SOON_PICKUP = 102
	this.PUSH_SOON_DROPOFF = 103;
	this.PUSH_ORDER_ADD = 200;
	this.PUSH_ORDER_CANCEL = 201;
	this.PUSH_PICKUP_COMPLETE = 202;
	this.PUSH_DROPOFF_COMPLETE = 203;
	this.PUSH_MEMBER_JOIN = 204;
	this.PUSH_DELIVERER_JOIN = 205;
	this.PUSH_CHANGE_ACCOUNT_ENABLED = 206;
	this.DUPLICATION = 207;
	this.INVALID = 208;
}
