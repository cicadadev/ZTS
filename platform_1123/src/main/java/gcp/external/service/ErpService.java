package gcp.external.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.ErpApxItemmapping;
import gcp.external.model.ErpItem;
import gcp.pms.model.search.PmsProductSearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service("erpService")
public class ErpService extends BaseService {

	/**
	 * ERP에 상품 등록
	 * 
	 * @Method Name : insertErpProduct
	 * @author : eddie
	 * @date : 2016. 6. 28.
	 * @description :
	 *
	 * @param productId
	 * @throws ServiceException
	 */
	public void insertErpProduct(String productId) throws ServiceException {

		PmsProductSearch search = new PmsProductSearch();
		search.setProductId(productId);
		search.setStoreId(SessionUtil.getStoreId());
		// 대상 상품 아이템 조회
		List<ErpApxItemmapping> list = (List<ErpApxItemmapping>) dao.selectList("pms.product.getProductErpInterfaceItems",
				search);

		if (list == null) {
			return;
		}

		// APX_ITEMMAPPINGTEMP 에 insert
		for (ErpApxItemmapping item : list) {
			item.setInventcolorid(CommonUtil.replaceNull(item.getInventcolorid()));
			item.setInventsizeid(CommonUtil.replaceNull(item.getInventsizeid()));
			try {
				erp.insert("external.erp.insertApxItemmappingtemp", item);
			} catch (Exception e) {
				String[] param = { "external.erp.insertApxItemmappingtemp", "[ " + item.getItemId() + " ]" + e.getMessage() };
				throw new ServiceException("pms.sql.error", param);
			}
		}
	}

	/**
	 * ERP에서 itemid로 상품정보 조회
	 * 
	 * @Method Name : getErpItem
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param itemid
	 */
	public List<ErpItem> getErpItem(String itemid) {
		return (List<ErpItem>) erp.selectList("external.erp.selectItem", itemid);
	}

	/**
	 * ERP에서 itemid로 상품정보 조회 ( paging )
	 * 
	 * @Method Name : getErpItemList
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param search
	 */
	public List<ErpItem> getErpItemList(PmsProductSearch search) {
		return (List<ErpItem>) erp.selectList("external.erp.selectItemList", search);
	}

	/**
	 * 
	 * @Method Name : getItemSaleProductList
	 * @author : allen
	 * @date : 2016. 9. 1.
	 * @description : ERP 단품 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	public List<ErpItem> getItemSaleProductList(PmsProductSearch search) {
		return (List<ErpItem>) erp.selectList("external.erp.getItemSaleProductList", search);
	}

}
