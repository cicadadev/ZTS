$(document).ready(function(){

	/* 베스트(실시간,주간 탭) : 2016.09.05 */
	$(".best_type > li > a").off("click").on("click", function() {
		$(this).parent().addClass("on").siblings("li").removeClass("on");
	});
	
	
	/* 베스트 : 2016.09.30 */
	function bestShopEffect() {
//		$(".best_tab .best_type").on("click", "li", function() {
//			var tab = $(this).find("ol").attr("id");
//			
//			$(".best_tab .best_type").find("li").removeClass("on");
//			$(this).addClass("on");
//
//			if (tab == "categoryArea") {
//				$("#categoryArea").find("li").removeClass("on");
//				$("#categoryArea .item1").addClass("on");
//			} else if (tab == "categoryArea2") {
//				$("#categoryArea2").find("li").removeClass("on");
//				$("#categoryArea2 .item1").addClass("on");
//			}
//		});
		
		$(".main_best #categoryArea").on("click", "li", function() {
			$("#categoryArea").find("li").removeClass("on");
			$(this).addClass("on");
		});
		
		$(".main_best #categoryArea2").on("click", "li", function() {
			$("#categoryArea2").find("li").removeClass("on");
			$(this).addClass("on");
		});
		
		$(".main_best .real").on("click", function() {
			$("#infoTxt").text("* 고객님들이 많이 클릭한 상품순입니다.");
			
			$(".main_best #categoryArea2").hide();
			$(".main_best #categoryArea").show();
		});
		
		$(".main_best .week").on("click", function() {
			$("#infoTxt").text("* 고객님들이 많이 주문한 상품순입니다.");
			
			$(".main_best #categoryArea").hide();
			$(".main_best #categoryArea2").show();
		});
	};
	bestShopEffect();
	
	// 16.10.23 PICKUP 이용방법
	$(".mobile .btn_pickupHow").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .ly_pickupHow") );
		}
	});


	/* 작은레이어 팝업 위치 설정 : 2016.08.22 */
	function fnLayerPosition(target_pop) {
		$(target_pop).show();
		$(target_pop).height( $(document).height() );

		var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
		$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}

	/* 메인 이런 상품 */
	if( $(".mobile .howAboutBox .product_type1").length ){
		var gnb_left = 0;

		$(".mobile .howAboutBox .product_type1 li").each(function(index) {
			$(this).css({"left" : gnb_left + "px"});
			gnb_left = gnb_left + $(this).innerWidth();
		});
	};

	/* 메인 멤버십 상품 */
	if( $(".mobile .memberBox .product_type1").length ){
		var gnb_left = 0;

		$(".mobile .memberBox .product_type1 li").each(function(index) {
			$(this).css({"left" : gnb_left + "px"});
			gnb_left = gnb_left + $(this).innerWidth();
		});
	};

	/* 메인 계절 상품 */
	if( $(".mobile .seasonBox .product_type1").length ){
		var gnb_left = 0;

		$(".mobile .seasonBox .product_type1 li").each(function(index) {
			$(this).css({"left" : gnb_left + "px"});
			gnb_left = gnb_left + $(this).innerWidth();
		});
	};

	/* 메인 - 브랜드 스와이프 : 2016.09.19 */
	if( $(".mobile .main .brandBox ul").length ){
		var gnb_left = 0;

		$(".mobile .main .brandBox li").each(function(index) {
			$(this).css({"left" : gnb_left + "px"});
			gnb_left = gnb_left + $(this).innerWidth();
		});

		swipe_ani_type1( $(".mobile .main .brandBox ul") );
	}
});	


/* 작은레이어 팝업 위치 설정 : 2016.08.22 */
function fnLayerPosition(target_pop) {
	$(target_pop).show();
	$(target_pop).height( $(document).height() );

	var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
	$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
}