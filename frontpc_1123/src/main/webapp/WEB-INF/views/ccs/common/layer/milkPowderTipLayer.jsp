
<%--
	화면명 : 분유관 노하우 Tip
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	
function goTab(value){
	
	for (i= 1; i < 5; i++) {
		$('#tab'+i).removeClass(" tab_conOn");		
	}
	$('#tab'+value).addClass(" tab_conOn");
}

$(document).ready(function() {
	$(".tabBox a, .tabBox1 a, .tabBox2 a").off("click").on("click", function(e){
		var idx = $(this).parent().index();
		var parent_ul;

		if( $(this).closest("ul").hasClass("tabBox") ){
			parent_ul = $(this).closest(".tabBox");
		}else if( $(this).closest("ul").hasClass("tabBox1") ){
			parent_ul = $(this).closest(".tabBox1");
		}else{
			parent_ul = $(this).closest(".tabBox2");
		}

		$(this).parent().addClass("on").siblings("li").removeClass("on");
		$(parent_ul).siblings(".tab_con").eq(idx).addClass("tab_conOn").siblings(".tab_con").removeClass("tab_conOn");

//		e.preventDefault();
	});
	
})

	
</script>
    
<!-- 16.10.06 : 팝업 - 분유 노하우 tip -->
<div class="pop_wrap ly_milk" id="milkPowderTipLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">분유 노하우 tip</h3>
		</div>
		<div class="pop_content">
			<div>
				<ul class="tabBox tab4ea" id="on">
					<li class="on"><a href="javascript:goTab('1');"><span>월령별</span>분유섭취량</a></li>
					<li><a href="javascript:goTab('2');"><span>분유</span> 타는 법</a></li>
					<li><a href="javascript:goTab('3');"><span>분유</span> 교체 하는 법</a></li>
					<li><a href="javascript:goTab('4');">특수분유</a></li>
				</ul>
				<div class="tab_con tab_01 tab_conOn" id="tab1">
					<dl>
						<dt>월령별 분유 섭취량</dt>
						<dd>
							<p>분유는 성분상 큰 차이는 없으므로 아기가 잘 먹고 소화에 무리함이 없는 것을 선택하시는 것이 좋습니다. 분유의 섭취량, 하루에 먹는 횟수는 아이 발달에 따라 개인차가 크기 때문에 권장량을 기준으로 내 아기의 습관과 특성을 충분히 고려하여 판단하는 것이 좋습니다.</p>
						</dd>
					</dl>
					<dl>
						<dt>월령별 1회, 1일 권장 섭취량</dt>
						<dd>
							<table>
								<colgroup>
									<col style="width:25%;" />
									<col style="width:25%;" />
									<col style="width:25%;" />
									<col style="width:25%;" />
								</colgroup>
								<thead>
									<tr>
										<th rowspan="2">월령</th>
										<th colspan="2">1회 사용량</th>
										<th rowspan="2">1일 사용횟수</th>
									</tr>
									<tr>
										<th><span>스푼수</span>(1스푼 =40ml)</th>
										<th>조유량 (ml)</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>0~1/2</td>
										<td>2</td>
										<td>80</td>
										<td>7~8</td>
									</tr>
									<tr>
										<td>1/2~1</td>
										<td>3</td>
										<td>120</td>
										<td>6~7</td>
									</tr>
									<tr>	
										<td>1~1</td>
										<td>4</td>
										<td>160</td>
										<td>6</td>
									</tr>
									<tr>
										<td>2~백일</td>
										<td>4</td>
										<td>160</td>
										<td>6</td>
									</tr>
									<tr>	
										<td>백일~6개월</td>
										<td>5</td>
										<td>200</td>
										<td>5</td>
									</tr>
									<tr>
										<td>6~첫돌</td>
										<td>6</td>
										<td>240</td>
										<td>4~5</td>
									</tr>
									<tr>
										<td>첫돌~24개월까지</td>
										<td>6</td>
										<td>240</td>
										<td>3~4</td>
									</tr>
								</tbody>
							</table>
						</dd>
					</dl>
				</div>
				<div class="tab_con tab_02" id="tab2">
					<dl>
						<dt>분유 타는 법</dt>
						<dd>
							<p>쉬우면서도 어려운 분유 타는 법을 알려 드릴께요. ^^ 분유를 아기에게 먹이기 전에 꼭 손을 씻어서 청결한 상태로 해주세요. 미리 타놓지 마시고 먹이기 바로 전에 타야 해요.<br .>먹다 남긴 것이 아깝다고 다시 먹이지는 말아주세요.</p>
							<div class="milkImg">
								<img src="/resources/img/pc/bg/milk.jpg" alt="분유 타는 법" />
							</div>
							<ol>
								<li><i>1.</i>분유를 탈 때는 물을 먼저 팔팔 끓인 후 70도 이상으로  식혀서  사용해야 하며, 식힌 물을 젖병에 수유량의 1/2을 넣어주세요.</li>
								<li><i>2.</i>젖병에 아기 월령과 몸무게에 맞게 분유를 넣은 후 가볍게 흔들어 주세요.  분유통 안에 있는 전용스푼을 
								사용하는 것이  좋아요.</li>
								<li><i>3.</i>분유가 모두 녹았다면 식힌 물을 최종 수유량까지 넣고 젖꼭지를 끼워 가볍게 흔들어서 섞어주세요.</li>
								<li><i>4.</i>손목 안쪽이나 손등에 조금 떨어뜨려 너무 뜨겁지 않고,  따뜻하면 아기에게 먹이기 좋은 온도랍니다.</li>
							</ol>
						</dd>
					</dl>
				</div>
				<div class="tab_con tab_03" id="tab3">
					<dl>
						<dt>분유 교체 하는 법</dt>
						<dd>
							<p>분유를 갑자기 바꾸면 아기의 소화기(위/장)에 영향이 발생하여 자극을 주게 됩니다.<br />현재 먹이는 분유와 새로 먹일 분유를 섞어서 먹임으로써 아기 소화기관이 서서히 적응을 할 수 있도록 해주는 것이 좋습니다.</p>
						</dd>
					</dl>
					<ul>
						<li><i>*</i>분유를 섞는 비율은 처음에는 기존 분유와 새로운 분유가 7:3 비율로 먹여 아기의 변 상태를 지켜봅니다.</li>
						<li><i>*</i>상태가 정상이면 5:5 비율, 마지막으로 3:7 비율로 먹인 후에 바꾸어 주면 됩니다. </li>
					</ul>
					<dl class="tbl">
						<dt>유아식 바꾸어 먹이는 방법</dt>
						<dd>
							<table>
								<colgroup>
									<col style="width:25%;" />
									<col style="width:25%;" />
									<col style="width:25%;" />
									<col style="width:25%;" />
								</colgroup>
								<thead>
									<tr>
										<th>1~3일째</th>
										<th>4~5일째</th>
										<th>6~7일째</th>
										<th>8일째</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>타사제품 3/4<span class="plus">+</span>매일분유 1/4</td>
										<td>타사제품 2/4<span class="plus">+</span>매일분유 2/4</td>
										<td>타사제품 1/4<span class="plus">+</span>매일분유 3/4</td>
										<td>매일분유 4/4</td>
									</tr>
								</tbody>
							</table>
						</dd>
					</dl>
				</div>
				<div class="tab_con tab_04" id="tab4">
					<dl>
						<dt>특수분유</dt>
						<dd>
							<p>특수분유는 아기가 질병에 걸렸거나 영양상 특별한 이유 (알레르기 체질, 유당 분해 효소의 결핍, 미숙아, 저체중아 등)가 있을 경우 의사와 상의하여 아이 체질에 맞는 특수 조제 분유를 먹이셔야 합니다.</p>
						</dd>
					</dl>
					<div class="milkImg">
						<img src="/resources/img/pc/bg/milk1.jpg" alt="특수분유" />
					</div>
				</div>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- //16.10.06 : 팝업 - 분유 노하우 tip -->