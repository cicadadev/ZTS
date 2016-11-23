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
	
	SYSTEM 구분 : ccs, dms, mms, oms, pms, sps, gsf
	업무 구분 : product, order, coupon, brand, plan, event...
	기능 구분(화면유형) : list, detail, insert, update, manager
	
	1. Controller
		- ViewController
		
			/{영역}/{업무}/{기능}
			/{영역}/{업무1}/{업무2}/{기능}
			/{영역}/{업무}/popup/{기능}
			/ccs/popup/{기능}		
			
			/pms/product/list (목록화면) => /pms/product/productList.jsp (하위업무+기능.jsp)
			/pms/product/unit/list (목록화면) => /pms/product/unit/unitList.jsp
			/pms/product/detail (상세화면) => /pms/product/productDetail.jsp
			/pms/product/manager (관리화면) => /pms/product/productManager.jsp
			/pms/product/popup/insert( 등록 팝업 화면)  => /pms/product/popup/productInsert.jsp
			/pms/product/popup/update( 수정 팝업 화면)  => /pms/product/popup/productUpdate.jsp
			/ccs/popup/postList (공통 팝업)  => /ccs/popup/postList.jsp
			
			*url과 jsp 경로는 동일하게
		    
		
		- RestController
		
			POST,GET /api/{영역}/{업무}
			POST,GET /api/{영역}/{업무}/{기능}
			PUT /api/{영역}/{업무}/{id}
			DELETE /api/{영역}/{업무}/{id}
			GET /api/{영역}/{업무}/{id}
			or
			POST,GET /api/{영역}/{업무1}/{업무2}/{기능}
			PUT /api/{업무1}/{업무2}/{id}
			DELETE /api/{영역}/{업무1}/{업무2}/{id}
			GET /api/{영역}/{업무1}/{업무2}/{id}
			
			3 depth
			POST,GET /api/pms/product
			POST,GET /api/pms/product/check
			PUT /api/pms/product/{id}
			DELETE /api/pms/product/{id}
			GET /api/pms/product/{id}
			4 depth
			POST,GET /api/pms/product/unit
			PUT /api/pms/product/unit/{id} 
			DELETE /api/pms/product/unit/{id} 
			GET /api/pms/product/unit/{id} 
	
		** URL은 모두 소문자
	
	2. Service method
		
		- 조회 prefix
		
			select*
			get*
			find*
			list*
			view*
			search*
			
		- CUD prefix (transaction REQUIRED)
		
			insert*
			create*
			update*
			delete*
			
		- CUD new transaction suffix (transaction REQUIRED_NEW)
		 
			insert*NewTx
			create*NewTx
			update*NewTx
			delete*NewTx						

</xmp>
</p>
</div>

</body>
</html>

