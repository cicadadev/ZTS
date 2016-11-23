
	

$(document).ready(function(){
	
	// 상품상세 상품평 쓰기
	$(".mobile .btn_review_write").off("click").on({
		"click" : function() {
			fnLayerOpen( $(".layer_review_write") );
		}
	});

	// 상품상세 QA 쓰기
	$(".mobile .btn_qa_write").off("click").on({
		"click" : function(e) {
			fnLayerOpen( $(".layer_qa") );
		}
	});

	// 상품상세 미리계산하기
	$(".mobile .price .btn_calc").off("click").on({
		"click" : function() {
			fnLayerOpen( $(".layer_prediction") );
		}
	});

	// 상품상세 지도보기
	if( $(".mobile .layer_type1 .btn_map").length > 0 ){
		$(".mobile .layer_type1 .btn_map").off("click").on({
			"click" : function() {
				if( $(this).text() == "지도보기" ){
					$(this).text("지도닫기").addClass("map_on").parent().nextAll().show();
				}else if( $(this).text() == "지도닫기" ){
					$(this).text("지도보기").removeClass("map_on").parent().nextAll().hide();
				}

			}
		});
	}

	// 상품상세 상품평 - 파일첨부
	$(".mobile input[type='file']").off("change").on({
		"change" : function() {
			$(this).next().val( $(this).val() );
		}
	});
	
	// 16.11.11 : 배송비 상세 보기
	$(".mobile .btn_delivery").off("click").on({
		"click" : function(e) {
			if( $(this).closest(".delivery_info").hasClass("on") ){
				$(this).closest(".delivery_info").removeClass("on");
			}else{
				$(this).closest(".delivery_info").addClass("on");
			}
		}
	});

	// 상품상세 찜버튼
	$(".mobile .productDetail .btn_jjim").off("click").on({
		"click" : function() {
			$(this).toggleClass("active");
		}
	});


	// 상품상세 상품찜
	$(".mobile .prod_info .icon_box .btn_jjim").off("click").on({
		"click" : function() {
			$(this).toggleClass("on_jjim");
		}
	});
	

	// 상품상세 수량 옵션 제거
	$(".mobile .buy_fixed .ea_box .btn_del").off("click").on({
		"click" : function() {
			$(this).closest(".ea_box").remove();
		}
	});
	
	
	/* 카드 무이자 할부 레이어팝업 : 2016.08.22 */
	$(".mobile .infomation .btn_installment").off("click").on({
		"click" : function() {
			//fnLayerOpen( $(".mobile .layer_card") );
			ccs.layer.copyLayerToContentChild("layer_card");
		}
	});
	/* 상품상세 - 상품평쓰기 팝업 : 2016.08.24 */
	$(".mobile .productDetail .btnReview").off("click").on({
		"click" : function(e) {
//			모바일 팝업 제거 fnLayerOpen( $(".mobile .layer_reWrite") );

			//e.preventDefault();
		}
	});
	/* 상품상세 - Q&A쓰기 팝업 : 2016.08.24 */
	$(".mobile .productDetail .btn_qaWrite").off("click").on({
		"click" : function(e) {
			fnLayerOpen( $(".mobile .layer_qa") );

			//e.preventDefault();
		}
	});
	
	/* 포토 상품평 레이어팝업 : 2016.08.22 */
//	if( $(".mobile .layer_photoReview ul").length ){
//		swipe_ani_type2( $(".mobile .layer_photoReview ul"), false );
//	}
	
	$(".mobile .comment .btn_photoReview").off("click").on({
		"click" : function() {
			fnLayerOpen( $(".mobile .layer_photoReview") );
			swipeRePosition( $(".mobile .layer_photoReview ul") );
		}
	});
	

	/* 상품상세 - 구매/배송정보 내용 보기 :2016.08.22 */
	$(".mobile .prod_delivery .sub_title").off("click").on({
		"click" : function() {
			$(this).parent().toggleClass("zone_open");
		}
	});

	/* 상품상세 - 상품평 보기 : 2016.08.22 */
	$(".mobile .user_option .title").off("click").on({
		"click" : function() {
			$(this).parent().next().toggle();
		}
	});
	
	/* 상품상세 - 전체, 포토 평가 탭 : 2016.08.22 */
	$(".mobile .prod_cmd_tab a").off("click").on({
		"click" : function(e) {
			var prod_cmd_tab_num = $(".mobile .prod_cmd_tab li").index( $(this).parent() );

			$(".mobile .prod_cmd_tab li").removeClass("on").eq(prod_cmd_tab_num).addClass("on");
			$(".mobile .prod_review .comment").removeClass("cmd_on")
				.eq(prod_cmd_tab_num).addClass("cmd_on");
		}
	});



	/* 상품상세 - sns 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .btn_snsInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_sns") );
		}
	});
	/* 상품상세 - 정기배송안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .infomation .btn_repeatInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_repeat") );
		}
	});
	/* 상품상세 - 정기배송가 안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .infomation .btn_regularDeliveryInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .lp_regularDeliveryInfo") );
		}
	});
	/* 상품상세 - 매장픽업가 안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .infomation .btn_pickupPriceInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .lp_pickupPriceInfo") );
		}
	});
	/* 상품상세 - 매일포인트 안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .infomation .btn_maeilPoint").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_maeil") );
		}
	});
	/* 상품상세 - 선물포장 안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .infomation .btn_giftInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_gift") );
		}
	});

	// 16.10.16 : 상품상세 - 가격적용 안내(작은 레이어)
	$(".mobile .btn_priceGuide").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_priceGuide") );
		}
	});

	/* 상품상세 - 상품평쓰기 안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .productDetail .btn_reviewInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_review") );
		}
	});
	
	/* 상품상세 - 구매 옵션 버튼 : 2016.08.23 */
	if($(".mobile .buy_fixed").length ){
		$(".mobile .buy_fixed").css({"bottom" : "0"});


		/* 찜버튼 */
		$(".mobile .buy_fixed .btn_jjim").off("click").on({
			"click" : function(e) {
				$(this).toggleClass("active");

				//e.preventDefault();
			}
		});

		/* 옵션 삭제 버튼 */
		$(".mobile .buy_fixed .ea_box .btn_del").off("click").on({
			"click" : function(e) {
				$(this).closest("li").remove();

				//e.preventDefault();
			}
		});
	}
	
	/* 상품상세 상품정보 탭 위치 : 2016.08.22 */
	if( $(".prod_info_tab").length ){
		prod_info_tab_top = Math.floor($(".prod_info_tab").offset().top);
		
		function fnProdTabPosition() {
			
			if( $(window).scrollTop() > prod_info_tab_top - 90 && !$(".mobile .all_menu").hasClass("active_all") ){
				$(".content").addClass("content_fixed");
			}else{
				$(".content").removeClass("content_fixed");
				$(".prod_info_tab").removeAttr("style");
			}
		}
		//fnProdTabPosition();
	}
	
	
});	

