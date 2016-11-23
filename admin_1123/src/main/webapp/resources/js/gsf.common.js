  
/***********************************************************************/
/** 공통 Constants                                                    **/
/***********************************************************************/

Constants = {
	date_format_bar : "-",
	date_format_1 : "yyyy-MM-dd HH:mm:ss",
	date_format_2 : "yyyy-MM-dd",
	date_format_3 : "yyyy-MM-dd HH:mm",
	message_keys : [],
	header_keys : [],
    public_modules : ['ngResource','ngRoute','ngCookies', 'ngTouch',     
	'ui.date', 
	'ngCkeditor',
	'commonServiceModule', 	
	'dmsServiceModule',
	'pmsServiceModule',
	'spsServiceModule',
	'mmsServiceModule',
	'ccsServiceModule',
	'omsServiceModule',
	'gridServiceModule',
	'checklist-model']	
};

Rest = {
	context : {
		path : global.config.domainUrl
	},
	method : {
		GET : 'get',
		POST : 'post',
		PUT : 'put',
		DELETE : 'delete'
	},
	responseType : {
		SINGLE : "S", // single object
		MULTI : "M", // multi object
		TEXT : "T", // text
		VOID : "V" // void
	}
};

// 공통 서비스 선언
var commonServiceModule = angular.module("commonServiceModule", ['ngResource']);

// 공통 함수
common = {
		// scope apply적용
		safeApply : function(scope, fn) {
		    var phase = scope.$root.$$phase;
		    if(phase == '$apply' || phase == '$digest') {
			    if(fn && (typeof(fn) === 'function')) {
			        fn();
			    }
		    } else {
		    	try{
			    	if(fn && (typeof(fn) === 'function')){
			    		scope.$apply(fn);	
			    	}else{
			    		scope.$apply();
			    	}
		    	}catch(e){
		    		console.log("error !!! ");
		    	}
			    
		    }
		},resizeIframe : function(){
			 
			// 3초동안 0.5초 간격으로 화면 리사이징
            var myVar = setInterval(function(){ myTimer() }, 500);

			setTimeout(function(){
				myStopFunction();
				}, 3000 );
				
			function myTimer() {
				if(window.parent && angular.isDefined(__pageId) && __pageId != undefined && __pageId != null && __pageId != ""){
					window.parent.resizeHeight(__pageId+"_module", window.document.body.scrollHeight);
				}
			}
			
			function myStopFunction() {
			    clearInterval(myVar);
			}

			
		},
		//arraybuffer 객체를 스트링으로 변환
		arraybufferToStr : 	function(arrayBuffer) {
			  var result = "";
			  var i = 0;
			  var c = 0;
			  var c1 = 0;
			  var c2 = 0;

			  var data = new Uint8Array(arrayBuffer);

			  // If we have a BOM skip it
			  if (data.length >= 3 && data[0] === 0xef && data[1] === 0xbb && data[2] === 0xbf) {
			    i = 3;
			  }

			  while (i < data.length) {
			    c = data[i];

			    if (c < 128) {
			      result += String.fromCharCode(c);
			      i++;
			    } else if (c > 191 && c < 224) {
			      if( i+1 >= data.length ) {
			        throw "UTF-8 Decode failed. Two byte character was truncated.";
			      }
			      c2 = data[i+1];
			      result += String.fromCharCode( ((c&31)<<6) | (c2&63) );
			      i += 2;
			    } else {
			      if (i+2 >= data.length) {
			        throw "UTF-8 Decode failed. Multi byte character was truncated.";
			      }
			      c2 = data[i+1];
			      c3 = data[i+2];
			      result += String.fromCharCode( ((c&15)<<12) | ((c2&63)<<6) | (c3&63) );
			      i += 3;
			    }
			  }
			  return result;
        },
        // 숫자인지 체크
        checkNumber :function(value){
			var regExp = /^[0-9]+$/;
			return regExp.test(value);
        },
        config : {},// config정보
    	// 값이 undefined 이거나 null인지 체크
    	isNull : function(value){
    		if(value == null || typeof value == "undefined" ){
    			return true;
    		}
    		return false;
    	},
    	// 값이 undefined 이거나 null 이거나 '' 인지 체크
    	isEmpty : function(value) {
			if (typeof value === 'number') {
				return false;
			}
		
			if (!value || value == null || value == undefined || typeof value == "undefined" || value == 'null' || value == '') {
				return true;
			}
			return false;
		},
    	// 객체가 null 이거나 {} 이면 true
    	isEmptyObject : function(obj){
    		
    		var isEmpty = $.isEmptyObject(obj);
    		var isNull = (obj == null);
    		var isUndefined = (typeof obj == 'undefined');
    		return isEmpty || isNull || isUndefined;
    	},
    	// 문자열의 바이트 체크
    	getBytes : function(text){
    		var bytes = text.replace(/[\0-\x7f]|([0-\u07ff]|(.))/g,"$&$1$2").length;
    		//console.log(bytes);
    		return bytes;
    	}
};

//checkbox-list directive에서 custom 속성에 매핑될 combo box 목록
CHECKBOXLIST = {
	// Y or N
	YES_NO : [
        {name : '예', cd : 'Y'},      
        {name : '아니오', cd : 'N'}
    ],		
	// 사용여부
	USE_YN : [
        {name : '사용', cd : 'Y'},      
        {name : '미사용', cd : 'N'}
    ],
    // 전시여부
    DISPLAY_YN : [
    	{name : '전시', cd : 'Y'},      
    	{name : '미전시', cd : 'N'}
    ],
    // 공개여부
    PUBLIC_YN : [
    	{name : '공개', cd : 'Y'},      
    	{name : '미공개', cd : 'N'}
    ],
	//확인여부
	PMS_CONFIRM_YN : [
     	{cd : 'Y',	name : '확인'},
        {cd : 'N',	name : '미확인'}
    ],
    // 상품 상태
    PRODUCT_STATUS : [
		{cd:'SALE_STATE_CD.SALE', 		name:'판매중'}, 
		{cd:'SALE_STATE_CD.SOLDOUT',	name:'품절'}, 
		{cd:'SALE_STATE_CD.STOP', 		name:'일시정지'}, 
		{cd:'SALE_STATE_CD.MDSTOP', 	name:'MD정지'},
		{cd:'SALE_STATE_CD.END', 		name:'영구종료'} 
	],
    // 상품 상태2 ( 상품 승인 관리 )
    PRODUCT_STATUS2 : [
		{cd:'SALE_STATE_CD.REQ', 		name:'승인전'}, 
		{cd:'SALE_STATE_CD.APPROVAL1',	name:'QC검수요청'}, 
		{cd:'SALE_STATE_CD.APPROVAL2', 	name:'QC검수완료'}, 
		{cd:'SALE_STATE_CD.REJECT', 	name:'반려'}
	],
	// 상품 유형
	PRODUCT_TYPE :  [
		{cd:'PRODUCT_TYPE_CD.GENERAL', 	name:'일반상품'}, 
		{cd:'PRODUCT_TYPE_CD.SET', 		name:'세트상품'} 
	],
	
	// 상품 유형2
	PRODUCT_TYPE2 :  [
		{cd:'PRODUCT_TYPE_CD.GENERAL', 	name:'일반상품'}, 
		{cd:'PRODUCT_TYPE_CD.PRESENT', 	name:'사은품'} 
	],
	// 상품구분
	PRODUCT_ATTR : [
        {cd:'reserveYn', 			name:'예약'},
        {cd:'offshopPickupYn', 		name:'픽업'},
        {cd:'fixedDeliveryYn', 		name:'지정일배송'},
        {cd:'regularDeliveryYn', 	name:'정기배송'},
        {cd:'giftYn', 				name:'기프티콘'},
        {cd:'wrapYn', 				name:'선물포장'}
    ],
	//공지사항 유형
	NOTICE_TYPE : [
	    {cd : 'NOTICE_TYPE_CD.FRONT',	name : 'FO 일반'}, 
  		{cd : 'NOTICE_TYPE_CD.EVENT',	name : 'FO 당첨자 발표'}, 
  		{cd : 'NOTICE_TYPE_CD.PARTNER',	name : 'PO'}, 
  		{cd : 'NOTICE_TYPE_CD.ADMIN',	name : 'BO'}
    ],
	//팝업 유형
	POPUP_TYPE : [
     	{cd : 'POPUP_TYPE_CD.FRONT', 	name : 'FO 팝업'}, 
     	{cd : 'POPUP_TYPE_CD.PARTNER',	name : 'PO 팝업'}
    ],
	//팝업 채널 유형
	POPUP_CHANNEL_TYPE : [
     	{cd : 'PC',		name : 'PC'}, 
     	{cd : 'MOBILE',	name : 'MOBILE'}
    ],
	//상품평 유형
	REVIEW_TYPE : [
     	{cd : 'N',		name : '일반'},
        {cd : 'Y',		name : '체험단'}
    ],
    // 딜 유형
    DEAL_TYPE : [
        {cd : 'DEAL_TYPE_CD.MEMBER', name : '멤버쉽관'},
        {cd : 'DEAL_TYPE_CD.SHOCKDEAL', name : '쇼킹제로관'},
        {cd : 'DEAL_TYPE_CD.EMPLOYEE', name : '임직원관'},
        {cd : 'DEAL_TYPE_CD.CHILDREN', name : '다자녀관'}
    ],
    // 주문 유형
    ORDER_TYPE : [
        {cd : 'GENERAL', name : '일반주문'},
        {cd : 'REGULARDELIVERY', name : '정기배송주문'},
        {cd : 'B2E', name : 'B2E주문'}
    ],
    // 발송 유형
    ORDER_DELIVERY_TYPE : [
        {cd : 'ORDER', name : '주문'},
        {cd : 'CLAIM', name : '클레임'}
    ],
    // 단품 여부
    ORDER_PRODUCT_TYPE : [
        {cd : 'SINGLE', name : '단품주문'},
        {cd : 'OTHERS', name : '단품주문外'}
    ],
    // 배송방법
    DELIVERY_METHOD : [
        {cd : 'PURCHASE', name : '센터배송'},
        {cd : 'CONSIGN', name : '업체배송'}
    ],
    // 전송여부
    SEND_YN : [
        {cd : 'Y', name : '전송'},
        {cd : 'N', name : '미전송'}
    ],
    // 출고구분
    CONFIRM_YN : [
        {cd : 'ORDER_PRODUCT_STATE_CD.SHIP', name : '출고완료'},
        {cd : 'ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY', name : '부분출고'},
        {cd : 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY', name : '미출고'},
    ],
    // 입고구분
    RETURN_YN : [
        {cd : 'LOGISTICS_STATE_CD.RETURN_READY', name : '입고대기'},
        {cd : 'LOGISTICS_STATE_CD.RETURN', name : '입고완료'}
    ],
    // 입고구분2
    RETURN_YN2 : [
        {cd : 'LOGISTICS_STATE_CD.RETURN', name : '입고'},
        {cd : 'LOGISTICS_STATE_CD.RETURN_READY', name : '미입고'},
    ],
    // 승인상태
    APPROVAL_STATUS : [
        {cd : 'APPROVAL', name : '승인'},
        {cd : 'NO_APPROVAL', name : '미승인'},
        {cd : 'CANCEL_DELIVERY', name : '미출고'},
        {cd : 'CANCEL_APPROVAL', name : '승인취소'}
    ],
    // 중국몰주문여부
    CHINAMALL_YN : [
        {cd : 'N', name : '중국몰外'},
        {cd : 'Y', name : '중국몰'}
    ],
    // 로케이션사용여부
    LOCATION_USE_YN : [
        {cd : 'Y', name : '사용'},
        {cd : 'N', name : '미사용'}
    ],
 // 로케이션사용여부2
    LOCATION_USE_YN2 : [
        {cd : 'Y', name : '사용'},
        {cd : 'N', name : '미사용'},
        {cd : 'NR', name : '미등록'}
    ],
 // 회원 등급
    MEM_GRADE : [
        {cd : 'MEM_GRADE_CD.VIP', name : 'VIP'},
        {cd : 'MEM_GRADE_CD.GOLD', name : 'GOLD'},
        {cd : 'MEM_GRADE_CD.SILVER', name : 'SILVER'},
        {cd : 'MEM_GRADE_CD.FAMILY', name : 'FAMILY'},
        {cd : 'MEM_GRADE_CD.WELCOME', name : 'WELCOME'}
    ],
 // 수수료율
    COMMISSION_RATE : [
        {cd : 'Y', name : '10% 이상'},
        {cd : 'N', name : '10% 미만'}
    ],
 //오차여부
	ERROR_YN : [
     	{cd : 'N',		name : '정상'},
        {cd : 'Y',		name : '오차'}
    ],
 //결제수단
	PAYMENT_METHOD : [
	    {cd : 'PAYMENT_METHOD_CD.CARD',	name : '신용카드'},
	    {cd : 'PAYMENT_METHOD_CD.TRANSFER',	name : '실시간계좌이체'},
	    {cd : 'PAYMENT_METHOD_CD.VIRTUAL', name : '가상계좌'},
	    {cd : 'PAYMENT_METHOD_CD.MOBILE', name : '휴대폰'}
	],
 //정산유형
	SETTLE_TYPE : [
	    {cd : '1',	name : '사입'},
	    {cd : '2',	name : '위탁'},
	    {cd : '4', 	name : '위탁사입'}
	]
	, CLAIM_TYPE_CD : [
	    { cd : 'CLAIM_TYPE_CD.RETURN', name : '반품' },
		{ cd : 'CLAIM_TYPE_CD.EXCHANGE', name : '교환' }, 
		{ cd : 'CLAIM_TYPE_CD.REDELIVERY', name : '재배송' }
	]
	, CLAIM_STATE_CD : [
	    { cd : 'CLAIM_STATE_CD.REQ', name : '신청' },
	    { cd : 'CLAIM_STATE_CD.ACCEPT', name : '접수' }, 
	    { cd : 'CLAIM_STATE_CD.COMPLETE', name : '완료' },
	    { cd : 'CLAIM_STATE_CD.PAYMENT_READY', name : '추가결제대기' },
	    { cd : 'CLAIM_STATE_CD.REJECT', name : '반려' },
	    { cd : 'CLAIM_STATE_CD.WITHDRAW', name : '철회' },
    ]
	, RETURN_STATUS : [
		{ cd : '1', name : '미입고' }, 
		{ cd : '2', name : '부분입고' }, 
		{ cd : '3', name : '전체입고' }
	]


},

RADIOLIST = {
	// 승인여부
    ORDER_PRODUCT_STATE : [
        {cd : '', name : '전체'},
        {cd : 'ORDER_PRODUCT_STATE_CD.READY', name : '정상'},
        {cd : 'ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY', name : '부분출고'},
        {cd : 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY', name : '미출고'},
        {cd : 'ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL', name : '승인취소'}
    ],
    // 전송오류여부
	NORMAL_YN : [
        {cd : '', name : '전체'},
        {cd : 'Y', name : '정상'},
        {cd : 'N', name : '오류'}
    ]
}



/***********************************************************************/
/** 공통 Factory                                                      **/
/***********************************************************************/

// Rest 조회를 위한 Factory
commonServiceModule.factory('restFactory', 
	function($resource) {
		return {

			transaction : function(method, responseType, url, param, data,
					callbackFn, headers) {

				var params = {};
				params.method = method;
				params.headers = headers;
				if (params.headers == null) {
					params.headers = {
						Ajax : "Y"
					};
				} else {
					params.headers.Ajax = "Y";
				}

				if (responseType == Rest.responseType.MULTI) {
					params.isArray = true;
				}

				if (responseType == Rest.responseType.TEXT) {
					params.transformResponse = function(data,
							headersGetter, status) {
						return {
							content : data
						};
					};
				}
				params.timeout = 1000 * 60 * 60;
				
				var Resource = $resource(url, param, {
					'call' : params
				}, {});

				// REST API 호출
				var Result = Resource.call(data);
				Result.$promise.then(function(response) {
					
					if (callbackFn != null) {
						if (responseType == Rest.responseType.VOID) {
							callbackFn();
						} else {
							callbackFn(response);
						}
					} else {
						return response;
					}
				}, function(response) {
					
					if(response.statusText){
						alert("response statusText : "+response.statusText);
					}
					
//					if(response.status == 500){
//						location.href = "/ccs/login";
//					}															
					
					return null;
					
				});
				return Result;
			},
			transaction2 : function(method, responseType, url, param, data, headers) {// promise 객체 리턴
	
				var params = {};
				params.method = method;
				params.headers = headers;
				if (params.headers == null) {
					params.headers = {
						Ajax : "Y"
					};
				} else {
					params.headers.Ajax = "Y";
				}
	
				if (responseType == Rest.responseType.MULTI) {
					params.isArray = true;
				}
	
				if (responseType == Rest.responseType.TEXT) {
					params.transformResponse = function(data,
							headersGetter, status) {
						return {
							content : data
						};
					};
				}
				
				var Resource = $resource(url, param, { 'call' : params }, {});
				
				return Resource.call(data).$promise;
				
//				return new Promise(function (resolve, reject) {
//					
//					
//					
//					Resource.call(data).$promise.then(function(response){
//						resolve(response);
//					},function(response){
//						
//						if(response.statusText){
//							alert("response statusText : "+response.statusText);
//						}
//						
//						//reject(response);
//						
//					});
//					
//				});
				
			}			
		}

	}).factory('commonFactory', 
	function(restFactory) {
		return {
			getValidationField : function(data, callback) {
				var valiationurl = Rest.context.path + "/api/ccs/common/field";
//				if(angular.isDefined(data) && data.length > 0){
					return restFactory.transaction2(Rest.method.POST, Rest.responseType.SINGLE, valiationurl, {}, data, callback);
//				}else{
					//callback([]);
//				}
				
			}
		}

	});