<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<script>
$(document).ready(function(){
	var tag = '<%=request.getParameter("footer_pc_column")%>';
	
	if(common.isNotEmpty(tag)){
		$('#footer_pc_column' + tag.replace('.', '_')).addClass("on").parent().siblings("li").children("a").removeClass("on");
	}
});

</script>
<div class="footer_pc">
	<div class="inner">
		<div class="columnL">
			<ul>
				<li>
					<a href="http://www.zerotoseven.co.kr" target="_blank" ><em>회사소개</em></a>
				</li>
				<li>
					<a id="footer_pc_column1" href="/ccs/common/service?footer_pc_column=1"><em>이용약관</em></a>
				</li>
				<li>
					<a id="footer_pc_column2" href="/ccs/common/privacy?footer_pc_column=2"><em>개인정보 취급방침</em></a>
				</li>
				<li>
					<a id="footer_pc_column3" href="/ccs/cs/main?footer_pc_column=3"><em>고객센터</em></a>
				</li>
				<li>
					<a id="footer_pc_column4" href="/ccs/offshop/search?footer_pc_column=4"><em>매장찾기</em></a>
				</li>
				<li>
					<a id="footer_pc_column5" href="/ccs/common/alliance?footer_pc_column=5"><em>입점제휴안내</em></a>
				</li>
				<li>
					<a href="#none">협력업체 어드민</a>
				</li>
			</ul>
		
			<div class="group">
				<img src="/resources/img/pc/logo/footer_logo.jpg" alt="zero to seven" class="footer_logo" />
		
				<div class="address">
					<p>
						<b>(주)제로투세븐</b> 대표이사 : 김정민,조성철<br />
						(03926) 서울시 마포구 상암산로76 (상암동, YTN뉴스퀘어 17층/18층)<br />
						<span>Tel : 1588-8744 </span>
						<span>Fax : 02-740-3197</span>
						<span>E-mail : mallmaster@maeil.com</span><br />
						<span>사업자 등록번호 200-81-76684</span>
						<span>통신판매업신고번호 제 01-50</span>
						<a href="#none">사업자정보확인</a>
					</p>
		
					<p>
						제로투세븐 닷컴은 매일유업(주)이 설립한 육아전문기업 (주)제로투세븐이 운영합니다.
					</p>
		
					<small>
						Copyright ⓒ zero to seven. All right Reserved.
					</small>
				</div>
			</div>
		</div>
		
		<div class="columnR">
			<div class="select_box1" >
				<label>Family Site</label>
				<select onchange="ccs.go_url(this.options[this.selectedIndex].value);">
					<option value="http://www.maeil.com" selected>매일유업</option>
					<option value="http://www.maeili.com">매일아이</option>
					<option value="http://www.sanghafarm.co.kr">상하농원</option>
					<option value="http://absolute.maeili.com/absolute/index.jsp">앱솔루트태교</option>
					<option value="">맘스마일</option>
					<option value="">맘마밀</option>
					<option value="http://direct.maeil.com">가정배달</option>
					<option value="http://www.sanghacheese.co.kr">상하치즈</option>
					<option value="http://sanghafarm.maeil.com">상하목장</option>
				</select>
			</div>
		
			<p>
				<em>
					<img src="/resources/img/pc/logo/lg.jpg" alt="LG U+" /> 구매 안전 서비스 안내
				</em>
		
				<span>
					고객님의 안전거래를 위해 현금등으로 결제 시 저희 쇼핑몰에서 가입한 LG U+구매안전(에스크로) 서비스를 이용하실 수 있습니다. 
				</span>
		
				<a href="#" onclick="window.open('http://pgweb.uplus.co.kr/ms/escrow/s_escrowYn.do?mertid=0to7_00', 'LGU+구매안전','height=520, width=460, scrollbars=yes');return false;" target="_blank" class="btn_lg">서비스 가입사실 확인</a>
			</p>
		
			<div class="sns">
				<a href="https://www.facebook.com/zerotoseven07" target="_blank" class="f">페이스북</a>
				<a href="https://story.kakao.com/ch/zerotoseven" target="_blank" class="k">카카오스토리</a>
				<a href="http://zerotoseven.tistory.com" target="_blank" class="t">티스토리</a>
			</div>
		</div>
	</div>
</div>		