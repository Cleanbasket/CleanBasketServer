<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  
  <title></title>
  <link rel="stylesheet" href="./resources/styles/common.css">
  <link rel="stylesheet" href="./resources/styles/sub04.css">
</head>
<body>
  <div id="root">
    <%@ include file="./partials/header.jspf"%>
    <div class="titlebar">
      <div class="container">
        <h1>크린파트너 모집</h1>
        <p>크린바스켓의 얼굴이 되어 함께 꿈을 키워나갈 파트너를 모십니다.</p>  
      </div>
    </div>
    <div class="content">
      <div class="message">
        <div class="container">
          <h2 class="message-subject">일상을 배달하는 <mark>크린파트너</mark></h2>
          <p class="message-text">
            크린파트너란 크린바스켓의 얼굴이자<br>
            배트맨에게 있어 로빈, 제임스 본드에게 있어 본드걸 같은 존재입니다.<br>
            그저 더러워진 빨래를 세탁하고, 고객에게 배달하는 크린파트너가 아닌,<br>
            고객의 일상까지 책임질 크린바스켓의 얼굴, 크린파트너!<br>
            <br>
            고객님들께 깨끗함을 전달하는 크린파트너!<br>
            크린바스켓과 함께 성장하는 크린파트너!<br>
          </p>
        </div>
      </div>
      <div class="duty">
        <div class="container">
          <div class="keystone">
            <p>
              주요<br>
              업무
            </p>
          </div>
          <div class="duty-detail">
            <p class="duty-subject">
              세탁물 수거 및 배달.<br>
              그리고 친절한 서비스
            </p>
            <p class="duty-text">
              표면적으로는 단순해 보이는 일이지만,<br>
              크린파트너에겐 늘 고객을 생각하는 마음이 있습니다.<br>
              깨끗해진 옷을 배달하는 크린파트너에게 크린바스켓의 서비스란<br>
              평범한 세탁 수거 서비스가 아닌<br>
              고객에게 감동과 여유를 전달하는 최고의 서비스입니다.
            </p>
          </div>
        </div>
      </div>
      <div class="environment">
        <div class="container">
          <div class="keystone">
            <p>
              근무<br>
              환경
            </p>
          </div>
          <div class="environment-wrapper">
            <ul class="row environment-detail">
              <li class="col-1-of-3 environment-item">
                <img src="./resources/images/car.png" alt="">
                <h3>배달차량 지원</h3>
                <p>
                  크린파트너가 서비스 지역을<br>
                  보다 효율적으로 돌아다니기 위한<br>
                  차량을 지원합니다.
                </p>
              </li>
              <li class="col-1-of-3 environment-item">
                <img src="./resources/images/washer.png" alt="">
                <h3>세탁비 무상지원</h3>
                <p>
                  크린파트너의 유니폼은 물론,<br>
                  개인 세탁물 역시<br>
                  크린바스켓에서 책임지겠습니다.
                </p>
              </li>
              <li class="col-1-of-3 environment-item">
                <img src="./resources/images/sandwitch.png" alt="">
                <h3>간식 및 음료제공</h3>
                <p>
                  배달에 지친 크린파트너에게<br>
                  꿀맛같은 휴식과 함께<br>
                  간식과 음료를 제공합니다.
                </p>
              </li>
            </ul>
            
          </div>
        </div>
      </div>
      <div class="organization">
        <div class="container">
          <div class="keystone">
            <p>
              직급<br>
              체계
            </p>
          </div>
          <img class='organization-diagram' src="./resources/images/organization_diagram.png" alt="">
        </div>
      </div>
      <div class="qualifi">
        <div class="keystone">
          <p>
            자격<br>
            요건
          </p>
        </div>
        <div class="qualifi-detail container">
          <ul class="qualifi-list">
            <li class="qualifi-item">
              <div class="num">
                <span>1</span>
              </div><!-- inline-block fix
              --><div class="text">
                <span>서비스 마인드가 투철해야 합니다.</span>
              </div>
            </li>
            <li class="qualifi-item">
              <div class="num">
                <span>2</span>
              </div><!-- inline-block fix
              --><div class="text">
                <span>함께하고 함께 성장해 나갈 협동정신이 있어야 합니다.</span>
              </div>
            </li>
            <li class="qualifi-item">
              <div class="num">
                <span>3</span>
              </div><!-- inline-block fix
              --><div class="text">
                <span>운전면허를 소지하고 있어야 합니다.(경력자를 우대합니다)</span>
              </div>
            </li>
          </ul>
          <p>위 자격요건에 적합하신 분들께서는 크린파트너에 도전해보세요!</p>
          <a class="alert-apply" href="#"><button class="btn btn-border">지원하기</button></a>
        </div>
      </div>
    </div>
    <%@ include file="./partials/footer.jspf"%>
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
</body>
  <script src="./resources/scripts/sub04.js"></script>
</html>