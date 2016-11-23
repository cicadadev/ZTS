var productDetailApp = angular.module("productDetailApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule', 'ccsServiceModule'
                                                       , 'ui.date', 'ngCkeditor']);
//상품 상세 팝업
productDetailApp.controller("productDetailController", function($window, $scope,$filter, productService, gridService, commonService, commonPopupService, userService, businessService, attributeService  ) {
	

	
	
	var pScope = $window.opener.$scope;//부모창의 scope
	
	//ck 에디터 설정
	$scope.ckOption = {
			language : 'ko'										
			, filebrowserImageUploadUrl : '/api/ccs/common/ckUpload'		
	}
	
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = '3_product';
	}

	
//	var _ZEROTO7_BUSN_ID = "1";// 자사(제로투세븐) 업체 ID
//	var ZEROTO7_BUSN_NAME = "제로투세븐";
	
	$scope.search = {};
	$scope.common = common;// 공통 함수를 jsp에서 사용하도록 설정
	
	$scope.pmsProduct = {};//상품 마스터
	$scope.pmsProduct.pmsBrand = {};//브랜드
	$scope.pmsProduct.pmsCategory = {};//표준카테고리
//	$scope.pmsProduct.ccsBusiness = {};//업체
	$scope.pmsProduct.dmsDisplaycategoryproducts = [];//전시카테고리
	$scope.pmsProduct.pmsSaleproducts = [];//단품
	$scope.pmsProduct.ccsDeliverypolicy = {};//배송정책
	$scope.pmsProduct.pmsProductattributes = []; // 상품속성
	$scope.pmsProduct.pmsSetproducts = [];//세트상품
	$scope.pmsProduct.pmsProductoptions = [];
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	// 단품 정보 변경 플래그 ( 서버로직에서 사용 )
	$scope.pmsProduct.changeSaleproductYn = 'N';

	$scope.isSetProduct = pScope.isSetProduct ? true : false; // 세트여부
	$scope.isCopyProduct = pScope.productCopy ? true : false; // 복사 등록 여부
//	$scope.productId = pScope.selectedProductId;// 상품 ID
	
	// 신규 여부
	$scope.isNew = pScope.selectedProductId && !$scope.isCopyProduct ? false : true;

//	pScope.selectedProductId = null;
	pScope.isSetProduct = null;
	pScope.productCopy = null;
	
	//기본값 설정
	
	$scope.pmsProduct.regularDeliveryYn = "N";
	$scope.pmsProduct.offshopPickupYn='N';
	$scope.pmsProduct.giftYn='N';
	$scope.pmsProduct.wrapVolume=0;
	$scope.pmsProduct.wrapYn='N';
	$scope.pmsProduct.overseasPurchaseYn='N';
	$scope.pmsProduct.minQty=1;
	$scope.pmsProduct.deliveryFeeFreeYn = 'N';
	if($scope.isSetProduct){
		$scope.pmsProduct.optionYn=='N';
	}else{
		$scope.pmsProduct.optionYn='Y';
	}
	
	$scope.pmsProduct.textOptionYn='N';
	$scope.pmsProduct.boxUnitQty=1;
	$scope.pmsProduct.unitQty=1;
	
	// MD로그인 여부
	$scope.isMdLogin = global.session.mdYn;
	////////////////////////////////////////////////////////////////////////////////////////////////
	// 그리드 초기화
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	//전시 카테고리 그리드
	var columnDefs = [
			             { field: 'displayCategoryId', 			width:120, colKey:"dmsDisplaycategory.displayCategoryId"},      			
			             { field: 'depthFullName',				colKey:"dmsDisplaycategory.name"},          
			             { field: 'displayYn',					width:120, colKey:"dmsDisplaycategory.displayYn" , cellFilter : 'displayYnFilter' }
			             ];
	
	var gridParam = {
			scope : $scope,
			gridName : "grid1",
			columnDefs : columnDefs,   
			gridOptions : { pagination : false }
	};
	
	$scope.dmsCategoryGrid = new gridService.NgGrid(gridParam);
	
	var saleProductGridEenableCellEdit = false;
	
	// 신규일때만 수정 가능
	if($scope.isNew){
		saleProductGridEenableCellEdit = true;
		
		var d = new Date(3000,11,31,23,59,59);
		//3000-12-31 23:59:59 
		$scope.pmsProduct.saleStartDt = $filter('date')(new Date(), Constants.date_format_1);
		$scope.pmsProduct.saleEndDt = $filter('date')(d, Constants.date_format_1);
		
		
		
		$scope.pmsProduct.claimInfo = ''
			+ '<div class="article_box">'
			+ '<span class="com_sub_title1">1. 국내 배송정보</span>'
			+ '<ul>'
			+ '<li>'
			+ '	<i>-</i> 배송지역 : 전국 가능'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 배송비 : 공급업체별 배송정책에 따름<br>'
			+ '	(도서,산간 등 일부 지역의 경우 추가배송비가 발생 할 수 있습니다.)'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 배송기간 : 결제완료/입금 확인일로부터 2일 ~ 5일 소요<br>'
			+ '	(주말, 공휴일은 배송 기간에서 제외됩니다.)<br>'
			+ '	<b>'
			+ '		<i>※</i> 도서,산간지역 및 사서함주소인 경우 배송이 다소 지연 될 수 있습니다.'
			+ '	</b>'
			+ '</li>'
			+ '</ul>'
			+ '<span class="com_sub_title1">2. 해외 배송정보</span>'
			+ '<ul>'
			+ '<li>'
			+ '	<i>-</i> 배송지역: 전국가능'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 배송비 : 공급업체별 배송정책에 따름<br>'
			+ '	(도서,산간 등 일부 지역의 경우 추가배송비가 발생 할 수 있습니다.)'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 배송기간: 결제완료 / 입금 확인 후 해외 발주가 진행되므로, 평균 10일~15일 정도 소요됩니다.<br>'
			+ '	(주말, 공휴일은 배송 기간에서 제외됩니다.)<br>'
			+ '	<b>'
			+ '		<i>※</i> 해외구매대행 상품의 경우 현지 배송사정, 연휴 및 통관 등의 사유로 배송이 지연 될 수 있습니다.'
			+ '	</b>'
			+ '</li>'
			+ '</ul>'
			+ '<span class="com_sub_title1">3. 교환/반품정보</span>'
			+ '<p><em>상품 수령 후 7일 이내에 신청 가능합니다. 단, 상품이 표시,광고의 내용과 다르거나, 다르게 이행된 경우 상품 수령일부터 3개월, 그 사실을 안 날 또는 알 수 있었던 날로부터 30일 이내 청약철회를 할 수 있습니다.</em></p>'
			+ '<ul>'
			+ '<li>'
			+ '	<i>-</i> 단순 변심 및 고객의 사정에 의한 교환/반품하실 경우, 배송비는 고객님 부담이며, 상품 불량 또는 오배송으로 인한 배송비는 제로투세븐닷컴에서 부담합니다.'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 해외구매대행 상품의 경우 교환/반품시 취소수수료가 발생할 수 있으며, 취소수수료는 소비자 부담입니다'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 해외구매대행 상품의 교환/반품은 반드시 고객센터로 문의 후, 안내절차에 따라 진행 해 주셔야 합니다. 임의로 상품을 해외 판매처로 반송 시 반품이 불허될 수 있으며, 제반 비용은 소비자가 부담해야 합니다.'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 해외구매대행 상품의 경우 국내에 재고를 보유하고 있지 않으므로 즉시 교환 불가하며, 구매상품 반품  후 재구매로 진행될 수 있습니다.'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 해외구매대행 상품의 경우 국내 제조사 및 판매처와 연계되어 있지 않으므로, A/S가 불가합니다.'
			+ '</li>'
			+ '</ul>'
			+ '<span class="com_sub_title1">4. 교환/반품 불가 사유</span>'
			+ '<p>'
			+ '<em>다음의 경우 반품/교환이 불가능합니다.</em>'
			+ '</p>'
			+ '<ul>'
			+ '<li>'
			+ '	<i>-</i> 반품교환 가능 기간을 초과한 경우'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 포장을 개봉하였거나 포장이 훼손되어 상품가치가 현저히 상실된 경우'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 상품의 구성품 중 일부가 누락되었거나 파손된 경우'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 고객님의 책임 있는 사유로 상품 등이 멸실 또는 훼손된 경우'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 사용 또는 일부 소비에 의하여 상품의 가치가 현저히 감소된 경우'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 고객님의 요청에 따라 상품사양이 변경되어 주문 제작된  경우'
			+ '</li>'
			+ '<li>'
			+ '	<i>-</i> 증명서나 상품태그, 라벨 등을 분리, 제거, 훼손된 경우'
			+ '</li>'
			+ '</ul>'
			+ '</div>';
	
		
	}
	
	//단품 목록 그리드
		//, enableCellEdit : true, vKey:"pmsSaleproduct.realStockQty"
        //, enableCellEdit : saleProductGridEenableCellEdit, vKey:"pmsSaleproduct.addSalePrice"
	var columnDefs2 = [
			             { field: 'saleproductId',		colKey:"pmsSaleproduct.saleproductId" },      			
			             { field: 'name',				colKey:"pmsSaleproduct.name" },          
			             { field: 'erpSaleproductId', 	displayName : "ERP코드/모델명"},
			             { field: 'saleproductStateCd',	colKey:"pmsSaleproduct.saleproductStateCd", enableCellEdit : true, dropdownCodeEditor : "SALEPRODUCT_STATE_CD", cellFilter : "saleproductStateFilter" },
			             { field: 'realStockQty',		colKey:"pmsSaleproduct.realStockQty" , enableCellEdit : true, vKey:"pmsSaleproduct.realStockQty"},
			             { field: 'addSalePrice',		colKey:"pmsSaleproduct.addSalePrice" , enableCellEdit : true, vKey:"pmsSaleproduct.addSalePrice" },
			             { field: 'sortNo',				colKey:"pmsSaleproduct.sortNo" , 		enableCellEdit : true, vKey:"pmsSaleproduct.sortNo" }
			             ];
	
	
	var gridParam2 = {
			scope : $scope, 			//mandatory
			gridName : "grid2",	//mandatory
			columnDefs : columnDefs2,    //mandatory
			gridOptions : { pagination : false, 
							checkBoxEnable : false},
			callbackFn : function(){
				// 상품 수정 화면일경우 상품 정보 조회
				if(pScope.selectedProductId){
					$scope.getProductDetail(pScope.selectedProductId);
				}
				
			}
	};
	$scope.saleProductGrid = new gridService.NgGrid(gridParam2);	

	
	
	new gridService.NgGrid({ scope : $scope,  gridName : "grid3",  gridOptions : { pagination : false }, columnDefs : [] });
	
	// 세트일경우 구성상품
	var setGridInit = function(){
			var columnDefs3 = [
					             { field: 'subProductId',			colKey:"pmsProduct.productId"},      		
					             { field: 'pmsProduct.name',		colKey:"pmsProduct.name" },          
					             { field: 'name',					colKey:"pmsSetproduct.name",  	enableCellEdit : true, vKey:"pmsSetproduct.name" },          
					             { field: 'qty', 					displayName : '구성수량',  	enableCellEdit : true, vKey:"pmsSetproduct.qty"}
					             ];
			
			
			var gridParam3 = {
					scope : $scope, 			//mandatory
					gridName : "grid3",	//mandatory
					columnDefs : columnDefs3,    //mandatory
					gridOptions : { pagination : false },
					callbackFn : function(){
						if($scope.pmsProduct.productId){
							
							// 구성상품 목록 조회
							productService.getSetProductList($scope.pmsProduct.productId, function(data){
								
								$scope.pmsProduct.pmsSetproducts = data;
								$scope.setProductGrid.setData($scope.pmsProduct.pmsSetproducts, false);
							});
						
						}
					}
			};
			$scope.setProductGrid = new gridService.NgGrid(gridParam3);
	}
	
	// 세트 구성상품 그리드 초기화
	if($scope.isNew && $scope.isSetProduct){
		setGridInit();
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	//  공통 검색 팝업 선언
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 브랜드 검색 팝업
	this.brandSearch = function() {
		commonPopupService.brandPopup($scope,"callbackBrandSearch", false);
	}
	// 브랜드 검색팝업 콜백
	$scope.callbackBrandSearch = function(data){
		$scope.pmsProduct.brandId = data[0].brandId;
		$scope.pmsProduct.pmsBrand = {};
		$scope.pmsProduct.pmsBrand.name = data[0].name;
		common.safeApply($scope);
	}
	
	// 세트상품 구성상품 추가를 위한 상품 검색 팝업
	this.productSearch = function() {
		commonPopupService.productPopup($scope,"callbackProductSearch", false);
	}
	// 상품 검색팝업 콜백
	$scope.callbackProductSearch = function(products){
		
		// 구성상품의 단품목록 조회
		productService.getSaleproductList(products[0].productId, function(data){
			
			var setCompProduct = {
					pmsProduct : { name : products[0].name},
					subProductId : products[0].productId,
					name : products[0].name,
					qty : 1,
					pmsSaleproducts : data
				}
			
			// 모델에 추가
			$scope.pmsProduct.pmsSetproducts.push(setCompProduct);
			
			//그리드에 추가
			$scope.setProductGrid.setData($scope.pmsProduct.pmsSetproducts);
			
		})
	}
	
	// 업체 검색 팝업
	this.businessSearch = function() {
		commonPopupService.businessPopup($scope,"callbackBusinessSearch",false);
	}
	// 업체 검색팝업 콜백
	$scope.callbackBusinessSearch = function(data){
		if(common.isEmptyObject($scope.pmsProduct)){
			$scope.pmsProduct = {};
		}
		$scope.pmsProduct.businessId = data[0].businessId;
		if(!$scope.pmsProduct.ccsBusiness){
			$scope.pmsProduct.ccsBusiness={};
		}
		
		//IE 버그로 객체매핑 못하고 컬럼단위 매핑
		$scope.pmsProduct.ccsBusiness.businessId = data[0].businessId;
		$scope.pmsProduct.ccsBusiness.saleTypeCd = data[0].saleTypeCd;
		$scope.pmsProduct.ccsBusiness.purchaseYn = data[0].purchaseYn;
		$scope.pmsProduct.ccsBusiness.overseasPurchaseYn = data[0].overseasPurchaseYn;
		$scope.pmsProduct.ccsBusiness.name = data[0].name;
		if($scope.pmsProduct.ccsBusiness.overseasPurchaseYn!='Y'){
			$scope.pmsProduct.overseasPurchaseYn='N';
		}	
		// 배송정책 조회 : 업체의 배송 정책을 조회하여 배송정책 항목에 combo box로 노출한다.
		$scope.pmsProduct.deliveryPolicyNo = '';//정책값초기화
		$scope.pmsProduct.ccsDeliverypolicy = {};//정책값초기화
////		$scope.selectDeliverypolicyByBusinessId(data[0].businessId, data[0].saleTypeCd);
//		
		// erp상품 여부
		setErpProductYn();
		
		// 판매방식이 위탁일경우 처리
		if($scope.erpProductYn == false){
			// ERP 관련 정보 삭제
			$scope.clearErpProductInfo();
			
			// 업체,카테고리별 수수료율 목록 조회
			getCommissionList();
			
		}
		
		common.safeApply($scope);		
	}
	

	
	// 업체,카테고리별 수수료율 조회
	var getCommissionList = function(){
		//$scope.pmsProduct.commissionRate = '';
			
		if(!common.isEmpty($scope.pmsProduct.businessId) && !common.isEmpty($scope.pmsProduct.categoryId)){
		
			var param = {businessId : $scope.pmsProduct.businessId, categoryId : $scope.pmsProduct.categoryId};
			businessService.getCommissionListByCategory(param, function(data){
				$scope.commissionList = data;
			})
		}
		
	}
	
	
	//업체 ID로 배송정책 조회
	$scope.selectDeliverypolicyByBusinessId = function(businessId, saleTypeCd){
		// 사입이면 0TO7 자사의 배송정책 조회
		if(saleTypeCd=='SALE_TYPE_CD.PURCHASE'){
			businessId =global.config.ownBusinessId;
		}
		businessService.selectDeliverypolicyByBusinessId( businessId , function(data){
			
			if(data.length > 0){
				$scope.policyList = data;
			}else{
				$scope.policyList = [];
			}
		})
		
	}
	
	// 배송정책 콤보박스 선택
	this.selectDeliverypolicy = function(deliveryPolicyNo){
		
		$scope.pmsProduct.deliveryPolicyNo = '';//정책값초기화
		$scope.pmsProduct.ccsDeliverypolicy = {};//정책값초기화
		
		for(var i = 0 ; i < $scope.policyList.length ; i++){
			if($scope.policyList[i].deliveryPolicyNo==deliveryPolicyNo){
				$scope.pmsProduct.deliveryPolicyNo = deliveryPolicyNo;
				$scope.pmsProduct.ccsDeliverypolicy = $scope.policyList[i];
			}
		}
	}
	
	// 표준 카테고리 검색 팝업
	this.categorySearch = function(type) {
		if(type=='pms'){
			commonPopupService.categoryPopup($scope, "callbackPmsCategory", false, '/pms/category/popup/search');
		}else{
			commonPopupService.categoryPopup($scope, "callbackDmsCategory", false, '/dms/displayCategory/popup/search');
		}
	}
	// 표준카테고리 검색팝업 콜백
	$scope.callbackPmsCategory = function(data) {
		$scope.pmsProduct.pmsCategory.depthFullName = data.depthFullName;
		$scope.pmsProduct.categoryId = data.categoryId;
		$scope.pmsProduct.pointSaveRate = data.pointSaveRate;
		
		//담당 md검색
		var param = {categoryId : data.categoryId};
		userService.getUserByCategoryId(param, function(response){
			
			$scope.pmsProduct.ccsUserMd = {};
			$scope.pmsProduct.ccsUserMd.name = response.name;
		});
		
		//속성 목록 조회
		$scope.getCategoryAttributeList(data.categoryId, "ATTR"); 
		
		
		// 업체,카테고리별 수수료율 목록 조회 ( 입점업체일 경우)
		getCommissionList();
		
		
		common.safeApply($scope);
	}
		
	// 전시카테고리 검색팝업 콜백
	$scope.callbackDmsCategory = function(data) {
		
		var displayCategoryProductMap = {
			displayCategoryId : data.displayCategoryId,
			productId : pScope.selectedProductId,
			displayYn : 'Y'
		};
		//그리드에 추가
		$scope.dmsCategoryGrid.addRow({
			displayYn : 'Y',
			displayCategoryId : data.displayCategoryId,
			depthFullName : data.depthFullName
		});
	}
	
	
	// ERP 상품 검색 팝업
	this.erpProductSearch = function(type) {
		commonPopupService.erpProductSearchPopup($scope, "callbackErpProduct", false);
	}
	// ERP상품 검색 팝업 콜백
	$scope.callbackErpProduct = function(data) {
		
		// itemid 로 단품 정보 조회
		productService.getErpItem(data[0].itemid, function(data){
			if(!data || data.length == 0){
				// ERP연동 정보 초기화
				$scope.clearErpProductInfo();
				alert("연동 불가한 상품 입니다.");
				return;
			}
				
			//$scope.pmsProduct.ccsBusiness = null;
			// erp 대상 업체가 아니면 초기화
//			if(!$scope.pmsProduct.ccsBusiness.erpBusinessId){
//				$scope.pmsProduct.ccsBusiness = null;
//				$scope.pmsProduct.businessId = null;
//			}
			
			//업체를 자사로 고정
//			if($scope.pmsProduct.businessId != ZEROTO7_BUSN_ID){
//				$scope.pmsProduct.businessId = ZEROTO7_BUSN_ID;
//				$scope.pmsProduct.ccsBusiness = {name : ZEROTO7_BUSN_NAME};
//				
//				$scope.pmsProduct.deliveryPolicyNo = '';//정책값초기화
//				$scope.pmsProduct.ccsDeliverypolicy = {};//정책값초기화
//				//자사 배송 정책 목록 조회
//				$scope.selectDeliverypolicyByBusinessId($scope.pmsProduct.businessId, "SALE_TYPE_CD.PURCHASE");
//			}

			
			// erp정보로 상품 정보 세팅
			$scope.pmsProduct.name = data[0].itemname;
			//$scope.pmsProduct.salePrice = data[0].tkrLikelysales; // 확인
			$scope.pmsProduct.erpProductId = data[0].itemid; //ERP 상품 코드
			$scope.pmsProduct.salePrice = data[0].tkrTagprice; //판매가
			$scope.pmsProduct.listPrice = data[0].tkrTagprice; //정상가
			$scope.pmsProduct.supplyPrice = data[0].tkrEstimatecost; // 공급가
			$scope.pmsProduct.origin = data[0].tkrOrigin;//원산지
			$scope.pmsProduct.maker = data[0].apxManufacture;//제조업체
			$scope.pmsProduct.taxTypeCd = data[0].apxTax==0 ? 'TAX_TYPE_CD.TAX' : 'TAX_TYPE_CD.FREE';//과세구분 :0 과세, 1 비과세 ( 확인바람 )
			
			if(data[0].pmsBrand && data[0].pmsBrand.brandId){
				$scope.pmsProduct.brandId = data[0].pmsBrand.brandId; // brandId
				$scope.pmsProduct.pmsBrand = {name : data[0].pmsBrand.name } // brandName
			}else{
				$scope.pmsProduct.brandId = ''; // brandId
				$scope.pmsProduct.pmsBrand = {name : '' } // brandName
			}
//				$scope.pmsProduct.categoryId = data[0].ztsStdcategory;//표준카테고리
			//수수료율 계싼
			$scope.changePrice($scope.pmsProduct);

			
			//단품 설정
			var pmsSaleproducs = [];
			
			$scope.erpNoticeInfo = {};
			$scope.erpNoticeInfo.tkrOrigin = data[0].tkrOrigin;
			$scope.erpNoticeInfo.inventsizeid = data[0].inventsizeid;
			$scope.erpNoticeInfo.inventcolorid = data[0].inventcolorid;
			$scope.erpNoticeInfo.apxManufacture = data[0].apxManufacture;
//				$scope.erpNoticeInfo.materialGroupName = data[0].materialGroupName;
			
			// 단품 옵션 세팅
			if(data.length==1 && common.isEmpty(data[0].inventsizeid) && common.isEmpty(data[0].inventcolorid)){// 옵션 없는 상품 설정
				
				$scope.pmsProduct.optionYn= "N";
				
				// 옵션 없는 단품 세팅
				var saleProduct = getNoOptionProduct();
				pmsSaleproducs.push(saleProduct);
				
			}else{// 옵션이 존재하는 경우
				
				$scope.pmsProduct.optionYn= "Y";
				var inventsizeidArr = [];
				var inventcoloridArr = [];
				
				// 옵션명 객체에 담음
				var optionNames = [];
				if(!common.isEmpty(data[0].option1Name)){
					optionNames.push(data[0].option1Name);
				}
				if(!common.isEmpty(data[0].option2Name)){
					optionNames.push(data[0].option2Name);
				}
				
				for(var i = 0 ; i < data.length ; i++){
					
					var saleProduct = {
						erpSaleproductId : data[i].itembarcode,
						erpColorId : data[i].inventcolorid,
						erpSizeId : data[i].inventsizeid,
						name : data[i].inventsizeid + "/"+data[i].inventcolorid,// 단품명
						addSalePrice : 0,
						realStockQty : 999,
						saleproductStateCd : "SALEPRODUCT_STATE_CD.SALE",
						pmsSaleproductoptionvalues : [
						                              {
						                            	  optionName : data[i].option1Name,
						                            	  optionValue : data[i].inventsizeid
						                              },
						                              {
						                            	  optionName : data[i].option2Name,
						                            	  optionValue : data[i].inventcolorid
						                              }
						                              ]
					}
					
					pmsSaleproducs.push(saleProduct);
					
					// 색상, 사이즈 고시정보 에 매핑하기 위해 스트링 만들기
					if(inventsizeidArr.indexOf(data[i].inventsizeid) < 0){
						inventsizeidArr.push(data[i].inventsizeid);
					};
					if(inventcoloridArr.indexOf(data[i].inventcolorid) < 0){
						inventcoloridArr.push(data[i].inventcolorid);
					};
				}

				
				// 상품옵션정보 ( pms_productoption)
				$scope.pmsProduct.pmsProductoptions = [];
				if(optionNames.length > 0){
					for(var i = 0 ; i < optionNames.length ; i++){
						$scope.pmsProduct.pmsProductoptions.push({
							optionName : optionNames[i],
							sortNo : i})
					}
				}
				
				
				$scope.erpNoticeInfo.inventsizeid = inventsizeidArr.join();
				$scope.erpNoticeInfo.inventcolorid = inventcoloridArr.join();
			}
			
			
			// erp고시정보와 매핑
			setErpNoticeInfo();
			
			$scope.pmsProduct.pmsSaleproducts = pmsSaleproducs;//단품 모델
			$scope.saleProductGrid.setData(pmsSaleproducs, false);//단품 그리드
			
			// 단품 변경 플래그
			$scope.pmsProduct.changeSaleproductYn = "Y";
				
				
			
			//console.log(data);
		});
	
		
	}
	
	//단품 정보 초기화
	$scope.clearSaleProduct = function(){
		$scope.pmsProduct.pmsSaleproducts = [];//단품
		$scope.saleProductGrid.setData([], false);//단품
		
		// 단품 변경 플래그
		$scope.pmsProduct.changeSaleproductYn = "Y";
		
		// 상품 옵션 초기화
		$scope.pmsProduct.pmsProductoptions = [];
		
	}
	
	//ERP상품 정보를 초기화 : 입점 업체 선택시, erp상품 변경시
	$scope.clearErpProductInfo = function(){
		

//		$scope.pmsProduct.name = '';
		//$scope.pmsProduct.salePrice = data[0].tkrLikelysales; // 확인
		$scope.pmsProduct.erpProductId = ''; //erp상품ID
		$scope.pmsProduct.salePrice = 0; //판매가
		$scope.pmsProduct.listPrice = 0; //정상가
		$scope.pmsProduct.supplyPrice = 0; // 공급가
//		$scope.pmsProduct.origin = '';//원산지
//		$scope.pmsProduct.maker = '';//제조업체
		
//		$scope.pmsProduct.maker = data[0].apxTax;//과세구분 : int형으로 넘어옴
//		$scope.pmsProduct.brandId = data[0].tkrBrand; // brandId
//		$scope.pmsProduct.pmsBrand = {}; // brandId
//		$scope.pmsProduct.categoryId = data[0].ztsStdcategory;//표준카테고리
		
		$scope.erpNoticeInfo = {};
		
		//단품 초기화
		$scope.clearSaleProduct();
		
		
		
	}
	
	// 표준카테고리에 해당하는 속성 기준정보 조회
	$scope.getCategoryAttributeList = function(categoryId, type){
		if(common.isEmpty(categoryId)){
			$scope.categoryAttributeList = []; 
			return;
		}
		var param = {categoryId : categoryId, attributeTypeCd : type };// 
		attributeService.getCategoryAttributeList(param).then(function(data){
			
			var pmsProductattributes = $scope.pmsProduct.pmsProductattributes;
			
			//속성 항목 ng-model에 매핑
			for(var i = 0 ; i < data.length ; i++){
				
				data[i].selects = [];
				
				if(angular.isDefined(pmsProductattributes)){
					for(var j = 0 ; j < pmsProductattributes.length ; j++){
						if(data[i].attributeId==pmsProductattributes[j].attributeId){
							
							if(pmsProductattributes[j].attributeValue==''){
								continue;
							}
							
							if(data[i].attributeTypeCd=='ATTRIBUTE_TYPE_CD.MULTIPLE'){
								data[i].selects = pmsProductattributes[j].attributeValue.split(",");
							}else {
								data[i].value = pmsProductattributes[j].attributeValue;
							}						
							
						}
					}
				}
			}
			
			$scope.categoryAttributeList = data;
			
		});
	}
	// 팝업 닫기
	this.close = function() {
		$window.close();
	}

	// 단품팝업에서 단품 저장
	$scope.saveSaleProduct = function(saleProducts, productOptions){
		
		// 단품옵션팝업에서 넘어온 객체에 기존 단품 속성 설정 : 단품ID, 재고, 판매상태, 추가판매가
		var setSaleproductInfo = function(data){
			var gridData = $scope.saleProductGrid.getData();
			
			for(var i = 0 ; i < gridData.length ; i++){
				
				var gridName = gridData[i].name;
				
				for(var j = 0 ; j < data.length ; j++){
					
					if(compareSaleproductOptions(gridData[i].pmsSaleproductoptionvalues, data[j].pmsSaleproductoptionvalues) > 0){
						data[j].saleproductId = gridData[i].saleproductId;
						data[j].realStockQty = gridData[i].realStockQty;
						data[j].addSalePrice = gridData[i].addSalePrice;
						data[j].erpSaleproductId = gridData[i].erpSaleproductId;
						data[j].saleproductStateCd = gridData[i].saleproductStateCd;
						data[j].sortNo = gridData[i].sortNo;
						break;
					}
				}
			}
		}
		
		//기존 단품설정값들 유지
		setSaleproductInfo(saleProducts);
		
		$scope.saleProductGrid.setData([]);
		
		for(var i = 0 ; i < saleProducts.length ; i++){
			
			
			var saleproduct = {
				saleproductId : saleProducts[i].saleproductId,
				name : saleProducts[i].name,
				//safeStockQty : saleProducts[i].safeStockQty,
				realStockQty : saleProducts[i].realStockQty,
				addSalePrice : saleProducts[i].addSalePrice,
				erpSaleproductId : saleProducts[i].erpSaleproductId,
				saleproductStateCd : saleProducts[i].saleproductStateCd,
				sortNo : saleProducts[i].sortNo
//				pmsSaleproductoptionvalues : saleProducts[i].pmsSaleproductoptionvalues		
//				pmsSaleproductoptionvalues : [{optionValue : "111", optionName : "222"}]		
			}
			saleproduct.pmsSaleproductoptionvalues = [];
			
			for(var j = 0 ; j < saleProducts[i].pmsSaleproductoptionvalues.length ; j++){
				var opvals = {
						optionValue : saleProducts[i].pmsSaleproductoptionvalues[j].optionValue,
						optionName : saleProducts[i].pmsSaleproductoptionvalues[j].optionName,
						attributeId : saleProducts[i].pmsSaleproductoptionvalues[j].attributeId,
						attributeValue : saleProducts[i].pmsSaleproductoptionvalues[j].attributeValue
				}
				saleproduct.pmsSaleproductoptionvalues.push(opvals);
			}
			
			$scope.saleProductGrid.addRow(saleproduct);
		}
		
		// 상품 옵션( pms_productoption )
		$scope.pmsProduct.pmsProductoptions = angular.copy(productOptions);
		
		//console.log($scope.pmsProduct.pmsProductoptions);
		
		// 단품 변경 플래그
		$scope.pmsProduct.changeSaleproductYn = "Y";
		
	}

	// 단품 속성 비교 ( 1 이면 동일, -1 이면 다른 단품)
	var compareSaleproductOptions = function(target1, target2){
		
		// 사이즈가 다르면 리턴
		if(target1.length != target2.length){
			return -1;
		}
		
		var target1options = [];
		
		for(var i = 0 ; i < target1.length ; i++){
			target1options.push(target1[i].optionName+"_"+target1[i].optionValue);
		}
		
		for(var j = 0 ; j < target2.length ; j++){
			var option = target2[j].optionName+"_"+target2[j].optionValue;
			
			if(target1options.indexOf(option) < 0){
				return -1;
			}
			
		}
		
		return 1;
	}
	
	// 전시카테고리 그리드 행 삭제
	this.deleteDisplayCategory = function(){
		$scope.dmsCategoryGrid.deleteRow();
	}
	
	// 세트 상품 구성 상품 그리드 삭제
	this.deleteSetProduct = function(){
		$scope.setProductGrid.deleteRow();
	}
	
	
//	$scope.$watch('pmsProduct.ageTypeCds', function(value, oldValue){
//		
//		
//		// 월령정보 설정
//		if(!common.isEmpty(value) && value.length > 0){
//			
//			$scope.pmsProduct.pmsProductages = [];
//			
//			var msg = "월령정보는 최대3개 연속된 값만 선택이 가능합니다.";
//
//			var ageTypeCdArr = $scope.pmsProduct.ageTypeCds.split(",");
//			
//			if(ageTypeCdArr.length > 3){
//				alert(msg);
////				$scope.pmsProduct.ageTypeCds=oldValue;
//			}
//			
//			var indexArr = [];
//			
//			var cds = ['AGE_TYPE_CD.3MONTH',
//						'AGE_TYPE_CD.6MONTH',
//						'AGE_TYPE_CD.12MONTH',
//						'AGE_TYPE_CD.24MONTH',
//						'AGE_TYPE_CD.4YEAR',
//						'AGE_TYPE_CD.6YEAR',
//						'AGE_TYPE_CD.OVER7',
//						'AGE_TYPE_CD.ETC'];
//
//			for(var i = 0 ; i < ageTypeCdArr.length ; i++ ){
//				$scope.pmsProduct.pmsProductages.push({ ageTypeCd : ageTypeCdArr[i] });
//				indexArr.push(cds.indexOf(ageTypeCdArr[i]));
//			}
//			
//			// 연속되지 않은 월령정보 체크
//			if(ageTypeCdArr.length >= 2 && ageTypeCdArr.length <= 3){
//				
//				indexArr.sort();
//
//				if(indexArr[0] + 1 != indexArr[1]){
//					alert(msg);
//				}
//				if(indexArr.length ==3 && indexArr[1] + 1 != indexArr[2]){
//					alert(msg)
//				}
//				
//			}
//			
//		}
//	});
	

	//상품 저장
	this.saveProduct = function(){
		
		if(!confirm("저장 하시겠습니까?")){
			return;
		}
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		
		//판매기간 필수 체크
		if(common.isEmpty($scope.pmsProduct.saleStartDt) || common.isEmpty($scope.pmsProduct.saleStartDt)){
			alert("판매기간을 입력해 주세요.");
			return;
		}
		
		//전시카테고리 필수 체크
		var displayCategory = $scope.dmsCategoryGrid.getData();
		if( displayCategory.length == 0 ){
			alert("전시카테고리를 설정해 주세요.");
			return;
		}
		
		//ERP코드 필수 체크
		if(!$scope.isSetProduct && $scope.erpProductYn && common.isEmpty($scope.pmsProduct.erpProductId)){
			alert("ERP 코드를 입력해 주세요");
			return;
		}
		
		// 기프티콘 사용시 선물테마 필수 체크
		if($scope.pmsProduct.giftYn=='Y' && common.isEmpty($scope.pmsProduct.themeCd)){
			alert("선물 테마를 선택해 주세요");
			return;
		}
		
		// 월령정보 설정
		if(!common.isEmpty($scope.pmsProduct.ageTypeCds) && $scope.pmsProduct.ageTypeCds.length > 0){
			
			$scope.pmsProduct.pmsProductages = [];
			
			var msg = "월령정보는 최대3개 연속된 값만 선택이 가능합니다.";

			var ageTypeCdArr = $scope.pmsProduct.ageTypeCds.split(",");
			
			if(ageTypeCdArr.length > 3){
				alert(msg);
				return;
			}
			
			var indexArr = [];
			
			var cds = ['AGE_TYPE_CD.3MONTH',
						'AGE_TYPE_CD.6MONTH',
						'AGE_TYPE_CD.12MONTH',
						'AGE_TYPE_CD.24MONTH',
						'AGE_TYPE_CD.4YEAR',
						'AGE_TYPE_CD.6YEAR',
						'AGE_TYPE_CD.OVER7',
						'AGE_TYPE_CD.ETC'];

			for(var i = 0 ; i < ageTypeCdArr.length ; i++ ){
				$scope.pmsProduct.pmsProductages.push({ ageTypeCd : ageTypeCdArr[i] });
				indexArr.push(cds.indexOf(ageTypeCdArr[i]));
			}
			
			// 연속되지 않은 월령정보 체크
			if(ageTypeCdArr.length >= 2 && ageTypeCdArr.length <= 3){
				
				indexArr.sort();

				if(indexArr[0] + 1 != indexArr[1]){
					alert(msg);
					return;
				}
				if(indexArr.length ==3 && indexArr[1] + 1 != indexArr[2]){
					alert(msg)
					return;
				}
				
			}
			
		}else{
			alert("월령정보를 선택해 주세요");
			return;
		}
//		console.log($scope.pmsProduct.pmsProductages)
		
		//단품 필수 체크
		var pmsSaleproducts = $scope.saleProductGrid.getData();
		if($scope.pmsProduct.optionYn=='Y' && pmsSaleproducts.length == 0){
			alert("단품을 설정해 주세요.");
			return;
		}
		
		// 단품 객체 설정
		$scope.pmsProduct.pmsSaleproducts = $scope.saleProductGrid.getData();
		
		// 전시 카테고리 객체 설정
		$scope.pmsProduct.dmsDisplaycategoryproducts = $scope.dmsCategoryGrid.getData();
        
		
		//세트구성상품  필수 체크
		if($scope.isSetProduct){
			var setProducts = $scope.setProductGrid.getData();
			if( setProducts.length == 0 ){
				alert("구성상품을 설정해 주세요.");
				return;
			}
		}
		
		// 정기배송 체크
		if($scope.erpProductYn && ($scope.pmsProduct.regularDeliveryYn == 'Y' && (common.isEmpty($scope.pmsProduct.regularDeliveryMinCnt) || common.isEmpty($scope.pmsProduct.regularDeliveryMaxCnt)))){
			alert("유효하지 않은 항목이 존재합니다.");
			return;
		}
		
		// 상품 속성 설정
		$scope.pmsProduct.pmsProductattributes = [];
		
		for(var i = 0 ; i < $scope.categoryAttributeList.length ; i++){
			var attribute = $scope.categoryAttributeList[i];
			if(attribute.attributeTypeCd=='ATTRIBUTE_TYPE_CD.MULTIPLE'){
				$scope.pmsProduct.pmsProductattributes.push({
					attributeId : attribute.attributeId,
					attributeValue : attribute.selects.join(",")
				});
			}else if(attribute.attributeTypeCd=='ATTRIBUTE_TYPE_CD.SINGLE'){
				$scope.pmsProduct.pmsProductattributes.push({
					attributeId : attribute.attributeId,
					attributeValue : attribute.value
				});
			}else if(attribute.attributeTypeCd=='ATTRIBUTE_TYPE_CD.INPUT'){
				$scope.pmsProduct.pmsProductattributes.push({
					attributeId : attribute.attributeId,
					attributeValue : attribute.value
				});
			}
		}
		
		if($scope.isNew){
			// 상퓸 유형 : 일반/세트
			$scope.pmsProduct.productTypeCd = $scope.isSetProduct ? "PRODUCT_TYPE_CD.SET" : "PRODUCT_TYPE_CD.GENERAL";
		}
		//console.log($scope.pmsProduct)
		
		// 사입/위탁 상품 여부
		$scope.pmsProduct.erpProductYn = $scope.erpProductYn ? "Y" : "N";
		
		// 고시정보 유효성체크
		var inValid = false;
		$('[name^=notice_detail_]').each(function(){
			
			if('상품상세설명참조' == $.trim($(this).val().replace(/ /g, ''))){
				alert('정확한 상품 정보 제공을 위해 항목에 맞는 실제 정보를 입력해 주세요.');
				$(this).focus();
				inValid = true;
				return false;
			}
			
			if(common.getBytes($.trim($(this).val())) < 2){
				alert("최소 1자(한글기준) 이상 입력하셔야 합니다.");
				$(this).focus();
				inValid = true;
				return false;
			}
			
			//달력 체크
			
//			var regExp = /^(19|20)\d{2}\/(0[1-9]|1[012])$/;
//			if($(this).attr("title")=='제조일자' && !regExp.test($.trim($(this).val()))){
//				alert("날짜 형식이 올바르지 않습니다. (YYYY/MM)");
//				$(this).focus();
//				inValid = true;
//				return false;
//			}
			
			
			
		});
		
		if(inValid){
			return;
		}
		// 저장
		productService.saveProduct($scope.pmsProduct, function(data){
			
        	alert("성공적으로 저장 하였습니다.");
        	
        	pScope.selectedProductId = data.content;
        	
    		$window.location.reload();
        });
	}

	// 탭 전환 버튼 클릭
	this.changeTab = function(){
		if(common.isEmpty($scope.pmsProduct.productId)){
			alert("상품 기본정보 저장 후 이미지를 등록 할 수 있습니다.");
			return;
		}
		
		if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
			$window.location.href="/pms/product/popup/images";
		}
	}
	
	//가격 변경 예약
	this.openReservePrice = function(){
		// 가격정보 예약변경 팝업
		var winName='priceReservePopup';
		var winURL = Rest.context.path +"/pms/product/popup/reservePrice";
		popupwindow(winURL,winName,1000,650);
	}

	
	
	//optionYn 변경
	// optinoYn on-change
//	$scope.$watch('pmsProduct.optionYn', function(value){
//		if(angular.isDefined(value)){
//			if(value=='N'){
//				
//				var p =  [{
//					name : "기본단품",
//					realStockQty : 0,
//					safeStockQty : 0,
//					addSalePrice : 0,
//					saleproductStateCd : 'SALEPRODUCT_STATE_CD.SALE'
//					}];
//				$scope.saleProductGrid.setData(p);
//				
//			}else{
//				$scope.saleProductGrid.setData([]);
//			}
//			
//		}
//	})
	// 옵션여부 클릭
	this.clickOptionYn = function(value){
		if(value=='Y'){
			$scope.pmsProduct.pmsSaleproducts = [];//단품
			$scope.pmsProduct.pmsProductoptions = [];
			$scope.saleProductGrid.setData([], false);//단품
			
		}else{
			var saleProduct = getNoOptionProduct();
			$scope.pmsProduct.pmsSaleproducts = [saleProduct];//단품
			$scope.saleProductGrid.setData([saleProduct], false);//단품
		}
	}

	
	
	//가격이력팝업
	this.openPriceHistory = function(){
		var winName='priceHistoryPopup';
		var winURL = Rest.context.path +"/pms/product/popup/priceHistory";
		popupwindow(winURL,winName,1200,550);
	}
	
	//상품 정보 변경 이력
	this.reserveHistory = function(){
		var winName='상품정보예약변경내역';
		var winURL = Rest.context.path +"/pms/product/popup/infoHistory";
		popupwindow(winURL,winName,1200,550);
	}
	
	//옵션 등록 팝업
	this.openOptionManager = function(){
		if(common.isEmpty($scope.pmsProduct.categoryId)){
			alert("표준 카테고리를 먼저 선택해 주세요.");
			return;
		}
		var winName='옵션 등록';
		var winURL = Rest.context.path +"/pms/product/popup/option";
		popupwindow(winURL,winName,1200,380);
	}

	// erp 상품정보의 고시정보 세팅 : 원산지, 사이즈, 색상, 제조업체, 혼용율
	var setErpNoticeInfo = function(){
		
		if(common.isEmpty($scope.pmsProduct.productNoticeTypeCd)){
			return;
		}
		if(common.isEmptyObject($scope.erpNoticeInfo)){
			return;
		}
		
		for(var i = 0 ; i < $scope.pmsProduct.pmsProductnotices.length ; i++){
//			console.log($scope.pmsProduct.pmsProductnotices[i])
			var key = $scope.pmsProduct.pmsProductnotices[i].pmsProductnoticefield.erpFieldName;
			
			if(!key){
				continue;
			}
			var values = $scope.erpNoticeInfo[key];
			
			if(values){
				$scope.pmsProduct.pmsProductnotices[i].detail = values;
			}
		}
	}
	
	//고시 유형 변경
	$scope.$watch('pmsProduct.productNoticeTypeCd', function(value){
//		var noticeProductId = $scope.isNew ? "empty" : pScope.selectedProductId;
		var noticeProductId = pScope.selectedProductId;
		if( !common.isEmpty(value)){
			
			$scope.pmsProduct.productNoticeTypeCd = value;
			
			productService.getNoticeList({productId : noticeProductId, productNoticeTypeCd : value }, function(data){
				$scope.pmsProduct.pmsProductnotices = data;
				
				// erp고시정보와 매핑
				setErpNoticeInfo();
			});
		}else{
			$scope.pmsProduct.pmsProductnotices = [];
		}
	});

	//업체 변경 watch
	$scope.$watch("pmsProduct.ccsBusiness", function(ccsBusiness){
		
		if(common.isEmptyObject($scope.pmsProduct.ccsBusiness)){
			return;
		}
		
		// 배송정책 목록 조회
		$scope.selectDeliverypolicyByBusinessId(ccsBusiness.businessId, ccsBusiness.saleTypeCd);
		
	}, true);

	
	// 옵션 없는 단품 객체 생성
	var getNoOptionProduct = function(){
		var saleProduct = {
				name : $scope.pmsProduct.name,
				saleproductStateCd : "SALEPRODUCT_STATE_CD.SALE",
				realStockQty : 999,
				addSalePrice : 0,
				pmsSaleproductoptionvalues : []
			}
		return saleProduct;
	}
	
	
	// PO 신규 일경우 업체 정보 조회
	var poBusinessId = global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	if(!common.isEmpty(poBusinessId) && $scope.isNew){
		var param = {businessId : poBusinessId};
		businessService.getBusiness(param, function(data){
			$scope.callbackBusinessSearch([data]);
		});
	}
	
	// erp 연동 상퓸 여부 : 업체 매입유형으로 판단
	var setErpProductYn = function(){
		$scope.erpProductYn = $scope.pmsProduct.ccsBusiness.purchaseYn=='N' && $scope.pmsProduct.ccsBusiness.saleTypeCd == 'SALE_TYPE_CD.CONSIGN' ? false : true;
	}
	
	//상품 정보 조회
	$scope.getProductDetail = function(productId){
		productService.getProductDetail(productId, function(data){
			
			$scope.pmsProduct = data;
			
			// 세트여부
			$scope.isSetProduct = $scope.pmsProduct.productTypeCd == "PRODUCT_TYPE_CD.SET" ? true : false;
			
			// erp상품 여부
			setErpProductYn();
			
			if($scope.isSetProduct){
				setGridInit();
			}
			
			// 단품 세팅
			if(!$scope.isCopyProduct && !common.isEmptyObject($scope.pmsProduct.pmsSaleproducts) && $scope.pmsProduct.pmsSaleproducts.length > 0){
				$scope.saleProductGrid.setData($scope.pmsProduct.pmsSaleproducts, false);//단품
			}
			
			// 전시 카테고리 세팅
			$scope.dmsCategoryGrid.setData($scope.pmsProduct.dmsDisplaycategoryproducts, false);//전시카테고리
			
			// 상품 속성 목록 조회
			if(!common.isEmpty(data.categoryId)){
				$scope.getCategoryAttributeList(data.categoryId, "ATTR");
			}
			
			//상품 복사 기능일 경우
			if($scope.isCopyProduct){
				$scope.pmsProduct.productId = null;
				$scope.pmsProduct.name = "Copy_"+$scope.pmsProduct.name;// 복사 상품명 
				$scope.pmsProduct.approvalYn = "N";//승인이전 상태
				$scope.pmsProduct.saleStateCd = "SALE_STATE_CD.REQ";//신규요청 상태
				$scope.pmsProduct.optionYn = "Y"; // 옵션여부 Y
				$scope.pmsProduct.erpProductId = "";// erp 상품번호 초기화
				$scope.pmsProduct.rejectReason = "";
				$scope.pmsProduct.pmsProductoptions = [];
				
				$scope.clearErpProductInfo();
				
				// 업체,카테고리별 수수료율 목록 조회
				getCommissionList();
				
			}
			
			
			// 수수료율 조회
			if($scope.erpProductYn == false){
				getCommissionList();
			}

			// 월령정보
			var ages = $scope.pmsProduct.pmsProductages;
			
			for(var i = 0 ; i < ages.length ; i++){
				if(common.isEmpty($scope.pmsProduct.ageTypeCds)){
					$scope.pmsProduct.ageTypeCds = ages[i].ageTypeCd;
				}else{
					$scope.pmsProduct.ageTypeCds += "," + ages[i].ageTypeCd;
				}
			}
		});
	}
	
	// 수수료율 계산
	$scope.changePrice = function(targetObject){
		
		if($scope.erpProductYn){
			// ERP상품일 경우
			
			if(targetObject.salePrice > 0 && targetObject.supplyPrice > 0){
				var commission = (targetObject.salePrice-targetObject.supplyPrice) / targetObject.salePrice * 100;
				targetObject.commissionRate= $filter('number')(commission, 1);
			}else{
				targetObject.commissionRate = 0;
			}
			
		} else{
			// erp상품 아닐경우 수수료율 계산
			if(!common.isEmpty(targetObject.salePrice) && !common.isEmpty(targetObject.commissionRate)){
				
				var supplyPrice = targetObject.salePrice - (targetObject.commissionRate * targetObject.salePrice ) / 100;
//				$scope.pmsProduct.supplyPrice= $filter('number')(supplyPrice, 1);
				targetObject.supplyPrice= supplyPrice;
			}else{
				targetObject.supplyPrice = 0;
			}
			// 업체 수수료는 정상가를 판매가로 세팅
			if(!common.isEmpty(targetObject.salePrice)) {
				targetObject.listPrice = targetObject.salePrice;
			}
						
		}
	}
	
	// 정기배송가 'Y' 선택시 빈값 초기화
	$scope.changeDeliveryFeeFree = function(targetObject){
		if($scope.pmsProduct.regularDeliveryYn == 'Y'){
			$scope.pmsProduct.regularDeliveryPrice = '';
		}
	}
		
	// 상품 상태 변경 : type [ 1.품절, 2.일시정지, 3.판매중, 4.MD중지 ]
	$scope.saveStatus = function(type){
		var next = "";
		var statusCd = "SALE_STATE_CD.";
		if(type==1){
			next = "품절";
		    statusCd = "SALE_STATE_CD.SOLDOUT";
		}else if(type==2){
			next = "일시정지";
			statusCd = "SALE_STATE_CD.STOP";
		}else if(type==3){
			next = "판매중";
			statusCd = "SALE_STATE_CD.SALE";
		}else if(type==4){
			next = "MD정지";
			statusCd = "SALE_STATE_CD.MDSTOP";
		}else if(type==5){
			next = "영구종료";
			statusCd = "SALE_STATE_CD.END";
		}else if(type==6){
			next = "승인전";
			statusCd = "SALE_STATE_CD.REQ";
		}
		
		if(!confirm(next+" 상태로 변경 하시겠습니까?")){
			return;
		}
		
		var param = { saleStateCd : statusCd, productId : $scope.pmsProduct.productId }
		productService.savePmsProduct(param, function(){
			alert("변경 되었습니다.");
			$window.location.reload();
		});
		
		
	}
}).filter('saleproductStateFilter', function() {// SALEPRODUCT_STATE_CD 
	
	var comboHash = {
		    'SALEPRODUCT_STATE_CD.SALE': '판매중',
		    'SALEPRODUCT_STATE_CD.SOLDOUT': '품절',
		    'SALEPRODUCT_STATE_CD.STOP': '판매중지'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
});


// 상품 상세 : 이미지 탭
productDetailApp.controller("productImagesController", function($window, $scope, productService, commonService) {
	
	var pScope = $window.opener.$scope;//부모창의 scope
	
	$scope.pmsProduct = {productId : pScope.selectedProductId };
	$scope.pmsProduct.pmsProductimgs = [];
	$scope.defaultImage = {};
	
	
	// zip 이미지 업로드 콜백
	$scope.bulkImageUploadCallback = function(response){
		//console.log(response);
		
		$scope.defaultImage = {};
		$scope.pmsProduct.pmsProductimgs = [];
		$scope.pmsProduct.offshopImg = {};
		$scope.pmsProduct.bulkImageUpload = 'Y';
		
		if(response==null || response.length == 0){
			alert("업로드된 이미지가 없습니다.");
			return;
		}
		for(i = 0 ; i < response.length ; i++){
			
			//console.log(response[i]);
			
			var tempPath = response[i].tempFullPath;
			var fileName = response[i].fileName;
			
			//console.log(fileName);
			
			
			if(fileName.indexOf("_0")>=0){
				$scope.uploadDefaultImageCallback(tempPath);
			}else if(fileName.indexOf("_offshop")>=0){
				$scope.uploadOffshopImageCallback(tempPath);
			}else{
				$scope.uploadImageCallback(tempPath, "new");
			}
		}
	}
	// 상품 이미지 정보 조회
	if(!common.isEmpty(pScope.selectedProductId)){
		
		productService.getProductImages(pScope.selectedProductId, function(data){
			var images = data.pmsProductimgs;
			
			$scope.pmsProduct.offshopImg = data.offshopImg;
			
			if( images.length> 0){
				$scope.pmsProduct.pmsProductimgs = [];
			}
			else{
				return;
			}
			
			var d = new Date();
			
			var productId = pScope.selectedProductId;
			for(var i = 0 ; i < images.length ; i++){
				if(images[i].imgNo==0){
					$scope.defaultImage = images[i];
					$scope.defaultImage.img = "/image/pms/product/"+productId+"/"+productId+"_0_180.jpg?d="+d.getTime();
				}else{
					images[i].img = "/image/pms/product/"+productId+"/"+productId+"_"+images[i].imgNo+"_180.jpg?d="+d.getTime();
					$scope.pmsProduct.pmsProductimgs.push(images[i]);
					
				}
			}
			
//			$scope.pmsProduct.pmsProductimgs = images;
		});
	}
	// 저장
	this.save = function(tabChange){
		if(!confirm("저장 하시겠습니까?")){
			return;
		}
		if(common.isEmpty($scope.defaultImage.imgNo)){
			alert("대표이미지를 업로드해 주세요.");
			return;
		}
		
		$scope.pmsProduct.pmsProductimgs.push($scope.defaultImage);//대표이미지
		
		productService.saveProductImages($scope.pmsProduct, function(data){
			
        	if(tabChange){
        		$window.location.href="/pms/product/popup/detail";
        	}else{
        		$window.location.reload();
        	}
        	
        });
		
	}
	
	this.close = function() {
		$window.close();
	}
	
	// 이미지 업로드 콜백
	$scope.uploadImageCallback = function(filePath, index){

		if(index!='new'){
			$scope.pmsProduct.pmsProductimgs[index].img = filePath;
			$scope.pmsProduct.pmsProductimgs[index].crudType = "C";
		}else{
			$scope.pmsProduct.pmsProductimgs.push({img:filePath, crudType:'C',imgNo:''});
		}
		
	}
	
	// 대표이미지 업로드 콜백
	$scope.uploadDefaultImageCallback = function(filePath){
		
		var newImage = {
				img : filePath,
				imgNo : 0,// 대표이미지는 0
				crudType : "C",
				productId : pScope.selectedProductId
		};
		
		$scope.defaultImage = angular.copy(newImage);
		
		
	}
	
	// 매장컷미지 업로드 콜백
	$scope.uploadOffshopImageCallback = function(filePath, volume, size){
		$scope.pmsProduct.offshopImg = filePath;
	}
	
	// 탭전환
	this.changeTab = function(){
		if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
			$window.location.href="/pms/product/popup/detail";
		}
	}
	this.getDetailImage = function(){
		
	}
	// 이미지 삭제
	this.deleteImage = function(index){

		var target = angular.copy($scope.pmsProduct.pmsProductimgs[index]);
		
		if(common.isEmpty(target)){
			return;
		}
		
		
		//신규 템프 파일은 바이너리 바로 삭제
		if(target.crudType=='C'){
			commonService.deleteFile(target.img, function(data){
				//모델에서 삭제
				$scope.pmsProduct.pmsProductimgs.splice(index, 1);
	        });
			
		}else{
			$scope.pmsProduct.pmsProductimgs[index].crudType = "D";
			$scope.pmsProduct.pmsProductimgs[index].deleteImg = angular.copy($scope.pmsProduct.pmsProductimgs[index].img);
			$scope.pmsProduct.pmsProductimgs[index].img = '';
		}
		alert('일반 이미지를 삭제했습니다.');
	}
	
	//매장컷 이미지 삭제
	this.deleteOffshopImage = function(){
		$scope.pmsProduct.offshopImg = ''
		alert('매장컷 이미지를 삭제했습니다.');
	}
	
	//대표 이미지 삭제
//	this.deleteDefaultImage = function(){
//		
//		var target = angular.copy($scope.pmsProduct.pmsProductimgs[0]);
//		
//		if(common.isEmpty(target)){
//			return;
//		}
//		
//		//신규 템프 파일은 바이너리 바로 삭제
//		if(target.crudType=='C'){
//			commonService.deleteFile(target.img, function(data){
//				//모델에서 삭제
//				$scope.pmsProduct.pmsProductimgs.splice(0, 1);
//				$scope.defaultImage.img = '';
//	        });
//			
//		}else{
//			$scope.pmsProduct.pmsProductimgs[0].crudType = "D";
//			$scope.defaultImage.img = '';
//		}
//		alert('대표 이미지를 삭제했습니다.');
//	}
});

//상품상세 가격변경예약 팝업
productDetailApp.controller("pmsProductReservepriceController", function($window, $scope, $filter, productService, commonService, gridService) {
	
	var pScope = $window.opener.$scope;//부모창의 scope
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {productId : pScope.pmsProduct.productId};
	$scope.productId = pScope.pmsProduct.productId;
	$scope.erpProductYn = pScope.erpProductYn;
	
	var poBusinessId = global.session.businessId;
	var regularYn = pScope.pmsProduct.regularDeliveryYn;
	//그리드에 추가
	this.addRow = function(){
		$scope.priceGrid.addRow({
			productId : pScope.pmsProduct.productId,
			listPrice : pScope.pmsProduct.listPrice,
			supplyPrice : pScope.pmsProduct.supplyPrice,
			salePrice : pScope.pmsProduct.salePrice,
			commissionRate : pScope.pmsProduct.commissionRate,
			pointSaveRate : pScope.pmsProduct.pointSaveRate,
			regularDeliveryPrice : pScope.pmsProduct.regularDeliveryPrice,
			priceReserveStateName : "변경요청"
			/*deliveryFeeFreeYn : pScope.pmsProduct.deliveryFeeFreeYn*/
		});
	}

	//가격 예약 저장
	this.saveGrid = function(){
		$scope.priceGrid.saveGridData(null);
	}
	
	this.close = function(){
		$window.close();
	}
	
	
	var btnStr = "";
	var cellTemplate = "<div class=\"ui-grid-cell-contents\" title=\"\"><button class=\"btn_type2\" ng-click=\"grid.appScope.openSaleproductPricePopup(row.entity)\"><b>"
	+"{{row.entity.salePriceReserveYn=='Y' ? '단품예약변경' : '단품예약'}}</b></button></div>";
	
	
//	var cellTemplate = "<button ng-click=\"grid.appScope.openSaleproductPricePopup(row.entity)\"><b>단품예약</b></button>";
	var columnDefs = [];
	
	var today = new Date();
	var dateClass = '';
	dateClass += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"';
	dateClass += '	row-idx="{{grid.options.getRowIdentity(row.entity)}}" ng-model="row.entity.reserveDt"';
	dateClass += '	datetime-picker grid min-date="'+$filter('date')(new Date(), Constants.date_format_2)+'">';
	dateClass += '</div>';
	
	if($scope.erpProductYn){// erp 상품인 경우
		
		var salePriceTemplate =   "<div><form name=\"inputForm\"><input type=\"INPUT_TYPE\" ng-change=\"grid.appScope.changePrice(row.entity)\" ng-class=\"'colt' + col.uid\" ui-grid-editor ng-model=\"MODEL_COL_FIELD\"></form></div>";
		
		columnDefs = [];
		columnDefs.push({ field: "reserveDt", 		    cellTemplate :dateClass, displayName : "예약일시", width :'150'});
		columnDefs.push({ field: 'listPrice',			colKey:"pmsPricereserve.listPrice", vKey : "pmsPricereserve.listPrice", enableCellEdit: true});
		columnDefs.push({ field: 'supplyPrice',		colKey:"pmsPricereserve.supplyPrice", vKey : "pmsPricereserve.supplyPrice", enableCellEdit: true ,editableCellTemplate : salePriceTemplate});
		columnDefs.push({ field: 'salePrice',			colKey:"pmsPricereserve.salePrice", vKey : "pmsPricereserve.salePrice", enableCellEdit: true, editableCellTemplate : salePriceTemplate});
		columnDefs.push({ field: 'commissionRate',     colKey:"pmsPricereserve.commissionRate",  enableCellEdit: false });
		
		if(!poBusinessId){// 업체는 포인트, 정기배송 입력 불가
			columnDefs.push({ field: 'pointSaveRate',		colKey:"pmsPricereserve.pointSaveRate", vKey : "pmsPricereserve.pointSaveRate", enableCellEdit: true});
			if(regularYn){
				columnDefs.push({ field: 'regularDeliveryPrice',	colKey:"pmsPricereserve.regularDeliveryPrice", vKey : "pmsPricereserve.regularDeliveryPrice", enableCellEdit: true});
			}
		}
		
		columnDefs.push({ field: 'priceReserveStateName',	colKey:"pmsPricereserve.priceReserveStateCd" });
		columnDefs.push({ field: "saleReserveBtn", 		cellTemplate :cellTemplate, displayName : "단품예약", width :'130'});
	}else{ //erp 미연동 상품
		
		$scope.commissionList = [];
		
		for(var i = 0 ; i < pScope.commissionList.length ; i++){
			var rateOption = {id : pScope.commissionList[i].commissionRate };
			$scope.commissionList.push(rateOption);
		}
		
		var commissionTemplate = "<div><form name=\"inputForm\">" +
				"<select ng-change=\"grid.appScope.changePrice(row.entity)\" ng-class=\"'colt' + col.uid\" ui-grid-edit-dropdown ng-model=\"MODEL_COL_FIELD\" ng-options=\"field[editDropdownIdLabel] as field[editDropdownValueLabel] CUSTOM_FILTERS for field in editDropdownOptionsArray\">" +
				"</select></form></div>";
		
		var salePriceTemplate =   "<div><form name=\"inputForm\"><input type=\"INPUT_TYPE\" ng-change=\"grid.appScope.changePrice(row.entity)\" ng-class=\"'colt' + col.uid\" ui-grid-editor ng-model=\"MODEL_COL_FIELD\"></form></div>";
		
		columnDefs = [];
		columnDefs.push( { field: "reserveDt", 		    cellTemplate :dateClass, displayName : "예약일시", width :'150'});
		columnDefs.push({ field: 'listPrice',			colKey:"pmsPricereserve.listPrice", vKey : "pmsPricereserve.listPrice", enableCellEdit: true});
		columnDefs.push({ field: 'supplyPrice',		colKey:"pmsPricereserve.supplyPrice", vKey : "pmsPricereserve.supplyPrice", enableCellEdit: false});
		columnDefs.push({ field: 'salePrice',			colKey:"pmsPricereserve.salePrice", vKey : "pmsPricereserve.salePrice", enableCellEdit: true, editableCellTemplate : salePriceTemplate});
		columnDefs.push({ field: 'commissionRate', colKey:"pmsPricereserve.commissionRate", editableCellTemplate: commissionTemplate, enableCellEdit: true, editDropdownValueLabel: 'id', editDropdownOptionsArray: $scope.commissionList });
  		if(!poBusinessId){// 업체는 포인트, 정기배송 입력 불가
			columnDefs.push({ field: 'pointSaveRate',		colKey:"pmsPricereserve.pointSaveRate", vKey : "pmsPricereserve.pointSaveRate", enableCellEdit: true});
			if(regularYn){
				columnDefs.push({ field: 'regularDeliveryPrice',	colKey:"pmsPricereserve.regularDeliveryPrice", vKey : "pmsPricereserve.regularDeliveryPrice", enableCellEdit: true});
			}
		}		
	     columnDefs.push({ field: 'priceReserveStateName',	colKey:"pmsPricereserve.priceReserveStateCd" });
	     columnDefs.push({ field: "saleReserveBtn", 		cellTemplate :cellTemplate, displayName : "단품예약", width :'130'});
	}
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "myGrid",	//mandatory
			url :  '/api/pms/product/price/reserve',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : { pagination : false },
			callbackFn : function(){	//optional
				$scope.priceGrid.loadGridData();
			}
	};
	
	// 수수료율 계산
	$scope.changePrice = function(rowEntity){
		pScope.changePrice(rowEntity);
	}
	
	//그리드 초기화
	$scope.priceGrid = new gridService.NgGrid(gridParam);
	
	this.deleteGrid = function(){
		$scope.priceGrid.deleteGridData();
	}
	
	// 단품 가격 예약 팝업
	$scope.openSaleproductPricePopup = function(entity){
		
		
		
//		$scope.priceReserveNo = entity.priceReserveNo;
//		console.log(rowEdit)
//		rowEdit.setRowsDirty([param.scope[param.gridName].data[index]]);
		
		$scope.selectedReserveObj = entity;
//		$scope.priceGrid.setDirtyRows([entity]);
		var winName='단품 가격 예약 설정';
		var winURL = Rest.context.path +"/pms/product/popup/reserveSalePrice";
		popupwindow(winURL, winName, 1000, 350);
	}
	
	$scope.changeSalePriceReserve = function(list){
		$scope.selectedReserveObj.pmsSaleproductpricereserves = list;
		$scope.selectedReserveObj.salePriceReserveYn = 'Y';
		
		// set dirty
		$scope.myGrid.gridApi.rowEdit.setRowsDirty([$scope.selectedReserveObj]);
		
		common.safeApply($scope);
	}
	
});

// 단품 가격 예약 팝업
productDetailApp.controller("pmsSaleProductReservepriceController", function($window, $scope, productService, commonService, gridService){//단품 가격 변경 히스토리
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	
	
	var param = {productId : pScope.productId};
	
	// 부모화면에 단품 가격 목록이 없으면 db로부터 단품목록 조회 ( 신규 가격 등록에만 해당 )
	if(angular.isUndefined(pScope.selectedReserveObj.pmsSaleproductpricereserves )){
		productService.getSaleproductList(pScope.productId, function(data){
			$scope.reserveList = [];
			
			for(var i = 0 ; i < data.length ; i++){
				var pmsSaleproductpricereserve = {
					pmsSaleproduct : data[i],
					saleproductId : data[i].saleproductId
				}
				$scope.reserveList.push(pmsSaleproductpricereserve);
				
			}
		})
	}else{
		$scope.reserveList = pScope.selectedReserveObj.pmsSaleproductpricereserves;
	}

	
	this.save = function(){
		for(var i = 0 ; i < $scope.reserveList.length ; i++){
			if(common.isEmpty($scope.reserveList[i].addSalePrice)){
				alert("입력하지 않은 추가금액 항목이 있습니다.");
				return;
			}
		}
		
		pScope.changeSalePriceReserve($scope.reserveList);
		
		$window.close();
	}
	
	this.close = function(){
		$window.close();
	}
	
	
});

//옵션 등록 팝업
productDetailApp.controller("pmsProductOptionController", function($window, $scope, productService, commonService, gridService, attributeService ) {
	
	var pScope = $window.opener.$scope;//부모창의 scope
	
//	$scope.search = {productId : pScope.productId};
	
	// 단품 목록 세팅
	var saleProducts = pScope.grid2.data;
	
	$scope.optionGroups = [];// 단품팝의 메인 객체
	
	// pms_productoption 을 optionGroups에 담기
	//console.log("productOptions : ", pScope.pmsProduct.pmsProductoptions);
	
	// 옵션명 목록을 객체에 저장
	if(pScope.pmsProduct.pmsProductoptions && pScope.pmsProduct.pmsProductoptions.length > 0){
		for(var i = 0 ; i < pScope.pmsProduct.pmsProductoptions.length ; i++){
			$scope.optionGroups.push({optionName : pScope.pmsProduct.pmsProductoptions[i].optionName, optionValues : []})
		}
	}
	
	
	// erp 상품 여부
	$scope.isErpProduct = !common.isEmpty(pScope.pmsProduct.erpProductId) ? true : false;
	// 승인 이후 상태 여부
	$scope.isApproval = pScope.pmsProduct.approvalYn == 'Y' ? true : false;
	
	
	// 표준카테고리 속성 콤보 변경 : 속성값 콤보를 재구성한다.
	$scope.selectAttribute = function(group){
		
		if(!group.attributeId){
			group.pmsAttributevalues = [];
		}else{
			for(var i = 0 ; i < $scope.attributeList.length ; i++){
				if($scope.attributeList[i].attributeId == group.attributeId){
					group.pmsAttributevalues = $scope.attributeList[i].pmsAttributevalues;
				}
			}
		}
	}
	
	
	//옵션그룹 추가 버튼 클릭
	this.addOptionGroup = function(){
		$scope.optionGroups.push({
			groupName : '',
			optionValues : [{
				optionName : ''
			}]
		});
	}
	
	//옵션 삭제 버튼 클릭
	this.deleteOptionGroup = function(index){
		
		$scope.optionGroups.splice(index, 1);
	}
	
	
	//옵션값 추가 버튼 클릭
	this.addOptionValue = function(index){
		$scope.optionGroups[index].optionValues.push({
			optionName : ''
		});
	}
	
	// 옵션값 삭제 버튼 클릭
	this.deleteOptionValue = function(index, index2){
		$scope.optionGroups[index].optionValues.splice(index2, 1);
	}
	
	
	
	
	//설정값 저장 : 단품 객체 구조로 변환
	this.save = function(){
		
		var values = [];
		var saleProducts = [];// 단품목록
		var productOptions = [];//상품옵션목록
		var validationChk = false;
		
		//pms_productoption 객체 설정
		for( var i = 0 ; i < $scope.optionGroups.length ; i ++){
			productOptions.push({optionName : $scope.optionGroups[i].optionName, sortNo : i})
		}

		// 단품 객체를 만들기 위해 재귀 함수 호출
		makeSaleproductObject($scope.optionGroups, 0, values);
		
		function makeSaleproductObject( optionGroups , index, values) {
		    var optionGroup = optionGroups[index];
		    
		    for( var i = 0 ; i < optionGroup.optionValues.length ; i ++){
		    	optionGroup.optionValues[i].optionName = optionGroup.optionName;
		    	if(common.isEmpty(optionGroup.optionName)){
		    		alert('유효하지 않은 항목이 존재합니다.');
		    		validationChk = true;
		    		return;
		    	}
		    	optionGroup.optionValues[i].attributeId = optionGroup.attributeId;
		    	if(common.isEmpty(optionGroup.optionValues[i].optionValue)){
		    		alert('입력하지 않은 옵션항목이 존재합니다.');
		    		validationChk = true;
		    		return;
		    	}
		        values[index] = optionGroup.optionValues[i];
		        
		        if (index < optionGroups.length - 1) {
		        	makeSaleproductObject(optionGroups, index+1, values);
		        } else {
		        	
		        	var sname = "";

		            var saleproductoptionvalues = [{
		            	optionName :  values[0].optionName,
		            	optionValue :  values[0].optionValue,
		            	attributeId : values[0].attributeId,
	            		attributeValue : values[0].attributeValue
		            }];
		            //단품명
		            sname+=values[0].optionValue;
		            for (var j = 1; j < values.length; ++j) {
		            	console.log(values[j].optionValue);
		            	saleproductoptionvalues.push({
		            		optionName :  values[j].optionName,
		            		optionValue :  values[j].optionValue,
		            		attributeId : values[j].attributeId,
		            		attributeValue : values[j].attributeValue
		            	});
		            	//단품명
		            	//sname+=", "+values[j].optionName +":"+values[j].optionValue;
		            	sname+=","+values[j].optionValue;
		            }

		        	var saleproduct = {
			        		saleproductId : "",
			        		name : sname,
			        		safeStockQty : 0,
			        		realStockQty : 999,
			        		addSalePrice : 0,
			        		erpSaleproductId : '',
			        		saleproductStateCd : "SALEPRODUCT_STATE_CD.SALE",
			        		pmsSaleproductoptionvalues : saleproductoptionvalues
			        			
			        	};
		        	
		            saleProducts.push(saleproduct);
		        }
		    }
		}

		if(!validationChk){
			$window.opener.$scope.saveSaleProduct(saleProducts, productOptions);
			
			$window.close();	
		}
	}
	
	this.close = function(){
		$window.close();
	}
	
	
	
	// 표준카테고리별 속성 목록 조회
	var param = {categoryId : pScope.pmsProduct.categoryId, attributeTypeCd : "SALEPRODUCT"};
	attributeService.getCategoryAttributeList(param).then(function(response) {
		
		$scope.attributeList = response;
		
	}).then(function(data){
		
		// 부모창의 단품 객체를 팝업에 사용할 객체로 변환
		for(var i = 0 ; i < saleProducts.length ; i++){//grid 객체 loop
			
			var options = saleProducts[i].pmsSaleproductoptionvalues;
			if(options && Array.isArray(options)){
				for(var j = 0 ; j < options.length ; j++){
					pushGroups(options[j]);
				}
			}
		}
		//options[j].optionName, options[j].optionValue
		//옵션 array 에 옵션값을 추가
		function pushGroups(optionvalueObj){
			var existOption = false;
			
			for(var i = 0 ; i < $scope.optionGroups.length ; i++){
				if($scope.optionGroups[i].optionName != optionvalueObj.optionName){
					
				}else{
					existOption = true;// 선택된 옵션이 존재함
					
					$scope.optionGroups[i].attributeId  = optionvalueObj.attributeId;
					$scope.selectAttribute($scope.optionGroups[i]);
					
					var existValue = false;
					for(var j = 0 ; j < $scope.optionGroups[i].optionValues.length ; j++){// 단품을 루프돌리면서 기존 옵션값이 있으면existValue를 true
						if($scope.optionGroups[i].optionValues[j].optionValue == optionvalueObj.optionValue){
							existValue = true;
						}
					}
					if(!existValue){
						$scope.optionGroups[i].optionValues.push(
								{	
									optionValue : optionvalueObj.optionValue, 
									attributeValue : optionvalueObj.attributeValue
								}
						);
					}
				}
			}
			
			if(!existOption){
				
				var groups = {
						optionName : optionvalueObj.optionName,
						attributeId : optionvalueObj.attributeId,
						optionValues : [{
											optionValue : optionvalueObj.optionValue,
											attributeValue : optionvalueObj.attributeValue
										}]
						};
				$scope.selectAttribute(groups);
				$scope.optionGroups.push(groups);
			}
		}
		
		
		// 옵션정보 조회 및 초기화
		if($scope.optionGroups.length == 0){
			$scope.optionGroups = [{  optionValues : [{ }] }];
		}
		
//		common.safeApply($scope);
		
		//console.log($scope.optionGroups)
	});
	
	
	
}).controller("pmsPriceHistorycontroller", function($window, $scope, priceService, commonService, gridService, commonPopupService){// 가격 변경 히스토리 팝업
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	$scope.search.productId = pScope.pmsProduct.productId;
	$scope.productId = pScope.pmsProduct.productId;
	
	var columnDefs =  [
	                   { field: 'priceReserveNo', 						colKey: "pmsPricereserve.priceReserveNo"},
	                   { field: 'listPrice', 							colKey: "pmsPricereserve.listPrice"},
	                   { field: 'supplyPrice',							colKey: "pmsPricereserve.supplyPrice"},
	                   { field: 'salePrice', 							colKey: "pmsPricereserve.salePrice"},
	                   { field: 'pointSaveRate',						colKey: "pmsPricereserve.pointSaveRate"},
	                   { field: 'regularDeliveryPrice', 				colKey: "pmsPricereserve.regularDeliveryPrice"},
	                   { field: 'completeDt', 							colKey: "pmsPricereserve.completeDt",			cellFilter: "date:\'yyyy-MM-dd\'"},
	                   { field: "itemHistCd", 							colKey: "c.pmsProductreserve.itemPriceHistory", linkFunction : 'linkFunction'},
	                   { field: 'insId', 								userFilter :'insId,insName',	colKey: "c.grid.column.insId"},
	                   { field: 'insDt', 								colKey: "c.grid.column.insDt"}
	       	]
	       	
	       	var gridParam = {
	       		scope : $scope,
	       		gridName : "grid_price",
	       		url : '/api/pms/product/productPriceHistory',
	       		searchKey : "search",
	       		columnDefs : columnDefs,
	       		gridOptions : {  checkBoxEnable : false
	       		},
	       		callbackFn : function() {
	       		//myGrid.loadGridData();
	       		}
	       	};
	
			$scope.myGrid = new gridService.NgGrid(gridParam);	
			
			$scope.searchGrid = function(){
				$scope.myGrid.loadGridData();
			}
			
			$scope.popup = function(url) {
				
				popupwindow(url,"ItemPriceHistoryPopup", 1100, 500);
			}
			
			
			$scope.linkFunction = function(field, row) {
				//$scope.serch = {productId:row.entity.productId,priceReserveNo:row.priceReserveNo}
				$scope.priceReserveNo = row.priceReserveNo;
				$scope.popup('/pms/product/popup/itemPriceList'); 
			}
			
			
			this.gridInit = function(){
				priceService.getProductPriceHistoryList($scope.search, function(response) {
					$scope.myGrid.loadGridData();
				});
			}
			
}).controller("pmsItemPriceHistorycontroller", function($window, $scope, priceService, commonService, gridService, commonPopupService){//단품 가격 변경 히스토리
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	
	$scope.search = {};
	//console.log(pScope.productId);
	//console.log(pScope.priceReserveNo);
	//console.log(pScope);
	
	$scope.search.productId = pScope.productId;
	$scope.search.priceReserveNo = pScope.priceReserveNo;
	
	var columnDefs =  [
	                   { field: 'pmsSaleproduct.name', 					colKey: "pmsSaleproduct.name"},
	                   { field: 'pmsSaleproduct.addSalePrice',			colKey: "c.pmsSaleproduct.addSalePrice.now"},
	                   { field: 'addSalePrice', 						colKey: "c.pmsSaleproduct.addSalePrice.change"},
	                   { field: 'insId', 								userFilter :'insId,insName',	colKey: "c.grid.column.insId"},
	                   { field: 'insDt', 								colKey: "c.grid.column.insDt", }
	       	]
	       	
	       	var gridParam = {
	       		scope : $scope,
	       		gridName : "grid_item",
	       		url : '/api/pms/product/itemPriceHistory',
	       		searchKey : "search",
	       		columnDefs : columnDefs,
	       		gridOptions : { checkBoxEnable : false
	       		},
	       		callbackFn : function() {
	       		//myGrid.loadGridData();
	       			$scope.myGrid.loadGridData();
	       		}
	       	};
	
			$scope.myGrid = new gridService.NgGrid(gridParam);	
			
//			$scope.searchGrid = function(){
//				$scope.myGrid.loadGridData();
//			}
			
//			this.gridInit = function(){
//				priceService.getItemPriceHistoryList($scope.search, function(response) {
//					$scope.myGrid.loadGridData();
//				});
//			}
}).controller("pmsProductHistoryController", function($window, $scope, priceService, commonService, gridService, commonPopupService){// 상품정보 변경 히스토리 팝업
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	$scope.search.productId = pScope.pmsProduct.productId;
	$scope.productId = pScope.pmsProduct.productId;
	
	var columnDefs =  [
       { field: 'adCopy', 								colKey: "pmsProductreserve.adCopy"},
       { field: 'saleStateName', 						colKey: "pmsProductreserve.saleStateCd"},
       { field: 'productReserveStateName', 				colKey: "pmsProductreserve.productReserveStateCd"},
       { field: 'reserveDt',							colKey: "pmsProductreserve.reserveDt"},
       { field: 'insId', 								userFilter :'insId,insName',	colKey: "c.grid.column.insId"},
       { field: 'insDt', 								colKey: "c.grid.column.insDt"}
   	]
	       	
   	var gridParam = {
   		scope : $scope,
   		gridName : "grid1",
   		url : '/api/pms/product/reserve',
   		searchKey : "search",
   		columnDefs : columnDefs,
   		gridOptions : {  
   			checkBoxEnable : false
   		},
   		callbackFn : function() {
   			$scope.myGrid.loadGridData();
   		}
   	};

	$scope.myGrid = new gridService.NgGrid(gridParam);	
			
})
