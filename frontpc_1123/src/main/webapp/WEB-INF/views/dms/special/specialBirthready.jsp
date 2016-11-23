<%--
	화면명 : 프론트 & 모바일  출산준비관
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	pageContext.setAttribute("birthready", Config.getString("corner.special.ready.img.1"));
%>

<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	special.common.category.orderBy.pageInit();
	//$("#miniTabCtg_"+$('#categoryIds').val()).find("a").addClass("on");
	
	/* if(ccs.common.mobilecheck()){
		dms.common.searchMobilePage();
	}  */
	
	if(ccs.common.mobilecheck()){	
		swiperCon('birthdaySwiper_category', '5'); //메인 - 카테고리 탭
	}else{
		swiperCon('birthdaySwiper_category', 400, 6, 0, false, true); //프리미엄 멤버쉽 베너
		
	}
});

</script>

	<c:set var="birthreadyMap"  value="${cornerMap[birthready]}"/>
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="전문관|출산준비관" name="pageNavi"/>
	</jsp:include>		
	
	<div class="inner">
		<div class="special birthready">
			<!-- 16.10.05 : 상단 비주얼 -->
			<c:if test="${not empty birthreadyMap.dmsDisplayitems}">
				<div class="visual one">
						<ul>
							<!-- pc -->
							<c:forEach var="item" items="${birthreadyMap.dmsDisplayitems}" varStatus="status">
								<li ${status.index ==0 ? 'class="on"' : '' }>
									<span class="bg" style="background-color:#ffbab0;">출산 준비관</span>
									<div class="img_outer">
										<c:choose>
											<c:when test="${isMobile}">
												<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="" />
											</c:when>
											<c:otherwise>
												<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="" />
											</c:otherwise>
										</c:choose>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
			</c:if>
			<!-- //16.10.05 : 상단 비주얼 -->
			
			<div>
				<ul class="tabBox big tp1">
					<li class="on"><a href="#none">출산준비 checklist</a></li>
					<li><a href="#none">출산준비 추천상품</a></li>
				</ul>
				
				<!-- 출산준비 checklist -->
				<div class="tab_con tab_01 tab_conOn">
					<div class="chkList">
						<!-- 16.11.09 : 수정 -->
						<dl class="open">
							<dt>신생아의류</dt>
							<dd>
								<ul>
									<li>
										<div class="chkItem">
											<span>배냇저고리</span>
											<span>3-5</span>
											<!-- <div class="bg_star">
												<span style="width:60%;">
													<em>60</em>
												</span>
											</div> -->
										</div>
										<div class="chkTxt">
											아이가 태어난 뒤에 처음으로 입히는 옷이기 때문에 보온과 위생에 중점을 두고 혈액순환도 잘 되며,  입히고 벗기기 쉽도록 넉넉하고 단추없이 심플한 스타일이 좋으며, 시접선이 바깥쪽인 것으로 구매합니다.

										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>우주복</span>
											<span>2-3</span>
											<!-- <div class="bg_star">
												<span style="width:60%;">
													<em>60</em>
												</span>
											</div> -->
										</div>
										<div class="chkTxt">
											옷의 다리 부분에 지퍼나 단추가 있어 기저귀를 갈 때 편리하며, 손/발싸개가 포함된 제품은 신생아의 상처방지와 보온에 좋습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>내의</span>
											<span>3-4</span>
										</div>
										<div class="chkTxt">
											계절에 따라 얇거나 두꺼운 재질, 짧은 소매와 긴 소매로 구분이 됩니다. 색상이 연한제품은 변색이 적고 겉옷 위로 비치지 않으며 색상이 화려한 제품은 실내에서 실내복으로 입을 수 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>손,발싸개</span>
											<span>2</span>
										</div>
										<div class="chkTxt">
											신생아의 손을 부드럽게 감싸 피부나 외부 물체로부터 손을 보호할 수 있습니다. 손톱으로 얼굴이나 몸에 상처가 나지 않도록 장갑처럼 아이 손에 끼워주며, 이것저것 만져보기 좋아하는 2개월이 지나 아이가 답답해하면 빼주는 것이 좋습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>턱받이</span>
											<span>2-3</span>
										</div>
										<div class="chkTxt">
											아기들은 소화기능의 미숙으로 잘 토하기 때문에 턱받이를 채워두면 아기 옷이 토물로 오염되지 않아 위생적이고 청결하게 해줍니다. 면 턱받이와 방수가 되는 이유식 턱받이가 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>배가리개</span>
											<span>1-2</span>
										</div>
										<div class="chkTxt">
											아기의 가슴은 물론 배까지 완전히 가려주는 대형 턱받이로 여름에는 상의로 겸용할수 있다. 더위때문에 이불을 자주 걷어차고 자는 아기들의 배탈을 막기위해 필요하다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>신생아모자</span>
											<span>2</span>
										</div>
										<div class="chkTxt">
											머리에서 열이 빠져 나가므로 보온성을 유지하고 직사광선이나 공해로부터 보호해 주기 위해서 외출 시 꼭 모자를 씌워 머리를 보호해 줍니다. 부드럽고 신축성 있는 것으로 구매합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>보행기신발</span>
											<span>1-2</span>
										</div>
										<div class="chkTxt">
											보행기를 타기 시작하는 유아가 신는 신발입니다. 6~28개월 정도 발 보호용으로 신으며 실내, 야외에서 착용할 수 있습니다. 미끄럼방지 처리된 제품은 유아가 보행기를 탔을 때 원하는 방향으로 갈 수 있어 도와주며, 미끄러져 넘어지는 것을 막아줍니다.
										</div>
									</li>

									<li>
										<div class="chkItem">
											<span>신생아양말</span>
											<span>2</span>
										</div>
										<div class="chkTxt">
											신생아의 경우 아기의 체온유지를 위해 양말을 신켜줍니다.
										</div>
									</li>
								</ul>
							</dd>
						</dl>
						<dl>
							<dt>수유용품</dt>
							<dd>
								<ul>
									<li>
										<div class="chkItem">
											<span>젖병</span>
											<span>3-6</span>
										</div>
										<div class="chkTxt">
											젖병은 열에 강하며 가볍고 견고한 소재가 좋습니다. 맑고 투명도가 뛰어난 제품이 분유를 탈 때 물의 양, 상태 등을 관찰하기 편리합니다. 젖병 재질로는 유리, PPSU, PP, PA, PES 등이 있습니다. 구매 시 재질의 환경호르몬 검출 여부 및 특성을 고려하여 선택해주세요.

										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>젖꼭지</span>	
											<span>3-6</span>	
										</div>
										<div class="chkTxt">
											젖꼭지는 아기의 개월 수와 먹는 속도에 맞춰 선택합니다. 제조사마다 대상 연령에 따라서 속도를 구분하는 구멍 개수나 형태가 다를 수 있으니 반드시 확인하시고 구매하시기 바랍니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>젖병브러쉬</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											젖병 솔과 젖꼭지 솔이 세트로 되어 있어 세척시에 용이합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>젖병집게</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											젖병 소독 후 꺼낼 수 있는 집게로 젖병에 닿는 부분이 고무바킹으로 되어 있는 제품이 미끄러지지 않고 사용하기 편합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>치아발육기</span>	
											<span>1-2</span>
										</div>
										<div class="chkTxt">
											이가 나기 시작할 때 아이의 씹고자 하는 욕구를 충족시킬 수 있는 놀잇감입니다. 아이가 치발기를 물고 씹을 때 입안의 간지러움을 없애줍니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>노리개젖꼭지</span>	
											<span>1-2</span>
										</div>
										<div class="chkTxt">
											노리개 젖꼭지는 아기에게 충분히 빨 수 있는 기회를 주어서 정서적으로 안정감을 주고 특히 잠투정이 있는 아기들에게 잠을 쉽게 들 수 있도록 도와주는 갓난 아기의 좋은 친구입니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>쥬스컵</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											아기가 12개월 전 후가 되면 밥을 먹여야 하고 아기가 밥을 잘 먹게 되면 젖병을 뗄 준비를 시작해야 합니다. 주스컵은 아기가 젖병을 떼기 몇 개월 전부터 주어서 충분히 익숙해지도록 만들어 줍니다.
										</div>
									</li>
								</ul>
							</dd>
						</dl>
						<dl>
							<dt>발육 및 기타용품</dt>
							<dd>
								<ul>
									<li>
										<div class="chkItem">
											<span>유모차</span>	
											<span>1-2</span>
										</div>
										<div class="chkTxt">
											생후 3개월부터 태울 수 있으며 안전기능, 아이의 성장상태, 설치방법, AS 등을 잘 살펴보고 구매합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>유모차커버</span>	
											<span>1-2</span>	
										</div>
										<div class="chkTxt">
											자외선과 먼지, 해충으로부터 유아를 보호할 수 있습니다. 갑작스러운 기후 변화와 황사에 대처할 수 있으며, 끈이나 벨크로를 이용해 유모차에 쉽게 장착할 수 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>카시트</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											연령별로 신생아용, 컨버터블형, 부스터형에 맞게 고려하여 구매합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>보행기</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											아기가 스스로 허리에 힘을 줄 수 있을 때 다리에 힘을 키우기 위해 사용합니다. 5개월 이후 사용 적당하며 너무 오래도록 태우면 무리가 될 수 있으니 아이의 컨디션이나 성장과정에 따라 적당한 시간을 태워주시기 바랍니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>처네</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											엄마가 아기를 업으면 아기를 엄마 등에 완전히 밀착시켜 포근히 감싸주므로 아기에게 정서적인 안정감을 줄 수 있고 보온성이 뛰어난 사계절용 7부처네와 또한 시원한 여름용 망사 처네 등 다양하게 선택할 수 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>힙시트/아기띠</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											허리벨트와 어깨끈을 조절하는 제품이 대부분으로 유아와 보호자의 체형에 맞춰 착용할 수 있습니다. 수면모자가 장착되어 있는지, 외출시 사용할 수 있는 워머 등을 쉽게 장착할 수 있는지 등도 꼼꼼히 고려해야 합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>힙시트/아기띠</span>	
											<span>1</span>	
										</div>
										<div class="chkTxt">
											허리벨트와 어깨끈을 조절하는 제품이 대부분으로 유아와 보호자의 체형에 맞춰 착용할 수 있습니다. 수면모자가 장착되어 있는지, 외출시 사용할 수 있는 워머 등을 쉽게 장착할 수 있는지 등도 꼼꼼히 고려해야 합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>무릎보호대</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											걸음마가 서툰 어린이의 무릎에 간단히 착용할수 있도록합니다. 무릎을 다쳐 상처나는것을 막을수 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>모빌</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											신생아의 경우, 명암 및 색채를 구별하지 못하므로 색의 대비가 강한 모빌을 선택하여 시각의 발달을 도와주는 것이 좋습니다. 신생아는 흑백모빌을 권장하며, 3개월 이후부터는 컬러 모빌을 사용하도록 합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>딸랑이</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											회전볼을 회전시키거나 놀잇감을 만지면서 촉각이 발달하며, 딸랑이의 다양한 색채는 아이의 시각을 자극하여 색상 분별력을 길러줍니다.
										</div>
									</li>

									<li>
										<div class="chkItem">
											<span>기저귀가방</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											방수되는 가벼운 원단으로 여러 용도의 물품을 넣을 수 있는 수납공간이 많은 제품이 편합니다.
										</div>
									</li>
								</ul>
							</dd>
						</dl>
						<dl>
							<dt>침구류</dt>
							<dd>
								<ul>
									<li>
										<div class="chkItem">
											<span>이불,요세트</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											이불은 가볍고 따뜻하며 지나치게 두껍지 않은 것으로 선택합니다. 사계절 이불은 솜의 두께를 조절해 줄 수 있어서 편리하고, 목화 솜은 자주 일광 소독해야 아기가 쾌적한 잠자리를 할 수 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>낮잠이불세트</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											이불, 패드, 베개가 연결되어 있으며 접었을 때 휴대하기 편리한 형태가 됩니다. 아이들도 쉽게 접고 펼 수 있으며, 어린이집에서 낮잠을 잘 때 많이 사용합니다. 
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>겉싸개</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											아기를 가뿐하게 잘 감싸주므로 출생 후 퇴원용이나 아기와 외출 할 때는 물론, 집안에서도 간편하게 사용할 수 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>방수요</span>	
											<span>1-2</span>	
										</div>
										<div class="chkTxt">
											방수가 되기 때문에 일반 요 위에 깔면 요를 청결하게 유지할 수 있습니다. 기저귀를 갈 때나 여름철 기저귀를 채우지 않고 눕혀놓을 때 사용하기 좋습니다. 
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>담요</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											외출시 아기의 체온을 유지하기 위해 덮어줍니다. 집아에서 겉싸개, 이불대신으로도 사용 합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>속싸개</span>	
											<span>2-3</span>
										</div>
										<div class="chkTxt">
											유아가 움직이지 못하게 감싸 몸을 보호해줍니다. 수면 중 자신의 몸놀림에 놀라 깨는 12개월 미만의 유아에게 사용합니다. 유아가 움직이지 못하도록 타이트하게 싸매야 합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>짱구베개</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											오랜 시간 누워있는 아가의 머리모양을 예쁘게 해줍니다. 열 흡수가 잘 안 되기 때문에 좁쌀베개와 번갈아서 잠깐씩만 베어주는게 좋습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>좁쌀베개</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											신생아용 베개로 아기가 열이 많기 때문에 열을 식혀줄 수 있는 찬 곡식이 들어있습니다. 천연소재이기 때문에 햇볕에 자주 널어서 일광소독 해 주는 것이 좋습니다.
										</div>
									</li>
								</ul>
							</dd>
						</dl>
						<dl>
							<dt>산모용품</dt>
							<dd>
								<ul>
									<li>
										<div class="chkItem">
											<span>유축기</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											수동형과 전동형이 있으며 세척과 호환여부에 따라 살펴보고 구매해야 합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>모유보관팩</span>	
											<span>1set</span>
										</div>
										<div class="chkTxt">
											냉동고에 보관하면서 유아에게 먹이고 싶을 때 한팩씩 꺼내어 먹이기 편리하며, 투명한 비닐팩으로 멸균소독이 되어 있어 위생적입니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>수유패드</span>	
											<span>2set</span>
										</div>
										<div class="chkTxt">
											모유가 새거나 수유 중에 다른 한쪽에서 모유가 나와 옷이 젖는 것을 방지해줍니다. 하루에 3~4회 이상 교체하는 것이 좋으며, 외출 시 사용하기 좋습니다. 나올 때마다 교체해주는 일회용과 세탁하여 여러 번 사용이 가능한 세탁용이 있습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>수유쿠션</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											엄마가 아기를 안고 수유, 분유시에 쿠션을 사용함으로 엄마의 허리와 팔목에 무리를 가지 않게 하고 아기도 엄마의 품에서 정서적으로 안정감 있게 모유나 분유를 먹을 수 있게 도와줍니다.
										</div>
									</li>
								</ul>
							</dd>
						</dl>

						<dl>
							<dt>목욕.위생용품</dt>
							<dd>
								<ul>
									<li>
										<div class="chkItem">
											<span>욕조</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											아기가 커서도 사용할 수 있게 큰 사이즈를 선택하며 목욕 보조받침이 있는 제품이 엄마가 아기를 씻길 때 편리합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>기저귀set</span>
											<span>20-30</span>
										</div>
										<div class="chkTxt">
											하루도 빠짐없이 계속 사용해야 하는 것으로 흡습성과 세탁성이 좋은 것으로 선택합니다. 하루에 20회 이상 갈아주어야 하므로 조금 넉넉하게 장만합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>기저귀밴드</span>	
											<span>2</span>
										</div>
										<div class="chkTxt">
											일자형 종이기저귀를 쓸 때 밴드로 고정시켜 줍니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>기저귀커버</span>	
											<span></span>
										</div>
										<div class="chkTxt">
											방수, 발수의 이중효과가 있는 소재로 배꼽선 아래로 가도록 제 몸에 잘 맞는 사이즈로 선택합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>면봉</span>	
											<span>2</span>
										</div>
										<div class="chkTxt">
											아기용이기 때문에 살균소독이 되어 있는 제품이 좋고 일반 어른 사이즈보다 훨씬 작은 것을 고릅니다. 목욕 후 귀 속 물기 제거에 필요합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>아기화장품</span>	
											<span>각1</span>
										</div>
										<div class="chkTxt">
											구매 하기 전에 샘플을 사용해보고 아기피부에 맞는지 체크 후 구매하도록 합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>물휴지</span>	
											<span>3</span>
										</div>
										<div class="chkTxt">
											습성분이 함유 되어 있는 부드러운 제품을 선택합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
										<span>손수건세트</span>	
										<span>20-30</span>	
										</div>
										<div class="chkTxt">
											여러 용도로 많이 사용하게 되므로 넉넉하게 준비합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>변기</span>	
											<span>1</span>
										</div>
										<div class="chkTxt">
											기저귀를 뗄 시기에 배변 습관을 위해서 미리 변기에 앉아 있도록 놀이로 시작하면서 적응할 수 있도록 해 주시면 좋습니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
										<span>체온계</span>	<span>1</span>	


										</div>
										<div class="chkTxt">
											아기의 몸상테는 항상 체크해야 하므로 구매필수이며, 이마형, 귓속형, 부인형이 있으며 활동이 많은 아이에게 간편한 상품으로 구매합니다.
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>코흡입기</span>
											<span>1</span>
										</div>
										<div class="chkTxt">
											유아의 코 안의 콧물 등의 이물질을 제거 할 수 있습니다. 유아의 답답한 코를 시원하게 뚫어주기 때문에 유아가 호흡하기가 편합니다. 코점막을 다치지 않게 흡입구가 부드러운 것을 선택해야 합니다. 
										</div>
									</li>
									<li>
										<div class="chkItem">
											<span>손톱가위</span>
											<span>1</span>	
										</div>
										<div class="chkTxt">
											아기의 손톱은 연약하므로 손톱깎이보다는 손톱가위로 잘라주는 것이 편리합니다. 
										</div>
									</li>
								</ul>
							</dd>
						</dl>
						<!-- //16.11.09 : 수정 -->
					</div>
				</div>
				
				<!-- 출산준비 추천상품 -->
				<div class="tab_con tab_02">
				
					<c:choose>
						<c:when test="${isMobile}">
							<!-- 카테고리 // -->
							<div class="tabFixW">
								<div class="tabFix">
									<div class="tab_outer swiper-container birthdaySwiper_category">
										<ul class="tabBox swiper-wrapper">
											<li class="swiper-slide">
												<a href="#none" onclick="special.common.category.getSpecialCategory('all','ALL');" class="theme1">전체</a>
											</li>
											<c:if test="${not empty depth1}">
												<c:forEach var="depth1" items="${depth1}" varStatus="status">
													<li class="swiper-slide">
														<a href="#none" onclick="special.common.category.getSpecialCategory('category','${depth1.displayCategoryId}');" class="category_${depth1.displayCategoryId}"><c:out value="${depth1.name}"/></a> 
													</li>
												</c:forEach>
											</c:if>
										</ul>
									</div>
								</div>
							</div>
							<!-- // 카테고리  -->	
							
							<!-- 정렬 // -->
							<div class="tit_style3 mo_only">
								<div class="sortBoxList sort_2ea" id="moSortBoxList" style="display:bolck;">
								
									<ul>
										<li>
											<div class="select_box1" id="moSpeCtgList">
												<jsp:include page="/WEB-INF/views/dms/include/categorySelete.jsp" flush="false"/>
											</div>
										</li>
										<li>
											<div class="select_box1">
												<label></label>
												<select id="sortSelect">
													<option value="ORDER_QTY">인기상품순</option>
													<option value="RATING">상품평순</option>
													<option value="LOW_PRICE">낮은가격순</option>
													<option value="SALE_PRICE">높은가격순</option>
													<option value="DATE">최근등록순</option>
												</select>
											</div>
										</li>
										<li>
											<button type="button" class="btnListType list">블록형 / 리스트형</button>
										</li>
									</ul>
								</div> 
							</div>
							<!-- // 정렬  -->
							
							<!-- //상품 리스트  : 검색 상품 목록-->
							<div class="list_group" id="productList" style="display:block;">
								<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
									<jsp:param value="N" name="pagingYn"/>
								</jsp:include>
							</div>
							<!-- //상품 리스트  : 검색 상품 목록 -->
							
						</c:when>
						<c:otherwise>
							
							<!-- 카테고리 // -->
							<div class="swiper_wrap">
								<div class="tab_outer swiper-container mainSwiper_category">
									<ul class="tabBox swiper-wrapper">
										<li class="swiper-slide">
											<a href="#none" onclick="special.common.category.getPcCategoryPrd('BIRTH','ALL');" class="theme1">전체</a>
										</li>
										<c:if test="${not empty depth1}">
											<c:forEach var="depth1" items="${depth1}" varStatus="status">
												<li class="swiper-slide">
													<a href="#none" onclick="special.common.category.getPcCategoryPrd('BIRTH','${depth1.displayCategoryId}');" class="category_${depth1.displayCategoryId}"><c:out value="${depth1.name}"/></a>
												</li>
											</c:forEach>
										</c:if>
									</ul>
								    <div class="swiper-button-next btn_tp6"></div>
								    <div class="swiper-button-prev btn_tp6"></div>
								</div>
							</div>
							<!-- // 카테고리  -->	
	
							<!-- 정렬 //-->
							<c:if test="${categorySearch.upperDisplayCategoryId eq 'ALL' }">
								<div class="tit_style3 pc_only" style="display:block;" id="allOrderByDiv">
									<strong class="sort_num">
										<span>
											총 <em id="searchCount">${search.totalCount}</em>건
										</span>
									</strong>	
									
									<!-- 정렬 //-->
									<jsp:include page="/WEB-INF/views/dms/include/searchOrderby.jsp" flush="false"/>
								</div>
							</c:if>
							<!-- //정렬 -->
							
							<!-- 상품 리스트  : 검색 상품 목록 //-->
							<div class="list_group" id="productList" style="display:block;">  
								<jsp:include page="/WEB-INF/views/dms/special/inner/spcSearchProductList.jsp" flush="false"/>
							</div>
							<!-- //상품 리스트  : 검색 상품 목록-->
							
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>

<!-- 팝업 : 전체보기 -->
<c:if test="${isMobile}">
	<c:if test="${birthreadyMap.totalCount > 0}">
		<div class="pop_wrap ly_visual">
			<div class="pop_inner">
				<div class="pop_header type1">
					<h3>전체보기</h3>
				</div>
				<div class="pop_content">
					<ul>
						<c:forEach var="item" items="${birthreadyMap.dmsDisplayitems}" varStatus="status">
							<li><a href="${item.url1}"><img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.text2}" /></a></li>
						</c:forEach>
					</ul>
				</div>
				<button type="button" class="btn_x pc_btn_close">닫기</button>
			</div>
		</div>
	</c:if>
</c:if>
<!-- //팝업 : 전체보기 -->