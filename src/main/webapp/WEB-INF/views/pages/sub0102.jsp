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
            <h2 class="section-title">크린바스켓 세탁서비스 _ 물세탁편</h2>
            <span class="section-meta">2016.10.28</span>
          </header>
          <iframe width="560" height="315" src="https://www.youtube.com/embed/BJQZw3MfTl4" frameborder="0" allowfullscreen></iframe>
          <div class="lab-text">
            <p>
              크린바스켓은 30년 이상의 경력을 가진 베테랑 세탁소를 엄선하여 세탁을 진행합니다. <br>
              패딩 등 물빨래를 필요로 하는 제품을 위한 전문적 물세탁 과정을 소개해드립니다. <br>
              물세탁이라고 모두 다 같은 물세탁이 아니라는 것! <br>
            </p>
          </div>
          <div class="lab-tag">
            <span class="tag"># 크린바스켓</span>
            <span class="tag"># 서비스소개</span>
            <span class="tag"># 물세탁</span>
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