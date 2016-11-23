var eventApp = angular.module("eventApp", [	'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'spsServiceModule',
                                           	'ui.date', 'ngCkeditor' ]);
//메시지
Constants.message_keys = ["sps.event.register.already", "sps.event.select.brand", "sps.event.select.coupon", "sps.event.need.prize",
                          "sps.event.register.new", "common.label.confirm.save", "sps.event.select.gift",
                          "sps.event.delete.validation", "sps.event.img1.register.validation", "sps.event.img2.register.validation",
                          "sps.event.html.register.validation", "sps.event.changeType.msg.coupon", "sps.event.changeType.msg.attend", 
                          "sps.event.changeType.msg.exp", "sps.event.changeType.msg.manual", "sps.event.change.state.msg",
                          "sps.event.prize.coupon.validation", "sps.event.product.validation", "common.label.alert.save",
                          "sps.event.winNoticeDate.validation1", "sps.event.winNoticeDate.validation2",
                          "sps.event.joinDate.validation", "sps.event.commentDate.validation", "sps.event.joinDate.validation2"];

eventApp.controller("sps_eventListApp_controller", function($window, $scope, $filter, commonService, gridService, eventService) {
	
	// 부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	// 객체 생성
	$scope.search = {};

	if(!common.isEmpty($("#pageId").val())){
		$scope.pageId = $("#pageId").val();
		console.log($scope.pageId);
	}

	
	angular.element(document).ready(function () {		
		commonService.init_search($scope, 'search');
		$scope.search.searchKeyword = "name";
	});

	var	columnDefs = [
         { field: 'eventId', colKey: 'c.sps.event.eventId', linkFunction: "openEventDetail", type: 'number' },
         { field: 'name', colKey: 'c.sps.event.name', linkFunction: "openEventDetail" },
         { field: 'eventTypeName', colKey: 'c.sps.event.eventType' },
         { field: 'eventDivName', colKey: 'c.sps.event.eventDiv' },
         { field: 'displayYn', colKey: 'spsEvent.displayYn', cellFilter:'displayYnFilter' },
         { field: 'eventStartDt', colKey: 'spsEvent.eventStartDt', cellFilter: "date:\'yyyy-MM-dd\'" },
         { field: 'eventEndDt', colKey: 'spsEvent.eventEndDt', cellFilter: "date:\'yyyy-MM-dd\'" },
         { field: 'eventStateName', colKey: 'c.sps.event.eventState' },
         { field: 'winNoticeDate', colKey: 'c.sps.event.winNoticeDate', cellFilter: "date:\'yyyymmdd\'" },
         { field: 'insId', colKey: 'c.grid.column.insId' , userFilter :'insId,insName'},
         { field: 'insDt', colKey: 'c.grid.column.insDt' },
         { field: 'updId', colKey: 'c.grid.column.updId' , userFilter :'updId,updName'},
         { field: 'updDt', colKey: 'c.grid.column.updDt' }	   	              
	];

	var gridParam = {
			scope : $scope,
			gridName : "grid_event",
			url :  '/api/sps/event',
			searchKey : "search",
			columnDefs : columnDefs,
			gridOptions : {
			},
			callbackFn : function() {
			}
	};
	// 그리드 객체생성
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	// message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	this.reset = function() {
		commonService.reset_search($scope, 'search');
		
		angular.element(".day_group").find('button:first').addClass("on");
		$scope.search.searchKeyword = "name";
	}
	
	// 이벤트 검색
	this.getEventList = function() {
		if ($scope.search.searchKeyword == 'eventId') {
			var eventIds = $scope.search.event;
			
			if (eventIds != null && eventIds != '') {
				$scope.search.eventIds = '';
				
				angular.forEach(eventIds.split(','), function(eventId) {
					$scope.search.eventIds += "'"+eventId+"',";
				});
				
				eventIds = $scope.search.eventIds;
				$scope.search.eventIds = eventIds.substring(0, eventIds.length-1);
			} else {
				$scope.search.eventIds = '';
			}
		} else if ($scope.search.searchKeyword == 'name') {
			$scope.search.name = $scope.search.event;
		}
		$scope.myGrid.loadGridData();
	}

	// Grid: event ID 선택 호출
	$scope.openEventDetail = function(fieldValue, rowEntity) {
		$scope.eventId = rowEntity.eventId;
		$scope.eventTypeCd = rowEntity.eventTypeCd;
		
		var url = Rest.context.path + '/sps/event/popup/detail';
		popupwindow(url, "이벤트 기본정보", 1200, 600);
	}

	// 등록 팝업 호출
	this.insertEventPopup = function() {
		$scope.eventId = "";
		var url = Rest.context.path + '/sps/event/popup/detail';
		popupwindow(url, "이벤트 등록", 1200, 600);
	}
	
	// 이벤트 삭제
	this.deleteGridData = function() {
		var checkEventState = true;
		
		var selectRows = $scope.myGrid.getSelectedRows();
		angular.forEach(selectRows, function(row) {
			if (checkEventState && row.eventStateCd != 'EVENT_STATE_CD.READY') {	// alert 한번만 띄우기 위해.
				alert($scope.MESSAGES["sps.event.delete.validation"]);
				checkEventState = false;
			}
		});
		
		if (checkEventState) {
			$scope.myGrid.deleteGridData();
		}	
	}
	
}).controller("sps_eventDetailPopApp_controller", function($window, $scope, $compile, $filter, 
		commonService, commonPopupService, gridService, eventService) { //이벤트상세 PopUp Controller

	//현재 팝업에서 부모 scope 접근하기 위한 변수 설정
	pScope = $window.opener.$scope;
	
	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}
	
	//tab 이동
	this.moveTab = function($event, param) {
		if ("joinlist" == param) {
			 $window.location.href = Rest.context.path + "/sps/event/popup/joinList";
		} 
	}

	//현재 팝업 정보 설정: 제한유형 팝업에서 사용
	$window.$scope = $scope;

	//이벤트 상세정보 변수
	$scope.spsEvent = {}; //이벤트
	$scope.spsEvent.spsEventbrands = []; //이벤트 브랜드
	$scope.spsEvent.spsEventprizes = []; //이벤트 혜택

	//CK Editor 설정
	$scope.ckPcOption = {
		language : 'ko',								//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}

	$scope.ckMobileOption = {
		language : 'ko',								//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}

	//쿠폰목록 Grid
	var	couponColumnDefs = [
         { field: 'couponId', colKey: "c.sps.event.couponId", vKey: "spsEventcoupon.couponId", validators: {required :true}, linkFunction: "couponDetail", type: 'number' },
         { field: 'spsCoupon.name', colKey: "c.sps.event.couponName", linkFunction: "couponDetail" },
         { field: 'spsCoupon.couponTypeName', colKey: "c.sps.coupon.pop.coupon.type" },
         { field: 'spsCoupon.dcValue', colKey: "c.sps.event.couponDc" },
         { field: 'spsCoupon.couponStateName', colKey: "c.sps.coupon.pop.coupon.state" },
         { field: 'spsCoupon.issueStartDt', colKey: "c.sps.event.issueStartDt", cellFilter: "date:\'yyyy-MM-dd\'" },
         { field: 'spsCoupon.issueEndDt', colKey: "c.sps.event.issueEndDt", cellFilter: "date:\'yyyy-MM-dd\'" },
 		 { field: 'insId', colKey: 'c.grid.column.insId' , userFilter :'insId,insName'},
 	     { field: 'insDt', colKey: 'c.grid.column.insDt' , cellFilter: "date:\'yyyy-MM-dd\'" },
 	     { field: 'updId', colKey: 'c.grid.column.updId' , userFilter :'updId,updName'},
 	     { field: 'updDt', colKey: 'c.grid.column.updDt' , cellFilter: "date:\'yyyy-MM-dd\'" }
     ];

	var gridParam = {
			scope : $scope,
			gridName : "grid_couponEvent",
			url :  '/api/sps/event/popup/coupon',
			searchKey : "spsEvent",
			columnDefs : couponColumnDefs,
			gridOptions : {
			},
			callbackFn : function() {
			}
	};
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	//경품정보 Grid
	var	giftColumnDefs = [
         { field: 'giftNo', colKey: "spsEventgift.giftNo", type: 'number' },
         { field: 'name', colKey: "spsEventgift.name", vKey: "spsEventgift.name", validators: {required :true}, enableCellEdit: true },
         { field: 'winnerNumber', colKey: "spsEventgift.winnerNumber", vKey: "spsEventgift.winnerNumber", validators: {required :true}, enableCellEdit: true, type: 'number' }
     ];

	var giftGridParam = {
			scope : $scope,
			gridName : "grid_eventGift",
			url :  '/api/sps/event/gift',
			searchKey : "spsEvent",
			columnDefs : giftColumnDefs,
			gridOptions : {
			},
			callbackFn : function() {
			}
	};
	$scope.eventGiftGrid = new gridService.NgGrid(giftGridParam);	
	

	//an array of files selected
    $scope.files = [];

    //listen for the file selected event
    $scope.$on("fileSelected", function (event, args) {
        $scope.$apply(function() {
            //add the file object to the scope's files collection
            $scope.files.push(args.file);
        });
    });

	this.detInit = function() {
		//이벤트 상세정보
		
		if (pScope.eventId != "" && pScope.eventId != undefined) {
			$scope.spsEvent.eventId = pScope.eventId;
			eventService.getEventDetail($scope.spsEvent, function(response) {
				$scope.spsEvent = response;
				if (response.eventStateCd != "EVENT_STATE_CD.READY") {
					angular.element(document.getElementsByName("eventDetailDiv")).find("input[type=text]").attr("readonly", true);
					angular.element(document.getElementsByName("eventRadio")).find("input[type=radio]").attr("readonly", true);	
					angular.element(document.getElementsByName("eventRadio")).find("input[type=radio]").attr("disabled", true);
				} else {
					angular.element(document.getElementsByName("eventDetailDiv")).find("input[type=text]").attr("readonly", false);
					angular.element(document.getElementsByName("eventRadio")).find("input[type=radio]").attr("readonly", false);
					angular.element(document.getElementsByName("eventRadio")).find("input[type=radio]").attr("disabled", false);
				}
				
				// 브랜드
				for (var i=0; i<$scope.spsEvent.spsEventbrands.length; i++) {
					var brand = $scope.spsEvent.spsEventbrands[i];
					if (i > 0) {
						$scope.ctrl.addBrand();
					}
					$scope.spsEvent["spsEventbrand"+i] = {
							brandId : brand.brandId,
							brandName : brand.brandName
					};			
				}
				
				//날짜값 setting
				$scope.spsEvent.eventStartDt = $filter('date')(response.eventStartDt, Constants.date_format_3);
				$scope.spsEvent.eventEndDt = $filter('date')(response.eventEndDt, Constants.date_format_3);
				$scope.spsEvent.eventJoinStartDt = $filter('date')(response.eventJoinStartDt, Constants.date_format_3);
				$scope.spsEvent.eventJoinEndDt = $filter('date')(response.eventJoinEndDt, Constants.date_format_3);
				$scope.spsEvent.winNoticeDate = $filter('date')(response.winNoticeDate, Constants.date_format_3);
				
				// 쿠폰이벤트일 경우 쿠폰 목록 조회, 수동관리 이벤트일 경우 경품 조회
				if ('EVENT_TYPE_CD.COUPON' == $scope.spsEvent.eventTypeCd) {
					$scope.myGrid.loadGridData();
				} else if ('EVENT_TYPE_CD.MANUAL' == $scope.spsEvent.eventTypeCd) {
					$scope.eventGiftGrid.loadGridData();
				}
				
				if ('EVENT_TYPE_CD.ATTEND' != $scope.spsEvent.eventTypeCd) {
					$scope.spsEvent.spsEventprizes[0].joinTypeCd = 'JOIN_TYPE_CD.DATE';
					$scope.spsEvent.spsEventprizes[0].prizeTypeCd = 'PRIZE_TYPE_CD.COUPON';
				} else {
					for (var i=0; i<$scope.spsEvent.spsEventprizes.length; i++) {
						$scope.spsEvent["spsEventprizes"+i] = $scope.spsEvent.spsEventprizes[i];
						$scope.spsEvent["spsEventprizes"+i].joinDate = $scope.spsEvent.spsEventprizes[i].joinEndDt;
					}
				}
				
				// 이벤트 타입 변경을 위해 현재 이벤트 유형 기억
				$scope.nowEventType = $scope.spsEvent.eventTypeCd;
			});
		}
	};

	//제한 유형 팝업
	this.restrictPopup = function() {
		commonPopupService.restrictPopup($scope, true, true, true, true);
	}

	//브랜드 검색 팝업
	this.brandSearch = function(index) {
		$scope.brandIndex = index;
		commonPopupService.brandPopup($scope, "callback_brand", false);
	}
	$scope.callback_brand = function(data) {
		var brandCnt = $scope.spsEvent.spsEventbrands.length;
		var dupYn = false;
		for (var i = 0; i < brandCnt; i++) {
			if ($scope.spsEvent.spsEventbrands[i].brandId == data[0].brandId) {
				dupYn = true;
	            break;
			}
		}
		if (dupYn) {
			alert(pScope.MESSAGE["sps.event.register.already"]);
            return;
		} else {
			var newEventbrand = "spsEventbrand" + $scope.brandIndex;
			$scope.spsEvent[newEventbrand] = {
					brandId : data[0].brandId,
					brandName : data[0].name
			};
			$scope.$apply();
			
			$scope.spsEvent.spsEventbrands.push({
				brandId : data[0].brandId,
				brandName : data[0].name
			});
		}
	}
	
	//브랜드 검색내용 지우기
	this.eraseBrand = function(index) {
		var newEventbrand = "spsEventbrand" + index;
		var deleteBrandId = $scope.spsEvent[newEventbrand].brandId;
		
		$scope.spsEvent[newEventbrand].brandId = null;
		$scope.spsEvent[newEventbrand].brandName = null;
		
		for (var i=0; i<$scope.spsEvent.spsEventbrands.length; i++) {
			if ($scope.spsEvent.spsEventbrands[i].brandId == deleteBrandId) {
				$scope.spsEvent.spsEventbrands[i] = {
					brandId : "",
					brandName : ""		
				}
			}
		}
	}
	
	// 브랜드 필드 삭제
	this.deleteBrand = function(index) {
		var deleteBrandId = "";
		
		var newEventbrand = "spsEventbrand" + index;
		if ($scope.spsEvent[newEventbrand] != null && $scope.spsEvent[newEventbrand] != undefined) {
			if ($scope.spsEvent[newEventbrand].brandId != "" && $scope.spsEvent[newEventbrand].brandId != undefined 
					&&$scope.spsEvent[newEventbrand].brandName != "" && $scope.spsEvent[newEventbrand].brandName != undefined) {
				if ($scope.spsEvent[newEventbrand] != null) {
					deleteBrandId = $scope.spsEvent[newEventbrand].brandId;
					delete $scope.spsEvent[newEventbrand];
				}
				
				for (var i=0; i<$scope.spsEvent.spsEventbrands.length; i++) {
					if ($scope.spsEvent.spsEventbrands[i].brandId == deleteBrandId) {
						$scope.spsEvent.spsEventbrands.splice(i, 1);
						angular.element(document.querySelector('#td_brand')).find('div').eq(i).remove();
					}
				}
			}
		}
		
	}
	
	// 브랜드 input 필드 추가
	this.addBrand = function() {
		var divCnt = angular.element(document.querySelector('#td_brand')).find('div').length;
		
		if ($scope.spsEvent["spsEventbrand"+(divCnt-1)] == undefined) {
			if ($scope.spsEvent.spsEventbrands.length > 0) {
				divCnt += 1;
			}
		}
		
		if ($scope.spsEvent["spsEventbrand"+(divCnt-1)] != null && $scope.spsEvent["spsEventbrand"+(divCnt-1)] != undefined) {
			if ($scope.spsEvent["spsEventbrand"+(divCnt-1)].brandId != "" && $scope.spsEvent["spsEventbrand"+(divCnt-1)].brandId != undefined 
					&&$scope.spsEvent["spsEventbrand"+(divCnt-1)].brandName != "" && $scope.spsEvent["spsEventbrand"+(divCnt-1)].brandName != undefined) {
				var strHtml = '<div><input type="text" ng-model="spsEvent.spsEventbrand'+divCnt+'.brandId" style="width:29%;" readonly disabled/>';
				strHtml += ' <input type="text" ng-model="spsEvent.spsEventbrand'+divCnt+'.brandName" style="width:30%;" readonly disabled/>'; 
				strHtml += ' <button type="button" class="btn_type2" ng-click="ctrl.brandSearch('+divCnt+')"><b>검색</b></button>';
				strHtml += ' <button type="button" class="btn_eraser" ng-class="{\'btn_eraser_disabled\' : !spsEvent.spsEventbrand'+divCnt+'.brandId}" ng-click="ctrl.eraseBrand('+divCnt+')">지우개</button>';
				strHtml += ' <button type="button" class="btn_minus" ng-click="ctrl.deleteBrand('+divCnt+')">삭제</button></div>';
				
			angular.element(document.querySelector('#td_brand')).append($compile(strHtml)($scope));
			}
		}	
	}
	
	//이미지 미리보기 삭제
	this.deletePreview = function(name) {
		commonService.deleteFile($scope.spsEvent[name], function(data) {
			$scope.spsEvent[name] = "";
	    });
		angular.element(document.querySelector('img[name='+ name+']')).attr("src", null);
		angular.element(document.querySelector('#' + name + 'Path')).val("");
	}

	//쿠폰 추가 팝업
	this.addCouponPopup = function(callType, index) {
		if ('eventcoupon' == callType) {
			commonPopupService.couponPopup($scope, "callback_coupon", false);
		} else if ('eventprize' == callType) {
			$scope.prizeCouponIndex = index;
			commonPopupService.couponPopup($scope, "callback_prizeCoupon", false);
		}
	}
	
	//쿠폰이벤트 쿠폰추가
	$scope.callback_coupon = function(data) {
		if (data[0].couponStateCd == "COUPON_STATE_CD.READY") {
			alert(pScope.MESSAGES["sps.event.prize.coupon.validation"]);
			return;
		}
		if ($scope.checkDupCoupon(data[0].couponId)) {
			alert(pScope.MESSAGE["sps.event.register.already"]);
			return;
		}
		var expDate = '';
		if ('TERM_TYPE_CD.DAYS' == data[0].termTypeCd) {  //일수
			expDate = data[0].termDays;
		} else { //기간
			var term_stdt = data[0].termStartDt;
			var term_eddt = data[0].termEndDt;
			expDate = term_stdt.substr(0,10) + " ~ " + term_eddt.substr(0,10);
		}
		$scope.myGrid.addRow({
			couponId: data[0].couponId,
			spsCoupon: { name: data[0].name,
						 couponTypeName: data[0].couponTypeName, 
						 dcValue: data[0].dcValue,
						 couponStateName: data[0].couponStateName,
						 issueStartDt: data[0].issueStartDt,
						 issueEndDt: data[0].issueEndDt },
			insDt: data[0].insDt,
			insId: data[0].insId,
			updDt: data[0].updDt,
			updId: data[0].updId
		});
	}
	
	//쿠폰 중복여부 체크
	$scope.checkDupCoupon = function(couponId) {
		var dupYn = false;
		var couponCnt = $scope.grid_couponEvent.data.length;
		for (var i=0; i<couponCnt; i++) {
			if ($scope.grid_couponEvent.data[i].couponId == couponId) {
				dupYn = true;
				break;
			}
		}
		return dupYn;
	}
	
	//쿠폰 그리드 or 상품 그리드 row 삭제
	this.deleteGridRows = function(gridDiv) {
		if (gridDiv == 'event') {
			$scope.myGrid.deleteRow();
		} else if (gridDiv == 'gift') {
			$scope.eventGiftGrid.deleteRow();
		}
	}
	
	//쿠폰 Grid: coupon ID 선택시 쿠폰 상세 출력
	$scope.couponDetail = function(fieldValue, rowEntity) {
		$scope.couponId = { couponId : rowEntity.couponId };
		
		var url = Rest.context.path + '/sps/coupon/popup/detail';
		popupwindow(url, "쿠폰 상세정보", 1100, 600);
	}

	//출석이벤트 쿠폰선택
	$scope.callback_prizeCoupon = function(data) {
		if (data[0].couponStateCd == "COUPON_STATE_CD.READY") {
			alert(pScope.MESSAGES["sps.event.prize.coupon.validation"]);
		} else {
			$scope.spsEvent["spsEventprizes"+$scope.prizeCouponIndex].couponId = data[0].couponId;
			$scope.spsEvent["spsEventprizes"+$scope.prizeCouponIndex].couponName = data[0].name;
			$scope.$apply();
		}
	}

	// 쿠폰 검색내용 지우기
	this.eraseCoupon = function(index) {
		$scope.spsEvent["spsEventprizes"+index].couponId = "";
		$scope.spsEvent["spsEventprizes"+index].couponName = "";
	}
	
	//출석이벤트 혜택추가
	this.addEventprize = function(){
		var divCnt = angular.element(document.querySelector('#td_prize')).find('table').length;
		
		var strHtml = '<div id="div_prize'+divCnt+'"><div class="btn_alignR marginT1">';
		strHtml += '<button type="button" class="btn_type1" ng-click="ctrl.deleteEventprize('+divCnt+')"><b>삭제</b></button></div>';
		strHtml += '<table class="tb_type1 marginT1" style="border-top:1px solid #cfcfcf">';
		strHtml += '<colgroup><col width="13%" /><col width="45%" /><col width="13%" /><col width="*" /></colgroup>';
		strHtml += '<tbody><tr><th>혜택조건  <i>필수입력</i></th>';
		strHtml += '<td><radio-list ng-model="spsEvent.spsEventprizes'+divCnt+'.joinTypeCd" code-group="JOIN_TYPE_CD" ></radio-list>';
		strHtml += '<div ng-if="spsEvent.spsEventprizes'+divCnt+'.joinTypeCd == \'JOIN_TYPE_CD.DATE\'" style="margin-top:10px;">';
		strHtml += '<input type="text" ng-model="spsEvent.spsEventprizes'+divCnt+'.joinDate" placeholder="" v-key="spsEvent.spsEventprizes'+divCnt+'.joinDate" required period-end datetime-picker /></div>';
		strHtml += '<div ng-if="spsEvent.spsEventprizes'+divCnt+'.joinTypeCd == \'JOIN_TYPE_CD.TERM\'" style="margin-top:10px;">';
		strHtml += '<input type="text" ng-model="spsEvent.spsEventprizes'+divCnt+'.joinStartDt" placeholder="" datetime-picker period-start required/>';
		strHtml += '~ <input type="text" ng-model="spsEvent.spsEventprizes'+divCnt+'.joinEndDt" placeholder="" datetime-picker period-end required/>';
		strHtml += '<p class="information" ng-show="spsEvent.spsEventprizes'+divCnt+'.joinStartDtInput == null || spsEvent.spsEventprizes'+divCnt+'.joinEndDtInput == null">필수 입력 항목 입니다.</p></div>';
		strHtml += '<div ng-if="spsEvent.spsEventprizes'+divCnt+'.joinTypeCd == \'JOIN_TYPE_CD.DAYS\' || spsEvent.spsEventprizes'+divCnt+'.joinTypeCd == \'JOIN_TYPE_CD.JOINDAYS\'" style="margin-top:10px;">';
		strHtml += '<input type="number" ng-model="spsEvent.spsEventprizes'+divCnt+'.dayValue" v-key="spsEvent.spsEventprizes'+divCnt+'.dayValue" required />&nbsp;<span>일</span></div></td>';
		strHtml += '<th>혜택 유형</th>';
		strHtml += '<td><radio-list ng-model="spsEvent.spsEventprizes'+divCnt+'.prizeTypeCd" code-group="PRIZE_TYPE_CD" ></radio-list>';
		strHtml += '<div ng-if="spsEvent.spsEventprizes'+divCnt+'.prizeTypeCd == \'PRIZE_TYPE_CD.COUPON\'" style="margin-top:10px;">';
		strHtml += '<input type="text" ng-model="spsEvent.spsEventprizes'+divCnt+'.couponId" ng-readonly="true" disabled/>';
		strHtml += '<input type="text" ng-model="spsEvent.spsEventprizes'+divCnt+'.couponName" ng-readonly="true" disabled/>';
		strHtml += '<button type="button" class="btn_eraser" ng-class="{\'btn_eraser_disabled\' : !spsEvent.spsEventprizes'+divCnt+'.couponId}" ng-click="ctrl.eraseCoupon('+divCnt+')">지우개</button>';
		strHtml += '<button type="button" class="btn_type2" ng-click="ctrl.addCouponPopup(\'eventprize\', '+divCnt+')"><b>쿠폰 검색</b></button></div>';
		strHtml += '<div ng-if="spsEvent.spsEventprizes'+divCnt+'.prizeTypeCd == \'PRIZE_TYPE_CD.POINT\'" style="margin-top:10px;">';
		strHtml += '<input type="number" ng-model="spsEvent.spsEventprizes'+divCnt+'.savePoint" v-key="spsEvent.spsEventprizes'+divCnt+'.savePoint"/>&nbsp;<span>점</span></div></td></tr></tbody></table>';
		strHtml += '<div class="btn_alignR"><button type="button" class="btn_type1" ng-click="ctrl.addEventprize()"><b>혜택 추가</b></button></div>';
	
		angular.element(document.querySelector('#td_prize')).append($compile(strHtml)($scope));
		
		$scope.spsEvent["spsEventprizes"+divCnt] = {
			joinTypeCd : 'JOIN_TYPE_CD.DATE',
			joinStartDt : '',
			joinEndDt : '',
			dayValue : '',
			prizeTypeCd : 'PRIZE_TYPE_CD.COUPON',
			couponId : '',
			couponName : '',
			savePoint : ''
		};
	}
	
	//출석이벤트 혜택 삭제
	this.deleteEventprize = function(index) {
		if ($scope.spsEvent.spsEventprizes[index] != null) {
			$scope.spsEvent.spsEventprizes.splice(index, 1);
		}
		$scope.spsEvent["spsEventprizes"+index] = null;
		angular.element(document.querySelector('#div_prize'+index)).remove();
	}
	
	// 이벤트 유형 변경
	$scope.$watch('spsEvent.eventTypeCd', function(value) {
		if (value != $scope.nowEventType) {
			if ('EVENT_TYPE_CD.COUPON' == $scope.nowEventType) {
				if ($scope.grid_couponEvent.data.length > 0) {
					if (confirm(pScope.MESSAGES["sps.event.changeType.msg.coupon"])) {
						$scope.spsEvent.couponExistYn = 'Y';
					}
				}		
			} else if ('EVENT_TYPE_CD.ATTEND' == $scope.nowEventType) {
				if (confirm(pScope.MESSAGES["sps.event.changeType.msg.attend"])) {
					$scope.spsEvent.prizeExistYn = 'Y';
				}
			} else if ('EVENT_TYPE_CD.EXP' == $scope.nowEventType) {
				if ($scope.spsEvent.pmsProduct.productId != null && $scope.spsEvent.pmsProduct.productId != '') {
					if (confirm(pScope.MESSAGES["sps.event.changeType.msg.exp"])) {
						$scope.spsEvent.productExistYn = 'Y';
					}
				}
			} else if ('EVENT_TYPE_CD.MANUAL' == $scope.nowEventType) {
				if ($scope.grid_eventGift.data.length > 0) {
					if (confirm(pScope.MESSAGES["sps.event.changeType.msg.manual"])) {
						$scope.spsEvent.giftExistYn = 'Y';
					}
				}
			}
		}
		
		if ('EVENT_TYPE_CD.INFO' == value) {
			$scope.spsEvent.joinControlCd = 'JOIN_CONTROL_CD.DAY';
			angular.element('[ng-model="spsEvent.joinControlCd"]').find('input').attr('disabled', true);
		}else if ('EVENT_TYPE_CD.EXP' == value) {
			$scope.spsEvent.joinControlCd = 'JOIN_CONTROL_CD.TOTAL';
			angular.element('[ng-model="spsEvent.joinControlCd"]').find('input').attr('disabled', true);
		}else{
			$scope.spsEvent.joinControlCd = 'JOIN_CONTROL_CD.DAY';
			angular.element('[ng-model="spsEvent.joinControlCd"]').find('input').attr('disabled', false);
		}
				
	});
	
	// 경품 추가_행 추가
	this.addEventGift = function() {
		$scope.eventGiftGrid.addRow({
			giftNo: "",
			name: "",
			winnerNumber: ""
		});
	}

	//변경된 이벤트 정보 저장
	this.saveEvent = function() {
		var check = true;
		
		//확인
		if (!confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			return;
		}
		
		//폼 체크
		if (!commonService.checkForm($scope.frmEventUpdate)) {
			return;
		}
		
		if(!$scope.spsEvent.eventStartDt || !$scope.spsEvent.eventEndDt){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		if(!$scope.spsEvent.eventJoinStartDt || !$scope.spsEvent.eventJoinEndDt){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		if(!$scope.spsEvent.winNoticeDate){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		// 날짜 체크
		if ($scope.spsEvent.eventStartDt > $scope.spsEvent.eventJoinStartDt) {
			alert(pScope.MESSAGES["sps.event.joinDate.validation"]);
			return;
		}
		if ($scope.spsEvent.eventEndDt < $scope.spsEvent.eventJoinEndDt) {
			alert(pScope.MESSAGES["sps.event.joinDate.validation2"]);
			return;
		}
		
//		당첨자 발표일 등록은 전시기간과 상관없이 저장 
//		if ($scope.spsEvent.winNoticeDate < $scope.spsEvent.eventEndDt
//				|| $scope.spsEvent.winNoticeDate < $scope.spsEvent.eventStartDt) {
//			alert(pScope.MESSAGES["sps.event.winNoticeDate.validation1"]);
//			return;
//		}
		if ($scope.spsEvent.winNoticeDate < $scope.spsEvent.eventJoinEndDt
				|| $scope.spsEvent.winNoticeDate < $scope.spsEvent.eventJoinStartDt) {
			alert(pScope.MESSAGES["sps.event.winNoticeDate.validation2"]);
			return;
		}
		if ('EVENT_TYPE_CD.EXP' == $scope.spsEvent.eventTypeCd) {
			if ($scope.spsEvent.commentEndDt < $scope.spsEvent.eventJoinStartDt) {
				alert(pScope.MESSAGES["sps.event.commentDate.validation"]);
				check = false;
			}
			if (!check) {
				return;
			}
		}
				
		// HTML 체크
//		if(common.isEmpty($scope.spsEvent.html1) && common.isEmpty($scope.spsEvent.html2)){
//			alert(pScope.MESSAGES["sps.event.html.register.validation"]);
//			return;
//		}

		// 배너 이미지 체크
		if (!common.isEmpty($scope.spsEvent.html1) && common.isEmpty($scope.spsEvent.img1)) {
			alert(pScope.MESSAGES["sps.event.img1.register.validation"]);
			return;
		}
		if (!common.isEmpty($scope.spsEvent.html2) && common.isEmpty($scope.spsEvent.img2)) {
			alert(pScope.MESSAGES["sps.event.img2.register.validation"]);
			return;
		}

		//쿠폰이벤트: 쿠폰목록 처리
		if ('EVENT_TYPE_CD.COUPON' == $scope.spsEvent.eventTypeCd) {
			var couponCnt = $scope.grid_couponEvent.getDirtyRows;
			if (couponCnt > 0) {
				$scope.spsEvent.spsEventcoupons = [];
				for (var i=0; i<couponCnt; i++) {
					$scope.spsEvent.spsEventcoupons.push({
						couponId: $scope.grid_couponEvent.data[i].couponId
					});
				}
			} else {
				couponCnt = $scope.grid_couponEvent.data.length;
				if (couponCnt > 0) {
					$scope.spsEvent.spsEventcoupons = [];
					for (var i=0; i<couponCnt; i++) {
						$scope.spsEvent.spsEventcoupons.push({
							couponId: $scope.grid_couponEvent.data[i].couponId
						});
					}
				} else {
					alert(pScope.MESSAGES["sps.event.select.coupon"]);
					check = false;
				}
			}
		}
		
		//출석이벤트: 데이터 유효성 체크
		else if ('EVENT_TYPE_CD.ATTEND' == $scope.spsEvent.eventTypeCd) {
			var prizeCnt = angular.element(document.querySelector('#td_prize')).find('table').length;
			
			for (var i=0; i<prizeCnt; i++) {
				var newData = false;
				var eventPrize = {};
				
				if ($scope.spsEvent.spsEventprizes[i] != null && $scope.spsEvent.spsEventprizes[i] != undefined) {
					eventPrize = $scope.spsEvent.spsEventprizes[i];
				} else {
					eventPrize = $scope.spsEvent["spsEventprizes"+i];
					newData = true;
				}
				
				if ($scope.check_allPrizeNull(eventPrize)) {
					alert(pScope.MESSAGES["sps.event.need.prize"]);
					check = false;
				} else {
					if (eventPrize.dayValue == '' || eventPrize.dayValue == null) {
						eventPrize.dayValue = '0';
					}					
					if ('JOIN_TYPE_CD.DATE' == eventPrize.joinTypeCd) {
						var joinDt = eventPrize.joinDate;
						eventPrize.joinStartDt = joinDt.slice(0, 10) + " 00:00:00"; 
						eventPrize.joinEndDt = joinDt;
					}
					
					if (newData) {
						$scope.spsEvent.spsEventprizes.push(eventPrize);
					}					
				}
			}
		}
		
		// 체험이벤트: 상품 등록 및 상품 삭제
		else if ('EVENT_TYPE_CD.EXP' == $scope.spsEvent.eventTypeCd) {
			var expProductId = $scope.spsEvent.pmsProduct.productId;
			if (expProductId != null && expProductId != '') {
				$scope.spsEvent.productId = $scope.spsEvent.pmsProduct.productId;
			} else {
				alert(pScope.MESSAGES["sps.event.product.validation"]);
				check = false;
			}
		}
		
		// 수동이벤트: 경품 등록 및 경품 삭제
		else if ('EVENT_TYPE_CD.MANUAL' == $scope.spsEvent.eventTypeCd) {
			var giftCnt = $scope.grid_eventGift.getDirtyRows;
			if (giftCnt > 0) {
				$scope.spsEvent.spsEventgifts = [];
				for (var i=0; i<giftCnt; i++) {
					$scope.spsEvent.spsEventgifts.push({
						giftNo: $scope.grid_eventGift.data[i].giftNo,
						name : $scope.grid_eventGift.data[i].name,
						winnerNumber : $scope.grid_eventGift.data[i].winnerNumber
					});
					$scope.spsEvent.winnerNumber = $scope.grid_eventGift.data[i].winnerNumber;
				}
			} else {
				giftCnt = $scope.grid_eventGift.data.length;
				if (giftCnt > 0) {
					$scope.spsEvent.spsEventgifts = [];
					for (var i=0; i<giftCnt; i++) {
						$scope.spsEvent.spsEventgifts.push({
							giftNo: $scope.grid_eventGift.data[i].giftNo,
							name : $scope.grid_eventGift.data[i].name,
							winnerNumber : $scope.grid_eventGift.data[i].winnerNumber
						});
						$scope.spsEvent.winnerNumber = $scope.grid_eventGift.data[i].winnerNumber;
					}
				} else {
					alert(pScope.MESSAGES["sps.event.select.gift"]);
					check = false;
				}
			}
		}

		if (check) {			
			if ($scope.spsEvent.eventId == null || $scope.spsEvent.eventId == '') {
				$scope.spsEvent.eventStateCd = 'EVENT_STATE_CD.READY';
				if ($scope.spsEvent.winnerShowYn == null || $scope.spsEvent.winnerShowYn == '') {
					$scope.spsEvent.winnerShowYn = 'N';
				}		
				
				eventService.insertEvent($scope.spsEvent, function(response) {
					if (response != null) {
						$scope.spsEvent.eventId = response.content;
						pScope.eventId = response.content;
						
						if ('EVENT_TYPE_CD.COUPON' == $scope.spsEvent.eventTypeCd) {
							$scope.myGrid.loadGridData();
						} else if ('EVENT_TYPE_CD.MANUAL' == $scope.spsEvent.eventTypeCd) {
							$scope.eventGiftGrid.loadGridData();
						}
						
						alert(pScope.MESSAGES["common.label.alert.save"]);
						pScope.myGrid.loadGridData();
					}
				});
			} else {
				eventService.updateEventInfo($scope.spsEvent, function(response) {
					if ('EVENT_TYPE_CD.COUPON' == $scope.spsEvent.eventTypeCd) {
						$scope.myGrid.loadGridData();
					} else if ('EVENT_TYPE_CD.MANUAL' == $scope.spsEvent.eventTypeCd) {
						$scope.eventGiftGrid.loadGridData();
					}
					
					alert(pScope.MESSAGES["common.label.alert.save"]);
					pScope.myGrid.loadGridData();
				});
			}

		}

	}
	
	//prize 전체 값 check
	$scope.check_allPrizeNull = function(eventPrize) {
		var check = false;

		if (!eventPrize.joinStartDt && !eventPrize.joinEndDt && !eventPrize.dayValue &&
			!eventPrize.couponId && !eventPrize.savePoint) {
			check = true;
		}
		
		//혜택 조건이 연속출석이나 출석일수일 때 입력값이 숫자가 아닌 경우  
		if ("JOIN_TYPE_CD.DAYS" == eventPrize.joinTypeCd ||
			"JOIN_TYPE_CD.JOINDAYS" == eventPrize.joinTypeCd) {
			var days = eventPrize.dayValue;
			if (!days) {
				check = true;
			} else if (!angular.isNumber(Number(days))) {
				check = true;
			} else if (days <= 0) {
				check = true;
			}
		}
		
		//혜택 유형이 포인트일 때 입력값이 숫자가 아닌 경우  
		if ("PRIZE_TYPE_CD.POINT" == eventPrize.prizeTypeCd) {
			var point = eventPrize.savePoint;			
			if (!point) {
				check = true;
			} else if (!angular.isNumber(Number(point))) {
				check = true;
			} else if (point <= 0) {
				check = true;
			}
		}
		
		return check;
	}
	
	// 체험이벤트 상품 등록
	this.addProductPopup = function() {
		commonPopupService.productPopup($scope, "callback_product", false);
	}
	$scope.callback_product = function(data) {
		$scope.spsEvent.pmsProduct = {
			productId : data[0].productId,
			name : data[0].name
		};
		$scope.$apply();
	}
	
	// 체험이벤트 상품 그리드: 상품 상세
	$scope.productDetail = function(data) {
		commonPopupService.openProductDetailPopup($scope, data.productId, false);
	}
	
	// 체험이벤트 상품 그리드: 공급업체 상세
	$scope.businessDetail = function(data) {
		$scope.businessId = data.ccsBusiness.businessId;
		var url = Rest.context.path + '/ccs/business/popup/detail';
		popupwindow(url, "공급업체 상세", 1200, 600);
	}

	this.close = function() {
		$window.close();
	}
	
	// 이벤트 상태 변경
	this.changeState = function(param) {
		if (confirm(pScope.MESSAGES["sps.event.change.state.msg"])) {
			$scope.chgEvent = {};
			$scope.chgEvent.eventId = $scope.spsEvent.eventId;
			if (param == 'run') {
				$scope.chgEvent.eventStateCd = 'EVENT_STATE_CD.RUN';
			} else if (param == 'stop') {
				$scope.chgEvent.eventStateCd = 'EVENT_STATE_CD.STOP';
			}
			
			eventService.updateEventStatus($scope.chgEvent, function() {
				alert(pScope.MESSAGES["common.label.alert.save"]);
				
				pScope.myGrid.loadGridData();
				$window.location.reload();
			});
		}
	}
	
	
}).controller("sps_eventJoinlistPopApp_controller", function($window, $scope, commonService, gridService, eventService, commonPopupService) { //응모정보 PopUp Controller
	
	//팝업에서 부모 scope 접근하기 위함
	pScope = $window.opener.$scope;
	

	if(!common.isEmpty(pScope.pageId)){
		$scope.pageId = pScope.pageId;
	}

	//팝업 저장 후 부모 scope의 조건값 clear
	//pScope.search = {};

	//tab 이동
	this.moveTab = function($event, param) {
		if ("detail" == param) {
			$window.location.href = Rest.context.path + "/sps/event/popup/detail";
		}
	}

	//현재 팝업 정보 설정
	$window.$scope = $scope;
	
	//이벤트 응모정보 변수
	$scope.join = {};
	$scope.spsEventjoin = []; //이벤트 응모

	var pScopeId = angular.fromJson(pScope.eventId);
	var	joinColumnDefs = [
	   	 { field: 'joinNo', colKey: "c.sps.eventJoin.joinNo", linkFunction: "", type: 'number' },             
         { field: 'memberNo', colKey: "mmsMember.memberNo", linkFunction: "memberDetail(row.entity)" },
         { field: 'memName', colKey: "c.sps.eventJoin.memName", linkFunction: "memberDetail(row.entity)" },
         { field: 'phone1', colKey: "c.sps.eventJoin.phone1", cellFilter:'tel' },
         { field: 'phone2', colKey: "c.sps.eventJoin.phone2", cellFilter:'tel' },
         { field: 'memAddress', colKey: "c.mms.address.address1" },
         { field: 'orderId', colKey: "c.oms.order.id", linkFunction: "orderDetail(row.entity)" },
         { field: 'winHist', colKey: "c.sps.eventJoin.winHist", linkFunction: "winHistory" },
         { field: 'naverblogUrl', colKey: "spsEventjoin.naverblogUrl" },
         { field: 'instagramUrl', colKey: "spsEventjoin.instagramUrl" },
         { field: 'facebookUrl', colKey: "spsEventjoin.facebookUrl" },
         { field: 'kakaostoryUrl', colKey: "spsEventjoin.kakaostoryUrl" },
         { field: 'url', colKey: "c.sps.eventJoin.url" },
         { field: 'insDt', colKey: "c.sps.eventJoin.insDt", cellFilter: "date:\'yyyy-MM-dd hh:mm:ss\'" },
         { field: 'winYn', colKey: "spsEventjoin.winYn", vKey: 'spsEventjoin.winYn', enableCellEdit:true, cellFilter:'yesOrNoFilter' },
         { field: 'giftName', colKey: "spsEventgift.name" }
     ];

	var gridParam = {
			scope : $scope,
			gridName : "grid_joinEvent",
			url :  '/api/sps/event/popup/join',
			searchKey : "join",
			columnDefs : joinColumnDefs,
			gridOptions : {
				enableCellEdit : false,
			},
			callbackFn : function() {
				$scope.join.eventId = pScopeId;
				$scope.myGrid.loadGridData();
			}
	};
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	// Grid: 회원 상세정보
	$scope.memberDetail = function(data) {
		commonPopupService.openMemberDetailPopup($scope, data.memberNo);
	}
	
	// Grid: 당첨이력 호출
	$scope.winHistory = function(fieldValue, rowEntity) {
		$scope.memberNo = { memberNo : rowEntity.memberNo };
		
		var url = Rest.context.path + '/sps/event/popup/winhistory';
		popupwindow(url, "이벤트 당첨이력", 830, 460);
	}
	
	// Grid: 응모정보 호출
	$scope.joinDetail = function(fieldValue, rowEntity) {
		$scope.searchInfo = { eventId : pScopeId.eventId, memberNo : rowEntity.memberNo };
		
		var url = Rest.context.path + '/sps/event/popup/joindetail';
		popupwindow(url, "이벤트 응모상세", 400, 500);
	}
	
	// Grid: 주문 상세정보
	$scope.orderDetail = function(data) {
		$scope.orderId = data.orderId;
		commonPopupService.orderPopup($scope, null, false);
	}
	
	// 당첨자 일괄 업로드
	this.batchWinnerUpload = function() {
		commonPopupService.gridbulkuploadPopup($scope, "excelUpload_callback", "eventWinner");
	}
	
	$scope.excelUpload_callback = function(response) {
		var data = response.resultList;

		for (var i=0; i<data.length; i++) {
			$scope.myGrid.addRow(data[i].winnerList);
		}
	}
	
	// 당첨자 저장
	this.saveGridData = function() {
		var pUrl = Rest.context.path + '/api/sps/event/popup/join/insert';
		$scope.myGrid.saveGridData(pUrl, function(response) {
			$scope.myGrid.loadGridData();
		});
	}
	
}).controller("sps_eventWinHistoryPopApp_controller", function($window, $scope, gridService) { //당첨이력 PopUp Controller
	
	//팝업에서 부모 scope 접근하기 위함
	pScope = $window.opener.$scope;
	
	//이벤트 당첨이력 변수
	$scope.winhist = {};

	var pScopeId = angular.fromJson(pScope.memberNo);
	var	winColumnDefs = [
	   	 { field: 'spsEvent.name', colKey: "c.sps.event.name" },
	   	 { field: 'insDt', colKey: "c.sps.eventJoin.insDt", cellFilter: "date:\'yyyy-MM-dd hh:mm:ss\'" },
	   	 { field: 'winYn', colKey: "spsEventjoin.winYn", vKey: 'spsEventjoin.winYn', cellFilter:'yesOrNoFilter' }
    ];

	var gridParam = {
		scope : $scope,
		gridName : "grid_winHist",
		url : '/api/sps/event/popup/winhistory',
		searchKey : "winhist",
		columnDefs : winColumnDefs,
		gridOptions : {
			enableRowSelection : false,
			checkBoxEnable : false
		},
		callbackFn : function() {
			$scope.winhist.memberNo = pScopeId.memberNo;
			$scope.myGrid.loadGridData();
		}
	};
	$scope.myGrid = new gridService.NgGrid(gridParam);

	this.close = function() {
		$window.close();
	}
	
}).controller("sps_eventJoinHistoryPopApp_controller", function($window, $scope, eventService) { //상세 응모정보 PopUp Controller
	//팝업에서 부모 scope 접근하기 위함
	pScope = $window.opener.$scope;

	$scope.joinhist = {};

	var pScopeId = angular.fromJson(pScope.searchInfo);
	$scope.joinhist.eventId = pScopeId.eventId;
	$scope.joinhist.memberNo = pScopeId.memberNo;
	
	eventService.getEventjoinDetail($scope.joinhist, function(response) {
	    $scope.spsEventjoin = response;
	});

	this.close = function() {
		$window.close();
	}
});