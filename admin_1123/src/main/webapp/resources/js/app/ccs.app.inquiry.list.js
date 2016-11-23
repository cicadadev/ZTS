

var inquiryManagerApp = angular.module("inquiryManagerApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

inquiryManagerApp.controller("inquiryManagerController", function($window, $scope, $filter, inquiryService, commonService, gridService, commonPopupService){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	$scope.search = {};
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	// PO일경우 업체ID
	var poBusinessId = global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	$scope.periodType = [
	                        {val : 'QNA_DATE',text : '문의등록일'}, 
	                        {val : 'ANSWER_DATE',text : '답변등록일'},
	                        {val : 'CONFIRM_DATE',text : '문의확인일'}
	                        ];
	$scope.serchType = [
	                    {val : 'MEM_NAME',text : '고객명'},
	                    {val : 'QNA_NAME',text : '문의등록자'},
	                    {val : 'ANS_NAME',text : '답변등록자'},
	                    {val : 'CONTENT',text : '내용'},
	                    {val : 'TITLE',text : '제목'} 
	                   ];
	
	angular.element(document).ready(function () {		
//		$scope.search.startDate = $filter('date')(new Date(), Constants.date_format_2);
//		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);
		commonService.init_search($scope,'search');
	});
	
//	cellGradeStr = "<div class=\"ui-grid-cell-contents\"  title=\"\">{{row.entity.mmsMember.mmsMemberZts.memGradeName == undefined?'비회원':row.entity.mmsMember.mmsMemberZts.memGradeName}}</div>"; 
	
	var columnDefs =  [
               { field: 'inquiryNo', 					width : '100',	colKey: "ccsInquiry.inquiryNo",		linkFunction : 'popup.detail'},
               { field: 'title', 						width : '100',	colKey: "ccsInquiry.title",			linkFunction : 'popup.detail'},
               { field: 'inquiryChannelName', 			width : '100',	colKey: "c.ccs.qna.channel"},
               { field: 'memberId', 					width : '100',	userFilter :'memberId,memberName',	colKey: "c.ccs.inquiry.permit.custom"},
               { field: 'mmsMemberzts.memGradeName', 	width : '100',	colKey: "c.mmsMember.memberGrade" , cellFilter : 'gradeNvl'},
               { field: 'inquiryTypeName', 				width : '100',	colKey: "c.ccs.qna.qnaType"},
               { field: 'pmsBrand.name', 				width : '100',	displayName : '문의한 브랜드'},
               { field: 'inquiryStateName', 			width : '100',	colKey: "c.ccs.qna.state"},
               { field: 'insId', 						width : '200',	userFilter :'insId,insName',	colKey: "c.ccs.inquiry.ins.custom"},
               { field: 'insDt', 						width : '200',	colKey: "c.ccs.inquiry.insDt"},
               { field: 'confirmId', 					width : '100',	userFilter :'confirmId,confirmName',	colKey: "c.ccs.inquiry.confirmId"},
               { field: 'confirmDt', 					width : '200',	colKey: "c.ccs.inquiry.confirmDt"},
               { field: 'answerer', 					width : '100',	colKey: "c.ccs.inquiry.answerId"},
               { field: 'answerDt', 					width : '200',	colKey: "c.ccs.inquiry.answerDt"},
               { field: 'passTime', 					width : '100',	colKey: "c.ccs.qna.passTime"}
	       	]
   	var gridParam = {
   		scope : $scope,
   		gridName : "grid_inquiry",
   		url : '/api/ccs/inquiry',
   		searchKey : "search",
   		columnDefs : columnDefs,
   		gridOptions : {
   			checkBoxEnable : false
   		},
   		callbackFn : function() {
   		//myGrid.loadGridData();
   		}
   	};

	$scope.myGrid = new gridService.NgGrid(gridParam);	
	
	//=================== search	
	$scope.searchGrid = function(){
		$scope.myGrid.loadGridData();
	}
	
	//================= reset
	this.reset = function(){		
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	this.eraser = function(name){
		if(name === 'insInfo'){
			$scope.search.csUserId = "";
			$scope.search.csUserName = "";
		}else if(name === 'member'){
			$scope.search.memberLoginId = "";
		}else if(name === 'answerInfo'){
			$scope.search.answerId = "";
			$scope.search.answererName = "";
		}else if(name === 'business'){
			$scope.search.businessId = "";
			$scope.search.businessName = "";
		}
	}
	// 사용자 검색 팝업
	this.searchUserPopup = function(callback) {
		commonPopupService.userPopup($scope, callback, false);
	}
	
	// 회원 검색 팝업
	this.searchMemberPopup = function(type) {
		commonPopupService.memberPopup($scope,"callback_mem",false);
	}
	// 업체 검색 팝업
	this.searchBusinessPopup = function(){
		commonPopupService.businessPopup($scope,"callback_business",false);
	}
	
	// CS등록자(사용자)
	$scope.callback_ins = function(data) {
		$scope.search.csUserId = data[0].userId;
		$scope.search.csUserName = data[0].name;
		common.safeApply($scope);
	}
	// 문의고객(회원)
	$scope.callback_mem = function(data){
		$scope.search.memberNo = data[0].mmsMember.memberNo;
		$scope.search.memberLoginId = data[0].mmsMember.memberId;
		common.safeApply($scope);
	}
	// 답변자(사용자)
	$scope.callback_answer = function(data){
		$scope.search.answerId = data[0].userId;
		$scope.search.answererName = data[0].name;
		common.safeApply($scope);
	}
	
	//업체검색
	$scope.callback_business = function(data) {
		$scope.search.businessId = data[0].businessId;
		$scope.search.businessName = data[0].name;
		common.safeApply($scope);
	}
	
	$scope.inquiryNo = '';
	$scope.popup = {
		detail : function(field, row){
			$scope.inquiryNo = row.inquiryNo;
			$scope.type = 'D'
			popupwindow('/ccs/inquiry/popup/detail','inquiryDetailPopup',1000, 600);
		},
		insert : function(field, row){
			$scope.type = 'I'
			popupwindow('/ccs/inquiry/popup/detail','inquiryInsertPopup',1000, 600);
		},
		member : function(field, row){					
			commonPopupService.openMemberDetailPopup($scope, row.memberInfo);	
		}
		
	}
	// 문의등록자명 변경
    $scope.changeCsUserName = function(){
		$scope.search.csUserId = '';
	}	
	// 답변자명 변경
    $scope.changeAnswererName = function(){
		$scope.search.answerId = '';
	}	
    // 회원ID 변경
    $scope.changeMemberLoginId = function(){
		$scope.search.memberNo = '';
	}		    
			
			
}).filter('gradeNvl', function() {// null일때 기본값
	return function(input) { return input ? input :  '비회원' };		
}).controller("inquiryDetailController", function($window, $scope, $filter, inquiryService, commonService, gridService, commonPopupService){
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	$scope.ccsInquiry = {};
	$scope.search={};
	$scope.type = pScope.type;
	
	// PO일경우 업체ID
	var poBusinessId = global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	// 문의 상세 조회
	this.detail = function(){
		
		if($scope.type === 'D'){// 상세조회
			$scope.ccsInquiry.inquiryNo = pScope.inquiryNo;
			inquiryService.selectInquiry($scope.ccsInquiry, function(response) {
				$scope.ccsInquiry = response;
				$scope.ccsInquiry.confirmDt = $filter('date')($scope.ccsInquiry.confirmDt, Constants.date_format_1);
				$scope.ccsInquiry.answerDt = $filter('date')($scope.ccsInquiry.answerDt, Constants.date_format_1);
				$scope.ccsInquiry.insDt = $filter('date')($scope.ccsInquiry.insDt, Constants.date_format_1);
				if($scope.ccsInquiry.memberYn=='Y'){
					setMemberInfo(response.memberId, response.memberName, response.mmsMemberzts.memGradeName);
				}
			});
		}else{//신규등록
			$scope.ccsInquiry.inquiryChannelCd = 'INQUIRY_CHANNEL_CD.CALL';
			$scope.ccsInquiry.inquiryStateCd = 'INQUIRY_STATE_CD.ACCEPT';
			$scope.ccsInquiry.inquiryNo ='';
			
			if(!common.isEmpty($scope.poBusinessId)){
				$scope.ccsInquiry.businessId = $scope.poBusinessId;
			}
		}
//		console.log("$scope.ccsInquirye ", $scope.ccsInquiry);
	}
	
	// 검색 팝업 ( 회원, 업체, 주문상품 )
	this.searchPopup = function(type){
		if(type == 'member'){
			commonPopupService.memberPopup($scope, "callback_member", false);
		}else if(type == 'business'){
			
			commonPopupService.businessPopup($scope,"callback_business",false);
		}else if(type =='order'){
			
			if($scope.ccsInquiry.memberYn == 'Y'){
				$scope.search.ordererType = 'id';
				$scope.search.memberId = $scope.ccsInquiry.memberId;
				$scope.search.memberNo = $scope.ccsInquiry.memberNo;
			}else{
				$scope.search.ordererType = 'name';
				$scope.search.orderer = $scope.ccsInquiry.customerName;
			}
			commonPopupService.orderPopup($scope, 'callback_orderInfo', false);
		}
	}
	
	//업체검색 콜백
	$scope.callback_business = function(data) {
		$scope.ccsInquiry.businessId = data[0].businessId;
		$scope.ccsInquiry.ccsBusiness = {};
		$scope.ccsInquiry.ccsBusiness.name = data[0].name;
		common.safeApply($scope);
	}
	
	//회원검색 콜백
	$scope.callback_member = function(data){
		console.log(data);
		$scope.ccsInquiry.memberName = data[0].mmsMember.memberName;
		$scope.ccsInquiry.memberNo = data[0].memberNo;
		
		setMemberInfo(data[0].mmsMember.memberId, data[0].mmsMember.memberName, data[0].memGradeName);
			
		
		common.safeApply($scope);
	}
	
	// 주문상품 검색 콜백
	$scope.callback_orderInfo = function(data){
		$scope.ccsInquiry.omsOrderproduct={};
		$scope.ccsInquiry.productId = data[0].productId;
		$scope.ccsInquiry.orderId =  data[0].orderId;
		$scope.ccsInquiry.saleproductId = data[0].saleproductId;
		$scope.ccsInquiry.omsOrderproduct.saleproductName = data[0].saleproductName;
		$scope.ccsInquiry.omsOrderproduct.productName = data[0].productName;
		common.safeApply($scope);
	}
	
	var setMemberInfo = function(memberId, memberName, gradeName){
		$scope.ccsInquiry.member = memberId+'/'+memberName+'/'+gradeName;
	}
	
	// 문의 저장
	this.save = function(){
		
		if(!commonService.checkForm($scope.form2)){
			return;
		}
		
		/*//유효성 체크
		if($scope.ccsInquiry.memberYn === 'Y'){
			if($scope.ccsInquiry.memberNo === null || $scope.ccsInquiry.memberNo === ''){
				alert('회원정보가 존재하지 않습니다. 회원검색후 다시 시도해주세요.');
				return;
			}
		}else{
			if($scope.ccsInquiry.customerName === null || $scope.ccsInquiry.customerName === ''){
				alert('비회원 정보가 존재하지 않습니다. 입력후 다시 시도해주세요.');
				return;
			}
		}*/
		
		/*// 확인 메세지
		if(!confirm("저장하시겠습니까?")){
			return;
		}*/
		
		// 문의채널 : 신규등록은 모두 CALL
		if(common.isEmpty($scope.ccsInquiry.inquiryNo)){
			// 확인 메세지
			if(!confirm("문의를 생성하시겠습니까?")){
				return;
			}
			$scope.ccsInquiry.inquiryChannelCd = "INQUIRY_CHANNEL_CD.CALL";
		}else{
			// 확인 메세지
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
		}
		
		inquiryService.saveInquiry($scope.ccsInquiry, function(response) {
			if($scope.type === 'I'){
				pScope.inquiryNo = response.content;
				pScope.type = 'D';
				alert('문의가 생성되었습니다.');
			}else{
				alert($scope.MESSAGES["common.label.alert.save"]);
			}
//			pScope.myGrid.loadGridData();
			$window.location.reload();
		});
	}
	
	//문의 확인
	this.saveConfirm = function(){
		
		
		if(!$scope.ccsInquiry.inquiryNo){
			return;
		}

		if(!confirm($scope.ccsInquiry.inquiryStateName + " 상태를 문의확인 상태로 변경하시겠습니까?")){
			return;
		}
		
		var param = {inquiryNo : $scope.ccsInquiry.inquiryNo};
		inquiryService.saveConfirm(param, function(response) {
//				pScope.myGrid.loadGridData();
			alert('문의확인 상태로 변경되었습니다.');
			$window.location.reload();
		});
	}
	
	//답변 완료
	this.saveAnswer = function(){
		
		if(!$scope.ccsInquiry.inquiryNo){
			return;
		}
		
		if(!confirm($scope.ccsInquiry.inquiryStateName + " 상태를 답변완료 상태로 변경하시겠습니까?")){
			return;
		}
		
		if(!commonService.checkForm($scope.form3)){
			return;
		}		
		
		$scope.ccsInquiry.inquiryStateCd = 'INQUIRY_STATE_CD.COMPLETE';
		
		var param = {inquiryNo : $scope.ccsInquiry.inquiryNo,
				answer : $scope.ccsInquiry.answer};
		
		inquiryService.saveAnswer($scope.ccsInquiry, function(response) {
//			pScope.myGrid.loadGridData();
			alert('답변완료 상태로 변경되었습니다.');
			$window.location.reload();
		});
	}
	

	// 지우게
	this.eraser = function(type){
		if(type =='mmsMember'){
			if($scope.ccsInquiry != null){
				$scope.ccsInquiry.memberId = "";
			}
			$scope.ccsInquiry.memberNo = "";
			$scope.ccsInquiry['customerName']= "";
			$scope.ccsInquiry.member = '';
			
		}else if(type=='business'){
			$scope.ccsInquiry.businessId = "";
			$scope.ccsInquiry.ccsBusiness.name = "";
		}else if(type=='order'){
			$scope.ccsInquiry.orderId = "";
			$scope.ccsInquiry.saleproductId = "";
			$scope.ccsInquiry.productId = "";
			$scope.ccsInquiry.omsOrderproduct.productName = "";
			$scope.ccsInquiry.omsOrderproduct.saleproductName = "";
		}
	}
	
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	
	this.linkOrderFunction = function() {
		$scope.orderId = $scope.ccsInquiry.orderId;
		popupwindow('/oms/order/popup/detail', '주문상세', 1100, 600);
	}
	this.linkProductFunction = function() {
		commonPopupService.openProductDetailPopup($scope, $scope.ccsInquiry.productId);
	}

	$scope.ckOption = {
		language : 'ko'										//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		, filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}
	
});
