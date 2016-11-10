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

      <div id="price-modal">
        <div class="modal-content">
          <span class="modal-close">×</span>
          <h2>요금 안내</h2>
          
          <table>
            <thead>
              <tr>
                  <th colspan="2">생활빨래</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>생활 빨래</td>
                <td>₩10,000 / 수거가방</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">비즈니스</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>와이셔츠</td>
                <td>₩2,000</td>
              </tr>
              <tr>
                <td>정장(한벌)</td>
                <td>₩6,000</td>
              </tr>
              <tr>
                <td>정장 상의</td>
                <td>₩3,500</td>
              </tr>
              <tr>
                <td>정장 하의</td>
                <td>₩3,500</td>
              </tr>
              <tr>
                <td>여성 정장 상의</td>
                <td>₩3,500</td>
              </tr>
              <tr>
                <td>여성 정장 하의</td>
                <td>₩3,500</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">상의</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>블라우스</td>
                <td>₩4,000</td>
              </tr>
              <tr>
                <td>롱스웨터</td>
                <td>₩5,000</td>
              </tr>
              <tr>
                <td>스웨터</td>
                <td>₩4,000</td>
              </tr>
              <tr>
                <td>가디건</td>
                <td>₩4,000</td>
              </tr>
              <tr>
                <td>티셔츠</td>
                <td>₩3,000</td>
              </tr>
              <tr>
                <td>조끼</td>
                <td>₩4,000</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">하의</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>바지</td>
                <td>₩3,000</td>
              </tr>
              <tr>
                <td>스커트</td>
                <td>₩3,000</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">아우터</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>반코트</td>
                <td>₩8,000</td>
              </tr>
              <tr>
                <td>코트 일반</td>
                <td>₩10,000</td>
              </tr>
              <tr>
                <td>트렌치코트</td>
                <td>₩12,000</td>
              </tr>
              <tr>
                <td>점퍼</td>
                <td>₩7,000</td>
              </tr>
              <tr>
                <td>자켓</td>
                <td>₩4,000</td>
              </tr>
              <tr>
                <td>가죽점퍼</td>
                <td>₩40,000</td>
              </tr>
              <tr>
                <td>가죽점퍼(Long)</td>
                <td>₩50,000</td>
              </tr>
              <tr>
                <td>패딩/아웃도어</td>
                <td>₩12,000</td>
              </tr>
              <tr>
                <td>인조무스탕</td>
                <td>₩15,000</td>
              </tr>
              <tr>
                <td>골프웨어</td>
                <td>₩10,000</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">기타</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>원피스</td>
                <td>₩7,000</td>
              </tr>
              <tr>
                <td>현장확인</td>
                <td>₩0</td>
              </tr>
              <tr>
                <td>인형</td>
                <td>₩10,000</td>
              </tr>
              <tr>
                <td>교복</td>
                <td>₩5,000</td>
              </tr>
              <tr>
                <td>한복</td>
                <td>₩15,000</td>
              </tr>
              <tr>
                <td>넥타이</td>
                <td>₩1,000</td>
              </tr>
              <tr>
                <td>스카프</td>
                <td>₩2,000</td>
              </tr>
              <tr>
                <td>목도리</td>
                <td>₩2,000</td>
              </tr>
              <tr>
                <td>스키복 상의</td>
                <td>₩15,000</td>
              </tr>
              <tr>
                <td>스키복 하의</td>
                <td>₩15,000</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">이불</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>이불(더블)</td>
                <td>₩15,000</td>
              </tr>
              <tr>
                <td>이블(싱글)</td>
                <td>₩10,000</td>
              </tr>
              <tr>
                <td>이불(퀸)</td>
                <td>₩18,000</td>
              </tr>
              <tr>
                <td>이불 커버</td>
                <td>₩8,000</td>
              </tr>
              <tr>
                <td>오리털 이불</td>
                <td>₩21,000</td>
              </tr>
              <tr>
                <td>거위털 이불</td>
                <td>₩23,000</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">신발</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>운동화</td>
                <td>₩5,000</td>
              </tr>
              <tr>
                <td>등산화</td>
                <td>₩7,000</td>
              </tr>
              <tr>
                <td>구두</td>
                <td>₩7,000</td>
              </tr>
              <tr>
                <td>어그 부츠</td>
                <td>₩15,000</td>
              </tr>
              <tr>
                <td>골프화</td>
                <td>₩8,000</td>
              </tr>
              <tr>
                <td>명품 운동화</td>
                <td>₩20,000</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">가방</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>책가방</td>
                <td>₩8,000</td>
              </tr>
              <tr>
                <td>명품 가방(천)</td>
                <td>₩50,000</td>
              </tr>
              <tr>
                <td>명품 가방(가죽)</td>
                <td>₩70,000</td>
              </tr>
              <tr>
                <td>가방(천)</td>
                <td>₩40,000</td>
              </tr>
              <tr>
                <td>가방(가죽)</td>
                <td>₩50,000</td>
              </tr>
            </tbody>
          </table>
          <table>
            <thead>
              <tr>
                  <th colspan="2">추가비용</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>실크</td>
                <td>₩2,000</td>
              </tr>
              <tr>
                <td>마</td>
                <td>₩2,000</td>
              </tr>
              <tr>
                <td>울</td>
                <td>₩2,000</td>
              </tr>
              <tr>
                <td>캐시미어</td>
                <td>₩2,000</td>
              </tr>
            </tbody>
          </table>
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