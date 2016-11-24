$(document).ready(function () {
  init();

  /***** 휴대폰 번호 무조건 11자리 숫자로만 *****/
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
  /****************************************/ 
})

function init() {
  $('.alert-notyetopen').click(function (e) {
    e.preventDefault();
    alert('서비스 준비중입니다. 빠른 시일내로 찾아뵙겠습니다.');
  });

  $('#ask-form').submit(function (e) {
  	e.preventDefault();
    if(checkForm()){
        ajaxPostAskData();
    }   
  });
}

function checkForm() {
    var ask_form = document.ask;

    if (ask_form.title.value == ""){
        alert("제목을 입력해주세요.");
        return false;
    } else if (ask_form.content.value == ""){
        alert("내용을 입력해주세요.");
        return false;
    } else if (ask_form.name.value == ""){
        alert("이름을 입력해주세요.");
        return false;
    } else if (ask_form.phone.value == ""){
        alert("휴대폰 번호를 입력해주세요.");
        return false;
    } else if (ask_form.phone.value.length !== 11){
        alert("핸드폰 번호를 다시 확인해주세요.(11자리 숫자)");
        return false;
    } else if (ask_form.email.value == ""){
        alert("이메일 주소를 입력해주세요.");
        return false;
    } else {
      return true;
    }
}

function ajaxPostAskData(){
    var ask_form = document.ask,
        obj = new Object(),
        questionType = $("input[name='question-type']:checked").val(),
        answerType = $("input[name='get-answer']:checked").val();
    	
    obj.body = "[" + questionType + "] " + ask_form.title.value;
    obj.connectColor = "#FAC11B";
    obj.connectInfo = [{
		title: "문의내용",
		description: ask_form.content.value},
		{
		title: "고객정보",
		description: "이름("+ ask_form.name.value +"), 휴대폰번호("+ ask_form.phone.value +"), 이메일("+ ask_form.email.value +"), 답변받기("+ answerType +")"
	}];
    
	var json_data = JSON.stringify(obj);

    $.ajax({                  
        type: "POST",
        url: "https://wh.jandi.com/connect-api/webhook/11486269/bb9f2b635b980e2a4ec4257e8ea7355c",
        headers: {
        	"Accept" : "application/vnd.tosslab.jandi-v2+json",
        	"Content-Type" : "application/json"
        },
        dataType: "json",
        data: json_data,
        success: function() {
       		alert("1:1 문의가 등록되었습니다.");
       		window.location.href = '/';
        },
        error: function(request,status,error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });
}