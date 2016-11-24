<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
	<head>
        <%@ include file="./partials/m_head.jspf"%>

        <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="./resources/styles/materialize.min.css"  media="screen,projection"/>
        <link rel="stylesheet" href="./resources/styles/m_headerfooter.css">
    	<link rel="stylesheet" href="./resources/styles/m_order.css">
        <title></title>
	</head>

	<body>
        <%@ include file="./partials/m_header.jspf"%>
        <div class="row main">            
            <!-- 품목 탭 메뉴 -->
            <div class="tab-menu">
              <ul class="tabs">
                <li class="tab" data-category="1">
                    <a href="#laundry">
                        <img class="tab-img" src="./resources/images/ic_laundry.png" alt="laundry">
                        <img class="tab-img-on" src="./resources/images/ic_laundry_on.png" alt="laundry_clicked">
                        <div>생활빨래</div>
                    </a>
                </li>
                <li class="tab" data-category="2">
                    <a href="#business">
                        <img class="tab-img" src="./resources/images/ic_business.png" alt="business">
                        <img class="tab-img-on" src="./resources/images/ic_business_on.png" alt="business_clicked">
                        <div>비즈니스</div>
                    </a>
                </li>
                <li class="tab" data-category="3">
                    <a href="#top">
                        <img class="tab-img" src="./resources/images/ic_top.png" alt="top">
                        <img class="tab-img-on" src="./resources/images/ic_top_on.png" alt="top_clicked">
                        <div>상의</div>
                    </a>
                </li>
                <li class="tab" data-category="4">
                    <a href="#bottom">
                        <img class="tab-img" src="./resources/images/ic_bottom.png" alt="bottom">
                        <img class="tab-img-on" src="./resources/images/ic_bottom_on.png" alt="bottom_clicked">
                        <div>하의</div>
                    </a>
                </li>
                <li class="tab" data-category="5">
                    <a href="#outer">
                        <img class="tab-img" src="./resources/images/ic_outer.png" alt="outer">
                        <img class="tab-img-on" src="./resources/images/ic_outer_on.png" alt="outer_clicked">
                        <div>아우터</div>
                    </a>
                </li>
                <li class="tab" data-category="6">
                    <a href="#etc">
                        <img class="tab-img" src="./resources/images/ic_etc.png" alt="etc">
                        <img class="tab-img-on" src="./resources/images/ic_etc_on.png" alt="etc_clicked">
                        <div>기타</div>
                    </a>
                </li>
                <li class="tab" data-category="7">
                    <a href="#bedclothes">
                        <img class="tab-img" src="./resources/images/ic_bedclothes.png" alt="bedclothes">
                        <img class="tab-img-on" src="./resources/images/ic_bedclothes_on.png" alt="bedclothes_clicked">
                        <div>이불</div>
                    </a>
                </li>
                <li class="tab" data-category="8">
                    <a href="#footwear">
                        <img class="tab-img" src="./resources/images/ic_footwear.png" alt="footwear">
                        <img class="tab-img-on" src="./resources/images/ic_footwear_on.png" alt="footwear_clicked">
                        <div>신발</div>
                    </a>
                </li>
                <li class="tab" data-category="9">
                    <a href="#bag">
                        <img class="tab-img" src="./resources/images/ic_bag.png" alt="bag">
                        <img class="tab-img-on" src="./resources/images/ic_bag_on.png" alt="bag_clicked">
                        <div>가방</div>
                    </a>
                </li>
                <li class="tab" data-category="10">
                    <a href="#add">
                        <img class="tab-img" src="./resources/images/ic_add.png" alt="add">
                        <img class="tab-img-on" src="./resources/images/ic_add_on.png" alt="add_clicked">
                        <div>추가비용</div>
                    </a>
                </li>
              </ul>
            </div>

            <!-- 선택된 품목별 컨텐츠 -->
            <div class="contents-container swiper-container">
                <div class="swiper-wrapper">
                    <div id="laundry" class="contents swiper-slide">
                        <img class="laundry-img" src="./resources/images/tab_1.png" alt="laundry">
                        <div class="item-box-container"></div>
                    </div>
                    <div id="business" class="contents swiper-slide">
                        <img src="./resources/images/tap_2.png" alt="business">
                        <ul class="item-box-container row"></ul>
                    </div>
                    <div id="top" class="contents swiper-slide">
                        <img src="./resources/images/tap_3.png" alt="top">
                        <ul class="item-box-container row"></ul>
                    </div>
                    <div id="bottom" class="contents swiper-slide">
                        <img src="./resources/images/tap_4.png" alt="bottom">
                        <ul class="item-box-container row"></ul>
                    </div>
                    <div id="outer" class="contents swiper-slide">
                        <img src="./resources/images/tap_5.png" alt="outer">
                        <ul class="item-box-container row"></ul>
                    </div>
                    <div id="etc" class="contents swiper-slide">
                        <img src="./resources/images/tap_6.png" alt="etc">
                        <ul class="item-box-container row"></ul>
                    </div>
                    <div id="bedclothes" class="contents swiper-slide">
                        <img src="./resources/images/tap_7.png" alt="bedclothes">
                        <ul class="item-box-container row"></ul>
                    </div>
                    <div id="footwear" class="contents swiper-slide swiper-slide">
                        <img src="./resources/images/tap_8.png" alt="footwear">
                        <ul class="item-box-container row"></ul>
                    </div>  
                    <div id="bag" class="contents swiper-slide">
                        <img src="./resources/images/tap_9.png" alt="bag">
                        <ul class="item-box-container row"></ul>
                    </div>  
                    <div id="add" class="contents swiper-slide">
                        <img src="./resources/images/tap_10.png" alt="add">
                        <ul class="item-box-container row"></ul>
                    </div>  
                </div>
            </div>
        </div>

        <!-- 주문 정보 -->
        <div class="cart-info row">
                <div class="cart col">
                    <div class="col s12">
                        <div class="left">품목</div>
                        <div class="right total-number"></div>
                    </div>
                    <div class="col s12">
                        <div class="left">합계</div>
                        <div class="right total-price"></div>
                    </div>
                </div>
                <button class="cart-btn btn waves-effect waves-light col" type="submit">주문하기</button>
        </div>
        <%@ include file="./partials/m_footer.jspf"%>
        
        <!-- <script src="./resources/scripts/jquery.min.js"></script> -->
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="./resources/scripts/mobile-detect.min.js"></script>
        <script src="./resources/scripts/m_header.js"></script>
        <script src="./resources/scripts/m_appdown.js"></script>
    	<script src="./resources/scripts/materialize.min.js"></script>
        <script src="./resources/scripts/app.js"></script>
        <script src="./resources/scripts/storage.js"></script>
        <script src="./resources/scripts/view.js"></script>
	</body>
</html>