<%@ page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" session="false"%>
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <link rel="stylesheet" href="./resources/styles/common.css">
  <link rel="stylesheet" href="./resources/styles/sub02-1.css">
</head>
<body>
  <div id="root">
    <%@ include file="./partials/header.jspf"%>
    <div class="titlebar titlebar-tabs">
      <h1>문의하기</h1>
      <p>크린바스켓 서비스 이용에 관한 궁금증을 해결해 드립니다.</p>
      <div class="tabs">
        <a href="../sub02" class="btn btn-transparent">FAQ</a>
        <a href="../sub02ask" class="btn btn-filled">1:1문의</a>
      </div>
    </div>
    <div class="content">
      <div class="container">
        <div class="ask">
          <form action="">
            <section class="section">
              <header class="section-header">
                <h2 class="section-title">문의정보</h2>
              </header>
              <table class="table">
                <tr>
                  <td>문의종류</td>
                  <td>
                    <input type="radio" name="question-type" value="order">
                    <span class="radio-text">주문</span>
                    <input type="radio" name="question-type" value="wash">
                    <span class="radio-text">세탁</span>
                    <input type="radio" name="question-type" value="deliver">
                    <span class="radio-text">수집/배달</span>
                    <input type="radio" name="question-type" value="cash">
                    <span class="radio-text">결제</span>
                    <input type="radio" name="question-type" varle="partner">
                    <span class="radio-text">제휴문의</span>
                  </td>
                </tr>
                <tr>
                  <td>제목</td>
                  <td><input class="input-fluid" type="text" placeholder="제목을 입력해 주세요."></td>
                </tr>
                <tr>
                  <td class='text-top'>내용</td>
                  <td>
                    <textarea name="" id="" cols="30" rows="10" placeholder="상담내용을 입력해 주세요."></textarea>
                  </td>
                </tr>
                <tr>
                  <td>첨부파일</td>
                  <td>
                    <!-- <div class="filebox">
                      <label for="add-file">파일추가</label>
                      <input type="file" id="add-file" class='upload-hidden'>
                    </div> -->
                    <button class="attached-btn filebox-trigger" data-filebox-id='attached'>파일추가</button>
                    <div class="filebox-inputs" data-filebox-id='attached'>
                      
                    </div>
                    <span class='attached-text'>파일당 <strong>최대 10M까지</strong> 등록할 수 있습니다.</span>
                    <div class='attached-box filebox-box' data-filebox-id='attached'></div>
                  </td>
                </tr>
              </table>
            </section>
            <section class="section">
              <header class="section-header">
                <h2 class="section-title">고객정보</h2>
              </header>
              <table class='table'>
                <tr>
                  <td>고객명</td>
                  <td><input type="text" placeholder="홍길동"></td>
                </tr>
                <tr>
                  <td>휴대폰 번호</td>
                  <td><input type="text" placeholder="-없이 입력해 주세요"></td>
                </tr>
                <tr>
                  <td>이메일 주소</td>
                  <td><input type="text" placeholder="example@gmail.com"></td>
                </tr>  
                <tr>
                  <td>답변받기</td>
                  <td>
                    <input type="radio" name="get-answer" value="email">
                    <span class="radio-text">이메일답변</span>
                    <input type="radio" name="get-answer" value="sms">
                    <span class="radio-text">SNS답변</span>
                  </td>
                </tr>
              </table>
            </section>       
            <div class="ask-btns">
              <button class="btn btn-filled" type="submit">등록</button>
              <a class="btn btn-default" href="../sub02">
                취소
              </a>
            </div>
          </form>
        </div>

      </div>
    </div>
    <%@ include file="./partials/footer.jspf"%>
  </div>
  <script src="./resources/scripts/jquery.min.js"></script>
  <script src="./resources/scripts/filebox.js"></script>
  <script src="./resources/scripts/sub02-1.js"></script>
</body>
</html>