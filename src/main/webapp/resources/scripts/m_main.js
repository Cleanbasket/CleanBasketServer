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
}

function popup_init() {
  var $popup = $(".popup")
  $popup.find(".popup-close").click(function (e) {
    $popup.addClass('close');
  });
}