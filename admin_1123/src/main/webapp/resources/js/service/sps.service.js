//사용할 메세지 키 정의
Constants.message_keys = ["SPS.COUPON.REG.INFO", "SPS.COUPON.REG.INFO2"];

var spsServiceModule = angular.module("spsServiceModule", ['ngResource']);

spsServiceModule.service('couponService',function(restFactory){
//	var url = Rest.context.path + "/api/sps/coupon/:id";
	var url = "";
	var param = {};
	
	return {
		
		createCoupon : function(data, callback){
			url = Rest.context.path +"/api/sps/coupon/popup/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		getCouponCount : function(data, callback){
			url = Rest.context.path + "/api/sps/coupon/couponIssueState";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
//		duplicateCheck : function(data, callback) {
//			url = Rest.context.path + "/api/sps/coupon/duplicateCheck";
//			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
//		},
		createPrivateCin : function(data, callback) {
			url = Rest.context.path + "/api/sps/coupon/createPrivateCin";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		getCouponDetail : function(data, callback){
			url = Rest.context.path +"/api/sps/coupon/popup/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		updateCoupon : function(data, callback){
			url = Rest.context.path +"/api/sps/coupon/update";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		updateCouponState : function(data, callback){
			url = Rest.context.path +"/api/sps/coupon/updateCouponState";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		stopIssueCoupon : function(data, callback) {
			url = Rest.context.path +"/api/sps/coupon/issued/stop";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, param, data, callback);
		},
		issueCoupon : function(data, callback) {
			url = Rest.context.path +"/api/sps/coupon/issue";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		}
		
	}
	
}).service('promotionPresentService', function(restFactory){
	var param = [];
	
	return {
		getPresentPromotionList : function(data,callback) {
			url = Rest.context.path + "/api/sps/present/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		},
		updatePresent : function(data,callback) {
			url = Rest.context.path + "/api/sps/present/popup/update";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		getPresentPromotionDetail : function(data,callback) {
			url = Rest.context.path + "/api/sps/present/popup/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		checkIdDuplicatePresent : function(data, callback){
			url = Rest.context.path +"/api/sps/present/checkIdDuplicatePresent";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		},
		updatePresentState : function(data, callback){
			url = Rest.context.path +"/api/sps/present/updatePresentState";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		}
	}	
}).service('eventService', function(restFactory){
	var param = [];

	return {
		getEventDetail : function(data,callback) {
			var url = Rest.context.path + "/api/sps/event/popup/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		updateEventInfo : function(data,callback) {
			var url = Rest.context.path + "/api/sps/event/popup/update";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		insertEvent : function(data,callback) {
			var url = Rest.context.path + "/api/sps/event/popup/insert";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		updateEventcoupon : function(data,callback) {
			var url = Rest.context.path + "/api/sps/event/popup/coupon/update";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		getEventjoinDetail : function(data,callback) {
			var url = Rest.context.path + "/api/sps/event/popup/join/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		deleteEventProduct : function(data, callback) {
			var url = Rest.context.path + "/api/sps/event/product/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		updateEventStatus : function(data, callback) {
			var url = Rest.context.path + "/api/sps/event/updateEventStatus";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		}
	}
}).service('cardService', function(restFactory){
	var param = {};

	return {
		//카드사 할인정보 목록 조회
		getCreditCardList : function(data,callback) {
			var url = Rest.context.path + "/api/sps/discount/card/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		},
		//카드사 할인정보 상세조회
		getCreditCardInfo : function(data, callback) {
			var url = Rest.context.path+"/api/sps/discount/card/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		//카드사 할인정보 저장
		saveCreditCard: function(data, callback){
			var url = Rest.context.path+"/api/sps/discount/card/save";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		},
		changeState : function	(data, callback){
			var url = Rest.context.path+"/api/sps/discount/changeState";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, url, param, data, callback);
		}
	}
	
}).service('dealService', function(restFactory){
	var param = [];

	return {
		getDealDetail : function(data,callback) {
			var url = Rest.context.path + "/api/sps/deal/" + data;
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		updateDeal : function(data,callback) {
			var url = Rest.context.path + "/api/sps/deal/updateDeal";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, param, data, callback);
		},
		getDealGroupTree : function(data,callback) {
			var url = Rest.context.path + "/api/sps/deal/dealGroupTree";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		},
		getSaleProdcutList : function(data,callback) {
			var url = Rest.context.path + "/api/sps/deal/product/saleProdcutList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		},
		checkDealProduct : function(data,callback) {
			var url = Rest.context.path + "/api/sps/deal/product/checkDealProduct";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		getDealMemberGradeBenefit : function(data, callback) {
			var url = Rest.context.path + "/api/sps/deal/product/memberGradeBenefit";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		},
		getDealSaleProductPrice : function(data, callback) {
			var url = Rest.context.path + "/api/sps/deal/product/SaleProductPrice";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		}
}
	
}).service('pointService', function(restFactory){
	var param = [];

	return {
		getPointDetail : function(data,callback) {			
			var param = {pointSaveId : '@pointSaveId'}
			var url = Rest.context.path + "/api/sps/point/:pointSaveId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, data, callback);
		},
		savePoint : function(data,callback) {
			var url = Rest.context.path + "/api/sps/point/save";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		updatePointState : function(data, callback){
			url = Rest.context.path +"/api/sps/point/updatePointState";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		}
		
	}
	
}).service('carrotService', function(restFactory){
	var param = [];

	return {
		saveCarrot : function(data,callback) {
			var url = Rest.context.path + "/api/sps/carrot/save";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		},
		getCarrotSummry : function(data, callback) {
			var url = Rest.context.path + "/api/sps/carrot/summry";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, data, callback);
		}
		
	}
	
});