var catalogApp = angular.module("catalogApp", ['commonServiceModule', 'dmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule', 'pmsServiceModule', 'ui.date']);

// 메시지
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "dms.catalog.detail.title",
                          "dms.catalog.moveTap.message1", "dms.catalog.moveTap.message2", "c.dms.catalog.confirm.delete",
                          "dms.catalog.img.btn.register", "dms.catalog.btn.register", "dms.catalog.img.register.validation",
                          "dms.catalog.searchbrand.already", "common.label.alert.cancel", "dms.catalog.confirm.close", 
                          "common.label.alert.delete", "common.label.confirm.delete"];

// 브랜드 컨텐츠 관리 메인 컨트롤러
catalogApp.controller("dms_catalogManagerApp_controller", function($window, $scope, $filter,
		commonService, gridService, commonPopupService, catalogService) {
	
	// 객체 생성
	$scope.catalogImg_grid = {};
	$scope.catalogProduct_grid = {};
	
	// 팝업에서 부모 scope 접근하기 위함.
	$window.$scope = $scope;
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	// LOOKBOOK 컨텐츠 그리드
	var columnDefs =  [        
	    { field: 'catalogId', colKey: "c.dms.catalog.catalogId", vKey: "c.dms.catalog.catalogId" , linkFunction: 'catalogDetail(row.entity)' }
		, { field: 'name', colKey: "c.dms.catalog.catalog.name", linkFunction: 'catalogDetail(row.entity)' }
		, { field: 'pmsBrand.name', colKey: "c.pmsBrand.brand" }
		, { field: 'catalogTypeName', colKey: "c.dms.catalog.catalogType", vKey: "c.dms.catalog.catalogType" }
		, { field: 'sortNo', colKey: "c.dms.catalogimg.sortNo", enableCellEdit: true, type:"number" }
		, { field: 'displayYn', colKey: "dmsCatalog.displayYn", enableCellEdit: true, cellFilter: 'displayYnFilter' }
		, { field: 'insId', colKey: "c.grid.column.insId" , userFilter :'insId,insName'}
		, { field: 'insDt', colKey: "c.grid.column.insDt", enableCellEdit: false, cellFilter: 'date:\'yyyy/MM/dd\'' }
		, { field: 'updId', colKey: "c.grid.column.updId" , userFilter :'updId,updName'}
		, { field: 'updDt', colKey: "c.grid.column.updDt", enableCellEdit: false, cellFilter: 'date:\'yyyy/MM/dd\'' }

	];
		
	var gridParam = {
		scope : $scope, 
		gridName : 'grid_catalog',
		url : '/api/dms/catalog',
		searchKey : "search", 
		columnDefs : columnDefs,
		gridOptions : {
		},
		callbackFn : function() {
		}
	};
	$scope.catalog_grid = new gridService.NgGrid(gridParam);
	
	// 컨텐츠 상세
	$scope.catalogDetail = function(data) {
		$scope.catalogId = data.catalogId;
		$scope.catalogName = data.name;
		var winURL = Rest.context.path + "/dms/catalog/popup/detail";
		popupwindow(winURL, $scope.MESSAGES["dms.catalog.detail.title"], 1200, 800);
	}
	
	// 컨텐츠 등록 팝업 호출
	this.registerCatalog = function() {
		$scope.catalogId = "";
		var winURL = Rest.context.path + "/dms/catalog/popup/detail";
		popupwindow(winURL, $scope.MESSAGES["dms.catalog.btn.register"], 1200, 800);
	}
	
	// 초기화_ search Data 초기화
	$scope.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element('.day_group').find('button').eq(0).click();
	}
	
	//브랜드 검색 팝업
	this.brandSearch = function() {
		commonPopupService.brandPopup($scope, "callback_brand", false);
	}
	$scope.callback_brand = function(data) {
		if ($scope.search.brandId == data[0].brandId) {
			alert(pScope.MESSAGE["dms.catalog.searchbrand.already"]);
            return;
		} else {
			$scope.search.brandId = data[0].brandId;
			$scope.search.brandName = data[0].name;
			$scope.$apply();
		}
	}
	
	//브랜드 검색내용 지우기
	this.eraseBrand = function() {
		$scope.search.brandId = "";
		$scope.search.brandName = "";
	}
	
	// 컨텐츠 목록 수정
	this.saveGridData = function() {
		var pUrl = Rest.context.path + "/api/dms/catalog/save";
		$scope.catalog_grid.saveGridData(pUrl, function(){
			$scope.catalog_grid.loadGridData();
		});
	}
	
// 컨텐츠 상세 / 등록 팝업 컨트롤러
}).controller("dms_catalogDetailPopApp_controller", function($window, $scope, $filter, $compile, 
		commonService, gridService, commonPopupService, catalogService, displayService) {
	
	pScope = $window.opener.$scope;
	
	// 객체 생성
	$scope.dmsCatalog = {};

	//an array of files selected
    $scope.files = [];

  //message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

    //listen for the file selected event
    $scope.$on("fileSelected", function (event, args) {
        $scope.$apply(function () {            
            //add the file object to the scope's files collection
            $scope.files.push(args.file);
        });
    });
	
	// 브랜드 컨텐츠 등록&상세 팝업 init Method
    $scope.dmsCatalog.pmsBrand = {};
	this.init = function() {
		if (pScope.catalogId != '' && pScope.catalogId != undefined) {
			catalogService.getCatalogDetail(pScope.catalogId, function(data) {
				$scope.dmsCatalog = data;			
			});
		}
	}
	
    // 탭 전환
	this.moveTab = function($event, param) {
		if ("img" == param) {
			if (confirm(pScope.MESSAGES["dms.catalog.moveTap.message1"])) {
				$window.location.href = Rest.context.path +"/dms/catalog/popup/imgInfo";
			}			
		} else if ("detail" == param) {		
			if (confirm(pScope.MESSAGES["dms.catalog.moveTap.message1"])) {				
				$window.location.href = Rest.context.path +"/dms/catalog/popup/detail";
			}
		}
	}
	
	// 컨텐츠 저장
	this.insertCatalog = function() {
		if (!commonService.checkForm($scope.form2)) {
			return;
		}
		
		if ($scope.dmsCatalog.catalogId != '' && $scope.dmsCatalog.catalogId != undefined) {
			if (confirm(pScope.MESSAGES["common.label.confirm.save"])) {
				catalogService.updateCatalog($scope.dmsCatalog, function(response) {
					if (response.content != "fail") {
						alert(pScope.MESSAGES["common.label.alert.save"]);
						
						pScope.catalog_grid.loadGridData();
					}
				});
			}
		} else {
			if (confirm("컨텐츠를 생성하시겠습니까?")) {
				catalogService.insertCatalog($scope.dmsCatalog, function(response) {
					if (response.content != "fail") {
						pScope.catalogId = response.content;
						alert("컨텐츠가 생성되었습니다.");
						$window.location.reload();
						pScope.catalog_grid.loadGridData();
					}
				});
			}
		}
			
	}

	// 취소(닫기)
	this.close = function(){
		if (!confirm($scope.MESSAGES["dms.catalog.confirm.close"])) {
			return;
		}

		$window.close();
	}
	
	// 이미지 삭제
	this.deletePcImage = function(name) {
		commonService.deleteFile($scope.dmsCatalog.img1, function(data){
			$scope.dmsCatalog.img1 = null;
	    });
		angular.element(document.querySelector('img[name='+ name+']')).attr("src", null);
		angular.element(document.querySelector('#' + name + 'Path')).val(null);
	}
	
	// 이미지 삭제
	this.deleteMbImage = function(name) {
		commonService.deleteFile($scope.dmsCatalog.img2, function(data){
			$scope.dmsCatalog.img2 = null;
		});
		angular.element(document.querySelector('img[name='+ name+']')).attr("src", null);
		angular.element(document.querySelector('#' + name + 'Path')).val(null);
	}
	
	//브랜드 검색 팝업
	this.brandSearch = function() {
		commonPopupService.brandPopup($scope, "callback_brand", false);
	}
	$scope.callback_brand = function(data) {
		if ($scope.brandId == data[0].brandId) {
			alert(pScope.MESSAGE["dms.catalog.searchbrand.already"]);
            return;
		} else {
			$scope.dmsCatalog.brandId = data[0].brandId,
			$scope.dmsCatalog.brandName = data[0].name
			$scope.$apply();
		}
	}
	
	//브랜드 검색내용 지우기
	this.eraseBrand = function() {
		$scope.dmsCatalog.brandId = null;
		$scope.dmsCatalog.brandName = null;
	}
	
// 컨텐츠 이미지 정보 탭 컨트롤러
}).controller("dms_catalogImgInfoPopApp_controller", function($window, $scope, $filter, $compile, 
		commonService, gridService, commonPopupService, catalogService, displayService, productService) {
	
	pScope = $window.opener.$scope;
	$window.$scope = $scope;
	
	// 객체 생성
	$scope.popupSearch = {};
	$scope.dmsCatalogimgs = [];
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	//  카탈로그 이미지 목록 그리드
	var imgColumnDefs =  [        
	      { field: 'catalogImgNo'	, colKey: "c.dms.catalog.catalogImgNo"	, linkFunction: 'openImgDetail(row.entity)'							}
   		, { field: 'name'			, colKey: "c.dms.catalog.img.title"		, linkFunction: 'openImgDetail(row.entity)', enableCellEdit: true 	}
   		, { field: 'sortNo'			, colKey: "c.dms.catalog.sortNo"		, enableCellEdit: true	, type:"number" 							}
   		, { field: 'displayYn'		, colKey: "dmsCatalog.displayYn"		, enableCellEdit: true	, cellFilter: 'displayYnFilter' 			}
   		, { field: 'insId'			, colKey: "c.grid.column.insId" 		, userFilter :'insId,insName'										}
   		, { field: 'insDt'			, colKey: "c.grid.column.insDt"			, cellFilter: 'date:\'yyyy/MM/dd\'' 								}
   		, { field: 'updId'			, colKey: "c.grid.column.updId" 		, userFilter :'updId,updName'										}   		
   		, { field: 'updDt'			, colKey: "c.grid.column.updDt"			, cellFilter: 'date:\'yyyy/MM/dd\'' 								}
   	];
   		
   	var imgGridParam = {
   		scope : $scope, 
   		gridName : 'grid_catalogImg',
   		url : '/api/dms/catalog/img',
   		searchKey : "popupSearch", 
   		columnDefs : imgColumnDefs,
   		gridOptions : {
   			enableRowSelection: true,
   			noUnselect : true,
			multiSelect: false,
			pagination : false,
			rowSelectionFn : function(row){
				$scope.getCatalogProduct(row.entity);
			}
   		},
   		callbackFn : function() {
			$scope.popupSearch.catalogId = pScope.catalogId;
			$scope.popupSearch.catalogName = pScope.catalogName;
			$scope.catalogImg_grid.loadGridData();
   		}
   	};
   	$scope.catalogImg_grid = new gridService.NgGrid(imgGridParam);
   	$scope.gridShow1 = true;
   	
   	
   	// 카탈로그 상품 목록 그리드
	var productColumnDefs =  [        
 	    { field: 'productId', colKey: "c.dms.catalog.productNo", linkFunction: 'openProductDetail(row.entity)' }
 		, { field: 'pmsProduct.name', colKey: "c.dms.catalog.product.name", linkFunction: 'openProductDetail(row.entity)' }
 		, { field: 'sortNo', colKey: "c.dms.catalog.sortNo", enableCellEdit: true, type:"number" }
 		, { field: 'pmsProduct.productTypeName', colKey: "c.dms.catalog.product.productType" }
 		, { field: 'pmsProduct.saleStateName', colKey: "c.dms.catalog.product.saleState" }
 		, { field: 'pmsProduct.salePrice', colKey: "pmsProduct.salePrice" }
 		, { field: 'pmsProduct.brandName', colKey: "c.pmsBrand.brand" }
 		, { field: 'pmsProduct.saleStartDt', colKey: "pmsProduct.saleStartDt", cellFilter: 'date:\'yyyy/MM/dd\'' }
 		, { field: 'pmsProduct.saleEndDt', colKey: "pmsProduct.saleEndDt", cellFilter: 'date:\'yyyy/MM/dd\'' }
 		, { field: 'insId', colKey: "c.grid.column.insId" , userFilter :'insId,insName'}
 		, { field: 'insDt', colKey: "c.grid.column.insDt", cellFilter: 'date:\'yyyy/MM/dd\'' }
 		, { field: 'updId', colKey: "c.grid.column.updId" , userFilter :'updId,updName'}
 		, { field: 'updDt', colKey: "c.grid.column.updDt", cellFilter: 'date:\'yyyy/MM/dd\'' }
 		
 	];
 		
 	var productGridParam = {
 		scope : $scope, 
 		gridName : 'grid_catalogProduct',
 		url : '/api/dms/catalog/product',
 		searchKey : "popupSearch", 
 		columnDefs : productColumnDefs,
 		gridOptions : {
 			pagination : false
 		},
 		rowSelectionFn : function(row){
			$scope.categoryImgProduct(row.entity);
		},
 		callbackFn : function() {
 		}
 	};
 	$scope.catalogProduct_grid = new gridService.NgGrid(productGridParam); 
 	$scope.gridShow2 = false;
 	
	// 상품 목록 그리드
	$scope.getCatalogProduct = function(data) {
		$scope.popupSearch.catalogImgNo = data.catalogImgNo;
		$scope.catalogProduct_grid.loadGridData();
		$scope.gridShow2 = true;	
	}
	
	
	// 이미지 상세
	$scope.openImgDetail = function(row) {
		$scope.selectCatalogImgNo = row.catalogImgNo;
		var winURL = Rest.context.path + "/dms/catalog/popup/imageInsert";
		popupwindow(winURL, "이미지상세", 970, 420);
	}
	
	//이미지 등록 팝업
	this.openItemRegPopup = function(itemType){
		if (itemType == 'img') {
			$scope.selectCatalogImgNo = "";
			var winURL = Rest.context.path + "/dms/catalog/popup/imageInsert";
			popupwindow(winURL, "이미지등록", 970, 420);
		} 
	}	
	
	// 상품 상세
	$scope.openProductDetail = function(data) {
		commonPopupService.openProductDetailPopup($scope, data.productId, false);
	}
	
    // 탭 전환
	this.moveTab = function($event, param) {
		if ("img" == param) {
			if (confirm(pScope.MESSAGES["dms.catalog.moveTap.message1"])) {
				$window.location.href = Rest.context.path +"/dms/catalog/popup/imgInfo";
			}			
		} else if ("detail" == param) {		
			if (confirm(pScope.MESSAGES["dms.catalog.moveTap.message1"])) {				
				$window.location.href = Rest.context.path +"/dms/catalog/popup/detail";
			}
		}
	}
	
 	
	// 이미지목록 그리드 데이터삭제 
	this.imgGridData = function() {
		$scope.catalogImg_grid.deleteGridData();
		$scope.gridShow2 = false;
	}
	
	// 상품 검색 팝업
	this.searchProduct = function() {
		commonPopupService.productPopup($scope, "callback_product", true);
	}
	
	$scope.callback_product = function(data) {
		for (var i=0; i< data.length; i++) {
			var flag = true;
			for (var j=0; j < $scope.grid_catalogProduct.data.length; j++) {
				if (data[i].productId == $scope.grid_catalogProduct.data[j].productId) {
					flag = false;
					break;
				}
			}
			
			if (flag) {
				$scope.catalogProduct_grid.addRow({
					pmsProduct : {productId : data[i].productId, name : data[i].name, productTypeName : data[i].productTypeName
						, saleStateName : data[i].saleStateName
						, salePrice : data[i].salePrice, brandName : data[i].brandName
						, saleStartDt : data[i].saleStartDt, saleEndDt : data[i].saleEndDt},
						crudType 				: 'I',
						productId : data[i].productId,
						displayYn : 'Y',
						catalogImgNo 	: $scope.popupSearch.catalogImgNo,
						catalogId	: $scope.popupSearch.catalogId
				});
			}
		}
	}
	
//	일괄 이미지 등록 & 그리드 호출 TODO
	$scope.dmsCatalogimgCallback = function(response) {
		var temp = response;

		if(!common.isEmptyObject(response)){
			for(i in response){
				
				if (!common.isEmpty(response[i].tempFullPath) && !common.isEmpty(response[i].fileName) && response[i].fileName.match(/(pc)/)) {
				
					var responseSortNo = response[i].fileName.split("_");
					var moImg = response[i].fileName.replace('pc', 'mo');
					if(!common.isEmpty(responseSortNo[1])){
						for(j in response){
							if(moImg == response[j].fileName){
								$scope.dmsCatalogimgs.push(
									{
										catalogId : $scope.popupSearch.catalogId,
										img1 : response[i].tempFullPath,
										img2 : response[j].tempFullPath,
										sortNo : responseSortNo[1],
										displayYn : 'Y',
										name : $scope.popupSearch.catalogName + "_" + responseSortNo[1]
									});
							}
						}
						
					}
						
				}
			}
			
			if (!confirm('이미 등록되있던 브랜드가 삭제됩니다. 그래도 등록하시겠습니까?')) {
				return;
			}
			
			console.log($scope.dmsCatalogimgs);
			
			if(!common.isEmptyObject($scope.dmsCatalogimgs)){
				catalogService.insertCatalogImgs($scope.dmsCatalogimgs, function(response) {
					$scope.catalogImg_grid.loadGridData();
				});
			}
		}
	}
	
	// 취소(닫기)
	this.close = function(){
		if (!confirm($scope.MESSAGES["dms.catalog.confirm.close"])) {
			return;
		}

		$window.close();
	}

	
	// 컨텐츠 이미지&상품 등록 컨트롤러	
}).controller("dms_catalogImgInsertPopApp_controller", function($window, $scope, commonService, catalogService) {

	pScope = $window.opener.$scope;

	// 객체 생성
	$scope.dmsCatalogimg = {};
	
	//an array of files selected
    $scope.files = [];
    
    $scope.search = {};

  //message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

    //listen for the file selected event
    $scope.$on("fileSelected", function (event, args) {
        $scope.$apply(function () {            
            //add the file object to the scope's files collection
            $scope.files.push(args.file);
        });
    });

    
    this.init = function() {
    	
    	if (pScope.selectCatalogImgNo != '' && pScope.selectCatalogImgNo != undefined) {
    		$scope.search.catalogId = pScope.popupSearch.catalogId;
    		$scope.search.catalogImgNo =pScope.selectCatalogImgNo;
    		catalogService.getCatalogImgDetail($scope.search, function(data) {
    			$scope.dmsCatalogimg = data;
    		});
    	}
    }
    
    // 이미지 등록
    this.insertCatalogImg = function() {
		// 폼 체크
		if (!commonService.checkForm($scope.form)) {
			return;
		}
		if ($scope.dmsCatalogimg.img1 == null || $scope.dmsCatalogimg.img1 == "") {
			alert("PC " + pScope.MESSAGES["dms.catalog.img.register.validation"]);
			return;
		}
		if ($scope.dmsCatalogimg.img2 == null || $scope.dmsCatalogimg.img2 == "") {
			alert("MOBILE " + pScope.MESSAGES["dms.catalog.img.register.validation"]);
			return;
		}
		
		if ($scope.search.catalogImgNo != '' && $scope.search.catalogImgNo != undefined) {
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
			catalogService.updateCatalogImg($scope.dmsCatalogimg, function(response) {
				alert($scope.MESSAGES["common.label.alert.save"]);
				pScope.catalogImg_grid.loadGridData();
				$window.close();
			});
		} 
		else {
			if (!confirm("이미지를 등록하시겠습니까?")) {
				return;
			}
			$scope.dmsCatalogimg.catalogId = pScope.popupSearch.catalogId;
			catalogService.insertCatalogImg($scope.dmsCatalogimg, function(response) {
				alert("이미지가 등록되었습니다.");
				pScope.catalogImg_grid.loadGridData();
				$window.close();
			});
		}
    }
    
	// 이미지 삭제
	this.deleteImage = function(name) {
		commonService.deleteFile($scope.dmsCatalogimg[name], function(data){
			$scope.dmsCatalogimg[name] = null;
	    });
		angular.element(document.querySelector('img[name='+ name+']')).attr("src", null);
		angular.element(document.querySelector('#' + name + 'Path')).val(null);
	}
    

	// 취소(닫기)
	this.close = function(){
		if (!confirm($scope.MESSAGES["dms.catalog.confirm.close"])) {
			return;
		}

		$window.close();
	}	
	
});