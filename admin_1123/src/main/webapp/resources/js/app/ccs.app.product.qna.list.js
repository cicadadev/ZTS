

var productQnaManagerApp = angular.module("productQnaManagerApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel"
                          , "common.label.alert.delete", "common.label.confirm.delete", "common.label.alert.update", "common.label.confirm.update"];

productQnaManagerApp.controller("productQnaListController", function($window, $scope, $filter, prdQnaService, commonService, gridService, commonPopupService){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	// PO일경우 업체ID
	var poBusinessId = global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	$scope.search = {};
	$scope.periodType = [
	                        {val : 'QNA_DATE',text : 'Q&A 등록일'}, 
	                        {val : 'ANSWER_DATE',text : 'Q&A 답변등록일'},
	                        {val : 'CONFIRM_DATE',text : 'Q&A 확인일'}
	                        ];	
	$scope.searchType2 = [{val : 'NAME',text : '상품명'},
	                    {val : 'ID',text : '상품ID'}
	                    ];
	
	$scope.displayYns = [
	 	                {
	 	     				val : 'Y',
	 	     				text : '전시'
	 	     			}, {
	 	     				val : 'N',
	 	     				text : '미전시'
	 	     			}
	 	     	];
	angular.element(document).ready(function () {		
//		$scope.search.startDate = $filter('date')(new Date(), Constants.date_format_2);
//		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);
		commonService.init_search($scope,'search');
	});
//	angular.element(document).ready(function () {		
//		$scope.search.startDate = $filter('date')(new Date(), Constants.date_format_2);
//		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);
//		
//		commonService.init_search($scope,'search');
//	});
	
	cellGradeStr = "<div class=\"ui-grid-cell-contents\"  title=\"\">{{row.entity.mmsMember.mmsMemberZts.memGradeName == undefined?'-':row.entity.mmsMember.mmsMemberZts.memGradeName}}</div>";
	
	var columnDefs =  [
	                   { field: 'productQnaNo', 				width : '100',	cellClass : 'alignC',	colKey: "pmsProductqna.productQnaNo",		linkFunction : 'linkFunction'},
	                   { field: 'title', 						width : '200',	cellClass : 'alignC',	colKey: "pmsProductqna.title",				linkFunction : 'linkFunction'},
	                   { field: 'productId', 					width : '100',	cellClass : 'alignC',	displayName : "상품번호",			linkFunction : 'linkProductFunction'	/*colKey: "pmsProductqna.productId"*/},
	                   { field: 'pmsProduct.name', 				width : '100',	cellClass : 'alignC',	colKey: "pmsProduct.name",		linkFunction : 'linkProductFunction'},
	                   { field: 'ccsBusiness.name', 			width : '100',	cellClass : 'alignC',	displayName : "담당업체"/*,		linkFunction : 'linkBusinessFunction'*/	/*colKey: "ccsBusiness.name"*/},
	                   { field: 'mmsMemberzts.memGradeName', 	width : '100',	cellClass : 'alignC',	cellClass : 'alignC',	colKey: "c.mmsMember.memberGrade"},
	                   { field: 'productQnaStateName', 			width : '100',	cellClass : 'alignC',	displayName : "Q&A상태",		colKey: "c.ccs.qna.state"},	
	                   { field: 'insId',	 					width : '100',	userFilter :'insId,insName',			cellClass : 'alignC',	displayName : "Q&A등록자"	/*colKey: "c.ccs.qna.mbrId"*/},
	                   { field: 'insDt',		 				width : '150',	cellClass : 'alignC',	displayName : "Q&A등록일시"	/*colKey: "pmsProductqna.productQnaDt"*/},
	                   { field: 'confirmId', 					width : '100',	userFilter :'confirmId,confirmName',	cellClass : 'alignC',	displayName : "Q&A확인자"	/*colKey: "pmsProductqna.confirmDt"*/},
	                   { field: 'confirmDt', 					width : '150',	cellClass : 'alignC',	colKey: "pmsProductqna.confirmDt"},
	                   { field: 'answerer', 					width : '100',	cellClass : 'alignC',	displayName : "답변등록자"		/*colKey: "pmsProductqna.answerId"*/},
	                   { field: 'answerDt', 					width : '150',	cellClass : 'alignC',	displayName : "답변등록일"		/*colKey: "pmsProductqna.answerDt"*/},
	                   { field: 'passTime', 					width : '100',	cellClass : 'alignC',	colKey: "c.ccs.qna.passTime"}
	                  ]
	       	
	//PO이면 업체 컬럼 삭제
	if(!common.isEmpty(poBusinessId)){
		columnDefs.splice(4, 1);	
	}
	
   	var gridParam = {
   		scope : $scope,
   		gridName : "grid_prdQna",
   		url : '/api/ccs/productQna',
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
	
	$scope.productQnaNo = '';
	$scope.popup = function(url) {
		popupwindow(url,"productQnaDetailPopup", 1100, 900);
	}
	
	
	$scope.linkFunction = function(field, row) {
		$scope.productQnaNo = row.productQnaNo;
		$scope.popup('/ccs/productQna/popup/detail');
	}
	
	$scope.linkProductFunction = function(field, row) {
		commonPopupService.openProductDetailPopup($scope, row.productId);
	}

	//공개여부
	this.displayYnCheck = function(param) {
		if(angular.isDefined(param)) {//전체선택
			if ($scope.search.displayAll) {
				$scope.search.displayYns = "'Y','N'";
				$scope.search.displayY = true;
				$scope.search.displayN = true;
			} else {
				$scope.search.displayYns = '';
				$scope.search.displayY = false;
				$scope.search.displayN = false;
			}
		} else {
			var ynArr = [];
			
			if ($scope.search.displayY) {
				ynArr.push("'Y'")
			}
			if ($scope.search.displayN) {
				ynArr.push("'N'")
			}
			$scope.search.displayYns = ynArr.join(",");
			
			if ($scope.search.displayY && $scope.search.displayN) {
				$scope.search.displayAll = true;
			} else{
				$scope.search.displayAll = false;
			}
		}
	}
	
	// 업체 검색 팝업
	this.searchBusinessPopup = function(){
		commonPopupService.businessPopup($scope,"callback_business",false);
	}
	
	// 회원 검색 팝업
	this.searchMemberPopup = function() {
		commonPopupService.memberPopup($scope,"callback_mem",false);
	}
	
	// 답변 등록자 검색(사용자검색) 팝업
	this.searchUserPopup = function() {
		commonPopupService.userPopup($scope,"callback_answer",false);
	}
	// 회원검색팝업 콜백
	$scope.callback_mem = function(data) {
		$scope.search.memberNo = data[0].mmsMember.memberNo;
		$scope.search.memberLoginId = data[0].mmsMember.memberId;
		common.safeApply($scope);				
	}
	// 답변자 검색 팝업 콜백
	$scope.callback_answer = function(data) {
		$scope.search.answerId = data[0].userId;
		$scope.search.answererName = data[0].name;
		common.safeApply($scope);	
	}
	// 업체 검색 팝업 콜백
	$scope.callback_business = function(data){
		$scope.search.businessName = data[0].name;
		$scope.search.businessId = data[0].businessId;
		common.safeApply($scope);	
	}
	
	
	// 검색 지우기
	this.eraser = function(name){
		if(name === 'answer'){
			$scope.search.answerId = "";
			$scope.search.answererName = "";
		}else if(name === 'business'){
			$scope.search.businessId = "";
			$scope.search.businessName = "";
		}else if(name === 'member'){
			$scope.search.memberLoginId = "";
			$scope.search.memberNo = "";
		}
	}
	
	// 답변자명 변경
    $scope.changeAnswererName = function(){
		$scope.search.answerId = '';
	}	
    // 회원ID 변경
    $scope.changeMemberLoginId = function(){
		$scope.search.memberNo = '';
	}
		    
			
}).controller("productQnaDetailController", function($window, $scope, $filter, prdQnaService, commonService, gridService, commonPopupService){
	
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.pmsProductqna = {};
	$scope.pmsProductqna.productQnaNo = pScope.productQnaNo;
	
	// PO일경우 업체ID
	var poBusinessId = global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	$scope.displayYns = [
	 	                {
	 	     				val : 'Y',
	 	     				text : '전시'
	 	     			}, {
	 	     				val : 'N',
	 	     				text : '미전시'
	 	     			}
	 	     	];
	

	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	this.detail = function(){

		prdQnaService.getProductQnaDetail($scope.pmsProductqna, function(response) {
			$scope.pmsProductqna = response;
			
			$scope.pmsProductqna.insDt = $filter('date')($scope.pmsProductqna.insDt, Constants.date_format_1);
			$scope.pmsProductqna.answerDt = $filter('date')($scope.pmsProductqna.answerDt, Constants.date_format_1);
		});
	}
	
	// 문의 수정
	this.update = function(){

		if (!confirm($scope.MESSAGES["common.label.confirm.update"])) {
			return;
		}
		
//		if(!commonService.checkForm($scope.form2)){
//			return;
//		}
		
//		var param = {
//				productQnaNo : $scope.pmsProductqna.productQnaNo,
//				productId : $scope.pmsProductqna.productId,
//				displayYn : $scope.pmsProductqna.displayYn};
		
		prdQnaService.updateProductQna($scope.pmsProductqna, function(response) {
			alert($scope.MESSAGES["common.label.alert.update"]);
			$window.location.reload();
		});
	}
	// 문의 확인 처리
	this.updateQnaConfirm = function(){
		if(!confirm($scope.pmsProductqna.productQnaStateName + "를 문의확인 상태로 변경하시겠습니까?")){
			return;
		}
		var param = {
				productQnaNo : $scope.pmsProductqna.productQnaNo,
				productId : $scope.pmsProductqna.productId};
		
		prdQnaService.updateQnaConfirm(param, function(response) {
//				pScope.myGrid.loadGridData();
			alert('문의확인 상태로 변경되었습니다.');
			$window.location.reload();
		});
	}
	
	// 답변 완료 처리
	this.updateAnswer = function(){
		
		
		if(!commonService.checkForm($scope.form3)){
			return;
		}

		if(!confirm($scope.pmsProductqna.productQnaStateName + "를 답변완료 상태로 변경하시겠습니까?")){
			return;
		}
		
		var param = {
				productQnaNo : $scope.pmsProductqna.productQnaNo,
				productId : $scope.pmsProductqna.productId,
				answer : $scope.pmsProductqna.answer};
		
		prdQnaService.updateAnswer(param, function(response) {
//				pScope.myGrid.loadGridData();
			alert('답변완료 상태로 변경되었습니다.');
			$window.location.reload();
		});
	}
	
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}

	$scope.ckOption = {
		language : 'ko'										//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		, filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}
	
});
