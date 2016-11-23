<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
$(function(){

	//전시카테고리 네비게이션 이벤트
	$('#dispSelect select').change(function(){
	  	var dipCtgId= $(this).val();
	  	if(dipCtgId !== ''){
	  		
	  		var depth1 = "";
	  		var depth2 = "";
	  		
	  		$("#dispSelect select").find("option").each(function() {
	  	      if(this.value == dipCtgId) {
	  	    	depth1= this.dataset.depth1;
	  	    	depth2= this.dataset.depth2;
	  	    	
	  	         return false;
	  	      }
	  	   });
	  		
			if(depth1 == '1'){
				//2depth 카테고리 변경
				var param = {'linkType':'Y','displayCategoryId':dipCtgId,'depth':depth2};
				$.post('/dms/category/navi',param, function(html) {
					$('#categoryInner').html(html);
					
				}).fail(function(xhr, status, error) {
					console.log("code:"+status+"\n"+"error:"+error);
				});
			}else{
				ccs.link.display.dispTemplate(dipCtgId);
			}
 		}
	});	
});
</script>
<!-- 전시카테고리 네비게이션 -->
<li>홈</li>
<c:choose>
	<c:when test="${search.linkType eq 'Y'}">
		<c:set var="id" value="${currentCategory.displayCategoryId}" />
		<c:set var="name" value="${currentCategory.name}" />
		<c:set var="depthId2" value="${currentCategory.dmsDisplaycategory.displayCategoryId}" />
		<c:set var="depthName2" value="${currentCategory.dmsDisplaycategory.name}" />
		<c:set var="depthId3" value="${currentCategory.dmsDisplaycategory.dmsDisplaycategory.displayCategoryId}" />
		<c:set var="depthName3" value="${currentCategory.dmsDisplaycategory.dmsDisplaycategory.name}" />
		<c:forEach begin="1" end="${search.depth}" varStatus="status" var="depth">
		
			<li>
				<div class="select_box1" id="dispSelect">
					<c:choose>
						<c:when test="${depth eq '1' }">
							<label>${name}</label>
						</c:when>
						<c:when test="${depth eq '2' }">
							<label>${depthName2}</label>
						</c:when>
						<c:otherwise>
							<label>${depthName3}</label>
						</c:otherwise>
					</c:choose>
						
					<select>
						<c:forEach var="list" items="${categoryList}" varStatus="status1">
							<c:if test="${depth == list.depth}">
								<c:choose>
									<c:when test="${depth eq '1' }">
										<option ${id == list.displayCategoryId ? 'selected="selected"' : '' } value="${list.displayCategoryId}" data-depth1="${list.depth}", data-depth2="${search.depth}"><c:out value="${list.name}"/></option>
									</c:when>	
									<c:when test="${depth eq '2' }">
										<c:if test="${id ==  list.upperDisplayCategoryId}">
											<option ${status1.index == 0 ? 'selected="selected"' : '' } value="${list.displayCategoryId}"  data-depth1="${list.depth}", data-depth2="${currentCategory.depth}"><c:out value="${list.name}"/></option>
										</c:if>
									</c:when>
									<c:otherwise>
										<c:if test="${depthId2 ==  list.upperDisplayCategoryId}">
											<option ${status1.index == 0 ? 'selected="selected"' : '' } value="${list.displayCategoryId}"  data-depth1="${list.depth}", data-depth2="${currentCategory.depth}"><c:out value="${list.name}"/></option>
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:forEach>
					</select>
				</div>
			</li>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:forEach begin="1" end="${currentCategory.depth}" varStatus="status" var="depth">
			<!-- depth의 id와 name -->
			<c:set var="name" value="${fn:split(currentCategory.depthFullName,'|')[status.index-1]}" />
			<c:set var="id" value="${fn:split(currentCategory.depthDisplayCategoryId, '|')[status.index-1]}" />
			<c:set var="upId" value="${fn:split(currentCategory.depthDisplayCategoryId, '|')[status.index-2]}" />
			<li>
				<div class="select_box1" id="dispSelect">
					<label><c:out value="${name}"/></label>
					<select>
						<c:forEach var="list" items="${categoryList}" >
							<c:if test="${status.index == list.depth}">
								<c:choose>
									<c:when test="${status.index eq '1' }">
										<option ${id == list.displayCategoryId ? 'selected="selected"' : '' } value="${list.displayCategoryId}" data-depth1="${list.depth}", data-depth2="${currentCategory.depth}"><c:out value="${list.name}"/></option>
									</c:when>
									<c:otherwise>
										<c:if test="${upId ==  list.upperDisplayCategoryId}">
											<option ${id == list.displayCategoryId ? 'selected="selected"' : '' } value="${list.displayCategoryId}"  data-depth1="${list.depth}", data-depth2="${currentCategory.depth}"><c:out value="${list.name}"/></option>
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:forEach>
					</select>
				</div>
			</li>
		</c:forEach>
	</c:otherwise>
</c:choose> 
