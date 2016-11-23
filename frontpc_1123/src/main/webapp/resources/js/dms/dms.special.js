/**
 * 업무 : 전시 - 전문관 
 *  
 */

var cornerNo = global['corner'];
//function
special = {
		
		common : {
			
			/***********************************************************
			 * 분유관, 출산준비관 탭 이벤트 : special.common.category
			 ***********************************************************/
			category : {
				
				//대카 클릭 이벤트 :special.common.category.getSpecialCategory
				getSpecialCategory : function(type,value){
					
					var searchYn = false;
					//분유관 탭 전체
					if(type === 'all'){
						
						if(ccs.common.mobilecheck()){
							$('#moSortBoxList').show();
							$('#cornerOrderByDiv').hide();
							$('#moAllCount').show();
						}else{
							$('#moSortBoxList').hide();
							$('#pcSpeCtgList').hide();
						}
						$('#cornerProduct').hide();
						$('#productList').show();
						$('#brandInfoCorner').hide();
						$('#allOrderByDiv').show();
						searchYn = true;
							
					}else{
						
						$('#cornerProduct').hide();
						$('#productList').empty();
						$('#productList').show();
						$('#allOrderByDiv').hide();
						if(ccs.common.mobilecheck()){
							$('#moSortBoxList').show();
							$('#cornerOrderByDiv').hide();
							$('#moAllCount').hide();
						}else{
							$('#pcSpeCtgList').show();
						}
						searchYn = true;
						
					}
					
					if(searchYn){
						//검색 파라미터 설정
						
						param = {'displayCategoryId':value, 
								'linkType':$('#eventType').val(),
								'categoryViewType':$('#categoryViewType').val()
						}
						$.post('/dms/special/specialCategory',param, function(html) {
							
							if(ccs.common.mobilecheck()){
								$("#moSpeCtgList").html(html);
							}else{
								$("#pcSpeCtgList").html(html);
							}	
							var displayCategoryId = $('#displayCategoryId').val();
							
							if(type === 'all'){
								var categoryIds = displayCategoryId.replace(regExp, "");
								ctgId = categoryIds.replace(/(\s*)/g,"");
								displayCategoryId = ctgId;
							}
							
							//코너아이템 조회
							if(displayCategoryId !== ''){
								
								param1 ={'displayCategoryId' :displayCategoryId, 
										'viewName':'/dms/special/inner/milkPowderCorner',
										'displayId':global['corner'].milk1}
								dms.common.getCategoryCornerItems(param1,function(html) {
									$('#brandInfoCorner').show();
									$("#brandInfoCorner").html(html);
								});
								
								
								//sort 초기화
								special.common.category.orderBy.pageInit(displayCategoryId);
								
								//console.log(displayCategoryId);
								//검색 호출
								var param ={'categoryIdList' :displayCategoryId,'pageSize':80,'pagingYn':'N'}
								dms.search.option.getProduct(param,"");
							}
							
						}).fail(function(xhr, status, error) {
							console.log("code:"+status+"\n"+"error:"+error);
						});
					}
				},
				
				//PC 대 카테고리 상품 목록 조회 : special.common.category.getPcCategoryPrd
				getPcCategoryPrd : function(type,value){
					$('#cornerProduct').hide();
					$('#productList').empty();
					$('#productList').show();
					
					if(value === 'ALL'){
						$('#allOrderByDiv').show();
						$('#cornerOrderByDiv').hide();
						
					}else{
						$('#allOrderByDiv').hide();
						$('#cornerOrderByDiv').hide();
					}
					//검색 파라미터 설정
					var param1 = {'displayCategoryId':value,'categoryViewType':type}
					$.post('/dms/special/categoryProduct',param1, function(html) {
						
						$('#productList').html(html);
					}).fail(function(xhr, status, error) {
						console.log("code:"+status+"\n"+"error:"+error);
					});
				},
				
				//코너 상품 목록 조회 : special.common.category.getCornerPrd
				getCornerPrd : function(displayId){

					if(ccs.common.mobilecheck()){
						$('#moSortBoxList').hide();
						$('#cornerOrderByDiv').show();
					}else{
						$('#moSortBoxList').hide();
						$('#pcSpeCtgList').hide();
						$('#cornerOrderByDiv').show();
						$('#allOrderByDiv').hide();
					}
					$('#productList').hide();
					$('#cornerProduct').show();
					$('#brandInfoCorner').hide();
					
					
					//코너 조회
					if(displayId !== ''){
						var param = { 'sort': 'POPULAR',
								'displayId' :displayId,
								'pagingYn':'Y',
								'pageSize':40,
								'currentPage':1};
						dms.common.getCornerProductList(param,function(html) {
							$("#cornerProduct").html(html);
						//	$('#searchCount').append();
						});
						
						if(ccs.common.mobilecheck()){
							$('#moSortBoxList').hide();
						}
					}
				},
				
				//중카 클릭 이벤트 : special.common.category.getCategoryPrd
				getCategoryPrd : function(id, name){
					
					$(" .miniTabBox1 li").each(function() {
						$(this).find("div a").removeClass("on")
					});
					
					//코너아이템 조회
					if(id !== ''){
						
						param1 ={'displayCategoryId' :id, 
								'viewName':'/dms/special/inner/milkPowderCorner',
								'displayId':global['corner'].milk1}
						dms.common.getCategoryCornerItems(param1,function(html) {
							$('#brandInfoCorner').empty();
							$("#brandInfoCorner").html(html);
						});
						
						//sort 초기화
						special.common.category.orderBy.pageInit(id);
						//검색 호출
						var param ={'categoryIdList' :id, 'pageSize':80,'pagingYn':'N'}
						dms.search.option.getProduct(param,"");
					}
				},
				
				//정렬 : special.common.category.orderBy
				orderBy:{
					
					//Page Laoding : special.common.category.orderBy.pageInit
					pageInit : function(value){
						//order by, pageSize
						if($('#sort').val() === 'SALE_PRICE' && $('#direction').val() ==='ASC'){
							$('#sortSelect').val('LOW_PRICE').prop("selected", true);
						}else{
							$('#sortSelect').val($('#sort').val()).prop("selected", true);
						}
						$('#pageSelect').val($('#pageSize').val()).prop("selected", true);
						
						var ctgId = "";
						if(value !== '' && value != undefined){
							var categoryIds = value.replace(regExp, "");
							ctgId = categoryIds.replace(/(\s*)/g,"");
						}else{
							if($('#categoryIds').val() !=null && $('#categoryIds').val() !==''){
								var categoryIds = $('#categoryIds').val().replace(regExp, "");
								ctgId = categoryIds.replace(/(\s*)/g,"");
							}
						}
						$('#spcCtgSelect').val(ctgId).prop("selected", true);
						$('#categoryIds').val(ctgId);
						
						dms.common.selectStyle();
						
						//정렬 select 이벤트
						$('#sortSelect').off("change").on("change",function(){
							var sort = $(this).val();
							var direction = '';
							
							if(sort == 'LOW_PRICE'){
								sort = 'SALE_PRICE';
								direction = 'ASC';
							}else{
								direction = 'DESC';
							}

							var param = { 'sort': sort,'direction': direction,'pagingYn':'N'};
							dms.search.option.getProduct(param,"");
							
						});

						$('#birthSelect').off("change").on("change",function(){
							var categoryId= $(this).val();
							
							param1 ={'displayCategoryId' :categoryId, 
									'viewName':'/dms/special/inner/milkPowderCorner',
									'displayId':global['corner'].milk1}
							dms.common.getCategoryCornerItems(param1,function(html) {
								$('#brandInfoCorner').empty();
								$("#brandInfoCorner").html(html);
							});
							
							$('#productList').empty();
							var param ={'categoryIdList' :categoryId,'pagingYn':'N'}
							dms.search.option.getProduct(param,"");
						});
					},
					
					//분유관 코너 페이징 이벤트
					cornerPageInit : function(value){
						var sort = '';
						
						if($('#cornerSort').val() != ''){
							var srt = $('#cornerSort').val().split(".");
							sort = srt[1];
						}
						
						$('#cornerSortSelect').val(sort).prop("selected", true);
						$('#cornerPageSelect').val($('#pageSize').val()).prop("selected", true);
						
						dms.common.selectStyle();
						
						//정렬 select 이벤트
						$('#cornerSortSelect').off("change").on("change",function(){
							var sort = $(this).val();
							
							var param = { 'sort': sort,
									'pagingYn':'Y',
									'pageSize':$('#pageSize').val(),
									'displayId':$('#displayId').val(),
									'currentPage':$('#currentPage').val()};
							
							dms.common.getCornerProductList(param,function(html) {
								$("#cornerProduct").html(html);
							});
							
						});

						$('#cornerPageSelect').off("change").on("change",function(){
							var size = $(this).val();
							var param = { 'sort': $('#cornerSort').val(),
									'pagingYn':'Y',
									'pageSize':size,
									'displayId':$('#displayId').val(),
									'currentPage':$('#currentPage').val()};
							dms.common.getCornerProductList(param,function(html) {
								$("#cornerProduct").html(html);
							});
							
						});
					}
					
				},
				cornerCallback : function(html){
					$("#cornerProduct").html(html);
					
				}
				
			}
			
		},
		
		/**********************************************************
		 * 레이어 : special.layer
		 **********************************************************/
		layer : {
			//분유관 노하우Tip layer : special.common.layer.tipLayer
			tipLayer : function(){
				var url = "/dms/special/milkPowderTip/layer";
				ccs.layer.open(url, "", "milkPowderTipLayer");
			}
		},
	
		// 프리미엄 멤버쉽관
		premium : {
			// 프리미엄 멤버쉽관 인증전 페이지 로딩
			getBeforeProductList : function() {
				$.get( '/dms/special//premium/before/content/ajax' ).done(function( res ) {
					$('#beforeProductList').html(res);
				});
			},
			// 페이지이동 설정
			enter : function(isLogin) {
				if(common.isNotEmpty(isLogin)) {
					$.ajax({
						contentType:"application/json; charset=UTF-8",
						url : "/api/dms/special/check/premiumMember",
						type : "POST",
						data : {},
						success : function(res) {
							console.log(res);
							if(res == 'Y') {
								special.premium.init();
							} else {
								alert("프리미엄 멤버십 회원 고객님만 입장 가능합니다.");
							}
						},
						error : function(req, status, err) {
							// TODO
							console.log(req);
							console.log(status);
							console.log(err);
						}
					});
				} else {
					ccs.link.login();
				}
			},
			depthClick : function(elm, depth1, depth2, index, size) {
				
				$('#dept1_Id').val(depth1);
				$('#dept2_Id').val(depth2);
				
				// 모바일시 테마 select
				if(ccs.common.mobilecheck()) {
					$('div.sortBoxList > ul > li').hide();
					if(size > 0) {
						$('.selectBox_'+depth1).show();
					}
					$('.productSort').show();
					$('.listType').show();
				}
				
				// 2depth on clear
				$('.miniTabBox1 > li').find('a').removeClass('on');
				// 2depth img bnr clear
				
				// 2depth click
				if(common.isNotEmpty(depth1) && common.isNotEmpty(depth2)) {
					//$('.depthName').text($(elm)[0].getAttribute('name'));
					$(elm).addClass('on');
					
					// 2depth bnr 제어
					$('div.visual > ul > li').find('img').css('display','none');
					var img = $('.selectMa_'+depth1+'_sub_'+depth2 + ' > img');
					if(img.length > 0) {
						$('div.visual').css('display','block');
						$('.selectMa_'+depth1+'_sub_'+depth2).css('display','block');
						$(img).css('display','block');
					}
					
				} else {
					//$('div.visual').css('display','none');
					$(elm).parent().parent().find('li').removeClass('on');
					$(elm).parent().addClass('on');
					$('.depthName').empty();
					
					// 1depth click
					if(common.isNotEmpty(depth1) && common.isEmpty(depth2)) {
						$('.miniTabBox1').hide();
						$('#subtab'+index).show();
						
						$('.sortBoxList').removeClass('sort_1ea');
						$('.sortBoxList').addClass('sort_2ea');
					} else {
						$('.miniTabBox1').hide();
						$('.sortBoxList').removeClass('sort_2ea');
						$('.sortBoxList').addClass('sort_1ea');
					}
					
					special.premium.loadDepthProduct();
				} 
				
			},
			// MO 구분타이틀 2depth select 할 때
			moSelect : function(depth1, depth2) {
				
				$('.depthName').text($('.selBox_'+depth1+' option:selected').text());
				$('#dept1_Id').val(depth1);
				
				if(common.isEmpty(depth2)) {
					$('#dept2_Id').val("");
				} else {
					$('#dept2_Id').val(depth2);
				}
				
				special.premium.loadDepthProduct();
			},
			// 정렬순 호출
			sortType : function(type) {
				$('#sortType').val(type);
				special.premium.loadDepthProduct(type);
			},
			// 데이터 호출
			loadDepthProduct : function(type, isScroll) {
				
				var depth1 = $('#dept1_Id').val();
				var depth2 = $('#dept2_Id').val();

				if(common.isEmpty(type)) {
					type = $(".productSortType option:selected").val();
				}
								
				var param;
				
				var isMobile = ccs.common.mobilecheck();
				// 모바일 scroll 상품 더보기 페이징
				if (isMobile && isScroll) {
					ccs.common.moLoadingBar('.product_type1');
					$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
					param = {upperDealGroupNo: depth1, dealGroupNo: depth2, sortType: type, currentPage: $("input[name=currentPage]").val()};
				}
				// 모바일 scroll 상품 더보기 초기화
				else {
					$("input[name=currentPage]").val(Number(1));
					param = {upperDealGroupNo: depth1, dealGroupNo: depth2, sortType: type};						
				}
				$.get({
					url : "/dms/special/premium/after/productList/ajax",
					data : param
				}).done(function(response) {
					if(isMobile) {
						if(isScroll) {
							special.premium.pageCallback(response, isScroll);
						} else {
							special.premium.pageCallback(response);	
						}
					} else {
						special.premium.pageCallback(response);						
					}
				});
				
			},
			pageCallback : function(response, isScroll){
				if(ccs.common.mobilecheck()) {
					// 모바일 scroll 상품 더보기
					if(isScroll) {
						$('#moProductArea').append(response);
						ccs.common.moLoadingBar();
					}
					// 새로운 뎁스 진입
					else {
						$('#moProductArea').html(response);
					}
				} else {
					$('#pcConentHere').html(response);
				}
			},
			// 남은 시간 카운트다운
			countdown : function(divName, endDt, type) {
				$("#"+divName).countdown(endDt, function(event) {
					if(type == "day") {
						$(this).text(event.strftime('D- %D'));
					} else {
						$(this).text(event.strftime('%H:%M'));
					}
				});
			}
		
		},
		// 픽업관
		pickup : {
			getAllPickupProductList : function(param1, param2, param3) {
				
				$("#select_brand").val("");
				$("#brand_label").text("브랜드");
				$("#select_brand").attr("disabled",false);
				
				param = {displayType : param1, offshopId : param2, searchKeyword : param3};
				$.ajax({
				  method: "post",
				  url: "/dms/special/pickup/list/ajax",
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				}).done(function( html ) {
					special.pickup.listCallback(html);
					if (ccs.common.mobilecheck()) {
						blockNListView();
						ccs.mainSwipe.calculateHeight();
					}
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
			// SET AJAX RETURN HTML
			listCallback : function(html) {
				$("#categoryPrdList_div").html(html);
			},
			
			
			// 시/도 변경
			changeSido : function(param) {
				
				$("#offshopArea2").prev().text("시/군/구");
				
				var param = {
						offshopArea1 : $(param).val(),
						pickupYn : 'Y'
						};
				
				$.ajax({
					  method: "post",
					  url: "/api/ccs/common/pickup/areadiv2",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( data ) {
						$('#offshopArea2').html('<option>시/군/구</option>');
						for(var i = 0 ; i <data.length ; i++){
							$('#offshopArea2').append("<option value='"+ data[i].areaDiv2 +"'>"+data[i].areaDiv2+"</option>");
						}
					});
			},
			
			// 매장 검색
			searchOffshop : function() {
				var displayType = "";
				var brandId = "";
				$(".tabBox > li").each(function() {
					if ($(this).hasClass("on")) {
						if ($(this).attr("id") == 'all') {
							displayType = $(this).attr("id");
						} else {
							brandId = $(this).attr("id"); 	
						}
					}
				});
				
				var sido = $("#offshopArea1 option:selected").val();
				var sigungu = $("#offshopArea2 option:selected").val();
				var searchKeyword = $("#searchKeyword").val();
				var brandId = $("#brand_label").next().find("option:selected").val();
				if (sido == '시/도') {
					sido = "";
				}
				if (sigungu == '시/군/구') {
					sigungu = "";
				}
				
				var param = {
						brandId : brandId,
						areaDiv1 : sido,
						areaDiv2 : sigungu,
						brandId : brandId,
						searchKeyword : searchKeyword,
						displayType : displayType
				};
				
				$.ajax({
					  method: "post",
					  url: "/dms/special/pickup/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#categoryPrdList_div").html(html);
					});
			},
			
			// 브랜드 클릭 (상품리스트 조회)
			getPickupPrdList : function(brandId) {
				if (brandId == 'all') {
					brandId = "";
				} else {
					$("#select_brand").val(brandId);
					$("#brand_label").text($("#brand_label").next().find("option:selected").text());
					$("#select_brand").attr("disabled",true);
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
						calculateHeight();
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
			}
		},
		// 기프트샵
		giftShop : {
			
			getAllGiftshopProductList : function(param1, param2) {
				
				var isCurrent = false;
				$(".tabBox > li").each(function() {  
					if ($(this).hasClass('on')) { 
						if ($(this).attr("id") == param1) {
							isCurrent = true;
						} 
					}  
				});
				
				if (isCurrent && $("#hidCurrentPage").val() != 0) {
					return;
				}
				
				var ageTypeCds = [];
				var mobileSort = "";
				if ($("#hidAgeTypeCds").val() != '') {
					var obj = JSON.parse($("#hidAgeTypeCds").val());
					ageTypeCds = obj; 
				}
				
				// 검색조건 하나라도 존재시 검색영역 펼침
				if ($("#hidMinPrice").val() != '' || $("#hidMaxPrice").val() !=''
					|| $("#hidGenderTypeCd").val() != '' || ageTypeCds.length > 0) {
					$(".detailOptBox").css("display", "block");
				}
				
				if (ccs.common.mobilecheck()) {
					mobileSort = $("#select_sort option:selected").val();
				}
				
				
				param = {	displayType : param1
						  , currentPage : ""
						  , mobileSort : mobileSort
						  , minPrice : $("#hidMinPrice").val()
						  , maxPrice : $("#hidMaxPrice").val()
						  , ageTypeCds : ageTypeCds
						  , genderTypeCd : $("#hidGenderTypeCd").val()
						  , displayId : ""
						  , themeCd : param2
				};
				common.showLoadingBar();
				$.ajax({
				  method: "post",
				  url: "/dms/special/giftShop/list/ajax",
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				}).done(function( html ) {
					special.giftShop.listCallback(html);
					$("#hidCurrentPage").val($("input[name=currentPage]").val());
					common.hideLoadingBar();
				});
			},
			// SET AJAX RETURN HTML
			listCallback : function(html) {
				if (ccs.common.mobilecheck()) {
					$("#productList_div").append(html);
				} else {
					$("#productList_div").html(html);
				}
				setEvent();
			},
			
			
			// 코너, 테마 탭 변경시
			changeTab : function(param1, param2) {
				var displayId = "";
				var themeCd = "";
				var displayType = param1;
				$("#hidCurrentPage").val(0);

				// 폼 초기화
				$("#giftForm").each(function() {
					this.reset();
				});
				$(".detailOptBox").css("display", "none");
				
				
				if (param1 == 'theme') {
					themeCd = param2;
				} else if (param1 == 'corner'){
					displayId = param2;
				} 
				var param = {
						  displayType : displayType 
						, displayId : displayId 
						, themeCd : themeCd
						, currentPage : $("#hidCurrentPage").val()
				}       
				$("#hidCurrentDisplayId").val(displayId);
				$("#hidCurrentThemeCd").val(themeCd);
				$("#hidCurrentDisplayType").val(displayType);
				$.ajax({
					  method: "post",
					  url: "/dms/special/giftShop/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#productList_div").html(html);
						setEvent();
					});
				
			},
			
			// 상세 검색
			detailSearch : function(deviceType) {
				var genderTypeCd = "";
				var ageTypeCds = [];
				var minPrice;
				var maxPrice;
				// currentPage 초기화
				$("#hidCurrentPage").val(0);
				
				
				if (deviceType == 'pc') {
					// 월령
					$("#PC_AGE_UL> li").children().find('em').each(function() {  
						if ($(this).hasClass("selected")) {   
							ageTypeCds.push($(this).children("input[type=checkbox]").val());    
						}    
					});
					
					// 성별
					genderTypeCd = $("#PC_GENDER_UL > li").children().find(':radio[name="GENDER_TYPE_CD"]:checked').val();
					
					// min, max 가격
					minPrice = $("input[name=pc_minPrice]").val();
					maxPrice = $("input[name=pc_maxPrice]").val();
					
				} else {
					
					$("#MO_AGE_UL> li").children().find('em').each(function() {  
						if ($(this).hasClass("selected")) {   
							ageTypeCds.push($(this).children("input[type=checkbox]").val());    
						}    
					});
					
					$("#MO_GENDER_UL> li").children().find('em').each(function() {  
						if ($(this).hasClass("selected")) {   
							 genderTypeCd = $(this).children("input[type=radio]").val();    
						}    
					});
					
					// min, max 가격
					minPrice = $("input[name=mo_minPrice]").val();
					maxPrice = $("input[name=mo_maxPrice]").val();
				}
				
				if ((minPrice != '' && maxPrice == '') || (minPrice == '' && maxPrice != '')) {
					alert("검색 가격을 입력해주세요.");
					return;
				}
				
				if (minPrice != '' && maxPrice != '') {
					if (minPrice > maxPrice) {
						alert("검색가격을 다시 입력해주세요.");
						return;
					}
				}
				
				var param = {
						  displayType : $("#hidCurrentDisplayType").val() 
						, displayId : $("#hidCurrentDisplayId").val() 
						, themeCd : $("#hidCurrentThemeCd").val()
						, minPrice : minPrice
						, maxPrice : maxPrice
						, genderTypeCd : genderTypeCd
						, ageTypeCds : ageTypeCds
						, currentPage : $("#hidCurrentPage").val()
				}
				
				$("#hidMinPrice").val(minPrice);
				$("#hidMaxPrice").val(maxPrice);
				$("#hidGenderTypeCd").val(genderTypeCd);
				$("#hidAgeTypeCds").val( JSON.stringify(ageTypeCds) );
				
				
				$.ajax({
					  method: "post",
					  url: "/dms/special/giftShop/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#productList_div").html(html);
						setEvent();
					});
				
				
			},
			
			// 모바일 정렬
			changeSort : function(param) {
				
				$("input[name=currentPage]").val("0");
				var data = {
						  displayType : $("#hidCurrentDisplayType").val() 
						, displayId : $("#hidCurrentDisplayId").val() 
						, themeCd : $("#hidCurrentThemeCd").val()
						, mobileSort : param.value
						, currentPage : $("input[name=currentPage]").val()
				}
				
				$.ajax({
					  method: "post",
					  url: "/dms/special/giftShop/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(data) 
					}).done(function( html ) {
						$("#productList_div").html(html);
						setEvent();
					});
				
			},
			
			// 기프트샵 쿠폰 받기
			receiveCoupon : function(couponId) {
				
				var couponIds = [];
				couponIds.push(couponId);
				sps.coupon.issue( couponIds, function(response) {
					var success = 0;
					var msg = "";
			
					for(i = 0 ; i < response.length ; i++){
						if (response[i].resultCode=='0000') {
							if (ccs.common.mobilecheck()) {
								// 쿠폰 받기 완료 레이어
								$(".mobile .sLayer_chance").show();
								$(".mobile .sLayer_chance").height( $(document).height() );
								var base_top = ($(window).height() - $(" > .box", $(".mobile .sLayer_chance")).innerHeight()) / 2;
								$(" > .box", $(".mobile .sLayer_chance")).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
							} else {
								$(".pcLayer").show();
								ccs.layer.fnPopPosition($("#sLayer_chance"));
							}
							
						} else if (response[i].resultCode=='0002') {
							alert("쿠폰을 이미 발급 받으셨습니다. \nGIFT SHOP 선물포장 쿠폰은 매월 ID당 1회만 발행됩니다.");
						} else {
							alert(response[i].resultMsg);
						}
					}
				});
			}
		},
		
		// 임직원관
		employee : {
			depthClick : function(elm, depth1, depth2, index, size) {
				
				$('#dept1_Id').val(depth1);
				$('#dept2_Id').val(depth2);
				
				// 모바일시 테마 select
				if(ccs.common.mobilecheck()) {
					$('div.sortBoxList > ul > li').hide();
					if(size > 0) {
						$('.selectBox_'+depth1).show();
					}
					$('.productSort').show();
					$('.listType').show();
				}
				
				// 2depth on clear
				$('.miniTabBox1 > li').find('a').removeClass('on');
				
				// 2depth click
				if(common.isNotEmpty(depth1) && common.isNotEmpty(depth2)) {
//					$('.miniTabBox1').find('li').removeClass('on');
					$('.depthName').text($(elm)[0].getAttribute('name'));
					$(elm).parent().addClass('on');
				} else {
					// 1depth click
					if(common.isNotEmpty(depth1) && common.isEmpty(depth2)) {
						$('.miniTabBox1').hide();
						$('#subtab'+index).show();
						
						$('.sortBoxList').removeClass('sort_1ea');
						$('.sortBoxList').addClass('sort_2ea');
					} else {
						$('.miniTabBox1').hide();
						$('.sortBoxList').removeClass('sort_2ea');
						$('.sortBoxList').addClass('sort_1ea');
					}
					
					$(elm).parent().parent().find('li').removeClass('on');
					$(elm).parent().addClass('on');
				} 
				
				special.employee.loadDepthProduct();
			},
			// MO 구분타이틀 2depth select 할 때
			moSelect : function(depth1, depth2) {
				
				$('.depthName').text($('.selBox_'+depth1+' option:selected').text());
				$('#dept1_Id').val(depth1);
				
				if(common.isEmpty(depth2)) {
					$('#dept2_Id').val("");
				} else {
					$('#dept2_Id').val(depth2);
				}
				
				special.employee.loadDepthProduct();
			},
			// 정렬순 호출
			sortType : function(type) {
				$('#sortType').val(type);
				special.employee.loadDepthProduct(type, null);
			},
			// 개수 호출
			pageSize : function(pageSize) {
				$('#pageSize').val(pageSize);
				special.employee.loadDepthProduct(null, pageSize);
			},
			detailSearch : function() {
				var age = '';
				var gender = ''
				var prePrice = '';
				var postPrice = '';
				var color = '';
				var material = '';
				var benefit = '';

				$('.optionItem.optDetail.age').find('em[class="selected"] > input').each(function(i,elem) {
					age += '\''+$(elem).val()+'\',';
				});
				$('.optionItem.optDetail.material').find('em[class="selected"] > input').each(function(i,elem) {
					material += '\''+$(elem).val()+'\',';
				});
				$('.optionItem.optDetail.benefit').find('em[class="selected"] > input').each(function(i,elem) {
					benefit += '\''+$(elem).val()+'\',';
				});
				$('.optionItem.optDetail.color').find('.active > span').each(function(i,elem) {
					color += '\''+$(elem).text()+'\',';
				});
				
				age = age.slice(0,-1);
				material = material.slice(0,-1);
				benefit = benefit.slice(0,-1);
				color = color.slice(0,-1);
				
				$('.optionItem.optDetail.gender').find('em[class="selected"] > input').each(function(i,elem) {
					gender = $(elem).val();
				});
				
//				console.log(age);
//				console.log(material);
//				console.log(benefit);
//				console.log(color)
				
				prePrice = $("#prePrice").val();
				postPrice = $("#postPrice").val();
				
				var detailParam = {
						ageTypeCd:age
						, genderTypeCd:gender
						, prePrice:prePrice
						, postPrice:postPrice
						, color:color
						, material:material
						, benefit:benefit
						}
				
				special.employee.loadDepthProduct(null,null,detailParam);
			},
			// 데이터 호출
			loadDepthProduct : function(type, pageSize, detailParam, isScroll) {
				
				var depth1 = $('#dept1_Id').val();
				var depth2 = $('#dept2_Id').val();

				if(common.isEmpty(type)) {
					type = $(".productSortType option:selected").val();
				}
				if(common.isEmpty(pageSize)) {
					pageSize = $(".productPageSize option:selected").val();
				}
				
				var param;
				var isMobile = ccs.common.mobilecheck();
				if (isMobile && isScroll) {
					ccs.common.moLoadingBar('.product_type1');
					$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
					param = {upperDealGroupNo: depth1, dealGroupNo: depth2, sortType: type, pageSize: pageSize, currentPage: $("input[name=currentPage]").val()};
				}
				else {
					$("input[name=currentPage]").val(Number(1));
					param = {upperDealGroupNo: depth1, dealGroupNo: depth2, sortType: type, pageSize: pageSize};
				}

				if(detailParam != undefined) {
					$.extend(param, detailParam);
				}
				
				$.get({
					url : "/dms/special/employee/productList/ajax",
					data : param
				}).done(function(response) {
					if(ccs.common.mobilecheck()) {
						if(isScroll) {
							ccs.common.moLoadingBar();
							special.employee.pageCallback(response, isScroll);
						} else {
							special.employee.pageCallback(response);	
						}
					} else {
						special.employee.pageCallback(response);						
					}
				});
			},
			pageCallback : function(response ,isScroll){

				if(ccs.common.mobilecheck()) {
					// 모바일 scroll 상품 더보기
					if(isScroll) {
						$('#moProductArea').append(response);
					}
					// 새로운 뎁스 진입
					else {
						$('#moProductArea').html(response);
					}
				} else {
					$('#list > .list_group').html(response);
				}
			}
		
		},
		
		// 정기배송관
		subscription : {
			
			init : function() {
			
				if ($("#hidCurrentDisplayType").val() != 'all') {
					
					$(".tabBox > li").each(function() {
						$(this).removeClass("on");
					});
				
					if ($("#hidCurrentDisplayType").val() == 'corner') {
						$("#" + $("#hidCurrentDisplayId").val()).addClass("on");
						special.subscription.changeTab('corner', $("#hidCurrentDisplayId").val(), "", $("#hidCurrentSort").val());
					} else {
						$("#" + $("#hidCurrentDisplayCategoryId").val()).addClass("on");
						special.subscription.changeTab('category', $("#hidCurrentDisplayCategoryId").val(),"", $("#hidCurrentSort").val());
					}
					
				} else {
					special.subscription.changeTab('all')
				}
			},

			// 코너 및 중카테고리 탭 변경시
			changeTab : function(param1, param2, param3, sort) {
				var displayId = "";
				var displayType = param1;
				var displayCategoryId = "";
				var displayCategoryName = "";
				
				if (param1 == 'category') {
					displayCategoryId = param2;
					displayCategoryName = param3;
				} else if (param1 == 'corner'){
					displayId = param2;
				} 
				var param = {
						  displayType : displayType 
						, displayId : displayId 
						, displayCategoryId : displayCategoryId
						, name : displayCategoryName
						, sort : sort
				}       
				$("#hidCurrentDisplayId").val(displayId);
				$("#hidCurrentDisplayCategoryId").val(displayCategoryId);
				$("#hidCurrentDisplayType").val(displayType);
				$("#hidCurrentDisplayCategoryName").val(displayCategoryName);
				$.ajax({
					  method: "post",
					  url: "/dms/special/subscription/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#productList_div").html(html);
						setEvent();
					});
			},
			 
			changeSort : function(param) {
				
				var sort = $(param).val();
				$("#hidCurrentSort").val(sort);
				var param = { 'sort': sort, 'displayType' : $("#hidCurrentDisplayType").val(), 'displayCategoryId' : $("#hidCurrentDisplayCategoryId").val(), 'displayId' : $("#hidCurrentDisplayId").val()};
				$.ajax({
					  method: "post",
					  url: "/dms/special/subscription/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#productList_div").html(html);
						btnListTypeEvt();
					});
				
			}
		}
}