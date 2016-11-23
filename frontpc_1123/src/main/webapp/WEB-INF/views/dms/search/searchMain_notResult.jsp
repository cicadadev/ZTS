<%--
	화면명 : 검색 메인
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<script type="text/javascript">

if('${deviceTypeCd}' == 'DEVICE_TYPE_CD.PC'){
	device = 'PW';
}else if('${deviceTypeCd}' == 'DEVICE_TYPE_CD.APP'){
	device = 'MI';
}else if ('${deviceTypeCd}' == 'DEVICE_TYPE_CD.MW'){
	device = 'MW';
}

//추천엔진수집 스크립트
window._rblqueue = window._rblqueue || [];
  _rblqueue.push(['setVar','cuid',global['config'].recobelCuid]);
  _rblqueue.push(['setVar','device',device]);
  _rblqueue.push(['setVar','userId','']);		// optional
  _rblqueue.push(['setVar','searchTerm','${keyword}']);
  _rblqueue.push(['track','search']);	
  setTimeout(function() {
    (function(s,x){s=document.createElement('script');s.type='text/javascript';
    s.async=true;s.defer=true;s.src=(('https:'==document.location.protocol)?'https':'http')+
    '://assets.recobell.io/rblc/js/rblc-apne1.min.js';
    x=document.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);})();
  }, 0);

 $(document).ready(function(){
	 
	 //추천엔진
	 var suggestedQuery =  '${suggestedQuery}';
	 var keyword =  '${keyword}';
	 var searchTerm ="";
	 
	 if(suggestedQuery != '' && suggestedQuery != null){
		 searchTerm = suggestedQuery;
	 }else{
		 searchTerm = keyword
	 }
	 
	dms.search.getRecommendationProductList('relationProduct', {recType:'s001', size : 8, st:searchTerm});

});

</script>
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="검색결과" name="pageNavi"/>
	</jsp:include>		
	<div class="inner">
		<div class="srchResult">
		
			<c:choose>
				<c:when test="${not empty suggestedQuery}">
					<div class="no_result">
						<div class="txt">
							검색결과가 없습니다.
							<p>‘<a href="javaScript:dms.common.setKeyWord('${suggestedQuery}')"><c:out value="${suggestedQuery}"/></a>’ 로 검색하시겠습니까?</p>
						</div>
					</div>
					
					<div class="rolling_box " id="relationProduct">
						
					</div>	
				</c:when>
				<c:otherwise>
					<div class="no_result">
						<div class="txt">
							검색결과가 없습니다. 
						</div>
					</div>
					<c:if test="${not empty keyword}">
						<div class="rolling_box " id="relationProduct">
							
						</div>
					</c:if>
				</c:otherwise>
			</c:choose>
			
		</div>
	</div>
