/**
 * 
 */

CONST = {
		//로그인 체크 유형
		LOGIN_CHECK_TYPE_MEMBER : 1,//일반회원로그인체크
		LOGIN_CHECK_TYPE_NON_MEMBER : 2,//비회원로그인체크
		LOGIN_CHECK_TYPE_NONE : 3,//로그인체크불필요
		
		// SSL여부
		SSL : true,
		NO_SSL : false
}

ccs = {
		
	/**
	 * 레이어 팝업 공통
	 */
	layer : {
		// 공통 레이어 열기 ccs.layer.open(url, param, target_pop, callback)
		open : function(url, param, target_pop, callback){
			$.ajax({
				  method: "post",
				  url: url,
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				}).done(function( html ) {
					if(common.isEmpty(html)){
						return;
					}
					
					$('.wrap').append(html);
					
					if (ccs.common.mobilecheck()) {
						ccs.layer.fnLayerOpen($("#"+target_pop));
					} else {
						// 위치잡기
						ccs.layer.fnPopPosition($("#"+target_pop));
					}
					
					// 닫기 이벤트
					$("#"+target_pop+" .pc_btn_close").off("click").on("click", function(e){
						$("#"+target_pop).remove();
						if ($("#deliveryAddressLayer").css("display") == 'none') {
							$("#deliveryAddressLayer").css("display", "block");
						}
					});
					
					$(".mobile .pop_wrap .btn_x").off("click").on({
						"click" : function() {
							// Roy
							$("#"+target_pop).remove();
							if ($("#deliveryAddressLayer").css("display") == 'none') {
								$("#deliveryAddressLayer").css("display", "block");
							} else {
								$(this).closest(".pop_wrap").hide();
								$(".mobile .content").removeClass("content_fixed").show();
								$(".header_mo, .footer, .footer_mo").show();
								$(".mobile").removeAttr("style");
								
							}
						}
					});
					
					if (callback != null && callback != '') {
						callback();
					}
				});
			
		},
		close : function(layerId){
			// Roy
			$("#"+layerId).remove();
			if ($("#deliveryAddressLayer").css("display") == 'none') {
				$("#deliveryAddressLayer").css("display", "block");
			} else {
				$(this).closest(".pop_wrap").hide();
				$(".mobile .content").removeClass("content_fixed").show();
				$(".header_mo, .footer, .footer_mo").show();
				$(".mobile").removeAttr("style");
				
				if (layerId == "interestInfoLayer") {
					ccs.mainSwipe.calculateHeight();
				}
				
			}
		},
		
		//메인 레이아웃에서 쓰인다.
		closeMain : function(layerId){				
			$("#"+layerId).remove();
			if ($("#deliveryAddressLayer").css("display") == 'none') {
				$("#deliveryAddressLayer").css("display", "block");
			} else {
				$(this).closest(".pop_wrap").hide();
				$(".mobile .content").removeClass("content_fixed").show();
				$(".header_mo, .footer, .footer_mo").show();
				$(".mo_mainNavi").show();
				$(".mobile").removeAttr("style");
			}
		},
		
		fnPopPosition : function(target_pop) {
			$(target_pop).show();
			$(target_pop).height( $(document).height() );

			var base_top = ($(window).height() - $(".pop_inner", target_pop).innerHeight()) / 2;
			var target_po = base_top + $(window).scrollTop() + $(".pop_inner", target_pop).innerHeight();
			var target_poMin = 10;
			var target_poMax = base_top + $(window).scrollTop() - (target_po - $(document).height() + 10);
			
			if(target_po > $(document).height()){
				$(".pop_inner", target_pop).css({"marginTop" : target_poMax + "px"});
			}else if(base_top + $(window).scrollTop() < 0){
				$(".pop_inner", target_pop).css({"marginTop" : target_poMin + "px"});
			}else{
				$(".pop_inner", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
			}
		},
		//모바일 레이어 열기
		fnLayerOpen : function(target_tag) {
			if ($("#deliveryAddressLayer").css("display") == 'block') {
				$("#deliveryAddressLayer").hide();
			}
			$(target_tag).show();
			$(".header_mo, .mobile .content, .footer_mo").hide();
			$(window).scrollTop(0);
		},
		
		//메인 레이아웃에서 사용한다.
		fnLayerOpenMain : function(target_tag) {
			$(target_tag).show();
			$(".header_mo, .mo_mainNavi, .mobile .content, .footer_mo").hide();
			$(window).scrollTop(0);
		},
				
		// .content 클래스 안에있는 레이어 열기
		copyLayerToContentChild : function(layerId){
			
			var html = "";
			var main = false;
			//메인의 기획전과 홈의 메인 이미지 레이어를 띄우기 위해 추가
			if(layerId == 'exhibit' || layerId == 'main'){
				
				html =$('#'+layerId+'Layer').html();
				layerId = 'ly_visual';
				main = true;
			}else {
				html =$('.'+layerId).html();
			}
			
			var layerP = '<div class="pop_wrap '+layerId+'" id="'+layerId+'-layer">'+html+'</div>';
			$('.wrap').append(layerP);
			
			if(global.channel.isMobile=='true'){
				//메인은 레이아웃을 다르게 쓰기 때문.
				if(main){
					ccs.layer.fnLayerOpenMain($('#'+layerId+'-layer'));
					$("#"+layerId+"-layer .btn_x").off("click").on({
						"click" : function() {
							ccs.layer.closeMain(layerId+'-layer');
						}
					});
					
				}else{
					ccs.layer.fnLayerOpen($('#'+layerId+'-layer'));
					$("#"+layerId+"-layer .btn_x").off("click").on({
						"click" : function() {
							ccs.layer.close(layerId+'-layer');
						}
					});
				}
				
			}else{
				$(".pc .pop_wrap .pc_btn_close").off("click").on("click", function(e){
					$(".pc .pop_wrap").hide();
				});
				
				ccs.layer.fnPopPosition($('#'+layerId+'-layer'));
			}
			
		},
		/**
		 * 매장픽업 레이어 Object 
		 */
		pickupLayer : {
			
			callback : null, 
			saleproductInfos : {},
			/**
			 * 매장픽업 팝업 열기 ccs.layer.pickupLayer.open(saleproductInfos, callback)
			 * 
			 * @param 단품ID목록, [saleproductInfos]
			 * @callback
			 */
			open : function(saleproductInfos, callback){
				
				var url = "/ccs/common/pickup/layer";
				
				ccs.layer.pickupLayer.saleproductInfos = saleproductInfos;//변수저장
				
				var params = [];
				for(key in saleproductInfos){
					params.push({saleproductId : key, offshopId : saleproductInfos[key].offshopId});
				}
				//console.log(params)
				ccs.layer.open(url, params, "pickupLayer");
				
				ccs.layer.pickupLayer.callback = callback;
			},
			selectAreaDiv1 : function(saleproductId){
				
				// area1 라벨 초기화
				$('#offshopArea1Label_'+saleproductId).html( $('#offshopArea1_'+saleproductId+' option:selected').text());
				
				// area2, shop 라벨 초기화
				$('#offshopArea2Label_'+saleproductId).html("시/군/구");
				$('#offshopIdLabel_'+saleproductId).html("지점");
				
				var offShopIdObj = $('#offshopId_'+saleproductId);
				offShopIdObj.html('<option value="">지점</option>');
				
				var param = {
						saleproductId : saleproductId, 
						offshopArea1 : $('#offshopArea1_'+saleproductId).val(),
						pickupYn : 'Y'
						};
				
				$.ajax({
					  method: "post",
					  url: "/api/ccs/common/pickup/areadiv2",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( data ) {
						$('#offshopArea2_'+saleproductId).html('<option>시/군/구</option>');
						for(var i = 0 ; i <data.length ; i++){
							$('#offshopArea2_'+saleproductId).append("<option value='"+ data[i].areaDiv2 +"'>"+data[i].areaDiv2+"</option>");
						}
					});
				
			},
			selectAreaDiv2 : function(saleproductId){
				var areDiv1Obj = $('#offshopArea1_'+saleproductId);
				var areDiv2Obj = $('#offshopArea2_'+saleproductId);
				
				// 현재 선택값
				areDiv2Obj.prev().html( $('#offshopArea2_'+saleproductId+' option:selected').text());
				
				// 지점 라벨 초기화
				$('#offshopIdLabel_'+saleproductId).html("지점");
				
				
				var param = {saleproductId : saleproductId , 
						offshopArea1 : areDiv1Obj.val(),
						offshopArea2 : areDiv2Obj.val()
						};
				
				$.ajax({
					  method: "post",
					  url: "/api/ccs/common/pickup/shoplist",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( data ) {
						
						var offShopIdObj = $('#offshopId_'+saleproductId);
						
						offShopIdObj.html('<option value="">지점</option>');
						for(var i = 0 ; i <data.length ; i++){
							
							
							var selectedQty = ccs.layer.pickupLayer.saleproductInfos[saleproductId].selectedQty;
							var disabled = selectedQty > data[i].realStockQty ? 'disabled' : '';
							offShopIdObj.append("<option "+disabled+" value='"+ data[i].offshopId +"'>"+data[i].name+"(재고:"+data[i].realStockQty+")</option>");
						}
					});
				
			},
			selectOffShop : function(saleproductId){
				$('#offshopIdLabel_'+saleproductId).html( $('#offshopId_'+saleproductId+' option:selected').text());
			},
			confirm : function(){
				
//				var resultMap = {};
//				$('[id^=offshopId_]').each(function(){
//					resultMap[$(this).data('saleproduct')] = $(this).val();
//					resultMap[$(this).data('saleproduct')+"text"] = $(this).text();
//				});
				
				
				var result = {};
				var notSelected = false;
				$('[id^=offshopId_]').each(function(){
					
					if($(this).val()==''){
						notSelected = true;
					}
					result[$(this).data('saleproduct')] = { offshopId :  $(this).val(), shopName : $(this).text()};
				});
				if(notSelected){
					alert("지점을 선택해 주세요.");
					return;
				}
				ccs.layer.close('pickupLayer');
				ccs.layer.pickupLayer.callback(result);
			},
			cancel : function(obj){
				ccs.layer.close('pickupLayer');
			}
		},
		
		/**
		 * 배송지 레이어 Object
		 */
		deliveryAddressLayer : {
			callback : null,
			/**
			 * 배송지 팝업 열기
			 * 
			 */
			open : function(param, callback){
				
				var url = "/ccs/common/deliveryAddress/layer";
				
				ccs.layer.open(url, "", "deliveryAddressLayer", function() {
					if (callback != null) {
						ccs.layer.deliveryAddressLayer.callback = callback;
						ccs.layer.deliveryAddressLayer.callback(param);
					}
				});
				
			},
			confirm : function(){
				
				ccs.layer.close('deliveryAddressLayer');
				
				ccs.layer.deliveryAddressLayer.callback(resultMap);
			},
			cancel : function(){
				ccs.layer.close('deliveryAddressLayer');
			}
		},
		
		/**
		 * 우편번호 찾기 레이어
		 */
		searchAddressLayer : {
			selectIndex : "",
			callback : null,
			/**
			 * 우편번호 찾기 팝업 열기
			 * 
			 */
			open : function(param, callback){
				
				var url = "/ccs/common/searchAddress/layer";
				
				if ($("#deliveryAddressLayer").css("display") == 'block') {
					$("#deliveryAddressLayer").css("display", "none");
				}
				
				
				ccs.layer.open(url, "", "searchAddressLayer", function() {
					if (callback != null) {
						ccs.layer.searchAddressLayer.callback = callback;
						$(".tabBox a").off("click");
					}
					ccs.layer.searchAddressLayer.reset();
				});
				
			},
			changeTab : function(param) {
				if ("jibun" == param) {
					if ($("#jibun").hasClass("on")) {
						return;
					}

					$("#"+param).addClass("on").siblings("li").removeClass("on");
					$(".pop_content .tab_con").eq(0).addClass("tab_conOn");
					$(".pop_content .tab_con").eq(1).removeClass("tab_conOn");
					ccs.layer.searchAddressLayer.reset();
				} else {
					if ($("#road").hasClass("on")) {
						return;
					}
					$("#"+param).addClass("on").siblings("li").removeClass("on");
					$(".pop_content .tab_con").eq(0).removeClass("tab_conOn");
					$(".pop_content .tab_con").eq(1).addClass("tab_conOn");
					ccs.layer.searchAddressLayer.reset();
				}
				
			},
			
			reset : function() {
				if ($("#jibun").hasClass("on")) {
					$("input[name=jibunSearchKeyword]").val("");
					$(".zip_result").hide();
					$("#jibunResultDiv").hide();
					$("#jibunAddrResult").html("");
				} else {
					$(".zip_result").hide();
					$("#roadResultDiv").hide();
					$("#roadAddrResult").html("");
					$("#districtLabel").text("시/도");
					$("#districtLabel").next("select").val("");
					$("#sigunguLabel").text("시/군/구");
					$("#sigunguLabel").next("select").val("");
					$("input[name=roadSearchKeyword]").val("");
				}
			},
			confirm : function(){
				if ($("input[name=clickAddr2]").val() == '') {
					alert("상세주소를 입력해주세요.");
					return;
				}
				
				var index = ccs.layer.searchAddressLayer.selectIndex;
				$("#zip_result2").show();
				ccs.layer.fnPopPosition($("#searchAddressLayer"));
				
				var strJibunAddr = $("input[name=jibun_"+index+"]").val();
				if ($("input[name=bunji_"+index+"]").val() != '') {
					strJibunAddr += " " + $("input[name=bunji_"+index+"]").val();
				}
				if ($("input[name=ho_"+index+"]").val() != '') {
					strJibunAddr += " - " + $("input[name=ho_"+index+"]").val();
				}
				var strRoadAddr = $("input[name=road_"+index+"]").val();
				if ($("input[name=buildmn_"+index+"]").val() != '') {
					strRoadAddr += " " + $("input[name=buildmn_"+index+"]").val();
				}
				if ($("input[name=buildnm_"+index+"]").val() != '') {
					strRoadAddr += " " + $("input[name=buildnm_"+index+"]").val();
				}
				if ($("input[name=buildsn_"+index+"]").val() != '' && $("input[name=buildsn_"+index+"]").val() != '0') {
					strRoadAddr += " - " + $("input[name=buildsn_"+index+"]").val();
				}
				strJibunAddr +=" " + $("input[name=clickAddr2]").val();
				strRoadAddr +=" " + $("input[name=clickAddr2]").val();
				$("#selectJibunZipCd").text($("input[name=zipCd_"+index+"]").val());
				$("#selectRoadZipCd").text($("input[name=zipCd_"+index+"]").val());
				$("#selectJibunAddr").text(strJibunAddr);
				$("#selectRoadAddr").text(strRoadAddr);
			},
			changeDistrict : function(obj){
				var selectedValue = obj.options[obj.selectedIndex].value;
				var selectedText = obj.options[obj.selectedIndex].text;
				param = {searchStr : selectedValue, searchFlag : 'B' };
				
				$("#districtLabel").text(selectedText);
				$.ajax({
					  method: "post",
					  url: "/api/ccs/common/districtList",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( data ) {
						var sigungu = "";
						for(var i=0; i < data.length; i++) {
							sigungu += ("<option value='"+ data[i].regioncd +"'>"+data[i].regionnm+"</option>");
						}
						$("#sigunguLabel").text("시/군/구");
						$("#sigunguSelect > option").eq(0).attr("selected", "selected");
						$("#sigunguSelect option:not(:first)").remove();
						$("#sigunguSelect").append(sigungu);
						
					});
			},
			changeSigungu : function(obj){
				var selectedValue = obj.options[obj.selectedIndex].value;
				var selectedText = obj.options[obj.selectedIndex].text;
				
				$("#sigunguLabel").text(selectedText);
			},
			search : function() {
				
				var param = {};
				var addrType; 
				$(".tabBox > li").each(function() { 
					if ($(this).hasClass("on")) { 
						addrType = $(this).attr("id");
					}   
				});
				
				var searchResultDiv;
				var searchAddrList;
				if ("jibun" == addrType) {
					var searchKeyword = $("input[name=jibunSearchKeyword]").val();
					
					if (searchKeyword == '') {
						alert("동(읍/면) 이름을 입력해 주세요.");
						return;
					}
					
					param = {addrType : addrType, searchKeyword : searchKeyword};
					// 히든값 세팅
					$("input[name=addrType]").val(addrType);
					$("input[name=searchKeyword]").val(searchKeyword);

					searchResultDiv = "jibunResultDiv";
					searchAddrList = "jibunAddrResult";
				} else if ("road" == addrType) {
					var sidoName = $("#districtLabel").next().find("option:selected").text();
					var sigunguName = $("#sigunguLabel").next().find("option:selected").text();
					var searchKeyword = $("input[name=roadSearchKeyword]").val();
					
					if (sidoName == '' || sidoName == '시/도') {
						alert("시/도를 선택해 주세요.");
						return;
					}
					if (sigunguName == '' || sigunguName == '시/군/구') {
						alert("시/군/구를 선택해 주세요.");
						return;
					}
					if (searchKeyword == '') {
						alert("도로명 또는 건물명을 입력해 주세요.");
						return;
					}
					
					param = {addrType : addrType, sidoName : sidoName, sigunguName : sigunguName, searchKeyword : searchKeyword};
					searchResultDiv = "roadResultDiv";
					searchAddrList = "roadAddrResult";
					// 히든값 세팅
					$("input[name=addrType]").val(addrType);
					$("input[name=searchKeyword]").val(searchKeyword);
					$("input[name=sigunguName]").val(sigunguName);
					$("input[name=sidoName]").val(sidoName);
				}
				
				$.get("/ccs/common/searchAddress/list/ajax", param).done(function(html) {
					$("#"+searchAddrList).html(html);
					$("#"+searchResultDiv).show();
				});
				if (!ccs.common.mobilecheck()) {
					ccs.layer.fnPopPosition($("#searchAddressLayer"));
				}
			},
			
			listCallback : function(html) {
				var addrType; 
				$(".tabBox > li").each(function() { 
					if ($(this).hasClass("on")) { 
						addrType = $(this).attr("id");
					}   
				});
				if ("jibun" == addrType) {
					$("#jibunAddrResult").html(html);
				} else if ("road" == addrType) {
					$("#roadAddrResult").html(html);
				}
			},
			
			clickAddrRow : function(param) {
				$("#addrlist > li").css({"color" : "#666", "background-color" : "transparent"}).find("a").css("color","#666").end()
				.eq(param).css({"color" : "#fff", "background-color" : "#141759"}).find("a").css("color","#fff");
				
				var addrType; 
				$(".tabBox > li").each(function() { 
					if ($(this).hasClass("on")) { 
						addrType = $(this).attr("id");
					}   
				});
				
				var strAddr = "";
				if ("jibun" == addrType) {
					strAddr += $("input[name=jibun_"+param+"]").val();
					if ($("input[name=bunji_"+param+"]").val() != '') {
						strAddr += " " + $("input[name=bunji_"+param+"]").val();
					}
					if ($("input[name=ho_"+param+"]").val() != '' && $("input[name=ho_"+param+"]").val() != '0') {
						strAddr += " - " + $("input[name=ho_"+param+"]").val();
					}
				} else if ("road" == addrType) {
					strAddr += $("input[name=road_"+param+"]").val();
					if ($("input[name=buildmn_"+param+"]").val() != '') {
						strAddr += " " + $("input[name=buildmn_"+param+"]").val();
					}
					if ($("input[name=buildnm_"+param+"]").val() != '') {
						strAddr += " " + $("input[name=buildnm_"+param+"]").val();
					}
					if ($("input[name=buildsn_"+param+"]").val() != '' && $("input[name=buildsn_"+param+"]").val() != '0') {
						strAddr += " - " + $("input[name=buildsn_"+param+"]").val();
					}
				}
				$("input[name=clickAddr1]").val(strAddr);
				$("input[name=clickAddr2]").val("");
				$("input[name=clickZipCd]").val($("#addrlist > li").eq(param).children().find(".col1 > span").text());
				ccs.layer.searchAddressLayer.selectIndex = param;
				$(".zip_result").show();
				$(".btn_wrapC").show();
				if (!ccs.common.mobilecheck()) {
					ccs.layer.fnPopPosition($("#searchAddressLayer"));
				}
			},
			
			selectAddr : function(param) {
				var index = ccs.layer.searchAddressLayer.selectIndex;
				
				var zipCd = $("input[name=zipCd_"+index+"]").val();
				var address1="";
				var address2="";
				if (param == "jibun") {
					address1 += $("input[name=jibun_"+index+"]").val();
					if ($("input[name=bunji_"+index+"]").val() != '') {
						address1 += " " + $("input[name=bunji_"+index+"]").val();
					}
					if ($("input[name=ho_"+index+"]").val() != '' && $("input[name=ho_"+index+"]").val() != '0') {
						address1 += " - " + $("input[name=ho_"+index+"]").val();
					}
				} else {
					address1 += $("input[name=road_"+index+"]").val();
					if ($("input[name=buildmn_"+index+"]").val() != '') {
						address1 += " " + $("input[name=buildmn_"+index+"]").val();
					}
					if ($("input[name=buildnm_"+index+"]").val() != '') {
						address1 += " " + $("input[name=buildnm_"+index+"]").val();
					}
					if ($("input[name=buildsn_"+index+"]").val() != '' && $("input[name=buildsn_"+index+"]").val() != '0') {
						address1 += " - " + $("input[name=buildsn_"+index+"]").val();
					}
				}
				address2 = $("input[name=clickAddr2]").val();
				
				var data = {zipCd : zipCd, address1 : address1, address2 : address2};
				ccs.layer.searchAddressLayer.callback(data);
//				$(window).scrollTop( $("#deliveryAddressLayer .pop_inner").offset().top / 2 );
				ccs.layer.close('searchAddressLayer');
				
			},
			
			// 주소 찾기 더보기
			moreAddressList : function() {
				$("input[name=currentPage]").val(Number($("input[name=currentPage]").val())+ 1);
				param = {addrType : $("input[name=addrType]").val()
						, sidoName : $("input[name=sidoName]").val()
						, sigunguName : $("input[name=sigunguName]").val()
						, searchKeyword : $("input[name=searchKeyword]").val()
						, currentPage : $("input[name=currentPage]").val()};
//				console.log(param);
				var searchAddrList = "";
				if ("jibun" == $("input[name=addrType]").val()) {
					searchAddrList = "jibunAddrResult";
				} else {
					searchAddrList = "roadAddrResult";
				}
				$.get("/ccs/common/searchAddress/list/ajax", param).done(function(html) {
					$("#"+searchAddrList).append(html);
				});
			}
		},
		
		offlineOrderLayer : {
			callback : null,
			/**
			 * 오프라인 구매 팝업 열기
			 * 
			 */
			open : function(param, callback){
				
				var url = "/ccs/common/offlineOrder/layer";
				ccs.layer.open(url, param, "offlineOrder", null);
				
			}
		},
		
		/**
		 * 주문상품 조회 레이어 Object
		 */
		orderProductLayer : {
			callback : null,
			/**
			 * 주문상품 팝업 열기
			 * 
			 */
			open : function(param, callback){
				var url = "/ccs/common/searchOrderProduct/layer";
				
				ccs.layer.open(url, "", "searchOrderProductLayer", function() {
					if (callback != null) {
						//ccs.layer.searchOrderProductLayer.callback(param);
						ccs.layer.orderProductLayer.search();
//						$(".tabBox a").off("click");
					}
				});
				
			},
			
			search : function(){
				var param = {startDate : $('#startDate').val(), endDate : $('#endDate').val()};
				$.get("/ccs/common/orderProduct/list/ajax", param).done(function(html) {
					$("#orderProductResult").html(html);
					$("#orderProductResult").parent().show();
				});
				
				//ccs.layer.fnPopPosition($("#searchOrderProductLayer"));
			},
			
			select : function(orderId, productId, productName, saleproductId, saleproductName){
				custcenter.qna.getOrderProduct(orderId, productId, productName, saleproductId, saleproductName);
				ccs.layer.close('searchOrderProductLayer');
			},
			cancel : function(){
				ccs.layer.close('searchOrderProductLayer');
			}
		},
		
		/**
		 * 당첨자 목록 레이어 Object
		 */
		winnerListLayer : {
			callback : null,
			/**
			 * 당첨자 목록 팝업 열기
			 * 
			 */
			open : function(param, callback){
				
				var url = "/ccs/common/searchWinnerList/layer";
				
				ccs.layer.open(url, {eventId : $('#eventId').val()}, "searchWinnerListLayer", function() {
					//ccs.layer.winnerListLayer.search();
/*					if (callback != null) {
						//ccs.layer.searchOrderProductLayer.callback(param);
//						$(".tabBox a").off("click");
					}
*/				});
				
			},
			
			search : function(){
				var param = {eventId : $('#eventId').val()};
				/*$.get("/ccs/common//winnerList/list/ajax", param).done(function(html) {
					$("#winnerListResult").html(html);
					$("#winnerListResult").parent().show();
				});*/
				
				$.ajax({
					  method: "post",
					  url: "/ccs/common//winnerList/list/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( html ) {
						$("#winnerListResult").html(html);
						$("#winnerListResult").parent().show();
					});
				
				/*ccs.layer.fnPopPosition($("#searchWinnerListLayer"));*/
			},
			cancel : function(){
				ccs.layer.close('searchWinnerListLayer');
			}
		},
		
		/**
		 * 공지사항 팝업 레이어
		 */
		noticePopupLayer : {
			callback : null,
			/**
			 * 공지사항 팝업 열기
			 * 
			 */
			open : function(param, callback){
				
				// 팝업 url 설정
				var url = window.location.href;
				var param = {url : window.location.href};
				
				var cookieStr = url.replace(global.config.domainUrl, '').substr(0, 10);
//				if(url.match(/(ccs\/cs\/notice\/list)/)){
//					url = window.location.href;
//				}else {
//					return;
//				}
				
				// 팝업 url 체크				
				$.ajax({
					  method: "post",
					  url: "/api/ccs/common/popupList",
					  contentType:"application/json; charset=UTF-8",
					  data: JSON.stringify(param) 
					}).done(function( response ) {
						
						
						if (ccs.common.mobilecheck()) {
							// mobile 팝업
							for(i in response){
								var cookieKey = "popupNotice"+response[i].popupNo + "" + cookieStr;
								response[i].cookieKey = cookieKey;
								if(!ccs.layer.noticePopupLayer.checkPoupCookie(cookieKey)){
									var param = {
											cookieKey : cookieKey,
											popupNo : response[i].popupNo,
											title : response[i].title,
											detail2 : response[i].detail2
									}
									ccs.layer.noticePopupLayer.mobile.open(param, response[i].popupNo);
//									window.open("/ccs/common/popup/notice?popupNo=" + response[i].popupNo, response[i].title,'menubar=no, status=no, toolbar=no, location=no');
								}
							}
						} else {
							// pc 경우 좌표 설정 후 생성
							for(i in response){
								var position = response[i].position.split(',');  // top : position[0], left : position[1], width : position[2], height : position[3]
								var cookieKey = "popupNotice"+response[i].popupNo;
								var type = 'top='+position[0]+',left='+position[1]+',width='+position[2]+',height='+position[3]+', menubar=no, status=no, toolbar=no, location=no, directoryies=no';
								if(!ccs.layer.noticePopupLayer.checkPoupCookie(cookieKey)){
									window.open("/ccs/common/popup/notice?popupNo=" + response[i].popupNo + "&cookieKey=" + cookieKey, '팝업공지'+i, type);
								}
							}
						}
					});
			},
			
			setCookie : function(name, value, expiredays){
				console.log("set", name);
				var todayDate = new Date();
				todayDate.setDate(todayDate.getDate() + expiredays);
				document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
			},
			
			closePop : function(param, param1){
				ccs.layer.noticePopupLayer.setCookie(param, param1, 1);
				window.close();
			},
			
			
			checkPoupCookie : function(cookieName){
				var cookie = document.cookie;
				
				// 현재 쿠키가 존재할 경우
				if(cookie.length > 0){
					// 자식창에서 set해준 쿠키명이 존재하는지 검색
					startIndex = cookie.indexOf(cookieName);
					
					
					// 존재한다면
					if(startIndex != -1){
						var expires = new Date();
						var value = ccs.layer.noticePopupLayer.getCookie(cookieName);
//						expires.setDate(expires.getDate() - 1);
//						document.cookie = cookieName + "= " + "; expires=" + expires.toGMTString() + "; path=/";
						return true;
					}else{
						// 쿠키 내에 해당 쿠키가 존재하지 않을 경우
						return false;
					}
				}else{
					// 쿠키 자체가 없을 경우
					return false;
				}
			},
			
			getCookie : function  (name) {
				
				// cookie string
				var cs = document.cookie;
				var prefix = name + "=";
				
				// cookie's Start Index of the information of it.
				var cSI = cs.indexOf(prefix);
				if (cSI == -1) {
					return null;
				}
				
				// Find cookie's End Index of the information of the cookie.
				var cEI = cs.indexOf(";", cSI + prefix.length);
				
				// If it din't find the CEI, then set it to the end of the cs
				if (cEI == -1) {
					cEI = cs.length;
				}
				// Decode the value of the cookie's
				return unescape(cs.substring(cSI + prefix.length, cEI));		
			},
			
			// 모바일 레이어 팝업
			mobile : {
					open : function(param, popupNo){
						var targetPop = "#noticePopupLayer"+popupNo;
						$.ajax({
							  method: "post",
							  url: "/ccs/common/noticePopup/layer",
							  contentType:"application/json; charset=UTF-8",
							  data: JSON.stringify(param) 
							}).done(function( html ) {
								if(common.isEmpty(html)){
									return;
								}
								
								$('.wrap').append(html);
								
								$(".header_mo, .mobile .content, .footer_mo").hide();
								$(window).scrollTop(0);
								
								
//								$(".mobile .pop_wrap .btn_x").off("click").on({
//									"click" : function() {
//										// Roy
//										$(targetPop).remove();
////										if ($("#deliveryAddressLayer").css("display") == 'none') {
////											$("#deliveryAddressLayer").css("display", "block");
////										} else {
////											$(this).closest(".pop_wrap").hide();
////											$(".mobile .content").removeClass("content_fixed").show();
////											$(".header_mo, .footer, .footer_mo").show();
////											$(".mobile").removeAttr("style");
////											
////										}
//									}
//								});
								
//								if (callback != null && callback != '') {
//									callback();
//								}
							});
					}
			}
		},
		
		/**
		 * 상품평 레이어 팝업 
		 */
		reviewLayer : {
			callback : '',
			/**
			 * 상품평 팝업 열기
			 * 
			 */
			open : function(params, callback){
				
				mms.common.loginCheck(function(success){
					
					if(success){
						var url = "/ccs/common/reviewLayer/layer";
						ccs.layer.open(url, params, "reviewLayer", function(callback) {
							
						});
						ccs.layer.reviewLayer.callback = callback;
					}
							
				});

//				등록
//				param = {orderId : orderId, productId : productId, productName : productName, saleproductId : '', saleproductName : ''}
				
//				상세 
//				param = {reviewNo : reviewNo, productId : productId}
				
	
			},
			
			save : function(type){
				if(type == 'insert'){
					if(!confirm("등록하시겠습니까?")){
						return;
					}
				}
				else if(type == 'update'){
					if(!confirm("수정하시겠습니까?")){
						return;
					}
				}
				// reviewForm
				var form1 = $("#reviewForm").serialize();
				var str = decodeURIComponent(form1 + '').replace(/\+/g, ' ');
				var param = ccs.layer.reviewLayer.setReviewRatings(str);
//				var param = {productId : 117, orderId:292920, saleproductId : 111, detail:""};
//				console.log(JSON.stringify(param) );
				/*method: "post",
				  url: url,
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				  */
				$.ajax({
					url : "/api/mms/mypage/review/save",
					method: "post",
					contentType:"application/json; charset=UTF-8",
					data : JSON.stringify(param)
				}).done(function(response) {
					if (response.RESULT == "SUCCESS") {
						if(type == 'insert'){
							alert("정상적으로 등록되었습니다.");
						}
						else if(type == 'update'){
//							mypage.review.reviewSearch();
							alert("정상적으로 수정되었습니다.");
						}
//						window.location.reload();
						ccs.layer.reviewLayer.callback(response.RESULT);
						ccs.layer.close('reviewLayer');
						//common.pageMove('${pageScope.id}',{'pageType' : 'MYQA'},'/mms/mypage/activity/inquiry');
					} else {
						alert(response.MESSAGE);
						if(response.EXIST == "TRUE"){
							ccs.layer.close('reviewLayer');
						}
					}
				});
			},
			
			cancle : function(){
				if(!confirm("취소하시겠습니까?")){
					return;
				}
				ccs.layer.close('reviewLayer');
			},
			// JSON Type 변경
			setReviewRatings : function(param){
				var params = param.split('&');
				//pmsReviewratings
				//var ratingParams = '&pmsReviewratings[]={ratingId : 1}';
				var pmsReviewratings= new Array();
				var jsonArray = new Array();
				var jsonObject = new Object();
				var paramsStr = '';
				if(common.isEmptyObject(params)){
					return param;
				}else{
					for(i in params){
						if(params[i].match(/ratingId_/)){
							var rating = params[i].replace('ratingId_', '').split('=');
							
							//ratingParams += ;
							pmsReviewratings.push({
								ratingId : rating[0], rating : rating[1]
							});
						}else{
							if(i != 0){
								paramsStr += ', ';
							}
							var str = params[i].split('=');

							
							paramsStr += str[0] + ':' + (common.isEmptyObject(str[1])?'\'\'':str[1]);
							
							jsonObject[str[0]] = str[1];
						}
					}
				}
				jsonObject["pmsReviewratings"] = pmsReviewratings;
				return jsonObject;
			}
		},
		
		/**
		 * 회원 배송지 조회
		 */
		memberAddressLayer : {
			callback : '',
			open : function(callback){
				var url = "/ccs/common/memberAddress/layer";
				var params = "";
				common.showLoadingBar();
				ccs.layer.open(url, params, "deliveryAddressList", function() {
					common.hideLoadingBar();
				});
				ccs.layer.memberAddressLayer.callback = callback;
				
//				$("#deliveryAddressList").show();
			},
			close : function(){				
				ccs.layer.close("deliveryAddressList");	
//				모바일 2중 레이어 팝업에 영향을 주어 주석 
//				$(".header_mo, .mobile .content, .footer_mo").show();
			},
			select : function(data){
				ccs.layer.memberAddressLayer.callback(data);				
			},
			addressDelete : function(addressNo) {
				if (confirm("배송지를 삭제하시겠습니까?")) {
					var param = {addressNo : addressNo };
					$.ajax({ 				
						url : "/api/mms/mypage/deliveryAddress/delete",
						type : "POST",		
						data: JSON.stringify(param),
						contentType : "application/json; charset=UTF-8",
						success : function(response) {
							alert("삭제 되었습니다.");
							$("#"+addressNo).remove();
						},
						error : function(response) {
							alert(response.MESSAGE);
						} 
					});
				}
			}
		},
		
		/**
		 * 옵션선택 레이어 Object
		 */
		optionSelectLayer : {
			callback : null,
			
			// 팝업 열기
			open : function(param, callback){
				ccs.layer.open("/pms/product/option/select/ajax", param, "optionSelectLayer");
			}
		},
		
		/**
		 *  옵션선택 레이어 Object
		 */
		optionChangeLayer : {
			
			
			// 팝업 열기
			open : function(param, callback){
				ccs.layer.open("/pms/product/option/change/ajax", param, "optionSelectLayer");		
			}
		},
		
		/**
		 * 상품평 레이어 팝업 
		 */
		mobileAppLayer : {
			callback : null,
			/**
			 * 상품평 팝업 열기
			 * 
			 */
			open : function(){
				var url = "/ccs/common/mobileApp/layer";
				
//				console.log(params);
				ccs.layer.open(url, {}, "mobileAppLayer", function() {
				});
			}
		},
		
		/**
		 * 관심정보 레이어 팝업
		 * 
		 * 쿠키 값 무시하고 강제 호출 
		 * ex) ccs.layer.interestInfoLayer.open(null, true);
		 */
		interestInfoLayer : {
			callback : null,
			
			// 열기_ startIndex : 1,2,3,4
			open : function(startIndex, param) {
				mms.common.isLogin(function(result) {
					if (result == "1") {

						ccs.layer.open("/ccs/common/interestInfo/layer", param, "interestInfoLayer", function() {
							
							if (common.isEmpty(startIndex)) {
								$("#interestInfoLayer .step01").show();
							} else {
								$("#indexForm #hid_startIndex").val(startIndex);
								$("#indexForm #hid_lastIndex").val(4);		// 관심정보 설정에서는 새단장기념 안내 페이지 안 보이기
								
								$("#interestInfoLayer .step0" + startIndex).show();
							}
													
							// 레이어 팝업 위치 조정
							if (!ccs.common.mobilecheck()) {
								ccs.layer.interestInfoLayer.fnPopPosition("#interestInfoLayer .step0" + startIndex);
							}
							
							// 아이생일 년도 selectbox (현재 기준으로 15년 전 ~ 1년 후)
							var yearHtml = "<option>년</option>";
							var currentYear = parseInt($("#currentYear").val());
							yearHtml += "<option value=\"" + (currentYear + 1) + "\">" + (currentYear + 1) + "</option>";
							yearHtml += "<option value=\"" + currentYear + "\">" + currentYear + "</option>";
							for (var i=1; i<16; i++) {
								yearHtml += "<option value=\"" + (currentYear - (i)) + "\">" + (currentYear - (i))  + "</option>";
							}
							$("#birthYearSelect").html(yearHtml);
							$("#birthYearSelect").parent().find("label").text("년");
							
							// 팝업 띄운 후에 카테고리 조회(팝업 뜨는 속도 줄이기 위해)
							$.ajax({
								contentType : "application/json; charset=UTF-8",
								url : "/ccs/common/interest/category",
								type : "POST"
							}).done(function(html) {
						 		$("#interestInfoLayer .step03").html(html);
							});
							
							// 멤버쉽 회원일 경우, 자녀 생일 입력
							if (common.isNotEmpty($("#babyBirth").val())) {
								var birthYear = $("#babyBirth").val().slice(0,4);
								$("#birthYearSelect").parent().find("label").text(birthYear);
								$("#birthYearSelect").val(birthYear).attr("selected", "selected");
								
								var birthMonth = $("#babyBirth").val().slice(5,7);
								$("#birthMonthSelect").parent().find("label").text(birthMonth);
								$("#birthMonthSelect").val(birthMonth).attr("selected", "selected");
								
								var birthDay = $("#babyBirth").val().slice(8,10);
								$("#birthDaySelect").parent().find("label").text(birthMonth);
								$("#birthDaySelect").val(birthDay).attr("selected", "selected");
							}
														
							// 관심매장 selectbox 비활성화
							$("#selectOffshopArea").find(".select_box1 select").attr("disabled", true);
						});
					}
				});	
			},
			
			// 이전 페이지
			prev : function(index) {
				var startIndex = $("[name=hid_startIndex]").val();				
				if (startIndex != null) {
					if (startIndex != index) {
						$("#interestInfoLayer .step0" + index).hide();
						$("#interestInfoLayer .step0" + (index - 1)).show();
					}
				} else {
					$("#interestInfoLayer .step0" + index).hide();
					$("#interestInfoLayer .step0" + (index - 1)).show();
				}
				
				// 위치잡기
				if (!ccs.common.mobilecheck()) {
					var marginPx = ccs.layer.interestInfoLayer.keepStyle();
					$("#interestInfoLayer .step0" + (index - 1)).css({"marginTop" : marginPx + "px"});
				}
			},
			
			// 다음 페이지
			next : function(index) {
				var nextPossible = true;
				
				if (index == 2) {
					nextPossible = ccs.layer.interestInfoLayer.babyInfoValid();
				} else if (index == 3) {
					nextPossible = ccs.layer.interestInfoLayer.interestInfoValid();
				} else if (index == 4) {
					nextPossible = ccs.layer.interestInfoLayer.pushInfoValid();
				}
				
				if (nextPossible) {
					if ($("#indexForm #hid_lastIndex").val() == index) {
						ccs.layer.interestInfoLayer.setComplete();
					} else {						
						$("#interestInfoLayer .step0" + index).hide();
						$("#interestInfoLayer .step0" + (index + 1)).show();
					}
					
					// 위치잡기
					if (!ccs.common.mobilecheck()) {
						var marginPx = ccs.layer.interestInfoLayer.keepStyle();
						$("#interestInfoLayer .step0" + (index + 1)).css({"marginTop" : marginPx + "px"});
					}
				}	
			},
			
			// 관심 카테고리 설정
			chooseCategory : function(index) {
				if ($("#category1_em" + index).hasClass("selected")) {
					$("#category2_div" + index).hide();

					$("#category2_ul").parent().hide();
					
				} else {
					$("#category2_ul").parent().show();
					
					$("#category2_ul div").hide();
					$("#category2_div" + index).show();
				}
			},
			
			// 선택한 지역 매장 조회
			searchOffshop : function() {
				var brand, area1, area2;
				
				if ($("#selectBrand option:selected").text() == "브랜드") {
					brand = "";
				} else {
					brand = $("#selectBrand option:selected").text();
				}
				if ($("#selectArea1 option:selected").text() == "시/도") {
					area1 = "";
				} else {
					area1 = $("#selectArea1 option:selected").text();
				}
				if ($("#selectArea2 option:selected").text() == "시/군/구") {
					area2 = "";
				} else {
					area2 = $("#selectArea2 option:selected").text();
				}
				
				var data = {
					offshopBrand : brand,
					offshopArea1 : area1,
					offshopArea2 : area2
				};
				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/api/ccs/offshop/list",
					type : "POST",
					data : JSON.stringify(data)
				}).done(function(response) {
					if (response != null) {
						var strHtml = "<option value=''>매장</option>";
						for (var i=0; i<response.offshopList.length; i++) {
							strHtml += "<option value='" + response.offshopList[i].offshopId + "'>" + response.offshopList[i].name + "</option>";
						}
						$("#selectOffshop").html(strHtml);
					}
				});
			},
			
			// 관심정보 설정 완료
			setComplete : function() {				
				if (!ccs.layer.interestInfoLayer.pushInfoValid()) {
					return;
				}
				
				// ************** MMS_MEMBER_ZTS
				var mmsMemberZts = {
						babyYnCd : "",
						babyGenderCd : "",
						babyBirthday : "",
						mmsInterestoffshops : [{
							offshopId : ""
						}]
				};
				
				// 아이 정보(여부)
				var babyYn = "";
				for (var i= 0; i <$("#babyYnArea").find("em").length; i++) {
					if ($("#babyYnArea").find("em:eq(" + i + ")").is(".selected")) {
						mmsMemberZts.babyYnCd = "BABY_YN_CD." + $("#babyYnArea").find("em:eq(" + i + ") input").val();
						babyYn = $("#babyYnArea").find("em:eq(" + i + ") input").val();
						break;
					}
				}
				if (babyYn == "Y" || babyYn == "READY") {
					// 아이 정보(성별)
					for (var j=0; j<$("#babyGenderArea").find("em").length; j++) {
						if ($("#babyGenderArea").find("em:eq(" + j + ")").is(".selected")) {
							mmsMemberZts.babyGenderCd = "BABY_GENDER_CD." + $("#babyGenderArea").find("em:eq(" + j + ") input").val();
							break;
						}
					}
					
					// 아이 정보(생일)
					var babyBirthday = "";
					for (var b=0; b<$("#babyBirthdayArea").find("select").length; b++) {
						var birth = $("#babyBirthdayArea").find("select:eq(" + b + ")").parent().find("label").text();
						if ($.isNumeric(birth)) {
							babyBirthday += birth;
						}		
					}
					mmsMemberZts.babyBirthday = babyBirthday + "000000";
				}
				
				// 관심매장
				if ($(".step04 .pop_set_chk em:eq(1)").is(".selected")) {
					mmsMemberZts.mmsInterestoffshops[0].offshopId = $("#selectOffshop option:selected").val();					
				}
				
				// ************** MMS_INTEREST
				var interestInfoList = [];
				
				var interestInfo= {
						interestTypeCd : "",
						displayCategoryId : "",
						brandName : "",
						purposeTypeCd  : "",
						interestProductTypeCd : "",
						mmsMemberZts : {}
				}
				
				// 방문 동기
				for (var p=0; p<$("#purposeArea").find("em").length; p++) {
					var interestPurpose = $.extend({}, interestInfo);
					
					if ($("#purposeArea").find("em:eq(" + p + ")").is(".selected")) {
						interestPurpose.interestTypeCd = "INTEREST_TYPE_CD.PURPOSE";
						interestPurpose.purposeTypeCd = $("#purposeArea").find("em:eq(" + p + ") input").val();
						
						interestInfoList.push(interestPurpose);
					}
				}
				
				// 관심 카테고리
				for (var c=0; c<$("#category2_ul").find("em").length; c++) {
					if ($("#category2_ul").find("em:eq(" + c + ")").is(".selected")) {
						var interestCategory = $.extend({}, interestInfo);
						
						interestCategory.interestTypeCd = "INTEREST_TYPE_CD.CATEGORY";
						interestCategory.displayCategoryId = $("#category2_ul").find("em:eq(" + c + ") input").val();

						interestInfoList.push(interestCategory);
					}
				}
				
				// 쇼핑스타일 및 관심정보(관심상품 유형)
				for (var pr=0; pr<$("#productTypeArea").find(".option_style1").length; pr++) {
					if ($("#productTypeArea").find("em:eq(" + pr + ")").is(".selected")) {						
						if ($("#productTypeArea").find("em:eq(" + pr + ")").siblings("span").text() == "0to7 브랜드") {
							for (var pb=0; pb<$("#brand_ul").find("em").length; pb++) {			
								if ($("#brand_ul").find("em:eq(" + pb + ")").is(".selected")) {
									var interestProductType = $.extend({}, interestInfo);
									
									interestProductType.interestTypeCd = "INTEREST_TYPE_CD.BRAND";
									interestProductType.brandName = $("#brand_ul").find("em:eq(" + pb + ")").siblings("span").text();
									
									interestInfoList.push(interestProductType);
								}
							}
							
						} else {
							var interestProductType = $.extend({}, interestInfo);
							
							interestProductType.interestTypeCd = "INTEREST_TYPE_CD.PRODUCT";
							interestProductType.interestProductTypeCd = $("#productTypeArea").find("em:eq(" + pr + ") input").val();

							interestInfoList.push(interestProductType);
						}
					}
				}
				
				for (var si=0; si<interestInfoList.length; si++) {
					interestInfoList[si].mmsMemberZts = mmsMemberZts;
				}				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/api/mms/member/interest/info/save",
					type : "POST",
					data : JSON.stringify(interestInfoList)
				}).done(function(response) {
					if(response[0] != undefined) {
						if(response[0].resultCode == '0000') {
							// TODO app 설치 구매 유도 쿠폰 _ 문구 정의 필요
							alert(response[0].resultMsg);
						}
					}
					
					if (common.isEmpty($("#indexForm #hid_lastIndex").val())) {
						ccs.layer.interestInfoLayer.next(4);
					} else {
						ccs.layer.close("interestInfoLayer");
					}
					
					common.showLoadingBar();
					window.location.reload(true);
				});
			},
			
			// 아이정보 validation
			babyInfoValid : function() {				
				for (var i= 0; i <$("#babyYnArea").find("em").length; i++) {
					if ($("#babyYnArea").find("em:eq(" + i + ")").is(".selected")) {
						var babyYn = $("#babyYnArea").find("em:eq(" + i + ") input").val();
						
						if (babyYn == "Y" || babyYn == "READY") {
							// 아이 성별
							var count = 0;
							for (var j=0; j<$("#babyGenderArea").find("em").length; j++) {
								if ($("#babyGenderArea").find("em:eq(" + j + ")").is(".selected")) {
									count += 1;
								}
							}
							if (count == 0) {
								alert("아이 성별을 선택해 주세요.");
								return false;
							}
							// 아이 생일
							for (var b=0; b<$("#babyBirthdayArea").find("select").length; b++) {
								var birth = $("#babyBirthdayArea").find("select:eq(" + b + ")").parent().find("label").text();
								if (!$.isNumeric(birth)) {
									alert("아이 월령/연령을 선택해 주세요.");
									return false;
								}
							}
						}
					}
				}

				return true;
			},
			
			// 관심 정보 validation
			interestInfoValid : function() {
				// 관심 카테고리
				for (var c=0; c<$("[name=category1_em]").length; c++) {
					if ($("[name=category1_em]").is(".selected")) {
						var c2Count = 0;
						
						for (var c2=0; c2<$("#category2_ul").find("em").length; c2++) {
							if ($("#category2_ul").find("em:eq(" + c2 + ")").is(".selected")) {
								c2Count += 1;
							}					
						}
						if (c2Count == 0) {
							alert("선택한 카테고리에 속하는 하위 카테고리는 1개 이상 선택해 주세요.");
							return false;
						}
					}
				}
				
				return true;
			},
			
			// 쇼핑스타일 & 관심 매장 validation
			pushInfoValid : function() {
				// 쇼핑스타일 및 관심정보(관심상품 유형)
				for (var pr=0; pr<$("#productTypeArea").find(".option_style1").length; pr++) {
					if ($("#productTypeArea").find("em:eq(" + pr + ")").is(".selected")) {						
						if ($("#productTypeArea").find("em:eq(" + pr + ")").siblings("span").text() == "0to7 브랜드") {	
							var pbCount = 0;
							for (var pb=0; pb<$("#brand_ul").find("em").length; pb++) {			
								if ($("#brand_ul").find("em:eq(" + pb + ")").is(".selected")) {
									pbCount += 1;
								}
							}
							if (pbCount == 0) {
								alert("브랜드는 1개 이상 선택해 주세요.");
								return false;
							}
						}
					}
				}
				
				// 관심매장
				var oCount = 0;
				if ($(".step04 .pop_set_chk em:eq(1)").is(".selected")) {
					for (var o=0; o<$("#selectOffshopArea").find("dd").length; o++) {
						if ($("#selectOffshopArea").find("dd:eq(" + o + ")").parent().find("label").text() != null) {
							oCount += 1;
						}
					}
					
					if (oCount == 0) {
						if (confirm("선택된 매장이 없습니다. 매장 설정을 취소하시겠습니까?")) {
							$(".step04 .pop_set_chk em:eq(0)").removeClass("selected");
						}
					}
					
				}
				
				return true;
			},
			
			// 레이어 팝업 위치 조정
			fnPopPosition : function(target_pop) {
				var base_top = ($(window).height() - $(target_pop  + ' .pop_set_cnt_wrap').innerHeight()) / 2;
				var target_po = base_top + $(window).scrollTop() + $(target_pop + ' .pop_set_cnt_wrap').innerHeight();
				var target_poMin = 10;
				var target_poMax = base_top + $(window).scrollTop() - (target_po - $(document).height() + 10);
				
				if(target_po > $(document).height()){
					$(target_pop).css({"marginTop" : target_poMax + "px"});
				}else if(base_top + $(window).scrollTop() < 0){
					$(target_pop).css({"marginTop" : target_poMin + "px"});
				}else{
					$(target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
				}
			},
			
			// 첫 페이지 marginTop 유지
			keepStyle : function() {
				var startIndex = $("[name=hid_startIndex]").val();
				var marginPx = 0;
				
				if (startIndex != null) {
					marginPx = $("#interestInfoLayer .step0" + startIndex).attr("style").split("margin-top: ")[1].split("px")[0];
				} else {
					marginPx = $("#interestInfoLayer .step01").attr("style").split("margin-top: ")[1].split("px")[0];
				}
				
				return marginPx;
			}
		},
		
		/**
		 * 카드 인증 레이어 Object
		 */
		cardAuthLayer : {
			callback : null,
			open : function(childrencardTypeCd, cardName){
				var param = {childrencardTypeCd : childrencardTypeCd};
//				var param = {cardId = cardId};
				ccs.layer.open("/ccs/common/cardAuth/layer", param, "cardAuthLayer", function() {
					$("#cardTitle").text(cardName);
				});	
			},
			
			cancle : function(){
				ccs.layer.close("cardAuthLayer");
			},
			
			auth : function(){
				
				var memberName = $("#memberName").val();
				if (memberName == "") {
					alert("고객명을 입력해주세요.");
					return;
				}
				
				if ($("#accountNo1").val() == "" || $("#accountNo2").val() == "" 
					|| $("#accountNo3").val() == "" || $("#accountNo4").val() == "") {
					alert("카드번호를 입력해주세요");
					return;
				}
				if ($("#accountNo1").val().length < 4 || $("#accountNo2").val().length < 4 
					|| $("#accountNo3").val().length < 4 || $("#accountNo4").val().length < 4) {
					alert("전체 카드번호를 입력해주세요.");
					return;
				}
				
				var accountNo = $("#accountNo1").val() + $("#accountNo2").val() + $("#accountNo3").val() +$("#accountNo4").val();
				
				var param = {
							accountNo : accountNo,
							childrencardTypeCd : $("#cardAuthForm input[name=childrencardTypeCd]").val(),
							memberName : memberName
				};
				$.ajax({
					url : "/api/dms/special/cardAuth",
					method: "post",
					contentType:"application/json; charset=UTF-8",
					data : JSON.stringify(param)
				}).done(function(response) {
					if (response.RESULT == "SUCCESS") {
						alert("인증 되었습니다.");
						ccs.link.go("/dms/special/multiChildren", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_MEMBER);
					} else {
						alert(response.MESSAGE);
					}
				});
			}
		},
		
		/**
		 * 정기배송 상세 레이어 Object
		 */
		regularDetailLayer : {
			callback : null,
			
			open : function(param){
				
				ccs.layer.open("/ccs/common/regularDetail/layer", param, "regularDetailLayer", function() {
					
				});	
			},
			
			cancle : function(){
				ccs.layer.close("regularDetailLayer");
			}
		}
	},

	common : {
		// 모바일 scroll 
		moLoadingBar : function(target) {
			if(target != undefined) {
				$(target).append(
						'<img id="tempLoadingBar" src="/resources/img/mobile/Loading.gif" height="70px;" width="70px;"' 
						+ 'style="display: block; margin-left: auto; margin-right: auto; padding-top: 100px;"/>');
			} else {
				$('#tempLoadingBar').remove();
			}
		},
		// 수량 감소 : min이 없으면 1
		quantityMinus : function(obj, min){
			var temp_ea = parseInt($(obj).val()) - 1;
			if(!min){
				min = 1;
			}
			if(temp_ea <  min){
				return temp_ea + 1;
			}
			$(obj).val(temp_ea);
			return temp_ea;
		},
		// 수량 증가 : max가 없으면 100
		quantityPlus : function(obj, max){
			var temp_ea = parseInt($(obj).val()) + 1;
			if(!max){
				max = 100;
			}
			if(temp_ea > max){
				return temp_ea - 1;
			}
			$(obj).val(temp_ea);
			return temp_ea;
		},
		// 모바일 여부 체크
		mobilecheck : function() {
			  var check = false;
			  (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
			  return check;
		},
		
		appcheck : function() {
			  var check = false;
			  if (/zerotosevenapp/gi.test(navigator.userAgent)) {
				  check = true;
			  }
			  return check;
		},
		
		
		// 디바이스 OS TYPE
		deviceOsType : function() {
			var currentOS = "";
			var mobile = (/iphone|ipad|ipod|android/i.test(navigator.userAgent.toLowerCase()));
			if (mobile) {
				var userAgent = navigator.userAgent.toLowerCase();
				if (userAgent.search("android") > -1) {
					currentOS = "android";
				} else if ((userAgent.search("iphone") > -1) || (userAgent.search("ipod") > -1)
							|| (userAgent.search("ipad") > -1)) {
					currentOS = "ios";
				}
			} else {
				// 모바일이 아닐 때
				currentOS = "nomobile";
			}
			return currentOS;
		},
		
		// URL 체크 
		// param(value, message, requied)
		urlcheck : function(url, message, requied) {
			var regExp = /^(((http(s?))\:\/\/)?)([0-9a-zA-Z\-]+\.)+[a-zA-Z]{2,6}(\:[0-9]+)?(\/\S*)?$/;
			
			if(requied && common.isEmpty(url)){
				alert(message + '은(는) 필수 입력 항목입니다.');
				return true;
			}else if(common.isNotEmpty(url)){
				if(regExp.test(url)){
					return false;
				}
				
				alert(message + ' 형식이  유효하지 않습니다.');
				return true;
			}
			return false;
		},
		
		// EMAIL 체크
		// param(id, message, requied)
		emailcheck : function(id, message, requied) {
			var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			
			var email1 = $('#' + id + '_email1').val() ? $('#' + id + '_email1').val() : '';
			var email2 = $('#' + id + '_email2').val() ? $('#' + id + '_email2').val() : '';
			
			var email = email1 + '@' + email2;
			
			if(requied && common.isEmpty(email1)){
				alert(message + '은(는) 필수 입력 항목입니다.');
				return true;
			}else if(common.isNotEmpty(email1)){
				if(regExp.test(email)){
					$('#' + id).val(email);
					return false;
				}
				alert(message + ' 형식이  유효하지 않습니다.');				
				return true;
			}
			return false;
		},
		
		// 이메일 도메인 세팅
		emailDomain : function(id) {
			var value = $('#'+id+'_domain').text();
			$('#'+id+'_email2').val(value);
//			console.log(value);
			
			if(value == null){
				$('#'+id+'_email2').val('');
				$('#'+id+'_domain').text('');
			}else{
				if(common.isEmpty($('#'+id+'_domain').text())){
					document.getElementById(id + '_email2').readOnly = false;
				}else{
					document.getElementById(id + '_email2').readOnly = true;
				}
			}
		},
		
		// 전화번호 체크
		// param(id, message, requied)
		telcheck : function(id, message, requied) {
			var regExp = /^(0(1[0|1|6|7|8|9]|2|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|80|130|50[2-7]))([0-9]){7,8}/i;
			
			var regExp2 = /^([0-9]){4}/i;
			
			var type = $('#' + id + '_areaCode')[0].nodeName;
			
			//var areaCode = $('#' + id + '_areaCode').val() ? $('#' + id + '_areaCode').val() : '';
			var areaCode = $('#' + id + '_areaCode').text() ? $('#' + id + '_areaCode').text() : '';
			if(type == "INPUT"){
				areaCode = $('#' + id + '_areaCode').val() ? $('#' + id + '_areaCode').val() : '';
        	}
			var num1 = $('#' + id + '_num1').val() ? $('#' + id + '_num1').val() : '';
			var num2 = $('#' + id + '_num2').val() ? $('#' + id + '_num2').val() : '';
			
			var tel = areaCode + num1 + num2;
			var tel2 = areaCode + '-' + num1 + '-' + num2;
			
			if(requied && common.isEmpty(num1) && common.isEmpty(num2)){
				alert(message + '은(는) 필수 입력 항목입니다.');
				return true;
			}else if(common.isNotEmpty(num1) || common.isNotEmpty(num2)){
				if(regExp.test(tel) && regExp2.test(num2)){
					$('#' + id).val(tel2);
					return false;
				}
				
				alert(message + ' 형식이  유효하지 않습니다.');
				
				return true;
			}
			return false;
		},
		
		// 전화번호 세팅
		telset : function(id, tel) {
//			return;
			var type = $('#' + id + '_areaCode')[0].nodeName;
			if (common.isEmpty(tel)) {
//				$('#' + id + '_areaCode').val('010'); 
//				$('#' + id + '_areaCode').change();
				if(type == "INPUT"){
	        		$('#' + id + '_areaCode').val('');
	        	}else{
	        		$('#' + id + '_areaCode').text(undefined);
	        	}
				$('#' + id + '_num1').val('');
				$('#' + id + '_num2').val('');
				return; 
			}
			
	        var value = $.trim(tel.toString());
	        	value = value.replace(/[^0-9]/g, '');
	        //.replace(/^\+/, '').split('-').join('');
	        var regExp = /^(0(1[0|1|6|7|8|9]|2|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|80|130|50[2-7]))([0-9]){7,8}/i;
	        
	        if (value.match(/[^0-9]/)) {
//				$('#' + id + '_areaCode').val('010'); 
//				$('#' + id + '_areaCode').change();	    
	        	if(type == "INPUT"){
	        		$('#' + id + '_areaCode').val('');
	        	}else{
	        		$('#' + id + '_areaCode').text(undefined);
	        	}
	        	$('#' + id + '_num1').val('');
				$('#' + id + '_num2').val('');
	            return;
	        }
	        
	        if(!regExp.test(value)){
//				$('#' + id + '_areaCode').val('010'); 
//				$('#' + id + '_areaCode').change();
	        	if(type == "INPUT"){
	        		$('#' + id + '_areaCode').val('');
	        	}else{
	        		$('#' + id + '_areaCode').text(undefined);
	        	}
	        	$('#' + id + '_num1').val('');
				$('#' + id + '_num2').val('');
	        	return;
	        }else{
	        	var country, city, number;
	        	
	        	switch (value.length) {
	        	case 1:
	        	case 2:
	        	case 3:
	        		city = value;
	        		break;
	        		
	        	default:
	        		city = value.slice(0, 4);
            		number = value.slice(4);
	        	}
	        	
	        	if(city.slice(0,2).match(/^(02)/)){
	        		number = city.slice(2,4) + value.slice(4);
	        		if(number){
	        			if(number.length>3){
	        				if(number.length > 7){
//	        					$('#' + id + '_areaCode').val(city.slice(0,2)); 
//	        					$('#' + id + '_areaCode').change();
	        					if(type == "INPUT"){
	        		        		$('#' + id + '_areaCode').val(city.slice(0,2));
	        		        	}else{
	        		        		$('#' + id + '_areaCode').text(city.slice(0,2));
	        		        		$('#' + id + '_areaCode').next().val(city.slice(0,2)).attr("selected", "selected");
	        		        	}
	        					$('#' + id + '_num1').val(number.slice(0, 4));
	        					$('#' + id + '_num2').val(number.slice(4, 8));
	        				}
	        				else{
//	        					$('#' + id + '_areaCode').val(city.slice(0,2)); 
//	        					$('#' + id + '_areaCode').change();
	        					if(type == "INPUT"){
	        		        		$('#' + id + '_areaCode').val(city.slice(0,2));
	        		        	}else{
	        		        		$('#' + id + '_areaCode').text(city.slice(0,2));
	        		        		$('#' + id + '_areaCode').next().val(city.slice(0,2)).attr("selected", "selected");
	        		        	}
	        					$('#' + id + '_num1').val(number.slice(0, 3));
	        					$('#' + id + '_num2').val(number.slice(3, 7));
	        				}
	        			}
	        		}
	        	}else if(city.slice(0,3).match(/^(0(1[0|1|6|7|8|9]|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70|80))/)){
	        		number = city.slice(3,4) + value.slice(4);
	        		if(number){
	        			if(number.length>3){
	        				if(number.length > 7){
//	        					$('#' + id + '_areaCode').val(city); 
//	        					$('#' + id + '_areaCode').change();
	        					if(type == "INPUT"){
	        		        		$('#' + id + '_areaCode').val(city.slice(0,3));
	        		        	}else{
	        		        		$('#' + id + '_areaCode').text(city.slice(0,3))
	        		        		$('#' + id + '_areaCode').next().val(city.slice(0,3)).attr("selected", "selected");
	        		        	}
	        					$('#' + id + '_num1').val(number.slice(0, 4));
	        					$('#' + id + '_num2').val(number.slice(4, 8));
	        				}
	        				else{
//	        					$('#' + id + '_areaCode').val(city); 
//	        					$('#' + id + '_areaCode').change();
	        					if(type == "INPUT"){
	        		        		$('#' + id + '_areaCode').val(city.slice(0,3));
	        		        	}else{
	        		        		$('#' + id + '_areaCode').text(city.slice(0,3));
	        		        		$('#' + id + '_areaCode').next().val(city.slice(0,3)).attr("selected", "selected");
	        		        	}
	        					
	        					$('#' + id + '_num1').val(number.slice(0, 3));
	        					$('#' + id + '_num2').val(number.slice(3, 7));
	        				}
	        			}
	        		}
	        	}else if(city.match(/^(0(130|50[2-7]))/)){
	            	if(number){
	            		if(number.length>3){
	        				if(number.length > 7){
//	        					$('#' + id + '_areaCode').val(city); 
//	        					$('#' + id + '_areaCode').change();
	        					if(type == "INPUT"){
	        		        		$('#' + id + '_areaCode').val(city);
	        		        	}else{
	        		        		$('#' + id + '_areaCode').text(city);
	        		        		$('#' + id + '_areaCode').next().val(city).attr("selected", "selected");
	        		        	}
	        					$('#' + id + '_num1').val(number.slice(0, 4));
	        					$('#' + id + '_num2').val(number.slice(4, 8));
	        				}
	        				else{
//	        					$('#' + id + '_areaCode').val(city); 
//	        					$('#' + id + '_areaCode').change();
	        					if(type == "INPUT"){
	        		        		$('#' + id + '_areaCode').val(city);
	        		        	}else{
	        		        		$('#' + id + '_areaCode').text(city);
	        		        		$('#' + id + '_areaCode').next().val(city).attr("selected", "selected");
	        		        	}
	        					$('#' + id + '_num1').val(number.slice(0, 3));
	        					$('#' + id + '_num2').val(number.slice(3, 7));
	        				}
	        			}
	                }
	            }
	        }
		},
		
		// 이메일 세팅
		emailset : function(id, email) {
			
			if (!email) { return; }

	        var value = $.trim(email.toString());
	        var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	        
	        if(!regExp.test(value)){
	        	$('#' + id + '_email1').val('');
	        	return;
	        }else{
	        	var emailStr = value.split('@');
	        	if(emailStr.length == 2){
	        		$('#' + id + '_email1').val(emailStr[0]);
	        		$('#' + id + '_domain').next().val(emailStr[1]).attr("selected", "selected");
	        		$('#' + id + '_email2').val(emailStr[1]);
//					$('#' + id + '_domain').val(emailStr[1]);
//					if(common.isEmpty($('#' + id + '_domain').val())){
//						$('#' + id + '_domain').val('');
//						$('#' + id + '_email2').val(emailStr[1]);
//					}else{
//						$('#' + id + '_domain').change();
//					}
					
					
	        	}
	        }
		},
		
		// 숫자만 입력
		inputNumber : function(event, type) {
			var key = event.keyCode;
			if(type == 'number'){
				if (key == 8 || key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
//					console.log(key);
				}else{
					return false;
				}
			}
//			var key = evt.which?evt.which:event.keyCode;
//			
//			if (key == 8 || key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
//				console.log(key);
//				return true;
//			}else{
//		    	
//		        return false;
//		    }

			//	        return true;
	    },
		
	    // 입력 키 체크
	    fn_press : function(evt, type) {
			
			var key = evt.which?evt.which:event.keyCode;
			
//			key code
//			0~9 => 48 <= key && key <= 57
//			a~z => 65 <= key && key <= 90
			
//			 예외 
//			backspace, delete
//			if(8 == key || 46 == key || 37 == key || 39 == key){
//				
//			}else{
//			}
			
						
//			console.log(key);
//			// 숫자만 가능
			if(type == 'number'){
				if (key == 8 || key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)){
	        		return true;
	        	} else {	        		
	        		var value = event.key.replace(/[^0-9]/g, '');
	        		if (value == '') {
	        			return false;
	        		} 
	        	}

//			// 영어와 숫자만 가능
			}else if(type == 'english'){
//				if ((48 <= key && key <= 57) || (65 <= key && key <= 90)){
//				}else{
//					event.returnValue = false;
//					return false;
//				}
			}
	    },
		
	    fn_press_han : function(obj) {
			obj.value = obj.value.replace(/[\a-zㄱ-하-ㅣ가-힣]/g, '');
	    },
		
		alliance :{
			// 제휴 문의 등록
			insert : function() {
				
				if(!$("input:checkbox[id='agreeChk']").is(":checked")){
					alert("개인 정보 허용을 동의하지 않으셨습니다.");
					return;
				}
				
				if(ccs.common.telcheck('phone', '전화번호', false)){
					return;
				}				
				if(ccs.common.telcheck('managerPhone1', '담당자 연락처', false)){
					return;
				}
				if(ccs.common.emailcheck('managerEmail', 'email', false)){
					return;
				}
				if(ccs.common.urlcheck($('#alliance_homepageUrl').val(), '홈페이지 주소', false)){
					return;
				}
				if(!confirm("등록하시겠습니까?")){
					return;
				}
				
				
				// 표준 카테고리 변수 제어
				if(common.isEmpty($("#alliance_category1_3").val())){
					if(common.isEmpty($("#alliance_category1_2").val())){
						
					}else{
						$("#alliance_category1_1").val('');
					}
				}else{
					$("#alliance_category1_1").val('');
					$("#alliance_category1_2").val('');
				}
				
				if(common.isEmpty($("#alliance_category2_3").val())){
					if(common.isEmpty($("#alliance_category2_2").val())){
						
					}else{
						$("#alliance_category2_1").val('');
					}
				}else{
					$("#alliance_category2_1").val('');
					$("#alliance_category2_2").val('');
				}
				
				var form1 = $("#businessInquiryForm").serialize();
				var str = decodeURIComponent(form1 + '').replace(/\+/g, ' ');
				var param = ccs.common.alliance.setParams(str);
				//console.log($("#businessInquiryForm").serialize());
//				console.log(JSON.stringify(param));
				$.ajax({
					url : "/api/ccs/common/alliance/insert",
					method: "post",
					contentType:"application/json; charset=UTF-8",
					data : JSON.stringify(param)
				}).done(function(response) {
					if (response.RESULT == "SUCCESS") {
						alert("정상적으로 신청되었습니다.");
						ccs.link.common.main();
					} else {
						alert(response.MESSAGE);
					}
				});
			},
			
			// 제휴 문의 취소
			cancle : function() {
				if(!confirm("취소하시겠습니까?")){
					return;
				}
				ccs.link.common.main();
			},

			// JSON Type 변경
			setParams : function(param){
				var params = param.split('&');
				var ccsBusinessinquirycategorys= new Array();
				var jsonArray = new Array();
				var jsonObject = new Object();
				var paramsStr = '';
				if(common.isEmptyObject(params)){
					return param;
				}else{
					for(i in params){
						if(params[i].match(/categoryId_/)){
							var category = params[i].replace('categoryId_', '').split('=');
							if(common.isNotEmpty(category[1])){
								ccsBusinessinquirycategorys.push({
									categoryId : category[1]
								});
							}
						}else{
							if(i != 0){
								paramsStr += ', ';
							}
							var str = params[i].split('=');

							
							paramsStr += str[0] + ':' + (common.isEmptyObject(str[1])?'\'\'':str[1]);
							
							jsonObject[str[0]] = str[1];
						}
					}
				}
				
				if(ccsBusinessinquirycategorys.length == 2){
					if(ccsBusinessinquirycategorys[0].categoryId == ccsBusinessinquirycategorys[1].categoryId){
						ccsBusinessinquirycategorys.splice(0, 1);
					}
				}
				jsonObject["ccsBusinessinquirycategorys"] = ccsBusinessinquirycategorys;
				return jsonObject;
			},
			
			selectorCall : function(){

				$.get( '/ccs/common/alliance/select/ajax', { category1 : $("#alliance_category1_1").val(),  category2 : $("#alliance_category1_2").val(),  category3 : $("#alliance_category1_3").val(),
							category4 : $("#alliance_category2_1").val(),  category5 : $("#alliance_category2_2").val(),  category6 : $("#alliance_category2_3").val()} )
				.done(function( html ) {
					$("#allianceSelectorDiv").html(html);
				});
			},
			
			changeSelector : function(param){
				if(param == '1'){
					$("#alliance_category1_3").val('');
				}
				if(param == '2'){
					$("#alliance_category2_3").val('');
				}
			}
		}, 
		
		// 전화번호 포맷(000-000-000)으로 변경
		phone_format : function (id ,num) { 
//			console.log(num);
//			console.log(num.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3"));
			$("#"+id).text(num.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3")); 
		},
		
		onLoadging : function() {
			var loadingImg = '<div id="loadingBar" class="loading_stopwatch" align="center">';
//			loadingImg += '	<img src="/resources/img/loading_stopwatch.gif" alt="" />';
			loadingImg += '	<img src="/resources/img/mobile/Loading.gif" alt="" />';
			loadingImg += '</div>';
			return loadingImg;
		},
		
		// placeHolder 이벤트
		fnPlaceholder : function(){
			$(".inputTxt_place1 input[type='text']").each(function() {
				if($(this).val().length > 0){
					$(this).parent().prev().hide();
				}else{
					$(this).parent().prev().show();
				}
			});
			
			$(".inputTxt_place1 input[type='text']").off("focus blur").on({
				"focus" : function() {
					$(this).parent().prev().hide();
					$(this).closest(".inputTxt_place1").addClass("place_hover");
				},
				"blur" : function() {
					$(this).closest(".inputTxt_place1").removeClass("place_hover");
					if($(this).val().length > 0){
						$(this).parent().prev().hide();
					}else{
						$(this).parent().prev().show();
					}
				}
			});
		},
		
//		모바일앱 다운 링크발송
		mobileAppSend : {
			callback : '',
			/**
			 * 모바일앱 링크발송
			 * 
			 */
			send : function(phone, temp){
				var param = {phone : phone};
				
				if(common.isEmpty(phone)){
					alert("번호를 입력해주세요.");
					return;
				}
				ccs.common.mobileAppSend.callback = temp;
				
				$.get("/api/ccs/common/mobileApp/send/"+phone, null)
				.done(function(response) {
					if (response.RESULT == "SUCCESS") {
						alert("모바일 앱 SMS 발송되었습니다.");
					} else {
						alert('모바일 앱 SMS 발송중 오류가 발생했습니다.');
					}
					ccs.common.mobileAppSend.callback(response.RESULT);
				});
			}
		},
		
//		공통 모바일앱 다운 링크발송 
		mobileSend : function(){
			var phoneNumber = $('#phoneNumber').val();
			var param = {phone : phoneNumber};
			
			ccs.common.mobileAppSend.send($('#phoneNumber').val(), null);
		}
	},
	// 페이지링크 ( ccs.link )
	link : {
		paramSet : function(param){// 파라메터 자동 생성
			var parmaString="";
			if(common.isEmptyObject(param)){
				return "";
			}
			
			for(key in param){
				if(common.isEmpty(parmaString)){
					parmaString+="?"+key+"="+param[key];
				}else{
					parmaString+="&"+key+"="+param[key];
				}
			}
			return parmaString;
		},
		/**
		 * 
		 * 페이지 이동
		 * 
		 * @param url 호출할 url
		 * @param isSsl ssl 페이지 여부
		 * @param loginCheckType 
		 * 				LOGIN_CHECK_TYPE_NON_MEMBER : 비회원로그인체크
		 *			 	LOGIN_CHECK_TYPE_MEMBER : 일반회원 로그인 체크
		 *				LOGIN_CHECK_TYPE_NONE : 로그인체크 안함
		 */
		go : function(url, isSsl, loginType){

			if(loginType==CONST.LOGIN_CHECK_TYPE_MEMBER || loginType==CONST.LOGIN_CHECK_TYPE_NON_MEMBER){
				
				// 로그인 체크 필요한 url
				
				mms.common.isLogin(function(result){
					
					var success = false;
					if(loginType==CONST.LOGIN_CHECK_TYPE_MEMBER && result=='1'){
						//로그인 체크 성공
						success = true;
					}else if(loginType==CONST.LOGIN_CHECK_TYPE_NON_MEMBER && result=='2'){
						//비회원 로그인 체크 성공
						success = true;
					}else{
						success = false;
					}
					var returnUrl;
					if(isSsl){
						returnUrl=global.config.domainSslUrl+url;
					}else{
						returnUrl=global.config.domainUrl+url;
					}
					
					if(success){
						location.href=returnUrl;
					}else{
						ccs.link.login({returnUrl:returnUrl});
						return;
					}
					return;
				})
				
			}else{
				// 로그인 체크 필요없는 url
				var returnUrl;
				
				if(isSsl){
					returnUrl=global.config.domainSslUrl+url;
				}else{
					returnUrl=global.config.domainUrl+url;
				}
				
				location.href=returnUrl;
			}
			
		},
		
		common : {
			//메인페이지
			main : function(){
				ccs.link.go("/ccs/common/main", CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);
			}
		},
		// 
		skyScraper : {
			isLogin : function(isMember) {
				if(!isMember) {
					ccs.link.login();
				} 
			}
		},
		//마이페이지
		mypage : {
			main : function(){
				ccs.link.go('/mms/mypage/main', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
			},
			// 주문관리
			order : {
				// 선물함 관리
				gift : function() {
					ccs.link.go('/mms/mypage/order/gift', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 오프라인 구매내역
				offline : function() {
					ccs.link.go('/mms/mypage/order/offline', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 주문 /배송 조회 
				delivery : function() {
					ccs.link.go('/mms/mypage/order/history', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				}
			},
			// 활동관리
			activity : {
				// MO 히스토리
				history : function() {
					ccs.link.go('/mms/mypage/activity/history', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 상품평
				review : function() {
					ccs.link.go('/mms/mypage/activity/review', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 나의 문의
				inquiry : function(type) {
					// 1. 1:1 문의 = MYQA, 2. 상품QnA = PRODUCT
					ccs.link.go('/mms/mypage/activity/inquiry?pageType='+type, CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				event : function() {
					ccs.link.go('/mms/mypage/activity/event', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 쇼핑찜
				wishlist : function() {
					ccs.link.go('/mms/mypage/activity/wishlist', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 최근 본 상품
				latestProduct : function() {
					ccs.link.go('/mms/mypage/activity/latestProduct', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 스타일 관리
				style : function() {
					ccs.link.go('/mms/mypage/activity/style', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 의류 AS현황
				as : function() {
					ccs.link.go('/mms/mypage/activity/clothesAs', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				}
			},
			// 혜택관리
			benefit : {
				// 멤버십 혜택
				membership : function() {
					ccs.link.go('/sps/event/benefit', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 당근
				carrot : function() {
					ccs.link.go('/mms/mypage/benefit/carrot', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 쿠폰
				coupon : function() {
					ccs.link.go('/mms/mypage/benefit/coupon', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 예치금
				deposit : function() {
					ccs.link.go('/mms/mypage/benefit/deposit', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				}
			},
			// 정보관리
			info : {
				// 환불 계좌 관리
				refund : function() {
					ccs.link.go('/mms/mypage/info/refund', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 배송지 관리
				deliveryAddress : function() {
					ccs.link.go('/mms/mypage/info/deliveryAddress', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 영수증 조회
				receipt : function() {
					ccs.link.go('/mms/mypage/info/receipt', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 관심매장 설정
				offshop : function() {
					ccs.link.go('/mms/mypage/info/offshop', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 관심정보 설정 ( 미구현 )
				information : function() {
					ccs.link.go('/mms/mypage/info/', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 회원정보 수정
				edit : function() {
					ccs.link.go('/mms/mypage/info/edit', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				},
				// 마이메뉴
				mymenu : function() {
					ccs.link.go('/mms/mypage/info/myMenu', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				}
			}
		},
		//고객센터
		custcenter : {
			main :function(){
				ccs.link.go("/ccs/cs/main", CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);
			},
			
			qna : {
				go : function(){	// FAQ ccs.link.custcenter.qna.go("FAQ_TYPE_CD.ORDER")
					ccs.link.go("/ccs/cs/qna/insert", CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
				}
			},
			// 비회원 주문조회
			nonMemberOrder : function(){// ccs.link.custcenter.nonMemberOrder()
				ccs.link.go("/ccs/guest/order", CONST.SSL, CONST.LOGIN_CHECK_TYPE_NON_MEMBER);
			}
		},
		//전시
		display : {
			//판매장 매장
			sellerShop : function(businessId){//ccs.link.display.sellerShop(businessId)
				if(businessId !==null && businessId !==''){
					ccs.link.go("/dms/display/sellerShop?businessId="+businessId, CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);
				}
			},
			//카테고리 매장
			dispTemplate : function(dispCategoryId){//ccs.link.display.dispTemplate(dispCategoryId);
				if(dispCategoryId !==null && dispCategoryId !==''){
					ccs.link.go("/dms/common/templateDisplay?dispCategoryId="+dispCategoryId, CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);
				}
			},
			//쇼킹제로
			shockingzero: function(){
				ccs.link.go("/sps/deal/shockingzero/main", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			//배스트샵
			bestShop:function(){
				ccs.link.go("/dms/display/bestShop", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			//기획전
			exhibit : function(){
				ccs.link.go("/dms/exhibit/main", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			//스타일
			style : function(){
				ccs.link.go("/dms/display/styleMain", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			// 샵온
			shopOn : function(){
				ccs.link.go("/dms/display/shopOn", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			// 이벤트&쿠폰
			event : function(){
				ccs.link.go("/sps/event/main", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			// 모바일 앱
			mobileApp : function(){
				ccs.link.go("/sps/event/mobileApp", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			}
		},
		//주문
		order : {
			//장바구니
			cart : function(cartTypeCd){
				if(cartTypeCd != 'CART_TYPE_CD.GENERAL'){
					//TODO LOGIN CHECK
				}
				common.showLoadingBar();
				ccs.link.go("/oms/cart/list?cartTypeCd="+cartTypeCd, CONST.NO_SSL);
			}
		},
		//상품
		product : { 
			detail : function (productId, keyword, styleNo){// 상품상세 ccs.link.product.detail({productId : '',keyword:''})
				
				var parameter  = "productId="+productId;
				if(keyword){
					parameter +="&keyword="+keyword;
				}
				if(styleNo){
					parameter +="&styleNo="+styleNo;
				}
				
				ccs.link.go("/pms/product/detail?"+parameter, CONST.NO_SSL);
			},
			
			brandProdutList : function(type,brandId,dispCategoryId,rootCategoryId){
				var parameter  = "type="+type+"&brandId="+brandId+"&dispCategoryId="+dispCategoryId+"&rootCategoryId="+rootCategoryId;
				ccs.link.go("/pms/brand/product?"+parameter, CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);
			}
		},
		//이벤트
		event : {
			
		},
		// 전문관 ccs.link.special
		special : {
			premium : function() {
				ccs.link.go("/dms/special/premium", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			giftShop : function() {
				ccs.link.go("/dms/special/giftShop", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			employee : function() {
				ccs.link.go("/dms/special/employee", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_MEMBER);
			},
			milkPowder : function(){
				ccs.link.go("/dms/special/milkPowder", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			birthready : function(){
				ccs.link.go("/dms/special/birthready", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			subscription : function(){
				ccs.link.go("/dms/special/subscription", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			pickup : function(){
				ccs.link.go("/dms/special/pickup", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			multiChildrenInfo : function(){
				ccs.link.go("/dms/special/multiChildrenInfo", CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			},
			url : function(url){
				ccs.link.go(url, CONST.NO_SSL , CONST.LOGIN_CHECK_TYPE_NONE);
			}
			//
			
			
		},
		// 로그인 팝업 띄우기
		/**
		 *  로그인 팝업 띄우기
		 *  param : {returnUrl : returnUrl, type : type, isReload : isReload}
		 *  	returnUrl : 로그인 성공 후 호출되는 URL
		 *  	type : 주문 로그인일 경우 "ORDER", default NULL
		 *  	isReload : false이면 redirect/reload 하지 않음, returnUrl 값 무시됨, default true
		 *      callbackFn : isReload가 N일경우 콜백 함수(필수아님)
		 */
		login : function(param){// ccs.link.login()
			
			var returnUrl = "";
			var type;
			var isReload = true;
			
			if(param && param.returnUrl){
				 returnUrl = param.returnUrl;
			}
			if(param && param.type){
				type = param.type;
			}
			if(param && param.isReload=="N"){
				isReload = false;
			}
			// isReload 이 true면 화면 reroad
			if(isReload==true){
				mms.LOGINCALLBACKFN = null;
			}else{
				
				// isReload == false 리면 바닥화면 refresh하지 않고 팝업닫고 callbackFn 호출
				if(param.callbackFn){
					mms.LOGINCALLBACKFN = param.callbackFn;
				}else{
					mms.LOGINCALLBACKFN = function(){};
				}
				
			}
			// 모바일은 returnUrl 필수
			if(global.channel.isMobile=="true" && common.isEmpty(returnUrl)){
				returnUrl = document.location.href;
				
				//POST일경우 메인이동.
				if(returnUrl.indexOf("/oms/order/sheet") > -1){
					returnUrl = "/ccs/common/main";
				}
			}
			var callbackUrl=global.config.domainUrl+"/mms/login/callback";
			
			if(common.isNotEmpty(type)){
				callbackUrl+="?type="+type+"&url="+returnUrl;
			}else{
				callbackUrl+="?url="+returnUrl;
			}
			
			
			var chnlCd = "3";//pc
			
			if(global.channel.isApp=='true'){
				chnlCd = "42";// app
			}else if(global.channel.isMobile=='true'){
				chnlCd = "41";// mobile web
			}
			var membershipUrl = global.config.membershipUrl+"?chnlCd="+chnlCd+"&ntryPath="+global.member.coopcoCd+"&coopcoCd="+global.member.coopcoCd;
			
			if(type == "ORDER"){
				membershipUrl += "&orderReturnUrl="+global.config.domainUrl+"/oms/order/login/return";
			}
			
			membershipUrl += "&returnUrl="+callbackUrl;
			
			// 모바일이면 페이지 이동
			if(chnlCd!='3'){

				location.href=membershipUrl;
				
			}else{
				
				var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540";
				
				window.open(membershipUrl,"login",status);
			}
			
			
			
		},
		// 로그아웃 처리
		logout : function(){// ccs.link.logout()
			
			// sso 로그아웃
			//location.href=global.config.logoutUrl+"?returnUrl="+global.config.domainUrl;
			
			$.ajax({
				  method: "get",
				  url: "/api/mms/login/logout/ajax"
				}).done(function( data ) {
					//alert("로그아웃!");
					
					location.href=global.config.logoutUrl+"?returnUrl="+global.config.domainUrl;
					//ccs.link.common.main();
				});
		},
		// 매장
		offshop : function(brandName) {
			ccs.link.go("/ccs/offshop/search?brandName="+brandName, CONST.NO_SSL);
		},
		// 새창열기
		openWindow : function(productId){
			var newWindow = window.open("about:blank");
			newWindow.location.href = global.config.domainUrl+"/pms/product/detail?productId="+productId;
			//ccs.go_url(global.config.domainUrl+"/pms/product/detail?productId="+productId, "상품상세");
		}
	},
	
	// url 링크
	go_url : function(url, name){
		window.open(url, name);
	},
	
	sns : {
		// 공유
		share : function(type) {
			var img = $("meta[property='og:image']").attr("content");
			var url = $("meta[property='og:url']").attr("content");
			var text = $("meta[property='og:title']").attr("content");
			switch (type) {
			case 'link' : 
				if (ccs.common.appcheck()) {
// 				APP에서 링크 
				var item = {
				        "text": url
				};
				var method = "clipboardcopy";
				var json = encodeURIComponent(JSON.stringify(item));
				var applink = "app://" + method + "?json=" + json;
				window.location.href = applink;
					
				} else {
					var IE=(document.all)?true:false;
					if (IE) {
						window.clipboardData.setData("Text", url);
					} else {
						if (ccs.common.mobilecheck()) {
							temp = prompt("URL을 복사하세요", url);
						} else {
							temp = prompt("Ctrl+C를 눌러 클립보드로 복사하세요", url);
						}
					}
				}
				break;
			case 'kakaoLink' :
				      Kakao.Link.sendTalkLink({
				        label: text,
				        image: {
				            src: img,
				            width: '300',
				            height: '200'
				          },
				          webLink: {  
								text:'웹으로 연결',
								url:url
							  },
						  appButton: {
							    text: '앱으로 연결',
							    execParams:{iphone:{UrlLink:url},android:{UrlLink:url}}
							  }
				      });
				break;
			case 'kakaoStory' :
				Kakao.Story.share({
					url: url,
					text: text
			      });
				break;
			case 'facebook' :
				window.open("http://www.facebook.com/sharer.php?u="+encodeURIComponent(url) , "FaceBook", "height=500, width=620, scrollbars=yes");
				break;
			case 'twitter' :
				window.open("https://twitter.com/intent/tweet?url=" + encodeURIComponent(url) + "&text=" + encodeURIComponent(text), "twitter", "height=500, width=620, scrollbars=yes");
				break;
			case 'sms' :
				// 문자 발송
				var applink = "";
				var deviceOsType = ccs.common.deviceOsType();
				if (deviceOsType == 'android') {
					applink = "sms:?body=" + encodeURIComponent(url);
				} else if (deviceOsType == 'ios') {
					applink = "sms:&body=" + encodeURIComponent(url);
				}
				window.location.href = applink;
				
				break;
			case 'blog' :
//				window.open("http://share.naver.com/web/shareView.nhn?url="+encodeURIComponent(url)+"&title="+encodeURIComponent(text));
				window.open("http://blog.naver.com/openapi/share?url="+encodeURIComponent(url)+"&title="+encodeURIComponent(text), "Blog", "height=500, width=620, scrollbars=yes");
				break;
			}
		}
	},
	
	
	/********************************************************
	 * 매장 찾기
	 ********************************************************/
	offshop : {
		
		// 매장 찾기 init
		setCurrentPosition : function(cntLimit, pickupYn, brandName) {
			if (common.isEmpty(cntLimit)){
				cntLimit = null;
			}
			if (common.isEmpty(pickupYn)) {
				pickupYn = null;
			}
			if (common.isEmpty(brandName)) {
				brandName = null;
			}
			
			if (ccs.common.mobilecheck()) {
				if (navigator.geolocation) {
			        navigator.geolocation.getCurrentPosition(success, error);
			    } else {
			        alert("not support geolocation");
			    }
				
				function success(position) {
					var latitude = position.coords.latitude;
					var longitude = position.coords.longitude;
					
					var data = {"latitude" : latitude, "longitude" : longitude};
					ccs.offshop.makeSearchParam(data, cntLimit, pickupYn, brandName);
				}
				
				function error(positionError) {
					console.warn('ERROR(' + positionError.code + '): ' + positionError.message);
					
					var data = {"defaultYn" : "Y"};
					ccs.offshop.makeSearchParam(data, cntLimit, pickupYn);
				}
			} else {
				var data = {"latitude" : null, "longitude" : null};
				ccs.offshop.makeSearchParam(data, cntLimit, pickupYn, brandName);
			}
		},
		
		// 매장 검색 조건 판별
		makeSearchParam : function(data, cntLimit, pickupYn, brandName) {
			var brand, area1, area2;
			
			if (common.isNotEmpty(brandName)) {
				brand = brandName;
				$("#selectBrand option:selected").text(brandName).attr("selected", "selected");
				$("#selectBrandLabel").text(brandName);
			} else {
				if ($("#selectBrand option:selected").text() == "브랜드") {
					brand = "";
				} else {
					brand = $("#selectBrand option:selected").text();
				}
			}
			if ($("#selectArea1 option:selected").text() == "시/도") {
				area1 = "";
			} else {
				area1 = $("#selectArea1 option:selected").text();
			}
			if ($("#selectArea2 option:selected").text() == "시/군/구") {
				area2 = "";
			} else {
				area2 = $("#selectArea2 option:selected").text();
			}
			
			var sCondition = {
				offshopBrand : brand,
				offshopArea1 : area1,
				offshopArea2 : area2,
				searchKeyword : $("#searcyKeyword").text(),
				latitude : data.latitude,
				longitude : data.longitude,
				cntLimit : cntLimit,
				pickupYn : pickupYn,
				defaultYn : ""
			};
			
			if (data.defaultYn == "Y") {
				if (common.isNotEmpty(area1) && common.isNotEmpty(area2)) {
					sCondition.defaultYn = "N";
				} else {
					sCondition.defaultYn = "Y";
				}
			}
			
			ccs.offshop.searchOffshop(sCondition);
		},
		
		// 매장 검색
		searchOffshop : function(data) {
			console.log(data);
			//common.showLoadingBar();
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/ccs/offshop/list/ajax",
				type : "POST",
				data : JSON.stringify(data)
			}).done(function(html) {
				ccs.offshop.makeSearchData(html);
				//common.hideLoadingBar();
			});
		},
		
		// SET AJAX RETURN HTML
		makeSearchData : function(html) {
			$("#offshopInfoArea").html(html);
			ccs.offshop.getOffshopDetail(0);
		},
		
		// 매장 수
		setTotalCount : function() {
			$("#offshopCount").text("(" + $("#hidOffshopTotalCnt").val() + ")");
		},
		
		// 매장 지도
		roadMap : function(divId, latitude, longitude) {
			common.map(divId, latitude, longitude);
		},
		
		// 시/군/구
		getArea2List : function() {
			var	brandId = $("#selectBrand option:selected").val();
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/ccs/offshop/area2/search",
				type : "POST",
				data : JSON.stringify({brandId: brandId, offshopArea1 : $("#selectArea1 option:selected").text()})
			}).done(function(response) {
				if (response.area2List.length > 0) {
	 				var strHtml = "<option>시/군/구</option>";
	 				for (var i=0; i<response.area2List.length; i++) {
	 					strHtml += "<option>" + response.area2List[i].areaDiv2 + "</option>";
	 				}
	 				$("#selectArea2").html(strHtml);
	 			}
			});
		},
		
		// 브랜드별 매장 보기
		getOffshopByBrand : function(brandId) {
			for (var i=0; i<$("#selectBrand").find("option").length; i++) {
				var value = $("#selectBrand").find("option:eq(" + i + ")").val();
				if (common.isNotEmpty(brandId)) {
					if (value == brandId) {
						$("#selectBrandLabel").text($("#selectBrand").find("option:eq(" + i + ")").text());
						$("#selectBrand option:eq(" + i + ")").attr("selected", "selected");
					}
				} else {
					$("#selectBrandLabel").text("브랜드");
					$("#selectBrand option:eq(0)").attr("selected", "selected");
				}
				
			}
			ccs.offshop.setCurrentPosition();		
		},
		
		// 매장 상세정보
		getOffshopDetail : function(liIndex) {
			if (ccs.common.mobilecheck()) {
				if ($("#li_" + liIndex).hasClass("on")) {
					$("#li_" + liIndex).removeClass("on");
					$("#detailDiv" + liIndex).hide();
				} else {
					$("#li_" + liIndex).addClass("on");
					$("#detailDiv" + liIndex).show();
				}				
			} else {
				$("#ul_offshop").find("li").removeClass("on");
				$("#li_" + liIndex).addClass("on");

				$("#detailDiv" + liIndex).show();
			}
			
		},
		
		// 관심매장 저장/해제_ 판별
		saveInterestOffshop : function(thisBtn, offshopId) {
			mms.common.isLogin(function(result) {
				if (result == "1") {
					if ($(thisBtn).hasClass("checked")) {
						if (confirm("관심 매장에서 해제하시겠습니까?")) {
							ccs.offshop.deleteInterestOffshop(thisBtn, offshopId);
						}			
					} else {
						if (confirm("관심 매장으로 등록하시겠습니까?")) {
							ccs.offshop.insertInterestOffshop(thisBtn, offshopId);
						}
					}
				} else {
					if (confirm("로그인이 필요한 서비스입니다.\n로그인 하시겠습니까?")) {
						ccs.link.login({returnUrl:"/ccs/offshop/search"});
					}
				}
			});
		},
		
		// 관심매장 저장
		insertInterestOffshop : function(thisBtn, offshopId) {
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/ccs/offshop/update/interest",
				type : "POST",
				data : JSON.stringify([{"offshopId" : offshopId, "interestYn": "Y"}])
			}).done(function(response) {
				$(thisBtn).addClass("checked");
		 			alert("관심 매장으로 등록되었습니다.");
			});
		},
		
		// 관심매장 해제
		deleteInterestOffshop : function(thisBtn, offshopId) {
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/ccs/offshop/update/interest",
				type : "POST",
				data : JSON.stringify([{"offshopId" : offshopId, "interestYn": "N"}])
			}).done(function(response) {
				$(thisBtn).removeClass("checked");
 				alert("관심 매장에서 해제되었습니다.");
			});
		},
		
		// 픽업 가능 매장만 보기
		searchPickupOffshop : function() {			
			var pickupYn = "";
			if ($("#pickupCheckbox").is(":checked")) {
				pickupYn = "Y";
			} else {
				pickupYn = "";
			}
			
			ccs.offshop.setCurrentPosition(null, pickupYn);
		},
		
		// 주변매장 보기(mobile)
		getCloseOffshop : function() {
			ccs.offshop.setCurrentPosition(50, null);
		},
		
		// 픽업 가능 상품 보기
		showPickupProduct : function(offshopId, offshopName) {
			if (ccs.common.mobilecheck()) {
				ccs.link.go("/dms/display/shopOn?offshopId="+offshopId+"&name="+offshopName, CONST.NO_SSL);
			} else {
				ccs.link.go("/dms/special/pickup?offshopId="+offshopId+"&name="+offshopName, CONST.NO_SSL);
			}
		},
		
		// 브랜드 선택 시, 시/도 조회 및 시/군/구 초기화
		changeBrand : function() {
			var brandId = $("#selectBrand option:selected").val();
			
			if (common.isNotEmpty(brandId)) {
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/api/ccs/offshop/area1/search",
					type : "POST",
					data : JSON.stringify({"brandId" : brandId})
				}).done(function(response) {
					if (response.area1List.length > 0) {
		 				var strHtml = "<option>시/도</option>";
		 				for (var i=0; i<response.area1List.length; i++) {
		 					strHtml += "<option>" + response.area1List[i].areaDiv1 + "</option>";
		 				}
		 				$("#selectArea1").html(strHtml);
		 			} else {
		 				$("#selectArea1").html("<option>시/도</option>");
		 			}
				});
			} else {	// 브랜드 전체 선택
				$("#selectBrandLabel").text("브랜드");
				$("#selectBrand option:eq(0)").attr("selected", "selected");
				
				$("#selectArea1").html("<option>시/도</option>");
			}
			
			$("#selectArea1").parent().find("label").text("시/도");
			
			$("#selectArea2").html("<option>시/군/구</option>");
			$("#selectArea2").parent().find("label").text("시/군/구");
		}
	},
	
	/********************************************************************
	 * ccs.mainSwipe
	 ********************************************************************/
	mainSwipe: {
		
		/*
		 * 스와이프 높이 계산
		 * .swiper-wrapper:eq(0) : gnb
		 * .swiper-wrapper:eq(1) : mainCon > inner
		 */
		calculateHeight : function() {
			for (var i=0; i<$(".mobile .swiper-wrapper:eq(1)").find(".inner").length; i++) {
				if ($(".mobile .swiper-wrapper:eq(1) .inner:eq(" + i + ")").is(".swiper-slide-active")) {
					var inner_height = $(".mobile .swiper-wrapper:eq(1) .inner:eq(" + i + ")").height();
					//$(".mobile .swiper-wrapper:eq(1)").height( inner_height );
					$("#mainSwiperWrapper").height( inner_height );
				}
			}
		}
	
	}
}