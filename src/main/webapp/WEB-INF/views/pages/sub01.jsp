<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <link rel="stylesheet" href="./resources/styles/common.css">
  <link rel="stylesheet" href="./resources/styles/sub01.css">
</head>
<body>
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
        <div class="post">
          <div class="post-header">
            <h2 class="post-title">세탁학개론</h2>
            <div class="vertical-divider"></div>
            <!-- <a class="tag active" href=""># 이불빨래</a>
            <a class="tag" href=""># 황사</a>
            <a class="tag" href=""># 미세먼지</a>
            <a class="tag" href=""># 여름</a>
            <a class="tag" href=""># 세탁장인</a>
            <a class="tag" href=""># 계절빨래</a>
            <a class="tag" href=""># 생활꿀팁</a>
            <a class="tag" href=""># 세탁앱</a>
            <a class="tag" href=""># 드라이클리닝</a> -->
            <ul class="post-list row">
              <li class="post-item col-1-of-3">
                <div class="card">
                  <a href="../sub0101">
                    <div class="card-image">
                      <img src="https://placehold.it/410x200" alt="">
                    </div>
                  </a>
                  <div class="card-meta"><span class="card-date">2016.04.14</span></div>
                  <h3 class="card-title">
                    <a href="../sub0101">
                    test
                    </a>
                  </h3>
                </div>
              </li>
<!--          <li class="post-item col-1-of-3">
                <div class="card">
                  <div class="card-image"><img src="https://placehold.it/410x200" alt=""></div>
                  <div class="card-meta"><span class="card-date">2016.04.14</span></div>
                  <h3 class="card-title">여름철 빨래 방법</h3>
                </div>
              </li> -->
            </ul>
            <div class="post-footer">
              <button class="post-more btn btn-filled">더보기</button>  
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="./partials/footer.jspf"%>
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
  <script src="./resources/scripts/sub01.js"></script>
</body>
</html>