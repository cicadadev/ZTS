mo = {};
mo.main = {

	// 홈
	getHomeMainMo : function(){
		// 메인 추천
		var recobellParam = {recType : 'p001', size : 12, pcid : global.RB_PCID};
		pms.common.getRecommendationProductList($('[name=clickProduct]'), recobellParam, function(){
			swiperCon('main_prodSwiper_2', '3'); //메인 - 상품 추천
		});
		
		// 베스트
		dms.main.makeParam('');
		
		// 관심정보 설정
		if ($("#cookieLinkYn").val() == "Y") {
			ccs.layer.interestInfoLayer.open(1, true);
		}
	},
	//샵온 메인
	getShopOnMainMo :function(){
		
		//ajax 호출
		$.get( "/ccs/common/getShopOnMainMo", function(response) {
			$('#shopOnMainDiv').html(response);
			
			special.pickup.getAllPickupProductList('all');//상품조회
			
			//shopOn
			swiperCon('shopOnSwiper_shopList', '1'); //샵on - 매장리스트
			swiperCon('mainShopOn_banner_1', '1'); //샵on - 배너
			swiperCon('shopOnSwiper_bestProd', '5'); //샵on - 매장픽업 BEST 상품
			mo.main.mainVisualLayer("shopOnVisualLayer");
		});
	},
	//스타일샵 메인
	getStyleMainMo :function(){
		
		//ajax 호출
		$.get( "/ccs/common/getStyleMainMo", function(response) {
			$('[name=styleMainDiv]').html(response);
			
			swiperCon('styleSwiper_banner_1', '1'); //샵on - 배너
			swiperCon('styleDetail_prodList_1', '5', 10);
			display.style.getStyleList();
		});
	}, 
	// 기획전 메인
	getExhibitionMainMo :function(){
		
		var paramCategoryId = "";
		var paramBrandId = "";
		
		if(_hash_param && _hash_param.indexOf("#3")>-1){
			if(_hash_param.length > 1){
				paramCategoryId = _hash_param[1];
			}
			if(_hash_param.length > 2){
				paramBrandId = _hash_param[2];
			}
			
		}
		
		//ajax 호출
		$.get( "/ccs/common/getExhibitionMainMo", function(response) {
			$('#exhibitionMainDiv').html(response);
			
			swiperCon('exhibitBanner_swiper_1', '1'); //배너 스와이프 초기화
			
			mo.main.mainVisualLayer("exhibitVisualLayer");//비주얼 레이어 초기화
			
			exhibit.main.getExhibitList(paramCategoryId, paramBrandId);// 상품 목록 조회
			
		});
	}, 	
	//쇼킹제로 메인
	getShockingzeroMainMo :function(){
		
//		sortType:popular
//		firstRow:1
//		currentPage:1
		var sortType = "popular";
		var currentPage;
		var firstRow;
		
		//해시값으로 파라메터 설정
		if(_hash_param && _hash_param.indexOf("#1")>-1){
			if(_hash_param.length > 1){
				sortType = _hash_param[1];
			}
			if(_hash_param.length > 2){
				currentPage = _hash_param[2];
			}
			if(_hash_param.length > 3){
				firstRow =  _hash_param[3];
			}
		}
		
		
		//ajax 호출
		$.get( "/ccs/common/getShockingzeroMainMo", function(response) {
			
			$('#shockingzeroMainDiv').html(response);
			
			sps.deal.shockingzero.resorting(sortType, currentPage, firstRow);
			
		});
	}, 	
	//이벤트 메인
	getEventMainMo :function(){
		//ajax 호출
		$.get( "/ccs/common/getEventMainMo", function(response) {
			$('#eventMainDiv').html(response);
			//ccs.mainSwipe.calculateHeight();
			
			var calcHeight = setHeightInterval();
			intervalCalcHeight(calcHeight);
		});
	}, 	
	mainVisualLayer : function(tergetClass){
		
		//ccs.layer.copyLayerToContentChild(targetId);		
	},
	
	naviSwiper : function(){
		
		if( $(".mobile .mainCon").length ){
			
			var mainSwiper_header = new Swiper('.mo_mainNavi', {
				slidesPerView: 'auto', //가로 길이
		        paginationClickable: true,
		        //hashnav : true,
		        spaceBetween: 0 //간격

			});
			
			//텝클릭시 스와이프 기능
			$('.gnb > li > a').off("click").on('click', function(e){
				$(this).parent().addClass('on').siblings().removeClass('on');
				var tabIdx = $(this).parent().index();
				//0 부터 시작 탭의 index번호
				//console.log('탭클릭 Idx : '+tabIdx);
				
				mainSwiper_body.slideTo(tabIdx + 1, 300 );
				startSwipe(tabIdx);
				e.preventDefault();
				
			});
			
			var tabLen = $('.gnb > li').length;
			
			// 스와이핑시 컨텐츠 조회
			var startSwipe = function(idx){
				
				if( LOADED[idx]){
					return;
				}
				if(idx==0){
					// 홈
					mo.main.getHomeMainMo();
				}
				else if(idx==1){
					//shocking zero
					mo.main.getShockingzeroMainMo();
					
				}else if(idx==2){
					// 베스트
					display.bestShop.main();
				}else if(idx==3){
					// 기획전
					mo.main.getExhibitionMainMo();
				}else if(idx==4){
					// 이벤트 혜택
					mo.main.getEventMainMo();
				}else if(idx==5){
					// 샵온
					mo.main.getShopOnMainMo();
					
				}else if(idx==6){
					// 스타일
					mo.main.getStyleMainMo();
				}
				console.log("Get Idx : " + idx);
				LOADED[idx]=true;
			}
			//현재 인덱스
			var currentIdx = function(swiper){
				//console.log("activateIndex:"+swiper.activeIndex);
				var idx = swiper.activeIndex-1;
				
				if( idx < 0 ) { 
					idx = tabLen - 1;
				} else if( idx == tabLen ){
					idx = 0;
				}
				
				return idx;
				
			}
			
			var prevIndex = function(idx){
				idx--;
				
				if( idx < 0 ) { 
					idx = tabLen - 1;
				}
				return idx;
				
			}
			var nextIndex = function(idx){
				idx++;
				if( idx == tabLen ) { 
					idx = 0;
				}
				return idx;
			}
			
			mainSwiper_body = new Swiper('.mainCon', {
				initialSlide : 0,
				autoHeight: true,
				calculateHeight:true,
				slidesPerView: 1,
				spaceBetween: 0,
				pagination: false,
				hashnav : true,
				//hashnavWatchState : true,
				loop: true,
				onInit : function(swiper){
					startSwipe(currentIdx(swiper));
				},
				// 슬라이드 시작 시점
				onTransitionStart : function(swiper){
					
				},
				onTransitionEnd : function(swiper){
					
				},
				// 슬라이드 오른쪽 종료 시점
				onSlideNextStart : function(swiper){
					if(common.isEmpty(swiper.swipeDirection)){
						return;
					}
					startSwipe(currentIdx(swiper));
				},
				// 슬라이드 오른쪽 종료 시점
				onSlideNextEnd : function(swiper){
					if(common.isEmpty(swiper.swipeDirection)){
						return;
					}
					startSwipe(nextIndex(currentIdx(swiper)));
				},	
				// 슬라이드 왼쪽 종료 시점
				onSlidePrevStart : function(swiper){
					if(common.isEmpty(swiper.swipeDirection)){
						return;
					}
					startSwipe(currentIdx(swiper));
				},
				// 슬라이드 왼쪽 종료 시점
				onSlidePrevEnd : function(swiper){
					if(common.isEmpty(swiper.swipeDirection)){
						return;
					}
					startSwipe(prevIndex(currentIdx(swiper)));
				},
				// 슬라이드 시작시 헤더 선택되게
				onSlideChangeStart: function(swiper){
					
					//startSwipe(currentIdx(swiper));
					//console.log(swiper.swipeDirection)
					var speed = 300;
					if(common.isEmpty(swiper.swipeDirection)){
						speed = 0;
					}else{
						speed = 300;
					}
//					//console.log(swiper.previousIndex+','+swiper.activeIndex+','+swiper.realIndex)
					var idx = currentIdx(swiper);
					$('.gnb > li').removeClass('on').eq(idx).addClass('on');
					if( idx < tabLen ) {
						mainSwiper_header.slideTo(idx-1, speed);
					}
				
					$(window).scrollTop(0);	
					
				},
				onSlideChangeEnd: function(swiper){
					
				}
			});
		}		
		
	}
	
}
var hash;
var _hash_param;
var LOADED = [false, false,false, false,false, false, false];
if(location.hash){
//	location.hash
	
	hash = location.hash;
	_hash_param = hash.split("II");
	
	location.hash = hash.split("II")[0];
	
}else{
	location.hash = "#0";
}
//alert(location.hash)
//mo.main.naviSwiper();

$(document).ready(function() {
	// 메인 스와이프
	//mo.main.naviSwiper();
	
	//공통
	swiperCon('mainSwiper_category', '5'); //메인 - 카테고리 탭
	
	//홈
	swiperCon('mainBanner_swiper_1', '1',0 ,'fraction'); //메인 - 메인 배너
	swiperCon('main_prodSwiper_1', '3', 10, '', true); //메인 - MD 특가
	swiperCon('main_prodSwiper_2', '3', 10, '', true); //메인 - 상품 추천
	swiperCon('main_prodSwiper_3', '3', 10, '', true); //메인 - HOT 상품
	swiperCon('main_brandSwiper', '4'); //메인 - 제휴배너
	swiperCon('mainBanner_swiper_2', '1'); //메인 - 광고배너
	
	//베스트
	//swiperCon('linkBestSwiper_category', 'auto'); //베스트 - 중카테고리
	
	// 앱 이용방법
	swiperCon('appGuide_swiper', '1'); //앱 이용방법
	
	//홈 컨텐트 조회
//	mo.main.getHomeMainMo();
});	

$(window).load(function() {
	// 전체 스와이프 높이 계산
	ccs.mainSwipe.calculateHeight();
});