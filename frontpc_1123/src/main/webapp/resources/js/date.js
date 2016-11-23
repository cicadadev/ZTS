/**
 * + basic
 * 	- 시작일<input id="startDate"/>
 * 	- 종료일<input id="endDate"/>
 * 	- 기본 세팅일 = 현재
 *  - input에 callback="각 페이지 조회 메소드"
 *    ex) <input .... calback="mypage.carrot.search">
 * + option
 *  - 기간버튼 설정
 *  	- input에 period-set 추가
 *  
 *  - 기간버튼 이전/이후 설정
 *  	- default 이전일 설정
 *  	- 이후일 설정시 after-set 추가
 *  
 *  - 기본 세팅일
 *  	- input에 day="0" 추가
 *  	- 0= 1개월, 1= 3개월, 2= 6개월
 * */
$(function() {

	// 날짜
	makeDate = function(param, before) {

		// 월, 일 한자리 두자리로 포맷팅
		function setFormat(val) {
			if (val < 10) {
				val = '0' + val;
			}
			return val;
		}

		var today = new Date();
		var editMonth;
		if (param != undefined) {
			if (param == '0') {
				editMonth = before == true ? today.getMonth() - 1 : today.getMonth() + 1;
			} else if (param == '1') {
				editMonth = before == true ? today.getMonth() - 3 : today.getMonth() + 3;
			} else if (param == '2') {
				editMonth = before == true ? today.getMonth() - 6 : today.getMonth() + 6;
			}
			// TODO 개별 원하는 날짜 세팅 필요한지?
			// else {
			// editMonth = before == true ? today.getMonth() - param : today.getMonth() + param;
			// }
			today.setMonth(editMonth);
		}
		var year = today.getFullYear();
		var month = today.getMonth() + 1;
		var day = today.getDate();
		month = setFormat(month);
		day = setFormat(day);

		return year + '-' + month + '-' + day;
	}

	// 기간 설정 버튼
	setDate = function(param, before, obj, callback) {
		
		var root = $(obj).parent().parent();
		
		root.children().find('a').removeClass('on');
		$(obj).addClass('on');
		
//		console.log("root.children().find('a')",root.children().find('a'));
		
//		$('.periodList').children().find('a').removeClass('on');
//		$('.periodList').children().eq(param).find('a').addClass('on');

		// 현재날짜
		var currentDate = makeDate();

		// 수정설정
		var editDate = makeDate(param, before);

		// var startDate = $('input[period-set]')[0].id;
		// var endDate = $('input[period-set]')[1].id;
		// var startDate = $('input[period-set]')[0].getAttribute('class');
		// var endDate = $('input[period-set]')[1].getAttribute('class');

		var start = root.next().find('input')[0].id;
		var end = root.next().find('input')[1].id;
		
		if (before) {
			$('#'+start).val(editDate);
			$('#'+end).val(currentDate);
		} else {
			$('#'+start).val(currentDate);
			$('#'+end).val(editDate);
		}
		
		// 모바일 월 버튼 클릭시 정해진 callback 실행
		if (ccs.common.mobilecheck() && common.isNotEmpty(callback)) {
			callback();
		}
	}
	
	
	/**
	 * 달력 초기화
	 */
	
	initCal =function(startDateId, endDateId){
		
		
		// 달력 기본 설정
		var dates = $('#'+startDateId+', #'+endDateId).datepicker({
			prevText: '이전 달',
			nextText: '다음 달',
			monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			monthNamesShort: ['01','02','03','04','05','06','07','08','09','10','11','12'],
			dayNames: ['일','월','화','수','목','금','토'],
			dayNamesShort: ['일','월','화','수','목','금','토'],
			dayNamesMin: ['일','월','화','수','목','금','토'],
			dateFormat : 'yy-mm-dd',
			showMonthAfterYear: true,
			showOtherMonths: true,
			selectOtherMonths: true,
			changeMonth: true,
			changeYear: true,
			yearRange: 'c-99:c+99',
			showOn: 'button',
			buttonImage: '/resources/img/pc/btn/btn_calendar.png',
			buttonImageOnly: false,
			buttonText: '날짜 선택',
			onSelect : function(selectedDate) {
				// var className =this.getAttribute('class').split(' ');
				var option = this.id == startDateId ? 'minDate' : 'maxDate'; 
						
				var instance = $(this).data('datepicker');
				var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defalts.dateFormat, selectedDate, instance.settings);
				dates.not(this).datepicker('option', option, date);
			}
		});
		
//		$("img.ui-datepicker-trigger").attr("style", "margin-left:2px; vertical-align:middle; cursor: Pointer;");    
//		$("#ui-datepicker-div").attr("style", "z-index:9999 !important");
		
		customCalender(startDateId, endDateId);
		
	}
	
	/**
	 * 달력 기간 관련 설정
	 */
	var customCalender = function(startDateId, endDateId){


		var before = true; // 이전, 이후 구분자
		if ($('input[after-set]')[0] != undefined) {
			before = false;
		}

		var currentDate = makeDate();

		// 당일 세팅
		if ($('input[day]')[0] == undefined) {
			$('#'+startDateId).val(currentDate);
			$('#'+endDateId).val(currentDate)
		}
		// 당일에서 원하는 날짜 세팅
		else {
			var param = $('input[day]')[0].getAttribute('day');
			var editDate = makeDate(param, before);
			if (before) {
				$('#'+startDateId).val(editDate);
				$('#'+endDateId).val(currentDate);
			} else {
				$('#'+startDateId).val(currentDate);
				$('#'+endDateId).val(editDate);
			}
		}

		// 달력 기간 버튼
		if ($('input[period-set]')[0] != undefined && $('input[period-set]')[1] != undefined) {

			// 모바일 달력 일때 callback 추가
			var callback = '\'\'';
			if (ccs.common.mobilecheck()) {
				if($('input[callback]')[0] != undefined) {
					if (common.isNotEmpty($('input[callback]')[0].getAttribute('callback'))) {
						callback = $('input[callback]')[0].getAttribute('callback');
					}
				}
			}
			
			// TODO 기간설정 버튼 유형 더 있는지 파악
			var button = $('input[day]')[0].getAttribute('day');
			var html = '';
//			html += '<div class="group group_date">';
//			html += '	<strong>조회기간</strong>';
//			html += '	<ul class="periodList">';
//			html += '		<li><a href="javascript:void(0)" class="btn_day" onclick="setDate(0,' + before + ' ,\'' + callback + '\');">1개월</a></li>';
//			html += '		<li><a href="javascript:void(0)" class="btn_day" onclick="setDate(1,' + before + ' ,\'' + callback + '\');">3개월</a></li>';
//			html += '		<li><a href="javascript:void(0)" class="btn_day" onclick="setDate(2,' + before + ' ,\'' + callback + '\');">6개월</a></li>';
			html += '		<li><a href="javascript:void(0)" class="btn_day" onclick="setDate(0,' + before + ', this, ' + callback + ');">1개월</a></li>';
			html += '		<li><a href="javascript:void(0)" class="btn_day" onclick="setDate(1,' + before + ', this, ' + callback + ');">3개월</a></li>';
			html += '		<li><a href="javascript:void(0)" class="btn_day" onclick="setDate(2,' + before + ', this, ' + callback + ');">6개월</a></li>';
			html += '		<li class="btnPeriod"><a href="javascript:void(0)">기간설정</a></li>';
//			html += '	</ul>';
//			html += '	<div class="calendarBox">';
//			html += '		<div class="btnR">';
//			html += '			<a href="javascript:void(0);" class="btn_x btn_close">닫기</a>';
//			html += '		</div>';
//			html += '		<span class="inpCalendar">';
//			html += '			<input type="text" id="startDate" period-set day="0" />';
//			html += '		</span>';
//			html += '		<span class="swung">~</span>';
//			html += '		<span class="inpCalendar">';
//			html += '			<input type="text" id="endDate" period-set />';
//			html += '		</span>';
//			html += '	</div>';
//			html += '</div>';
			
			var here = $("#"+startDateId).parent().parent().prev();
			here.append(html);
			here.children().eq(button).find('a').addClass('on');
		}

		// 마이페이지 기간설정 버튼
		if ($('.periodBox').length > 0) {
			$('.mobile .btnPeriod').off('click').on('click', function() {
				$(this).closest('.periodBox').toggleClass('on');
			});
		}
	
	}
	
});