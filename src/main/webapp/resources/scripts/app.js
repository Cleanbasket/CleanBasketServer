$(window).ready(function() {
	app.init();
});

var app = {
	init: function() {
		m_header_init();
		m_appdown_init();
		storage.init();
		view.init();
	},

	changeCategory: function(categoryId) {
		view.currentCategory = categoryId;
		// view.drawItem(categoryId);
	},

	increaseCartItem: function(itemId, category) {
		storage.setCartItem(itemId);
		storage.increaseCartItem(itemId);
		storage.updateTotals();
		view.drawItem(category);
		view.drawCartInfo();
	},

	decreaseCartItem: function(itemId, category) {
		storage.setCartItem(itemId);
		storage.decreaseCartItem(itemId);
		storage.updateTotals();
		view.drawItem(category);
		view.drawCartInfo();
	}

	// // swipe paging
	// swiperFunc: function(){
	//     // $('ul.tabs').tabs('select_tab', 'bottom');
	//     var swiper = new Swiper('.swiper-container', {
	//         slidesPerView: 1,
	//         paginationClickable: true,
	//         spaceBetween: 30,
	//         loop: true
	//     });
	// }

	
};
