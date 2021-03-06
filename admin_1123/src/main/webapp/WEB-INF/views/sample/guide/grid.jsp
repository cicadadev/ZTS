<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<title>Admin</title>
<link rel="stylesheet" type="text/css" href="/resources/js/angular-1.5.4/ui-grid.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/common.css" />
</head>
<body>
<div>
<p>
<xmp>
	1. grid header 다국어
	
		- column 정의에 messageKey로 정의한다. (*.properties에 정의된 key)
		- grid init 필수 (commonService.initGrid($scope, "grid_order", Rest.context.path + "/api/oms/order/list","search");)
		
		ex) $scope.grid_order = {
								columnDefs: [
								             { field: 'orderId', 				width: '100', messageKey: "c.omsOrder.orderId"   },
								             { field: 'orderDt', 				width: '100', messageKey: "omsOrder.orderDt"  },
								             { field: 'orderSaleproductStateCd',width: '100', messageKey: "c.omsOrdersaleproduct.orderSaleproductStateCd"  },
								             { field: 'paymentTypeCd', 			width: '100', messageKey: "omsPayment.paymentStateCd"  }			             		             			            
								         ]			
								};		
</xmp>
</p>
</div>

</body>
</html>

