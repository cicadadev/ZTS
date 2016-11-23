package gcp.external.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.ErpItem;
import gcp.pms.model.PmsBarcode;
import intune.gsf.common.utils.DateUtil;

@Service
public class SyncErpBarcodeService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	public int insertErpBarcodeInfo(String storeId, String updId, String jobType) throws Exception {
		//날짜설정
		String toDate = DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		Map<String, String> erpMap = new HashMap<String, String>();
		if ("ALL".equals(jobType)) {
			erpMap.put("modifyDate", null);
		} else {
			erpMap.put("modifyDate", DateUtil.getAddDate(DateUtil.FORMAT_3, toDate, new BigDecimal(-1)));
		}
		logger.debug("Today: " + toDate + ", jobDate: " + erpMap.get("modifyDate"));

		//ERP 바코드 정보 조회
		List<ErpItem> items = (List<ErpItem>) erp.selectList("external.erp.getErpBarcodeList", erpMap);
		logger.info("ERP BARCODE COUNT: " + items.size());
		int insCnt = 0;
		for (ErpItem itemOne : items) {
			Map<String, String> srchMap = new HashMap<String, String>();
			srchMap.put("storeId", storeId);
			srchMap.put("erpProductId", itemOne.getItemid());
			srchMap.put("erpSizeId", itemOne.getInventsizeid());
			srchMap.put("erpColorId", itemOne.getInventcolorid());
			PmsBarcode pmsBarcode = (PmsBarcode) dao.selectOne("external.syncbarcode.getSaleproductInfo", srchMap);
			if (null == pmsBarcode) {
				continue;
			}

			pmsBarcode.setStoreId(storeId);
			pmsBarcode.setErpBarcode(itemOne.getItembarcode());
			pmsBarcode.setInsId(updId);
			pmsBarcode.setUpdId(updId);

			//barcode 등록
			if (((SyncErpBarcodeService) getThis()).insertErpBarcodeInfoNewTx(pmsBarcode)) {
				insCnt++;
			}
		}

		logger.debug("insertErpBarcodeInfo Success!!");

		return insCnt;
	}

	public boolean insertErpBarcodeInfoNewTx(PmsBarcode pmsBarcode) throws Exception {
//		logger.debug("====================== request =====================");
//		logger.debug(pmsBarcode.toString());
//		logger.debug("====================== request =====================");

		if ((dao.insert("external.syncbarcode.updatePmsBarcode", pmsBarcode)) == 1) {
			return true;
		} else {
			logger.debug("ErpBarcode Insert error: " + pmsBarcode.toString());
			return false;
		}
	}
}
