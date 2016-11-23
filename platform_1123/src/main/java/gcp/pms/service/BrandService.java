package gcp.pms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;

import gcp.ccs.service.BaseService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.base.BasePmsBrand;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.model.search.PmsBrandPopupSearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.pms.service
 * @FileName : BrandService.java
 * @author : dennis
 * @date : 2016. 5. 25.
 * @description : brand service
 */
@Service
public class BrandService extends BaseService {

	/**
	 * 
	 * @Method Name : getBrandList
	 * @author : dennis
	 * @date : 2016. 5. 25.
	 * @description : 브랜드 조회
	 *
	 * @param pmsBrandPopupSearch
	 * @return List<PmsBrand>
	 */
	public List<PmsBrand> getBrandList(PmsBrandPopupSearch pmsBrandPopupSearch) {
		return (List<PmsBrand>) dao.selectList("pms.brand.getBrandPopupList", pmsBrandPopupSearch);
	}

	/**
	 * 
	 * @Method Name : getBrandList
	 * @author : stella
	 * @date : 2016. 7. .
	 * @description : 브랜드 조회(그리드 데이터)
	 *
	 * @param pmsBrandPopupSearch
	 * @return List<PmsBrand>
	 */
	public List<PmsBrand> getBrandList(BrandSearch brandSearch) {
		return (List<PmsBrand>) dao.selectList("pms.brand.getBrandList", brandSearch);
	}

	public List<PmsBrand> getCouponBrandList(BrandSearch brandSearch) {
		return (List<PmsBrand>) dao.selectList("pms.brand.getCouponBrandList", brandSearch);
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
	 */
	public String insertBrand(BasePmsBrand brand) throws Exception {
		
		//이미지 real로 복사
		moveBrandImage(brand);
		
		// 브랜드 등록
		dao.insert("pms.brand.insertBrand", brand);
		return brand.getBrandId();
	}

	private void moveBrandImage(BasePmsBrand brand) {
		//이미지 복사( temp to real )
		brand.setLogoImg(FileUploadUtil.moveTempToReal(brand.getLogoImg()));
		brand.setImg1(FileUploadUtil.moveTempToReal(brand.getImg1()));
		brand.setImg2(FileUploadUtil.moveTempToReal(brand.getImg2()));
	}

	/**
	 * 브랜드 수정
	 * 
	 * @Method Name : updateBrand
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description 
	 *
	 * @param brand
	 * @throws Exception
	 */
	public void updateBrand(List<PmsBrand> brandList) throws Exception {
		String storeId = SessionUtil.getStoreId();
		
		for (PmsBrand brand : brandList) {
			if (CommonUtil.isEmpty(brand.getTemplateId())) {
				brand.setTemplateId("");
			}
			
			//이미지 real로 복사
			moveBrandImage(brand);

			brand.setStoreId(storeId);
			brand.setUpdId(SessionUtil.getLoginId());
			
			dao.updateOneTable(brand);
		}
		
	}

	/**
	 * leafYn 을 N로 수정하는데 템플릿이 존재하는지 체크
	 * 
	 * @Method Name : checkUdateLeaf
	 * @author : eddie
	 * @date : 2016. 5. 11.
	 * @description : 템플릿이 존재하는 브랜드의 leafYn 을 N로 변경 불가함.
	 *
	 * @param brandId
	 * @throws Exception
	 */
	public void checkUdateLeafN(String brandId) throws Exception {
		BasePmsBrand brand = new BasePmsBrand();
		brand.setBrandId(brandId);
		brand.setStoreId(SessionUtil.getStoreId());

		// 템플릿이 존재하는지
		BasePmsBrand result = (BasePmsBrand) dao.selectOneTable(brand);

		if (CommonUtil.isNotEmpty(result.getTemplateId())) {
			throw new ServiceException("pms.brand.update.leaf");
		}
	}

	/**
	 * leafYn 을 Y로 수정하는데 하위가 존재하는 경우 체크
	 * 
	 * @Method Name : checkUdateLeaf
	 * @author : eddie
	 * @date : 2016. 5. 11.
	 * @description :
	 *
	 * @param brandId
	 * @throws ServiceException
	 */
	public void checkUdateLeafY(String brandId) throws ServiceException {
		BrandSearch search = new BrandSearch();
		search.setBrandId(brandId);
		search.setStoreId(SessionUtil.getStoreId());

		//하위 전시가 존재하는지
		String exists1 = (String) dao.selectOne("pms.brand.checkChildBrandExist", search);
		if (CommonUtil.isNotEmpty(exists1)) {
			throw new ServiceException("pms.brand.update.child.leaf");
		}
	}

	/**
	 * 브랜드 삭제
	 * 
	 * @Method Name : deleteBrand
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description <<삭제 불가 조건>> <br/>
	 *              1. 하위 브랜드가 존재하면 삭제 불가. <br/>
	 *              2. 해당 브랜드에 속한 상품이 존재하면 삭제 불가.
	 *
	 * @param brand
	 * @throws Exception
	 */
	public void deleteBrand(List<PmsBrand> brandList) throws Exception {
		for (PmsBrand brand : brandList) {
			BrandSearch search = new BrandSearch();
			
			search.setBrandId(brand.getBrandId());
			search.setStoreId(SessionUtil.getStoreId());

			// 매핑 상품 존재할때 
			String mappingProduct = (String) dao.selectOne("pms.brand.checkBrandProductMap", search);
			if (CommonUtil.isNotEmpty(mappingProduct)) {
				throw new ServiceException("pms.brand.delete.product");
			}

			// 기획전-브랜드 매핑 존재할때 
			String exhibit = (String) dao.selectOne("pms.brand.checkExhibitBrandMap", search);
			if (CommonUtil.isNotEmpty(exhibit)) {
				throw new ServiceException("pms.brand.delete.exhibit");
			}

			String event = (String) dao.selectOne("pms.brand.checkEventBrandMap", search);
			if (CommonUtil.isNotEmpty(event)) {
				throw new ServiceException("pms.brand.delete.event");
			}

			// DB삭제
			brand.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(brand);
		}

	}

	/**
	 * ERP Brand ID 에 매핑된 브랜드 정보 조회
	 * 
	 * @Method Name : getBrandByErpBrandId
	 * @author : eddie
	 * @date : 2016. 6. 19.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public PmsBrand getBrandByErpBrandId(BrandSearch search) {
		PmsBrand pmsBrand = new PmsBrand();
		try {
			pmsBrand = (PmsBrand) dao.selectOne("pms.brand.getBrandByErpBrandId", search);
		} catch (Exception e) {
			return null;
		}
		return pmsBrand;
	}

	/**
	 * 브랜드샵 정보(이미지) 조회
	 * 
	 * @Method Name : getBrandDetail
	 * @author : stella
	 * @date : 2016. 7. 15.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public PmsBrand getBrandShopDetail(BrandSearch search) throws Exception {
		return (PmsBrand) dao.selectOne("pms.brand.getBrandDetail", search);
	}

	/**
	 * 브랜드샵 이름 조회
	 * 
	 * @Method Name : getBrandNameList
	 * @author : stella
	 * @date : 2016. 8. 23.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsBrand> getBrandNameList(BrandSearch search) throws Exception {
		return (List<PmsBrand>) dao.selectList("pms.brand.getBrandNameList", search);
	}
	
	/**
	 * 
	 * @Method Name : remakeUserId
	 * @author : stella
	 * @date : 2016. 8. 19.
	 * @description : 등록자, 최종수정자 '( )' 제거 작업
	 *
	 * @param remakeUserId
	 * @return
	 */	
	private String remakeUserId(String userId) {
		if (userId.indexOf("(") >= 0) {
			int index1 = userId.indexOf("(")+1;
			int index2 = userId.indexOf(")");
			
			return userId.substring(index1, index2); 
		} else {
			return userId;
		}
	}
	
	/**
	 * 프론트 자사 브랜드 목록조회
	 * @Method Name : getBrandCodeList
	 * @author : intune
	 * @date : 2016. 9. 30.
	 * @description : 
	 *		
	 * @param search
	 * @return
	 */
	@Cacheable(cacheName = "getBrandCodeList-cache")
	public List<PmsBrand> getBrandCodeList(BrandSearch search){
		return (List<PmsBrand>) dao.selectList("pms.brand.getBrandCodeList", search);
	}
	
	/**
	 * 프론트 추천 브랜드 목록 조회
	 * @Method Name : getBrandCodeList
	 * @author : emily
	 * @date : 2016. 11. 1.
	 * @description : 
	 *	검색 키워드로 브랜드를 조회한다.
	 * @param searchKeyword
	 * @return
	 */
	public List<PmsBrand> getRecommendBrand(String searchKeyword){
		return (List<PmsBrand>) dao.selectList("pms.brand.getRecommendBrand", searchKeyword);
	}
	
	/**
	 * @Method Name : getBrand
	 * @author : ian
	 * @date : 2016. 11. 14.
	 * @description : 단일 브랜드 조회
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public PmsBrand getBrand(PmsBrand search) throws Exception {
		return (PmsBrand) dao.selectOneTable(search);
	}
}	
