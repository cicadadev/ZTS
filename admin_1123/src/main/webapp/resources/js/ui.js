document.createElement('header');
document.createElement('section');
document.createElement('article');
document.createElement('footer');
document.createElement('aside');
document.createElement('video');
document.createElement('audio');
document.createElement('nav');
document.createElement('figure');
document.createElement('figcaption');

$(document).ready(function() {
	/* 날짜 버튼 */
	$(".day_group button").on({
		"click" : function() {
			$(this).addClass("on").siblings().removeClass("on");
			fnCalendarRange($(this).text());

			$(this).parent().siblings("input[type='text']").eq(0).val(setYear + "/" + (setMonth+1) + "/" + setDay);
			$(this).parent().siblings("input[type='text']").eq(1).val(todayYear + "/" + (todayMonth+1) + "/" + todayDay);
		}
	});

	/* 오늘 날짜 */
	var today = new Date();
	var todayYear  = today.getFullYear();
	var todayMonth = today.getMonth();
	var todayDay = today.getDate();

	/* 계산된 날짜 */
	var settingDate;
	var setYear, setMonth, setDay;
	function fnCalendarRange(param) {
		settingDate = new Date();
		//console.log(settingDate.getFullYear(), settingDate.getMonth(), settingDate.getDate());

		if(param == "1주일") {
			settingDate.setDate(settingDate.getDate()-7);
		}else if(param == "15일"){
			settingDate.setDate(settingDate.getDate()-15);
		}else if(param == "30일"){
			settingDate.setDate(settingDate.getDate()-30);
		}else if(param == "60일"){
			settingDate.setDate(settingDate.getDate()-60);
		}else if(param == "90일"){
			settingDate.setDate(settingDate.getDate()-90);
		}

		setYear = settingDate.getFullYear();
		setMonth = settingDate.getMonth();
		setDay = settingDate.getDate();

		//console.log(setYear, setMonth, setDay);
	}

	/* 트리 구조 메뉴 : 2016.05.10 수정 */
	$(".category .list_dep button").on({
		"click" : function() {
			var cate_dep = parseInt($(this).parent().attr("class").replace("dep", "").replace("on", ""));

			if($(this).parent().hasClass("on")){
				$(this).parent().removeClass("on").nextUntil(".dep" + cate_dep, "li").hide();
				$(this).parent().nextUntil(".dep" + cate_dep, ".dep" + (cate_dep+1)).removeClass("on");
			}else{
				$(this).parent().addClass("on").nextUntil(".dep" + cate_dep, ".dep" + (cate_dep+1)).show();
			}
		}
	});
	$(".category .list_dep a").on({
		"click" : function(e) {
			if(!$(this).hasClass("active")){
				$(".category .list_dep a").removeClass("active");
				$(this).addClass("active");
			}

			e.preventDefault();
		}
	});

	/* 접근성 트리 구조 메뉴 : 2016.04.20 수정 */
	$(".category .list button").on({
		"click" : function() {
			if($(this).parent().hasClass("on")){
				$(this).parent().removeClass("on");
				$(this).next().find("li").removeClass("on");
			}else{
				if($(this).parent().has("ul").length > 0){
					$(this).parent().addClass("on");
				}else{
					$(this).parent().addClass("on").siblings("li").removeClass("on");
				}
			}
		}
	});

	/* 파일 첨부 버튼*/
	$(".btn_addFile").on({
		"click" : function() {
			$(this).siblings("input[type='file']").trigger("click");
		}
	});

	$(".input_file input[type='file']").on({
		"change" : function() {
			$(this).next().val($(this).val());
		}
	});

	/* 첨부 파일 삭제*/
	$(".btn_file_del").on({
		"click" : function() {
			$(this).siblings("input[type='file']").val("");
			$(this).siblings("input[type='text']").val("");
		}
	});

	/* 전시 기획전 상세 팝업 : 2016.05.17 */
	$(".ad_disabled").on("change", function() {
		if($(this).val() == "true"){
			$(this).siblings(".ad_select").find("option:eq(0)").prop("selected", true);
			$(this).siblings(".ad_select").prop("disabled", true);
			$(this).siblings(".ad_box").html("");
		}else{
			$(this).siblings(".ad_select").prop("disabled", false);
		}
	});

//	$(".ad_select").on({
//		"change" : function() {
//			if($("option:selected", this).index() == 0){
//				return false;
//			}
//
//			var now_str = $("option:selected", this).text();
//			var ad_box_inner = true;
//
//			$(this).next().find("span").each(function() {
//				if($(this).text().replace("삭제", "") == now_str){
//					ad_box_inner = false;
//					return false;
//				}
//			});
//
//			if(ad_box_inner){
//				var temp_name = "<span>" + $("option:selected", this).text() + "<button type='button' class='btn_txt_del' ng-click='ctrl.deleteChannel(" + $("option:selected", this).val() + ")' >삭제</button></span>"
//				$(this).siblings(".ad_box").append(temp_name);
//			}
//		}
//	});

	$(".ad_box").on("click", ".btn_txt_del", function() {
		$(this).parent().remove();
	});

	/* 공통 메뉴 컨트롤 : 2016.04.20 수정 */
	$(".btn_lnb_control").on({
		"click" : function() {
			if($(this).parents(".wrap").hasClass("lnb_hide")){
				$(this).addClass("btn_lnb_show").parents(".wrap").removeClass("lnb_hide");
			}else{
				$(this).removeClass("btn_lnb_show").parents(".wrap").addClass("lnb_hide");
			}
		}
	});

	/* 탭 클릭 순서 기억하기 */
	var tab_click_idx = new Array();
	var tab_idx;

	function fnTabClickArray(tab_idx, push_idx) {
		var temp_tab_idx = new Array();

		for(var i = 0; i < tab_click_idx.length; i++){
			if(tab_click_idx[i] != tab_idx){
				if(push_idx){
					temp_tab_idx.push(tab_click_idx[i]);
				}else{
					if(tab_click_idx[i] > tab_idx){
						temp_tab_idx.push(tab_click_idx[i]-1);
					}else{
						temp_tab_idx.push(tab_click_idx[i]);
					}
				}
				//console.log("for : " + temp_tab_idx);
			}
		}

		tab_click_idx = temp_tab_idx;

		if(tab_click_idx.length == 0){
			tab_click_idx = [0];
		}

		if(push_idx){
			tab_click_idx.push(tab_idx);
		}
		//console.log(tab_click_idx);
	}

	/* 탭 버튼 - 2016.07.08 수정 */
	$(".con_box").eq(0).addClass("con_on");

	$(".tab_type1").on("click", "button:not('.tab_close')", function() {
		tab_idx = $(".tab_type1 li").index($(this).parent());

		$(".con_box").removeClass("con_on").eq(tab_idx).addClass("con_on");
		$(".tab_type1 li").removeClass("on").eq(tab_idx).addClass("on");

		fnTabClickArray(tab_idx, true);
		fnTabPosition();
	});

	/* 탭 닫기 - 2016.07.08 수정 */
	$(".tab_type1").on("click", ".tab_close", function() {
		tab_idx = $(".tab_type1 li").index($(this).parent());

		fnTabClickArray(tab_idx, false);

		$(".tab_type1 li").eq(tab_idx).remove();
		$(".con_box").eq(tab_idx).remove();

		if($(this).parent().hasClass("on")){
			$(".con_box").eq(tab_click_idx[tab_click_idx.length - 1]).addClass("con_on");
			$(".tab_type1 li").eq(tab_click_idx[tab_click_idx.length - 1]).addClass("on");
		}
	});

	/* 팝업 탭 : 2016.05.17 */
	$(".box_type1 .tab_cont").hide().eq(0).show();
/*	$(".tab_type2 li:not('.disabled') button").on({
		"click" : function() {
			var tabIdx = $(this).parent().index();

			$(this).parent().addClass("on").siblings("li").removeClass("on");
			$(".box_type1 .tab_cont").hide().eq(tabIdx).show();
		}
	});
*/
	/* 탭 모두 닫기 : 2016.04.22 수정 */
	$(".btn_tab_control > button").on({
		"click" : function() {
			//tab_click_idx = [];
			$(".tab_type1 .tab_close").each(function() {
				$(this).trigger("click");
			});
		}
	});

	/* 탭 좌,우 이동 */
	$(".btn_tab_control .btn_next").on({
		"click" : function() {
			$(".tab_type1 li.on").next().find("button:first").trigger("click");
		}
	});
	$(".btn_tab_control .btn_prev").on({
		"click" : function() {
			$(".tab_type1 li.on").prev().find("button:first").trigger("click");
		}
	});

	/* 탭 추가 관련 기능들.. - 2016.07.08 추가 */
	$(".position_tab .btn_addTab").on({
		"click" : function(e) {
			var str_addTab = '\
						<li class="on">\
							<button type="button">010-5681-5682 / ' + Math.ceil(Math.random() * 10000) + '</button>\
							<button type="button" class="tab_close">탭 닫기</button>\
						</li>';

			$(this).next().find("ul").find("li").removeClass("on");
			$(this).next().find("ul").append(str_addTab);
			fnTabPosition();

			e.preventDefault();
		}
	});

	/* 탭 추가시 화면에 위치 잡기 - 2016.07.08 추가 */
	function fnTabPosition() {
		var tab_view_width = $(".position_tab .box").width() - parseInt($(".position_tab .tab_type1").css("marginLeft"));
		var tab_on_left = $(".position_tab .tab_type1 li.on").position().left + $(".position_tab .tab_type1 li.on").outerWidth() + 1;
		var go_move = 0;

		if( tab_on_left > tab_view_width ){
			$(".position_tab .tab_type1 li").each(function() {
				//console.log($(this).position().left, tab_on_left - $(".position_tab .box").width());

				if( $(this).position().left > tab_on_left - $(".position_tab .box").width() ){
					go_move = -$(this).position().left;

					return false;
				}
			});

			$(".position_tab .tab_type1").css({"marginLeft" : go_move});
		}else if( $(".position_tab .tab_type1 li.on").position().left < Math.abs(parseInt($(".position_tab .tab_type1").css("marginLeft"))) ){
			$(".position_tab .tab_type1 li").each(function() {
				if( $(this).position().left > tab_on_left - $(".position_tab .box").width() ){
					go_move = -$(this).position().left;

					return false;
				}
			});

			$(".position_tab .tab_type1").css({"marginLeft" : go_move});
		}
	}

	/* 공통 메뉴(좌측 메뉴) : 2016.05.09 수정 */
	$("nav>ul>li>a").on({
		"click" : function(){
			if($(this).parent().hasClass("on")){
				$(this).parent().removeClass("on");
				$(this).next("ul").hide();
			} else {
				$("nav>ul>li").removeClass("on");
				$("nav>ul>li>a").next("ul").hide();
				$(this).parent().addClass("on");
				$(this).next("ul").show();
			}
		}
	});

	$("nav ul ul a").on({
		"click" : function(){
			if(!$(this).parent().hasClass("on")){
				$(this).parent().addClass("on").siblings("li").removeClass("on");
			}
		}
	});

	/* 레이어 팝업 닫기 : 2016.06.02 추가 */
//	$(".layer_box .btn_layer_close").on({
//		"click" : function(){
//			$(this).parents(".layer_box").hide();
//		}
//	});
});