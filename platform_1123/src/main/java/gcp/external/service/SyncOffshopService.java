package gcp.external.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.CcsOffshopbrand;
import gcp.ccs.model.CcsOffshopholiday;
import gcp.ccs.service.BaseService;
import gcp.external.model.ErpApxCustHoliday;
import gcp.external.model.ErpApxSellingBrand;
import gcp.external.model.ErpCustTable;
import intune.gsf.common.constants.BaseConstants;

@Service
public class SyncOffshopService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : updateOffshopByErp
	 * @author : intune
	 * @date : 2016. 8. 1.
	 * @description : ERP 매장정보를 조회하여 BO에 insert
	 *
	 * @param storeId
	 * @param updId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateOffshopByErp(String storeId, String updId) throws Exception {
		logger.debug("Store ID: " + storeId + ", Upd ID: " + updId);

		//기존 매장정보 모두 상태코드 '중지'로 변경
		dao.delete("external.syncoffshop.deleteCcsOffshop", updId);
		//기존 매장 휴일정보 모두 삭제
		dao.delete("external.syncoffshop.deleteCcsOffshopHoliday", null);
		//기존 매장 취급브랜드정보 모두 삭제
		dao.delete("external.syncoffshop.deleteCcsOffshopBrand", null);

		//ERP 매장 및 휴일정보 조회
		List<ErpCustTable> offShopList = (List<ErpCustTable>) erp.selectList("external.erp.getErpOffshopList", null);
		for (ErpCustTable erpShop : offShopList) {
			logger.debug("shop: " + erpShop.getAccountNum() + ", state: " + erpShop.getState() + ", county: "
					+ erpShop.getCounty() + ", holidayInfo: " + erpShop.getHolidayInfo());

			CcsOffshop offShop = new CcsOffshop();
			//상점ID
			offShop.setStoreId(storeId);
			//매장ID
			offShop.setOffshopId(erpShop.getAccountNum());
			//매장명
			offShop.setName(erpShop.getZtsCustname());
			//매장유형코드
			offShop.setOffshopTypeCd(getOffshopTypeCode(erpShop.getTkrShopType()));
			//우편번호
			offShop.setZipCd(erpShop.getZipCode());
			//주소: 할인점("0")이나 백화점("3")은 중간관리자 정보를 통해 주소 다시 조회
			logger.debug("ERP ShopType: " + erpShop.getTkrShopType());
			if ("0".equals(erpShop.getTkrShopType()) || "3".equals(erpShop.getTkrShopType())) {
				offShop.setAddress1((String) erp.selectOne("external.erp.getErpShopInfoOfManager", erpShop.getAccountNum()));
			} else {
				offShop.setAddress1(erpShop.getEsgKrKraddress());
			}
			//매장전화번호
			offShop.setOffshopPhone(erpShop.getPhone());
			//운영자전화번호
			offShop.setManagerPhone(erpShop.getTkrManagerPhone());
			//GPS 위도
			offShop.setLatitude(new BigDecimal(erpShop.getZtsLatitude()));
			//GPS 경도
			offShop.setLongitude(new BigDecimal(erpShop.getZtsLongitude()));
			if (erpShop.getPickup() == 1) {
				//픽업가능여부
				offShop.setOffshopPickupYn("Y");
			} else {
				//픽업가능여부
				offShop.setOffshopPickupYn("N");
			}
			//매장상태코드
			offShop.setOffshopStateCd(BaseConstants.OFFSHOP_STATE_CD_RUN);
			//매장지역구분1
			offShop.setAreaDiv1(erpShop.getState());
			//매장지역구분2
			offShop.setAreaDiv2(erpShop.getCounty());
			//매장휴일안내
			offShop.setHolidayInfo(erpShop.getHolidayInfo());
			//매장계열명
			offShop.setOffshopAffiliation(erpShop.getTkrSeriesName());
			//등록자
			offShop.setInsId(updId);
			offShop.setUpdId(updId);
			//오프라인매장 정보 등록
			dao.update("external.syncoffshop.updateCcsOffshop", offShop);

			//오프라인매장 취급브랜드 정보
			logger.debug("shop [" + erpShop.getAccountNum() + "] Brand Count: " + erpShop.getErpApxSellingBrands().size());
			for (ErpApxSellingBrand erpBrand : erpShop.getErpApxSellingBrands()) {
				CcsOffshopbrand brand = new CcsOffshopbrand();
				brand.setStoreId(storeId);
				brand.setOffshopId(erpShop.getAccountNum());
				//취급브랜드
				Map<String, String> srchMap = new HashMap<String, String>();
				srchMap.put("storeId", storeId);
				srchMap.put("erpBrand", erpBrand.getBrand());
				brand.setBrandId((String) dao.selectOne("external.syncoffshop.getPmsBrandInfo", srchMap));
				brand.setInsId(updId);
				brand.setUpdId(updId);
				//오프라인매장 취급브랜드 등록
				dao.insert("external.syncoffshop.insertCcsOffshopBrand", brand);
			}

			//오프라인매장 휴일 정보
			logger.debug("shop [" + erpShop.getAccountNum() + "] Holiday Count: " + erpShop.getErpApxCustHolidays().size());
			for (ErpApxCustHoliday erpHoliday : erpShop.getErpApxCustHolidays()) {
				if (null == erpHoliday.getHoliday() || "".equals(erpHoliday.getHoliday())) {
					logger.error("shop [" + erpShop.getAccountNum() + "] Holiday Value Error: " + erpHoliday.getHoliday());
					continue;
				}
				CcsOffshopholiday holiday = new CcsOffshopholiday();
				holiday.setStoreId(storeId);
				holiday.setOffshopId(erpShop.getAccountNum());
				//매장휴일
				holiday.setHoliday(erpHoliday.getHoliday());
				holiday.setInsId(updId);
				holiday.setUpdId(updId);
				//오프라인매장 휴일정보 등록
				dao.insert("external.syncoffshop.insertCcsOffshopHoliday", holiday);
			}
		}
	}

	/**
	 * @Method Name : getOffshopType
	 * @author : intune
	 * @date : 2016. 8. 1.
	 * @description : 매장유형 BO 코드 찾기
	 *
	 * @param erpShopType
	 * @return
	 */
	private String getOffshopTypeCode(String erpShopType) {
		String shopTypeCd = "";
		switch (erpShopType) {
		case "0": //할인점
			shopTypeCd = "OFFSHOP_TYPE_CD.DISCOUNT";
		break;
		case "1": //대리점
			shopTypeCd = "OFFSHOP_TYPE_CD.AGENT";
		break;
		case "2": //직영점
			shopTypeCd = "OFFSHOP_TYPE_CD.DIRECT";
		break;
		case "3": //백화점
			shopTypeCd = "OFFSHOP_TYPE_CD.DEPART";
		break;
		case "4": //특약점
			shopTypeCd = "OFFSHOP_TYPE_CD.SPECIAL";
		break;
		case "5": //온라인
			shopTypeCd = "OFFSHOP_TYPE_CD.ONLINE";
		break;
		case "6": //중간관리자
			shopTypeCd = "OFFSHOP_TYPE_CD.MIDDLEMANAGER";
		break;
		case "7": //자사몰
			shopTypeCd = "OFFSHOP_TYPE_CD.SELF";
		break;
		case "8": //오픈몰
			shopTypeCd = "OFFSHOP_TYPE_CD.OPEN";
		break;
		case "9": //수출
			shopTypeCd = "OFFSHOP_TYPE_CD.EXPORT";
		break;
		case "10": //할인점(온라인)
			shopTypeCd = "OFFSHOP_TYPE_CD.DISCOUNTONLINE";
		break;
		case "11": //종합몰
			shopTypeCd = "OFFSHOP_TYPE_CD.COMPLEX";
		break;
		case "12": //매일유업
			shopTypeCd = "OFFSHOP_TYPE_CD.MAEIL";
		break;
		case "13": //면세점
			shopTypeCd = "OFFSHOP_TYPE_CD.TAXFREE";
		break;
		case "99": //기타
		default:
			shopTypeCd = "OFFSHOP_TYPE_CD.ETC";
		break;
		}

		return shopTypeCd;
	}
}
