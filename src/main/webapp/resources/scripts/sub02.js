$(document).ready(function () {
  init();
  $(document).on('click','.expandable-subject', expandableInit);
})


function init() {
  $('.alert-notyetopen').click(function (e) {
    e.preventDefault();
    alert('서비스 준비중입니다. 빠른 시일내로 찾아뵙겠습니다.');
  }) 
}

function expandableInit() {
  var item = $(this).parents('li');
  var icon = item.find('.expandable-toggle');
  item.toggleClass('opened');
  if (item.hasClass('opened')) {
    icon.removeClass('icon-arrow-down').addClass('icon-arrow-up');
  } else {
    icon.removeClass('icon-arrow-up').addClass('icon-arrow-down');
  }
}
