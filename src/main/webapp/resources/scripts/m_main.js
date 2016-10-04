$(document).ready(function () {
  init();
})

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