<%--
	화면명 : 문의관리 > 문의검색목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.inquiry.list.js"></script>

<article class="con_box con_on" ng-app="inquiryManagerApp" ng-controller="inquiryManagerController as ctrl">

	<form name="form2">
		<h2 class="sub_title1">
			<spring:message code="c.ccs.qna.mng">
				<!--문의 관리 -->
			</spring:message>
		</h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="34%" />
					<col width="18%" />
					<col width="*" />
				</colgroup>
				<tbody ng-if="!poBusinessId"><%--BO일경우 --%>
					<tr>
						<th><spring:message code="c.ccs.qna.regDt"></spring:message></th>
						<!--문의 기간 -->
						<td colspan="4">
							<select ng-model="search.periodType" ng-init="search.periodType = 'QNA_DATE'">
								<option ng-repeat="info in periodType" value="{{info.val}}">{{info.text}}</option>
							</select> 
							<input type="text" style="width: 120px;" ng-model="search.startDate" datetime-picker period-start date-only /> 
							~ <input type="text" style="width: 120px;" ng-model="search.endDate" datetime-picker period-end date-only />
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" calendar-button="B" date-only init-button="0"/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.inquiry.data" /></th><!-- 제목/내용 -->
						<td>
							<input type="text" ng-model="search.keyword" style="width:80%"/>
						</td>		
						<th>업체코드 / 명</th>
						<td>
							<input type="text" ng-model="search.businessId" style="width:20%;" />
							<input type="text" ng-model="search.businessName" style="width:20%;" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchBusinessPopup()">
								<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('Business')">지우개</button>
						</td>									
					</tr>
					<%-- <tr>
						<th><spring:message code="ccsQna.displayYn"></spring:message> </th><!-- 전시여부  -->
						<td>
							<radio-yn ng-model="search.displayYn" labels='<spring:message code="c.input.radio.displayY" />,<spring:message code="c.input.radio.displayN" />' init-val="Y"></radio-yn>
						</td> 
						
						<th><spring:message code="c.ccs.qna.siteTypeCd"></spring:message></th><!-- 사이트유형 -->
						<td>
							<selectlist ng-model="search.siteType"  codegroup="SITE_TYPE_CD" all="true" val></selectlist>
						</td>
					</tr> --%>
					<tr>
						<th><spring:message code="c.ccs.inquiry.memgrade" /></th>
						<%--회원등급 --%>
						<td>
							<checkbox-list ng-model="search.memGrade" code-group="MEM_GRADE_CD"  all-check></checkbox-list>
						</td>	
						<th><spring:message code="c.ccs.inquiry.permit.custom"/><%--문의고객 --%>
							(<radio-yn ng-model="search.memberYn" labels='회원,비회원' init-val="Y"></radio-yn>)
						</th>
						<td ng-if="search.memberYn=='Y'"><%--문의고객 : 회원 --%>
							<input type="text" ng-change="changeMemberLoginId()" ng-model="search.memberLoginId" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchMemberPopup('member')">
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('member')">지우개</button>
						</td>		
						<td ng-if="search.memberYn=='N'"><%--문의고객 : 비회원 --%>
							<input type="text" ng-model="search.customerName" />
						</td>															
					</tr>
					<tr>
						<th><spring:message code="c.ccs.qna.channel"/></th>
						<!-- 문의채널 -->
						<td><checkbox-list ng-model="search.inquiryChannel" code-group="INQUIRY_CHANNEL_CD"  all-check ></checkbox-list>
						</td>
						<th><spring:message code="c.ccs.inquiry.ins.custom" /></th><!-- 문의등록자(CS등록자) -->
						<td>
							<input type="text" ng-change="changeCsUserName()" ng-model="search.csUserName" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchUserPopup('callback_ins')">
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('insInfo')">지우개</button>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.qna.state" /> <!--문의상태 --></th>
						<td><checkbox-list ng-model="search.inquiryState" code-group=INQUIRY_STATE_CD  all-check ></checkbox-list>
							<%-- <input type="checkbox" name="search.answerYn" id="search.answerYn" value="ALL"/><spring:message code="c.select.all"></spring:message><!-- 전체 -->
							<input type="checkbox" name="search.answerYn" id="search.answerYn" value="Y"/><spring:message code="c.ccs.qna.answerYes"></spring:message><!-- 답변 -->
							<input type="checkbox" name="search.answerYn" id="search.answerYn" value="N"/><spring:message code="c.ccs.qna.answerNo"></spring:message><!-- 미답변 --> --%>

						</td>
						<th><spring:message code="c.ccs.inquiry.answerid" /></th><!-- 답변등록자 -->
						<td>
							<input type="text" ng-change="changeAnswererName()" ng-model="search.answererName" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchUserPopup('callback_answer')">
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('answerInfo')">지우개</button>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.qna.qnaType" /> <!-- 문의유형 --></th>
						<td><select-list ng-model="search.inquiryTypeCd" code-group="INQUIRY_TYPE_CD" all="true" val></select-list></td>
						<th>문의한 브랜드<!-- 브랜드 --></th>
						<td><select-list ng-model="search.brandId" code-group="BRAND_CD" all="true" val></select-list></td>						
					</tr>
					<%-- <tr>
						<th><spring:message code="c.ccs.qna.searchType" /> <!-- 검색기준--></th>
						<td colspan="3">
							<select ng-model="search.serchType" ng-init="search.serchType = 'QNA_NAME'">
									<option ng-repeat="info in serchType" value="{{info.val}}">{{info.text}}</option>
							</select> <input type="tel" id="" value="" placeholder="" style="width: 40%" ng-model="search.searchKeyword" />
						</td>
					</tr> --%>
				</tbody>
				<tbody ng-if="poBusinessId"><%-- PO일경우 --%>
					<tr>
						<th><spring:message code="c.ccs.qna.regDt"></spring:message></th>
						<!--문의 기간 -->
						<td colspan="4">
							<select ng-model="search.periodType" ng-init="search.periodType = 'QNA_DATE'">
								<option ng-repeat="info in periodType" value="{{info.val}}">{{info.text}}</option>
							</select> 
							<input type="text" style="width: 120px;" ng-model="search.startDate" datetime-picker period-start date-only /> 
							~ <input type="text" style="width: 120px;" ng-model="search.endDate" datetime-picker period-end date-only />
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" calendar-button="B" init-button="0"/>
						</td>
					</tr>				
					<tr>
						<th><spring:message code="c.ccs.qna.channel"/></th>
						<!-- 문의채널 -->
						<td><checkbox-list ng-model="search.inquiryChannel" code-group="INQUIRY_CHANNEL_CD"  all-check ></checkbox-list>
						</td>
						<th><spring:message code="c.ccs.qna.state" /> <!--문의상태 --></th>
						<td><checkbox-list ng-model="search.inquiryState" code-group="INQUIRY_STATE_CD"  all-check ></checkbox-list>
						</td>						
					</tr>				
					<tr>
						<th><spring:message code="c.ccs.inquiry.data" /></th><!-- 제목/내용 -->
						<td>
							<input type="text" ng-model="search.keyword" style="width:80%"/>
						</td>		
						<th><spring:message code="c.ccs.qna.qnaType" /> <!-- 문의유형 --></th>
						<td><select-list ng-model="search.inquiryTypeCd" code-group="INQUIRY_TYPE_CD" all="true" val></select-list></td>								
					</tr>
				</tbody>				
			</table>
		</div>

		<div class="btn_alignR">

			<button type="button" ng-click="ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" ng-click="searchGrid()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>


		<div class="btn_alignR  marginT3">
			<button type="button" class="btn_type1" ng-click="popup.insert();">
				<b><spring:message code="c.ccs.inquiry.insert" /></b><!-- 문의 등록 -->
			</button>
		</div>
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.ccs.qna.qnaList" />
				<!-- 문의정보 내역 -->
				<!-- <span id="totalLen">(총 <b>{{qnaListSize}}</b>건)</span> -->
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_inquiry.totalItems }}" /></span>
			</h3>

			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
				<!-- 			<button type="button" class="btn_tb_util tb_util1">되돌리기</button> -->
				<!-- 			<button type="button" class="btn_tb_util tb_util2">엑셀받기</button> -->
				<!-- 			<button type="button" class="btn_tb_util tb_util3">셀잠그기</button> -->
				<!-- 			<button type="button" class="btn_tb_util tb_util4">전체체크</button> -->

				<!-- 			<select style="width:105px;"> -->
				<!-- 				<option value="">200건</option> -->
				<!-- 			</select> -->

				<!-- 			<span class="page"> -->
				<!-- 				<button type="button" class="btn_prev">이전</button> -->
				<!-- 				<input type="text" value="1" placeholder="" /> -->
				<!-- 				<u>/</u><i>24</i> -->
				<!-- 				<button type="button" class="btn_next">다음</button> -->
				<!-- 			</span> -->

				<!-- 			<button type="button" class="btn_type2"> -->
				<!-- 				<b>이동</b> -->
				<!-- 			</button> -->
			</div>

			<div class="gridbox gridbox500">
				<div class="grid" data-ui-grid="grid_inquiry"
					data-ui-grid-move-columns data-ui-grid-resize-columns
					data-ui-grid-pagination data-ui-grid-auto-resize
					data-ui-grid-exporter data-ui-grid-edit data-ui-grid-selection
					data-ui-grid-validate></div>
			</div>
			<!-- <div class="tb_bar">
				<button type="button" class="btn_grid_more" onclick="javascript:moreGrid('grid')">더보기</button>
			</div> -->
		</div>

		<!-- <div class="btn_alignR">
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="memCtrl.move('/mms/qna/insert')">
				<b>문의등록</b>
			</button>
		</div> -->
	</form>

</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />

