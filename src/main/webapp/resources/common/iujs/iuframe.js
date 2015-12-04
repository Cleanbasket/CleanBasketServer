/**************************************
Common functions
***************************************/

// shim layer with setTimeout fallback
window.requestAnimFrame = (function(){
  return  window.requestAnimationFrame       ||
          window.webkitRequestAnimationFrame ||
          window.mozRequestAnimationFrame    ||
          function( callback ){
            window.setTimeout(callback, 1000 / 60);
          };
})();

/**************************************
View Port
***************************************/

function isElementInViewport (el) {
    
	//special bonus for those using jQuery
	if (typeof jQuery === "function" && el instanceof jQuery) {
		el = el[0];
	}
    
	var rect = el.getBoundingClientRect();
    
	return (
		rect.top >= 0 &&
		rect.left >= 0 &&
		rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && /*or $(window).height() */
		rect.right <= (window.innerWidth || document.documentElement.clientWidth) /*or $(window).width() */
	);
}

function isElementIntersectViewport (el) {
	//special bonus for those using jQuery
	if (typeof jQuery === "function" && el instanceof jQuery) {
		el = el[0];
	}
    
	var rect = el.getBoundingClientRect();
    
	return (
		rect.top >= 0 &&
		rect.left >= 0 &&
		(rect.top <= (window.innerHeight || document.documentElement.clientHeight) || /*or $(window).height() */
		rect.left <= (window.innerWidth || document.documentElement.clientWidth) /*or $(window).width() */)
	);
}

function getCurrentData(mqDict){
	var viewportWidth = window.innerWidth;
	var minWidth = 10000;
	var currentData;
	for (var key in mqDict){
		var width = parseInt(key);
        
		if (viewportWidth <= width && minWidth > width){
			currentData = mqDict[key];
			minWidth = width;
		}
	}
	return currentData;
}


/**************************************
Resize
***************************************/


function makefullSizeSection(){
	var respc = $('[enableFullSize="1"]').toArray();
	var windowHeight = window.innerHeight;
	
	$.each(respc, function(){
		var section = $(this)[0];
		window.requestAnimFrame(function(){
			section.style.height = windowHeight+'px';
		});
	});
}

function resizeCollection(){
	$('.IUCollection').each(function(){
		//find current count
		var responsive = $(this).attr('responsive');
		responsiveArray = eval(responsive);
		count = $(this).attr('defaultItemCount');
		viewportWidth = window.width;
		var minWidth = 9999;
		for (var index in responsiveArray){
			dict = responsiveArray[index];
			width = dict.width;
			if (viewportWidth<width && minWidth > width){
				count = dict.count;
				minWidth = width;
			}
		}
		var width = $(this).width()/count;
		$(this).children().css('width', width.toFixed(0)+'px');
	});
}


/**************************************
Reset Origin
***************************************/

function reframeCenterIU(iu){

	console.timeStart("reframeCenterIU");
	var ius = [];
	if($(iu).attr('horizontalcenter')=='1' || $(iu).attr('lazyhorizontalcenter')=='1'){
		ius.push($(iu));
	}
	ius = $.merge(ius, $(iu).find('[horizontalcenter="1"]').toArray());
	ius = $.merge(ius, $(iu).find('[lazyhorizontalcenter="1"]').toArray());

	if($(iu).attr('verticalcenter')=='1' || $(iu).attr('lazyverticalcenter')=='1'){
		ius.push($(iu));
	}
	ius = $.merge(ius, $(iu).find('[verticalcenter="1"]').toArray());
	ius = $.merge(ius, $(iu).find('[lazyverticalcenter="1"]').toArray());
	    
	$(ius).each(function(){
		calculateCenterPosition(this);
	});
	
	applyCenter(ius);
	console.timeEnd("reframeCenterIU");
}


function calculateCenterPosition(iu){
	if (iu instanceof jQuery) {
		iu = iu[0];
	}
	var pos = $(iu).css('position');
	
	if (pos != 'fixed' && iu.offsetParent == undefined) {
		return;
	}
	
	var id  = iu.id;
	var isHorizontal = iu.hasAttribute('horizontalCenter') || iu.hasAttribute('lazyHorizontalCenter');
	var isVertical = iu.hasAttribute('verticalCenter') || iu.hasAttribute('lazyVerticalCenter');
	var parentSize = iu.offsetParent == undefined ? {'width':window.innerWidth, 'height':window.innerHeight} :  {'width':iu.offsetParent.clientWidth, 'height' : iu.offsetParent.clientHeight};
	
	
	
	if (pos == 'absolute' || pos == 'fixed'){

		var hCenter = (parentSize.width-iu.offsetWidth)/2;
		var vCenter = (parentSize.height-iu.offsetHeight)/2;
		
		if ( pos == 'absolute' && $(iu).hasClass('IUScrollAnimator')) {
			var propertyKeys = getScrollAnimatorPropertyKeys(iu);
			
			var keyIndexForX = jQuery.inArray('scrollAnimatorX', propertyKeys);
			var keyIndexForY = jQuery.inArray('scrollAnimatorY', propertyKeys);
			
			if (keyIndexForX > -1 || keyIndexForY > -1) {
				if (isHorizontal && keyIndexForX > -1) {
					setPostHCenterForScrollAnimatorX(iu, hCenter);
				}
				
				if (isVertical && keyIndexForY > -1){
					setPostVCenterForScrollAnimatorY(iu, vCenter);
					setPostCenterYForScrollAnimator(iu, vCenter + $(iu).outerHeight()/2);
				}
			}
			else { // only opacity
				var frame = {};
				if ( isHorizontal ){
					frame['left'] = hCenter + 'px';
				}
				if ( isVertical ){
					frame['top'] = vCenter +'px';
				}
				$(iu).data('centerPosition', frame);
			}
		}
		else {
			var frame = {};
			if ( isHorizontal ){
				frame['left'] = hCenter + 'px';
			}
			if ( isVertical ){
				frame['top'] = vCenter +'px';
			}
			$(iu).data('centerPosition', frame);

		}
	}
	else {
		var frame = {};
		if ( isHorizontal ){
			frame['margin-left'] = 'auto';
			frame['margin-right'] = 'auto';
			frame['left'] = '';
		}
		if ( isVertical ){
			frame['margin-top'] = 'auto';
			frame['margin-bottom'] = 'auto';
			frame['top'] = '';
			
		}
		$(iu).data('centerPosition', frame);
	}
}

function calculateCenter(){
	console.timeStart("caculateCenter");
	var horizontalCenterIUs = $('[horizontalCenter="1"]').toArray();
	var verticalCenterIUs = $('[verticalCenter="1"]').toArray();
    
	var centerIUs = horizontalCenterIUs.concat(verticalCenterIUs);
    
	$(centerIUs).each(function(){
		calculateCenterPosition(this);
	});
    
	console.timeEnd("calculateCenter");
    return centerIUs;
}

function calculateLazyCenter(){
	console.timeStart("calculateLazyCenter");
	var horizontalCenterIUs = $('[lazyhorizontalCenter="1"]').toArray();
	var verticalCenterIUs = $('[lazyverticalCenter="1"]').toArray();
    
	var centerIUs = horizontalCenterIUs.concat(verticalCenterIUs);
    
	$(centerIUs).each(function(){
		calculateCenterPosition(this);
	});
    
	console.timeEnd("calculateLazyCenter");
    return centerIUs;
}



function reframeCenter() {
	var centerIUs = calculateCenter();
	applyCenter(centerIUs);
}

function lazyReframeCenter(){
	console.timeStart("lazyReframeCenter");
	var centerIUs = calculateLazyCenter();
	applyCenter(centerIUs);
	console.timeEnd("lazyReframeCenter");
}


function setFrameForIU(frame, iu){
	if (frame == undefined) {
		return;
	}
	
	window.requestAnimFrame(function(){
		if (frame["top"] != undefined) {
			iu.style.top = frame["top"];
		}
		if (frame["left"] != undefined) {
			iu.style.left = frame["left"];
		}
		if (frame["margin-left"] != undefined) {
			iu.style.marginLeft = frame["margin-left"];
		}
		if (frame["margin-right"] != undefined) {
			iu.style.marginRight = frame["margin-right"];
		}
		if (frame["margin-top"] != undefined) {
			iu.style.marginTop = frame["margin-top"];
		}
		if (frame["margin-bottom"] != undefined) {
			iu.style.marginBottom = frame["margin-bottom"];
		}
	});
	
}

function applyCenter(ius) {
	for (var i=0; i<ius.length; i++) {
		setFrameForIU($(ius[i]).data('centerPosition'), ius[i]);
	}
}


