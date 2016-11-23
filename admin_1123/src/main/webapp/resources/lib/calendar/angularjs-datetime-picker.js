(function() {
	'use strict';

	angular.module('ui.date', []);

	var getTimezoneOffset = function(date) {
		(typeof date == 'string') && (date = new Date(date));
		var jan = new Date(date.getFullYear(), 0, 1);
		var jul = new Date(date.getFullYear(), 6, 1);
		var stdTimezoneOffset = Math.max(jan.getTimezoneOffset(), jul.getTimezoneOffset());
		var isDST = date.getTimezoneOffset() < stdTimezoneOffset;
		var offset = isDST ? stdTimezoneOffset - 60 : stdTimezoneOffset;
		var diff = offset >= 0 ? '-' : '+';
		return diff + ("0" + (offset / 60)).slice(-2) + ':' + ("0" + (offset % 60)).slice(-2);
	};

	var DatetimePicker = function($compile, $document, $controller) {
		var datetimePickerCtrl = $controller('DatetimePickerCtrl'); //directive controller
		return {
			open : function(options) {
				datetimePickerCtrl.openDatetimePicker(options);
			},
			close : function() {
				datetimePickerCtrl.closeDatetimePicker();
			}
		};
	};
	DatetimePicker.$inject = ['$compile', '$document', '$controller'];
	angular.module('ui.date').factory('DatetimePicker', DatetimePicker);

	var DatetimePickerCtrl = function($compile, $document) {
		var datetimePickerEl;
		var _this = this;
		var removeEl = function(el) {
			el && el.parentNode && el.parentNode.removeChild(el);	//      el && el.remove();
			$document[0].body.removeEventListener('click', _this.closeDatetimePicker);
		};

		this.openDatetimePicker = function(options) {
			this.closeDatetimePicker();
			var div = angular.element('<div datetime-picker-popup ng-cloak></div>');
			options.dateFormat && div.attr('date-format', options.dateFormat);
			options.ngModel && div.attr('ng-model', options.ngModel);
			options.year && div.attr('year', parseInt(options.year));
			options.month && div.attr('month', parseInt(options.month));
			options.day && div.attr('day', parseInt(options.day));
			options.hour && div.attr('hour', parseInt(options.hour));
			options.minute && div.attr('minute', parseInt(options.minute));
			options.grid && div.attr('grid', parseInt(options.grid));
			options.regular && div.attr('grid', parseInt(options.regular));
			options.rowIdx && div.attr('row-idx', options.rowIdx);
			if (options.dateOnly === '' || options.dateOnly === true) {
				div.attr('date-only', 'true');
			}
			if (options.periodEnd === '' || options.periodEnd === true) {
				div.attr('period-end', 'true');
			}
			if (options.periodStart === '' || options.periodStart === true) {
				div.attr('period-start', 'true');
			}
			options.maxDate && div.attr('max-date', options.maxDate);
			options.minDate && div.attr('min-date', options.minDate);
			if (options.closeOnSelect === 'false') {
				div.attr('close-on-select', 'false');
			}

			if (options.grid) {
				div.attr('grid', 'true');
			}
			
			if (options.regular) {
				div.attr('regular', 'true');
			}
			
			var triggerEl = options.triggerEl;
			options.scope = options.scope || angular.element(triggerEl).scope();
			datetimePickerEl = $compile(div)(options.scope)[0];
			datetimePickerEl.triggerEl = options.triggerEl;

			$document[0].body.appendChild(datetimePickerEl);

			//show datetimePicker below triggerEl
			var bcr = triggerEl.getBoundingClientRect();

			options.scope.$apply();

			var datePickerElBcr = datetimePickerEl.getBoundingClientRect();

			datetimePickerEl.style.position = 'absolute';
			if (bcr.width > datePickerElBcr.width) {
				//datetimePickerEl.style.left= (bcr.left + bcr.width - datePickerElBcr.width + window.scrollX) + 'px';
				datetimePickerEl.style.left = (bcr.left + bcr.width - datePickerElBcr.width + $(window).scrollLeft()) + 'px';
			} else {
				//datetimePickerEl.style.left= (bcr.left + window.scrollX) + 'px';
				datetimePickerEl.style.left = (bcr.left + $(window).scrollLeft()) + 'px';
			}

			if (bcr.top < 300 || window.innerHeight - bcr.bottom > 300) {
				//datetimePickerEl.style.top = (bcr.bottom + window.scrollY) + 'px';
				datetimePickerEl.style.top = (bcr.bottom + $(window).scrollTop()) + 'px';
			} else {
				//datetimePickerEl.style.top = (bcr.top - datePickerElBcr.height + window.scrollY) + 'px';
				datetimePickerEl.style.top = (bcr.top - datePickerElBcr.height + $(window).scrollTop()) + 'px';
			}
			
			$document[0].body.addEventListener('click', this.closeDatetimePicker);
		};

		this.closeDatetimePicker = function(evt) {
			var target = evt && evt.target;
			var popupEl = $document[0].querySelector('div[datetime-picker-popup]');
			if (evt && target) {
				if (target.hasAttribute('datetime-div')) { // element with datetimePicker behaviour
					// do nothing
				} else if (popupEl && popupEl.contains(target)) { // datetimePicker itself
					// do nothing
				} else {
					removeEl(popupEl);
				}
			} else {
				removeEl(popupEl);
			}
		}
	};
	DatetimePickerCtrl.$inject = ['$compile', '$document'];
	angular.module('ui.date').controller('DatetimePickerCtrl', DatetimePickerCtrl);

	var tmpl = [
			'<div class="angularjs-datetime-picker">',
			'	<div class="adp-month">',
			'		<button type="button" class="adp-prevYear" ng-click="addYear(-1)">&laquo;</button>',
			'		<button type="button" class="adp-prev" ng-click="addMonth(-1)">&lsaquo;</button>',
			'		<span title="{{months[mv.month].fullName}}">{{months[mv.month].shortName}}</span> {{mv.year}}',
			'		<button type="button" class="adp-next" ng-click="addMonth(1)">&rsaquo;</button>',
			'		<button type="button" class="adp-nextYear" ng-click="addYear(1)">&raquo;</button>',
			'	</div>',
			'	<div class="adp-days" ng-click="setDate($event, true)">',
			'		<div class="adp-day-of-week" ng-repeat="dayOfWeek in ::daysOfWeek" title="{{dayOfWeek.fullName}}">{{::dayOfWeek.firstLetter}}</div>',
			'		<div class="adp-day" ng-show="mv.leadingDays.length < 7" ng-repeat="day in mv.leadingDays">{{::day}}</div>',
			'		<div class="adp-day " ng-repeat="day in mv.days" today="{{today}}" d2="{{mv.year + \'-\' + (mv.month + 1) + \'-\' + day}}"',
			'			ng-class="{',
			'						selected: (day == selectedDay),',
			'						today: (today == (mv.year + \'-\' + (mv.month + 1) + \'-\' + day)),',
			'						weekend: (mv.leadingDays.length + day)%7 == 1 || (mv.leadingDays.length + day)%7 == 0',
			'						,selectable: isAvailable(day, (mv.leadingDays.length + day)%7)',
			'		}">',
			'		{{::day}}',
			'		</div>',
			'		<div class="adp-day" ng-show="mv.trailingDays.length < 7" ng-repeat="day in mv.trailingDays">{{::day}}</div>',
			'	</div>',
			'	<div class="adp-days" id="adp-time"> ',
//			'		<label class="timeLabel">Time:</label> <span class="timeValue">{{("0"+inputHour).slice(-2)}} : {{("0"+inputMinute).slice(-2)}} : {{("0"+inputSecond).slice(-2)}}</span><br/>',
/*			'		<label class="hourLabel">Hour:</label> <input class="hourInput" type="range" min="0" max="23" ng-model="inputHour" ng-change="updateNgModel()" />',
			'		<label class="minutesLabel">Min:</label> <input class="minutesInput" type="range" min="0" max="59" ng-model="inputMinute"  ng-change="updateNgModel()"/> ',
			'		<label class="minutesLabel">Sec:</label> <input class="minutesInput" type="range" min="0" max="59" ng-model="inputSecond"  ng-change="updateNgModel()"/> ',*/
			'		<input class="minutesInput" style="width:20px" type="text" maxlength="2" ng-model="inputHour" ng-change="checkHour()"/> 시 :',
			'		<input class="minutesInput" style="width:20px" type="text" maxlength="2" ng-model="inputMinute" ng-change="checkMinute()"/> 분 : ',
			'		<input class="minutesInput" style="width:20px" type="text" maxlength="2" ng-model="inputSecond" ng-change="checkSecond()" /> 초',
			'		<button type="button" class="btn_exType" ng-click="setDate(\'setTime\', false)"><b >시간입력</b></button> ',
			'	</div> ',
			'</div>'
	].join("\n");

	var datetimePickerPopup = function($locale, dateFilter, $interval) {
		var days, months, daysOfWeek, firstDayOfWeek;
		var initVars = function() {
			days = [], months = [];
			daysOfWeek = [], firstDayOfWeek = 0;
			for (var i = 1; i <= 31; i++) {
				days.push(i);
			}

			for (var i = 0; i < 12; i++) { //jshint ignore:line
				months.push({
					fullName : $locale.DATETIME_FORMATS.MONTH[i],
					shortName : $locale.DATETIME_FORMATS.SHORTMONTH[i]
				});
			}

			for (var i = 0; i < 7; i++) { //jshint ignore:line
				var day = $locale.DATETIME_FORMATS.DAY[(i + firstDayOfWeek) % 7];

				daysOfWeek.push({
					fullName : day,
					firstLetter : day.substr(0, 1)
				});
			}
			firstDayOfWeek = 0;
		};

		var getMonthView = function(year, month) {
			if (month > 11) {
				year++;
			} else if (month < 0) {
				year--;
			}
			month = (month + 12) % 12;
			var firstDayOfMonth = new Date(year, month, 1)
			  , lastDayOfMonth = new Date(year, month + 1, 0)
			  , lastDayOfPreviousMonth = new Date(year, month, 0)
			  , daysInMonth = lastDayOfMonth.getDate()
			  , daysInLastMonth = lastDayOfPreviousMonth.getDate()
			  , dayOfWeek = firstDayOfMonth.getDay()
			  , leadingDays = (dayOfWeek - firstDayOfWeek + 7) % 7 || 7	// Ensure there are always leading days to give context
			  , trailingDays = days.slice(0, 6 * 7 - (leadingDays + daysInMonth));
			
			if (trailingDays.length > 7) {
				trailingDays = trailingDays.slice(0, trailingDays.length - 7);
			}

			return {
				year : year,
				month : month,
				days : days.slice(0, daysInMonth),
				leadingDays : days.slice(-leadingDays - (31 - daysInLastMonth), daysInLastMonth),
				trailingDays : trailingDays
			};
		};

		var linkFunc = function(scope, element, attrs, ctrl) { //jshint ignore:line
			initVars(); //initialize days, months, daysOfWeek, and firstDayOfWeek;

			if (angular.isUndefined(attrs.dateFormat)) {
				if (angular.isDefined(attrs.dateOnly)) {
					//attrs.dateFormat = Constants.default_date_format_2;
					attrs.dateFormat = "yyyy-MM-dd";
				} else {
//					attrs.dateFormat = Constants.default_date_format_1;
					attrs.dateFormat = "yyyy-MM-dd HH:mm:ss";
				}
			}
			scope.checkHour = function($event){
				
				if(!common.checkNumber(scope.inputHour)){
					scope.inputHour = '';
				}
				
				if(scope.inputHour > 23){
					scope.inputHour = 23;
				}
	        }
			scope.checkMinute = function($event){
				if(!common.checkNumber(scope.inputMinute)){
					scope.inputMinute = '';
				}
				
				if(scope.inputMinute > 59){
					scope.inputMinute = 59;
				}
	        }
			scope.checkSecond = function($event){
				if(!common.checkNumber(scope.inputSecond)){
					scope.inputSecond = '';
				}
				
				if(scope.inputSecond > 59){
					scope.inputSecond = 59;
				}
	        }
			
			var dateFormat = attrs.dateFormat || 'short';

			scope.months = months;
			scope.daysOfWeek = daysOfWeek;
			scope.inputHour;
			scope.inputMinute;
			scope.inputSecond;

			if (scope.dateOnly === true) {
				element[0].querySelector('#adp-time').style.display = 'none';
			}

			scope.$applyAsync(function() {
				ctrl.triggerEl = angular.element(element[0].triggerEl);
				if (attrs.ngModel) { // need to parse date string
					var dateStr = ctrl.triggerEl.scope().$eval(attrs.ngModel);
					if (dateStr && angular.isDefined(dateStr)) {
//						if (!dateStr.match(/[0-9]{2}:/)) { // if no time is given, add 00:00:00 at the end
//							dateStr += " 00:00:00";
//						}
//						dateStr = dateStr.replace(/([0-9]{2}-[0-9]{2})-([0-9]{4})/, '$2-$1'); //mm-dd-yyyy to yyyy-mm-dd
//						dateStr = dateStr.replace(/([\/-][0-9]{2,4})\ ([0-9]{2}\:[0-9]{2}\:)/, '$1T$2'); //reformat for FF
//						dateStr = dateStr.replace(/EDT|EST|CDT|CST|MDT|PDT|PST|UT|GMT/g, ''); //remove timezone
//						dateStr = dateStr.replace(/\s*\(\)\s*/, ''); //remove timezone
//						dateStr = dateStr.replace(/[\-\+][0-9]{2}:?[0-9]{2}$/, ''); //remove timezone
//						dateStr += getTimezoneOffset(dateStr);
						var agent = navigator.userAgent.toLowerCase();
						// for IE browser
						if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
							if (dateStr.length > 10) {	
								dateStr = (dateStr.substring(0,10).trim() + 'T' + dateStr.substring(10).trim());
							}
						}
						
						var d = new Date(dateStr);
						scope.selectedDate = new Date(d.getFullYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds());
					}
				}
				//!isNaN(scope.selectedDate.getTime()
				var isExistValue = scope.selectedDate;
//				if (isExistValue && attrs.periodStart) { // no predefined date
//					var today = new Date();
//					var year = isExistValue.year || today.getFullYear();
//					var month = isExistValue.month ? (scope.month - 1) : today.getMonth();
//					var day = isExistValue.day || today.getDate();
//					var hour = isExistValue.hour || 0;
//					var minute = isExistValue.minute || 0;
//					var second = isExistValue.second || 0;
//					scope.selectedDate = new Date(year, month, day, hour, minute, second);
//				}else if(isExistValue && attrs.periodEnd){
//					var today = new Date();
//					var year = isExistValue.year || today.getFullYear();
//					var month = isExistValue.month ? (isExistValue.month - 1) : today.getMonth();
//					var day = isExistValue.day || today.getDate();
//					var hour = isExistValue.hour || 23;
//					var minute = isExistValue.minute || 59;
//					var second = isExistValue.second || 59;
//					scope.selectedDate = new Date(year, month, day, hour, minute, second);
//				}else{
//					var today = new Date();
//					var year = scope.year || today.getFullYear();
//					var month = scope.month ? (scope.month - 1) : today.getMonth();
//					var day = scope.day || today.getDate();
////					var hour = scope.hour || 0;
////					var minute = scope.minute || 0;
////					var second = scope.second || 0;
//					var hour = scope.hour || today.getHours();
//					var minute = scope.minute || today.getMinutes();
//					var second = scope.second || today.getSeconds();					
//					scope.selectedDate = new Date(year, month, day, hour, minute, second);
//				}
				if (!isExistValue) {
					var today = new Date();
					var year = today.getFullYear();
					var month = today.getMonth();
					var day = today.getDate();
					var hour, minute, second;
					if (attrs.periodStart) {
						hour = 0;
						minute = 0;
						second = 0;
					} else if (attrs.periodEnd) {
						hour = 23;
						minute = 59;
						second = 59;
					} else {
						// hour = today.getHours();
						// minute = today.getMinutes();
						// second = today.getSeconds();
						hour = 0;
						minute = 0;
						second = 0;
					}
					
					scope.selectedDate = new Date(year, month, day, hour, minute, second);
				}
				scope.inputHour = scope.selectedDate.getHours();
				scope.inputMinute = scope.selectedDate.getMinutes();
				scope.inputSecond = scope.selectedDate.getSeconds();

				// Default to current year and month
				scope.mv = getMonthView(scope.selectedDate.getFullYear(), scope.selectedDate.getMonth());
				scope.today = dateFilter(new Date(), 'yyyy-M-d');
				if (scope.mv.year == scope.selectedDate.getFullYear() && scope.mv.month == scope.selectedDate.getMonth()) {
					scope.selectedDay = scope.selectedDate.getDate();
				} else {
					scope.selectedDay = null;
				}
			});

			scope.addMonth = function(amount) {
				scope.mv = getMonthView(scope.mv.year, scope.mv.month + amount);
			};
			
			scope.addYear = function(amount) {
				scope.mv = getMonthView(scope.mv.year + amount, scope.mv.month);
			};
			

			scope.isAvailable = function(day, dayLength) {
				var selectedDate = dateFilter(new Date(scope.mv.year, scope.mv.month, day), "yyyy-MM-dd");
//				console.log('attrs.maxDate : ', attrs.maxDate);
//				console.log('attrs.minDate : ', attrs.minDate);

				var isAvailable = true;
//				if (angular.isDefined(attrs.maxDate)) {
				if (attrs.maxDate != undefined && attrs.maxDate != '' && attrs.maxDate != null) {
					if (selectedDate > attrs.maxDate) {
						isAvailable = false;
					}
				}
//				if (angular.isDefined(attrs.minDate)) {
				if (attrs.minDate != undefined && attrs.minDate != '' && attrs.minDate != null) {
					if (selectedDate < attrs.minDate) {
						isAvailable = false;
					}
				}
//				정기배송, (일,월) 선택불가
				if (angular.isDefined(attrs.regular)) {
					if(dayLength == '2' || dayLength == '1'){
						isAvailable = false;
					}
				}
				
				
				return isAvailable;
			}

			scope.setDate = function(evt, bool) {
				var div = angular.element('<div datetime-picker-popup ng-cloak></div>');
				if(evt == "setTime") {
					var selectDate = scope.selectedDate.getDate();
					var result = scope.updateNgModel(parseInt(selectDate), true);
					if (scope.closeOnSelect !== false && result) {
						if (scope.dateOnly === true || bool === false) {
							ctrl.closeDatetimePicker();
						}else if(bool === true){
						}
					}
				} else {
					var target = angular.element(evt.target)[0];
					if (target.className.indexOf('selectable') !== -1) {
						var result = scope.updateNgModel(parseInt(target.innerHTML));
						if (scope.closeOnSelect !== false && result) {
							if (scope.dateOnly === true || bool === false) {
								ctrl.closeDatetimePicker();
							}else if(bool === true){
							}
						}
					}
				}
			};

			scope.updateNgModel = function(day, setTime) {

				day = day ? day : scope.selectedDate.getDate();
				if (angular.isDefined(attrs.dateOnly) && angular.isDefined(attrs.periodEnd)) {
					scope.selectedDate = new Date(scope.mv.year, scope.mv.month, day, 23, 59, 59);
				} else if (angular.isDefined(attrs.dateOnly) && angular.isDefined(attrs.periodStart)) {
					scope.selectedDate = new Date(scope.mv.year, scope.mv.month, day);
				} else {
					scope.selectedDate = new Date(scope.mv.year, scope.mv.month, day, scope.inputHour, scope.inputMinute, scope.inputSecond);
				}
				
				scope.selectedDay = scope.selectedDate.getDate();
				if (attrs.ngModel) {
					var elScope = ctrl.triggerEl.scope(), dateValue;
					if (elScope.$eval(attrs.ngModel) && elScope.$eval(attrs.ngModel).constructor.name === 'Date') {
						dateValue = new Date(dateFilter(scope.selectedDate, dateFormat));
					} else {
						dateValue = dateFilter(scope.selectedDate, dateFormat);
					}

					if (attrs.periodStart) {
						var endModel = attrs.ngModel.replace("Start", "End").replace("start", "end")
						var endValue = elScope.$eval(endModel);
						var selectedDate = dateValue;
						if (endValue && endValue.length == 10) {
							selectedDate = dateFilter(scope.selectedDate, "yyyy-MM-dd");
						}
						if (endValue && endValue < selectedDate) {
							alert("종료일보다 클 수 없습니다.");
							return false;
						}
					} else if (attrs.periodEnd) {

						var startModel = attrs.ngModel.replace("End", "Start").replace("end", "start")
						var startValue = elScope.$eval(startModel);
						var selectedDate = dateValue;
						if (startValue && startValue.length == 10) {
							selectedDate = dateFilter(scope.selectedDate, "yyyy-MM-dd");
						}
						if (startValue && startValue > selectedDate) {
							alert("시작일보다 작을 수 없습니다.");
							return false;
						}
					}
					
					// 실제 저장될 모델
					elScope.$eval(attrs.ngModel + '= date', {
						date : dateValue
					});

					// text박스에 보여줄 모델
					if(angular.isUndefined(setTime)) {
						elScope.$eval(attrs.ngModel + 'Input= date', {
							date : dateFilter(scope.selectedDate, attrs.dateFormat)
						});
					}

					if (angular.isUndefined(attrs.grid)) {
						$(".btn_day").siblings().removeClass("on");
					} else {
						if (angular.isDefined(elScope.col)) {
							for (var i = 0; i < elScope.col.grid.rows.length; i++) {
								if (elScope.col.grid.rows[i].entity.$$hashKey == attrs.rowIdx) {
									var data = [];
									data.push(elScope.col.grid.rows[i].entity);
									$interval(function() {
										elScope.col.grid.api.rowEdit.setRowsDirty(data);
									}, 1, 1);

									if (dateValue != "") {
										elScope.grid.api.edit.raise.afterCellEdit(elScope.col.grid.rows[i].entity, elScope.col.colDef, dateValue, "");
									}
								}
							}
						}
					}
				}

				return true;
			};

			scope.$on('$destroy', ctrl.closeDatetimePicker);
		};
		return {
			restrict : 'A',
			template : tmpl,
			controller : 'DatetimePickerCtrl',
			replace : true,
			scope : {
				year : '=',
				month : '=',
				day : '=',
				hour : '=',
				minute : '=',
				dateOnly : '=',
				periodEnd : '=',
				periodStart : '=',
				closeOnSelect : '='
			},
			link : linkFunc
		};
	};
	datetimePickerPopup.$inject = ['$locale', 'dateFilter', '$interval'];
	angular.module('ui.date').directive('datetimePickerPopup', datetimePickerPopup);

	var datetimePicker = function($parse, DatetimePicker, $interval, $filter) {
		return {
			// An ngModel is required to get the controller argument
			require : 'ngModel',
			replace : true,//<div class="enhanced-datepicker"><div class="proxied-field-wrap">
			template : function(elem, attrs) {
				var width = "70px;";
				if (angular.isDefined(attrs.width)) {
					width = attrs.width;
				} else {
					if (angular.isUndefined(attrs.dateOnly)) {
						width = "120px;";
					}
				}
				var tag = '<div style="display:inline-block;">';
				tag += '<input type="text" style="width:' + width + '" readOnly ng-model="' + attrs.ngModel + 'Input" value="' + attrs.value + '" placeholder=""  datetime-div/>&nbsp;&nbsp;';
				tag += '<button type="button" class="btn_date" datetime-div>달력</button>'
				tag += '</div>';
				
				if (angular.isDefined(attrs.grid)) {
					
					tag = '<div class="ui-grid-cell-contents" row-idx="{{grid.options.getRowIdentity(row.entity)}}" '; 
					tag += 'ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" title="{{grid.validate.getTitleFormattedErrors(row.entity,col.colDef)}}">';
					tag += '<input type="hidden" ui-grid-editor ng-model="' + attrs.ngModel + '" style="width:' + width + '">';
					tag += '{{' + attrs.ngModel + '}}&nbsp;&nbsp;';
					tag += '<button type="button" class="btn_date" datetime-div>달력</button>';
					tag += '</div>';
				}

				return tag;
			},
			link : function(scope, element, attrs, ctrl) {
//				scope.$eval(attrs.ngModel + "Input="+attrs.ngModel);
				
				// Attach validation watcher
				scope.$watch(attrs.ngModel, function(value) {

					if (!value || value == '') {
						return;
					}

					// The value has already been cleaned by the above code
					var date = new Date(value);
					ctrl.$setValidity('date', !date ? false : true);
					var now = new Date();
					if (attrs.hasOwnProperty('futureOnly')) {
						ctrl.$setValidity('future-only', date < now ? false : true);
					}
					var fommedValue;
					if (angular.isDefined(attrs.dateOnly)) {
						fommedValue = $filter('yyyymmdd')(scope.$eval(attrs.ngModel));
					} else {
						fommedValue = scope.$eval(attrs.ngModel);
					}
					scope.$eval(attrs.ngModel + "Input='" + fommedValue + "'");
				});

				var input = element.find("input");

				input[0].addEventListener('change', function(evt) {
					if (angular.isDefined(scope.col)) {
						for (var i = 0; i < scope.col.grid.rows.length; i++) {
							if (scope.col.grid.rows[i].entity.$$hashKey == attrs.rowIdx) {

								var data = [];
								data.push(scope.col.grid.rows[i].entity);
								$interval(function() {
									scope.col.grid.api.rowEdit.setRowsDirty(data);
								}, 1, 1);

								if (input[0].value != "") {
									scope.grid.api.edit.raise.afterCellEdit(scope.col.grid.rows[i].entity, scope.col.colDef, input[0].value, "");
								}
							}
						}
					}
				});

				element[0].addEventListener('click', function() {
					var gYn = false;
					if (angular.isDefined(attrs.grid)) {
						gYn = true;
					}
					var regularYn = false;
					if (angular.isDefined(attrs.regularDate)) {
						regularYn = true;
					}
					console.log('test1', angular.isDefined(attrs.regularDate));
					DatetimePicker.open({
						triggerEl : element[0],
						dateFormat : attrs.dateFormat,
						ngModel : attrs.ngModel,
						year : attrs.year,
						month : attrs.month,
						day : attrs.day,
						hour : attrs.hour,
						minute : attrs.minute,
						dateOnly : attrs.dateOnly,
						periodEnd : attrs.periodEnd,
						periodStart : attrs.periodStart,
						maxDate : attrs.maxDate,
						minDate : attrs.minDate,
						futureOnly : attrs.futureOnly,
						closeOnSelect : attrs.closeOnSelect,
						grid : gYn,
						rowIdx : attrs.rowIdx,
						regular : regularYn
					});
				});
			}
		};
	};
	datetimePicker.$inject = ['$parse', 'DatetimePicker', '$interval', '$filter'];
	angular.module('ui.date').directive('datetimePicker', datetimePicker);
})();
