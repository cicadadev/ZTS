package gcp.pms.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.pms.model.PmsStyleproduct;
import gcp.pms.model.search.PmsStyleProductSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class StyleProductService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 
	 * @Method Name : getStyleProductList
	 * @author : allen
	 * @date : 2016. 8. 19.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PmsStyleproduct> getStyleProductList(PmsStyleProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<PmsStyleproduct>) dao.selectList("pms.styleShop.getStyleProductList", search);
	}

	/**
	 * 
	 * @Method Name : getStyleProductDetail
	 * @author : allen
	 * @date : 2016. 8. 19.
	 * @description : 스타일샵 상품 상세
	 *
	 * @param styleProductNo
	 * @return
	 */
	public PmsStyleproduct getStyleProductDetail(String styleProductNo) {
		PmsStyleProductSearch search = new PmsStyleProductSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setStyleProductNo(styleProductNo);
		return (PmsStyleproduct) dao.selectOne("pms.styleShop.getStyleProductDetail", search);
	}

	public void saveStyleProduct(PmsStyleproduct pmsStyleproduct) throws Exception {
		if (StringUtils.isNotEmpty(pmsStyleproduct.getCrudType())) {
			if (pmsStyleproduct.getCrudType().equals("C")) {
				// 이미지 temp to real
				if (CommonUtil.isNotEmpty(pmsStyleproduct.getImg())) {
					pmsStyleproduct.setImg(FileUploadUtil.moveTempToReal(pmsStyleproduct.getImg()));
				}
				dao.insertOneTable(pmsStyleproduct);
			} else {
				// 이미지 temp to real
				if (CommonUtil.isNotEmpty(pmsStyleproduct.getImg())) {
					pmsStyleproduct.setImg(FileUploadUtil.moveTempToReal(pmsStyleproduct.getImg()));
				}
				dao.updateOneTable(pmsStyleproduct);
			}
		} else {
			// 이미지 temp to real
			if (CommonUtil.isNotEmpty(pmsStyleproduct.getImg())) {
				pmsStyleproduct.setImg(FileUploadUtil.moveTempToReal(pmsStyleproduct.getImg()));
			}
			dao.updateOneTable(pmsStyleproduct);
		}
	}

	/**
	 * 
	 * @Method Name : deleteStyleProduct
	 * @author : allen
	 * @date : 2016. 10. 8.
	 * @description : 스타일 상품 삭제
	 *
	 * @param pmsStyleproductList
	 * @throws Exception
	 */
	public void deleteStyleProduct(List<PmsStyleproduct> pmsStyleproductList) throws Exception {
		for (PmsStyleproduct product : pmsStyleproductList) {
			product.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(product);
		}
	}

	/**
	 * 
	 * @Method Name : getStyleCategoryProductList
	 * @author : allen
	 * @date : 2016. 10. 8.
	 * @description : 스타일상품의 카테고리및 상품리스트 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DmsDisplaycategory> getStyleCategoryProductList(DmsDisplaySearch search) {
		return (List<DmsDisplaycategory>) dao.selectList("pms.styleShop.getStyleCategoryProductList", search);
	}

	/**
	 * 
	 * @Method Name : getAppStyleProductList
	 * @author : allen
	 * @date : 2016. 10. 10.
	 * @description : App에서 사용될 스타일 상품 목록 조회
	 *
	 * @return
	 */
	public List<PmsStyleproduct> getAppStyleProductList(DmsDisplaySearch search) {

		return (List<PmsStyleproduct>) dao.selectList("pms.styleShop.getAppStyleProductList", search);
	}

}
