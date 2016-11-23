<%--
	화면명 : 팝업관리 > 팝업 상세
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.popupnotice.js"></script>

<div class="wrap_popup" ng-app="popupnoticeApp" ng-controller="popCtrl as ctrl" ng-init="ctrl.detail()">

	<form name="form2">
		<h2 class="sub_title1">팝업공지<%-- <spring:message code="c.ccs.notice.title" /> --%> {{flagTxt}}</h2>
		<div class="box_type1">
			<%-- <h3 class="sub_title2">팝업공지<spring:message code="c.ccs.notice.title" /> {{flagTxt}}</h3> --%>

			<table class="tb_type1">
				<colgroup>
					<col width="13%" />
					<col width="38%" />
					<col width="13%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>팝업공지번호<%-- <spring:message code="c.ccs.notice.number" /> --%></th>
						<td ng-bind="ccsPopup.popupNo"></td>
						<th><spring:message code="ccsPopup.title" /><i><spring:message code="c.input.required" /></i></th>
						<td><input type="text" name="title" ng-model="ccsPopup.title" style="width: 100%;" v-key="ccsPopup.title" required/></td>
					</tr>
					<tr ng-if="ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.FRONT'">
						<th>공지유형<%-- <spring:message code="c.ccs.notice.type" /> --%></th>
							<td ng-bind="ccsPopup.popupTypeName">
						</td>
						<th>팝업공지채널<%-- <spring:message code="c.ccs.notice.type" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td>
							<input type="checkbox" ng-model="ccsPopup.pcDisplayYn" id="chk1"><label for="chk1">PC</label> 
							<input type="checkbox" ng-model="ccsPopup.mobileDisplayYn" id="chk2"><label for="chk2">MOBILE</label>
							<p class="information" ng-show="ccsPopup.pcDisplayYn==false && ccsPopup.mobileDisplayYn==false">필수 입력 항목 입니다.</p>
						</td>
					</tr>
					<tr ng-if="ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.PARTNER'">
						<th>공지유형<%-- <spring:message code="c.ccs.notice.type" /> --%></th>
						<td colspan="3" ng-bind="ccsPopup.popupTypeName">
						</td>
					</tr>
					<tr>
						<th>공지기간<%-- <spring:message code="c.ccs.notice.period" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td>
							<input type="text" ng-model="ccsPopup.startDt" datetime-picker width="130px" period-start/>
							~ 
							<input type="text" ng-model="ccsPopup.endDt" datetime-picker width="130px" period-end/>
							<p class="information" ng-show="!ccsPopup.startDt || !ccsPopup.endDt">필수 입력 항목 입니다.</p>
						</td>
						<th>전시여부<%-- <spring:message code="c.ccs.notice.displayYn" /> --%><i><spring:message code="c.input.required" /></i></th><!-- 전시여부 -->
						<td>
							<span ng-repeat="display in displayYns" >
								<input type="radio" id="displayYn{{use.val}}" ng-model="ccsPopup.displayYn" ng-init="ccsPopup.displayYn = 'Y'" ng-value="display.val" style="cursor: pointer;" />
								<label for="displayYn{{display.val}}" style="cursor: pointer;">{{display.text}}&nbsp;</label>
							</span>
						</td>
					</tr>
					<tr ng-if="ccsPopup.pcDisplayYn == true && ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.FRONT'">
						<th>PC 전시  URL<%-- <spring:message code="c.ccs.notice.position" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3" id="td_pcurl">
						<%-- <span ng-repeat="url in ccsPopup.ccsPcUrls">
							<div>
								<input type="text" ng-model="url.Url" style="width:29%;"/>
								<button type="button" class="btn_plus" ng-show="$last || !url.Url" ng-click="ctrl.addUrl('pc')"><spring:message code="c.common.add" /></button>
								<button type="button" class="btn_minus" ng-show="url.Url && !$last" ng-click="ctrl.deleteUrl($index, 'pc')"><spring:message code="c.common.delete" /></button>
								<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : !url.Url}" ng-click="ctrl.eraseUrl($index, 'pc')">지우개</button>							
							</div>
						</span>
						
						</td>
						<td> --%>
							<div ng-repeat="url in ccsPcUrls">
								<input type="text" ng-model="url.Url" style="width:34%;" ng-change="UrlsValidation('pc')"/>
								<button type="button" class="btn_minus" ng-show="!($first && $last)" ng-click="ctrl.deleteUrl($index, 'pc')"><spring:message code="c.common.delete" /></button>
								<button type="button" class="btn_plus" ng-show="$last" ng-click="ctrl.addUrl($index, 'pc')"><spring:message code="c.common.add" /></button>
								<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : !url.Url}" ng-click="ctrl.eraseUrl($index, 'pc')">지우개</button>
							</div>
							<p class="information" ng-show="pcUrlsRequired == true">
								{{pcUrlsRequired == true?'필수 입력 항목 입니다.':''}}
							</p>
						</td>
					</tr>
					<tr ng-if="ccsPopup.pcDisplayYn == true || ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.PARTNER'">
						<th>PC 전시 좌표<%-- <spring:message code="c.ccs.notice.position" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3">
							<label>Top: </label>
							<input type="text" ng-change="ctrl.inputNumber('positionT')" style="width:5%; text-align: center;" ng-model="ccsPopup.positionT" placeholder="-"/>
							<label>Left: </label>
							<input type="text" ng-change="ctrl.inputNumber('positionL')" style="width:5%; text-align: center;" ng-model="ccsPopup.positionL" placeholder="-"/>
							<label>Width: </label>
							<input type="text" ng-change="ctrl.inputNumber('positionW')" style="width:5%; text-align: center;" ng-model="ccsPopup.positionW" placeholder="-"/>
							<label>Height: </label>
							<input type="text" ng-change="ctrl.inputNumber('positionH')" style="width:5%; text-align: center;" ng-model="ccsPopup.positionH" placeholder="-"/>
							<p class="information" ng-show="!ccsPopup.positionT || !ccsPopup.positionL || !ccsPopup.positionW || !ccsPopup.positionH">필수 입력 항목 입니다.</p>
						</td>
					</tr>
					<tr ng-if="ccsPopup.pcDisplayYn == true || ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.PARTNER'">
						<th>PC 팝업공지내용<%-- <spring:message code="c.ccs.notice.detail" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3"><textarea cols="50" rows="20" name="detail1" ckeditor="ckOption" ng-model="ccsPopup.detail1" v-key="required"></textarea></td>
					</tr>
					<tr ng-if="ccsPopup.mobileDisplayYn == true && ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.FRONT'">
						<th>MOBILE 전시 URL<%-- <spring:message code="c.ccs.notice.position" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3" id="td_mourl">
							<div ng-repeat="url in ccsMoUrls">
								<input type="text" ng-model="url.Url" style="width:34%;" ng-change="UrlsValidation('mo')"/>
								<button type="button" class="btn_minus" ng-show="!($first && $last)" ng-click="ctrl.deleteUrl($index, 'mo')"><spring:message code="c.common.delete" /></button>
								<button type="button" class="btn_plus" ng-show="$last" ng-click="ctrl.addUrl($index, 'mo')"><spring:message code="c.common.add" /></button>
								<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : !url.Url}" ng-click="ctrl.eraseUrl($index, 'mo')">지우개</button>
							</div>
							<p class="information" ng-show="moUrlsRequired == true">
								{{moUrlsRequired == true?'필수 입력 항목 입니다.':''}}
							</p>
						</td>
					</tr>
					<tr ng-if="ccsPopup.mobileDisplayYn == true && ccsPopup.popupTypeCd == 'POPUP_TYPE_CD.FRONT'">
						<th>MOBILE 팝업공지내용<%-- <spring:message code="c.ccs.notice.detail" /> --%><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3"><textarea cols="50" rows="20" name="detail2" ckeditor="ckOption" ng-model="ccsPopup.detail2" v-key="required"></textarea></td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="btn_alignC marginT3">
			<!-- <button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.preview()">
				<b>미리보기</b>
			</button> -->
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