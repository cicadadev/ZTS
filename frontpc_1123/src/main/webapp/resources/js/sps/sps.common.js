//프로모션
sps = {
		
	/**
	 * 딜
	 */
	deal : {
		
		/**
		 * 쇼킹제로
		 */
		shockingzero : {
			
			// 남은 시간 카운트다운
			countdown : function(divName, endDt) {
				$("#"+divName).countdown(endDt, function(event) {
					$(this).text(
				      event.strftime('%D일 %H:%M:%S')
				    );
				});
			},
			
			// 쇼킹제로 상품 정렬
			resorting : function(sortType, currentPage, firstRow) {
				//common.showLoadingBar();
				$("#sortBox").find("li").removeClass("active");
				if (sortType == "popular") {
					$("#popular_li").addClass("active");
				} else if (sortType == "new") {
					$("#new_li").addClass("active");
				} else if (sortType == "end") {
					$("#end_li").addClass("active");
				}
				
				if (common.isEmpty(firstRow)) {
					firstRow = 1;
				}
				if (common.isEmpty(currentPage)) {
					currentPage = 1;
				} else {
					currentPage = Number(currentPage) + 1;
				}
				
				
				//hash 값 저장
				if(ccs.common.mobilecheck() && location.hash.indexOf("#1") > -1) {
					location.hash = "#1II"+sortType;
				}
				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/sps/deal/shockingzero/product/ajax",
					type : "get",
					data : {"sortType": sortType, "firstRow" : firstRow, "currentPage" : currentPage}
				}).done(function(html) {
					
					if(ccs.common.mobilecheck() && firstRow > 1) {
						sps.deal.shockingzero.shockingzeroCallback_mo(html);
						
					} else {
						sps.deal.shockingzero.shockingzeroCallback(html);
					}
					//common.hideLoadingBar();
				});
			},
			shockingzeroCallback : function(html) {
				$("#shockingProductArea").html(html);
				ccs.mainSwipe.calculateHeight();
//				$("#txtTotalCnt").text($("#totalCnt").val() + "개");
			},
			shockingzeroCallback_mo : function(html) {
				$("#shockingProductArea").append(html);
				ccs.mainSwipe.calculateHeight();
//				$("#txtTotalCnt").text($("#totalCnt").val() + "개");
			},
			
			// 쇼킹제로 상품 정렬(모바일)
			sortChange : function() {
				$("#sortSelect").siblings("label").text($("#sortSelect option:selected").text());
				sps.deal.shockingzero.resorting($("#sortSelect option:selected").val());
			},
			
			// 쇼킹제로 더보기(모바일)
			viewMore : function() {
				$(window).scroll(function() {					
					var rowCount = $("#shockingProductArea").find("li").length;
					var totalCount = Number($("#totalCnt").val());
					var maxPage = Math.ceil(totalCount/10);
					
					if($(window).scrollTop() == $(document).height() - $(window).height()) {
						if(rowCount !=0 && (rowCount < totalCount)){
							sps.deal.shockingzero.resorting($("#sortType").val(), $("#currentPage").val(), rowCount+1);
						}
					}
				});			
			}
			
		}	// shockingzero

	}	// deal
	, coupon : {
		/**
		 * 쿠폰 발급
		 * 
		 * 1. 일반쿠폰 다운로드
		 * ex) sps.coupon.issue( couponId, callback, null);
		 * 
		 * 2. 개별 인증 코드 이용한 발급
		 * ex) sps.coupon.issue( couponIds, callback, privateCin);
		 * 
		 */
		issue : function(couponIds, callback, privateCin) {
			
			// 일반 쿠폰 다운로드
			var url = "/api/sps/coupon/issueCoupon";
			var data = [];
			// 개별 인증코드 검증
			if(privateCin != undefined) {
				
				privateCin = privateCin.toUpperCase();
				
				if(privateCin.length < 1 ) {
					alert("인증코드를 입력해 주세요.");
					$("#privateCin").focus();
					return callback(false);
				}
				
				if(privateCin.replace(/[A-Z|0-9]/g, '').length != 0) {
					alert("유효하지 않은 인증코드입니다.");
					$("#privateCin").focus();
					return callback(false);
				}
				else if(privateCin.length != 14) {
					alert("유효하지 않은 인증코드입니다.");
					$("#privateCin").focus();
					return callback(false);
				}
				
				data.push({couponId: null, privateCin : privateCin}); 
				
				//url = "/api/sps/coupon/issueCertificationCoupon";
				url = "/api/sps/coupon/issueCoupon";
			} else {
				for(i = 0 ; i < couponIds.length ; i++){
					data.push({couponId: couponIds[i], privateCin : null}); 
				}
			}
			common.showLoadingBar();	
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : url,
				type : "POST",
				data : JSON.stringify(data),
				success : function(res) {
					if(common.isNotEmpty(callback)) {
						callback(res);
					} else {
						alert(res[0].resultMsg);
					}
					common.hideLoadingBar();
				},
				error : function(req, status, err) {
					common.hideLoadingBar();
				}
			});
		}
	},
	
	/**
	 * 이벤트&혜택
	 */
	event : {		
		// 카드 혜택 페이지 호출
		getCardBenefit : function(cardName) {
			if ($(".pscTab").is(":visible")) {
				$(".pscTab").hide();
			}
			$("#psCardInner").html("");
			
			common.showLoadingBar();			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/sps/event/psCard/" + cardName + "/ajax",
				type : "GET"
			}).done(function(html) {
				$("#psCardInner").html(html);	

				$(".pscTab").show();
				
				common.hideLoadingBar();
			});
		},
		// 등급별 쿠폰 다운로드 : sps.event.downloadMemberGradeCoupon(couponIdStr);
		downloadMemberGradeCoupon : function(couponIdStr){
			
			
			// 쿠폰 발급 가능 시간 체크
			if(!sps.event.checkIssueable()){
				alert("회원등급이 처리되는 매월 1일 00시~03시 사이에는 쿠폰이 다운로드 되지 않습니다.");
				return;
			}
			if(couponIdStr=='' || couponIdStr==null){
				alert("발급받으실 쿠폰이 없습니다.");
				return;
			}
			var couponIds = [];
			for(i = 0 ; i < arrs.length ; i++){
				couponIds.push(arrs[i]);
			}
			
			common.showLoadingBar();
			
			// 온라인 쿠폰 발급
			sps.coupon.issue( couponIds, function(response){
				var couponCnt = couponIds.length;
				var success = 0;
				for(i = 0 ; i < response.length ; i++){
					if(response[i].resultCode=='0000'){
						success++;
					}
				}
				
				// 오프라인 쿠폰 발급
				if(success > 0){
					sps.event.issueOffCoupon();
				}
				
				common.hideLoadingBar();
				
				if(couponCnt== success){
					alert("쿠폰이 정상적으로 다운로드 되었습니다.");
				}else if(success > 0){
					alert("쿠폰이 정상적으로 다운로드 되었습니다.");
					//alert("전체 "+couponCnt+"개의 쿠폰중 "+ success+" 개의 쿠폰을 발급 받았습니다.");
				}else{
					alert("쿠폰을 이미 다운로드 받으셨습니다.");
				}
				
			}, null);
		},
		//오프라인 쿠폰 발급(동기방식)
		issueOffCoupon : function(){
			
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : "/api/sps/coupon/issueOffcoupon",
				type : "get",
				async: false,
				success : function(res) {
				},
				error : function(req, status, err) {
				}
			});			
		},
		//등급별 쿠폰 발급 가능 시간 체크(동기방식)
		checkIssueable : function(){
			var isSuccess = false;
			$.ajax({
				url : "/api/sps/coupon/checkissueable",
				type : "get",
				async: false,
				success : function(res) {
					if("success"==res){
						isSuccess = true;
					}else{
						isSuccess = false;
					}
				},
				error : function(req, status, err) {
					
				}
			});			
			return isSuccess;
		},
		
		// 이벤트 신청(응모) URL 기본 도메인 : naver, instagram, facebook, kakao(kakaostory)
		urlDomainSet : function(sns) {
			var validCnt = 0;
			var urlLength = $("#joinUrl_ul").find("li").length;
			
			$("#joinUrl_ul").find("li").each(function() {
				var url = $(this).find("input[type=text]").val();
				
				if (validCnt == 0) {
					if (sns == "etc") {
						if (url.indexOf("naver") < 0 && url.indexOf("instagram") < 0 
								&& url.indexOf("facebook") < 0 && url.indexOf("kakao") < 0) {
							if (urlLength > 1) {
								alert("기타 URL은 한 개만 등록하실 수 있습니다.");
								validCnt += 1;
							}		
						}
					} else {
						if ($(this).find("input[type=text]").val().indexOf(sns) > 0) {
							alert("SNS당 한 개의 URL만 등록하실 수 있습니다.");
							validCnt += 1;
						}
					}
				}				
			});
			
			if (validCnt == 0) {
				if (sns == "naver") {
					$("#joinUrl_ul li:last input[type=text]").val("http://blog.naver.com/");
				} else if (sns == "instagram") {
					$("#joinUrl_ul li:last input[type=text]").val("https://www.instagram.com/");
				} else if (sns == "facebook") {
					$("#joinUrl_ul li:last input[type=text]").val("https://www.facebook.com/");
				} else if (sns == "kakao") {
					$("#joinUrl_ul li:last input[type=text]").val("https://story.kakao.com/");
				} else if (sns == "etc") {
					$("#joinUrl_ul li:last input[type=text]").val("http://");
				}
			}
		},
		
		// 생생테스터
		vividity : {
			
			// 이벤트&생생테스터 탭 전환
			tabEffect : function() {
				$(".tabBox").on("click", "li", function() {
					$(".tabBox li").removeClass("on");
					$(this).addClass("on");
					
					$(".tab_con").removeClass("tab_conOn");
					
					var tabIdx = $(this).index();
					if (tabIdx == 0) {						
						$("#eventArea").addClass("tab_conOn");
					} else if (tabIdx == 1) {
						$("#vividityArea").addClass("tab_conOn");
					}
				});
			},
			
			// 신청내역
			joinList : function(eventId) {
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url : "/sps/event/vividity/join/ajax?eventId=" + eventId,
					type : "GET"
				}).done(function(html) {
					sps.event.vividity.joinAjaxCallback(html);
				});
			},
			joinAjaxCallback : function(html) {
				$("#vividityJoinList_Area").html(html);
			},
			
			// 신청 URL 입력란 추가
			addUrl : function() {
				var idx = $("#joinUrl_ul").find("li").length;
				if (idx == 5) {
					alert("URL은 최대 5개까지 등록할 수 있습니다.");
				} else {
					var prevUrl = $("#joinUrl_ul li:eq(" + (idx-1) + ") input[type=text]").val();
					if (prevUrl == "" || prevUrl == "SNS를 선택해주세요.") {	// URL 없으면 입력란 추가 못하게.
						alert("블로그 및 SNS 주소를 넣어주세요.");
					} else {
						var strHtml = "<li>";
						
						strHtml += "<input type='text' value='SNS를 선택해주세요.' class='inputTxt_style1' onfocus=\"if (this.value == 'SNS를 선택해주세요.') {this.value='';}\" onblur=\"if (this.value == '') {this.value='SNS를 선택해주세요';}\" />";
						strHtml += "<a href='javascript:sps.event.vividity.delUrl(" + (idx+1) + ");' class='btn_del2'>삭제</a>";
						strHtml += "</li>";
						
						$("#joinUrl_ul").append(strHtml);
					}	
				}
			},
			
			// 신청 URL 입력란 제거
			delUrl : function(idx) {
				$("#joinUrl_ul li:eq(" + (idx-1) + ")").remove();
			},
			
			// 이벤트 신청
			saveJoinUrl : function(eventId) {
				mms.common.isLogin(function(result) {
					if (result == 1) {
						var etcUrl, naverblogUrl, instagramUrl, facebookUrl, kakaostoryUrl;

						// URL validation
						var valid = true;
						$("#joinUrl_ul").find("li").each(function() {
							var url = $(this).find("input[type=text]").val();
							
							if (valid) {
								if (url == "SNS를 선택해주세요.") {
									alert("URL 주소를 한 개 이상 입력해주세요.");
									valid = false;
								}
								
								if (url.indexOf("naver") > 0) {
									if (common.isEmpty(url.split("blog.naver.com/")[1])) {
										alert("블로그 URL을 정확하게 입력해주세요.");
										valid = false;
									} else {
										naverblogUrl = url;
									}
									
								} else if (url.indexOf("instagram") > 0) {
									if (common.isEmpty(url.split("www.instagram.com/")[1])) {
										alert("인스타그램 URL을 정확하게 입력해주세요.");
										valid = false;
									} else {
										instagramUrl = url;
									}
									
								} else if (url.indexOf("facebook") > 0) {
									if (common.isEmpty(url.split("www.facebook.com/")[1])) {
										alert("페이스북 URL을 정확하게 입력해주세요.");
										valid = false;
									} else {
										facebookUrl = url;
									}
									
								} else if (url.indexOf("kakao") > 0) {
									if (common.isEmpty(url.split("story.kakao.com/")[1])) {
										alert("카카오스토리 URL을 정확하게 입력해주세요.");
										valid = false;
									} else {
										kakaostoryUrl = url;
									}
									
								} else {
									if (common.isEmpty(url)) {		// TODO: URL 형식 VALIDATION
										alert("URL을 정확하게 입력해주세요.");
										valid = false;
									} else {
										etcUrl = url;
									}
								}
							}		
						});	
						
						// 댓글 validation
						if (valid) {
							var detail = $(".txtarea_box textarea").val();
							if (detail != "") {
								if (detail.length > 600) {
									alert("댓글은 600자 이내로 작성해주세요.");
									valid = false;
								}
							} else {
								alert("댓글을 작성해주세요.");
								valid = false;
							}
						}
						
						// 응모이력 저장
						if (valid) {
							var param = {
									eventId : eventId,
									url : etcUrl,
									naverblogUrl : naverblogUrl,
									instagramUrl : instagramUrl,
									facebookUrl : facebookUrl,
									kakaostoryUrl : kakaostoryUrl,
									detail : $(".txtarea_box textarea").val()
							};
							
							$.ajax({
								contentType : "application/json; charset=UTF-8",
								url : "/api/sps/event/join/save",
								type : "POST",
								data : JSON.stringify(param)
							}).done(function(response) {
								if (common.isNotEmpty(response.eventId)) {
									sps.event.vividity.joinList(response.eventId);
								}								
							});
						}
					} else {
						ccs.link.login({returnUrl:global.config.domainUrl + "/sps/event/vividity/detail?eventId="+eventId});
					}
				});
				
			}
		},
		
		// 이벤트 응모하기 공통 TODO 임시
		saveJoin : function(eventId) {
			mms.common.isLogin(function(result) {
				if (result == 1) {
					var url = "/api/sps/event/join/save2";
					var data = {eventId : eventId};

					$.get( url, data ).done(function( response ) {
						console.log('response', response);
						if(response.RESULT == 'FAIL'){
							alert(response.MESSAGE);
						}else{
							alert('응모가 완료되었습니다.');
						}
						
					});
				}else{
					ccs.link.login({returnUrl:global.config.domainUrl + "/sps/event/vividity/detail?eventId="+eventId});
				}
			});
		}
	}
	
}