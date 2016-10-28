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
            <h2 class="section-title">test</h2>
            <span class="section-meta">2016.04.14</span>
          </header>
          <div class="lab-sns clearfix">
            <div class="lab-snsleft">
              <div class="fb-like" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-action="like" data-size="small" data-show-faces="true" data-share="false"></div>
            </div>
            <div class="lab-snsright">
              <span>Share</span>
              <a href=""><span class="icon icon-facebook-grey"></span></a>
              <a href=""><span class="icon icon-twitter-grey"></span></a>
            </div>
            
          </div>
          <div class="lab-images">
            <div class="swiper-container">
              <!-- Additional required wrapper -->
              <div class="swiper-wrapper">
                  <!-- Slides -->
                  <div class="swiper-slide slide-one">
                    <img src="http://placehold.it/686x690" alt="">
                  </div>
                  <div class="swiper-slide slide-two">
                    <img src="http://placehold.it/686x690" alt="">
                  </div>
                  <div class="swiper-slide slide-two">
                    <img src="http://placehold.it/400x300" alt="">
                  </div>
                  <div class="swiper-slide slide-one">
                    <img src="http://placehold.it/686x690" alt="">
                  </div>
                  <div class="swiper-slide slide-two">
                    <img src="http://placehold.it/686x690" alt="">
                  </div>
                  <div class="swiper-slide slide-two">
                    <img src="http://placehold.it/400x300" alt="">
                  </div>
                  <div class="swiper-slide slide-two">
                    <img src="http://placehold.it/400x300" alt="">
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
              난감한 겨드랑이 땀 얼룩부터 케첩, 기름, 커피 자국까지! <br>
              정독하시고 응급처치책으로 꼭 알아두세요! <br>
              <br>
              사실 얼룩은 묻은 즉시 응급처치를 해주는 것이 좋기 때문에 이렇게 하셔도 안지워질 수가 있어요. <br>
              처치곤란한 얼룩이 남아있는 세탁물이 있다면? <br>
              <br>
              크린바스켓에 꼭 맡겨주세요! <br>
            </p>
          </div>
          <div class="lab-tag">
            <span class="tag"># 얼룩지우기</span>
            <span class="tag"># 생활꿀팁</span>
            <span class="tag"># 세탁</span>
            <span class="tag"># 빨</span>
          </div>
          <hr class="lab-hr">
          <div class="lab-btns clearfix">
            <button class="lab-prevbtn btn btn-default"><span class="icon icon-prev-small"></span>이전</button>
            <button class="lab-nextbtn btn btn-default">다음<span class="icon icon-next-small"></span></button>
            <a class="lab-listbtn btn btn-filled">리스트</a>
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