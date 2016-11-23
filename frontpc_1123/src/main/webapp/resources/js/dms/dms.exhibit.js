/**
 * 업무 : 전시 - 기획전 
 *  
 */

//function
exhibit = {
	
		main : {
				/***************************************************************
				 * 기획전 리스트 조회
				 * 
				 ***************************************************************/
				getExhibitList : function (paramCategoryId, paramBrandId){
					
					
					
					var categoryId ="";
					var brandId = "";
					if (ccs.common.mobilecheck()) {
						if ((paramCategoryId != "" && paramCategoryId != undefined )|| (paramBrandId != "" && paramBrandId != undefined)) {
							categoryId = paramCategoryId;
							brandId = paramBrandId;
							
							if (paramCategoryId != "") {
								$("#select_cate").val(paramCategoryId);
							}
							if (paramBrandId != "") {
								$("#select_brand").val(paramBrandId);
							}
							
						} else {
							categoryId = $("#select_cate").find("option:selected").val();
							brandId = $("#select_brand").find("option:selected").val();
						}
						
					} else {
						categoryId = paramCategoryId;
						brandId = paramBrandId;
					}
					if (categoryId == 'all') {
						categoryId ="";
					}
					if (brandId == 'all') {
						brandId = "";
					}
					
					//hash 값 저장
					if(ccs.common.mobilecheck() && location.hash.indexOf("#3") > -1) {
						location.hash = "#3II"+categoryId+"II"+brandId;
					}
					
					var currentPage = $("[name=currentPage]").val();
					
					if (currentPage == undefined) {
						currentPage = 0;
					} 
					
					$(".exhibitList_ul").append('<div id="lodingBar" align="center"><img src="/resources/img/mobile/Loading.gif" alt="" /></div>');  

					$.ajax({
						contentType : "application/json; charset=UTF-8",
						url : "/dms/exhibit/list/ajax",
						type : "GET",		
						data : {displayCategoryId:categoryId, brandId : brandId, currentPage : currentPage}
					}).done(function( html ) {
						var moreYn = "Y";
						if (currentPage == 0) {
							moreYn = "N";
						}
						exhibit.main.listCallback(html, moreYn);
						common.hideLoadingBar();
					});
				},
				
				// SET AJAX RETURN HTML
				listCallback : function(html, moreYn) {
					if(ccs.common.mobilecheck()) {
						if (moreYn != undefined && moreYn == 'Y') {
							$(".exhibitList_ul").append(html);
						} else {
							$(".exhibitList_ul").html(html);
						}
						var calcHeight = setHeightInterval();
						intervalCalcHeight(calcHeight);
					} else {
						$(".exhibitList_ul").html(html);
					}
				},
				
				changeCategoryNbrand : function(param) {
					$("[name=currentPage]").val(0);
					if ($(param).attr("id") == select_cate) {
						
					}
					
					exhibit.main.getExhibitList();
				}, 
				
			},
			
		detail : {
			/***************************************************************
			 * 쿠폰 다운로드
			 * 
			 ***************************************************************/
			
			couponDnLoginCheck : function(param1, param2) {
//				mms.common.isLogin(function(result) {
//					if (result == null || result == 2 || result == "") {
//						if (confirm("쿠폰을 받으시려면 로그인이 필요합니다. 로그인 하시겠습니까?")) {
//							mms.common.loginCheck(function(result){
////								exhibit.detail.couponDownload(param1, param2);
//							});
//						}
//					} else {
//						exhibit.detail.couponDownload(param1, param2);
//					}
//				});
				
				mms.common.loginCheck(function(result){
					if(result){
						exhibit.detail.couponDownload(param1, param2);
					}
				});
			},
			
			couponDownload : function(param1, param2) {
				
				var count = 0;
				var couponIds = [];
				if ('single' == param1) {
					couponIds.push(Number(param2));
				} else if ('multi' == param1) {
					
					$(".couponInfoList > ul > li").each(function() {
						if ($(this).children().find("input[type=checkbox]").is(":checked")) {
							couponIds.push(Number($(this).children().find("input[type=checkbox]").attr("id")));
							count++;
						}
					});
					if (count == 0) {
						alert("쿠폰을 선택해 주세요.");
						return;
					}
				} else if ('all' == param1) {
					$(".couponInfoList > ul > li").each(function() {
						couponIds.push(Number($(this).children().find("input[type=checkbox]").attr("id")));
					});
				}
				
				sps.coupon.issue( couponIds, function(response){
					var msg = "";
//					 * 		   0000 : 발급성공<br/>
//					 *         0001 : 총 발급수량 초과<br/>
//					 *         0002 : 인당 발급수량 초과<br/>
//					 *         0003 : 유효하지 않은 쿠폰<br/>
//					 *         0010 : 적용 대상 아님(허용설정체크실패)<br/>
//					 *         0100 : 회원 정보 쿠폰 발급기준 미 충족<br/>
					
					for(i = 0 ; i < response.length ; i++){
						if (response[i].resultCode=='0000') {
							alert("쿠폰 발급이 완료되었습니다.");	
						} else if (response[i].resultCode=='0001') {
							alert("아쉽게도~ 쿠폰 발급 수량 선착순 마감 되었습니다.");
						} else if (response[i].resultCode=='0002') {
							alert("이미 발급 받으신 쿠폰이 있습니다.");
						}
					}
					
				}, null);
				
//				$.ajax({ 				
//					url : "/api/sps/coupon/issueCoupon",
//					method : "post",		
//					contentType : "application/json; charset=UTF-8",
//					data: JSON.stringify(data),
//					success : function(response) {
//						console.log(response);
//						var count = 0;
//						
//						for (var i=0; i < response.length; i++) {
//							if (response[i].resultCode != '0000') {
//								count++; 
//							}
//						}
//						if (count == 0) {
//							alert("쿠폰이 발급되었습니다.");
//						} else if (response.length == count) {
//							alert("쿠폰을 이미 받으셨습니다.");
//						} else {
//							alert("이미 발급된 쿠폰을 제외한 쿠폰이 발급 되었습니다.");
//						}
//					},
//					error : function(response) {
//						alert("쿠폰 발급중 오류가 발생하였습니다.");
//					}
//				});
			}, 
			
			// MOBILE 구분타이틀 셀렉트 박스 변경
			changeGubunTitle : function(param) {
				var groupNo = "";
				var exhibitId = $("input[name=exhibitId]").val();
				var currentPage = $("input[name=currentPage]").val();
				if (param.value != '전체보기') {
					groupNo = param.value; 
				}
				
				var data = {exhibitId : exhibitId, groupNo : groupNo, currentPage : currentPage}; 
				$.ajax({ 				
					url : "/dms/exhibit/product/list/ajax",
					method : "post",		
					contentType : "application/json; charset=UTF-8",
					data: JSON.stringify(data),
					success : function(response) {
						$("#exhibitProduct_div").html(response);
						
						exhibit.detail.changeUrl(groupNo)
						
					},
					error : function(response) {
						
					}
				});
			}, 
			
			moreMobileProductList : function() {
				
				var exhibitId = $("input[name=exhibitId]").val();
				var currentPage = Number($("input[name=currentPage]").val());
				
				currentPage++;
				$("input[name=currentPage]").val(currentPage);
				
				var data = {exhibitId : exhibitId, currentPage : currentPage}; 
				$.ajax({ 				
					url : "/dms/exhibit/product/list/ajax",
					method : "post",		
					contentType : "application/json; charset=UTF-8",
					data: JSON.stringify(data),
					success : function(response) {
						$("#exhibitProduct_div > ul").append(response);
					},
					error : function(response) {
						
					}
				});
			}, 
			
			
			changeUrl : function(groupNo) {
				var params = location.search.substring(1);
			    if (groupNo != "") {
			        var obj = JSON.parse('{"' + decodeURI(params).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}');
			        obj.groupNo = groupNo;
			        history.replaceState("", "", location.pathname + "?" + $.param(obj));
			    } else {
			    	var obj = JSON.parse('{"' + decodeURI(params).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}');
			    	var exhibitId = {exhibitId : obj.exhibitId};
			        history.replaceState("", "", location.pathname + "?" + $.param(exhibitId));
			    }
			}
		}
}

