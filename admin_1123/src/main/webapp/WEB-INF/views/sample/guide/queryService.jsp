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
	QueryService.java : DAO역활, Controller와 Service에서 모두 사용 가능
	
		public List<?> selectList(String queryId, Object object);
		=>  queryId 에 해당하는 list 쿼리 호출

		public PageList<?> selectListPages(String queryId, Object object);
		=>  queryId 에 해당하는 paging list 쿼리 호출
		
		public int selectCount(String queryId, Object object);
		=>  queryId 에 해당하는total count 조회 쿼리 호출
		
		public Object selectOne(String queryId, Object object);
		=>  queryId 에 해당하는 단건조회 쿼리 호출
		
		public int insert(String queryId, Object object);
		=>  queryId 에 해당하는 insert 쿼리 호출
		
		public int update(String queryId, Object object);
		=>  queryId 에 해당하는 update 쿼리 호출
		
		public int delete(String queryId, Object object);
		=>  queryId 에 해당하는 delete 쿼리 호출
		
		public Object selectOneTable(Object obj) throws Exception;
		=>  임의의 테이블의 단건 조회 쿼리 호출, 파라메터로 pk 세팅
		
			BaseCcsField selectCf1 = new BaseCcsField();
			selectCf1.setMsgCd("12");
			BaseCcsField baseCcsField = (BaseCcsField) queryService.selectOneTable(selectCf1);
		
		
		public String insertOneTable(Object obj) throws Exception;
		=>  임의의 테이블의 insert 쿼리 호출
		
			BasePmsCategory cate = new BasePmsCategory();
			cate.setStoreId(BaseConstants.DEFAULT_STORE_ID);
			cate.setCategoryId("00001");
			cate.setName("test category 1");
			cate.setLeafYn("Y");
			cate.setUseYn("Y");
			queryService.insertOneTable(cate);		
			
			
		public String updateOneTable(Object obj) throws Exception;
			
		=>  임의의 테이블의 update 쿼리 호출
		
			BaseMmsMember mb = new BaseMmsMember();
			mb.setStoreId("1001");//pk 필수
			mb.setMemId("eddie2");//pk 필수
			mb.setChannelId("1001");
			mb.setMemGradeCd("01");
			mb.setAddress1(null);			// case1 : update 안됨
			mb.setAddress2("");				// case2 :  null 로 update
			//mb.setAddress3("address3");	// case3 :  update 안됨
			mb.setBirth("19790722");		// case4 : 세팅값으로 update
			mb.setCloseDt(DateUtil.convertStringToTimestamp("2012-06-03", DateUtil.FORMAT_2));
			mb.setCountryNo("USA");
			mb.setRegDt(DateUtil.getCurrnetTimestamp()); // => 현재시간을 입력해야 할경우 was서버의 현재시간 입력... was와 db의 국가가 상이할 경우 문제 발생.. 추후 수정.
			queryService.updateOneTable(mb);
		
		public String deleteOneTable(Object obj) throws Exception;
		=>  임의의 테이블의 delete 쿼리 호출
	
</xmp>
</p>
</div>

</body>
</html>

