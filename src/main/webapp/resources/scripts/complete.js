$( document ).ready(function(){
    m_header_init();
    m_appdown_init();
   	var session = window.sessionStorage;
   	if(session.getItem('cart-db')){
       session.removeItem('cart-db');
   	}
});

