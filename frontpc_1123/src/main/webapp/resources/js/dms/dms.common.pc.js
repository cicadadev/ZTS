/**
 * 전시공통 PC 스크립트
 */

var cursorPos = -1;								// 자동완성 커서 위치 값
var tempQuery= "";								// 자동완성 커서 위치 값
var totalFwCount = 0;							// 전방 검색 전체 개수
var totalRwCount = 0;							// 후방 검색 전체 개수
var isKeyup = true;								// 자동검색 여부
var cornerNo = global['corner'];

$(document).ready(function(){
	
	//장바구니 카운트
	//dmspc.header.getCartCount();// loginBtnbox 에서 호출로 변경
	dmspc.common.getCommonConner();
	dmspc.gnb.getCommonGnb();
	dmspc.skyScraper.getSkyScraper();
	dmspc.header.getPopularSearch();
	dmspc.header.latelySearch();
	
});

var imageUrl = global.config.imageDomain;
dmspc = {
		
		common : {
			/***************************************************************
			 * 공통영역 코너 목록조회 
			 ***************************************************************/
			getCommonConner :function(){
				//common.showLoadingBar();
				$.post( "/api/dms/corner/connerList", function(response) {
					cornerInfo = response;
					corner(response);
					//common.hideLoadingBar();
				},"json")
				.fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
					//common.hideLoadingBar();
				}); 
				
				var corner = function(object){
					$.map( object, function( value, key ) {
				    	//console.log('##keys:'+key+'/value:'+value.name);
				    	
				    	var divHtml ="";
				    	
				    	//SkyScraper(우측) 이미지배너 코너 
				    	if(key === cornerNo.skyScraperRgt){
				    		var ulHtml ="";
				    		if(value.dmsDisplayitems != null){
				    			var pghtml="";
				    			$.each( value.dmsDisplayitems, function( index, item ) {
				    				html ="<li "+((index+1) == 1 ?" class='swiper-slide on'":"class='swiper-slide'")+">"
				    				+"<a href='" +item.url1 +"'>"
				    				+"<img src=\""+imageUrl+item.img1+"\" alt=\"\" />"
				    				//	+"<img src=\"/resources/img/pc/temp/sky_banner"+(index+1)+".jpg\" alt=\"\" />"
				    				+"</a>"
				    				+"</li>";
				    				ulHtml+=html;
				    				
				    				phtml = "<li "+((index+1) == 1 ?" class='on'":"")+">"
				    				+"<button type=\"button\">"+(index+1)+"</button>"
				    				+"</li>";
				    				pghtml+=phtml;
				    			});
				    			$('#corner'+key+'Paging').append(pghtml);
				    		}
				    		$('#corner'+key+' > div > ul').append(ulHtml);
				    		
				    		swiperCon('comSwiper_quickBanner', 400, 1, 0, 2000, true);
				    	}
				    	
				    	//검색어 Text배너 코너 
				    	if(key === cornerNo.searchWord){
				    		if(value.dmsDisplayitems != null){
				    			var searchDiv = "";
				    			$.each(value.dmsDisplayitems, function( index, item ) {
				    				html = "<span "+(index == 0 ?" class='active'":"")+" data-url="+item.url1+">"+item.title+"</span>";
				    				searchDiv+=html
				    			});
				    			$('#isTextBanner').val("Y");
				    			$('#corner'+key+" label").append(searchDiv);
				    		}
				    		
						//SkyScraper(좌측) 이미지배너 코너 
				    	}else if(key === cornerNo.skyScraperLeft){
				    		if(value.dmsDisplayitems != null){
				    			divHtml = "<a href=\""+ value.dmsDisplayitems[0].url1 +"\">"
								+"<img src=\""+imageUrl+value.dmsDisplayitems[0].img1+"\" alt=\"\" />"
								+"</a>";
				    			
				    			$('#corner'+key).append(divHtml);
				    		}
				    	
				    	//월령별 이미지배너 코너 
				    	}else if(key === cornerNo.ageImg){
				    		if(value.dmsDisplayitems != null){
				    			$.each( value.dmsDisplayitems, function( index, item ) {
				    				html = "";
				    				if(item != null && item != ''){
				    					html = "<a href='"+item.url1+"'>"
				    						+"<img src=\""+imageUrl+item.img1+"\" alt=\"\" />"
				    						+"<span>월령별</span>"
				    						+"</a>";
				    				}
				    				divHtml+=html;
				    			});
				    			
				    			$('#corner'+key).append(divHtml);
				    		}
				    	}
				    	
				    	
				    	//$('#corner'+key).append(divHtml);
				    
					});
				}
			}
		},
		header : {
			
			/***************************************************************
			 * 공통영역 장바구니 : dmspc.header.getCartCount
			 ***************************************************************/
			getCartCount :function(){
				//common.showLoadingBar();
				$.post( "/api/oms/cart/count", function(response) {
					$('.btn_cart em').html("("+response.totalCnt+")");
					//common.hideLoadingBar();
				},"json")
				.fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
					//common.hideLoadingBar();
				}); 
			},
			
			/***************************************************************
			 * 인기검색어 : dmspc.header.getPopularSearch
			 ***************************************************************/
			getPopularSearch : function (){
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
									+ "<em>"+value.id+"</em>"
									+ "<a href=\"javaScript:dms.common.setKeyWord('"+value.content+"')\">"+value.content+"</a>"
									+ "</li>";
						returnHtml+=html;
					});
					$('#rankSearch_pc').append(returnHtml);
				});
			},
			
			/***************************************************************
			 * 즐겨찾기 : dmspc.header.addFavorite
			 ***************************************************************/
			addFavorite :function (){
				
				//console.log(document);
				var bookmarkURL = window.location.href;
			    var bookmarkTitle = document.title;
			    var triggerDefault = false;

			    if (window.sidebar && window.sidebar.addPanel) {
			        window.sidebar.addPanel(bookmarkTitle, bookmarkURL, '');
			    } else if ((window.sidebar && (navigator.userAgent.toLowerCase().indexOf('firefox') > -1)) || (window.opera && window.print)) {
			        var $this = $(this);
			        $this.attr('href', bookmarkURL);
			        $this.attr('title', bookmarkTitle);
			        $this.attr('rel', 'sidebar');
			        $this.off(e);
			        triggerDefault = true;
			    } else if (window.external && ('AddFavorite' in window.external)) {
			        window.external.AddFavorite(bookmarkURL, bookmarkTitle);
			    } else {
			        alert((navigator.userAgent.toLowerCase().indexOf('mac') != -1 ? 'Cmd' : 'Ctrl') + '+D 키를 눌러 즐겨찾기에 등록하실 수 있습니다.');
			    }

			    return triggerDefault;
			},
			
			/***************************************************************
			 * 검색창 링크 :dmspc.header.searchLink
			 ***************************************************************/
			searchLink : function(){
				console.log("searchLink");
				var isTextBanner = $('#isTextBanner').val();
				
				if(isTextBanner === 'Y'){
					if($('#textBannerUrl').val() =='' || $('#textBannerUrl').val() == "null"){
						var value = $("label span.active").text();
						if(value !== '' && value != null){
							dms.common.setKeyWord(value);
						}
					}else{
						location.href = $('#textBannerUrl').val();
					}
				}else{
					var keyword = $('#header_search').val();
					if(keyword !== ''){
						dms.common.setKeyWord(keyword);
					}
				}
			},
			
			/***************************************************************
			 * 바로방문_ URL 바로가기 : dmspc.header.copy_clipboard
			 ***************************************************************/
			copy_clipboard : function(){

				var agent = navigator.userAgent.toLowerCase();
				 
				if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
					window.clipboardData.setData("Text", "http://www.0to7.com");
					alert("주소(http://www.0to7.com)가 복사되었습니다. 주소입력창에서 ctrl+V를 눌러주세요");
				}
				else {
					temp = prompt("이 0to7의 사이트 주소입니다. Ctrl+C를 눌러 클립보드로 복사하세요", "http://www.0to7.com");
				}
			},
			
			/***************************************************************
			 * 최근 검색어 : dmspc.header.latelySearch
			 ***************************************************************/
			latelySearch : function(){
				param = {'name':"latelyKeyword"};
				$.post('/api/dms/search/latelySearch', function(data) {
					addLatelySearchHtml(data);
				}).fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
				});
				
				var addLatelySearchHtml = function(data){
					$('#latelySearch').empty();
					var str =[];
					var returnHtml="";
					if(data !=null && data !==""){
						data = data.replace(/'/g,"");
						str= data.split(",");
						$.each(str, function(index, value){
							if(index < 10){
								var keyWord = decodeURI(decodeURIComponent(value));
								var html ="<li>"
									+ "<a href=\"javascript:dms.common.setKeyWord('"+keyWord+"');\">"+keyWord+"</a>"
									+ "<button type=\"button\" class=\"btn_strDel\" onclick=\"dmspc.header.deleteKeyWord('"+keyWord+"');\">검색어 지우기</button>"
									+ "</li>";
								returnHtml+=html;
							}
						});
						$('#latelySearch').append(returnHtml);
					}
				}
			},
			
			/******************************************************************
			 * 검색어 삭제 : dmspc.header.deleteKeyWord
			 *****************************************************************/
			deleteKeyWord : function(keyWord){
				console.log(keyWord);
				param = {}
				if(keyWord != undefined){
					param['type']='ONE';
					param['keyWord']=encodeURI(encodeURIComponent(keyWord));
					console.log("EEEEEEEEEEEEE",param['keyWord']);
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
						dmspc.header.latelySearch();
					},
				    error:function(request,status,error){
				    	console.log("code:"+request.status+"\n"+"error:"+error);
				    } 
				});
			},
			
			/******************************************************************
			 * 자동완성 검색어 방향키 조절 : dmspc.header.moveFocusEvent
			 *****************************************************************/
			moveFocusEvent : function (event) {
				isKeyup = false;
				
				//방향키 위
				if (event.keyCode == 38) {
					if (cursorPos==-1 || cursorPos==0) {
						cursorPos = -1;
						$('#header_search').val(tempQuery);
						tempQuery = "";
						
						if(cursorPos = -1){
							isKeyup = true;
						}
						
					} else {
						dmspc.header.onMouseOutKeyword(cursorPos);
						cursorPos = cursorPos - 1;
						dmspc.header.onMouseOverKeyword(cursorPos);
						$('#header_search').val($("#f" + cursorPos).val());
					}
				//방향키 아래
				} else if (event.keyCode == 40) {
					if(cursorPos == -1) {
						tempQuery = $('#header_search').val();
					}
					if ((totalFwCount + totalRwCount) > (cursorPos+1)) {
						dmspc.header.onMouseOutKeyword(cursorPos);
						cursorPos = cursorPos + 1;
						dmspc.header.onMouseOverKeyword(cursorPos);
						$('#header_search').val($("#f" + cursorPos).val());
					}
				}
			},
			
			/*****************************************************************
			 * MouseOut 일 경우 설정한 배경을 초기화 : dmspc.header.onMouseOutKeyword
			 * @param cursorNum 커서의 위치 인덱스 값
			 *****************************************************************/
			onMouseOutKeyword : function (curSorNum) {
				cursorPos = curSorNum;
				$("#autoKey_" + cursorPos).css({"background-color" : "#ffffff"});
			},

			/******************************************************************
			 * MouseOver 일 경우 선택한 배경을 설정 : dmspc.header.onMouseOverKeyword
			 * @param cursorNum 커서의 위치 인덱스 값
			 ******************************************************************/
			onMouseOverKeyword : function (cursorNum) {
				dmspc.header.clearCursorPos();
				cursorPos = cursorNum;
				$("#autoKey_" + cursorNum).css({"background-color" : "#eeeeee"});
				$("#autoKey_" + cursorNum).css({"cursor" : "pointer"});
			},

			/******************************************************************
			 * 커서 위치가 변경될 때마다 선택되지 않은 부분 초기화 : dmspc.header.clearCursorPos
			 ******************************************************************/
			clearCursorPos : function () {
				for(var i=0; i<(totalFwCount + totalRwCount); i++){
					$("#autoKey_" + i).css({"background-color" : "#ffffff"});
				}
			},
			
			/******************************************************************
			 * 추천 카테고리, 브랜드, 기획전 : dmspc.header.recommend
			 ******************************************************************/
			recommend : function(param, callback){
				
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url:"/api/dms/search/recommend",
					type: "POST",
					data: JSON.stringify(param),
					dataType:"json",
					success: function(data){
						if(data.message =='SUCCESS'){
							callback(data);
						}
					},
				    error:function(request,status,error){
				    	console.log("code:"+request.status+"\n"+"error:"+error);
				    	common.hideLoadingBar();
				    }
				});
			},
			
			/******************************************************************
			 * 추천 카테고리, 브랜드, 기획전 : dmspc.header.recommendAddHtml
			 ******************************************************************/
			recommendAddHtml : function(param){
				$('#getRecommendBrand').empty();
	    		$('#getRecommendExhibit').empty();
	    		$('#getRecommendCategory').empty();
				dmspc.header.recommend(param, function(data){
		    		var exhibitHtml="";
		    		var categoryHtml="";
		    		var brandHtml="";
		    		
		    		if(data['brandList'] == undefined && data['exhibitList'] == undefined && data['categoryList'] == undefined){	
						$('#recommend').hide();
					}else{
						$('#recommend').show();
					}
		    		
					if(data['brandList'] != undefined){
						$('#getRecommendBrand').empty();
						brandHtml = "<span class=\"icon_type1 iconBlue3\">브랜드</span>";
						$.each( data['brandList'], function( index, item ) {
							var html="";
							if(index > 0){
								if(item != null && item != undefined){
									if(item.corporateYn =='Y'){
										html = " ,<a href=\"javascript:brand.template.main('"+item.brandId+"');\">"+item.name+"</a>";
									}else{
										html = " ,<a href=\"javascript:ccs.link.product.brandProdutList('BRAND','"+item.brandId+"','','');\">"+item.name+"</a>";
									}
								}
							}else{
								if(item != null && item != undefined){
									if(item.corporateYn =='Y'){
										html = "<a href=\"javascript:brand.template.main('"+item.brandId+"');\">"+item.name+"</a>";
									}else{
										html = "<a href=\"javascript:ccs.link.product.brandProdutList('BRAND','"+item.brandId+"','','');\">"+item.name+"</a>";
									}
								}
							}
							
							brandHtml+=html;
						});
						$('#getRecommendBrand').append(brandHtml);
					}
					
					if(data['exhibitList'] != undefined){
			    		$('#getRecommendExhibit').empty();
						exhibitHtml="<span class=\"icon_type1 iconBlue3\">기획전</span>";
						$.each( data['exhibitList'], function( index, item ) {
							var html ="";
							if(index > 0){
								if(item != null && item != undefined){
									html = " ,<a href=\"/dms/exhibit/detail?exhibitId="+item.exhibitId+"\">"+item.name+"</a>";
								}
							}else{
								if(item != null && item != undefined){
									html = "<a href=\"/dms/exhibit/detail?exhibitId="+item.exhibitId+"\">"+item.name+"</a>";
								}
							}
							exhibitHtml+=html;
						});
						$('#getRecommendExhibit').append(exhibitHtml);
					}
					
					if(data['categoryList'] != undefined){
			    		$('#getRecommendCategory').empty();
						categoryHtml = "<span class=\"icon_type1 iconBlue3\">카테고리</span>";
						$.each( data['categoryList'], function( index, item ) {
							var html ="";
							if(index > 0){
								if(item != null && item != undefined){
									html = " ,<a href=\"javascript:ccs.link.display.dispTemplate('"+item.displayCategoryId+"');\">"+item.depthFullName+"</a>";
								}
							}else{
								if(item != null && item != undefined){
									html = "<a href=\"javascript:ccs.link.display.dispTemplate('"+item.displayCategoryId+"');\">"+item.depthFullName+"</a>";
								}
							}
							categoryHtml+=html;
						});
						$('#getRecommendCategory').append(categoryHtml);
					}
					
				});
			},
			
			/********************************************************
			 * 검색자동완성
			 ********************************************************/
			searchKeyUp : function (obj){
				var value= obj.value;
				if(value !== ''){
					if(isKeyup){
						$('#isTextBanner').val("N");
						var param ={"searchWord":value,"type":"A"};
						
						dms.common.searchApi(param, function(data, totalCount){
							var returnHtml="";
							if (totalCount == 0) {
								totalFwCount = totalCount;
							} else {
								totalRwCount = totalCount;
							}
							
							$('#autoSearch .link').empty();
							$.each(data, function(num,item){
								//callback(item.keyword);
								var html ="<li id=\"autoKey_"+num+"\">"
								+ "<input type=\"hidden\" id=\"f" + num + "\" value=\""+item.keyword+"\"/>"
								+ "<a href=\"javaScript:dms.common.setKeyWord('"+item.keyword+"')\">"
								+ "<i>"+item.keyword.substring(0,1)+"</i>"+item.keyword.substring(1,item.keyword.length)
								+ "</a>"
								+ "</li>";
								returnHtml+=html;
							});
							
							$('#autoSearch .link').append(returnHtml);
						});
						
						var value= obj.value.replace(/ /gi, '');
						var pattern1 = /([^가-힣\x20])/i; //한글인데 자음 모음 중 하나 없는 문자
						var pattern2 = /([^가-힣ㄱ-ㅎㅏ-ㅣ\x20])/i; //한글 자음 모음을 제외한 모든 문자
						var pattern3 = /^[0-9a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ\x20]*$/gi; //특수문자, 이모티콘을 제외한 문자
						var pattern4 = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi; //특수문자
						var str = value.substring(0,1);
						var c=value.charCodeAt(value); 
						
						//( 0xAC00 <= c && c <= 0xD7A3 ) 초중종성이 모인 한글자 
						//( 0x3131 <= c && c <= 0x318E ) 자음 모음 
						if( !( ( 0xAC00 <= c && c <= 0xD7A3 ) || ( 0x3131 <= c && c <= 0x318E ) ) ) { 
							//console.log("한글을 제외한 모든 문자");
							//특수문자 체크를 제외한 문자
							if(pattern3.test(str)){
								
						    	//console.log("특수문자 제외"); 
						    	var param ={"searchWord":str};
						    	dmspc.header.recommendAddHtml(param);
						    }else{
						    	$('#recommend').hide();
						    }
						}else{ 
							if (!pattern1.test(str)) {
								//console.log("한글 단어");
								//ajax
								var param ={"searchWord":str};
								dmspc.header.recommendAddHtml(param);
							}else{
								$('#recommend').hide();
							}
						}
					}
					
					//검색메인으로 이동
					if(event.keyCode==13){
						var keyWord = obj.value;
						dms.common.setKeyWord(keyWord);
					}
					
					//방향키 조절
					if(event.keyCode==40 || event.keyCode==38){
						dmspc.header.moveFocusEvent(event);
					}
				}
			}
		},
		
		gnb : {
			/***********************************************************************
			 * GNB : 전체카테고리, 월령, 브랜드, 전문관 조회
			 **********************************************************************/
			getCommonGnb : function(){
				var allCategoryList = new Array;
				var ageCodeList = new Array;
				var corner;
				
				//ajax 호출
				$.post( "/api/dms/common/gbnInfo", function(response) {
					
					allCategoryList = response.ctgList;
					ageCodeList = response.ageCodeList;
					corner =  response.corner;
					categoryHtml(allCategoryList,corner);
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
				var categoryHtml = function(object, corner){
					//var pcHtmlList=[];
					
					//var corner = value.dmsDisplayitems;
					var clickHtml = "";
					var text = "";
					var corner_1 ="";
					var corner_2 ="";
					var corner_3 ="";
					var corner_4 ="";
					
					var url_1 ="";
					var url_2 ="";
					var url_3 ="";
					var url_4 ="";
					
					if(corner != null){
						$.map( corner, function( value, key ) {
						
							if(value.dmsDisplayitems != null){
								if(key == cornerNo.ctgImg1){
									//console.log('##keys:'+key+'/value:'+JSON.stringify(value.dmsDisplayitems[0].img1));
									corner_1 = value.dmsDisplayitems[0].img1;
									url_1 = value.dmsDisplayitems[0].url1;
								}
								
								if(key == cornerNo.ctgImg2){
									//console.log('##keys:'+key+'/value:'+JSON.stringify(value.dmsDisplayitems[0].img1));
									corner_2 = value.dmsDisplayitems[0].img1;
									url_2 = value.dmsDisplayitems[0].url1;
								}
								
								if(key == cornerNo.ctgImg3){
									//console.log('##keys:'+key+'/value:'+JSON.stringify(value.dmsDisplayitems[0].img1));
									corner_3 = value.dmsDisplayitems[0].img1;
									url_3 = value.dmsDisplayitems[0].url1;
								}
								
								if(key == cornerNo.ctgImg4){
									//console.log('##keys:'+key+'/value:'+JSON.stringify(value.dmsDisplayitems[0].img1));
									corner_4 = value.dmsDisplayitems[0].img1;
									url_4 = value.dmsDisplayitems[0].url1;
								}
							}
						});	
					}
					
					//console.log(object.length);
					if(object.length > 0 && object != null){
						$.each( object, function( index, value ) {
							if(index < 4){
								var image = "";
								var url = "";
								if(index ==0){
									image = corner_1;
									url =url_1;
								}else if(index ==1){
									image = corner_2;
									url =url_2;
								}else if(index ==2){
									image = corner_3;
									url =url_3;
								}else if(index ==3){
									image = corner_4;
									url =url_4;
								}
								//console.log("index:",index);
								//console.log("image:",image);
								//console.log("url:",url);
								
								clickHtml ="<dt>"
									+"<a href='"+url+"'>"
									+"<img src=\""+imageUrl+image+"\" alt=\"\" />"
									+"<span>"+value.name+"</span>"
									+"</a>"
									+"</dt>"
									+"<dd>"
									+ depth2Html(value.dmsDisplaycategorys,value.pageSize,value.totalCount, (index == 3?true:false))
									+"</dd>";
								
								$('#pcCategory_'+(index+1)).append(clickHtml);
							}
							//pcHtmlList.push(clickHtml);
						});
					}
				}

				// 전체 카테고리 depth2 HTML
				var depth2Html = function(object,pageSize,totalCount,divYn){
					var returnHtml="";
					if(Number(object.length) >0){
						$.each( object, function( index, value ) {
							var html="";
							if(value.displayCategoryId !== null){
								html= "<a href=\"#none\" onclick=\"ccs.link.display.dispTemplate('"+value.displayCategoryId+"')\">"+value.name+"</a>";
							}
							returnHtml+=html;
						
						});
					}
					return returnHtml;
				}
				
				// 전체 카테고리 depth3 HTML
				var depth3Html = function(object){
					var returnHtml="";

					if(Number(object.length) >0){
						$.each( object, function( index, value ) {
							var html="";
							if(value.displayCategoryId !== null){
								html="<li>"
									+"<a href=\"#none\" onclick=\"ccs.link.display.dispTemplate('"+value.displayCategoryId+"')\">"+value.name+"</a>"
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
						var html ="<a href=\"/dms/display/ageShop?ageCode="+cdoeValue+"\">"+value.name+"</a>";
						htmlList.push(html);
					});
					$('#ageCode').append(htmlList);
					//console.log($('div.floatG dl').children("dd")[0].innerHTML);
				}
			}
		},
		
		skyScraper : {
			/***************************************************************
			 * 공통영역 최근본상품, 찜한상품, 추천상품, 내지갑 조회
			 ***************************************************************/
			getSkyScraper : function (){
				//common.showLoadingBar();
				$.get( "/api/dms/common/skyScraper", null ).done(function( html ) {
					$(".sky_scraper .list").html(html);
					dmspc.skyScraper.btnShow();	
					//common.hideLoadingBar();
				});
			},
			btnShow : function() {
				$(".sky_scraper .btn_show").off("click").on({
					"click" : function(e) {
						var id = $(this)[0].id;
						
						if( $(this).parent().hasClass("on") ){
							$(this).parent().removeClass("on");
							if(id == "wish") {
								$("#latestPrShow").parent().removeClass('on');
							} else if(id == "latest") {
								$("#wiShow").parent().removeClass('on');
							}
						}else{
							$(this).parent().addClass("on");
							if(id == "wish") {
								$("#latestPrShow").parent().removeClass('on');
							} else if(id == "latest") {
								$("#wiShow").parent().removeClass('on');
							}
						}
					}
				});
				
				$(".sky_scraper .showroom button").off("click").on({
					"click" : function() {
						var target_tag = $(this).parent().siblings("ul");
						var target_length = $(" li", target_tag).length;
						var now_num = $(this).siblings("span").find("i").text();
						var max_num = Math.round(target_length / 2);

						if( $(this).hasClass("btn_prev") ){
							now_num--;
							if( now_num < 1){
								now_num = max_num;
							}
						}else if( $(this).hasClass("btn_next") ){
							now_num++;
							if( now_num > max_num){
								now_num = 1;
							}
						}

						$("li", target_tag).removeClass("on")
							.eq( now_num*2 - 2 ).addClass("on")
							.end().eq( now_num*2 - 1).addClass("on");

						$(this).siblings("span").find("i").text(now_num);
					}
				});
			}
		}
		
}