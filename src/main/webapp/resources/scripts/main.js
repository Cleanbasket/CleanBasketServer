$(document).ready(function () {
  init();
})

function init() {
  fitHero();

  var swiper = new Swiper('.swiper-container', {
    pagination: '.swiper-pagination',
    nextButton: '.swiper-button-next',
    prevButton: '.swiper-button-prev',
    slidesPerView: 1,
    paginationClickable: true,
    spaceBetween: 30,
    loop: true,
    autoplay: 6 * 1000
  });

  $(window).on("resize", function () {
    console.log("resize");
    fitHero();
  })

  $(".area-input").keypress(function(e) { 
    if (e.keyCode == 13){
        searchAddress();
    }    
  });

  $(".area-submit").click(function(e) { 
      searchAddress();
  });

  $(".area-seeall").click(function(e) {
    $('.area-allplace').toggleClass('closed')
  });
  $(".area-allplace .close").click(function(e) {
    $('.area-allplace').toggleClass('closed')
  });

  $('.area-place .closebox').click(function (e) {
    $(this).parent().addClass("closed")
  });

  var priceModal = document.getElementById('price-modal'),
    openBtn = document.getElementsByClassName("price-btn")[0],
    closeBtn = document.getElementsByClassName("modal-close")[0];

  openBtn.onclick = function() {
      priceModal.style.display = "block";
  }

  closeBtn.onclick = function() {
      priceModal.style.display = "none";
  }

  window.onclick = function(event) {
      if (event.target == priceModal) {
          priceModal.style.display = "none";
      }
  }
}

function fitHero () {
  var headerHeight = 95;
  var windowHeight = $(window).height();
  
  var $hero = $("#hero").height(windowHeight - headerHeight);
}

function searchAddress() {
  var text = $(".area-input")[0].value;
  if (!text || typeof text !== "string") return;

  // 맨뒤 단어가 동이라 가정
  var address = text.split(' ');
  var dong = address[address.length - 1];

  // 뒤에 "동" 추가
  if (dong[dong.length - 1] !== "동") {
    dong = dong + "동";
  }

  closePlaceEnable();
  closePlaceUnable();

  // 서비스 중이 아님
  if (CLaddresses.indexOf(dong) < 0) {
    showPlaceUnable();
  // 서비스 중임
  } else {
    showPlaceEnable();
  } 
}

function showPlaceEnable() {
  $('.area-place.enable').removeClass('closed')
}

function closePlaceEnable() {
  $('.area-place.unable').addClass('closed')  
}

function showPlaceUnable() {
  $('.area-place.unable').removeClass('closed')
}

function closePlaceUnable() {
  $('.area-place.unable').addClass('closed')
}



// "alert()"
//  onClick=
//   onClick="alert('서비스 준비중입니다. 빠른 시일내로 찾아뵙겠습니다.')"