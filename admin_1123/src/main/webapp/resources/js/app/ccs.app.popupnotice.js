// POPUONOTICE 화면 모듈
var popupnoticeApp = angular.module('popupnoticeApp', [
		'ui.date', 'commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'ngCkeditor', 'commonPopupServiceModule'
]);

//메시지
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

popupnoticeApp.controller('listCtrl', function($window, $scope, $filter, popupService, gridService, commonService, commonPopupService) {
	
	var columnDefs = [
			{	field : 'popupNo',			colKey : 'c.ccs.popup.notice.number',	width : '100',		type : 'number',		cellClass : 'alignC',	linkFunction : 'popup.detail'	}, 
			{	field : 'title',			colKey : 'ccsNotice.title',			 	width : '150',		cellClass : 'alignC',	linkFunction : 'popup.detail'},
			{	field : 'popupTypeName',	colKey : 'c.ccs.popup.notice.type',		width : '150',		cellClass : 'alignC'}, 
			{	field : 'channelName',		displayName : "전시 채널",		cellClass : 'alignC', /*colKey : 'c.ccs.popup.notice.channel',*/	width : '100'}, 
			{	field : 'startDt',			displayName : "팝업공지시작 일시",/*colKey : 'c.ccs.popup.notice.startdt',*/	width : '200',		cellClass : 'alignC'}, 
			{	field : 'endDt',			displayName : "팝업공지마감 일시",/*colKey : 'c.ccs.popup.notice.enddt',*/	width : '200',			cellClass : 'alignC'}, 
			{	field : 'displayYn',		displayName : "전시여부",					width : '150',		cellFilter: 'displayYnFilter',	cellClass : 'alignC'},
			{	field : 'insId',			displayName : "등록자",			userFilter :'insId,insName',			width : '130',		cellClass : 'alignC'}, 
			{	field : 'insDt',			displayName : "등록일시",			colKey : 'c.grid.column.insDt',			width : '200',		cellClass : 'alignC'}, 
			{	field : 'updId',			userFilter :'updId,updName',    colKey : 'c.grid.column.updId',			width : '130',		cellClass : 'alignC'}, 
			{	field : 'updDt',			displayName : "최종수정일시",			colKey : 'c.grid.column.updDt',			width : '200',		cellClass : 'alignC'}
	];
	var gridParam = {
		scope : $scope,
		gridName : 'grid_popup',
		url : '/api/ccs/popupnotice',
		searchKey : 'search',
		columnDefs : columnDefs
	// callbackFn : gridCallbackFn
	};

	$scope.myGrid = new gridService.NgGrid(gridParam);

	this.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	$window.$scope = $scope;
	$scope.search = {};
	
	
	$scope.list = {
		search : function() {
			$scope.search.pcChannelY = '';
			$scope.search.mobileChannelY = '';
			if($scope.search.channelYn != null && $scope.search.channelYn != ''){
				var str = $scope.search.channelYn.split(',');
				for(i in str){
					if(str[i] == 'PC'){
						$scope.search.pcChannelY = 'Y';
					}else if(str[i] == 'MOBILE'){
						$scope.search.mobileChannelY = 'Y';
					}
				}
			}
			$scope.myGrid.loadGridData();
		},
		save : function() {
			$scope.myGrid.saveGridData();
		},
		remove : function() {
			$scope.myGrid.deleteGridData(null, function(){					
				$scope.myGrid.loadGridData();
			});
		}
	}

	// 팝업 검색어 지우기
	this.eraser = function(){
		$scope.search.insInfoId = "";
		$scope.search.insInfoName = "";
	}
	
	// 검색어 변경시 초기화
	this.change = function(){
		$scope.search.insInfoId = "";
	}
	
	// 회원 검색
	this.searchUser = function() {
		commonPopupService.userPopup($scope,'callback_ins',false);					
	}
	
	$scope.callback_ins = function(data) {
		$scope.search.insInfoId = data[0].userId;
		$scope.search.insInfoName = data[0].name;
		$scope.$apply();
	}
	
	$scope.popupNo = '';
	$scope.flagTxt = '';
	$scope.popupTypeCd = '';
	$scope.popup = {
		detail : function(field, row) { // linkfunction
			$scope.popupNo = row.popupNo;
			$scope.flagTxt = '상세';
			$scope.type = '';
			popupwindow('/ccs/popupnotice/popup/detail', '팝업 상세', 1100, 850);
		},
		insert : function() {
			$scope.popupNo = '';
			$scope.flagTxt = '등록';
			$scope.popupTypeCd = 'POPUP_TYPE_CD.FRONT';
			popupwindow('/ccs/popupnotice/popup/detail', '팝업 등록', 1100, 850);
		},
		insert2 : function() {
			$scope.popupNo = '';
			$scope.flagTxt = '등록';
			$scope.popupTypeCd = 'POPUP_TYPE_CD.PARTNER';
			popupwindow('/ccs/popupnotice/popup/detail', '팝업 등록', 1100, 850);
		}
	}
	
	angular.element(document).ready(function() {
		commonService.init_search($scope, 'search');
	});
	
});

popupnoticeApp.controller('popCtrl', function($window, $scope, $filter, $compile, popupService, gridService, commonService, commonPopupService) {

	pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	$scope.ccsPopup = {};
	$scope.ccsPcUrls = [];
	$scope.ccsMoUrls = [];
	
	// Url Validation
	$scope.pcUrlsRequired;
	$scope.moUrlsRequired;
	$scope.flagTxt = pScope.flagTxt;
	
	$scope.displayYns = [
		 	                {
		 	     				val : 'Y',
		 	     				text : '전시'
		 	     			}, {
		 	     				val : 'N',
		 	     				text : '미전시'
		 	     			}
		 	     	];
	
	angular.element(document).ready(function() {
		if (pScope.popupNo == '') {
			$scope.ccsPcUrls.push({
				Url: ''
			});
			$scope.ccsMoUrls.push({
				Url: ''
			});
			$scope.UrlsValidation('pc');
			$scope.UrlsValidation('mo');
			
			$scope.ccsPopup.pcDisplayYn = true;			
			$scope.ccsPopup.mobileDisplayYn = true;
		}
	});
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	this.insert = function() {
		
		// 폼 체크
		if (!commonService.checkForm($scope.form2)) {
			return;
		}
		
		if($scope.ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.PARTNER'){
			if(!$scope.ccsPopup.startDt || !$scope.ccsPopup.endDt){
				alert('유효하지 않은 항목이 존재합니다.');
				return;
			}
			if((!$scope.ccsPopup.positionT || !$scope.ccsPopup.positionL || !$scope.ccsPopup.positionW || !$scope.ccsPopup.positionH)){
				alert('유효하지 않은 항목이 존재합니다.');
				return;
			}
			$scope.ccsPopup.position = $scope.ccsPopup.positionT + "," + $scope.ccsPopup.positionL + "," + $scope.ccsPopup.positionW + "," + $scope.ccsPopup.positionH;
		}else{
			if(($scope.ccsPopup.pcDisplayYn == false && $scope.ccsPopup.mobileDisplayYn == false)
					|| (!$scope.ccsPopup.startDt || !$scope.ccsPopup.endDt)){
				alert('유효하지 않은 항목이 존재합니다.');
				
				return;
			}
			
			// 팝업 전시채널 설정
			if($scope.ccsPopup.pcDisplayYn == true){
				if((!$scope.ccsPopup.positionT || !$scope.ccsPopup.positionL || !$scope.ccsPopup.positionW || !$scope.ccsPopup.positionH)){
					alert('유효하지 않은 항목이 존재합니다.');
					return;
				}
				$scope.ccsPopup.position = $scope.ccsPopup.positionT + "," + $scope.ccsPopup.positionL + "," + $scope.ccsPopup.positionW + "," + $scope.ccsPopup.positionH;
				if($scope.pcUrlsRequired == true){
					alert('유효하지 않은 항목이 존재합니다.');
					return;
				}
				// PC URL 설정
				$scope.pcUrls = [];
				for (i in $scope.ccsPcUrls) {
					if ($scope.ccsPcUrls[i].Url != null && $scope.ccsPcUrls[i].Url != '') {
						$scope.pcUrls.push($scope.ccsPcUrls[i].Url);
					}else{
						alert('유효하지 않은 항목이 존재합니다.');
						return;
					}
				}
				if($scope.pcUrls.length < 1){
					alert('유효하지 않은 항목이 존재합니다.');
					return;
				}
				$scope.ccsPopup.ccsPcUrls = $scope.pcUrls;
			}
			
			if($scope.ccsPopup.mobileDisplayYn == true){
				if($scope.moUrlsRequired == true){
					alert('유효하지 않은 항목이 존재합니다.');
					return;
				}
				// MO URL 설정
				$scope.moUrls = [];
				for (i in $scope.ccsMoUrls) {
					if ($scope.ccsMoUrls[i].Url != null && $scope.ccsMoUrls[i].Url != '') {
						$scope.moUrls.push($scope.ccsMoUrls[i].Url);
					}else{
						alert('유효하지 않은 항목이 존재합니다.');
						return;
					}
				}
				
				if($scope.moUrls.length < 1){
					alert('유효하지 않은 항목이 존재합니다.');
					return;
				}
				$scope.ccsPopup.ccsMoUrls = $scope.moUrls;
			}
			
		}
		
		// 확인 메세지
		if ($scope.ccsPopup.popupNo != undefined && $scope.ccsPopup.popupNo != null && $scope.ccsPopup.popupNo != '') {
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
		}else{
			if(!confirm("팝업 공지사항을 생성하시겠습니까?")){
				return;
			}
		}
		
		if($scope.ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.PARTNER'){
			$scope.ccsPopup.pcDisplayYn = 'Y';
			$scope.ccsPopup.mobileDisplayYn = 'N';
		}else{
			if($scope.ccsPopup.pcDisplayYn == true){
				$scope.ccsPopup.pcDisplayYn = 'Y';
			}
			else{
				$scope.ccsPopup.pcDisplayYn = 'N';
			}
			if($scope.ccsPopup.mobileDisplayYn == true){
				$scope.ccsPopup.mobileDisplayYn = 'Y';
			}
			else{
				$scope.ccsPopup.mobileDisplayYn = 'N';
			}
		}
		
		if ($scope.ccsPopup.popupNo != undefined && $scope.ccsPopup.popupNo != null && $scope.ccsPopup.popupNo != '') {
			popupService.update($scope.ccsPopup, function(response) {
				pScope.$apply();
				pScope.myGrid.loadGridData();
				alert($scope.MESSAGES["common.label.alert.save"]);
				$window.close();
			});
		} else {
			popupService.insert($scope.ccsPopup, function(response) {
				pScope.$apply();
				pScope.myGrid.loadGridData();
				alert('팝업공지사항이 생성되었습니다.');
				$window.close();
			});
		}
	};
	
	$scope.UrlsValidation = function(param){
		if(param == 'pc'){
			for (i in $scope.ccsPcUrls) {
				if ($scope.ccsPcUrls[i].Url == null || $scope.ccsPcUrls[i].Url == '') {
					$scope.pcUrlsRequired = true;
					return;
				}
			}
			$scope.pcUrlsRequired = false;
		}else if(param == 'mo'){
			for (i in $scope.ccsMoUrls) {
				if ($scope.ccsMoUrls[i].Url == null || $scope.ccsMoUrls[i].Url == '') {
					$scope.moUrlsRequired = true;
					return;
				}
			}
			$scope.moUrlsRequired = false;
		}
	}
	
	//Url 추가
	this.addUrl = function(index, flag) {
		if(flag == 'pc'){
			if ($scope.pcUrlsRequired == true) {
//				alert("PC URL 입력에 오류가 있습니다.");
				return;
			}
			$scope.ccsPcUrls.push({
				Url: null
			});
			$scope.UrlsValidation('pc');
		}else if(flag == 'mo'){
			if ($scope.moUrlsRequired == true) {
//				alert("MO URL 입력에 오류가 있습니다.");
				return;
			}
			$scope.ccsMoUrls.push({
				Url: null
			});
			$scope.UrlsValidation('mo');
		}
		
	}	
		
	//Url내용 지우기
	this.eraseUrl = function(index, flag) {
		if(flag == 'pc'){
			$scope.ccsPcUrls[index].Url = "";
			$scope.UrlsValidation('pc');
		}else if(flag == 'mo'){
			$scope.ccsMoUrls[index].Url = "";
			$scope.UrlsValidation('mo');
		}
		
	}
	
	// Url 내용 삭제
	this.deleteUrl = function(index, flag) {
		if(flag == 'pc'){
			var pcurl = $scope.ccsPcUrls[index];
			if (pcurl != null) {
				pcurl.Url = "";
			}
			
			$scope.ccsPcUrls.splice(index, 1);
			angular.element(document.querySelector('#td_pcurl')).find('div').eq(index).remove();
			$scope.UrlsValidation('pc');
		}else if(flag == 'mo'){
			var mourl = $scope.ccsMoUrls[index];
			if (mourl != null) {
				mourl.Url = "";
			}
			
			$scope.ccsMoUrls.splice(index, 1);
			angular.element(document.querySelector('#td_mourl')).find('div').eq(index).remove();
			$scope.UrlsValidation('mo');
		}
	}
	
	//팝업 상세
	this.detail = function() {
		if (pScope.popupNo != '') {
			$scope.ccsPopup.popupNo = pScope.popupNo;
			
			// 팝업 상세 조회
			popupService.selectOne($scope.ccsPopup, function(response) {
				$scope.ccsPopup = response;
				
				// PC URL 세팅
				for (var i = 0; i < response.ccsPopupurls.length; i++) {
					if(response.ccsPopupurls[i].deviceChannelTypeCd == 'DEVICE_CHANNEL_TYPE_CD.PC'){
						$scope.ccsPcUrls.push({
							Url : response.ccsPopupurls[i].popupUrl
						});
					}else if(response.ccsPopupurls[i].deviceChannelTypeCd == 'DEVICE_CHANNEL_TYPE_CD.MW'){
						$scope.ccsMoUrls.push({
							Url : response.ccsPopupurls[i].popupUrl
						});
					}
				}
				if($scope.ccsPcUrls.length == 0){
					$scope.ccsPcUrls.push({
						Url : null
					});
				}
				if($scope.ccsMoUrls.length == 0){
					$scope.ccsMoUrls.push({
						Url : null
					});
				}
				
				if($scope.ccsPopup.pcDisplayYn == 'Y'){
					var str = $scope.ccsPopup.position.split(',');
					$scope.ccsPopup.positionT = str[0];
					$scope.ccsPopup.positionL = str[1];
					$scope.ccsPopup.positionW = str[2];
					$scope.ccsPopup.positionH = str[3];
					
					$scope.ccsPopup.pcDisplayYn = true;
				}else{
					$scope.ccsPopup.pcDisplayYn = false;
				}
				
				if($scope.ccsPopup.mobileDisplayYn == 'Y'){
					$scope.ccsPopup.mobileDisplayYn = true;
				}else{
					$scope.ccsPopup.mobileDisplayYn = false;
				}
				$scope.UrlsValidation('pc');
				$scope.UrlsValidation('mo');
			});
		}else{
			$scope.ccsPopup.popupTypeCd = pScope.popupTypeCd;
			if($scope.ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.FRONT'){
				$scope.ccsPopup.popupTypeName = 'FO 팝업';
			}else if($scope.ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.PARTNER'){
				$scope.ccsPopup.popupTypeName = 'PO 팝업';
			}
		}
		
	};
	
	// PC 좌표 숫자만 입력
	this.inputNumber = function(name){
		
		var str = $scope.ccsPopup[name];
		
		if(str != null){
			str = str.replace(/[^0-9]/g, '');
			$scope.ccsPopup[name] = str;
		}
	}

	
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	
	$scope.popup = function(url,name) {
		popupwindow(url,name, 1000, 800);
	}
	// 팝업 미리보기
	this.preview = function() {
		$scope.detail1 = $scope.ccsPopup.detail1;
		$scope.detail2 = $scope.ccsPopup.detail2;
		$scope.popup(Rest.context.path +"/ccs/popupnotice/popup/preview",'PopupPreview', 700, 700);
	}

	$scope.ckOption = {
		language : 'ko'										//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		, filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}
	
}).controller("popupPreviewController", function($window, $scope, $filter, popupService, commonService,gridService, commonPopupService){
	
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.search = {};
	$scope.ccsPopup = {};
	
	$scope.ccsPopup.detail1 = pScope.popup[0].detail;
	$scope.cookieKey =  pScope.popup[0].cookieKey;
	document.title = pScope.popup[0].title;
	pScope.popup.splice(0, 1);
	
//	$scope.ccsPopup.detail2 = pScope.detail2;
	
	this.init = function(){
		var content = $scope.ccsPopup.detail1;
		if(content == undefined)
			return false;
		
		var iframe = document.getElementById('targetCode');
		iframe = (iframe.contentWindow) ? iframe.contentWindow : (iframe.contentDocument.document) ? iframe.contentDocument.document : iframe.contentDocument;
		iframe.document.open();
		iframe.document.write(content);
		iframe.document.close();
		return false;

		$('#previewLoad').load(str);
	}
	
	this.init2 = function(){
		var content = $scope.ccsPopup.detail2;
		if(content == undefined)
			return false;
		
		var iframe = document.getElementById('targetCode2');
		iframe = (iframe.contentWindow) ? iframe.contentWindow : (iframe.contentDocument.document) ? iframe.contentDocument.document : iframe.contentDocument;
		iframe.document.open();
		iframe.document.write(content);
		iframe.document.close();
		return false;

		$('#previewLoad').load(str);
	}
	
	$scope.setCookie   = function(value, expiredays){
		//console.log("set", name);
		var todayDate = new Date();
		todayDate.setDate(todayDate.getDate() + expiredays);
		document.cookie = $scope.cookieKey + '=' + escape( value ) + '; path=/; expires=' + todayDate.toGMTString() + ';';
	};
	
	this.closePop  = function(param1){
		if(document.getElementById("noticePopupCheck").checked){
			$scope.setCookie(param1, 1);
		}
		window.close();
	};
	
	
	this.close = function(){
		window.close();
	}

});