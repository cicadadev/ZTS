var productListApp = angular.module("productListApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule'
                                                       , 'ui.date']);
Constants.message_keys = ["common.label.confirm.save"];

productListApp.controller("pms_productListApp_controller", function($window, $scope, $filter, productService, commonService, gridService, commonPopupService) {
	
	// PO로그인일경우 업체ID
	var poBusinessId = global.session.businessId=='null' ? null : global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	if(!common.isEmpty($("#pageId").val())){
		$scope.pageId = $("#pageId").val();
		console.log($scope.pageId);
	}

	
	// 검색 조건 초기화 내용 저장
	angular.element(document).ready(function () {	
		
		commonService.init_search($scope,'search');

	});			
	
	var cellTemplate = "<div class=\"ui-grid-cell-contents\" title=\"\"><button class=\"btn_type2\" ng-click=\"grid.appScope.copyReg(row.entity)\"><b>복사등록</b></button></div>";
	
	var columnDefs = [];
        columnDefs.push({ field: 'productId',			width:'100',		displayName : "상품번호", linkFunction : "openProductPopup" , type:"number"});      			
		columnDefs.push({ field: 'name',				width:'100',        colKey:"pmsProduct.name", linkFunction : "openProductPopup"});       
		columnDefs.push({ field: 'pmsCategory.name',	width:'100', 		colKey:"c.pmsCategory.category" });
		columnDefs.push({ field: 'productTypeName',	width:'100',        displayName : "상품유형",	vKey:"pmsProduct.productTypeCd" });
		columnDefs.push({ field: 'saleStateName',		width:'100',        displayName : "판매상태", 	vKey:"pmsProduct.saleStateCd"});
		columnDefs.push({ field: 'pmsBrand.name',		width:'100',		colKey:"pmsBrand.name"});
		columnDefs.push({ field: 'listPrice',		type : 'number', 		width:'100',		colKey:"pmsProduct.listPrice"});
		columnDefs.push({ field: 'salePrice',		type : 'number', 		width:'100',		colKey:"pmsProduct.salePrice"});
		columnDefs.push({ field: 'supplyPrice',		type : 'number', 		width:'100',		colKey:"pmsProduct.supplyPrice"});
		columnDefs.push({ field: 'commissionRate',	type : 'number',		width:'100',		colKey:"pmsProduct.commissionRate"});
		columnDefs.push({ field: 'erpProductId',		width:'100',		colKey:"pmsProduct.erpProductId"});
		columnDefs.push({ field: 'productGubun', 		width:'100',		colKey:"c.pms.product.gubun"}); 
		
		// PO 일경우
		if(common.isEmpty(poBusinessId)){
			columnDefs.push({ field: 'ccsBusiness.businessId',	width:'100',    displayName : "공급업체번호" });
			columnDefs.push({ field: 'ccsBusiness.name',	width:'100', 		displayName : "공급업체명" });
		}
		
		columnDefs.push({ field: 'dmsCategoryName',	width:'100', 		colKey:"c.dmsDisplaycategory.category" });
		columnDefs.push({ field: 'saleStartDt', 		width:'100',		colKey:"pmsProduct.saleStartDt" });
		columnDefs.push({ field: 'saleEndDt', 			width:'100',		colKey:"pmsProduct.saleEndDt"});
		columnDefs.push({ field: 'productCopy',		width:'100',		colKey:"c.pms.product.copy.reg", cellTemplate :cellTemplate});
		columnDefs.push({ field: 'insId', 				width:'100',		userFilter :'insId,insName',	colKey: "c.grid.column.insId"  });
		columnDefs.push({ field: 'insDt', 				width:'100',		colKey: "c.grid.column.insDt" , cellFilter: "date:\'yyyy-MM-dd\'" });
		columnDefs.push({ field: 'updId', 				width:'100',		userFilter :'updId,updName',	colKey: "c.grid.column.updId"  });
		columnDefs.push({ field: 'updDt', 				width:'100',		colKey: "c.grid.column.updDt" , cellFilter: "date:\'yyyy-MM-dd\'" });
		         

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_product",	//mandatory
			url :  '/api/pms/product',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	// 검색 조건 초기화
	this.resetData = function() {
		/* search Data 초기화 */
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	this.eraser = function(val1, val2) {
		$scope.search[val1] = "";
		
		if(!common.isEmpty(val2)) {
			$scope.search[val2] = "";
		}
	}
	
	this.getProductList = function(){

		$scope.myGrid.loadGridData();
	}
	
	// 각 검색 팝업
	this.openPopup = function(kindOf) {
		if(kindOf == 'brand') {
			commonPopupService.brandPopup($scope,"callback_brand", false);
			$scope.callback_brand = function(data){
				$scope.search.brandId = data[0].brandId;
				$scope.search.brandName = data[0].name;
				
				$scope.$apply();		
			}
		} else if(kindOf == 'pms/category' || kindOf == 'dms/displayCategory') {
			var url = '/' + kindOf + '/popup/search';
			commonPopupService.categoryPopup($scope, "callback_category", false, url);
			$scope.callback_category = function(data) {
				console.log(data);
				if (kindOf == 'pms/category') {
					$scope.search.categoryId = data.categoryId;
					$scope.search.categoryName = data.name;
				} else {
					$scope.search.dispCategoryId = data.displayCategoryId;
					$scope.search.dispCategoryName = data.name;
				}
				$scope.$apply();
			}
		} else if(kindOf == 'md') {
			commonPopupService.userPopup($scope,"callback_user",false,"USER_TYPE_CD.MD");
			$scope.callback_user = function(data){
				$scope.search.userId = data[0].userId;
				$scope.search.userName = data[0].name;
				$scope.$apply();
			}

		} else if(kindOf == 'business') {
			commonPopupService.businessPopup($scope,"callback_business",false);
			$scope.callback_business = function(data){
				$scope.search.businessId = data[0].businessId;
				$scope.search.businessName = data[0].name;
				$scope.$apply();		
			}
		}
	}
	
	// 상품 복사
	$scope.copyReg = function(rowEntity) {
		var productCopy = true;
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId, productCopy);
	}
	
	//상품 상세 팝업
	$scope.openProductPopup = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
	
	// 신규 등록 팝업 호출
	$scope.openNewProductPopup = function(isSetProduct){
		$scope.isSetProduct = isSetProduct;
		commonPopupService.openProductDetailPopup($scope, null);
	}
	
	// 상품일괄 (가격예약, 변경예약, 등록, 변경)
	$scope.bulkType = '';
	this.bulk = {
		download : {
//			console.log("########### download ########");
//			return alert('download');
		},
		upload : {
			excel : function(flag) {
				$scope.bulkType = flag;
				var winName = 'Product Bulk Upload';
				var winURL = Rest.context.path + "/pms/product/popup/bulkupload";

				var width = 1100;
				var height = 400;
				switch (flag) {
					case 1:
						winName += ' (가격변경예약)';
						break;
					case 2:
						winName += ' (상품변경예약)';
						break;
					case 3:
						winName += ' (상품일괄등록)';
						height = 530;
						break;
					case 4:
						winName += ' (상품일괄변경)';
						break;
				}
//				$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
				popupwindow(winURL, winName, width, height);
			}
		}
	}
});

//엑셀 일괄 업로드
productListApp.controller("bulkUploadCtrl", function($window, $scope, productService, commonService) {
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.bulkType = pScope.bulkType;
	var upPath = '';
	var fileName = '';
	switch ($scope.bulkType) {
		case 1:
			$scope.bulkTypeTxt = '상품가격 예약변경';
			upPath = 'price';
			fileName += '상품가격변경예약_템플릿.xlsx';
			break;
		case 2:
			$scope.bulkTypeTxt = '상품정보 예약변경';
			upPath = 'status';
			fileName += '상품정보변경예약_템플릿.xlsx';
			break;
		case 3:
			$scope.bulkTypeTxt = '상품 일괄등록';
			upPath = 'insert';
			fileName += '상품일괄등록_템플릿.xlsx';
			break;
		case 4:
			$scope.bulkTypeTxt = '상품정보 일괄 즉시변경';
			upPath = 'update';
			fileName += '상품일괄변경_템플릿.xlsx';
			break;
		case 5:
			$scope.bulkTypeTxt = '품목정보 일괄등록';
			upPath = 'notice';
			fileName += '상품품목정보_템플릿_All.zip';	
			break;
	}

	$scope.files = [];
	// listen for the file selected event
	$scope.$on("fileSelected", function(event, args) {
		$scope.$apply(function() {
			// add the file object to the scope's files collection
//			var ele = event.target.id;
//			if (ele == 'img') {
//				if (!/(\.zip|\.png|\.jpg|\.jpeg)$/i.test((args.file.name).toLowerCase())) {
//					alert("파일형식은 .zip.png.jpg.jpeg만 지원됩니다.");
//					$scope.filePath = "";
//				} else {
//					$scope.files.push(args.file);
//				}
//			} else {
//				if (!/(\.xlsx|\.xls)$/i.test(args.file.name)) {
//					alert("엑셀 파일이 아닙니다.");
//					$scope.filePath = "";
//				} else {
//					$scope.files.push(args.file);
//				}
//			}
			$scope.files.push(args.file);
		});
	});

	// 팝업 닫기
	this.close = function() {
		$window.close();
	}

	// 파일 경로 지우기
	this.eraser = function() {
		$scope.filePath = "";
	}

	// 파일 일괄 업로드
	uploadfile = function(file, url){
		commonService.uploadFileToUrl(file, null, url, function(response) {
			var result = JSON.parse(response);
			$scope.totalCnt = result.totalCnt;
			$scope.successCnt = result.successCnt;
			$scope.failCnt = result.failCnt;
			$scope.excelPath = result.excelPath;
			
			alert('업로드를 완료하였습니다.');
			
			if ($scope.$$phase != '$apply' && $scope.$$phase != '$digest') {
				$scope.$apply();
			}
		});
	}
	this.checkExcel = function() {
		var file = $scope.files[0];
		if (file != null) {
			if (!/(\.xlsx|\.xls)$/i.test(file.name)) {
				$scope.filePath = "";
				alert("엑셀 파일이 아닙니다.");
				return false;
			}
			var url = Rest.context.path + '/api/pms/product/bulk/' + upPath;
			uploadfile(file, url);
		}
	}
	this.checkImg = function() {
		var file = $scope.files[0];
		if (file != null) {
			if (!/(\.zip|\.png|\.jpg|\.jpeg)$/i.test((file.name).toLowerCase())) {
				$scope.filePath = "";
				alert("파일형식은 .zip.png.jpg.jpeg만 지원됩니다.");
				return false;
			}
			var url = Rest.context.path + '/api/pms/product/bulk/img';
			uploadfile(file, url);
		}
	}

	// 엑셀템플릿 다운로드 
	this.downTemplate = function() {
		//location.href = Rest.context.path + '/resources/excel/template/상품가격변경예약_템플릿.xlsx';
		commonService.getConfig("excel.download.path.template", function(response) {
			var fullPath = response.content + '/' + fileName;
			$window.location = fullPath;
		});
//		$.ajax({
//			type : 'get',
//			url : 'C:/ZTS/project/gcp2.0_admin/src/main/resources/config/system/properties.xml',
//			dataType : 'xml',
//			success : function(data){
//				$(data).find('entry').each(function(){
//					if($(this).attr('key') == 'excel.upload.path.error'){
//						console.log('>>>>>' + $(this).text());
//						alert('excel.upload.path.error : ' + $(this).text());
//					}
//				});
//			},
//			error : function(xhr, status, error){
//				alert('error : ' + error);
//			}
//		});
	}

	// 오류데이터 다운로드
	this.downFailDataExcel = function() {
		if ($scope.excelPath) {
			$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + $scope.excelPath;
		}
	}
	
	

});
