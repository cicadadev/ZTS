//회원
mms = {
		LOGINCALLBACKFN : null,//로그인 콜백 함수
		common : {
				Wishlist : function(productId,saleproductId,mmsWishlists){
					/**
					 * productId , (필수)
					 * saleproductID , (필수)
					 * mmsWishlists, (SET 일때 필수), Type : Array
					 * 
					 */
					this.productId = productId;
					this.saleproductId = saleproductId;
					this.mmsWishlists = mmsWishlists;
				},
				deleteWishlist : function(productId, callback){	
					
					//로그인 체크 후 찜담기
					mms.common.loginCheck(function(result){
						
						if(result){
							$.ajax({
								//contentType : "application/json; charset=UTF-8",
								url : "/api/mms/member/wish/delete",
								type : "POST",		
								data : {productId : productId}
							}).done(function(response){
									
								alert("찜이 해제되었습니다.");
								if(callback){
									callback();	
								}
							});
						}else{
							
						}
					});
					
					

				},
				saveWishlist : function(mmsWishlists, callback){
					/**
					 * var wishlists = [];
					 * 
					 * var mmsWishlist = new mms.Wishlist(productId,saleproductId,subWishlists);
					 * 
					 * wishlists.push(mmsWishlist);
					 * 
					 * mms.saveWishlist(wishlists,function(response){
					 *		alert("저장하였습니다.");
					 * });
					 */
					
					if(common.isEmptyObject(mmsWishlists) || mmsWishlists.length==0){
						callback("상품번호 없음!.");
						return;
					}
					var mmsMember = {mmsWishlists : []};
					mmsMember.mmsWishlists = mmsWishlists;
					
					//로그인 체크 후 찜담기
					mms.common.loginCheck(function(result){
						
						if(result){
							$.ajax({
								contentType : "application/json; charset=UTF-8",
								url : "/api/mms/member/wish/save",
								type : "POST",		
								data : JSON.stringify(mmsMember)
							}).done(function(response){
								if(response.RESULT == "SUCCESS"){
									alert("쇼핑찜에 추가하였습니다.");
									if(callback){
										callback();
									}
			//					alert("저장되었습니다.");					
								}else{
									alert(response.MESSAGE);
								}				
							});
						}else{
							
						}
					});
					

				},
				restfulTrx : function(url, type, data, callback_success, callback_send, callback_error) {
					$.ajax({
						contentType : "application/json; charset=UTF-8",
						url : url,
						type : type,
						data : JSON.stringify(data),
						beforeSend : function(xhr) {
							if (callback_send != undefined) {
								callback_send(xhr);
							} else {
								common.showLoadingBar();
							}
						}
					}).done(function(data, status, xhr){
						common.hideLoadingBar();
						if (callback_success != undefined) {
							callback_success(data);
						}
					}).fail(function(xhr, status, error) {
						if (callback_error != undefined) {
							callback_error(error);
						} else {
							alert(error);
							console.log(xhr);
							console.log(status);
							console.log(error);
						}
					}).always(function() {
						common.hideLoadingBar();
					});
				},
				paging : function(url, data, callback) {
					$.get( url, data ).done(function( html ) {
						callback(html);
					});
				},
				// 가입 팝업 mms.common.joinPopup(returnUrl)
				joinPopup : function(url){
					
					if(common.isEmpty(url)){
						url = global.config.domainUrl+"/ccs/common/main";
					}
					var returnUrl = global.config.domainUrl+"/mms/login/callback2?ctype=1&url="+url;//ctype=1 : 로그인 처리 및 메인화면으로 이동
					var chnlCd = "3";//pc
					
					if(global.channel.isApp=='true'){
						chnlCd = "42";// app
					}else if(global.channel.isMobile=='true'){
						chnlCd = "41";// mobile web
					}
					
					var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=500,height=600";
					var url = global.config.joinUrl+"?purpose=joinMember&ntryPath="+global.member.coopcoCd+"&chnlCd="+chnlCd+"&coopcoCd="+global.member.coopcoCd+"&returnUrl="+returnUrl;
					window.open(url,"join",status);
				},
				// 회원정보 수정 팝업
				memberUpdatePopup : function(returnUrl,type){// 통합멤버십 회원 수정 팝업 : mms.common.memberUpdatePopup()
					var chnlCd = "3";//pc
					if(global.channel.isApp=='true'){
						chnlCd = "42";// app
					}else if(global.channel.isMobile=='true'){
						chnlCd = "41";// mobile web
					}
					var returnUrl = global.config.domainUrl+"/mms/login/callback2?ctype=2";//ctype=2 :팝업 닫기(리로드없음)
					var url = global.config.memberUpdateUrl+"?chnlCd="+chnlCd+"&ntryPath="+global.member.coopcoCd+"&coopcoCd="+global.member.coopcoCd+"&returnUrl="+returnUrl;
					// 모바일이면 페이지 이동
					if(chnlCd!='3'){

						location.href=url;
						
					}else{
						console.log(url);
						var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540";
						
						window.open(url,"login",status);
					}
					
				},				
				// 약관 동의 철회 팝업
				memberDisagreePopup : function(){// 통합멤버십 회원 수정 팝업 : mms.common.memberDisagreePopup()
					var chnlCd = "3";//pc
					if(global.channel.isApp=='true'){
						chnlCd = "42";// app
					}else if(global.channel.isMobile=='true'){
						chnlCd = "41";// mobile web
					}
					var returnUrl = global.config.domainUrl+"/mms/login/callback2?ctype=3";// ctype=3 : 로그아웃처리
					var url = global.config.memberDisagreeUrl+"?chnlCd="+chnlCd+"&ntryPath="+global.member.coopcoCd+"&coopcoCd="+global.member.coopcoCd+"&returnUrl="+returnUrl;
					
					console.log(url);
					
					// 모바일이면 페이지 이동
					if(chnlCd!='3'){

						location.href=url;
						
					}else{
						console.log(url);
						var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540";
						
						window.open(url,"login",status);
					}
					
				},// 비밀번호 변경 팝업
				memberPwChangePopup : function(){// mms.common.memberPwChangePopup()
					var chnlCd = "3";//pc
					if(global.channel.isApp=='true'){
						chnlCd = "42";// app
					}else if(global.channel.isMobile=='true'){
						chnlCd = "41";// mobile web
					}
					var returnUrl = global.config.domainUrl+"/mms/login/callback2?ctype=2";//ctype=2 :팝업 닫기(리로드없음)
					var url = global.config.memberPwChangeUrl+"?chnlCd="+chnlCd+"&ntryPath="+global.member.coopcoCd+"&coopcoCd="+global.member.coopcoCd+"&returnUrl="+returnUrl;
					
					console.log(url);
					
					// 모바일이면 페이지 이동
					if(chnlCd!='3'){
						location.href=url;
					}else{
						var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540";
						window.open(url,"login",status);
					}
					
				},// 마케팅 동의여부 변경 팝업		
				memberMktAgreePopup : function(){
					
					
					var chnlCd = "3";//pc
					if(global.channel.isApp=='true'){
						chnlCd = "42";// app
					}else if(global.channel.isMobile=='true'){
						chnlCd = "41";// mobile web
					}
					var returnUrl = global.config.domainUrl+"/mms/login/callback2?ctype=2";//ctype=2 :팝업 닫기(리로드없음)
					var url = global.config.memberMktAgreeUrl+"?chnlCd="+chnlCd+"&ntryPath="+global.member.coopcoCd+"&coopcoCd="+global.member.coopcoCd+"&returnUrl="+returnUrl;
					
					// 모바일이면 페이지 이동
					if(chnlCd!='3'){
						location.href=url;
					}else{
						var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540";
						window.open(url,"login",status);
					}
				},					
				// 로그인 체크 : return (1 : 일반회원 로그인, 2 : 비회원주문조회 로그인)
				isLogin : function(callback){//mms.common.isLogin(callback)
					$.ajax({
						  contentType : "application/json; charset=UTF-8",
						  method: "get",
						  cache : false,
						  url: "/api/mms/login/checklogin/ajax"
						}).done(function( data ) {
							callback(data.loginType);
						});
				},
				// 로그인 체크 후 미로그인시 로그인 팝업 : 로그인 후 refresh 하지 않음
				loginCheck2 : function(callback){ //mms.common.loginCheck2();
					
					mms.common.isLogin(function(result){
						
						var success = true;
						
						if(result!=1){
							success = false;
						}
						
						if(result!=1){//로그인 실패
							mms.LOGINCALLBACKFN = callback;
							ccs.link.login({isReload : "N"});
						}
						
						callback(success);
					})
				},
				// 로그인 체크 후 미로그인시 로그인 팝업 : 로그인 후 refresh
				loginCheck : function(callback){ //mms.common.loginCheck();
					mms.common.isLogin(function(result){
						var success = true;
						if(result!=1){
							success = false;
						}
						if(result!=1){//로그인 실패
							ccs.link.login();
						}
						
						callback(success);
					})
				},
				// 비회원 주문조회 로그인 팝업
				nonMemberLoginLayer : function(data){// mms.common.nonMemberLoginLayer()
					if (ccs.common.mobilecheck()) {
						ccs.layer.fnLayerOpen($("#noneMemberLoginLayer"));
					} else {
						// 위치잡기
						ccs.layer.fnPopPosition($("#noneMemberLoginLayer"));
					}
				},				
				// 비회원 주문조회 로그인
				nonMemberLogin : function(data){// mms.common.nonMemberLogin()
					$.ajax({
						  contentType : "application/json; charset=UTF-8",
						  data : JSON.stringify(data),
						  method: "post",
						  url: "/api/mms/login/nonmember/ajax"
						}).done(function( data ) {
							if(data=='success'){
								ccs.link.custcenter.nonMemberOrder()
							}else{
								alert("일치하는 주문내역이 없습니다. 다시 한 번 확인해 주시기 바랍니다");
							}
							
						});
				},				
				// 비회원 주문번호 찾기
				nonMemberFindOrderId : function(data){// mms.common.nonMemberFindOrderId()
					$.ajax({
						  contentType : "application/json; charset=UTF-8",
						  data : JSON.stringify(data),
						  method: "post",
						  url: "/api/ccs/user/nonmember/findOrderId/ajax"
						}).done(function( data ) {
							if(data!='fail'){
								var html = '고객님의 주문번호는 <u>' + data + '</u> 입니다.';
								$('#orderIdResult').html(html);
							}else{
								var html = '일치하는 주문내역이 없습니다. ';
								$('#orderIdResult').html(html);
							}
							
						});
				},				
				// 비회원 주문 비밀번호 찾기
				nonMemberFindOrderPwd : function(data){// mms.common.nonMemberFindOrderPwd()
					$.ajax({
						  contentType : "application/json; charset=UTF-8",
						  data : JSON.stringify(data),
						  method: "post",
						  url: "/api/ccs/user/nonmember/findOrderPwd/ajax"
						}).done(function( data ) {
							if(data=='success'){
								var html = 'SMS로 비밀번호가 전송되었습니다.';
								$('#orderPwdResult').html(html);
							}else{
								var html = '일치하는 주문내역이 없습니다. ';
								$('#orderPwdResult').html(html);
							}
							
						});
				},						
				// list 분류 선택 표시
				typeExpression : function (content) {
					if(content != undefined) {
						if (content != "else" && content != "coupon") {
							$(".sortBox").children('li').removeClass('active');
							$(content).addClass('active');
						} else if(content == "coupon") {
							$(".sortBox").children('li').eq(0).addClass('active');	
						}
					} 
					else {
						$(".sortBox").children('li').removeClass('active');
						$(".sortBox").children('li').eq(0).addClass('active');
					}
				},
				// list 분류 카운트
				setCnt : function() {
					$("#totalCnt").text(" ("+$("#total").val()+")");
					$("#item1Cnt").text(" ("+$("#item1").val()+")");
					$("#item2Cnt").text(" ("+$("#item2").val()+")");
				}

		}		// mms.common END
}
