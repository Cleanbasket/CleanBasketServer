<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
	<head>
        <%@ include file="./partials/m_head.jspf"%>
    	<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    	<link type="text/css" rel="stylesheet" href="./resources/styles/materialize.min.css"  media="screen,projection"/>
        <link rel="stylesheet" href="./resources/styles/m_headerfooter.css">
    	<link rel="stylesheet" href="./resources/styles/m_write.css">
	</head>
	<body>
        <%@ include file="./partials/m_header.jspf"%>
        <div class="row main"> 
            <div class="top col s12">주문서 작성</div>

            <div class="section col s12">
                <h5>주문 내역</h5>
                <div class="divider"></div>
                <table>
                    <thead>
                      <tr>
                          <th data-field="id">품목</th>
                          <th data-field="name">수량</th>
                          <th data-field="price">가격</th>
                      </tr>
                    </thead>

                    <tbody id="show-cart"></tbody>
                </table>
                <div class="divider gray"></div>
                <p>+ 주문 금액이 2만원 미만이면 배달비용 2000원이 추가됩니다.</p>
                <button class="cancel-btn btn waves-effect waves-light col s4" type="submit">변경/추가</button>
            </div>
            
            <div class="section col s12">
              <form name="date_time_info">
                <h5 class="headline-info">수거 및 배달 시간 선택</h5>
                <div class="select-wrapper">
                    <div class="text">수거</div>
                    <input type="date" id="pickup_date" name="pickup_date" required>
                    <select name="pickup_time" id="pickup_time" required></select>
                </div>
                <div class="select-wrapper">
                    <div class="text">배달</div>
                    <input type="date" id="dropoff_date" name="dropoff_date" required>
                    <select name="dropoff_time" id="dropoff_time" required></select>
                </div>
              </form>
            </div>


            <div class="section col s12">
                <h5 class="headline-info">주문 정보 입력</h5>
                <form name="order_info" >
                    <div class="input-wrapper">
                        <div class="s12">
                          <input id="phone" type="number"  name="phone" placeholder="휴대폰 번호" required>
                        </div>
                        <div class="search-wrap s12">
                          <i class="search-btn material-icons" onclick="execDaumPostcode()">search</i>
                          <input id="address" type="text" name="address" onclick="execDaumPostcode()" placeholder="주소 검색" readonly="readonly" required>

                            <div id="address-wrap" style="display:none;border:1px solid;width:100%;height:100%;position:relative">
                              <img src="//i1.daumcdn.net/localimg/localimages/07/postcode/320/close.png" id="btnFoldWrap" style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1" onclick="foldDaumPostcode()" alt="접기 버튼">
                            </div>
                        </div>
                        <div class="s12">
                          <input id="addr_building" type="text" name="addr_building" placeholder="상세주소 입력" required>
                        </div>
                        <div class="s12">
                          <input id="memo" type="text" name="memo" placeholder="추가 요청 사항">
                        </div>
                    </div>

                      <input type="checkbox" name="agree" class="filled-in" id="filled-in-box"/>
                      <label for="filled-in-box" class="checkbox-text">본인은 <a href="http://cleanbasket.co.kr/privacy" target="_blank">개인정보 제3자 제공 동의에 관한 내용</a>을 모두 이해하였으며 이에 동의합니다.</label>

                    <button class="order-btn btn-border waves-effect waves-light col s12" type="submit" disabled>주문접수</button>        
                </form>
            </div>
        </div>
        <%@ include file="./partials/m_footer.jspf"%>

    	<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    	<script src="./resources/scripts/mobile-detect.min.js"></script>
        <script src="./resources/scripts/m_header.js"></script>
        <script src="./resources/scripts/m_appdown.js"></script>
        <script src="./resources/scripts/materialize.min.js"></script>
    	<script src="./resources/scripts/write.js"></script>
        <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
        <script>
        // 다음 우편검색 api 
        var element_wrap = document.getElementById('address-wrap');

        // 'x (닫기)' 버튼 눌렀을 때 
        function foldDaumPostcode() {
            // iframe을 넣은 element를 안보이게 한다.
            element_wrap.style.display = 'none';
        }

        function execDaumPostcode() {
          var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
            new daum.Postcode({
                oncomplete: function(data) {
                  // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var fullAddr = ''; // 최종 주소 변수
                    // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        fullAddr = data.jibunAddress;
                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                        fullAddr = data.jibunAddress;
                    }
                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('address').value = fullAddr;
                    // 커서를 상세주소 필드로 이동한다.
                    document.getElementById('addr_building').focus();
                    // iframe을 넣은 element를 안보이게 한다.
                    // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                    element_wrap.style.display = 'none';
                    // 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
                    document.body.scrollTop = currentScroll;
                },
                // 우편번호 찾기 화면 크기가 조정되었을때 실행할 코드를 작성하는 부분. iframe을 넣은 element의 높이값을 조정한다.
                onresize : function(size) {
                    element_wrap.style.height = size.height+'px';
                },
                width : '100%',
                height : '100%'
            }).embed(element_wrap);
            // iframe을 넣은 element를 보이게 한다.
            element_wrap.style.display = 'block';
        }
        </script>
          
	</body>
</html>