var ccsServiceModule = angular.module("ccsServiceModule", [
	"ngResource"
]);

ccsServiceModule.service('ccsService', function(restFactory) {

	var url = Rest.context.path + "/j_spring_security_check";
	var param = null;

	return {

		loginProcess : function(data, callback) {
			url = Rest.context.path + "/j_spring_security_check";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback, {
				'Content-Type' : 'application/x-www-form-urlencoded'
			});
		},
		getChannelList : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/channels";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		getSimpleChannelList : function(callback) {
			var url = Rest.context.path + "/api/ccs/common/channelList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, null, callback);
		},
		getChannelDetail : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/channelDetail";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, null, data, callback);
		},
		getChannelBusiness : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/channelBusinessList";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, null, data, callback);
		},
		insertChannel : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/channel/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		updateChannel : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/channel/save";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		deleteChannel : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/channel/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		updateChannelStatus : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/channel/updateChannelStatus";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		checkExternalNetwork : function(callback) {
			var url = Rest.context.path + "/api/ccs/common/checkExternalNetwork";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, null, callback);
		},
		sendAuthSms : function(data,callback) {
			var url = Rest.context.path + "/api/ccs/common/sendAuthSms";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		checkAuthSms : function(data,callback) {
			var param = {authNumber : data};
			var url = Rest.context.path + "/api/ccs/common/checkAuthSms";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, null, callback);
		},
	}

}).service('roleService', function(restFactory) {
	var url = Rest.context.path + "/api/ccs/roles/:roleId";
	var amfUrl = Rest.context.path + "/api/ccs/roles/:roleId/amfs";
	var param = {};
	// var param = {'id': '@roleId'};

	return {
		//사용자등록시 권한그룹 조회 팝업
		getRoleList : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, data, callback);
		},
		getRole : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		insertRole : function(data, callback) {
			var pathUrl = Rest.context.path + "/api/ccs/role/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, pathUrl, null, data, callback);
		},
		updateRole : function(data, callback) {
			var pathUrl = Rest.context.path + "/api/ccs/role/update";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, pathUrl, null, data, callback);
		},
		updateRoleMenu : function(data, callback) {
			var pathUrl = Rest.context.path + "/api/ccs/role/menu/update";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, pathUrl, null, data, callback);
		},
		updateRoleMenuFunction : function(data, callback) {
			var pathUrl = Rest.context.path + "/api/ccs/role/menu/function/update";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, pathUrl, null, data, callback);
		},
		deleteRole : function(data, callback) {
			var pathUrl = Rest.context.path + "/api/ccs/role/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, pathUrl, null, data, callback);
		},
		getAmfList : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, amfUrl, param, data, callback);
		},
		getRoleGroup : function(callback) {
			var pathUrl = Rest.context.path + "/api/ccs/role/group/list";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, pathUrl, null, null, callback);
		},
		menuCheck : function(data, callback){
			var url = Rest.context.path + "/api/ccs/role/menu/check";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE,url, param, data, callback);	
		},
		deleteCheck : function(data, callback){ // 하위 기능 권한 체크
			var pathUrl = Rest.context.path + "/api/ccs/role/check";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE,pathUrl, param, data, callback);	
		}
	}
}).service('userService', function(restFactory) {
	var url = Rest.context.path + "/api/ccs/user/:userId";
	var param = {};
	// var param = {'id': '@roleId'};

	return {
		//사용자 목록 조회
		getUserList : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/user/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//사용자 상세 조회
		getUser : function(data, callback) {
			//var param = {'userId': data.userId};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		//사용자 수정
		updateUser : function(data, callback) {
			var durl = "/api/ccs/user/updateUser";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, durl, param, data, callback);
		},		
		//사용자 등록
		insertUser : function(data, callback) {
			var durl = "/api/ccs/user/insertUser";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, durl, param, data, callback);
		},
		getUserByCategoryId : function(data, callback){
			var url = "/api/ccs/user/category";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		//사용자 ID 중복 체크
		getUserIdDuplicate : function (data, callback){
			var durl = "/api/ccs/user/duplicateCheck";
			var param = {"id":data};
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, durl, param, data, callback);
		},
		// 사용자 비밀번호 찾기
		findPwd : function (data, callback){
			var durl = "/api/ccs/user/findPwd";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, durl, param, data, callback);
		},
		checkUserInfo : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/user/checkUserInfo";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		}
	}
}).service('faqService', function(restFactory) {
	var url = Rest.context.path + "/api/ccs/faq";
	var param = {};

	return {
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		insert : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		update : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		}
	}
}).service('noticeService', function(restFactory) {
	var url = Rest.context.path + "/api/ccs/notice";
	var param = {};
	
	return {
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		selectMdNotice : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/notice/md";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},		
		insert : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		update : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		},
		checkEvent : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/notice/checkEvent";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		},
		selectPoList : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/notice/polist";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		}
	}
}).service('popupService', function(restFactory) {
	var url = Rest.context.path + "/api/ccs/popupnotice";
	var param = {};
	
	return {
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		insert : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		update : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, url, null, data, callback);
		}
	}
}).service('controlService', function(restFactory) {
	var param = [];

	return {
		getControlInfo : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/common/control/popup/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		getAllChannelList : function(callback) {
			var url = Rest.context.path + "/api/ccs/common/channelList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, null, callback);
		}
	}
}).service('inquiryService',function(restFactory,$http){
	var url = Rest.context.path + "/api/ccs/inquiry/:inquiryNo";
	var param = {};
	
	return{
		//문의목록 조회
		selectInquiryList : function(data, callback){
			var url = Rest.context.path+"/api/ccs/inquiry/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);	
		},
		//문의 내용 상세 조회
		selectInquiry: function (data, callback){
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		//문의 수정& 등록
		saveInquiry: function(data, callback){
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		},
		//문의 답변 등록& 수정
//		saveAnswer : function(data, callback){
//			var url = Rest.context.path + "/api/ccs/inquiry/saveAnswer";
//			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
//		},
		//문의 확인
		saveConfirm : function(data, callback){
			var url = Rest.context.path + "/api/ccs/inquiry/saveConfirm";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, url, param, data, callback);
		},
		//답변 하기
		saveAnswer : function(data, callback){
			var url = Rest.context.path + "/api/ccs/inquiry/saveAnswer";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, url, param, data, callback);
		}
	}
}).service('reviewService',function(restFactory,$http){
	var param = {};
	return{
		//상품평 목록 조회
		getReviewList : function(data, callback){
			var url = Rest.context.path+"/api/pms/product/productReview/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);	
		},
		//상품평 상세 조회
		getReview : function (data, callback){
			var url = Rest.context.path + "/api/pms/product/productReview/detail";
			//var param = {reviewNo:data.reviewNo};
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		//상품평 수정
		update : function(data, callback) {
			var url = Rest.context.path + "/api/pms/product/productReview";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		}
	}
}).service('prdQnaService',function(restFactory,$http){
	var param = {};
	return{
		//상품QnA 목록 조회
		getProductQnaList : function(data, callback){
			var url = Rest.context.path+"/api/ccs/productQna/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);	
		},
		//상품QnA 상세 조회
		getProductQnaDetail : function (data, callback){
			var url = Rest.context.path + "/api/ccs/productQna/:productQnaNo";
			var param = {productQnaNo:data.productQnaNo};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		//상품QnA 문의 수정
		updateProductQna: function(data, callback){
			var url = Rest.context.path+"/api/ccs/productQna/update";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, url, null, data, callback);
		},
		//상품QnA 문의 확인
		updateQnaConfirm : function(data, callback){
			var url = Rest.context.path + "/api/ccs/productQna/confirm";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, url, param, data, callback);
		},
		//상품QnA 답변 완료
		updateAnswer : function(data, callback){
			var url = Rest.context.path + "/api/ccs/productQna/answer";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, url, param, data, callback);
		}
	}
}).service('businessService',function(restFactory){
	
	var baseUrl = Rest.context.path+"/api/ccs/business/:businessId";
	var param = {};
	return{
		// 업체의 배송정책 목록 조회
		selectDeliverypolicyByBusinessId : function(businessId, callback){
			var param = {businessId : businessId}
			var url = Rest.context.path+"/api/ccs/business/dilivery/policy/:businessId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, null, callback);
		},
		//업체관리 화면 검색 목록 조회
		getBusinessManagerList : function(data, callback){
			var url = Rest.context.path+"/api/ccs/business/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//업체정보 상세 조회
		getBusiness : function(data, callback){
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, baseUrl, null, data, callback);
		},
		//업체정보 수정, 등록
		saveBusiness : function (data, callback){
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, baseUrl, param, data, callback);
		},
		//업체 승인(승인중 -> 미운영)
		changeState : function	(data, callback){
			var url = Rest.context.path+"/api/ccs/business/changeState";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		},
		//업체 배송정책 상세 조회
		getDeliverypolicy : function(data, callback) {
			var url = Rest.context.path+"/api/ccs/business/delivery/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		//업체 배송정책 수정
		updateDeliverypolicy : function(data, callback) {
			var url = Rest.context.path+"/api/ccs/business/delivery/:deliveryPolicyNo";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		},
		//업체 배송정책 목록
		getDeliveryList : function(data, callback){
			var url = Rest.context.path+"/api/ccs/business/delivery/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//업체 휴일 목록조회
		getBusinessholidayList : function (data, callback){
			var url = Rest.context.path+"/api/ccs/business/holiday/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//업체 배송정책 등록
		insertDelivery : function(data, callback) {
			var url = Rest.context.path+"/api/ccs/business/delivery/:deliveryPolicyNo";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		//업체 사용자 목록
		getUserList : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/user/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//업체,카테고리별 수수료율 조회
		getCommissionListByCategory : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/business/commission/category/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		}
		,//업체 수수료 목록조회
		getCommissionList : function (data, callback){
			var url = Rest.context.path+"/api/ccs/business/commission/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		}
	}
}).service('codeService',function(restFactory){
	var param = {};
	
	return{
		checkGridCodeGrp : function(data, callback){
			var url = Rest.context.path+"/api/ccs/code/group/check";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE,url, null, data, callback);	
		}
	}
}).service("menuService",  function(restFactory){
	return{
		getMenuTree : function (data, callback) {			
			var url = Rest.context.path + "/api/ccs/menu/group/tree";
			console.log("url1 :" , url);
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		getMenuList : function (callback) {			
			var url = Rest.context.path + "/api/ccs/menu/group/list";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, null, null, callback);
		},
		insertMenuGroup : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/menu/group/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		updateMenuGroupInfo : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/menu/group/update";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		deleteMenuGroup : function(menuGroupId, callback) {
			var url = Rest.context.path + "/api/ccs/menu/group/"+menuGroupId+"/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, null, callback);
		}
	}
}).service("reviewpermitService",  function(restFactory){
	var baseUrl = Rest.context.path+"/api/ccs/user/reviewpermit/:permitNo";
	var param = {};
	return {
		//체험관리 등록
		saveReviewpermit : function (data, callback){
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, baseUrl, param, data, callback);
		},
		//체험관리 검색 목록 조회
		getReviewpermitList : function(data, callback){
			var url = Rest.context.path+"/api/ccs/user/reviewpermit/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//회원 검색 목록조회
		getMemberList : function(data, callback){
			var url = Rest.context.path+"/api/mms/member/reviewpermit/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//체험관리 회원의 상품 목록 조회
		getReviewpermiProductList : function(data, callback){
			var url = Rest.context.path+"/api/ccs/user/reviewpermit/product/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//체험관리 회원과 상품 중복 체크
		getReviewpermitDuplicate :function(data, callback){
			var url = Rest.context.path+"/api/ccs/user/reviewpermit/duplicate";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		}
	}
}).service("applyService",  function(restFactory){
	var url = "";
	var param = {};
	return {
		// 적용대상, 제외상품 중복확인
		checkIdDuplicate : function(data, callback){
			url = Rest.context.path +"/api/ccs/common/checkIdDuplicate";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		}
	}
}).service("mainService",  function(restFactory){
	var url = "";
	var param = {};
	return {
		// 공지 사항 조회
		getNoticeList : function(callback) {
			var url = Rest.context.path + "/api/ccs/main/noticeList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, null, callback);
		},
		// 문의목록 조회
		getInquiryList : function(callback) {
			var url = Rest.context.path + "/api/ccs/main/inquiryList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, null, callback);
		},
		//문의 현황 조회
		getInquiryState : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/main/inquiryState";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		//상품QnA 목록 조회
		getProductQnaList2 : function(callback) {
			var url = Rest.context.path + "/api/ccs/main/productQnaList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, null, callback);
		},
		//상품QnA 현황 조회
		getProductQnaState : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/main/productQnaState";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		//주문 현황 조회
		getOrderState : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/main/orderState";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		// 업무 담당자 조회
		getMdList : function(callback) {
			var url = Rest.context.path + "/api/ccs/main/mdList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, null, callback);
		},
		// PO 공지 조회
		getPopupList : function(data, callback) {
			var url = Rest.context.path + "/api/ccs/main/popupList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		}		
	}
}).service('businessinquiryService',function(restFactory){
	
	var baseUrl = Rest.context.path+"/api/ccs/businessinquiry/:businessInquiryNo";
	var param = {};
	return{
		//입점상담정보 상세 조회
		getBusinessInquiry : function(data, callback){
			var url = Rest.context.path+"/api/ccs/businessinquiry/:businessInquiryNo";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		}
	}
});