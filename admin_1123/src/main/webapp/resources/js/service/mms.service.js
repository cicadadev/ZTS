var mmsServiceModule = angular.module("mmsServiceModule", ['ngResource']);

// 회원 서비스
mmsServiceModule.service("qnaService",  function(restFactory) {
	return {
		getQnaCategories: function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, '/api/mms/qna/category/list', null, data, callback);
		},
		getQnaList: function(callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/mms/qna/list', null, null, callback);
		},
		getQnaDetail: function(qnaNo, data, callback) {
			var param = {qnaNo: qnaNo};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/mms/qna/:qnaNo/detail', param, data, callback);
		},
		setQna: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, '/api/mms/qna/register', null, data, callback);
		}
	}
}).service('mmsService', function(restFactory){
	var param = [];
	
	return {
		getMemberDetail: function(memberNo, callback) {
			var param = {memberNo : memberNo};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/mms/member/detail/:memberNo', param, null, callback);
		},
		getMemberGradeHistory: function(memberNo, callback) {
			var param = {memberNo : memberNo};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, '/api/mms/member/gradeHistory', param, null, callback);
		},
		updateMarketingReceipt: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, '/api/mms/member/updateMarketingReceipt', null, data, callback);
		},
		getMemberCouponList: function(memberNo, callback) {
			var param = {memberNo : memberNo};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, '/api/mms/member/getMemberCouponList', param, null, callback);
		},
		getMemberCouponCnt: function(memberNo, callback) {
			var param = {memberNo : memberNo};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/mms/member/coupon/count', param, null, callback);
		},
		checkIssueCoupon: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, '/api/mms/member/checkIssueCoupon', null, data, callback);
		},
		saveMemberDeposit: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, '/api/mms/member/deposit/save', null, data, callback);
		},
		carrotBalanceAmt: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, '/api/mms/member/carrot/carrotBalanceAmt', null, data, callback);
		},
		depositBalanceAmt: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, '/api/mms/member/deposit/depositBalanceAmt', null, data, callback);
		},
		getMemberOffOrderProductList: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/mms/member/offlineOrder/productList', null, data, callback);
		},
		getMemberInterestOffshopList: function(data, callback) {
			var param = {memberNo : data};
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/mms/member/interestOffshop/list', param, null, callback);
		},
	}	
}).service('blacklistService', function(restFactory){
	var param = [];
	
	return {
		saveBlacklist: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, '/api/mms/blacklist/save', null, data, callback);
		},
		updateBlacklist: function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, '/api/mms/blacklist/update', null, data, callback);
		},
		getBlacklistDetail: function(blacklistNo, callback) {
			var param = {blacklistNo : blacklistNo};
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/mms/blacklist/detail/:blacklistNo', param, null, callback);
		},
		
	}	
});