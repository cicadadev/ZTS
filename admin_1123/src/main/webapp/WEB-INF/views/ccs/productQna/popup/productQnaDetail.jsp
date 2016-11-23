<%--
	화면명 : 상품QnA관리 > 상품QnA 상세, 답변등록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.product.qna.list.js"></script>	

<div class="wrap_popup" ng-app="productQnaManagerApp" data-ng-controller="productQnaDetailController as ctrl" data-ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.productqna.detail"/> <!-- 문의 상세 및 답변 --></h2>
		<div class="box_type1">
			<h3 class="sub_title2"><spring:message code="c.ccs.productqna.qna"/></h3>
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="15%" />
					<col width="9%" />
					<col width="15%" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="pmsProductqna.productQnaNo"/><!-- qna	번호 --></th>
						<td>{{ pmsProductqna.productQnaNo }}</td>
						<th>Q&A유형</th>
						<td> {{pmsProductqna.productQnaTypeName}}
							<!-- <select ng-model="pmsProductqna.productQnaTypeCd" select-code="PRODUCT_QNA_TYPE_CD" style="width:150px;"  v-key="required">
								<option ng-selected="pmsProductqna.productQnaTypeCd" value="{{ pmsProductqna.productQnaTypeCd }}"></option>
							</select> -->
						</td>
					</tr>
					<tr ng-if="!poBusinessId">
						<th>담당업체</th>
						<td>{{pmsProductqna.ccsBusiness.name}}</td>
						<th>공개여부<%-- <spring:message code="pmsProductqna.displayYn"/> --%></th><!-- 전시여부  -->
						<td>
							<span ng-repeat="display in displayYns" >
								<input type="radio" id="displayYn{{use.val}}" ng-model="pmsProductqna.displayYn" ng-init="pmsProductqna.displayYn = 'Y'" ng-value="display.val" style="cursor: pointer;" />
								<label for="displayYn{{display.val}}" style="cursor: pointer;">{{display.text}}&nbsp;</label>
							</span>
						</td> 
					</tr>
					<tr>
						<th>상품명</th>
						<td>{{ pmsProductqna.pmsProduct.name }}</td>
						<th>상품 정보</th>
						<td>
							<div data-ng-hide="pmsProductqna.productId == null">상품번호 :{{pmsProductqna.productId}}</div>
							<div data-ng-hide="pmsProductqna.saleproductId == null">단품번호 :{{pmsProductqna.saleproductId}} / {{pmsProductqna.pmsSaleproduct.name}}</div>
						</td>
					</tr>
					<tr>
						<th>Q&A상태</th>
						<td>{{pmsProductqna.productQnaStateName}}
						</td>
						<th>경과시간</th>
						<td>{{pmsProductqna.passTime}}</td>
					</tr>
					<tr>
						<th>Q&A등록자</th>
						<td>{{pmsProductqna.questioner}}</td>
						<th>Q&A등록일시</th>
						<td>{{pmsProductqna.insDt}}</td>
					</tr>
					<tr>
						<th>Q&A확인자</th>
						<td><div ng-hide ="pmsProductqna.confirmId == null">{{ pmsProductqna.confirmer}}</div></td>
						<th>Q&A확인일시</th>
						<td>{{ pmsProductqna.confirmDt}}</td>
					</tr>
					<tr>
						<th><spring:message code="pmsProductqna.title"/><!-- 문의제목 --></th>
						<td colspan="3">{{ pmsProductqna.title }}</td>
					</tr>
					<tr>
						<th><spring:message code="pmsProductqna.detail"/><!-- 문의내용 --></th>
						<td colspan="3" class="ValignT" style="height:90px;">
							{{ pmsProductqna.detail }}
						</td>
					</tr>
					<tr>
						<th>첨부이미지</th>
						<td colspan="3" class="ValignT" style="height:90px;">
						<img-tag ng-model="pmsProductqna.img" ></img-tag>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
		<div class="btn_alignC">
			<button type="button" data-ng-click="ctrl.update()" class="btn_type3 btn_type3" ng-if="!poBusinessId">
				<b>수정</b>
			</button>
			<button type="button" data-ng-click="ctrl.updateQnaConfirm()" class="btn_type3 btn_type3_purple" data-ng-hide="pmsProductqna.productQnaStateCd != 'PRODUCT_QNA_STATE_CD.ACCEPT'">
				<b>Q&A 확인</b>
			</button>
		</div>
	</form>
		
	<form name="form3">
		<div class="box_type1 marginT2">
			<h3 class="sub_title2"> Q&A 답변내용 {{pmsProductqna.productQnaStateName}}<%-- <spring:message code="pmsProductqna.answer"/> --%></h3>
	
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="15%" />
					<col width="9%" />
					<col width="15%" />
				</colgroup>
				<tbody>
					<tr>
						<th>Q&A 답변등록자<%-- <spring:message code="c.ccs.productqna.userid"/> --%><!-- 담당자 --></th>
						<td><div ng-hide ="pmsProductqna.answerId == null">{{ pmsProductqna.answerer}}</div></td>
						<th>등록일시<%-- <spring:message code="pmsProductqna.answerDt"/> --%><!-- 답변일시 --></th>
						<td>{{ pmsProductqna.answerDt }}</td>
					</tr>
					<tr>
						<th>
							내용<%-- <spring:message code="pmsProductqna.answer"/> --%><!-- 답변내용 -->
						</th>
						<td colspan="3">
							<textarea style="height: 200px;" cols="50" rows="20" ng-required="true" v-key="pmsProductqna.answer" ng-model="pmsProductqna.answer" ckeditor="ckOption" >{{ pmsProductqna.answer }}</textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateAnswer()" ng-if="pmsProductqna.productQnaStateCd != 'PRODUCT_QNA_STATE_CD.COMPLETE'">
				<b>답변완료</b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>