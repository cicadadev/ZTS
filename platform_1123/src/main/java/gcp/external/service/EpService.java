package gcp.external.service;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.EpNaver;
import gcp.pms.model.PmsProduct;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;

@Service
public class EpService extends BaseService {
	private final Log		logger		= LogFactory.getLog(getClass());

	//상품정보 상수
	private final String	CHANNEL_NAVER	= "0001";
	private final String	CHANNEL_DAUM	= "0002";
	private final String	CHANNEL_COOCHA	= "0014";
	private final String	CHANNEL_COUPON	= "0015";
	private final String	CHANNEL_SHODOC	= "0016";
	private final String	CHANNEL_DANAWA	= "0004";
	private final String	CHANNEL_ENURI	= "0003";

	//네이버 주문정보 상수
	private final String	MSTART		= "<<<mstart>>>";
	private final String	MEND		= "<<<mend>>>";
	private final String	PSTART		= "<<<pstart>>>";
	private final String	PEND		= "<<<pend>>>";
	private final String	SEPARATOR	= "|";

	/**
	 * @Method Name : makeEpChangeProduct
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : EP 요약데이터 생성
	 *
	 * @param storeId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void makeEpChangeProduct(String storeId) throws Exception {
		String nowHour = DateUtil.getCurrentDate("HH");
		logger.debug("Hour: " + nowHour);

		String getDataTime = "";
		switch (nowHour) {
		case "08":
			getDataTime = "7";
		break;
		case "10":
		case "12":
		case "14":
		case "16":
		case "18":
		case "20":
			getDataTime = "2";
		break;
		default:
			getDataTime = "";
		break;
		}
		logger.debug("getDataTime: " + getDataTime);
//		getDataTime = "72";
		if (!"".equals(getDataTime)) {
			//환경변수
			String filePath = Config.getString("upload.affiliate.path") + File.separator;
			String channelUrl = Config.getString("channel.gate.url");
			String imageUrl = Config.getString("image.domain");

			//naver
			String naverFileName = filePath + "naver" + File.separator + "time.txt";
			logger.debug("File: " + naverFileName);
//			File naverFile = new File(naverFileName);
//			BufferedWriter bfwNaver = new BufferedWriter(new FileWriter(naverFile, false));
			FileWriter fwNaver = new FileWriter(naverFileName, false);
			//daum
			String daumFileName = filePath + "daum" + File.separator + "time.txt";
			logger.debug("File: " + daumFileName);
//			File daumFile = new File(daumFileName);
//			BufferedWriter bfwDaum = new BufferedWriter(new FileWriter(daumFile, false));
			FileWriter fwDaum = new FileWriter(daumFileName, false);
			//coocha
			String coochaFileName = filePath + "coocha" + File.separator + "time.txt";
			logger.debug("File: " + coochaFileName);
//			File coochaFile = new File(coochaFileName);
//			BufferedWriter bfwCoocha = new BufferedWriter(new FileWriter(coochaFile, false));
			FileWriter fwCoocha = new FileWriter(coochaFileName, false);
			//shodoc
			String shodocFileName = filePath + "shodoc" + File.separator + "time.txt";
			logger.debug("File: " + shodocFileName);
//			File shodocFile = new File(shodocFileName);
//			BufferedWriter bfwShodoc = new BufferedWriter(new FileWriter(shodocFile, false));
			FileWriter fwShodoc = new FileWriter(shodocFileName, false);
			//danawa
			String danawaFileName = filePath + "danawa" + File.separator + "time.txt";
			logger.debug("File: " + danawaFileName);
//			File danawaFile = new File(danawaFileName);
//			BufferedWriter bfwDanawa = new BufferedWriter(new FileWriter(danawaFile, false));
			FileWriter fwDanawa = new FileWriter(danawaFileName, false);
			//enuri
			String enuriFileName = filePath + "enuri" + File.separator + "time.txt";
			logger.debug("File: " + enuriFileName);
//			File enuriFile = new File(enuriFileName);
//			BufferedWriter bfwEnuri = new BufferedWriter(new FileWriter(enuriFile, false));
			FileWriter fwEnuri = new FileWriter(enuriFileName, false);

			//헤더 저장
			StringBuffer sbNaverHeader = makeNaverHeader();
			fwNaver.write(sbNaverHeader.toString());
			StringBuffer sbEnuriHeader = makeEnuriHeader();
			fwEnuri.write(sbEnuriHeader.toString());

			//데이터 조회
			Map<String, String> epMap = new HashMap<String, String>();
			epMap.put("storeId", storeId);
			epMap.put("getDataTime", getDataTime);
			List<PmsProduct> productList = (List<PmsProduct>) dao.selectList("external.ep.getEpProductList", epMap);

			//데이터 저장
			int dataCnt = 0;
			for (PmsProduct ppOne : productList) {
				dataCnt++;

				fwNaver.write(makeNaverData(ppOne, channelUrl, imageUrl));
				fwDaum.write(makeDaumPartData(ppOne, channelUrl, imageUrl, CHANNEL_DAUM));
				fwCoocha.write(makeDaumPartData(ppOne, channelUrl, imageUrl, CHANNEL_COOCHA));
				fwShodoc.write(makeDaumPartData(ppOne, channelUrl, imageUrl, CHANNEL_SHODOC));
				fwDanawa.write(makeDanawaData(ppOne, channelUrl, imageUrl));
				fwEnuri.write(makeEnuriData(ppOne, channelUrl, imageUrl));

				if (dataCnt % 1000 == 0) {
					fwNaver.flush();
					fwDaum.flush();
					fwCoocha.flush();
					fwShodoc.flush();
					fwDanawa.flush();
					fwEnuri.flush();
				}
			}
			fwNaver.close();
			fwDaum.close();
			fwCoocha.close();
			fwShodoc.close();
			fwDanawa.close();
			fwEnuri.close();
		}
	}

	/**
	 * @Method Name : makeEpAllProduct
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : EP 전체데이터 생성
	 *
	 * @param storeId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void makeEpAllProduct(String storeId) throws Exception {
		//환경변수
		String filePath = Config.getString("upload.affiliate.path") + File.separator;
		String channelUrl = Config.getString("channel.gate.url");
		String imageUrl = Config.getString("image.domain");

		//naver
		String naverFileName = filePath + "naver" + File.separator + "all.txt";
		logger.debug("File: " + naverFileName);
//		File naverFile = new File(naverFileName);
//		BufferedWriter bfwNaver = new BufferedWriter(new FileWriter(naverFile, false));
		FileWriter fwNaver = new FileWriter(naverFileName, false);
		//daum
		String daumFileName = filePath + "daum" + File.separator + "all.txt";
		logger.debug("File: " + daumFileName);
//		File daumFile = new File(daumFileName);
//		BufferedWriter bfwDaum = new BufferedWriter(new FileWriter(daumFile, false));
		FileWriter fwDaum = new FileWriter(daumFileName, false);
		//coocha
		String coochaFileName = filePath + "coocha" + File.separator + "all.txt";
		logger.debug("File: " + coochaFileName);
//		File coochaFile = new File(coochaFileName);
//		BufferedWriter bfwCoocha = new BufferedWriter(new FileWriter(coochaFile, false));
		FileWriter fwCoocha = new FileWriter(coochaFileName, false);
		//shodoc
		String shodocFileName = filePath + "shodoc" + File.separator + "all.txt";
		logger.debug("File: " + shodocFileName);
//		File shodocFile = new File(shodocFileName);
//		BufferedWriter bfwShodoc = new BufferedWriter(new FileWriter(shodocFile, false));
		FileWriter fwShodoc = new FileWriter(shodocFileName, false);
		//danawa
		String danawaFileName = filePath + "danawa" + File.separator + "all.txt";
		logger.debug("File: " + danawaFileName);
//		File danawaFile = new File(danawaFileName);
//		BufferedWriter bfwDanawa = new BufferedWriter(new FileWriter(danawaFile, false));
		FileWriter fwDanawa = new FileWriter(danawaFileName, false);
		//enuri
		String enuriFileName = filePath + "enuri" + File.separator + "all.txt";
		logger.debug("File: " + enuriFileName);
//		File enuriFile = new File(enuriFileName);
//		BufferedWriter bfwEnuri = new BufferedWriter(new FileWriter(enuriFile, false));
		FileWriter fwEnuri = new FileWriter(enuriFileName, false);

		//헤더 저장
		StringBuffer sbNaverHeader = makeNaverHeader();
		fwNaver.write(sbNaverHeader.toString());
		StringBuffer sbEnuriHeader = makeEnuriHeader();
		fwEnuri.write(sbEnuriHeader.toString());

		//데이터 조회
		Map<String, String> epMap = new HashMap<String, String>();
		epMap.put("storeId", storeId);
		epMap.put("getDataTime", "");
		List<PmsProduct> productList = (List<PmsProduct>) dao.selectList("external.ep.getEpProductList", epMap);

		//데이터 저장
		int dataCnt = 0;
		for (PmsProduct ppOne : productList) {
			dataCnt++;

			fwNaver.write(makeNaverData(ppOne, channelUrl, imageUrl));
			fwDaum.write(makeDaumAllData(ppOne, channelUrl, imageUrl, CHANNEL_DAUM));
			fwCoocha.write(makeDaumAllData(ppOne, channelUrl, imageUrl, CHANNEL_COOCHA));
			fwShodoc.write(makeDaumAllData(ppOne, channelUrl, imageUrl, CHANNEL_SHODOC));
			fwDanawa.write(makeDanawaData(ppOne, channelUrl, imageUrl));
			fwEnuri.write(makeEnuriData(ppOne, channelUrl, imageUrl));

			if (dataCnt % 1000 == 0) {
				fwNaver.flush();
				fwDaum.flush();
				fwCoocha.flush();
				fwShodoc.flush();
				fwDanawa.flush();
				fwEnuri.flush();
			}
		}
		fwNaver.close();
		fwDaum.close();
		fwCoocha.close();
		fwShodoc.close();
		fwDanawa.close();
		fwEnuri.close();
	}

	/**
	 * @Method Name : makeEpNaverSaleData
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 네이버 주문데이터 생성
	 *
	 * @param storeId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void makeEpNaverSaleData(String storeId) throws Exception {
		//실제 적용할 경로및 파일
		String filePath = Config.getString("upload.affiliate.path") + File.separator;
		String fullFileName = filePath + "naver" + File.separator + "naverSaleIndex.txt";
		FileWriter fw = new FileWriter(fullFileName, false);

		List<EpNaver> saleList = (List<EpNaver>) dao.selectList("external.ep.getEpNaverSaleList", null);
		if (saleList.size() > 0) {
			StringBuffer sbMeta = new StringBuffer();
			StringBuffer sbDetail = new StringBuffer();
			String newLine = System.lineSeparator();

			sbMeta.append(MSTART).append(newLine);
			sbDetail.append(PSTART).append(newLine);
			long priceSum = 0L;

			for (EpNaver epOne : saleList) {
				//단품ID
				sbDetail.append(epOne.getProductId());
				//주문수량
				sbDetail.append(SEPARATOR).append(epOne.getSumOrderQty());
				//주문금액
				sbDetail.append(SEPARATOR).append(epOne.getSumSalePrice());
				//단품의 상품평 건수
				Map<String, String> srchMap = new HashMap<String, String>();
				srchMap.put("storeId", storeId);
				srchMap.put("productId", epOne.getProductId());
				int reviewCnt = dao.selectCount("external.ep.getEpNaverSaleReviewCount", srchMap);
				sbDetail.append(SEPARATOR).append(reviewCnt);
				sbDetail.append(newLine);

				priceSum += epOne.getSumSalePrice();
			}
			sbDetail.append(PEND);

			//총금액
			sbMeta.append(priceSum);
			//총건수
			sbMeta.append(SEPARATOR).append(saleList.size());
			//판매일자(어제 날짜)
			String format = DateUtil.FORMAT_3;
			String toDate = DateUtil.getCurrentDate(format);
			String saleDate = DateUtil.getAddDate(format, toDate, new BigDecimal(-1));
			logger.debug("Sale Date: " + saleDate);
			sbMeta.append(SEPARATOR).append(saleDate);
			sbMeta.append(newLine);
			sbMeta.append(MEND);
			sbMeta.append(newLine);

			fw.write(sbMeta.toString());
			fw.write(sbDetail.toString());
		}
		fw.close();
	}

	/**
	 * @Method Name : makeEpCoochaXmlProduct
	 * @author : peter
	 * @date : 2016. 11. 2.
	 * @description : 쿠차 XML용 최근 정보가 변경된 상품 1000개
	 *
	 * @param storeId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void makeEpCoochaXmlProduct(String storeId) throws Exception {
		//환경변수
		String filePath = Config.getString("upload.affiliate.path") + File.separator;
		String channelUrl = Config.getString("channel.gate.url");
		String imageUrl = Config.getString("image.domain");

		String coochaFileName = filePath + "coocha" + File.separator + "all.xml";
		logger.debug("File: " + coochaFileName);
//		File coochaFile = new File(coochaFileName);
//		BufferedWriter bfwCoocha = new BufferedWriter(new FileWriter(coochaFile, false));
		FileWriter fwCoocha = new FileWriter(coochaFileName, false);

		//전체상품정보 시작
		fwCoocha.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		fwCoocha.write("<products>\n");

		//데이터 조회
		List<PmsProduct> productList = (List<PmsProduct>) dao.selectList("external.ep.getEpCoochaXmlList", storeId);

		//데이터 저장
		int dataCnt = 0;
		for (PmsProduct ppOne : productList) {
			dataCnt++;

			fwCoocha.write(makeCoochaXmlData(ppOne, channelUrl, imageUrl));

			if (dataCnt % 1000 == 0) {
				fwCoocha.flush();
			}
		}
		//전체상품정보 끝
		fwCoocha.write("</products>\n");

		fwCoocha.close();
	}

	/**
	 * @Method Name : makeNaverHeader
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 네이버 헤더 생성
	 *
	 * @return
	 */
	private StringBuffer makeNaverHeader() {
		String[] header = { "id", "title", "price_pc", "normal_price", "link", "image_link", "category_name1", "category_name2",
				"category_name3", "category_name4", "import_flag", "brand", "maker", "origin", "shipping", "gender", "class",
				"update_time" };

		StringBuffer sbHeader = new StringBuffer();
		for (int i = 0, size = header.length; i < size; i++) {
			sbHeader.append(header[i]);
			if (i == (size - 1)) {
				sbHeader.append("\n");
			} else {
				sbHeader.append("\t");
			}
		}

		return sbHeader;
	}

	/**
	 * @Method Name : makeEnuriHeader
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 에누리 헤더 생성
	 *
	 * @return
	 */
	private StringBuffer makeEnuriHeader() {
		String[] header = { "상품코드", "상품명", "가격", "URL", "배송", "IMGURL", "제조사", "분류", "모바일가격" };

		StringBuffer sbHeader = new StringBuffer();
		for (int i = 0, size = header.length; i < size; i++) {
			sbHeader.append(header[i]);
			if (i == (size - 1)) {
				sbHeader.append("\n");
			} else {
				sbHeader.append("<|>");
			}
		}

		return sbHeader;
	}

	/**
	 * @Method Name : makeNaverData
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 네이버 전체/요약 데이터 생성
	 *
	 * @param prdOne
	 * @param channelUrl
	 * @param imageUrl
	 * @return
	 */
	private String makeNaverData(PmsProduct prdOne, String channelUrl, String imageUrl) {
		StringBuffer sbData = new StringBuffer();
		String newLine = System.lineSeparator();
		String sprt = "\t";

		//상품ID
		sbData.append(prdOne.getProductId()).append(sprt);
		//상품명
		sbData.append(prdOne.getName().replaceAll(sprt, " ")).append(sprt);
		//판매가
		sbData.append(prdOne.getSalePrice()).append(sprt);
		//정상가
		sbData.append(prdOne.getListPrice()).append(sprt);
		//상품URL
		String productUrl = channelUrl + CHANNEL_NAVER + "&productId=" + prdOne.getProductId();
		sbData.append(productUrl).append(sprt);
		//이미지URL
		String imageAllUrl = imageUrl + prdOne.getImgUrl();
		sbData.append(imageAllUrl).append(sprt);
		//전시카테고리명: 대분류
		sbData.append(prdOne.getDetail1()).append(sprt);
		//전시카테고리명: 중분류
		sbData.append(prdOne.getDetail3()).append(sprt);
		//전시카테고리명: 소분류
		sbData.append(prdOne.getDetail5()).append(sprt);
		//전시카테고리명: 세분류(무조건 공백)
		sbData.append("").append(sprt);
		//해외구매대행여부
		sbData.append(prdOne.getOverseasPurchaseYn()).append(sprt);
		//브랜드명
		sbData.append(prdOne.getBrandName()).append(sprt);
		//제조사
		sbData.append(CommonUtil.replaceNull(prdOne.getMaker())).append(sprt);
		//원산지
		sbData.append(prdOne.getOrigin()).append(sprt);
		//배송료
		if ("Y".equals(prdOne.getDeliveryFeeFreeYn())) {
			sbData.append("0").append(sprt);
		} else {
			sbData.append(prdOne.getDeliveryFee()).append(sprt);
		}
		//성별
		sbData.append(prdOne.getDetail7()).append(sprt);
		//데이터종류: I(신규등록), U(업데이트), D(품절)
		sbData.append(prdOne.getSaveType()).append(sprt);
		//상품정보생성시각
		sbData.append(DateUtil.getCurrentDate(DateUtil.FORMAT_1)).append(newLine);

		return sbData.toString();
	}

	/**
	 * @Method Name : makeDaumAllData
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 다음양식 전체데이터 생성
	 *
	 * @param prdOne
	 * @param channelUrl
	 * @param imageUrl
	 * @param channelId
	 * @return
	 */
	private String makeDaumAllData(PmsProduct prdOne, String channelUrl, String imageUrl, String channelId) {
		StringBuffer sbData = new StringBuffer();
		String newLine = System.lineSeparator();

		//시작
		sbData.append("<<<begin>>>").append(newLine);
		//상품ID
		sbData.append("<<<mapid>>>").append(prdOne.getProductId()).append(newLine);
		//할인적용가(할인후가격)
		sbData.append("<<<price>>>").append(prdOne.getSalePrice()).append(newLine);
		//상품명
		String pname = prdOne.getName().replace("[", "").replace("]", "").replace("★", "");
		sbData.append("<<<pname>>>").append(pname).append(newLine);
		//상품URL
		String productUrl = channelUrl + channelId + "&productId=" + prdOne.getProductId();
		sbData.append("<<<pgurl>>>").append(productUrl).append(newLine);
		//이미지URL
		String imageAllUrl = imageUrl + prdOne.getImgUrl();
		sbData.append("<<<igurl>>>").append(imageAllUrl).append(newLine);
		//전시카테고리명: 대분류
		sbData.append("<<<cate1>>>").append(prdOne.getDetail1()).append(newLine);
		//전시카테고리ID: 대분류
		sbData.append("<<<caid1>>>").append(prdOne.getDetail2()).append(newLine);
		//전시카테고리명: 중분류
		sbData.append("<<<cate2>>>").append(prdOne.getDetail3()).append(newLine);
		//전시카테고리ID: 중분류
		sbData.append("<<<caid2>>>").append(prdOne.getDetail4()).append(newLine);
		//전시카테고리명: 소분류
		sbData.append("<<<cate3>>>").append(prdOne.getDetail5()).append(newLine);
		//전시카테고리ID: 소분류
		sbData.append("<<<caid3>>>").append(prdOne.getDetail6()).append(newLine);
		//브랜드명
		sbData.append("<<<brand>>>").append(prdOne.getBrandName()).append(newLine);
		//제조사
		sbData.append("<<<maker>>>").append(CommonUtil.replaceNull(prdOne.getMaker())).append(newLine);
		//적립금
		sbData.append("<<<point>>>").append(prdOne.getTotalPoint() + "원").append(newLine);
		//배송료
		if ("Y".equals(prdOne.getDeliveryFeeFreeYn())) {
			sbData.append("<<<deliv>>>").append("0").append(newLine);
		} else {
			sbData.append("<<<deliv>>>").append(prdOne.getDeliveryFee()).append(newLine);
		}
		//끝알림
		sbData.append("<<<ftend>>>").append(newLine);

		return sbData.toString();
	}

	/**
	 * @Method Name : makeDaumPartData
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 다음양식 요약데이터 생성
	 *
	 * @param prdOne
	 * @param channelUrl
	 * @param imageUrl
	 * @param channelId
	 * @return
	 */
	private String makeDaumPartData(PmsProduct prdOne, String channelUrl, String imageUrl, String channelId) {
		StringBuffer sbData = new StringBuffer();
		String newLine = System.lineSeparator();

		//시작
		sbData.append("<<<begin>>>").append(newLine);
		//상품ID
		sbData.append("<<<mapid>>>").append(prdOne.getProductId()).append(newLine);
		//할인적용가(할인후가격)
		sbData.append("<<<price>>>").append(prdOne.getSalePrice()).append(newLine);
		//상품상태값
		sbData.append("<<<class>>>").append(prdOne.getSaveType()).append(newLine);
		//상품정보 변경시각
		sbData.append("<<<utime>>>").append(prdOne.getUpdDt()).append(newLine);
		//상품명
		String pname = prdOne.getName().replace("[", "").replace("]", "").replace("★", "");
		sbData.append("<<<pname>>>").append(pname).append(newLine);
		//상품URL
		String productUrl = channelUrl + channelId + "&productId=" + prdOne.getProductId();
		sbData.append("<<<pgurl>>>").append(productUrl).append(newLine);
		//이미지URL
		String imageAllUrl = imageUrl + prdOne.getImgUrl();
		sbData.append("<<<igurl>>>").append(imageAllUrl).append(newLine);
		//전시카테고리명: 대분류
		sbData.append("<<<cate1>>>").append(prdOne.getDetail1()).append(newLine);
		//전시카테고리ID: 대분류
		sbData.append("<<<caid1>>>").append(prdOne.getDetail2()).append(newLine);
		//전시카테고리명: 중분류
		sbData.append("<<<cate2>>>").append(prdOne.getDetail3()).append(newLine);
		//전시카테고리ID: 중분류
		sbData.append("<<<caid2>>>").append(prdOne.getDetail4()).append(newLine);
		//전시카테고리명: 소분류
		sbData.append("<<<cate3>>>").append(prdOne.getDetail5()).append(newLine);
		//전시카테고리ID: 소분류
		sbData.append("<<<caid3>>>").append(prdOne.getDetail6()).append(newLine);
		//브랜드명
		sbData.append("<<<brand>>>").append(prdOne.getBrandName()).append(newLine);
		//제조사
		sbData.append("<<<maker>>>").append(CommonUtil.replaceNull(prdOne.getMaker())).append(newLine);
		//적립금
		sbData.append("<<<point>>>").append(prdOne.getTotalPoint() + "원").append(newLine);
		//배송비
		if ("Y".equals(prdOne.getDeliveryFeeFreeYn())) {
			sbData.append("<<<deliv>>>").append("0").append(newLine);
		} else {
			sbData.append("<<<deliv>>>").append(prdOne.getDeliveryFee()).append(newLine);
		}
		//끝알림
		sbData.append("<<<ftend>>>").append(newLine);

		return sbData.toString();
	}

	/**
	 * @Method Name : makeDanawaData
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 다나와 전체/요약 데이터 생성
	 *
	 * @param prdOne
	 * @param channelUrl
	 * @param imageUrl
	 * @return
	 */
	private String makeDanawaData(PmsProduct prdOne, String channelUrl, String imageUrl) {
		StringBuffer sbData = new StringBuffer();
		String newLine = System.lineSeparator();
		String sprt = "^";

		//상품ID
		sbData.append(prdOne.getProductId()).append(sprt);
		//전시카테고리명: 대분류|중분류|소분류
		String categoryNames = prdOne.getDetail1() + "|" + prdOne.getDetail3() + "|" + prdOne.getDetail5();
		sbData.append(categoryNames).append(sprt);
		//상품명
		sbData.append(prdOne.getName()).append(sprt);
		//제조사
		sbData.append(prdOne.getMaker()).append(sprt);
		//이미지URL
		String imageAllUrl = imageUrl + prdOne.getImgUrl();
		sbData.append(imageAllUrl).append(sprt);
		//상품URL
		String productUrl = channelUrl + CHANNEL_DANAWA + "&productId=" + prdOne.getProductId();
		sbData.append(productUrl).append(sprt);
		//판매가
		sbData.append(prdOne.getSalePrice()).append(sprt);
		//적립금
		sbData.append(prdOne.getTotalPoint()).append(sprt);
		//할인쿠폰
		sbData.append("").append(sprt);
		//무이자할부
		sbData.append("").append(sprt);
		//사은품
		sbData.append("").append(sprt);
		//모델명
		sbData.append("").append(sprt);
		//추가정보
		sbData.append("").append(sprt);
		//출시일
		sbData.append("").append(sprt);
		//배송료
		if ("Y".equals(prdOne.getDeliveryFeeFreeYn())) {
			sbData.append("0").append(sprt);
		} else {
			sbData.append(prdOne.getDeliveryFee()).append(sprt);
		}
		//카드프로모션명
		sbData.append("").append(sprt);
		//카드프로모션가
		sbData.append("").append(sprt);
		//쿠폰다운로드필요여부
		sbData.append("").append(sprt);
		//모바일상품가격
		sbData.append("").append(sprt);
		//차등배송비여부
		sbData.append("").append(sprt);
		//차등배송비내용
		sbData.append("").append(sprt);
		//별도설치비유무
		sbData.append("").append(sprt);
		//재고유무
		sbData.append("").append(newLine);

		return sbData.toString();
	}

	/**
	 * @Method Name : makeEnuriData
	 * @author : peter
	 * @date : 2016. 10. 31.
	 * @description : 에누리 전체/요약 데이터 생성
	 *
	 * @param prdOne
	 * @param channelUrl
	 * @param imageUrl
	 * @return
	 */
	private String makeEnuriData(PmsProduct prdOne, String channelUrl, String imageUrl) {
		StringBuffer sbData = new StringBuffer();
		String newLine = System.lineSeparator();
		String sprt = "<|>";

		//상품코드
		sbData.append(prdOne.getProductId()).append(sprt);
		//상품명
		sbData.append(prdOne.getName()).append(sprt);
		//가격
		sbData.append(prdOne.getSalePrice()).append(sprt);
		//URL
		String productUrl = channelUrl + CHANNEL_ENURI + "&productId=" + prdOne.getProductId();
		sbData.append(productUrl).append(sprt);
		//배송
		String deliveryFee = "";
		if ("N".equals(prdOne.getDeliveryFeeFreeYn())) {
			if (prdOne.getDeliveryFee() == 0) {
				deliveryFee = "무료배송";
			} else {
				if (prdOne.getMinDeliveryFreeAmt() == 0) {
					deliveryFee = String.valueOf(prdOne.getDeliveryFee()) + "원";
				} else {
					deliveryFee = String.valueOf(prdOne.getMinDeliveryFreeAmt()) + "원 미만 "
							+ String.valueOf(prdOne.getDeliveryFee()) + "원";
				}
			}
		} else {
			deliveryFee = "무료배송";
		}
		sbData.append(deliveryFee).append(sprt);
		//이미지URL
		String imageAllUrl = imageUrl + prdOne.getImgUrl();
		sbData.append(imageAllUrl).append(sprt);
		//제조사
		sbData.append(prdOne.getMaker()).append(sprt);
		//전시카테고리명: 대분류_중분류_소분류
		String categoryNames = prdOne.getDetail1() + "_" + prdOne.getDetail3() + "_" + prdOne.getDetail5();
		sbData.append(categoryNames).append(newLine);

		return sbData.toString();
	}

	private String makeCoochaXmlData(PmsProduct prdOne, String channelUrl, String imageUrl) {
		StringBuffer sbData = new StringBuffer();
		String newLine = System.lineSeparator();

		//개별상품정보 시작
		sbData.append("<product>").append(newLine);
		//상품코드
		sbData.append("<product_id><![CDATA[").append(prdOne.getProductId()).append("]]></product_id>").append(newLine);
		//상품명
		sbData.append("<product_title><![CDATA[").append(prdOne.getName()).append("]]></product_title>").append(newLine);
		//상품상세: 상품명과 동일
		sbData.append("<product_desc><![CDATA[").append(prdOne.getName()).append("]]></product_desc>").append(newLine);
		//URL
		//=> 쿠차
		String coochaUrl = channelUrl + CHANNEL_COOCHA + "&productId=" + prdOne.getProductId();
		sbData.append("<product_url><![CDATA[").append(coochaUrl).append("]]></product_url>").append(newLine);
		sbData.append("<mobile_url><![CDATA[").append(coochaUrl).append("]]></mobile_url>").append(newLine);
		//=> 쿠폰모아
		String couponUrl = channelUrl + CHANNEL_COUPON + "&productId=" + prdOne.getProductId();
		sbData.append("<product_url2><![CDATA[").append(couponUrl).append("]]></product_url2>").append(newLine);
		sbData.append("<mobile_url2><![CDATA[").append(couponUrl).append("]]></mobile_url2>").append(newLine);
		//상품판매시작일시
		sbData.append("<sale_start><![CDATA[").append(prdOne.getSaleStartDt()).append("]]></sale_start>").append(newLine);
		//상품판매종료일시
		sbData.append("<sale_end><![CDATA[").append(prdOne.getSaleEndDt()).append("]]></sale_end>").append(newLine);
		//정상가
		sbData.append("<price_normal><![CDATA[").append(prdOne.getListPrice()).append("]]></price_normal>").append(newLine);
		//판매가
		sbData.append("<price_discount><![CDATA[").append(prdOne.getSalePrice()).append("]]></price_discount>").append(newLine);
		//할인율
		int discountRate = prdOne.getCommissionRate().intValue();
		if (discountRate < 0) {
			discountRate = 0;
		}
		sbData.append("<discount_rate><![CDATA[").append(discountRate).append("]]></discount_rate>").append(newLine);
		//쿠폰유효기간시작일시
		sbData.append("<coupon_use_start><![CDATA[").append("").append("]]></coupon_use_start>").append(newLine);
		//쿠폰유효기간종료일시
		sbData.append("<coupon_use_end><![CDATA[").append("").append("]]></coupon_use_end>").append(newLine);
		//쿠폰바로사용가능여부
		sbData.append("<now_use><![CDATA[").append("").append("]]></now_use>").append(newLine);
		//전시카테고리정보시작
		sbData.append("<categorys>").append(newLine);
		sbData.append("<category>").append(newLine);
		//전시카테고리명1
		sbData.append("<category1><![CDATA[").append(prdOne.getDetail1()).append("]]></category1>").append(newLine);
		//전시카테고리명2
		sbData.append("<category2><![CDATA[").append(prdOne.getDetail2()).append("]]></category2>").append(newLine);
		//전시카테고리명3
		sbData.append("<category3><![CDATA[").append(prdOne.getDetail3()).append("]]></category3>").append(newLine);
		//전시카테고리정보끝
		sbData.append("</category>").append(newLine);
		sbData.append("</categorys>").append(newLine);
		//상품거래성사를 위해 최소 필요한 인원수(구매개수)
		sbData.append("<buy_limit><![CDATA[").append(prdOne.getMinQty()).append("]]></buy_limit>").append(newLine);
		//상품구매 가능한 최대 인원수(구매개수)
		sbData.append("<buy_max><![CDATA[").append(999999).append("]]></buy_max>").append(newLine);
		//상품구매 인원수(구매개수)
		sbData.append("<buy_count><![CDATA[").append(0).append("]]></buy_count>").append(newLine);
		//배송(F=무료배송/A=조건부 무료배송/C=유료배송)
		String deliveryFee = "";
		if ("N".equals(prdOne.getDeliveryFeeFreeYn())) {
			if (prdOne.getDeliveryFee() == 0) {
				deliveryFee = "F";
			} else {
				if (prdOne.getMinDeliveryFreeAmt() == 0) {
					deliveryFee = "C";
				} else {
					deliveryFee = "A";
				}
			}
		} else {
			deliveryFee = "F";
		}
		sbData.append("<free_shipping><![CDATA[").append(deliveryFee).append("]]></free_shipping>").append(newLine);
		//대표이미지URL
		String imageAllUrl = imageUrl + prdOne.getImgUrl();
		sbData.append("<image_url1><![CDATA[").append(imageAllUrl).append("]]></image_url1>").append(newLine);
		//판매점정보시작
		sbData.append("<shops>").append(newLine);
		sbData.append("<shop>").append(newLine);
		//상품판매상점명
		sbData.append("<shop_name><![CDATA[").append("제로투세븐").append("]]></shop_name>").append(newLine);
		//서비업체전화번호
		sbData.append("<shop_tel><![CDATA[").append("1588-8744").append("]]></shop_tel>").append(newLine);
		//판매상점주소
		sbData.append("<shop_address><![CDATA[").append("서울시 마포구 상암산로76(상암동, YTN뉴스퀘어17층,18층)").append("]]></shop_address>")
				.append(newLine);
		//판매점위도
		sbData.append("<shop_latitude><![CDATA[").append("").append("]]></shop_latitude>").append(newLine);
		//판매점경도
		sbData.append("<shop_longitude><![CDATA[").append("").append("]]></shop_longitude>").append(newLine);
		//판매점정보시작
		sbData.append("</shop>").append(newLine);
		sbData.append("</shops>").append(newLine);
		//개별상품정보 끝
		sbData.append("</product>").append(newLine);

		return sbData.toString();
	}
}
