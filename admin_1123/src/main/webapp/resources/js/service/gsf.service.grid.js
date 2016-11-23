/** ******************************************************************** */
/** 그리드 템플릿 * */
/** ******************************************************************** */
angular.module('ui.grid').run(['$templateCache', function($templateCache) {
	'use strict';
	$templateCache.put('custom/checkBoxHeader', '<div class="check_header"><input type="checkbox" ng-click="grid.appScope.checkBoxAll(grid, $event)" ng-style="{\'cursor\': \'pointer\'}"/></div>');

	// $templateCache.put('custom/uiGridHeaderEditableCell',
	// "<div role=\"columnheader\" ng-class=\"{ 'sortable': sortable }\" ui-grid-one-bind-aria-labelledby-grid=\"col.uid + '-header-text ' + col.uid + '-sortdir-text'\" aria-sort=\"{{col.sort.direction == asc ? 'ascending' : (
	// col.sort.direction == desc ? 'descending' : (!col.sort.direction ? 'none' : 'other'))}}\"><div role=\"button\" tabindex=\"0\" class=\"ui-grid-cell-contents ui-grid-header-cell-primary-focus\" col-index=\"renderIndex\"
	// title=\"TOOLTIP\"><span class=\"ui-grid-header-cell-label\" ui-grid-one-bind-id-grid=\"col.uid + '-header-text'\">{{ col.displayName CUSTOM_FILTERS }}<br/>(수정가능)</span> <span ui-grid-one-bind-id-grid=\"col.uid +
	// '-sortdir-text'\" ui-grid-visible=\"col.sort.direction\" aria-label=\"{{getSortDirectionAriaLabel()}}\"><i ng-class=\"{ 'ui-grid-icon-up-dir': col.sort.direction == asc, 'ui-grid-icon-down-dir': col.sort.direction ==
	// desc, 'ui-grid-icon-blank': !col.sort.direction }\" title=\"{{isSortPriorityVisible() ? i18n.headerCell.priority + ' ' + ( col.sort.priority + 1 ) : null}}\" aria-hidden=\"true\"></i> <sub
	// ui-grid-visible=\"isSortPriorityVisible()\" class=\"ui-grid-sort-priority-number\">{{col.sort.priority + 1}}</sub></span></div><div role=\"button\" tabindex=\"0\" ui-grid-one-bind-id-grid=\"col.uid + '-menu-button'\"
	// class=\"ui-grid-column-menu-button\" ng-if=\"grid.options.enableColumnMenus && !col.isRowHeader && col.colDef.enableColumnMenu !== false\" ng-click=\"toggleMenu($event)\"
	// ng-class=\"{'ui-grid-column-menu-button-last-col': isLastCol}\" ui-grid-one-bind-aria-label=\"i18n.headerCell.aria.columnMenuButtonLabel\" aria-haspopup=\"true\"><i class=\"ui-grid-icon-angle-down\"
	// aria-hidden=\"true\">&nbsp;</i></div><div ui-grid-filter></div></div>"
	// );

	$templateCache.put('custom/uiGridHeaderEditableCell',"<div role=\"columnheader\" ng-class=\"{ 'sortable': sortable }\" ui-grid-one-bind-aria-labelledby-grid=\"col.uid + '-header-text ' + col.uid + '-sortdir-text'\" aria-sort=\"{{col.sort.direction == asc ? 'ascending' : ( col.sort.direction == desc ? 'descending' : (!col.sort.direction ? 'none' : 'other'))}}\"><div role=\"button\" tabindex=\"0\" class=\"ui-grid-cell-contents edit_cell ui-grid-header-cell-primary-focus\" col-index=\"renderIndex\" title=\"TOOLTIP\"><span class=\"ui-grid-header-cell-label\" ui-grid-one-bind-id-grid=\"col.uid + '-header-text'\">{{ col.displayName CUSTOM_FILTERS }}</span> <span ui-grid-one-bind-id-grid=\"col.uid + '-sortdir-text'\" ui-grid-visible=\"col.sort.direction\" aria-label=\"{{getSortDirectionAriaLabel()}}\"><i ng-class=\"{ 'ui-grid-icon-up-dir': col.sort.direction == asc, 'ui-grid-icon-down-dir': col.sort.direction == desc, 'ui-grid-icon-blank': !col.sort.direction }\" title=\"{{isSortPriorityVisible() ? i18n.headerCell.priority + ' ' + ( col.sort.priority + 1 )  : null}}\" aria-hidden=\"true\"></i> <sub ui-grid-visible=\"isSortPriorityVisible()\" class=\"ui-grid-sort-priority-number\">{{col.sort.priority + 1}}</sub></span></div><div role=\"button\" tabindex=\"0\" ui-grid-one-bind-id-grid=\"col.uid + '-menu-button'\" class=\"ui-grid-column-menu-button\" ng-if=\"grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false\" ng-click=\"toggleMenu($event)\" ng-class=\"{'ui-grid-column-menu-button-last-col': isLastCol}\" ui-grid-one-bind-aria-label=\"i18n.headerCell.aria.columnMenuButtonLabel\" aria-haspopup=\"true\"><i class=\"ui-grid-icon-angle-down\" aria-hidden=\"true\">&nbsp;</i></div><div ui-grid-filter></div></div>");
	$templateCache.put('custom/uiGridLinkCell', "<div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><a style=\"text-decoration: underline\" href=\"#none\" ng-click=\"grid.appScope.openProductPopup('{{grid.getCellValue(row, col)}}')\">{{COL_FIELD CUSTOM_FILTERS}}</a></div>");
	$templateCache.put('custom/imageCell', "<img ng-src=\"{{grid.getCellValue(row, col)}}\" lazy-src >");
}]);

var gridServiceModule = angular.module("gridServiceModule", [
		'ui.grid', 'ui.grid.cellNav', 'ui.grid.edit', 'ui.grid.rowEdit', 'ui.grid.selection', 'ui.grid.pagination', 'ui.grid.validate', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.autoResize'
]);

gridServiceModule.service('gridService', function(restFactory, $filter, $interval, uiGridValidateService, uiGridRowEditService, commonFactory, commonService, uiGridConstants) {

	return {

		/**
		 * 그리드 생성 클래스
		 * 
		 * @param param {scope - Scope 객체 ,gridName - 그리드 명 ,url - 그리드 데이터 조회 URL ,searchKey - scope 객체에서 검색 object key ,columnDefs - 컬럼 정의 ,gridOptions - 그리드 옵션값 객체(optional) ,callbackFn - 콜백함수(optional)}
		 */
		NgGrid : function(param) {

			if (!param.scope[param.gridName]) {
				param.scope[param.gridName] = {};
			}

			// 디폴트 옵션
			param.scope[param.gridName].rowEditWaitInterval = -1;
			param.scope[param.gridName].enableRowHeaderSelection = false;
			param.scope[param.gridName].enableRowSelection = angular.isDefined(param.scope[param.gridName].enableRowSelection) ? param.scope[param.gridName].enableRowSelection : false;

			param.scope[param.gridName].checkMultiSelect = true;
			param.scope[param.gridName].multiSelect = false;
			param.scope[param.gridName].enableColumnMenus = false;
			param.scope[param.gridName].checkBoxEnable = true; // 체크박스 여부
			param.scope[param.gridName].enableCellEdit = false;

			angular.extend(param.scope[param.gridName], param.gridOptions);

			// 페이징은 true가 디폴트
			if (param.scope[param.gridName].pagination == false) {

			} else {
				param.scope[param.gridName].pagination = true;
				if(!angular.isDefined(param.scope[param.gridName].paginationPageSizes)){
					param.scope[param.gridName].paginationPageSizes = [100, 200, 500];
					param.scope[param.gridName].paginationPageSize = 100;
				}
				param.scope[param.gridName].useExternalPagination = true;
			}

			param.scope[param.gridName].columnDefs = [];
			param.scope[param.gridName].totalItems = 0;
			param.scope[param.gridName].tempData = [];

			// 체크박스 disable && row selection 관련하여 추가.
			if (!angular.isDefined(param.scope[param.gridName].isRowSelectable)) {
				param.scope[param.gridName].isRowSelectable = function(row) {
					return true;
				}
			}

			var paramColDef = param.columnDefs;

			var param_scope = param.scope;
			var param_gridName = param.gridName;
			var param_url = param.url;
			var param_searchKey = param.searchKey;
			var param_callbackFn = param.callbackFn;
			var param_style = param.style;

			if (!angular.isDefined(param_scope[param_searchKey])) {
				// searchScope = param_scope[param_searchKey];
				// }else{
				param_scope[param_searchKey] = {};
			}

			// 데이터 클린
			param.scope[param.gridName].data = angular.copy(param.scope[param.gridName].tempData);

			// 수정한 rows 조회
			var getDirtyRows = function() {

				if (param.scope[param.gridName].gridApi && param.scope[param.gridName].gridApi.rowEdit) {
					var dirtyRows = param.scope[param.gridName].gridApi.rowEdit.getDirtyRows();
					var dataRows = dirtyRows.map(function(gridRow) {
						return gridRow.entity;
					});
					return dataRows;
				} else {
					return [];
				}
			}

			// 그리드 데이터 값
			this.getData = function() {
				return param.scope[param.gridName].data;
			}

			// 그리드 데이터 값
			this.setData = function(newData, isSetDirty) {

				cleanDirtyRows();

				param.scope[param.gridName].data = newData;

				// param.scope.$apply();
				if (isSetDirty) {
					common.safeApply(param.scope, function() {
						$interval(function() {
							param.scope[param.gridName].gridApi.rowEdit.setRowsDirty(param.scope[param.gridName].data);
						}, 1, 1);
					});
				}else{
					common.safeApply(param.scope);
				}


			}

			// Get Search Parameter
			// this.getSearchParam = function(){
			// return param_scope[param_searchKey];
			// }

			// Get Grid Scope
			this.getGridScope = function() {
				return param.scope[param.gridName];
			}

			// 그리드 행 추가
			this.addRow = function(data) {

				if (angular.isUndefined(param.scope[param.gridName].data)) {
					param.scope[param.gridName].data = [];
				}
				data.newRow = true;
				param.scope[param.gridName].data.unshift(data);

				common.safeApply(param.scope, function() {
					$interval(function() {
						param.scope[param.gridName].gridApi.rowEdit.setRowsDirty([
							data
						]);
					}, 1, 1);
				})

			};

			// index row의 field에 해당하는 데이터를 변경
			this.changeData = function(index, field, data) {

				if (angular.isUndefined(param.scope[param.gridName].data)) {
					return;
				}

				param.scope[param.gridName].data[index][field] = data;

				common.safeApply(param.scope, function() {
					$interval(function() {
						param.scope[param.gridName].gridApi.rowEdit.setRowsDirty([
							param.scope[param.gridName].data[index]
						]);
					}, 1, 1);
				});
			};

			// 엑셀 다운로드
			this.exportExcel = function(fileNm) {
				// 엑셀파일이름 설정
				param.scope[param_searchKey].fileNm = fileNm;

				// field 값 저장
				// param.scope[param_searchKey].field = [];
				// param.scope[param_searchKey].headerNm = [];
				// var j = 0;
				// for(var i = 0; i < paramColDef.length; i++){
				//					
				// if("checked" == paramColDef[i].field){//체크박스 행은 패스
				// continue;
				// }
				// console.log(paramColDef[i].field);
				//					
				// if(!paramColDef[i].field){
				// continue;
				// }
				// if(paramColDef[i].field.endsWith('Name')) {//code Util file pass
				// // var here = paramColDef[i].vKey.split('.');
				// param.scope[param_searchKey].field[j] = paramColDef[i].field.replace("Name", "Cd");
				// } else {
				// param.scope[param_searchKey].field[j] = paramColDef[i].field;
				// }
				// param.scope[param_searchKey].headerNm[j] = paramColDef[i].displayName;
				//					
				// //console.log(param.scope[param_searchKey].field[j]);
				//					
				//					
				// j++;
				// }

				// param.scope[param_searchKey].firstRow = null;// 페이징 안되게
				// alert(param.scope[param_searchKey].firstRow)

				param.scope[param_searchKey].pagingYn = 'N';
				window.location = param_url.replace("api", "excel") + "?" + $.param(param.scope[param_searchKey]);

				// commonService.exportExcel(url, param.scope[param_searchKey], function(response) {
				// if((response.content).substring(0,5) == "error") {
				// alert(response.content);
				//							
				// } else {
				// // alert("엑셀 다운르도 성공 \n" + "Path : " + response.content);
				//						 
				// window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + response.content;
				//						 
				// }
				// });

			}

			// 선택된 rows 조회(check box)
			this.getSelectedRows = function() {
				var rows = param.scope[param.gridName].data;

				var checkedList = [];

				for (var i = 0; i < rows.length; i++) {

					if (rows[i].checked) {
						checkedList.push(rows[i]);
					}
				}

				return checkedList;
			}

			// grid 선택여부(check box)
			this.isChecked = function() {
				var selRows = this.getSelectedRows();
				if (angular.isUndefined(selRows) || selRows.length == 0) {
					alert("선택된 대상이 없습니다.");
					return false;
				} else {
					return true;
				}
			}

			// 선택/에디트 모드 토글
			this.toggleRowSelection = function() {
				param.scope[param.gridName].gridApi.selection.clearSelectedRows();
				param.scope[param.gridName].enableRowSelection = !param.scope[param.gridName].enableRowSelection;
				param.scope[param.gridName].gridApi.core.notifyDataChange(uiGridConstants.dataChange.OPTIONS);
			};

			// 전체check 여부
			param.scope[param.gridName].allCheck = false;

			// 체크박스 전체선택
			param.scope.checkBoxAll = function(grid, evt) {
				if (!grid.options.checkMultiSelect) {
					return;
				}
				grid.options.allCheck = !grid.options.allCheck;

				var rows = grid.rows;
				for (var i = 0; i < rows.length; i++) {
					rows[i].entity.checked = grid.options.allCheck;
					if (grid.options.multiSelect && grid.options.enableRowSelection) {
						rows[i].isSelected = grid.options.allCheck;
					}
				}
			}

			// grid check
			param.scope.selectCheck = function(grid, rowEntity) {
				var gridName = commonService.getGridName(grid);

				// console.log(grid);
				if (!grid.options.checkMultiSelect) {
					var rows = grid.appScope[gridName].data;
					for (var i = 0; i < rows.length; i++) {
						if (grid.options.getRowIdentity(rowEntity) != rows[i].$$hashKey) {
							rows[i].checked = false;
						}
					}
				}
			}

			// check된 row setDirty
			this.setSelectRowsDirty = function() {
				var selectRows = this.getSelectedRows();
				$interval(function() {
					param.scope[param.gridName].gridApi.rowEdit.setRowsDirty(selectRows);
				}, 1, 1);
			}

			// 특정 data setDirty
			this.setRowsDirty = function(selectRows) {
				$interval(function() {
					param.scope[param.gridName].gridApi.rowEdit.setRowsDirty(selectRows);
				}, 1, 1);
			}

			// 모든 dirty rows 삭제
			var cleanDirtyRows = function(targets) {
				if (targets && targets.length > 0) {
					param.scope[param.gridName].gridApi.rowEdit.setRowsClean(targets);
				} else {
					var dirtyRows = getDirtyRows();

					if (dirtyRows.length > 0) {
						param.scope[param.gridName].gridApi.rowEdit.setRowsClean(dirtyRows);
					}
				}
			}

			// 특정 dirty row 삭제
			var cleanDirtyRow = function(dirtyRow) {
				param.scope[param.gridName].gridApi.rowEdit.setRowsClean(dirtyRow);
			}

			// 그리드 초기화
			this.initGrid = function() {
				cleanDirtyRows();
				param.scope[param.gridName].data = angular.copy(param.scope[param.gridName].tempData);

			}

			// 그리드 유효성 체크
			this.validate = function(dirtyRows) {
				
				if(angular.isUndefined(dirtyRows) || dirtyRows==null){
					dirtyRows = getDirtyRows();
				}
				
				var isValidGrid = true;

				for (var i = 0; i < dirtyRows.length; i++) {
					var rowEntity = dirtyRows[i];
					
					for (var j = 0; j < paramColDef.length; j++) {

						var colDef = paramColDef[j];
						var validator = colDef.validators;

						if (validator != null && angular.isDefined(validator)) {
							var cellValue = '';
							var listIdx = colDef['field'].indexOf('[0].');
							if (listIdx > -1) {
								var field = colDef['field'].substring(listIdx + 4);
								var paramArray = colDef['field'].substring(0, listIdx);
								cellValue = rowEntity[paramArray][0][field];
							} else {
								cellValue = rowEntity[colDef['field']];
							}
//							var cellValue = dirtyRows[i][columDef.field];
							
							if (angular.isUndefined(cellValue) || common.isEmpty(cellValue)) {
								cellValue = '';
							}

							if (validator.required && common.isEmpty(cellValue)) {// 필수값 체크하여 invalid 설정
								uiGridValidateService.setInvalid(dirtyRows[i], colDef);
							}

							var isInvalid = uiGridValidateService.isInvalid(dirtyRows[i], colDef);

							if (isInvalid) {
								isValidGrid = false;
							}
						}
					}

				}

				if (!isValidGrid) {
					alert('그리드에 유효하지 않은 값이 존재합니다.');
					return false;
				}

				return true;
			};

			// 서버로부터 그리드에 데이터 로딩
			this.loadGridData = function(pUrl) {

				if (param.scope[param.gridName].pagination) {
					param.scope[param.gridName].useExternalPagination = true;

					// 페이지 초기화
					param.scope[param.gridName].paginationCurrentPage = 1;

					if (!angular.isUndefined(param_scope[param_searchKey].sort)) {
						param_scope[param_searchKey].sort = "";
						param_scope[param_searchKey].direction = "";
					}

					// 페이지 param set
					pageParamSet(1);
				}

				setGridDataList(pUrl, function(response) {
					if (response.length > 0) {
						param.scope[param.gridName].data = response;
						param.scope[param.gridName].totalItems = response[0].totalCount;
						// Grid의 주문상품 갯수
						if(angular.isDefined(param.scope.totalProducts)){
							param.scope.totalProducts = response[0].totalCount;
						}
						// Grid의 주문번호 갯수
						if(angular.isDefined(param.scope.totalOrders)){
							param.scope.totalOrders = response[0].totalOrderCount;
						}
						// return grid_info;
					} else {
						// alert("조회된 데이터가 없습니다.");
						param.scope[param.gridName].totalItems = 0;
						param.scope[param.gridName].data = [];
						// Grid의 주문상품 갯수
						if(angular.isDefined(param.scope.totalProducts)){
							param.scope.totalProducts = 0;
						}
						// Grid의 주문번호 갯수
						if(angular.isDefined(param.scope.totalOrders)){
							param.scope.totalOrders = 0;
						}

						// return grid_info;
					}
				});
			};// end getGridData()

			var loadGridData = this.loadGridData;

			// 패턴 벨리데이터 추가
			uiGridValidateService.setValidator('pattern', function(argument) {
				return function(oldValue, newValue, rowEntity, colDef) {
					if (!newValue) {
						return true;
					} else {
						argument = argument.replace(/\//g, "");
						var reg = new RegExp(argument);
						return reg.test(newValue);
					}
				};
			}, function(argument) {
				return 'error pattern';
			});

			// dropdown code option 목록
			var getCodeList = function(options, groupCode) {

				// 코드 목록 조회
				commonService.getCodeList({
					cdGroupCd : groupCode
				}).then(function(data) {

					var editDropdownOptionsArray = [];
					options.push({
						id : '',
						comboLabel : '선택하세요'
					});
					for (var j = 0; j < data.length; j++) {
						options.push({
							id : data[j].cd,
							comboLabel : data[j].name
						});
					}

				});

			}

			var _header = [];// header title message param
			var _val_param = [];// validation param

			// cellTemplate 설정
			for (var i = 0; i < paramColDef.length; i++) {

				// 수정가능한 컬럼 설정과 Input Hidden
				if (paramColDef[i].enableCellHiddenInputEdit) {
					paramColDef[i].enableCellEdit = false;
					paramColDef[i].headerCellTemplate = "custom/uiGridHeaderEditableCell";
				}
				// 수정가능한 컬럼 헤더 설정
				else if (paramColDef[i].enableCellEdit) {
					paramColDef[i].headerCellTemplate = "custom/uiGridHeaderEditableCell";
				}
				// validator cell template
				if (angular.isDefined(paramColDef[i].validators) || angular.isDefined(paramColDef[i].vKey)) {
					paramColDef[i].cellTemplate = "ui-grid/cellTitleValidator";
				}

//				var periodStart = '';
//				var periodEnd = '';
//				if (paramColDef[i].type == "datetime" || paramColDef[i].type == "date") {
//					if (paramColDef[i].periodStart) {
//						periodStart = "period-start";
//					}
//					if (paramColDef[i].periodEnd) {
//						periodEnd = "period-end";
//					}
//				}
				// date
				var dateHtml = '<input type="text" ng-model="row.entity.' + paramColDef[i].field + '" datetime-picker grid ';
				if (angular.isDefined(paramColDef[i].periodStart)) {
					dateHtml += " period-start";
				}
				if (angular.isDefined(paramColDef[i].periodEnd)) {
					dateHtml += " period-end";
				}
				if (angular.isDefined(paramColDef[i].maxDate)) {
					dateHtml += (' max-date="' + paramColDef[i].maxDate + '"');
				}
				if (angular.isDefined(paramColDef[i].minDate)) {
					dateHtml += (' min-date="' + paramColDef[i].minDate + '"');
				}
				if (paramColDef[i].type == "datetime") { // datetime
//					paramColDef[i].cellTemplate = '<input type="text" ng-model="row.entity.' + paramColDef[i].field + '" date-format="' + Constants.date_format_1 + '" datetime-picker grid ' + periodStart + ' ' + periodEnd + '/>';
					dateHtml += (' date-format="' + Constants.date_format_1 + '" />');
					paramColDef[i].cellTemplate = dateHtml;
				} else if (paramColDef[i].type == "date") { // date
//					paramColDef[i].cellTemplate = '<input type="text" ng-model="row.entity.' + paramColDef[i].field + '" date-format="' + Constants.date_format_2 + '" datetime-picker grid date-only ' + periodStart + ' ' + periodEnd + '/>';
					dateHtml += (' date-format="' + Constants.date_format_2 + '" date-only />');
					paramColDef[i].cellTemplate = dateHtml;
				}

				// link
				if (angular.isDefined(paramColDef[i].linkFunction)) {
					var linkFunction = paramColDef[i].linkFunction;
					paramColDef[i].cellTemplate = '<div ng-class=\"{invalid:grid.validate.isInvalid(row.entity,col.colDef)}\" class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><a style=\"text-decoration: underline\" href=\"javascript:void(0);\"' + 'ng-click=\"grid.appScope.' + linkFunction + '(\'' + paramColDef[i].field + '\',row.entity)\">'
							+ '{{COL_FIELD CUSTOM_FILTERS}}</a></div>';
				}

				// image column 세팅
				var image = paramColDef[i].image;
				if (angular.isDefined(image)) {
					paramColDef[i].cellTemplate = "custom/imageCell";
				}

				// 타이틀 문구 조회를 위한 파라메터 세팅
				if (angular.isDefined(paramColDef[i].colKey)) {
					_header.push(paramColDef[i].colKey);
				}

				if (angular.isDefined(paramColDef[i].vKey)) {
					_val_param.push({
						fieldCd : paramColDef[i].vKey
					});
				}

				var filter = paramColDef[i].cellFilter;
				if (angular.isDefined(filter) && (filter == "displayYnFilter" || filter == "useYnFilter" || filter == "yesOrNoFilter")) {
					paramColDef[i].editableCellTemplate = "ui-grid/dropdownEditor";
					paramColDef[i].editDropdownValueLabel = "comboLabel";

					// dropdownEditor 설정 : 전시여부
					if (filter == "displayYnFilter") {
						paramColDef[i].editDropdownOptionsArray = [
								{
									id : 'Y',
									comboLabel : '전시'
								}, {
									id : 'N',
									comboLabel : '미전시'
								}
						];
					}

					// dropdownEditor 설정 : 사용/미사용
					if (filter == "useYnFilter") {
						paramColDef[i].editDropdownOptionsArray = [
								{
									id : 'Y',
									comboLabel : '사용'
								}, {
									id : 'N',
									comboLabel : '미사용'
								}
						];
					}

					// dropdownEditor 설정 : 예/아니오
					if (filter == "yesOrNoFilter") {
						paramColDef[i].editDropdownOptionsArray = [
								{
									id : 'Y',
									comboLabel : '예'
								}, {
									id : 'N',
									comboLabel : '아니오'
								}
						];
					}
				}
				
				// 사용자 필터 model1,model2 => model1(model2)
				var split = paramColDef[i].userFilter;
				if (angular.isDefined(split) && (split.indexOf(",") != -1)) {
					var str = split.trim().split(",", 2);
					if(str.length == 2)
						paramColDef[i].cellTemplate = '<div ng-class=\"{invalid:grid.validate.isInvalid(row.entity,col.colDef)}\" class=\"ui-grid-cell-contents\" title=\"TOOLTIP\">{{row.entity.' + str[1] + ' == undefined?row.entity.' + str[0] + ':row.entity.' + str[1] + '+\'(\'+row.entity.'+ str[0] + '+\')\'}}</div>';
				}

				var dropdownCodeEditor = paramColDef[i].dropdownCodeEditor;
				if (angular.isDefined(dropdownCodeEditor)) {

					paramColDef[i].editableCellTemplate = "ui-grid/dropdownEditor";
					paramColDef[i].editDropdownValueLabel = "comboLabel";

					// var groupCode = paramColDef[i].groupCode;

					paramColDef[i].editDropdownOptionsArray = [];
					// 콤보박스의 code option 목록 조회
					getCodeList(paramColDef[i].editDropdownOptionsArray, dropdownCodeEditor);
				}

			}

			// 체크박스 선택 컬럼 추가
			if (param.scope[param.gridName].checkBoxEnable) {
				var checkBoxCol = {
					field : "checked",
					enableCellEdit : false,
					cellTemplate : '<div style="text-align:center;"><input type="checkbox" ng-model="row.entity.checked" ng-click="grid.appScope.selectCheck(grid,row.entity)" ng-disabled="!grid.options.isRowSelectable(row)" ng-style="{\'cursor\': row.cursor,\'margin\':\'9px 0 0 0\'}" ></div>',// ng-click="toggle(row.entity.name,row.entity.dude)
					width : 45,
					pinnedLeft : true,
					displayName : '선택'
				};

				if (param.scope[param.gridName].checkMultiSelect) {
					checkBoxCol = {
						field : "checked",
						enableCellEdit : false,
						cellTemplate : '<div style="text-align:center;"><input type="checkbox" ng-model="row.entity.checked" ng-click="grid.appScope.selectCheck(grid,row.entity)" ng-disabled="!grid.options.isRowSelectable(row)" ng-style="{\'cursor\': row.cursor,\'margin\':\'9px 0 0 0\'}"></div>',// ng-click="toggle(row.entity.name,row.entity.dude)
						width : 40,
						pinnedLeft : true,
						headerCellTemplate : "custom/checkBoxHeader"
					};
				}

				paramColDef.splice(0, 0, checkBoxCol);// 체크박스 추가
			}

			// type 설정
			for (var k = 0; k < paramColDef.length; k++) {

				// timestamp 형은 date, 나머지는 string type으로 설정
				if (!(paramColDef[k].type == "date" || paramColDef[k].type == "datetime") && angular.isUndefined(paramColDef[k].type)) {// date 형은 date 타입
					paramColDef[k].type = "string";// 나머지는 string 타입
				}
			}

			// db로부터 그리드 조회
			var setGridDataList = function(pUrl, callback) {

				$(".ui-grid-pager-count-container").hide();
				$(".ui-grid-pager-row-count-label").hide();

				// console.log(searchScope);

				var url = param_url + "/list";
				if (angular.isDefined(pUrl) && pUrl != null) {
					url = pUrl;
				}
				if (param.scope[param.gridName].pagination) {
					param_scope[param_searchKey].pagingYn = 'Y';
				} else {
					param_scope[param_searchKey].pagingYn = 'N';
				}
				restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, {}, param_scope[param_searchKey], function(data) {
					// dirty rows 제거
					cleanDirtyRows();

					// restore를 위해 백업
					saveTemp(data);

					if (angular.isDefined(callback)) {
						callback(data);
					}
				});
			}

			// 그리드 행 삭제 : DB삭제는 하지 않음
			this.deleteRow = function() {

				var gridData = param.scope[param.gridName].data;
				if (!gridData || gridData.length == 0) {
					return;
				}

				var selectedRows = this.getSelectedRows();

				if (selectedRows == 0) {
					alert("삭제할 항목을 선택해 주세요.");
					return;
				}

				if (!confirm("삭제하시겠습니까?")) {
					return;
				}

				cleanDirtyRows(selectedRows);

				// 그리드 행 삭제
				for (var i = gridData.length - 1; i >= 0; i--) {

					if (gridData[i].checked) {
						gridData.splice(i, 1);
					}
				}
			};

			// 그리드 데이터 삭제
			this.deleteGridData = function(pUrl) {
				var gridData = param.scope[param.gridName].data;
				if (!gridData || gridData.length == 0) {
					return;
				}

				var selectedRows = this.getSelectedRows();

				if (selectedRows == 0) {
					alert("삭제할 항목을 선택해 주세요.");
					return;
				}

				if (!confirm("삭제하시겠습니까?")) {
					return;
				}

				cleanDirtyRows(selectedRows);

				// newRow는 그리드에서만 삭제
				for (var i = gridData.length - 1; i >= 0; i--) {

					if (gridData[i].checked && gridData[i].newRow) {
						gridData.splice(i, 1);
					}
				}

				// newRow 제거하고 다시 선택된 row 조회
				var checkedList = this.getSelectedRows();
				if (checkedList.length == 0) {
					return;
				}

				// DB삭제
				var url = param_url + "/delete";
				if (angular.isDefined(pUrl) && pUrl != null) {
					url = pUrl;
				}
				restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, {}, checkedList, function() {
					alert("삭제되었습니다.");
					loadGridData();
				});
			}

			var saveTemp = function(data) {
				if (!data || data == null) {
					param.scope[param.gridName].tempData = [];
				} else {
					param.scope[param.gridName].tempData = angular.copy(data);
				}
			}

			// 그리드 db저장
			this.saveGridData = function(pUrl, callback) {

				var dirtyRows = getDirtyRows();

				if (dirtyRows.length > 0) {

					if (!confirm("저장하시겠습니까?")) {
						return;
					}

					// 유효성 체크
					if (!this.validate(dirtyRows)) {
						return;
					}
					var url = param_url + "/save";
					if (angular.isDefined(pUrl) && pUrl != null) {
						url = pUrl;
					}
					restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, {}, dirtyRows, function(response) {

						if (angular.isUndefined(response.success) || angular.isDefined(response) && response.success) {
							alert("저장되었습니다.");

							// newRow 를 false
							for (var i = 0; i < param.scope[param.gridName].data.length; i++) {
								param.scope[param.gridName].data[i].newRow = false;
							}

							// dirty clean
							param.scope[param.gridName].gridApi.rowEdit.setRowsClean(dirtyRows);

							// 원복기능을 위해 현재 데이터를 TEMP로 복사
							saveTemp(param.scope[param.gridName].data);

							if (angular.isDefined(callback)) {
								callback();
							}
						} else {
							var msg = response.resultMessage == null || response.resultMessage == '' ? "Save Error!" : response.resultMessage;
							alert(msg);
							return;
						}

					});

				} else {
					alert("수정한 항목이 없습니다.");
				}
			}

			var pageParamSet = function(currentPage) {

				param_scope[param_searchKey].firstRow = (currentPage - 1) * param.scope[param.gridName].paginationPageSize + 1;
				param_scope[param_searchKey].lastRow = param_scope[param_searchKey].firstRow + param.scope[param.gridName].paginationPageSize - 1;
			}

			// sorting, paging 설정
			param.scope[param.gridName].onRegisterApi = function(gridApi) {

				param.scope[param.gridName].gridApi = gridApi;

				// row selection 시에 클릭 이벤트
				if (angular.isDefined(param.scope[param.gridName].rowSelectionFn)) {
					gridApi.selection.on.rowSelectionChanged(param.scope, function(row) {
						param.scope[param.gridName].rowSelectionFn(row);
					});
				}
				// edit 전의 이벤트(row 단위)
				if (angular.isDefined(param.scope[param.gridName].beginCellEdit)) {
					gridApi.edit.on.afterCellEdit(param.scope, function(rowEntity, colDef) {
						param.scope[param.gridName].beginCellEdit(rowEntity, colDef);
					});
				}
				// edit 후의 이벤트(row 단위)
				if (angular.isDefined(param.scope[param.gridName].afterCellEdit)) {
					gridApi.edit.on.afterCellEdit(param.scope, function(rowEntity, colDef, newValue, oldValue) {
						param.scope[param.gridName].afterCellEdit(rowEntity, colDef, newValue, oldValue);
					});
				}
				// edit 후의 이벤트(column 단위)
				if (angular.isDefined(param.scope[param.gridName].gridApi.edit)) {
					gridApi.edit.on.afterCellEdit(param.scope, function(rowEntity, colDef) {
						if (angular.isDefined(colDef.afterCellEdit)) {
							colDef.afterCellEdit(rowEntity);
						}
					});
				}

				// 현재 정렬로 페이징
				// gridApi.core.on.sortChanged(param_scope, function(grid, sortColumns) {
				//					
				// if (sortColumns.length != 0) {
				//						
				// // 페이지 초기화
				// param.scope[param.gridName].paginationCurrentPage = 1;
				// currentPage = 1;
				//						
				// pageParamSet(currentPage);//1페이지 검색
				//
				// // 선택한 컬럼 sorting
				// // 그리드 filed, DBcolumn화
				// var strings = (sortColumns[0].field).trim();
				// var i = 0;
				// var character = '';
				// var dbColumn = "";
				//						
				// while (i < strings.length){
				// character = strings.charAt(i);
				// if (character == character.toUpperCase()) {
				// dbColumn += '_' + character;
				// } else if (character == character.toLowerCase()){
				// dbColumn += character;
				// }
				// i++;
				// }
				// param.scope[param.gridName].sort = $filter('uppercase')(dbColumn);
				// param.scope[param.gridName].direction = sortColumns[0].sort.direction;
				//										
				// //데이터 조회
				// setGridDataList(function(response) {
				// param.scope[param.gridName].data = response;
				// });
				// }
				// });
				if (param.scope[param.gridName].pagination) {
					// 현재 페이지로 검색
					gridApi.pagination.on.paginationChanged(param_scope, function(newPage, pageSize) {
						if (param.scope[param.gridName].useExternalPagination) {

							param_scope[param_searchKey].firstRow = (newPage - 1) * pageSize + 1;
							param_scope[param_searchKey].lastRow = param_scope[param_searchKey].firstRow + pageSize - 1;

							// 데이터 조회
							setGridDataList(null, function(response) {
								param.scope[param.gridName].data = response;
							});
						}
					});
				}
			};// end girdApi();

			this.initGrid();

			// validation field 설정
			var setValidationField = function() {

				if (_val_param.length > 0) {

					// 필드 유효성 체크 세팅
					commonFactory.getValidationField(_val_param).then(function(vfieldInfo) {

						for (var k = 0; k < paramColDef.length; k++) {

							var valInfo = {};
							if (angular.isDefined(vfieldInfo)) {
								valInfo = vfieldInfo[paramColDef[k].vKey];
							}

							var validationVal = {};

							// if(!dropdownEditor){

							// DB field 기준으로 유효성 체크 설정
							if (angular.isDefined(valInfo)) {
								var required = valInfo["requiredYn"];
								var maxLength = valInfo["length"];
								var format = valInfo["format"];

								var custom = paramColDef[k].validators;

								if (!param.scope[param.gridName].columnDefs[k].validators) {
									param.scope[param.gridName].columnDefs[k].validators = {};
								}

								// length check
								if (maxLength > 0 && (!custom || custom && angular.isUndefined(custom.maxLength))) {
									param.scope[param.gridName].columnDefs[k].validators["maxLength"] = maxLength;
								}
								// null check
								if (required == 'Y' && (!custom || custom && angular.isUndefined(custom.required))) {
									param.scope[param.gridName].columnDefs[k].validators["required"] = true;
								}
								if (format && format != '' && format.length > 0 && (!custom || custom && angular.isUndefined(custom.pattern))) {
									param.scope[param.gridName].columnDefs[k].validators["pattern"] = format;
								}
							}

						}// end for loop

					});
				}

			}

			// 헤터 타이틀 설정
			if (_header.length > 0) {
				commonService.getGridField(_header).then(function(response) {

					for (var i = 0; i < paramColDef.length; i++) {
						// 타이틀 세팅
						if (angular.isUndefined(paramColDef[i].displayName)) {
							paramColDef[i].displayName = response[paramColDef[i].colKey];
						}
					}

					common.resizeIframe(param.scope);// 높이 조절

					param.scope[param.gridName].columnDefs = paramColDef;

				}).then(function() {
					// validation 세팅
					setValidationField();

					// grub init의 최종 콜백
					if (angular.isDefined(param_callbackFn)) {
						param_callbackFn();
					}

				});
			} else {
				param.scope[param.gridName].columnDefs = paramColDef;

				// validation 세팅
				setValidationField();

				// grub init의 최종 콜백
				if (angular.isDefined(param_callbackFn)) {
					param_callbackFn();
				}
			}

		}//Grid Class end

	};// return gridService end
});
