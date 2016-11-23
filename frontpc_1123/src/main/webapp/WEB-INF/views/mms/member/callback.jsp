<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>	
<script>


var url = '${url}';

if(window.opener){
	if(url!=''){
		window.opener.location.href=url;
		//window.opener.location.href="${url}";
	}else{
		
		window.opener.location.href=location.reload();
	}
	
	window.close();
	
	

}else{
	
	if(url!=''){
		location.href=url;
		//window.opener.location.href="${url}";
	}else{
		
		location.reload();
	}
	

}

</script>
