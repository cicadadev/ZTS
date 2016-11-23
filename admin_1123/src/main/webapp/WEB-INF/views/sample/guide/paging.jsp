<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<title>Admin</title>
<link rel="stylesheet" type="text/css" href="/resources/js/angular-1.5.4/ui-grid.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/common.css" />
</head>
<body>
<div>
<p>
<xmp>

	1. 페이징이 필요한 Controller 에 pageService 주입
	ex )
	 App.controller('ExcelController', [ 'PageService', function( PageService) {
	....
	}
	
	2. grid init 시 onRegisterApi 선언
	ex )
		$scope.gridOptions = {
			enableGridMenu: true
			, onRegisterApi: function (api) {
				gridApi = api;
				gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
					...
			    });
				gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					...
				});
			}
		}
		
	3. jsp 에 선언한 ng-model 값 선언
	ex )
		this.searchDataReq;
	
		[jsp]
		STORE ID <input type="text" data-ng-model="vm.searchDataReq.storeId">
		DISPLAY ID <input type="text" data-ng-model="vm.searchDataReq.displayId">
		INS ID <input type="text" data-ng-model="vm.searchDataReq.insId">
		
		
	4. Method Sample
	ex )
	4-1. 최초 조회
	 this.search = function () {
	 	// 최초 조회시 page 데이터 세팅
	 	// PageService.initPaging($scope, 사용데이터)
		data = PageService.initPaging($scope, this.searchDataReq);
		
		url = "/sample/pagingTotalCnt";
		// 페이지 전체카운트 세팅, 콜백 필요
		// PageService.getTotalCnt(url, data, 콜백);
		PageService.getTotalCnt(url, data, this.cnt);
		
		url = "/sample/paging";
		// 페이징한 데이터 호출
		// PageService.getList(url, data)
		$scope.gridOptions.data = PageService.getList(url, data);
	}
	
	// 페이지 전체카운팅 콜백함수
	this.cnt = function (response) {
		$scope.gridOptions.totalItems = Number(response.content);
	}
	
	4-2. sortIng 조회
		- 컬럼 설정시 name 필드 추가 및 DB 컬럼 이름으로 설정
		ex )
			columnDefs: [
						{ field: 'storeId', name: 'STORE_ID', width: '100', displayName: "상점번호"  }, 
						...
			         ]
		- 2.에서 선언된 onRegisterApi의 gridApi.core.on.sortChanged 구현
		ex )
				gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
					if (sortColumns.length != 0) {
						url = "/sample/paging";
						
						// 현재 sorting 값으로 페이징 
						// PageService.setCurrentSorting(
													$scope.[jsp에 선언된 controller 이름].searchDataReq
													, 컬럼이름
													, 정렬방향)
						data = PageService.setCurrentSorting(
													$scope.vm.searchDataReq
													, sortColumns[0].name
													, sortColumns[0].sort.direction);
						$scope.gridOptions.data = PageService.getList(url, data, this.callback);
					}
			    });
			    
			    [jsp]
			    <div data-ng-controller="ExcelController as vm">
			    
	4-3. 그리드 pagination으로 조회
		- 2.에서 선언된 onRegisterApi의 gridApi.pagination.on.paginationChanged 구현
		ex )
				url = "/sample/paging";
				// 현재 page값으로 페이징
				// PageService.setCurrentPaging(
												$scope.[jsp에 선언된 controller 이름].searchDataReq
												, 페이지번호
												, 그리드에 표시되는 row 수
												)
					
				data = PageService.setCurrentPaging($scope.vm.searchDataReq, newPage, pageSize);
				$scope.gridOptions.data = PageService.getList(url, data, this.callback);
	
	5. 각 search MODEL 에 BaseSearchCondition을 extends 한다.
		ex )
			public class DmsDisplaySearch extends BaseSearchCondition {
			....
			}
			
	6. SQL
		- 전체카운팅 쿼리와, 페이징 쿼리의 검색조건을 동일하게 설정한다.
		- 페이징 쿼리의 pagingPre 와 pagingPost를 include 한다.
		ex )
			[전체카운팅]
			<select id="getPageCnt" parameterType="dmsDisplaySearch" resultType="int">
			/* [sample."getPageCnt"][ian][2016. 5. 2.] */
				SELECT COUNT(*) AS TOTALITEMS
				FROM DMS_DISPLAY
				<trim prefix="WHERE" prefixOverrides="AND |OR ">
					<if test="storeId != null and storeId != ''">
						STORE_ID = # {storeId}
					</if>
					<if test="displayId != null and displayId != ''">
						AND DISPLAY_ID = # {displayId}
					</if>
					<if test="insId != null and insId != ''">
						AND INS_ID = # {insId}
					</if>
				</trim>
			</select>
			
			[페이징]
			<select id="getPagingList" parameterType="dmsDisplaySearch" resultType="dmsDisplaySearch">
			/* [sample.getPagingList][ian][2016. 4. 28.] */
				<include refid="common.pagingPre" />
				SELECT
					STORE_ID
					, DISPLAY_ID
					, NAME
					, DISPLAY_TYPE_CD
					, DISPLAY_ITEM_TYPE_CD
					, LEAF_YN
					, DISPLAY_YN
					, USE_YN
					, UPPER_DISPLAY_ID
					, SORT_NO
					, INS_ID
					, INS_DT
					, UPD_DT
					, UPD_ID
				FROM DMS_DISPLAY
				<trim prefix="WHERE" prefixOverrides="AND |OR ">
					<if test="storeId != null and storeId != ''">
						STORE_ID = # {storeId}
					</if>
					<if test="displayId != null and displayId != ''">
						AND DISPLAY_ID = # {displayId}
					</if>
					<if test="insId != null and insId != ''">
						AND INS_ID = # {insId}
					</if>			
				</trim>
				<include refid="common.pagingPost" />
			</select>
			
	7. SAMPLE SOURCE
		url : /samplePage/excelGrid
		
</xmp>
</p>
</div>

</body>
</html>

