<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.common.util.BoSessionUtil" %>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta name="_csrf" content="${_csrf.token }">
	<meta name="_csrf_header" content="${_csrf.headerName }">
	<meta name="_csrf_parameter" content="${_csrf.parameterName }">
	<meta name="_system_type" content="${_system_type }">	
	
	
	<script type="text/javascript">
	
	// global variable
	global = {
		config : {//from properties.xml
				ownBusinessId : '<%=Config.getString("zeroto7.business.id")%>', // 자사업체ID( global.config.ownBusinessId )
				imageDomain :  	'<%=Config.getString("image.domain")%>', //이미지도메인( global.config.imageDomain )
				sslImageDomain :  	'<%=Config.getString("image.ssl.domain")%>', //SSL이미지도메인( global.config.sslImageDomain )
				domainUrl :  '<%=Config.getString("admin.domain.url")%>' ,//도메인URL ( global.config.domainUrl )
				domainUrlFront :  '<%=Config.getString("front.domain.url")%>' ,//도메인URL ( global.config.domainUrl )
				rootCategoryId :  '<%=Config.getString("partner.rootcategory")%>' ,//도메인URL ( global.config.domainUrl )
				},
		session : {
			businessId:  '<%=BoSessionUtil.getBusinessId()==null ? "" : BoSessionUtil.getBusinessId()%>',//po일경우업체ID
			mdYn: '<%=BoSessionUtil.getMdYn()%>',//MD 여부
			roleId: '<%=BoSessionUtil.getRoleId()%>',//권한ID
		}
				
	};
	</script>
	
	
	<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>
	<script type="text/javascript" src="/resources/js/ui.js"></script>
	
    <script type="text/javascript" src="/resources/lib/angular-1.5.4/angular.js"></script>
    <script type="text/javascript" src="/resources/lib/angular-1.5.4/angular-touch.js"></script>
    <script type="text/javascript" src="/resources/lib/angular-1.5.4/angular-animate.js"></script>
	<script type="text/javascript" src="/resources/lib/angular-1.5.4/ui-grid-3.1.1.js"></script>
	<script type="text/javascript" src="/resources/lib/angular-1.5.4/angular-resource.js"></script>
	<script type="text/javascript" src="/resources/lib/angular-1.5.4/angular-route.js"></script>
	<script type="text/javascript" src="/resources/lib/angular-1.5.4/angular-cookies.js"></script>
	<script type="text/javascript" src="/resources/lib/angular-1.5.4/angular-locale_ko-kr.js"></script>		
	
	<script type="text/javascript" src="/resources/lib/calendar/angularjs-datetime-picker.js"></script>
	                                              
	<script type="text/javascript" src="/resources/lib/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/lib/ckeditor/ng-ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/dirPagination.js"></script>
	
			
<!--  	<link rel="stylesheet" type="text/css" media="screen" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
	<link href="//cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/build/css/bootstrap-datetimepicker.css" rel="stylesheet">
	<script type="text/javascript" src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
	<script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment-with-locales.js"></script>
	<script src="//cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/src/js/bootstrap-datetimepicker.js"></script> --> 
	
	<%-- 공통 Angular --%>
	<script type="text/javascript" src="/resources/js/gsf.common.js"></script>
	<script type="text/javascript" src="/resources/js/pwdValidation.js"></script>

	<script type="text/javascript" src="/resources/js/directive/gsf.directive.js"></script>

	<script type="text/javascript" src="/resources/js/filter/gsf.filter.js"></script>

	<script type="text/javascript" src="/resources/js/util/gsf.util.js"></script>

	<script type="text/javascript" src="/resources/js/service/gsf.service.js"></script>
	<script type="text/javascript" src="/resources/js/service/gsf.service.grid.js"></script>
	<script type="text/javascript" src="/resources/js/service/gsf.service.popup.js"></script>
	
		
	<%-- 업무별 서비스 --%>
 	<script type="text/javascript" src="/resources/js/service/ccs.service.js"></script>
	<script type="text/javascript" src="/resources/js/service/dms.service.js"></script>
	<script type="text/javascript" src="/resources/js/service/mms.service.js"></script>
	<script type="text/javascript" src="/resources/js/service/oms.service.js"></script>
	<script type="text/javascript" src="/resources/js/service/pms.service.js"></script>
	<script type="text/javascript" src="/resources/js/service/sps.service.js"></script>
	
			
	<%-- 스타일 시트 --%>
	<link rel="stylesheet" type="text/css" href="/resources/lib/angular-1.5.4/ui-grid.min.css">
	<link rel="stylesheet" type="text/css" href="/resources/css/common.css" />		
	<link rel="stylesheet" type="text/css" href="/resources/lib/ckeditor/ng-ckeditor.css">
	
	<link rel="stylesheet" type="text/css" href="/resources/lib/calendar/angularjs-datetime-picker.css">
	<input type="hidden" id="pageId" value="${pageId }" />
	
	<script type="text/javascript">
	// IE INPUT 한글 짤림 방지 스크립트 
	angular.element(document).ready(function(){
		$('input[type="text"]').off('compositionstart').on('compositionstart', function(e) {
			e.stopImmediatePropagation();
		});
		
		$('textarea').off('compositionstart').on('compositionstart', function(e) {
			e.stopImmediatePropagation();
		});
	});
	</script>
