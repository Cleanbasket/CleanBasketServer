<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
	<head>
        <%@ include file="./partials/m_head.jspf"%>

    	<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    	<link type="text/css" rel="stylesheet" href="./resources/styles/materialize.min.css"  media="screen,projection"/>
        <link rel="stylesheet" href="./resources/styles/m_headerfooter.css">
    	<link rel="stylesheet" href="./resources/styles/m_trace.css">
        <title></title>
    </head>

	<body>
        <%@ include file="./partials/m_header.jspf"%>
        <div class="row main">
            <div class="bg_box">
                <img class="bg_img" src="./resources/images/bg_trace.png">
            </div>
            <div class="content-wrapper">
                <div class="content">
                    <div class="title">주문조회</div>
                    <p>주문시 작성하셨던 휴대폰 번호로<br>세탁 주문 현황을 조회하실 수 있습니다.</p>
                    <input id="phone" type="number" maxlength="11" placeholder="휴대폰 번호를 입력해주세요.">
                    <a class="trace-btn modal-trigger btn waves-effect waves-light" href="#modal-trace">주문확인</a> 
                </div>
            </div>
        </div>

        <!-- 모달 -->
        <div id="modal-trace" class="modal">
            <div class="modal-content">
                <i class="close-btn modal-close modal-action material-icons">close</i>
                <div class="caption center">최근 주문 조회</div>
                <img src="./resources/images/ic_trace.png">
                <div class="center subtitle">주문 조회를<br>실패했습니다.</div>
                
                <div class="progress-bar">
                    <div class="step">
                        <div class="circle">
                            <div></div>
                        </div>
                        <div class="progress-name">수거</div>
                    </div>
                    
                    <div class="bar"></div>

                    <div class="step">
                        <div class="circle">
                            <div></div>
                        </div>
                        <div class="progress-name">세탁</div>
                    </div>
                    
                    <div class="bar"></div>

                    <div class="step">
                        <div class="circle">
                            <div></div>
                        </div>
                        <div class="progress-name">배달</div>
                    </div>
                    
                    <div class="bar"></div>

                    <div class="step">
                        <div class="circle">
                            <div></div>
                        </div>
                        <div class="progress-name">완료</div>
                    </div>
                </div>
                
                <div class="divider"></div>

                <table class="my-order-info">
                    <tr>
                        <td>주소</td>
                        <td>
                            <div data-field="address">주소</div>
                            <div data-field="building">상세 주소</div>
                        </td>
                    </tr>
                    <tr>
                        <td>수거시간</td>
                        <td data-field="pickup">0000-00-00 00:00</td>
                    </tr>
                    <tr>
                        <td>배달시간</td>
                        <td data-field="dropoff">0000-00-00 00:00</td>
                    </tr>
                </table>
            </div>
        </div>
        <%@ include file="./partials/m_footer.jspf"%>

    	<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    	<script src="./resources/scripts/mobile-detect.min.js"></script>
        <script src="./resources/scripts/m_header.js"></script>
        <script src="./resources/scripts/m_appdown.js"></script>
        <script src="./resources/scripts/materialize.min.js"></script>
        <script src="./resources/scripts/trace.js"></script>
	</body>
</html>