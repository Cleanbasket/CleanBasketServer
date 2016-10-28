$(document).ready(function () {
  init();
})

function init() {
  expandableInit();
  $('.alert-notyetopen').click(function (e) {
    e.preventDefault();
    alert('서비스 준비중입니다. 빠른 시일내로 찾아뵙겠습니다.');
  }) 
}

function expandableInit() {
  $('.expandable-item').map(function (index, item) {
    var $item = $(item);
    $item.find('.expandable-subject').click(function () {
      var $this = $(this);
      $item.toggleClass('opened');
      if ($item.hasClass('opened')) {
        $this.removeClass('icon-arrow-down').addClass('icon-arrow-up');
      } else {
        $this.removeClass('icon-arrow-up').addClass('icon-arrow-down');
      }
    })
  })
}