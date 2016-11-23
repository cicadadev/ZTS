<%--
	화면명 : 상품평관리 > 상품평 상세, 수정
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.review.manager.js"></script>	

<div class="wrap_popup" ng-app="productReviewApp" data-ng-controller="ccs_productReviewDetailApp_controller as ctrl" data-ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.review.detail"/> <!--상품평 상세 --></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="41%" />
					<col width="9%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<%-- <th>사이트<spring:message code="ccsSite.name"/><!-- 사이트 --></th>
						<td>{{ pmsReview.siteName }}</td> --%>
						<th>게시번호<%-- <spring:message code="pmsReviewrating.reviewNo"/> --%><!-- 구매후기번호 --></th>
						<td  colspan="3">{{ pmsReview.reviewNo }}</td>
					</tr>
					<tr>
						<th rowspan="2"><spring:message code="pmsProduct.name"/><!-- 상품명--></th>
						<td rowspan="2">{{ pmsReview.pmsProduct.name }}</td>
						<th>상품ID<%-- <spring:message code="pmsReview.productId"/> --%><!-- 상품번호--></th>
						<td>
							<div><a style="text-decoration: underline" href="javascript:void(0);" data-ng-click="ctrl.productPopup()">{{ pmsReview.productId }}</a></div>
						</td>
					</tr>
					<tr>
						<th>단품번호<!-- 단품번호--></th>
						<td>
							<div ng-hide ="pmsReview.saleproductId == null">{{ pmsReview.saleproductId }} / {{ pmsReview.saleproductName }}</div>
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsReview.displayYn"/><!-- 전시여부--></th>
						<td><!-- {{pmsReview.displayYn == 'Y' ? '전시' : '미전시'}} -->
							<input type="radio" value="Y" name="" ng-model="pmsReview.displayYn" id="displayYnY" style="cursor: pointer;" /><label for="displayYnY" style="cursor: pointer;">전시</label>
							<input type="radio" value="N" name="" ng-model="pmsReview.displayYn" id="displayYnN" style="cursor: pointer;" /><label for="displayYnN" style="cursor: pointer;">미전시</label>
						</td>
						<th>우수상품평</th>
						<td>
							<input type="radio" value="Y" name="" ng-model="pmsReview.bestYn" id="bestYnY" style="cursor: pointer;" /><label for="bestYnY" style="cursor: pointer;">Y</label>
							<input type="radio" value="N" name="" ng-model="pmsReview.bestYn" id="bestYnN" style="cursor: pointer;" /><label for="bestYnN" style="cursor: pointer;">N</label>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.mmsMember.memName"/><!-- 회원명--></th>
						<td>{{ pmsReview.memberInfo }}</td>
						<th>작성일<%-- spring:message code="c.grid.column.insDt"/> --%><!-- 등록일--></th>
						<td>{{ pmsReview.insDt }}</td>
					</tr>
<%-- 					<tr>
						<th>평균평점<spring:message code="pmsReviewrating.rating"/><!-- 평균별점--></th> 
						<td>{{ pmsReview.rating }}</td>
						<th><spring:message code="pmsReview.displayYn"/><!-- 전시여부--></th>
						<td>{{pmsReview.displayYn == 'Y' ? '전시' : '미전시'}}</td>
					</tr>
 --%>					<tr>
						<th><spring:message code="pmsReview.title"/><!-- 후기제목 --></th>
						<td colspan="3">{{ pmsReview.title }}</td>
					</tr>
					<tr>
						<th>평점</th>
						<td colspan="3" class="ValignT" style="height:90px;">
							<label style="width:100%">평균  : {{ pmsReview.rating }}</label>
							<br/>
							<br/>
							<div >
								<label style="width:50%" ng-repeat="rating in pmsReview.pmsReviewratings">{{ rating.ratingName }}  : {{ rating.rating }}</label>
							</div>
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsReview.detail"/><!-- 내용 --></th>
						<td colspan="3" class="ValignT" style="height:90px;">
							<br/>{{ pmsReview.detail }}
						</td>
					</tr>
					<tr>
						<th>첨부이미지</th>
						<td colspan="3">
							<img ng-src="pmsReview.img1" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt=""  style=" max-width: 33%; max-height: 300px;"> 
							<img ng-src="pmsReview.img2" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt=""  style=" max-width: 33%; max-height: 300px;"> 
							<img ng-src="pmsReview.img3" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt=""  style=" max-width: 33%; max-height: 300px;"> 
						</td>
					</tr>
					<!-- <tr>
						<th>첨부파일</th>
						<td colspan="3">
							{{ pmsReview.img1 }} {{ pmsReview.img2 }} {{ pmsReview.img3 }}
							<button type="button" class="btn_type2">
								<b>파일다운로드</b>
							</button>
						</td> 
					</tr>-->
				</tbody>
			</table>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close"/></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()" >
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>