<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>	
<script>


var url = '${url}';

if(window.opener){
	if(url!=''){
		window.opener.location.href=ccs.config.domain()+"/sso.jsp?url="+url;
		//window.opener.location.href="${url}";
	}else{
		
		window.opener.location.href=ccs.config.domain()+"/sso.jsp";
	}
	
	window.close();
	
	

}else{
	location.reload();
}

</script>
