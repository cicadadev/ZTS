var attrApp = angular.module("attrApp", [	'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'pmsServiceModule',
                                           	'ui.date' ]);
//메시지
Constants.message_keys = ["pms.attribute.register.input", "pms.attribute.update.success", "pms.attribute.update.fail"];

attrApp.controller("pms_attributeListApp_controller", function($window, $scope, commonService, gridService, attributeService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
	});

	var	columnDefs = [
		                 { field: 'attributeId'		, width: '10%',	colKey: 'c.pmsAttribute.id', linkFunction: "attributeDetail" },
		                 { field: 'attributeTypeName'	, width: '15%',	colKey: 'c.pmsAttribute.type' },
		                 { field: 'name'			, width: '15%',	colKey: 'pmsAttribute.name', linkFunction: "attributeDetail" },
		                 { field: 'useYn'			, width: '10%',	colKey: 'pmsAttribute.useYn', enableCellEdit:true,	cellFilter : 'useYnFilter'},
		                 { field: 'insId'			, width: '15%',	colKey: 'c.grid.column.insId' , userFilter :'insId,insName'},
		                 { field: 'insDt'			, width: '15%',	colKey: 'c.grid.column.insDt' , cellFilter: "date:\'yyyy-MM-dd\'" },
		                 { field: 'updId'			, width: '15%',	colKey: 'c.grid.column.updId' , userFilter :'updId,updName'},
		                 { field: 'updDt'			, width: '15%',	colKey: 'c.grid.column.updDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
		                ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_attr",	//mandatory
			url :  '/api/pms/attribute',  //mandatory
			searchKey : "search",     //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				checkBoxEnable : false	//row header에 check box 노출여부
			},
			callbackFn : function() {	//optional
			}
	};

	//그리드 객체생성
	$scope.myGrid = new gridService.NgGrid(gridParam);

	//message 정의
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	this.reset = function() {
		commonService.reset_search($scope, 'search');
	}

	//Grid: event ID 선택 호출
	$scope.attributeDetail = function(fieldValue, rowEntity) {
		$scope.attributeId = rowEntity.attributeId;
		attributeService.openAttributeDetailPopup();
	}
	
	// 속성 목록 수정
	this.saveGridData = function() {
		$scope.myGrid.saveGridData(null, function(data){
			$scope.myGrid.loadGridData();
		});
	}
	

	//등록 팝업 호출
	this.insertAttrPopup = function() {
		var url = Rest.context.path + '/pms/attribute/popup/insert';
		popupwindow(url, "Attribute Insert", 1200, 275);
	}

}).controller("pms_attrDetailPopApp_controller", function($window, $scope, commonService, commonPopupService, attributeService) { //속성상세 PopUp Controller
	//현재 팝업에서 부모 scope 접근하기 위한 변수 설정
	pScope = $window.opener.$scope;

	// 저장 버튼 숨김 여부 : 표준카테고리 -> 속성팝업 열기에는 저장 버튼 숨김
	$scope.saveBtn = pScope.saveBtn == false ? false : true;
	
	//현재 팝업 정보 설정: 속성값 팝업에서 사용
//	$window.$scope = $scope;

	//속성 정보 변수
	$scope.pmsAttribute = {};
	
	this.detInit = function() {
		$scope.pmsAttribute.attributeId = pScope.attributeId;
		//속성 상세정보
		attributeService.getAttributeDetail($scope.pmsAttribute, function(response) {
			
	    	$scope.pmsAttribute = response;
	    	if (response.pmsAttributevalues != undefined) {
	    		$scope.oldAttributeValueArr = [];
	    		var arrCnt = response.pmsAttributevalues.length;
		    	for (var i=0; i<arrCnt; i++) {
		    		$scope.oldAttributeValueArr.push(response.pmsAttributevalues[i].attributeValue);
		    	}
		    	$scope.pmsAttribute.lastSortNo = arrCnt;
	    	} else {
	    		$scope.pmsAttribute.lastSortNo = 0;
	    	}
	    	
	    	$scope.pmsAttribute.pmsAttributevalues = [];
	    	$scope.pmsAttribute.pmsAttributevalues.push({
				attributeValue: null
			});
	    });
	};

	//속성값 추가
	this.addAttrValue = function(index) {
		if (!$scope.pmsAttribute.pmsAttributevalues[index].attributeValue) {
			alert(pScope.MESSAGES["pms.attribute.register.input"]);
			return;
		}
		$scope.pmsAttribute.pmsAttributevalues.push({
			attributeValue: null
		});
	}
	
	//속성값 삭제
	this.delAttrValue = function(index) {
		if ($scope.pmsAttribute.pmsAttributevalues.length == 1) {
			return;
		}
		$scope.pmsAttribute.pmsAttributevalues.splice(index, 1);
	}
	
	//변경된 속성 정보 저장
	this.saveAttribute = function() {
		//폼 체크
//		if(!commonService.checkForm($scope.frmAttrUpdate)) {
//			return;
//		}


		attributeService.updateAttribute($scope.pmsAttribute, function(response) {

			if(null != response.content && '' != response.content) {
				alert(pScope.MESSAGES["pms.attribute.update.success"]);
				pScope.myGrid.loadGridData();
			} else {
				alert(pScope.MESSAGES["pms.attribute.update.fail"]);
			}
			
			$window.close();
	    });
	}

	this.close = function() {
		$window.close();
	}
}).controller("pms_attrInsertPopApp_controller", function($window, $scope, commonService, commonPopupService, attributeService) { //속성등록 PopUp Controller
	//현재 팝업에서 부모 scope 접근하기 위한 변수 설정
	pScope = $window.opener.$scope;
	
	//현재 팝업 정보 설정: 속성값 팝업에서 사용
//	$window.$scope = $scope;

	//속성 정보 변수
	$scope.pmsAttribute = {};
	$scope.pmsAttribute.pmsAttributevalues = []; //속성값

	this.attrInit = function() {
		$scope.pmsAttribute.pmsAttributevalues.push({
			attributeValue: null
		});
	}

	//속성값 추가
	this.addAttrValue = function(index) {
		if (!$scope.pmsAttribute.pmsAttributevalues[index].attributeValue) {
			alert(pScope.MESSAGES["pms.attribute.register.input"]);
			return;
		}
		$scope.pmsAttribute.pmsAttributevalues.push({
			attributeValue: null
		});
	}
	
	//속성값 삭제
	this.delAttrValue = function(index) {
		if ($scope.pmsAttribute.pmsAttributevalues.length == 1) {
			return;
		}
		$scope.pmsAttribute.pmsAttributevalues.splice(index, 1);
	}

	//신규 속성 저장
	this.saveNewAttribute = function() {
		//폼 체크
		if(!commonService.checkForm($scope.frmAttrInsert)) {
			return;
		}

		//입력형인 경우
		if ('ATTRIBUTE_TYPE_CD.INPUT' == $scope.pmsAttribute.attributeTypeCd) {
			$scope.pmsAttribute.pmsAttributevalues = null;
		} else { //속성 값이 한 건도 없는 경우
			if ($scope.pmsAttribute.pmsAttributevalues.length == 1) {
				if (!$scope.pmsAttribute.pmsAttributevalues[0].attributeValue) {
					alert(pScope.MESSAGES["pms.attribute.register.input"]);
					return;
				}
			}
		}

		attributeService.insertAttribute($scope.pmsAttribute, function(response) {

			if(null != response.content && '' != response.content) {
				alert(pScope.MESSAGES["pms.attribute.update.success"]);
				pScope.myGrid.loadGridData();
			} else {
				alert(pScope.MESSAGES["pms.attribute.update.fail"]);
			}

			$window.close();
	    });
	}

	this.close = function() {
		$window.close();
	}
});