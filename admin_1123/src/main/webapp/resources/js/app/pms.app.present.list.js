//message init
Constants.message_keys = [];

var presentApp = angular.module("presentApp", [	'commonServiceModule', 'gridServiceModule', "commonPopupServiceModule",
                                               	'pmsServiceModule',
                                               	'ui.date'
                                               	]);
presentApp.controller("pms_presentListApp_controller", function($window, $scope, $filter, presentService, commonService, gridService) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	var url = "";
	var excelFileName = "present";
	
	$scope.search = {};
	
	// 초기화
	angular.element(document).ready(function () {	
		commonService.init_search($scope,'search');
	});			
	
	var columnDefs = [
	                 { field: 'productId'				,width:'100' , colKey: "spsPresent.presentId", linkFunction:"openPresentPopup" },
	                 { field: 'name'								 , colKey: "c.pms.present.name", linkFunction:"openPresentPopup"  },
	                 { field: 'pmsSaleproduct.realStockQty'			,width:'100' , colKey: "pmsSaleproduct.realStockQty"  },
	                 { field: 'useYn'					,width:'100' , colKey: "pmsProduct.useYn", cellFilter: "useYnFilter"  },
	                 { field: 'erpProductId',			 width:'100',  colKey: "pmsProduct.erpProductId" },
	                 { field: 'insId'					,width:'100' , colKey: "c.grid.column.insId" , userFilter :'insId,insName'},
	                 { field: 'insDt'								 , colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" },
	                 { field: 'updId'					,width:'100' , colKey: "c.grid.column.updId" , userFilter :'updId,updName'},
	                 { field: 'updDt'								 , colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" }
		            ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "present",	//mandatory
			url :  '/api/pms/present',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.presentGrid = new gridService.NgGrid(gridParam);
	
	// 검색 조건 초기화
	this.resetData = function() {
		/* search Data 초기화 */
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
		
		// check box initial
		for(var i=1; i<4; i++) {
			$scope["useYn"+i] = false;
		}
	}
	
	// 검색버튼 클릭
	$scope.searchPresent = function() {
		
		if($scope.search.searchType=='1'){
			$scope.search.productId=$scope.search.keyword;
			$scope.search.name = null;
		}else{
			$scope.search.name=$scope.search.keyword;
			$scope.search.productId = null;
		}
		$scope.presentGrid.loadGridData();
	};
	
	//사은품 상세 팝업
	$scope.openPresentPopup = function(fieldValue, rowEntity){
		if(rowEntity){
			$scope.productId = rowEntity.productId;
		}else{
			$scope.productId = null;
		}
		
		$scope.presentDetailPopup();
	}
	
	// 상세 팝업 호출
	$scope.presentDetailPopup = function() {
		url = "/pms/present/popup/detail";
		popupwindow(url, "사은품 상세", 1000, 420);
	}
	
}).controller("pms_presentRegPopApp_controller", function($window, $scope, presentService, productService, commonService, gridService, commonPopupService) {
/* ******************insert POPUP Controller *************************/
	// 팝업에서 부모 scope 접근하기 위함.		
	pScope = $window.opener.$scope;
	
	$scope.pmsProduct = {};
	$scope.pmsProduct.pmsSaleproduct = [];//단품
	
	$scope.searchPresent = function(productId) {
		$scope.pmsProduct.productId = productId;
		
		presentService.getPresentProductDetail($scope.pmsProduct, function(data) {
			$scope.pmsProduct = data;
			$scope.pmsProduct.saveType = "U";
			
			if($scope.pmsProduct.pmsProductimg == null) {
				$scope.pmsProduct.imgInsert = "C";
			} else {
				$scope.pmsProduct.pmsProductimg.orgPath = $scope.pmsProduct.pmsProductimg.img; 
				$scope.pmsProduct.imgInsert = "U";
			}
		});
	}
	
	// 초기화
	angular.element(document).ready(function () {
		// 팝업 저장 후 grid Show 값
//		commonService.init_search($scope,'spsPresent');
		
		if(pScope.productId != null) {
			$scope.searchPresent(pScope.productId);
		} else {
			$scope.pmsProduct.imgInsert = "C";
			$scope.pmsProduct.saveType = "I";
		}
	});

	// ERP 상품 검색 팝업
	this.erpProductSearch = function(type) {
		commonPopupService.erpProductSearchPopup($scope, "callbackErpProduct", false);
	}
	// ERP상품 검색 팝업 콜백
	$scope.callbackErpProduct = function(data) {
		// TODO 재고수량 은 나중 batch 로 동작
		console.log("outer return",data);
		productService.getErpItem(data[0].itemid, function(data){
			
			console.log("inner return",data);
			
			$scope.pmsProduct.erpProductId = data[0].itemid; // erp 상품 코드
			$scope.pmsProduct.taxTypeCd = data[0].apxTax==0 ? 'TAX_TYPE_CD.TAX' : 'TAX_TYPE_CD.FREE';//과세구분 :0 과세, 1 비과세 ( 확인바람 )
			
			var pmsSaleproduct = {};
			pmsSaleproduct.saleproductId = $scope.pmsProduct.pmsSaleproduct.saleproductId;
			pmsSaleproduct.erpSaleproductId = data[0].itembarcode;
			pmsSaleproduct.name = "size :" + data[0].inventsizeid + ", color:" + data[0].inventcolorid;
			pmsSaleproduct.addSalePrice = 0;
			pmsSaleproduct.realStockQty = 0;
			pmsSaleproduct.saleproductStateCd = "SALEPRODUCT_STATE_CD.SALE";
			pmsSaleproduct.pmsSaleproductoptionvalues = [
				                              {
				                            	  optionName : 'size',
				                            	  optionValue : data[0].inventsizeid
				                              },
				                              {
				                            	  optionName : 'color',
				                            	  optionValue : data[0].inventcolorid
				                              }
				                              ];
				
			$scope.pmsProduct.pmsSaleproduct = pmsSaleproduct;//단품 모델
		});
	}
	
	
/* *********************** 버튼 *************************/
	this.savePresent = function() {
		
		if(!$scope.paramCheck()) return false;
		
		$scope.pmsProduct.offshopPickupDcRate = '0';
		$scope.pmsProduct.dcDisplayYn = 'Y';
		$scope.pmsProduct.realStockQty = '0';
		
		
		presentService.savePresent($scope.pmsProduct, function(response) {
			// 사은품 프로모션에서 수정한 경우
			if(angular.isUndefined(pScope.isPromotion)) {
				$scope.searchPresent(response.content);
				pScope.searchPresent(response.content);
			}
			alert("저장이 완료되었습니다.")
			$window.close();
		});
	}

	// 파라메터 체크
	$scope.paramCheck = function() {
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return false;
		}
		
		// 사은품 설명
		if(angular.isUndefined($scope.pmsProduct.detail) || $scope.pmsProduct.detail == '') {
			alert("사은품 설명을 입력해 주세요.");
			angular.element("#presentDetail").focus();
			return false;
		}
		
		return true;
	}
	
	$scope.uploadImageCallback = function(path) {
		if($scope.pmsProduct.pmsProductimg == null) {
			$scope.pmsProduct.pmsProductimg = {};
		}
		$scope.pmsProduct.pmsProductimg.img = path;
		$scope.pmsProduct.pmsProductimg.crudType = $scope.pmsProduct.imgInsert;
		
	}
	
	// 이미지 삭제
	this.deleteImage = function(){
		if($scope.pmsProduct.pmsProductimg != null) {
			if(confirm("이미지를 삭제 하시겠습니까?")){
				var target = angular.copy($scope.pmsProduct.pmsProductimg);
				
				//신규 템프 파일은 바이너리 바로 삭제
				if(target.crudType=='C'){
					commonService.deleteFile(target.img, function(data){
						console.log("b",$scope.pmsProduct.pmsProductimg);
						//모델에서 삭제
						delete $scope.pmsProduct.pmsProductimg;
						console.log("a",$scope.pmsProduct.pmsProductimg);
						
					});
					
				} else {
					$scope.pmsProduct.pmsProductimg.crudType = "D";
					$scope.pmsProduct.pmsProductimg.orgPath = "";
				}
			} else {
				return false;
			}
		}
	}
	
	this.closePopup = function() {
		$window.close();
	}
});

