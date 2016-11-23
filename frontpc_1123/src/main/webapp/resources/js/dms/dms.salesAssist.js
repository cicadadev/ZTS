/**
 * 업무 : 브랜드관
 *  
 */

//function
sales = {
		contentsInit : function() {
			$(".tabBox").find("li").on("click", function() {
				$(this).parent().find("li").removeClass("on");
				$(this).addClass("on");
				
				var btnText = $(this).find("a").text();
				sales.tabEffect(btnText);
			});
		},			
		tabEffect : function(btnText) {
			if (btnText == "COLLECTION") {
				$(".coordiBox").hide();
				$(".lookbookBox").show();
				
				$("#lookbook_detail").find(".style_type_book").hide();
				$("#catalogue_detail_pc").hide();
			} 
			else if (btnText == "COORDI BOOK") {						
				$(".lookbookBox").hide();
				$(".coordiBox").show();

				for (var i=0; i<$(".mobile .coordiBox ul").find("[name=coordi_detailLink]").length; i++) {
					var img = $(".mobile .coordiBox ul").find("[name=coordi_detailLink]:eq(" + i + ") img");
					img.css("height", img.width());
				}
				
				$("#lookbook_detail").find(".style_type_book").hide();
				$("#catalogue_detail_pc").hide();
			} 
//				var calcHeight = setHeightInterval();
//				intervalCalcHeight(calcHeight);
		},
		// 코너&코너 아이템 조회
		itemSearch : function(brandId) {
			var seasonCd = "";
			var seasonName = "";
			
			var param = {
					brandId : $("#hid_brandId").val(), 
					brandName : $("#hid_brandName").val(),
					salesAssist : "Y"
			};
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/dms/catalog/list",
				type : "POST",
				data : JSON.stringify(param)
			}).done(function(html) {						
				$("#lookbook_list").html(html);
				$("#lookbook_detail").hide();
				
//				var calcHeight = setHeightInterval();
//				intervalCalcHeight(calcHeight);
				
				if ($("#hid_brandClassName").val() == "brand7") {	//츄즈는 룩북만 있음. 코디북 탭 숨기기(16.10.25)
					$(".lookbook .tabBox li:eq(1)").hide();
				}
				
				$("#lookbook_list").show();	
				$(".brand_lookbook").show();
											
			});
		},
						
		// CATALOGUE or COORDIBOOK 리스트
		lookbookDetail : function(catalogId, title) {				
			var templateType = $("#hid_templateType").val();
			var brandId = $("#hid_brandId").val();

			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/dms/catalog/detail/list",
				type : "POST",
				data : JSON.stringify({"templateType" : templateType, "brandId" : brandId, "catalogId" : catalogId, "salesAssist" : "Y"})
			}).done(function(html) {
				$(".mo_mainNavi").hide();
				$(".mobile .content").removeAttr("style").css("padding", "50px 0 347px 0");
				
				sales.makeHtml(html);
				sales.initLightsiders();
				
				for (var i=0; i<$("#mo_seasonSelect").find("option").length; i++) {
					if ($("#mo_seasonSelect").find("option:eq(" + i + ")").text() == title) {
						$("#mo_seasonSelect").find("option:eq(" + i + ")").attr("selected", "selected");
					}
				}
//				ccs.mainSwipe.calculateHeight();

				$("#brandForm #catalogDetailYn").val("Y");

				sales.lookbookProductList(templateType, catalogId, 1);
				
			});
		},
		makeHtml : function(html) {
			$("#lookbook_detail").html(html);
			
			$("#lookbook_list").hide();
			$("#lookbook_detail").show();
		},
		
		// LOOKBOOK 상품
		lookbookProductList : function(templateType, catalogId, catalogImgNo, currentPage) {
			
			if (common.isEmpty(currentPage)) {
				currentPage = 1;
			}
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/dms/catalog/detail/product/list",
				type : "POST",
				data : JSON.stringify({"templateType" : templateType, "catalogId" : catalogId, "catalogImgNo" : catalogImgNo, "currentPage" : currentPage, "salesAssist" : "Y"})
			}).done(function(html) {					
				$("#lookbookProduct").html(html);
				
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
				
//				ccs.mainSwipe.calculateHeight();
				$(".style_book_list .paginate .total").text($("[name=productCnt]").val());					
			});
		},
		
		// LOOKBOOK 상세 lightsiders
		initLightsiders : function(catalogId, catalogImgNo) {
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
					
					sales.lookbookProductList(templateType, imgInfo.split('|')[1], imgInfo.split('||')[1]);
				}
			});
			
			if (common.isNotEmpty(catalogId) && common.isNotEmpty(catalogImgNo)) {
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
						sales.lookbookProductList('A', catalogInfo.split('|')[0], catalogInfo.split('|')[1]);
					}
				}
			}					
		},
		
		appendHtml : function(appendArea) {
			if (common.isNotEmpty(appendArea)) {
				var strHtml = $("#"+appendArea).text();
				
				if (common.isNotEmpty(strHtml)) {
					$("#" + appendArea).html(strHtml);
					$("#" + appendArea).show();
				}					
			} else {	// 브랜드관에서 사용
				var strCollection = $("#cornerHtmlDiv").text();
				var strProduct = $(".productBox").text();
				
				if (common.isNotEmpty(strCollection)) {
					$("#cornerHtmlDiv").html(strCollection);
				}
				if (common.isNotEmpty(strProduct)) {
					$(".productBox").html(strProduct);
				}
			}
			
//			if (ccs.common.mobilecheck()) {
//    			// 스와이프 높이 다시 계산
//        		ccs.mainSwipe.calculateHeight();
//    		}
		},
		
		// 카탈로그 SELECTBOX 필터링 
		seasonSelect : function() {
			var selectBook = $("#mo_seasonSelect option:selected").val();
			var selectBookName = $("#mo_seasonSelect option:selected").text();
			
			sales.lookbookDetail(selectBook, selectBookName);
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
					sales.lookbookProductList(templateType, imgInfo.split('|')[1], imgInfo.split('||')[1], Number(currentPage)-1);
				}
			} else if (div == 'next') {
				if (currentPage < lastPage) {						
					sales.lookbookProductList(templateType, imgInfo.split('|')[1], imgInfo.split('||')[1], Number(currentPage)+1);
				}
			}
			
		},
		
		// 룩북, 코디북 상품 상세 가기 전, 페이지 기억
		productDetail : function(catalogId, productId) {
			var activeIdx = "";
			
			for (var i=0; i<$(".gnb").find("li").length; i++) {
				if ($(".gnb").find("li:eq(" + i + ")").is(".on")) {
					$("#brandForm #hidCurrentPage").val(i);
				}
			}
			activeIdx = Number($("#activeImgForm #hid_activeIdx").val()) - 1;
			
			var imgInfo = $("#imageGallery li:eq(" + activeIdx + ")").attr("data-thumb");					
			$("#brandForm #directCatalogYn").val("Y");
			$("#catalogForm #hid_catalogId").val(imgInfo.split('|')[1]);
			$("#catalogForm #hid_catalogImgNo").val(imgInfo.split('||')[1]);

			ccs.link.product.detail(productId);
		}

}