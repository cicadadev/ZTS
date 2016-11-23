/**
 * 확인 modal
 * 
 * 
 */
commonServiceModule.directive('ngConfirmClick', [ 
            		 function(){
            		     return {
            		         priority: 1,
            		         terminal: true,
            		         link: function (scope, element, attr) {
            		             var msg = attr.ngConfirmClick || "Are you sure?";
            		             var clickAction = attr.ngClick;
            		            var newMsg = "";
            		             if( msg.indexOf("||") > -1 ){
            		            	 
            		            	 newMsg = msg.split("||");
            		            	 newMsg = newMsg[0] + "\n" + newMsg[1];
            		             }
            		             else{
            		            	 
            		            	 newMsg = msg
            		             }
            		             element.bind('click',function (event) {
            		            
            		            	 if ( window.confirm(newMsg) ) {
            			                    	 
            			                  scope.$eval(clickAction)
            			             }
            		            	 else{
            		            		 return false;
            		            	 }
            		            	 
            			         });
            			    }
            			};
             }])

             
/**
 * input 바이트 validation : v-key 에서 사용됨
 */
commonServiceModule.directive("byteLength", function($compile) {
    return {
		restrict : 'A',
		replace : false,
		terminal : true,
    	link : function($scope, $element, $attrs){
			
    		$element.bind('keydown',function (e) {
    			var kc = e.keyCode ? e.keyCode : e.which ? e.which : e.charCod;
//    			alert(kc)
    			if(kc==8 || kc==46  || (37 >= kc || kc <= 40)){
    				return true;
    			}
    			var bytes = common.getBytes( $element.val());
    			if($attrs.maxbytelength <= bytes){
    				return false;
    			}
	        });    	
    		
    		$element.bind('change',function (e) {
    			var bytes = common.getBytes( $element.val());
    			if($attrs.maxbytelength <= bytes){
    				var ngModel = $attrs.ngModel;
    				$element.val('');
    				alert("허용 길이를 초과 하였습니다.("+bytes+"/"+$attrs.maxbytelength+" bytes)");
    				return false;
    			}
    		});
	             
    	}
    };
});


/**
 * validation
 * 
 */
commonServiceModule.directive("vKey", function($compile, commonFactory) {
	return {
		restrict : 'A',
		replace : false,
		terminal : true,
		priority : 1000,
		link : function(scope, element, attrs) {
			

			var inputName = element.attr('name');
			var v_form = element.closest("form").attr('name');
			var v_name;
			if(angular.isUndefined(inputName)){
				var ngModel = attrs.ngModel;
				
//				var index = "";
//				if(attrs.dataIndex){
//					index = "_"+attrs.dataIndex;
//				}
				var randomNo = Math.floor((Math.random() * 10000) + 1);
				v_name = ngModel.replace(/\./g,"_")+"_"+randomNo;  
			}else{
				v_name = inputName;
			}
			
			var err = "<p class='information' ng-show='!"+v_form+'.'+v_name+".$valid'>"
			+ "{{"+v_form+"."+v_name +".$error.required?'필수 입력 항목 입니다.':"+v_form+"."+v_name+".$error.pattern?'유효하지 않은 형식입니다.':'유효하지 않습니다.'}}</p>";
			
			function recompile(){
				
				if(angular.isUndefined(inputName)){
					attrs.$set('name', v_name);
				}
				
				//재컴파일 하므로 디렉티브 삭제
				element.removeAttr("v-key");
				//alert(err)
				//에러 태그 삽입 : next sibling 이 존재하면 마지막 sibling 뒤에 p 태그 추가
				if($( element ).next().length > 0){
					$( element ).siblings().last().after(err);
					$compile(element)(scope);
					$compile(element.siblings().last())(scope);
				}else{
					$( element ).after(err);
					$compile(element)(scope);
					$compile(element.next())(scope);
				}
				
				
	
			}

					
			if(attrs.vKey=='required'){
				attrs.$set('dataNgRequired', "true");
//				err = '<p class="information" ng-show="!'+v_form+'.'+v_name+'.$valid">필수 입력 항목 입니다.</p>';
				
				recompile();
				
			}else{
				
			// DB 필드정보 조회
				scope.val_data = commonFactory.getValidationField([{fieldCd : attrs.vKey}]).then(function(response) {
					
					var noFieldInfo = false;
					
					if(response.length == 0){
						noFieldInfo = true;
					}
					var data = response[attrs.vKey];
					
					if(angular.isUndefined(data)){
						noFieldInfo = true;
					}

					if(!noFieldInfo){
						
						var maxLength = data["length"];
						var format = data["format"];
						
						// length check
						if (maxLength > 0) {
//							attrs.$set('dataNgMaxlength', maxLength);
							attrs.$set('maxbytelength', maxLength);
							attrs.$set('byte-length', true);
						}
						// null check
						if (data["requiredYn"] == 'Y') {
							attrs.$set('dataNgRequired', "true");

						}
		//						
						if (format && format!='' && format.length > 0) {
							attrs.$set('dataNgPattern', format);
						}
					}
					//err = '<p class="information" ng-show="!'+v_form+'.'+v_name+'.$valid">필수 입력 항목 입니다.</p>';
					
					recompile();
				});

			}

		}
	}
});

/**
 * excel upload
 * 
 */
commonServiceModule.directive("fileread", [function () {
	  return {
		    scope: { opts: '='}
	  		, link: function ($scope, $elm, $attrs) {
	  			$elm.on('change', function (changeEvent) {
	  				var reader = new FileReader();

	  				reader.onload = function (evt) {
	  					$scope.$apply(function () {
	  						var data = evt.target.result;
	  						
	  						var workbook = XLSX.read(data, {type: 'binary'}); // sheet 정보
//	  						var headerNames = XLSX.utils.sheet_to_json( workbook.Sheets[workbook.SheetNames[0]], { header: 1 })[0];	// header값 저장
	  						var data = XLSX.utils.sheet_to_json( workbook.Sheets[workbook.SheetNames[0]]);	// header 값에 따른 데이터 바인딩

//	  						$scope.opts.columnDefs = [
//	  						                          
//	  						                          ];
//	  						headerNames.forEach(function (h) {
//	  							$scope.opts.columnDefs.push({ field: h });
//	  						});

	  						$scope.opts.data = data;
	  						$scope.opts.useExternalPagination = false;	// server don't control
	  						$elm.val(null);
	  					});
	  				};
	    
	  				reader.readAsBinaryString(changeEvent.target.files[0]);
	  			});
	  		}
	  }
}]);

/**
 * CODE CHECKBOX
 * 
 * attr : 	data-ng-model : 선택된 값이 담길 모델명
 * 			codegroup : 코드로 combobox 구성
 *          custom : 사용자 정의 combobox 구성
 *          all-check : 전체사용여부
 * 선택사항 : codegroup(코드로부터 목록구성)/custom( gsf.common.js 의 CHECKBOXLIST 에 선언한 custom 목록 구성)
 * 
 * ex1)<checkbox-list ng-model="search.productQnaState" code-group="PRODUCT_QNA_STATE_CD" all-check ></checkbox-list>
 * ex1)<checkbox-list ng-model="search.displayYn" custom="PUBLIC_YN" all-check >
 * 
 */
commonServiceModule.directive("checkboxList", function($compile, commonService, restFactory) {
	return {
		template : function(elem, attr) {
			var name = (attr.ngModel).replace(/\./g, "_");
			var allChecked = attr.ngModel + "AllChecked";

			var tmp = '';
			if (angular.isDefined(attr.allCheck)) {
				tmp += '<input type="checkbox" ng-model="' + allChecked + '" id="' + attr.ngModel + '" value="true" '
					+ ' ng-change="isSelectAll(\'' + name + '\',\'' + attr.ngModel + '\')"/>' 
					+ '<label for="' + attr.ngModel + '">전체</label>\n';
			}
			tmp += '<span data-ng-repeat="label in ' + name + 'CodeList">'
			/*
					+ '<input type="checkbox" ng-model="label.selected" ng-click="isLabelChecked(\'' + name + '\',\'' + attr.ngModel + '\')"'
					+ 'value="{{label.cd}}" id="{{label.cd}}"/>'
					+ '<label  ng-checked="' + allChecked + ' || ' + attr.ngModel + '.indexOf(\'{{label.cd}}\')>-1" style="margin:0 5px 0 7px;">{{label.name}}</label></span>'
			*/
					+ '<input type="checkbox" ng-model="label.selected" value="{{label.cd}}"  ng-click="isLabelChecked(\'' + name + '\',\'' + attr.ngModel + '\')"'
					+ 'ng-checked="' + allChecked + ' || ' + attr.ngModel + '.split(\',\').indexOf(\'{{label.cd}}\')>-1" id="'+name+'_{{label.cd}}"/>'
					+ '<label for="'+name+'_{{label.cd}}" style="margin:0 5px 0 7px;">{{label.name}}</label></span>'
					
			if (angular.isDefined(attr.b2eOption)) {
				tmp += '<label for="' + attr.ngModel + '" ng-show="' + attr.ngModel + '.indexOf(\'B2E\') > -1" style="vertical-align: top;">(고정배송지여부'
					+ '<input type="checkbox" ng-model="search.fixedDelAddr" id="' + attr.ngModel + '" value="true"/>)</label>\n'
			}
			return tmp;
		},
		link : function($scope, $element, $attrs, $controller) {

			var ngModel = $attrs.ngModel;
			var gubun = ngModel.replace(/\./g, "_");
			if ((angular.isUndefined($scope.$eval($attrs.ngModel)))) {
				$scope.$eval(ngModel + "=''");
			}

			$scope.$eval(ngModel + "Cds=''");

			$scope[gubun + "SearchStr"] = '';
			$scope[gubun + "CdStr"] = '';
			$scope[gubun + "CdAll"] = [];

			if ($attrs.codeGroup) {// 코드목록으로 구성
				commonService.getCodeList({cdGroupCd : $attrs.codeGroup}).then(function(data) {
					if (angular.isDefined($attrs.showOnly)) {
						var codes = [];
						for (var i = 0; i < data.length; i++) {
							if ($attrs.showOnly.indexOf(data[i].sortNo) > -1) {
								codes.push(data[i]);
								$scope[gubun + "CdAll"].push(data[i].cd);
							}
						}
						$scope[gubun + "CodeList"] = codes;
					} else {
						$scope[gubun + "CodeList"] = data;
						for (var i = 0; i < data.length; i++) {
							$scope[gubun + "CdAll"].push(data[i].cd);
						}
					}
					common.safeApply($scope);
				});
			} else if ($attrs.custom) {// 사용자 정의 목록 구성
				var data = CHECKBOXLIST[$attrs.custom];

				$scope[gubun + "CodeList"] = data;
				for (var i = 0; i < data.length; i++) {
					$scope[gubun + "CdAll"].push(data[i].cd);
				}
				common.safeApply($scope);
			}

			$scope.isSelectAll = function(gubun1, model) {
				
				var allChecked =  eval('getScopeValue($scope.' + model + 'AllChecked)');
				
				if (allChecked) {
					var allList = $scope[gubun1 + "CdAll"];
					setGlobalVar(allList, model);

				} else {
					$scope.$eval(model + "Cds=\"\"");
					$scope.$eval(model + "=\"\"");
				}
			}
			function getScopeValue(value) {
				if (angular.isUndefined(value)) {
					return [];
				} else {
					return value;
				}

			}

			// model 에 현재 선택된 값 저장
			function setGlobalVar(valueArray, model) {

				if (valueArray.length > 0) {
					$scope.$eval(model + "Cds=\"'" + valueArray.join("','") + "'\"");
					$scope.$eval(model + "=\"" + valueArray.join() + "\"");

				} else {
					$scope.$eval(model + "Cds=\"\"");
					$scope.$eval(model + "=\"\"");
				}
				// console.log($scope.presentSearch.presentStateCds);
			}
			$scope.isLabelChecked = function(gubun1, model) {

				// $scope.ccsControl = {};
				var cdStr = [];
				cdStr = eval('getScopeValue($scope.' + model + ')');
				var valueArray = [];
				if (cdStr.length > 0) {
					valueArray = cdStr.split(",");
				}

				var _code = this.label.cd;
				if (this.label.selected) {
					valueArray.push(_code);
					if (valueArray.length == $scope[gubun1 + "CdAll"].length) {
						$scope.$eval(model + "AllChecked=true");
						
					}
				} else {
					$scope.$eval(model + "AllChecked=false");
					var index = valueArray.indexOf(_code);
					valueArray.splice(index, 1);// 삭제
				}

				setGlobalVar(valueArray, model);
			}
			
			if(angular.isDefined($attrs.optionCheck)){
	    		$element.bind('change', function(event){
	    			$scope.$emit("comboChanged", { target: event.target });
	    		});
	    	}
		}
	};
});
/**
 * radio
 * 
 */
commonServiceModule.directive("radioList", function($compile, commonService, restFactory) {
    return {
    	template :  function(elem, attr){
    		
        	var ngModel = attr.ngModel;
        	var model = ngModel.replace(/\./g,"_");
        	var style = '';
        	if (attr.codeGroup) {
        		style = 'margin:0 14px 0 7px;';
        	}else if(attr.custom){
        		style = 'margin:0 7px 0 3px;';
        	}
    		
        	var tmp='';
    		if (angular.isDefined(attr.disabled)) {
        		tmp = '<span ng-repeat="label in '+model+'List">'
        		+'<input type="radio" data-ng-model="'+attr.ngModel+'" value="{{label.cd}}" disabled>'
        		+'<label style="' + style + '">{{label.name}}</label></span>';
        	} else {
        		tmp = '<span ng-repeat="label in '+model+'List">'
        		+'<input type="radio" id="' + attr.ngModel +'{{$index}}" data-ng-model="'+attr.ngModel+'" value="{{label.cd}}" >'
        		+'<label for="' + attr.ngModel + '{{$index}}" style="' + style + '">{{label.name}}</label></span>';
        	}
        	
        	return tmp;
    	},
        link: function($scope, $element, $attrs, $controller){
        	var ngModel = $attrs.ngModel;
        	var model = ngModel.replace(/\./g,"_");
        	
        	if ($attrs.codeGroup) { // 코드 목록으로 구성
        		commonService.getCodeList({cdGroupCd :$attrs.codeGroup }).then(function(data){
        			
        			$scope[model+"List"] = data;
//            	if($attrs.ngModel)
        			if((angular.isUndefined($scope.$eval($attrs.ngModel)))){
        				//$scope.$eval($attrs.ngModel+"=\"" + data[0].cd+"\"");
        			}
        			
        			common.safeApply($scope);
        		});
        	}else if($attrs.custom){ // 사용자 정의 목록
        		var data = RADIOLIST[$attrs.custom];
        		
        		$scope[model+"List"] = data;
        		
        		common.safeApply($scope);
        		
        		if(angular.isDefined($attrs.optionCheck)){
        			$element.bind('change', function(event){
        				$scope.$emit("radioChanged", { target: event.target });
        			});
        		}
        	}
        }  
    };
});

/**
 * Radio Box - Y/N 
 */
commonServiceModule.directive("radioYn", function($compile) {
    return {
    	template :  function(elem, attr){
    		var arrs = attr.labels.split(",");
			
    		return '<input type="radio" id="' + attr.ngModel + '1" data-ng-model="'+attr.ngModel+'" value="Y" >'
    		+'<label for="' + attr.ngModel + '1">'+arrs[0]+'</label>'
        	+'<input type="radio" id="' + attr.ngModel + '2" data-ng-model="'+attr.ngModel+'" value="N" >'
        	+'<label for="' + attr.ngModel + '2">'+arrs[1]+'</label>';
    	}, link : function($scope, $element, $attrs){
    		
    		if(angular.isUndefined($scope.$eval($attrs.ngModel)) && angular.isDefined($attrs.initVal)){
    			$scope.$eval($attrs.ngModel + "=\""+ $attrs.initVal+"\"");
    		}
    		
    		if(angular.isDefined($attrs.optionCheck)){
    			$element.bind('change', function(event){
    				$scope.$emit("radioChanged", { target: event.target });
    			});
    		}
    	}
    };
});


/**
 * select box
 * 
 * validation check 시 required attrbute를 선언한다.
 * 
 * <selectlist data-ng-model="omsOrder.orderType"  codegroup="ORDER_TYPE_CD" all="true" val/>
 */
commonServiceModule.directive("selectList", function($compile, commonService, restFactory) {
    return {
    	replace : false,
    	template :  function(elem, attr){   
    		var gubun = (attr.ngModel).replace(/\./g,"_");
    		
    		var validation = "";
    		if(angular.isDefined(attr.required)){
    			validation = "v-key='required'";		    			
    		}
    		
    		var disabled = "";
    		if(angular.isDefined(attr.ngDisabled)){
    			disabled = 'data-ng-disabled="'+attr.ngDisabled+'"';		    			
    		}
    		
    		var style="";
    		if(angular.isDefined(attr.style)){
    			style = 'style="'+attr.style+'"';		    			
    		}
    		
    		
    		var tmp = '<select data-ng-model="'+attr.ngModel+'" '+validation+' '+style+' '+disabled+' ng-options="option.cd as option.name for option in '+gubun+'List">';
			tmp += '<option value="" selected>선택하세요</option></select>';
			
    		return tmp;
    	},
    	priority : 10000,
        link: function($scope, $element, $attrs, $controller){
        	var gubun = $attrs.ngModel;
        	gubun = gubun.replace(/\./g,"_");
        	
            commonService.getCodeList({cdGroupCd :$attrs.codeGroup }).then(
    		 function(data){
             	$scope[gubun+"List"] = data;
         	});
            
        }  
    };
});


commonServiceModule.directive("selectCode", function($compile, commonService, restFactory) {
    return {
    	restrict : 'A',
    	replace : false,
    	priority : 10000,
    	terminal : true,
        link: function($scope, $element, $attrs, $controller){
        	var ngModel = $attrs.ngModel;
        	var gubun = ngModel.replace(/\./g,"_");
        	
            commonService.getCodeList({cdGroupCd :$attrs.selectCode }).then(function(data){
            	
            	$scope[gubun+"List"] = data;
            	$attrs.$set('dataNgOptions', "option.cd as option.name for option in "+gubun+"List");
            	
            	$element.removeAttr("select-code");
            	
    			$compile($element)($scope);
				
        	});
        	
        }  
    };
});


/**
 * calendar button
 * 
 * attr	:	start-ng-model
 * 			end-ng-model
 * 			calendar-button
 * 
 * ex)	<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" calendar-button />	
 */
commonServiceModule.directive('calendarButton', function($compile, $filter) {
	return {
		replace : true,
		template : function($element, $attrs) {
			var html = '<div style="display:inline-block;">';
			if ($attrs.calendarButton == 'B') { // type B
				html += '<button type="button" class="btn_day" value="1">1일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="2">2일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="3">3일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="5">5일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="7">7일</button>&nbsp;';
			} else {// type A
				html += '<button type="button" class="btn_day" value="7">1주일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="15">15일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="30">30일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="60">60일</button>&nbsp;';
				html += '<button type="button" class="btn_day" value="90">90일</button>&nbsp;';
			}
			html += '</div>';
			return html;
		},
		link : function($scope, $element, $attrs, $controller) {

			/* 오늘 날짜 */
			var today = new Date();
			var todayYear = today.getFullYear();
			var todayMonth = today.getMonth();
			var todayDay = today.getDate();

			/* 계산된 날짜 */
			var settingDate;
			var setYear, setMonth, setDay;

			function fnCalendarRange(btnVal, direction) { // btnVal == 1일,2일3일,,,,,90일
				settingDate = new Date();
				var range = 1 - btnVal;
				if (direction == 'forward') {
					range = Math.abs(range);
				}
				settingDate.setDate(settingDate.getDate() + range);

				setYear = settingDate.getFullYear();
				setMonth = settingDate.getMonth();
				setDay = settingDate.getDate();
			}
			function setCalendarData(direction) {
//				var sDate, eDate;
//				if(angular.isDefined($attrs.setDate)) {
//					sDate = setYear + Constants.date_format_bar + ("0" + (setMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + setDay).slice(-2);
//					eDate = todayYear + Constants.date_format_bar + ("0" + (todayMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + todayDay).slice(-2);
//				} else {
//					var sDate = setYear + Constants.date_format_bar + ("0" + (setMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + setDay).slice(-2) + " 00:00:00";
//					var eDate = todayYear + Constants.date_format_bar + ("0" + (todayMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + todayDay).slice(-2) + " 23:59:59";
//				}
//				var sDate = setYear + Constants.date_format_bar + ("0" + (setMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + setDay).slice(-2) + " 00:00:00";
//				var eDate = todayYear + Constants.date_format_bar + ("0" + (todayMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + todayDay).slice(-2) + " 23:59:59";
//				var sDate = setYear + Constants.date_format_bar + ("0" + (setMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + setDay).slice(-2) + " 00:00:00";
//				var eDate = todayYear + Constants.date_format_bar + ("0" + (todayMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + todayDay).slice(-2) + " 23:59:59";
				// $scope.$eval($attrs.startNgModel + "='" + sDate.replace(/\//g,"") + "'");
				// $scope.$eval($attrs.endNgModel + "='" +eDate.replace(/\//g,"") +"'");
				
				if (direction == 'forward') {
					var sDate = todayYear + Constants.date_format_bar + ("0" + (todayMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + todayDay).slice(-2);
					var eDate = setYear + Constants.date_format_bar + ("0" + (setMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + setDay).slice(-2);
				} else {
					var sDate = setYear + Constants.date_format_bar + ("0" + (setMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + setDay).slice(-2);
					var eDate = todayYear + Constants.date_format_bar + ("0" + (todayMonth + 1)).slice(-2) + Constants.date_format_bar + ("0" + todayDay).slice(-2);
				}
				
				if (!angular.isDefined($attrs.dateOnly)) {
					sDate += " 00:00:00";
					eDate += " 23:59:59";
				}
				
				$scope.$eval($attrs.startNgModel + "='" + sDate + "'");
				$scope.$eval($attrs.endNgModel + "='" + eDate + "'");
				angular.element('[ng-model="' + $attrs.startNgModel + '"]').find('input').val(sDate);
				angular.element('[ng-model="' + $attrs.endNgModel + '"]').find('input').val(eDate);
			}
			
			var $button = $element.find('button');
			var $input = $element.find('input');
			$button.on('click', function() {
				$(this).addClass("on").siblings().removeClass("on");

//				fnCalendarRange($(this).text());
				var direction = 'backward';
				if(angular.isDefined($attrs.forward)) {
					direction = 'forward';
				}
				fnCalendarRange($(this).val(), direction);

				if ($scope.$parent.$$phase != '$apply' && $scope.$parent.$$phase != '$digest') {
//					$scope.$apply();
					$scope.$parent.$apply(function() {
						setCalendarData(direction);
					});
				} else {
					setCalendarData(direction);
				}
			});
			
			var initButton = $attrs.initButton;
			if (initButton != null && initButton != undefined && initButton != '') {
				$button.eq(initButton).click();
			}
		}
	}
});

/**
 * value binding
 * 
 * attr : data-ng-model value is-value
 * 
 * ex) <input type="text" data-ng-model="search.searchKeyword" value="TEST"
 * placeholder="" style="width:30%;" is-value/>
 * 
 */
commonServiceModule.directive('isValue', function() {
	  return {
	    restrict: 'A',
	    controller: [
	      '$scope', '$element', '$attrs', '$parse', function($scope, $element, $attrs, $parse) {	    	  
	        var getter, setter, val;
	        val = $attrs.isValue || $attrs.value;
	        getter = $parse($attrs.ngModel);
	        setter = getter.assign;	        
	        setter($scope, val);
	      }
	    ]
	  };
});

 
/**
 * TextArea change param ("," or "\n" 일때 IN 조건으로 변환 (suffix에 s를 붙여 scope에 담김.))
 * 
 * attr	:	data-ng-model
 * 			search-area
 * 
 * ex)	<textarea cols="30" rows="5" placeholder="" style="height:106px;" data-ng-model="search.orderId" search-area> </textarea>
 * 
 */
commonServiceModule.directive('searchArea', function() {
	return { restrict:'A', scope:{ 'searchArea':'=' }, link:function(scope, elm, attrs) {
		scope.$watch('searchArea', function(nVal) {
			elm.val(nVal);
		});
		elm.bind('blur', function() {

			var _setdata = function(currentValue) {
				var queryVal = '';

				var datas = currentValue.split(",");
				var i = 0;
				angular.forEach(datas, function(value, key) {
					var eDatas = value.split("\n");
					angular.forEach(eDatas, function(val2, key2) {
						var searchId = val2.trim();
						if (searchId != '') {
							if (i == 0) {
								queryVal += "'" + searchId + "'";
							} else {
								queryVal += ",'" + searchId + "'";
							}
							i++;
						}
					});
				});

				scope.$parent.$eval(attrs.ngModel + "s" + '="' + queryVal + '"');
			}

			var currentValue = elm.val();
			if (scope.onChange !== currentValue) {
				scope.$apply(function() {
					_setdata(currentValue);
				});
			}
		});
	} };
});



/**
 * file upload 페이지 로딩시, 파일 첨부시 즉시 업로드
 * 
 */
commonServiceModule.directive('fileUpload', function (commonService) {
    return {
    	scope: false,        // create a new scope
        link: function (scope, el, attrs) {
        	
            el.bind('change', function (event) {
            	
                var files = event.target.files;
                var id = event.currentTarget.id;
                
                //iterate files since 'multiple' may be specified on the element
                var url = Rest.context.path + "/api/ccs/common/imgFileUpload";
                
                for (var i = 0 ; i < files.length ; i++) {
                    //emit event upward
                	
                    scope.$emit("fileSelected", { file: files[i] });
                    
                    commonService.uploadFileToUrl(files[i], null, url, function(response) {
                    	
                    	if(response.indexOf("[업로드 실패]")>-1){
                    		alert(response);
                    		return;
                    	}
                    	
                    	if(attrs.callback){
                    		
//	                    	var str = attrs.imageKey + "=\""+Rest.context.path + response+"\"";
	                    	var str = response.replace(/\\/g,"/");
//	                    	var local = "c:\\ZTS";
	                    	
	                    	//console.log(">>>>>>>> str :" + str);
	                    	var evalStr = "";
	                    	if(attrs.index){
	                    		evalStr = "scope."+attrs.callback+"('"+str+"','"+ attrs.index +"')";
	                    	}else{
	                    		evalStr = "scope."+attrs.callback+"('"+str+"')";
	                    	}
                    		eval(evalStr);
                    		
//                    		scope.imageUploadCallback('\\upload\\temp\pms\product\2016\0617\product_1466145883635.png');
                    		
                    	}else{
	                    	//업로드한 경로 세팅
	                    	var str = attrs.imageKey + "=\""+Rest.context.path + response+"\"";
	                    	var str = attrs.imageKey + "=\""+response+"\"";

	                    	if(common.isEmpty(attrs.imageKey) && !common.isEmpty(attrs.gridKey)){
	                    		var str = attrs.gridKey + ".entity.img=\""+Rest.context.path + response+"\"";
	                    		var str = attrs.gridKey + ".entity.img=\""+response+"\"";
	                    		
	                    		for (var i = 0; i <  parent.$scope.grid_depth2.data.length; i++) {
	                    			if (parent.$scope.grid_depth2.data[i].$$hashKey == attrs.hashKey) {
	                    				var data = [];
	                    				data.push(parent.$scope.grid_depth2.data[i]);
	                    				parent.$scope.grid_depth2.gridApi.rowEdit.setRowsDirty(data);
	                    			}
	                    		}
	                    	}
	                    	str = str.replace(/\\/g,"/");
//	                    	console.log(">>>>>>>> str :" + str);
	                    	scope.$eval(str);
                    	}
                    });
                }                                       
            });
        }
    };
});

/**
 * file upload2
 * 페이지 로딩시, 파일 첨부시 즉시 업로드
 * 업로드된 파일객체를 리턴
 * 
 */
commonServiceModule.directive('fileUpload2', function (commonService) {
    return {
    	scope: false,        //create a new scope
        link: function (scope, el, attrs) {
        	
            el.bind('change', function (event) {
            	
                var files = event.target.files;
                var id = event.currentTarget.id;
                
                var url = Rest.context.path + "/api/ccs/common/imgFileUpload";
                
                for (var i = 0 ; i < files.length ; i++) {
                	
                    scope.$emit("fileSelected", { file: files[i] });
                    
                    commonService.uploadFileToUrl(files[i], null, url, function(response) {
                    	
                    	var fixedResponse = response.replace(/\\'/g, "'");
                    	var jsonObj = JSON.parse(fixedResponse);
                    	console.log(jsonObj)
                    	if(!common.isEmpty(jsonObj.errorMsg)){
                    		alert(jsonObj.errorMsg);
                    		return;
                    	}
                    	
                    	var filePath = jsonObj.uploadedFilePath;
                    	var volume = jsonObj.fileSize;
                    	var size = jsonObj.width+"x"+ jsonObj.height;
                    	if(attrs.callback){
                    		
	                    	var str = filePath.replace(/\\/g,"/");
	                    	
	                    	var evalStr = "";
	                    	if(attrs.index){
	                    		evalStr = "scope."+attrs.callback+"('"+filePath+"','"+volume+"','"+size+"','"+ attrs.index +"')";
	                    	}else{
	                    		evalStr = "scope."+attrs.callback+"('"+filePath+"','"+volume+"','"+size+"')";
	                    	}
	                    	alert(evalStr)
                    		eval(evalStr);
                    	}
                    });
                }                                       
            });
        }
    };
});

/**
 * file upload
 * 파일 첨부후 등록 이벤트 필요
 */
commonServiceModule.directive('excelUpload', ['$parse', function ($parse) {
    return {
       restrict: 'A',
       link: function(scope, element, attrs) {
          element.bind('change', function(event){
        	  var files = event.target.files;
        	  for (var i = 0;i<files.length;i++) {
                  //emit event upward
                  scope.$emit("fileSelected", { file: files[i] });
              }
          });
       }
    };
 }]);


/**
 * zip 압축 file upload 페이지 로딩시, 파일 첨부시 즉시 업로드
 * 
 */
commonServiceModule.directive('zipFileUpload', function (commonService) {
    return {
    	scope: false,        // create a new scope
        link: function (scope, el, attrs) {
        	
            el.bind('change', function (event) {
                var files = event.target.files;
//              var id = event.currentTarget.id; 키 넘기기 삭제
                
//              TODO
                //iterate files since 'multiple' may be specified on the element
                var url = Rest.context.path + "/api/ccs/common/zipFileUpload";
                
                for (var i = 0 ; i < files.length ; i++) {
                    //emit event upward
                	console.log('length', files.length);
                    scope.$emit("fileSelected", { file: files[i] });
                    
//                  attrs.zipFileUpload에 key 설정
                    commonService.uploadFileToUrl(files[i], attrs.zipFileUpload, url, function(response) {
                    	if(response.indexOf("[업로드 실패]")>-1){
                    		alert("[업로드 실패]");
                    		return;
                    	}
                    	
                    	if(attrs.callback){
//	                    	var str = response.replace(/\\/g,"/");
//	                    	파일 path = tempFilePath = response.info[i].fullPath + '/' + response.info[i].fileName  
                    		
	                    	var evalStr = "";
	                    	evalStr = "scope."+attrs.callback+"(" + response + ")";
	                    	
                    		eval(evalStr);
                    	}
                    });
                }  
                
//                clear
                el.val(null);
                
            });
        }
    };
});


/**
 * layer popup
 * 
 * ex) <layerpopup when="altProcLayer"> <div ............. </div></layerpopup>
 * 
 */
commonServiceModule.directive('layerpopup', function($templateCache, $document, $compile) {
    return {  
        restrict: 'E',                      
        link: function postLink(scope, element, attrs) {

            $(element).hide();
            
            scope.$watch(attrs.when, function(show) {                
                if(show){
                    $(element).show();
                } else {                    
                    $(element).hide();                                                  
                }                    
            });     
        }       
      }
});

/**
 * 권한 적용 버튼
 * 
 * ex) <button type="button" class="btn_type1" data-ng-click="ctrl.approval('PRODUCT_STATE_CD.APPROVAL1')" fn-id="APPROVAL_1"></button>
 */
commonServiceModule.directive('fnId', function(commonService) {
    return {    	
        restrict: 'A',          
        link: function postLink(scope, element, attrs) {
        	
        	// po 일때는 return
        	if(!common.isEmpty(global.session.businessId)){
        		return;
        	}
        	
        	var checkFunction = function(){
        		
//        		var fnId = attrs.fnId.split("_");
//        		if(fnId.length == 2){
//        			fnId = fnId[1];
//        		}
//        		
//        		if(angular.isUndefined(scope._auth_function[fnId])){
//        			element[0].remove();
//        		}else{
////        			버튼명 유지 
////        			element[0].innerHTML = "<b>" + scope._auth_function[fnId] + "</b>";
//        		}        		        		
        	}
        	
        	if(angular.isUndefined(scope._auth_function)){
        		
        		var fnId = attrs.fnId.split("_");
        		if(fnId.length == 2){
        			fnId = fnId[0];
        		}
        		scope.fnId = fnId;
        		commonService.getAuthFunction(scope,function(response){
        			scope.$parent._auth_function = response;
        			scope._auth_function = response;
					checkFunction();
				});	
        	}else{
        		checkFunction();
        	}
        	
        }       
      }
});


commonServiceModule.directive('codebuttonList', function(commonService) {
	return {
    	template :  function(elem, attr){    		
    		var name = (attr.code).replace(/\./g,"");
    		 return '<span ng-repeat="label in search_temp.'+name+'CdList">'
    		 +'<button type="button" class="btn_type1" data-ng-model="'+attr.code+'Cd" data-ng-click="'+attr.ngClick+'(label.cd)" fn-id="{{label.cd}}"><b>{{label.name}}</b></button> '
    		 +'</span>'
    	},
        link: function($scope, $element, $attrs, $controller){        	
        	var gubun = $attrs.code;
        	gubun = gubun.replace(/\./g,"");
        	
            commonService.getCodeList({cdGroupCd :$attrs.codeGroup } ).then(function(data){
            	_searchObj[gubun+"CdList"] = data;
            	
            	if(!$attrs.required){
            		$scope.$eval($attrs.code+"Cd=\"" + data[0].cd+"\"");
            	}
            	if(!angular.isUndefined($attrs.param)) {
            		$scope.$eval($attrs.code+"Cd" +"=\"" + $attrs.param+"\"");
            	}
        	});
        	
            $scope.search_temp = {};	
            
            _searchObj = $scope.search_temp;
        }  
    };
});


//check box 값을 모델에 담는 directive
commonServiceModule.directive('checklistModel', ['$parse', '$compile', function($parse, $compile) {
	  // contains
	  function contains(arr, item, comparator) {
	    if (angular.isArray(arr)) {
	      for (var i = arr.length; i--;) {
	        if (comparator(arr[i], item)) {
	          return true;
	        }
	      }
	    }
	    return false;
	  }

	  // add
	  function add(arr, item, comparator) {
	    arr = angular.isArray(arr) ? arr : [];
	      if(!contains(arr, item, comparator)) {
	          arr.push(item);
	      }
	    return arr;
	  }  

	  // remove
	  function remove(arr, item, comparator) {
	    if (angular.isArray(arr)) {
	      for (var i = arr.length; i--;) {
	        if (comparator(arr[i], item)) {
	          arr.splice(i, 1);
	          break;
	        }
	      }
	    }
	    return arr;
	  }

	  // http://stackoverflow.com/a/19228302/1458162
	  function postLinkFn(scope, elem, attrs) {
	     // exclude recursion, but still keep the model
	    var checklistModel = attrs.checklistModel;
	    attrs.$set("checklistModel", null);
	    // compile with `ng-model` pointing to `checked`
	    $compile(elem)(scope);
	    attrs.$set("checklistModel", checklistModel);

	    // getter / setter for original model
	    var getter = $parse(checklistModel);
	    var setter = getter.assign;
	    var checklistChange = $parse(attrs.checklistChange);
	    var checklistBeforeChange = $parse(attrs.checklistBeforeChange);

	    // value added to list
	    var value = attrs.checklistValue ? $parse(attrs.checklistValue)(scope.$parent) : attrs.value;


	    var comparator = angular.equals;

	    if (attrs.hasOwnProperty('checklistComparator')){
	      if (attrs.checklistComparator[0] == '.') {
	        var comparatorExpression = attrs.checklistComparator.substring(1);
	        comparator = function (a, b) {
	          return a[comparatorExpression] === b[comparatorExpression];
	        };
	        
	      } else {
	        comparator = $parse(attrs.checklistComparator)(scope.$parent);
	      }
	    }

	    // watch UI checked change
	    scope.$watch(attrs.ngModel, function(newValue, oldValue) {
	      if (newValue === oldValue) { 
	        return;
	      } 

	      if (checklistBeforeChange && (checklistBeforeChange(scope) === false)) {
	        scope[attrs.ngModel] = contains(getter(scope.$parent), value, comparator);
	        return;
	      }

	      setValueInChecklistModel(value, newValue);

	      if (checklistChange) {
	        checklistChange(scope);
	      }
	    });

	    function setValueInChecklistModel(value, checked) {
	      var current = getter(scope.$parent);
	      if (angular.isFunction(setter)) {
	        if (checked === true) {
	          setter(scope.$parent, add(current, value, comparator));
	        } else {
	          setter(scope.$parent, remove(current, value, comparator));
	        }
	      }
	      
	    }

	    // declare one function to be used for both $watch functions
	    function setChecked(newArr, oldArr) {
	      if (checklistBeforeChange && (checklistBeforeChange(scope) === false)) {
	        setValueInChecklistModel(value, scope[attrs.ngModel]);
	        return;
	      }
	      scope[attrs.ngModel] = contains(newArr, value, comparator);
	    }

	    // watch original model change
	    // use the faster $watchCollection method if it's available
	    if (angular.isFunction(scope.$parent.$watchCollection)) {
	        scope.$parent.$watchCollection(checklistModel, setChecked);
	    } else {
	        scope.$parent.$watch(checklistModel, setChecked, true);
	    }
	  }

	  return {
	    restrict: 'A',
	    priority: 1000,
	    terminal: true,
	    scope: true,
	    compile: function(tElement, tAttrs) {
	      if ((tElement[0].tagName !== 'INPUT' || tAttrs.type !== 'checkbox') && (tElement[0].tagName !== 'MD-CHECKBOX') && (!tAttrs.btnCheckbox)) {
	        throw 'checklist-model should be applied to `input[type="checkbox"]` or `md-checkbox`.';
	      }

	      if (!tAttrs.checklistValue && !tAttrs.value) {
	        throw 'You should provide `value` or `checklist-value`.';
	      }

	      // by default ngModel is 'checked', so we set it if not specified
	      if (!tAttrs.ngModel) {
	        // local scope var storing individual checkbox model
	        tAttrs.$set("ngModel", "checked");
	      }

	      return postLinkFn;
	    }
	  };
	}]);

//제한설정 영역 생성 directive : input radio & 버튼
commonServiceModule.directive('controlSet', function(commonPopupService) {

	return {
		restrict: 'E ',
    	template :  function(elem, attr){    		
    		var controlNo = attr.modelName+".controlNo";

            var templateHtml =  "<radio-yn ng-model=\""+attr.modelName+".isAllPermit\" labels=\""+attr.lebels+"\" init-val=\"Y\"></radio-yn>"
			+ "<button type=\"button\" class=\"btn_plus\" ng-class=\"{'btn_plus_disabled' : "+attr.modelName+".isAllPermit=='Y' }\" "
			+ "ng-disabled=\""+attr.modelName+".isAllPermit=='Y'\" ng-click=\"restrictPop("+attr.flag+")\" "
			+ "ng-model=\""+attr.modelName+".isMakeModel\"></button>";
            return templateHtml;
    		
    	},
        link: function($scope, $element, $attrs, $controller){   
        	var ngModel = $attrs.modelName;
        	var except = angular.isDefined($attrs.except)? $attrs.except :"";//type,grade,channel,device
        	
        	// 라디오 버튼 클릭 watch
        	$scope.$watch($attrs.modelName+".isAllPermit", function(newValue, oldValue){
        		
        		// 전체 허용 선택일 경우
        		if(newValue=='Y'){
        			$scope.$eval(ngModel+".ccsControl" + "=null");
        		}
        		
        		if(oldValue=='Y' && newValue=='N'){
        			// 허용 필터 적용할때 기본값 : 전체 선택
        			if($scope.ccsControl){}
        			$scope.ccsControl = {};
        			
        			// TODO 하드코딩 삭제할것.
        			if(except.indexOf("mtype") == -1 ){
        				$scope.ccsControl.memberTypes = "MEMBER_TYPE_CD.GENERAL,MEMBER_TYPE_CD.MEMBERSHIP,MEMBER_TYPE_CD.PREMIUM,MEMBER_TYPE_CD.EMPLOYEE,MEMBER_TYPE_CD.CHILDREN,MEMBER_TYPE_CD.B2E";
        				$scope.ccsControl.memberTypeArr = ["MEMBER_TYPE_CD.GENERAL", "MEMBER_TYPE_CD.MEMBERSHIP","MEMBER_TYPE_CD.PREMIUM","MEMBER_TYPE_CD.EMPLOYEE","MEMBER_TYPE_CD.CHILDREN","MEMBER_TYPE_CD.B2E"];
        			}
        			if(except.indexOf("mgrade") == -1 ){
        				$scope.ccsControl.memGrades = "MEM_GRADE_CD.VIP,MEM_GRADE_CD.GOLD,MEM_GRADE_CD.SILVER,MEM_GRADE_CD.FAMILY,MEM_GRADE_CD.WELCOME";
        				$scope.ccsControl.memGradeArr = ["MEM_GRADE_CD.VIP","MEM_GRADE_CD.GOLD","MEM_GRADE_CD.SILVER","MEM_GRADE_CD.FAMILY","MEM_GRADE_CD.WELCOME"];
        			}
        			if(except.indexOf("device") == -1 ){
        				$scope.ccsControl.deviceTypes = "DEVICE_TYPE_CD.PC,DEVICE_TYPE_CD.MW,DEVICE_TYPE_CD.APP";
        				$scope.ccsControl.deviceTypeArr = ["DEVICE_TYPE_CD.PC", "DEVICE_TYPE_CD.MW", "DEVICE_TYPE_CD.APP"];
        			}
        			
        			$scope.ccsControl.channelControlCd="CHANNEL_CONTROL_CD.ALL";
        		}
        	},true);
        	
        	var controlNo = $attrs.modelName+".controlNo";
        	// controlNo 초기 값에 따른 라디오 버튼 선택
        	$scope.$watch($attrs.modelName+".controlNo", function(newValue, oldValue){
        		if($scope.$eval(controlNo+"==null")){
        			$scope.$eval(ngModel+".isAllPermit='Y'");
        		}else{
        			$scope.$eval(ngModel+".isAllPermit" + "='N'");
        			$scope.$eval("controlNo" + "="+controlNo);
        		}
        	});
        	//팝업으로 부터 변경되면 발생하는 watch
        	$scope.$watch("ccsControl", function(newValue, oldValue){
        		if($scope.$eval(ngModel+"!=null")){
        			$scope.$eval(ngModel+".ccsControl=ccsControl");
        		}
        	});
        	
        	$scope.restrictPop = function(flag){
        		$scope.$eval(ngModel+".isMakeModel" + "='N'");
        		if(!except){
        			commonPopupService.restrictPopup($scope, true, true, true, true, flag);
        		}else{
        			commonPopupService.restrictPopup($scope, except.indexOf("mtype")==-1, except.indexOf("mgrade")==-1, except.indexOf("channel")==-1, except.indexOf("device")==-1, flag);
        		}
        	}
        	
        }  
    };
});
commonServiceModule.directive('faxInput', function($filter, $browser) {// 전화번호 입력 형식
	//console.log("test");
    return {
        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
            var listener = function() {
                var value = $element.val().replace(/[^0-9]/g, '');
                $element.val($filter('fax')(value, false));
            };

            // This runs when we update the text field
            ngModelCtrl.$parsers.push(function(viewValue) {
            	if(viewValue.slice(0,2).match(/^(02)/)){
            		return viewValue.replace(/[^0-9]/g, '').slice(0,10);
            	}else if(viewValue.slice(0,2).match(/^(0(3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|80))/)){
            		return viewValue.replace(/[^0-9]/g, '').slice(0,11);
            	}else
            		return viewValue.replace(/[^0-9]/g, '').slice(0,12);
            });

            // This runs when the model gets updated on the scope directly and keeps our view in sync
            ngModelCtrl.$render = function() {
                $element.val($filter('fax')(ngModelCtrl.$viewValue, false));
            };

            $element.bind('change', listener);
            $element.bind('keydown', function(event) {
                var key = event.keyCode;
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
                    return;
                }
                $browser.defer(listener); // Have to do this or changes don't get picked up properly
            });

            $element.bind('paste cut', function() {
                $browser.defer(listener);
            });
        }

    };
});
commonServiceModule.directive('telInput', function($filter, $browser) {// 전화번호 입력 형식
	//console.log("test");
    return {
        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
            var listener = function() {
                var value = $element.val().replace(/[^0-9]/g, '');
                $element.val($filter('tel')(value, false));
            };

            // This runs when we update the text field
            ngModelCtrl.$parsers.push(function(viewValue) {
            	if(viewValue.slice(0,2).match(/^(02)/)){
            		return viewValue.replace(/[^0-9]/g, '').slice(0,10);
            	}else if(viewValue.slice(0,2).match(/^(0(1[0|1|6|7|8|9]|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|80))/)){
            		return viewValue.replace(/[^0-9]/g, '').slice(0,11);
            	}else
            		return viewValue.replace(/[^0-9]/g, '').slice(0,12);

            });

            // This runs when the model gets updated on the scope directly and keeps our view in sync
            ngModelCtrl.$render = function() {
                $element.val($filter('tel')(ngModelCtrl.$viewValue, false));
            };

            $element.bind('change', listener);
            $element.bind('keydown', function(event) {
                var key = event.keyCode;
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
                    return;
                }
                $browser.defer(listener); // Have to do this or changes don't get picked up properly
            });

            $element.bind('paste cut', function() {
                $browser.defer(listener);
            });
        }

    };
});
commonServiceModule.directive('regInput', function($filter, $browser) {// 전화번호 입력 형식
	//console.log("test");
    return {
        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
            var listener = function() {
                var value = $element.val().replace(/[^0-9]/g, '');
                $element.val($filter('reg')(value, false));
            };

            // This runs when we update the text field
            ngModelCtrl.$parsers.push(function(viewValue) {
                return viewValue.replace(/[^0-9]/g, '').slice(0,10);
            });

            // This runs when the model gets updated on the scope directly and keeps our view in sync
            ngModelCtrl.$render = function() {
                $element.val($filter('reg')(ngModelCtrl.$viewValue, false));
            };

            $element.bind('change', listener);
            $element.bind('keydown', function(event) {
                var key = event.keyCode;
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
                    return;
                }
                $browser.defer(listener); // Have to do this or changes don't get picked up properly
            });

            $element.bind('paste cut', function() {
                $browser.defer(listener);
            });
        }

    };
});
// 숫자 이외 입력 금지
commonServiceModule.directive('numberOnly', function($filter, $browser) {
    return {
//        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
            $element.bind('keydown', function(event) {
            	var key = event.keyCode;
            	if (key == 8 || key == 91 || (15 < key && key < 19) || (35 <= key && key <= 40)){
            		return true;
            	} else {
            		var value = event.key.replace(/[^0-9]/g, '');
            		if (value == '') {
            			return false;
            		} 
            	}

            });
        }

    };
});


//금액 표시 천단위 표시(1,234)(with comma)
commonServiceModule.directive('priceInput', function($filter, $browser) {
    return {
        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
            var listener = function() {
                var value = $element.val().replace(/[^0-9]/g, '');
                $element.val($filter('price')(value, false));
            };

            // This runs when we update the text field
            ngModelCtrl.$parsers.push(function(viewValue) {
                return viewValue.replace(/[^0-9]/g, '').slice(0,10);
            });

            // This runs when the model gets updated on the scope directly and keeps our view in sync
            ngModelCtrl.$render = function() {
            	$element.val($filter('price')(ngModelCtrl.$viewValue, false));
            };

            $element.bind('change', listener);
            $element.bind('keydown', function(event) {
                var key = event.keyCode;
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
                    return;
                }
                $browser.defer(listener); // Have to do this or changes don't get picked up properly
            });

            $element.bind('paste cut', function() {
                $browser.defer(listener);
            });
        }

    };
});

// input text number(with comma)
commonServiceModule.directive('format', ['$filter', function ($filter) {
    return {
        require: '?ngModel',
        link: function (scope, elem, attrs, ctrl) {
            if (!ctrl) return;

            ctrl.$formatters.unshift(function (a) {
                return $filter(attrs.format)(ctrl.$modelValue)
            });

            ctrl.$parsers.unshift(function (viewValue) {
                var plainNumber = viewValue.replace(/[^\d|\-+|\.+]/g, '');
                elem.val($filter(attrs.format)(plainNumber));
                return plainNumber;
            });
        }
    };
}]);

/**
 * URL validation
 * 
 */
commonServiceModule.directive("urlInput", function($compile, commonFactory) {
	return {
		restrict : 'A',
		replace : false,
		terminal : true,
		priority : 1000,
		link : function(scope, element, attrs) {
			
			var err;
			var inputName = element.attr('name');
			var v_form = element.closest("form").attr('name');
			var v_name;
			if(angular.isUndefined(inputName)){
				var ngModel = attrs.ngModel;
				v_name = ngModel.replace(/\./g,"_");  
			}else{
				v_name = inputName;
			}
			function recompile(){
				
				if(angular.isUndefined(inputName)){
					attrs.$set('name', v_name);
				}
				
				//재컴파일 하므로 디렉티브 삭제
				element.removeAttr("url-input");
			}
			
			var regExp = /^(((http(s?))\:\/\/))((www.)?)([0-9a-zA-Z\-]+\.)+[a-zA-Z]{2,6}(\:[0-9]+)?(\/\S*)?$/;
			
			attrs.$set('dataNgPattern', regExp);

			recompile();
		}
	}
});

// 이미지 태그 : E타입(태그생성)
commonServiceModule.directive('imgTag', function(commonService) {
    return {    	
		restrict : 'E',
		replace : true,
		terminal : true,
		priority : 1000,      
		template : function(elem, attr) {
			var tag = "<img src=''/>";
			return tag;
		},
        link: function postLink(scope, element, attrs) {
        	
        	scope.$watch(attrs.ngModel, function(nVal) { 
        		if(common.isEmpty(nVal)){
        			return;
        		}
        		attrs.$set('src', global.config.sslImageDomain + nVal);
        	});            
            
        }       
      }
});

//이미지 태그 :A타입 : path에 이미지 도메인을 붙인다.
commonServiceModule.directive('imgDomain', function(commonService) {
    return {    	
		restrict : 'A',
		replace : false,
		terminal : true,
		priority : 10000,      
        link: function postLink(scope, element, attrs) {
        	scope.$watch(attrs.ngSrc, function(nVal) { 
        		if(common.isEmpty(nVal)){
        			return;
        		}
        		attrs.$set('src', global.config.sslImageDomain + nVal);
        	});            
            
        }       
      }
});


// 상품명 일부 특수문자&단어 제외
commonServiceModule.directive('productExceptWord', function($filter, $browser) {
    return {
        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
        	var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]|최저가|노마진|특가+/g;
        	
            var listener = function() {
                var value = $element.val().replace(regExp, '');
                $element.val(value);
            };

            // This runs when we update the text field
            ngModelCtrl.$parsers.push(function(viewValue) {
                return viewValue.replace(regExp, '');
            });

            // This runs when the model gets updated on the scope directly and keeps our view in sync
            ngModelCtrl.$render = function() {
            	if(!common.isEmpty(ngModelCtrl.$viewValue)){
            		$element.val(ngModelCtrl.$viewValue.replace(regExp, ''));
            	}else{
            		$element.val(ngModelCtrl.$viewValue);
            	}
            };

            $element.bind('change', listener);
            $element.bind('keydown', function(event) {
                var key = event.keyCode;
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
//                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
//                    return;
//                }
                $browser.defer(listener); // Have to do this or changes don't get picked up properly
            });

            $element.bind('paste cut', function() {
                $browser.defer(listener);
            });
        }

    };
});

//상품 강조문구 일부 특수문자 제외
commonServiceModule.directive('productExceptWord2', function($filter, $browser) {
    return {
        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
        	var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$\\\=\(\'\"]+/g;
        	
            var listener = function() {
                var value = $element.val().replace(regExp, '');
                $element.val(value);
            };

            // This runs when we update the text field
            ngModelCtrl.$parsers.push(function(viewValue) {
                return viewValue.replace(regExp, '');
            });

            // This runs when the model gets updated on the scope directly and keeps our view in sync
            ngModelCtrl.$render = function() {
            	if(!common.isEmpty(ngModelCtrl.$viewValue)){
            		$element.val(ngModelCtrl.$viewValue.replace(regExp, ''));
            	}else{
            		$element.val(ngModelCtrl.$viewValue);
            	}
            };

            $element.bind('change', listener);
            $element.bind('keydown', function(event) {
                var key = event.keyCode;
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
//                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
//                    return;
//                }
                $browser.defer(listener); // Have to do this or changes don't get picked up properly
            });

            $element.bind('paste cut', function() {
                $browser.defer(listener);
            });
        }

    };
});
