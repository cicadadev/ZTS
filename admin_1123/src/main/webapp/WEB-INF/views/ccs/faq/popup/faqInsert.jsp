<%--
	화면명 : 문의관리 화면 > 신규 등록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.faq.js"></script>

<div class="wrap_popup" ng-app="faqApp" ng-controller="popCtrl as ctrl" ng-init="ctrl.detail()">
	<form name="form2">
		<h2 class="sub_title1">FAQ {{flagTxt}}</h2>
		<div class="box_type1">
			<h3 class="sub_title2">FAQ {{flagTxt}}<span ng-if="ccsNotice.noticeNo != null">(FAQ번호 {{ccsFaq.faqNo}}번)</span></h3>

			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="21%" />
					<col width="9%" />
					<col width="21%" />
					<col width="9%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr ng-if="ccsFaq.faqNo!=undefined">
						<th>등록자</th>
						<td>
							{{ ccsFaq.insId }}
							<!-- <input type="text" name="insdt" ng-model="ccsFaq.insId" style="width: 100%;" /> -->
						</td>
						<th>등록일시<i><spring:message code="c.input.required" /></i></th>
						<td>
							{{ ccsFaq.insDt }}
							<!-- <input type="text" name="insdt" ng-model="ccsFaq.insDt" style="width: 100%;" /> -->
						</td>
						<%-- <th><spring:message code="c.ccs.qna.sortno" /></th>
						<td><input type="tel" ng-model="ccsFaq.sortNo" v-key="ccsFaq.sortNo"/></td>
						<th><spring:message code="ccsFaq.useYn" /></th>
						<td>
							<input type="radio" value="Y" name="" ng-model="ccsFaq.useYn" id="useYnY" style="cursor: pointer;" ng-init="ccsFaq.useYn = 'Y'" /><label for="useYnY" style="cursor: pointer;">사용</label>
							<input type="radio" value="N" name="" ng-model="ccsFaq.useYn" id="useYnN" style="cursor: pointer;" /><label for="useYnN" style="cursor: pointer;">미사용</label>
						</td> --%>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.faq.type" /><i><spring:message code="c.input.required" /></i></th>
						<td><select-list ng-model="ccsFaq.faqTypeCd" code-group="FAQ_TYPE_CD" v-key="required"></select-list></td>
						<th>전시 여부<i><spring:message code="c.input.required" /></i></th>
						<td>
							<input type="radio" value="Y" name="" ng-model="ccsFaq.displayYn" id="displayYnY" style="cursor: pointer;" ng-init="ccsFaq.displayYn = 'Y'" /><label for="displayYnY" style="cursor: pointer;">전시</label>
							<input type="radio" value="N" name="" ng-model="ccsFaq.displayYn" id="displayYnN" style="cursor: pointer;" /><label for="displayYnN" style="cursor: pointer;">미전시</label>
						</td>
					</tr>
					<tr>
						<th><spring:message code="ccsFaq.title" /><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3"><input type="text" name="title" ng-model="ccsFaq.title" style="width: 100%;" v-key="ccsFaq.title" required/></td>
					</tr>
					<tr>
						<th>내용<%-- <spring:message code="c.ccs.qna.detail" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3"><textarea cols="50" rows="20" name="detail" ckeditor="ckOption" height="200px" ng-model="ccsFaq.detail" v-key="ccsFaq.detail" required></textarea></td>
					</tr>
					<%--
					<tr>
						<th>html</th>
						<td colspan="5"><textarea cols="50" rows="20" name="detail" ng-bind="detail"></textarea></td>
					</tr>
					--%>
				</tbody>
			</table>
		</div>

		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insert()" >
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />