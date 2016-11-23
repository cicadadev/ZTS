
//16.10.25 전문관 탭 고정[모바일]
//function fnTabPos() {
//	var tabFixW_top = $(".mobile .tabFixW").offset().top;
//	
//	if($(window).scrollTop() > tabFixW_top){
//		$(".mobile .tabFix").addClass("fixedTab");
//	} else {
//		$(".mobile .tabFix").removeClass("fixedTab");
//	}
//}

$(document).ready(function(){
	
	// 전시 - 소카테고리 닫기
	$(".catePaginate").find(".rw_btnCateClose").off("click").on("click", function(){
		$(".mo_navi").find(".rw_btnMCate").removeClass("active");
		$(".rw_displayListBox").find(".categoryBox").hide();
	});

	// 전시 - 상새검색 옵션 열기
	$(".mobile .mo_sortBox").find(".btnDetail").off("click").on("click", function(e){
		//e.preventDefault();
		$(".mobile .optionBox").show();
		$(".mobile .optionBox").find(".optionItem").show();
		$(".mobile .optionBox").find(".optionItem.brand").hide();
	});

	// 전시 - 브랜드 옵션 열기
	$(".mobile .mo_sortBox").find(".btnBrand").off("click").on("click", function(e){
		alert('3333333333333');
		//e.preventDefault();
		$(".mobile .optionBox").show();
		$(".mobile .optionBox").find(".optionItem").hide();
		$(".mobile .optionBox").find(".optionItem.brand").show();
	});

	// 전시 옵션 닫기
	$(".mobile .optionBox").find(".btn_close").off("click").on("click", function(e){
		//e.preventDefault();
		$(this).closest(".optionBox").hide();
	});

	// 전시 - 옵션 소재/혜택 선택
	$(".optionBox .btnList").find("button").off("click").on("click", function(){
		$(this).toggleClass("active");
	});

	// 전시 - 옵션 컬러 선택 : 주석 풀면 안됨. 페이지에 ready안에서 각각 써야 함. emily 2016.11.09
	/*$(".optionBox .colorList").find("a").off("click").on("click", function(e){
		//e.preventDefault();
		$(this).toggleClass("active");
	});*/

	// 전시 - 찜버튼
	$(".rw_btnWish").off("click").on("click", function(e){
		//e.preventDefault();
		$(this).toggleClass("active");
	});


	// 전시 - mo_sortBox 가로 사이즈
	if($(".mo_sortBox li").length == 3){
		$(".mo_sortBox li.listType").siblings('li').width("39%");
	}

	// 전시 - mo_sortBox 가로 사이즈
	if($(".mo_sortBox li").length == 2){
		$(".mo_sortBox li.listType").siblings('li').width("82%");
	}

	// 상단 비주얼 더보기 : 메인의 홈과 기획전의 레이어를 노출하기 위해 변경
	$(".mobile .mb_page").find("button").off("click").on("click", function(){
		
		//console.log($(this).data("type"));
		if($(this).data("type") != undefined){
			ccs.layer.copyLayerToContentChild($(this).data("type"));
		}else{
			ccs.layer.copyLayerToContentChild("ly_visual");
		}
	})
	
	
		// 전시 - 베스트
	if($(".bestBox .categoryBox").length>0){
		if(!$(this).hasClass("depth1")){
			$(this).find("a").off("click").on("click", function(e){
				//e.preventDefault();
				$(this).parent().siblings('li').removeClass("active");
				$(this).parent().addClass("active");
			});
		}
	}
	
	
	// [전문관] 기프트관
	if($(".babyInfo").length>0){
		$(".btnBaby").off("click").on("click", function(){
			$(".babyInfo").toggleClass("open");
		});

		$(".babyCheck").find("a").off("click").on("click", function(e){
			//e.preventDefault();
			$(this).toggleClass("active");
		});
	}
	
	// [전문관] - 출산준비관 토글
	$(".mobile .special .chkList").find("dt").off("click").on("click", function(){
		$(this).parent().toggleClass("open").siblings("dl").removeClass("open");
	});
	
	
	$(".mobile .storeList").find(".bookmark").off("click").on("click", function(){
		$(this).closest('li').find(".bookmark").toggleClass("checked");
	});
	
	
	
	// 16.10.23 스타일
	$(".mobile .btn_styleHow").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .ly_styleHow") );
		}
	});
	

	// 전시 - 옵션 소재/혜택 선택
	$(".optionBox .btnList").find("button").off("click").on("click", function(){
		$(this).toggleClass("active");
	});

	// 전시 - 옵션 컬러 선택 : 주석 풀면 안됨. 페이지에 ready안에서 각각 써야 함. emily 2016.11.09
/*	$(".optionBox .colorList").find("a").off("click").on("click", function(e){
		////e.preventDefault();
		$(this).toggleClass("active");
	});*/

	// 전시 - 찜버튼
	$(".rw_btnWish").off("click").on("click", function(e){
		////e.preventDefault();
		$(this).toggleClass("active");
	});

	// 전시 - 카테고리 메인 브랜드 더보기
	$(".btnMoreBrand").find("button").off("click").on("click", function(e){
		////e.preventDefault();
		$(this).parent().prev(".brandList").toggleClass("open");
	});
	
	// 전시 - 베스트
	if($(".bestBox .categoryBox").length>0){
		$(this).find("a").off("click").on("click", function(e){
			////e.preventDefault();
			$(this).parent().siblings('li').removeClass("active");
			$(this).parent().addClass("active");
		});
	}

	
});	

//$(window).scroll(function() {
//	//16.10.25 전문관 탭 고정
//	if( $(".mobile .tabFixW").length>0 ){
//		if($(".mobile .tabFixW").parent().hasClass("tab_con")){
//			if($(".mobile .tabFixW").parent().css("display") == "block"){
//				fnTabPos();
//			}
//			
//		} else {
//			fnTabPos();
//		}
//	}
//	
//});

