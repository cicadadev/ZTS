<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>

if(window.opener){
	
	if('${ctype}'=='1'){// 가입 후 로그인 처리
		window.close();
	}
	else if('${ctype}'=='2'){//팝업만 닫기
		window.close();
	}else if('${ctype}'=='3'){//로그아웃처리
		//window.opener.location.href=global.config.domainUrl+"/logout.jsp";
		window.opener.location.reload();
		window.close();
	}

}else{
	window.location.href="/ccs/common/main";
}

</script>