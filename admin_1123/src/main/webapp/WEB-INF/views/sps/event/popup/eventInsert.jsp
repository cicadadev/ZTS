<%--
	화면명 : 프로모션관리 > 이벤트 관리 > 이벤트 등록 팝업
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/lib/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="/resources/js/app/sps.app.event.list.js"></script>

<div class="wrap_popup" ng-app="eventApp" ng-controller="sps_eventInsertPopApp_controller as ctrl">
<form name="frmInsertEvent">
	<%-- <h2 class="sub_title1"><spring:message code="c.sps.event.subtitle2" /></h2> --%>
	<ul class="tab_type2">
		<li class="on">
			<button type="button"><spring:message code="c.sps.event.subtitle2" /></button>
		</li>
		<li class="disabled">
			<button type="button"><spring:message code="c.sps.event.subtitle5" /></button>
		</li>
	</ul>

	<!-- ### 이벤트_기본정보 ### -->
	<div class="box_type1 marginT2">
		<h3 class="sub_title2"><spring:message code="c.sps.event.subtitle2" /></h3>

		<table class="tb_type1">
			<colgroup>
				<col width="13%" />
				<col width="36%" />
				<col width="13%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.sps.event.eventId" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<span ng-model="spsEvent.eventId" value=""></span>
					</td>
					<th><spring:message code="c.sps.event.name" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="text" ng-model="spsEvent.name" placeholder="" style="width:40%;" v-key="spsEvent.name" required />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.eventType" /> <i><spring:message code="c.input.required" /></i></th>
					<td colspan="3">
						<radio-list ng-model="spsEvent.eventTypeCd" code-group="EVENT_TYPE_CD"></radio-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.eventDesc" /></th>
					<td colspan="3">
						<textarea rows="3" cols="2" ng-model="spsEvent.detail" ></textarea>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.displayPeriod" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="text" ng-model="spsEvent.eventStartDt" placeholder="" datetime-picker period-start required />
						~
						<input type="text" ng-model="spsEvent.eventEndDt" placeholder="" datetime-picker period-end required />
						<p class="information" ng-show="spsEvent.eventStartDt == null || spsEvent.eventEndDt == null">필수 입력 항목 입니다.</p>
					</td>
					<th><spring:message code="c.sps.event.eventState" /> <i><spring:message code="c.input.required" /></i></th>
					<td ng-bind="spsEvent.eventStateName"></td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.joinPeriod" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="text" ng-model="spsEvent.eventJoinStartDt" placeholder="" datetime-picker period-start required />
						~
						<input type="text" ng-model="spsEvent.eventJoinEndDt" placeholder="" datetime-picker period-end required />
						<p class="information" ng-show="spsEvent.eventJoinStartDt == null || spsEvent.eventJoinEndDt == null">필수 입력 항목 입니다.</p>
					</td>
					<th><spring:message code="spsEvent.displayYn" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="radio" ng-model="spsEvent.displayYn" value="Y"/><label for="radio1"><spring:message code="c.input.radio.displayY" /></label>
						<input type="radio" ng-model="spsEvent.displayYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.displayN" /></label>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.restrictType" /></th>
					<td>
						<control-set model-name="spsEvent" lebels="전체,사용자설정" ></control-set>
					</td>
					<th><spring:message code="c.sps.event.brand" /> </th>
					<td id="td_brand">
						<div>
							<input type="text" ng-model="spsEvent.spsEventbrand0.brandId" style="width:29%;" readonly disabled/>
							<input type="text" ng-model="spsEvent.spsEventbrand0.brandName" style="width:30%;" readonly disabled/> 
							<button type="button" class="btn_type2" ng-click="ctrl.brandSearch(0)"><b><spring:message code="c.search.btn.search" /></b></button>
							<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : !spsEvent.spsEventbrand0.brandId}" ng-click="ctrl.eraseBrand(0)"><spring:message code="c.search.btn.eraser" /></button>
							<button type="button" class="btn_plus" ng-click="ctrl.addBrand()"><spring:message code="c.common.add" /></button>
						</div>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.winNoticeDate" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="text" ng-model="spsEvent.winNoticeDate" placeholder="" v-key="spsEvent.winNoticeDate" datetime-picker date-only required />
					</td>
					<th><spring:message code="c.sps.event.joinRule" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<radio-list ng-model="spsEvent.joinControlCd" code-group="JOIN_CONTROL_CD"></radio-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="spsEvent.winnerNumber" /></th>
					<td>
						<input type="number" ng-model="spsEvent.winnerNumber" placeholder=""/>
					</td>
					<th><spring:message code="spsEvent.winnerShowYn" /></th>
					<td>
						<input type="radio" value="N" ng-model="spsEvent.winnerShowYn" /><label for="radio1"><spring:message code="c.sps.event.winnerShowN" /></label>
						<input type="radio" value="Y" ng-model="spsEvent.winnerShowYn" /><label for="radio2"><spring:message code="c.sps.event.winnerShowY" /></label>
					</td>
				</tr>				
				<tr>
					<th><spring:message code="c.sps.event.eventDiv" /> <i><spring:message code="c.input.required" /></i></th>
					<td colspan="3">
						<radio-list ng-model="spsEvent.eventDivCd" code-group="EVENT_DIV_CD"></radio-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.upperHtml" arguments="PC" htmlEscape="false" /></th>
					<td colspan="3">
						<textarea ckeditor="ckPcOption" ng-model="spsEvent.html1" v-key="spsEvent.html1"></textarea>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.upperHtml" arguments="Mobile" htmlEscape="false" /></th>
					<td colspan="3">
						<textarea ckeditor="ckPcOption" ng-model="spsEvent.html2" v-key="spsEvent.html2"></textarea>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- ### //이벤트_기본정보 ### -->

	<!-- ### 이벤트 배너정보 설정 ### -->
	<div>
		<table class="tb_type1">
			<colgroup>
				<col width="13%" />
				<col width="36%" />
				<col width="13%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr class="column2">
					<th>
						<spring:message code="c.sps.event.bannerImg" arguments="PC" htmlEscape="false" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td>
						<div class="input_file">
							<input type="file" id="img1" name="pcfileModel" image-key="spsEvent.img1" file-upload />
							<input type="text" id="img1Path" ng-model="spsEvent.img1" placeholder="" required />
							<button type="button" class="btn_type2 btn_addFile">
								<b><spring:message code="c.common.file.search" /></b>
							</button>
							<!-- <button type="button" class="btn_file_del">파일 삭제</button> -->
							<span><spring:message code="common.imgUpload.text1" /></span>
							<p class="information" ng-show="spsEvent.img1 == null">필수 입력 항목 입니다.</p>
						</div>

						<p class="txt_type1">
							<spring:message code="common.imgUpload.text2" />
						</p>

						<div class="preview" ng-show="spsEvent.img1 != '' && spsEvent.img1 != null">
							<img ng-src="spsEvent.img1" onError="this.src='/resources/img/bg/bg_temp_img.gif';" img-domain name="img1" alt="" />
							<button type="button" class="btn_file_del" ng-click="ctrl.deletePreview('img1')"><spring:message code="c.sps.event.deleteFile" /></button>
						</div>
					</td>
					<th>
						<spring:message code="c.sps.event.bannerImg" arguments="Mobile" htmlEscape="false" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td>
						<div class="input_file">
							<input type="file" id="img2" name="mofileModel" image-key="spsEvent.img2" file-upload />
							<input type="text" id="img2Path" ng-model="spsEvent.img2" placeholder="" required />
							<button type="button" class="btn_type2 btn_addFile">
								<b><spring:message code="c.common.file.search" /></b>
							</button>
							<!-- <button type="button" class="btn_file_del">파일 삭제</button> -->
							<span><spring:message code="common.imgUpload.text1" /></span>
							<p class="information" ng-show="spsEvent.img2 == null">필수 입력 항목 입니다.</p>
						</div>

						<p class="txt_type1">
							<spring:message code="common.imgUpload.text2" />
						</p>

						<div class="preview" ng-show="spsEvent.img2 != '' && spsEvent.img2 != null">
							<img ng-src="spsEvent.img2" onError="this.src='/resources/img/bg/bg_temp_img.gif';" img-domain name="img2" alt="" />
							<button type="button" class="btn_file_del" ng-click="ctrl.deletePreview('img2')"><spring:message code="c.sps.event.deleteFile" /></button>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- ### //이벤트 배너정보 설정 ### -->
	
	<!-- ### //쿠폰이벤트 쿠폰등록/목록 ### -->
	<div ng-if="spsEvent.eventTypeCd == 'EVENT_TYPE_CD.COUPON'">
		<table class="tb_type1">
			<colgroup>
				<col width="13%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>
						<spring:message code="c.sps.event.title.addCoupon" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td>
						<div class="btn_alignR marginT1">
							<button type="button" class="btn_type1" ng-click="ctrl.addCouponPopup('eventcoupon', 0)">
								<b><spring:message code="c.sps.event.btn.addCoupon" /></b>
							</button>
							<button type="button" class="btn_type1" ng-click="ctrl.deleteGridRows('coupon')">
								<b><spring:message code="c.common.delete" /></b>
							</button>
						</div>
		
						<div class="box_type1 marginT1">
							<h3 class="sub_title2">
								<spring:message code="c.sps.event.subtitle7" />
								<span><spring:message code="c.search.totalCount" arguments="{{ grid_couponEvent.totalItems }}" /></span>
							</h3>
							
							<div class="tb_util tb_util_rePosition">
								<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
							</div>
							
							<div class="gridbox gridbox300">
								<div class="grid" ui-grid="grid_couponEvent"
									ui-grid-move-columns 
									ui-grid-resize-columns
									ui-grid-auto-resize
									ui-grid-pagination 
									ui-grid-selection 
									ui-grid-row-edit
									ui-grid-cell-nav
									ui-grid-exporter
									ui-grid-edit 
									ui-grid-validate></div>
							</div>
							<p class="information" ng-show="grid_couponEvent.data.length==0">필수 입력 항목 입니다.</p>
						</div>		
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- ### //쿠폰이벤트 쿠폰등록/목록 ### -->
	
	<!-- ### //출석이벤트 혜택정보 ### -->
	<div ng-if="spsEvent.eventTypeCd == 'EVENT_TYPE_CD.ATTEND'">
		<table class="tb_type1">
			<colgroup>
				<col width="13%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>
						<spring:message code="c.sps.event.prize" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td id="td_prize">
						<div id="div_prize0">
							<table class="tb_type1 marginT1" style="border-top:1px solid #cfcfcf">
								<colgroup>
									<col width="13%" />
									<col width="45%" />
									<col width="13%" />
									<col width="*" />
								</colgroup>
								<tbody>
									<tr>
										<th><spring:message code="c.sps.event.joinType" />  <i><spring:message code="c.input.required" /></i></th>
										<td>
											<radio-list ng-model="spsEvent.spsEventprizes0.joinTypeCd" code-group="JOIN_TYPE_CD" ></radio-list>
											<div ng-if="spsEvent.spsEventprizes0.joinTypeCd == 'JOIN_TYPE_CD.DATE'" style="margin-top:10px;">
												<input type="text" ng-model="spsEvent.spsEventprizes0.joinDate" placeholder="" v-key="spsEvent.spsEventprizes0.joinDate" required period-end datetime-picker />
											</div>
											<div ng-if="spsEvent.spsEventprizes0.joinTypeCd == 'JOIN_TYPE_CD.TERM'" style="margin-top:10px;">
												<input type="text" ng-model="spsEvent.spsEventprizes0.joinStartDt" placeholder="" datetime-picker period-start required/>
												~
												<input type="text" ng-model="spsEvent.spsEventprizes0.joinEndDt" placeholder="" datetime-picker period-end required/>
												<p class="information" ng-show="spsEvent.spsEventprizes0.joinStartDtInput == null || spsEvent.spsEventprizes0.joinEndDtInput == null">필수 입력 항목 입니다.</p>
											</div>
											<div ng-if="spsEvent.spsEventprizes0.joinTypeCd == 'JOIN_TYPE_CD.DAYS' || spsEvent.spsEventprizes0.joinTypeCd == 'JOIN_TYPE_CD.JOINDAYS'" style="margin-top:10px;">
												<input type="number" ng-model="spsEvent.spsEventprizes0.dayValue" v-key="spsEvent.spsEventprizes0.dayValue" required />&nbsp;<span>일</span>
											</div>
										</td>
										<th><spring:message code="c.sps.event.prizeType" /></th>
										<td>
											<radio-list ng-model="spsEvent.spsEventprizes0.prizeTypeCd" code-group="PRIZE_TYPE_CD" ></radio-list>
											<div ng-if="spsEvent.spsEventprizes0.prizeTypeCd == 'PRIZE_TYPE_CD.COUPON'" style="margin-top:10px;">
												<input type="text" ng-model="spsEvent.spsEventprizes0.couponId" ng-readonly="true" disabled/>
												<input type="text" ng-model="spsEvent.spsEventprizes0.couponName" ng-readonly="true" disabled/>
												<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : !spsEvent.spsEventprizes0.couponId}" ng-click="ctrl.eraseCoupon(0)">지우개</button>
												<button type="button" class="btn_type2" ng-click="ctrl.addCouponPopup('eventprize', 0)"><b><spring:message code="c.sps.event.srchCoupon" /></b></button>
											</div>
											<div ng-if="spsEvent.spsEventprizes0.prizeTypeCd == 'PRIZE_TYPE_CD.POINT'" style="margin-top:10px;">
												<input type="number" ng-model="spsEvent.spsEventprizes0.savePoint" v-key="spsEvent.spsEventprizes0.savePoint"/>&nbsp;<span>점</span>
											</div>
										</td>
									</tr>								
								</tbody>
							</table>
							<div class="btn_alignR">
								<button type="button" class="btn_type1" ng-click="ctrl.addEventprize()">
									<b><spring:message code="c.sps.event.btn.addPrize" /></b>
								</button>
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- ### //출석이벤트 혜택정보 ### -->
	
	<!-- ### //체험이벤트 상품 등록/목록 ### -->
	<div ng-if="spsEvent.eventTypeCd == 'EVENT_TYPE_CD.EXP'">
		<table class="tb_type1">
			<colgroup>
				<col width="13%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.sps.event.commentEndDt" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="text" ng-model="spsEvent.commentEndDt" placeholder="" v-key="spsEvent.commentEndDt" datetime-picker required />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.product.idname" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="text" placeholder="" ng-model="spsEvent.pmsProduct.productId" readonly disabled required />
						<input type="text" placeholder="" ng-model="spsEvent.pmsProduct.name" readonly disabled required />
						<button type="button" class="btn_type2" ng-click="ctrl.addProductPopup()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<p class="information" ng-show="spsEvent.pmsProduct.productId == null || spsEvent.pmsProduct.name == null">필수 입력 항목 입니다.</p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- ### //체험이벤트 상품 등록/목록 ### -->
	
	<!-- ### //수동이벤트 경품 등록/목록 ### -->
	<div ng-if="spsEvent.eventTypeCd == 'EVENT_TYPE_CD.MANUAL'">
		<table class="tb_type1">
			<colgroup>
				<col width="13%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>
						<spring:message code="c.sps.event.gift.title" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td>
						<div class="btn_alignR marginT1">
							<button type="button" class="btn_type1" ng-click="ctrl.addEventGift()">
								<b><spring:message code="c.sps.event.gift.add.btn" /></b>
							</button>
							<button type="button" class="btn_type1" ng-click="ctrl.deleteGridRows('gift')">
								<b><spring:message code="c.common.delete" /></b>
							</button>
						</div>
		
						<div class="box_type1 marginT1">
							<h3 class="sub_title2">
								<spring:message code="c.sps.event.gift.list.title" />
								<span><spring:message code="c.search.totalCount" arguments="{{ grid_eventGift.totalItems }}" /></span>
							</h3>
							
							<div class="tb_util tb_util_rePosition">
								<button type="button" class="btn_tb_util tb_util2" ng-click="eventGift_grid.exportExcel()">엑셀받기</button>
							</div>
							
							<div class="gridbox gridbox300">
								<div class="grid" ui-grid="grid_eventGift"
									ui-grid-move-columns 
									ui-grid-resize-columns
									ui-grid-auto-resize
									ui-grid-pagination 
									ui-grid-selection 
									ui-grid-row-edit
									ui-grid-cell-nav
									ui-grid-exporter
									ui-grid-edit 
									ui-grid-validate></div>
							</div>
							<p class="information" ng-show="grid_eventGift.data.length==0">필수 입력 항목 입니다.</p>
						</div>		
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- ### //수동이벤트 경품 등록/목록 ### -->
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>	
		<button type="submit" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveNewEvent()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</form>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>