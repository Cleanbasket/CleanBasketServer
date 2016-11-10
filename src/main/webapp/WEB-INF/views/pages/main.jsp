<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <link rel="stylesheet" href="./resources/styles/swiper.min.css">
  <link rel="stylesheet" href="./resources/styles/common.css">
  <link rel="stylesheet" href="./resources/styles/main.css">
</head>
<body>
  <div id="root">
    <%@ include file="./partials/header.jspf"%>
    <div class="content">
      <div id="hero">
        <div class="swiper-container">
          <!-- Additional required wrapper -->
          <div class="swiper-wrapper">
              <!-- Slides -->
              <div class="swiper-slide slide-one"></div>
              <div class="swiper-slide slide-two"></div>
          </div>
          <!-- If we need pagination -->
          <div class="swiper-pagination"></div>
          
          <!-- If we need navigation buttons -->
          <div class="swiper-button-prev"></div>
          <div class="swiper-button-next"></div>
        </div>
      </div>
      
      <div class="area">
        <header class="area-header">
          <div class="container">
            <div class="area-left">
              <span class="area-title">서비스 지역</span>
              <span class="vertical-divider"></span>
              <p class="area-text">서비스가 가능한 지역을 확인해 보세요</p>
            </div>
            <div class="area-right">
              <div class="area-location">
                <span class="icon icon-gps"></span>
                <input class="area-input" type="text" placeholder="동 이름을 입력하세요.">
              </div>
              <button class="btn area-submit">확인</button><!-- inline-block fix --><button class="btn btn-filled area-seeall"><span class="icon icon-map"></span><span>서비스지역 전체보기</span></button>
            </div>              
          </div>
        </header>
        <div class="area-content">
          <div class='area-map'>
            <iframe src="https://www.google.com/maps/d/embed?mid=1tC3mzUUTqSYGnBlbVZNUW10Ap70&hl=ko&z=13"></iframe>
            <div class='scroll-disable' 
              onClick="style.pointerEvents='none'" 
              onmouseleave="style.pointerEvents='auto'"></div>
          </div>
          <div class="area-place enable closed">
            <div class="closebox"></div>
            <div class="clickbox"></div>
          </div>
          <div class="area-place unable closed">
            <div class="closebox"></div>
            <div class="clickbox"></div>
          </div>
        </div>
        
        <div class="area-allplace closed">
          <header class="area-allplace-header">
            <div class="container">
              <h2>서비스지역 전체보기</h2>
              <div class="area-allplace-close">
                <span>지도로 되돌아가기</span>
                <img src="./resources/images/icon_map_close.png" class="close" />
              </div>              
            </div>
          </header>
          <div class="area-allplace-content">
            <div class="container">
              <div class="left area-addresses">
                <span>서울</span>
                <p>강남구, 서초구, 마포구, 용산구, 동작구, 관악구, 성동구, 영등포구, 서대문구(아현동, 북아현동, 충현동, 대신동, 연희동, 신촌동)</p>
              </div>
              <div class="right area-addresses">
                <span>성남</span>
                <p>분당구</p>
              </div>
              <!-- <img src="./resources/images/ic_seoul.png" alt="">
              <div class='line'></div>
              <img src="./resources/images/ic_seongnam.png" alt="">                 -->
              <img src="./resources/images/icon_seoul_seongnam.png" alt="">
            </div>
          </div>
        </div>
      </div>
      <div class="process">
        <div class="container">
          <h2 class="process-title">
            <span class="process-title-first">터치.터치.끝!</span>
            <br>
            <span class="process-title-second">이렇게 진행됩니다</span>
          </h2>
          <div class="process-wrapper">
            <ul class="row">
              <li class="col-1-of-3">
                <div class="process-card">
                  <img src="./resources/images/ic_order.png" alt="">
                  <div class="bar"></div>
                  <h3>주문</h3>
                  <p>
                    편하신 방법으로  세탁물 수거 장소와<br>
                    시간을 설정해 주세요. (앱/전화)
                  </p>
                </div>
              </li>
              <li class="col-1-of-3">
                <div class="process-card">
                  <img src="./resources/images/ic_wash.png" alt="">
                  <div class="bar"></div>
                  <h3>세탁</h3>
                  <p>
                    세탁물 수거 후, 세탁 장인분들의 손길을 통해<br>
                    깨끗히 세탁 해 드립니다. 
                  </p>
                </div>
              </li>
              <li class="col-1-of-3">
                <div class="process-card">
                  <img src="./resources/images/ic_delivery.png" alt="">
                  <div class="bar"></div>
                  <h3>배달</h3>
                  <p>
                    원하시는 시간에 깨끗해진 세탁물을 <br>
                    고객님의 문 앞까지 배달 해 드립니다. 
                  </p>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="seemore">
        <!-- <div class="container"> -->
        <div class="seemore-item">
          <div class="seemore-detail seemore-wash">
            <h3 class="seemore-title">
              <span class="seemore-title-first">세탁의 품격</span>
              <br>
              <span class="seemore-title-second">숨어있던 장인들의 재발견</span>
            </h3>
            <p class="seemore-text">
              주위에 숨어계시던 세탁소 경력 30년 이상의 세탁 장인들이<br>
              고객님들의 세탁물을 손수 세탁하십니다. <br>
              30년의 노하우 X  크린바스켓, 기대하셔도 좋습니다!
            </p>
            <a href="../sub01"><button class="btn btn-border">장인들의 노하우 보러가기</button></a>
          </div>
          <div class="seemore-image seemore-image-first">
            
          </div>
        </div>
        <div class="seemore-item">
          <div class="seemore-image seemore-image-second">
            
          </div>
          <div class="seemore-detail seemore-price">
            <h3 class="seemore-title">
              <span class="seemore-title-first">
                셔츠 2천원<br>
                정장 한 벌 6천원
              </span>
              <br>
              <span class="seemore-title-second">합리적인 가격</span>
            </h3>
            <p class="seemore-text">
              동네세탁소와 비교해도 손색이 없는 가격.<br>
              하지만 고객님의 걱정을 덜어드릴<br>
              격이 다른 품질을 경험 해 보세요. 
            </p>
            <button class="price-btn btn btn-border">세탁요금 알아보기</button>
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
              <tr>
                <td></td>
                <td></td>
              </tr>
              <tr>
                <td></td>
                <td></td>
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

      <div class="appinfo">
        <div class="container">
          <h3 class="appinfo-title">
            지금 크린바스켓을 경험해 보세요 !
          </h3>
          <div class="badges">
            <a class="badge-iphone" href="https://itunes.apple.com/kr/app/keulinbaseukes-nae-son-ui/id933165319?mt=8">
            </a>
            <a class="badge-android" href='https://play.google.com/store/apps/details?id=com.bridge4biz.laundry&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'></a>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="./partials/footer.jspf"%>
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
  <script src="./resources/scripts/swiper.jquery.min.js"></script>
  <script src="./resources/scripts/CLaddress.js"></script>
  <script src="./resources/scripts/main.js"></script>
</body>
</html>