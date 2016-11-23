<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<article class="con_box" data-ng-app="ccsAppPopup" data-ng-controller="ccs_popupListApp_controller as ctrl">

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.businessPopup()">
			<b>거래처팝업</b>
		</button>
		<input type="text" data-ng-model="search.businessId" />
		{{search.businessName}}
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.memberPopup()">
			<b>회원팝업</b>
		</button>
		<input type="text" data-ng-model="search.memId" />
		{{search.memName}}
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.exhibitPopup()">
			<b>기획전팝업</b>
		</button>
		<input type="text" data-ng-model="search.exhibitId" />
		{{search.exhibitId}}
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.restrictPopup()">
			<b>제어설정팝업</b>
		</button>
		<input type="text" data-ng-model="rstrct.controlNo" />
		{{rstrct.controlNo}}
	</div>
	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.brandPopup()">
			<b>브랜드팝업</b>
		</button>
		<input type="text" data-ng-model="search.brandId" />
		{{search.brandName}}
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.openCategoryPopup('pms/category')">
			<b>표준카테고리검색팝업</b>
		</button>
		<br />CATEGORY ID :<input type="text" data-ng-model="search.categoryId" style="width: 300px;" />
		<br />CATEGORY NAME :<input type="text" data-ng-model="search.categoryName" style="width: 300px;" />
		<br />CATEGORY FULL NAME :<input type="text" data-ng-model="search.categoryFullName" style="width: 500px;" />
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.openCategoryPopup('dms/displayCategory')">
			<b>전시카테고리검색팝업</b>
		</button>
		<br />CATEGORY ID :<input type="text" data-ng-model="search.dispCategoryId" style="width: 300px;" />
		<br />CATEGORY NAME :<input type="text" data-ng-model="search.dispCategoryName" style="width: 300px;" />
		<br />CATEGORY FULL NAME :<input type="text" data-ng-model="search.dispCategoryFullName" style="width: 500px;" />
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.userPopup()">
			<b>사용자팝업</b>
		</button>
		<input type="text" data-ng-model="search.userId" />
		{{search.userName}}
	</div>
	
	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.mdPopup()">
			<b>MD팝업</b>
		</button>
		<input type="text" data-ng-model="search.mdId" />
		{{search.mdName}}
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.productPopup()">
			<b>상품팝업</b>
		</button>
		<input type="text" data-ng-model="productObj.productId" />
		{{productObj.name}}
	</div>

	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.couponSearchPop()">
			<b>쿠폰팝업</b>
		</button>
		<input type="text" data-ng-model="couponObj.couponId" />
		{{couponObj.name}}
	</div>
	
	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.gridbulkuloadPopup()">
			<b>grid bulk upload 팝업</b>
		</button>
	</div>
	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.memberDetailPopup()">
			<b>회원상세 팝업</b>
		</button>		
	</div>
	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.addressPopup()">
			<b>주소검색 팝업</b>
		</button>		
	</div>
	<div>
		<button type="button" class="btn_type1" data-ng-click="ctrl.orderPopup()">
			<b>주문상품검색 팝업</b>
		</button>
		<br/><input type="text" data-ng-model="search.orderId" />
		<br/><input type="text" data-ng-model="search.productId" />
		<br/><input type="text" data-ng-model="search.saleproductId" />
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />