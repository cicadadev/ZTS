var ccsAppPopup = angular.module('ccsAppPopup', ['ui.date','commonServiceModule','gridServiceModule'
                                                 ,'commonPopupServiceModule','ccsServiceModule'	//TODO 삭제 공통 POPUP TEST용
                                                 ,'pmsServiceModule','dmsServiceModule', 'mmsServiceModule', 'angularUtils.directives.dirPagination'
                                                 ]);

ccsAppPopup.controller('ccs_popupListApp_controller', function($window, $scope, commonPopupService) {
	
	/**
	 * 
	 * TODO 삭제
	 * 공통 POPUP TEST용
	 *  
	 */
	
	$scope.search = {};
	
	this.businessPopup = function(){
		commonPopupService.businessPopup($scope,'callback_business',false);
	}
	$scope.callback_business = function(data){
		$scope.search.businessId = data[0].businessId;
		$scope.search.businessName = data[0].name;
		$scope.$apply();		
	}
	
	this.memberPopup = function(){
		commonPopupService.memberPopup($scope,'callback_member',false);
	}
	$scope.callback_member = function(data){
		$scope.search.memId = data[0].memId;
		$scope.search.memName = data[0].name;
		$scope.$apply();		
	}
	
	this.exhibitPopup = function(){
		commonPopupService.exhibitPopup($scope,'callback_exhibit',false);
	}
	$scope.callback_exhibit = function(data){
		$scope.search.memId = data[0].memId;
		$scope.search.memName = data[0].name;
		$scope.$apply();		
	}
	
	//제어설정 팝업
	this.restrictPopup = function() {
		commonPopupService.restrictPopup($scope, true, true, true, true, flag);
	}

	this.brandPopup = function(){
		commonPopupService.brandPopup($scope,'callback_brand',false);
	}
	$scope.callback_brand = function(data){
		$scope.search.brandId = data[0].brandId;
		$scope.search.brandName = data[0].name;
		$scope.$apply();		
	}
	
	// 카테고리 검색 팝업
	this.openCategoryPopup = function(flag) {
		var url = '/' + flag + '/popup/search';
		commonPopupService.categoryPopup($scope, 'callback_category', false, url);
	}
	// 카테고리 callback function
	$scope.callback_category = function(data, flag) {
		if (flag == 'pms') {
			$scope.search.categoryId = data.categoryId;
			$scope.search.categoryName = data.name;
			$scope.search.categoryFullName = data.depthFullName;
		} else {
			$scope.search.dispCategoryId = data.displayCategoryId;
			$scope.search.dispCategoryName = data.name;
			$scope.search.dispCategoryFullName = data.depthFullName;
		}
		$scope.$apply();
	}
	
	this.userPopup = function(){
		commonPopupService.userPopup($scope,'callback_user',false);
	}
	$scope.callback_user = function(data){
		$scope.search.userId = data[0].userId;
		$scope.search.userName = data[0].name;
		$scope.$apply();
	}

	this.mdPopup = function(){
		commonPopupService.userPopup($scope,'callback_md',false,'USER_TYPE_CD.MD');
	}
	$scope.callback_md = function(data){
		$scope.search.mdId = data[0].userId;
		$scope.search.mdName = data[0].name;
		$scope.$apply();
	}
	
	//상품 검색 팝업
	this.productPopup = function(){
		
		commonPopupService.productPopup($scope,'callback_product', false);
	}
	
	$scope.callback_product = function(data){
		$scope.productObj = data[0];
		$scope.$apply();
	}
	
	this.couponSearchPop = function() {
		commonPopupService.couponPopup($scope,'callback_coupon', true);
	}
	
	$scope.callback_coupon = function(data){
//		console.log(data);
		$scope.couponObj = data[0];
		$scope.$apply();
	}

	//grid bulk upload 팝업
	this.gridbulkuploadPopup = function(){
		
		commonPopupService.gridbulkuploadPopup($scope,'callback_grid', false);
	}
	
	$scope.callback_grid = function(data){
//		console.log(data);
	}
	
	this.memberDetailPopup = function(){
		
		commonPopupService.openMemberDetailPopup($scope, '1');
	}
	
	this.addressPopup = function(){
		
		commonPopupService.openAddressPopup($scope, 'calback_address');
	}
	
	$scope.calback_address = function(data) {
		console.log(data);
	}
	
	
	// 주문조회 팝업
	this.orderPopup = function() {
		commonPopupService.orderPopup($scope, 'callback_order', false);
	}
	$scope.callback_order = function(selectedData) {
		// 주문번호, 상품번호, 단품번호
		$scope.search.orderId = selectedData[0].orderId;
		$scope.search.productId = selectedData[0].productId;
		$scope.search.saleproductId = selectedData[0].saleproductId;
		$scope.$apply();
	}
	
}).controller('pms_brandListApp_controller', function($window, $scope, commonService,gridService) {				
	
	$scope.search = {};
	
	angular.element(document).ready(function () {		
		
		commonService.init_search($scope,'search');	
					
	});						
	
	
	var columnDefs= [			             
		             { field: 'brandId', 			 colKey: "pmsBrand.brandId" },
		             { field: 'name', 				 colKey: "pmsBrand.name"},
		             { field: 'templateId', 		 colKey: "pmsBrand.templateId"  },
		             { field: 'displayYn', 				 colKey: "pmsBrand.displayYn", cellFilter: "displayYnFilter"},
		             { field: 'insId', 				 colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
		             { field: 'insDt', 				 colKey: "c.grid.column.insDt"  , displayName : "등록일시"},
		             { field: 'updId', 				 colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},
		             { field: 'updDt', 				 colKey: "c.grid.column.updDt"  , displayName : "최종수정일시"}		             			             			            
		         ];		
	
	var gridParam = {
			scope : $scope, 
			gridName : 'grid_brand',
			url :  '/api/pms/brand/popup', 
			searchKey : 'search', 
			columnDefs : columnDefs,			
			gridOptions : {checkMultiSelect : $window.opener.$scope._brand_param.multi}
	};
	
	var brandGrid = new gridService.NgGrid(gridParam);
	
	this.searchBrand = function(){		
		brandGrid.loadGridData();		
	}
	
	this.selectBrand = function(){
		$window.opener.$scope[$window.opener.$scope._brand_param.callback](brandGrid.getSelectedRows());
		window.close();		
	}
	
	this.close = function(){
		window.close();
	}
	
	this.reset = function(){
		commonService.reset_search($scope,'search');
	}
	 			
}).controller('ccs_businessListApp_controller', function($window, $scope, commonService,gridService) {
	
	// 업체 조회 팝업
	
	$scope.search = {};
	
	angular.element(document).ready(function () {		
		
		commonService.init_search($scope,'search');	
					
	});						
	
	
	var columnDefs= [			             
		             { field: 'businessId', 			colKey: "ccsBusiness.businessId" },
		             { field: 'name',					colKey: "ccsBusiness.name"  },
		             { field: 'erpBusinessId', 			colKey: "ccsBusiness.erpBusinessId" },
		             { field: 'businessStateName', 		colKey: "ccsBusiness.businessStateCd"  },
		             { field: 'saleTypeName', 			colKey: "ccsBusiness.saleTypeCd"  },
		             { field: 'purchaseYn', 			colKey: "ccsBusiness.purchaseYn"  },
		             { field: 'businessTypeName', 		colKey: "ccsBusiness.businessTypeCd"},
		             { field: 'overseasPurchaseYn', 	colKey: "ccsBusiness.overseasPurchaseYn"},
		             { field: 'managerName', 			colKey: "ccsBusiness.managerName"}
/*		             { field: 'insId', 					width: '100', colKey: "c.grid.column.insId"  },
		             { field: 'insDt', 					width: '100', colKey: "c.grid.column.insDt"  },
		             { field: 'updId', 					width: '100', colKey: "c.grid.column.updId"  },
		             { field: 'updDt', 					width: '100', colKey: "c.grid.column.insDt"  }		*/             			             			            
		         ];		
	
	var gridParam = {
			scope : $scope, 
			gridName : 'grid_business',
			url :  '/api/ccs/business/popup', 
			searchKey : 'search', 
			columnDefs : columnDefs,			
			gridOptions : {checkMultiSelect : $window.opener.$scope._business_param.multi}
	};
	
	var businessGrid = new gridService.NgGrid(gridParam);
	
	this.searchBusiness = function(){		
		businessGrid.loadGridData();		
	}
	
	this.selectBusiness = function(){
		$window.opener.$scope[$window.opener.$scope._business_param.callback](businessGrid.getSelectedRows());
		window.close();		
	}
	
	this.close = function(){
		window.close();
	}
	
	this.reset = function(){
		commonService.reset_search($scope,'search');
	}
	 			
}).controller('ccs_memberSearchApp_controller', function($window,$filter, $scope, commonService, gridService) {
	
	$scope.search = {};
	$window.$scope = $scope;
	angular.element(document).ready(function () {
		
		// 쿠폰에서 회원검색 팝업 띄웠을때 기간조건 제거
		var pScope = $window.opener.$scope;
		if(angular.isDefined(pScope.isPeriod)) {
			$scope.isPeriod = pScope.isPeriod;
			$scope.search.startDate = '';
			$scope.search.endDate = '';
		} else {
			$scope.isPeriod = true;
		}
		
		commonService.init_search($scope,'search');
		
		$scope.search.memState = "MEM_STATE_CD.REG";
		$scope.search.memStateCds = "'MEM_STATE_CD.REG'";
		$scope.search.blacklistN = true;
	});						
	
	
	var	columnDefs = [
	   	              	{ field: 'memberNo'					, width: '100'	, colKey: "c.mmsMember.memberNo"					, linkFunction:'memberDetail'   },
	   	              	{ field: 'mmsMember.memberName'		, width: '100'	, colKey: "c.mmsCustomer.customer.name"				, linkFunction:'memberDetail'   },
	   	              	{ field: 'mmsMember.birthday'		, width: '100'	, colKey: "c.mmsCustomer.ssnbirthday"				 								},
	   	              	{ field: 'mmsMember.genderCd'		, width: '100'	, colKey: "c.mmsCustomer.sex"							 							
	   	              										, dropdownCodeEditor : "SEX", cellFilter :'sexFilter'				, validators:{required:true}	},
	   	              	{ field: 'mmsMember.email'			, width: '100'	, colKey: "c.mmsMember.email"						 								},
	   	              	{ field: 'memGradeCd'				, width: '100'	, colKey: "c.mmsMemberZts.memGradeCd"		
	   	              										, dropdownCodeEditor : "MEM_GRADE_CD", cellFilter :'memGradeFilter'	, validators:{required:true}	},
	   	              	{ field: 'mmsMember.employeeYn'				, width: '100'	, colKey: "c.mmsCustomer.classId"					, enableCellEdit:false			},
	   	              	{ field: 'membershipYn'				, width: '100'	, colKey: "c.mmsMemberZts.membershipYn"					 							},
	   	              	{ field: 'childrenYn'				, width: '100'	, colKey: "c.mmsMemberZts.childrenCardYn"				 							},
	   	              	{ field: 'b2eYn'					, width: '100'	, colKey: "c.mmsMemberZts.b2eYn"						 							},
	   	              	{ field: 'mmsMember.premiumYn'		, width: '100'	, colKey: "c.mmsCustomer.premiumYn"						 							},
	   	              	{ field: 'mmsMember.memberStateCd'			, width: '100'	, colKey: "c.mmsMember.status"				
	   	              										, dropdownCodeEditor : "STATUS", cellFilter :'statusFilter'			, validators:{required:true}	},
	   	              	{ field: 'mmsMember.phone2'			, width: '100'	, colKey: "c.mmsCustomer.cellno"						 							},
	   	              	{ field: 'mmsMember.smsYn'			, width: '100'	, colKey: "c.mmsCustomer.smsreceptflag"					 							},
	   	              	{ field: 'mmsMember.emailYn'		, width: '100'	, colKey: "c.mmsCustomer.emailreceptflag"				 							},
	   	              	{ field: 'mmsMember.appPushYn'		, width: '100'	, colKey: "c.mmsCustomer.pushreceptflag"				 							},
	   	              	{ field: 'blacklistYn'				, width: '100'	, colKey: "c.mmsBlacklist.blacklistYn"					 							},
			            { field: 'mmsMember.regDt'			, width: '100'	, colKey: "c.grid.column.insDt"						, displayName : "등록일시"								},
			            { field: 'updDt'					, width: '100'	, colKey: "c.grid.column.updDt"						, displayName : "최종수정일시"		},
			            { field: 'updId'					, width: '100'	, colKey: "c.grid.column.updId"						, userFilter :'updId,updName'	}
			         ];	
	
	var gridParam = {
			scope : $scope, 
			gridName : 'grid_member',
			url :  '/api/mms/member/popup', 
			searchKey : 'search', 
			columnDefs : columnDefs,			
			gridOptions : {checkMultiSelect : $window.opener.$scope._member_param.multi}
	};
	
	$scope.infoType = [
	                    {val : 'USERID',text : '회원ID'},
	                    {val : 'NAME',text : '회원명'}
		              ];
	
	$scope.phoneType = [
						 {val : 'TEL',text : '전화번호'},
						 {val : 'CELL',text : '휴대폰번호'}
	                   ];
	
	$scope.memberGrid = new gridService.NgGrid(gridParam);
	
	this.searchMember = function(){		
		$scope.memberGrid.loadGridData();		
	}
	
	this.selectMember = function(){
		$window.opener.$scope[$window.opener.$scope._member_param.callback]($scope.memberGrid.getSelectedRows());
		window.close();		
	}
	
	this.close = function(){
		window.close();
	}
	
	$scope.memberDetail = function(field, row) {
		$scope.memberNo = row.memberNo;
		var winName='회원상세';
		var winURL = Rest.context.path +"/mms/member/popup/detail";
		popupwindow(winURL,winName,1200,1000);
	}
	
	this.reset = function(){
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
		
		$scope.search.memState = "MEM_STATE_CD.REG";
		$scope.search.memStateCds = "'MEM_STATE_CD.REG'";
		$scope.search.blacklistN = true;
	}
}).filter('memGradeFilter', function() {// MEM_GRADE_CD 
	
	var comboHash = {
		    'MEM_GRADE_CD.VIP': 'VIP',
		    'MEM_GRADE_CD.GOLD': '골드',
		    'MEM_GRADE_CD.SILVER': '실버',
		    'MEM_GRADE_CD.FAMILY': '패밀리',
		    'MEM_GRADE_CD.WELCOME': '웰컴'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };	
}).filter('statusFilter', function() {// MEM_GRADE_CD 
	
	var comboHash = {
			'1': '일반',
			'3': '준회원',
			'9': '휴면',
			'0': '탈퇴',
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };	
}).filter('sexFilter', function() {// SEX 
	
	var comboHash = {
			'GENDER_CD.MALE1': '남성',
			'GENDER_CD.MALE3': '여성',
			'GENDER_CD.FEMALE2': '남성',
			'GENDER_CD.FEMALE4': '여성',
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };	
	 			
}).controller('dms_exhibitSearchPopApp_controller', function($window, $scope, $interval, $compile, gridService, commonService) {
	var pScope = $window.opener.$scope;
	$window.$scope = $scope;
	$scope.exhibitSearch = {};
	
	$scope.productInfoType = [
	  	                   	{val : 'NAME',text : '상품명'},
	  	                    {val : 'PRODUCTID',text : '상품번호'}
	  		              ];
  	$scope.exhibitInfoType = [
  	                          {val : 'NAME',text : '기획전명'},
  	                          {val : 'EXHIBITID',text : '기획전ID'}
  	                          ];
	
	var	columnDefs = [
{ field: 'exhibitId'		, width: '10%'	, colKey: "dmsExhibit.exhibitId"	, enableCellEdit:false				, linkFunction:'detail'							},
{ field: 'name'				, width: '15%'	, colKey: "dmsExhibit.name"			, vKey:"dmsExhibit.name"			, linkFunction:'detail'	 , enableCellEdit: false},
{ field: 'exhibitTypeCd' 	, width: '10%'	, colKey: "c.dmsExhibit.type"		, vKey:"dmsExhibit.exhibitTypeName" , enableCellEdit:false 
							, dropdownCodeEditor : "EXHIBIT_TYPE_CD"			, cellFilter :'exhibitTypeFilter' 	, validators:{required:true}					},
{ field: 'exhibitStateCd'	, width: '10%'	, colKey: "c.dmsExhibit.state"	, enableCellEdit: false
							, dropdownCodeEditor : "EXHIBIT_STATE_CD"			, cellFilter :'exhibitStateFilter' 	, validators:{required:true}					},
{ field: 'sortNo'			, width: '10%'	, colKey: "c.dmsExhibit.sortNo"		, enableCellEdit: false				, type:'number'									},
{ field: 'startDt'			, width: '15%'	, colKey: "dmsExhibit.startDt"		, vKey:"dmsExhibit.startDt" 														},
{ field: 'endDt'			, width: '15%'	, colKey: "dmsExhibit.endDt"		, vKey:"dmsExhibit.endDt"															},
{ field: 'insDt'			, width: '15%'	, colKey: "c.grid.column.insDt"		, enableCellEdit:false				, displayName : "등록일시"												},
{ field: 'insId'			, width: '10%'	, colKey: "c.grid.column.insId"		, enableCellEdit:false				, userFilter :'insId,insName'					},
{ field: 'updDt'			, width: '15%'	, colKey: "c.grid.column.updDt"		, enableCellEdit:false				, displayName : "최종수정일시"						},
{ field: 'updId'			, width: '10%'	, colKey: "c.grid.column.updId"		, enableCellEdit:false				, userFilter :'updId,updName'					}	
			         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : 'grid_exhibit',	//mandatory
			url :  '/api/dms/exhibit',  //mandatory
			searchKey : 'exhibitSearch',       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				gridOptions : {checkMultiSelect : $window.opener.$scope._exhibit_param.multi}
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {		
		$interval(function() {
			commonService.init_search($scope, 'exhibitSearch');
		}, 1, 1);
	});
	// 기획전 리스트 조회
	this.getExhibitList = function() {
		$scope.myGrid.loadGridData();
	}
	
	$scope.detail = function(field, row) {
		$scope.exhibitId = row.exhibitId;
		var winName='기획전 상세';
		var winURL = Rest.context.path +'/dms/exhibit/popup/detail';
		popupwindow(winURL,winName,1100,800);
	}
	
	// 기획전 선택
	this.exhibitSelect = function() {
		$window.opener.$scope[$window.opener.$scope._exhibit_param.callback]($scope.myGrid.getSelectedRows());
		window.close();	
	}
	
	// 취소
	this.cancel = function() {
		$window.close();
	}
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'exhibitSearch');
		angular.element(".day_group").find('button:first').addClass("on");
	}
}).filter('exhibitTypeFilter', function() {// EXHIBIT_TYPE_CD 
	
	var comboHash = {
		    'EXHIBIT_TYPE_CD.EXHIBIT': '일반기획전',
		    'EXHIBIT_TYPE_CD.COUPON': '쿠폰받기',
		    'EXHIBIT_TYPE_CD.TIMESALE': '타임세일',
		    'EXHIBIT_TYPE_CD.ONEDAY': '원데이',
		    'EXHIBIT_TYPE_CD.OFFSHOP': '오프라인매장'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
}).filter('exhibitStateFilter', function() {// EXHIBIT_STATE_CD 
	
	var comboHash = {
			'EXHIBIT_STATE_CD.READY' : '대기',
			'EXHIBIT_STATE_CD.RUN' : '진행중',
			'EXHIBIT_STATE_CD.STOP' : '정지'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };
	
	
}).controller('ccs_restrictPopApp_controller', function($window, $scope, $compile, controlService) {// 제한설정 공통 팝업
	var pScope = $window.opener.$scope;
	
	$scope.trShowYn = {};
	$scope.ccsControl = {};
	$scope.ccsControl.ccsControlmembergrades = [];
	$scope.ccsControl.ccsControlmembertypes = [];
	$scope.ccsControl.ccsControlchannels = [];
	$scope.ccsControl.ccsControldevices = [];
	var channelIdArr = [];
	var channelInfoArr = [];
	
	angular.element(document).ready(function() {
		// 쿠폰 수정불가 요건으로 쿠폰상세에서만 disabled
		$scope.isRun = angular.isDefined(pScope.disableFlag) ? pScope.disableFlag : false;
		console.log("pop", $scope.isRun);
//		$scope.ccsControl = {controlNo : pScope.controlNo};
		
		$scope.ccsControl = pScope.ccsControl;
		$scope.trShowYn = pScope._control_param;
		var controlNo =  pScope.controlNo;
		//최초 팝업 로드이고 기존 제한정보가 존재하는 경우
		if ( angular.isDefined(controlNo) &&  common.isEmptyObject($scope.ccsControl)) {
				var jsonControlNo = { controlNo : controlNo };
				controlService.getControlInfo(jsonControlNo, function(response) {
		//			console.log('response==============>', response);
					$scope.ccsControl = response;
		//			console.log('ccsControl==============>', $scope.ccsControl);
					
					//회원유형 제한정보
					var memberType = $scope.ccsControl.ccsControlmembertypes[0].memberTypeCd;
					if (null !== memberType && '' != memberType) {
						var memberTypeArr = [];
						var memberTypeCnt = $scope.ccsControl.ccsControlmembertypes.length;
						for (var i=0; i < memberTypeCnt; i++) {
							memberTypeArr[i] = $scope.ccsControl.ccsControlmembertypes[i].memberTypeCd;
						}
						$scope.ccsControl.memberTypes = memberTypeArr.join();
					} else {
						$scope.ccsControl.memberTypes = '';
					}
					//회원등급 제한정보
					var memGrade = $scope.ccsControl.ccsControlmembergrades[0].memGradeCd;
					if (null != memGrade && '' != memGrade) {
						var memGradeArr = [];
						var memGradeCnt = $scope.ccsControl.ccsControlmembergrades.length;
						for (var i=0; i < memGradeCnt; i++) {
							memGradeArr[i] = $scope.ccsControl.ccsControlmembergrades[i].memGradeCd;
						}
						$scope.ccsControl.memGrades = memGradeArr.join();
					} else {
						$scope.ccsControl.memGrades = '';
					}
					//채널 제한정보
					if ('CHANNEL_CONTROL_CD.ALL' == $scope.ccsControl.channelControlCd || 'CHANNEL_CONTROL_CD.DIRECT' == $scope.ccsControl.channelControlCd) {
						angular.element(document.querySelector('.ad_select')).prop('disabled', true);
					} else {
						angular.element(document.querySelector('.ad_select')).prop('disabled', false);
		
						var channelCnt = $scope.ccsControl.ccsControlchannels.length;
						var temp_name = '';
						for (var i=0; i<channelCnt; i++) {
							channelIdArr.push($scope.ccsControl.ccsControlchannels[i].channelId);
							channelInfoArr.push({
								channelId: $scope.ccsControl.ccsControlchannels[i].channelId,
								channelName: $scope.ccsControl.ccsControlchannels[i].name
							})
							temp_name = '<span>' + $scope.ccsControl.ccsControlchannels[i].name + '<button type="button" class="btn_txt_del" ng-click="ctrl.deleteChannel(' + $scope.ccsControl.ccsControlchannels[i].channelId + ')" >삭제</button></span>';
							angular.element(document.querySelector('.ad_select')).siblings('.ad_box').append(temp_name);
						}
						var element = angular.element(document.querySelector('#adBox'));
						$compile(element)($scope);
					}
					//디바이스 제한정보
					var deviceType = $scope.ccsControl.ccsControldevices[0].deviceTypeCd;
					if (null != deviceType && '' != deviceType) {
						var deviceTypeArr = [];
						var deviceTypeCnt = $scope.ccsControl.ccsControldevices.length;
						for (var i=0; i < deviceTypeCnt; i++) {
							deviceTypeArr[i] = $scope.ccsControl.ccsControldevices[i].deviceTypeCd; 
						}
						$scope.ccsControl.deviceTypes = deviceTypeArr.join();
					} else {
						$scope.ccsControl.deviceTypes = '';
					}
		//			console.log('obj: ', JSON.stringify($scope.ccsControl));
				});
		}
		//팝업 재 로드인 경우
		else {
			//기존 채널정보가 있으면 화면 출력
			if (!angular.isUndefined(pScope.ccsControl.channelIdArr) && pScope.ccsControl.channelIdArr != null) {
				var channelCnt = pScope.ccsControl.channelIdArr.length;
				var temp_name = '';
				for (var i=0; i<channelCnt; i++) {
					channelIdArr.push(pScope.ccsControl.channelIdArr[i]);
					channelInfoArr.push({
						channelId: pScope.ccsControl.channelInfoArr[i].channelId,
						channelName: pScope.ccsControl.channelInfoArr[i].channelName
					});
					temp_name = '<span>' + pScope.ccsControl.channelInfoArr[i].channelName + '<button type="button" class="btn_txt_del" ng-click="ctrl.deleteChannel(' + pScope.ccsControl.channelIdArr[i] + ')" >삭제</button></span>';
					angular.element(document.querySelector('.ad_select')).siblings('.ad_box').append(temp_name);
				}
				var element = angular.element(document.querySelector('#adBox'));
				$compile(element)($scope);
			}
		}

		//채널 정보 조회
		controlService.getAllChannelList(function(response) {
//			console.log('channelList response==============>', response);
			$scope.channelList = response;
//			console.log('All_channel: ', $scope.channelList);
			$scope.allChannelCnt = $scope.channelList.length;

			// 채널 이름 저장정보 
//			if ('CHANNEL_CONTROL_CD.CHANNEL' == $scope.ccsControl.channelControlCd ) {
//				for(var i=0; i<$scope.ccsControl.channelIdArr.length; i++) {
//					channelIdArr.push($scope.ccsControl.ccsControlchannels[i].channelId);
//					temp_name = '<span>' + $scope.ccsControl.ccsControlchannels[i].name + '<button type='button' class='btn_txt_del' ng-click='ctrl.deleteChannel(' + $scope.ccsControl.ccsControlchannels[i].channelId + ')' >삭제</button></span>';
//					angular.element(document.querySelector('.ad_select')).siblings('.ad_box').append(temp_name);
//				}
//				var element = angular.element(document.querySelector('#adBox'));
//				$compile(element)($scope);
//			}
		});
	});
	
	//채널명 조회
	$scope.getChannelName = function(channelId) {
		var channelName = '';
		for (var i=0; i<$scope.allChannelCnt; i++) {
			if ($scope.channelList[i].channelId == channelId) {
				channelName = $scope.channelList[i].name;
				break;
			}
		}
		return channelName;
	}

	this.selectChannel = function() {
//		alert('Add ID: ' + $scope.channelId);
		if(null == $scope.channelId) {
			alert('광고채널을 선택해 주세요');
			return;
		}

		if (channelIdArr.indexOf($scope.channelId) == -1) {
			channelIdArr.push($scope.channelId);
			channelInfoArr.push({
				channelId: $scope.channelId,
				channelName: $scope.getChannelName($scope.channelId)
			});
        } else {
            alert('이미 등록된 값입니다');
            return;
        }

		var now_str = angular.element('.ad_select option:selected').text();
		var temp_name = '<span>' + now_str + '<button type="button" class="btn_txt_del" ng-click="ctrl.deleteChannel(' + $scope.channelId + ')" >삭제</button></span>'
		angular.element('.ad_select').siblings('.ad_box').append(temp_name);
		var element = angular.element(document.querySelector('#adBox'));
		$compile(element)($scope);
	}

	this.deleteChannel = function(id) {
//		alert('Del ID: ' + id);
		for (var i = 0; i < channelIdArr.length; i++) {
			if (channelIdArr[i] == id) {
				channelIdArr.splice(i, 1);
				channelInfoArr.splice(i, 1);
			}
		}
//		console.log('final ID: ', channelIdArr);
	}
	
	//제한 설정 정보 저장
	this.saveRestrict = function() {
		$scope.ccsControl.memberTypeArr = [];
		$scope.ccsControl.memGradeArr = [];
		$scope.ccsControl.deviceTypeArr = [];
		
		if(common.isEmpty($scope.ccsControl.memberTypes) && $scope.trShowYn.type){
			alert("허용 회원 유형을 선택해 주세요.");
			return;
		}
		if(common.isEmpty($scope.ccsControl.memGrades) && $scope.trShowYn.grade){
			alert("허용 회원 등급을 선택해 주세요.");
			return;
		}
		if(common.isEmpty($scope.ccsControl.deviceTypes) && $scope.trShowYn.device){
			alert("허용 device를 선택해 주세요.");
			return;
		}
		//회원유형 배열변환
		if (null != $scope.ccsControl.memberTypes && '' != $scope.ccsControl.memberTypes) {
			var typeArr = $scope.ccsControl.memberTypes.split(',');
			for (var i=0; i<typeArr.length; i++) {
				$scope.ccsControl.memberTypeArr[i] = typeArr[i];
			}
		} else {
			$scope.ccsControl.memberTypeArr = null;
		}
		//회원등급 배열변환
		if (null != $scope.ccsControl.memGrades && '' != $scope.ccsControl.memGrades) {
			var gradeArr = $scope.ccsControl.memGrades.split(',');
			for (var i=0; i<gradeArr.length; i++) {
				$scope.ccsControl.memGradeArr[i] = gradeArr[i];
			}
		} else {
			$scope.ccsControl.memGradeArr = null;
		}
		//채널 배열
		if ('CHANNEL_CONTROL_CD.CHANNEL' == $scope.ccsControl.channelControlCd) {
			$scope.ccsControl.channelIdArr = channelIdArr;
			$scope.ccsControl.channelInfoArr = channelInfoArr;
		} else {
			$scope.ccsControl.channelIdArr = null;
			$scope.ccsControl.channelInfoArr = null;
		}
		//device 배열변환
		if (null != $scope.ccsControl.deviceTypes && '' != $scope.ccsControl.deviceTypes) {
			var deviceArr = $scope.ccsControl.deviceTypes.split(',');
			for (var i=0; i<deviceArr.length; i++) {
				$scope.ccsControl.deviceTypeArr[i] = deviceArr[i];
			}
		} else {
			$scope.ccsControl.deviceTypeArr = null;
		}

		$scope.ccsControl.firstYn = false;
		pScope.ccsControl = $scope.ccsControl;
		common.safeApply(pScope);
//		console.log('pScope_final==============>', pScope.ccsControl);

		$window.close();
	}

	this.close = function() {
		$window.close();
	}
});


//7. 카테고리 팝업
ccsAppPopup.controller('categoryCtrl', function($window, $scope, categoryService, displayCategoryService, displayTreeService) {
	// 팝업에서 부모 scope 접근하기 위함
	var pScope = $window.opener.$scope;
	
	$scope.categories = [];
	$scope.search = {};
	
	if(!common.isEmpty(pScope.search)){
		$scope.search.categoryRootId = pScope.search.categoryRootId;
	}
	
	//console.log($scope.search)
	var exhibit = common.isEmpty(pScope.exhibit) ? null : pScope.exhibit;
	$scope.exhibit = exhibit;
	
	// 트리 조회
	this.tree = function(flag) {
		if (flag == 'pms') {
			//console.log($scope.search)
			categoryService.getCategories($scope.search, function(data) {
				$scope.categories = data;
			});
		} else {
			displayCategoryService.getDisplayCategories($scope.search, function(data) {
				$scope.categories = data;
			});
		}
	}

	// 카테고리 하위 열기/닫기
	this.openFolder = function(index, icon, flag) {
		if (flag == 'pms') {
			displayTreeService.openTree(index, icon, $scope.categories, 'categoryId', 'upperCategoryId');
		} else {
			displayTreeService.openTree(index, icon, $scope.categories, 'displayCategoryId', 'upperDisplayCategoryId');
		}
	}
	
	// 선택 카테고리 저장
	this.selectCategory = function(e, category, flag) {
		$window.opener.$scope[$window.opener.$scope._category_param.callback](category, flag);
		window.close();
	}

	this.close = function() {
		window.close();
	}
	
	this.reset = function(){
		commonService.reset_search($scope,'search');
	}
}).controller('ccs_userListApp_controller', function($window, $scope, commonService,gridService) {				
	
	$scope.search = {};
	$scope.userTypeDisabled = false;
	if(angular.isDefined($window.opener.$scope._user_param.userTypeCd)){
		$scope.userTypeDisabled = true;
		$scope.search.userTypeCd = $window.opener.$scope._user_param.userTypeCd;
		$scope.search.userType = $window.opener.$scope._user_param.userTypeCd;
	}

	angular.element(document).ready(function () {		

		commonService.init_search($scope,'search');	
					
	});						
	
	
	var columnDefs= [			             
		             { field: 'userId', 		width: '100', colKey: 'ccsUser.userId'},
		             { field: 'name', 			width: '100', colKey: 'ccsUser.name' },
		             { field: 'businessId',		width: '100', colKey: 'ccsUser.businessId'  },
		             { field: 'depName', 		width: '100', colKey: 'ccsUser.depCd'},
		             { field: 'email', 			width: '100', colKey: 'ccsUser.email'  },
		             { field: 'phone1', 		width: '100', colKey: 'ccsUser.phone1'  },
		             { field: 'phone2',	 		width: '100', colKey: 'ccsUser.phone2'  },
//		             { field: 'userTypeName',	width: '100', colKey: 'ccsUser.userTypeCd'  },
		             { field: 'userStateName',	width: '100', colKey: 'ccsUser.userStateCd'  },
		             { field: 'insId', 			width: '100', colKey: 'c.grid.column.insId'  , userFilter :'insId,insName'},
		             { field: 'insDt', 			width: '100', colKey: 'c.grid.column.insDt'  , displayName : "등록일시"},
		             { field: 'updId', 			width: '100', colKey: 'c.grid.column.updId'  , userFilter :'updId,updName'},
		             { field: 'updDt', 			width: '100', colKey: 'c.grid.column.updDt'  , displayName : "최종수정일시"}		             			             			            
		         ];		
	
	var gridParam = {
			scope : $scope, 
			gridName : 'grid_user',
			url :  '/api/ccs/user/popup', 
			searchKey : 'search', 
			columnDefs : columnDefs,			
			gridOptions : {checkMultiSelect : $window.opener.$scope._user_param.multi}
	};
	
	var userGrid = new gridService.NgGrid(gridParam);
	
	this.searchUser = function(){		
		userGrid.loadGridData();		
	}
	
	this.selectUser = function(){
		$window.opener.$scope[$window.opener.$scope._user_param.callback](userGrid.getSelectedRows());
		window.close();		
	}
	
	this.close = function(){
		window.close();
	}
	
	this.reset = function(){
		commonService.reset_search($scope,'search');
	}
	 			
}).controller('pms_productSearch_controller', function($window, $scope, commonService, gridService, commonPopupService) {		
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	// PO로그인일경우 업체ID
	var poBusinessId = global.session.businessId=='null' ? null : global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');	
	});						
	
	$scope.infoType = [
	                    {val : 'ID',text : '상품번호'},
	                    {val : 'NAME',text : '상품명'}
		              ];

	$scope.priceCompareType = [
	                    {val : 'UP',text : '이상'},
	                    {val : 'DOWN',text : '이하'}
		              ];
	
	var columnDefs= [			             
		             { field: 'productId', 		 	colKey: 'pmsProduct.productId' , linkFunction : 'openProductPopup'},
		             { field: 'productTypeName', 		colKey: 'pmsProduct.productTypeCd' },
		             { field: 'name',				colKey: 'pmsProduct.name'  },
		             { field: 'pmsBrand.name',  colKey: 'pmsBrand.name'},
		             { field: 'saleStateName', 		colKey: 'pmsProduct.saleStateCd'  },
		             { field: 'insId', 				colKey: 'c.grid.column.insId'  , userFilter :'insId,insName'},
		             { field: 'insDt', 				colKey: 'c.grid.column.insDt'  , displayName : "등록일시"},
		             { field: 'displayYn', 			colKey: 'pmsProduct.displayYn'  }		             			             			            
		         ];
	
	var gridParam = {
			scope : $scope, 
			gridName : 'productSearchGrid',
			url :  '/api/pms/product', 
			searchKey : 'search',
			columnDefs : columnDefs,
			gridOptions : {checkMultiSelect : $window.opener.$scope.searchParam.multi}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	this.getProductList = function(){		
		$scope.myGrid.loadGridData();		
	}
	
	this.select = function(){
		$window.opener.$scope[$window.opener.$scope.searchParam.callback]($scope.myGrid.getSelectedRows());
		window.close();		
	}
	
	this.close = function(){
		window.close();
	}
	
	this.brandSearch = function() {
		commonPopupService.brandPopup($scope, 'callback_brand', false);
	}
	
	$scope.callback_brand = function(data) {
		$scope.search.brandId = data[0].brandId;
		$scope.search.brandName = data[0].name;
		$scope.$apply();
	}
	
	//지우기
	this.eraser = function(val1, val2) {
		$scope.search[val1] = '';
		
		if(angular.isDefined(val2)) {
			$scope.search[val2] = '';
		}
	}
	 		
	
	// 검색 조건 초기화
	this.resetData = function() {
		/* search Data 초기화 */
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	// 각 검색 팝업
	this.openPopup = function(kindOf) {
		if(kindOf == 'brand') {
			commonPopupService.brandPopup($scope,'callback_brand', false);
			$scope.callback_brand = function(data){
				$scope.search.brandId = data[0].brandId;
				$scope.search.brandName = data[0].name;
				
				$scope.$apply();		
			}
		} else if(kindOf == 'pms/category' || kindOf == 'dms/displayCategory') {
			var url = '/' + kindOf + '/popup/search';
			commonPopupService.categoryPopup($scope, 'callback_category', false, url);
			$scope.callback_category = function(data) {
				if (kindOf == 'pms/category') {
					$scope.search.categoryId = data.categoryId;
				} else {
					$scope.search.dispCategoryId = data.displayCategoryId;
				}
				$scope.$apply();
			}
		} else if(kindOf == 'md') {
			commonPopupService.userPopup($scope,'callback_user',false);
			$scope.callback_user = function(data){
				$scope.search.userId = data[0].userId;
				$scope.search.userName = data[0].name;
				$scope.$apply();
			}
		} else if(kindOf == 'business') {
			commonPopupService.businessPopup($scope,'callback_business',false);
			$scope.callback_business = function(data){
				$scope.search.businessId = data[0].businessId;
				$scope.search.businessName = data[0].name;
				$scope.$apply();		
			}
		}
	}
	
	
	//상품 상세 팝업
	$scope.openProductPopup = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
	
	
}).controller('sps_couponSearchPopApp_controller', function($window, $scope, $interval, $filter,commonService, gridService ) {
	
	$window.$scope = $scope;
	
	$scope.search = {};
	
	$scope.infoType = [
	                    {val : 'ID',text : '쿠폰번호'},
	                    {val : 'NAME',text : '쿠폰명'}
		              ];
	
	var	columnDefs = [
			             { field: 'couponId'		, colKey:'c.sps.coupon.coupon.id'	  		, linkFunction:'couponDetail'		, type:"number"},
			             { field: 'name'			, colKey:'spsCoupon.name' 					, linkFunction:'couponDetail'		},
			             { field: 'couponTypeName'	, colKey:'c.sps.coupon.pop.coupon.type'  	, vKey: 'spsCoupon.couponTypeCd'	},
			             { field: 'chgDcValue'		, colKey:'spsCoupon.dcValue'																					},
			             { field: 'couponStateName'	, colKey:'spsCoupon.couponStateCd' 		, vKey: 'spsCoupon.couponStateCd' },
			             { field: 'issueStartDt'	, colKey:'spsCoupon.issueStartDt'		, cellFilter: 'date:\'yyyy-MM-dd hh:mm:ss\''  							},
			             { field: 'issueEndDt'		, colKey:'spsCoupon.issueEndDt'			, cellFilter: 'date:\'yyyy-MM-dd hh:mm:ss\''    						},
			             { field: 'insId'			, colKey:'c.grid.column.insId'  		, userFilter :'insId,insName'		   									},
			             { field: 'insDt'			, colKey:'c.grid.column.insDt'			, displayName : "등록일시"		, cellFilter: 'date:\'yyyy-MM-dd hh:mm:ss\''							},
			             { field: 'updId'			, colKey:'c.grid.column.updId'			, userFilter :'updId,updName'											},
			             { field: 'updDt'			, colKey:'c.grid.column.updDt'			, displayName : "최종수정일시"	, cellFilter: 'date:\'yyyy-MM-dd hh:mm:ss\''   	 						}
			         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : 'gridOptions',	//mandatory
			url :  '/api/sps/coupon',  //mandatory
			searchKey : 'search',       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {checkMultiSelect : $window.opener.$scope.searchParam.multi}
	};

	
	//그리드 초기화
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	this.searchCoupon = function(){
		$scope.myGrid.loadGridData();
	};
	
	this.resetCoupon = function(){
		/* search Data 초기화 */
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	this.close = function(){
		window.close();
	}
	
	this.addCoupon = function() {
		$window.opener.$scope[$window.opener.$scope.searchParam.callback]($scope.myGrid.getSelectedRows());
		window.close();	
	}
	
	$scope.couponDetail = function(field, row) {
		$scope.couponId = row.couponId;
		var url= Rest.context.path + '/sps/coupon/popup/detail';
		popupwindow(url,'couponDetail',1100,600);
	}
	
	angular.element(document).ready(function () {
		$interval(function() {
			commonService.init_search($scope, 'search');
		}, 1, 1);
	});
	
}).controller('pms_presentSearch_controller', function($window, $scope, $compile,commonService, gridService ) {
	
	$scope.search = {};
	
	angular.element(document).ready(function () {		
		commonService.init_search($scope,'search');		
	});
	
	$scope.infoType = [
	                    {val : 'ID',text : '사은품번호'},
	                    {val : 'NAME',text : '사은품명'}
		              ];

	
	var columnDefs = [
		                 { field: 'productId'		, colKey: 'spsPresent.presentId'},
		                 { field: 'name'			, colKey: 'c.pms.present.name'  },
		                 { field: 'pmsSaleproduct.realStockQty'	, colKey: 'pmsSaleproduct.realStockQty'  },
		                 { field: 'useYn'			, colKey: 'pmsProduct.useYn', cellFilter: "useYnFilter"}
			            ];
	var gridParam = {
			scope : $scope,
			gridName : 'myGrid',
			url :  '/api/pms/present/',
			searchKey : 'search',
			columnDefs : columnDefs,
			gridOptions : {checkMultiSelect : $window.opener.$scope.searchParam.multi}
	};
	
	//그리드 초기화
	$scope.presentGrid = new gridService.NgGrid(gridParam);
	
	this.search = function(){
		$scope.presentGrid.loadGridData();
	};
	
	 this.reset = function() {
		 commonService.reset_search($scope,'search');
		 angular.element(".day_group").find('button:first').addClass("on");
	 }
	
	 
	 this.select = function() {
		 $window.opener.$scope[$window.opener.$scope.searchParam.callback]($scope.presentGrid.getSelectedRows());
		 window.close();	
	 }
	 
	this.close = function(){
		window.close();
	}

// 회원상세 팝업
}).controller('mms_memberDetailPopApp_controller', function($window, $scope, $compile,commonService, mmsService) {
	$window.$scope = $scope;
	var pScope = $window.opener.$scope;// 부모창의 scope
	$scope.mmsMemberZts = {};
	$scope.interestCategory;
	$scope.interestBrand;
	$scope.interestAge;
	$scope.interestStyle;
	$scope.interestOffshop;
	$scope.basicAddress={};
	
	// init
	this.init = function() {
		window.resizeTo(1200, 1000);
		var memberNo = pScope.memberNo;
		$scope.memberNo = pScope.memberNo;
		$scope.memberDetailTab = true;
		mmsService.getMemberDetail(memberNo, function(data) {
			$scope.mmsMemberZts = data;
			
			var strCateInterest = "";
			var strBrandInterest = "";
			var strAgeInterest = "";
			var strStyleInterest = "";
			if (data.mmsInterests != null) {
				if (data.mmsInterests.length > 0) {
					for ( var i=0;  i < data.mmsInterests.length; i++) {
						if (data.mmsInterests[i].interestTypeCd == "INTEREST_TYPE_CD.CATEGORY") {
							strCateInterest += data.mmsInterests[i].displayCategoryName + ", ";
						}
						if (data.mmsInterests[i].interestTypeCd == "INTEREST_TYPE_CD.BRAND") {
							strBrandInterest += data.mmsInterests[i].brandName + ", ";
						}
						if (data.mmsInterests[i].interestTypeCd == "INTEREST_TYPE_CD.AGE") {
							strAgeInterest += data.mmsInterests[i].ageTypeName + ", ";
						}
						if (data.mmsInterests[i].interestTypeCd == "INTEREST_TYPE_CD.STYLE") {
							strStyleInterest += data.mmsInterests[i].styleTypeName + ", ";
						}
					}
					
					$scope.interestCategory = strCateInterest.substring(0, strCateInterest.length-2);
					$scope.interestBrand = strBrandInterest.substring(0, strBrandInterest.length-2);
					$scope.interestAge = strAgeInterest.substring(0, strAgeInterest.length-2);
					$scope.interestStyle = strStyleInterest.substring(0, strStyleInterest.length-2);
				}
			}
			
			// 관심 매자 조회
			mmsService.getMemberInterestOffshopList(memberNo, function(data) {
				$scope.interestOffshop = data;
			});

		});
	}
	
	
	// SMS 수신 변경 팝업
	this.chgSmsReceive = function() {
		var winName='SMS수신변경';
		var winURL = Rest.context.path +'/mms/member/popup/changeSmsReceive';
		popupwindow(winURL,winName,900,300);
	}
	
	// EMAIL 수신 변경 팝업
	this.chgEmailReceive = function() {
		var winName='EMAIL수신변경';
		var winURL = Rest.context.path +'/mms/member/popup/changeEmailReceive';
		popupwindow(winURL,winName,900,300);
	}
	
	// appPush 수신 변경 팝업
	this.chgAppPushReceive = function() {
		var winName='AppPush수신변경';
		var winURL = Rest.context.path +'/mms/member/popup/changeAppPushReceive';
		popupwindow(winURL,winName,900,300);
	}
	
	// 임시비밀번호 발급 팝업
	this.issueTempPwd = function() {
		var winName='AppPush수신변경';
		var winURL = Rest.context.path +'/mms/member/popup/issueTempPwd';
		popupwindow(winURL,winName,900,300);
	}
	
	// 등급 변경 이력 조회 팝업
	this.gradeHistory = function() {
		var winName='등급 변경 이력 조회';
		var winURL = Rest.context.path +'/mms/member/popup/gradeHistory';
		popupwindow(winURL,winName,900,300);
	}
	
	// 팝업 닫기
	this.close = function(){
		$window.close();
	}
	
	this.moveTab = function($event, param) {
		if ('point' == param) {
			$window.location.href = Rest.context.path +'/mms/member/popup/point';
		} else if ('carrot' == param) {
			$window.location.href = Rest.context.path +'/mms/member/popup/carrot';
		} else if ('coupon' == param) {
			$window.location.href = Rest.context.path +'/mms/member/popup/coupon';
		} else if ('deposit' == param) {
			$window.location.href = Rest.context.path +'/mms/member/popup/deposit';
		} else if ('onlineOrder' == param) {
			$window.location.href = Rest.context.path +'/mms/member/popup/onlineOrder';
		} else if ('offlineOrder' == param) {
			$window.location.href = Rest.context.path +'/mms/member/popup/offlineOrder';
		}
	}
});


//ERP상품검색
ccsAppPopup.controller('pms_erpItemSearch_controller', function($window, $scope, $compile,commonService, gridService ) {
	
	$scope.search = {};
	
	angular.element(document).ready(function () {		
		commonService.init_search($scope,'search');		
	});
	
	var columnDefs = [
		                 { field: 'itemid'},
		                 { field: 'itemname',		colKey : 'pmsProducthistory.name'},
		                 { field: 'brandname',		displayName : '브랜드명' },
		                 { field: 'tkrTagprice',	displayName : '판매가'},
		                 { field: 'apxTax',			displayName : '과세유형', cellFilter: 'apxTax'}
			            ];
	var gridParam = {
			scope : $scope,
			gridName : 'myGrid',
			url :  '/api/pms/product/erp/item',
			searchKey : 'search',
			style : {height : 300},
			columnDefs : columnDefs,
			gridOptions : {checkMultiSelect : $window.opener.$scope.searchParam.multi}
	};
	
	//그리드 초기화
	$scope.presentGrid = new gridService.NgGrid(gridParam);
	
	// 검색
	this.search = function(){
		if($scope.search.itemid==null && $scope.search.itemname==null){
			alert('검색어를 입력해 주세요.');
			return;
		}
		$scope.presentGrid.loadGridData();
	};
	
	 this.reset = function() {
		 commonService.reset_search($scope,'search');		
	 }
	
	 
	 this.select = function() {
		 $window.opener.$scope[$window.opener.$scope.searchParam.callback]($scope.presentGrid.getSelectedRows());
		 window.close();	
	 }
	 
	this.close = function(){
		window.close();
	}
	
}).filter('apxTax', function () {
	return function (value) {
	    return value=='0'?'과세':'면세';
	};
});
//ERP 단품 검색
ccsAppPopup.controller('pms_erpSaleProductSearch_controller', function($window, $scope, $compile,commonService, gridService ) {
	
	$scope.search = {};
	
	angular.element(document).ready(function () {		
		commonService.init_search($scope,'search');		
	});
	
	var columnDefs = [
	                  { field: 'itemid',		displayName : 'ERP 코드'},
	                  { field: 'itemname',		colKey : 'pmsProducthistory.name'},
	                  { field: 'brandname',		displayName : '브랜드명' },
	                  { field: 'inventcolorid',		displayName : '컬러' },
	                  { field: 'inventsizeid',		displayName : '사이즈' },
	                  { field: 'tkrTagprice',	displayName : '판매가'},
	                  { field: 'apxTax',			displayName : '과세유형', cellFilter: 'apxTax'}
	                  ];
	var gridParam = {
			scope : $scope,
			gridName : 'myGrid',
			url :  '/api/pms/product/erp/item/saleProduct',
			searchKey : 'search',
			style : {height : 300},
			columnDefs : columnDefs,
			gridOptions : {checkMultiSelect : $window.opener.$scope.searchParam.multi}
	};
	
	//그리드 초기화
	$scope.presentGrid = new gridService.NgGrid(gridParam);
	
	// 검색
	this.search = function(){
		if($scope.search.itemid==null && $scope.search.itemname==null){
			alert('검색어를 입력해 주세요.');
			return;
		}
		$scope.presentGrid.loadGridData();
	};
	
	this.reset = function() {
		commonService.reset_search($scope,'search');		
	}
	
	
	this.select = function() { 
		$window.opener.$scope[$window.opener.$scope.searchParam.callback]($scope.presentGrid.getSelectedRows());
		window.close();	
	}
	
	this.close = function(){
		window.close();
	}
	
}).filter('apxTax', function () {
	return function (value) {
		return value=='0'?'과세':'면세';
	};
});

//벌크 업로드
ccsAppPopup.controller('ccs_gridbulkupload_controller', function($window, $scope, $compile,commonService, gridService ) {
	
	pScope = $window.opener.$scope;// 부모창의 scope
	
	angular.element(document).ready(function () {		
		
	});
	
	$scope.bulkType = pScope._gridbulkupload_Param.bulkType;
	//console.log($scope);
	var upName = '';
	var downName = 'c:/ZTS/upload/temp';
	var fileName = ""; 
	switch ($scope.bulkType) {
		case 'product':
			$scope.bulkTypeTxt = '상품 업로드';
			upName = 'product';
			fileName = '상품_업로드_템플릿.xlsx'
			//downName += 'priceReserveTemplate';
			break;
		case 'category':
			$scope.bulkTypeTxt = '전시카테고리 업로드';
			upName = 'category';
			fileName = '전시카테고리_업로드_템플릿.xlsx'
			break;
		case 'brand':
			$scope.bulkTypeTxt = '브랜드 업로드';
			upName = 'brand';
			fileName = '브랜드_업로드_템플릿.xlsx'
			break;
		case 'coupon':
			$scope.bulkTypeTxt = '쿠폰 일괄발급';
			upName = 'coupon';
			fileName = '쿠폰_업로드_템플릿.xlsx'
			break;
		case 'present':
			$scope.bulkTypeTxt = '사은품 상품 업로드';
			upName = 'present';
			fileName = '사은품_업로드_템플릿.xlsx'
			break;
		case 'holiday':
			$scope.bulkTypeTxt = '휴일 일괄 업로드';
			upName = 'holiday';
			fileName = '휴일목록변경_템플릿.xlsx'
			break;
		case 'offshop':
			$scope.bulkTypeTxt = '오프라인 매장 일괄 업로드';
			upName = 'offshop';
			fileName = '오프라인매장_업로드_템플릿.xlsx'
			break;
		case 'carrot':
			$scope.bulkTypeTxt = '당근 일괄 조정';
			upName = 'carrot';
			fileName = '당근_업로드_템플릿.xlsx'
			break;
		case 'deposit':
			$scope.bulkTypeTxt = '예치금 일괄 조정';
			upName = 'deposit';
			fileName = '예치금_업로드_템플릿.xlsx'
			break;
		case 'eventWinner':
			$scope.bulkTypeTxt = '당첨자 일괄 업로드';
			upName = 'eventWinner';
			fileName = '당첨자_업로드_템플릿.xlsx'
			break;
		case 'childrencard':
			$scope.bulkTypeTxt = '다자녀카드 일괄 업로드';
			upName = 'childrencard';
			fileName = '다자녀카드_업로드_템플릿.xlsx'
			break;
	}
	
	$scope.files = [];
	// listen for the file selected event
	$scope.$on('fileSelected', function(event, args) {
		if (!/(\.xlsx|\.xls)$/i.test(args.file.name)) {
			alert('엑셀 파일이 아닙니다.');
		} else {
			$scope.files = [];
			$scope.files.push(args.file);
		}
		
		common.safeApply($scope);
	});
	 
	// 팝업 닫기
	this.close = function() {
//		$window.opener.$scope[$window.opener.$scope._gridbulkupload_Param.callback]($scope.result);
		$window.close();
	}
	
	// 파일 경로 지우기
	this.eraser = function() {
		angular.element('input[name="filePath"]').val("");
	}
	
	// 파일 업로드
	this.uploadExcel = function() {
		if (angular.isDefined($scope.files[0]) && $scope.files[0] != null) {
			
			if(upName == 'coupon') {
				upName +=","+pScope.search.couponId;
			}
			
			var url = Rest.context.path + '/api/ccs/common/gridbulkupload/' + upName;
			commonService.uploadFileToUrl($scope.files[0], null, url, function(response) {
				
				var result = JSON.parse(response);
				if(Number(result.totalCnt) > 0 ) {
					$scope.result = result;
					$scope.totalCnt = result.totalCnt;
					$scope.successCnt = result.successCnt;
					$scope.failCnt = result.failCnt;
					$scope.excelPath = result.excelPath;
					$scope.resultList = result.resultList;
					
					$window.opener.$scope[$window.opener.$scope._gridbulkupload_Param.callback]($scope.result);
					alert('업로드를 완료하였습니다.');
					
				} else {
					$scope.totalCnt = 0;
					$scope.successCnt = 0;
					$scope.failCnt = 0;
					$scope.excelPath = "";
					
					alert('유효한 템플릿이 아닙니다.');
				}
				common.safeApply($scope);

			});
		} else {
			alert("업로드 할 EXCEL 파일을 등록해주세요.");
		}
	}
	
	this.downFailDataExcel = function(){
		if ($scope.excelPath) {
			$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + $scope.excelPath;
		}
//		var url = '/api/ccs/common/gridbulkupload/' + upName + '/errordown';
//		if($scope.result.errorList != null && $scope.result.errorList.length != 0){
//			commonService.exportExcel(url, $scope.result.errorList, function(response) {
//				if((response.content).substring(0,5) == 'error') {
////				 alert(response.content);
//				} else {
//					alert('엑셀 다운로드 성공 \n' + 'Path : ' + response.content);
//				}
//			});
//		}
	}
	
	// 엑셀템플릿 다운로드 
	this.downTemplate = function() {
		commonService.getConfig("excel.download.path.template", function(response) {
			var fullPath = response.content + '/' + fileName;
			$window.location = fullPath;
//			console.log(fullPath);
			//$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + fullPath;
		});
	}
});


// 주문검색팝업
ccsAppPopup.controller('orderPopupCtrl', function($window, $scope, commonService, gridService, commonPopupService) {
	
	var columnDefs = [
			{
				field : 'omsOrder.orderId',
				width : '150',
				colKey : 'c.oms.order.id',
				cellClass : 'alignC'
			}, {
				field : 'omsOrder.orderDt',
				width : '150',
				colKey : 'c.oms.order.datetime',
				cellClass : 'alignC'
			}, {
				field : 'productId',
				width : '150',
				colKey : 'c.oms.order.product_id',
				cellClass : 'alignC'
			}, {
				field : 'productName',
//				width : '100',
				colKey : 'c.oms.order.product_name'
			}, {
				field : 'saleproductId',
				width : '150',
				colKey : 'c.oms.order.saleproduct_id',
				cellClass : 'alignC'
			}, {
				field : 'saleproductName',
//				width : '100',
				colKey : 'c.oms.order.saleproduct_name'
			}, {
				field : 'omsOrder.name1',
				width : '100',
				colKey : 'c.oms.order.name',
				cellClass : 'alignC'
			}
	];
	var gridParam = {
		scope : $scope,
		gridName : 'grid_order',
		url : '/api/oms/order',
		searchKey : 'search',
		columnDefs : columnDefs,
		gridOptions : {
			checkMultiSelect : $window.opener.$scope.searchParam.multi,
			enableRowSelection : true,
			multiSelect : false,
			pagination : false,
			rowSelectionFn : function(row) {
				if (!$window.opener.$scope.searchParam.multi) {
					var entities = this.data;
					for (var i = 0; i < entities.length; i++) {
						entities[i].checked = false;
					}
				}
				row.entity.checked = row.isSelected;
			}
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);
	
	// 선택 상품 저장
	this.select = function() {
		$window.opener.$scope[$window.opener.$scope.searchParam.callback](myGrid.getSelectedRows());
		window.close();
	}

	this.close = function() {
		window.close();
	}
	
	var pScope = $window.opener.$scope;
//	$window.$scope = $scope;
	$scope.search = {
		searchId : 'oms.order.select.productList',
		reset : function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		},
		init : function() {
		},
		memberNo : pScope.search.memberNo,
		ordererType : pScope.search.ordererType,
		orderer : pScope.search.orderer
	}
	$scope.list = {
		search : function() {
			// 폼 체크
			if (!commonService.checkForm($scope.searchForm)) {
				return;
			}
			myGrid.loadGridData();
		}
	}
	
	angular.element(document).ready(function() {
		commonService.init_search($scope, 'search');
	});
	
	// 오프라인 매장 검색 팝업
}).controller("ccs_offshopListPopupController", function($window, $scope, $filter, userService, commonService,gridService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$scope.search = {};
	
	var columnDefs =  [
	                   	{ field: 'offshopId'		, colKey: "c.ccs.offShop.offShopId"			},
 	                     { field: 'offshopTypeName'	, colKey: "c.ccs.offShop.offShopTypeCd"		},
 	                     { field: 'name'			, colKey: "c.ccs.offShop.name"				},
 	                     { field: 'offshopBrands'	, colKey: "c.ccs.offShop.brand"				},
 	                     { field: 'address'			, colKey: "c.ccs.offShop.address"			}
	       	]	
	
	var gridParam = {
			scope : $scope, 
			gridName : "grid_offshop",
			url :  "/api/ccs/offshop/popup", 
			searchKey : "search", 
			columnDefs : columnDefs,			
			gridOptions : {checkMultiSelect : $window.opener.$scope._offshop_param.multi}
	};
	
	
	var offGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'search');
	}
	
	this.searchGrid = function(){		
		offGrid.loadGridData();		
	}
	
	this.selectOffshop = function() {
		$window.opener.$scope[$window.opener.$scope._offshop_param.callback](offGrid.getSelectedRows());
		window.close();	
	}
	
	this.close = function(){
		window.close();
	}
	
	
}).controller("addressPopupController", function($window, $scope,$filter, userService, commonService, gridService) {	
	
	// paging 
	var vm = this;
	vm.addressList = []; //declare an empty array
    vm.pageno = 1; // initialize page no to 1
    vm.total_count = 0;
    vm.itemsPerPage = 10; //this could be a dynamic value from a drop down
   
    vm.getData = function(pageno){ // This would fetch the data on page change.
       $scope.ctrl.searchAddress(pageno);
    };
	
	$scope.search = {};
	$scope.search.district =[];
	$scope.districtList = [];
	$scope.sigunguList = [];
	$scope.searchResultList = [];
	$scope.search.sidoName = "";
	$scope.search.sigunguName = "";
	$scope.searchSelect = false;
	$scope.searchResult = false;
	$scope.jibunTab = true;
	$scope.roadTab = false;
	
	angular.element(document).ready(function () {
		$scope.ctrl.districtList();
	});
	
	// 시도 조회
	this.districtList = function() {
		$scope.search = {};
		commonService.getDistrictList($scope.search, function(data) {
			$scope.districtList = data;
		});
	}
	
	// 시도 선택
	this.changeDistrict = function(param) {
		if (param != null) {
			$scope.sigunguList = [];
			$scope.search.searchStr = param.regioncd;
			$scope.search.sidoName = param.regionnm;
			
			$scope.search.searchFlag = 'B';
			commonService.getDistrictList($scope.search, function(data) {
				$scope.sigunguList = data;
			});
		}
	}
	
	// 시군구 선택
	this.changeSigungu = function(param) {
		$scope.search.sigunguName = param.regionnm;
	}
	
	this.onKeyDown = function(keyEvent) {
	     if (keyEvent.which == 13) {
	        $scope.ctrl.searchAddress();
	     }
	}
	
	// 주소 검색
	this.searchAddress = function(pageNo) {
		
		// 초기화
		$scope.search.detailAddress = "";
		$scope.search.selectRoadAddr = "";
		$scope.search.selectJibunAddr = "";
		$scope.search.clickAddressIndex = "";
		$scope.search.clickPostNo =  "";
		$scope.search.clickAddress = "";
		
		$scope.searchSelect = false;
		$scope.searchResult = false;
		$scope.detailInput = false;
		
		
		if ($scope.search.district == '' || $scope.search.district == undefined) {
			alert("시/도 선택해주세요.");
			return;
		}
		if ($scope.search.sigungu == '' || $scope.search.sigungu == undefined) {
			if ($scope.search.district.regioncd != '3600000000') {
				alert("시/군/구 선택해주세요.");
				return;
			}
		}
		
		if ($scope.search.searchKeyword == '' || $scope.search.searchKeyword == undefined) {
			alert("검색어를 입력해주세요.");
			return;
		}
		
		$scope.search.pageNo = pageNo;
		commonService.searchAddress($scope.search, function(data) {
			$scope.searchResultList = data;
			$scope.searchResult = true;
			vm.addressList = [];  
	        vm.addressList = data;  // data to be displayed on current page.
	        if (data.length > 0) {
	        	$scope.detailInput = true;
	        	vm.total_count = data[0].totrecord; // total data count.
	        	window.resizeBy(0, 300);
	        } else {
	        	vm.total_count = 0;
	        }
		});
		
	}
	
	// 주소 클릭
	this.addressClick = function(index) {
		
		angular.element("#row").find('tr').removeClass("on");
		angular.element("#row").find('tr').eq(index+1).addClass("on");
		
		$scope.search.clickAddressIndex = index; 
		$scope.search.clickPostNo =  $scope.searchResultList[index].bsizonno;
		var clickAddress = "";
		if ($scope.jibunTab) {
			clickAddress = $scope.searchResultList[index].sido +" "+ $scope.searchResultList[index].gungu; 
			clickAddress += " " + $scope.searchResultList[index].dong + " " + $scope.searchResultList[index].bunji;
			if ($scope.searchResultList[index].ho != 0 && $scope.searchResultList[index].ho !='') {
				clickAddress += "-"+ $scope.searchResultList[index].ho;
			}
		} else {
			clickAddress = $scope.searchResultList[index].sido +" "+ $scope.searchResultList[index].gungu ;
			clickAddress +=	" " + $scope.searchResultList[index].roadnm + " " + $scope.searchResultList[index].buildmn;
			if ($scope.searchResultList[index].buildsn != 0 && $scope.searchResultList[index].buildsn != '') {
				clickAddress += "-"+ $scope.searchResultList[index].buildsn + " (" + $scope.searchResultList[index].dong;
			}
			if ($scope.searchResultList[index].buildnm != 0 && $scope.searchResultList[index].buildnm !='') {
				clickAddress += ", " + $scope.searchResultList[index].buildnm + ")";
			} else {
				clickAddress += ")";
			}
											
		}
		$scope.search.clickAddress = clickAddress;
		
		// 초기화
		$scope.search.detailAddress = "";
		$scope.search.selectRoadAddr = "";
		$scope.search.selectJibunAddr = "";
		$scope.searchSelect = false;
	}
	
	
	// 주소 선택 필드
	this.selectAddress = function() {
		
		if ($scope.search.detailAddress == '' || $scope.search.detailAddress == null) {
			alert("상세주소를 입력해주세요.");
			return;
		}
		
		var data = $scope.searchResultList[$scope.search.clickAddressIndex];
		var road = $scope.search.clickPostNo  + " " + data.sido + " " + data.gungu + " " + data.roadnm + " "+ data.buildmn 
					if (data.buildsn != 0 && data.buildsn != '') {
						road += "-" +data.buildsn
					}
					road += " " + $scope.search.detailAddress + " (" + data.dong;
					if (data.buildnm != '') {
						road += ", " + data.buildnm + ")";
					} else {
						road += ")";
					}
		var jibun = $scope.search.clickPostNo  + " " + data.sido + " " + data.gungu + " " + data.dong 
					+ " "+ data.bunji;
					if (data.ho != 0 && data.ho != '') {
						jibun += "-" +data.ho 
					}
					jibun += " " + $scope.search.detailAddress;
		
		$scope.search.selectRoadAddrText = road; 
		$scope.search.selectRoadAddr1 =  data.sido + " " + data.gungu + " " + data.roadnm; 
		var roadStr =  data.buildmn + "-" + data.buildsn + " " + $scope.search.detailAddress + " (" + data.dong;
										if (data.buildnm != '') {
											roadStr += ", " + data.buildnm + ")";
										} else {
											roadStr += ")";
										}
		$scope.search.selectRoadAddr2 = roadStr;
		
		$scope.search.selectJibunAddrText = jibun;
		$scope.search.selectJibunAddr1 = data.sido + " " + data.gungu + " " + data.dong;
		$scope.search.selectJibunAddr2 = data.bunji + "-" +data.ho + " " + $scope.search.detailAddress;
		
		$scope.searchSelect = true;
	}
	
	// 닫기
	this.close = function(){
		window.close();
	}
	
	// 주소 최종 선택
	this.confirm = function(param) {
		var data = {};
		data.postNo = $scope.search.clickPostNo;
		
		if (param == 'jibun') {
			data.address1 = $scope.search.selectJibunAddr1;
			data.address2 = $scope.search.selectJibunAddr2;
		} else {
			data.address1 = $scope.search.selectRoadAddr1;
			data.address2 = $scope.search.selectRoadAddr2;
		}
		
		console.log(data);
		
		$window.opener.$scope[$window.opener.$scope.searchParam.callback](data);
		window.close();
	}
	
	this.moveTab = function(param) {
		if (param == 'jibun') {
			$scope.jibunTab = true;
			$scope.roadTab = false;
			
			$scope.ctrl.reset();
		} else {
			$scope.jibunTab = false;
			$scope.roadTab = true;
			
			$scope.ctrl.reset();
		}
	}
	
	// 초기화
	this.reset = function() {
		$scope.districtList = [];
		$scope.sigunguList = [];
		$scope.search.searchKeyword ='';
		$scope.searchSelect = false;
		$scope.searchResult = false;
		$scope.detailInput = false;
		
		// 시/도 재조회
		$scope.ctrl.districtList();
		
	}
});