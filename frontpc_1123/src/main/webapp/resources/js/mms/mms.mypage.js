//마이페이지
mypage = {
	/*****************************************************************************************************************************************************************************************************************************************************
	 * 마이페이지 메인
	 ****************************************************************************************************************************************************************************************************************************************************/		
	main : {
		goInterest : function() {
			if (!$("#interestInfoLayer").is(":visible")) {	// 이미 팝업 떠있을 때, 메뉴 클릭해도 팝업 뜨지 않도록.
				ccs.layer.interestInfoLayer.open(2, true);
			}
		},
		latestOrder  : function(){
			var url = "/mms/mypage/main/order/ajax";
			mms.common.paging(url, null, function(res) {
				$('.recentList').html(res);
			});
		},
		wishList : function() {
			var url = "/mms/mypage/main/wish/ajax";
			
			mms.common.paging(url, null, function(res) {
				$('#areaWishList').html(res);
			});
		},
		cartList : function() {
			var url = "/mms/mypage/main/cart/ajax";
			
			mms.common.paging(url, null, function(res) {
				$('#areaCartList').html(res);
			});
		},
		offcouponCnt : function(cnt) {
			$.ajax({
				url : "/api/mms/mypage/offshopCoupon/count",
				type : "POST"		
			}).done(function(response){
				if(common.isEmpty(cnt)) {
					cnt = 0;
				}
				
				var total = parseInt(cnt) + parseInt(response);

				var str = "<em>"+ total +"</em> 개";
				$("#couponTotal").html(str);
			})
		},
		
		nameLengthAdjust : function(name,mobile,app) {
			var temp = name.replace(/[A-Z|a-z]/g, '');
			var _length = 0;

			// MOBILE
			if (mobile) {
				// 한글
				if (common.isNotEmpty(temp)) {
					if (app) {
						if (name.length > 8) _length = 8;
					} else {
						if (name.length > 11) _length = 11;
					}
				}
				// 영문
				else {
					if (app) {
						if (name.length > 13) _length = 13;
					} else {
						if (name.length > 18) _length = 18;
					}
				}
			}
			// PC
			 else {
				// 한글
				if (common.isNotEmpty(temp)) {
					if (name.length > 5) _length = 5;
				}
				// 영문
				else {
					if (name.length > 6) _length = 6;
				}
			}
			
			if( _length > 0) {
				name = name.substr(0, _length);
				$('#memberName').html('<em>'+ name +'… 님</em>');
			}
		}
		
	},
		
	/*****************************************************************************************************************************************************************************************************************************************************
	 * 배송지관리 Page
	 ****************************************************************************************************************************************************************************************************************************************************/
	deliveryAddress : {
		
		// 회원 배송지 리스트 조회 AJAX
		getAddressList : function() {
			$.get( "/mms/mypage/deliveryAddress/list/ajax")
			.done(function( html ) {
				mypage.deliveryAddress.listCallback(html);
			});
		},
		// SET AJAX RETURN HTML
		listCallback : function(html) {
			$("#address").html(html);
			// 라디오버튼 이벤트
			mypage.deliveryAddress.fnRadioStyle();
		},
		// 새 배송지 입력 레이어 
		addNewDeliveryAddr : function() {
			var totalCnt = $("[name=TOTAL_CNT]").val();
			// 최대 등록 배송지 개수 10개
			if (common.isEmpty(totalCnt) || Number(totalCnt) < 10) {
				ccs.layer.deliveryAddressLayer.open("", function() {
					mypage.deliveryAddress.fnPlaceholder();
					mypage.deliveryAddress.fnChkStyle();
				});
			} else {
				alert("배송지 등록 개수를 초과 하였습니다.");
			}
		},
		// 새배송지 레이어 닫기
		layerClose : function() {
			ccs.layer.deliveryAddressLayer.cancel();
		},
		// 기본 배송지 설정
		setBasicDeliveryAddr : function() {
			if (!$(".radio_style1 input[type=radio]").is(':checked')) {
				alert("배송지를 선택해주세요.");
				return;
			}
			var addressNo = $(".radio_style1 input[type=radio]:checked").closest("li").attr("id");
			
			var param = {addressNo : addressNo};
			$.ajax({ 				
				url : "/api/mms/mypage/deliveryAddress/updateBasicAddress",
				method : "post",		
				contentType : "application/json; charset=UTF-8",
				data: JSON.stringify(param),
				success : function(response) {
					alert("기본 배송지 설정이 완료되었습니다.");
					window.location.reload(true);
				},
				error : function(response) {
					alert(response.MESSAGE);
				}
			});
		},
		// 새배송지 저장 & 배송지 수정
		saveDeliveryAddr : function(param) {
			
			if (!mypage.deliveryAddress.formValidation()) {
				return;
			}
			
			$("input[name=phone1]").val($("#tel_areaCode").next().find("option:selected").text()+"-"+ $("input[name=tel_num1]").val()+"-"+ $("input[name=tel_num2]").val());
			$("input[name=phone2]").val($("#cell_areaCode").next().find("option:selected").text()+"-"+ $("input[name=cell_num1]").val()+"-"+ $("input[name=cell_num2]").val());
			if ($("input[name=basicYn]").is(":checked")) {
				$("input[name=basicYn]").val("Y");
			}
			
			var msg = "";
			if (param == 'update') {
				$("#crudType").val("U");
				msg = "배송지가 수정되었습니다.";
			} else {
				$("#crudType").val("C");
				msg = "새 배송지가 저장되었습니다.";
			}
			
			var formData = $("#addrInputForm").serialize();
			console.log(formData);
			$.ajax({
				method: "POST",
				url: "/api/mms/mypage/deliveryAddress/save",
				data: formData,
				success : function(response) {
					alert(msg);
					window.location.reload(true);
				},
				error : function(response) {
					alert(response.MESSAGE);
				}
			});
		},
		// 배송지 수정
		modifyAddress : function(addressNo) {
			ccs.layer.deliveryAddressLayer.open(addressNo, mypage.deliveryAddress.callBack_modifyLayer);
		},
		
		callBack_modifyLayer : function(addressNo) {
			
			
				var phone1 = $("[name=phone1_" + addressNo + "]").val();
				var phone2 = $("[name=phone2_" + addressNo + "]").val();
				
				ccs.common.telset("cell", phone2);
				ccs.common.telset("tel", phone1);
				
			
			
			$("#layerTitle").text("배송지 수정");
			$("#modifyBtn").show();
			$("#regBtn").hide();
			$("[name=deliveryName1]").val($("[name=deliveryName1_" + addressNo + "]").val());
			
			$("[name=zipCd]").val($("[name=zipCd_" + addressNo + "]").val());
			$("[name=address1]").val($("[name=address1_" + addressNo + "]").val());
			$("[name=address2]").val($("[name=address2_" + addressNo + "]").val());
			$("[name=addressNo]").val($("[name=addressNo_" + addressNo + "]").val());
			mypage.deliveryAddress.fnPlaceholder();
			mypage.deliveryAddress.fnChkStyle();
		},
		// 배송지 삭제
		deleteAddress : function(addressNo) {
			if (confirm("배송지를 삭제하시겠습니까?")) {
				var param = {addressNo : addressNo };
				$.ajax({ 				
					url : "/api/mms/mypage/deliveryAddress/delete",
					type : "POST",		
					data: JSON.stringify(param),
					contentType : "application/json; charset=UTF-8",
					success : function(response) {
						alert("삭제 되었습니다.");
						mypage.deliveryAddress.getAddressList();
					},
					error : function(response) {
						alert(response.MESSAGE);
					} 
				});
			}
		},
		// 우편번호 찾기
		searchAddress : function() {
			
			ccs.layer.searchAddressLayer.open("", function(data) {
				mypage.deliveryAddress.tabClickEvent();
				mypage.deliveryAddress.fnSelectStyle();
				console.log(data);
				$("input[name=zipCd]").val(data.zipCd);
				$("input[name=address1]").val(data.address1);
				$("input[name=address2]").val(data.address2);
				mypage.deliveryAddress.fnPlaceholder();
				
			});
		},
		fnRadioStyle : function(){
			$(".pc .radio_style1 input").each(function() {
				if($(this).is(':checked')){
					$(this).parent().addClass("selected");
				}
			});
			
			$(".pc .radio_style1 input").off("change").on({
				"change" : function() {
					var this_name = $(this).attr("name");
					$("input[name='" + this_name + "']").parent().removeClass("selected");
					$(this).parent().addClass("selected");
				}
			});
		},
		fnPlaceholder : function(){
			$(".inputTxt_place1 input[type='text']").each(function() {
				if($(this).val().length > 0){
					$(this).parent().prev().hide();
				}else{
					$(this).parent().prev().show();
				}
			});
			
			$(".inputTxt_place1 input[type='text']").off("focus blur").on({
				"focus" : function() {
					$(this).parent().prev().hide();
					$(this).closest(".inputTxt_place1").addClass("place_hover");
				},
				"blur" : function() {
					$(this).closest(".inputTxt_place1").removeClass("place_hover");
					if($(this).val().length > 0){
						$(this).parent().prev().hide();
					}else{
						$(this).parent().prev().show();
					}
				}
			});
		},
		fnChkStyle : function(){
			$(".pc .chk_style1 input").each(function() {
				if($(this).is(':checked')){
					$(this).parent().addClass("selected");
				}
			});
			
			$(".pc .chk_style1 input").on({
				"change" : function() {
					if($(this).is(':checked')){
						$(this).parent().addClass("selected");
					}else{
						$(this).parent().removeClass("selected");
					}
				}
			});
		},
		
		fnSelectStyle : function() {
			$(".select_box1 select").each(function() {
				$(this).siblings('label').text( $("option:selected", this).text() );
			});
			
			$(".wrap .content").off("change").on("change", ".select_box1 select", function() {
				$(this).siblings('label').text( $("option:selected", this).text() );
			});
		},
		
		formValidation : function() {
			
			if ($("input[name=deliveryName1]").val() == '') {
				alert("받으실분 이름을 입력해주세요.");
				return false;
			}
			
			if ($(".select_box1 > select option:selected").eq(0).val() == '' 
				|| $("input[name=cell_num1]").val() == '' || $("input[name=cell_num2]").val() == '') {
				alert("휴대폰번호를 입력해주세요");
				return false;
			}
			
//			if ($(".select_box1 > select option:selected").eq(1).val() == '' 
//				|| $("input[name=tel_num1]").val() == '' || $("input[name=tel_num2]").val() == '') {
//				alert("전화번호를 입력해주세요");
//				return false;
//			}
			
			if ($("input[name=zipCd]").val() == '' || $("input[name=address1]").val() == '' || $("input[name=address2]").val() == '') {
				alert("주소를 입력해주세요");
				return false;
			}
			
			return true;
		},
		
		tabClickEvent : function() {
			/* 탭 공통 : 2016.07.27 추가 */
			$(".tabBox a, .tabBox1 a, .tabBox2 a").off("click").on("click", function(e){
				var idx = $(this).parent().index();
				var parent_ul;
				
				if( $(this).closest("ul").hasClass("tabBox") ){
					parent_ul = $(this).closest(".tabBox");
				}else if( $(this).closest("ul").hasClass("tabBox1") ){
					parent_ul = $(this).closest(".tabBox1");
				}else{
					parent_ul = $(this).closest(".tabBox2");
				}
				
				$(this).parent().addClass("on").siblings("li").removeClass("on");
				$(parent_ul).siblings(".tab_con").eq(idx).addClass("tab_conOn").siblings(".tab_con").removeClass("tab_conOn");
				
				e.preventDefault();
			});
		}
	},
	/***************************************************************
	 * 쿠폰 Page
	 ***************************************************************/
	coupon : {
		search : function(content, val, isScroll) {
			
			// list 분류 선택 표시
			if(content != null) {
				mms.common.typeExpression(content);
			}
			
			var paramType = "";	// 쿠폰유형
			var usePlace = "";	// 쿠폰 online/offline 구분

			paramType = $("select option:selected").val();
			if(common.isNotEmpty()) {
				usePlace = val;
			} else {
				usePlace = $(".sortBox").find('li.active').get(0).id;
			}
			
			if(usePlace == "OFFLINE") {
				paramType = "";
				$(".sortPkg .posR").hide();
			} else {
				$(".sortPkg .posR").show();
			}
			
			var url = "/mms/mypage/coupon/list/ajax";
			
			var data = {};
			if(isScroll) {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				data = { couponTypeCd : paramType, usePlace : usePlace, currentPage: $("input[name=currentPage]").val()};
				mms.common.paging(url, data, mypage.coupon.moCouponCallback);
			} else {
				$("input[name=currentPage]").val(Number(1));
				data = { couponTypeCd : paramType, usePlace : usePlace};
				mms.common.paging(url, data, mypage.coupon.couponCallback);
			}
			
			if(ccs.common.mobilecheck()) {
				common.showLoadingBar();
			}
		},
		// 쿠폰 콜백
		couponCallback : function(res) {
			$(".coupon_list").children().remove();
			$(".coupon_list").append(res);
			
			mms.common.setCnt();
			
			$("#mainCnt").text($("#total").val());
			$("#mainEndCnt").text($("#endCnt").val());
			common.hideLoadingBar();
		},
		moCouponCallback : function(res) {
			setEvent();
			$(".coupon_list").append(res);
			
			mms.common.setCnt();
			
			$("#mainCnt").text($("#total").val());
			$("#mainEndCnt").text($("#endCnt").val());
			common.hideLoadingBar();
		},
		issueCoupon : function() {
			var code = $("#privateCin").val();
			
			common.showLoadingBar();
			sps.coupon.issue(null, function(res) {
				common.hideLoadingBar();
				if(!res) {
					return;
				}
				
				if(res[0].resultCode == "0000") {
					alert(res[0].resultMsg);
					
					mypage.coupon.search("coupon", "ONLINE");
				} else if(res[0].resultCode == "0002") {
					alert("이미 쿠폰을 발급받으셨습니다.")
				} else {
					alert(res[0].resultMsg);
				}
			}, code);
		}
		
	},
	/***************************************************************
	 * 당근 Page
	 ***************************************************************/
	carrot : {
		search : function(content, param, isScroll) {
			
			// list 분류 선택 표시
			if(content != null) {
				mms.common.typeExpression(content);
			}
			
			var carrotSaveType = "";
			if(common.isNotEmpty(param)) {
				carrotSaveType = param;
			} else {
				carrotSaveType = $(".sortBox").find('li.active').get(0).id;
			}
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var url = "/mms/mypage/carrot/list/ajax";
			
			var data = {};
			if(isScroll) {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				
				data = {startDate : startDate, endDate: endDate, saveType: carrotSaveType, currentPage: $("input[name=currentPage]").val()};
				mms.common.paging(url, data, mypage.carrot.moCarrotCallback);
			} else {
				$("input[name=currentPage]").val(Number(1));
				
				data = {startDate : startDate, endDate: endDate, saveType: carrotSaveType};
				mms.common.paging(url, data, mypage.carrot.carrotCallback);
			}
			
			if(ccs.common.mobilecheck()) {
				common.showLoadingBar();
			}
		},
		carrotCallback : function(res) {
			$(".div_tb_tbody2").html(res);
			mms.common.setCnt();
			common.hideLoadingBar();
		},
		moCarrotCallback : function(res) {
			setEvent();
			$(".div_tb_tbody2").append(res);
			mms.common.setCnt();
			common.hideLoadingBar();
		}
	},
	/***************************************************************
	 * 예치금 Page v -- TODO 아이폰 예치금 환불 신청 팝업
	 ***************************************************************/
	deposit : {
		search : function(content, param, isScroll) {
			
			// list 분류 선택 표시
			if(content != null) {
				mms.common.typeExpression(content);
			}
			
			var carrotSaveType = "";
			if(common.isNotEmpty(param)) {
				depositSaveType = param;
			} else {
				depositSaveType = $(".sortBox").find('li.active').get(0).id;
			}
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var url = "/mms/mypage/deposit/list/ajax";
			var data = {};
			
			if(isScroll) {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				data = {startDate : startDate, endDate: endDate, saveType: depositSaveType, currentPage: $("input[name=currentPage]").val()};
				mms.common.paging(url, data, mypage.deposit.moDepositCallback);
			} else {
				$("input[name=currentPage]").val(Number(1));
				data = {startDate : startDate, endDate: endDate, saveType: depositSaveType};
				mms.common.paging(url, data, mypage.deposit.depositCallback);
			}
			
			if(ccs.common.mobilecheck()) {
				common.showLoadingBar();
			}
			
		},
		depositCallback : function(res) {
			$(".div_tb_tbody2").html(res);
			mms.common.setCnt();
			common.hideLoadingBar();
		},
		moDepositCallback : function(res) {
			setEvent();
			$(".div_tb_tbody2").append(res);
			mms.common.setCnt();
			common.hideLoadingBar();
		},
		refund : function() {
			var amt = $("#customerDepositAmt").text();
			if(Number(amt) < 1) {
				alert("보유한 예치금 없습니다.");
				return false;
			}
			
			var url = "/ccs/common/deposit/refund/ajax";
			ccs.layer.open(url, "", "depositRefundLayer", function() {
//				mypage.deliveryAddress.fnChkStyle();
//				mypage.deliveryAddress.fnSelectStyle();
			});
		},
		refundRequest : function() {
			
			// TODO 환불 신청시 유효성 검사
			// TODO 입력된 정보에서 수정 후 환불 신청할때 인증여부 검사 로직 필요 
			
			var businessCd = $("#bankName").next().find('option:selected').val();
			var businessNm = $("#bankName").next().find('option:selected').attr('name');
			var accountNo = $("#accountNo").val();
			var name = $("#name").val();
			
			if(name != $("#chkName").val()) {
				alert("환불계좌 정보들이 변경되어 재인증이 필요합니다.");
				return false;
			} else if( businessCd != $("#chkBankCd").val() ) {
				alert("환불계좌 정보들이 변경되어 재인증이 필요합니다.");
				return false;
			} else if( accountNo != $("#chkAccountNo").val()) {
				alert("환불계좌 정보들이 변경되어 재인증이 필요합니다.");
				return false;
			} else if( $("#autDt").attr('class') != "selected") {
				alert("동의여부를 체크해주세요");
				return false;
			}
			
			common.showLoadingBar();
			
			var url = "/api/mms/mypage/payment";
			var data = {
					paymentBusinessCd : businessCd
					, paymentBusinessNm : businessNm
					, refundAccountNo : accountNo
					, accountHolderName: name
			};
			mms.common.restfulTrx(url, "POST", data, function(res) {
				if(res == 'SUCCESS') {
					alert('예치금 환불신청을 완료하였습니다.');
					mypage.deposit.search();
					$("#customerDepositAmt").text("0");
					$("#layerClose").click();
				} else {
					alert('예치금 환불신청에 실패하였습니다.')
				}
			});
		},
		layerClose : function() {
			$("#layerClose").click();
		}
	},
	/***************************************************************
	 * 내가 문의한 글 (1:1문의, 상품 Q&A  Page) 
	 ***************************************************************/
	inquiry : {
		tab : function (type) {
			$("#pageType").val(type);
			mypage.inquiry.search();
		},
		search : function(content, listType, isScroll) {
			
			if(isScroll == undefined) {
				isScroll = false;
			}
			
			// list 분류 선택 표시
			mms.common.typeExpression(content);
			
			
			// tab 처리
			var type = $("#pageType").val();
			if (type == "MYQA") {
				if(!isScroll) {
					$(".tbl_article").children().remove();
				}
				$(".tab_con").removeClass("tab_02 tab_conOn");
				
				$(".tab_con").addClass("tab_01 tab_conOn");
				$("#inquiryTab > li").eq(0).addClass("on");
				
				$("#qaBtn").attr("style","visibility: visible");
				
			} else {
				if(!isScroll) {
					$(".tbl_qa").children().remove();
				}
				$(".tab_con").removeClass("tab_01 tab_conOn");
				
				$(".tab_con").addClass("tab_02 tab_conOn");
				$("#inquiryTab > li").eq(1).addClass("on");
				
				$("#qaBtn").attr("style","visibility: hidden")
			}
			
			// 전체, 완료, 대답준비중 listing
			var inquiryListType = "";
			if(listType != undefined) {
				inquiryListType = listType;
			}
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var url ="";
			if(type == "MYQA") {
				url = "/mms/mypage/inquiry/myqa/list/ajax";
			} else if (type == "PRODUCT") {
				url = "/mms/mypage/inquiry/product/list/ajax";
			}
			
			var data = {};
			if(isScroll) {
				$("#scrollInfo").remove();
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				data = {startDate : startDate, endDate: endDate, pageType: type, saveType: inquiryListType, currentPage: $("input[name=currentPage]").val(), isScroll : 'Y'};
				mms.common.paging(url, data, mypage.inquiry.moInquiryCallback);
			} else {
				$("input[name=currentPage]").val(Number(1));
				data = {startDate : startDate, endDate: endDate, pageType: type, saveType: inquiryListType, isScroll : 'N'};
				mms.common.paging(url, data, mypage.inquiry.inquiryCallback);
			}
			
		},
		inquiryCallback : function(res) {
			if ($("#pageType").val() == 'MYQA') {
				$(".tbl_qa").html('');
				$(".tbl_article").html(res);
			} else if ($("#pageType").val() == 'PRODUCT') {
				$(".tbl_article").html('');
				$(".tbl_qa").html(res);
			}
			
			mms.common.setCnt();
			mypage.inquiry.writeBtn();
			$("#pageType").val($("#hPageType").val());
		},
		moInquiryCallback : function(res) {
			setEvent();
			
			if ($("#pageType").val() == 'MYQA') {
				$("#myqaUl").append(res);
			} else if ($("#pageType").val() == 'PRODUCT') {
				$("#productUl").append(res);
			}
			
			mypage.inquiry.writeBtn();
			console.log($("#total_sub").val());
			console.log($("#item1_sub").val());
			$("#totalCnt").text(" ("+$("#total_sub").val()+")");
			$("#item1Cnt").text(" ("+$("#item1_sub").val()+")");
			$("#totalCount").val($("#totalCount_sub").val());
			$("#pageType").val($("#hPageType_sub").val());
		},
		deleteInquiry : function(type, pk) {
			var url = "/api/mms/mypage/inquiry/delete";
			var data = {pageType: type, inquiryNo: pk};
			
			mms.common.restfulTrx(url, "POST", data, function(res) {
				alert("삭제가 완료 되었습니다");
				if($(".sortBox li").eq(0).attr("class") != "") {
					mypage.inquiry.search("else");
				} else if($(".sortBox li").eq(1).attr("class") != "") {
					mypage.inquiry.search("else", "COMPLETE");
				}
			});
			
		},
		writeBtn : function() {
			//마이페이지 - 1:1문의
			$(".qArea").find(">a").off("click").on("click", function(){
				if($(this).hasClass("on")){
					$(this).removeClass("on");
					$(this).closest(".tr_box").next(".aArea").hide();
				} else {
					$(".qArea").find(">a").removeClass("on");
					$(".aArea").hide();
					$(this).addClass("on");
					$(this).closest(".tr_box").next(".aArea").show();
				}
			});
		}
	},
	
	/***************************************************************
	 * 관심매장 설정 Page
	 ***************************************************************/
	offshop : {
		// 매장정보 팝업
		offshopInfo : function(offshopId) {
			var param = {"offshopId" : offshopId};
			ccs.layer.open("/ccs/offshop/layer", param, "offshopLayer");
		},
		
		// 매장 지도
		roadMap : function(divId, latitude, longitude) {
			common.map(divId, latitude, longitude);
		},
		
		// 관심매장 편집
		editOffshop : function() {
			$("[name=bfEdit]").hide();
			$("[name=ingEdit]").show();
		},
		
		// 편집한 관심매장 정보 저장
		saveOffshop : function() {
			if (confirm("저장하시겠습니까?")) {
				var data = [];
				
				data.push({
					offshopName : $(".mystore .default_store #offshopName").text(),
					topYn : "Y",
					interestYn : "Y"
				});
				
				var favoriteLen = $(".mystore .favorite_store").find(".storeAddr").length;
				for (var i=0; i<favoriteLen; i++) {
					data.push({
						offshopName : $(".mystore .favorite_store").find(".storeAddr:eq("+i+") #offshopName").text(),
						sortNo : i + 1,
						topYn : "N",
						interestYn : "Y"
					});
				}
				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/api/ccs/offshop/update/interest",
					type : "POST",
					data : JSON.stringify(data)
				}).done(function(response) {
					alert("저장되었습니다.");
					ccs.link.mypage.info.offshop();
				});
				
				$("[name=bfEdit]").show();
				$("[name=ingEdit]").hide();
			}	
		},
		
		// 관심매장 해제
		deleteInterested : function(offshopId) {
			var data = [];
			data.push({
				offshopId : offshopId,
				interestYn : 'N'
			});
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/ccs/offshop/update/interest",
				type : "POST",
				data : JSON.stringify(data)
			}).done(function(response) {
				alert("관심 매장에서 해제되었습니다.");
				ccs.link.mypage.info.offshop();
			});
		},
		
		// 관심매장 조회
		getInterestOffshop : function(allOffshopYn) {			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/mms/mypage/offshop/list",
				type : "POST",
				data : JSON.stringify({"allOffshopYn": allOffshopYn})
			}).done(function(response) {
				if (allOffshopYn == "Y") {
					return response.offshopList;
				}
			});
		},
		
		// 대표매장 설정
		setTopOffshop : function() {
			if (confirm("대표 매장으로 설정하시겠습니까?")) {
				$(".mystore .default_store li:first").find("[name=ingEdit]").remove(); /* '대표매장 설정' 버튼 숨기기 */
				
				var favoriteLen = $(".mystore .favorite_store").find(".storeAddr").length;
				for (var i=0; i<favoriteLen; i++) {
					if (!$(".mystore .favorite_store").find(".storeAddr:eq("+i+") .box_01 a").hasClass("btn_default")) {
						var strHtml = "<a href='#none' class='btn_sStyle1 sPink1 btn_default' name='ingEdit' onclick='mypage.offshop.setTopOffshop();'>대표매장설정</a>";
						strHtml += "<div class='btn_sorting' name='ingEdit' style='margin-left:4px'>";
						strHtml += "	<a href='#none' class='btn_up'>위로</a>";
						strHtml += "	<a href='#none' class='btn_dw'>아래로</a>";
						strHtml += "</div>";
						$(".mystore .favorite_store li:first .box_01").append(strHtml);	/* '대표매장 설정' 버튼 만들기 */
					}
				}
				
				alert("확인을 누르면 저장됩니다.");
			}	
		}
	},
	
	/********************************************************
	 * 오프라인 구매내역
	 ********************************************************/
	offlineOrder : {
		// 지역변경
		changeRegion : function(obj) {
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/mms/mypage/offShopBranch/list/ajax",
				type : "POST",		
				data : JSON.stringify({"areaDiv1":obj.value}),
			}).done(function(data){
				var innerHtml = "<option>지점</option>";
				for (var i=0; i < data.branchList.length; i++) {
					innerHtml += "<option>" + data.branchList[i].name + "</option>"; 
				}
				$("#selBranch").html(innerHtml);
				$("#branchLabel").text("지점");
				
				mypage.offlineOrder.getOffOrderList(obj.value, "");
			});
		},
		changeBranch : function(obj) {
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/mms/mypage/offShopBranch/list/ajax",
				type : "POST",		
				data : JSON.stringify({"areaDiv1":obj.value}),
			}).done(function(data){
				
				mypage.offlineOrder.getOffOrderList("", obj.value);
			});
		},
		// 오프라인 구매내역 조회
		getOffOrderList : function(param1, param2) {
			if (param1 != "지점" && param2 != '지점') {
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/mms/mypage/offlineOrder/list/ajax",
					type : "POST",		
					data : JSON.stringify({"areaDiv1":param1, "branchName" : param2}),
				}).done(function( html ) {
					mypage.offlineOrder.listCallback(html);
				});
			}
		},
		// 오프라인 구매내역 날짜 조회
		getOffOrderSearchList : function() {
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/mms/mypage/offlineOrder/list/ajax",
				type : "POST",		
				data : JSON.stringify({"startDate":startDate, "endDate" : endDate}),
			}).done(function( html ) {
				mypage.offlineOrder.listCallback(html);
			});
		},
		// SET AJAX RETURN HTML
		listCallback : function(html) {
			$("#offOrderListDiv").html(html);
		},
		
		offOrderDetail : function(param) {
			data = {orderId : param};
			ccs.layer.offlineOrderLayer.open(data, null);
		}
	},
	
	/***************************************************************
	 * 환불계좌 관리
	 ***************************************************************/
	refund : {
		// 계좌 인증
		accountCertify : function($this) {
//			var url = "/api/mms/mypage/payment/account/certify";
			var url = "/api/ccs/guest/payment/account/certify";
			if (!common.isEmpty($this)) {
				var frmObj = $this.closest('form');
				if (!frmObj.find('input:checkbox').is(':checked')) {
					alert("환불계좌 수집/설정을 동의하지 않으셨습니다.");
					return false;
				}
				var data = {};
				var tmpArray = frmObj.serializeArray();
				for (var i = 0; i < tmpArray.length; i++) {
					var tmp = tmpArray[i];
					data[tmp['name']] = tmp['value'];
				}
				mms.common.restfulTrx(url, "POST", data, function(response) {
					alert(response.resultMessage);
					console.log('response : ',response);
					if (response.success) {
						frmObj.children('.chkBox').val(data.accountAuthDt);
						frmObj.children('.chkBox').hide();
						$this.hide();
					}
				});
			} else {
				var data = mypage.refund.depositSetData();
				
				if(!mypage.refund.validCheck(data)) {
					return false;
				};
				
				mms.common.restfulTrx(url, "POST", data, function(res) {
					// TODO 인증 성공여부 체크
					console.log(res);
					if(res.resultCode == '0000') {
						alert(res.resultMessage);
						$("#chkName").val(data.accountHolderName);
						$("#chkBankCd").val(data.paymentBusinessCd);
						$("#chkAccountNo").val(data.refundAccountNo);
						
						$("#isAuth").val(res.resultCode);
					} else {
						alert(res.resultMessage);
						$("#isAuth").val(res.resultCode);
					}
				});
			}
		},
		regRefundAccount : function() {
			var data = mypage.refund.depositSetData();
			
			if(!mypage.refund.validCheck(data, "save")) {
				return false;
			};
			
			
			var url = "/api/mms/mypage/refundAccount/reg"
			
			
			// TODO 인증 성공여부 체크
			
			mms.common.restfulTrx(url, "POST", data, function(res) {
				if(res == 'SUCCESS') {
					alert("환불계좌 등록이 정상적으로 처리되었습니다.");
					$("#chkName").val(data.paymentBusinessNm);
					$("#chkBankCd").val(data.paymentBusinessCd);
					$("#chkAccountNo").val(data.refundAccountNo)
				} else {
					alert("등록할 계좌의 예금주명과 회원정보가 일치하지 않습니다.")
				}
//				console.log(res);
			});
			
		}, 
		depositSetData : function() {
			var businessCd = $("#bankName").next().find('option:selected').val();
			var businessNm = $("#bankName").next().find('option:selected').attr('name');
			console.log(businessNm);
			var accountNo = $("#accountNo").val();
			var name = $("#name").val();
			
			var data = {paymentBusinessCd : businessCd, paymentBusinessNm : businessNm, refundAccountNo : accountNo, accountHolderName: name};
			
			return data;
		},
		validCheck : function(data, type) {
			if(common.isEmpty(data.accountHolderName)) {
				alert("환불계좌 예금주명을 입력해주세요.");
				$("#name").focus();
				return false;
			}
			if(data.paymentBusinessNm == undefined) {
				alert("환불계좌 은행을 선택해주세요.");
				$('.select_box1').focus();
				return false;
			}
			if(common.isEmpty(data.refundAccountNo)) {
				alert("환불계좌 계좌번호를 입력해주세요.");
				$("#accountNo").focus();
				return false;
			}
			
			if(type != undefined) {
				if($("#isAuth").val() != '0000') {
					alert("환불계좌를 인증해 주세요")
					return false;
				} else {
					// 인증후 계좌정보 입력 변경해서 등록 할 때
					if(data.accountHolderName != $("#chkName").val()) {
						
						alert("환불계좌 예금주명이 변경되었습니다. 인증을 다시해주세요.");
						$("#name").focus();
						return false;
					}
					if($('.select_box1 option:selected').val() != $("#chkBankCd").val()) {
						
						alert("환불계좌 은행이 변경되었습니다. 인증을 다시해주세요.");
						$('.select_box1').focus();
						return false;
					}
					if(data.refundAccountNo != $("#chkAccountNo").val()) {
						
						alert("환불계좌 계좌번호가 변경되었습니다. 인증을 다시해주세요.");
						$("#accountNo").focus();
						return false;
					}
				}
				
				if(!$("input:checkbox[name='accountAuthDt']").is(":checked")){
					alert("환불계좌 수집/설정을 동의하지 않으셨습니다.");
					$("input:checkbox[name='accountAuthDt']").focus();
					return false;
				}
			}
			
			return true;
		}
		
	},
	
	/********************************************************
	 * 모바일 마이 메뉴
	 ********************************************************/
	myMenu : {
		// 마이메뉴 저장
		save : function() {
			var data = [];
			var menuIds = [];
			$("#selectMenu_ul > li").each(function() {   
				data.push({
					menuId : $(this).children("input").attr("id").replace("select_", ""),
					sortNo : $(this).index() + 1
				});
				menuIds.push($(this).children("input").attr("id").replace("select_", ""));
			});
			
			console.log(data);
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/mms/mypage/myMenu/save",
				type : "POST",
				data : JSON.stringify(data)
			}).done(function(response) {
				alert("저장되었습니다.");
				mypage.myMenu.saveCallback(menuIds);
			});
		},
		
		// 저장된 퀵메뉴로 변경 
		saveCallback : function(menuIds) {
			console.log(menuIds);
			var strHtml = "";
			for (var i=0; i < menuIds.length; i++) {
				
				strHtml += '<li>';
				strHtml += '<a href="'+ $("input[name=url_"+menuIds[i]+"]").val() +'">'+ $("input[name=url_"+menuIds[i]+"]").next("label").text() +'</a>';
				strHtml += '</li>';
			}
			$(".setup_menu li:not(:first)").remove();
			
			if (menuIds.length < 5) {
				$(".setup_menu > li").eq(0).find("b").text("+메뉴추가");
			} else {
				$(".setup_menu > li").eq(0).find("b").text("+메뉴변경");
			}
			
			$(".setup_menu").append(strHtml);
		},
		
		clickMenuEvent : function() {
			// 마이페이지 퀵메뉴 설정
			$(".mobile #menu_ul_List .menu_sort .inp_chk").off("click").on({
				"click" : function(e) {
					if (!$(this).is(":checked")) {
						var id = $(this).attr("id"); 
						$("#select_"+id).parent("li").remove();
						
						if ($("#selectMenu_ul > li").length == 0) {
							var noData = '<li class="noData_tp2">선택한 메뉴가 없습니다.</li>';
							$("#selectMenu_ul").append(noData);
						}
						
						if ($("#menu_ul_List").find("li").children("input:checked").length < 6) {
							
							$(".menu_sort").children("li").children("input:not(:checked)").each(function() {
								$(this).attr("disabled", false);
							});
						}
						
					} else {
						if ($("#menu_ul_List").find("li").children("input:checked").length < 6) {
							
							var innerHtml = "<li>";
							innerHtml += '<input type="checkbox" value="" title="선택" name="select_'+$(this).attr("id")+'" id="select_'+$(this).attr("id")+'" class="inp_chk" checked="checked" onclick="mypage.myMenu.deleteMymenu(this)" />';
							innerHtml += '<input type="hidden" name="select_url_'+$(this).attr("id")+'" value="'+$(this).next().val()+'"/>';
							innerHtml += '<label for="select_'+$(this).attr("id")+'">'+$(this).next().next().text()+'</label>';
							innerHtml += '<button type="button" class="btn_move">이동</button>';
							innerHtml += "</li>";
							
							if ($("#selectMenu_ul > li").hasClass("noData_tp2")) {
								$("#selectMenu_ul > li").eq(0).remove();
							}
							
							$("#selectMenu_ul").append(innerHtml);
							
						} else {
							$(this).prop("checked", false);
							alert("메뉴는 최대 5개까지 등록가능합니다.");
							
							$(".menu_sort").children("li").children("input:not(:checked)").each(function() {
								$(this).attr("disabled", true);
							})
						}
					} 					
				}
			});
		}, 
		
		deleteMymenu : function(param) {
			var id = $(param).attr("id").replace("select_", "");
			$("#"+id).attr("checked", false);
			$(param).parent("li").remove();
			
			if ($("#selectMenu_ul > li").length == 0) {
				var noData = '<li class="noData_tp2">선택한 메뉴가 없습니다.</li>';
				$("#selectMenu_ul").append(noData);
			}
			
			if ($("#menu_ul_List").find("li").children("input:checked").length < 6) {
				
				$(".menu_sort").children("li").children("input:not(:checked)").each(function() {
					$(this).attr("disabled", false);
				});
			}
		}
	},
	
	/********************************************************
	 * 이벤트 참여내역 page
	 ********************************************************/
	event : {
		// 이벤트 참여내역 조회
		search : function(content, param) {
			
			// list 분류 선택 표시
			mms.common.typeExpression(content);
			
			var eventStateCd = "";
			if (param != undefined) {
				if (param == "RUN") {
					eventStateCd = "EVENT_STATE_CD.RUN";
				} else if (param = "STOP") {
					eventStateCd = "EVENT_STATE_CD.STOP";
				}
			}
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var url = "/mms/mypage/eventjoin/ajax";
			var data = {"startDate" : startDate, "endDate" : endDate, "eventStateCd" : eventStateCd};
			
			mms.common.paging(url, data, mypage.event.eventCallback);
		},
		
		// SET AJAX RETURN HTML
		eventCallback : function(html) {			
			$("#eventJoinHistoryDiv").html(html);
			mms.common.setCnt();
		}
	},
	
	/********************************************************
	 * 선물함 관리
	 ********************************************************/
	gifticon : {
		// 
		getMemberGiftOrderList : function() {
			
			param = {giftPhone : $("#giftPhone").val()};
			
			common.showLoadingBar();
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/mms/mypage/order/gift/list/ajax",
				type : "POST",		
				data : JSON.stringify(param),
			}).done(function( html ) {
				mypage.gifticon.listCallback(html);
				common.hideLoadingBar();
			});
		},
		getMemberGiftOrderSearchList : function() {
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var giftPhone = $("#giftPhone").val();
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/mms/mypage/order/gift/list/ajax",
				type : "POST",		
				data : JSON.stringify({"startDate":startDate, "endDate" : endDate, "giftPhone" : giftPhone}),
			}).done(function( html ) {
				mypage.gifticon.listCallback(html);
			});
		},
		
		listCallback : function(html) {
			$("#giftOrderDiv").html(html);
		},
		// 선물함 상세
		giftOrderDetail : function(param) {
			window.location.href = '/mms/mypage/order/giftDetail?orderId='+ encodeURIComponent(param);
		},
	
		// 인증번호 발송
		sendCertNum : function() {
			
			if ($("#cell_num1").val() == "" || $("#cell_num2").val() == "" || $(".select_box1").children("select").val() == "") {
				alert("휴대폰 번호를 입력해주세요.");
				return;
			}
			var giftPhone = $(".mo_certify .select_box1").children("select").prev().text() +"-"+ $("#cell_num1").val() +"-"+ $("#cell_num2").val();
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/mms/mypage/order/giftSendAuthSms",
				type : "POST",
				data : JSON.stringify({"giftPhone" : giftPhone}),
				success : function(response) {				 				
					alert("발송하였습니다.");
				},
				error : function(response) {
					alert(response.MESSAGE);
				}
			});
			
		}, 
		// 선물합 이동
		goToGiftPage : function() {
			
			if ($("#authNumber").val() == '') {
				alert("인증번호를 입력해주세요");
				return;
			}
			var giftPhone = $(".mo_certify .select_box1").children("select").prev().text()+"-"+$("#cell_num1").val()+"-"+$("#cell_num2").val();
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/mms/mypage/order/giftCheckAuthSms",
				data : JSON.stringify({"authNumber" : $("#authNumber").val()}),
				type : "POST",
				success : function(response) {				 				
					if (response) {
						$("#giftPhone").val(giftPhone);
						$('#giftForm').attr("action","/mms/mypage/order/gift");
						$('#giftForm').submit();
					} else {
						alert("잘못된 인증번호 입니다.");
					}
				},
				error : function(response) {
					alert(response.MESSAGE);
				}
			});
		},
		
		// 배송메세지 세팅
		setDeliveryMsg : function(param) {
			if ($(param).val() == "") {
				$("#deliveryMsg").val("");
				$("#deliveryMsg").closest("div").css("display", "block");
			} else {
				$("#deliveryMsg").closest("div").css("display", "none");
				$("#deliveryMsg").val($('option:selected', param).text());
			}
			
		},
		
		deliveryAddress : function() {
			ccs.layer.memberAddressLayer.open(function(data) {
				console.log(data);
				
				$("#defaultAddress").hide();
				$("#newAddress").show();
				
				$("#default").prop("checked", false);
				$("#new").prop("checked", true);
				
				$("#addr_name").val(data.deliveryName1);
				$("#addr_zipCd").val(data.zipCd);
				$("#addr_address1").val(data.address1);
				$("#addr_address2").val(data.address2);
				
				
				ccs.common.telset('deliveryPhone2', data.phone2);
				if (data.phone1 != '') {
					ccs.common.telset('deliveryPhone1', data.phone1);
				}
				$("#deliveryPhone2").val(data.phone2);
				$("#deliveryPhone1").val(data.phone1);
				$("#hidZipCd").val(data.zipCd);
				$("#hidAddress1").val(data.address1);
				$("#hidAddress2").val(data.address2);
				
				mypage.deliveryAddress.fnPlaceholder();
				
			});
		},
		
		// 배송요청하기
		deliveryApproval : function(param) {
			
			// 비로그인
			if (param == "") {
				// 비회원 구매 동의 체크 확인
				if(ccs.common.mobilecheck()) {
					if (!$("input[name=moAgreeCheck]").is(":checked")) {
						alert("비회원 구매 개인정보취급방침에 동의해주세요.");
						return;
					}
				} else {
					if (!$("input[name=pcAgreeCheck]").is(":checked")) {
						alert("비회원 구매 개인정보취급방침에 동의해주세요.");
						return;
					}
				}
			} 
				
			// TODO : VALIDATION
			if ( $("#addr_name").val() == "" ) {
				alert("받으실분 이름을 입력해주세요.");
				return;
			} 
			if ( $("#deliveryPhone2_areaCode").next().val() == "" || $("#deliveryPhone2_num1").val() == "" || $("#deliveryPhone2_num2").val() == "") {
				alert("휴대폰 번호를 입력해주세요.");
				return;
			} 
//			if ( $("#deliveryPhone1_areaCode").next().val() != "" || $("#deliveryPhone1_num1").val() != "" || $("#deliveryPhone1_num2").val() != "") {
//				alert("전화 번호를 입력해주세요.");
//				return;
//			} 
			if ( $("#addr_zipCd").val() == "" &&  $("#addr_address1").val() == "" && $("#addr_address2").val() == "") {
				alert("주소를 입력해주세요.");
				return;
			}
			
			$("input[name=name1]").val($("#addr_name").val());
			$("input[name=phone1]").val($("#deliveryPhone2_areaCode").next().val() +"-"+ $("#deliveryPhone1_num1").val() +"-"+ $("#deliveryPhone1_num2").val());
			$("input[name=phone2]").val($("#deliveryPhone2_areaCode").next().val() +"-"+ $("#deliveryPhone2_num1").val() +"-"+ $("#deliveryPhone2_num2").val());
			$("input[name=hidZipCd]").val($("#addr_zipCd").val());
			$("input[name=hidAddress1]").val($("#addr_address1").val());
			$("input[name=hidAddress2]").val($("#addr_address2").val());
			$("input[name=note]").val($("#deliveryMsg").val());
			
			
			var formData = $("#deliveryForm").serialize();
			console.log(formData);
			$.ajax({
				method: "POST",
				url: "/api/mms/mypage/order/gift/deleveryApproval",
				data: formData,
				success : function(response) {
					alert("배송 요청 되었습니다.");
					window.location.reload(true);
				},
				error : function(response) {
					alert(response.MESSAGE);
				}
			});
			
		}
	},
	
	/********************************************************
	 * 쇼핑찜 page
	 ********************************************************/
	wishlist : {
		
		// 쇼핑찜 리스트 조회
		search : function() {
			common.showLoadingBar();
			
			var url = "/mms/mypage/wishlist/ajax";					
			mms.common.paging(url, null, mypage.wishlist.wishlistCallback);
		},
		wishlistCallback : function(res) {
			$(".tbl_article").html(res);
			mms.common.setCnt();
			
			common.hideLoadingBar();
		},
		
		// 전체 삭제
		deleteAll : function() {
			if (confirm("상품을 정말로 삭제하시겠습니까?")) {
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/api/mms/mypage/wishlist/delete",
					data : JSON.stringify({"productId" : ""}),
					type : "POST"
				}).done(function(response) {
					alert("전체 삭제되었습니다.");
					mypage.wishlist.search();
				});
			};
		},
		
		/// 개별 삭제
		deleteItem : function(liIndex) {
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/mms/mypage/wishlist/delete",
				type : "POST",
				data : JSON.stringify({"productId" : $("#hidProductId" + liIndex).val()})
			}).done(function(response) {
				mypage.wishlist.search();
			});
		},
		
		// 장바구니 담기 & 바로 구매 - 옵션 선택 레이어
		openOptionLayer : function(part, liIndex) {
			if (part != undefined) {
				$("#hidCallbackPart").val(part);
			}
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/pms/product/option/select/ajax",
				type : "POST",
				data : JSON.stringify({productId: $("#hidProductId" + liIndex).val(), searchKeyword: part})
			}).done(function(html) {
				$('.wrap').append(html);
				
				if ($("#optionCnt").val() == "0") {
					mypage.wishlist.optionChoiceComp($("#selectedProduct").val());
				} else {
					if (ccs.common.mobilecheck()) {
						$("#optionSelectLayer").show();
						$(".header_mo, .mobile .content, .footer_mo").hide();
						
						var baseTop = $(document).height() / 2;
						var windowTop = $(window).scrollTop();
						
						if (windowTop == 0) {
							$("#optionSelectLayer").css({"paddingTop" : "100px"});
							$(window).scrollTop(baseTop);
						} else {
							if (baseTop >= windowTop) {
								$("#optionSelectLayer").css({"paddingTop" : windowTop + "px"});
								$(window).scrollTop(windowTop);
							} else {
								$("#optionSelectLayer").css({"paddingTop" : baseTop + "px"});
								$(window).scrollTop(baseTop);
							}
						}			
					} else {
						ccs.layer.fnPopPosition($("#optionSelectLayer"));
					}
				};		
			});
		},
		
		// 옵션 선택
		optionOnchange : function(index) {
			var selectOption = $("#optionSelect" + index + " option:selected").text();
			$("#optionSelect" + index).parent().find("label").text(selectOption);					
		},
		
		// 옵션 선택 완료
		optionChoiceComp : function(productId) {
			var selectedOptions = [];
			
			for (var i=0; i<$("#optionSelectDiv").find("dl").length; i++) {
				var optionValue = $("#optionSelect" + i + " option:selected").text();
				if (optionValue == "선택") {
					optionValue = "없음";
				}
				selectedOptions.push({
					optionName : $("#optionSelect" + i).parent().parent().parent().find("dt").text(),
					optionValue : optionValue
				});
			};
						
			var param = {
					productId : productId,
					selectedOptions : selectedOptions
			};
			
			var saleproductInfo = {};
			pms.optioncombo.getSaleproductInfo(param, function(data) {
				saleproductInfo = data;
				
				var deliveryCnt = null;
				var deliveryPeriodCd = null;
				var deliveryPeriodValue = null;
				
				var part = $("#hidCallbackPart").val();
				if (part == "cart") {
					
					
					var carts = [];
					var omsCarts = [];
					
					var cart = new oms.Cart(saleproductInfo);
					carts.push(cart);
					
					oms.saveCartList(carts, function(response) {
						if (confirm("상품이 장바구니에 담겼습니다. 장바구니로 이동하시겠습니까?")) {
							ccs.layer.close('optionSelectLayer');
							common.pageMove('cart', '', '');
						} else {
							ccs.layer.close('optionSelectLayer');
						}
					});
					
				} else if (part == "order") {
					var orders = [];
					var omsOrderproducts = [];
					
					var order = new oms.Order(saleproductInfo);
					orders.push(order);
					
					oms.directOrderList(orders);
				}
			});	
		}
		
	},
	/********************************************************
	 * 상품평 page
	 ********************************************************/
	review : {
		getReviewList : function(){
			var param = {};
			$.get( "/mms/mypage/product/list/ajax", param)
			.done(function( html ) {
				mypage.review.listCallbackProduct(html);
			});
			$("input[name=currentProductPage]").val(Number(1));
			$.get( "/mms/mypage/review/list/ajax", param)
			.done(function( html ) {
				mypage.review.listCallbackReview(html);
			});
		},
		
		pagingListCallback : function(){
			var data = {};
			
			if($("#mypageReviewTab").find("li:eq(0)").hasClass("on")){
				$("input[name=currentProductPage]").val(Number($("input[name=currentProductPage]").val())+ 1);
				here = "#productListDiv .div_tb_tbody3";
				url = "/mms/mypage/product/list/ajax";
				data = {currentPage: $("input[name=currentProductPage]").val(), isScroll:'true'};
			}else if($("#mypageReviewTab").find("li:eq(1)").hasClass("on")){
				$("input[name=currentReviewPage]").val(Number($("input[name=currentReviewPage]").val())+ 1);
				here = "#reviewListDiv .div_tb_tbody3";
				url = "/mms/mypage/review/list/ajax";
				data = {startDate : $('#startDate').val(), endDate : $('#endDate').val(), imgYn : $("#review_ImgYn").val(), currentPage: $("input[name=currentReviewPage]").val(), isScroll:'true'};
			}
			
			if(ccs.common.mobilecheck()) {
				 ccs.common.moLoadingBar(here);
			}
			
			$.get( url, data ).done(function( html ) {
				$(here).append(html);
				ccs.common.moLoadingBar();
			});
		},
		
		// 상품평 작성 가능한 리스트 ajax
		listCallbackProduct : function(html) {
			$("#productListDiv").html(html);
		},
		
		// 리뷰 리스트 ajax
		listCallbackReview : function(html) {
			$("#reviewListDiv").html(html);
		},
		
		// 리뷰 검색
		reviewSearch : function(){
			//console.log({startDate : $('#startDate').val(), endDate : $('#endDate').val(), imgYn : $("#review_ImgYn").val()});
			$("input[name=currentReviewPage]").val(Number(1));
			$.get( "/mms/mypage/review/list/ajax", {startDate : $('#startDate').val(), endDate : $('#endDate').val(), imgYn : $("#review_ImgYn").val()})
			.done(function( html ) {
				mypage.review.listCallbackReview(html);
			});
		},
		
		// 리뷰 이미지 조건 검색
		imgYnSearch : function(param){
			$("#review_ImgYn").val(param);
			mypage.review.reviewSearch();
		},
		
		// 상품평 작성 레이어 팝업 호출
		insertReview : function(params){
			ccs.layer.reviewLayer.open(params, function(callback) {
				if(callback == 'SUCCESS'){
					mypage.review.getReviewList();
				}
			});
		},
		
		// 상품평 수정 레이어 팝업 호출
		updateReview : function(reviewNo, productId){
			var params = {reviewNo : reviewNo, productId : productId};
			ccs.layer.reviewLayer.open(params, function(callback) {
				if(callback == 'SUCCESS'){
					mypage.review.reviewSearch();
				}
			});
			
		},
		
		// 상품평 삭제 호출
		deleteReview : function(reviewNo, productId){
			var params = {reviewNo : reviewNo, productId : productId, displayYn : 'N'};
			
			// 전시 여부 상태를 변경한다.
			
			if(!confirm("삭제하시겠습니까?")){
				return;
			}
			
			$.ajax({
				url : "/api/mms/mypage/review/delete",
				contentType : "application/json; charset=UTF-8",
				type : "POST",
				data : JSON.stringify(params)
			}).done(function(response) {
				if (response.RESULT == "SUCCESS") {
					alert("삭제되었습니다.");
				} else {
					alert(response.MESSAGE);
				}
			});
		},
		// 상품평 이미지 지우기
		imageDelete : function(index){
			$('[name=img'+index+']').val('');
			$('#reviewImageIndex'+index).remove();
		}
	},
	
	/********************************************************
	 * 영수증 조회 -- TODO mobile scroll
	 ********************************************************/
	receipt : {
		search : function (isScroll) {
			
			var orderId = '';
			if(common.isNotEmpty($('#orderId').val())) {
				orderId = $('#orderId').val();
			}
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var url = "/mms/mypage/receipt/list/ajax";
			
			var data = {};
			if(isScroll) {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				data = {startDate : startDate, endDate: endDate, orderId: orderId, currentPage: $("input[name=currentPage]").val()};
				mms.common.paging(url, data, mypage.receipt.moReceiptCallback);
			} else {
				$("input[name=currentPage]").val(Number(1));
				data = {startDate : startDate, endDate: endDate, orderId: orderId};
				mms.common.paging(url, data, mypage.receipt.receiptCallback);
			}
			
			if(ccs.common.mobilecheck()) {
				common.showLoadingBar();
			}
		},
		receiptCallback : function(res) {
			$(".div_tb_tbody3").html(res);
			mms.common.setCnt();
			common.hideLoadingBar();
		},
		moReceiptCallback : function(res) {
			$(".div_tb_tbody3").append(res);
			mms.common.setCnt();
			common.hideLoadingBar();
		}
	},
	
	/********************************************************
	 * 최근 본 상품
	 ********************************************************/
	latestProduct : {
		search : function() {
			var url = "/mms/mypage/latestProduct/list/ajax";
			mms.common.paging(url, {}, mypage.latestProduct.latestProductCallback);
		},
		latestProductCallback : function(res) {
			$(".tbl_article").html(res);
			mms.common.setCnt();
		},
		deleteLatestProduct : function(id) {
			if(id == undefined) {
				id = "ALL";
			}
			var url = "/api/mms/mypage/latestProduct/delete"
			$.ajax({
				url : url,
				contentType : "application/text; charset=UTF-8",
				type : "POST",
				data : id
			}).done(function(response) {
				if(id == "ALL") {
					ccs.link.mypage.activity.latestProduct();
					dmspc.skyScraper.getSkyScraper();
				} else {
					mypage.latestProduct.search();
					dmspc.skyScraper.getSkyScraper();
				}
			});
		}
	},
	
	/********************************************************
	 * MO 히스토리
	 ********************************************************/
	history : {
		search : function() {
			var url = "/mms/mypage/history/list/ajax";
			mms.common.paging(url, {}, mypage.history.historyCallback);
		},
		historyCallback : function(res) {
			$(".rw_tb_tbody3").html(res);
		},
		deleteHistory : function(id) {
			if(id == undefined) {
				id = "ALL";
			}
			var url = "/api/mms/mypage/history/delete"
			$.ajax({
				url : url,
				contentType : "application/text; charset=UTF-8",
				type : "POST",
				data : id
			}).done(function(response) {
					alert("삭제되었습니다.");
					mypage.history.search();
			});
		}
	},
	
	/********************************************************
	 * 스타일 관리
	 ********************************************************/
	style : {
		
		// 회원 스타일 목록 조회
		getMemberStyleList: function(param) {
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/mms/mypage/activity/style/list/ajax",
				type : "POST",		
				data : JSON.stringify({"sortKeyword" : param}),
			}).done(function( html ) {
				mypage.style.listCallback(html);
			});
			
		},
		
		// SET AJAX RETURN HTML
		listCallback : function(html) {
			$("#styleList").html(html);
		},
		
		deleteStyle : function(param1) {
			if (confirm("해당 스타일을 삭제 하시겠습니까?")){
				
				param = {styleNo : param1};
				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/api/mms/mypage/style/delete",
					type : "POST",		
					data : JSON.stringify(param),
				}).done(function( response ) {
					alert("삭제 되었습니다.");
					window.location.reload(true);
				});
			}
		},
		
		
		// 스타일 정렬 변경
		changeSort : function(param) {
			mypage.style.getMemberStyleList(param.value);
		}, 
		
		// 스타일 상세
		styleDetailLayer : function(param1, param2) {
			param = {styleNo : param1, idShowYn : 'N', memberNo : param2};
			var url = "/ccs/common/styleDetail/layer";
			
			ccs.layer.open(url, param, "styleLayer", function() {
				
			});
		},
		
		// 스타일 만들기
		makeAndModifyStyle : function(param1, param2) {
			var item = {
					"id":param1	// 신규일때는 null
					,"memberId":param2	// 신규일때는 null
				};
				var method = "styleshop";
				var json = encodeURIComponent(JSON.stringify(item));
				var applink = "app://" + method + "?json=" + json;
				window.location.href = applink;
		},
	},
	/***************************************************************
	 * 의류 AS 현황 Page
	 ***************************************************************/
	as : {
		search : function(isScroll) {
			
			if(isScroll == undefined) {
				isScroll = false;
			}
			
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			var url = "/mms/mypage/clothesAs/list/ajax";
			
			var data = {};
			if(isScroll) {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				
				data = {startDate : startDate, endDate: endDate, currentPage: $("input[name=currentPage]").val()};
				mms.common.paging(url, data, mypage.as.moAsCallback);
			} else {
				$("input[name=currentPage]").val(Number(1));
				
				data = {startDate : startDate, endDate: endDate};
				mms.common.paging(url, data, mypage.as.asCallback);
			}
		},
		asCallback : function(res) {
			$(".div_tb_tbody3").html(res);
			mms.common.setCnt();
		},
		moAsCallback : function(res) {
			btnListTypeEvt();
			$(".div_tb_tbody3").append(res);
			mms.common.setCnt();
		}
	},
}	
