function m_expandable_init() {
  var $trigger = $('.expandable-trigger');
  var $content = $('.expandable-content');
  $trigger.click(function (e) {
    var id = $(this).data("expandable-id");
    console.log(id);

    var $target = $content.filter(function () {
      return $(this).data("expandable-id") == id;
    })

    $target.toggleClass('closed');
    // hard coded
    if($target.hasClass('closed')) {
      $('.category-name').find('.icon')
        .addClass('icon-arrow-down-mobile')
        .removeClass('icon-arrow-up-mobile');
      } else {
        $('.category-name').find('.icon').addClass('icon-arrow-up-mobile').removeClass('icon-arrow-down-mobile')
      }
  })
}