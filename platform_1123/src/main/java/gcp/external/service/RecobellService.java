package gcp.external.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.RecobellData;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;

@Service
public class RecobellService extends BaseService {
	private final Log		logger		= LogFactory.getLog(getClass());

	//전체 전시카테고리 변수
	private List<String>	allCategoryList	= null;
	/**
	 * @Method Name : makeProductInfoForRecobell
	 * @author : peter
	 * @date : 2016. 10. 22.
	 * @description : 레코벨 전송데이터 생성
	 *
	 * @param storeId
	 * @param jobType
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int makeProductInfoForRecobell(String storeId, String jobType) throws IOException, Exception {
		//날짜설정
		String toDate = DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		Map<String, String> recobellMap = new HashMap<String, String>();
		recobellMap.put("storeId", storeId);
		recobellMap.put("jobType", jobType);

		//실제 적용할 경로및 파일
		String uploadPath = Config.getString("upload.affiliate.path") + File.separator + "recobell";
		String fileName = uploadPath + File.separator + toDate + ".csv";
		logger.debug("File: " + fileName);
//		File recobellFile = new File(fileName);
//		BufferedWriter bfw = new BufferedWriter(new FileWriter(recobellFile, false));
		FileWriter fw = new FileWriter(fileName, false);

		//전체카테고리리스트
//		allCategoryList = (List<String>) dao.selectList("external.ep.getDisplayCategoryList", storeId);

		//헤더 저장
		StringBuffer sbHeader = makeRecobellHeader();
		fw.write(sbHeader.toString());

		//데이터 저장
		String newLine = System.lineSeparator();
		List<RecobellData> recobellList = (List<RecobellData>) dao.selectList("external.ep.getRecobellProductList", recobellMap);
		int dataCnt = 0;
		for (RecobellData recobell : recobellList) {
			dataCnt++;

			StringBuffer sbData = new StringBuffer();
			//상품ID
			sbData.append(recobell.getProductId()).append(",");
			//ERP상품ID
			String erpProductId = recobell.getErpProductId() == null ? "" : recobell.getErpProductId();
			sbData.append(erpProductId.replace(" ", "")).append(",");
			//브랜드ID
			sbData.append(recobell.getBrandId()).append(",");
			//전시카테고리ID1
			sbData.append(recobell.getDisplayCategoryId1()).append(",");
			//전시카테고리ID2
			sbData.append(recobell.getDisplayCategoryId2()).append(",");
			//전시카테고리ID3
			sbData.append(recobell.getDisplayCategoryId3()).append(",");
			//전시카테고리IDs
//			sbData.append(getDisplayCategorySets(recobell.getDisplayCategoryId())).append(",");
			//정상가
			sbData.append(recobell.getListPrice()).append(",");
			//판매가
			sbData.append(recobell.getSalePrice()).append(newLine);

			fw.write(sbData.toString());

			if (dataCnt % 1000 == 0) {
				fw.flush();
			}
		}
		fw.close();

		return recobellList.size();
	}

	/**
	 * @Method Name : makeFileHeader
	 * @author : peter
	 * @date : 2016. 10. 22.
	 * @description : 레코벨 헤더정보 생성
	 *
	 * @return
	 */
	private StringBuffer makeRecobellHeader() {
		String[] header = { "PRODUCT_ID", "ERP_PRODUCT_ID", "BRAND_ID", "CATEGORY_ID1", "CATEGORY_ID2", "CATEGORY_ID3",
				"LIST_PRICE", "SALE_PRICE" };

		StringBuffer sbHeader = new StringBuffer();
		for (int i = 0, size = header.length; i < size; i++) {
			sbHeader.append(header[i]);
			if (i == (size - 1)) {
				sbHeader.append("\n");
			} else {
				sbHeader.append(",");
			}
		}

		return sbHeader;
	}

	/**
	 * @Method Name : getDisplayCategoryList
	 * @author : peter
	 * @date : 2016. 10. 27.
	 * @description : 해당 상품의 전시카테고리ID 조합 추출
	 *
	 * @param baseCategoryId
	 * @return
	 */
//	private String getDisplayCategorySets(String baseCateId) {
//		String displayCategoryIds = "";
//		for (String cateId : allCategoryList) {
//			if (cateId.contains(baseCateId)) {
//				displayCategoryIds = cateId;
//				break;
//			}
//		}
//
//		return displayCategoryIds;
//	}
}
