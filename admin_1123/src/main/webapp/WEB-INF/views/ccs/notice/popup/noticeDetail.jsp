 <%--
	화면명 : 공지사항 관리 > 공지사항 상세
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.notice.js"></script>

<div class="wrap_popup" ng-app="noticeApp" ng-controller="popCtrl as ctrl" ng-init="ctrl.detail()">

	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.notice.title" /> {{flagTxt}}</h2>
		<div class="box_type1">
			<%-- <h3 class="sub_title2"><spring:message code="c.ccs.notice.title" /> {{flagTxt}}<span ng-if="ccsNotice.noticeNo != null">(<spring:message code="c.ccs.notice.number" /> {{ccsNotice.noticeNo}}번)</span></h3> --%>

			<table class="tb_type1">
				<colgroup>
					<col width="12%" />
					<col width="41%" />
					<col width="12%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.ccs.notice.number" /></th>
						<td ng-bind="ccsNotice.noticeNo"></td>
						<th>조회수</th>
						<td> {{ ccsNotice.readCnt | number }} </td>
					</tr>
					<tr ng-if="ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.EVENT'">
						<th><spring:message code="c.ccs.notice.detail.type" /><i><spring:message code="c.input.required" /></i></th><!-- 공지사항 유형  -->
						<td colspan="3">
							{{ ccsNotice.noticeTypeName }}
						</td>
					</tr>
					<tr ng-if="ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.EVENT'">
						<th>이벤트번호<i><spring:message code="c.input.required" /></i></th><!-- 공지사항 유형  -->
						<td>
							<input type="text" ng-model="ccsNotice.eventId" v-key='required'/>
						</td>
						<th>구분 <i><spring:message code="c.input.required" /></i></th>
						<td>
							<span ng-repeat="permit in permitYns" >
								<input type="radio" id="permitYn{{permit.val}}" ng-model="ccsNotice.eventTargetDivCd" ng-value="permit.val" style="cursor: pointer;" />
								<label for="permitYn{{permit.val}}" style="cursor: pointer;">{{permit.text}}&nbsp;</label>
							</span>
						</td>
							<!-- <select ng-if="type == 'F'" ng-model="ccsNotice.noticeTypeCd">
								<option ng-if="ccsNotice.noticeNo == undefined" value="">선택하세요</option>
								<option ng-repeat="noticeType in noticeTypeCdsF" value="{{noticeType.cd}}" >{{noticeType.name}}</option>
							</select>
							<select ng-if="type == 'P'" ng-model="ccsNotice.noticeTypeCd">
								<option ng-if="ccsNotice.noticeNo == undefined" value="">선택하세요</option>
								<option ng-repeat="noticeType in noticeTypeCdsP" value="{{noticeType.cd}}" >{{noticeType.name}}</option>
							</select>
							<input type="hidden" ng-model="ccsNotice.noticeTypeCd" v-key='required'/> -->
					</tr>
					<tr ng-if="ccsNotice.noticeTypeCd != 'NOTICE_TYPE_CD.EVENT'">
						<th><spring:message code="c.ccs.notice.detail.type" /><i><spring:message code="c.input.required" /></i></th><!-- 공지사항 유형  -->
						<td colspan="3">
							{{ ccsNotice.noticeTypeName }}
						</td>
					</tr>
					<tr ng-if="ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.FRONT' || ccsNotice.noticeTypeCd == 'NOTICE_TYPE_CD.EVENT'">
						<th>브랜드</th><!-- 공지사항 유형  -->
						<td id="td_brand">
							<div ng-repeat="brand in ccsBrands">
								<select ng-model="brand.name" select-code="BRAND_CD" ng-change="ctrl.brandValidation($index)" style="width:150px;">
									<option value="">선택하세요</option>
								</select>
								<button type="button" class="btn_minus" ng-show="!($first && $last)" ng-click="ctrl.deleteBrand($index)"><spring:message code="c.common.delete" /></button>
								<button type="button" class="btn_plus" ng-show="$last" ng-click="ctrl.addBrand($index)"><spring:message code="c.common.add" /></button>
							</div>
							<!-- <p class="information" ng-show="BrandsCheck == true">필수 입력 항목 입니다.</p> -->
						</td>
						<th>TOP노출여부</th>
						<td>
							<span ng-repeat="top in topYns" >
								<input type="radio" id="topNoticeYn{{top.val}}" ng-model="ccsNotice.topNoticeYn" ng-value="top.val" style="cursor: pointer;" />
								<label for="topNoticeYn{{top.val}}" style="cursor: pointer;">{{top.text}}&nbsp;</label>
							</span>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.notice.detail.period" /><i><spring:message code="c.input.required" /></i></th><!-- 공지기간 -->
						<td>
							<input type="text" ng-model="ccsNotice.startDt" datetime-picker width="150px"  period-start/>
							~ 
							<input type="text" ng-model="ccsNotice.endDt" datetime-picker width="150px"  period-end/>
							<p class="information" ng-show="!ccsNotice.startDt || !ccsNotice.endDt">필수 입력 항목 입니다.</p>
						</td>
						<th>전시여부<%-- <spring:message code="c.ccs.notice.displayYn" /> --%><i><spring:message code="c.input.required" /></i></th><!-- 전시여부 -->
						<td colspan="3">
							<span ng-repeat="display in displayYns" >
								<input type="radio" id="displayYn{{display.val}}" ng-model="ccsNotice.displayYn" ng-init="ccsNotice.displayYn = 'Y'" ng-value="display.val" style="cursor: pointer;" />
								<label for="displayYn{{display.val}}" style="cursor: pointer;">{{display.text}}&nbsp;</label>
							</span>
						</td>
						<%-- <th><spring:message code="ccsNotice.fixYn" /></th>
						<td>
							<input type="radio" value="Y" name="" ng-model="ccsNotice.fixYn" id="fixYnY" style="cursor: pointer;" ng-init="ccsNotice.fixYn = 'Y'" /><label for="fixYnY" style="cursor: pointer;"><spring:message code="c.input.radio.fixY" /></label>
							<input type="radio" value="N" name="" ng-model="ccsNotice.fixYn" id="fixYnN" style="cursor: pointer;" /><label for="fixYnN" style="cursor: pointer;"><spring:message code="c.input.radio.fixN" /></label>
						</td> --%>
						<%-- <th><spring:message code="c.ccs.notice.type" /><i><i><spring:message code="c.input.required" /></i></th>
						<td><select-list ng-model="ccsNotice.noticeTypeCd" code-group="NOTICE_TYPE_CD" v-key="required"></select-list></td>
						<th><spring:message code="ccsNotice.useYn" /></th>
						<td>
							<input type="radio" value="Y" name="" ng-model="ccsNotice.useYn" id="useYnY" style="cursor: pointer;" ng-init="ccsNotice.useYn = 'Y'" /><label for="useYnY" style="cursor: pointer;"><spring:message code="c.input.radio.useY" /></label>
							<input type="radio" value="N" name="" ng-model="ccsNotice.useYn" id="useYnN" style="cursor: pointer;" /><label for="useYnN" style="cursor: pointer;"><spring:message code="c.input.radio.useN" /></label>
						</td> --%>
					</tr>
					<tr>
						<th><spring:message code="ccsNotice.title" /><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3"><input type="text" name="title" ng-model="ccsNotice.title" style="width: 100%;" v-key="ccsNotice.title" required/></td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.notice.detail" /><i><spring:message code="c.input.required" /></i></th><!-- 내용 -->
						<td colspan="3"><textarea cols="50" rows="20" name="detail" ckeditor="ckOption" ng-model="ccsNotice.detail" v-key="ccsNotice.detail" required></textarea></td>
					</tr>
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