/**************************************
IUBoxes Default Functions
***************************************/

function readyIUBoxes(){	
	//init switch
	$(".PGSwitch").each(function(){
		//init
		$($(this).children()[1]).css('display', 'block');
		var checkbox = $(this).children('input')[0];
		var checkedAttr = $(checkbox).attr('checked');
		if (checkedAttr == undefined) {
			//off
			$($(this).children()[2]).css('display', 'block');
			$($(this).children()[1]).css('visibility', 'hidden');
		}
		else {
			//on
			$($(this).children()[2]).css('display', 'none');
			$($(this).children()[1]).css('visibility', 'visible');
		}
	});
	
	//init carousel
	$('.IUCarousel').each(function(){
		initCarousel(this.id);
	});
	
}

function initLoadIUBoxes(){
	//init google map
	$('.IUGoogleMap').each(function(){
		initIUGoogleMap(this);
	});
    
    
	//init transition
	$('.IUTransition').each(function(){
		var eventType = $(this).attr('transitionevent');
                            
		if (eventType=='click'){
			$(this).css('cursor', 'pointer');
                            
		}
                            
		//make initial transition state to first item
		if ($(this).hasClass('PGSwitch')) {
			if($($(this).children('input')[0]).attr('checked') != undefined) {
				$(this).data('isSelected', 'true');
			}
			else {
				$(this).data('isSelected', 'false');
			}
		}
		else {
			$(this).data('isSelected', 'true');
		}
	});
    
	//init iuwebmovie-vimeo
	$('.IUWebMovie[videotype="vimeo"][webvideoeventautoplay] > iframe').each(function(){
		Froogaloop(this).addEvent('ready', vimeoReady);
	});
	function vimeoReady(playerId){
		$('#'+playerId).parent().data('ready', true);
	}
    
	//init iumovie
	$('.IUMovie').click(function(){
		$(this).get(0).paused ? $(this).get(0).play() : $(this).get(0).pause();
	});
    
    
	//init panel
	$('.IUPanel > .IUPanel-closeButton').each(function(){
		$(this).click(
			function(){
				var panelId = $(this).parent()[0].id;
				closePanel(panelId);
			}
		);
	})
    
    
	$('#IUPanelDimColor').click(function(){
		var externalClose = $(currentDisplayPanel).attr('externalClose');
		if (externalClose == 'true'){
			closePanel($(currentDisplayPanel).attr('id'));
		}
	});
    
	$('#IUPopUpDimColor').click(function(){
		var externalClose = $(currentDisplayPopUp).attr('externalClose');
		if (externalClose == 'true'){
			toggleShowPopUp(currentDisplayPopUp, false);
		}
	});
    
    
	$('.PGFileUploadInnerButton').change(function(){
		filename=$(this).val().replace(/^.*[\\\/]/, '')
		$($(this).parent().parent().children()[1]).html(filename);
	});
    
    
	$(".PGSwitch").each(function(){
		// click event                        
		$(this).click(function(){
			var checkbox = $(this).children('input')[0];
			var checkedAttr = $(checkbox).attr('checked');
			if (checkedAttr != undefined) {
				$(checkbox).removeAttr('checked');
			}
			else {
				$(checkbox).attr('checked', '');
			}
			reframeCenterIU(this);
		});
                        
	});
    
	initAllPGSlide();
	initAllPGRangeSlide();
	
	//init popup
	initAllIUPopUp();
	initAllPGFlipSwitch();
    
}

function initAfterCenter(){
	//init scroll event
	if ( $('.IUScrollAnimator').length > 0){
		initScrollAnimator();
	}
}


function activateLink(iu, location){
	if (iu ==  undefined) {
		return;
	}
	var url = window.location.pathname;
	var links =  iu.href.split('#');
	var urlRegExp = new RegExp(url == '/' ? window.location.origin + '/?$' : url.replace(/\/$/,''));
	if(urlRegExp.test(links[0]) && links.length==1 ){
		if(location=='parent'){
			$(iu).parent().addClass('active');
		}
		else if(location=='child'){
			$(iu).children().first().addClass('active');
		}
	}
	else{
		if(location=='parent'){
			if ($(iu).parent().hasClass('active')) {
				$(iu).parent().removeClass('active');
			}
		}
		else if(location=='child'){
			if ($(iu).children().first().hasClass('active')) {
				$(iu).children().first().removeClass('active');
			}
		}
	}
}


$(window).mousemove(function(event) {
	if (clickedSlidePoint != undefined) {
		changeSlideInnerBarWidthByMouseEvent(event);
	}
	else if (clickedRangeSlidePointStart != undefined) {
		changeRangeSlideInnerBarWidthByStartPointMouseEvent(event);
	}
	else if (clickedRangeSlidePointEnd != undefined) {
		changeRangeSlideInnerBarWidthByEndPointMouseEvent(event);
	}
});

$(window).mouseup(function(event){
	if (event.which == 1) {
		clickedSlidePoint = undefined;
		clickedRangeSlidePointStart = undefined;
		clickedRangeSlidePointEnd = undefined;
	}
});


/**************************************
IUPopUp
***************************************/
var currentDisplayPopUp;
var popUpPostTop = {};
var isPopUpAnimating = false;

function initAllIUPopUp(){
	
	resetPopUpPostTop();
	
	$('.PopUpCloseButton').click(function(){
		var popup = $(this).parent();
		toggleShowPopUp(popup, false);
	});
}

function resetPopUpPostTop(){
	$('.IUPopUp').each(function(){
		
		var postTop = (window.innerHeight - this.offsetHeight)/2;
		popUpPostTop[this.id] = postTop;
				
		if ($(this).is(currentDisplayPopUp)) {
			var effect = $(this).attr('PopUpShowAnimation');
			var preTop = effect == "topdown" ? -1 * (this.offsetHeight) : window.innerHeight;
			var willMoveY = postTop - preTop;				
			$(this).velocity({top:preTop+'px', translateY: willMoveY+'px'}, {duration:0});
						
		}
	});
}

function showPopUp(popUp, effect, duration){
	if (isPopUpAnimating == true) {
		return;
	}
	isPopUpAnimating = true;
	var isPopUp = popUp.hasClass('IUPopUp');
	if (isPopUp) {
		var popUpEl = popUp[0];
		var popupId = popUpEl.id;
		var currentTop = popUpEl.offsetTop;
		var postTop = popUpPostTop[popupId];
		var willMoveY = postTop - currentTop;
		
        
		if (currentDisplayPopUp != undefined) {
			toggleShowPopUp(currentDisplayPopUp, false);
		}
        
		var dimZIndex = parseInt(popUp.css('z-index')) - 1;
		var popupDimColor = $('#IUPopUpDimColor');
		
		popupDimColor.css({'display': 'block', 'opacity': 0, 'z-index': dimZIndex});
		popupDimColor.addClass(popupId +'DimColor');
        
		var popUpAnimationOption = {duration: duration, complete: function(){
			isPopUpAnimating = false;
		}};
		
		popUp.velocity({translateY: willMoveY+'px'}, popUpAnimationOption);
		popupDimColor.velocity({opacity:1}, {duration: duration});
        
		currentDisplayPopUp = popUp;
		disableScrolling();		
		pauseCarousel(true);
	}
    
}

function hidePopUp(popUp, effect, duration){
	if (isPopUpAnimating == true) {
		return;
	}
	isPopUpAnimating = true;
	var isPopUp = popUp.hasClass('IUPopUp');
	if (isPopUp) {
		var preTop = 0;
		var popUpElement = popUp[0];
		
		if (effect == "topdown") {
			preTop = -1 * popUpElement.offsetHeight;
		}
		else {
			preTop = window.innerHeight;
		}
        
		var currentTop = popUpElement.offsetTop;
		var willMoveY = preTop - currentTop;
        
		var popUpAnimationOption = {duration: duration, complete:function(){
			isPopUpAnimating = false;
		}};
		var popupDimColorElement = $('#IUPopUpDimColor')[0];
		
		var dimAnimationOption = {duration:duration, complete:function(){
			$(this)[0].style.display = 'none'; 
			$(this).removeClass(popUpElement.id+'DimColor');
		}};
		$(popUpElement).velocity({translateY:willMoveY}, popUpAnimationOption);
		$(popupDimColorElement).velocity({opacity:0}, dimAnimationOption);
        
		currentDisplayPopUp = undefined;
		enableScrolling();
        restartCarousel(true);
	}
}

function toggleShowPopUp(popUp, show){
	var duration = parseFloat(popUp.attr('PopUpShowDuration'))*1000;
	var effect = popUp.attr('PopUpShowAnimation');
	if (show == true){
		showPopUp(popUp, effect, duration);
	}
	else {
		hidePopUp(popUp, effect, duration);
	}
}


/**************************************
IUCollapsible
***************************************/

function toggleShowCollapsible(iuCollapsible, toggle){
	var collapsibleHeader = $($(iuCollapsible).children()).filter('.IUCollapsibleHeader');
	var collapsibleContent = $($(iuCollapsible).children()).filter('.IUCollapsibleContent');
	var foldedHeaderItem = $($(collapsibleHeader).children()).filter('.IUCollapsibleHeaderItem')[0];
	var unfoldedHeaderItem = $($(collapsibleHeader).children()).filter('.IUCollapsibleHeaderItem')[1];
    
	var animation = $(iuCollapsible).attr('JQueryShowAnimation');
	var duration = parseFloat($(iuCollapsible).attr('JQueryShowDuration'))*1000;
	var unfolded = $(iuCollapsible).attr('unfoldedcollapsible') == 'true';
	if (unfolded == toggle) {
		return;
	}
	
	if (toggle) {
		$(iuCollapsible).attr('unfoldedcollapsible', 'true');
		if (!jQuery.isEmptyObject(unfoldedHeaderItem)) {
			$(foldedHeaderItem).css('display', 'none');
			$(unfoldedHeaderItem).css('display', 'block');
			reframeCenterIU(unfoldedHeaderItem);
		}
	}
	else {
		$(iuCollapsible).attr('unfoldedcollapsible', 'false');
		if (!jQuery.isEmptyObject(unfoldedHeaderItem)) {
			$(foldedHeaderItem).css('display', 'block');
			$(unfoldedHeaderItem).css('display', 'none');
			reframeCenterIU(foldedHeaderItem);
		}
	}
	
	toggleShowIU(collapsibleContent, toggle, animation, duration);
	
}



/**************************************
IUTransition
***************************************/

function transitionAnimationOn(eventObject){
	var transition = eventObject.currentTarget;
	var secondObj = $(transition).find('.IUTransitionItem')[1];
	var animation = $(transition).attr('JQueryShowAnimation');
	var duration = parseFloat($(transition).attr('JQueryShowDuration'))*1000;
    
	var firstObj = $(transition).find('.IUTransitionItem')[0];
	toggleShowIU(secondObj, true, animation, duration, {},function(){
		$(firstObj).css('visibility', 'hidden');
	});
    
	$(transition).data('isSelected', 'false');
}

function transitionAnimationOff(eventObject){
	var transition = eventObject.currentTarget;
	var secondObj = $(transition).find('.IUTransitionItem')[1];
	var animation = $(transition).attr('JQueryShowAnimation');
	var duration = parseFloat($(transition).attr('JQueryShowDuration'))*1000;
    
	var firstObj = $(transition).find('.IUTransitionItem')[0];
	$(firstObj).css('visibility', 'visible');
	toggleShowIU(secondObj, false, animation, duration);
    
	$(transition).data('isSelected', 'true');
}

function transitionAnimation(eventObject){
    
	var transition = eventObject.currentTarget;
	var effect = $(transition).attr('transitionanimation');
	var isSelected= $(transition).data('isSelected');
    
    
	if (isSelected=='true'){
		transitionAnimationOn(eventObject);
	}
	else {
		transitionAnimationOff(eventObject);
	}
}

/**************************************
IUMovie, WebMovie
***************************************/

function autoPlayIUWebMovieDuringFocusing(){
	//autoplay during focusing
	var scrollY = window.scrollY;
	var screenH = window.innerHeight;
	var maxY = scrollY + screenH;
	$('[webVideoEventAutoplay]').each(function(){
		var type = $(this).attr('videotype');
		var display = $(this).css('display');
		if(isElementInViewport(this) && display != 'none'){
			//play
			if(type=='vimeo'){
				var isReady = $(this).data('ready');
				if (isReady){
					var playerID = $(this).attr('id')+'_vimeo';
					Froogaloop(playerID).api('play');
				}
			}
			else if(type=='youtube'){
				var id = $(this).attr('id')+'_youtube';
				var youtube = document.getElementById(id);
				youtube.playVideo();
			}
		}
		else{
			//stop
			if(type=='vimeo'){
				var isReady = $(this).data('ready');
				if (isReady){
					var playerID = $(this).attr('id')+'_vimeo';
					Froogaloop(playerID).api('pause');
				}
			}
			else if(type=='youtube'){
				var id = $(this).attr('id')+'_youtube';
				var youtube = document.getElementById(id);
				youtube.pauseVideo();
			}
		}
	});
}

function autoPlayIUMovieDuringFocusing(){
	//autoplay during focusing
	var scrollY = $(document).scrollTop();
	var screenH = $(window).height();
	var maxY = scrollY + screenH;
	$('[videoEventAutoplay]').each(function(){
		var display = $(this).css('display');
		if(isElementInViewport(this) && display != 'none'){
			//play
			this.play();
		}
		else{
			//stop
			this.pause();
		}
	});
}

/**************************************
IUText
***************************************/

function reloadTextMediaQuery() {
	var viewport = window.innerWidth;
	$('p').filter('.IUViewPortChangeableText').each(function(index){
		var text = $(this)[0];
                                                    
		if ($(this).attr('maxviewport') < viewport || $(this).attr('minviewport') > viewport ){
            text.style.display = 'none';
		}
		else {
            text.style.display = 'block';
		}
	});
}

/**************************************
IUTabView
***************************************/

function checkHashInTabView(){
	$('.IUTabView').each(function(){
		//check contain current link path
		if($.contains(this, $(window.location.hash)[0])){
			//check header at first
			var index=-1;
			$(this).children('.IUTabHeader').children().each(function(i){
				if($.contains(this, $(window.location.hash)[0])){
					index = i;
				}
			});
			if (index >= 0){
				selectTabViewAtIndex(this, index);
			}
			else{
				//check content
				$(this).children('.IUTabContent').children().each(function(i){
					if($.contains(this, $(window.location.hash)[0])){
						index = i;
					}
				});
				if (index >= 0){
					selectTabViewAtIndex(this, index);
				}
			}
                         
		}
		else{
			selectTabViewAtIndex(this, 0);
		}
	});
}

function clickTabHeaderItem(item, event){
	var tabView = item.parentElement.parentElement;
	var tabHeader = item.parentElement;
	var selectedIndex = $(tabHeader.children).index(item);
	selectTabViewAtIndex(tabView, selectedIndex);
}

function selectTabViewAtIndex(tabView, index){
	var prevSelectedIndex = $(tabView).data('prevSelectedIndex');
	if(index == prevSelectedIndex){
		return;
	}
    
	var tabHeader = tabView.children[0];
	var tabContent = tabView.children[1];
    
	//header
	$(tabHeader.children).filter('.IUTabHeaderItem').each(function(i){
		var activeItem  = $(this.children).filter('.IUTabHeaderActiveItem');
		var defaultItem = $(this.children).filter('.IUTabHeaderDefaultItem');
		if (i==index){
			activeItem.css('display', 'block');
			defaultItem.css('display', 'none');
		}
		else{
			activeItem.css('display', 'none');
			defaultItem.css('display', 'block');
		}
	});
    
	//content
	$(tabContent.children).filter('.IUTabContentItem').each(function(i){
		if(i != index){
			$(this).css('display', 'none');
			$(this).finish();
                                                            
			if ($(this).hasClass('active')){
				$(this).removeClass('active');
			}
		}
	});
    
	var selectedItem = $(tabContent.children).filter('.IUTabContentItem')[index];
	$(selectedItem).addClass('active');
	//add new active
	var animation = $(tabView).attr('JQueryShowAnimation');
	var duration = parseFloat($(tabView).attr('JQueryShowDuration'))*1000;
	var option;
	if (index > prevSelectedIndex) {
		option = {direction: 'right'};
	}
	else {
		option = {direction: 'left'};
	}
    
	//move to header - scroll
	var tabViewTop = $(tabView)[0].offsetTop;
    $('.IUPage').animate({scrollTop:tabViewTop}, 0);
    
	toggleShowIU(selectedItem, true, animation, duration, option);
	var prevSelectedIndex = $(tabView).data('prevSelectedIndex', index);
    
    
}


/**************************************
Methods for Panel, Popup
***************************************/
var prevWindowScrollY;

function disableScrolling(){
	prevWindowScrollY = window.scrollY;
	var page = $('.IUPage')[0];
	$(page).addClass('stop-scrolling');
	$.Velocity.hook(page, "translateY", '-' + prevWindowScrollY + 'px');
	//for mobile-device
	$(page).bind('touchmove', function(e){e.preventDefault()})
    
}
function enableScrolling(){
	var page = $('.IUPage')[0];
	$(page).removeClass('stop-scrolling');
	$.Velocity.hook(page, "translateY", '0px');
	$(page).css('transform', '');  
	window.scrollTo(0, prevWindowScrollY);
	//for mobile-device
	$(page).unbind('touchmove')
}

/**************************************
IUPanel
***************************************/

var currentDisplayPanel;
var isPanelAnimating = false;

function togglePanel(panelId){
	var panel = $('#'+panelId);
    
	var isShown = panel.data('isShown');
	if (isShown){
		closePanel(panelId);
	}
	else{
		showPanel(panelId);
	}
    
}

function resetPanelPosition(){
	$('.IUPanel').each(function(){
		var direction = $(this).attr('direction');
		var panelOuterWidth = this.offsetWidth;
		var panelInitX = -1 * panelOuterWidth + 'px';
		var panelInitCSS = direction == 'left'? {left : panelInitX} : {right : panelInitX};
		var panel = this;
		if ($(this).is(currentDisplayPanel)) {
			window.requestAnimFrame(function(){
				$(panel).css(panelInitCSS);
				var translateX = direction == 'left'? panelOuterWidth : -1 * panelOuterWidth;
				panel.style.transform = 'translateX(' + translateX + 'px)';
			});
		}
		
	});
}


function showPanel(panelId){
	
	var panel = $('#'+panelId);
	var page = $('.IUPage');
	var dimColor = $('#IUPanelDimColor');
	
	if (isPanelAnimating == true) {
		return ;
	}
	isPanelAnimating = true;
	
	dimColor.addClass($(panel).attr('id')+'DimColor');
	dimColor.css({'display': 'block', 'opacity': 0});
	
	var animation = panel.attr('JQueryShowAnimation');
	var duration = parseFloat(panel.attr('JQueryShowDuration'))*1000;
	var direction = panel.attr('direction');
	var panelOuterWidth = panel[0].offsetWidth;
	var panelDestinationX = direction == 'left'? panelOuterWidth+'px' : -1 * panelOuterWidth+'px';
	
	var dimAnimationCSS = {opacity:1};
    
	if (animation == 'overlay'){
        
		var panelAnimationCSS = {translateX:panelDestinationX};
		var panelAnimationOption = {duration:duration, complete:function(){isPanelAnimating = false;}};
		var dimZIndex = parseInt($(panel).css('z-index')) - 1;
		
		window.requestAnimFrame(function(){
			dimColor.css('z-index', dimZIndex);
		});
        
		panel.velocity(panelAnimationCSS, panelAnimationOption);
	}
    
	else if (animation == 'reveal' || animation == 'push'){
		page.css({'-webkit-backface-visibility':'hidden', '-moz-backface-visibility':'hidden', 'backface-visibility':'hidden'});
        
		var dimDestinationX = panelDestinationX;
		var pageDestinationX = panelDestinationX;
        
		if (animation == 'reveal'){
			
			window.requestAnimFrame(function(){
				panel.css('z-index', -1);
				page.css('z-index',0);
				dimColor.css('z-index', 0);
			});
						
			var pageAnimationCSS = {translateX : pageDestinationX};
			var pageAnimationOption = {duration:duration, complete:function(){
				isPanelAnimating = false;
				page.css({'-webkit-backface-visibility':'','-moz-backface-visibility':'','backface-visibility':''});
			}};
			
			dimAnimationCSS['translateX'] =  dimDestinationX;
			
			page.velocity(pageAnimationCSS, pageAnimationOption);
		}
		else{ // push
			var dimZIndex = parseInt($(panel).css('z-index')) - 1;
            
			window.requestAnimFrame(function(){
				dimColor.css('left', '0px');
				dimColor.css('z-index', dimZIndex);
			});
            
			
			var panelAnimationCSS = {translateX:panelDestinationX};
			var panelAnimationOption = {duration:duration, complete:function(){isPanelAnimating = false;}};
			var pageAnimationCSS = {translateX : pageDestinationX};
			var pageAnimationOption = {duration:duration, complete:function(){
				page.css({'-webkit-backface-visibility':'',
				'-moz-backface-visibility':'',
				'backface-visibility':''});
			}};
            
			panel.velocity(panelAnimationCSS, panelAnimationOption);
			page.velocity(pageAnimationCSS, pageAnimationOption);
		}
	}
    
	dimColor.velocity(dimAnimationCSS, {duration:duration});
	
	panel.data('isShown', true);
	currentDisplayPanel = panel;
	disableScrolling();
	pauseCarousel(true);
}


function closePanel(panelId){
	var panel = $('#'+panelId);
	var page = $('.IUPage');
	var dimColor = $('#IUPanelDimColor');
	
	if (isPanelAnimating == true) {
		return ;
	}
	isPanelAnimating = true;
	
	var animation = panel.attr('JQueryShowAnimation');
	var direction = panel.attr('direction');
	var duration = parseFloat(panel.attr('JQueryShowDuration'))*1000;
    
	if (animation == 'overlay'){
		var panelAnimationCSS = {translateX:'0px'};
		var panelAnimationOption = {duration:duration, complete:function(){ 
																isPanelAnimating = false;
																enableScrolling();
																restartCarousel(true);
															}};
																
		var dimAnimationCSS = {opacity: 0};
        
		var dimAnimationOption = {duration:duration, complete:function(){
			dimColor.css('display', 'none');
			dimColor.removeClass($(panel).attr('id')+'DimColor');
		}};
        
		$(panel).velocity(panelAnimationCSS, panelAnimationOption);
		$('#IUPanelDimColor').velocity(dimAnimationCSS, dimAnimationOption);
        
	}
    
	else if (animation == 'reveal' || animation == 'push'){
		var panelOuterWidth = panel.outerWidth();
		page.css({'-webkit-backface-visibility':'hidden', '-moz-backface-visibility':'hidden', 'backface-visibility':'hidden'});
		
		if (animation == 'reveal'){
			var pageAnimationCSS = {translateX : '0px'};
			var pageAnimationOption = {duration:duration, complete:function(){
													$(panel).css('z-index', '');
													$(page).css('z-index', '');
													$(page).css({'-webkit-backface-visibility':'',
																'-moz-backface-visibility':'',
																'backface-visibility':''});
													isPanelAnimating = false;
									                enableScrolling();
													restartCarousel(true);
												}};
			var dimAnimationCSS = {translateX : '0px'};
			dimAnimationCSS['opacity'] = 0;
            
			var dimAnimationOption = {duration:duration, complete:function(){
				dimColor.css('display', 'none');
				dimColor.css('z-index', '');
				dimColor.removeClass($(panel).attr('id')+'DimColor');
			}};
            
			page.velocity(pageAnimationCSS, pageAnimationOption);
			dimColor.velocity(dimAnimationCSS, dimAnimationOption);
		}
		else{ // push
			var panelAnimationCSS = {translateX : '0px'};
			var panelAnimationOption = {duration:duration, complete:function(){
				isPanelAnimating = false;
			}};
            
			var pageAnimationCSS = {translateX : '0px'};
			var pageAnimationOption = {duration:duration, complete:function(){
							$(page).css({'-webkit-backface-visibility':'',
								  		'-moz-backface-visibility':'',
								  		'backface-visibility':''});
							enableScrolling();
							restartCarousel(true);
				}};
			
			var dimAnimationCSS = {opacity: 0};			
			var dimAnimationOption = {duration:duration, complete:function(){
				dimColor.css('display', 'none');
				dimColor.removeClass($(panel).attr('id')+'DimColor');
			}};
			
			panel.velocity(panelAnimationCSS, panelAnimationOption);
			page.velocity(pageAnimationCSS, pageAnimationOption);
			dimColor.velocity(dimAnimationCSS, dimAnimationOption);
		}
        
	}
    
	panel.data('isShown', false);
	currentDisplayPanel = undefined;
}

/**************************************
PGFlipSwitch
***************************************/

function initAllPGFlipSwitch(){
	$('.PGFlipSwitch').each(function(){
		if (!isMobile()){
			//$(this).css('cursor', 'pointer');
			this.style.cursor = 'pointer';
		}
		$(this).click(function(){
			togglePGFlipSwitch(this);
		});
		
		var checkbox = $(this).children()[0];
		var checkedAttr = $(checkbox).attr('checked');
		var offItem = $(this).find('.PGFlipOffItem')[0];
		var onItem = $(this).find('.PGFlipOnItem')[0];
		var flipSwitchButton = $(this).find('.PGFlipSwitchButton')[0];
		
		if (checkedAttr != undefined) {
			
			window.requestAnimFrame(function(){
				offItem.style.display = 'none';
			});
		}
		else {
			window.requestAnimFrame(function(){
				onItem.style.display = 'none';
			});
		}
	});
}

function togglePGFlipSwitch(flipSwitch){
	var checkbox = $(flipSwitch).children()[0];
	var checkedAttr = $(checkbox).attr('checked');
	var offItem = $(flipSwitch).find('.PGFlipOffItem')[0];
	var onItem = $(flipSwitch).find('.PGFlipOnItem')[0];
	var flipSwitchButton = $(flipSwitch).find('.PGFlipSwitchButton')[0];
	if ($(offItem).hasClass('velocity-animating') || $(onItem).hasClass('velocity-animating') || $(flipSwitchButton).hasClass('velocity-animating')) {
		return;
	}
    $(onItem).css('display', '');
    $(offItem).css('display', '');
    var itemWidth = onItem.offsetWidth > 0? onItem.offsetWidth : offItem.offsetWidth;
    
    var onItemWillMoveX = itemWidth;
	var onItemTranslateX = parseInt($(onItem).css('transform').split(',')[4]);
	onItemTranslateX = (isNaN(onItemTranslateX))? 0: onItemTranslateX;
	onItemWillMoveX = (onItemTranslateX == 0)? onItemWillMoveX : 0;
	
	var offItemWillMoveX = itemWidth;
	var offItemTranslateX = parseInt($(offItem).css('transform').split(',')[4]);
	offItemTranslateX = (isNaN(offItemTranslateX))? 0: offItemTranslateX;
    offItemWillMoveX = (offItemTranslateX == 0)? offItemWillMoveX : 0;
    
	var buttonDestX = flipSwitch.clientWidth - flipSwitchButton.offsetWidth - flipSwitchButton.offsetLeft;
	var buttonWillMoveX = buttonDestX - flipSwitchButton.offsetLeft;
	var buttonTranslateX = parseInt($(flipSwitchButton).css('transform').split(',')[4]);
	buttonTranslateX = (isNaN(buttonTranslateX))? 0: buttonTranslateX;
	buttonWillMoveX = (buttonTranslateX == 0)? buttonWillMoveX : 0;
	
	var duration = 300;
	if (checkedAttr == undefined) {
		$(checkbox).attr('checked', '');
		
		if (onItem.style.left == "" && onItem.style.transform == "") {
			$(onItem).css('left', onItem.offsetWidth * -1 + 'px');	
		}
		$(onItem).css('z-index', 1);
		$(offItem).css('z-index', '');
		$(flipSwitchButton).css('z-index', 2);
        
		$(flipSwitchButton).velocity({translateX: buttonWillMoveX + 'px'}, {duration: duration, begin:function(){reframeCenterIU(onItem)}});
		$(onItem).velocity({translateX: onItemWillMoveX +'px'}, {duration: duration,
			complete: function(){
				$(onItem).css('z-index', '');
				$(offItem).css('display', 'none');
			}
		});
		$(offItem).velocity({translateX: offItemWillMoveX + 'px'}, {duration: duration,
			complete: function(){
				$(flipSwitchButton).css('z-index', '');
			}
		});
	}
	else {
		onItemWillMoveX = -1 * onItemWillMoveX;
		offItemWillMoveX = -1 * offItemWillMoveX;
		$(checkbox).removeAttr('checked');
		
		if (offItem.style.right == "" && offItem.style.transform == "") {
			$(offItem).css('right', offItem.offsetWidth * -1 + 'px');	
		}
		$(offItem).css('z-index', 1);
		$(onItem).css('z-index', '');
		$(flipSwitchButton).css('z-index', 2);
		$(flipSwitchButton).velocity({translateX: buttonWillMoveX + 'px'}, {duration: duration, begin:function(){reframeCenterIU(offItem)}});
        
		$(offItem).velocity({translateX: offItemWillMoveX + 'px'}, {duration: duration,
			complete: function(){
				$(offItem).css('z-index', '');
				$(onItem).css('display', 'none');
			}
		});
		$(onItem).velocity({translateX: onItemWillMoveX + 'px'}, {duration: duration,
			complete: function(){
				$(flipSwitchButton).css('z-index', '');
			}
		});
	}
    
}

/**************************************
PGSlideBar
***************************************/

var clickedSlidePoint;

function initAllPGSlide(){
	$('.PGSlide').each(function(){
		initPGSlideWithHtmlId($(this).attr('id'));       
	});
}

function initPGSlideWithHtmlId(htmlId) {
	var slide = $('#' + htmlId)[0];
	var slidebar = $(slide).find('*').filter('.PGSlideBar')[0];
		var point = $(slide).find('*').filter('.PGSlidePoint')[0];
		var slideTextField = $(slide).children('.PGSlideTextField')[0];
		if (isMobile()) {
			// android에서 touch 할때 더블 탭 이벤트 때문에  touchend 후에 300milisec 의 delay가 생김, 그래서 preventDefault를 호출해야함
			$(point).bind({
				touchstart: function(event){
					clickedSlidePoint = this;
					event.preventDefault();
				},
				touchmove:function(event){
					clickedSlidePoint = this;
					changeSlideInnerBarWidthByMouseEvent(event);
					event.preventDefault();
				},
				touchend:function(event){
					clickedSlidePoint = undefined;
					event.preventDefault();
				},
				touchcancle:function(event){
					clickedSlidePoint = undefined;
					event.preventDefault();
				}
			});
                       
			$(slidebar).bind({
				touchstart: function(event){
					clickedSlidePoint = $(this).find('*').filter('.PGSlidePoint');
					changeSlideInnerBarWidthByMouseEvent(event);
					event.preventDefault();
				},
				touchmove:function(event){
					clickedSlidePoint = $(this).find('*').filter('.PGSlidePoint');
					changeSlideInnerBarWidthByMouseEvent(event);
					event.preventDefault();
				},
				touchend:function(event){
					clickedSlidePoint = undefined;
					event.preventDefault();
				},
				touchcancle:function(event){
					clickedSlidePoint = undefined;
					event.preventDefault();
				}
			});
		}
		else {
			$(point).css('cursor', 'pointer');
			changeSlideInnerBarWidthBySlideTextField(slideTextField);
                       
			$(point).bind({
				mousedown: function(event){
					if (event.which == 1) { // left mouse
						clickedSlidePoint = slide;
					}
				},
				mouseup:function(event){
					if (event.which == 1) { // left mouse
						clickedSlidePoint = undefined;
					}
				}
			});
                       
			$(slidebar).bind({
				mousedown: function(event){
					if (event.which == 1) { // left mouse
						clickedSlidePoint = $(this).find('*').filter('.PGSlidePoint');
						changeSlideInnerBarWidthByMouseEvent(event);
					}
				},
				mouseup:function(event){
					if (event.which == 1) { // left mouse
						clickedSlidePoint = undefined;
					}
				}
			});
		}
                       
		$(slideTextField).keyup(function(event){
			if (event.keyCode == 13) { //enter key
				var textFieldValue = $(slide).val();
				var checkValue = textFieldValue.search(/[^0-9]/i);
				if (checkValue < 0) {
					changeSlideInnerBarWidthBySlideTextField(this);
				}
			}
                                               
		});
}


function changeSlideInnerBarWidthBySlideTextField(slideTextField) {
	var slide = $(slideTextField).parent()[0];
	var slidebar = $(slide).children('.PGSlideBar')[0];
	var innerBar = $(slidebar).children('.PGSlideBar-innerBar')[0];
	var maxValue = parseInt($(slide).attr('maxvalue'));
	var minValue = parseInt($(slide).attr('minvalue'));
    
	var willChangeValue = parseInt($(slideTextField).val());
	if (willChangeValue < minValue){
		willChangeValue = minValue;
	}
	else if (willChangeValue > maxValue){
		willChangeValue = maxValue;
	}
	
	$(slide).children('input').val(willChangeValue);
	
	if (slidebar.offsetWidth == 0) { // display: none
		var willChangeInnerBarWidth = willChangeValue / (maxValue - minValue) * 100;
		$(innerBar).css('width', willChangeInnerBarWidth + '%'); 
	}
    else {
	   	var maxWidth = slidebar.clientWidth;
		var valueRange = maxValue - minValue;
		var xRange = slidebar.clientWidth;
		var willChangeInnerBarWidth = Math.round((xRange * willChangeValue) / valueRange + minValue);
		if (willChangeInnerBarWidth > maxWidth){
			willChangeInnerBarWidth = maxWidth;
		}
		if (isNaN(willChangeInnerBarWidth)) {
			willChangeInnerBarWidth = 0;
		}
		$(innerBar).css('width', willChangeInnerBarWidth + 'px'); 
    }
	
}

function changeSlideInnerBarWidthByMouseEvent(event) {
	// $(window).mousemove, $(slideBar).mousedown 에서 실행됨
	var slide = $(clickedSlidePoint).parent().parent().parent()[0];
	var slidebar = $(slide).children('.PGSlideBar')[0];
	var innerBar = $(slidebar).children('.PGSlideBar-innerBar')[0];
	var willChangeInnerBarWidth;
	if (isMobile()) {
		willChangeInnerBarWidth = event.changedTouches[0].pageX - $(slidebar).offset().left;
	}
	else {
		willChangeInnerBarWidth = event.pageX - $(slidebar).offset().left;
	}
	var maxWidth = slidebar.clientWidth;
	if (willChangeInnerBarWidth > maxWidth){
		willChangeInnerBarWidth = maxWidth;
	}
	if (isNaN(willChangeInnerBarWidth)) {
		willChangeInnerBarWidth = 0;
	}
    
	$(innerBar).css('width', willChangeInnerBarWidth +'px');
	var maxValue = parseInt($(slide).attr('maxvalue'));
	var minValue = parseInt($(slide).attr('minvalue'));
	var valueRange = maxValue - minValue;
	var xRange = slidebar.clientWidth;
	var value = Math.round((willChangeInnerBarWidth * valueRange) / xRange - minValue);
	if (value > maxValue) {
		value = maxValue;
	}
	else if (value < minValue) {
		value = minValue;
	}
	$(slide).children('.PGSlideTextField').val(value);
	$(slide).children('input').val(value);
}

function resetSlideInnerBarWidth(){
	$('.PGSlide').each(function(){
		var slideTextField = $(this).find('*').filter('.PGSlideTextField')[0];
		changeSlideInnerBarWidthBySlideTextField(slideTextField);
	});
}

/**************************************
PGRangeSlideBar
***************************************/

var clickedRangeSlidePointStart;
var clickedRangeSlidePointEnd;

function initAllPGRangeSlide(){
	$('.PGRangeSlide').each(function(){
		initPGRangeSlideWithHtmlId($(this).attr('id')); 
	});
}

function initPGRangeSlideWithHtmlId(htmlId) {
	var rangeSlide = $('#' + htmlId)[0];
	var rangeSlidebar = $(rangeSlide).find('*').filter('.PGRangeSlideBar')[0];
		var startPoint = $(rangeSlide).find('*').filter('.PGRangeSlidePointStart')[0];
		var endPoint = $(rangeSlide).find('*').filter('.PGRangeSlidePointEnd')[0];
		var startSlideTextField = $(rangeSlide).children('.PGRangeSlideTextFieldStart')[0];
		var endSlideTextField = $(rangeSlide).children('.PGRangeSlideTextFieldEnd')[0];
		if (isMobile()) {
			// android에서 touch 할때 더블 탭 이벤트 때문에  touchend 후에 300milisec 의 delay가 생김, 그래서 preventDefault를 호출해야함 
			$(startPoint).bind({
				touchstart: function(event){
					clickedRangeSlidePointStart = this;
					event.preventDefault();
				},
				touchmove:function(event){
					clickedRangeSlidePointStart = this;
					changeRangeSlideInnerBarWidthByStartPointMouseEvent(event);
					event.preventDefault();
				},
				touchend:function(event){
					clickedRangeSlidePointStart = undefined;
					event.preventDefault();  
				},
				touchcancle:function(event){
					clickedRangeSlidePointStart = undefined;
					event.preventDefault();
				}
			});
                            
			$(endPoint).bind({
				touchstart: function(event){
					clickedRangeSlidePointEnd = this;
					event.preventDefault();
				},
				touchmove:function(event){
					clickedRangeSlidePointEnd = this;
					changeRangeSlideInnerBarWidthByEndPointMouseEvent(event);
					event.preventDefault(); 
				},
				touchend:function(event){
					clickedRangeSlidePointEnd = undefined;
					event.preventDefault();    
				},
				touchcancle:function(event){
					clickedRangeSlidePointEnd = undefined;
					event.preventDefault();
				}
			});
                            
			$(rangeSlidebar).bind({
				touchstart: function(event){
					var innerBar = $(this).children('.PGRangeSlideBar-innerBar');
					var startPointOffsetX = $(innerBar).offset().left;
					var endPointOffsetX = $(innerBar).offset().left + $(innerBar).width();
					if (event.changedTouches[0].pageX < startPointOffsetX) {
						clickedRangeSlidePointStart = $(rangeSlide).find('*').filter('.PGRangeSlidePointStart');
						changeRangeSlideInnerBarWidthByStartPointMouseEvent(event);
					}
					else if (event.changedTouches[0].pageX > endPointOffsetX) {
						clickedRangeSlidePointEnd = $(rangeSlide).find('*').filter('.PGRangeSlidePointEnd');
						changeRangeSlideInnerBarWidthByEndPointMouseEvent(event);
					}
					event.preventDefault();
				},
				touchmove:function(event){
					if (clickedRangeSlidePointStart != undefined) {
						changeRangeSlideInnerBarWidthByStartPointMouseEvent(event);
					}
					else if (clickedRangeSlidePointEnd != undefined) {
						changeRangeSlideInnerBarWidthByEndPointMouseEvent(event);
					}
					event.preventDefault();
				},
				touchend:function(event){
					clickedRangeSlidePointStart = undefined;
					clickedRangeSlidePointEnd = undefined;
					event.preventDefault();
				},
				touchcancle:function(event){
					clickedRangeSlidePointStart = undefined;
					clickedRangeSlidePointEnd = undefined;
					event.preventDefault();
				}
			});
		}
		else {
			$(startPoint).css('cursor', 'pointer');
			$(startPoint).bind({
				mousedown: function(event){
					if (event.which == 1) { // left mouse
						clickedRangeSlidePointStart = this;
					}
				},
				mouseup:function(event){
					if (event.which == 1) { // left mouse
						clickedRangeSlidePointStart = undefined;    
					}
				}
			});
			$(endPoint).css('cursor', 'pointer');
			$(endPoint).bind({
				mousedown: function(event){
					if (event.which == 1) { // left mouse
						clickedRangeSlidePointEnd = this;
					}
				},
				mouseup:function(event){
					if (event.which == 1) { // left mouse
						clickedRangeSlidePointEnd = undefined;    
					}
				}
			});
                            
			$(rangeSlidebar).bind({
				mousedown: function(event){
					if (event.which == 1) { // left mouse
						var innerBar = $(this).children('.PGRangeSlideBar-innerBar');
						var startPointOffsetX = $(innerBar).offset().left;
						var endPointOffsetX = $(innerBar).offset().left + $(innerBar).width();
						if (event.pageX < startPointOffsetX) {
							clickedRangeSlidePointStart = $(rangeSlide).find('*').filter('.PGRangeSlidePointStart');
							changeRangeSlideInnerBarWidthByStartPointMouseEvent(event);
						}
						else if (event.pageX > endPointOffsetX) {
							clickedRangeSlidePointEnd = $(rangeSlide).find('*').filter('.PGRangeSlidePointEnd');
							changeRangeSlideInnerBarWidthByEndPointMouseEvent(event);
						}
					}
				},
				mouseup:function(event){
					if (event.which == 1) { // left mouse
						clickedRangeSlidePointStart = undefined;
						clickedRangeSlidePointEnd = undefined;     
					}
				}
			});
		}
                            
                            
                            
		$(startSlideTextField).keyup(function(event){
			if (event.keyCode == 13) { //enter key
				var textFieldValue = $(rangeSlide).val();
				var checkValue = textFieldValue.search(/[^0-9]/i);
				if (checkValue < 0) {
					changeRangeSlideInnerBarWidthByRangeSlideTextFieldStart(this);
				}
			}
                                                         
		});
                            
		$(endSlideTextField).keyup(function(event){
			if (event.keyCode == 13) { //enter key
				var textFieldValue = $(rangeSlide).val();
				var checkValue = textFieldValue.search(/[^0-9]/i);
				if (checkValue < 0) {
					changeRangeSlideInnerBarWidthByRangeSlideTextFieldEnd(this);
				}
			}
                                                       
		});
}

function changeRangeSlideInnerBarWidthByStartPointMouseEvent(event) {
	// $(window).mousemove , $(rangeSlideBar).mousedown에서 실행됨
	// innerBar left를 마우스가 이동한 만큼 증가(감소)시키고 , width는 left가 이동한 만큼 반대로 감소(증가)시킨다.
	var rangeSlide = $(clickedRangeSlidePointStart).parent().parent().parent()[0];
	var rangeSlideBar = $(rangeSlide).children('.PGRangeSlideBar')[0];
	var innerBar = $(rangeSlideBar).children('.PGRangeSlideBar-innerBar')[0];
	var innerBarLeft = $(innerBar).offset().left - $(rangeSlideBar).offset().left;
	var changeInnerBarLeft;
	if (isMobile()) {
		changeInnerBarLeft = (event.changedTouches[0].pageX - $(innerBar).offset().left); 	
	}
	else {
		changeInnerBarLeft = (event.pageX - $(innerBar).offset().left);
	}
    
	if (changeInnerBarLeft + innerBarLeft <= 0) {
		changeInnerBarLeft = -1 * innerBarLeft;
	}
    
	if (changeInnerBarLeft > $(innerBar).width()) {
		changeInnerBarLeft = $(innerBar).width();
	}
	var willChangeInnerBarLeft = innerBarLeft + changeInnerBarLeft;
	var maxLeft = $(rangeSlideBar).width();
	if (willChangeInnerBarLeft > maxLeft){
		willChangeInnerBarLeft = maxLeft;
	}
	else if (willChangeInnerBarLeft < 0) {
		willChangeInnerBarLeft = 0;
	}
	if (isNaN(willChangeInnerBarLeft)) {
		willChangeInnerBarLeft = 0;
	}
	$(innerBar).css('left', willChangeInnerBarLeft +'px');
    
	var willChangeInnerBarWidth = $(innerBar).width() - changeInnerBarLeft;
	if (willChangeInnerBarWidth > maxLeft){
		willChangeInnerBarWidth = maxLeft;
	}
	else if (willChangeInnerBarWidth < 0) {
		willChangeInnerBarWidth = 0;
	}
	$(innerBar).css('width', willChangeInnerBarWidth +'px');
    
	var maxValue = parseInt($(rangeSlide).attr('maxvalue'));
	var minValue = parseInt($(rangeSlide).attr('minvalue'));
	var valueRange = maxValue - minValue;
	var xRange = $(rangeSlideBar).width();
	var value = Math.round((willChangeInnerBarLeft * valueRange) / xRange - minValue);
	if (value > maxValue) {
		value = maxValue;
	}
	else if (value < minValue) {
		value = minValue;
	}
	$(rangeSlide).children('.PGRangeSlideTextFieldStart').val(value);
	$(rangeSlide).children('.PGRangeSlideInputStart').val(value);
}

function changeRangeSlideInnerBarWidthByEndPointMouseEvent(event) {
	// $(window).mousemove,  $(rangeSlideBar).mousedown에서 실행됨
	// innerBar width를 마우스가 이동한 만큼 증가(감소)시키고, left는 그대로 둔다.
	var rangeSlide = $(clickedRangeSlidePointEnd).parent().parent().parent()[0];
	var rangeSlideBar = $(rangeSlide).children('.PGRangeSlideBar')[0];
	var innerBar = $(rangeSlideBar).children('.PGRangeSlideBar-innerBar')[0];
	var innerBarLeft = $(innerBar).offset().left - $(rangeSlideBar).offset().left;
	var maxWidth = $(rangeSlideBar).width() - innerBarLeft;
    
	var willChangeInnerBarWidth;
	if (isMobile()) {
		willChangeInnerBarWidth = (event.changedTouches[0].pageX - $(innerBar).offset().left); 	
	}
	else {
		willChangeInnerBarWidth = (event.pageX - $(innerBar).offset().left);
	}
    
	if (willChangeInnerBarWidth > maxWidth){
		willChangeInnerBarWidth = maxWidth;
	}
	else if (willChangeInnerBarWidth < 0) {
		willChangeInnerBarWidth = 0;
	}
	$(innerBar).css('width', willChangeInnerBarWidth +'px');
    
	var maxValue = parseInt($(rangeSlide).attr('maxvalue'));
	var minValue = parseInt($(rangeSlide).attr('minvalue'));
	var valueRange = maxValue - minValue;
	var xRange = $(rangeSlideBar).width();
	var value = Math.round(((willChangeInnerBarWidth + innerBarLeft) * valueRange) / xRange - minValue);
	if (value > maxValue) {
		value = maxValue;
	}
	else if (value < minValue) {
		value = minValue;
	}
	$(rangeSlide).children('.PGRangeSlideTextFieldEnd').val(value);
	$(rangeSlide).children('.PGRangeSlideInputEnd').val(value);
}

function changeRangeSlideInnerBarWidthByRangeSlideTextFieldStart(rangeSlideTextFieldStart) {
	var rangeSlide = $(rangeSlideTextFieldStart).parent()[0];
	var rangeSlideBar = $(rangeSlide).children('.PGRangeSlideBar')[0];
	var innerBar = $(rangeSlideBar).children('.PGRangeSlideBar-innerBar')[0];
	var maxValue = parseInt($(rangeSlide).attr('maxvalue'));
	var minValue = parseInt($(rangeSlide).attr('minvalue'));
	var endSlideTextField = $(rangeSlide).children('.PGRangeSlideTextFieldEnd')[0];
	var endValue = parseInt($(endSlideTextField).val());
    
	var willChangeValue = parseInt($(rangeSlideTextFieldStart).val());
	if (willChangeValue < minValue){
		return;
	}
	else if (willChangeValue >= maxValue || willChangeValue >= endValue){
		return;
	}
	$(rangeSlide).children('.PGRangeSlideInputStart').val(willChangeValue);
	
	if (rangeSlideBar.offsetWidth == 0) {
		var willChangeInnerBarLeft = startValue / (maxValue - minValue) * 100;
		$(innerBar).css('width', willChangeInnerBarWidth + '%');
	}
	else {
		var maxLeft = $(rangeSlideBar).width();
		var valueRange = maxValue - minValue;
		var willChangeInnerBarLeft = Math.round((maxLeft * willChangeValue) / valueRange + minValue);
		if (willChangeInnerBarLeft > maxLeft){
			willChangeInnerBarLeft = maxLeft;
		}
		if (isNaN(willChangeInnerBarLeft)) {
			willChangeInnerBarLeft = 0;
		}
	    
		var innerBarWidth = $(innerBar).width();
		var innerBarLeft = $(innerBar).offset().left - $(rangeSlideBar).offset().left;
		$(innerBar).css('left', willChangeInnerBarLeft + 'px');
		var willChangeInnerBarWidth = innerBarWidth - (willChangeInnerBarLeft - innerBarLeft);
	    
		var maxWidth = $(rangeSlideBar).width();
		if (willChangeInnerBarWidth > maxWidth){
			willChangeInnerBarWidth = maxWidth;
		}
		if (isNaN(willChangeInnerBarWidth)) {
			willChangeInnerBarWidth = 0;
		}
	    
		$(innerBar).css('width', willChangeInnerBarWidth + 'px');
	}
    
	
}

function changeRangeSlideInnerBarWidthByRangeSlideTextFieldEnd(rangeSlideTextFieldEnd) {
	var rangeSlide = $(rangeSlideTextFieldEnd).parent()[0];
	var rangeSlideBar = $(rangeSlide).children('.PGRangeSlideBar')[0];
	var innerBar = $(rangeSlideBar).children('.PGRangeSlideBar-innerBar')[0];
	var maxValue = parseInt($(rangeSlide).attr('maxvalue'));
	var minValue = parseInt($(rangeSlide).attr('minvalue'));
	var startSlideTextField = $(rangeSlide).children('.PGRangeSlideTextFieldStart')[0];
	var startValue = parseInt($(startSlideTextField).val());
    
	var willChangeValue = parseInt($(rangeSlideTextFieldEnd).val());
	if (willChangeValue <= minValue || willChangeValue <= startValue){
		return;
	}
	else if (willChangeValue > maxValue){
		return;
	}
	$(rangeSlide).children('.PGRangeSlideInputEnd').val(willChangeValue);
	
	if (rangeSlideBar.offsetWidth == 0) {
		var willChangeInnerBarWidth = (endValue - startValue) / (maxValue - minValue) * 100;
		$(innerBar).css('width', willChangeInnerBarWidth + '%');
	}
	else {
		var maxWidth = $(rangeSlideBar).width();
		var valueRange = maxValue - minValue;
		var innerBarLeft = $(innerBar).offset().left - $(rangeSlideBar).offset().left;
		var willChangeInnerBarWidth = Math.round((maxWidth * willChangeValue) / valueRange + minValue) - innerBarLeft;
		if (willChangeInnerBarWidth > maxWidth){
			willChangeInnerBarWidth = maxWidth;
		}
		if (isNaN(willChangeInnerBarWidth)) {
			willChangeInnerBarWidth = 0;
		}
		$(innerBar).css('width', willChangeInnerBarWidth + 'px');
	}
    
	
}

