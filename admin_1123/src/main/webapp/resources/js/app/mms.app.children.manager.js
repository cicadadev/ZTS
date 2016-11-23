
var childrenManagerApp = angular.module("childrenManagerApp", ['commonServiceModule', 'mmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

childrenManagerApp.controller("mms_childrenManagerApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService) {
	
$scope.childrenSearch = {};
$window.$scope = $scope;
	var	columnDefs = [
  						{ field: 'childrencardTypeCd'	, width: '20%'	, colKey: "c.mmsChildrencard.childrencardType"	, enableCellEdit:true 
  														, dropdownCodeEditor : "CHILDRENCARD_TYPE_CD"
  													    , cellFilter :'childrencardTypeFilter'	, validators:{required:true}					},
  						{ field: 'name'					, width: '20%'	, colKey: "mmsChildrencard.name" 				, enableCellEdit:true	, validators:{required:true}},	
  						{ field: 'accountNo'			, width: '20%'	, colKey: "mmsChildrencard.accountNo"	, type:'number'		, enableCellEdit:true	, validators:{required:true}},
  						{ field: 'regYn'				, width: '20%'	, colKey: "mmsChildrencard.regYn"		, cellFilter : 'regYnFilter'								},
  						{ field: 'startDt'				, width: '20%'	, colKey: "mmsChildrencard.startDt"				 						},
  						{ field: 'endDt'				, width: '20%'	, colKey: "mmsChildrencard.endDt"				 						},
  						{ field: 'beforeAccountNo'		, visible:false													},
  					];
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_children",		//mandatory
			url :  '/api/mms/childrencard',  		//mandatory
			searchKey : "childrenSearch",      //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
				
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'childrenSearch');
	});
	
	// 검색조건 초기화
	$scope.reset = function() {
		commonService.reset_search($scope, 'childrenSearch');
		angular.element(".day_group").find('button:first').addClass("on");
		$scope.childrenSearch.startDate ="";
		$scope.childrenSearch.endDate = "";
	}
	
	// 행 추가
	this.addRow = function() {
		$scope.myGrid.addRow({
			crudType  : 'C',
		});
	}
	
	// 다자녀카드 엑셀 업로드
	this.batchChildrencardUpload = function() {
		commonPopupService.gridbulkuploadPopup($scope,"excelUpload_callback","childrencard");
	}
	
	
	$scope.excelUpload_callback = function(response) {
		var data = response.resultList;
		console.log(response.resultList);
		for (var i=0; i < data.length; i++) {
			console.log(data[i].childrencard.accountNo);
			
			$scope.myGrid.addRow({
				accountNo : data[i].childrencard.accountNo,
				name : data[i].childrencard.name,
				childrencardTypeCd :data[i].childrencard.childrencardTypeCd, 
				crudType  : 'C',
			});
		}
	}
	
}).filter('childrencardTypeFilter', function() {// CHANNEL_TYPE_CD 
	
	var comboHash = {
						'CHILDRENCARD_TYPE_CD.GYEONGGI'	: '경기 多자녀 I-PLUS'
					  , 'CHILDRENCARD_TYPE_CD.DAEJEON'	: '대전시 꿈나무 사랑 카드'
					  , 'CHILDRENCARD_TYPE_CD.CHUNGNAM'	: '충청남도 多사랑 카드'
					  , 'CHILDRENCARD_TYPE_CD.GWANGJU'	: '광주 아이사랑 카드'
					  , 'CHILDRENCARD_TYPE_CD.INCHEON'	: '인천 아이모아 카드'
					  , 'CHILDRENCARD_TYPE_CD.GANGWON'	: '강원도 반비다복 카드'
					  , 'CHILDRENCARD_TYPE_CD.GYEONGNAM': '경상남도 i 다누리 카드'
					  , 'CHILDRENCARD_TYPE_CD.BUSAN'	: '부산 가족사랑 카드'
					  , 'CHILDRENCARD_TYPE_CD.CHUNGBUK'	: '충청북도 아이사랑 보너스 카드'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };		
}).filter('regYnFilter', function() { 
	
	var comboHash = {
			'Y': '예',
			'N': '아니요'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };			
	
});
