<%--
	화면명 : 문의관리 > 문의상세, 답변등록 화면
	작성자 : emily
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.inquiry.list.js"></script>	

<div class="wrap_popup" ng-app="inquiryManagerApp" data-ng-controller="inquiryDetailController as ctrl" data-ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1">{{ !ccsInquiry.inquiryNo ? '문의 등록' : '문의 상세 및 답변'}}</h2>
		
		<div class="box_type1">
			<h3 class="sub_title2">문의 내용</h3>
	
			<table class="tb_type1">
				<colgroup>
					<col width="8%" />
					<col width="15%" />
					<col width="8%" />
					<col width="15%" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.ccs.qna.qnaNo"/><!-- 문의번호 --></th>
						<td>
							<div data-ng-show="type =='D'">{{ ccsInquiry.inquiryNo }}</div>
						</td>
						<th><spring:message code="c.ccs.qna.channel"/><!-- 문의채널--><i><spring:message code="c.input.required"/><!-- 필수입력 --></i></th>
						<td ng-if="ccsInquiry.inquiryNo">
							<radio-list data-ng-model="ccsInquiry.inquiryChannelCd" code-group="INQUIRY_CHANNEL_CD"></radio-list>
						</td>
						<td ng-if="!ccsInquiry.inquiryNo">CALL</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.qna.qnaType"/><!-- 문의유형--><i><spring:message code="c.input.required"/><!-- 필수입력 --></i></th>
						<td>
							<select data-ng-model="ccsInquiry.inquiryTypeCd" select-code="INQUIRY_TYPE_CD"  v-key="required" style="width:150px;">
								<option value="">선택하세요</option>
								<option data-ng-selected="ccsInquiry.inquiryTypeCd" value="{{ ccsInquiry.inquiryTypeCd }}"></option>
							</select>
						</td>
						<th>문의고객<i><spring:message code="c.input.required"/></i></th>
						<td ng-if="!poBusinessId"><%--BO일경우 --%>
							<div data-ng-init="ccsInquiry.memberYn='Y'">
								<input type="radio" data-ng-model="ccsInquiry.memberYn" value="Y" ng-change="ctrl.eraser('mmsMember')" v-key="required"/>
								<label>회원</label>
								<input type="radio" data-ng-model="ccsInquiry.memberYn" value="N" ng-change="ctrl.eraser('mmsMember')" v-key="required"/>
								<label>비회원</label>
							</div>
							<span ng-if="ccsInquiry.memberYn=='Y'">
								<input type="text" data-ng-disabled="true" value="{{ccsInquiry.member}}" style="width:200px"/>
								
								<button type="button" class="btn_type2" data-ng-click="ctrl.searchPopup('member')" data-ng-disabled="ccsInquiry.memberYn == 'N'">
									<b><spring:message code="c.search.btn.search"/></b>
								</button>
								<button type="button" class="btn_eraser" data-ng-click="ctrl.eraser('mmsMember')">지우개</button>
								<p class="information" ng-show="ccsInquiry.memberNo=='' || ccsInquiry.memberNo==null">필수 입력 항목 입니다.</p>
							</span>
							<span ng-if="ccsInquiry.memberYn=='N'">
								<input type="text" data-ng-model="ccsInquiry.customerName"/>
								<p class="information" ng-show="ccsInquiry.customerName==null || ccsInquiry.customerName==''">필수 입력 항목 입니다.</p>
							</span>
						</td>
						<td ng-if="poBusinessId"><%--PO일경우 --%>
						{{ccsInquiry.member}}
						</td>						
					</tr>
					<tr>
						<th>SMS 수신여부<i><spring:message code="c.input.required"/></i></th>
						<td data-ng-init="ccsInquiry.smsYn='Y'">
							<input type="radio" data-ng-model="ccsInquiry.smsYn" value="Y"/>
							<label>수신</label>
							<input type="radio" data-ng-model="ccsInquiry.smsYn" value="N"/>
							<label>미수신</label>
						</td>
						<th>E-mail 수신여부<i><spring:message code="c.input.required"/></i></th>
						<td data-ng-init="ccsInquiry.emailYn='Y'">
							<input type="radio" data-ng-model="ccsInquiry.emailYn" value="Y"/>
							<label>수신</label>
							<input type="radio" data-ng-model="ccsInquiry.emailYn" value="N"/>
							<label>미수신</label>
						</td>						
					</tr>
					<tr>
						<th>연락처<i><spring:message code="c.input.required"/></i></th>
						<td colspan="3" ng-if="ccsInquiry.smsYn=='Y'" >
							<input type="text" ng-if="ccsInquiry.smsYn=='Y'" style="width: 40%;" tel-input data-ng-model="ccsInquiry.phone" v-key="required"/>
						</td>
						<td colspan="3" ng-if="ccsInquiry.smsYn=='N'" >
							<input type="text" ng-if="ccsInquiry.smsYn=='N'" style="width: 40%;" tel-input data-ng-model="ccsInquiry.phone"/>
						</td>
					</tr>
					<tr>
						<th>E-mail<i><spring:message code="c.input.required"/></i></th>
						<td colspan="3">
							<input type="text" style="width: 40%;"  data-ng-model="ccsInquiry.email" v-key="ccsInquiry.email"/>
						</td>
					</tr>
					<tr ng-if="!poBusinessId">
						<th>담당업체<%-- <spring:message code="c.ccs.user.business"/> --%></th> <!-- 업체ID-->
						<td colspan="3">
							<input type="text" data-ng-model="ccsInquiry.ccsBusiness.name" style="width:34%;" ng-disabled="true"/>
							<button type="button" class="btn_type2" data-ng-click="ctrl.searchPopup('business')">
								<b>검색</b>
							</button>
							<button type="button" class="btn_eraser" data-ng-click="ctrl.eraser('business')">지우개</button>
						</td>
					</tr>
					<tr>
						<th>주문상품명</th>
						<td>
							<input type="text" data-ng-model="ccsInquiry.omsOrderproduct.productName" style="width: 50%;" ng-disabled="true"/>
							<button type="button"  class="btn_type2" data-ng-click="ctrl.searchPopup('order')">
								<b>검색</b>
							</button>
							<button type="button" class="btn_eraser" data-ng-click="ctrl.eraser('order')">지우개</button>
						</td>
						<th>주문상품 정보</th>
						<td ng-if="ccsInquiry.orderId">
							<div data-ng-hide="ccsInquiry.orderId == null">주문번호 :<a style="text-decoration: underline" href="javascript:void(0);" data-ng-click="ctrl.linkOrderFunction()">{{ ccsInquiry.orderId }}</a></div>
							<div data-ng-hide="ccsInquiry.productId == null">상품번호 :<a style="text-decoration: underline" href="javascript:void(0);" data-ng-click="ctrl.linkProductFunction()">{{ ccsInquiry.productId }}</a></div>
							<!-- <div data-ng-hide="ccsInquiry.orderId == null">주문번호 :{{ccsInquiry.orderId}}</div>
							<div data-ng-hide="ccsInquiry.productId == null">상품번호 :{{ccsInquiry.productId}}</div> -->
							<div data-ng-hide="ccsInquiry.saleproductId == null">단품번호 :{{ccsInquiry.saleproductId}} / {{ccsInquiry.omsOrderproduct.saleproductName}}</div>
						</td>
						<td ng-if="!ccsInquiry.orderId"></td>
					</tr>
					<tr data-ng-show ="type == 'D'">
						<th>문의상태</th>
						<td>{{ccsInquiry.inquiryStateName}}
							<!-- <select ng-model="ccsInquiry.inquiryStateCd" select-code="INQUIRY_STATE_CD" style="width:150px;"  v-key="required" data-ng-disabled="type =='D' && ccsInquiry.answerConfirmYn == 'Y'">
								<option value="">선택하세요</option>
								<option ng-selected="ccsInquiry.inquiryStateCd" value="{{ ccsInquiry.inquiryStateCd }}"></option>
							</select> -->
						</td>
						<th>경과시간</th>
						<td>{{ccsInquiry.passTime}}</td>
					</tr>
					<tr data-ng-show ="type == 'D'">
						<th>문의 등록자</th>
						<td>{{ ccsInquiry.creator == '' ? "" : ccsInquiry.creator}}</td>
						<th>문의 등록일시</th>
						<td>{{ ccsInquiry.insDt}}</td>
					</tr>
					<tr data-ng-show ="type == 'D'">
						<th>문의 확인자</th>
						<td>{{ccsInquiry.confirmer}}</td>
						<th>문의 확인일시</th>
						<td>{{ ccsInquiry.confirmDt}}</td>
					</tr>
					<tr>
						<th>문의한 브랜드</th>
						<td colspan="3"><select-list ng-model="ccsInquiry.brandId" code-group="BRAND_CD" all="true"></select-list></td>
					</tr>					
					<tr>
						<th><spring:message code="c.ccs.qna.title"/><!-- 문의제목 --><i><spring:message code="c.input.required"/></i></th>
						<td colspan="3"><input type="text" style="width: 100%;" data-ng-model="ccsInquiry.title" v-key="ccsInquiry.title"/></td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.qna.contInfo"/><!-- 문의내용 --><i><spring:message code="c.input.required"/></i></th>
						<td colspan="3" >
							<textarea cols="20" rows="7" data-ng-model="ccsInquiry.detail" v-key="ccsInquiry.detail"></textarea>
						</td>
					</tr>
					<tr ng-if="ccsInquiry.inquiryNo!=null && ccsInquiry.inquiryNo!=''">
						<th>첨부이미지</th>
						<td colspan="3" style="height:100px;" >
							<img ng-src="ccsInquiry.img" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt=""  style=" max-width: 100%;"> 
						</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="btn_alignC">
			<button type="button" data-ng-click="ctrl.save()" class="btn_type3 btn_type3_purple" ng-hide="ccsInquiry.answerConfirmYn == 'Y'">
				<b>문의 저장</b>
			</button>
			<button type="button" data-ng-click="ctrl.saveConfirm()" class="btn_type3 btn_type3_purple"
				 ng-if="ccsInquiry.inquiryNo && !ccsInquiry.confirmId && ccsInquiry.inquiryStateCd!='INQUIRY_STATE_CD.COMPLETE'">
				<b>문의 확인 {{ ccsInquiry.confirmId}}</b>
			</button>
		</div>
	</form>	
	
	<form name="form3" >
		<div class="box_type1 marginT2" ng-if="ccsInquiry.inquiryNo">
			<h3 class="sub_title2"> <spring:message code="ccsInquiry.answer"/></h3>
	
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="15%" />
					<col width="9%" />
					<col width="15%" />
				</colgroup>
				<tbody>
					<tr>
						<th>답변등록자</th>
						<td>{{ ccsInquiry.answerer }}</td>
						<th>답변등록일시</th>
						<td>{{ ccsInquiry.answerDt }}</td>
					</tr>
					<tr>
						<th>내용<i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<textarea cols="20" rows="10" v-key="ccsInquiry.answer" v-key="required" ng-model="ccsInquiry.answer" ckeditor="ckOption" >
								{{ ccsInquiry.answer }}
							</textarea>
							<!-- <p class="information" ng-if="!ccsInquiry.answer">필수 입력 항목 입니다.</p> -->
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
		<div class="btn_alignC marginT3" ng-if="ccsInquiry.inquiryNo && ccsInquiry.inquiryStateCd!='INQUIRY_STATE_CD.COMPLETE'">
			<button type="button" data-ng-click="ctrl.saveAnswer()" class="btn_type3 btn_type3_purple" ng-hide="ccsInquiry.answerConfirmYn == 'Y'">
				<b>답변 완료</b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>