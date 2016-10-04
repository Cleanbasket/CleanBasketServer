function m_header_init() {
  var $header = $('.header');
  var $headerNav = $header.find('.header-nav');
  $header.find('.header-hamburger').click(function (e) {
    $headerNav.toggleClass('closed');
  })

}