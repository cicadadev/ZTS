<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>

<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
	<script type="text/javascript">			
		var casnote = function(){
			$("#CASNOTE").submit();
		}
	</script>
</head>
<body>
<button onclick="javascript:casnote()">입금</button>
	<form method="post" name="CASNOTE" id="CASNOTE" action="/api/oms/pg/casnote">
		LGD_RESPCODE	:<input type="text" name="LGD_RESPCODE" id="LGD_RESPCODE" value='0000' /><br/>
		LGD_RESPMSG		:<input type="text" name="LGD_RESPMSG" id="LGD_RESPMSG" value='${omsPaymentif.LGD_RESPMSG }' /><br/>
		LGD_MID			:<input type='text' name='LGD_MID' id='LGD_MID' value='${omsPaymentif.LGD_MID }'/><br/>
		LGD_OID			:<input type='text' name='LGD_OID' id='LGD_OID' value='${omsPaymentif.LGD_OID }'/><br/>
		LGD_AMOUNT		:<input type='text' name='LGD_AMOUNT' id='LGD_AMOUNT' value='${omsPaymentif.LGD_AMOUNT }'/><br/>
		LGD_TIMESTAMP		:<input type='text' name='LGD_TIMESTAMP' id='LGD_TIMESTAMP' value='${omsPaymentif.LGD_TIMESTAMP }'/><br/>
		LGD_HASHDATA	:<input type='text' name='LGD_HASHDATA' id='LGD_HASHDATA' value='${omsPaymentif.LGD_HASHDATA }'/><br/>
		LGD_CASFLAG		:<input type='text' name='LGD_CASFLAG' id='LGD_CASFLAG' value='I'/><br/>				
	</form>	
</body>
</html>