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
            <h2 class="section-title">크린바스켓 세탁서비스 _ 다림질편</h2>
            <span class="section-meta">2016.10.28</span>
          </header>
          <div class="lab-video">
            <iframe width="1180" height="664" src="https://www.youtube.com/embed/0zs3FLrXXcs" frameborder="0" allowfullscreen></iframe>
          </div>
          <div class="lab-text">
            <p>
              크린바스켓은 30년 이상의 경력을 가진 베테랑 세탁소를 엄선하여 세탁을 진행합니다. <br>
              세탁 후 다림질로 반듯하게 다려 댁으로 배달해드립니다~ <br>
              <br>
              의류 하나하나 정성스럽게 다림질을 진행하며 혹시 얼룩이 남아있진 않은지 꼼꼼하게 확인해주신답니다! <br>
              전문가의 손길이 느껴지는 다림질 과정을 확인해보세요! <br>
            </p>
          </div>
          <div class="lab-tag">
            <span class="tag"># 크린바스켓</span>
            <span class="tag"># 서비스소개</span>
            <span class="tag"># 다림질</span>
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