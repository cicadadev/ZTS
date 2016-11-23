// NOTICE 화면 모듈
var noticeApp = angular.module('noticeApp', [
		'ui.date', 'commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'ngCkeditor', 'commonPopupServiceModule'
]);

Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

noticeApp.controller('listCtrl', function($window, $scope, $filter, noticeService, gridService, commonService, commonPopupService) {
	var columnDefs = [{	field : 'noticeNo',			colKey : 'c.ccs.notice.number',		width : '150',		type : 'number',	cellClass : 'alignC',		linkFunction : 'popup.detail'}, 
	                  {	field : 'title',			colKey : 'ccsNotice.title',			width : '200',		cellClass : 'alignC',		enableSorting : false,		linkFunction : 'popup.detail'}, 
	                  {	field : 'noticeTypeName',	displayName:'유형', 					colKey : 'c.ccs.notice.type',	cellClass : 'alignC',		width : '150'},
	                  {	field : 'topNoticeYn',		displayName:'TOP노출여부',				width : '150',		cellClass : 'alignC'},
	                  {	field : 'startDt',			colKey : 'c.ccs.notice.startdt',	width : '200',		cellClass : 'alignC'}, 
	                  {	field : 'endDt',			colKey : 'c.ccs.notice.enddt',		width : '200',		cellClass : 'alignC'}, 
	                  {	field : 'displayYn',		colKey : 'ccsNotice.displayYn',		width : '100',		cellFilter: 'displayYnFilter',		cellClass : 'alignC'},
	                  {	field : 'readCnt',			displayName : '조회수', /*colKey : 'c.grid.column.insId',*/	width : '100',		type : 'number',		cellClass : 'alignC'},
	                  {	field : 'insId',			displayName : "등록자",			userFilter :'insId,insName', 	colKey : 'c.grid.column.insId',		width : '130',		cellClass : 'alignC'}, 
	                  {	field : 'insDt',			displayName : "등록일시",			colKey : 'c.grid.column.insDt',		width : '200',		cellClass : 'alignC'}, 
	                  {	field : 'updId',			userFilter :'updId,updName',	colKey : 'c.grid.column.updId',		width : '130',		cellClass : 'alignC'}, 
	                  {	field : 'updDt',			displayName : "최종수정일시",			colKey : 'c.grid.column.updDt',		width : '200',		cellClass : 'alignC'}
//			{
//				field : 'detail',
//				colKey : 'ccsNotice.detail',
//				width : '300',
//				enableSorting : false
//			}, {
//				field : 'useYn',
//				colKey : 'ccsNotice.useYn',
//				width : '90',
//				enableCellEdit : true,
//				cellFilter: 'useYnFilter',
//				cellClass : 'alignC'
//			}, {
//				field : 'fixYn',
//				colKey : 'ccsNotice.fixYn',
//				width : '90',
//				enableCellEdit : true,
//				cellFilter: 'yesOrNoFilter',
//				cellClass : 'alignC'
//			}, 
	];
	var gridParam = {
		scope : $scope,
		gridName : 'grid_notice',
		url : '/api/ccs/notice',
		searchKey : 'search',
		columnDefs : columnDefs
	// callbackFn : gridCallbackFn
	};

	$scope.myGrid = new gridService.NgGrid(gridParam);

	$window.$scope = $scope;
	$scope.search = {
		reset : function() {
			commonService.reset_search($scope, 'search');
//			angular.element(".day_group").find('button:first').addClass("on");
			angular.element('.day_group').find('button').eq(0).click();
		},
		init : function() {
		}
	}
	
	$scope.list = {
		search : function() {
			$scope.myGrid.loadGridData();
		},
		save : function() {
			$scope.myGrid.saveGridData();
		},
		excel : function() {
			$scope.myGrid.exportExcel();
		},
		remove : function() {
			$scope.myGrid.deleteGridData(null, function(){					
				$scope.myGrid.loadGridData();
			});
		}
	}

	// 검색 지우기
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
	
	// 공지 등록 및 상세 팝업
	$scope.noticeNo = '';
	$scope.flagTxt = '';
	$scope.popup = {
			detail : function(field, row) { // linkfunction
				$scope.noticeNo = row.noticeNo;
				$scope.flagTxt = '상세';
				$scope.type = '';
				popupwindow('/ccs/notice/popup/detail', 'NOTICE 상세', 1100, 850);
			},
			insert1 : function() { // FO 일반 등록
				$scope.noticeNo = '';
				$scope.flagTxt = '등록';
				$scope.type = 'NOTICE_TYPE_CD.FRONT';
				popupwindow('/ccs/notice/popup/detail', 'NOTICE 등록', 1100, 850);
			},
			insert2 : function() { // FO 이벤트 등록
				$scope.noticeNo = '';
				$scope.flagTxt = '등록';
				$scope.type = 'NOTICE_TYPE_CD.EVENT';
				popupwindow('/ccs/notice/popup/detail', 'NOTICE 등록', 1100, 850);
			},
			insert3 : function() { // BO 공지 등록
				$scope.noticeNo = '';
				$scope.flagTxt = '등록';
				$scope.type = 'NOTICE_TYPE_CD.ADMIN';
				popupwindow('/ccs/notice/popup/detail', 'NOTICE 등록', 1100, 850);
			},
			insert4 : function() { // PO 공지 등록
				$scope.noticeNo = '';
				$scope.flagTxt = '등록';
				$scope.type = 'NOTICE_TYPE_CD.PARTNER';
				popupwindow('/ccs/notice/popup/detail', 'NOTICE 등록', 1100, 850);
			}
		}
	
	angular.element(document).ready(function() {
		commonService.init_search($scope, 'search');
	});
	
});

noticeApp.controller('popCtrl', function($window, $scope, $filter, noticeService, gridService, commonService, commonPopupService) {

	pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;
	$scope.ccsNotice = {};
	$scope.param = {};
	$scope.flagTxt = pScope.flagTxt; // 등록, 상세 구분
	$scope.ccsNotice.noticeTypeCd = pScope.type; // 공지 유형
	$scope.ccsBrands = [];
	$scope.BrandsCheck;
	
	$scope.displayYns = [
	 	                {
	 	     				val : 'Y',
	 	     				text : '전시'
	 	     			}, {
	 	     				val : 'N',
	 	     				text : '미전시'
	 	     			}
	 	     	];

	$scope.topYns = [
		 	                {
		 	     				val : 'Y',
		 	     				text : '노출'
		 	     			}, {
		 	     				val : 'N',
		 	     				text : '비노출'
		 	     			}
		 	     	];
	
	$scope.permitYns = [
		 	                {
		 	     				val : 'EVENT_TARGET_DIV_CD.GENERAL',
		 	     				text : '일반'
		 	     			}, {
		 	     				val : 'EVENT_TARGET_DIV_CD.EXP',
		 	     				text : '체험단'
		 	     			}
		 	     	];
	
	angular.element(document).ready(function() {
		if (pScope.noticeNo == '' && (pScope.type == 'NOTICE_TYPE_CD.FRONT' || pScope.type == 'NOTICE_TYPE_CD.EVENT')) {
			$scope.ccsBrands.push({
				name: ''
			});
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
		
		if(!$scope.ccsNotice.startDt || !$scope.ccsNotice.endDt){
			alert('유효하지 않은 항목이 존재합니다.');
			return;
		}
		
		// 확인 메세지
		if ($scope.ccsNotice.noticeNo != undefined && $scope.ccsNotice.noticeNo != null && $scope.ccsNotice.noticeNo != '') {
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
		}else{
			if(!confirm("공지사항을 생성하시겠습니까?")){
				return;
			}
		}
		
		// FO 공지사항 (일반, 이벤트) 등록일때 브랜드 리스트 추가
		$scope.Brands = [];
		if($scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.FRONT' || $scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.EVENT'){
			for (i in $scope.ccsBrands) {
				if ($scope.ccsBrands[i].name != null && $scope.ccsBrands[i].name != '') {
					$scope.Brands.push($scope.ccsBrands[i].name);
				}
			}
			$scope.ccsNotice.ccsBrands = $scope.Brands;
		}
		
		if ($scope.ccsNotice.noticeNo != undefined && $scope.ccsNotice.noticeNo != null && $scope.ccsNotice.noticeNo != '') {
			if($scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.EVENT'){
				noticeService.checkEvent($scope.ccsNotice, function(response) {
					if (response.content === 'fail') {
						alert('이벤트가 존재하지 않습니다.');
						return;
					} else {
						noticeService.update($scope.ccsNotice, function(response) {
							if (response.content === 'fail') {
								alert('공지사항 변경시 에러가 발생 하였습니다.');
							} else {
								pScope.$apply();
								pScope.myGrid.loadGridData();
								alert($scope.MESSAGES["common.label.alert.save"]);
								$window.close();
							}
						});
					}
				});
			}else{
				noticeService.update($scope.ccsNotice, function(response) {
					if (response.content === 'fail') {
						alert('공지사항 변경시 에러가 발생 하였습니다.');
					} else {
						pScope.$apply();
						pScope.myGrid.loadGridData();
						alert($scope.MESSAGES["common.label.alert.save"]);
						$window.close();
					}
				});
			}
		} else {
			$scope.ccsNotice.readCnt = '0';
			if($scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.EVENT'){
				noticeService.checkEvent($scope.ccsNotice, function(response) {
					if (response.content === 'fail') {
						alert('이벤트가 존재하지 않습니다.');
						return;
					} else {
						noticeService.insert($scope.ccsNotice, function(response) {
							if (response.content === 'fail') {
								alert('공지사항 등록시 에러가 발생 하였습니다.');
							} else {
								pScope.$apply();
								pScope.myGrid.loadGridData();
								alert('공지사항이 생성되었습니다.');
								$window.close();
							}
						});
					}
				});
			}
			else{
				noticeService.insert($scope.ccsNotice, function(response) {
					if (response.content === 'fail') {
						alert('공지사항 등록시 에러가 발생 하였습니다.');
					} else {
						pScope.$apply();
						pScope.myGrid.loadGridData();
						alert('공지사항이 생성되었습니다.');
						$window.close();
					}
				});
			}
		}
	};
	
	this.detail = function() {
		if (pScope.noticeNo != '') {
			$scope.ccsNotice.noticeNo = pScope.noticeNo;
			noticeService.selectOne($scope.ccsNotice, function(response) {
				$scope.ccsNotice = response;
				
				//Brand 세팅
				for (i in response.ccsNoticeBrands) {
					$scope.ccsBrands.push({
							name : response.ccsNoticeBrands[i].brandId
					});
				}
				if($scope.ccsBrands == null || $scope.ccsBrands.length == 0){
					$scope.ccsBrands.push({
						name : ''
					});
				}
			});
		}else{
			$scope.ccsNotice.topNoticeYn = 'N'
			// 공지사항 유형 설정
			if($scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.FRONT'){
				$scope.ccsNotice.noticeTypeName = 'FO 일반 공지';
			}else if($scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.EVENT'){
				$scope.ccsNotice.eventTargetDivCd = 'EVENT_TARGET_DIV_CD.EXP';
				$scope.ccsNotice.noticeTypeName = '당첨자 발표 공지';
				$scope.ccsNotice.detail = '<p>당첨 경품은 제로투세븐닷컴에 등록된 기본배송지 주소로 배송됩니다. 주소정보 미등록, 잘못된 주소로 인한 오배송은 제로투세븐닷컴에서 책임지지 않사오니, '
					+ '반드시 주소를 확인해주세요. <br/> <a href=\"#none\" class=\"btn_sStyle1\" onclick=\"javascript:ccs.link.mypage.info.deliveryAddress();\">주소 확인하기</a></p>'
					+ '<a href=\"#none\" class=\"btn_sStyle4 sPurple1 btn_winner\" onclick=\"javascript:custcenter.event.searchWinnerList()();\">당첨자 명단 전체보기</a></p>';
				
			}else if($scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.ADMIN'){
				$scope.ccsNotice.noticeTypeName = 'BO 공지';
			}else if($scope.ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.PARTNER'){
				$scope.ccsNotice.noticeTypeName = 'PO 공지';
			}
		}
	};
	
	// Brand 중복 검사
	this.brandValidation = function(index){
		for (i in $scope.ccsBrands) {
			if (index != i && $scope.ccsBrands[i].name == $scope.ccsBrands[index].name) {
				$scope.ccsBrands[index].name = '';
				return;
			}
		}
	}
	
	//Brand 추가
	this.addBrand = function(index) {
		if (!$scope.ccsBrands[index].name) {
			return;
		}
		$scope.ccsBrands.push({
			name: null
		});
	}
	
	// Brand 삭제
	this.deleteBrand = function(index) {
		var brand = $scope.ccsBrands[index];
		if (brand != null) {
			brand.name = "";
		}
		
		$scope.ccsBrands.splice(index, 1);
		angular.element(document.querySelector('#td_brand')).find('div').eq(index).remove();
	}
	
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	$scope.popup = function(url,name) {
		popupwindow(url,name, 1000, 400);
	}
	
	$scope.detail = '';
	this.preview = function() {
		$scope.detail = $scope.ccsNotice.detail;
		$scope.param = {callback : "callback_role", multi:false};
		$scope.popup(Rest.context.path +"/ccs/notice/popup/preview",'NoticePreview', 700, 700);
	}

	$scope.ckOption = {
		language : 'ko'										//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		, filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}
	
}).controller("noticePreviewController", function($window, $scope, $filter, noticeService, commonService,gridService, commonPopupService){
	
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.search = {};
	$scope.ccsNotice = {};
	
	$scope.ccsNotice.detail = pScope.detail;
	
	this.init = function(){
		var content = $scope.ccsNotice.detail;
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
	this.close = function(){
		window.close();
	}

}).controller("ponoticeDetailController", function($window, $scope, $filter, noticeService, commonService,gridService, commonPopupService){
	
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.search = {};
	$scope.ccsNotice = {};
	
	this.detail = function(){
		$scope.ccsNotice.noticeNo = pScope.noticeNo;
		noticeService.selectMdNotice($scope.ccsNotice, function(response) {
			$scope.ccsNotice = response;
			
			var content = $scope.ccsNotice.detail;
			if(content == undefined)
				return false;
			
			var iframe = document.getElementById('targetCode');
			iframe = (iframe.contentWindow) ? iframe.contentWindow : (iframe.contentDocument.document) ? iframe.contentDocument.document : iframe.contentDocument;
			iframe.document.open();
			iframe.document.write(content);
			iframe.document.close();
			return false;

			$('#previewLoad').load(str);
		});
	}
	this.close = function(){
		window.close();
	}

});

noticeApp.controller('polistCtrl', function($window, $scope, $filter, noticeService, gridService, commonService, commonPopupService) {
	$window.$scope = $scope;
	if(!common.isEmpty($window.opener)){
		
		pScope = $window.opener.$scope;// 부모창의 scope
	}
	$scope.search = {};
	$scope.ccsNotices = [];
	$scope.paging = [];
	
//	페이징
	$scope.startPage = '1';
	$scope.endPage;
	$scope.pageCnt;
	$scope.currentPage = '1';
	$scope.search.firstRow = '1';
	$scope.search.lastRow = '10';
	
	this.detail = function() {
		noticeService.selectPoList($scope.search, function(response) {
			$scope.ccsNotices = response;
			
			if($scope.ccsNotices.length > 0){
				$scope.endPage = $scope.ccsNotices[0].totalCount;
				$scope.pageCnt = parseInt($scope.ccsNotices[0].totalCount / 10 + 1);
				
				var paging = parseInt($scope.pageCnt) - parseInt($scope.currentPage / 10);
								
				for(var i = 1; i <= paging; i++){
					$scope.paging.push({page : parseInt(i) + parseInt($scope.currentPage / 10) * parseInt(10)});
				}
			}
		});
	}

	$scope.reset = function() {
		$scope.ccsNotices = [];
		$scope.paging = [];
		noticeService.selectPoList($scope.search, function(response) {
			$scope.ccsNotices = response;
			
			if($scope.ccsNotices.length > 0){
				$scope.endPage = $scope.ccsNotices[0].totalCount;
				$scope.pageCnt = parseInt($scope.ccsNotices[0].totalCount / 10 + 1);
				
				var paging = parseInt($scope.pageCnt) - parseInt($scope.currentPage / 10);
								
				for(var i = 1; i <= paging; i++){
					$scope.paging.push({page : parseInt(i) + parseInt($scope.currentPage / 10) * parseInt(10)});
				}
			}
		});
	}
	
	this.pageMove = function(param){
		if(param == 'first'){
			$scope.currentPage = '1';
		}else if(param == 'prev'){
			if($scope.currentPage == '1'){
				return;
			}
			$scope.currentPage = Number($scope.currentPage) - Number(1);
			
		}else if(param == 'next'){
			if($scope.currentPage == $scope.pageCnt){
				
			}
			$scope.currentPage = Number($scope.currentPage) + Number(1);
		}else if(param == 'last'){
			$scope.currentPage = $scope.pageCnt;
		}else{
			$scope.currentPage = param;
		}
		$scope.search.firstRow = (Number($scope.currentPage) - Number(1)) * Number(10) + Number(1);
		$scope.search.lastRow = Number($scope.currentPage) * Number(10);
		
		$scope.reset();
	}
	this.close = function(){
		window.close();
	}
	
	// 공지 상세 팝업
	this.detailNotice = function(noticeNo){
		$scope.noticeNo = noticeNo;
		$scope.type = '';
		popupwindow('/ccs/ponotice/popup/detail', 'NOTICE 상세', 1100, 750);
	}
	/*
	var columnDefs = [{	field : 'noticeNo',			colKey : 'c.ccs.notice.number',		width : '33%',		type : 'number',	cellClass : 'alignC',		linkFunction : 'popup.detail'}, 
	                  {	field : 'title',			colKey : 'ccsNotice.title',			width : '33%',		enableSorting : false,		linkFunction : 'popup.detail'}, 
	                  {	field : 'insDt',			colKey : 'c.grid.column.insDt',		width : '33%',		cellClass : 'alignC'} 
	];
	var gridParam = {
		scope : $scope,
		gridName : 'grid_notice',
		url : '/api/ccs/notice',
		searchKey : 'search',
		columnDefs : columnDefs
	// callbackFn : gridCallbackFn
	};

	$scope.myGrid = new gridService.NgGrid(gridParam);

	$window.$scope = $scope;
	$scope.search = {
		reset : function() {
			commonService.reset_search($scope, 'search');
			angular.element(".day_group").find('button:first').addClass("on");
		}
	}
	
	$scope.noticeTypeCds = [
	 	     			{
	 	     				cd : 'NOTICE_TYPE_CD.FRONT',
	 	     				name : 'FO 일반'
	 	     			}, 
	 	     			{
	 	     				cd : 'NOTICE_TYPE_CD.EVENT',
	 	     				name : 'FO 이벤트'
	 	     			}, 
	 	     			{
	 	     				cd : 'NOTICE_TYPE_CD.PARTNER',
	 	     				name : 'PO'
	 	     			}, 
	 	     			{
	 	     				cd : 'NOTICE_TYPE_CD.ADMIN',
	 	     				name : 'BO'
	 	     			}
	 	     	];
	
	$scope.list = {
		search : function() {
			$scope.search.noticeTypeCds = "'NOTICE_TYPE_CD.PARTNER'";
			$scope.myGrid.loadGridData();
		}
	}

	$scope.noticeNo = '';
	$scope.flagTxt = '';
	$scope.popup = {
			detail : function(field, row) { // linkfunction
				$scope.noticeNo = row.noticeNo;
				$scope.flagTxt = '상세';
				$scope.type = '';
				popupwindow('/ccs/ponotice/popup/detail', 'NOTICE 상세', 1100, 550);
			}
		}
	
	
	angular.element(document).ready(function() {
		commonService.init_search($scope, 'search');
	});*/
});