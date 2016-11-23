
	var detailT = 0;
	
	// 상품상세 탭 위치 계산
	function fnDetailTabPosition() {
		if( $(window).scrollTop() > detailT ){
			$("#goodsCont").addClass("fixed");
		} else {
			$("#goodsCont").removeClass("fixed");
		}
	}

	

$(document).ready(function(){
	

	
	// 상품상세 탭
	$("#goodsCont").find(".detail_tab>li").off("click").on("click", function(e){
		/*
		$("html, body").animate({
			scrollTop : $(".detail_cont").eq(idx).offset().top - 50
		});
		*/
		var idx = $(this).index();

		$(".detail_tab>li").eq(idx).addClass("on").siblings("li").removeClass("on");
		$(".pc .goods_cont_box .detail_cont").hide().eq(idx).show();

//		상품 상세 탭으로 이동
		var position=$('.detail_tab').offset(); // 위치값
		$('html,body').animate({scrollTop:position.top},100); // 이동
		
//		console.log('test', position);
		e.preventDefault();
	});
	
	// 상세 스크롤
	if( $("#goodsCont").length > 0 ){
		detailT = $("#goodsCont").offset().top;
		fnDetailTabPosition();
	}
	
	/* 상품상세 이미지 이벤트 : 2016.08.09 */
	$(".goods_detail_box .dots span").on({
		"mouseover" : function() {
			var prod_img_num = $(".goods_detail_box .dots span").index( $(this) );

			$(".goods_detail_box .dots span").removeClass("on").eq(prod_img_num).addClass("on");
			$(".goods_img li").removeClass("on").eq(prod_img_num).addClass("on");
			$(".thum_img_list li").removeClass("on").eq(prod_img_num).addClass("on");
		}
	});
	$(".goods_detail_box .btn_large").on('click', function (){
		$('.goods_img_detail').show();
	});
	function goodsAction() {
		var goodsWrap = $('.goods_img_detail'),
			 goodsBtn = goodsWrap.find('.thum_img_list a'),
			 goodsCont = $('.goods_img_detail .thum_img');

		goodsBtn.on('click', function(e) {
			//e.preventDefault();

			var _this = $(this).parent('li'),
				index = _this.index();

			_this.addClass('on').siblings().removeClass('on');
		});
	}
	goodsAction();
	
	
	/* 상품상세 옵션 삭제 버튼 : 2016.08.10 */
	$(".pc .selectOption .btnDelete").off("click").on("click", function(){
		if($(".selectOption li").length == 1){
			$(".selectOption").hide();
			$(this).parent("li").remove();
		} else {
			$(this).parent("li").remove();
		}
	});
	
	
	// 상품상세 - 선물포장 안내
	$(".pc .btn_giftInfo").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_gift");
	});
	
	// 상품상세 - 배송비안내 팝업
	$(".pc .btn_freeDelivery").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_delivery");
	});

	// 상품상세 - 매일포인트 팝업
	$(".pc .btn_mpoint").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_mpoint");
	});

	// 상품상세 - 정기배송 신청 안내 팝업
	$(".pc .btn_regular").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_regular");
	});

	// 상품상세 - 매장픽업 안내
	$(".pc .btn_pickup").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_pickup");
	});
	
	// 상품상세 - 카드혜택
	$(".pc .btn_cardbnf").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_cardbnf");
	});

	// 상품상세 - 상품평 작성 유의사항
	$(".pc .btn_review_guide").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_review");
	});

	// 상품상세 -가격적용 안내
	$(".pc .btn_priceGuide").on("click", function(e){
		ccs.layer.copyLayerToContentChild("sLayer_priceGuide");
		//e.preventDefault();
	});
	
	
	// 상품평 - 더보기 이동
	$("#productReviewMore").on("click", function(e){
		$("#productReviewTab").trigger("click");
		var position=$('#productReviewTab').offset(); // 위치값
		$('html,body').animate({scrollTop:position.top},100); // 이동
	});
	
	/* 상품상세 - 하단 옵션 박스(정기배송 체크) : 2016.08.18  */
	$(".pc .bottom_option .inp_repeat").on({
		"change" : function(e) {
			if( $(this).is(":checked") ){
				$(this).closest(".chk_box").next().show();
			}else{
				$(this).closest(".chk_box").next().hide();
			}
		}
	});
	
});	


$(window).scroll(function() {

	fnDetailTabPosition();

});

$(window).load(function() {
	/* 상품상세 - 하단 옵션 박스 : 2016.08.18 */
	if( $(".pc .bottom_option").length ){
		$(".pc .bottom_option .btn_opChoice").off("click").on({
			"click" : function(e) {
				$(this).parent().toggleClass("bot_opOpen");

				if( $(this).parent().hasClass("bot_opOpen") ){
					$(".pc .bottom_option .box").hide();
					$(".pc .bottom_option").css({"bottom" : -$(".pc .bottom_option").outerHeight()});
				}else{
					$(".pc .bottom_option .box").show();
					$(".pc .bottom_option").css({"bottom" : 0});
				}

				////e.preventDefault();
			}
		});

		setTimeout(function() {
			$(".pc .bottom_option").css({"bottom" : -$(".pc .bottom_option").outerHeight()});
		}, 500);
	}
});