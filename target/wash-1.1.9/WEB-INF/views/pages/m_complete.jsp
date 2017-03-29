<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
	<head>
        <%@ include file="./partials/m_head.jspf"%>

    	<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    	<link type="text/css" rel="stylesheet" href="./resources/styles/materialize.min.css"  media="screen,projection"/>
        <link rel="stylesheet" href="./resources/styles/m_headerfooter.css">
    	<link rel="stylesheet" href="./resources/styles/m_complete.css">
        <title></title>
	</head>

	<body>
        <%@ include file="./partials/m_header.jspf"%>
        <div class="row main">
            <div class="bg_box">
                <img class="bg_img" src="./resources/images/bg_complete.png">
            </div>
            <div class="content-wrapper">
                <div class="content">
                    <img src="./resources/images/ic_complete.png">
                    <div class="title">주문이<br>접수되었습니다.</div>
                    <p>주문 관련 질문 혹은 변경사항이 있으신 경우,<br>크린바스켓 웹페이지에 있는 1:1 문의<br>혹은 고객센터(1588-XXXX)로 문의 주시면<br>성심성의껏 답변 해드리겠습니다.</p>
                    <div class="divider"></div>
                    <div class="app-info">크린바스켓 앱을 이용하시면 더욱 편리하게 이용하실 수 있습니다.</div>
                    <button class="app-btn btn waves-effect waves-light appdown-trigger" type="submit">앱다운로드</button> 
                </div>
            </div>
        </div>
        <%@ include file="./partials/m_footer.jspf"%>

    	<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
    	<script src="./resources/scripts/mobile-detect.min.js"></script>
        <script src="./resources/scripts/m_header.js"></script>
        <script src="./resources/scripts/m_appdown.js"></script>
        <script src="./resources/scripts/materialize.min.js"></script>
        <script src="./resources/scripts/complete.js"></script>
	</body>
</html>