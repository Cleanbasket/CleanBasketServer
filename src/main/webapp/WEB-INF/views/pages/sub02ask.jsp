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
          <form id="ask-form" name="ask">
            <section class="section">
              <header class="section-header">
                <h2 class="section-title">문의정보</h2>
              </header>
              <table class="table">
                <tr>
                  <td>문의종류</td>
                  <td>
                    <input type="radio" name="question-type" value="주문"  checked="checked">
                    <span class="radio-text">주문</span>
                    <input type="radio" name="question-type" value="세탁">
                    <span class="radio-text">세탁</span>
                    <input type="radio" name="question-type" value="수거/배달">
                    <span class="radio-text">수거/배달</span>
                    <input type="radio" name="question-type" value="결제">
                    <span class="radio-text">결제</span>
                    <input type="radio" name="question-type" varle="제휴문의">
                    <span class="radio-text">제휴문의</span>
                  </td>
                </tr>
                <tr>
                  <td>제목</td>
                  <td><input class="input-fluid" type="text" name="title" placeholder="제목을 입력해 주세요." required></td>
                </tr>
                <tr>
                  <td class='text-top'>내용</td>
                  <td>
                    <textarea name="content" id="" cols="30" rows="10" placeholder="상담내용을 입력해 주세요." required></textarea>
                    <p class="msg">첨부파일이 있는 문의는 help@cleanbasket.co.kr로 문의 부탁드립니다.</p>
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
                  <td><input type="text" name="name" placeholder="홍길동" required></td>
                </tr>
                <tr>
                  <td>휴대폰 번호</td>
                  <td><input type="text" name="phone" placeholder="-없이 입력해 주세요" required></td>
                </tr>
                <tr>
                  <td>이메일 주소</td>
                  <td><input type="text" name="email" placeholder="example@gmail.com" required></td>
                </tr>  
                <tr>
                  <td>답변받기</td>
                  <td>
                    <input type="radio" name="get-answer" value="이메일 답변" checked="checked">
                    <span class="radio-text">이메일 답변</span>
                    <input type="radio" name="get-answer" value="문자 답변">
                    <span class="radio-text">문자 답변</span>
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
  <script src="./resources/scripts/sub02-1.js"></script>
</body>
</html>