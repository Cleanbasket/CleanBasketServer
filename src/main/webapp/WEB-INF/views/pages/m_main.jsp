<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="./partials/m_head.jspf"%>
  <link rel="stylesheet" href="./resources/styles/m_main.css">
  <title></title>
</head>
<body>
  <div id="root">
  	<%@ include file="./partials/m_header.jspf"%>
    <div class="content">
      <div class="hero">
        <div class="container">
          <div class="hero-typo">
            <div class="hero-bar"></div>
            <p class="hero-text">
              여유로운<br>생활의 시작<br><strong>크린바스켓</strong>
            </p>
            <button class="btn btn-border hero-btn appdown-trigger">주문하기</button>
          </div>
        </div>
        <div class="popup">
          <button class="popup-close">
            <span class="icon icon-popupclose"></span>
          </button>
          <img class="popup-img" src="./resources/images/icon_app.png" alt="">
          <p class="popup-text">
            크린바스켓 앱을 다운로드 하세요.<br>
            다양한 이벤트와 혜택이 고객님을 기다립니다.
          </p>
          <div class="popup-appdown appdown-trigger">
            <img src="./resources/images/icon_download.png" alt="">
          </div>
        </div>
      </div>
      <div class="process">
        <div class="container">
          <h2 class="process-title">
            <mark>터치.터치.끝!</mark>
            <br>
            이렇게 진행됩니다.
          </h2>
          <ul class="process-list">
            <li class="process-item clearfix">
              <div class="left">
                <img src="./resources/images/icon_phone_mobile.png" alt="">
              </div>
              <div class="right">
                <h3>주문</h3>
                <p>
                  편하신 방법으로  세탁물 수거 장소와 시간을 설정해 주세요. (앱/전화)
                </p>
              </div>
            </li>
            <li class="process-item clearfix">
              <div class="left">
                <img src="./resources/images/icon_wash_mobile.png" alt="">
              </div>
              <div class="right">
                <h3>세탁</h3>
                <p>
                  세탁물 수거 후, 세탁 장인분들의 손길을 통해 깨끗히 세탁 해 드립니다. 
                </p>
              </div>
            </li>
            <li class="process-item clearfix">
              <div class="left">
                <img src="./resources/images/icon_delivery_mobile.png" alt="">
              </div>
              <div class="right">
                <h3>배달</h3>
                <p>
                  원하시는 시간에 깨끗해진 세탁물을 고객님의 문 앞까지 배달 해 드립니다. 
                </p>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <div class="price">
        <div class="container">
          <div class="price-typo">
            <p class="price-text">
              셔츠 2천원<br>
              정장한벌 6천원<br>
              합리적인 가격
            </p>
            <p class="price-subtext">
              격이 다른 품질의 크린바스켓 서비스<br>
              이제 모바일에서도 경험 해 보세요!
            </p>
            <button class="price-btn btn">요금안내</button>
          </div>            
        </div>
      </div>
      <div class="appdown">
        <p class="appdown-text">
          더 많은 혜택을 드리는<br>
          크린바스켓 앱을 만나보세요
        </p>
        <button class="btn btn-border appdown-btn appdown-trigger">APP DOWNLOAD</button>
      </div>
      <%@ include file="./partials/m_footer.jspf"%>
    </div>
    
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
  <script src="./resources/scripts/mobile-detect.min.js"></script>
  <script src="./resources/scripts/swiper.jquery.min.js"></script>
  <script src="./resources/scripts/m_header.js"></script>
  <script src="./resources/scripts/m_appdown.js"></script>
  <script src="./resources/scripts/m_main.js"></script>
</body>
</html> 