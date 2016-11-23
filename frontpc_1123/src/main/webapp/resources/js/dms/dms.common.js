/**
 * 업무 : 전시 
 * 페이지 : 카테고리 매장(대,중,소), 검색 , 템플릿
 */

// 검색API param
var optionParam = {}

//optionFilter 히스토리 param
var optionHistory={'categoryId':[],
					'brandId':[],
					'ageTypeCode':[],
					'inpGender':{},
					'material':[],
					'material1':[],
					'couponYn':{},
					'deliveryFreeYn':{},
					'pointSaveYn':{},
					'presentYn':{},
					'regularDeliveryYn':{},
					'offshopPickupYn':{},					
					'color':[]}
//선택해제를 위한
var optionName = ["categoryId",
                  "brandId", 
                  "ageTypeCode", 
                  "material", 
                  "material1",
                  "couponYn",
                  "deliveryFreeYn",
                  "pointSaveYn",
                  "regularDeliveryYn",
                  "offshopPickupYn",
                  "color",
                  "presentYn"];

//옵션선택시 해당카테고리 select하기 위한 변수
var brandSearchYn = "N";
var colorList = [];
//검색에서 List를 String으로 form에 저장할때 []를 없애기 위한 정규식
var regExp = /[\{\}\[\]\/?.;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;

//function
dms ={
		
		common : {
		
			/***************************************************************
			 * 검색API : 인기검색어(P) & 자동완성(A) & 연관검색어(R) : dms.common.searchApi
			 * 	result[0].items : 검색어로 시작하는 단어들
			 * 	result[1].items : 검색어로 끝나는 단어들
			 ***************************************************************/
			searchApi : function(param, callback) {
				var type = param['type'];
				$.ajax({
					contentType : "application/json; charset=UTF-8",
					url:"/api/dms/search/searchApi",
					type: "POST",
					data: JSON.stringify(param),
					dataType:"json",
					success: function(data){
						if(type === 'A'){
							var totalCount = parseInt(data.result[0].totalcount);
							if(totalCount > 0){
								//검색어로 시작하는 단어들
								callback(data.result[0].items, totalCount);
								
								//검색어로 끝나는 단어들
								//callback(data.result[1].items);
							}
						}else{
							callback(data.Data);
						}
					},
				    error:function(request,status,error){
				    	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				    	common.hideLoadingBar();
				    }
				});
			},
			
			/***************************************************************
			 * 검색상품목록 페이징 : dms.common.searchPaging
			 ***************************************************************/
			searchPaging :function(map){
				
				var param = {'currentPage' : map["currentPage"], 
						'pageSize' : map["pageSize"]
						};
				dms.search.option.getProduct(param,"");
				
			},

			/***************************************************************
			 * 검색상품색인 API : dms.common.changePage
			 ***************************************************************/
			changePage : function (param,callback){
				
				common.showLoadingBar();
				$.post('/dms/search/productList',param, function(html) {
					callback(html);
					if($('#optionBrandSrcYn').val() === 'Y'){
						dms.search.option.categoryChange();
					}
					common.hideLoadingBar();
				}).fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
				});
			},
			
			/***************************************************************
			 * selectBox label text노출을 위한 style : dms.common.selectStyle
			 ***************************************************************/
			selectStyle : function(){
				$(".select_box1 select").each(function() {
					$(this).siblings('label').text( $("option:selected", this).text() );
				});
			},
			
			/***************************************************************
			 * 검색메인페이지로 이동 : dms.common.setKeyWord
			 * encoding keyWord
			 ***************************************************************/
			setKeyWord : function (keyWord){
				//var keyWord= value.replace(/ /gi, '');
				//코너의 텍스트배너와 중복되서 깨짐 방지를 위한 empty
				$('.search_box label').empty();
				$('#header_search').val(keyWord);
				var param = '';
				
				if(_BRAND_ID != ''){
					param = "&brandShopId="+_BRAND_ID
				}
				
				window.location.href = '/dms/search/main?keyword='+encodeURI(encodeURIComponent(keyWord))+param;
			},
			
			/***************************************************************
			 * 코너 상품목록 조회 : dms.common.getCornerProductList
			 ***************************************************************/
			getCornerProductList : function (param, callback){
				$.post('/dms/corner/productList',param, function(html) {
					callback(html);
				}).fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
				});
			},
			
			/***************************************************************
			 * 코너 카테고리아이템 조회 : dms.common.getCornerProductList
			 ***************************************************************/
			getCategoryCornerItems: function (param, callback){
				$.post('/dms/corner/categoryCornerItems',param, function(html) {
					callback(html);
				}).fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
				});
			},
			
			/***************************************************************
			 * 모바일 검색 상품목록 스크롤 페이징 처리 : dms.common.searchMobilePage
			 **************************************************************/
			searchMobilePage : function() {
				
				$(window).scroll(function() {
					//$(window).scrollTop(0);
					var rowCount = $("#productList").children().find("li").length;
					var totalCount = Number(dms.search.list.uncomma($("#searchCount").text()));
					var maxPage = Math.ceil(totalCount/40);
					var currentPage = Math.ceil(rowCount/40);
					
					var maxHeight = $(window, document).scrollTop();
					var currentScroll = $(document).height() - $(window).height();
					
					if($(document).height()> 0){
						
						if (maxHeight == currentScroll) {
							
							//console.log("currentScroll:", currentScroll);
							//console.log("maxHeight:", maxHeight);
							//console.log("rowCount:", rowCount);
							//console.log("totalCount:", totalCount);
							
							if((currentPage+1 <= maxPage) && (rowCount < totalCount) && totalCount > 0){
								
								//console.log("currentPage:", currentPage+1);
								//console.log("maxPage:", maxPage);
								//$('#moCurrentPage').val(currentPage+1);
								param ={'currentPage' : currentPage+1}
								dms.search.option.getProduct(param,"add");
							}
						}
					}
				}); 
			}
		},
		
		
		/***************************************************************
		 * 검색 화면 :  dms.search
		 ***************************************************************/
		search : {

			/*****************************************************************
			 * 검색페이지의 OptionFilter : dms.search.option
			 *****************************************************************/
			option: {

				//성별 라디오박스 초기화 : dms.search.option.genderInit
				genderInit : function(){
					$('input:radio[name="inpGender"]').closest("em").removeClass("selected");
					$('input:radio[name=inpGender]').prop("checked", false);
					$('input:radio[name=inpGender]:input[value=ALL]').closest("em").addClass("selected");
					$('input:radio[name=inpGender]:input[value=ALL]').prop("checked", true);
					
					var genderCode = $(':input:radio[name=inpGender]:checked').val();
					if(genderCode !==''){
						var param = {'genderTypeCod': genderCode};
						$.extend( optionParam, param );
						
						//var history={'name':$('#gender_'+genderCode).text(), 'id':genderCode, 'show': true}
						//dms.search.option.addCheckedOption('inpGender',history);
					}
				},
				
				//가격 초기화 : dms.search.option.priceInit
				priceInit : function(){
					$('#lowPrice').val("0");
					$('#highPrice').val("0");
					var param = {'lowPrice':0, 'highPrice':0};
					$.extend( optionParam, param );
				},
				
				//Page Laoding :  dms.search.option.pageInit
				pageInit : function(){
					dms.search.option.priceInit();
					dms.search.option.genderInit();
					
					//카테고리 option 이벤트
					$("input[name='categoryId']").off("click").on("click", function() {
						var id = $(this).val();
						var name = $('#categoryName_'+id).data("name");
						var history={'name':name, 'id':id, 'show': true}
						dms.search.option.addCheckedOption('categoryId',history);
						
						var list = [];
						$("input[name='categoryId']:checked").each(function() {
							var value = $(this).val();
							if(value !== ''){
								list.push(value);
							}
						});
						
						var param = {'categoryIdList':list.toString()};
						dms.search.option.getProduct(param,"");
						
					});
					
					//브랜드 option 이벤트
					$("input[name='brandId']").off("click").on("click", function() {
						brandSearchYn = "Y"
						var id = $(this).val();
						var name = $('#brandName_'+id).data("name");
						var history={'name':name, 'id':id, 'show': true}
						dms.search.option.addCheckedOption('brandId',history);
						
						var list = [];
						$("input[name='brandId']:checked").each(function() {
							var value = $(this).val();
							if(value !== ''){
								list.push(value);
							}
						});
						
						var param = {'brandIdList':list.toString()};
						dms.search.option.getProduct(param,"");
						
					});
					
					//월령 option 이벤트
					$("input[name='ageTypeCode']").off("click").on("click", function() {
						var id = $(this).val();
						var name = $('#ageName_'+id).data("name");
						var history={'name':name, 'id':id, 'show': true}
						dms.search.option.addCheckedOption('ageTypeCode',history);
						
						var list = [];
						$("input[name='ageTypeCode']:checked").each(function() {
							var value = $(this).val();
							if(value !== ''){
								list.push(value);
							}
						});
						
						var param = {'ageTypeCodeList':list.toString()};
						dms.search.option.getProduct(param,"");
					});
				
					//성별 option 이벤트
					$("input[name='inpGender']").off("click").on("click", function() {
						var value = $('input:radio[name="inpGender"]:checked').val();
						var name = $('#gender_'+value).data("name");
						var history={'name':name, 'id':value, 'show': true}
						dms.search.option.addCheckedOption('inpGender',history);
						
						var param = {'genderTypeCod': value};
						dms.search.option.getProduct(param,"");
					});
					
					//소재 option 이벤트
					$("input[name='material']").off("click").on("click", function() {
						var id = $(this).val();
						var history={'name':id, 'id':id, 'show': true}
						dms.search.option.addCheckedOption('material',history);
						
						var list = [];
						$("input[name='material']:checked").each(function() {
							var value = $(this).val();
							if(value !== ''){
								list.push(value);
							}
						});
						var param = {'materialList':list.toString()};
						dms.search.option.getProduct(param,"");
					});
					
					//젖병소재 option 이벤트
					$("input[name='material1']").off("click").on("click", function() {
						var id = $(this).val();
						var history={'name':id, 'id':id, 'show': true}
						dms.search.option.addCheckedOption('material1',history);
						
						var list = [];
						$("input[name='material1']:checked").each(function() {
							var value = $(this).val();
							if(value !== ''){
								list.push(value);
							}
						});
						var param = {'material1List':list.toString()};
						dms.search.option.getProduct(param,"");
					});
					
					//쿠폰할인 option 이벤트
					$("input[name='couponYn']").off("click").on("click", function() {
						var value = $("input:checkbox[name='couponYn']").is(":checked")?"Y":"";
						var show = $("input:checkbox[name='couponYn']").is(":checked")?true:false;
						var name = $('#bene_coupon').text();
						
						var history={'name':name, 'id':value, 'show': show};
						dms.search.option.addCheckedOption('couponYn',history);
						
						var param = {'couponYn':value};
						dms.search.option.getProduct(param,"");
					});
					
					// 무료배송 option 이벤트
					$("input[name='deliveryFreeYn']").off("click").on("click", function() {
						var value = $("input:checkbox[name='deliveryFreeYn']").is(":checked")?"Y":"";
						var show = $("input:checkbox[name='deliveryFreeYn']").is(":checked")?true:false;
						var name = $('#bene_deliveryFree').text();
						
						var history={'name':name, 'id':value, 'show': show};
						dms.search.option.addCheckedOption('deliveryFreeYn',history);
						
						
						var param = {'deliveryFreeYn':value};
						dms.search.option.getProduct(param,"");
					});
					
					//포인트적립 option 이벤트
					$("input[name='pointSaveYn']").off("click").on("click", function() {
						var value = $("input:checkbox[name='pointSaveYn']").is(":checked")?"Y":"";
						var show = $("input:checkbox[name='pointSaveYn']").is(":checked")?true:false;
						var name = $('#bene_pointSave').text();
						
						var history={'name':name, 'id':value, 'show': show};
						dms.search.option.addCheckedOption('pointSaveYn',history);
						
						var param = {'pointSaveYn':value};
						dms.search.option.getProduct(param,"");
					});
					
					//사은품 option 이벤트
					$("input[name='presentYn']").off("click").on("click", function() {
						var value = $("input:checkbox[name='presentYn']").is(":checked")?"Y":"";
						var show = $("input:checkbox[name='presentYn']").is(":checked")?true:false;
						var name = $('#bene_present').text();
						
						var history={'name':name, 'id':value, 'show': show};
						dms.search.option.addCheckedOption('presentYn',history);
						
						var param = {'presentYn':value};
						dms.search.option.getProduct(param,"");
					});
					
					//정기배송 option 이벤트
					$("input[name='regularDeliveryYn']").off("click").on("click", function() {
						var value = $("input:checkbox[name='regularDeliveryYn']").is(":checked")?"Y":"";
						var show = $("input:checkbox[name='regularDeliveryYn']").is(":checked")?true:false;
						var name = $('#bene_regularDelivery').text();
						
						var history={'name':name, 'id':value, 'show': show};
						dms.search.option.addCheckedOption('regularDeliveryYn',history);
						
						var param = {'regularDeliveryYn':value};
						dms.search.option.getProduct(param,"");
					});
					
					//매장픽업 option 이벤트
					$("input[name='offshopPickupYn']").off("click").on("click", function() {
						var value = $("input:checkbox[name='offshopPickupYn']").is(":checked")?"Y":"";
						var show = $("input:checkbox[name='offshopPickupYn']").is(":checked")?true:false;
						var name = $('#bene_offshopPickup').text();
						
						var history={'name':name, 'id':value, 'show': show};
						dms.search.option.addCheckedOption('offshopPickupYn',history);
						
						var param = {'offshopPickupYn':value};
						dms.search.option.getProduct(param,"");
					});
					
					//컬러 option 이벤트
					$(".optionBox .colorList").find("a").off("click").on("click", function(e){
						$(this).toggleClass("active");
						
						var color = $(this).attr('name');
						var check = $(this).attr('class').split(' ')[1];
						
						if(check ==='active'){
							colorList.push(color);
						}else{
							colorList = $.grep(colorList, function(value) {
								  return value != color;
							});
						}
						
						var param = {'colorList':colorList.toString()};
						dms.search.option.getProduct(param,"");
						
						var history={'name':color, 'id':color, 'show': true}
						dms.search.option.addCheckedOption('color',history);
						
					});
					
					//가격 option 이벤트
					$("#lowPrice").off("click").on("click", function() {
						$("#lowPrice").val("");
					});
					
					$("#highPrice").off("click").on("click", function() {
						$("#highPrice").val("");
					});
				},

				//옵션선택시 히스토리 기능을 위한 객체 값 설정 : dms.search.option.addCheckedOption
				addCheckedOption : function (type, object){
					
					var addId = object.id;
					add = false; 
				
					if(type == 'brandId' || type =='ageTypeCode' || type =='material' || type =='material1' || type =='color' || type =='categoryId'){
						$.each(optionHistory, function(key, value){
							if(value.length > 0){
								//option의 value 단위
								$.each(value, function(i, item){
									if(addId === item['id']){
										if(item['show']){
											//선택한 option의 check가 해지
											item['show'] = false;
										}else{
											//한번 선택한 option의 check가 해지가 되서 재선택시
											item['show'] = true;
										}
										add = true;
										return false;
									}
									
								});
							}
						});
						//선택한 option의 중복 처리
						if(!add){
							optionHistory[type].push(object);
						}
					}else{
						$.extend( optionHistory[type], object );
					}
					
					//console.log("총 들어있는 것들 :"+JSON.stringify(optionHistory));
					dms.search.option.addHistoryHtml(optionHistory);
				},
				
				//옵션선택시 히스토리 기능을 위한 HTML생성 : dms.search.option.addHistoryHtml
				addHistoryHtml : function(object){
					
					$('#checkedOption').empty();
					
					var returnHtml="";
					$.each(object, function(key, value){
						
						if(key == 'brandId' || key =='ageTypeCode' || key =='material' || key =='material1' || key =='color' ||key =='categoryId'){
							$.each(value, function(i, item){
								var html ="<li id="+key+"_"+item['id']+""+(!item['show'] ?" class='blur'":"")+" >"
											+"<span>"+item['name']+"</span>"
											+"<a href=\"javaScript:dms.search.option.optionInit('','"+key+"_"+item['id']+"');\" class=\"btn_delete\">삭제</a>"
											+"</li>";
								returnHtml+=html
							});
						}else if(key == 'inpGender'){
							if(value['name'] != undefined){
								var html ="<li id="+key+">"
									+"<span>"+value['name']+"</span>"
									+"<a href=\"javaScript:dms.search.option.optionInit('','"+key+"');\" class=\"btn_delete\">삭제</a>"
									+"</li>";
									returnHtml+=html						
							}
						}else{
							if(value['name'] != undefined){
								var html ="<li id="+key+" "+(!value['show'] ?" class='blur'":"")+">"
											+"<span>"+value['name']+"</span>"
											+"<a href=\"javaScript:dms.search.option.optionInit('','"+key+"');\" class=\"btn_delete\">삭제</a>"
											+"</li>";
								returnHtml+=html
							}
						}
					});
					$('#checkedOption').append(returnHtml);
				},
				
				//선택한 option초기화 : dms.search.option.optionInit
				optionInit : function(type, name){
					
					//옵션 전체초기화
					if(type == 'all'){
						dms.search.option.genderInit();
						dms.search.option.priceInit();
						
						//카테고리,브랜드,월령,소재,혜택, 성별 CSS 변경
						$.each(optionName, function(key, val){
							$("input[name="+val+"]:checkbox").each(function() {
								$(this).prop("checked", false);
								$("input[name="+val+"]").closest("em").removeClass("selected");
								
							});
						});
						
						//색상 CSS 변경
						var di = $(".colorList").find("li").find("a");
						$.each(di, function(key, val){
							var color = di[key].name;
							var check = di[key].className.split(" ")[1];
							$.each(optionHistory, function(i, item){
								$.each(item, function(ke, it){
									if(i === 'color'){
										if(color === it['name'] && check ==='active'){
											var classNm = di[key].className.split(" ")[0];
											//di[key].className = it['name'];
											di[key].className = classNm;
											return;
										}
									}
								});
							});
						});
						
						//optiont선택 히스토리 초기화
						hstParam={'categoryId':[],'brandId':[],'ageTypeCode':[],'material':[],'material1':[],'couponYn':{},
								'deliveryFreeYn':{},
								'pointSaveYn':{},
								'presentYn':{},
								'regularDeliveryYn':{},
								'offshopPickupYn':{},		
								'color':[]}
						$.extend( optionHistory, hstParam );
						$('#checkedOption').empty();
						
						//상품조회 파라미터 초기화 : 키워드가 있을시 키워드를 제외하고 초기화 해야함.
						optionParam = new Object; 
						colorList = [];
						
					//옵션 개별 초기화
					}else{
						
						var id= name.split("_")[0];
						var valueStr= name.split("_")[1];
						
						//console.log(id);
						//console.log(valueStr);
						
						
						// json : 혜택, 성별 CSS변경
						if(valueStr == undefined){
							$("input:checkbox[name='"+id+"']").closest("em").removeClass("selected");
							$("input[name='"+id+"']:checkbox").prop("checked", false);
							$('#'+id).css("display","none");
							
							//객체에서 파라미터 삭제
							optionHistory[id] = {};
							delete optionParam[id];
							
							//성별코드
							if(id == 'inpGender'){
								delete optionParam['genderTypeCod'];
								dms.search.option.genderInit();
							}
							
						}else {
							 
							// array : 컬러,브랜드,카테고리,월령,소재 CSS 변경 
							if(id === 'color'){
								
								var di = $(".colorList").find("li").find("a");
								$.each(di, function(key, val){
									var color = di[key].name;
									var check = di[key].className.split(" ")[1];
									
									if(color === valueStr && check ==='active'){
										$(this).removeClass("active");
										return;
									}
								});
								$('#'+name).css("display","none");
							}else{
								$("input:checkbox[name='"+id+"']:input[value='"+valueStr+"']").closest("em").removeClass("selected");
								$("input[name='"+id+"']:checkbox").prop("checked", false);
								$('#'+name).css("display","none");
							}
							
							// 히스토리객체에서 파라미터 삭제
							$.each(optionHistory, function(key, item){
								if(key == id){
									for (var i = 0; i < item.length; i++) {
										if(item[i].id == valueStr){
											item.splice(i, 1);//Array는 delete로 삭제할경우 null로 치환된다.
										}
									}
								}
							});
							
							//검색API객체에서 파라미터 삭제
							$.each(optionParam, function(key, item){
								if(id+'List' == key){
									//console.log('index:' + key + ' / ' + 'item:' + item);
									var list = item.split(",");
									for (var i = 0; i < list.length; i++) {
										if(list[i] == valueStr){
											list.splice(i, 1);
										}
									}
									
									delete optionParam[id+'List'];
									optionParam[id+'List'] =list.toString();
								}
								
							});
							
							//색상 변수에서 active 아닌것은 지워준다. 
							if(id == 'color'){
								if(colorList.toString() !==''){
									for (var i = 0; i < colorList.length; i++) {
										if(colorList[i] == valueStr){
											colorList.splice(i, 1);
										}
									}
								}
							}
						}
						
					}
					
					//console.log("삭제후:"+JSON.stringify(optionHistory));
					//console.log("optionParameter :"+JSON.stringify(optionParam));
					dms.search.option.getProduct(optionParam,"");
					
				},
				
				//상품 검색API을 위한 파라미터 셋팅 : dms.search.option.getProduct
				getProduct : function (param,htmlType){
					
					var type = $('#searchViewType').val();
					var pagingYn ="";
					var currentPage = "";
					
					//키워드 검색 : 검색메인
					if(type == "KEYWORD"){
						
						//결과내재검색키워드
						var reKeyword="";
						if(param['keyword'] != undefined){
							reKeyword = param['keyword']
						}else{
							reKeyword = $('#subKeyword').val();
						}
						param['keyword'] = reKeyword;
						param['searchKeyword'] = $('#searchKeyword').val();
						param['optionBrandSrcYn'] = brandSearchYn;
						$('#optionBrandSrcYn').val(brandSearchYn);
					
					//카테고리 매장, 전문관
					}else if(type =="CATEGORY" || type == "SPECIAL"){
						
						// 카테고리 목록String을 공백과 특수문자제거, 중카의 소카테고리 목록을 넘길때
						var categoryList = [];
						if( $('#categoryIds').val() !== ''){
							var str = $('#categoryIds').val().replace(regExp, "");
							var list = str.split(",");
							$.each(list, function(key, value){
								if(value !== '' && value != undefined){
									categoryList.push(value.toString());
								}
							});
							
							if(categoryList.length > 0){
								param['categoryIdList'] = categoryList.toString();
							}
						}
					
					//판매자 매장
					}else if(type == "BUSINESS"){
						if( businessId !== ''){
							param['businessId'] = $('#businessId').val();
						}
						
					//브랜드템플릿 A,B,C
					}else if(type == "BRAND"){
						var str = $('#brandId').val().replace(regExp, "");
						id = str.replace(/(\s*)/g,"");
						param['brandIdList'] = id;
						
						//소카테고리 목록
						var depthCategoryList = [];
						if( $('#depthCategoryIds').val() !== ''){
							var str = $('#depthCategoryIds').val().replace(regExp, "");
							var list = str.split(",");
							$.each(list, function(key, value){
								if(value !== '' && value != undefined){
									depthCategoryList.push(value.toString());
								}
							});
							
							if(depthCategoryList.length > 0){
								param['depthCategoryIds'] = depthCategoryList.toString();
							}
						}
						
						var paramCtgList = param['categoryIdList']; //선택한 카테고리
						var valueCtgList = $('#categoryIds').val();
						
						//카테고리를 클릭했을때 (select가 false, true 둘중 하나 값이 있거나 empty임.)
						if(paramCtgList == '' || paramCtgList == null){
							param['categoryIdList'] = depthCategoryList.toString();
							
						}else if(paramCtgList == undefined){
							//카테고리를 클릭을 안헀을때
							if(valueCtgList !== ''){
								var categoryList = [];
								var str = $('#categoryIds').val().replace(regExp, "");
								var list = str.split(",");
								$.each(list, function(key, value){
									if(value !== '' && value != undefined){
										categoryList.push(value.toString());
									}
								});
								
								if(categoryList.length > 0){
									param['categoryIdList'] = categoryList.toString();
								}
							}else{
								param['categoryIdList'] = depthCategoryList.toString();
							}
						}
						
					//월령검색	
					}else if(type == "AGE"){
						var str = $('#ageTypeCode').val().replace(regExp, "");
						id = str.replace(/(\s*)/g,"");
						param['ageTypeCodeList'] = id;
					
					//브랜드관 검색 : 레이아웃이 다르므로 브랜드관에서 쓰는 검색타입
					}else if(type == "BRANDSHOP"){
						
						//결과내재검색키워드
						var reKeyword="";
						if(param['keyword'] != undefined){
							reKeyword = param['keyword']
						}else{
							reKeyword = $('#subKeyword').val();
						}
						param['keyword'] = reKeyword;
						param['searchKeyword'] = $('#searchKeyword').val();
						
						//브랜드관 ID
						var str = $('#brandId').val().replace(regExp, "");
						id = str.replace(/(\s*)/g,"");
						param['brandIdList'] = id;
					}
					
					
					//가격 validation
					var startPrice = $('#lowPrice').val();
					var endPrice = $('#highPrice').val();
					if(startPrice != undefined && endPrice != undefined){
						if($('#lowPrice').val() !=='' && $('#highPrice').val() != ''){
							var lowPrice = $('#lowPrice').val().replace(/,/g, '');
							var highPrice = $('#highPrice').val().replace(/,/g, '');
							lowPrice1 = lowPrice.replace(/ /g, '');
							highPrice1 = highPrice.replace(/ /g, '');
							
							if(Number(lowPrice1) <= Number(highPrice1)){
								param['lowPrice'] = lowPrice1;
								param['highPrice'] = highPrice1;
							}else{
								param['lowPrice'] = 0;
								param['highPrice'] = 0;
							}
						}
					}
					
					//객체를 ADD 한다.
					$.extend( optionParam, param );
					
					//파라미터 빈값 제거
					$.each(optionParam, function(key, value){
					    if(value ===''){
							delete optionParam[key]
					    }
					});
					
					//모바일과 PC의 페이징 노출여부
					if(ccs.common.mobilecheck()){
						pagingYn = "N";
						currentPage = param['currentPage'];
						
						if(currentPage == undefined || currentPage == ''){
							currentPage = 1;
						}
					}else{
						pagingYn = "Y";
						currentPage = $('#currentPage').val();
					}
					
					//검색API의 기본파라미터 셋팅
					var baseParam = {'currentPage' : currentPage, 
							'pageSize' : $('#pageSize').val(),
							'sort': $('#sort').val(),
							'direction':$('#direction').val(),	
							'searchViewType' : $('#searchViewType').val(),
							'pagingYn':pagingYn
							};
					
					//객체를 Add한다
					$.extend( baseParam, optionParam );
					//console.log("총 들어있는 것:"+JSON.stringify(baseParam))
					
					//검색색인 API호출
					if(htmlType == 'add'){
						dms.common.changePage(baseParam,dms.search.list.addPrd);
					}else{
						dms.common.changePage(baseParam,dms.search.list.addHtml);
					}
				},
				
				/*
				 * 브랜드 검색후 카테고리 체크 : dms.search.option.categoryChange
				 * 선택된 카테고리가 있어도 브랜드를 체크하고 검색후 브랜드에 해당하는 카테고리만 재 선택 해준다.
				 */
				categoryChange : function(){
					var ctgStr = $('#optionCtgList').val();
					
					var ctgList=[];
					if(ctgStr !== ''){
						ctgList = ctgStr.split(",");
						
						$.each(optionHistory, function(k, checkValue){
							if(k =='categoryId'){
								show = true;
								$.each(checkValue, function(i, item){
									$.each(ctgList, function(key, value){
										if(item['id'] == value){
											show = true;
											return false;
										}else{
											show = false;
										}
									});
									
									//show 변경
									item['show'] = show;
									if(!show){
										$("input:checkbox[name='categoryId']:input[value='"+item['id']+"']").closest("em").removeClass("selected");
										$("input:checkbox[name='categoryId']:input[value='"+item['id']+"']").prop("checked", false);
									}else{
										$("input:checkbox[name='categoryId']:input[value='"+item['id']+"']").closest("em").addClass("selected");
										$("input:checkbox[name='categoryId']:input[value='"+item['id']+"']").prop("checked", true);
									}
								});
							}
						});	
						
						dms.search.option.addHistoryHtml(optionHistory);
						
						var searchlist = [];
						$("input[name='categoryId']:checked").each(function() {
							var value = $(this).val();
							if(value !== ''){
								searchlist.push(value);
							}
						});
					
						delete optionParam['categoryIdList'];
						var param = {'categoryIdList':searchlist.toString()};
						$.extend( optionParam, param );
					}
					
					brandSearchYn = "N";
				},
				
				//가격 적용 버튼 이벤트 : dms.search.option.priceApply
				priceApply : function(){
					
					var startPrice = $('#lowPrice').val();
					var endPrice = $('#highPrice').val();
					var param = {};
					
					if(startPrice !=='' && endPrice !== ''){
						
						var lowPrice = $('#lowPrice').val().replace(/,/g, '');
						var highPrice = $('#highPrice').val().replace(/,/g, '');

						lowPrice1 = lowPrice.replace(/ /g, '');
						highPrice1 = highPrice.replace(/ /g, '');
						
						if((Number(lowPrice1) > 0 && Number(highPrice1) > 0) && (Number(lowPrice1) <= Number(highPrice1))){
							
							param['lowPrice'] = lowPrice1;
							param['highPrice'] = highPrice1;
							
						}else{
							param['lowPrice'] = 0;
							param['highPrice'] = 0;
						}
					}else{
						param['lowPrice'] = 0;
						param['highPrice'] = 0;
						
					}
					
					dms.search.option.getProduct(param,"");
				}
			},
			
			/*****************************************************************
			 * 검색페이지의 상품목록 : dms.search.list
			 *****************************************************************/
			list : {
				//상품목록 카운트 : dms.search.list.listCount
				listCount : function(cnt){
					$('#searchCount').empty();
					$('#searchCount').append(dms.search.list.comma(cnt));
				},
				
				//PC 상품목록  : dms.search.list.addHtml
				addHtml : function(html){
					$("#productList").html(html);
				},
				
				//모바일 상품목록 : dms.search.list.addPrd
				addPrd : function (html){
					$("#productList").append(html);
					dms.search.list.blockYn();
					
				},
				
				//모바일 상품목록 블럭형 or 리스트 노출 : dms.search.list.blockYn
				blockYn : function(){
					
					var className = $(".sortBoxList button").attr("class"); 

					//모바일에서 리스트와 블럭형을 노출해준다.
					if(className != undefined){
						//0보다 크면 블럭형이다 className에서 block 추가한다.
						if(Number(className.indexOf('block')) > 0){
							
							$(".list_group").find(".product_type1").addClass("list");
							$(".list_group").find(".product_type1").removeClass("block");
						}else{
							$(".list_group").find(".product_type1").addClass("block");
							$(".list_group").find(".product_type1").removeClass("list");
						}
					}
				},
				
				//콤마찍기 : dms.search.list.comma
				comma :function(str) {
					str = String(str);
				    return str.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
				},
				
				//콤마풀기 : dms.search.list.uncomma
				uncomma :function (str) {
				    str = String(str);
				    return str.replace(/[^\d]+/g, '');
				}
				
			},
			
			/*****************************************************************
			 * 검색페이지의 OrderBy : dms.search.orderBy
			 * - sort, page 
			 *****************************************************************/
			orderBy:{
				
				//Page Laoding : ms.search.orderBy.pageInit
				pageInit : function(value){
					//order by, pageSize
					if($('#sort').val() === 'SALE_PRICE' && $('#direction').val() ==='ASC'){
						$('#sortSelect').val('LOW_PRICE').prop("selected", true);
					}else{
						$('#sortSelect').val($('#sort').val()).prop("selected", true);
					}
					$('#pageSelect').val($('#pageSize').val()).prop("selected", true);
					
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
						
						var param = { 'sort': sort,'direction': direction,'currentPage' : '1'};
						dms.search.option.getProduct(param,"");
						
					});
					
					//pageSize select 이벤트
					$('#pageSelect').off("change").on("change",function(){
						var pageSize= $(this).val();
						
						var param = {'currentPage' : '1', 
										'pageSize' : pageSize
									};
						dms.search.option.getProduct(param,"");
					});
					
					//블럭형과 리스트형을 노출한다.
					dms.search.list.blockYn();
				}
			},
			/*****************************************************************
			 * 검색페이지의 상품목록 : dms.search.getRecommendationProductList
			 *****************************************************************/
			getRecommendationProductList: function(appendTagId, param){
				//console.log('appendTagId:',appendTagId);
				//console.log('param:',param);
				 $.ajax({
		            	type: 'POST',
		            	url: '/dms/search/recommenadation/ajax',
		            	data: param,
		            	success:function(data) {
		            		$('#'+appendTagId).html(data);
		            		
		            		 if (ccs.common.mobilecheck()) {
		 						swiperCon('cartSwiper_pordList', '2');
		 					}else{
		 						swiperCon('cartSwiper_pordList', 400, 4, 12, false, true, 4);
		 					}
		            	}
		            }); 
			}
		},
		
		/***********************************************************************
		 * 메인_ 실시간 클릭 추천
		 ***********************************************************************/
		main : {
			// 실시간 클릭/주간판매
        	makeParam : function(categoryId) {
        		
        		var member = global['member'];
        		var gender = member.babyGenderCd;
        		var babyMonth = member.babyMonthCd;
        		
        		var paramKey = "";
        		if (common.isNotEmpty(categoryId) && categoryId != 'undefined') {
        			paramKey += categoryId;
        		}
        		
        		if(babyMonth == 'undefined' || babyMonth == undefined || babyMonth == '' ||babyMonth == null){
        			babyMonth = "";
        		}else{
        			babyMonth = babyMonth;
        		}
        		
        		if(gender == 'undefined' || gender == undefined || gender == '' ||gender == null){
        			gender = "";
        		}else{
        			gender = gender;
        		}
        		
        		paramKey += "-" +gender+ "-" + babyMonth+"-view";
        		var param = {recType:"x", size:10, key:paramKey, callback:"foobar"};
        		
        		$.ajax({
					url : "/dms/display/bestShop/ajax?type=main",
					type : "get",
					data : param,
					success :function(html) {
						dms.main.bestCallback(html);
						ccs.mainSwipe.calculateHeight();
                   	}
				});
        	},
        	
        	bestCallback : function(html) {
        		if (ccs.common.mobilecheck()) {
        			$(".bestListArea").html(html);
        			
        		}else{
        			$("#bestListArea").html(html);
        		}
        	},
		}
		
}
