<%--
	화면명 : 회원 관리 > 회원 상세 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<!-- <script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script> -->
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<div class="wrap_popup"  ng-app="ccsAppPopup" ng-controller="mms_memberDetailPopApp_controller as ctrl" data-ng-init="ctrl.init()">
		<jsp:include page="/WEB-INF/views/mms/member/popup/memberTab.jsp" flush="true"/>
		
		<!-- 회원기본정보:Start -->
		<h1 class="marginT2">회원기본 정보<!-- 회원기본 정보 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
					<colgroup>
						<col width="15%" />
						<col width="20%" />
						<col width="15%" />
						<col width="20%" />
						<col width="15%" />
						<col width="*" />
					</colgroup>
				<tbody>
					<tr>
						<th>
							회원번호<!-- 회원번호 -->
						</th>
						<td>
							{{mmsMemberZts.memberNo}}
						</td>
						<th>
							회원ID<!-- 회원ID -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.memberId}}
						</td>
						<th>
							회원명<!-- 회원명 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.memberName}}
						</td>
					</tr>
					
					<tr>
						<th>
							회원상태<!-- 회원번호 -->
						</th>
						<td>
							<div ng-show="mmsMemberZts.mmsMember.memberStateCd == 'MEMBER_STATE_CD.NORMAL'">일반</div>
							<div ng-show="mmsMemberZts.mmsMember.memberStateCd == 'MEMBER_STATE_CD.SLEEP'">휴면</div>
							<div ng-show="mmsMemberZts.mmsMember.memberStateCd == 'MEMBER_STATE_CD.WITHDRAW'">탈퇴</div>
						</td>
						<th>
							간편 가입 SNS ID<!-- 간편 가입 SNS ID -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.snsId}} <div ng-show="mmsMemberZts.mmsMember.snsChannelCd != '' && mmsMemberZts.mmsMember.snsChannelCd != null">({{mmsMemberZts.mmsMember.snsChannelCd}})</div>
						</td>
						<th>
							내 외국인 구분<!-- 내 외국인 구분 -->
						</th>
						<td>
							<div ng-show="mmsMemberZts.mmsMember.foreignerYn == 'Y'">외국인</div>
							<div ng-show="mmsMemberZts.mmsMember.foreignerYn == 'N'">내국인</div>
						</td>
					</tr>
					
					<tr>
						<th>
							성별<!-- 성별 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.genderName}}
						</td>
						<th>
							생년월일<!-- 생년월일 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.birthday}}
							<span ng-show="mmsMemberZts.mmsMember.lunarYn == 'N'">(양력)</span>
							<span ng-show="mmsMemberZts.mmsMember.lunarYn == 'Y'">(음력)</span>
						</td>
						<th>
							E-Mail<!-- E-Mail -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.email}}
						</td>
						
					</tr>
					
					<tr>
						<th>
							휴대폰 번호<!-- 휴대폰 번호 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.phone2}}
						</td>
						<th>
							제휴카드 등록정보<!-- 제휴카드 등록정보 -->
						</th>
						<td colspan="3">
							
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 회원기본정보:End -->
		
		<!-- 회원등급/유형정보:Start -->
		<h1 class="marginT2">회원 등급/유형 정보<!-- 회원 등급/유형 정보 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							회원등급<!-- 회원등급 -->
						</th>
						<td colspan="5">
							{{mmsMemberZts.memGradeName}} 
							<a href="#" ng-click="ctrl.gradeHistory()">(등급변경 이력보기)</a>
						</td>
					</tr>
					
					<tr>
						<th>
							임직원 여부<!-- 임직원 여부 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.employeeYn}} 
						</td>
						<th>
							멤버십 여부<!-- 멤버십 여부 -->
						</th>
						<td>
							{{mmsMemberZts.membershipYn}} 
							<span ng-show="mmsMemberZts.membershipRegDt != '' && mmsMemberZts.membershipRegDt != null">
								({{mmsMemberZts.membershipRegDt}} 인증)
							</span>
						</td>
						<th>
							프리미엄 여부<!--프리미엄 여부 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.premiumYn}} <span ng-show="mmsMemberZts.mmsMember.premiumRegDt != '' && mmsMemberZts.mmsMemberpremiumRegDt != null">({{mmsMemberZts.mmsMember.premiumRegDt}} 등록)</span>
						</td>
					</tr>
					
					<tr>
						<th>
							B2E 여부<!-- B2E 여부 -->
						</th>
						<td>
							{{mmsMemberZts.b2eYn}} <span ng-show="mmsMemberZts.b2eRegDt != '' && mmsMemberZts.b2eRegDt != null">({{mmsMemberZts.b2eRegDt}} 등록)</span>
							<br><span ng-show="mmsMemberZts.regNo != '' && mmsMemberZts.regNo != null">사업자 번호 : {{mmsMemberZts.regNo}}</span>
						</td>
						<th>
							다자녀 여부<!--다자녀 여부 -->
						</th>
						<td colspan="3">
							{{mmsMemberZts.childrenYn}} <span ng-show="mmsMemberZts.childrenRegDt != '' && mmsMemberZts.childrenRegDt != null">({{mmsMemberZts.childrenRegDt}} 인증)</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 회원등급/유형정보:End -->
		
		<!-- 마케팅수신여부:Start -->
		<h1 class="marginT2">마케팅수신여부<!-- 마케팅수신여부 --><!--  <a href="#">(마케팅수신변경이력보기)</a>--></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							SNS 수신 여부<!-- SNS 수신 여부 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.smsYn}} &nbsp;
							<button type="button" class="btn_type2" ng-click="ctrl.chgSmsReceive();">
								<b>변경</b>
							</button>
						</td>
						<th>
							EMAIL 수신 여부<!-- EMAIL 수신 여부 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.emailYn}} &nbsp;
							<button type="button" class="btn_type2" ng-click="ctrl.chgEmailReceive();">
								<b>변경</b>
							</button>
						</td>
						<th>
							APP Push 수신 여부<!--APP Push 수신 여부 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.appPushYn}} &nbsp;
							<button type="button" class="btn_type2" ng-click="ctrl.chgAppPushReceive();">
								<b>변경</b>
							</button>
						</td>
					</tr>
					
					<tr>
						<th>위치정보동의여부</th>
						<td colspan="5">Y (필드 없음 - 하드코딩)</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 마케팅수신여부:End -->
		
		<!-- 로그인 / 가입 정보:Start -->
		<h1 class="marginT2">로그인 / 가입 정보<!-- 로그인 / 가입 정보 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							통합 멤버십 가입일시<!-- 통합 멤버십 가입일시 -->
						</th>
						<td>
							{{mmsMemberZts.mmsMember.regDt}}
						</td>
						<th>
							ZTS 가입일시<!-- ZTS 가입일시 -->
						</th>
						<td colspan="3">
							{{mmsMemberZts.insDt}}
						</td>
					</tr>
					<tr>
						<th>
							PC 최종 로그인 일시
						</th>
						<td>
							{{mmsMemberZts.latestPcLoginDt}}
						</td>
						<th>
							Mobile Web<br> 최종로그인 일시
						</th>
						<td>
							{{mmsMemberZts.latestMwLoginDt}}
						</td>
						<th>
							Mobile APP<br> 최종로그인 일시
						</th>
						<td>
							{{mmsMemberZts.latestAppLoginDt}}
						</td>
					
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 로그인 / 가입 정보:End -->
		
		<!-- 배송지 정보:Start -->
		<h1 style="margin-top:20px;">배송지 정보</h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr ng-repeat="mmsAddress in mmsMemberZts.mmsAddresss">
						<th ng-show="mmsAddress.addressNo == mmsMemberZts.addressNo">
							기본 배송지정보
						</th>
						<td ng-show="mmsAddress.addressNo == mmsMemberZts.addressNo">
							({{mmsAddress.zipCd}}) {{mmsAddress.address1}} {{mmsAddress.address2}} 
						</td>
					</tr>
					<tr ng-repeat="mmsAddress in mmsMemberZts.mmsAddresss">
						<th ng-if="mmsAddress.addressNo != mmsMemberZts.addressNo">
							배송지정보{{ $index}}
						</th>
						<td ng-if="mmsAddress.addressNo != mmsMemberZts.addressNo">
							({{mmsAddress.zipCd}}) {{mmsAddress.address1}} {{mmsAddress.address2}} 
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 배송지 정보:End -->
		
		<!-- 환불계좌:Start -->
		<h1 class="marginT2">환불계좌<!-- 환불계좌 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							은행명<!-- 은행명 -->
						</th>
						<td>
							{{mmsMemberZts.bankName}}
						</td>
						<th>
							계좌번호<!-- 계좌번호 -->
						</th>
						<td>
							{{mmsMemberZts.accountNo}}
						</td>
						<th>
							예금주<!-- 예금주 -->
						</th>
						<td>
							{{mmsMemberZts.accountHolderName}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 환불계좌:End -->
		
		<!-- 통합멤버십 자녀정보:Start -->
		<h1 class="marginT2">통합멤버십 자녀정보 (매일멤버십 연동 - 미구현)<!-- ZTS 자녀정보 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							수유형태<!-- 자녀유무 -->
						</th>
						<td colspan="5">
							Y
						</td>
					</tr>
				</tbody>
			</table>
			<table class="tb_type1 box_type1" style="margin-top:5px;">
				<colgroup>
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							자녀순서<!-- 자녀순서 -->
						</th>
						<th>
							쌍둥이 여부<!-- 쌍둥이 여부 -->
						</th>
						<th>
							이름<!-- 이름 -->
						</th>
						<th>
							생일 (음/양)<!-- 생일 (음/양) -->
						</th>
						<th>
							성별<!-- 성별 -->
						</th>
						<th>
							기본아이구분<!-- 기본아이구분 -->
						</th>
					</tr>
				</tbody>
			</table>
			
		</div>
		<!-- 자녀정보:End -->
		
		<!-- ZTS 자녀정보:Start -->
		<h1 class="marginT2">ZTS 자녀정보<!-- ZTS 자녀정보 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="20%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							자녀유무<!-- 자녀유무 -->
						</th>
						<td colspan="3">
							{{mmsMemberZts.babyYnCd}}
						</td>
					</tr>
					<tr>
						<th>
							성별
						</th>
						<td>
							{{mmsMemberZts.babyGenderCd}}
						</td>
						<th>
							아이생일
						</th>
						<td>
							{{mmsMemberZts.babyBirthday}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 자녀정보:End -->
		
		<!-- 관심 정보:Start -->
		<h1 class="marginT2">관심 정보<!-- 관심 정보 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							0to7.com 방문동기

						</th>
						<td>
							{{interestAge}}
						</td>
					</tr>
					<tr>
						<th>
							관심 카테고리
						</th>
						<td>
							{{interestCategory}}
						</td>
					</tr>
					
					<tr>
						<th>
							쇼핑스타일및 관심정보
						</th>
						<td>
							{{interestProduct}}
						</td>
					</tr>
					<tr>
						<th>
							0to7 선호 브랜드
						</th>
						<td>
							{{interestBrand}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 관심 정보:End -->
		
		<!-- 관심 매장:Start -->
		<h1 class="marginT2">관심 매장<!-- 관심 매장 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="5%" />
					<col width="10%" />
					<col width="15%" />
					<col width="15%" />
					<col width="30%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							순서
						</th>
						<th>
							브랜드
						</th>
						<th>
							지점
						</th>
						<th>
							전화번호
						</th>
						<th>
							주소
						</th>
						<th>
							판매 브랜드
						</th>
					</tr>
					<tr ng-repeat="interest in interestOffshop">
						<td>
							{{$index + 1}} 
						</td>
						<td>
							{{interest.ccsOffshop.ccsOffshopbrands[0].name}}
						</td>
						<td>
							{{interest.ccsOffshop.name}}
						</td>
						<td>
							{{interest.ccsOffshop.managerPhone}} 
						</td>
						<td>
							{{interest.ccsOffshop.address1}} 
						</td>
						<td>
							<span ng-repeat="brand in interest.ccsOffshop.ccsOffshopbrands">
								{{brand.name}} 
								<span ng-if="!$last">/</span>
							</span>
						</td>
					</tr>
										
				</tbody>
			</table>
		</div>
		<!-- 관심 매장:End -->
		
		<h1 class="marginT2">혜택 정보<!-- 혜택 정보 --></h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							사용가능 매일포인트<!-- 사용가능 매일포인트 -->
						</th>
						<td>
							{{mmsMemberZts.point}}
						</td>
					</tr>
					
					<tr>
						<th>
							사용가능 당근<!-- 사용가능 당근 -->
						</th>
						<td>
							{{mmsMemberZts.carrotBalanceAmt != null ?  mmsMemberZts.carrotBalanceAmt : 0}}
						</td>
					</tr>
					
					<tr>
						<th>
							사용가능 쿠폰<!-- 사용가능 쿠폰 -->
						</th>
						<td>
							{{mmsMemberZts.cpnIssueCnt != null ?  mmsMemberZts.cpnIssueCnt : 0}}
						</td>												   
					</tr>
					<tr>
						<th>
							사용가능 예치금<!-- 사용가능 예치금 -->
						</th>
						<td>
							{{mmsMemberZts.depositBalanceAmt != null ?  mmsMemberZts.depositBalanceAmt : 0}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
		</button>
	</div>
</div>


<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>