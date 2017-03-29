$(document).ready(function () {
  init();
})

function init() {
  $('.alert-notyetopen').click(function (e) {
    e.preventDefault();
    alert('서비스 준비중입니다. 빠른 시일내로 찾아뵙겠습니다.');
  });

  $('.alert-apply').click(function (e) {
    e.preventDefault();
    alert('1843-8543 으로 연락을 주시거나, hr@cleanbasket.co.kr 로 지원바랍니다.');
  });

}