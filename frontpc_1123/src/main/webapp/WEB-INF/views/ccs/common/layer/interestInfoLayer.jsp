<%--
	화면명 : 메인&멤버십관 > 관심정보 레이어
	작성자 : stella
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#productTypeArea label").on("click", "em", function(e) {
		if ($(this).hasClass("selected")) {
			$(this).removeClass("selected");
			e.preventDefault();
		} else {
			$(this).addClass("selected");
			e.preventDefault();
		}
	});
	
	$("#brand_Area").on("click", "em", function() {
		if ($(this).hasClass("selected")) {
			$("#brand_ul").parent().show();
		} else {
			$("#brand_ul").parent().hide();
		}
	});
	
	$("#pushCheckbox").on("change", function(e) {
		if ($("#selectOffshopArea").find(".select_box1").is(".sel_disabled")) {
			$("#pushCheckbox").parent().addClass("selected");
			
			$("#selectOffshopArea").find(".select_box1").removeClass("sel_disabled");
			$("#selectOffshopArea").find(".select_box1 select").attr("disabled", false);	
		} else {
			$("#pushCheckbox").parent().removeClass("selected");
			
			$("#selectOffshopArea").find(".select_box1").addClass("sel_disabled");
			$("#selectOffshopArea").find(".select_box1 select").attr("disabled", true);
		}
		e.preventDefault();
	});
});
</script>

<div>
	<form name="indexForm" id="indexForm">
		<input type="hidden" name="hid_startIndex" id="hid_startIndex" value="" />
		<input type="hidden" name="hid_lastIndex" id="hid_lastIndex" value="" />
	</form>
</div>

<div id="interestInfoLayer">
	<!-- 관심정보설정_(01시작popup) -->
	<div class="pop_set_wrap step01" style="display:none">
		<div class="pop_set_cnt_wrap">
			<p class="title">
				<c:if test="${isMobile ne 'true'}">
					<img src="/resources/img/pc/txt/txt_info_set_01.png" class="pc_only" alt="우리아이 정보 설정하시고 혜택과 맞춤상품 추천 받으세요!">
				</c:if>
				<c:if test="${isMobile eq 'true'}">
					<img src="/resources/img/mobile/txt/txt_info_set_01.png" class="mo_only" style="width:100%;" alt="우리아이 정보 설정하시고 혜택과 맞춤상품 추천 받으세요!">
				</c:if>
			</p>
			
			<div class="pop_set_info">
				<strong class="tit">우리아이 맞춤혜택</strong>
				<ul class="set_info_list">
					<li>Welcome 5천원 장바구니 쿠폰</li>
					<li>개인맞춤상품 &amp; 이벤트 안내</li>
					<li>0to7 브랜드 행사 안내</li>
					<li>기념일 축하 12% 쿠폰 <br>
							(본인생일, 아이백일/돌/생일쿠폰)</li>
				</ul>
			</div>
		</div>
		<div class="btn_wrapC btn1ea">
			<a href="javascript:ccs.layer.interestInfoLayer.next(1);" class="btn_next"><span>시작</span></a>
		</div>
		<div class="btn_wrap_close">
			<a href="javascript:ccs.layer.close('interestInfoLayer');" class="btn_pop_close">닫기</a>
		</div>
	</div>
	<!-- //관심정보설정_(01시작popup) -->
	
	<!-- 관심정보설정_(02아이정보popup) -->
	<div class="pop_set_wrap step02" style="display:none">
		<div class="pop_set_cnt_wrap">
			<p class="title">
				<c:if test="${isMobile ne 'true'}">
					<img src="/resources/img/pc/txt/txt_info_set_02.png" class="pc_only" alt="아이 성별/생일은 어떻게 되시나요? 맞춤 추천을 받으실 대표아이 정보를 입력해주세요!">
				</c:if>
				<c:if test="${isMobile eq 'true'}">
					<img src="/resources/img/mobile/txt/txt_info_set_02.png" class="mo_only" style="width:100%;" alt="아이 성별/생일은 어떻게 되시나요? 맞춤 추천을 받으실 대표아이 정보를 입력해주세요!">
				</c:if>
			</p>
			<dl class="pop_set_survey">
				<dt>자녀가 있으십니까?</dt>

				<dd id="babyYnArea">
					<label class="radio_style1 option_style1">
						<c:choose>
							<c:when test="${not empty memberInfo && memberInfo.babyYnCd == 'BABY_YN_CD.Y'}">
								<em class="selected">
							</c:when>
							<c:otherwise>
								<em>
							</c:otherwise>
						</c:choose>
							<input type="radio" name="ra1_1" value="Y"/>
						</em>
						<span>예</span>
					</label>
					<label class="radio_style1 option_style1">
						<c:choose>
							<c:when test="${not empty memberInfo && memberInfo.babyYnCd == 'BABY_YN_CD.N'}">
								<em class="selected">
							</c:when>
							<c:otherwise>
								<em>
							</c:otherwise>
						</c:choose>
							<input type="radio" name="ra1_1" value="N" />
						</em>
						<span>아니요</span>
					</label>
					<label class="radio_style1 option_style1">
						<c:choose>
							<c:when test="${not empty memberInfo && memberInfo.babyYnCd == 'BABY_YN_CD.READY'}">
								<em class="selected">
							</c:when>
							<c:otherwise>
								<em>
							</c:otherwise>
						</c:choose>
							<input type="radio" name="ra1_1" value="READY" />
						</em>
						<span>출산예정</span>
					</label>
				</dd>
				<dt>성별</dt>
				<dd id="babyGenderArea">
					<label class="radio_style1 option_style1">
						<c:choose>
							<c:when test="${memberInfo != null && memberInfo.babyGenderCd == 'BABY_GENDER_CD.BOY'}">
								<em class="selected">
							</c:when>
							<c:otherwise>
								<em>
							</c:otherwise>
						</c:choose>
							<input type="radio" name="ra1_2" value="BOY" />
						</em>
						<span>남아</span>
					</label>
					<label class="radio_style1 option_style1">
						<c:choose>
							<c:when test="${memberInfo != null && memberInfo.babyGenderCd == 'BABY_GENDER_CD.GIRL'}">
								<em class="selected">
							</c:when>
							<c:otherwise>
								<em>
							</c:otherwise>
						</c:choose>
							<input type="radio" name="ra1_2" value="GIRL" />
						</em>
						<span>여아</span>
					</label>
					<label class="radio_style1 option_style1">
						<c:choose>
							<c:when test="${memberInfo != null && memberInfo.babyGenderCd == 'BABY_GENDER_CD.UNKNOWN'}">
								<em class="selected">
							</c:when>
							<c:otherwise>
								<em>
							</c:otherwise>
						</c:choose>
							<input type="radio" name="ra1_2" value="UNKNOWN" />
						</em>
						<span>모름</span>
					</label>
				</dd>
				<dt>아이생일 <span>출산예정이신 경우 출산예정일을 입력해 주세요!</span></dt>
				<dd id="babyBirthdayArea">
					<input type="hidden" name="babyBirth" id="babyBirth" value="${memberInfo.babyBirthday}" />
					<div class="select_box1 year_ip">
						<input type="hidden" name="currentYear" id="currentYear" value="${currentYear}" />
						<label>년</label>
						<select id="birthYearSelect">
						</select>
					</div>
					<div class="select_box1">
						<label>월</label>
						<select id="birthMonthSelect">
							<option>월</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
						</select>
					</div>
					<div class="select_box1">
						<label>일</label>
						<select id="birthDaySelect">
							<option>일</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
						</select>
					</div>
				</dd>
			</dl>
		</div>
		<div class="btn_wrapC btn2ea">
			<a href="javascript:ccs.layer.interestInfoLayer.prev(2);" class="btn_prev"><span>이전</span></a><a href="javascript:ccs.layer.interestInfoLayer.next(2);" class="btn_next"><span>다음</span></a>
		</div>
		<div class="btn_wrap_close">
			<a href="javascript:ccs.layer.close('interestInfoLayer');" class="btn_pop_close">닫기</a>
		</div>
	</div>
	<!-- //관심정보설정_(02아이정보popup) -->
	
	<!-- 관심정보설정_(03관심정보popup) -->
	<div class="pop_set_wrap step03" style="display:none">
		
	</div>
	<!-- //관심정보설정_(03관심정보popup) -->
	
	<!-- 관심정보설정_(04알림신청popup) -->
	<div class="pop_set_wrap step04" style="display:none">
		<div class="pop_set_cnt_wrap">
			<p class="title">
				<c:if test="${isMobile ne 'true'}">
					<img src="/resources/img/pc/txt/txt_info_set_04.png" class="pc_only" alt="알림 신청하면 좋아요! 알림 서비스 수신동의 해주시면 맞춤 혜택과 상품정보를 보내드립니다.">
				</c:if>
				<c:if test="${isMobile eq 'true'}">
					<img src="/resources/img/mobile/txt/txt_info_set_04.png" class="mo_only" style="width:100%;" alt="알림 신청하면 좋아요! 알림 서비스 수신동의 해주시면 맞춤 혜택과 상품정보를 보내드립니다.">
				</c:if>
			</p>
			<div class="pop_set_infoWrap">
				<dl class="pop_set_survey" style="${isMobile eq 'true' ? 'min-width:245px; padding:9px 6px 0;' : ''}">
					<dt>쇼핑스타일 및 관심정보</dt>
					<dd class="odd_type2" id="productTypeArea">
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="SALE" />
							</em>
							<span>세일상품</span>
						</label>
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="NEW" />
							</em>
							<span>신상품</span>
						</label>
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="TRENDY" />
							</em>
							<span>트렌디 상품</span>
						</label>
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="WNB" />
							</em>
							<span>화이트/블랙</span>
						</label>
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="COLOR" />
							</em>
							<span>컬러풀</span>
						</label>
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="CHARACTER" />
							</em>
							<span>캐릭터</span>
						</label>
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="PRESENT" />
							</em>
							<span>사은품&amp;혜택</span>
						</label>
						<label class="chk_style1 option_style1">
							<em>
								<input type="checkbox" name="ra1_3" value="EVENT" />
							</em>
							<span>이벤트</span>
						</label>
						<label class="chk_style1 option_style1" id="brand_Area">
							<em>
								<input type="checkbox" name="ra1_3" value="BRAND" />
							</em>
							<span>0to7 브랜드</span>
						</label>
						<div class="set_sort_outer" style="display:none;">
							<ul class="set_sort_list" id="brand_ul">
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>궁중비책</span>
									</label>
								</li>
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>토미티피</span>
									</label>
								</li>
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>알로앤루</span>
									</label>
								</li>
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>알퐁소</span>
									</label>
								</li>
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>포래즈</span>
									</label>
								</li>
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>섀르반</span>
									</label>
								</li>
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>츄즈</span>
									</label>
								</li>
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" value="" />
										</em>
										<span>와이볼루션</span>
									</label>
								</li>
							</ul>
						</div>
					</dd>
				</dl>	
				
				<div class="pop_set_info">
					<strong class="tit">나의 관심매장 설정	</strong>
					<div class="pop_set_chk">

						<label class="chk_style1">
							<em>
								<input type="checkbox" value="" id="pushCheckbox"/>
							</em>
							<span>나의 관심매장 설정</span>
						</label>
					</div>
					<dl class="pop_set_survey" id="selectOffshopArea">
						<dt>해당 매장의 행사를 APP 푸시로 알려드립니다.</dt>
						<dd>
							<div class="select_box1 sel_disabled">
								<label>브랜드</label>
								<select id="selectBrand" onchange="ccs.offshop.changeBrand();">
									<option value="">브랜드</option>
									<c:forEach items="${brandCodeList}" var="brand" varStatus="brand_status">
										<option value="${brand.brandId}">${brand.name}</option>
									</c:forEach>
								</select>
							</div>
						</dd>
						<dd>
							<div class="add_wrap">
								<div class="select_box1 add1 sel_disabled">
									<label>시/도</label>
									<select id="selectArea1" onchange="ccs.offshop.getArea2List();">
										<option>시/도</option>
										<c:forEach items="${area1List}" var="area1" varStatus="status">
											<option>${area1.areaDiv1}</option>
										</c:forEach>
									</select>
								</div>
								<div class="select_box1 add1 sel_disabled">
									<label>시/군/구</label>
									<select id="selectArea2" onchange="ccs.layer.interestInfoLayer.searchOffshop();">
										<option>시/군/구</option>
									</select>
								</div>
								<div class="select_box1 add2 sel_disabled">
									<label>매장</label>
									<select id="selectOffshop">
										<option>매장</option>
									</select>
								</div>
							</div>
						</dd>
					</dl>
					<p class="set_txt_info">저장하신 정보는 마이쇼핑 &gt; 관심정보 수정에서 확인 및 수정 가능합니다.</p>
				</div>
			</div>
		</div>
		<div class="btn_wrapC btn2ea">
			<a href="javascript:ccs.layer.interestInfoLayer.prev(4);" class="btn_prev"><span>이전</span></a><a href="javascript:ccs.layer.interestInfoLayer.setComplete();" class="btn_next"><span>완료</span></a>
		</div>
		<div class="btn_wrap_close">
			<a href="javascript:ccs.layer.close('interestInfoLayer');" class="btn_pop_close">닫기</a>
		</div>
	</div>
	<!-- //관심정보설정_(04알림신청popup) -->
	
	<!-- 관심정보설정_(05새단장기념안내popup) -->
	<div class="pop_set_wrap step05" style="display:none;">
		<div class="pop_set_cnt_wrap">
			<p class="title">
				<span class="pc_only"><img src="/resources/img/pc/txt/txt_info_set_05.png"  alt="새단장 기념! 더 빨라지고 편리해진 0to7.com을 즐겨보세요!"></span>
				<span class="mo_only">
				<img src="/resources/img/mobile/txt/txt_info_set_05.png" alt="회원전용 한정특가 멤버십관을 아시나요?!" style="width:320px;"></span>
			</p>
			<div class="pop_set_info">
				<strong class="tit">Welcome <br>
					장바구니 쿠폰이 발급되었습니다.
				</strong>
				<p class="icon_coupon">
				<span class="pc_only"><img src="/resources/img/pc/txt/info_set05_icon01.png" alt="0TO7 5,000 쿠폰 장바구니"></span>
				<span class="mo_only"><img src="/resources/img/mobile/txt/info_set05_icon01.png" alt="0TO7 5,000 쿠폰 장바구니" style="width:145px;"></span>
				</p>
				<span class="txt">
					5만원 이상 구매 시, 5천원 할인 <br>
					신규APP 설치 회원에게 ID당 1회만 제공됩니다.
				</span>
			</div>
		</div>
		<div class="btn_wrapC btn1ea">
			<a href="javascript:ccs.layer.close('interestInfoLayer');" class="btn_next">쇼핑 시작하기</a>
		</div>
		
		<div class="btn_wrap_close">
			<a href="javascript:ccs.layer.close('interestInfoLayer');" class="btn_pop_close">닫기</a>
		</div>
	</div>
	<!-- //관심정보설정_(05새단장기념안내popup) -->
	
</div>

<style>
	.mobile .pop_set_wrap .btn_next {height:45px; padding:12px 0 0;  border-radius:4px; text-align:center;}
</style>