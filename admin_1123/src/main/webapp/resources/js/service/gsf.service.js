
/***********************************************************************/
/** 공통 Service                                                      **/
/***********************************************************************/



commonServiceModule.service('commonService',   
	function(restFactory, $filter, $http, commonFactory) {
		return {
			checkForm : function(form){//폼 체크
				if(form.$invalid){
					alert("유효하지 않은 항목이 존재합니다.");
					return false;
				}
				return true;
			},
			// 화면에서 사용할 메세지 로딩
			getMessages : function(callback) {
				var url = Rest.context.path + "/api/ccs/common/messages";
				var param = null;
				if (Constants.message_keys.length == 0) {
					return {};
				} else {
					return restFactory.transaction(Rest.method.POST,
							Rest.responseType.SINGLE, url, param,
							Constants.message_keys, callback);
				}
			},
			getCodeList : function(param) {
				var url = Rest.context.path + "/api/ccs/common/codegroup";
				return restFactory.transaction2(Rest.method.GET, Rest.responseType.MULTI, url, param, null);
			},
			init_search : function($scope,ngModel){
				$scope["org_"+ngModel] = angular.copy($scope[ngModel]);
				$scope["org_"+ngModel].startDateInput = $scope[ngModel].startDate;
				$scope["org_"+ngModel].endDateInput = $scope[ngModel].endDate;
				
			},
			
			reset_search : function($scope,ngModel){		
				$(".btn_day").siblings().removeClass("on");
				
				$scope[ngModel] = angular.copy($scope["org_"+ngModel]);
				
				// 체크박스 초기화
//				angular.element("input[type='checkbox']").each(function(idx,obj) {
//					var name = $(this)[0].getAttribute("ng-model");
//					if(angular.isDefined($scope[name])) {
//						$scope[name] = false;
//					}
////					else {
////						
////						$(this).prop("checked",false);
//////						console.log(name);
//////						if(name != 'label.selected') {
//////							console.log($(this));
//////						}
////					}
//					
//				});
				
				common.safeApply($scope);
//				$(".tb_type1").find("input[type='checkbox']").each(function(idx,obj){
//					$(this).prop("checked",false);
//				}) 
				
//				var directiveComp = $scope.directiveList;
//				angular.forEach(directiveComp,function(value,key){
//					$scope.search_temp[value+'Selected'] = [];	
//					
//					$scope.search_temp[value+"All"] = false;
//					
//					angular.forEach($scope.search_temp[value+"CdList"], function (item) {
//					    item.selected = false;
//					});
//					
//				})
			},
			getGridField : function( data ) {
				var url = Rest.context.path + "/api/ccs/common/grid/field"; 
				var param = null;
				if (data.length == 0) {
					return {};
				} else {
					return restFactory.transaction2(Rest.method.POST, Rest.responseType.SINGLE, url, param, data);
				}
			},
			exportExcel : function( url, data, callback) {
				var url = Rest.context.path + url
				var param = null;
				
				restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
			},
			uploadFileToUrl : function(file, pKey, url, callback) {
//				console.log("file >> ", file);
				var fd = new FormData();
				var decodedString = "";
				fd.append('file', file);
				fd.append('pKey', pKey);
				
				//console.log("fd >>", fd );
				$http.post(url, fd, {
					withCredentials : false,
					transformRequest : angular.identity,
					headers : {
						'Ajax' : "Y",
						'Content-Type' : undefined
					},
					responseType: "arraybuffer"
				}).success(function(response, status, headers, config) {
			        
					
					callback(common.arraybufferToStr(response));

			        
				}).error(function(error, status, headers, config) {
					alert("Fail");
					console.log(error.stack);
				});
			},
			/**
			 * 선택한 코드에대한 이름
			 * 
			 * commonService.getValueToName($scope,"altProc.productStateCd");
			 */
			getValueToName : function(scope, ngModel){
				var name = ngModel.replace(/\./g,"") + "List";
//				console.log(name);
//				console.log(scope.search_temp);
				var data = scope.search_temp[name];
				var names = ngModel.split(".");
				var value = scope;
				for(var j=0;j<names.length;j++){
					value = value[names[j]];
				}				
				for(var i=0;i<data.length;i++){
					if(data[i].cd == value){
						return data[i].name;
					}
				}
			},
//			getAuthFunction : function($scope,callback){
//				var pageId = $("#pageId").val();
//				if(common.isEmpty(pageId) && !common.isEmpty($scope.pageId)){
//					pageId = $scope.pageId;
//				}
//				if(angular.isDefined(pageId)){
//					var url = Rest.context.path + "/api/ccs/menu/function/auth/list"; 
//					var param = {pageId : pageId};
//					
//					restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, null, callback);	
//				}								
//				
//			},
			
//			TODO 권한 pageId -> fnId
			getAuthFunction : function($scope,callback){
				var fnId = $scope.fnId;
				if(angular.isDefined(fnId)){
					var url = Rest.context.path + "/api/ccs/menu/function/auth/list"; 
					var param = {fnId : fnId};
					restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, null, callback);	
				}
			},
			getGridName : function(grid){
				var gridName = "";
				if(angular.isDefined(grid.element[0].attributes['ui-grid'])){
					gridName = grid.element[0].attributes['ui-grid'].value;
				}
				else if(angular.isDefined(grid.element[0].attributes['data-ui-grid'])){
					gridName = grid.element[0].attributes['data-ui-grid'].value;
				}
				
//				var dataUiGrid = grid.element[0].attributes['ui-grid'].value;
//				if(angular.isDefined(dataUiGrid)){
//					gridName = dataUiGrid;
//				}else{
//					gridName = grid.element[0].attributes['ui-grid'].value;
//				}
				return gridName;
			}		
			//파일 삭제
			,deleteFile : function(fullPath, callback){
				var data = {fullPath : fullPath}
				var url = Rest.context.path+"/api/ccs/common/file/delete";
				return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null , data,  callback);
			}
			,getConfig : function(key, callback) {
				var param = {key : key};
				var url = Rest.context.path + "/api/ccs/common/configuration";
				return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, null, callback);
			}
			, getDistrictList : function(data, callback){
				var url = Rest.context.path+"/api/ccs/common/districtList";
				return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null , data,  callback);
			}
			, searchAddress : function(data, callback){
				var url = Rest.context.path+"/api/ccs/common/addressList";
				return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null , data,  callback);
			},
			// using commonly
			selectList : function(data, url, callback) {
				return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
			}, 
			selectOne : function(data, url, callback) {
				return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, null, data, callback);
			}, 
			selectValue : function(data, url, callback) {
				return restFactory.transaction(Rest.method.GET, Rest.responseType.TEXT, url, null, data, callback);
			}, 
			insert : function(data, url, callback) {
				return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
			}, 
			update : function(data, url, callback) {
				return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, null, data, callback);
			}
			
		}
	}
).service("displayTreeService", function(){//트리 서비스
	
	return{
		// 하위 열기
		openTree : function(index, icon, baseTree, treeColumnName, treeUpperCoulmnName){
			if($.type(icon)=='undefined'){
				icon = "";
			}
			else if(icon=="end"){
				return;
			}
			
			var selected = baseTree[index];
			if(icon=="on"){//자식 펼치기
				selected.icon="";//자신 
			}else{
				selected.icon="on";//자신
			}
			for(var i = index + 1 ; i < baseTree.length ; i++){
				
				
				var item = baseTree[i];
				if(icon=="on"){//자식 접기
					if(selected.depth < item.depth){
						item.show = "N";
						if(item.leafYn!="Y"){
							item.icon="";
						}
					}else{
						i = baseTree.length + 1;//break
					}

				}else{// 하위 펼치기
					if(item[treeUpperCoulmnName] == selected[treeColumnName]){//자식일 경우
						item.show="Y";
					}
				}
			}
		},
		//트리에 노드 추가
		insertNodeToTree : function(baseTree, newTree, treeColumnName, treeUpperCoulmnName){
			
			if(baseTree.length> 0 ){
				var pushIndex = 0;
				for(var i = 0 ; i < baseTree.length; i++){
					
					//부모일경우 부모 index 구하기
					if( baseTree[i][treeColumnName]==newTree[treeUpperCoulmnName]){
						
						newTree.depth = parseInt(baseTree[i].depth) +1;						
						
						pushIndex = i + 1;
						// 부모의 아이콘
						if(baseTree[i].icon=='on'){
							newTree.show = "Y";
						}
						continue;
					 }
					
					//형제일경우 삽입될 INDEX구하기
					if( baseTree[i][treeUpperCoulmnName]==newTree[treeUpperCoulmnName]){
						// 내동생이 있으면 바로 위가 내자리
						 if(baseTree[i].sortNo >= newTree.sortNo){
							 pushIndex = i;
							 break;
						 }else{// 
							 pushIndex = i + 1;
						 }
					 }
				}
				
				baseTree.splice(pushIndex, 0 , newTree);
			}else{
				baseTree.push(newTree);
			}
			
		},
		// ID가 없는노드(등록중인 노드) 삭제
		deleteNewNode : function(baseTree, treeColumnName){
			for(var i = baseTree.length -1 ; i >= 0; i--){
				if(common.isEmpty(baseTree[i][treeColumnName])){// ID가 없는 노드는 삭제
					baseTree.splice(i, 1);
				}
			}
		},
		//트리에 신규노드 추가
		insertNewNode : function(baseTree, newTree, treeColumnName, treeUpperCoulmnName){
			
			
//			for(var i = baseTree.length -1 ; i >= 0; i--){
//				if(common.isEmpty(baseTree[i][treeColumnName])){// ID가 없는 노드는 삭제
//					baseTree.splice(i, 1);
//				}
//			}
			// 기존 신규 노드 삭제
			this.deleteNewNode(baseTree, treeColumnName);
			
			if(baseTree.length> 0 ){
				var pushIndex = -1;
				for(var i = 0 ; i < baseTree.length; i++){
					var curDepth = parseInt(baseTree[i].depth);
					//부모일경우 부모 index 구하기
                    if( baseTree[i][treeColumnName]==newTree[treeUpperCoulmnName]){
						newTree.depth = curDepth +1;						
						pushIndex = i + 1;
					}
					
					//형제일경우 삽입될 INDEX구하기
					else if( baseTree[i][treeUpperCoulmnName]==newTree[treeUpperCoulmnName]){
						 pushIndex = i + 1;
					}
                    // 부모를 찾았는데 내 depth보다 작으면 다른 부모
					else if( pushIndex >= 0 && curDepth < newTree.depth  ){
						pushIndex = i;
						break;
                    }
                    // 부모를 찾았고, 형제의 자식들일 경우 
					else if( pushIndex >= 0 && curDepth > newTree.depth  ){
						pushIndex = i + 1;
                    }
				    	
                }
				baseTree.splice(pushIndex, 0 , newTree);
			}else{
				baseTree.push(newTree);
			}
			
			//노드 선택
			this.selectTreeItem(baseTree, null, newTree[treeUpperCoulmnName], treeColumnName, treeUpperCoulmnName);
			
		},
		// 트리에서 노드 삭제
		deleteNodeTree : function(baseTree, treeColumnName, deleteTreeId){
			if (baseTree.length > 0) {
				
				for(var i = 0 ; i < baseTree.length; i++){
					if (baseTree[i][treeColumnName] == deleteTreeId) {
						baseTree.splice(i, 1);
					}
				}
			} else {
				baseTree.push(null);
			}
		},
		// 노드 선택 : 선택한 노드를 활성화하고 부모 노드들을 활성화 한다.
		selectTreeItem : function(tree, targetId, upperTargetId, treeColumnName, treeUpperCoulmnName){
			
			// 기존 선택 영역 해제
			$(".category .list_dep a").removeClass("active");
			
			var callCnt = 0;
			// 
			function setNode(tree, upperTargetId, startIndex){
				
				for(var i = startIndex ; i >= 0 ; i--){
					
					//형제들이면 폴더 show
					if(tree[i][treeUpperCoulmnName] == upperTargetId){
						tree[i].show = "Y";
					}
					
					// 부모이면 show & 아이콘 열기
					if(tree[i][treeColumnName] == upperTargetId){
						tree[i].show = "Y";
						tree[i].icon = "on";
						if(!common.isEmpty(tree[i][treeUpperCoulmnName])){
							setNode(tree, tree[i][treeUpperCoulmnName], startIndex);
						}
						
						break;
					}
				}
				
			}
			var startIndex = tree.length -1;
			var selectedNodeObject = null;
			// 선택한 항목의 형제, 부모 활성화
			for(var i = startIndex ; i >= 0 ; i--){
				
				tree[i].active = "";
				
				//자신이면 색깔
				if(tree[i][treeColumnName] == targetId){
					tree[i].show = "Y";
					tree[i].active = "active";
					selectedNodeObject = tree[i];
				}
				
				//형제들이면 폴더 show
				if(tree[i][treeUpperCoulmnName] == upperTargetId){
					tree[i].show = "Y";
				}
				
				// 부모이면 show & 아이콘 열기
				if(tree[i][treeColumnName] == upperTargetId){
					tree[i].show = "Y";
					tree[i].icon = "on";
						
					// 부모의 형제 활성화
					if(!common.isEmpty(tree[i][treeUpperCoulmnName])){
						setNode(tree, tree[i][treeUpperCoulmnName], startIndex);
					}
					
					break;
				}
			}
			
			
			return selectedNodeObject;
		}
	}
	
	
});

/***********************************************************************/
/** 공통 Config                                                       **/
/***********************************************************************/

commonServiceModule.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.xsrfCookieName = '_csrf';
	$httpProvider.defaults.xsrfHeaderName = 'X-XSRF-Token';
	
	var loadingCnt = 0;
	 
	$httpProvider.interceptors.push(function($q, $rootScope, $location) {
		return {
			'request' : function(config) {
				
				if(loadingCnt == 0){					
					$("#loadingBar").show();
					
					//$rootScope.$broadcast("loadingOn");
				}				
				loadingCnt++;
				
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				
				if("" != token){
					config.headers[header] = token;	
				}
				
				config.headers['_system_type'] = $("meta[name='_system_type']").attr("content");
				

				return config || $q.when(config);
			},
			'response' : function (response) {

				loadingCnt--;
				if(loadingCnt == 0){					
					$("#loadingBar").hide();
					//$rootScope.$broadcast("loadingOff");
				}
				
	            return response || $q.when(response);

	        },
			'responseError' : function(rejection) {
				
				loadingCnt--;
				if(loadingCnt == 0){
					$("#loadingBar").hide();
					//$rootScope.$broadcast("loadingOff");
				}
				 
				if(rejection.status == 901){
					alert("Session Timeout!!!");
					parent.parent.location.href = "/ccs/login";					
				}else{
					var data = rejection.data;
					
					//alert(data);					
				}
				return $q.reject(rejection);
				
			}
		}
	});	
	
} ]);