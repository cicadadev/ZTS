// 고객센터
custcenter = {
	// cs layout_type 클래스 변경
	setCsLayoutType : function(param){
		document.getElementById("cs_layout_type").className = "layout_type1 " + param; 
	},
	
	// ajax callback
	listCallback : function(div, html){
	    $("#" + div).html(html);
	    /*$('#' + div + ' tr').on("click", function(e){
			if( $(this).next(".tr_cont").hasClass("tr_cont_hide") ){
				$(this).siblings(".tr_cont").addClass("tr_cont_hide");
				$(this).next(".tr_cont").removeClass("tr_cont_hide");
			}else{
				$(this).next(".tr_cont").addClass("tr_cont_hide");
			}
		});*/
	},
	
	paging : function(url, data, callback) {
		$.get( url, data ).done(function( html ) {
			callback(html);
			common.hideLoadingBar();
		});
	},
	
	main : {
		faq_search : function(id){
			var searchKey = $("#searchWord").val();
			
			if(searchKey == ''){
				alert('FAQ가 없습니다. 다시 검색해주세요.');
				return;
			}else{
				custcenter.faq.go('',searchKey,'');
			}
		}
	},
	
	qna : {
		
		// 주문 상품 레이어 팝업 호출
		searchOrderProduct : function() {
			var params = [];
			ccs.layer.orderProductLayer.open("", function(data) {
				
			});
		},
		
		// 레이어 팝업 callBack
		getOrderProduct : function(orderId, productId, productName, saleproductId, saleproductName) {
		    $("#qna_order1").val(productName);			// 상품명
		    $("#qna_order2").val(saleproductName);		// 단품명
		    $("#qna_order3").val(orderId);				// 주문 번호
		    $("#qna_order4").val(productId);			// 상품 번호
		    $("#qna_order5").val(saleproductId);		// 단품 번호
//		    ccs.layer.orderProductLayer.close();
		},
		
		// 1:1 문의 저장
		save : function() {
			mms.common.loginCheck(function(success){
				
				if(success){
					if(ccs.common.emailcheck('email', 'email', true)){
						return;
					}
					
					if(ccs.common.telcheck('phone', '전화번호', document.getElementById('qna_smsYn').checked?true:false)){
						return;
					}
					
					if(!confirm("등록하시겠습니까?")){
						return;
					}
					// qnaForm
					var form1 = $("#qnaForm");
					var form2 = form1.serialize();
					
					$.ajax({
						url : "/api/ccs/qna/save",
						type : "POST",
						data : form1.serialize()
					}).done(function(response) {
						if (response.RESULT == "SUCCESS") {
							alert("정상적으로 등록되었습니다.");
							ccs.link.mypage.activity.inquiry('MYQA');
						} else {
							alert(response.MESSAGE);
						}
					});
				}
			});
			
		},
		
		// 초기화
		clear : function() {
			if(!confirm("입력하신 내용이 삭제됩니다. 취소하시겠습니까?")){
				return;
			}
			/* $("#form").trigger('reset'); //jquery
			document.getElementById("myform").reset(); //native JS */
		},
		
		// 핸드폰 번호 추가
		checkSmsYn : function(phone){
			if (document.getElementById('qna_smsYn').checked){
				ccs.common.telset('phone', phone);
			}
			else{
				ccs.common.telset('phone', '');
			}
		},
		
		// 주문 상품 초기화
		clearOrderPopupInfo : function(){
			for(var i = 1; i < 6; i++){
				document.getElementById("qna_order" + i.toString()).value = '';
			}
		},
		
		// 문의 유형 이벤트
		selectChange : function(){
			if($("#qnaForm").find("#inquiryTypeCd").val() == 'INQUIRY_TYPE_CD.BRAND'){
				$('.brand_add').closest("li").next().hide();
				$('.brand_add').next().show();
				console.log($('#select_brandId').next().val(""));
				$('#select_brandId').next().val("").prop("selected", true);
				custcenter.qna.clearOrderPopupInfo();
			}else if($("#qnaForm").find("#inquiryTypeCd").val().match(/DELIVERY|CLAIM|ORDER/)){
				$('.brand_add').closest("li").next().show();
				$('.brand_add').next().hide();
				$("#qnaForm").find("#brandId").val("");
			}
			else{
				$('.brand_add').closest("li").next().hide();
				$('.brand_add').next().hide();
				$("#qnaForm").find("#brandId").val("");
				custcenter.qna.clearOrderPopupInfo();
			}
		},
		
		// 브랜드관 1:1 문의
		setBrandQna : function(brandId) {
			$("#inquiry_selectBox select").find("option").each(function(index) {
				if ($("#inquiry_selectBox select").find("option:eq(" + index +")").val() == "INQUIRY_TYPE_CD.BRAND") {
					var inqueryOption = $("#inquiry_selectBox select").find("option:eq(" + index +")");
					inqueryOption.attr("selected", "selected");
					$("#select_inquiryTypeCd").text(inqueryOption.text());
				}
			});
			
			$("#brand_selectBox").show();
			$("#brand_selectBox select").find("option").each(function(index) {
				if ($("#brand_selectBox select").find("option:eq(" + index +")").val() == brandId) {
					var brandOption = $("#brand_selectBox select").find("option:eq(" + index +")");
					brandOption.attr("selected", "selected");
					$("#select_brandId").text(brandOption.text());
				}	
			});
		}
	},
	
	notice : {
		// ajax callback
		listCallback : function(html){
			$("#noticeDiv").html(html);
			$('#noticeDiv tr').on("click", function(e) {
				if ($(this).next(".tr_cont").hasClass("tr_cont_hide")) {
					$(this).siblings(".tr_cont").addClass("tr_cont_hide");
					$(this).next(".tr_cont").removeClass("tr_cont_hide");
				} else {
					$(this).next(".tr_cont").addClass("tr_cont_hide");
				}
				//e.preventDefault();
			});
		},
		moListCallback : function(html) {
			$(".div_tb_tbody4").append(html);
			ccs.common.moLoadingBar();
		},
		
		// ajx 호출
		listCall : function(url, isScroll) {
			
			var data = {};
			if(isScroll) {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				
				data = {info : $("#searchWord").val(), type : $("#typeSelect").val(), currentPage: $("input[name=currentPage]").val(), isScroll:isScroll};
				custcenter.paging(url, data, custcenter.notice.moListCallback);
				if(ccs.common.mobilecheck()) {
					ccs.common.moLoadingBar('.div_tb_tbody4');
//					$(".div_tb_tbody4").append('<div id="lodingBar" align="center"><img src="/resources/img/mobile/Loading.gif" alt="" /></div>');
				}
			}else{
				$("input[name=currentPage]").val(Number(1));
				
				data = {info : $("#searchWord").val(), type : $("#typeSelect").val()};
				custcenter.paging(url, data, custcenter.notice.listCallback);
			}
		},
		
		detail : function(noticeNo, readCnt){ // 공지  상세 ccs.link.custcenter.notice.detail("1001245", "1")
			ccs.link.go("/ccs/cs/notice/detail?noticeNo="+noticeNo+"&readCnt="+readCnt, CONST.NO_SSL);
		},
		
		brand : function(brandId) {
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/ccs/notice/brand/list?brandId="+brandId,
				type : "GET"
			}).done(function(response) {
				var notice = response.notice;
				
				var color = "";
				if (ccs.common.mobilecheck()) {
					color="#000";
				} else {
					color="#fff";
				}
					
				var strHtml = "<dd>";
				if (notice != null) {
					strHtml += "	<span><a href='javascript:custcenter.notice.detail("+notice.noticeNo+","+notice.readCnt+");'><h3 style=\"color:"+color+";\">"+notice.title+"</h3></a></span>";
					strHtml += "	<a href='javascript:ccs.link.go(\"/ccs/cs/notice/list?brandId="+brandId+"\", CONST.NO_SSL);' class='btn_moreView' style=\"color:"+color+";\">더보기</a>";
				}
				strHtml += "</dd>";
						
				$("#noticeArea").html(strHtml);
				
				if (ccs.common.mobilecheck()) {
        			// 스와이프 높이 다시 계산
            		ccs.mainSwipe.calculateHeight();
        		}
			});
		},
		searchKeyUp : function (obj){
			//엔터 검색으로 이동
			if(event.keyCode==13){
				custcenter.notice.listCall('/ccs/cs/notice/list/ajax');
			}
		}
	},
	
	event : {
		
		detail : function(noticeNo, readCnt){ // 당첨자 공지  상세 ccs.link.custcenter.event.detail("1001245", "1")
			ccs.link.go("/ccs/cs/event/detail?noticeNo="+noticeNo+"&readCnt="+readCnt, CONST.NO_SSL);
		},
	
		// 당첨자 목록 레이어 팝업 호출
		searchWinnerList : function() {
			var params = [];
			ccs.layer.winnerListLayer.open("", function(data) {
				
			});
		},
		// 레이어 팝업 검색
		search : function(){
			var param = {eventId : $('#eventId').val(), memberId : $('#searchKeyword').val()};
//					$.get("/ccs/common/winnerList/list/ajax", param).done(function(html) {
//						$("#winnerListResult").html(html);
//						$("#winnerListResult").parent().show();
//					});
			
			$.ajax({
				  method: "post",
				  url: "/ccs/common/winnerList/list/ajax",
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				}).done(function( html ) {
					$("#winnerListResult").html(html);
					$("#winnerListResult").parent().show();
				});
			
			/*if (ccs.common.mobilecheck()) {
				ccs.layer.fnLayerOpen($("#searchWinnerListLayer"));
			} else {
				// 위치잡기
				ccs.layer.fnPopPosition($("#searchWinnerListLayer"));
			}*/
			
			
		},
		searchKeyUp : function (obj){
			//엔터 검색으로 이동
			if(event.keyCode==13){
				custcenter.notice.listCall('/ccs/cs/event/notice/list/ajax');
			}
		}
	},
	
	faq : {
		
		// ajax callback
		listCallback : function(html){
			$("#faqDiv").html(html);
			$('#faqDiv tr').on("click", function(e) {
				if ($(this).next(".tr_cont").hasClass("tr_cont_hide")) {
					$(this).siblings(".tr_cont").addClass("tr_cont_hide");
					$(this).next(".tr_cont").removeClass("tr_cont_hide");
				} else {
					$(this).next(".tr_cont").addClass("tr_cont_hide");
				}
				//e.preventDefault();
			});
		},
		moListCallback : function(html) {
			$(".div_tb_tbody4").append(html);
			ccs.common.moLoadingBar();
		},
		
		
		// faq link
		go : function(faqTypeCd, info, faqNo){
			ccs.link.go("/ccs/cs/faq/list?faqTypeCd="+faqTypeCd+"&info="+info+"&faqNos="+faqNo, CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);
		},
		
		// ajax FAQ 리스트 콜백
		listCall : function(url, isScroll){
			//alert($("#faqForm").find("#faqTypeCd").val());
			var type = $("#faqForm").find("#faqTypeCd").val();
			
			if(common.isNotEmpty(type)){
				var splitType = type.split('.');
				if(common.isNotEmpty(splitType[1])){
					$('#faqTypeCd_' + splitType[1]).parent().addClass("on").siblings("li").removeClass("on");
				}
			}
			
			var data = {};
			if(isScroll) {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				
				data = {info : $("#faqForm").find("#info").val() , faqTypeCd : $("#faqForm").find("#faqTypeCd").val(), currentPage: $("input[name=currentPage]").val(), isScroll:isScroll};
				custcenter.paging(url, data, custcenter.faq.moListCallback);
				if(ccs.common.mobilecheck()) {
					ccs.common.moLoadingBar(".div_tb_tbody4");
				}
			}else{
				$("input[name=currentPage]").val(Number(1));
				
				data = {info : $("#faqForm").find("#info").val() , faqTypeCd : $("#faqForm").find("#faqTypeCd").val()};
				custcenter.paging(url, data, custcenter.faq.listCallback);
			}
		},
		
		// FAQ 유형 검색
		select_type : function(type) {
			$("#faqForm").find("#faqTypeCd").val(type);
			$("#faqForm").find("#info").val('');
			$("#searchKeyword").val('');
			if(common.isNotEmpty(type)){
				$('#' + type.replace('.', '_')).parent().addClass("on").siblings("li").removeClass("on");
			}
			
			custcenter.faq.listCall("/ccs/cs/faq/list/ajax");
		},
		
		// FAQ 검색어 선택
		select_word : function(word) {
			var keyword;
			if(word != ''){
				if(word == 'SEARCH.DELIVERY'){
					keyword = '배송';
				}else if(word == 'SEARCH.CANCLE'){
					keyword = '취소';
				}else if(word == 'SEARCH.REFUND'){
					keyword = '반품';
				}else if(word == 'SEARCH.EXCHANGE'){
					keyword = '교환';
				}else if(word == 'SEARCH.ORDER'){
					keyword = '주문';
				}else if(word == 'SEARCH.RETURN'){
					keyword = '환불';
				}else{
					keyword = word;
				}
			}
			$("#faqForm").find("#info").val(keyword);
			$("#faqForm").find("#faqTypeCd").val('');
			$("#searchKeyword").val(keyword);
			$('#faqTypeCd_ALL').parent().addClass("on").siblings("li").removeClass("on");
			custcenter.faq.listCall("/ccs/cs/faq/list/ajax");
		},
		// FAQ 검색어 조회
		search_word : function() {
			var word = $("#searchKeyword").val();

			if (word == null || word == '') {
				alert("검색어를 입력해주세요.");
				return;
			}

			$("#faqForm").find("#info").val(word);
			$("#faqForm").find("#faqTypeCd").val('');
			$('#faqTypeCd_ALL').parent().addClass("on").siblings("li").removeClass("on");
			custcenter.faq.listCall("/ccs/cs/faq/list/ajax");
		},
		
		searchKeyUp : function (obj){
			//엔터 검색으로 이동
			if(event.keyCode==13){
				custcenter.faq.search_word();
			}
		}
	}
}
