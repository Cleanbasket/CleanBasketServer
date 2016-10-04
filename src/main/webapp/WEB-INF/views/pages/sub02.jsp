<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <link rel="stylesheet" href="./resources/styles/sub02.css">
</head>
<body>
  <div id="root">
    <%@ include file="./partials/header.jspf"%>
    <div class="titlebar titlebar-tabs">
      <h1>문의하기</h1>
      <p>크린바스켓 서비스 이용에 관한 궁금증을 해결해 드립니다.</p>
      <div class="tabs">
        <a href="../sub02" class="btn btn-filled">FAQ</a>
        <a href="../sub02ask" class="btn btn-transparent">1:1문의</a>
      </div>
    </div>
    <div class="content">
      <div class="container">
        <section class="section">
          <header class="section-header">
            <h2 class="section-title">
							<div class="container">
								일반 이용문의
							</div>
						</h2>
          </header>
          <ul class="expandable">
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                  어느 지역에서 이용가능한가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                현재 크린바스켓은 서울 10개구 및 성남시 분당구에서 서비스를 제공하고 있습니다.
                <br><br>
서울 지역은 마포구, 서대문구, 중구, 용산구, 성동구, 영등포구, 동작구, 관악구, 서초구, 강남구에서 서비스가 가능하며, 성남시는 분당구 전 지역에서 가능합니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                크린바스켓은 어디에 있는 세탁소 인가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                크린바스켓은 세탁소를 직접 운영하고 있지는 않습니다! <br>
                용산, 강남, 분당에 각각 물류센터를 두고 인근의 우수 세탁소와 제휴를 맺고 그쪽에서 세탁을 진행해드리고 있습니다. 모두 최소 30년 이상 세탁업에 종사하신 세탁 장인 분들이시고, 대량으로 세탁을 진행하는 공장형 세탁소가 아니기 때문에 믿고 맡겨주셔도 됩니다. 
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                밤 늦게도 서비스를 이용할 수 있나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                매일(주말 포함) 오전 10시부터 밤 12시까지 수거 및 배달을 하고 있습니다!<br>
                 오전 10시~밤 12시 사이라면 고객님께서 원하시는 시간에 방문 드리겠습니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                주문하면 지금 바로 와주실 수 있나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                주문은 희망하시는 수거 요청 시간으로부터 최소 2시간 전까지 해주셔야 합니다.<br>
                 즉, 오후 6시~7시 사이에 수거를 원하시면 4시 전까지 주문을 해주세요!
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                밤 9시로 신청하면 9시 정각에 와주시나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  저희 크린바스켓은 시내 교통상황과 직원 동선 등으로 인해 한 시간 단위로 주문을 받고 있습니다. <br>고객님의 개인 사정으로 인해 별도로 요청하시면 최대한 원하시는 시간에 방문 드릴 수 있도록 최대한의 노력을 기울이겠으나, 같은 시간에 주문하신 다른 고객님께도 방문 드려야 하기 때문에 확실하게 장담해드리지 못하는 점은 양해 부탁드립니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                최소 주문금액이 있나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>네! 최소 주문금액은 배송료 제외 10,000원입니다. 세탁 비용이 10,000원 미만이면 수거가 불가능합니다. </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                배송료가 따로 있나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  20,000원 미만 주문에는 배송료 2,000원이 추가되며, 20,000원 이상부터는 배송료 무료입니다! <br>
                  세탁물 배달과 동시에 현장에서 추가 수거 요청을 하셔도 20,000원 미만이라면 배송료가 붙습니다. 
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                오늘 세탁물 맡기면 언제까지 받아볼 수 있나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  수거 시간 기준으로 최소 48시간 이후부터 받아보실 수 있으며, 일요일이 포함되면 72시간이 소요됩니다. 즉, 금요일 수거 요청은 월요일 배달, 토요일 수거 요청은 화요일 배달, 그리고 일요일 
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                많이 급한데, 당일 세탁은 안될까요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  죄송하지만 당일 세탁은 어렵습니다.<br>
                   고객님으로부터 수거한 세탁물은 저희 물류센터에서 분류 및 태깅 작업을 거친 후 세탁소로 이동하고, 세탁소에서 세탁이 완료되면 세탁물을 다시 물류센터로 가져와 커버를 한 번 더 씌우는 작업을 거치기 때문에 최소 48시간이 소요되고 있습니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                기타 문의사항은 어디로 연락하면 될까요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  어플리케이션 내 1:1 상담창구를 이용하셔도 좋고, 1833-8543으로 전화 주셔도 됩니다! <br>
                  고객센터 영업시간은 10:00부터 18:30까지입니다. (13:00~14:30 점심시간)<br>
                   이 외의 시간에는 1:1 상담창구 통해 문의 글 남겨주시면 이틀 내로 고객님께 연락드리겠습니다.
                </p>
              </div>
            </li>
          </ul>
        </section>      
        <section class="section">
          <header class="section-header">
            <h2 class="section-title">
							<div class="container">
								수거/배달
							</div>
						</h2>
          </header>

          <ul class="expandable">
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
              수거•배달 예정 시간에 집에 없을 것 같아요. 시간 변경이 가능한가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  어플리케이션 메뉴 중 '주문 현황' 탭에 주문을 변경 및 취소할 수 있는 페이지가 있습니다. <br>
                  '주문 변경/취소'를 누르시면 원하는 날짜 및 시간으로 직접 변경이 가능합니다.<br>
                  <br>
                  수거시간 변경의 경우, 변경하는 시점에서 최소 2시간 이후부터 방문이 가능하며, <br>
                   배달시간 변경의 경우, 수거 시점으로부터 48시간 이후로만 가능합니다
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                수거 시간 전에 나가봐야 할 것 같아요. 문 앞에 걸어두고 가도 되나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  네! 문 앞이든, 소화기구함 안이든, 경비실이든 가능합니다. 다만 분실의 위험이 없는 안전한 장소에 놓아 주세요. 문 앞에 걸어두신 세탁물이 분실되었을 경우, 크린바스켓은 이에 대해 책임을 지지 않습니다. <br>
                  또한, 파트너가 방문 확인 전화를 드릴 때 문 앞에 걸어두고 가신다고 말씀해주시면 더욱더 감사하겠습니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                세탁물 분류를 미리 해두어야 하나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  분류는 미리 해 두실 필요 없습니다. 파트너가 방문 시 직접 세탁물 하나하나를 확인하고 수거 가방에 담아드리기 때문에 세탁물 준비만 해주시면 됩니다!
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                수거지와 배달지를 다르게 할 수 있나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  네! 가능합니다. 수거지와 배달지 모두 동일한 물류센터에서 담당하는 지역이라면 무료로 가능하고, 다른 물류센터에서 담당하는 지역이라면 추가 비용 10,000원이 발생합니다.  <br>
                  용산 센터: 서대문구, 마포구, 관악구, 영등포구, 중구(일부지역), 영등포구, 용산구 (한남동 제외)<br>
                   강남 센터: 서초구, 강남구, 성동구, 중구(일부지역) 용산구 한남동 <br>
                  분당 센터: 분당구 전 지역<br>
                    예를 들어, 서대문구 수거/영등포구 배달, 혹은 서초구 수거/성동구 배달은 모두 무료로 가능합니다.<br>
                   그러나 강남구 수거 분당구 배달인 경우에는 센터 간 이동이 필요하기 때문에 추가비용이 발생합니다.<br>
                   배송지를 다른 곳으로 요청하실 때에는 수거 파트너 분께 전달해주시거나, 고객센터로 문의 하세요!
                </p>
              </div>
            </li>
          </ul>
        </section>
        <section class="section">
          <header class="section-header">
            <h2 class="section-title">
							<div class="container">
								세탁
							</div>
						</h2>
          </header>
          <ul class="expandable">
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                크린바스켓은 드라이클리닝 서비스인가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  드라이클리닝 혹은 물세탁 서비스입니다! 크린바스켓 세탁소에서는 세탁물의 케어라벨에 표기된 세탁법 그대로를 따르고 있습니다. 가격표에 표기된 금액은 케어라벨에 따른 세탁 처리비용입니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                케어라벨에는 물세탁이라고 되어있지만, 드라이를 하고 싶어요. 가능할까요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  네! 물론 가능합니다. 다만 물세탁을 필요로 하는 일부 제품은 드라이클리닝을 하게 되면 약품 냄새가 짙게 스며든다거나, 오히려 손상이 될 수가 있어요. 그렇기 때문에 케어라벨과 다른 세탁법을 요청하시면 그에 따른 책임은 모두 고객님에게 있습니다. 
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                셔츠 다림질만 가능한가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  고객님께서 직접 세탁하신 후 다림질만 맡기시는 것도 가능합니다. <br>
                  셔츠 다림질은 1,500원으로 진행해드리고 있습니다!
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                '물빨래'는 어떤 품목인가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  '물빨래'품목은 저희가 드리는 40cm*60cm 크기의 빨래 주머니에 수건, 속옷, 양말, 얇은 티셔츠 등의 생활 빨래를 담아주시는 품목이며, 다림질은 따로 제공해드리지 않지만 차곡차곡 개어서 드립니다.<br>
                  주로 댁에서 세탁기에 던져 넣는 옷들을 넣어주시면 그 주머니 그대로 단독 세탁 해드리기때문에 손빨래, 혹은 드라이클리닝이 필수인 옷을 넣어주시면 해당 세탁물의 손상에 대해서는 저희가 따로 책임을 지지 않습니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                물빨래를 하고 싶은데, 검은 옷과 흰 옷이 섞여있어요. 색깔 분류를 위해 추가 비용을 지불해야하냐요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  물빨랫감이 모두 한 주머니 안에 들어간다면 색깔 분류 비용은 따로 지불하지 않으셔도 됩니다! 색깔 분류는 저희 세탁소에서 해드리고 있습니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                물세탁이 필수인 패딩이 있는데, 품목 중에서 '물빨래'를 선택해야 하나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  품목 중 '물빨래'는 어떤 세탁물에 대해 물세탁을 요청하시는 품목이 아닙니다!<br>
                  <br>
                  FAQ 리스트의 <물빨래는 어떤 품목인가요?> 질문을 참고해주세요.<br>
                  <br>
                  대부분의 패딩 세탁은 물세탁 진행해드리니 별도로 요청해주시지 않으셔도 되고, 품목은 '아웃도어/패딩'으로 분류하여 주문해주시면 됩니다. 
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                가격표에 없는 세탁물도 맡길 수 있나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  네! 저희 제휴 세탁소에서 세탁이 가능한 제품이라면 물론 가능합니다! <br>
                  세탁 여부를 확인하기 위해서는 1:1 상담창구를 이용하시거나 1833-8543 콜센터로 문의 주세요.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                수선도 가능한가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  죄송하지만 현재 수선 서비스는 제공하고 있지 않습니다
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                찌든 담배 냄새도 제거 가능한가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  담배 냄새가 스며든 정도와 기간에 따라 다를 수 있지만, 냄새가 오랫동안 짙게 밴 경우에는 완벽 제거가 어렵습니다. 다만 별도로 요청하시면 최대한의 노력을 기울여 냄새 제거 처리를 해드리고 있습니다!
                </p>
              </div>
            </li>
          </ul>
        </section>
        <section class="section">
          <header class="section-header">
            <h2 class="section-title">
							<div class="container">
								결제
							</div>
						</h2>
          </header>
          <ul class="expandable">
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                결제는 언제 하면 되나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  결제는 세탁물이 완료된 후 배달 시에 이루어집니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                카드로도 결제가 가능한가요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  결제 시 현금과 카드 모두 이용 가능하며, 법인카드로도 결제 가능합니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                현금영수증 발급 되나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  개인 휴대전화 번호나 사업자 번호로 저희 배달 직원분께 현금영수증 요청하시면 발급해드립니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                마일리지나 쿠폰 사용은 어떻게 하나요?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  수거/배달 파트너에게 마일리지나 쿠폰 사용을 요청하시면 됩니다! <br>
                  마일리지는 앱의 ‘내 정보’탭에서 적립금액을 확인하신 후, 사용하고자 하는 금액을 말씀해주시면 되고, 보유하고 계신 쿠폰은 현장에서 보여주시면 됩니다. <br>
                  9월부터는 어플리케이션 내에서 직접 쿠폰 및 마일리지를 적용할 수 있도록 기능을 추가할 예정이오니 조금만 기다려주세요! 
                </p>
              </div>
            </li>
          </ul>
        </section>
        <section class="section">
          <header class="section-header">
            <h2 class="section-title">
							<div class="container">
								불만사항
							</div>
						</h2>
          </header>
          <ul class="expandable">
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                세탁물을 받아보았는데, 품질이 만족스럽지가 않아요.
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  믿고 맡겨주신 고객님께 실망을 시켜드려 정말 죄송합니다! <br>
                  세탁 품질에 만족하지 못하셨다면, 어플리케이션 내 1:1 상담창구를 이용하시거나 1833-8543 콜센터 쪽으로 주문번호, 세탁물 종류, 그리고 불만족 사유를 알려주세요. 최대한 빠르게 답변드리겠습니다.
                </p>
              </div>
            </li>
            <li class="expandable-item">
              <h3 class="expandable-subject">
                <span class="icon icon-q"></span>
                세탁물에 손상이 갔어요. 어떻게 해야하죠?
                <span class="expandable-toggle icon icon-arrow-down"></span>
              </h3>
              <div class="expandable-inner">
                <span class="icon icon-a"></span>
                <p>
                  고객님의 소중한 세탁물에  저희 본사 측에서 다시 세탁물을 받아보고 사고 경위를 확인해보겠습니다. <br>
                   어플리케이션 내 1:1 상담창구를 이용하시거나 1833-8543 콜센터 쪽으로 고객님의 주문번호와 관련 내용을 알려주세요! 빠른 안내 도와드리겠습니다.
                </p>
              </div>
            </li>
          </ul>
        </section>
        
      </div>
    </div>
    <%@ include file="./partials/footer.jspf"%>
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
  <script src="./resources/scripts/sub02.js"></script>
</body>
</html>