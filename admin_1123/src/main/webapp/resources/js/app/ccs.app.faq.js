// FAQ 화면 모듈
var faqApp = angular.module('faqApp', [
		'ui.date', 'commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'ngCkeditor', 'commonPopupServiceModule'
]);

Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

faqApp.controller('listCtrl', function($window, $scope, faqService, $filter, gridService, commonService, commonPopupService) {
	var columnDefs = [
			{	field : 'faqNo',		colKey : 'ccsFaq.faqNo',		width : '10%',		type : 'number',					cellClass : 'alignC',		linkFunction : 'popup.detail'}, 
			{	field : 'title',		colKey : 'ccsFaq.title',		width : '20%',		linkFunction : 'popup.detail',		enableSorting : false},
			{	field : 'faqTypeName',	displayName:'유형',				colKey : 'c.ccs.faq.typeCd',	width : '10%', cellClass : 'alignC'}, 
			{	field : 'displayYn',	colKey : 'c.ccs.faq.displayYn',	width : '10%',	cellFilter : 'displayYnFilter',cellClass : 'alignC'},
			{	field : 'sortNo',		displayName:'우선순위',/*colKey : 'c.ccs.faq.displayYn',*/	width : '10%', cellClass : 'alignC', enableCellEdit : true, type : 'number'},
			{	field : 'insId',		userFilter :'insId,insName',	colKey : 'c.grid.column.insId',	width : '10%',	cellClass : 'alignC'}, 
			{	field : 'insDt',		displayName:'등록일시',				colKey : 'c.grid.column.insDt',	width : '15%',	cellClass : 'alignC'}, 
			{	field : 'updId',		userFilter :'updId,updName',	colKey : 'c.grid.column.updId',	width : '10%',	cellClass : 'alignC'}, 
			{	field : 'updDt',		displayName:'최종수정일시',			colKey : 'c.grid.column.updDt',	width : '15%',	cellClass : 'alignC'}
//			{
//				field : 'detail',
//				colKey : 'ccsFaq.detail',
//				// width : '400',
//				enableSorting : false
//			}, {
//				field : 'sortNo',
//				colKey : 'ccsFaq.sortNo',
//				width : '70',
//				type : 'number',
//				enableCellEdit : true,
//				cellClass : 'alignC'
//			}, {
//				field : 'useYn',
//				colKey : 'ccsFaq.useYn',
//				width : '90',
//				enableCellEdit : true,
//				cellFilter : 'useYnFilter',
//				cellClass : 'alignC'
//			}, 
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
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	var gridParam = {
		scope : $scope,
		gridName : 'grid_faq',
		url : '/api/ccs/faq',
		searchKey : 'search',
		columnDefs : columnDefs
	// callbackFn : gridCallbackFn
	};

	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	// 코드 엑셀 다운로드
	this.exportFaqExcel = function(){
		myGrid.exportExcel();
	}
	
	this.displayYnCheck = function(param) {
		if(angular.isDefined(param)) {
			if ($scope.search.displayYn) {
				$scope.search.displayY = true;
				$scope.search.displayN = true;
			} else {
				$scope.search.displayY = false;
				$scope.search.displayN = false;
			}
		} else {
			if ($scope.search.displayY && $scope.search.displayN) {
				$scope.search.displayYn = true;
			} else {
				$scope.search.displayYn = false;
			}
		}
	}
	
	$window.$scope = $scope;
	$scope.search = {
		reset : function() {
			commonService.reset_search($scope, 'search');
		},
		init : function() {
		}
	}
	$scope.infoTypes = [
			{
				val : 'title',
				text : '제목'
			}, {
				val : 'contents',
				text : '내용'
			}, {
				val : 'all',
				text : '제목+내용'
			}
	];
	$scope.useYns = [
			{
				val : '',
				text : '전체'
			}, {
				val : 'Y',
				text : '사용'
			}, {
				val : 'N',
				text : '미사용'
			}
	];

	$scope.list = {
		search : function() {
			$scope.myGrid.loadGridData();
		},
		save : function() {
			$scope.myGrid.saveGridData(null, function(data){
				$scope.myGrid.loadGridData();
			});
		},
		remove : function() {
			$scope.myGrid.deleteGridData(null, function(){					
			});
			$scope.myGrid.loadGridData();
		}
	}
	
	$scope.searchGrid = function(){
		
		$scope.myGrid.loadGridData();
	}

	// 검색 지우기
	this.eraser = function(){
		$scope.search.insInfoId = "";
		$scope.search.insInfoName = "";
	}
	
	// 검색어 변경시 초기화
	this.change = function(){
		$scope.search.insInfoId = "";
	}
	
	// 회원 검색
	this.searchUser = function() {
		commonPopupService.userPopup($scope,'callback_ins',false);					
	}
	
	$scope.callback_ins = function(data) {
		$scope.search.insInfoId = data[0].userId;
		$scope.search.insInfoName = data[0].name;
		$scope.$apply();
	}
	
	$scope.faqNo = '';
	$scope.flagTxt = '';
	$scope.popup = {
		detail : function(field, row) { // linkfunction
			$scope.faqNo = row.faqNo;
			$scope.flagTxt = '상세';
			popupwindow('/ccs/faq/popup/insert', 'FAQ 상세', 900, 700);
		},
		insert : function() {
			$scope.faqNo = '';
			$scope.flagTxt = '등록';
			popupwindow('/ccs/faq/popup/insert', 'FAQ 등록', 900, 700);
		}
	}

	angular.element(document).ready(function() {
		commonService.init_search($scope, 'search');
	});
	
});

faqApp.controller('popCtrl', function($window, $scope, faqService, gridService, commonService) {

	pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	$scope.ccsFaq = {};
	$scope.flagTxt = pScope.flagTxt;

	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	this.insert = function() {
		// 폼 체크
		if (!commonService.checkForm($scope.form2)) {
			return;
		}

		// 확인 메세지
		if ($scope.ccsFaq.faqNo != undefined && $scope.ccsFaq.faqNo != null && $scope.ccsFaq.faqNo != '') {
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
		}else{
			if(!confirm("FAQ를 생성하시겠습니까?")){
				return;
			}
		}
		
		if ($scope.ccsFaq.faqNo != undefined && $scope.ccsFaq.faqNo != null && $scope.ccsFaq.faqNo != '') {
			faqService.update($scope.ccsFaq, function(response) {
				if (response.content == '1') {
					pScope.$apply();
					pScope.myGrid.loadGridData();
					alert($scope.MESSAGES["common.label.alert.save"]);
					$window.close();
				} else {
					alert('FAQ 변경시 에러가 발생 하였습니다.');
				}
			});
		} else {
			faqService.insert($scope.ccsFaq, function(response) {
				if (response.content == '1') {
					pScope.$apply();
					pScope.myGrid.loadGridData();
					alert('FAQ가 생성되었습니다.');
					$window.close();
				} else {
					alert('FAQ 등록시 에러가 발생 하였습니다.');
				}
			});
		}			
	};
	this.detail = function() {
		if (pScope.faqNo != '') {
			$scope.ccsFaq.faqNo = pScope.faqNo;
			faqService.selectOne($scope.ccsFaq, function(response) {
				$scope.ccsFaq = response;
			});
		}
	};
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