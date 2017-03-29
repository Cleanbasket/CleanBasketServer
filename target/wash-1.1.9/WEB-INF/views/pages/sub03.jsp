<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <link rel="stylesheet" href="./resources/styles/common.css">
  <link rel="stylesheet" href="./resources/styles/sub03.css">
</head>
<body>
  <div id="root">
    <%@ include file="./partials/header.jspf"%>
    <div class="titlebar">
      <div class="container">
        <h1>크린바스켓 소개</h1>
        <p>크린바스켓의 철학이 담긴 서비스를 소개합니다.</p>  
      </div>
    </div>
    <div class="content">
      <div class="about">
        <div class="about-titlebar">
          <h2 class="about-title">
            모두의 행복을 담는<br><mark>크린바스켓</mark>
          </h2>
          <p class="about-subtitle">크린바스켓 빨래바구니에는 그저 빨래만 담겨있지 않습니다.<br>고객님의 여유, 크린파트너의 꿈, 우리 모두의 행복도 같이 담겨있습니다.</p>
        </div>
        <div class="about-detail container">
          <ul class="row">
            <li class="col-1-of-3">
              <div class="about-bar"></div>
              <span class="about-subject">당신을 위한</span>
              <p class="about-text">
                야근을 하고 늦게 집에 돌아왔을 때,<br>
                주말에 늦잠 자고 싶을 때,<br>
                잠시 여행을 다녀오고 싶을 때,<br>
                크린바스켓은 오전 10시부터 밤12시까지<br>
                당신이 원하는 시간과 장소에서<br>
                당신의 소중한 세탁을 위해 기다리겠습니다.
              </p>
            </li>
            <li class="col-1-of-3">
              <div class="about-bar"></div>
              <span class="about-subject">당신에 의한</span>
              <p class="about-text">
                크린바스켓을 구성하는 크린파트너는
                각자의 목표를 가지고 있습니다.<br>
                누군가는 배우를, 누군가는 작곡가를,<br>
                또 누군가는 창업자의 꿈을 꿉니다.<br>
                열정가득한 사람들이 만드는 크린바스켓.<br>
                우리는 당신의 꿈을 응원합니다.</p>
            </li>
            <li class="col-1-of-3">
              <div class="about-bar"></div>
              <span class="about-subject">당신의</span>
              <p class="about-text">
                여유가 생기면 무엇을 하시겠어요?<br>
                읽지 못했던 책 읽기?<br>
                아이들과 함께 시간 보내기?<br>
                세탁물은 잠시 내려 놓으세요.<br>
                소소한 일상의 여유를 가져보세요</p>
            </li>
          </ul>
        </div>        
        <div class="about-apply">
          <p>
            함께 꿈을<br>
            키워나갈<br>
            크린파트너를<br>
            모집합니다.
          </p>
          <a class='alert-apply' href="#"><span class='icon icon-go'></span>지원하기</a>
        </div> 
      </div>
      <div class="business">
        <div class="business-titlebar">
          <div class="keystone"><p>비즈니스<br>소개</p></div>
          <h2 class="business-title">"너무 바쁜 당신께 휴식을 선물합니다"</h2>
        </div>
        <div class="business-detail">
          <div class="container">
            <div class="clearfix business-detail-item">
              <p class="left">
                크린바스켓 서비스는<br>
                편리한 온라인세탁 서비스입니다.
              </p>
              <p class="right">
                크린바스켓 어플리케이션을 통해 서비스 지역 내  어디서든지 세탁을 의뢰하실 수 있습니다.<br>
  오전 10시부터 밤 12시 사이라면 언제든지 고객님이 요청하신 장소로 달려갑니다.<br>
  크린맨이 요청하신 장소와 시간에 맞춰 고객님의 소중한 세탁물을 하나씩 살펴보고 수거합니다.<br>
  30년 이상의 세탁 장인들의 손길이 닿은 세탁이 뽀송뽀송하게 완료되면, 요청하신 장소로 시간에 맞게
  배달을 해 드립니다.
              </p>
            </div>
            <div class="clearfix business-detail-item">
              <p class="left">
                크린바스켓은<br>
                기업 고객분들의 요청에도 응합니다.
              </p>
              <p class="right">
                이번 봄 운동회 때 입었던 회사 유니폼.  세탁을  어디에 연락을 해야 할지 모르시겠다면,<br>
  호텔에서 투숙객이 피부로 느끼는 이불, 매트리스 커버에 뽀송뽀송한 세탁이 필요하다면,<br>
  음식물로 더럽혀진 레스토랑의 테이블보, 쿠션커버,무릎 담요 등의 오염이 신경쓰이신다면,<br>
  크린바스켓이 여러분의 세탁을 돕겠습니다.
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="company">
        <div class="keystone"><p>기업제휴<br>서비스</p></div>
        <p class="company-up">크린바스켓은 정기적으로 많은 양의 세탁을 필요로 하시는 <br>기업체나 사업자를 위해 B2B대량 세탁을 도와드리고 있습니다.</p>
        <img class='company-diagram' src="./resources/images/b2b_diagram.png">
        <p class="company-down">B2B 세탁 서비스는 위의 안내드린 세탁물 이외에도 다양하게 제공 가능합니다.<br>B2B 세탁에 관한 문의는 1833-8543으로 연락 주시거나 홈페이지를 통해 문의사항을 남겨주시면 연락드리겠습니다.</p>
        <a class="btn btn-border" href="../sub02ask">문의하기</a>
      </div>
    </div>
    <%@ include file="./partials/footer.jspf"%>
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
  <script src="./resources/scripts/sub03.js"></script>
</body>
</html>