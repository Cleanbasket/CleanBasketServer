<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <link rel="stylesheet" href="./resources/styles/common.css">
  <link rel="stylesheet" href="./resources/styles/sub01-1.css">
  <link rel="stylesheet" href="./resources/styles/swiper.min.css">
</head>
<body>
  <div id="fb-root"></div>
  <script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/ko_KR/sdk.js#xfbml=1&version=v2.7";
    fjs.parentNode.insertBefore(js, fjs);
  }(document, 'script', 'facebook-jssdk'));</script>
  <div id="root">
    <%@ include file="./partials/header.jspf"%>
    <div class="titlebar titlebar-tabs">
      <h1>크바연구소</h1>
      <p>오로지 세탁만을 연구하는 크린바스켓과 세탁 장인들이 알려주는 세탁 노하우!</p>
      <div class="tabs">
        <a href="../sub01" class="btn btn-filled">세탁학개론</a>
      </div>
    </div>
    <div class="content">
      <div class="container">
        <section class="section lab">
          <header class="section-header">
            <h2 class="section-title">통돌이 vs 드럼 세탁기, 뭘 선택하지?</h2>
            <span class="section-meta">2016.10.28</span>
          </header>
          <div class="lab-images">
            <div class="swiper-container">
              <!-- Additional required wrapper -->
              <div class="swiper-wrapper">
                  <!-- Slides -->
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_1.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_2.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_3.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_4.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_5.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_6.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_7.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_7/img_07_8.png" alt="">
                  </div>
              </div>
              <!-- If we need pagination -->
              
              <!-- If we need navigation buttons -->
              <div class="swiper-button-prev"></div>
              <div class="swiper-button-next"></div>
              <!-- <div class="swiper-pagination"></div> -->
            </div>
            <div class="swiper-pagination"></div>
          </div>
          <div class="lab-text">
            <p>
              세탁기 사려고하는데, 클래식한 통돌이? 스타일리시한 드럼? <br>
              크린바스켓이 알려드리는 두 가지의 차이점을 확인하시고 상황에 맞는 세탁기를 선택하세요! <br>
            </p>
          </div>
          <div class="lab-tag">
            <span class="tag"># 세탁기선택</span>
            <span class="tag"># 통돌이</span>
            <span class="tag"># 드럼</span>
            <span class="tag"># 세탁</span>
            <span class="tag"># 빨래</span>
          </div>
          <hr class="lab-hr">
          <div class="lab-btns clearfix">
            <a href="../sub01" class="lab-listbtn btn btn-filled">리스트</a>
          </div>
        </section>
      </div>
    </div>
    <%@ include file="./partials/footer.jspf"%>
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
  <script src="./resources/scripts/swiper.jquery.min.js"></script>
  <script src="./resources/scripts/sub01-1.js"></script>
</body>
</html>