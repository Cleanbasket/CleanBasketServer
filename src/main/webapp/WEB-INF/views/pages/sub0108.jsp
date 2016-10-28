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
            <h2 class="section-title">가을이 다가온다, 가을/겨울옷 세탁법</h2>
            <span class="section-meta">2016.10.28</span>
          </header>
          <div class="lab-images">
            <div class="swiper-container">
              <!-- Additional required wrapper -->
              <div class="swiper-wrapper">
                  <!-- Slides -->
                  <div class="swiper-slide">
                    <img src="./resources/images/img_8/img_08_1.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_8/img_08_2.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_8/img_08_3.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_8/img_08_4.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_8/img_08_5.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_8/img_08_6.png" alt="">
                  </div>
                  <div class="swiper-slide">
                    <img src="./resources/images/img_8/img_08_7.png" alt="">
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
              옷장에 넣어둔 가을/겨울옷 꺼낼 시기! <br>
              깔끔하게 세탁 후 입으면 옷 맵시가 더욱 살겠죠? <br>
              그래서 크린바스켓이 가을/겨울옷 재질별 셀프 세탁법을 알려드립니다! <br>
              <br>
              잘못된 세탁 시 쉽게 망가지는 가을/겨울 옷, <br>
              꼼꼼한 세탁을 원하신다면 크린바스켓에 맡겨주세요! <br>
            </p>
          </div>
          <div class="lab-tag">
            <span class="tag"># 겨울옷세탁</span>
            <span class="tag"># 가을옷세탁</span>
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