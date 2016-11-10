$(document).ready(function () {
  init();
});

$(document).on('click','.header-alert',function(){
  alert("서비스 준비중입니다. 빠른 시일내로 찾아뵙겠습니다.");
});

function init() {
  m_header_init();
  m_appdown_init();
  popup_init();
  priceModal();
}

function popup_init() {
  var $popup = $(".popup")
  $popup.find(".popup-close").click(function (e) {
    $popup.addClass('close');
  });
}

function priceModal(){
	var priceModal = document.getElementById('price-modal'),
		openBtn = document.getElementsByClassName("price-btn")[0],
		closeBtn = document.getElementsByClassName("modal-close")[0];

	openBtn.onclick = function() {
	    priceModal.style.display = "block";
	}

	closeBtn.onclick = function() {
	    priceModal.style.display = "none";
	}

	window.onclick = function(event) {
	    if (event.target == priceModal) {
	        priceModal.style.display = "none";
	    }
	}
}