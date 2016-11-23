var creditcardApp = angular.module("creditcardApp", ['commonServiceModule', 'gridServiceModule','spsServiceModule','commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

// 메시지
Constants.message_keys = ["common.label.alert.save", "common.label.alert.delete", "common.label.alert.fail", 
                          "common.label.confirm.save", "common.label.confirm.delete", "sps.cardpromotion.delete.validation",
                          "sps.cardpromotion.state.update.run", "sps.cardpromotion.state.update.stop"];

creditcardApp.controller("sps_creditcardListApp_controller", function($window, $scope, $filter, 
		cardService, commonService, gridService, commonPopupService) {

	//팝업에서 부모 scope 접근하기 위함.
	$window.$scope = $scope;
	
	// 객체 생성
	$scope.search = {};

	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	// message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	// 카드사할인 그리드
	var	columnDefs = [
           { field: 'cardPromotionNo', colKey: 'c.sps.cardpromotion.no', linkFunction: "linkFunction" },
           { field: 'name', colKey: 'c.sps.cardpromotion.name', linkFunction: "linkFunction" },
           { field: 'cardPromotionStateName', colKey: 'c.sps.cardpromotion.state' },
           { field: 'startDt', colKey: 'c.sps.cardpromotion.startDt', cellFilter: "date:\'yyyy-MM-dd\'" },
           { field: 'endDt', colKey: 'c.sps.cardpromotion.endDt', cellFilter: "date:\'yyyy-MM-dd\'" },
           { field: 'insId', colKey: 'c.grid.column.insId' , userFilter :'insId,insName'},
           { field: 'insDt', colKey: 'c.grid.column.insDt' },
           { field: 'updId', colKey: 'c.grid.column.updId' , userFilter :'updId,updName'},
           { field: 'updDt', colKey: 'c.grid.column.updDt' }
  	];

  	var gridParam = {
  			scope : $scope,
       		gridName : "grid_card",
       		url : '/api/sps/discount/card',
       		searchKey : "search",
       		columnDefs : columnDefs,
  			gridOptions : {
  			},
  			callbackFn : function() {
  			}
  	};
  	// 그리드 객체생성
  	$scope.myGrid = new gridService.NgGrid(gridParam);
	
  	
	// 카드사할인 목록 조회	
	$scope.searchGrid = function(){
		$scope.myGrid.loadGridData();
	}
	
	this.insertPopup = function(){
		$scope.type = 'I'
		popupwindow('/sps/creditcard/popup/insert',"cardDisctInsertPopup", 1200, 700);
	}
	
	this.reset = function() {
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	$scope.popup = function(url) {
		popupwindow(url,"cardDisctDetailPopup", 1200, 650);
	}
	
	// 카드사할인 상세 팝업
	$scope.linkFunction = function(field, row) {
		$scope.cardPromotionNo = row.cardPromotionNo;
		$scope.type = 'D';
		$scope.popup('/sps/creditcard/popup/insert');		
	}
	
	// 삭제
	this.deleteCardPromotion = function() {
		var checkEventState = true;
		
		var selectRows = $scope.myGrid.getSelectedRows();
		
		angular.forEach(selectRows, function(row) {
			if (checkEventState && row.cardPromotionStateCd != 'CARD_PROMOTION_STATE_CD.READY') {	// alert 한번만 띄우기 위해.
				alert($scope.MESSAGES["sps.cardpromotion.delete.validation"]);
				checkEventState = false;
			}
		});
		
		if (checkEventState) {
			$scope.myGrid.deleteGridData();
		}
	}
	
}).controller("sps_creditcardDetailApp_controller", function($window, $scope, $filter, 
		cardService, commonService, gridService,commonFactory) {
	
	// 부모창의 scope
	var pScope = $window.opener.$scope;
	
	// 객체 생성
	$scope.spsCardpromotion={};	
	$scope.type = pScope.type;
	
	console.log('type :'+pScope.type);
	
	//CK Editor 설정
	$scope.ckPcOption = {
		language : 'ko',								//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}

	$scope.ckMobileOption = {
		language : 'ko',								//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}
	
	// 카드사할인 상세 조회
	this.detail = function(){
		if($scope.type == 'D'){
			$scope.spsCardpromotion.cardPromotionNo = pScope.cardPromotionNo;
			cardService.getCreditCardInfo($scope.spsCardpromotion, function(response) {
				$scope.spsCardpromotion = response;
			});
		} else {
			$scope.spsCardpromotion.cardPromotionNo = '';
			$scope.spsCardpromotion.cardPromotionStateCd = 'CARD_PROMOTION_STATE_CD.READY';
			$scope.spsCardpromotion.cardPromotionStateName = '대기';
		}
	}
	
	// 수정내용 저장 or 신규 등록
	this.save = function() {
		
		
		//폼 체크
		if (!commonService.checkForm($scope.form2)) {
			return;
		}
		
		if(!$scope.spsCardpromotion.startDt || !$scope.spsCardpromotion.endDt){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		if(!confirm("저장 하시겠습니까?")){
			return;
		}
		
		cardService.saveCreditCard($scope.spsCardpromotion, function(response) {
			if(response.content == 'fail') {
				alert('신규등록시 에러가 발생 하였습니다.');
			}else{
				if($scope.type == 'I'){
					pScope.cardPromotionNo = response.content;
					pScope.type = 'D';
				}
				pScope.myGrid.loadGridData();
				alert('저장 되었습니다.');
				$window.location.reload();
			}
		});
	}
	
	// 카드사할인 상태 변경
	this.updatePromotionState = function(state) {
		var msg = ""
			var preState = "";
			var afterState = "";
		
		$scope.cardpromotion = {};
		$scope.cardpromotion.cardPromotionNo = $scope.spsCardpromotion.cardPromotionNo;
		if (state == 'run') {
			preState = "대기";
			afterState = "진행";
			$scope.cardpromotion.cardPromotionStateCd = 'CARD_PROMOTION_STATE_CD.RUN';
		} else if (state == 'stop') {
			preState = "진행";
			afterState = "중지";
			$scope.cardpromotion.cardPromotionStateCd = 'CARD_PROMOTION_STATE_CD.STOP';
			
		}
		
		msg = "'"+ preState +"상태'를 '" + afterState + "상태' (으)로 변경하시겠습니까?";
		if (confirm(msg)) {
			cardService.changeState($scope.cardpromotion, function(response) {
				pScope.myGrid.loadGridData();
				alert("'"+afterState+"상태'(으)로 변경되었습니다.");
				$window.location.reload();
			});
		}
	}
	
	// 취소(닫기)
	this.close = function() {
		$window.close();
	}
		
	
});