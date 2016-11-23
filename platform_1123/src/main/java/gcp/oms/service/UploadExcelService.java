package gcp.oms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsUploadconf;
import gcp.oms.model.excel.UploadExcelInfo;
import gcp.oms.model.excel.UploadExcelOrder;
import gcp.oms.model.search.OmsUploadexcelSearch;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.search.PmsProductSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class UploadExcelService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	//중국몰용 저장변수
	private final String	RECEIVE_TEL		= "01066602414";
	private final String	RECEIVE_CEL		= "01066602414";
	private final String	RECEIVE_NAME	= "김도형";
	private final String	RECEIVE_ZIPCODE	= "415816";
	private final String	RECEIVE_ADDR	= "경기도 김포시 고촌읍 전호리 748 소재 물류창고 지상 3층 B구간 현대로지스틱스㈜";

	/**
	 * @Method Name : readExcelFile
	 * @author : peter
	 * @date : 2016. 9. 29.
	 * @description : 실제 엑셀파일 읽어서 처리
	 *
	 * @param file
	 * @param ous
	 * @return
	 * @throws Exception
	 */
	public List<UploadExcelOrder> readExcelFile(File file, OmsUploadexcelSearch ous) throws Exception {
		//해당 사이트에 설정된 title 정보 조회
		OmsUploadconf confTitles = (OmsUploadconf) dao.selectOne("oms.uploadexcel.getSiteUploadconfInfo", ous);

		//업로드설정값이 있을 때만 처리
		if (null != confTitles && !"".equals(confTitles)) {
			//상점ID
			String storeId = ous.getStoreId();
			//사이트코드
			String siteId = confTitles.getSiteId();
			//사이트명
			String siteName = confTitles.getSiteName();
			//사이트 유형 코드
			String siteTypeCd = confTitles.getSiteTypeCd();
			//판매단가
			String salePrice = confTitles.getSalePrice();
			//제목행
			BigDecimal titleRow = confTitles.getTitleRow();
			//제목행 시작점: excel row가 0부터 시작하므로 -1
			int titleStart = titleRow.intValue() - 1;
			//데이터행
			BigDecimal dataRow = confTitles.getDataRow();
			//데이터행 시작점: excel row가 0부터 시작하므로 -1
			int dataStart = dataRow.intValue() - 1;

			//엑셀 제목열 설정
			List<UploadExcelInfo> excelList = initUploadExcelInfos(confTitles);

			//Excel 파일읽기
			Workbook wb = createWorkbook(file);
			//첫번째 sheet
			Sheet sheet = wb.getSheetAt(0);
			//수식처리자
			FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

			//제목열 설정: 사이트코드, 사이트명, 제목행을 위해 +3
			Row rowTitle = sheet.getRow(titleStart);
			//제목행의 열값이 일치하는 Cell을 기억시킴
			setExcelTitleIndex(excelList, rowTitle);

			//Row Data 처리
			List<UploadExcelOrder> orderList = new ArrayList<UploadExcelOrder>();
			logger.debug("총 row 수: " + sheet.getPhysicalNumberOfRows());
			int totalRowCount = sheet.getPhysicalNumberOfRows();
			//엑셀 행 간에 공백행이 있으면 총 Row 수를 강제로 늘려준다
			if (dataStart >= totalRowCount) {
				totalRowCount = totalRowCount + (dataStart - totalRowCount) + 1;
			}
			for (int i = dataStart; i < totalRowCount; i++) {
				Row row = sheet.getRow(i);
				logger.debug(i + "번째 Row: LastCell Index [" + row.getLastCellNum() + "]");
				//			logger.debug(row.getRowNum() + "번 Row -> first: " + row.getFirstCellNum() + ", last: " + row.getLastCellNum());
				if (row != null) {
					//				if (row.getRowNum() >= dataStart) {
					//title value 초기화
					initializeTitleValue(excelList);
					//title value 저장
					setExcelOrderData(evaluator, row, excelList);
					String checkId = getOrderMapping(excelList, "siteOrderId");
					logger.debug("orderId: " + checkId + ", bondYn: " + getOrderMapping(excelList, "bondYn"));
					//주문번호가 존재하는 경우만 처리
					if (null != checkId && !"".equals(checkId)) {
						UploadExcelOrder orderOne = makeExcelOrder(excelList, salePrice, siteTypeCd);

						orderOne.setStoreId(storeId);
						orderOne.setSiteId(siteId);
						orderOne.setSiteName(siteName);
						orderOne.setTitleRow(titleRow);
						orderOne.setDataRow(dataRow);
						//주문리스트에 추가
						orderList.add(orderOne);
					}
					//				}
				}
			}
			return orderList;
		} else {
			return null;
		}
	}

	/**
	 * @Method Name : insertExcelOrder
	 * @author : peter
	 * @date : 2016. 10. 5.
	 * @description : 엑셀주문 데이터 등록
	 *
	 * @param excelOrderList
	 * @return
	 * @throws Exception
	 */
	public String insertExcelOrder(List<UploadExcelOrder> excelOrderList) throws Exception {
		String result = "Y";
		String storeId = SessionUtil.getStoreId();
		String loginId = SessionUtil.getLoginId();

		//엑셀주문자료 사전 체크
		for (UploadExcelOrder orderOne : excelOrderList) {
			String rtnMsg = checkValidation(orderOne);
			logger.debug("rtnMsg: " + rtnMsg);
			if (null != rtnMsg && !"".equals(rtnMsg)) {
				result = "주문번호: " + orderOne.getSiteOrderId() + ", 에러내용: " + rtnMsg;
				break;
			}
		}

		//에러가 없는 경우만 주문데이터 생성 진행
		if ("Y".equals(result)) {
			//사이트유형코드 조회
			String siteTypeCd = (String) dao.selectOne("oms.uploadexcel.getSiteTypeCode", excelOrderList.get(0).getSiteId());

			//주문번호별 자료저장을 위한 변수
			Map<String, ArrayList<UploadExcelOrder>> orderGroupMap = new HashMap<String, ArrayList<UploadExcelOrder>>();
			//바로 전 정보 저장변수: 중복체크용
			String oldSiteOrderId = ""; //바로 전 주문번호
			for (UploadExcelOrder orderOne : excelOrderList) {
				logger.debug("orderId: " + orderOne.getSiteOrderId());

				//주문번호에서 "+" 제거
				orderOne.setSiteOrderId(orderOne.getSiteOrderId().replace("+", ""));
				String newSiteOrderId = orderOne.getSiteOrderId();

				orderOne.setStoreId(storeId);
				orderOne.setLoginId(loginId);
				orderOne.setSiteTypeCd(siteTypeCd);

				//새로운 주문번호인 경우
				if (!newSiteOrderId.equals(oldSiteOrderId)) {
					//새로운 주문번호를 중복체크용으로 old 변수에 저장
					oldSiteOrderId = newSiteOrderId;

					//주문번호별 Terminal 정보그룹 저장
					ArrayList<UploadExcelOrder> xlsGroupList = new ArrayList<UploadExcelOrder>();
					xlsGroupList.add(orderOne);
					orderGroupMap.put(newSiteOrderId, xlsGroupList);
				}
				//동일 주문번호인 경우
				else {
					//주문번호별 Terminal 정보그룹 저장
					ArrayList<UploadExcelOrder> xlsElements = orderGroupMap.get(oldSiteOrderId);
					xlsElements.add(orderOne);
					orderGroupMap.put(oldSiteOrderId, xlsElements);
				}
			}

			//주문번호별 BO 주문데이터 생성
			for (String orderId : orderGroupMap.keySet()) {
				//현 주문번호에 속한 Order List
				List<UploadExcelOrder> orderGroup = orderGroupMap.get(orderId);
				try {
					insertExcelOrderDataNewTx(orderId, orderGroup, storeId, loginId);
				} catch (Exception e) {
					logger.error("ReceiveOrder Exception: Order ID [" + orderId + "]");
					result = "주문데이터 생성오류입니다. 주문번호: " + orderId;
					break;
				}
			}
		}

		return result;
	}

	/**
	 * @Method Name : insertOrderData
	 * @author : peter
	 * @date : 2016. 10. 6.
	 * @description : 주문데이터 실제 등록 처리
	 *
	 * @param boOrderId
	 * @param xlsOrderList
	 * @throws Exception
	 */
	private void insertExcelOrderDataNewTx(String orderId, List<UploadExcelOrder> xlsOrderList, String storeId, String loginId)
			throws Exception {
		logger.debug("insertOrderData..Order ID [" + orderId + "] Count: " + xlsOrderList.size());

		//주문table 등록
		OmsOrder order = setOmsOrder(xlsOrderList.get(0));
		order.setStoreId(storeId);
		order.setInsId(loginId);
		order.setUpdId(loginId);
//		dao.insertOne(order);
		dao.insert("common.insert.OmsOrder", order);
		//=> 신규 주문번호
		String boOrderId = order.getOrderId();
		logger.debug("New BO Order ID: " + boOrderId);

		//결제table 등록
		OmsPayment payment = setOmsPayment(xlsOrderList.get(0));
		payment.setOrderId(boOrderId);
		payment.setInsId(loginId);
		payment.setUpdId(loginId);
//		dao.insertOne(payment);
		dao.insert("common.insert.OmsPayment", payment);

		String oldReceiveName = ""; //이전 수취자명
		String oldReceiveAddr = ""; //이전 수취자 주소
		BigDecimal boDeliveryAddressNo = new BigDecimal(0); //BO 배송지번호

		//주문번호별 주문총결제금액의 합계
		int totalOrderAmt = 0;
		//주문별,배송지번호별 배송정책번호 리스트
		List<BigDecimal> policyList = null;
		for (UploadExcelOrder xlsOrder : xlsOrderList) {
			//주문총결제금액 누적
			BigDecimal sumPrice = xlsOrder.getSalePrice().multiply(xlsOrder.getOrderQty());
			totalOrderAmt += sumPrice.intValue();

			//배송지table 등록: 동일배송지가 아닌 경우만 등록(수취인명과 수취주소가 동일하면 동일배송지로 인식)
			if (!xlsOrder.getName().trim().equals(oldReceiveName) || !xlsOrder.getAddress1().trim().equals(oldReceiveAddr)) {
				//새로운 수취자명과 주소를 중복체크용으로 변수에 저장
				oldReceiveName = xlsOrder.getName().trim();
				oldReceiveAddr = xlsOrder.getAddress1().trim();

				//배송지table 입력
				OmsDeliveryaddress address = setDeliveryAddress(xlsOrder);
				address.setOrderId(boOrderId);
				dao.insertOneTable(address);
				//신규 배송지 번호
				boDeliveryAddressNo = address.getDeliveryAddressNo();
				//배송정책번호 리스트 재설정
				policyList = new ArrayList<BigDecimal>();
			}

			//단품ID 추출
			String saleproductId = getSaleproductId(xlsOrder);
			logger.debug("saleproductId: " + saleproductId);
			//상품,단품정보 조회
			PmsProduct product = getProductInfo(xlsOrder.getStoreId(), saleproductId);
			logger.debug("Product ID: " + product.getProductId() + ", Saleproduct ID: "
					+ product.getPmsSaleproduct().getSaleproductId());

			//일반,세트,사은품 상품정보 저장
			List<OmsOrderproduct> orderProductList = setOrderProduct(xlsOrder, product);

			//주문상품 처리
			for (OmsOrderproduct oopOne : orderProductList) {
				//배송정책table 등록: 기등록 아닌 경우
				if (isDeliveryPolicyNotExist(policyList, oopOne.getDeliveryPolicyNo())) {
					//배송정책번호 리스트 추가
					policyList.add(oopOne.getDeliveryPolicyNo());

					//배송정책 조회
					OmsDelivery delivery = setDelivery(oopOne.getDeliveryPolicyNo());
					//주문번호
					delivery.setOrderId(boOrderId);
					//배송지번호
					delivery.setDeliveryAddressNo(boDeliveryAddressNo);
					dao.insertOneTable(delivery);
				}

				//주문상품table 등록
				oopOne.setStoreId(storeId);
				oopOne.setOrderId(boOrderId);
				//주문상품일련번호
				oopOne.setOrderProductNo((BigDecimal) dao.selectOne("external.receiveorder.getNewOrderProductNo", boOrderId));
				oopOne.setInsId(loginId);
				oopOne.setUpdId(loginId);
				oopOne.setDeliveryAddressNo(boDeliveryAddressNo);
				dao.insertOneTable(oopOne);
			}
		}
		//주문금액, 결제금액 update: OmsOrder, OmsPayment
		Map<String, Object> updMap = new HashMap<String, Object>();
		updMap.put("orderId", boOrderId);
		updMap.put("orderAmt", totalOrderAmt);
		//=> OmsOrder
		dao.update("oms.uploadexcel.updateOrderAmtInOmsOrder", updMap);
		//=> OmsPayment
		dao.update("oms.uploadexcel.updatePaymentAmtInOmsPayment", updMap);
	}

	/**
	 * @Method Name : isVersion2003
	 * @author : peter
	 * @date : 2016. 9. 29.
	 * @description : 엑셀파일이 2003년 이전 버젼인지 확인
	 *
	 * @param file
	 * @return
	 */
	private boolean isVersion2003(File file) {
		logger.debug("FileName: " + file.getName());
		return file.getName().endsWith(".xls");
	}

	/**
	 * @Method Name : createWorkbook
	 * @author : peter
	 * @date : 2016. 9. 29.
	 * @description : 엑셀파일 버젼에 따른 설정
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private Workbook createWorkbook(File file) throws IOException {
		if (isVersion2003(file)) {
			return new HSSFWorkbook(new FileInputStream(file));
		} else { // 2007+
			return new XSSFWorkbook(new FileInputStream(file));
		}
	}

	/**
	 * @Method Name : initUploadExcelInfos
	 * @author : peter
	 * @date : 2016. 10. 14.
	 * @description : 엑셀 업로드 Title 초기화
	 *
	 * @param ous
	 * @return
	 */
	private List<UploadExcelInfo> initUploadExcelInfos(OmsUploadconf titles) {
		List<UploadExcelInfo> excelList = new ArrayList<UploadExcelInfo>();
		UploadExcelInfo excel = null;

		//외부몰 주문번호: 필수(자사몰은 무의미한 일련번호)
		excel = new UploadExcelInfo();
		excel.setConfName("siteOrderId");
		excel.setTitleName(titles.getSiteOrderId());
		excel.setTitleValue("");
		excel.setIndex(-1);
		excelList.add(excel);
		//단품코드1: 필수
		excel = new UploadExcelInfo();
		excel.setConfName("saleproductId1");
		excel.setTitleName(titles.getSaleproductId1());
		excel.setTitleValue("");
		excel.setIndex(-1);
		excelList.add(excel);
		//수량: 필수
		excel = new UploadExcelInfo();
		excel.setConfName("orderQty");
		excel.setTitleName(titles.getOrderQty());
		excel.setTitleValue("");
		excel.setIndex(-1);
		excelList.add(excel);
		//수취인명: 필수
		excel = new UploadExcelInfo();
		excel.setConfName("name");
		excel.setTitleName(titles.getName());
		excel.setTitleValue("");
		excel.setIndex(-1);
		excelList.add(excel);
		//주소1: 필수
		excel = new UploadExcelInfo();
		excel.setConfName("address1");
		excel.setTitleName(titles.getAddress1());
		excel.setTitleValue("");
		excel.setIndex(-1);
		excelList.add(excel);
		//우편번호: 필수, 추가 존재분 처리
		if (titles.getZipCd().contains("&")) {
			String[] zipCd = titles.getZipCd().split("&");
			for (int i = 0; i < zipCd.length; i++) {
				excel = new UploadExcelInfo();
				excel.setConfName("zipCd");
				excel.setTitleName(zipCd[i]);
				excel.setTitleValue("");
				excel.setIndex(-1);
				excelList.add(excel);
			}
		} else {
			excel = new UploadExcelInfo();
			excel.setConfName("zipCd");
			excel.setTitleName(titles.getZipCd());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//전화번호: 필수, 추가 존재분 처리
		if (titles.getPhone1().contains("&")) {
			String[] phone1 = titles.getPhone1().split("&");
			for (int i = 0; i < phone1.length; i++) {
				excel = new UploadExcelInfo();
				excel.setConfName("phone1");
				excel.setTitleName(phone1[i]);
				excel.setTitleValue("");
				excel.setIndex(-1);
				excelList.add(excel);
			}
		} else {
			excel = new UploadExcelInfo();
			excel.setConfName("phone1");
			excel.setTitleName(titles.getPhone1());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//휴대폰번호 추가 존재분 처리
		if (null != titles.getPhone2() && !"".equals(titles.getPhone2())) {
			if (titles.getPhone2().contains("&")) {
				String[] phone2 = titles.getPhone2().split("&");
				for (int i = 0; i < phone2.length; i++) {
					excel = new UploadExcelInfo();
					excel.setConfName("phone2");
					excel.setTitleName(phone2[i]);
					excel.setTitleValue("");
					excel.setIndex(-1);
					excelList.add(excel);
				}
			} else {
				excel = new UploadExcelInfo();
				excel.setConfName("phone2");
				excel.setTitleName(titles.getPhone2());
				excel.setTitleValue("");
				excel.setIndex(-1);
				excelList.add(excel);
			}
		}
		//판매단가 추가 존재분 처리: 사칙연산자 하나만 허용
		if (null != titles.getSalePrice() && !"".equals(titles.getSalePrice())) {
			String regex = "";
			if (titles.getSalePrice().contains("+")) {
				regex = "[+]";
			} else if (titles.getSalePrice().contains("-")) {
				regex = "-";
			} else if (titles.getSalePrice().contains("*")) {
				regex = "[*]";
			} else if (titles.getSalePrice().contains("/")) {
				regex = "\\/";
			}
			if (!"".equals(regex)) {
				String[] salePrice = titles.getSalePrice().split(regex);
				for (int i = 0; i < salePrice.length; i++) {
					excel = new UploadExcelInfo();
					excel.setConfName("salePrice" + i);
					excel.setTitleName(salePrice[i]);
					excel.setTitleValue("");
					excel.setIndex(-1);
					excelList.add(excel);
				}
			} else {
				excel = new UploadExcelInfo();
				excel.setConfName("salePrice");
				excel.setTitleName(titles.getSalePrice());
				excel.setTitleValue("");
				excel.setIndex(-1);
				excelList.add(excel);
			}
		}
		//단품코드2
		if (null != titles.getSaleproductId2() && !"".equals(titles.getSaleproductId2())) {
			excel = new UploadExcelInfo();
			excel.setConfName("saleproductId2");
			excel.setTitleName(titles.getSaleproductId2());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//단품코드3
		if (null != titles.getSaleproductId3() && !"".equals(titles.getSaleproductId3())) {
			excel = new UploadExcelInfo();
			excel.setConfName("saleproductId3");
			excel.setTitleName(titles.getSaleproductId3());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//단품코드4
		if (null != titles.getSaleproductId4() && !"".equals(titles.getSaleproductId4())) {
			excel = new UploadExcelInfo();
			excel.setConfName("saleproductId4");
			excel.setTitleName(titles.getSaleproductId4());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//단품코드5
		if (null != titles.getSaleproductId5() && !"".equals(titles.getSaleproductId5())) {
			excel = new UploadExcelInfo();
			excel.setConfName("saleproductId5");
			excel.setTitleName(titles.getSaleproductId5());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//주소2
		if (null != titles.getAddress2() && !"".equals(titles.getAddress2())) {
			excel = new UploadExcelInfo();
			excel.setConfName("address2");
			excel.setTitleName(titles.getAddress2());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//배송메세지
		if (null != titles.getNote() && !"".equals(titles.getNote())) {
			excel = new UploadExcelInfo();
			excel.setConfName("note");
			excel.setTitleName(titles.getNote());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//통화코드
		if (null != titles.getCurrencyCd() && !"".equals(titles.getCurrencyCd())) {
			excel = new UploadExcelInfo();
			excel.setConfName("currencyCd");
			excel.setTitleName(titles.getCurrencyCd());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//외화금액
		if (null != titles.getCurrencyPrice() && !"".equals(titles.getCurrencyPrice())) {
			excel = new UploadExcelInfo();
			excel.setConfName("currencyPrice");
			excel.setTitleName(titles.getCurrencyPrice());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//LP번호
		if (null != titles.getLpNo() && !"".equals(titles.getLpNo())) {
			excel = new UploadExcelInfo();
			excel.setConfName("lpNo");
			excel.setTitleName(titles.getLpNo());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}
		//보세여부
		if (null != titles.getLocalDelivery() && !"".equals(titles.getLocalDelivery())) {
			excel = new UploadExcelInfo();
			excel.setConfName("bondYn");
			excel.setTitleName(titles.getLocalDelivery());
			excel.setTitleValue("");
			excel.setIndex(-1);
			excelList.add(excel);
		}

		return excelList;
	}

	/**
	 * @Method Name : setExcelTitleIndex
	 * @author : peter
	 * @date : 2016. 9. 29.
	 * @description : 제목열 색인 설정
	 *
	 * @param confTitles
	 * @param rowTitle
	 * @return
	 */
	private void setExcelTitleIndex(List<UploadExcelInfo> excelList, Row rowTitle) {
		for (int tdx = 0; tdx < rowTitle.getPhysicalNumberOfCells(); tdx++) {
			if (CommonUtil.isNotEmpty(rowTitle.getCell(tdx))) {
				String cellData = rowTitle.getCell(tdx).toString().trim().replace("\n", "");
				logger.debug(tdx + "번째 title: " + cellData);
				for (UploadExcelInfo infoOne : excelList) {
					if (cellData.equals(infoOne.getTitleName())) {
						infoOne.setIndex(tdx);
//						break;
					}
				}
			}
		}

		//로그 출력
		for (UploadExcelInfo one : excelList) {
			logger.debug("confName: " + one.getConfName() + ", titleName: " + one.getTitleName() + ", index: " + one.getIndex()
					+ "\n");
		}
	}

	/**
	 * @Method Name : initializeTitleValue
	 * @author : peter
	 * @date : 2016. 10. 16.
	 * @description : 엑셀 타이틀에 해당하는 값 초기화
	 *
	 * @param infos
	 */
	private void initializeTitleValue(List<UploadExcelInfo> infos) {
		for (UploadExcelInfo info : infos) {
			info.setTitleValue("");
		}
	}

	/**
	 * @Method Name : setExcelOrderData
	 * @author : peter
	 * @date : 2016. 9. 29.
	 * @description : 실 주문데이터 모델에 저장
	 *
	 * @param rowData
	 * @param excIdx
	 * @return
	 */
	private void setExcelOrderData(FormulaEvaluator evaluator, Row rowData, List<UploadExcelInfo> infos) {
		DecimalFormat df = new DecimalFormat("#.##");

		for (int index = 0; index < rowData.getLastCellNum(); index++) {
			Cell cell = rowData.getCell(index);
			if (CommonUtil.isNotEmpty(cell) && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				String sCellData = "";
				if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
					sCellData = cell.getStringCellValue();
				} else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
//					String temp = Integer.toString((int) cell.getNumericCellValue());
//					sCellData = temp.substring(0, temp.indexOf("."));
					sCellData = df.format((Double) cell.getNumericCellValue());
				} else if (Cell.CELL_TYPE_FORMULA == cell.getCellType()) {
					if (!(cell.toString() == "")) {
						if (evaluator.evaluateFormulaCell(cell) == HSSFCell.CELL_TYPE_NUMERIC) {
//							sCellData = Double.toString((Double) cell.getNumericCellValue());
//							double fddata = cell.getNumericCellValue();
//							sCellData = df.format(fddata);
							sCellData = df.format((Double) cell.getNumericCellValue());
						} else if (evaluator.evaluateFormulaCell(cell) == HSSFCell.CELL_TYPE_STRING) {
							sCellData = cell.getStringCellValue();
						} else if (evaluator.evaluateFormulaCell(cell) == HSSFCell.CELL_TYPE_BOOLEAN) {
							boolean fbdata = cell.getBooleanCellValue();
							sCellData = String.valueOf(fbdata);
						}
					}
				} else {
					logger.debug("Unknown => index: " + index + ", type: " + cell.getCellType() + ", data: " + sCellData);
					continue;
				}
				logger.debug("index: " + index + ", type: " + cell.getCellType() + ", data: " + sCellData);

				for (UploadExcelInfo info : infos) {
					if (index == info.getIndex()) {
						info.setTitleValue(sCellData);
//						break;
					}
				}
			}
		}
	}

	/**
	 * @Method Name : makeExcelOrder
	 * @author : peter
	 * @date : 2016. 10. 16.
	 * @description : 엑셀 주문모델에 값 저장
	 *
	 * @param infos
	 * @param confSalePrice
	 * @param siteTypeCd
	 * @return
	 */
	private UploadExcelOrder makeExcelOrder(List<UploadExcelInfo> infos, String confSalePrice, String siteTypeCd) {
		UploadExcelOrder orderOne = new UploadExcelOrder();

		//외부몰 주문번호
		orderOne.setSiteOrderId(getOrderMapping(infos, "siteOrderId"));
		//단품코드1
		orderOne.setSaleproductId1(getOrderMapping(infos, "saleproductId1"));
		//단품코드2
		orderOne.setSaleproductId2(getOrderMapping(infos, "saleproductId2"));
		//단품코드3
		orderOne.setSaleproductId3(getOrderMapping(infos, "saleproductId3"));
		//단품코드4
		orderOne.setSaleproductId4(getOrderMapping(infos, "saleproductId4"));
		//단품코드5
		orderOne.setSaleproductId5(getOrderMapping(infos, "saleproductId5"));
		//수량
		orderOne.setOrderQty(new BigDecimal(getOrderMapping(infos, "orderQty")));
		//주소2
		orderOne.setAddress2(getOrderMapping(infos, "address2"));
		//배송메세지
		orderOne.setNote(getOrderMapping(infos, "note"));
		//사이트유형코드
		orderOne.setSiteTypeCd(siteTypeCd);

		//중국몰의 경우 수취인정보 일괄 처리
		if ("SITE_TYPE_CD.CHINA".equals(siteTypeCd)) {
			//수취인명
			orderOne.setName(RECEIVE_NAME);
			//우편번호
			orderOne.setZipCd(RECEIVE_ZIPCODE);
			//전화번호
			orderOne.setPhone1(RECEIVE_TEL);
			//휴대폰번호
			orderOne.setPhone2(RECEIVE_CEL);
			//주소1
			orderOne.setAddress1(RECEIVE_ADDR);
			//통화코드
			orderOne.setCurrencyCd(getOrderMapping(infos, "currencyCd"));
			//외화금액
			orderOne.setCurrencyPrice(new BigDecimal(getOrderMapping(infos, "currencyPrice")));
			//LP No
			orderOne.setLpNo(getOrderMapping(infos, "lpNo"));
			//보세여부
			orderOne.setLocalDeliveryYn(getOrderMapping(infos, "bondYn"));
		} else {
			//수취인명
			orderOne.setName(getOrderMapping(infos, "name"));
			//우편번호
			orderOne.setZipCd(getOrderMapping(infos, "zipCd"));
			//전화번호
			orderOne.setPhone1(getOrderMapping(infos, "phone1"));
			//휴대폰번호
			orderOne.setPhone2(getOrderMapping(infos, "phone2"));
			//주소1
			orderOne.setAddress1(getOrderMapping(infos, "address1"));
		}
		//판매단가 연산
		if (null != confSalePrice && !"".equals(confSalePrice)) {
			String mathCode = "";
			if (confSalePrice.contains("+")) {
				mathCode = "+";
			} else if (confSalePrice.contains("-")) {
				mathCode = "-";
			} else if (confSalePrice.contains("*")) {
				mathCode = "*";
			} else if (confSalePrice.contains("/")) {
				mathCode = "/";
			}
			orderOne.setSalePrice(getOrderSalePrice(infos, mathCode));
		}

		return orderOne;
	}

	/**
	 * @Method Name : getOrderMapping
	 * @author : peter
	 * @date : 2016. 10. 16.
	 * @description : 엑셀 항목들 값 추출
	 *
	 * @param infos
	 * @param strKey
	 * @return
	 */
	private String getOrderMapping(List<UploadExcelInfo> infos, String strKey) {
		StringBuffer sbValue = new StringBuffer();

		for (UploadExcelInfo infoOne : infos) {
			if (strKey.equals(infoOne.getConfName())) {
				sbValue.append(infoOne.getTitleValue());
			}
		}

		return sbValue.toString();
	}

	/**
	 * @Method Name : getOrderSalePrice
	 * @author : peter
	 * @date : 2016. 10. 16.
	 * @description : 엑셀 판매단가만 추출
	 *
	 * @param infos
	 * @param mathCode
	 * @return
	 */
	private BigDecimal getOrderSalePrice(List<UploadExcelInfo> infos, String mathCode) {
		Double[] salePrice = new Double[2];
		double rtnValue = 0;

		//판매단가에 사칙연산이 없을 때
		if ("".equals(mathCode)) {
			for (UploadExcelInfo infoOne : infos) {
				if ("salePrice".equals(infoOne.getConfName())) {
					String temp = infoOne.getTitleValue();
					if (null != temp && !"".equals(temp)) {
						rtnValue = Double.parseDouble(temp);
					}
					break;
				}
			}
		}
		//판매단가에 사칙연산이 있을 때
		else {
			for (UploadExcelInfo infoOne : infos) {
				if (infoOne.getConfName().contains("salePrice0")) {
					salePrice[0] = Double.parseDouble(infoOne.getTitleValue());
				}
				if (infoOne.getConfName().contains("salePrice1")) {
					salePrice[1] = Double.parseDouble(infoOne.getTitleValue());
				}
			}
			if ("+".equals(mathCode)) {
				rtnValue = salePrice[0] + salePrice[1];
			} else if ("-".equals(mathCode)) {
				rtnValue = salePrice[0] - salePrice[1];
			} else if ("*".equals(mathCode)) {
				rtnValue = salePrice[0] * salePrice[1];
			} else if ("/".equals(mathCode) && salePrice[1] > 0) {
				rtnValue = Math.round(salePrice[0] / salePrice[1]);
			}
//		if ("+".equals(mathCode)) {
//		orderOne.setSalePrice(orderOne.getSalePrice().add(orderOne.getSalePriceEtc1()));
//	} else if ("-".equals(mathCode)) {
//		orderOne.setSalePrice(orderOne.getSalePrice().subtract(orderOne.getSalePriceEtc1()));
//	} else if ("*".equals(mathCode)) {
//		orderOne.setSalePrice(orderOne.getSalePrice().multiply(orderOne.getSalePriceEtc1()));
//	} else if ("/".equals(mathCode) && (orderOne.getSalePriceEtc1().compareTo(BigDecimal.ZERO) > 0)) { //나누는 값이 0보다 클때만
//		orderOne.setSalePrice(orderOne.getSalePrice().divide(orderOne.getSalePriceEtc1(), -1, BigDecimal.ROUND_DOWN));
//	}
		}

		return new BigDecimal(rtnValue);
	}

	/**
	 * @Method Name : getSaleproductId
	 * @author : peter
	 * @date : 2016. 10. 16.
	 * @description : 단품정보 조회
	 *
	 * @param excel
	 * @return
	 */
	private String getSaleproductId(UploadExcelOrder excel) {
		//단품ID setting
		String[] saleproductIds = new String[5];
		saleproductIds[0] = excel.getSaleproductId1();
		saleproductIds[1] = excel.getSaleproductId2();
		saleproductIds[2] = excel.getSaleproductId3();
		saleproductIds[3] = excel.getSaleproductId4();
		saleproductIds[4] = excel.getSaleproductId5();

		//단품ID 추출
		String saleproductId = "";
		for (String srchId : saleproductIds) {
			if (!StringUtils.isBlank(srchId)) {
				if (StringUtils.isNumeric(srchId)) {
					if ((int) dao.selectOne("oms.uploadexcel.getSaleproductExistCount", srchId) > 0) {
						saleproductId = srchId;
						break;
					}
				}
			}
		}

		return saleproductId;
	}

	/**
	 * @Method Name : setOmsOrder
	 * @author : peter
	 * @date : 2016. 10. 5.
	 * @description : 주문마스터 정보 설정
	 *
	 * @param orm
	 * @return
	 */
	private OmsOrder setOmsOrder(UploadExcelOrder excel) {
		OmsOrder order = new OmsOrder();

		//주문번호
		order.setOrderId((String) dao.selectOne("oms.order.getNewOrderId", null));
		//사이트ID
		order.setSiteId(excel.getSiteId());
		//사이트유형코드
		order.setSiteTypeCd(excel.getSiteTypeCd());
		if ("SITE_TYPE_CD.OWN".equals(excel.getSiteTypeCd())) { //자사몰인 경우
			//외부몰주문ID: 공백
			order.setSiteOrderId("");
			//주문유형코드: 일반 주문
			order.setOrderTypeCd("ORDER_TYPE_CD.GENERAL");
		} else { //외부몰, 중국몰인 경우
			//외부몰주문ID
			order.setSiteOrderId(excel.getSiteOrderId());
			//주문유형코드: 외부몰 주문
			order.setOrderTypeCd("ORDER_TYPE_CD.EXTERNAL");
		}
		//디바이스유형코드: PC
		order.setDeviceTypeCd("DEVICE_TYPE_CD.PC");
		//주문상태코드: 신규주문(001), 주문확인(002), null(TMALL, 0to7_CN, 0to7)만 존재 => 주문완료로 통일
		order.setOrderStateCd("ORDER_STATE_CD.COMPLETE");
		//배송상태코드: 출고지시 대기
		order.setOrderDeliveryStateCd("ORDER_DELIVERY_STATE_CD.READY");
		//주문자명: 수취인명 입력
		order.setName1(excel.getName());
		//주문자전화번호: 수취인 전화번호 입력
		order.setPhone1(excel.getPhone1());
		//주문자핸드폰번호: 수취인 휴대폰번호 입력
		order.setPhone2(excel.getPhone2());
		//주문금액: 처음은 '0'으로 하고 추후에 update
		order.setOrderAmt(BigDecimal.ZERO);
		//할인금액
		order.setDcAmt(BigDecimal.ZERO);
		//결제금액: 처음은 '0'으로 하고 추후에 orderAmt와 동일하게update
		order.setPaymentAmt(BigDecimal.ZERO);
		//주문일시
		order.setOrderDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));
		//주문자우편번호: 수취인 우편번호 입력
		order.setZipCd(excel.getZipCd());
		//주문자주소: 수취인 주소 입력
		order.setAddress1(excel.getAddress1());

		return order;
	}

	/**
	 * @Method Name : setOmsPayment
	 * @author : peter
	 * @date : 2016. 10. 6.
	 * @description : 결제정보 설정
	 *
	 * @param orm
	 * @return
	 */
	private OmsPayment setOmsPayment(UploadExcelOrder excel) {
		OmsPayment payment = new OmsPayment();

		//결제번호
		payment.setPaymentNo((BigDecimal) dao.selectOne("oms.payment.getNewPaymentNo", null));
		//결제수단코드: 무조건 외상매출금(자사든 외부몰이든 구분 없음)
		payment.setPaymentMethodCd("PAYMENT_METHOD_CD.CREDITSALE");
		//결제유형코드: 결제
		payment.setPaymentTypeCd("PAYMENT_TYPE_CD.PAYMENT");
		//주결제수단여부
		payment.setMajorPaymentYn("Y");
		//결제상태코드: 결제완료
		payment.setPaymentStateCd("PAYMENT_STATE_CD.PAYMENT");
		//무이자여부
		payment.setInterestFreeYn("N");
		//애스크로여부
		payment.setEscrowYn("N");
		//애스크로 연동여부
		payment.setEscrowIfYn("N");
		//부분취소가능여부
		payment.setPartialCancelYn("N");
		//결제금액: 처음은 '0'으로 하고 추후에 update
		payment.setPaymentAmt(excel.getSalePrice().multiply(excel.getOrderQty()));
		//결제수수료
		payment.setPaymentFee(BigDecimal.ZERO);
		//결제일시: 무조건 오늘 날짜
		payment.setPaymentDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));

		return payment;
	}

	/**
	 * @Method Name : setOrderProduct
	 * @author : peter
	 * @date : 2016. 10. 6.
	 * @description : 주문상품정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	private List<OmsOrderproduct> setOrderProduct(UploadExcelOrder excel, PmsProduct product) {
		List<OmsOrderproduct> orderProductList = null;

		if ("PRODUCT_TYPE_CD.GENERAL".equals(product.getProductTypeCd())) { //일반상품
			orderProductList = setOrderProductGeneral(excel, product);
			logger.debug("[OrderProduct-General] Saleproduct ID: " + orderProductList.get(0).getSaleproductId());
		} else if ("PRODUCT_TYPE_CD.SET".equals(product.getProductTypeCd())) { //세트상품
			orderProductList = setOrderProductSet(excel, product);
			logger.debug("[OrderProduct-Set] Saleproduct ID: " + orderProductList.get(0).getSaleproductId());
		} else if ("PRODUCT_TYPE_CD.PRESENT".equals(product.getProductTypeCd())) { //사은품
			orderProductList = setOrderProductPresent(excel, product);
			logger.debug("[OrderProduct-Present] Saleproduct ID: " + orderProductList.get(0).getSaleproductId());
		}

		return orderProductList;
	}

	/**
	 * @Method Name : setOrderProductGeneral
	 * @author : peter
	 * @date : 2016. 10. 6.
	 * @description : 일반 주문상품정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	private List<OmsOrderproduct> setOrderProductGeneral(UploadExcelOrder excel, PmsProduct product) {
		List<OmsOrderproduct> orderGeneralProductList = new ArrayList<OmsOrderproduct>();

		//기본항목 설정
		OmsOrderproduct orderGeneralProduct = setOrderProductBase(excel, product);
		//주문상품유형코드
		orderGeneralProduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.GENERAL");

		//리스트에 추가
		orderGeneralProductList.add(orderGeneralProduct);

		return orderGeneralProductList;
	}

	/**
	 * @Method Name : setOrderProductSet
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 세트 주문상품정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<OmsOrderproduct> setOrderProductSet(UploadExcelOrder excel, PmsProduct product) {
		List<OmsOrderproduct> orderSetProductList = new ArrayList<OmsOrderproduct>();

		//공통사용변수 설정
		String storeId = excel.getStoreId();
//		BigDecimal setDeliveryPolicyNo = product.getDeliveryPolicyNo();

		//### 세트 자체에 대한 주문상품 설정
		//기본항목 설정
		OmsOrderproduct orderSetProduct = setOrderProductBase(excel, product);

		//주문상품유형코드
		orderSetProduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.SET");

		//주문상품리스트 추가
		orderSetProductList.add(orderSetProduct);

		//### 세트 구성상품별 주문상품 설정
		//세트상품테이블 조회
		Map<String, String> srchMap = new HashMap<String, String>();
		srchMap.put("storeId", storeId);
		srchMap.put("productId", product.getProductId());
		List<PmsSetproduct> subProductList = (List<PmsSetproduct>) dao.selectList("oms.uploadexcel.getSubProductList",
				srchMap);
		//구성상품의 정상가 총액 산정
		double totalListPrice = 0;
		for (PmsSetproduct setOne : subProductList) {
			totalListPrice = totalListPrice + setOne.getSumListPrice().doubleValue();
		}

		//구성상품별 주문상품 설정
//		int setCnt = setProductList.size();
		BigDecimal saleCost = excel.getSalePrice(); //판매단가
		BigDecimal paymentAmt = excel.getSalePrice().multiply(excel.getOrderQty()); //총결제금액
		BigDecimal stackPaymentAmt = new BigDecimal(0); //구성상품의 누적결제금액
		BigDecimal restPaymentAmt = new BigDecimal(0); //자투리 금액

		BigDecimal stackCurrencyPrice = new BigDecimal(0); //구성상품의 외화누적금액
		BigDecimal restCurrencyPrice = new BigDecimal(0); //외화금액의 자투리 금액
		BigDecimal currencyPrice = excel.getCurrencyPrice(); //외화단가
		for (int idx = 0, setCnt = subProductList.size(); idx < setCnt; idx++) {
			PmsSetproduct setOne = subProductList.get(idx);
			BigDecimal subSetQty = setOne.getQty();
			//세트구성상품의 상품,단품정보 조회
			PmsProduct subProduct = getProductInfo(storeId, setOne.getSaleproductId());

			//기본항목 설정
			OmsOrderproduct orderItemProduct = setOrderProductBase(excel, subProduct);

			//주문상품유형코드: 세트 구성상품
			orderItemProduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.SUB");
			//세트구성수량: 재설정
			orderItemProduct.setSetQty(subSetQty);
			//주문수량: 재설정(세트구성수량 * 세트상품주문수량)
			BigDecimal totalOrderQty = subSetQty.multiply(excel.getOrderQty());
			orderItemProduct.setOrderQty(totalOrderQty);
			//판매가: 재설정(세트상품의 개당 판매가를 구성상품의 정상가*세트구성수량의 비중으로 금액분배하고 끝자리 조정)
			//총판매가_개당: 재설정(세트상품의 개당 판매가를 구성상품의 정상가*세트구성수량의 비중으로 금액분배하고 끝자리 조정)
			//=> 현 구성상품의 비율가
			double ratePrice = (setOne.getSumListPrice().doubleValue() / totalListPrice) * saleCost.doubleValue();
			//=> 현 구성상품의 비율가에 따른 단가(원단위 버림)
			BigDecimal rateUnitPrice = new BigDecimal(Math.floor(ratePrice / subSetQty.doubleValue()));
			rateUnitPrice = rateUnitPrice.setScale(-1, BigDecimal.ROUND_HALF_UP);
			orderItemProduct.setSalePrice(rateUnitPrice);
			orderItemProduct.setTotalSalePrice(rateUnitPrice);
			//최종결제가_상품당: 재설정(총판매가_개당 * 주문수량)
			orderItemProduct.setPaymentAmt(rateUnitPrice.multiply(totalOrderQty));
			//=> 자투리 계산을 위해 (판매단가 * 구성상품주문수량) 누적
			stackPaymentAmt = stackPaymentAmt.add(rateUnitPrice.multiply(totalOrderQty));
			//=> 마지막 자료에서 자투리 계산
			if (idx == (setCnt - 1)) {
				//자투리금액 = 전체 결제금액 - 누적 결제금액
				restPaymentAmt = paymentAmt.subtract(stackPaymentAmt);
				//보정판매가: 재설정(자투리금액)
				orderItemProduct.setCalibrateSalePrice(restPaymentAmt);
			}
			//세액: 재설정(구성상품은 0)
			orderItemProduct.setTax(BigDecimal.ZERO);
			//배송정책번호: 재설정(세트상품의 번호로 통일) => 세트구성상품의 정보로 설정
//			orderItemProduct.setDeliveryPolicyNo(setDeliveryPolicyNo);
			//외화금액: 재설정(중국몰의 경우)
			if (currencyPrice.compareTo(BigDecimal.ZERO) > 0) {
				//=> 현 구성상품의 비율가
				double chinaRatePrice = (setOne.getSumListPrice().doubleValue() / totalListPrice) * currencyPrice.doubleValue();
				//=> 현 구성상품의 비율가에 따른 단가(원단위 버림)
				BigDecimal chinaRateUnitPrice = new BigDecimal(Math.floor(chinaRatePrice / subSetQty.doubleValue()));
				chinaRateUnitPrice = chinaRateUnitPrice.setScale(0, BigDecimal.ROUND_DOWN);
				//=> 자투리 계산을 위해 (외화단가 * 세트구성수량) 누적
				stackCurrencyPrice = stackCurrencyPrice.add(chinaRateUnitPrice.multiply(subSetQty));
				logger.debug("stackCurrencyPrice: " + stackCurrencyPrice);
				//=> 마지막 자료에서 자투리 계산
				if (idx == (setCnt - 1)) {
					//자투리단가 = 전체 외화단가 - 누적 외화단가
					restCurrencyPrice = currencyPrice.subtract(stackCurrencyPrice);
					BigDecimal finalPrice = chinaRateUnitPrice.multiply(totalOrderQty);
					orderItemProduct.setCurrencyPrice(finalPrice.add(restCurrencyPrice));
				} else {
					orderItemProduct.setCurrencyPrice(chinaRateUnitPrice.multiply(totalOrderQty));
				}
			}

			//주문상품리스트 추가
			orderSetProductList.add(orderItemProduct);
		}

		return orderSetProductList;
	}

	/**
	 * @Method Name : setOrderProductPresent
	 * @author : peter
	 * @date : 2016. 10. 13.
	 * @description : 사은품 주문상품정보 설정
	 *
	 * @param orm
	 * @return
	 */
	public List<OmsOrderproduct> setOrderProductPresent(UploadExcelOrder excel, PmsProduct product) {
		List<OmsOrderproduct> orderPresentProductList = new ArrayList<OmsOrderproduct>();

		//기본항목 설정
		OmsOrderproduct orderPresentProduct = setOrderProductBase(excel, product);
		//주문상품유형코드: 상품사은품
		orderPresentProduct.setOrderProductTypeCd("ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT");

		//판매가: 재설정
		orderPresentProduct.setSalePrice(BigDecimal.ZERO);
		//총판매가_개당: 재설정
		orderPresentProduct.setTotalSalePrice(BigDecimal.ZERO);
		//최종결제가_상품당: 재설정
		orderPresentProduct.setPaymentAmt(BigDecimal.ZERO);
		//수수료율: 재설정(0)
		orderPresentProduct.setCommissionRate(BigDecimal.ZERO);
		//세액: 재설정
		orderPresentProduct.setTax(BigDecimal.ZERO);
		//외화금액: 재설정
		orderPresentProduct.setCurrencyPrice(BigDecimal.ZERO);

		//리스트에 추가
		orderPresentProductList.add(orderPresentProduct);

		return orderPresentProductList;
	}

	/**
	 * @Method Name : setOrderProductBase
	 * @author : peter
	 * @date : 2016. 10. 6.
	 * @description : 주문상품 기본정보 설정
	 *
	 * @param orm
	 * @param product
	 * @return
	 */
	private OmsOrderproduct setOrderProductBase(UploadExcelOrder excel, PmsProduct product) {
		OmsOrderproduct orderProduct = new OmsOrderproduct();

		//상품명
		orderProduct.setProductName(product.getName());
		//단품ID
		orderProduct.setSaleproductId(product.getPmsSaleproduct().getSaleproductId());
		//세트구성수량: 기본 "1", 세트구성상품만 재설정
		orderProduct.setSetQty(BigDecimal.ONE);
		//주문수량: 엑셀 자료, 세트구성상품만 재설정
		orderProduct.setOrderQty(excel.getOrderQty());
		//판매가: 엑셀 자료, 세트구성상품만 재설정
		orderProduct.setSalePrice(excel.getSalePrice());
		//총판매가_개당: 엑셀 자료, 세트구성상품만 재설정
		orderProduct.setTotalSalePrice(excel.getSalePrice());
		//보정판매가: 무조건 0
		orderProduct.setCalibrateSalePrice(BigDecimal.ZERO);
		//최종결제가_상품당: 엑셀 자료(판매단가 * 주문수량), 세트구성상품만 재설정
		orderProduct.setPaymentAmt(excel.getSalePrice().multiply(excel.getOrderQty()));
		//세액: 세트구성상품만 재설정
		if ("TAX_TYPE_CD.FREE".equals(product.getTaxTypeCd())) { //면세
			orderProduct.setTax(BigDecimal.ZERO);
		} else { //과세
			double taxAmt = excel.getSalePrice().doubleValue() * 0.1;
			orderProduct.setTax(new BigDecimal(taxAmt).setScale(-1, BigDecimal.ROUND_DOWN));
		}
		//보세여부에 따른 처리
		if ("Y".equals(excel.getLocalDeliveryYn().toUpperCase())) { //보세인 경우
			//현지배송여부(보세여부): 보세주문이면 "Y"
			orderProduct.setLocalDeliveryYn("Y");
			//주문상품상태코드: 보세주문이면 "출고완료" 처리 
			orderProduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.SHIP");
		} else { //보세가 아닌 경우
			//현지배송여부(보세여부): "N"
			orderProduct.setLocalDeliveryYn("N");
			//주문상품상태코드: Tmall 이외는 출고지시대기 
			orderProduct.setOrderProductStateCd("ORDER_PRODUCT_STATE_CD.READY");
		}
		//외화금액: 엑셀 자료(외화단가 * 주문수량), 세트구성상품은 재설정
		if (excel.getCurrencyPrice().intValue() >= 0) {
			orderProduct.setCurrencyPrice(excel.getCurrencyPrice().multiply(excel.getOrderQty()));
		} else {
			orderProduct.setCurrencyPrice(BigDecimal.ZERO);
		}
		//수출용ERP상품ID: 중국몰만 적용
		if ("SITE_TYPE_CD.CHINA".equals(excel.getSiteTypeCd())) {
			orderProduct.setExportErpProductId(product.getExportErpProductId());
		} else {
			orderProduct.setExportErpProductId("");
		}
		//배송정책번호
		orderProduct.setDeliveryPolicyNo(product.getDeliveryPolicyNo());

		//발송유형코드: 주문
		orderProduct.setOrderDeliveryTypeCd("ORDER_DELIVERY_TYPE_CD.ORDER");
		//카테고리ID
		orderProduct.setCategoryId(product.getCategoryId());
		//브랜드ID
		orderProduct.setBrandId(product.getBrandId());
		//상품ID
		orderProduct.setProductId(product.getProductId());
		//ERP상품ID
		orderProduct.setErpProductId(product.getErpProductId());
		//단품명
		orderProduct.setSaleproductName(product.getPmsSaleproduct().getName());
		//ERP단품ID
		orderProduct.setErpSaleproductId(product.getPmsSaleproduct().getErpSaleproductId());
		//업체ID
		orderProduct.setBusinessId(product.getBusinessId());
		//업체명
		orderProduct.setBusinessName(product.getCcsBusiness().getName());
		//매입유형코드
		orderProduct.setSaleTypeCd(product.getCcsBusiness().getSaleTypeCd());
		//위탁매입여부
		orderProduct.setPurchaseYn(product.getCcsBusiness().getPurchaseYn());
		//상품고시유형코드
		orderProduct.setProductNoticeTypeCd(product.getProductNoticeTypeCd());
		//과세구분코드
		orderProduct.setTaxTypeCd(product.getTaxTypeCd());
		//옵션여부
		orderProduct.setOptionYn(product.getOptionYn());
		//text옵션여부
		orderProduct.setTextOptionYn("N");
		//정상가
		orderProduct.setListPrice(product.getListPrice());
		//공급가
		orderProduct.setSupplyPrice(product.getSupplyPrice());
		//추가판매가: 무조건 0
		orderProduct.setAddSalePrice(BigDecimal.ZERO);
		//수수료율
		orderProduct.setCommissionRate(product.getCommissionRate());
		//포인트적립율: 현재는 0
		orderProduct.setPointSaveRate(BigDecimal.ZERO);
		//배송비무료여부
//		orderProduct.setDeliveryFeeFreeYn("Y");
		orderProduct.setDeliveryFeeFreeYn(product.getDeliveryFeeFreeYn());
		//상품쿠폰1개적용여부: null 처리
//		orderProduct.setProductSingleApplyYn("N");
		//플러스쿠폰1개적용여부: null 처리
//		orderProduct.setPlusSingleApplyYn("N");
		//상품적립포인트_개당(0)
		orderProduct.setProductPoint(BigDecimal.ZERO);
		//추가적립포인트_개당(0)
		orderProduct.setAddPoint(BigDecimal.ZERO);
		//총적립포인트_개당(0)
		orderProduct.setTotalPoint(BigDecimal.ZERO);
		//보정포인트(0)
		orderProduct.setCalibratePoint(BigDecimal.ZERO);
		//상품쿠폰할인가_개당: 현재는 0
		orderProduct.setProductCouponDcAmt(BigDecimal.ZERO);
		//보정상품쿠폰할인금액: 현재는 0
		orderProduct.setCalibrateProductDcAmt(BigDecimal.ZERO);
		//플러스쿠폰할인가_개당: 현재는 0
		orderProduct.setPlusCouponDcAmt(BigDecimal.ZERO);
		//보정플러스쿠폰할인금액: 현재는 0
		orderProduct.setCalibratePlusDcAmt(BigDecimal.ZERO);
		//주문쿠폰할인가_상품당: 현재는 0
		orderProduct.setOrderCouponDcAmt(BigDecimal.ZERO);
		//보정주문쿠폰할인금액: 현재는 0
		orderProduct.setCalibrateOrderDcAmt(BigDecimal.ZERO);
		//취소수량: 현재는 0
		orderProduct.setCancelQty(BigDecimal.ZERO);
		//출고수량: 현재는 0
		orderProduct.setOutQty(BigDecimal.ZERO);
		//반품수량: 현재는 0
		orderProduct.setReturnQty(BigDecimal.ZERO);
		//교환수량: 현재는 0
		orderProduct.setExchangeQty(BigDecimal.ZERO);
		//재배송수량: 현재는 0
		orderProduct.setRedeliveryQty(BigDecimal.ZERO);
		//가출고수량
		orderProduct.setVirtualOutQty(BigDecimal.ZERO);
		//가반품수량
		orderProduct.setVirtualReturnQty(BigDecimal.ZERO);
		//예약판매여부
		orderProduct.setReserveYn("N");
		//지정일배송여부
		orderProduct.setFixedDeliveryYn("N");
		//배송예정일시_예약상품인 경우, 지정일배송일시, 주문일시: 무조건 오늘 날짜
		orderProduct.setDeliveryReserveDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));
		//포장여부
		orderProduct.setWrapYn("N");
		//포장부피: 현재는 0
		orderProduct.setWrapVolume(BigDecimal.ZERO);
		//해외구매대행여부
		orderProduct.setOverseasPurchaseYn("N");
		//박스배송여부
		orderProduct.setBoxDeliveryYn("N");
		//외부몰주문번호
		orderProduct.setSabangOrderId(excel.getSiteOrderId());
		//중국주문LP번호: 엑셀 자료
		orderProduct.setLpNo(excel.getLpNo());
		//통화코드: 엑셀 자료
		orderProduct.setCurrencyCd(excel.getCurrencyCd());
		//주문일자: 오늘 날짜
		orderProduct.setOrderDt(DateUtil.getCurrentDate(DateUtil.FORMAT_10));

		//상점ID
		orderProduct.setStoreId(excel.getStoreId());
		//등록자ID
		orderProduct.setInsId(excel.getLoginId());
		//변경자ID
		orderProduct.setUpdId(excel.getLoginId());

		return orderProduct;
	}

	/**
	 * @Method Name : setDeliveryAddress
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 배송지정보 설정
	 *
	 * @param orm
	 * @return
	 */
	private OmsDeliveryaddress setDeliveryAddress(UploadExcelOrder excel) {
		OmsDeliveryaddress address = new OmsDeliveryaddress();

		//배송지번호
		address.setDeliveryAddressNo((BigDecimal) dao.selectOne("oms.order.getNewDeliveryAddressNo", null));
		//수취인명
		address.setName1(excel.getName());
		//전화번호
		String phone1 = excel.getPhone1();
		String phone2 = excel.getPhone2();
		//=> 전화번호1: 값이 없으면 전화번호2로 설정
		if (null != excel.getPhone1() && !"".equals(excel.getPhone1())) {
			address.setPhone1(phone1.trim());
		} else {
			address.setPhone1(phone2.trim());
		}
		//전화번호2: 값이 없으면 전화번호1로 설정
		if (null != excel.getPhone2() && !"".equals(excel.getPhone2())) {
			address.setPhone2(phone2.trim());
		} else {
			address.setPhone2(phone1.trim());
		}
		//우편번호
		String zipCd = excel.getZipCd().replaceAll("-", "").trim();
		address.setZipCd(zipCd);
		//주소
		String fullAddress = excel.getAddress1().replaceAll("_", " ").trim();
		if (null != excel.getAddress2() && !"".equals(excel.getAddress2())) {
			fullAddress = fullAddress + " " + excel.getAddress2().replaceAll("_", " ").trim();
		}
		address.setAddress1(fullAddress);
		//배송메세지
		if ("SITE_TYPE_CD.CHINA".equals(excel.getSiteTypeCd())) { //중국몰인 경우
			//Tmall: 배송메시지를 [LP번호]로 저장
			if ("246".equals(excel.getSiteId())) {
				address.setNote("[" + excel.getLpNo() + "]");
			}
			//JDmall: 배송메시지를 LP번호로 저장
			else if ("265".equals(excel.getSiteId()) || "266".equals(excel.getSiteId())) {
				address.setNote(excel.getLpNo());
			}
		} else { //자사몰, 국내외부몰인 경우
			address.setNote(excel.getNote());
		}

		//등록자ID
		address.setInsId(excel.getLoginId());
		//변경자ID
		address.setUpdId(excel.getLoginId());

		return address;
	}

	/**
	 * @Method Name : setDelivery
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 배송정보 설정
	 *
	 * @param storeId
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	private OmsDelivery setDelivery(BigDecimal deliveryPolicyNo) throws Exception {
		//배송정책 조회
		OmsDelivery delivery = (OmsDelivery) dao.selectOne("oms.uploadexcel.getOmsDeliveryInfo", deliveryPolicyNo);

		return delivery;
	}

	/**
	 * @Method Name : getProductInfo
	 * @author : peter
	 * @date : 2016. 7. 12.
	 * @description : 상품,단품정보 조회
	 *
	 * @param storeId
	 * @param saleproductId
	 * @return
	 */
	private PmsProduct getProductInfo(String storeId, String saleproductId) {
		PmsProductSearch search = new PmsProductSearch();
		search.setStoreId(storeId);
		search.setSaleproductId(saleproductId);

		return (PmsProduct) dao.selectOne("oms.uploadexcel.getProductInfo", search);
	}

	/**
	 * @Method Name : checkValidation
	 * @author : peter
	 * @date : 2016. 8. 2.
	 * @description : 주문수집 데이터 유효성 체크
	 *
	 * @param orm
	 * @return
	 */
	private String checkValidation(UploadExcelOrder excel) {
		String rtnMsg = "";

		//주문번호 오류
		if (null == excel.getSiteOrderId() || "".equals(excel.getSiteOrderId())) {
			logger.debug("주문번호 미존재 오류: Order ID [" + excel.getSiteOrderId() + "]");
			rtnMsg = "주문번호가 없습니다";
			return rtnMsg;
		}
		//단품번호 오류
		String saleproductId = getSaleproductId(excel);
		if (null == saleproductId || "".equals(saleproductId)) {
			logger.error("단품정보 미존재 오류: Order ID [" + excel.getSiteOrderId() + "]");
			rtnMsg = "단품번호가 없습니다";
			return rtnMsg;
		}
		//기등록 여부: 주문번호에 "+" 가 없는 경우만 체크, 자사몰은 체크 제외
		if (!"SITE_TYPE_CD.OWN".equals(excel.getSiteTypeCd())) {
			if (!excel.getSiteOrderId().contains("+")) {
				if ((int) dao.selectOne("oms.uploadexcel.getOrderExistCount", excel.getSiteOrderId()) > 0) {
					logger.debug("Order ID: " + excel.getSiteOrderId());
					rtnMsg = "기등록된 주문입니다";
					return rtnMsg;
				}
			}
		}
		//주문수량 오류
		if (null == excel.getOrderQty() || "".equals(excel.getOrderQty()) || "0".equals(excel.getOrderQty())) {
			logger.debug("Sale Count: " + excel.getOrderQty() + ", Order ID: " + excel.getSiteOrderId());
			rtnMsg = "주문수량이 없습니다";
			return rtnMsg;
		}
		//수취인명 오류
		if (null == excel.getName() || "".equals(excel.getName())) {
			logger.debug("Receive Name: " + excel.getName() + ", Order ID: " + excel.getSiteOrderId());
			rtnMsg = "수취인 이름이 없습니다";
			return rtnMsg;
		} else {
			String receiveName = excel.getName().trim();
			if (receiveName.length() > 100) {
				logger.debug("Receive Name: " + excel.getName() + ", Order ID: " + excel.getSiteOrderId());
				rtnMsg = "수취인명 길이가 100 byte를 초과하였습니다[" + excel.getName() + "]";
				return rtnMsg;
			}
		}
		//수취인 전화번호,핸드폰번호 오류
		if ((null == excel.getPhone1() || "".equals(excel.getPhone1()))
				&& (null == excel.getPhone2() || "".equals(excel.getPhone2()))) {
			logger.debug("Receive Tel: " + excel.getPhone1() + ", Receive Cel: " + excel.getPhone2() + ", Order ID: "
					+ excel.getSiteOrderId());
			rtnMsg = "수취인 연락처가 없습니다";
			return rtnMsg;
		}
		//수취인 우편번호 오류
		if (null == excel.getZipCd() || "".equals(excel.getZipCd())) {
			logger.debug("Receive Zipcode: " + excel.getZipCd() + ", Order ID: " + excel.getSiteOrderId());
			rtnMsg = "우편번호가 없습니다";
			return rtnMsg;
		} else {
			String zipCd = excel.getZipCd().replaceAll("-", "").trim();
			if (zipCd.length() < 5 || zipCd.length() > 6 || zipCd.contains("00000") || !isHanjinZipCode(zipCd)) {
				logger.debug("Receive Zipcode: " + excel.getZipCd() + ", Order ID: " + excel.getSiteOrderId());
				rtnMsg = "우편번호가 정확하지 않습니다[" + excel.getZipCd() + "]"; 
				return rtnMsg;
			}
		}
		//수취인 주소
		if (null == excel.getAddress1() || "".equals(excel.getAddress1())) {
			logger.debug("Receive Address: " + excel.getAddress1() + ", Order ID: " + excel.getSiteOrderId());
			return "수취인 주소가 없습니다";
		} else {
			String receiveAddr1 = excel.getAddress1().trim();
			if (receiveAddr1.getBytes().length > 200) {
				logger.debug("Receive Address1: " + excel.getAddress1() + ", Order ID: " + excel.getSiteOrderId());
				rtnMsg = "주소1 이 200 byte를 초과하였습니다[" + excel.getAddress1() + "]";
				return rtnMsg;
			}
		}
		if (null != excel.getAddress2() && !"".equals(excel.getAddress2())) {
			String receiveAddr2 = excel.getAddress2().trim();
			if (receiveAddr2.getBytes().length > 200) {
				logger.debug("Receive Address2: " + excel.getAddress2() + ", Order ID: " + excel.getSiteOrderId());
				rtnMsg = "주소2 가 200 byte를 초과하였습니다[" + excel.getAddress2() + "]";
				return rtnMsg;
			}
		}

		return rtnMsg;
	}

	/**
	 * @Method Name : isHanjinZipCode
	 * @author : peter
	 * @date : 2016. 8. 2.
	 * @description : 6자리 우편번호의 경우 한진택배 우편번호인지 확인
	 *
	 * @param zipCd
	 * @return
	 * @throws Exception
	 */
	private boolean isHanjinZipCode(String zipCd) {
		//6자리 같은번호가 한진우편번호인지 확인
		if (zipCd.length() == 6) {
			String zipCd1 = zipCd.substring(0, 3);
			String zipCd2 = zipCd.substring(3, 6);

			if (zipCd1.equals(zipCd2)) {
				//한진 배송DB 조회 
				int cnt = (int) edi.selectOne("oms.logistics.checkZipCodeHanjin", zipCd);

				if (cnt > 0) {
					return true;
				} else {
					return false;
				}

			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * @Method Name : isDeliveryPolicyNotExist
	 * @author : peter
	 * @date : 2016. 10. 21.
	 * @description : 배송정책 기등록여부
	 *
	 * @param orderId
	 * @param addressNo
	 * @param policyNo
	 * @return
	 */
	private boolean isDeliveryPolicyNotExist(List<BigDecimal> policyList, BigDecimal policyNo) {
		boolean result = true;
		for (BigDecimal policy : policyList) {
			if (policy.compareTo(policyNo) == 0) {
				result = false;
				break;
			}
		}

		return result;
	}
}
