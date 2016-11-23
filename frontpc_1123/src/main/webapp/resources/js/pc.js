// ############################## 공통 함수 ############################## //




// ############################## 일반 함수 ############################## //




// ############################## Swiper 함수 ############################## //


// swiperCon (스와이프 공통 함수 )
// 
// *************************
// obj : 태그이름,
// cnt : 리스트 개수
// itv : 간격
// autoMode : 수치(2000) or false
// loopMode : true or false
// slideGroup : swiper시 함께 움직일 오브젝트 개수
// paginationType : 하단 agination 타입
//
//*************************
function swiperCon(obj, speed, cnt, itv, autoMode, loopMode, slideGroup, paginationType) {
	var objTag = "."+obj;
	var obj_pagination = "."+obj+" .swiper-pagination";

	obj = new Swiper(objTag, {
		speed: speed,                                         // 속도
		pagination: obj_pagination,							  // Swiper 페이지
        slidesPerView: cnt,                                   // 뿌려질 개수 설정
        nextButton: "."+obj+" .swiper-button-next",           // 다음버튼
	    prevButton: "."+obj+" .swiper-button-prev",           // 이전버튼
	    spaceBetween: itv,                                    // 간격
	    loop: loopMode,                                       //무한 여부
	    autoplay: autoMode,                                   // 자동 여부
	    slidesPerGroup: slideGroup,                           // 한번에 이동하는 개수
	    paginationType: paginationType,						  // wiper 페이지 타입				
	    autoplayDisableOnInteraction: false,                  // false로 설정하면 Swiper 후에 자동 재생이 중지되지 않음.
	    paginationClickable: true                             // true 인 경우 페이지 번호 버튼을 클릭하면 해당 슬라이드로 전환.
    });
}

// pc 브랜드관 룩북 슬라이더
$(document).ready(function() {

	/* ###### Swiper ###### */
	// swiper 예시 swiperCon('태그클래스', 보여질개수, 간격, 자동여부, 루프여부, 슬라이더그룹, pagination 형태)  - 자동여부는  false:멈춤, 2000등의 수치를 입력하면 자동스와이프, 구치는 스와이프이동 시간 간격
//	swiperCon('mainSwiper_brand', 400, 6, 4,'',true); //브랜드 배너
//	swiperCon('mainSwiper_recoProducts_1', 800, 4, 12, false, true, 4);                      //추천 상품
//	swiperCon('mainSwiper_recoProducts_2', 800, 5, 12, false, true, 5);                      //MD 특가 상품
//	swiperCon('mainSwiper_popularItem.marketPicUp', 800, 2, 12, false, true, 2, 'fraction'); // 매장 픽업
//	swiperCon('mainSwiper_popularItem.giftShop', 800, 2, 12, false, true, 2, 'fraction');
//	swiperCon('mainSwiper_bigBanner_1', 400, 1, 0, 2000, true); 
//	swiperCon('mainSwiper_bigBanner_2', 400, 1, 0, 2000, true);
//	swiperCon('comSwiper_quickBanner', 400, 1, 0, 2000, true);
//
//	//브랜드
//	swiperCon('brandSwiper_banner_1', 400, 1, 0, false, true);
//	swiperCon('brandSwiper_realProdList_1', 400, 4, 12, false, true, 4);
//
//	//멤버쉽
//	swiperCon('premiumSwiper_prodList_1', 400, 4, 12, false, true, 4);
//
//	// 전시
//	//swiperCon('displaySwiper_prodList_1', 400, 4, 12, false, true, 4);
//	//swiperCon('displayCategorySwiper_banner_1', 400, 1, 0, false, true); //중 카테고리 - 상단 큰 배너
//
//
//	// 전문관
//	swiperCon('premiumMember_afterBanner_1', 400, 1, 0, false, true); //프리미엄 멤버쉽 베너
//	swiperCon('storePicupSwiper_categoryList', 400, 6, 0, false, false); //매장픽업관 카테고리
//	swiperCon('employeeSwiper_categoryList', 400, 6, 0, false, false); //임직원관 카테고리
//
//	// 장바구니
//	swiperCon('cartSwiper_pordList', 400, 4, 12, false, true, 4);
//
//	//상품상세
//	swiperCon('prodDetailSwiper_banner_1', 400, 1, 0, false, true); //프리미엄 멤버쉽 베너
//	swiperCon('prodDetailSwiper_bestProdList_1', 400, 5, 12, false, true, 5); //베스트 상품
//
//	// 마이페이지
//	swiperCon('mypageSwiper_cartProd_1', 800, 2, 12, false, true, 2, 'fraction');

	/* ######################## 전시 ########################*/

	/* 쇼킹제로 - 비주얼 영역  */
	$(".pc .main_zero .zero_visual .thumb a").off("mouseenter").on("mouseenter", function(e) { // 16.11.07 마우스오버로 변경(요청사항)
		$(this).parent().addClass("on").siblings("li").removeClass("on");
		$(".pc .zero_visual .view > li").eq( $(this).parent().index() ).addClass("on").siblings("li").removeClass("on");
	});

	/* 베스트(실시간,주간 탭) */
	$(".best_type > li > a").off("click").on("click", function() {
		$(this).parent().addClass("on").siblings("li").removeClass("on");
	});

	/* 기획전 탭 슬라이드 */
	$(".pc .promotion .list_package_ctrlwap .sortBox2 > li > a").off().on({
		"click" : function(e) {
			e.preventDefault();
			$(this).parent().addClass("on").siblings("li").removeClass("on");
			swiperCon('promotionSwiper_brandCategory', 400, 6, 0, false, false); //임직원관 카테고리
		}
	});

	// pc 브랜드관 룩북 슬라이더
	if( $('.pc #imageGallery').length ){
		$('.pc #imageGallery').lightSlider({
			gallery:true,
			item:1,
			thumbItem:11,
			slideMargin: 0,
			speed:500,
			auto:false,
			loop:true,
			thumbMargin:7,
			onSliderLoad: function() {
				//$('.pc #imageGallery').removeClass('cS-hidden');
				if( $('.pc .lSPager').width() < 1000 ){
					$('.pc .lSPager').css("marginLeft", ( 1000 - $('.pc .lSPager').width() )  / 2 );
				}
			}  
		});
	}


	/* ######################## 마이페이지  ########################*/
	

	/* ########################  ########################*/

	/* ########################  ########################*/

	/* ########################  ########################*/

	/* ########################  ########################*/

	/* ########################  ########################*/


});