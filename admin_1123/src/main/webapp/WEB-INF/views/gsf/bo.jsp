<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head id="mainAppHeader">
	<title>0to7 BO</title>	
	<jsp:include page="/WEB-INF/views/gsf/commonScript.jsp" flush="true"/>
	<script type="text/javascript" src="/resources/js/app/ccs.app.main.js"></script>
</head>
<script>

function calcHeight(frameId)
{
  //find the height of the internal page
  var the_height= document.getElementById(frameId).contentWindow.document.body.scrollHeight;
  document.getElementById(frameId).height= the_height;
  
  if(the_height == '0' && document.getElementById(frameId).height != '0'){
	  var url = document.getElementById(frameId).src;
	  document.getElementById(frameId).src= url;

  }
}


</script>
<style>
/* iframe { 
	min-height: 600px; 
} */
</style>
<body data-ng-app="mainApp">
	<div class="wrap">
		<!-- ### header : 2016.04.27 추가 ### -->
		<div id="main" class="header_group" ng-controller="boController as mainCtrl">
		
			<header>
				<h1>
					<a href="/main" data-ng-click="mainCtrl.main()">INTUNE GCP 2.0</a>
				</h1>			
				<div class="user_box">
					<b class="user_id">${loginInfo.loginName}</b>
					
					<a href="#none" class="btn_type4" data-ng-click="mainCtrl.openUser('${loginInfo.loginId}')">
						<b>정보변경</b>
					</a>
					<button type="button" class="btn_type4" data-ng-click="mainCtrl.logout()">
						<b>로그아웃</b>
					</button>
				</div>			
			</header>
			<nav class="lnb_menu">
				<ul>
					<li id="sm{{menuGroup.sortNo}}" class="{{menuGroup.isClick?'on':''}}" data-ng-repeat="menuGroup in menuGroupList">
						<a href="javascript:void(0);" data-ng-click="mainCtrl.openMenu(menuGroup)" data-ng-bind="menuGroup.name"></a>
						<ul style="display:{{menuGroup.isClick?'block':'none'}}">
							<li data-ng-repeat="menu in menuGroup.ccsMenus">
								-<a href="#none" menubtn data-menu-id="{{menu.menuId}}" data-pagename="{{menu.name}}" data-url="{{menu.url}}" data-ng-bind="menu.name"></a>
							</li>			
						</ul>
					</li>
				</ul>
				<button type="button" class="btn_lnb_control btn_lnb_show">좌측 메뉴 보기/숨기기</button>	
			</nav>
			<!-- ### 탭 메뉴 ### -->
			<div class="position_tab">
				<div class="box">
					<ul class="tab_type1">
						<opentab class="opentab"></opentab>
					</ul>
				</div>

				<!-- ### 탭 컨트롤 ### -->
				<div class="btn_tab_control">
					<span class="page">
						<button type="button" class="btn_prev">이전</button>
						<button type="button" class="btn_next">다음</button>
					</span>
					<button type="button" class="btn_type2 btn_type2_gray">
						<b>모두 닫기</b>
					</button>
				</div>
				<!-- ### //탭 컨트롤 ### -->
			</div>
			
		</div>
		<!-- ### //header : 2016.04.27 추가 ### -->
		
		<section class="content">
			<div class="inner">
			<article class="con_box con_on"  id="pages">
				<iframe src="/ccs/bomain" name='__pageSpan' id='mainbo' frameborder="0" onLoad='calcHeight(this.id);' style='width:100%;'></iframe>
			</article>
			</div>
	
			<!-- ### 푸터 : 2016.04.27 추가 ### -->
			<footer>
				Copyright © 2016 INTUNE CS. All rights reserved
			</footer>
			<!-- ### //푸터 : 2016.04.27 추가 ### -->
		</section>
		<!-- ### //content ### -->
	</div>
</body>
</html>
