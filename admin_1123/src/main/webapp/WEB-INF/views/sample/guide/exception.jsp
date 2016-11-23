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

	0. ServiceException 을 제외하고 Service Layer 에서 throws Exception 명시 할 필요 없음
	
	1. ServiceException : 비즈니스 로직에서 고의로 에러 발생
	
		public TableSample getSample(String sampleId) throws ServiceException {
		
			if ("1".equals(sampleId)) {
				throw new ServiceException("sample.error"); // sample.error 는 메세지 properties 의 key
			}
			return (TableSample) queryService.selectOne("sample.getSample", sampleId);
		}	
		
	2. Controller 처리 : ServiceException일 경우만 catch해서 처리
	
		@RequestMapping(value = "/sample/{id}", method = RequestMethod.GET)
		public TableSample getSample(@PathVariable("id") String id) {
		
			TableSample sample = null;
			try {
				sample = sampleService.getSample(id);
		
			} catch (ServiceException se) {
				sample = new TableSample();
				sample.setSuccess(false);
				sample.setResultCode(se.getMessageCd());
				sample.setResultMessage(se.getMessage());
			}
			return sample;
		}	  	
		
	3. 화면 처리		
	
		Sample.getSample2({id:identity}, function(response){
			if(response.success){
			// 다음 로직 수행
		}else{
		 	//서버로부터 넘어온 메세지 출력
			alert(response.resultMessage);
		}
			
		});
         	
</xmp>
</p>
</div>

</body>
</html>

