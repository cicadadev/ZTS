/**
 * 업무 : 전시
 *  
 */

//function
display = {
	
    	/***************************************************************
    	 * 베스트
    	 ***************************************************************/
		bestShop : {
			
			main : function() {
				$.get( "/api/dms/common/gbnInfo", function(response) {
					var strHtml = "<option value=''>전체</option>";
					
					$.each(response.ageCodeList, function(index, value) {						
						strHtml += "<option value="+value.cd+">" + value.name + "</option>";
					});
					$("#ageSelectbox").html(strHtml);
					
					display.bestShop.makeParam("click", "Y");
					display.bestShop.bestShopEffect();
				});
			},
			

        	// 실시간 클릭/주간판매
        	makeParam : function(menu, initYn, categoryId) {
        		var paramKey = "";
        		if (common.isNotEmpty(categoryId) && categoryId != 'undefined') {
        			paramKey += categoryId;
        		} else {	// categoryId가 없으면 '전체'
        			for (var i=0; i<$("#categoryArea").find("li").length; i++) {
            			if ($("#categoryArea").find("li:eq(" + i + ")").is(".on")) {
            				if (i > 0) {	// 대카도 '전체'가 아닐때만 catgoryId 넘김
            					paramKey += $("#categoryArea").find("li:eq(" + i + ") #hid_category1").val();
            				}	
            			}
        			}
        		}
        		
        		var searchData = display.bestShop.getSearchCondition();
        		paramKey += "-" + searchData.gender + "-" + searchData.age;
        		
        		if (menu == "click") {
        			paramKey += "-view";
        		} else if (menu == "order") {
        			paramKey += "-order";
        		}

        		display.bestShop.search({recType:"x", size:50, key:paramKey, callback:"foobar"});
        		if (initYn == "Y") {
        			if (menu == "click") {
            			$("#categoryArea2").find("li").removeClass("on");
            			$("#default1").addClass("on");
            		} else if (menu == "order") {
            			$("#categoryArea").find("li").removeClass("on");
            			$("#default2").addClass("on");
            		}
        			
        			if (ccs.common.mobilecheck()) {
        				$(".bestListArea").parent().css("border-top", "none");
        			}		
        			
        			$("#category2_Area").find("ul").hide();
        			$("#default3").show();
        		}
        		
        	},
			
        	search : function(data) {
        		//common.showLoadingBar();
        		
        		$.ajax({
					url : "/dms/display/bestShop/ajax",
					type : "get",
					data : data
				}).done(function(html) {
					display.bestShop.bestCallback(html);
					//common.hideLoadingBar();
				});
        		
        	},
        	bestCallback : function(html) {
        		if (ccs.common.mobilecheck()) {
        			$(".bestListArea").html(html);
        			
        			var calcHeight = setHeightInterval();
        			intervalCalcHeight(calcHeight);
        			
        		} else {
        			$("#bestListArea").html(html);
        		}
        	},
        	
        	// 대카 선택
        	chooseCategory : function(index) {
        		var categoryId = "";
        		var onMenu = "";
        		
        		if ($(".real").parent().is(".on")) {
        			onMenu = "click";
        			categoryId = $("#categoryArea li:eq(" + index + ")").find("#hid_category1").val();
        		} else if ($(".week").parent().is(".on")) {
        			onMenu = "order";
        			categoryId = $("#categoryArea2 li:eq(" + index + ")").find("#hid_category1").val();
        		}
        		
        		if (common.isEmpty(categoryId) || categoryId == 'undefined') {
        			display.bestShop.setSearchParam('all');
        			if(ccs.common.mobilecheck()) {
            			$(".bestListArea").parent().css("border-top", "none");
            		}
        		} else {
        			display.bestShop.setSearchParam(categoryId);
        			if(ccs.common.mobilecheck()) {
            			$(".bestListArea").parent().css("border-top", "1px solid #ddd");
            		}
        		}
 
        		$("#category2_Area").find("ul").hide();
        		$("#category2_Area").find("ul:eq(" + index + ")").show();
        		
        		if (!ccs.common.mobilecheck()) {
        			$("#category2_Area").find("ul:eq(" + index + ")").find("li:eq(0) a").addClass("on");
        		}
        		
        	},
        	
        	// 카테고리별 상품
        	setSearchParam : function(categoryId) {
        		var menu = "";
        		if ($(".real").parent().find("li").is(".on")) {
        			menu = "click";
        		} else if ($(".week").parent().find("li").is(".on")) {
        			menu = "order";
        		}

        		if (categoryId == "all") {
        			display.bestShop.makeParam(menu, "N");
        		} else {
        			display.bestShop.makeParam(menu, "N", categoryId);
        		}
        	},
        	
        	// 연령, 성별 셀렉트 박스 변경시
        	changeAgeOrGender : function() {
        		var menu = "";
        		if ($(".best_tab .best_type").find("li:eq(0)").is(".on")) {
        			menu = "click";
        		} else if ($(".best_tab .best_type").find("li:eq(1)").is(".on")) {
        			menu = "order";
        		};
        		
        		var categoryId;
        		for (var c1=0; c1<$("#categoryArea").find("li").length; c1++) {
        			if ($("#categoryArea").find("li:eq(" + c1 + ")").is(".on")) {
        				
        				var category2_list = $("#category2_Area").find("ul:eq(" + c1 + ")");
        				for (var c2=0; c2<category2_list.find("li").length; c2++) {
        					if (category2_list.find("li:eq(" + c2 + ")").is(".active")) {
                				categoryId = category2_list.find("li:eq(" + c2 + ") #hid_category2").val();
                			}
                		}
        				
        				if (common.isEmpty(categoryId) || categoryId == 'undefined') {
        					categoryId = $("#categoryArea").find("li:eq(" + c1 + ") #hid_category1").val();
        				}
        			}
        		}
	
        		if (common.isNotEmpty(categoryId) && categoryId != 'undefined') {
        			display.bestShop.makeParam(menu, N, categoryId);
				} else {
					display.bestShop.makeParam(menu, N);
				}
        	},
        	
        	// 연령, 성별 정보
        	getSearchCondition : function() {
        		var age = $("#ageSelectbox option:selected").val();
        		var gender = $("#genderSelectbox option:selected").val();

        		var babyMonthStr = "";
        		if (common.isNotEmpty(age) && age != 'undefined') {
        			if (age == "AGE_TYPE_CD.3MONTH") {
        				babyMonthStr = "A";
        			} else if (age == "AGE_TYPE_CD.6MONTH") {
        				babyMonthStr = "B";
        			} else if (age == "AGE_TYPE_CD.12MONTH") {
        				babyMonthStr = "C";
        			} else if (age == "AGE_TYPE_CD.24MONTH") {
        				babyMonthStr = "D";
        			} else if (age == "AGE_TYPE_CD.4YEAR") {
        				babyMonthStr = "E";
        			} else if (age == "AGE_TYPE_CD.6YEAR") {
        				babyMonthStr = "F";
        			} else if (age == "AGE_TYPE_CD.OVER7") {
        				babyMonthStr = "G";
        			}
        		} else {
        			babyMonthStr = age;
        		}
        		
        		return {"age" : babyMonthStr, "gender" : gender};
        	},
        	
        	// 랭킹베스트 클릭 이벤트
        	bestShopEffect : function () {
    			$("#categoryArea>li").on("click", function(e) {
        			display.bestShop.chooseCategory($(this).index());
        			
        			$("#categoryArea").find("li").removeClass("on");
        			$(this).addClass("on");
        			
        			e.preventDefault();
        		});
        		
        		$("#categoryArea2>li").on("click", function(e) {
        			display.bestShop.chooseCategory($(this).index());
        			
        			$("#categoryArea2").find("li").removeClass("on");
        			$(this).addClass("on");
        			
        			e.preventDefault();
        		});
        		
        		$(".main_best .best_type>li").on("click", function(e) {
        			var tabClass = $(this).find(">a").attr("class");
        			
        			if (tabClass == 'real') {
        				$("#infoTxt").text("* 고객님들이 많이 클릭한 상품순입니다.");
        				
        				$("#categoryArea2").hide();
            			$("#categoryArea").show();
        			} else if (tabClass == 'week') {
        				$("#infoTxt").text("* 고객님들이 많이 주문한 상품순입니다.");
        				
        				$("#categoryArea").hide();
            			$("#categoryArea2").show();
        			}
        			e.preventDefault();
        		});
        	}
		},
		
		/***************************************************************
    	 * 스타일
    	 ***************************************************************/
		style : {
			
			getStyleList : function(param1, param2, param3, param4) {
				
				//hash 값 저장
				if(ccs.common.mobilecheck() && location.hash.indexOf("#6") > -1) {
					location.hash = "#6II"+param1+"II"+param2+"II"+param3+"II"+param4;
				}
				
				if (param1 != "") {
					$("select[name=sel_gender]").val(param1);
				}
				if (param2 != "") {
					$("select[name=sel_theme]").val(param2);
				}
				if (param3 != "") {
					$("select[name=sel_brand]").val(param3);
				}
				if (param4 != "") {
					$(".option_style1 input").each(function() {
						$(this).removeAttr("checked");
						$(this).parent().removeClass("selected");
					});
					$("input[name="+param4+"]").attr("checked", "checked");
					$("input[name="+param4+"]").parent().addClass("selected");
				} else {
					$("input[name=popular]").attr("checked", "checked");
					$("input[name=popular]").parent().addClass("selected");
				}
				
				data = {
					  genderTypeCd : param1
					, themeCd : param2	
					, brandId : param3
				    , sortKeyword : param4	
				};
				//common.showLoadingBar();
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/dms/display/style/list/ajax",
					type : "POST",		
					data : JSON.stringify(data)
				}).done(function( html ) {
					display.style.listCallback(html);
					
					//common.hideLoadingBar();
//					$(".mobile .mainCon").height( $(".mainCon > .inner:first").innerHeight() );
				});
			},
		
			// SET AJAX RETURN HTML
			listCallback : function(html) {
				$(".stylist_div").html(html);
//				ccs.mainSwipe.calculateHeight();
				var calcHeight = setHeightInterval();
				intervalCalcHeight(calcHeight);
				
			},
			
			// 좋아요 업데이트
			updateLike : function(param, styleNo, memberId) {
				
				mms.common.isLogin(function(result) {

					if (result == 1) {
						var likeYn;
						$(param).toggleClass("on");
						
						if ($(param).hasClass("on")) {
							likeYn = "Y";
							var cnt = Number($(param).find("span").text()) + 1;
							$(param).find("span").text(cnt);
							
							if ($(param).children("span").attr("id") != "listlikeCnt_"+styleNo) {
								$("#listlikeCnt_"+styleNo).text(cnt)
								$("#listlikeCnt_"+styleNo).parent().addClass("on");
							}
						} else {
							likeYn = "N";
							var cnt = 0;
							if ($(param).find("span").text() != "0") {
								cnt = $(param).find("span").text() - 1;
							} else {
								return;
							}
							
							$(param).find("span").text(cnt);
							
							if ($(param).children("span").attr("id") != "listlikeCnt_"+styleNo) {
								$("#listlikeCnt_"+styleNo).text(cnt)
								$("#listlikeCnt_"+styleNo).parent().removeClass("on");
							}
						}
						
						data = {
								  "styleNo" : styleNo
								, "memberId" : memberId
								, "likeYn" : likeYn
						}
						$.ajax({
							contentType : "application/json; charset=UTF-8",
							url : "/api/dms/display/style/updateLikeCnt",
							type : "POST",		
							data : JSON.stringify(data)
						}).done(function( response ) {
							
						});
						
					} else {
						if (confirm("좋아요 하시려면 로그인이 필요합니다. 로그인하시겠습니까?")) {
							ccs.link.login();
						}
					}
				});
				
			},
			
			// 스타일 상세
			styleDetailLayer : function(param1, param2) {
				
				param = {styleNo : param1, idShowYn : 'Y', memberNo : param2};
				var url = "/ccs/common/styleDetail/layer";
				
				ccs.layer.open(url, param, "styleLayer", function() {
					
				});
			}, 
			
			// 성별, 테마, 브랜드, sort
			changeFilter : function($this) {
				var sortKeyword = "";  
				var brandId = "";
				var genderTypeCd = "";
				var themeCd = "";
				$this.closest("ul").children("li").find("div").each(function() {
					if ($(this).children("select").attr("id") == 'sel_brand') {
						brandId = $(this).children("select").val();     
					}
					if ($(this).children("select").attr("id") == 'sel_gender') {
						genderTypeCd = $(this).children("select").val();     
					}
					if ($(this).children("select").attr("id") == 'sel_theme') {
						themeCd = $(this).children("select").val();     
					}
				});
				
				if (ccs.common.mobilecheck()) {  
					$(".mobile .option_style1 input").each(function() {
						if($(this).is(':checked')){
							sortKeyword = $(this).val();
						}
					}); 
				} else {
					sortKeyword = $this.closest("ul").children("li").eq(1).find("select").val();
				}
				
				
				display.style.getStyleList(genderTypeCd, themeCd, brandId, sortKeyword);
			}
		},
		
		/***************************************************************
    	 * shopOn
    	 ***************************************************************/
		shopOn : {
			
			getData : function(param) {
				common.showLoadingBar();
				$.ajax({
					  method: "post",
					  url: "/dms/special/pickup/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#categoryPrdList_div").html(html);
//						ccs.mainSwipe.calculateHeight();
						var calcHeight = setHeightInterval();
						intervalCalcHeight(calcHeight);
						
						common.hideLoadingBar();
				});
			},
			
			
			// 픽업 가능 상품 보기
			getPickupProductList : function(param1, param2) {
				
				window.scrollTo(0, $("#shop_search").offset().top);

				$("#shopon_searchKeyword").val(param2);
				ccs.common.fnPlaceholder();
				var displayType = "";
				
				$(".best_box .tabBox > li").each(function() {  
					if ( $(this).find("a").hasClass("on")) { 
						displayType = $(this).attr("id"); 
					}   
				});
				
				
				var param = {
						searchKeyword : $("#shopon_searchKeyword").val(),
						displayType : displayType
				};
				display.shopOn.getData(param);
				
			}, 
			
			// 카테고리, 정렬, 검색, 관심매장상품만 보기
			searchPickupPrdList : function() {
				
				var displayType = "";
				var brandId = "";
				$(".tab_outer .tabBox").find("li").each(function() {
					if ($(this).hasClass("on")) {
						if ($(this).attr("id") == 'all') {
							displayType = $(this).attr("id");
						} else {
							brandId = $(this).attr("id");
						}
					}
				});
				var interestOffshopView = "N"; 
					
				if ($("input[name=interestOffshopCheckbox]").is(":checked")) {
					interestOffshopView = 'Y';
				} else {
					interestOffshopView = 'N';
				}
				
				var sido = "";
				var sigungu="";
				var brandId ="";
				if ($("#offshopArea1").val() != "시/도") {
					sido = $("#offshopArea1").val();
				}
				if ($("#offshopArea2").val() != "시/군/구") {
					sigungu = $("#offshopArea2").val();
				}
				if ($("#shopon_select_brand").val() != "브랜드") {
					brandId = $("#shopon_select_brand").val();
				}
				
				var mobileSort = $("#shopon_select_sort").val();
				var categoryId = $("#shopon_select_cate").val();
				
				
				param = {
							displayType : displayType
						 , brandId : brandId
						 , areaDiv1 : sido
						 , areaDiv2 : sigungu
						 , searchKeyword : $("#searchKeyword").val()  	 // 검색어
						 , mobileSort : mobileSort
						 , displayCategoryId : categoryId
						 , interestOffshopPrdView : interestOffshopView  // 관심매장 상품만 보기
				}
				
				display.shopOn.getData(param);
			},
			
			
			// 브랜드 클릭 (상품리스트 조회)
			getPickupPrdList : function(brandId) {
				// 전체, 브랜드 탭 선택처리
				
				if (brandId == 'all') {
					brandId = "";
				} else {
					$("#shopon_select_brand").val(brandId);
					$("#brand_label").text($("#brand_label").next().find("option:selected").text());
					$("#shopon_select_brand").attr("disabled",true);
				}
				
				var param = {
						brandId : brandId
				};
				
				$.ajax({
					  method: "post",
					  url: "/dms/special/pickup/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#categoryPrdList_div").html(html);
						ccs.mainSwipe.calculateHeight();
					});
				
				
				$.ajax({
					method: "post",
					url: "/api/ccs/common/pickup/areadiv1",
					contentType:"application/json; charset=UTF-8",
					data: JSON.stringify(param) 
				}).done(function( data ) {
					$('#offshopArea1').html('<option>시/도</option>');
					for(var i = 0 ; i < data.length ; i++){
						$('#offshopArea1').append("<option value='"+ data[i].areaDiv1 +"'>"+data[i].areaDiv1+"</option>");
					}
				});
			}, 
			
			getAllPickupProductList : function(param1, param2, param3) {
				
				$("#shopon_select_brand").val("");
				$("#brand_label").text("브랜드");
				$("#shopon_select_brand").attr("disabled",false);
				
				param = {displayType : param1, offshopId : param2, searchKeyword : param3};
				$.ajax({
				  method: "post",
				  url: "/dms/special/pickup/list/ajax",
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				}).done(function( html ) {
					special.pickup.listCallback(html);
					ccs.mainSwipe.calculateHeight();
				});
				
				
				$.ajax({
					method: "post",
					url: "/api/ccs/common/pickup/areadiv1",
					contentType:"application/json; charset=UTF-8",
					data: JSON.stringify(param) 
				}).done(function( data ) {
					$('#offshopArea1').html('<option>시/도</option>');
					for(var i = 0 ; i < data.length ; i++){
						$('#offshopArea1').append("<option value='"+ data[i].areaDiv1 +"'>"+data[i].areaDiv1+"</option>");
					}
				});
				
				
				
			},
		}
}

