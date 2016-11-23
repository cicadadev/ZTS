var mdnoticeApp = angular.module("mdnoticeManagerApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'ccsServiceModule', 'gridServiceModule', 'pmsServiceModule',
                                           	'ui.date', 'ngCkeditor' ]);

Constants.message_keys = ["common.label.confirm.save", "common.label.confirm.delete"];

mdnoticeApp.controller("pms_mdNoticeListApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService, epexcService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	
	angular.element(document).ready(function () {		
		/*$scope.search.startDate = $filter('date')(new Date(), Constants.date_format_2);
		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);
		*/
		commonService.init_search($scope,'search');
	});
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
	
	var	columnDefs = [
		                 { field: 'noticeNo'			, width: '10%', colKey: 'c.pms.mdnotice.noticeNo2', 			linkFunction: 'linkFunction'},
		                 { field: 'title'				, width: '15%',	colKey: 'c.pms.mdnotice.title', linkFunction: 'linkFunction'},
		                 /*{ field: 'detail'				,	colKey: 'ccsNotice.detail' },*/
		                 { field: 'displayYn'			, width: '10%',	colKey: 'ccsNotice.displayYn', cellFilter: "displayYnFilter",  aggregationHideLabel: true },
		                 { field: 'insId'				, width: '15%',	colKey: 'c.grid.column.insId' , userFilter :'insId,insName'},
		                 { field: 'insDt'				, width: '15%',	displayName : "등록일시",			colKey: 'c.grid.column.insDt' , cellFilter: "date:\'yyyy-MM-dd\'" },
		                 { field: 'updId'				, width: '15%',	colKey: 'c.grid.column.updId' , userFilter :'updId,updName'},
		                 { field: 'updDt'				, width: '15%',	displayName : "최종수정일시",			colKey: 'c.grid.column.updDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
		                ];


	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : 'myGrid',		//mandatory
			url : '/api/pms/mdnotice',  //mandatory
			searchKey : 'search',       //mandatory
			columnDefs : columnDefs     //mandatory
			,
       		/*gridOptions : {
       		},*/
       		callbackFn : function() {
       		}
	};
	
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
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	//그리드 객체생성
	$scope.mdnoticeGrid = new gridService.NgGrid(gridParam);

	this.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element(".day_group").find('button:first').addClass("on");
//		console.log("scope==> ", $scope);
	}

	$scope.loadGrid = function() {
		$scope.mdnoticeGrid.loadGridData();
	}
	
	// 코드 엑셀 다운로드
	this.exportMdnoticeExcel = function(){
		$scope.mdnoticeGrid.exportExcel('mdnotice', function(){ 
			alert("수정완료!");
		});
	}

	// 등록 팝업 호출
	this.regNoticePopup = function() {
		$scope.noticeNo = '';
		var url = Rest.context.path + "/pms/mdnotice/popup/detail";
		popupwindow(url, "MD 공지사항 기본정보", 1200, 600);
	}
	
	// 등록 팝업 호출
	$scope.linkFunction = function(field, row) {
		$scope.noticeNo = row.noticeNo;
		var url = Rest.context.path + "/pms/mdnotice/popup/detail?noticeNo";
		popupwindow(url, "MD 공지사항 기본정보", 1200, 600);
		
	}
}).controller("pms_mdnoticeDetailPopApp_controller", function($window, $scope, $filter, commonService, mdnoticeService, noticeService, gridService, commonPopupService) { //상세 PopUp Controller
	
	//현재 팝업에서 부모 scope 접근하기 위한 변수 설정
	pScope = $window.opener.$scope;
	
	$scope.ccsNotice = {};
	$scope.searchProduct = {};
	$scope.deleteBtn = false;
	
	$scope.productIds = [];
	$scope.noticeNo = pScope.noticeNo;
	
	$scope.displayYns = [
		 	                {
		 	     				val : 'Y',
		 	     				text : '전시'
		 	     			}, {
		 	     				val : 'N',
		 	     				text : '미전시'
		 	     			}
		 	     	];
	
	var columnDefs2 = [
			             { field: 'productId',			width:'80',			colKey:"pmsProduct.productId"},      			
			             { field: 'name',				width:'200',        colKey:"pmsProduct.name"},          
			             { field: 'productTypeName',	width:'100',        colKey:"pmsProduct.productTypeCd"}, 
			             { field: 'saleStateName',		width:'100',        colKey:"c.pms.product.saleStateName"}, 
			             { field: 'pmsBrandName',		width:'100',		colKey:"pmsBrand.name"},            
			             { field: 'pmsCategoryName',	width:'200', 		colKey:"c.pmsCategory.category" },
			             { field: 'dmsCategoryName',	width:'200', 		colKey:"c.dmsDisplaycategory.category" },
			             { field: 'saleStartDt', 		width:'200',		colKey:"pmsProduct.saleStartDt"},
			             { field: 'saleEndDt', 			width:'200',		colKey:"pmsProduct.saleEndDt"},
			             /*{ field: 'productCopyReg',		width:'100',		colKey:"c.pms.product.copy.reg"},*/
						 { field: 'insId', 				width:'200',		colKey: "c.grid.column.insId" , userFilter :'insId,insName'},
						 { field: 'insDt', 				width:'200',		displayName : "등록일시",			colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
						 { field: 'updId', 				width:'200',		colKey: "c.grid.column.updId" , userFilter :'updId,updName'},
						 { field: 'updDt', 				width:'200',		displayName : "최종수정일시", 		colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
			         ];
	
	
	var productGridParam = {
			scope : $scope, 			//mandatory
			gridName : 'myProductGrid',		//mandatory
			url : '/api/pms/mdnotice/product',  //mandatory
			searchKey : 'searchProduct',     //mandatory
			columnDefs : columnDefs2    //mandatory
			,
    		/*gridOptions : {
    		},*/
    		callbackFn : function() {
    			
    		}
	};	
	
	//그리드 객체생성
	$scope.mdnoticeProductGrid = new gridService.NgGrid(productGridParam);
	
	$scope.loadProductGrid = function() {
		$scope.mdnoticeProductGrid.loadGridData();
	}
	
	//MD공지 상세정보
	this.detail = function() {
		pScope = $window.opener.$scope;
		if (pScope.noticeNo != null && pScope.noticeNo != '') {
			$scope.ccsNotice.noticeNo = pScope.noticeNo;
			$scope.mdnoticeProductGrid.noticeNos = pScope.noticeNo;
			noticeService.selectMdNotice($scope.ccsNotice, function(response) {
				
				$scope.ccsNotice = response;
				$scope.searchProduct.noticeNos = pScope.noticeNo;
				$scope.loadProductGrid();
			});
			$scope.deleteBtn = true;
		}
		
	};
	
	//MD공지 삭제
	this.deleteMdnotice = function() {
		if (window.confirm("삭제하시겠습니까?")) {
			if (pScope.noticeNo != null && pScope.noticeNo != '') {
				mdnoticeService.deleteCheck($scope.searchProduct, function(data){
					if(angular.isUndefined(data.success) || angular.isDefined(data) && data.success){
						mdnoticeService.deleteNoticeproduct($scope.searchProduct, function(data){
							alert("삭제되었습니다.");
							pScope.mdnoticeGrid.loadGridData();
							$window.close();
						});
					}else{
						if (window.confirm("하위 MD공지상품이 존재합니다 삭제 하시겠습니까?")) {
							mdnoticeService.deleteNoticeproduct($scope.searchProduct, function(data){
								alert("삭제되었습니다.");
								pScope.mdnoticeGrid.loadGridData();
								$window.close();
							});
						}
					}
				});
			}
		}
	};
	
	//MD공지 수정
	this.update = function() {
		
		// 폼 체크
		if (!commonService.checkForm($scope.form2)) {
			return;
		}
		
		// 확인 메세지
		if (pScope.noticeNo != null && pScope.noticeNo != '') {
			if(!confirm("저장하시겠습니까?")){
				return;
			}
		}else{
			if(!confirm("MD 공지사항을 생성하시겠습니까?")){
				return;
			}
		}
		$scope.ccsNotice.topNoticeYn = 'N';
		var gridData = $scope.mdnoticeProductGrid.getGridScope();
		$scope.productIds = [];
		for (i in gridData.data) {
			if (gridData.data[i].productId != null && gridData.data[i].productId != '') {
				$scope.productIds.push(gridData.data[i].productId);
			}
		}
		$scope.searchProduct.productIds = $scope.productIds;
		//$scope.mdnoticeProductGrid.productIdList = $scope.productIds;
		if (pScope.noticeNo != null && pScope.noticeNo != '') {
			noticeService.update($scope.ccsNotice, function(response) {
				mdnoticeService.saveNoticeproduct($scope.searchProduct, function(data){
					alert('저장되었습니다.');
					pScope.mdnoticeGrid.loadGridData();
					$window.close();
				});
			});
		}
		else{
			$scope.ccsNotice.noticeTypeCd = "NOTICE_TYPE_CD.PRODUCT";
			$scope.ccsNotice.fixYn = "Y";
			
			noticeService.insert($scope.ccsNotice, function(response) {
				mdnoticeService.saveNoticeproduct($scope.searchProduct, function(data){
					alert('MD 공지사항이 생성되었습니다.');
					pScope.mdnoticeGrid.loadGridData();
					$window.close();
				});
			});
		}
	};
	
	//상품리스트 엑셀 다운
	this.exportProductExcel = function(){
		$scope.mdnoticeProductGrid.exportExcel('product', function(){
			//alert("수정완료!");
		});
	}
	
	//상품 그리드 행만 삭제
	this.deleteMdnoticeGrpGrid = function() {
		$scope.mdnoticeProductGrid.deleteRow();
	};
	
	//상품등록을 위한 상품검색 공통 팝업 호출
	this.regProductPopup = function(){
		commonPopupService.productPopup($scope, "callbackProductPopup", true);
	}
	//상품 검색 후 그리드에 추가
	$scope.callbackProductPopup = function(data){
		
		var successFlag = true;
		
		for(var i = 0 ; i < data.length ; i++){
			var insertFlag = true;
			var gridData = $scope.mdnoticeProductGrid.getGridScope();
			for (j in gridData.data) {
				if (data[i].productId == gridData.data[j].productId) {
					insertFlag = false;
					successFlag = false;
				}
			}
			if (insertFlag) {
				$scope.mdnoticeProductGrid.addRow({
					productId : data[i].productId,
					name  : data[i].name,
					productTypeName : data[i].productTypeName,
					saleStateName : data[i].saleStateName,
					pmsBrandName : data[i].brandName,
					pmsCategoryName : data[i].pmsCategory.name,
					dmsCategoryName : data[i].dmsCategoryName,
					saleStartDt : data[i].saleStartDt,
					saleEndDt : data[i].saleEndDt,
					productCopyReg : data[i].productCopyReg,
					insId : (data[i].insName == null) ? data[i].insId : data[i].insName + "(" + data[i].insId + ")",
							insDt : data[i].insDt,
							updId : (data[i].updName == null) ? data[i].updId : data[i].updName + "(" + data[i].updId + ")",
									updDt : data[i].updDt,
				});
			}
		}
		if(successFlag){
			alert('그리드에 추가되었습니다.');
		}else{
			alert('이미 등록된 상품을 제외하고 추가 되었습니다.');
		}
	}

	this.close = function() {
		if(!confirm("취소하시겠습니까?")){
			return;
		}
		alert('취소되었습니다.');
		$window.close();
	}
	
	$scope.ckOption = {
		language : 'ko'										//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		, filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}	
	
});