$( document ).ready(function(){
	m_header_init();
    m_appdown_init();
    
    var inputQuantity = [];
    
	$("#phone").each(function(i) {
	     inputQuantity[i]=this.defaultValue;
	     $(this).data("idx",i); // save this field's index to access later
	});
	
	$("#phone").on("keyup", function (e) {
		var $field = $(this),
			val=this.value,
			$thisIndex=parseInt($field.data("idx"),10); // retrieve the index
		if (this.validity && this.validity.badInput || isNaN(val)  ) {
			this.value = inputQuantity[$thisIndex];
			return;
		}
		if (val.length > Number($field.attr("maxlength"))) {
			val=val.slice(0, 11);
			$field.val(val);
		}
		inputQuantity[$thisIndex]=val;
	});      

    $('.trace-btn').click(function(){
    	var phone = $('#phone').val();
    	if(phone.length == 11){
	    	$('.modal').modal({
				ready: getOrderInfo(phone)
	    	});
    	} else {
    		alert("핸드폰 번호를 다시 확인해주세요.");
    	}
    })
});	


function getOrderInfo(phone) {
	$.ajax({                  
	    type: "GET",
	    url: "../member/order/recent/phone/" + phone,
	    dataType: "json",
	    success: function(resData) { 
	      if (resData.constant == 1){ // 1 = Success 
	      	var orderData = $.parseJSON(resData.data),
	      		state = orderData[0].state;

	      	var wrapper = $('#modal-trace'),
	      		subtitle = wrapper.find('.subtitle'),
		  		address = wrapper.find('[data-field="address"]'),
		    	building = wrapper.find('[data-field="building"]'),
		    	pickup = wrapper.find('[data-field="pickup"]'),
		    	dropoff = wrapper.find('[data-field="dropoff"]');
	    	
	    	address.html(orderData[0].address);
	    	building.html(orderData[0].addr_building);
      		pickup.html(orderData[0].pickup_date.substring(0, 16));
      		dropoff.html(orderData[0].dropoff_date.substring(0, 16));
	      	
	      	$('.circle div').removeClass('inner-circle');

	      	if (state == 0){ // 주문 완료
		      	subtitle.html('주문이<br>접수되었습니다.');
	      	} else if (state == 1){ // 수거 배정 
		      	subtitle.html(orderData[0].pickupInfo.name + ' 크린파트너가<br>방문 드릴 예정입니다.');
	      		$('.circle div').eq(0).addClass('inner-circle');
	      	} else if (state == 2){ // 수거 완료 
		      	subtitle.html('정상적으로 수거가<br>완료되었습니다.');
	      		$('.circle div').eq(1).addClass('inner-circle');
	      	} else if (state == 3){ // 배달 배정 
		      	subtitle.html(orderData[0].dropoffInfo.name + ' 크린파트너가<br>방문 드릴 예정입니다.');
	      		$('.circle div').eq(2).addClass('inner-circle');
	      	} else if (state == 4){ // 배달 완료 
		      	subtitle.html('정상적으로 배달이<br>완료되었습니다.');
	      		$('.circle div').eq(3).addClass('inner-circle');
	      	}
	      }
	    },
	    error: function(request,status,error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
	});
}