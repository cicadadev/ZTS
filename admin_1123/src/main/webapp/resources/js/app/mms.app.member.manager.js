
var memberManagerApp = angular.module("memberManagerApp", ['commonServiceModule', 'mmsServiceModule', 'spsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

memberManagerApp.controller("mms_memberManagerApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService) {
	
$scope.memberSearch = {};
	$window.$scope = $scope;
	if(!common.isEmpty($("#pageId").val())){
		$scope.pageId = $("#pageId").val();
		console.log($scope.pageId);
	}
	var	columnDefs = [
	   	              	{ field: 'memberNo'					, width: '100'	, colKey: "c.mmsMember.memberNo"					, linkFunction:'memberDetail'   },
	   	              	{ field: 'mmsMember.memberName'		, width: '100'	, colKey: "c.mmsCustomer.customer.name"				, userFilter :'mmsMember.memberId,mmsMember.memberName' , linkFunction:'memberDetail'   },
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
			            { field: 'mmsMember.regDt'			, width: '100'	, colKey: "c.grid.column.insDt"														},
			            { field: 'updDt'					, width: '100'	, colKey: "c.grid.column.updDt"														},
			            { field: 'updId'					, width: '100'	, colKey: "c.grid.column.updId"						, userFilter :'updId,updName'	}
			         ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_member",			//mandatory
			url :  '/api/mms/member',  			//mandatory
			searchKey : "memberSearch",        	//mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
				
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'memberSearch');
		
		$scope.memberSearch.memState = "MEM_STATE_CD.REG";
		$scope.memberSearch.memStateCds = "'MEM_STATE_CD.REG'";
		$scope.memberSearch.blacklistN = true;
	});
	
	// 검색조건 초기화
	$scope.reset = function() {
		commonService.reset_search($scope, 'memberSearch');
		angular.element(".day_group").find('button:first').addClass("on");
		
		$scope.memberSearch.memState = "MEM_STATE_CD.REG";
		$scope.memberSearch.memStateCds = "'MEM_STATE_CD.REG'";
		$scope.memberSearch.blacklistN = true;
	}
	
	$scope.infoType = [
	                    {val : 'USERID',text : '회원ID'},
	                    {val : 'NAME',text : '회원명'}
		              ];
	
	$scope.phoneType = [
						 {val : 'TEL',text : '전화번호'},
						 {val : 'CELL',text : '휴대폰번호'}
	                   ];
	
	$scope.memberDetail = function(field, row) {
		$scope.memberNo = row.memberNo;
		
		var winName='회원상세';
		var winURL = Rest.context.path +"/mms/member/popup/detail";
		popupwindow(winURL,winName,1200,1000);
	}
	
	this.blacklistYnCheck = function(param) {
		if(angular.isDefined(param)) {
			if ($scope.memberSearch.blacklistYn) {
				$scope.memberSearch.blacklistY = true;
				$scope.memberSearch.blacklistN = true;
			} else {
				$scope.memberSearch.blacklistY = false;
				$scope.memberSearch.blacklistN = false;
			}
		} else {
			if ($scope.memberSearch.blacklistY && $scope.memberSearch.blacklistN) {
				$scope.memberSearch.blacklistYn = true;
			} else {
				$scope.memberSearch.blacklistYn = false;
			}
		}
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
	
// 매일 포인트 내역
}).controller("mms_memberPointPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	var	columnDefs = [
	   	              { field: 'pointNo'		, width: '100'	, colKey: "mmsPoint.pointNo"			, enableCellEdit:false			},
	   	              	{ field: 'mmsPoint.pointTypeCd'		, width: '100'	, colKey: "mmsPoint.pointTypeCd"			, enableCellEdit:false			},
	   	              	{ field: 'point'		, width: '100'	, colKey: "mmsPoint.point"			, enableCellEdit:false			},
			            { field: 'insDt'			, width: '100'	, colKey: "c.grid.column.insDt"		, enableCellEdit:false									},
			            { field: 'insId'			, width: '100'	, colKey: "c.grid.column.insId"		, enableCellEdit:false		, userFilter :'insId,insName'},
			            { field: 'updDt'			, width: '100'	, colKey: "c.grid.column.updDt"		, enableCellEdit:false									},
			            { field: 'updId'			, width: '100'	, colKey: "c.grid.column.updId"		, enableCellEdit:false		, userFilter :'updId,updName'}
			         ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_memberPoint",			//mandatory
			url :  '/api/mms/member/point',  		//mandatory
			searchKey : "search",			        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		window.resizeTo(1200, 720);
		$scope.memberPointTab = true;
		$scope.search.memberNo = pScope.memberNo;
	});
	
	
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/detail";
		} else if ("carrot" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/carrot";
		} else if ("coupon" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/coupon";
		} else if ("deposit" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/deposit";
		} else if ("onlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/onlineOrder";
		} else if ("offlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/offlineOrder";
		}
	}

// 당근 내역
}).controller("mms_memberCarrotPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, mmsService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}
//	window.parent.document.getElementById('pageId')
	var	columnDefs = [
	   	              	{ field: 'carrotTypeName', colKey: "c.mmsCarrot.carrotType"		},
	   	              	{ field: 'carrot'		 , colKey: "c.sps.carrot.adjust2"		},
			            { field: 'note'			 , colKey: "c.mmsCarrot.note"			},
			            { field: 'balanceAmt'	 , colKey: "c.mmsCarrot.balanceAmt"		},
			            { field: 'insDt'		 , colKey: "c.grid.column.insDt"		},
			            { field: 'insId'		 , colKey: "c.grid.column.insId"		, userFilter :'insId,insName'},
			            { field: 'updDt'		 , colKey: "c.grid.column.updDt"		},
			            { field: 'updId'		 , colKey: "c.grid.column.updId"		, userFilter :'updId,updName'}
			         ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_memberCarrot",			//mandatory
			url :  '/api/mms/member/carrot',  		//mandatory
			searchKey : "search",			        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
		window.resizeTo(1200, 700);
		$scope.search.memberNo = pScope.memberNo;
		$scope.memberCarrotTab = true;
		$scope.myGrid.loadGridData();
		
		mmsService.carrotBalanceAmt($scope.search, function(data) {
			$scope.latestBalanceAmt = data.content;
		}); 
	});
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'search');
	}
	
	// 팝업 닫기
	this.close = function(){
		$window.close();
	}
	
	// 당근 조정 팝업
	$scope.carrotAdjustment = function() {
		$scope.memberNo = pScope.memberNo;
		var winURL = Rest.context.path +"/mms/member/popup/carrotAdjustment";
		popupwindow(winURL,"당근조정",850,300);
	}
	
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/detail";
		} else if ("point" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/point";
		} else if ("coupon" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/coupon";
		} else if ("deposit" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/deposit";
		} else if ("onlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/onlineOrder";
		} else if ("offlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/offlineOrder";
		}
	}
// 쿠폰 내역
}).controller("mms_memberCouponPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, mmsService, couponService) {
	var cellTemplate = "<div class=\"ui-grid-cell-contents\" title=\"\" style=\"text-align: center;\">";
		cellTemplate += '<a href="javascript:void(0);" ng-click="grid.appScope.popup.order(row.entity)" style="text-decoration: underline">';
		cellTemplate += '{{row.entity.orderId }}</a></div>';
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}
	
	var	columnDefs = [
	   	               	 { field: 'couponId'			, colKey:"c.sps.coupon.coupon.id"			, width:'150'	, linkFunction:"couponDetail"	},
			             { field: 'couponName'			, colKey:"spsCoupon.name" 					, width:'200'	, linkFunction:"couponDetail"	},
			             { field: 'couponTypeName'		, colKey:"c.sps.coupon.pop.coupon.type" 	, width:'150'									},
			             { field: 'dcValue'				, colKey:"spsCoupon.dcValue"		 		, width:'100'									},
			             { field: 'privateCin'			, colKey:"c.sps.coupon.pop.coupon.issue.type.cd"		 		, width:'100'									},
			             { field: 'regDt'				, colKey:"c.sps.coupon.issue.date"		 	, width:'150'									},
			             { field: 'useStartDt'			, colKey:"spsCouponissue.useStartDt"		, width:'150'									},
			             { field: 'useEndDt'			, colKey:"spsCouponissue.useEndDt"			, width:'150'									},
			             { field: 'couponIssueStateName', colKey:"spsCouponissue.couponIssueStateCd", width:'100'									},
			             { field: 'useDt'				, colKey:"spsCouponissue.useDt"				, width:'150'									},
			             { field: 'orderId'				, colKey:"omsOrdercoupon.orderId"			, width:'150'	, cellTemplate :cellTemplate	},
			            
	   	              ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_memberCoupon",			//mandatory
			url :  '/api/mms/member/coupon',  		//mandatory
			searchKey : "search",			        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		window.resizeTo(1200, 700);
		$scope.memberCouponTab = true;
		$scope.myGrid.loadGridData();
		$scope.search.memberNo = pScope.memberNo;
		
	});
	
	// 총쿠폰수, 사용가능 쿠폰수 조회
	$scope.getCouponCnt = function() {
		mmsService.getMemberCouponCnt(pScope.memberNo, function(data) {
			$scope.totalCount = data.totalCount;
			$scope.noUseCount = data.noUseCount;
		});
	}
	
	// 검색조건 초기화
	$scope.reset = function() {
		commonService.reset_search($scope, 'search');
	}
	
	// 쿠폰 사용 중지
	this.couponUseStop = function() {
		couponService.stopIssueCoupon($scope.myGrid.getSelectedRows(), function() {
			alert("수정되었습니다.");
			$scope.myGrid.loadGridData();
			$scope.getCouponCnt();
		});
	}
	
	// 쿠폰 수동 발급
	this.manualIssueCoupon = function() {
		commonPopupService.couponPopup($scope, "callback_coupon", false);
	}
	
	// 쿠폰 팝업 callBack
	$scope.callback_coupon = function(data) {
		data[0].memberNo = pScope.memberNo;
		mmsService.checkIssueCoupon(data[0], function(response) {
			console.log("response", response);
			if ("success" == response.content) {
				alert("발급 되었습니다.")
			} else {
				alert(response.content);
			}
			$scope.myGrid.loadGridData();
			$scope.getCouponCnt();
		});
	}
		
	
	// 팝업 닫기
	this.close = function(){
		$window.close();
	}
	
	// 쿠폰 상세 팝업
	$scope.couponDetail = function(field, row) {
		$scope.couponId = row.couponId;
		var url= Rest.context.path + '/sps/coupon/popup/detail';
		popupwindow(url,"couponDetail",1100,600);
	}
	
	$scope.popup = {
			order : function(row) { // linkfunction
				console.log(row);
				if (row.orderId != null) {
					$scope.orderId = row.orderId;
					popupwindow('/oms/order/popup/detail', '주문상세', 1100, 600);
				}
			}
		}
	
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/detail";
		} else if ("point" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/point";
		} else if ("carrot" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/carrot";
		} else if ("deposit" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/deposit";
		} else if ("onlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/onlineOrder";
		} else if ("offlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/offlineOrder";
		}
	}
}).controller("mms_memberDepositPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, mmsService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	var cellTemplate = "<div class=\"ui-grid-cell-contents\" title=\"\" style=\"text-align: center;\">";
		cellTemplate += '<a href="javascript:void(0);" ng-click="grid.appScope.popup.order(row.entity)" style="text-decoration: underline">';
		cellTemplate += '{{row.entity.orderId}}</a></div>';
	
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}

		
	var	columnDefs = [
	   	              { field: 'depositTypeName'	, width: '12%'	, colKey: "c.oms.deposit.type"										},
	   	              { field: 'depositAmt'			, width: '10%'	, colKey: "c.oms.deposit.adjust2"									},
	   	              { field: 'balanceAmt'			, width: '10%'	, colKey: "c.mmsDeposit.balanceAmt"									},
	   	              { field: 'insDt'				, width: '12%'	, colKey: "c.oms.deposit.reg.date"									},
	   	              { field: 'note'				, width: '10%'	, colKey: "c.mmsDeposit.note"										},
	   	              { field: 'orderId'			, width: '10%'	, colKey: "c.oms.order.id"			, cellTemplate :cellTemplate	},
	   	              { field: 'claimId'			, width: '10%'	, colKey: "c.oms.claim.id"			, cellTemplate :cellTemplate	},
	   	              { field: 'updDt'				, width: '12%'	, colKey: "c.grid.column.updDt"										},
	   	              { field: 'updId'				, width: '10%'	, colKey: "c.grid.column.updId"										}
	   	              ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_memberDeposit",			//mandatory
			url :  '/api/mms/member/deposit',  		//mandatory
			searchKey : "search",			        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		window.resizeTo(1200, 700);
		$scope.memberDepositTab = true;
		commonService.init_search($scope,'search');
		$scope.search.memberNo = pScope.memberNo;
		$scope.myGrid.loadGridData();
		
		mmsService.depositBalanceAmt($scope.search, function(data) {
			$scope.depositBalanceAmt = data.content;
		});
	});
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'search');
	}
	
	// 팝업 닫기
	this.close = function(){
		$window.close();
	}
	
	// 예치금 조정 팝업
	this.depositAdjustment = function() {
		$scope.memberNo = pScope.memberNo;
		popupwindow('/mms/member/popup/depositAdjustment', '예치금조정', 850, 300);
	}
	
	$scope.popup = {
			order : function(row) { // linkfunction
				console.log(row);
				if (row.orderId != null) {
					$scope.orderId = row.orderId;
					popupwindow('/oms/order/popup/detail', '주문상세', 1100, 600);
				}
			}
		}
	
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/detail";
		} else if ("point" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/point";
		} else if ("carrot" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/carrot";
		} else if ("coupon" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/coupon";
		} else if ("onlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/onlineOrder";
		} else if ("offlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/offlineOrder";
		}
	}
}).controller("mms_memberOnlineOrderPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}
	console.log($scope.pageId);
	var template = {
			memo : function() {
				var retHtml = '';
				retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
				retHtml += '<a href="javascript:void(0);" ng-click="grid.appScope.popup.memo(row.entity)"  ng-class="{underline:true,hasmemo:row.entity.memoCnt > 0}">';
				retHtml += '메모</a></div>';
				return retHtml;
				}
			};
	
	var	columnDefs = [
	   	              { field: 'orderId'				, width: '100'	, colKey: "c.oms.order.id"				, enableCellEdit:false		, linkFunction : 'popup.order'  },
	   	              { field: 'orderDt'				, width: '100'	, colKey: "c.oms.order.datetime"		, enableCellEdit:false										},
	   	              { field: 'orderTypeName'			, width: '100'	, colKey: "c.oms.order.type"			, enableCellEdit:false										},
	   	              { field: 'siteId'					, width: '100'	, colKey: "c.oms.order.site"			, enableCellEdit:false										},
	   	              { field: 'name1'					, width: '100'	, colKey: "c.oms.order.name"			, enableCellEdit:false										},
	   	              { field: 'phone1'					, width: '100'	, colKey: "c.oms.order.tel"				, enableCellEdit:false										},
	   	              { field: 'phone2'					, width: '100'	, colKey: "c.oms.order.mobile"			, enableCellEdit:false										},
	   	              { field: 'receiver'				, width: '100'	, colKey: "c.oms.delivery.name"			, enableCellEdit:false										},
	   	              { field: 'receiverMobile'			, width: '100'	, colKey: "c.oms.delivery.mobile"		, enableCellEdit:false										},
	   	              { field: 'orderStateName'			, width: '100'	, colKey: "c.oms.order.status"			, enableCellEdit:false										},
	   	              { field: 'orderDeliveryStateName'	, width: '100'	, colKey: "c.oms.delivery.status"		, enableCellEdit:false										},
	   	              { field: 'orderAmt'				, width: '100'	, colKey: "c.oms.order.amount"			, enableCellEdit:false										},
	   	              { field: 'dcAmt'			, width: '100'	, colKey: "c.oms.order.discount"		, enableCellEdit:false										},
	   	              { field: 'deliveryAmt'			, width: '100'	, colKey: "c.oms.delivery.amount"		, enableCellEdit:false										},
	   	              { field: 'wrappingAmt'			, width: '100'	, colKey: "c.oms.delivery.wrapping"		, enableCellEdit:false										},
	   	              { field: 'paymentAmt'				, width: '100'	, colKey: "c.oms.payment.amount"		, enableCellEdit:false										},
	   	              { field: 'orderQty'				, width: '100'	, colKey: "c.oms.order.qty"				, enableCellEdit:false										},
	   	              { field: 'returnQty'				, width: '100'	, colKey: "c.oms.order.return_qty"		, enableCellEdit:false										},
	   	              { field: 'exchangeQty'			, width: '100'	, colKey: "c.oms.order.exchange_qty"	, enableCellEdit:false										},
	   	              { field: 'deviceTypeName'			, width: '100'	, colKey: "c.oms.order.device"			, enableCellEdit:false										},
	   	              { field: 'storeId'				, width: '100'	, colKey: "c.oms.payment.pg_shop_id"	, enableCellEdit:false										},
	   	              { field: 'memo'					, width: '100'	, colKey: "c.oms.order.memo"			, enableCellEdit:false		, cellTemplate : template.memo	}
	   	              
	   	              ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_memberOnlineOrder",			//mandatory
			url :  '/api/mms/member/onlineOrder',  		//mandatory
			searchKey : "search",			        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		window.resizeTo(1200, 720);
		$scope.memberOnlineOrderTab = true;
		$scope.search.memberNo = pScope.memberNo;
	});
	
	// 검색조건 초기화
	$scope.reset = function() {
		commonService.reset_search($scope, 'search');
	}
	
	this.close = function(){
		$window.close();
	}
	
	$scope.popup = {
			order : function(field, entity) { // linkfunction
				$scope.orderId = entity.orderId;
				popupwindow('/oms/order/popup/detail', '주문상세', 1100, 600);
			},
			memo : function(entity) { // template
				$scope.orderId = entity.orderId;
				$scope.orderProductNo = 1;
				popupwindow('/oms/order/popup/memo', '주문메모', 1100, 400);
			}
		}
	
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/detail";
		} else if ("point" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/point";
		} else if ("carrot" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/carrot";
		} else if ("coupon" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/coupon";
		} else if ("deposit" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/deposit";
		} else if ("offlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/offlineOrder";
		}
	}
	
}).controller("mms_memberOfflineOrderPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	var	columnDefs = [
	   	              { field: 'orderId'		, colKey: "omsPosorder.orderId"		, linkFunction : "offOrderDetail"		},
	   	              { field: 'offshopId'		, colKey: "omsPosorder.offshopId"			},
	   	              { field: 'offshopName'	, colKey: "c.ccs.offShop.name"				},
	   	              { field: 'orderDt'		, colKey: "omsPosorder.orderDt"				},
	   	              { field: 'orderAmt'		, colKey: "omsPosorder.orderAmt"			},
	   	              ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_memberOfflineOrder",			//mandatory
			url :  '/api/mms/member/offlineOrder',  		//mandatory
			searchKey : "search",			        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	
	angular.element(document).ready(function () {
		window.resizeTo(1200, 720);
		$scope.memberOfflineOrderTab = true;
		$scope.search.memberNo = pScope.memberNo;
	});
	
	$scope.offOrderDetail = function(field, row) {
		$scope.offOrderId = row.orderId;
		var url= Rest.context.path + '/mms/member/popup/OffOrderDetail';
		popupwindow(url,"offOrderDetail",900,300);
	}
	
	this.close = function(){
		$window.close();
	}
	
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/detail";
		} else if ("point" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/point";
		} else if ("carrot" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/carrot";
		} else if ("coupon" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/coupon";
		} else if ("deposit" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/deposit";
		} else if ("onlineOrder" == param) {
			$window.location.href = Rest.context.path +"/mms/member/popup/onlineOrder";
		}
	}
	
// 임시 비밀번호 팝업	
}).controller("mms_memberTempPwdPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$scope.tempPwd = {};

	angular.element(document).ready(function () {
		$scope.tempPwd.userid = pScope.mmsMemberZts.mmsMember.userid;
		$scope.tempPwd.name = pScope.mmsMemberZts.mmsMember.mmsCustomer.customername;
		$scope.tempPwd.cellno = pScope.mmsMemberZts.mmsMember.cellno;
	});
	
	// 임시비밀번호 팝업 닫기
	this.close = function(){
		$window.close();
	}

	// 회원 등급 변경 이력 팝업
}).controller("mms_memberGradeHistory_controller", function($window, $scope, $filter, commonService, mmsService, commonPopupService) {	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$scope.mmsMemberZtsHistorys = {};
	angular.element(document).ready(function () {
		var memberNo = pScope.memberNo; 
		mmsService.getMemberGradeHistory(memberNo, function(data) {
			$scope.mmsMemberZtsHistorys = data;
		}); 
	});
	
	// 회원 등급 변경 이력 팝업 닫기
	this.close = function() {
		$window.close();
	}

// SMS 수신 변경 팝업
}).controller("mms_changeSmsReceivePopApp_controller", function($window, $scope, $filter, commonService, mmsService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	
	$scope.smsReceipt = {};

	angular.element(document).ready(function () {
		$scope.smsReceipt.memberId = pScope.mmsMemberZts.mmsMember.memberId;
		$scope.smsReceipt.name = pScope.mmsMemberZts.mmsMember.memberName;
		$scope.smsReceipt.smsYn = pScope.mmsMemberZts.mmsMember.smsYn;
		$scope.smsReceipt.memberNo = pScope.mmsMemberZts.mmsMember.memberNo;
	});
	
	// SMS 수신 변경 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
	// sms 수신 변경
	this.updateReceipt = function() {
		console.log($scope.smsReceipt);
		mmsService.updateMarketingReceipt($scope.smsReceipt, function() {
			alert("저장되었습니다.");
			pScope.mmsMemberZts.mmsMember.smsYn = $scope.smsReceipt.smsYn;
			pScope.$apply();
		});
	}

// EMAIL 수신 변경 팝업
}).controller("mms_changeEmailReceivePopApp_controller", function($window, $scope, $filter, commonService, mmsService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	
	$scope.emailReceipt = {};

	angular.element(document).ready(function () {
		$scope.emailReceipt.memberId = pScope.mmsMemberZts.mmsMember.memberId;
		$scope.emailReceipt.name = pScope.mmsMemberZts.mmsMember.memberName;
		$scope.emailReceipt.emailYn = pScope.mmsMemberZts.mmsMember.emailYn;
		$scope.emailReceipt.memberNo = pScope.mmsMemberZts.mmsMember.memberNo;
	});
	
	// EMAIL 수신 변경 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
	// email 수신 변경
	this.updateReceipt = function() {
		console.log($scope.emailReceipt);
		mmsService.updateMarketingReceipt($scope.emailReceipt, function() {
			alert("저장되었습니다.");
			pScope.mmsMemberZts.mmsMember.emailYn = $scope.emailReceipt.emailYn;
			pScope.$apply();
		});
	}
	
	
}).controller("mms_changeAppPushReceivePopApp_controller", function($window, $scope, $filter, commonService, mmsService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$scope.appPushReceipt = {};

	angular.element(document).ready(function () {
		$scope.appPushReceipt.memberId = pScope.mmsMemberZts.mmsMember.memberId;
		$scope.appPushReceipt.name = pScope.mmsMemberZts.mmsMember.memberName;
		$scope.appPushReceipt.appPushYn = pScope.mmsMemberZts.mmsMember.appPushYn;
		$scope.appPushReceipt.memberNo = pScope.mmsMemberZts.mmsMember.memberNo;
	});
	
	// EMAIL 수신 변경 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
	// appPush 수신 변경
	this.updateReceipt = function() {
		console.log($scope.appPushReceipt);
		mmsService.updateMarketingReceipt($scope.appPushReceipt, function() {
			alert("저장되었습니다.");
			pScope.mmsMemberZts.mmsMember.appPushYn = $scope.appPushReceipt.appPushYn;
			pScope.$apply();
		});
	}
}).controller("mms_depostAdjustmentPopApp_controller", function($window, $scope, $filter, commonService, mmsService, commonPopupService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	// 예치금 저장
	this.saveDeposit = function() {
		$scope.mmsDeposit.memberNo = pScope.memberNo;
		mmsService.saveMemberDeposit($scope.mmsDeposit, function(response) {
			if(response.content == "success") {
				alert("저장되었습니다.");
				pScope.myGrid.loadGridData();
				$window.close();
			} else {
				alert(res.content);
			}
		});
	}
	
	// 팝업 닫기
	this.close = function(){
		$window.close();
	}
}).controller("mms_carrotAdjustmentPopApp_controller", function($window, $scope, $filter, commonService, mmsService, commonPopupService, carrotService) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}
	console.log($scope.pageId);
	$scope.mmsCarrot = {};
	
	// 팝업 닫기
	this.close = function(){
		$window.close();
	}
	
	// 당근 저장
	this.saveCarrot = function() {
		$scope.mmsCarrot.memberNo = pScope.memberNo;
		
		carrotService.saveCarrot($scope.mmsCarrot, function(response) {
			if(response.content == "success") {
				alert("저장되었습니다.");
				pScope.myGrid.loadGridData();
				$window.close();
			} else {
				alert(res.content);
			}
		});
	}
	
}).controller("mms_offOrderDetailPopApp_controller", function($window, $scope, $filter, commonService, mmsService, commonPopupService, carrotService) {	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}
	$scope.search={};
	$scope.omsPosorderproduct = {};
	angular.element(document).ready(function () {
		// TODO : 오프라인 구매 상품 내역 조회
		$scope.search['orderId'] = pScope.offOrderId;
		mmsService.getMemberOffOrderProductList($scope.search, function(data) {
			$scope.omsPosorderproduct = data;
		});
	});
	
	// 팝업 닫기
	this.close = function(){
		$window.close();
	}
});
