function m_appdown_init() {
  var md = new MobileDetect(window.navigator.userAgent);
  var iOSAppAddress = "https://itunes.apple.com/kr/app/keulinbaseukes-nae-son-ui/id933165319?mt=8";
  var androidAppAddress = "https://play.google.com/store/apps/details?id=com.bridge4biz.laundry&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1";
  var appDownAddress;
  console.log(window.navigator.userAgent, md.is('iOS'), md.is('AndroidOS'))
  if (md.is('iOS')) {
    appDownAddress = iOSAppAddress;
  } else if (md.is('AndroidOS')) {
    appDownAddress = androidAppAddress;
  } else {
    appDownAddress = "#"
  }

  $('.appdown-trigger').click(function (e) {
    // e.preventDefault();
    console.log(appDownAddress);
    window.location.href = appDownAddress;
  })
} 