

/**
 * 전시공통 모바일 스크립트
 */
$(window).load(function(){
//	dmsmb.header.getCartCount();
//	dmsmb.lnb.getCommonGnb();
//	dmsmb.header.getPopularSearch();
//	dmsmb.lnb.brand.initSortList();
//	dmsmb.header.latelySearch();
	
	dmsmb.getCommonArea();// 공통 데이터
	
	$('#submit_btn').off("click").on("click", function() {
		value = $('#header_search').val();
		if(value !== ''){
			dms.common.setKeyWord(value);
		}
	});
});

//var imageUrl = global.config.imageDomain;

dmsmb ={
		// 공통 영역 조회
		getCommonArea : function(){
			
			//카트수
			dmsmb.header.getCartCount();
			
			// 왼쪽 메뉴
			dmsmb.lnb.getCommonGnb();
			
			// 검색관련
			dmsmb.header.getPopularSearch();
			dmsmb.lnb.brand.initSortList();
			dmsmb.header.latelySearch();
			
			ccs.layer.noticePopupLayer.open();// 공지 팝업
			
		},
		/***********************************************************************
		 * LNB : 전체카테고리, 월령, 브랜드, 조회
		 **********************************************************************/
		lnb : {
			getCommonGnb :function (){
				var allCategoryList = new Array;
				var ageCodeList = new Array;
				//ajax 호출
				$.get( "/api/dms/common/gbnInfo", function(response) {
					
					allCategoryList = response.ctgList;
					ageCodeList = response.ageCodeList;
					
					categoryHtml(allCategoryList);
					ageCodeHtml(ageCodeList);
					
				},"json")
				.fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
				}); 
				
				
				/* 전체 카테고리 depth1 HTML : click
				 * 대카는 최대 4개까지만 있는것으로 기획됨. 
				 * 화면에서 4개까지만 노출이 가능하게 퍼블리싱됨.
				 * 4개이상 노출시 퍼블리싱 재요청해야 함.
				 */			
				var categoryHtml = function(object){
					var mbHtmlList=[];
					$.each( object, function( index, value ) {
						
						var mbHtml = "<li "+(index == 0 ?" class='on'":"")+">"
									+"<a href=\"#none\" class=\"theme"+(index+1)+"\">"
									+"<em>"+value.name+"</em>"
									+"</a>"
									+"<ul class=\"dep2\">"
									+depth2Html(value.dmsDisplaycategorys)
									+"</ul>"
									+"</li>";
						mbHtmlList.push(mbHtml);
					});
					
					$('#mbCategory').append(mbHtmlList);
					
					cilckEvent();
					
				}
				
				// 전체 카테고리 depth3 HTML
				var depth2Html = function(object){
					var returnHtml="";

					if(Number(object.length) >0){
						$.each( object, function( index, value ) {
							var html="";
							if(value.displayCategoryId !== null){
								html="<li>"
									+"<a href=\"#none\" onclick=\"common.pageMove('dispTemplate',{'dispCategoryId':"+value.displayCategoryId+"},'')\">"+value.name+"</a>"
									+"</li>";
							}
							returnHtml+=html;
						});
					}
					return returnHtml;
				}
				
				//월령코드 목록 HTML
				var ageCodeHtml = function(object){
					var htmlList=[];
					$.each( object, function( index, value ) {
						var code = value.cd;
						var cdoeValue = code.split(".")[1];
						var html ="<li>"	
									+"<a href=\"/dms/display/ageShop?ageCode="+cdoeValue+"\">"+value.name+"</a>"
									+"</li>";
						htmlList.push(html);
					});
					$('#ageCode').append(htmlList);
					//console.log($('div.floatG dl').children("dd")[0].innerHTML);
					
				}
				
				var cilckEvent = function() {
					$(".mobile .category .item > li > a").off("click").on({
						"click" : function(e) {
							$(this).parent().addClass("on").siblings().removeClass("on");
							e.preventDefault();
						}
					});
				}
			},
			
			brand : {
				Consonant : [{name:"ㄱ", unicodeBefore:"44032", unicodeAfter:"45207"},
				             {name:"ㄴ", unicodeBefore:"45209", unicodeAfter:"45795"},
				             {name:"ㄷ", unicodeBefore:"45796", unicodeAfter:"46971"},
				             {name:"ㄹ", unicodeBefore:"46972", unicodeAfter:"47559"},
				             {name:"ㅁ", unicodeBefore:"47560", unicodeAfter:"48147"},
				             {name:"ㅂ", unicodeBefore:"48148", unicodeAfter:"49323"},
				             {name:"ㅅ", unicodeBefore:"49324", unicodeAfter:"50499"},
				             {name:"ㅇ", unicodeBefore:"50500", unicodeAfter:"51087"},
				             {name:"ㅈ", unicodeBefore:"51088", unicodeAfter:"52263"},
				             {name:"ㅊ", unicodeBefore:"52264", unicodeAfter:"52851"},
				             {name:"ㅋ", unicodeBefore:"52852", unicodeAfter:"53439"},
				             {name:"ㅍ", unicodeBefore:"53440", unicodeAfter:"54027"},
				             {name:"ㅌ", unicodeBefore:"54028", unicodeAfter:"54615"},
				             {name:"ㅎ", unicodeBefore:"54616", unicodeAfter:"55203"}
				             ]
				,
				Spelling :  ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"
				             , "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]
				,
				
				/**
				 * 브랜드 검색
				 * 
				 */
				search_Consonant : function(consonant, unicodeBefore, unicodeAfter){
					var param = {consonant : consonant, unicodeBefore : unicodeBefore, unicodeAfter : unicodeAfter};
					$.ajax({
						url : "/dms/common/search/list/ajax",
						type : "POST",
						data : param
					}).done(function(response) {
						$("#searchBrandResult").html(response);
						$("#searchBrandResult").parent().show();
					});
				},
				
				search : function(searchKeyword){
					var keyword;
					
					if(searchKeyword == null || searchKeyword == ''){
						keyword = $('#brandKeyword').val();
					}else{
						keyword = searchKeyword;
					}
					var param = {searchKeyword : keyword};
					$.ajax({
						url : "/dms/common/search/list/ajax",
						type : "POST",
						data : param
					}).done(function(response) {
						$("#searchBrandResult").html(response);
						$("#searchBrandResult").parent().show();
					});
				},
				
				changeLikeBrand : function(param){
					if ( !Storage.prototype.setObject ) {
					    Storage.prototype.setObject = function(key, value) {
					        this.setItem(key, JSON.stringify(value));
					    }
					}

					if ( !Storage.prototype.getObject ) {
					    Storage.prototype.getObject = function(key) {
					        var value = this.getItem(key);
					        return value && JSON.parse(value);
					    }
					}
					
					var brandId = [];
					
					if(common.isNotEmpty(localStorage.getObject('brandIds'))){
						brandId = localStorage.getObject('brandIds');
					}else{
						localStorage.setObject('brandIds', brandId );
					}

					var htmlList=[];
					$.each( brandId, function( index, value ) {
						//var html ="<li "+(index == 1 ?" class='on'":"")+">"
						var html ="<li>"
							+"		<a href=\"#none1\">"+ value.name + "</a>"
							+"		<label class=\"chk_style1\">"
							+"			<em>"
							+"				<input type=\"checkbox\" value=\"\" onClick=\"dmsmb.lnb.brand.change('','','" + index + "');\" checked/>"
							+"			</em>"
							+"			<span>즐겨찾기</span>"
							+"		</label>"
							+"	</li>";
						htmlList.push(html);
					});
					$('#likeBrandResult').empty();
					$('#likeBrandResult').append(htmlList);
				},
				
				// 관심 브랜드 변경
				change : function(name, brandId, index){
					if ( !Storage.prototype.setObject ) {
					    Storage.prototype.setObject = function(key, value) {
					        this.setItem(key, JSON.stringify(value));
					    }
					}

					if ( !Storage.prototype.getObject ) {
					    Storage.prototype.getObject = function(key) {
					        var value = this.getItem(key);
					        return value && JSON.parse(value);
					    }
					}
					
					// 관심 브랜드 리스트
					var brandList = [];
					
					if(common.isNotEmpty(localStorage.getObject('brandIds'))){
						brandList = localStorage.getObject('brandIds');
					}
					
					if(common.isNotEmpty(index)){
						brandList.splice(index, 1);
						
					}else if(common.isNotEmpty(brandId)){
						var dup = true;
						for (i in brandList) {
							if (brandList[i].brandId == brandId) {
								brandList.splice(i, 1);
								dup = false;
							}
						}
						if(dup){
							brandList.push({
								brandId : brandId, name : name
							});
						}
					}
					
					localStorage.setObject('brandIds', brandList );
					
					var htmlList=[];
					$.each( brandList, function( index, value ) {
						var html ="<li>"
							+"		<a href=\"#none1\">"+ value.name + "</a>"
							+"		<label class=\"chk_style1\">"
							+"			<em>"
							+"				<input type=\"checkbox\" value=\"\" onClick=\"dmsmb.lnb.brand.change('','','" + index + "');\" checked/>"
							+"			</em>"
							+"			<span>즐겨찾기</span>"
							+"		</label>"
							+"	</li>";
						htmlList.push(html);
					});
					
					
					$('#likeBrandResult').empty();
					$('#likeBrandResult').append(htmlList);
				},
				
				initSortList : function(){
					var htmlList=[];
					$.each( dmsmb.lnb.brand.Consonant, function( index, value ) {
						//var html ="<li "+(index == 1 ?" class='on'":"")+">"
						var html ="<li class=\"swiper-slide\">"
							+"<a onClick=\"dmsmb.lnb.brand.search_Consonant('"+value.name+"', '"+value.unicodeBefore+"', '"+value.unicodeAfter+"');\">"+value.name+"</a>"
							+"</li> ";
						htmlList.push(html);
					});
					$('#consonantList').append(htmlList);
					var htmlList2=[];
					$.each( dmsmb.lnb.brand.Spelling, function( index, value ) {
						//var html ="<li "+(index == 1 ?" class='on'":"")+">" '{searchKeyword:"+value+"}'
						var html ="<li class=\"swiper-slide\">"
							+"<a onClick=\"dmsmb.lnb.brand.search('"+value+"');\">"+value+"</a>"
							+"</li> ";
						htmlList2.push(html);
					});
					$('#spellingList').append(htmlList2);
					dmsmb.lnb.brand.changeLikeBrand();
					
					var target_name = $('#consonantList');
					$("a", target_name).on("click", function(event) {
						$("li", target_name).removeClass("on").eq( $("li", target_name).index( $(this).parent() ) ).addClass("on");
					});
					var target_name2 = $('#spellingList');
					$("a", target_name2).on("click", function(event) {
						$("li", target_name2).removeClass("on").eq( $("li", target_name2).index( $(this).parent() ) ).addClass("on");
					});
				},
				
				hideSearch : function(){
					$(" .sc_brand", $(".mobile .all_menu")).hide();
				}
			},
			
			// 고객명 조절
			nameLengthAdjust : function(name, mobile, app) {
				
				var temp = name.replace(/[A-Z|a-z]/g, '');
				var _length = 0;
				
				// MOBILE
				if (mobile) {
					// 한글
					if (common.isNotEmpty(temp)) {
						if (app) {
							if (name.length > 10) _length = 10;
						}
					}
					// 영문
					else {
						if (app) {
							if (name.length > 18) _length = 18;
						} else {
							if (name.length > 23) _length = 23;
						}
					}
				}
				
				if( _length > 0) {
					name = name.substr(0, _length);
					$('#loginName').html('<em>'+ name +'… 님</em>');
				}
			},
			
			// 쇼핑 알림 보관함 count
			alarmCnt : function() {
				$.ajax({
					url : "/api/mms/mypage/shoppingAlarm/count",
					type : "POST"		
				}).done(function(response){
					$("#alarmCnt").text(response);
				})
			}
		},
		
		/********************************************************
		 * RNB 자사 브랜드 리스트 조회
		 ********************************************************/
		rnb : {
			
			
			
		},
		
		
		/********************************************************
		 * 헤더
		 ********************************************************/
		header : {
			/***************************************************************
			 * 공통영역 장바구니 : dmsmb.header.getCartCount
			 ***************************************************************/
			getCartCount :function(){
				$.post( "/api/oms/cart/count", function(response) {
					$('.bottom_menu .btn_bot4 em').html(response.totalCnt);
				},"json")
				.fail(function(xhr, status, error) {
				}); 
			},
			 //인기검색어
			getPopularSearch :function (){
				var param ={"type":"P","range":"day"};
				var returnHtml="";
				
				//console.log(value.content); //검색어
				//console.log(value.updown); //인기검색어 순위 변동 구분 
				//console.log(value.id); 	//인기검색어 순위 
				//console.log(value.querycount); 
				//console.log(value.count); //조회건수

				dms.common.searchApi(param, function(data){
					$.each(data.Query, function(index, value){
						var html ="<li "+(Number(value.id)<5? 'class="rank_top"':"")+">"
									+ "<a href=\"javaScript:dms.common.setKeyWord('"+value.content+"')\">"
									+ "<em>"+value.id+"</em>"
									+ value.content
									+ "</a>"
									+ "</li>";
						returnHtml+=html;
					});
					$('#rankSearch_mb').append(returnHtml);
				});
			},
			
			/***************************************************************
			 * 최근 검색어 : dmsmb.header.latelySearch
			 ***************************************************************/
			latelySearch : function(){
				param = {'name':"latelyKeyword"};
				$.post('/api/dms/search/latelySearch', function(data) {
					addLatelySearchHtml(data);
				}).fail(function(xhr, status, error) {
					//console.log("code:"+status+"\n"+"error:"+error);
				});
				
				var addLatelySearchHtml = function(data){
					$('#latelySearch').empty();
					var str =[];
					var returnHtml="";
					if(data !=null && data !==""){
						data = data.replace(/'/g,"");
						str= data.split(",");
						$.each(str, function(index, value){
							if(index <= 10){
								var keyWord = decodeURI(decodeURIComponent(value));
								var html ="<li>"
									+ "<a href=\"javascript:dms.common.setKeyWord('"+keyWord+"');\">"+keyWord+"</a>"
									+ "<button type=\"button\" class=\"btn_strDel\" onclick=\"dmsmb.header.deleteKeyWord('"+keyWord+"');\">검색어 지우기</button>"
									+ "</li>";
								returnHtml+=html;
							}
						});
						$('#latelySearch').append(returnHtml);
					}
				}
			},
			
			/******************************************************************
			 * 검색어 삭제 : dms.search.deleteKeyWord
			 *****************************************************************/
			deleteKeyWord : function(keyWord){
				param = {}
				if(keyWord != undefined){
					param['type']='ONE';
					param['keyWord']=encodeURI(encodeURIComponent(keyWord));
				}else{
					param['type']='ALL';
					param['keyWord']="";
				}
				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url:"/api/dms/search/deleteSearch",
					type: "POST",
					data: JSON.stringify(param),
					dataType:"json",
					success: function(data){
						dmsmb.header.latelySearch();
					},
				    error:function(request,status,error){
				    	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				    }
				});
			},
			
			searchKeyUp : function (obj){
				//var value= obj.value.replace(/ /gi, '');
				var value= obj.value;
				var returnHtml="";
				var param ={"searchWord":value,"type":"A"};
				
				if(value !== ''){
					dms.common.searchApi(param, function(data){
						$('#autoSearch .link').empty();
						$.each(data, function(num,item){
							var html ="<li>"
								+ "<a href=\"javaScript:dms.common.setKeyWord('"+item.keyword+"')\">"
								+ "<i>"+item.keyword.substring(0,1)+"</i>"+item.keyword.substring(1,item.keyword.length)
								+ "</a>"
								+ "</li>";
							returnHtml+=html;
						});
						$('#autoSearch .link').append(returnHtml);
					});
					
					//검색메인으로 이동
					if(event.keyCode==13){
						//var keyWord = obj.value;
						dms.common.setKeyWord(value);
					}
				}
			
			}
		}
}
