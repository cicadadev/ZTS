var productQnaManagerApp = angular.module("productQnaManagerApp", ["ui.date","commonServiceModule","gridServiceModule","mmsServiceModule"]);

productQnaManagerApp.controller("mms_productQnaManagerApp_controller", function($window, $scope, qnaService, commonService) {
	
	// 객체 생성
	$scope.qnaListLen = "";
	$scope.qnaDetail = {};
	$scope.form = {};
	
	/* selectBox options
	 * viewQna 페이지 로딩될 때, 카테고리 가져와 options 만들 예정!
	 */
	$scope.qnaPeriods = ["문의일", "답변일"];
	$scope.categories1 = ["대분류"];
	$scope.categories2 = ["중분류"];
	$scope.categories3 = ["소분류"];
	$scope.qnaMemInfos = ["문의자ID", "문의자명"];
	$scope.qnaContents = ["제목", "내용", "제목+내용"];
	
	/*
	 * grid data style
	 */
	centerAlign = "<div style='text-align:center; padding-top:4px'>{{COL_FIELD}}</div>";	
	productQnaDetail = "<a href='#' data-ng-click='grid.appScope.searchDetail(row.entity)'>"
				+ "<font style='text-decoration:underline'>"+centerAlign+"</font></a>";
	padding = "<div style='padding-top:4px; padding-left:7px'>"+productQnaDetail+"</div>";
	
	/*
	 * ui-grid 속성 세팅
	 */
	$scope.gridOptions = {
			enableGridMenu: true,		// 그리드 메뉴(오른쪽)
			enableSorting: true,		// column 오른쪽 sorting 기능				
			enableRowSelection: true,		// 체크박스
			enableSelectAll: true,		// 체크박스 전체 선택
			selectionRowHeaderWidth: 30,		// 체크박스 column width
			enableFiltering: true,		// 그리드 내 해당 column 검색 조건
			columnDefs: [
				{ field: 'qnaNo' , displayName: 'Q&A번호', width: '8%', cellTemplate: productQnaDetail }
				, { field: 'qnaTypeCd', displayName: 'Q&A유형', width: '6%', cellTemplate: centerAlign }
				, { field: 'title', displayName: 'Q&A제목', width: '*', cellTemplate: centerAlign }
				, { field: 'qnaTypeCd', displayName: '상품코드', width: '6%', cellTemplate: productQnaDetail }
				, { field: 'qnaTypeCd', displayName: '상품명', width: '15%', cellTemplate: centerAlign }
				, { field: 'qnaTypeCd', displayName: 'Q&A 전시여부', width: '7%', cellTemplate: centerAlign }
				, { field: 'memId', displayName: '문의자', width: '8%', cellTemplate: centerAlign }
				, { field: 'qnaDate', displayName: '문의일', width: '6%', cellTemplate: centerAlign }
				, { field: 'qnaStateCd', displayName: '답변상태', width: '7%', cellTemplate: centerAlign }
				, { field: 'userId', displayName: '답변인', width: '7%', cellTemplate: centerAlign }
				, { field: 'answerDate', displayName: '답변일', width: '6%', cellTemplate: centerAlign }
			],
			data: [
			],
			checkMultiSelect: true		// 다중 선택(체크박스)
//	 		paginationPageSizes: [25, 50, 75],		// 한 페이지당 items 수 selectbox
//	 		paginationPageSize: 25		// 한 페이지당 items 수
		};
		
	this.search = function() {
		qnaService.getQnaList(function(response) {
			$scope.gridOptions.data = response;
			$scope.qnaListLen = response.length;
		});		
	};
	
	$scope.searchDetail = function(data) {
		qnaService.getQnaDetail(data.qnaNo, null, function(response) {
			$scope.qnaDetail = response;
		});		
	};
	
	this.insert = function(data) {
		qnaService.setQna(null, data, function(response) {
			
		});
	};
	
	// popup
	this.searchOrder = function() {
		var url = "/mms/qna/popup/searchOrder";
		popupwindow(url, "주문 조회", 820, 640);
	};
 		
});