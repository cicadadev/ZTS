var cornerManagerApp = angular.module("cornerManagerApp", ['commonServiceModule', 'dmsServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule', 'ngCkeditor', 'ui.date']);


Constants.message_keys = ["common.label.confirm.save",
                          "common.label.alert.save",
                          "common.label.alert.delete",
                          "common.label.confirm.delete",
                          "common.label.alert.cancel",
                          "common.label.confirm.cancel"];

cornerManagerApp.controller("dms_cornerManagerApp_controller", function($compile, $window, $scope,commonPopupService, productService, cornerService, commonService, displayTreeService, gridService, commonFactory, displayService) {
		
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.trees = [];//전시 코너 목록 ( 좌측 트리 )
	
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
//	$scope.selectDisplayCorner = false;
	
	var gridName = "grid_cornerItemList";
	
	$scope.search = {};
	$scope.girdView = true;
//	$scope.dmsDisplay = {};
	
	//아이템 등록 팝업 (상품, 기획전, 이미지, html)
	this.openItemRegPopup = function(itemType){
		var param = {
			displayId : $scope.dmsDisplay.displayId,
			displayItemDivId : null,
			gridName : 'myGrid',
			itemType : itemType
		};
		
		delete $scope.search.displayItemNo;
		
		displayService.initCornerItemReg($scope, param);
	}
	
	
	//트리 재조회 & 현재 노드 선택
	$scope.reloadTreeAndSelectItem = function(node){	
		
		// 트리조회
		cornerService.getDisplayTreeList(function(response) {
			$scope.trees = response;
			
			if(!common.isEmptyObject(node)){
				// 트리에서 현재 노드 선택
				displayTreeService.selectTreeItem($scope.trees, node.displayId, node.upperDisplayId, "displayId", "upperDisplayId");
					
				// 카테고리 상세 조회
				$scope.getCornerDetail(null, node.displayId);
			}
				
			common.safeApply($scope);
			
		});		
	}
	
	
	// 등록 취소
	$scope.cancelReg = function(){
		if(!confirm("등록을 취소 하시겠습니까?")){
			return;
		}
		displayTreeService.deleteNewNode($scope.trees, "displayId");
		$scope.regFlag = false;// 등록중 플래그 false
		$scope.dmsDisplay = null;//선택객체 초기화
	}
	
	//좌측 트리 조회
//	this.getDisplayTreeList = function(){	
//		cornerService.getDisplayTreeList(function(data){	
//			 $scope.trees = data;
//		});	
//	}
	//트리에서 코너 클릭
	$scope.getCornerDetail = function(e, displayId){
		
		// 등록중인 카테고리 선택시
		if(common.isEmpty(displayId)){
			return;
		}
		
		// 카테고리를 등록중일 때 확인 
		if($scope.regFlag && !confirm("전시코너 등록을 취소하시겠습니까?")){
			return;
		}
		
		//등록중이던 카테고리가 있으면 삭제
		if($scope.regFlag){
			displayTreeService.deleteNewNode($scope.trees, "displayId");
			$scope.regFlag = false;
		}
		
		// 클릭한 노드 선택
		if(e && !$(e.target).hasClass("active")){
			$(".category .list_dep a").removeClass("active");
			$(e.target).addClass("active");
		}
		
		
		//코너 상세 조회
		cornerService.getCornerDetail({displayId : displayId}, function(data){
		    $scope.dmsDisplay = data;
		    
//		    $scope.selectDisplayCorner = true;//영역 활성화
		    
			$scope.girdView = !common.isEmpty(data.displayItemTypeCd);
			$scope.search.displayId = displayId;
//			alert(data.displayItemNo)
//			$scope.search.displayItemNo = data.displayItemNo;
			
		    if($scope.girdView && !common.isEmpty($scope.dmsDisplay.displayId)){// 아이템 타입이 존재할 경우 그리드 노출
		    
		    	var gridUrl;
			    var columnDefs = [];
			    	
			    var gridUrl =  displayService.getCornerItemUrl(data.displayItemTypeCd);
			    var columnDefs = displayService.getCornerItemColDef($scope, data.displayItemTypeCd);
			    
		    	var gridParam = {
		    			scope : $scope,
		    			gridName : gridName,		//그리드명
		    			url :  gridUrl,				//기본 URL
		    			searchKey : "search",		//검색 객체명
		    			columnDefs : columnDefs,
		    			gridOptions : {				 //사용자 옵션
		    				// Input Hidden
		    				isRowHidetable : function(row) {
		    					var hideable = true;
//		    					if (row.entity.insId != null && row.entity.insId != '') {
//		    						hideable = false;
//		    					}
		    					if (hideable) {
		    						row.cursor = 'pointer';
		    					}
		    					return hideable;
		    				}
		    			},
		    			callbackFn : function(){	//콜백함수 
		    				$scope.myGrid.loadGridData();
		    			}
		    	};
		    	
		    	$scope.myGrid = new gridService.NgGrid(gridParam);
		    	
		    }// if($scope.girdView) end
		});
	
	    
	}
	
	
	// 등록 버튼 클릭
	this.register = function() {
		
		if(common.isEmptyObject($scope.dmsDisplay)){
			alert('상위 전시코너를 선택해 주세요.');
			return;
		}
//		if($scope.dmsDisplay.leafYn=='Y'){
//			alert("리프여부가 Y에는 하위 전시코너를 등록 할 수 없습니다.");
//			return;
//		}
		//  코너번호가 없으면 등록중 화면 ( 등록중인 코너 선택시 무반응 처리)
		if(common.isEmpty($scope.dmsDisplay.displayId)){
			return;
		}
		
		$scope.regFlag = true;// 등록중 플래그
		
		
		// 부모 노드 ID
		var upperTreeId = $scope.dmsDisplay.displayId;
		
		// 등록 화면 기본값 설정
		$scope.dmsDisplay = { 
			upperDisplayId : upperTreeId,
			useYn : 'Y',
			displayYn : 'Y',
			leafYn : 'N'
		};
		
		// 신규 노드 삽입
		var newTree = {
				displayId : null,
			upperDisplayId : upperTreeId,
			name : "신규 노드"// 트리에서 보일 임시노드 명
		};
		displayTreeService.insertNewNode($scope.trees, newTree, "displayId", "upperDisplayId");
		
	}
	
	// 에러방지를 위해 임시 그리드 선언
	new gridService.NgGrid({ scope : $scope,  gridName : gridName, searchKey : "search",  columnDefs : [] });
	

	//카테고리 하위 열기/닫기 
	this.openFolder = function(index, icon){
		displayTreeService.openTree(index, icon, $scope.trees, "displayId", "upperDisplayId");
	}
	
	//전시코너 수정
	this.updateCorner = function(){
		
		
		//폼 체크
		if(!commonService.checkForm($scope.form2)){
			return;
		}
		
		function reload(dmsDisplay){
			
			// 트리 재구성
			$scope.reloadTreeAndSelectItem(dmsDisplay);

			// 등록중 플래그 false
			$scope.regFlag = false;
		}
		
		if(common.isEmpty($scope.dmsDisplay.displayId)){
			if (!confirm("코너를 생성하시겠습니까?")) {
				return;
			}
	        //DB에 코너 등록
	        cornerService.insertCorner($scope.dmsDisplay, function(data){
				
				//insert callback
	        	$scope.dmsDisplay.displayId = data.content;
	        	alert("코너가 생성되었습니다.");
	        	reload($scope.dmsDisplay);
	        })
		}else{
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
			
			cornerService.updateCorner($scope.dmsDisplay, function(data){
				if(data.success){
					alert($scope.MESSAGES["common.label.alert.save"]);
					reload($scope.dmsDisplay);
				}else{
					alert(data.resultMessage);
				}
				 
			});
		}

	}
	
	// 등록 팝업 호출
//	this.openPopupInsert = function(){
//		if(!$scope.dmsDisplay){
//			alert('전시 코너를 선택해 주세요');
//			return;
//		}
//		
//		var leafYn = $scope.dmsDisplay.leafYn;
//		if("Y"==leafYn){
//			alert('Leaf에 전시코너를 추가할 수 없습니다.');
//			return;
//		}
////		console.log($scope.dmsDisplay.displayId)
//		var url = "/dms/corner/popup/insert";
//		popupwindow(url, "코너 등록", 850, 360);
//	}
	
	
	//삭제전 체크
	this.checkDeleteCorner = function(displayId){
		var param = {displayId : displayId};
		cornerService.checkDeleteCorner(param, function(data){
			 if(data.success){
				 if (!confirm($scope.MESSAGES["common.label.confirm.delete"])) {
					 return;
				 }
				 $scope.deleteCorner(displayId);
			 }else if(data.resultCode=='dms.display.delete.content'){// 매핑 컨텐츠 존재
				 if(confirm(data.resultMessage)){
					 $scope.deleteCorner(displayId);
				 }
			 }else{
				 alert(data.resultMessage);
			 }
		});
	}
	
	
	//코너 삭제
	$scope.deleteCorner = function(displayId){
		var param = {displayId : displayId};

		cornerService.deleteCorner(param, function(data){
			alert($scope.MESSAGES["common.label.alert.delete"]);
		 
			$scope.dmsDisplay = null;
			$scope.reloadTreeAndSelectItem($scope.dmsDisplay);
		});
	}
	
	$scope.saveGridData = function(){
		$scope.myGrid.saveGridData(null, function(){
//			$scope.myGrid.loadGridData();
			
			$scope.myGrid.loadGridData();
			common.safeApply($scope);
			// 트리 재구성
//			$scope.reloadTreeAndSelectItem($scope.dmsDisplay);
			
		});
	}
	
	// 그리드 리로드
//	$scope.reloadItemGrid = function(){
//		$scope.myGrid.loadGridData();
//		common.safeApply($scope);
//	}
	
	// 로딩시 트리 조회
	$scope.reloadTreeAndSelectItem(null);
	

	
})

// image 등록/상세 팝업 : 전시코너, 전시카테고리에서 공통 사용됨
cornerManagerApp.controller("imgDetailController", function($window, $scope, cornerService, commonService, displayTreeService) {
	
	$scope.dmsDisplayitem = {};
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	this.close = function(){
		$window.close();
	}
	
	// 저장
	this.save = function(){
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		if(!$scope.dmsDisplayitem.img1 && !$scope.dmsDisplayitem.img2){
			alert("유효하지 않는 항목이 있습니다.");
			return;
		}
		/*if(!$scope.dmsDisplayitem.url1 && !$scope.dmsDisplayitem.url2){
			alert("유효하지 않는 항목이 있습니다.");
			return;
		}*/
		if(!$scope.dmsDisplayitem.startDt || !$scope.dmsDisplayitem.startDt){
			alert("유효하지 않는 항목이 있습니다.");
			return;
		}

		if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
			return;
		}

		
		pScope = $window.opener.$scope;//부모창의 scope
		$scope.dmsDisplayitem.displayId = pScope.search.displayId;
		$scope.dmsDisplayitem.displayItemDivId = pScope.search.displayItemDivId;
		
		cornerService.saveCornerItem($scope.dmsDisplayitem, function(){
			// 그리드 리로드
			alert($scope.MESSAGES["common.label.alert.save"]);
			
			$scope.search = {};
			
			pScope.reloadItemGrid();
			
			$window.close();//팝업 닫기 ==> 맨 마지막에 수행해야함
			
		});

	}
	
	
	// 상세일경우 데이터 조회 
	$scope.search = {};
	this.init = function(){
		pScope = $window.opener.$scope;
		
		$scope.search.displayId = pScope.search.displayId;
		$scope.search.displayItemNo = pScope.search.displayItemNo;
		
		if(common.isEmpty($scope.search.displayId && $scope.search.displayItemNo)){
			return;
		}
		cornerService.getDisplayItem($scope.search, function(response) {
			$scope.dmsDisplayitem = response;
		});
	}
	
	
	
}).controller("htmlDetailController", function($window, $scope, cornerService, commonService, displayTreeService) {// html 등록/상세 팝업
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	//CK Editor 설정
	$scope.ckPcOption = {
		language : 'ko',								//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}

	$scope.ckMobileOption = {
		language : 'ko',								//	en, ja(일본어), ko, zh(중국어), zh-cn(중국어 간체)
		filebrowserImageUploadUrl : Rest.context.path + '/api/ccs/common/ckUpload'		// 이미지 섹션 - 업로드 탭 추가
	}
	
	this.close = function(){
		$window.close();
	}
	
	// 상세일경우 데이터 조회 
	$scope.search = {};
	this.init = function(){
		pScope = $window.opener.$scope;
		
		$scope.search.displayId = pScope.search.displayId;
		$scope.search.displayItemNo = pScope.search.displayItemNo;
		
		if(common.isEmpty($scope.search.displayId && $scope.search.displayItemNo)){
			return;
		}
		cornerService.getDisplayItem($scope.search, function(response) {
			$scope.dmsDisplayitem = response;
		});
	}
	
	// 저장
	this.save = function(){
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		if(!$scope.dmsDisplayitem.startDt || !$scope.dmsDisplayitem.startDt){
			alert("유효하지 않는 항목이 있습니다.");
			return;
		}
		
		if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
			return;
		}

		
		pScope = $window.opener.$scope;//부모창의 scope
		$scope.dmsDisplayitem.displayId = pScope.search.displayId;
		$scope.dmsDisplayitem.displayItemDivId = pScope.search.displayItemDivId;
		
		cornerService.saveCornerItem($scope.dmsDisplayitem, function(){
			// 그리드 리로드
			alert($scope.MESSAGES["common.label.alert.save"]);
			
			$scope.search = {};
			
			pScope.reloadItemGrid();
			
			$window.close();//팝업 닫기 ==> 맨 마지막에 수행해야함
			
		})

	}
	
	
	
}).controller("textInsertController", function($window, $scope, cornerService, commonService, displayTreeService) {
	$scope.dmsDisplayitem = {};
	this.close = function(){
		$window.close();
	}
	
	// 신규 코너 등록 ( 팝업에서 호출 )
	this.insert = function(){
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return;
		}
		
		if(!$scope.dmsDisplayitem.startDt || !$scope.dmsDisplayitem.endDt){
			alert("전시기간을 입력해 주세요.");
			return;
		}

		pScope = $window.opener.$scope;//부모창의 scope
        
        //아이템 추가
		pScope.addItem($scope.dmsDisplayitem);
		
        $window.close();//팝업 닫기 ==> 맨 마지막에 수행해야함

	}
	
});