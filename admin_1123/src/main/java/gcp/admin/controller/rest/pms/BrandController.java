package gcp.admin.controller.rest.pms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.base.BasePmsBrand;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.model.search.PmsBrandPopupSearch;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.BrandService;
import gcp.pms.service.ProductService;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController
@RequestMapping("api/pms/brand")
public class BrandController {

	@Autowired
	private ProductService	productService;

	@Autowired
	private BrandService	brandService;

	/**
	 * 브랜드 검색 (popup)
	 * 
	 * @Method Name : businessListPopup
	 * @author : dennis
	 * @date : 2016. 5. 25.
	 * @description : 
	 *
	 * @param pmsBrandPopupSearch
	 * @return
	 */
	@RequestMapping(value = "/popup/list", method = RequestMethod.POST)
	public List<PmsBrand> businessListPopup(@RequestBody PmsBrandPopupSearch pmsBrandPopupSearch) {
		pmsBrandPopupSearch.setStoreId(SessionUtil.getStoreId());
		pmsBrandPopupSearch.setLangCd(SessionUtil.getLangCd());
		return brandService.getBrandList(pmsBrandPopupSearch);
	}

	/**
	 * 브랜드 목록 조회
	 * 
	 * @Method Name : getbrandList
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param brandSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<PmsBrand> getbrandList(@RequestBody BrandSearch brandSearch) throws Exception {
		brandSearch.setStoreId(SessionUtil.getStoreId());		
		return brandService.getBrandList(brandSearch);
	}

	/** 
	 * 브랜드샵 정보(이미지) 조회 
	 * 
	 * @Method Name : getBrandShopDetail
	 * @author : stella
	 * @date : 2016. 7. 15.
	 * @description :
	 *
	 * @param brandSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shop/detail", method = RequestMethod.POST)
	public PmsBrand getBrandShopDetail(@RequestBody BrandSearch brandSearch) throws Exception {
		brandSearch.setStoreId(SessionUtil.getStoreId());
		return brandService.getBrandShopDetail(brandSearch);
	}

	/**
	 * 브랜드 코너 목록 조회
	 * 
	 * @Method Name : brandCorner
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param msProductSearch
	 * @return
	 */
	@RequestMapping(value = "/{id}/corner", method = { RequestMethod.GET })
	public List<PmsProduct> brandCorner(@RequestBody PmsProductSearch msProductSearch) {
		List<PmsProduct> list = productService.getProductList(msProductSearch);

		return list;
	}

	/**
	 * 브랜드 등록
	 * 
	 * @Method Name : insertBrand
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param brand
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.POST }, produces = "text/plain")
	public String insertBrand(@RequestBody BasePmsBrand brand) throws Exception {

		brand.setStoreId(SessionUtil.getStoreId());
		brand.setDisplayYn("Y");

		brandService.insertBrand(brand);
		return brand.getBrandId();
	}

	/** 
	 * 브랜드 조회
	 * 
	 * @Method Name : getBrand
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param brandId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = { RequestMethod.GET })
	public PmsBrand getBrand(@PathVariable("id") String brandId) throws Exception {

		PmsBrand search = new PmsBrand();
		search.setBrandId(brandId);
		search.setStoreId(SessionUtil.getStoreId());
		PmsBrand brand = (PmsBrand) brandService.selectOneTable(search);
		return brand;
	}

	/**
	 * 브랜드 수정
	 * 
	 * @Method Name : updateBrand
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param brandList
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public BaseEntity updateBrand(@RequestBody List<PmsBrand> brandList) throws Exception {
		BaseEntity base = new BaseEntity();

		try {
			brandService.updateBrand(brandList);
		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}

	/**
	 * 브랜드 삭제
	 * 
	 * @Method Name : deleteBrand
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param brandList
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public BaseEntity deleteBrand(@RequestBody List<PmsBrand> brandList) throws Exception {
		BaseEntity base = new BaseEntity();

		try {
			brandService.deleteBrand(brandList);
		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}

}
