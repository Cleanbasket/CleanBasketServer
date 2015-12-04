timerDictionary = {};

function reloadCarousel(carouselID){
	destroyCarousel(carouselID);
	initCarousel(carouselID);
}

function initCarousel(carouselID){
	var carousel = $('#'+carouselID);
	var pager = $('#CenterBox_12');
	var wrapper = carousel.find('.IUCarousel-wrapper');
	var count = $(wrapper).children().length;
    
	//copy first & last Obj
	var firstObj = $($(wrapper).children()[0]).clone(true);
	firstObj.find('*').each(function(){
		var currentId = this.id;
		$(this).attr('id', 'carousel_copy_'+currentId);
	});
	var currentId = firstObj[0].id;
	firstObj.attr('id', 'carousel_copy_'+currentId);	
	firstObj.addClass('carousel_copy');
	
	var lastObj = $($(wrapper).children()[count-1]).clone(true);
	lastObj.find('*').each(function(){
		var currentId = this.id;
		$(this).attr('id', 'carousel_copy_'+currentId);
	});
	currentId = lastObj[0].id;
	lastObj.attr('id', 'carousel_copy_'+currentId);	
	lastObj.addClass('carousel_copy');
    
	//insert copy objects
	$(firstObj).insertAfter($($(wrapper).children()[count-1]));
	$(lastObj).insertBefore($($(wrapper).children()[0]));
    
	//set position
	var width = 100 * (count+2);
	window.requestAnimFrame(function(){
		$(wrapper)[0].style.width = width +'%';
	});
    
	var childrenWidth = 100 / (count+2);
	$(wrapper).children().each(function(){
		var child = $(this)[0];
		window.requestAnimFrame(function(){
        	child.style.width = childrenWidth + '%';
		});
                               
	});
    
	carousel.data('index', 1);
	var left =  100 * -1;
    $(wrapper)[0].style.left = left +'%';
    
	//click binding
	carousel.find('.IUCarousel-Prev').click(function(){
		prevCarousel(carouselID);
	});
    
	carousel.find('.IUCarousel-Next').click(function(){
		nextCarousel(carouselID);
	});
    
	/* caoursel item binding */
	carousel.find('.IUCarouselItem')
	.on('movestart', function(e) {
		// If the movestart is heading off in an upwards or downwards
		// direction, prevent it so that the browser scrolls normally.
		if ((e.distX > e.distY && e.distX < -e.distY) ||
		(e.distX < e.distY && e.distX > -e.distY)) {
			e.preventDefault();
		}
		pauseCarousel(false);
	})
	.on('move', function(e) {
		// Move slides with the finger
		moveXCarouselItem(carouselID, e.distX);
	})
	.on('moveend', function(e) {
		endMoveCarouselItem(carouselID, e.distX);
		restartCarousel(false);
	});
    
	pager.find('.IUCarousel-Pager >li').each(function(index){
		$(this).click(function(){
			moveCarousel(carouselID, index+1);
		})
	});
    
	//timer
	var timer = carousel.attr('timer');
	if(timer != undefined){
		var time = parseInt(timer);
		if(time < 1000){
			time = 1000;
		}
		timerDictionary[carouselID] = window.setInterval(function(){
			nextCarousel(carouselID);
		}, time);
	}
    
	activeCarousel(carouselID);
}

function resizeCarousel(){
	$('.IUCarousel').each(function(){
		//image lazy load check
		var carousel = $(this);
		var wrapper = carousel.find('.IUCarousel-wrapper');
		var index = carousel.data('index');
		var height = $($(wrapper).children()[index]).height();
		var width = $(wrapper).children()[index].offsetWidth;
		$(wrapper).velocity({translateX:width * -1 * (index - 1) + 'px'}, {duration:0});
		
		window.requestAnimFrame(function(){
        	carousel[0].style.height = height + 'px';
		});
                          
	});
    
}

function pauseCarousel(reset) {
	var timerIdDict = timerDictionary;
	$('.IUCarousel').each(function(){
		var carouselID = this.id
		if (timerIdDict[carouselID] != undefined) {
			window.clearInterval(timerIdDict[carouselID]);
			timerIdDict[carouselID] = undefined;
		}
		if (reset) {
			rePositionCarousel(carouselID);	
		}
	});
}

function restartCarousel(reset) {
	//timer
	$('.IUCarousel').each(function(){
		var carouselID = this.id;
		var timer = $(this).attr('timer');
		if(timer != undefined){
			var time = parseInt(timer);
			if(time < 1000){
				time = 1000;
			}
			timerDictionary[carouselID] = window.setInterval(function(){
				nextCarousel(carouselID);
			}, time);
		}
		if (reset) {
			if (timer != undefined) {
				nextCarousel(carouselID);	
			}
			else {
				currentCarousel(carouselID);	
			}
		}
		else {
			activeCarousel(carouselID);
		}
	});
	
}

function destroyCarousel(carouselID){
	var carousel = $('#'+carouselID);
	var wrapper = carousel.find('.IUCarousel-wrapper');
	var copyObjects = $(wrapper).find('.carousel_copy');
	copyObjects.each(function(){
		$(this).remove();
	})
}

function nextCarousel(carouselID){
	var carousel = $('#'+carouselID);
	var wrapper = carousel.find('.IUCarousel-wrapper');
	var index = carousel.data('index')+1;
	if(index >= $(wrapper).children().length){
		index = 1;
	}
	moveCarousel(carouselID, index);
}

function currentCarousel(carouselID){
	var carousel = $('#'+carouselID);
	var wrapper = carousel.find('.IUCarousel-wrapper');
	var index = carousel.data('index');
	if(index >= $(wrapper).children().length){
		index = 1;
	}
	moveCarousel(carouselID, index);
}

function rePositionCarousel(carouselID){
	var carousel = $('#'+carouselID);
	var wrapper = carousel.find('.IUCarousel-wrapper');
	var index = carousel.data('index');
	if(index >= $(wrapper).children().length){
		index = 1;
	}
	moveCarousel(carouselID, index, true);
}

function prevCarousel(carouselID){
	var carousel = $('#'+carouselID);
	var wrapper = carousel.find('.IUCarousel-wrapper');
	var index = carousel.data('index')-1;
	if(index < 0){
		index = $(wrapper).children().length-2;
	}
	moveCarousel(carouselID, index);
}
function moveCarousel(carouselID, toIndex, reset){
	var carousel = $('#'+carouselID);
	var index = carousel.data('index');
    
    // moveXCarouselItem 이후 다시 현재 아이템으로 이동해야하므로 index 와 toIndex가 같아도 return 하지 않음
	/*
	if (index == toIndex){
		return;
	}
	*/
	var wrapper = carousel.find('.IUCarousel-wrapper')[0];
	var count = $(wrapper).children().length;
	var carouselWidth = carousel[0].clientWidth;
	if(index==0){
		var x = carouselWidth * -1 * (count-2);
		$.Velocity.hook($(wrapper), 'translateX', x + 'px');
	}
	else if(index==count-1){
		var x = carouselWidth * -1;
		$.Velocity.hook($(wrapper), 'translateX', x + 'px');
	}
    
	var moveLeft = carouselWidth * -1 * (toIndex - 1);
	carousel.data('index', toIndex);
    var duration = reset? 0 : 400;
	$(wrapper).velocity({translateX: moveLeft+'px'}, {duration:duration, complete: function(){
		if (toIndex == 0) {
			carousel.data('index', count-2);
			$.Velocity.hook($(wrapper), 'translateX', (-1 * (count-3) * carouselWidth)  + 'px');
		}
		else if(toIndex == count-1){
			carousel.data('index', 1);
			$.Velocity.hook($(wrapper), 'translateX', '0px');
		}
                       
		var height = $($(this).children()[toIndex]).height();
		$.Velocity.hook($(wrapper), 'height', height+'px');
		
		reframeCenterIU('#'+ carouselID);
		
	}});
	activeCarousel(carouselID);
    
}

function moveXCarouselItem(carouselID, distX){
    
	var carousel = $('#'+carouselID);
	var carouselWidth = carousel[0].clientWidth;
	var wrapper = carousel.find('.IUCarousel-wrapper')[0];
    
    
    ///////////////// 이전 코드
	if ($(wrapper).hasClass('transition') == false){
		var currentLeft = wrapper.offsetLeft;
		$(wrapper).addClass('transition');	
		carousel.data('startLeft', currentLeft);
	}
								/////////////////
    
    
	var preDistX = carousel.data('prevDistX');
    if (preDistX == undefined){
	    preDistX = 0;
    }
    
	var prevTranslateX = parseInt($(wrapper).css('transform').split(',')[4]);

	if (Math.abs(distX) > carouselWidth) {
		distX = distX < 0? carouselWidth * -1 : carouselWidth;
	}
	var changedX = distX - preDistX;
	var currentTranslateX = prevTranslateX + changedX;
	$.Velocity.hook(wrapper, "translateX", currentTranslateX + 'px');
	carousel.data('prevDistX', distX);
}

function endMoveCarouselItem(carouselID, distX){
	var carousel = $('#'+carouselID);
	var carouselWidth = carousel[0].offsetWidth;
	var wrapper = carousel.find('.IUCarousel-wrapper')[0];
	if ($(wrapper).hasClass('transition') == false){
		return;
	}
	$(wrapper).removeClass('transition');
    if (distX < 0 && Math.abs(distX) > carouselWidth/2){
	    nextCarousel(carouselID);
    }
    else if (distX > 0 && Math.abs(distX) > carouselWidth/2){
	    prevCarousel(carouselID);
    }
    else {
	    currentCarousel(carouselID);
    }
    carousel.data('prevDistX', 0);
}


function activeCarousel(carouselID){
	var carousel = $('#'+carouselID);
	var pager = $('#CenterBox_12');
	var wrapper = carousel.find('.IUCarousel-wrapper');
	var count = $(wrapper).children().length;
	var index = carousel.data('index');
    
	//select li class active
	var selectIndex = index -1;
	if(index == count-1){
		selectIndex = 0;
	}
	else if(index == 0){
		selectIndex = count-3;
	}
	pager.find('.IUCarousel-Pager').children().each(function(i){
		if(i == selectIndex){
			$(this).addClass('active');
		}
		else{
			$(this).removeClass('active');
		}
	});
    
}

