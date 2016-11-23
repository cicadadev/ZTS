function calcHeight(frameId)
{
  //find the height of the internal page
  var the_height= document.getElementById(frameId).contentWindow.document.body.scrollHeight;
  document.getElementById(frameId).height= the_height;
}


function resizeHeight(frameId, height){
    document.getElementById(frameId).height= height;
	  
}

function fnTabPosition() {
	
	var tab_view_width = angular.element(".position_tab .box").prop("offsetWidth") - parseInt(angular.element(".position_tab .tab_type1").css("marginLeft"));
	var onLeft = angular.element(".position_tab .tab_type1 .opentab li.on").prop("offsetLeft");
	var onOuterWidth = angular.element(".position_tab .tab_type1 .opentab li.on").prop("clientWidth");
	var tab_on_left = onLeft + onOuterWidth + 1;
	var go_move = 0;
	
	if( tab_on_left > tab_view_width ){
		angular.element(".position_tab .tab_type1 .opentab li").each(function() {
			if( angular.element(this).prop("offsetLeft") > tab_on_left - angular.element(".position_tab .box").prop("offsetWidth") ){
				go_move = -angular.element(this).prop("offsetLeft");

				return false;
			}
		});

		angular.element(".position_tab .tab_type1").css({"marginLeft" : go_move});
		
	}
 	else if (onLeft < Math.abs(parseInt(angular.element(".position_tab .tab_type1").css("marginLeft")))) {
 		
 		angular.element(".position_tab .tab_type1 .opentab li").each(function() {
			
			if( angular.element(this).prop("offsetWidth") > tab_on_left - angular.element(".position_tab .box").prop("offsetWidth") ){
				go_move = -angular.element(this).prop("offsetLeft");

				return false;
			}
		});

		angular.element(".position_tab .tab_type1").css({"marginLeft" : go_move});
	}
}

// main 화면 모듈
var mainApp = angular.module("mainApp", ["ui.date", "commonServiceModule","commonPopupServiceModule", "ccsServiceModule", 'pmsServiceModule', 'gridServiceModule']);


//메뉴 클릭 이벤트
mainApp.directive("menubtn", function($compile){
	return function(scope, element, attrs){
		element.bind("click", function(){
			
			// tab 최대 수량은 10, 해상도에 따라 보여지는 수량은 각기 다름.
			if(scope.openlist && scope.openlist.length >= 10){
				alert("더이상 탭을 추가할수 없습니다.");
				return;
			}
            var pagename = attrs.pagename;

		    var pagesDoc =  angular.element(document.getElementById('pages'));
		   
		    //기존 화면 숨기기
		    angular.element(document.getElementsByName('__pageSpan')).hide();
		    
		    // page id 생성 : 난수 발생
		    
		    var randomKey = Math.floor(Math.random() * 100000) + 1;
		    var pageid=attrs.menuId + "_" + randomKey;//페이지 번호 ( menuId + randomKey )

			
			var url = attrs.url+"?pageId="+pageid;
		    //새로운 영역 append
		    pagesDoc.append("<iframe onLoad='calcHeight(this.id);' style='width:100%;' name='__pageSpan' id='"+pageid + "_module' src='"+url+"' ></iframe>");
		    
		    	try{
				    // 기존 탭 비활성 : current 가 'on' 이면 활성
				    angular.forEach(scope.openlist, function(item){				    	
				    	item.current = '';
				    });
				    
				    // 현재 탭 활성
				    scope.openlist.push({pagename : pagename, pageid : pageid, current : 'on'});
				    
				    scope.$apply();
				    
		    	}catch(err){
		    		alert("페이지 로딩 에러!! - ["+err+"]");
		    		//appDom.remove();
		    	}
//		    });
		    
		    
		    fnTabPosition();
		});
	}
}).directive("opentab" , function(){//탭영역 자동 생성
	return {
		template : '<li id="{{open.pageid}}_tab"  class="{{open.current}}" ng-repeat="open in openlist">'
			+'<button type="button" ng-click="clickTab(open)" current="{{open.current}}" pageid="{{open.pageid}}">{{open.pagename}}</button>'
			+'	<button type="button" class="tab_close" tabclose pageid="{{open.pageid}}" >탭 닫기</button>'
			+'</li>',
		link : function(scope, element, attrs){
			

			scope.clickTab = function(obj){
				
				angular.forEach(scope.openlist, function(item, index){
					item.current="";
				})
				
				obj.current="on";
				
				angular.element(document.getElementsByName('__pageSpan')).hide();
				angular.element(document.getElementById(obj.pageid+'_module')).show();
				calcHeight(obj.pageid+'_module');
			}
		}
	} 
}).directive("tabclick" , function(){// 탭 클릭 이벤트 :  미사용...
	return function(scope, element, attrs){
		
		element.bind("click", function(obj){
			if(attrs.current=="on"){//활성화 탭이면
				//by pass
			}else{//비활성화 탭이면
				
				// 기존 화면 숨기기
                angular.element(document.getElementsByName('__pageSpan')).hide();
                angular.element(document.getElementById(attrs.pageid+'_module')).show();
			    angular.forEach(scope.openlist, function(item, index){
			    	
			    	if(item.pageid==attrs.pageid){
			    		item.current = "on";
			    	}else{
			    		item.current = "";
			    	}
			    });
			    
			    common.safeApply(scope);
			    
			}
			
			
			
		});
	} 
}).directive("tabclose" , function(){// 탭에서 삭제 클릭
	return function(scope, element, attrs){
		
		element.bind("click", function(){
			
			var prevIndex = 0;
		    angular.forEach(scope.openlist, function(item, index){
		    	
		    	if(item.pageid==attrs.pageid){
		    		prevIndex = index-1;		    		
		    		scope.openlist.splice(index, 1);
		    		
		    		var pageid;
		    		//열린 페이지가 없을때 마지막 tab open
		    		if(prevIndex > -1){
		    			pageid = scope.openlist[prevIndex].pageid;
		    		} else if (prevIndex == -1 && scope.openlist.length > 0){
		    			pageid = scope.openlist[0].pageid;
		    		}
		    		
		    		angular.element(document.getElementsByName('__pageSpan')).hide();
		    		angular.element(document.getElementById(pageid+'_module')).show();
		    		
		    		var nowPageOpen = false;
		    		angular.forEach(scope.openlist, function(item, index){
		    			if(item.current == "on"){
		    				nowPageOpen = true;
		    			}
		    		});
		    		
		    		if(!nowPageOpen){
		    			angular.forEach(scope.openlist, function(item, index){
		    				if(item.pageid==pageid){
		    					item.current = "on";
		    				}else{
		    					item.current = "";
		    				}
		    			});
		    			
		    			scope.$apply();
		    		}
		    	}
		    });
		    
		    // 현재 탭, 영역 삭제
			//angular.element(document.getElementById(attrs.pageid+"_tab")).remove();
			angular.element(document.getElementById(attrs.pageid+"_module")).remove();
			
			scope.$apply();
		});
	}
}).directive("menubtn2", function($compile){
	return function(scope, element, attrs){
		scope = parent.$scope;
		element.bind("click", function(){
			if(scope.openlist && scope.openlist.length >= 8){
				alert("더이상 탭을 추가할수 없습니다.");
				return;
			}
            var pagename = attrs.pagename;

		    var pagesDoc =  angular.element(window.parent.document.getElementById('pages'));
		   
		    //기존 화면 숨기기
		    angular.element(window.parent.document.getElementsByName('__pageSpan')).hide();
		    angular.element(window.parent.document.getElementById('mainpo')).hide();
		    
		    // page id 생성 : 난수 발생
		    
		    var randomKey = Math.floor(Math.random() * 100000) + 1;
		    var pageid=attrs.menuId + "_" + randomKey;//페이지 번호 ( menuId + randomKey )

			
			var url = attrs.url+"?pageId="+pageid;
			
			
		    //새로운 영역 append
		    pagesDoc.append("<iframe onLoad='calcHeight(this.id);' style='width:100%;' name='__pageSpan' id='"+pageid + "_module' src='"+url+"' ></iframe>");
		    
		    	try{
				    // 기존 탭 비활성 : current 가 'on' 이면 활성
				    angular.forEach(scope.openlist, function(item){				    	
				    	item.current = '';
				    });
				    
				    // 현재 탭 활성
				    scope.openlist.push({pagename : pagename, pageid : pageid, current : 'on'});
				    scope.$apply();
				    
		    	}catch(err){
		    		alert("페이지 로딩 에러!! - ["+err+"]");
		    		//appDom.remove();
		    	}
//		    });
		      
		    
		    
		    
		});
	}
})


// bo layout controller
mainApp.controller("boController", function($window, $scope, restFactory, commonPopupService, noticeService) {
	$window.$scope = $scope;
	$scope.menuGenCnt = 0;//전체 화면 생성 수
	restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/ccs/menu/list', null, null, function(response) {
		$scope.menuGroupList = [];
		$scope.menuGroupList = response;
	});
    
	//main
	$scope._main_page = true;
	
    // 현재 열려있는 메뉴목록 초기화
    $scope.openlist = [];        
    $scope.loginInfo = {};
    // 상단 우측 로그아웃 init
	this.logout = function(){
    	var url = Rest.context.path + "/j_spring_security_logout";
		return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE,url,null,null,this.logout_callback,{'Content-Type': 'application/x-www-form-urlencoded'});
	}
	
	this.openMenu = function(selectedMenuGroup){
		
		selectedMenuGroup.isClick = !selectedMenuGroup.isClick;
		
		for(var i = 0 ; i < $scope.menuGroupList.length ; i++){
			if($scope.menuGroupList[i].menuGroupId!=selectedMenuGroup.menuGroupId){
				$scope.menuGroupList[i].isClick=false;
			}
		}
	}
	
	this.openUser = function(param){
		$scope.userId = param;
		$scope.mainDetail = 'Y';
		popupwindow('/ccs/user/popup/detail',"userDetailPopup", 1100, 600);
	}
	
	this.logout_callback = function(response){
		var result=response.response;
		
		alert(result.message);
	
		if(!result.error){
			location.href="/ccs/login";						
		}
	}
	
	this.main = function(){
		$window.location.reload();
//		$scope.openlist = [];
//		angular.element(document.getElementsByName('__pageSpan')).hide();
//      angular.element(document.getElementById('mainpo')).show();
	}
	
})

// po layout controller
mainApp.controller("poController", function($window, $scope, restFactory, commonPopupService, businessService, noticeService, mainService, commonService) {
	$window.$scope = $scope;
	
	$scope.menuGenCnt = 0;//전체 화면 생성 수
	$scope.search = {};
	
    // 현재 열려있는 메뉴목록 초기화
    $scope.openlist = [];        
    $scope.loginInfo = {};
    $scope.businessId;
    // 상단 우측 로그아웃 init
	this.logout = function(){
    	var url = Rest.context.path + "/j_spring_security_logout";
		return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE,url,null,null,this.logout_callback,{'Content-Type': 'application/x-www-form-urlencoded'});
	}
	
	// 메뉴 클릭
	this.openMenu = function(selectedMenuGroup){
		
		selectedMenuGroup.isClick = !selectedMenuGroup.isClick;
		
		for(var i = 0 ; i < $scope.menuGroupList.length ; i++){
			if($scope.menuGroupList[i].menuGroupId!=selectedMenuGroup.menuGroupId){
				$scope.menuGroupList[i].isClick=false;
			}
		}
	}
	
	this.openMember = function(param){
		$scope.userId = param;
		$scope.type = 'D';
		popupwindow('/ccs/business/popup/userDetail',"userDetailPopup", 1100, 350);
		
	}
	
	this.logout_callback = function(response){
		var result=response.response;
		
		alert(result.message);
	
		if(!result.error){
			location.href="/ccs/loginpo";
		}
	}
	
	// login 정보 저장
	this.detail = function(param){
		$scope.businessId = param;
		
		$scope.openPopup.open();
		
//		위탁 매입 업체일경우 상품,주문,프로모션 메뉴그룹 제거
		$scope.ccsBusiness = {};
		$scope.ccsBusiness.businessId = global.session.businessId;
		businessService.getBusiness($scope.ccsBusiness, function(response) {
			$scope.ccsBusiness = response;
			restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/ccs/menu/list', null, null, function(response) {
				$scope.menuGroupList = [];
				$scope.menuGroupList = response;
				if($scope.ccsBusiness.saleTypeCd == 'SALE_TYPE_CD.CONSIGN' && $scope.ccsBusiness.purchaseYn == 'Y'){
					var i = 0;
					while(!common.isEmptyObject($scope.menuGroupList[i])){
						if($scope.menuGroupList[i].menuGroupId == '2' || $scope.menuGroupList[i].menuGroupId == '4' || $scope.menuGroupList[i].menuGroupId == '6'){
							$scope.menuGroupList.splice(i, 1);
						}else{
							i++;
						}
					}
				}
			});
		});
	}
	
	this.main = function(){
		$window.location.reload();
	}
		
	$scope.openPopup = {
		open : function(){
			
			// 팝업 url 설정
			var url = window.location.href;
//			var param = {url : window.location.href};
			
			var cookieStr = url.replace(global.config.domainUrl, '').substr(0, 10);
//			if(url.match(/(ccs\/cs\/notice\/list)/)){
//				url = window.location.href;
//			}else {
//				return;
//			}
			$scope.search.url = url;
			
			mainService.getPopupList($scope.search, function(response) {
				$scope.popup = [];
				for(i in response){
					if(!common.isEmpty(response[i].popupNo)){
						var cookieKey = "popupNotice"+response[i].popupNo + cookieStr;
						if(!$scope.openPopup.checkPoupCookie(cookieKey)){
							var position = response[i].position.split(',');
							$scope.popup.push({
								title : response[i].title,
								detail : response[i].detail1,
								cookieKey : cookieKey
							});
							console.log(cookieKey);
							window.open("/ccs/popupnotice/popup/preview", '0to7', 'top='+position[0]+', left='+position[1]+', width='+position[2]+', height='+position[3]+', menubar=no, status=no, toolbar=no, location=no, directoryies=no');
						}
					}
				}
			});
			
		},		
		
		checkPoupCookie : function(cookieName){
			var cookie = document.cookie;
			
			// 현재 쿠키가 존재할 경우
			if(cookie.length > 0){
				// 자식창에서 set해준 쿠키명이 존재하는지 검색
				startIndex = cookie.indexOf(cookieName);
				
				
				// 존재한다면
				if(startIndex != -1){
					var expires = new Date();
					var value = $scope.openPopup.getCookie(cookieName);
//					expires.setDate(expires.getDate() - 1);
//					document.cookie = cookieName + "= " + "; expires=" + expires.toGMTString() + "; path=/";
					return true;
				}else{
					// 쿠키 내에 해당 쿠키가 존재하지 않을 경우
					return false;
				}
			}else{
				// 쿠키 자체가 없을 경우
				return false;
			}
		},
		
		getCookie : function  (name) {
			
			// cookie string
			var cs = document.cookie;
			var prefix = name + "=";
			
			// cookie's Start Index of the information of it.
			var cSI = cs.indexOf(prefix);
			if (cSI == -1) {
				return null;
			}
			
			// Find cookie's End Index of the information of the cookie.
			var cEI = cs.indexOf(";", cSI + prefix.length);
			
			// If it din't find the CEI, then set it to the end of the cs
			if (cEI == -1) {
				cEI = cs.length;
			}
			// Decode the value of the cookie's
			return unescape(cs.substring(cSI + prefix.length, cEI));		
		}
	}
	
	this.menualDown = function(){
		var fiileName = 'ZTS모바일_사용자매뉴얼_Partneroffice_v0.6_20161011.pptx';
		commonService.getConfig("admin.domain.url", function(response) {
			var fullPath = response.content + '/resources/ppt/' + fiileName;
//			var fullPath = response.content + '/DOC/03.개발/' + fiileName;
			$window.location = fullPath;
//			$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + fullPath;
		});
	}
//	ZTS모바일_사용자매뉴얼_Partner office_v0.6_20161011
	
})

// po main 컨트롤러
mainApp.controller("pomainController", function($window, $scope, $filter, restFactory, gridService, commonService, commonPopupService, mainService, productService) {
	$window.$scope = $scope;	   // 팝업에서 부모 scope 접근하기 위함.
	$scope.ccsNotices = 		[];//공지
	$scope.ccsProductQnas = 	[];//상품 QNA
	$scope.ccsInquiryStates = 	[];//문의사항
	$scope.ccsProductQnas = 	[];//상품 QNA 현황
	$scope.ccsInquiryStates = 	[];//문의사항 현황
	$scope.ccsOrderStates = 	[];//주문 현황
	$scope.ccsOrderStates2 = 	[];//최근 30일 주문현황
	$scope.search = {};
	
	
	
	var columnDefs = [
		             { field: 'productId',						width:'20%',		displayName : "상품번호", linkFunction : "openProductPopup"},      			
		             { field: 'name',							width:'20%',        colKey:"pmsProduct.name", linkFunction : "openProductPopup"},        
		             { field: 'pmsSaleproduct.name',			width:'20%',        displayName : "단품명"}, 
		             { field: 'pmsCategory.name',				width:'20%', 		colKey:"c.pmsCategory.category" },
					 { field: 'pmsSaleproduct.realStockQty', 	width:'20%',		displayName : "재고수량"}
		         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_product",	//mandatory
			url :  '/api/ccs/main/product',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
			},
			callbackFn : function(){	//optional
				$scope.myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	// 메인 목록 조회 
	this.detail = function() {
		var date = new Date();
		$scope.search.startDate = $filter('date')(date.setDate(date.getDate()-30), Constants.date_format_2);
		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);		
		
		
		// 공지사항
		mainService.getNoticeList(function(response) {
			$scope.ccsNotices = response;
		});
		
		// 상품 QNA
		mainService.getProductQnaList2(function(response) {
			$scope.ccsProductQnas = response;
		});
		
		// 고객문의
		mainService.getInquiryList(function(response) {
			$scope.ccsInquirys = response;
		});
		
		// 최근 30일 상품 QNA 현황
		mainService.getProductQnaState($scope.search, function(response) {
			$scope.ccsProductQnaStates = response;
		});
		
		// 최근 30일 고객문의 현황
		mainService.getInquiryState($scope.search, function(response) {
			$scope.ccsInquiryStates = response;
		});
		
		// 최근 30일 주문현황
		mainService.getOrderState($scope.search, function(response) {
			$scope.ccsOrderStates2 = response;
			// 검색 시작일 오늘 날짜로 수정
			$scope.search.startDate = $filter('date')(date.setDate(date.getDate()+30), Constants.date_format_2);
			
			// 오늘 주문현황
			mainService.getOrderState($scope.search, function(response) {
				$scope.ccsOrderStates = response;
			});
		});
		// 품절 임박 상품
	}
	
	// 공지 관리 팝업 10-22 보류
	this.openNoticeList = function(){
		popupwindow('/ccs/ponotice/list', '공지 관리', 1100, 750);
	}
	
	// 공지 상세 팝업
	this.detailNotice = function(noticeNo){
		$scope.noticeNo = noticeNo;
		$scope.type = '';
		popupwindow('/ccs/ponotice/popup/detail', 'NOTICE 상세', 1100, 750);
	}
	
	// 문의 상세 팝업
	this.detailInquiry = function(inquiryNo){
		$scope.inquiryNo = inquiryNo;
		$scope.type = 'D'
		popupwindow('/ccs/inquiry/popup/detail','inquiryDetailPopup',1000, 600);
	}
	
	// 상품 qna 상세 팝업
	this.detailProductqna = function(productQnaNo){
		$scope.productQnaNo = productQnaNo;
		popupwindow('/ccs/productQna/popup/detail','productQnaDetailPopup', 1100, 900);
	}
	
	//상품 상세 팝업
	$scope.openProductPopup = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
})

// bo main 컨트롤러
mainApp.controller("bomainController", function($window, $scope, $filter, restFactory, gridService, commonPopupService, mainService, productService) {
	$window.$scope = $scope;	   // 팝업에서 부모 scope 접근하기 위함.
	$scope.ccsNotices = 		[];//공지
	$scope.ccsProductQnas = 	[];//상품 QNA 현황
	$scope.ccsInquiryStates = 	[];//문의사항 현황
	$scope.ccsMds 			= 	[];//담당자 정보
	$scope.search = {};


	// 메인 목록 조회 
	this.detail = function() {
		var date = new Date();
		$scope.search.startDate = $filter('date')(date.setDate(date.getDate()-30), Constants.date_format_2);
		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);
		
		// 공지사항
		mainService.getNoticeList(function(response) {
			$scope.ccsNotices = response;
		});
		
		// 최근 30일 상품 QNA 현황
		mainService.getProductQnaState($scope.search, function(response) {
			$scope.ccsProductQnaStates = response;
		});
		
		// 최근 30일 고객문의 현황
		mainService.getInquiryState($scope.search, function(response) {
			$scope.ccsInquiryStates = response;
		});
		
		// 업무 담당자 정보
		mainService.getMdList(function(response) {
			$scope.ccsMds = response;
		});
	}
	
	// 공지 상세 팝업
	this.detailNotice = function(noticeNo){
		$scope.noticeNo = noticeNo;
		$scope.type = '';
		popupwindow('/ccs/ponotice/popup/detail', 'NOTICE 상세', 1100, 750);
	}
})

