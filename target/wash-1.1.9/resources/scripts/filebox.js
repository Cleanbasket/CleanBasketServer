function fileboxInit() {
  var fileboxIdKey = 'filebox-id';
  var str_trigger = 'filebox-trigger';
  var str_tempInput = 'filebox-temp';
  var str_input = 'filebox-input';
  var str_inputs = 'filebox-inputs';
  var str_box = 'filebox-box';
  var exceedMessage = "10MB를 초과하였습니다.";  
  // var str_AttachedName = 'attached';
  // var str_AttachedIdPrefix = 'attached-';
  
  /* init */
  var fileboxLists = {};
  var $inputBoxs = $("." + str_inputs);

  $inputBoxs.map(function(i, elem) {
    var fileboxId = $(elem).data(fileboxIdKey);
    fileboxLists[fileboxId] = new fileboxList(fileboxId, 10*1024*1024);
  })
  
  $("." + str_trigger).click(function (e) {
    e.preventDefault();
    
    function getTempInput() {
      return $("." + str_tempInput).filter(function() {
        console.log(fileboxId, $(this).data(fileboxIdKey))
        return $(this).data(fileboxIdKey) === fileboxId;
      })  
    }

    var fileboxId = $(this).data(fileboxIdKey);
    var $inputBox = $("." + str_inputs).filter(function () {
      return $(this).data(fileboxIdKey) === fileboxId;
    });
    if (getTempInput().length) {
      console.log("temp input exist");
    } else {
      // temp input 설정
      var inputElem = $('<input />')
                    .attr('type', 'file')
                    .data(fileboxIdKey, fileboxId)
                    // .attr('name', 'temp')
                    .addClass(str_tempInput);

      $inputBox.append(inputElem);

      // temp input에 입력이 들어오면 
      // 1. name과 id를 바꾼다.
      // 2. filebox에 추가한다.
      inputElem.change(function(e) {
        // not empty
        if($(this).val() != "") {
          // 파일 이름 추출
          if(window.FileReader){  // modern browser
            var filename = $(this)[0].files[0].name;
            var size = $(this)[0].files[0].size;
          } else {  // old IE
            var filename = $(this).val().split('/').pop().split('\\').pop();  // 파일명만 추출
          }
          console.log(filename);

          var idCount = fileboxLists[fileboxId].count();
          if (typeof size === 'undefined') size = 0;
          var isNotExceed = fileboxLists[fileboxId].push({
            name: filename,
            elem: $(this),
            size: size
          })

          if (isNotExceed) {
            $(this)
              // .attr('name', fileboxId + "[" + idCount + "]")
              // .attr('id', fileboxId + "-" + idCount)
              .removeClass(str_tempInput)
              .addClass(str_input);
          } else {
            alert(exceedMessage);
          }
        } else {
          // $(this).parent().remove("." + str_tempInput);
        }

        bindFileBoxToInput(fileboxLists, fileboxId);
      })
    }

    getTempInput().trigger("click");
  })

  function bindFileBoxToInput (fileboxLists, fileTargetId) {
    // filebox를 렌더한다.
    var box = $("." + str_box).filter(function () {
      return $(this).data(fileboxIdKey) === fileTargetId;
    });
    $(box).html(fileboxLists[fileTargetId].render());
    // 연결되는 fileinput에 filebox와 같은 index의 name을 준다
    $("." + str_input)
      .filter(function () {
        return $(this).data(fileboxIdKey) === fileTargetId;
      })
      .map(function (i, elem) {
        $(elem).attr('name', fileTargetId + '[' + i + ']');
      })


    // delete 이벤트 추가
    var $items = $(box).find('.fileitem');
    $items.find(".fileitem-delete").click(function (e) {
      // console.log($(this).parent(), $items.index($(this).parent()));  
      // console.log($(this).parent().data(fileboxIdKey));
      var id = $(this).parent().data(fileboxIdKey);
      var fileboxList = fileboxLists[id];

      var idx = $items.index($(this).parent());
      fileboxList.deleteIdx(idx);
      $("." + str_input + "[name='" + id + "[" + idx + "]']").remove();
      bindFileBoxToInput(fileboxLists, id);
    })
  }


  /* filebox List */
  function fileboxList(id, maxSize) {
    var fileList = this.fileList = [];
    this.fileboxId = id;
    this.maxSize = maxSize || 10 * 1024 * 1024;
    // this.$Inputs = $("." + str_inputs).filter(function() {
    //   return $(this).data(fileboxIdKey) === fileboxId;
    // });
    // this.$box = $("." + str_box).filter(function() {
    //   return $(this).data(fileboxIdKey) === fileboxId;
    // });
    // if (!this.$Inputs) {}
    // if (!this.$box) {}
  }

  fileboxList.prototype.count = function () {
    return this.fileList.length;
  }

  fileboxList.prototype.isExceed = function (file) {
    var sizeBefore = this.fileList.reduce(function (total, file) {
      return total + file.size;
    }, 0)
    console.log("sizeBefore :", sizeBefore / 1024);
    return file.size + sizeBefore > this.maxSize;
  }

  fileboxList.prototype.concat = function (files) {
    var isExceed = false;
    for (var i = 0; i < files.length; i++) {
      isExceed = !this.push(files[i]);  
      if (isExceed) break;
    }
    return !isExceed;
  }

  fileboxList.prototype.push = function (file) {
    if (this.isExceed(file)) {
      return false;  
    }
    this.fileList.push(file);
    return true;
  }

  fileboxList.prototype.render = function() {
    var fileboxId = this.fileboxId;
    var rendered =  this.fileList.map(function(file) {
      return "<div class='fileitem' data-" + fileboxIdKey + "=" + fileboxId + ">" + 
      "<span class='fileitem-name'>" + file.name + "</span>" +
      "<span class='fileitem-size'>" + toKb(file.size, 1) + "KB</span>" + 
      "<span class='fileitem-delete'></span></div>";  
    })  

    return rendered.join("");
  }

  fileboxList.prototype.deleteIdx = function(idx) {
    this.fileList.splice(idx, 1);
  }
}

/* util */
function toKb(byte, roundSize) {
  var kb = byte / 1024
  var round = Math.pow(10, roundSize);
  return  Math.round(kb * round) / round;
}