/**
 * 업무 : 브랜드관
 *  
 */

//function
brand = {
		
	/***************************************************************
	 * 브랜드관 템플릿 : brand.template
	 ***************************************************************/
	template : {
			// 브랜드관 메인
			main : function(brandId) {
				if (common.isNotEmpty(brandId)) {
					ccs.link.go("/dms/common/templateDisplay?brandId="+brandId, CONST.NO_SSL);
				}
			},
			
			// 뒤로 가기시, 기존 페이지 기억 & sns 공유용 direct 페이지 이동
			backPage : function(swiper, direct, brandId, param1, param2) {
				if (common.isEmpty(brandId)) {
					brandId = $("#hid_brandId").val();
				}
				var isMobile = ccs.common.mobilecheck();
				var currentPage = $("#hidCurrentPage").val();
				
				if (common.isNotEmpty(direct)) {
					if (direct == "catalogDetail") {
						if (isMobile) {
							swiper.slideTo(3, 300);	// '페이지 바로 이동' 개발되면 다시 주석처리 할 예정
							brand.template.itemSearch('CATALOGUE', null, "Y", brandId, param1, param2, swiper);							
						} else {
							brand.template.cornerItem('CATALOGUE', "Y", brandId, param1, param2);
						}
					}
				} else {
					if (common.isNotEmpty(currentPage)) {
//						common.showLoadingBar();					
						if (isMobile) {
							if (currentPage == "2") {	// gnb_index : 0,1,2,3,4
								var catalogId = $("#catalogForm #hid_catalogId").val();
								var catalogImgNo = $("#catalogForm #hid_catalogImgNo").val();
								
								if (catalogId != "" && catalogImgNo != "") {
									brand.template.itemSearch('CATALOGUE', null, "Y", brandId, catalogId, catalogImgNo, swiper);
								}						
							} else if (currentPage == "4") {
								var currentTap = $("#brandForm #hidCurrentTab").val();
								
								var calcHeight = setHeightInterval();
								intervalCalcHeight(calcHeight);
							}
							swiper.slideTo(Number(currentPage)+1, 300);
						} else {
							if (currentPage == "CATALOGUE") {
								var catalogId = $("#catalogForm #hid_catalogId").val();
								var catalogImgNo = $("#catalogForm #hid_catalogImgNo").val();
								
								if (catalogId != "" && catalogImgNo != "") {
									brand.template.cornerItem('CATALOGUE', "Y", brandId, catalogId, catalogImgNo);
								}
							} else {
								brand.template.cornerItem(currentPage);
							}
						}
//						common.hideLoadingBar();
						
					}
				}
			},
			
			// 카탈로그&코디북 / 이벤트&기획전 탭 클릭 이벤트
			contentsInit : function() {
				$(".tabBox").find("li").on("click", function() {
					$(this).parent().find("li").removeClass("on");
					$(this).addClass("on");
					
					var btnText = $(this).find("a").text();
					brand.template.tabEffect(btnText);
				});
			},			
			tabEffect : function(btnText) {
				if (btnText == "COLLECTION") {
					$(".coordiBox").hide();
					$(".lookbookBox").show();
					
					if (ccs.common.mobilecheck()) {
						var calcHeight = setHeightInterval();
						intervalCalcHeight(calcHeight);
					}
					$("#lookbook_detail").find(".style_type_book").hide();
					$("#catalogue_detail_pc").hide();
				} 
				else if (btnText == "COORDI BOOK") {						
					$(".lookbookBox").hide();
					$(".coordiBox").show();

					if (ccs.common.mobilecheck()) {
						var calcHeight = setHeightInterval();
						intervalCalcHeight(calcHeight);
						
						// 코디북 이미지 사이즈 조정
						for (var i=0; i<$(".coordiBox .lookbook_list .img").length; i++) {
							var catalogueImg = $(".coordiBox .lookbook_list .img:eq(" + i + ")").find("img");
							var catalogTitle = $(".coordiBox .lookbook_list li:eq(" + i + ")").find(".info_pack");
							
							catalogueImg.height( catalogueImg.width() );
							catalogueImg.parent().find("div:eq(" + i + ")").height( catalogueImg.width() + catalogTitle.innerHeight() );
						}
					}
					$("#lookbook_detail").find(".style_type_book").hide();
					$("#catalogue_detail_pc").hide();
				} 
				else if (btnText == "이벤트") {	
					$(".brand_exhibition_list").parent().removeClass("tab_conOn");
					$(".brand_event_list").parent().addClass("tab_conOn");
						
					var calcHeight = setHeightInterval();
					intervalCalcHeight(calcHeight);
				} 
				else if (btnText == "기획전") {	
					$(".brand_event_list").parent().removeClass("tab_conOn");
					$(".brand_exhibition_list").parent().addClass("tab_conOn");
						
					var calcHeight = setHeightInterval();
					intervalCalcHeight(calcHeight);
				}
			},
			
			// 코너&코너 아이템 조회
			itemSearch : function(section, tab, directYn, brandId, param1, param2, swiper) {
				if (section == "STYLE") {	// STYLE
					var param = {
							brandId : $("#hid_brandId").val(),
							genderTypeCd : $("#styleForm #genderTypeCd").val(),
							themeCd : $("#styleForm #themeCd").val(),
							sortKeyword : $("#styleForm #sortKeyword").val()
					};
					
//					common.showLoadingBar();
					$.ajax({
						contentType : "application/json; charset=UTF-8",
						url : "/mms/member/style/list/ajax",
						type : "POST",
						data : JSON.stringify(param)
					}).done(function(html) {
						$("#styleArea").html(html); 
						
						if (!ccs.common.mobilecheck()) {
							$(".brand_style").show();
						}
						
						if (common.isNotEmpty($("#styleForm #genderTypeName").val())) {
							$("#gender_select").parent().find("label").text($("#styleForm #genderTypeName").val());
							$("#gender_select").val($("#styleForm #genderTypeCd").val()).attr("selected", "selected");
						}
						if (common.isNotEmpty($("#styleForm #themeName").val())) {
							$("#theme_select").parent().find("label").text($("#styleForm #themeName").val());
							$("#theme_select").val($("#styleForm #themeCd").val()).attr("selected", "selected");
						}
						if (common.isNotEmpty($("#styleForm #sortKeywordName").val())) {
							$("#sort_select").parent().find("label").text($("#styleForm #sortKeywordName").val());
							$("#sort_select").val($("#styleForm #sortKeyword").val()).attr("selected", "selected");
						}

						if (ccs.common.mobilecheck()) {
							var calcHeight = setHeightInterval();
							intervalCalcHeight(calcHeight);
		            	}
		            		
//						common.hideLoadingBar();
					});
				}
				else if (section == "CATALOGUE") {	// CATALOGUE
					if (directYn == "Y") {
//						common.showLoadingBar();
						$.ajax({
							contentType : "application/json; charset=UTF-8",
							url : "/dms/catalog/list",
							type : "POST",
							data : JSON.stringify({"directYn" : "Y", "brandId" : brandId})
						}).done(function(html) {
							$("#lookbook_list").html(html);
							
							$("#lookbook_list").show();	
							$(".brand_lookbook").show();
							
							if (ccs.common.mobilecheck()) {
								brand.template.lookbookDetail_direct(brandId, param1, param2, swiper);
							} else {
								brand.template.lookbookDetail_direct(brandId, param1, param2);
							}												
						});
					} else {
						var seasonCd = "";
						var seasonName = "";
						if(!ccs.common.mobilecheck()) {
							var selectSeason = $("#seasonSelect option:selected").val();
							if (common.isNotEmpty(selectSeason)) {
								if (selectSeason == 'all') {
									seasonCd = "";
									seasonName = "시즌/년도";
								} else {
									seasonCd = selectSeason;
									seasonName = $("#seasonSelect option:selected").text();
								}
							}
						}
						
						var param = {
								brandId : $("#hid_brandId").val(), 
								brandName : $("#hid_brandName").val(),
								catalogueSeason : seasonCd
						};
						
//						common.showLoadingBar();
						$.ajax({
							contentType : "application/json; charset=UTF-8",
							url : "/dms/catalog/list",
							type : "POST",
							data : JSON.stringify(param)
						}).done(function(html) {						
							$("#lookbook_list").html(html);
							
							if (!ccs.common.mobilecheck()) {
								var tabName = $("#brandForm #hidCurrentTab").val();
								if (common.isNotEmpty(tabName)) {
									for (var i=0; i<$(".lookbook .tabBox").find("li").length; i++) {
										if ($(".lookbook .tabBox").find("li:eq(" + i + ")").find("a").text() == tabName) {
											$(".lookbook .tabBox").find("li").removeClass("on");
											$(".lookbook .tabBox").find("li:eq(" + i + ")").addClass("on");
										}
									}
									brand.template.tabEffect(tabName);
								}
								$("#catalogue_detail_pc").hide();
							} else {
								$("#lookbook_detail").hide();
								
								var calcHeight = setHeightInterval();
								intervalCalcHeight(calcHeight);
							}
							
							if ($("#hid_brandClassName").val() == "brand7") {	//츄즈는 룩북만 있음. 코디북 탭 숨기기(16.10.25)
								$(".lookbook .tabBox li:eq(1)").hide();
							}
							
							$("#lookbook_list").show();	
							$(".brand_lookbook").show();
							
							if (common.isNotEmpty(seasonName)) {
								$("#seasonSelect").parent().find("label").text(seasonName);
								
								for (var s=0; s<$("#seasonSelect").find("option").length; s++) {
									if ($("#seasonSelect").find("option:eq(" + s + ")").text() == seasonName) {
										$("#seasonSelect").find("option:eq(" + s + ")").attr("selected", "selected");
									}
								}
							}
														
//							common.hideLoadingBar();
						});
					}
				} 
				else if (section == "EVENT") {	// EVENT					
					var param = {
							templateTypeCd : $("#hid_templateType").val(),
							brandId : $("#hid_brandId").val(), 
							brandName : $("#hid_brandName").val()
					};
					
					var url = "";
					var currentTap = $("#brandForm #hidCurrentTab").val();
					
					if (common.isNotEmpty(currentTap)) {
						if (currentTap == "이벤트") {
							tab = "EVENT";
							url = "/sps/event/brand/list";
						} else if (currentTap == "기획전") {
							tab = "EXHIBIT";
							url = "/dms/exhibit/brand/list";
						}
					} else {
						url = "/dms/corner/brand/event/list";
					}
					
					if (common.isNotEmpty(url)) {
//						common.showLoadingBar();
						$.ajax({
							contentType : "application/json; charset=UTF-8",
							url : url,
							type : "POST",
							data : JSON.stringify(param)
						}).done(function(html) {
							$(".brand_event").html(html);
							
							if (!ccs.common.mobilecheck()) {
								$(".brand_event").show();
								
								if ($("#hid_templateType").val() == "B") {
									$(".brand_event").addClass($("#hid_brandClassName").val());
								}
							} else {
								var calcHeight = setHeightInterval();
								intervalCalcHeight(calcHeight);
							}
							
//							common.hideLoadingBar();
						});
					}
				}
			},
			
			// PC. 코너별 아이템(SECTION 클릭시 이벤트. 모바일은 처음에 다 가져오므로 호출하지 않음)			
			cornerItem : function(section, directYn, brandId, param1, param2) {
				
				if (!ccs.common.mobilecheck()) {
					var templateType = $("#hid_templateType").val();
					var brandClassName = $("#hid_brandClassName").val();
					
					$(".mainCon").find(".inner").hide();
					if (templateType == "B") {
						$(".mainCon").removeClass(brandClassName);
						$(".mainCon").find(".inner").removeClass(brandClassName);
					}
					
					if (section == "STORY") {	// STORY
						$(".brand_story").show();
						if (templateType == "B") {
							$(".brand_story").addClass(brandClassName);
						}
					}
					else if (section == "PRODUCTS") {	// PRODUCTS
						$(".brand_products").show();
						if (templateType == "B") {
							$(".brand_products").addClass(brandClassName);
						}
					}
					else if (section == "STYLE") {	// STYLE
						brand.template.itemSearch(section);
					}
					else if (section == "EVENT") {	// EVENT
						brand.template.itemSearch(section, "EVENT");
					}
					else if (section == "CATALOGUE") {	// CATALOGUE
						brand.template.itemSearch(section, null, directYn, brandId, param1, param2);
					}
					
					brand.template.changeNavi(section);
				}
			},
			
			// PC. 코너별 네비게이션
			changeNavi : function(section) {
				var currentNavi = $(".location_inner").find("ul");
				
				var naviHtml = "<li>" + currentNavi.find("li:eq(0)").text() + "</li>";
				naviHtml += "<li><a href=\"javascript:brand.template.main('" + $("#hid_brandId").val() + "');\">" + currentNavi.find("li:eq(1)").text() + "</a></li>";
				naviHtml += "<li>" + section + "</li>";

				currentNavi.html(naviHtml);
			},
							
			// CATALOGUE or COORDIBOOK 리스트
			lookbookDetail : function(catalogId, title, swiper) {				
				var templateType = $("#hid_templateType").val();
				var brandId = $("#hid_brandId").val();

//				common.showLoadingBar();
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/dms/catalog/detail/list",
					type : "POST",
					data : JSON.stringify({"templateType" : templateType, "brandId" : brandId, "catalogId" : catalogId})
				}).done(function(html) {
					$(".mo_mainNavi").hide();
					$(".mobile .content").removeAttr("style").css("padding", "50px 0 347px 0");
					
					brand.template.makeHtml(html);
					brand.template.initLightsiders();
					
					if (ccs.common.mobilecheck()) {
						for (var i=0; i<$("#mo_seasonSelect").find("option").length; i++) {
							if ($("#mo_seasonSelect").find("option:eq(" + i + ")").text() == title) {
								$("#mo_seasonSelect").find("option:eq(" + i + ")").attr("selected", "selected");
							}
						}
						ccs.mainSwipe.calculateHeight();
						
						if(common.isEmpty($("#salesAssist").val())) {
							swiper.lockSwipes();	// 상세 화면에서 스와이프 방지
						}
						$("#brandForm #catalogDetailYn").val("Y");
					}				
					brand.template.lookbookProductList(templateType, catalogId, 1);
					
//					common.hideLoadingBar();
				});
			},
			makeHtml : function(html) {
				if (ccs.common.mobilecheck()) {
					$("#lookbook_detail").html(html);
					
					$("#lookbook_list").hide();
					$("#lookbook_detail").show();
				} else {
					$("#catalogue_detail_pc").html(html);
					
					$(".lookbookBox").hide();
					$(".coordiBox").hide();
					
					$("#catalogue_detail_pc").show();
				}
			},
			
			// SNS 공유용 LOOKBOOK_DETAIL
			lookbookDetail_direct : function(brandId, catalogId, catalogImgNo, swiper) {
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/dms/catalog/detail/list",
					type : "POST",
					data : JSON.stringify({"templateType" : "A", "brandId" : brandId, "catalogId" : catalogId, "directYn" : "Y", "catalogImgNo" : catalogImgNo})
				}).done(function(html) {
					$(".mo_mainNavi").hide();
					$(".mobile .content").removeAttr("style").css("padding", "50px 0 347px 0");
					
					brand.template.makeHtml(html);
					brand.template.initLightsiders(catalogId, catalogImgNo);
					
					if (ccs.common.mobilecheck()) {
						ccs.mainSwipe.calculateHeight();
						
						swiper.lockSwipes();	// 상세 화면에서 스와이프 방지
						$("#brandForm #catalogDetailYn").val("Y");
					}					
					brand.template.lookbookProductList("A", catalogId, catalogImgNo);
					
					if ($("#hid_brandClassName").val() == "brand7") {	//츄즈는 룩북만 있음. 코디북 탭 숨기기(16.10.25)
						$(".lookbook .tabBox li:eq(1)").hide();
					}

//					common.hideLoadingBar();
				});
			},
			
			// LOOKBOOK 상품
			lookbookProductList : function(templateType, catalogId, catalogImgNo, currentPage) {
//				common.showLoadingBar();
				
				if (common.isEmpty(currentPage)) {
					currentPage = 1;
				}
				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/dms/catalog/detail/product/list",
					type : "POST",
					data : JSON.stringify({"templateType" : templateType, "catalogId" : catalogId, "catalogImgNo" : catalogImgNo, "currentPage" : currentPage})
				}).done(function(html) {					
					$("#lookbookProduct").html(html);
//					common.hideLoadingBar();
					
					$(".mobile #productGallery").lightSlider({
						gallery:true,
						item:4,
						slideMargin: 0,
						speed:400,
						auto:false,
						loop:false,
						mode:'slide',
						pager:false,
						onSliderLoad: function() {
							$(".mobile #productGallery").css("height", $(".mobile #productGallery li:first").find("img").height() );
						}  
					});
					
					if (ccs.common.mobilecheck()) {
						ccs.mainSwipe.calculateHeight();
					}					
					$(".style_book_list .paginate .total").text($("[name=productCnt]").val());					
				});
			},
			
			// LOOKBOOK 상세 lightsiders
			initLightsiders : function(catalogId, catalogImgNo) {
				var pc_slider = $('.pc #imageGallery').lightSlider({
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
					},
					onAfterSlide: function(el) {
						var imgIdx;
						for (var i=0; i<$(".pc #imageGallery").find("li").length; i++) {
							if ($(".pc #imageGallery li:eq(" + i + ")").is(".active")) {
								imgIdx = i;
								$("#activeImgForm #hid_activeIdx").val(i);
							}
						}
						var imgInfo = $(".pc #imageGallery li:eq(" + imgIdx + ")").attr("data-thumb");
						var templateType = $("#hid_templateType").val();
						
						brand.template.lookbookProductList(templateType, imgInfo.split('|')[1], imgInfo.split('||')[1]);
					}
				});
				var mo_slider = $('.mobile #imageGallery').lightSlider({
					item:1,
					vertical:true,
					verticalHeight:$(".mobile #imageGallery li:first").find("img").width(),
					slideMargin:0,
					controls:false,
					onSliderLoad: function() {
						$('.mobile .lSSlideOuter .lightSlider li').css("height", $(".mobile #imageGallery li:first").find("img").width());
					},
					onAfterSlide: function(el) {
						var imgIdx;
						for (var i=0; i<$(".mobile #imageGallery").find("li").length; i++) {
							if ($(".mobile #imageGallery li:eq(" + i + ")").is(".active")) {
								imgIdx = i;
								$("#activeImgForm #hid_activeIdx").val(i);
							}
						}
						var imgInfo = $(".mobile #imageGallery li:eq(" + imgIdx + ")").attr("data-thumb");
						var templateType = $("#hid_templateType").val();
						
						brand.template.lookbookProductList(templateType, imgInfo.split('|')[1], imgInfo.split('||')[1]);
					}
				});
				
				if (common.isNotEmpty(catalogId) && common.isNotEmpty(catalogImgNo)) {
					if (ccs.common.mobilecheck()) {
						for (var i=0; i<$(".mobile #imageGallery").find("li").length; i++) {
							var imgInfo = $(".mobile #imageGallery li:eq(" + i + ")").attr("data-thumb");
							
							if (common.isNotEmpty(imgInfo)) {
								if (imgInfo.indexOf('|') >= 0 && imgInfo.indexOf('||') >= 0) {
									if (imgInfo.split('|')[1] == catalogId && imgInfo.split('||')[1] == catalogImgNo) {
										mo_slider.goToSlide(i);	
									}
								}
							}
						}
					} else {
						for (var i=0; i<$(".pc #imageGallery").find("li").length; i++) {
							var imgInfo = $(".pc #imageGallery li:eq(" + i + ")").attr("data-thumb");
							
							if (common.isNotEmpty(imgInfo)) {
								if (imgInfo.indexOf('|') >= 0 && imgInfo.indexOf('||') >= 0) {
									if (imgInfo.split('|')[1] == catalogId && imgInfo.split('||')[1] == catalogImgNo) {
										pc_slider.goToSlide(i);
									}
								}
							}
						}
					}
				}
			},
			
			// lightsiders prev/next 클릭, 해당하는 상품 조회
			confirmSelect : function(part) {
				for (var i=0; i<$(".lSGallery").find("li").length; i++) {
					if ((i-1 > 0) || (i+1 < $(".lSGallery").find("li").length)) {
						
						if ($(".lSGallery").find("li:eq(" + i + ")").is(".active")) {
							var activeIndex = 0;
							
							if (part == 'NEXT') {
								activeIndex = i+1;
							} else if (part == 'PREV') {
								activeIndex = i-1;
							}
							
							var catalogInfo = $(".lSGallery").find("li:eq(" + activeIndex + ")").html().split('value=')[1].split('>')[0].replace(/"/g, "");
							brand.template.lookbookProductList('A', catalogInfo.split('|')[0], catalogInfo.split('|')[1]);
						}
					}
				}					
			},
			
			// 모바일. SNS 공유 레이어팝업 열기/닫기
			snsShareLayer : function() {
				$(".sLayer_sns").show();
			},
			snsShareClose : function() {
				$(".sLayer_sns").hide();
			},
			
			// sns 공유용 metaData setting
			snsDataSetting : function(frontDomainUrl, brandId, name) {
				var imgInfo;
				
				var activeIdx = $("#activeImgForm #hid_activeIdx").val();
				if (ccs.common.mobilecheck()) {
					imgInfo = $(".mobile #imageGallery li:eq(" + activeIdx + ")").attr("data-thumb");
				} else {
					imgInfo = $(".pc #imageGallery li:eq(" + activeIdx + ")").attr("data-thumb");
				}

				var url = frontDomainUrl + "/dms/common/templateDisplay?brandId=" + brandId;
				url += "&direct=catalogDetail&catalogId="+imgInfo.split('|')[1]+"&catalogImgNo="+imgInfo.split('||')[1];
				
				$("meta[property='og:image']").attr("content", imgInfo.split('|')[0]);
				$("meta[property='og:url']").attr("content", url);
				$("meta[property='og:title']").attr("content", name);
			},
			
			// HTML 타입 데이터를 직접 TEXT로 입력했을 때, 다시 HTML로 변환하여 그려주기 위해.
			appendHtml : function(appendArea) {
				if (common.isNotEmpty(appendArea)) {
					var strHtml = $("#"+appendArea).text();
					
					if (common.isNotEmpty(strHtml)) {
						$("#" + appendArea).html(strHtml);
						$("#" + appendArea).show();
					}					
				} else {	// 브랜드관에서 사용
					var strCollection = $("#cornerHtmlDiv").text();
					var strStory = $(".storyBox").text();
					var strProduct = $(".productBox").text();
					
					var strVideo = "";
					if (ccs.common.mobilecheck()) {
						strVideo = $("#tempVideoHtml_mo").text();
						if (common.isEmpty(strVideo)) {
							strVideo = $("#tempVideoHtml_mo").html();
						}
					} else {
						strVideo = $("#tempVideoHtml_pc").text();
						if (common.isEmpty(strVideo)) {
							strVideo = $("#tempVideoHtml_pc").html();
						}
					}
					
					if (common.isNotEmpty(strVideo)) {
						$("#videoHtmlDiv").html(strVideo);
					}
					if (common.isNotEmpty(strCollection)) {
						$("#cornerHtmlDiv").html(strCollection);
					}
					if (common.isNotEmpty(strStory)) {
						$(".storyBox").html(strStory);
					}
					if (common.isNotEmpty(strProduct)) {
						$(".productBox").html(strProduct);
					}
				}
				
				if (ccs.common.mobilecheck()) {
            		//ccs.mainSwipe.calculateHeight();
					var calcHeight = setHeightInterval();
        			intervalCalcHeight(calcHeight);
        		}
			},
			
			// 브랜드 매장 찾기
			searchOffshop : function() {
				ccs.link.offshop($("#hid_brandName").val());
			},
			
			// 스타일 검색 필터링
			styleFiltering : function(div) {
				if (div == "GENDER") {
					if ($("#gender_select option:selected").val() == 'GENDER_TYPE_CD.ALL') {
						$("#genderTypeCd").val("");
						$("#genderTypeName").val("");
					} else {
						$("#genderTypeCd").val($("#gender_select option:selected").val());
						$("#genderTypeName").val($("#gender_select option:selected").text());
					}				
				} else if (div == "THEME") {
					if ($("#theme_select option:selected").val() == 'THEME_CD.ALL') {
						$("#themeCd").val("");
						$("#themeName").val("");
					} else {
						$("#themeCd").val($("#theme_select option:selected").val());
						$("#themeName").val($("#theme_select option:selected").text());
					}				
				} else if (div == "SORT") {
					$("#sortKeyword").val($("#sort_select option:selected").val());
					$("#sortKeywordName").val($("#sort_select option:selected").text());
				}
				
				brand.template.itemSearch('STYLE');
			},
			
			// 카탈로그 SELECTBOX 필터링
			seasonSelect : function(swiper) {
				if (ccs.common.mobilecheck()) {
					var selectBook = $("#mo_seasonSelect option:selected").val();
					var selectBookName = $("#mo_seasonSelect option:selected").text();
					
					brand.template.lookbookDetail(selectBook, selectBookName, swiper);
				} else {
					for (var i=0; i<$(".tabBox").find("li").length; i++) {
						if ($(".tabBox").find("li:eq(" + i + ")").is(".on")) {
							$("#brandForm #hidCurrentTab").val($(".tabBox").find("li:eq(" + i + ")").find("a").text());
						}
					}
					
					brand.template.itemSearch('CATALOGUE');
				}
			},
			
			// 모바일 대카 선택
			moChooseCate1 : function(a_id) {
				if (!$(".mobile .layer_style1").find("#" + a_id).is(".on")) {
					$("[name=brand_category1]").removeClass("on");
					$(".mobile .layer_style1").find("#" + a_id).parent().addClass("on");
				}
			},
			
			// 모바일 카탈로그 상세 TITLE SELECTBOX
			makeTitleSelect : function(catalogType) {
				var strHtml = "";
				
				if (catalogType == "CATALOG_TYPE_CD.LOOKBOOK") {
					for (var i=0; i<$(".mobile .lookbookBox ul").find("li").length; i++) {
						var lookbookInfo = $(".mobile .lookbookBox ul").find("li:eq(" + i + ")").find("a");
						strHtml += "<option value=" + lookbookInfo.find("#hid_lookbook_id").val() + ">" + lookbookInfo.find("#hid_lookbook_name").val() + "</option>";
					}
				} else if (catalogType == "CATALOG_TYPE_CD.COORDILOOK") {
					for (var i=0; i<$(".mobile .coordiBox ul").find("li").length; i++) {
						var coordibookInfo = $(".mobile .coordiBox ul").find("li:eq(" + i + ")").find("a");
						strHtml += "<option value=" + coordibookInfo.find("#hid_coordi_id").val() + ">" + coordibookInfo.find("#hid_coordi_name").val() + "</option>";
					}
				}
				
				$(".mobile #mo_seasonSelect").html(strHtml);
			},
			
			// 스타일 상세 pc/mobile 분기
			goStyleDetail : function(styleNo, memberNo) {
				for (var i=0; i<$(".gnb").find("li").length; i++) {
					if ($(".gnb").find("li:eq(" + i + ")").is(".on")) {
						$("#brandForm #hidCurrentPage").val(i);
					}
				}
				
				if (ccs.common.mobilecheck()) {
					ccs.link.go("/dms/display/styleDetail?styleNo=" + styleNo + "&memberNo=" + memberNo, CONST.NO_SSL);
				} else {
					display.style.styleDetailLayer(styleNo, memberNo);
				}
			},
			
			// 이벤트/기획전 상세 분기
			goEventDetail : function(div, id) {
				if (ccs.common.mobilecheck()) {
					for (var i=0; i<$(".gnb").find("li").length; i++) {
						if ($(".gnb").find("li:eq(" + i + ")").is(".on")) {
							$("#brandForm #hidCurrentPage").val(i);
						}
					}
				} else {
					$("#brandForm #hidCurrentPage").val('EVENT');
				}				
				
				if (div == 'event') {
					$("#brandForm #hidCurrentTab").val("이벤트");
					ccs.link.go("/sps/event/detail?eventId=" + id);
				} 
				else if (div == 'exhibit') {			
					$("#brandForm #hidCurrentTab").val("기획전");
					ccs.link.go("/dms/exhibit/detail?exhibitId=" + id);							
				}
			},
			
			// lookbook 상품 더보기
			moreProduct : function(div) {
				var currentPage = $("#pagingForm #hid_current").val();
				var pageSize = $("#pagingForm #hid_pageSize").val();
				var templateType = $("#hid_templateType").val();
				var totalCnt = $(".style_book_list .paginate .total").text();	
				
				var lastPage = 0;
				if (totalCnt % pageSize > 0) {
					lastPage = parseInt(totalCnt/pageSize) + 1;
				} else {
					lastPage = parseInt(totalCnt/pageSize);
				}
				
				var activeIdx = $("#activeImgForm #hid_activeIdx").val();
				var imgInfo = $("#imageGallery li:eq(" + activeIdx + ")").attr("data-thumb");
				
	
				if (div == 'prev') {
					if (currentPage > 1 && currentPage <= lastPage) {						
						brand.template.lookbookProductList(templateType, imgInfo.split('|')[1], imgInfo.split('||')[1], Number(currentPage)-1);
					}
				} else if (div == 'next') {
					if (currentPage < lastPage) {						
						brand.template.lookbookProductList(templateType, imgInfo.split('|')[1], imgInfo.split('||')[1], Number(currentPage)+1);
					}
				}
				
			},
			
			// 룩북, 코디북 상품 상세 가기 전, 페이지 기억
			productDetail : function(catalogId, productId) {
				var activeIdx = "";
				
				if (ccs.common.mobilecheck()) {
					for (var i=0; i<$(".gnb").find("li").length; i++) {
						if ($(".gnb").find("li:eq(" + i + ")").is(".on")) {
							$("#brandForm #hidCurrentPage").val(i);
						}
					}
					activeIdx = Number($("#activeImgForm #hid_activeIdx").val()) - 1;
				} else {
					$("#brandForm #hidCurrentPage").val('CATALOGUE');
					activeIdx = $("#activeImgForm #hid_activeIdx").val();			
				}
				
				var imgInfo = $("#imageGallery li:eq(" + activeIdx + ")").attr("data-thumb");					
				$("#brandForm #directCatalogYn").val("Y");
				$("#catalogForm #hid_catalogId").val(imgInfo.split('|')[1]);
				$("#catalogForm #hid_catalogImgNo").val(imgInfo.split('||')[1]);

				ccs.link.product.detail(productId);
			},
			
			// 브랜드관 메인 동영상 재생(이미지 클릭시 재생)
			playVideo : function(playUrl) {
				var videoHeight = $(".mobile .brand_home .video").height();
				
				if(ccs.common.mobilecheck()) {
					$(".video").html('<iframe width="100%" height="' + videoHeight + '" src="' + playUrl + '?&autoplay=1" frameborder="0" allowfullscreen="true"></iframe>');
				} else {
					$(".video").html('<iframe width="332" height="192" src="' + playUrl + '?&autoplay=1" frameborder="0" allowfullscreen="true"></iframe>');
				}
			},
			
			// 브랜드 FAQ
			getFaq : function() {
				var brandId = $("#hid_brandId").val();
				var templateType = $("#hid_templateType").val();
				
				var param;
				var target_pop = "";
				if (templateType == "A") {
					param = {templateId : templateType};
					target_pop = "templateA_faqLayer";
				} else if (templateType == "B") {
					param = {brandId: brandId};
					target_pop = brandId+"_faqLayer";
				}
				
				if (ccs.common.mobilecheck()) {
					ccs.layer.open('/ccs/common/brand/faq/layer', param, target_pop);
				} else {
					$.ajax({
						  method: "post",
						  url: "/ccs/common/brand/faq/layer",
						  contentType:"application/json; charset=UTF-8",
						  data: JSON.stringify(param)
					}).done(function(html) {
						$("#brandFaq_Area").html(html);
						
						var baseTop = $(window).scrollTop() - $(window).height();
						$("#" + target_pop).css({"paddingTop" : baseTop + "px"});
						
						$(window).scrollTop(baseTop+$(window).height()/2);
					});
				}

			}
		}

}