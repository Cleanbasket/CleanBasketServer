function m_header_init() {
  var $header = $('.header');
  var $headerNav = $header.find('.header-nav');
  $header.find('.header-hamburger').click(function (e) {
    $headerNav.toggleClass('closed');
  });

	var gotoOrderBtn = $('.goto-order-btn');
	gotoOrderBtn.click(function(){
		var session = window.sessionStorage;
		if(session.getItem('cart-db')){
		    session.removeItem('cart-db');
		}
		window.location.href = '../m_order';
	});
}