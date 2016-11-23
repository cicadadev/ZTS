
/* 작은레이어 팝업 위치 설정 : 2016.08.22 */
function fnLayerPosition(target_pop) {
	$(target_pop).show();
	$(target_pop).height( $(document).height() );

	var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
	$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
}

function intervalCalcHeight(param) {
	setTimeout(function(){	
		 clearInterval(param);
	}, 3000);
}

function setHeightInterval() {
	return setInterval(function() { 
			ccs.mainSwipe.calculateHeight(); 
			}, 500);
}

$(document).ready(function() {


	// 공통:셀렉트 박스 상단 라벨 표시
	function fnSelectChange(selectTarget) {
		selectTarget.each(function(){
			var select_name = $(this).children('option:selected').text();
			$(this).siblings('label').text(select_name);
		});
		selectTarget.change(function(){
			var select_name = $(this).children('option:selected').text();
			$(this).siblings('label').text(select_name);
		});
	}
	fnSelectChange( $('.selectbox select') );
	fnSelectChange( $('.sel_box select') );


	/* 셀렉트박스 : 2016.07.26 추가 */
	function fnSelectStyle(){
		$(".select_box1 select").each(function() {
			$(this).siblings('label').text( $("option:selected", this).text() );
		});

		$(".wrap .content").off("change").on("change", ".select_box1 select", function() {
			$(this).siblings('label').text( $("option:selected", this).text() );
		});
	}
	fnSelectStyle();
	

	// 탭 - 로그인, 상품 상세, 장바구니 등..
	$(".tab_box").find(".tab li").off("click").on("click", function(e){
		//e.preventDefault();
		var idx = $(this).index();

		$(this).addClass("on").siblings("li").removeClass("on");
		$(this).closest(".tab_box").find("> .tabcont").eq(idx).addClass("tabcontShow").siblings(".tabcont").removeClass("tabcontShow");
	});
	//$(".tab_box").find(".tab li").eq(0).trigger( "click" );

	//마이페이지 - 1:1문의
	$(".qArea").find(">a").off("click").on("click", function(){
		if($(this).hasClass("on")){
			$(this).removeClass("on");
			$(this).closest(".tr_box").next(".aArea").hide();
		} else {
			$(".qArea").find(">a").removeClass("on");
			$(".aArea").hide();
			$(this).addClass("on");
			$(this).closest(".tr_box").next(".aArea").show();
		}
	});

	// 파일첨부
	$(".file_style1 input[type='file']").off("change").on("change", function(){
		var fileName = $(this).val();
		$(this).prev().val(fileName);
	});
	$(".file_style1 .btn_file").off("click").on({
		"click" : function(e) {
			$(this).prev().trigger("click");
			//e.preventDefault();
		}
	});


	/* 파일 첨부 : 2016.07.26 추가 */
	$(".inputFile_style1 input[type='file'], .inputFile_style2 input[type='file']").off("change").on("change", function(){
		var fileName = $(this).val();
		$(this).prev().val(fileName);
	});
	$(".inputFile_style1 .btn_file, .inputFile_style2 .btn_file").off("click").on({
		"click" : function(e) {
			$(this).prev().find("input[type='file']").trigger("click");
			//e.preventDefault();
		}
	});
	

	/* 텍스트 박스 [placeholder] : 2016.07.26 추가 */
	function fnPlaceholder(){
		$(".inputTxt_place1 input[type='text']").each(function() {
			if($(this).val().length > 0){
				$(this).parent().prev().hide();
			}else{
				$(this).parent().prev().show();
			}
		});

		$(".inputTxt_place1 input[type='text']").off("focus blur").on({
			"focus" : function() {
				$(this).parent().prev().hide();
				$(this).closest(".inputTxt_place1").addClass("place_hover");
			},
			"blur" : function() {
				$(this).closest(".inputTxt_place1").removeClass("place_hover");
				if($(this).val().length > 0){
					$(this).parent().prev().hide();
				}else{
					$(this).parent().prev().show();
				}
			}
		});
		$(".inputTxt_place1 input[type='password']").each(function() {
			if($(this).val().length > 0){
				$(this).parent().prev().hide();
			}else{
				$(this).parent().prev().show();
			}
		});

		$(".inputTxt_place1 input[type='password']").off("focus blur").on({
			"focus" : function() {
				$(this).parent().prev().hide();
				$(this).closest(".inputTxt_place1").addClass("place_hover");
			},
			"blur" : function() {
				$(this).closest(".inputTxt_place1").removeClass("place_hover");
				if($(this).val().length > 0){
					$(this).parent().prev().hide();
				}else{
					$(this).parent().prev().show();
				}
			}
		});
	}
	fnPlaceholder();



	/* 탭 공통 : 2016.07.27 추가 */
	$(".tabBox a, .tabBox1 a, .tabBox2 a").off("click").on("click", function(e){
		/* 생생테스터 비활성화 : 2016.10.21 */
		if ($(this).attr("id") == "a_expTester") {
			return;
		};
		
		var idx = $(this).parent().index();
		var parent_ul;

		if( $(this).closest("ul").hasClass("tabBox") ){
			parent_ul = $(this).closest(".tabBox");
		}else if( $(this).closest("ul").hasClass("tabBox1") ){
			parent_ul = $(this).closest(".tabBox1");
		}else{
			parent_ul = $(this).closest(".tabBox2");
		}

		$(this).parent().addClass("on").siblings("li").removeClass("on");
		$(parent_ul).siblings(".tab_con").eq(idx).addClass("tab_conOn").siblings(".tab_con").removeClass("tab_conOn");

		if($(this).hasClass("btn_brandLeft")) {
			swiperCon('brandMenuSwiper_sortingWord_kr', 'auto'); //left 메뉴 - 브랜드명 sorting
		}
		//e.preventDefault();
	});
	
	$(".ly_box .btn_close").off("click").on({
		"click" : function(e) {
			if( $(this).closest(".ly_box ").hasClass("gift_box") ){
				$(this).closest(".ly_box ").prev().removeClass("on");
			}

			$(this).closest(".ly_box ").hide();

			//e.preventDefault();
		}
	});


	/* Textarea : 2016.08.10 추가 */
	function fnTextarea(){
		$(".txtarea_box textarea").each(function() {
			if($(this).val().length > 0){
				$(this).next().hide();
			}else{
				$(this).next().show();
			}
		});

		$(".txtarea_box textarea").off("focus blur").on({
			"focus" : function() {
				$(this).closest(".txtarea_box").addClass("txtarea_hover");
				$(this).next().hide();
			},
			"blur" : function() {
				$(this).closest(".txtarea_box").removeClass("txtarea_hover");
				if($(this).val().length > 0){
					$(this).next().hide();
				}else{
					$(this).next().show();
				}
			}
		});
	}
	fnTextarea();

	$(".selectGiftList input").on("change", function(e){
		if( $(this).prop("checked") ){
			$(this).parents('li').addClass("on");
		}else{
			$(this).parents('li').removeClass("on");
		}
	});

	/* 정렬탭 클릭 : 2016.09.02 */
	$(".sortBox a, .sortBox1 a").off("click").on("click", function(e) {
		$(this).parent().addClass("active").siblings("li").removeClass("active");

		//e.preventDefault();
	});

	
	/* 관심정보 레이어 팝업 : 2016.09.27 */
	function interestInfoLayerEffect() {
		$(".radio_style1").on("click", "em", function() {
			$(this).parent().parent().find(".radio_style1 em").removeClass("selected");
			$(this).addClass("selected");
			
			var babyYn = "";
			if ($(this).find("input").val() == "N") {
				babyYn = "N";
			} else {
				for (var i=0; i<$("#babyYnArea").find("em").length; i++) {
					if ($("#babyYnArea").find("em:eq(" + i + ")").is(".selected")) {
						babyYn = $("#babyYnArea").find("em:eq(" + i + ") input").val();
					}
				}
			}
			
			if (babyYn == "N") {
				$("#babyBirthdayArea").find(".select_box1").addClass("sel_disabled");
				$("#babyBirthdayArea").find(".select_box1 select").attr("disabled", true);
				
				$("#babyGenderArea").find("em").removeClass("selected");
			} else if (babyYn == "Y") {
				$("#babyBirthdayArea").find(".select_box1").removeClass("sel_disabled");
				$("#babyBirthdayArea").find(".select_box1 select").attr("disabled", false);
				
				$("#babyGenderArea").find("em:last").removeClass("selected");
			} else if (babyYn == "READY") {
				$("#babyBirthdayArea").find(".select_box1").removeClass("sel_disabled");
				$("#babyBirthdayArea").find(".select_box1 select").attr("disabled", false);
			}
		});

		$("[name=multiChk]").on("change", "em", function() {
			if ($(this).hasClass("selected")) {
				$(this).removeClass("selected");
			} else {
				$(this).addClass("selected");
			}	
		});
		
		$("[name=interestCategory]").on("change", "[name=category1_em]", function() {
			if ($(this).hasClass("selected")) {
				$(this).removeClass("selected");
			} else {
				$("[name=category1_em]").removeClass("selected");
				$(this).addClass("selected");
			}	
		});
		
		$("[name=category2_em]").on("change", function() {
			if ($(this).hasClass("selected")) {
				$(this).removeClass("selected");
			} else {
				$(this).addClass("selected");
			}
		});
		
		$(".step04 .chk_style1").on("change", "em", function() {
			if ($(this).hasClass("selected")) {
				$(this).removeClass("selected");
			} else {
				$(this).addClass("selected");
			}	
		});

		$(".select_box1 select").on("change", function() {
			$(this).siblings("label").text($(this).find("option:selected").text());
		});

	}
	interestInfoLayerEffect();
	

	
	// 로그인 input
	$(".inp_placeholder").find("input[type='text'], input[type='tel'], input[type='number'], input[type='password']").each(function(){
		$(this).focus(function(){
			$(this).addClass("focus");
			$(this).next("label").hide();
		});

		$(this).blur(function(){
			$(this).removeClass("focus");
			if($(this).val() == ""){
				$(this).next("label").show();
			}
		});
	});
	

	// 로그인 키보드 이미지 열고 닫기
	$(".btn_keyboard").off("click").on("click", function(e){
		//e.preventDefault();
		$(this).toggleClass("open");
		$(this).next(".keyboard").stop().slideToggle();
	});
	
	

	// 셀렉트박스 옵션 변경
	$(".select_style select").off("change").on({
		"change" : function() {
			$(this).closest(".select_style").find("label").text( $("option:selected", this).text() );
		}
	});
	
	/* 앱 가이드 닫기 */
	$(".appGuide_wrap .btn_close").off("click").on({
		"click" : function() {
			$(this).closest(".appGuide_wrap").fadeOut('fast');
		}
	});
	
	/* 스크롤 끝 체크 : 우편번호 찾기에서 사용*/
	$(".lucas_scroll").scroll(function(e) {
		var max_scroll = $(".conHeight", this).innerHeight();
		var now_scroll = $(this).innerHeight() + $(this).scrollTop();

		// if( now_scroll == max_scroll){
		// 	alert("스크롤 끝입니다.");
		// }
	});
});

