$(document).ready(function () {
  init();
})

function init() {
  $('.alert-notyetopen').click(function (e) {
    e.preventDefault();
    alert('서비스 준비중입니다. 빠른 시일내로 찾아뵙겠습니다.');
  });

  $('#ask-form').submit(function (e) {
  	e.preventDefault();
    ajaxPostAskData();
  });
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

	console.log(json_data);

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