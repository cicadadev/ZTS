//var csrf_header = $('meta[name="_csrf_header"]').attr('content');
//var csrf_token = $('meta[name="_csrf"]').attr('content');

var headers = {};
headers["Ajax"] = "Y";
//headers[csrf_header] = csrf_token;

$.ajaxSetup({
     headers: headers	         
});



$( document ).ajaxError(function( event, request, settings ) {
	if(request.status == 999){	//권한오류.
		//mms.common.loginPopup();
	}else{
		common.hideLoadingBar();
	}
});
//$( document ).ajaxStart(function() {
//	common.showLoadingBar();
//});
//$( document ).ajaxSuccess(function() {
//	common.hideLoadingBar();
//});
//$( document ).ajaxStop(function() {
//	common.hideLoadingBar();
//});


/*
 * 공통 화면URL 
 * 화면ID : URL 
 */
pageUrl = {
		main:"/ccs/common/main",
		dispTemplate : "/dms/common/templateDisplay",
		myPage:"/mms/mypage/main",
		csCenter:"/ccs/cs/main",
		cart:"/oms/cart/list"
};


//공통
common = {
		
		// 모바일 로딩 이미지 SHOW
		showLoadingBar : function() {
			if(global.channel.isMobile == "true"){
				$("body").append('<div id="lodingBar" class="loadingBar_mo" align="center"><img src="/resources/img/mobile/Loading.gif" alt="" /></div></div>');
			}else{
				$("body").append('<div id="lodingBar" class="loadingBar_pc" align="center"><img src="/resources/img/mobile/Loading.gif" alt="" /></div></div>');
			}
		} ,
		// 모바일 로딩 이미지 HIDE
		hideLoadingBar : function() {
			$("#lodingBar").remove();
		},
		
		
		/**
		 * 원단위 콤마
		 * 
		 * com_priceFormat("2000",true,false);
		 * 
		 */
		priceFormat : function(str,won,span) {
		    var resultStr = String(str);
		    resultStr = resultStr.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
		    if(won){
		    	if(span){
		    		resultStr += "<span>원</span>" ;
		    	}else{
		    		resultStr += "원" ;
		    	}
		    }
		    return resultStr;
		},
		/**
		 * FORM MERGE
		 * 
		 * ex) mergeForms("orderform1","orderform2","orderform3");
		 */
		mergeForms : function(){
		  var forms = [];
		  $.each($.makeArray(arguments), function(index, value) {
		      forms[index] = document.forms[value];
		  });
		  var targetForm = forms[0];
		  $.each(forms, function(i, f) {
		      if (i != 0) {
		          $(f).find('input, select, textarea')
		              .clone()
		              .hide()
		              .appendTo($(targetForm));
		      }
		  });
		},
		isEmpty : function(value){	
//			console.log("isEmpty Value : " + value);
			if(value instanceof Object){
				if(value.length > 0){
					return false;
				}
			}else if(value instanceof Number){
				return false;
			}else{
				if(value != null && value != undefined && value != 'NaN' && value != 'undefined' && value != 'null'){
					var v = $.trim(value.toString());	 			
					if(v.length > 0){
						return false;
					}
				}
			}
			return true;			
		},
		isNotEmpty : function(value){
			return !this.isEmpty(value);
		},
    	//객체가 null 이거나 {} 이면 true
    	isEmptyObject : function(obj){
    		
    		var isEmpty = $.isEmptyObject(obj);
    		var isNull = (obj == null);
    		var isUndefined = (typeof obj == 'undefined');
    		return isEmpty || isNull || isUndefined;
    	},
		nvl : function(value,defaultValue){
			if(this.isNotEmpty(value)){
				return value;
			}else{
				return defaultValue;
			}
		},
		
		// PageMove  
		pageMove : function(pageId,param,urlStr){
			
			var url ="";
			var paramStr = "";
			var count=0;
			//pageId = pageId !==""?(pageId.toLowerCase().replace(/ /gi,'')):"";
			pageId = pageId !==""?(pageId.replace( /(\s*)/g, "")):"";
			
			if(pageId !==""){
				url = pageUrl[pageId];
			}else {
				url = urlStr;
			}
			
			$.each(param, function(key, value) {
				count++;
				//console.log('index:'+count+ '\n' +'key: ' + key + '\n' + 'value: ' + value);
				if(count == 1 && key !== ""){
					paramStr+="?"+key+"="+value;
				}else{
					paramStr+= (key!==''?("&"+key+"="+value):"");
				}
				
			});
			
			//console.log("URL:"+url+paramStr);
			window.location.href = url+paramStr;
		},
		
		// 지도
		map : function(mapAreaId, latitude, longitude) {
			var map = new naver.maps.Map(mapAreaId, {
				center: new naver.maps.LatLng(latitude, longitude),
				zoom: 10
			});
			
			var marker = new naver.maps.Marker({
		        map: map,
		        position: new naver.maps.LatLng(latitude, longitude)
		    });
		},
		/**
		 * 페이징 처리 : PagingTag.java 에서 호출
		 */
		goPage : function(param){
			
			var map = common.makePagingParam(param);
			common.callPaging(map);
			
		}
		,callPaging : function(map){
			
			var type = map['type'];
			var url = map['url'];
			 
			if(type === 'ajax'){

				var param = "currentPage="+map['currentPage']+"&pageSize="+ map['pageSize'];
				
				if(!common.isEmpty(map['formId'])){
					 param +="&"+$('#'+map['formId']).serialize();
				}
				
				$.ajax({
					contentType : "text/html;charset=UTF-8",
					url:url,
					type: "get",
					data: param,
					dataType: 'html',
					success: function(response){
						eval(map['callback']+"(response)");
					}
				});
				
			}else{
				
				$('#currentPage').val(map['currentPage']);
				$('#pageSize').val(map['pageSize']);
				$('#'+map['formId']).prop('action', url);
				$('#'+map['formId']).submit();

			}
			
		}
		// 페이징을 위한 기본값 설정
		,makePagingParam : function(param){
			var realArray = $.makeArray(param!==''? param.split(","):"" );
			var map = new Map();
			
			$.each( realArray, function( index, value ) {
				map[this.split(":")[0]] = this.split(":")[1];
			});
			
			return map;
		}
		/**
		 * 검색 화면 페이징 처리 : SearchPagingTag.java 에서 호출
		 */
		,goSearchPage : function(param){
			
			var map = common.makePagingParam(param);
			
			// 검색엔진 호출조회
			eval(map['callback']+"(map)");
		},
		// ajax 이미지 업로드 
		imageUpload : function(inputName ,callback){
			
			var fileInput = $("input[name="+inputName+"]");
			
			
			fileInput.change(function(){
				
                var formData = new FormData();
		  	    formData.append("img", fileInput[0].files[0]);
		  	 
		  	    $.ajax({
		  	        url: '/api/ccs/common/uploadImage',
		  	        data: formData,
		  	        processData: false,
		  	        contentType: false,
		  	        type: 'POST',
		  	        success: function(data){
		  	        	callback(data.fullPath);
		  	        }
		  	    });
		    });
		},
		// 숫자만 입력 가능하게.. onkeydown="return common.chkNumKeyDwn(this, event);"
		chkNumKeyDwn : function(obj, e) {

			if (!e)
				e = window.event;

			var kc = e.keyCode ? e.keyCode : e.which ? e.which : e.charCod;
			if ((kc <= 57 && kc >= 48) || (kc <= 105 && kc >= 96) || kc < 33 || kc == 46 || kc == 37 || kc == 39) {
				if(kc == 13 && obj.onchange){
					obj.onchange();
					return false;
				}
				return true;
			} else {// 숫자가 아닐경우 firefox는 return, 나머지는 alert
				if (navigator.userAgent.toLowerCase().indexOf('firefox') > -1) {
					e.preventDefault();
				} else {
					alert("숫자만 입력할수 있습니다.");
					obj.value = Number(String(obj.value).replace(/\..*|[^\d]/g, ""));
					e.returnValue = false;
				}
				return false;
			}
		},
    	// 문자열의 바이트 체크
    	getBytes : function(text){
    		var bytes = text.replace(/[\0-\x7f]|([0-\u07ff]|(.))/g,"$&$1$2").length;
    		//console.log(bytes);
    		return bytes;
    	}


}


