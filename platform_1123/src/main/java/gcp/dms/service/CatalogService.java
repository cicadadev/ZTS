package gcp.dms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsCode;
import gcp.ccs.service.BaseService;
import gcp.dms.model.DmsCatalog;
import gcp.dms.model.DmsCatalogimg;
import gcp.dms.model.DmsCatalogproduct;
import gcp.dms.model.search.DmsCatalogSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

@Service("CatalogService")
public class CatalogService extends BaseService {
	
	/**
	 * 
	 * @Method Name : getCatalogList
	 * @author : stella
	 * @date : 2016. 6. 27.
	 * @description : 브랜드 컨텐츠(카탈로그) 조회
	 *
	 * @param search
	 * @return
	 */
	public List<DmsCatalog> getCatalogList(DmsCatalogSearch search) throws Exception {
		return (List<DmsCatalog>) dao.selectList("dms.catalog.getCatalogAll", search);
	}

	/**
	 * 
	 * @Method Name : getCatalogDetail
	 * @author : allen
	 * @date : 2016. 8. 22.
	 * @description : 카탈로그 상세
	 *
	 * @param catalogId
	 * @return
	 */
	public DmsCatalog getCatalogDetail(String catalogId) {
		return (DmsCatalog) dao.selectOne("dms.catalog.getCatalogDetail", catalogId);
	}

	/**
	 * 
	 * @Method Name : getCatalogImgList
	 * @author : stella
	 * @date : 2016. 6. 29.
	 * @description : 카탈로그 이미지 조회
	 *
	 * @param search
	 * @return
	 */
	public List<DmsCatalogimg> getCatalogImgList(DmsCatalogSearch search) throws Exception {
		return (List<DmsCatalogimg>) dao.selectList("dms.catalog.getCatalogImg", search);
	}
	
	public DmsCatalogimg getCatalogImgDetail(DmsCatalogSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return (DmsCatalogimg) dao.selectOne("dms.catalog.getCatalogImgDetail", search);
	}

	/**
	 * 
	 * @Method Name : getCatalogProductList
	 * @author : stella
	 * @date : 2016. 6. 29.
	 * @description : 카탈로그 상품 조회
	 *
	 * @param search
	 * @return
	 */
	public List<DmsCatalogproduct> getCatalogProductList(DmsCatalogSearch search) throws Exception {
		return (List<DmsCatalogproduct>) dao.selectList("dms.catalog.getCatalogProduct", search);
	}
	
	/**
	 * 
	 * @Method Name : updateCatalog
	 * @author : stella
	 * @date : 2016. 6. 28.
	 * @description : 카탈로그 정보 수정
	 *
	 * @param catalog
	 * @return
	 */
	public void updateCatalog(List<DmsCatalog> catalogList) throws Exception {
		for (DmsCatalog catalog : catalogList) {
			catalog.setStoreId(SessionUtil.getStoreId());
			
			if (CommonUtil.isNotEmpty(catalog.getImg1())) {
				catalog.setImg1(moveCatalogImage(catalog.getImg1()));
			}
			if (CommonUtil.isNotEmpty(catalog.getImg2())) {
				catalog.setImg2(moveCatalogImage(catalog.getImg2()));
			}
			
			catalog.setUpdId(SessionUtil.getLoginId());
			dao.updateOneTable(catalog);
		}	
	}
	
	/**
	 * 
	 * @Method Name : updateCatalogImg
	 * @author : stella
	 * @date : 2016. 6. 28.
	 * @description : 카탈로그 이미지 정보 수정
	 *
	 * @param catalogImgList
	 * @return
	 */
	public void updateCatalogImg(List<DmsCatalogimg> catalogImgList) throws Exception {		
		String storeId = SessionUtil.getStoreId();

		for (DmsCatalogimg catalogImg : catalogImgList) {
			catalogImg.setStoreId(storeId);
			if (CommonUtil.isNotEmpty(catalogImg.getImg1())) {
				catalogImg.setImg1(moveCatalogImage(catalogImg.getImg1()));
			}
			if (CommonUtil.isNotEmpty(catalogImg.getImg2())) {
				catalogImg.setImg2(moveCatalogImage(catalogImg.getImg2()));
			}
			catalogImg.setUpdId(SessionUtil.getLoginId());
			dao.updateOneTable(catalogImg);
		}	
	}

	public void insertCatalogImg(DmsCatalogimg catalogImg) throws Exception {
		String storeId = SessionUtil.getStoreId();
		catalogImg.setStoreId(storeId);
		if (CommonUtil.isNotEmpty(catalogImg.getImg1())) {
			catalogImg.setImg1(moveCatalogImage(catalogImg.getImg1()));
		}
		if (CommonUtil.isNotEmpty(catalogImg.getImg2())) {
			catalogImg.setImg2(moveCatalogImage(catalogImg.getImg2()));
		}
		dao.insertOneTable(catalogImg);

	}

	public void insertCatalogImgs(List<DmsCatalogimg> catalogImgList) throws Exception {
		DmsCatalogimg catalogImg = new DmsCatalogimg();
		catalogImg.setStoreId(SessionUtil.getStoreId());
		catalogImg.setCatalogId(catalogImgList.get(0).getCatalogId());

		if (CommonUtil.isNotEmpty(catalogImgList.get(0).getCatalogId())) {
			dao.delete("dms.catalog.deleteCatalogImg", catalogImg);
		}

		for (DmsCatalogimg info : catalogImgList) {
			String storeId = SessionUtil.getStoreId();
			info.setStoreId(storeId);
			if (CommonUtil.isNotEmpty(info.getImg1())) {
				info.setImg1(moveCatalogImage(info.getImg1()));
			}
			if (CommonUtil.isNotEmpty(info.getImg2())) {
				info.setImg2(moveCatalogImage(info.getImg2()));
			}
			dao.insertOneTable(info);
		}
	}

	/**
	 * 
	 * @Method Name : updateCatalogProduct
	 * @author : stella
	 * @date : 2016. 6. 28.
	 * @description : 카탈로그 상품 정보 수정
	 *
	 * @param catalogProductList
	 * @return
	 */
	public void updateCatalogProduct(List<DmsCatalogproduct> catalogProductList) throws Exception {		
		String storeId = SessionUtil.getStoreId();
		
		for (DmsCatalogproduct catalogProduct : catalogProductList) {
			catalogProduct.setStoreId(storeId);
			
			if ("I".equals(catalogProduct.getCrudType())) {
				dao.insertOneTable(catalogProduct);
			} else {
				catalogProduct.setUpdId(SessionUtil.getLoginId());
				dao.updateOneTable(catalogProduct);
			}			
		}
	}
		
	/**
	 * 
	 * @Method Name : insertCatalog
	 * @author : stella
	 * @date : 2016. 6. 29.
	 * @description : 카탈로그 등록
	 *
	 * @param catalog
	 * @return
	 */	
	public void insertCatalog(DmsCatalog catalog) throws Exception {
		if (CommonUtil.isNotEmpty(catalog.getImg1())) {
			catalog.setImg1(moveCatalogImage(catalog.getImg1()));
		}
		if (CommonUtil.isNotEmpty(catalog.getImg2())) {
			catalog.setImg2(moveCatalogImage(catalog.getImg2()));
		}
		
		if (CommonUtil.isEmpty(catalog.getCatalogId())) {
			dao.insert("dms.catalog.insertCatalog", catalog);
		} else {
			catalog.setUpdId(SessionUtil.getLoginId());
			dao.updateOneTable(catalog);
		}
		
	}

	/**
	 * 
	 * @Method Name : deleteCatalog
	 * @author : stella
	 * @date : 2016. 6. 29.
	 * @description : 카탈로그 삭제
	 *
	 * @param catalogList
	 * @return
	 */
	public void deleteCatalog(List<DmsCatalog> catalogList) throws Exception {
		String storeId = SessionUtil.getStoreId();

		DmsCatalogSearch search = new DmsCatalogSearch();
		for (DmsCatalog catalog : catalogList) {
			search.setStoreId(storeId);
			search.setCatalogId(catalog.getCatalogId());
			search.setFirstRow(1);
			search.setLastRow(100);			

			catalog.setDmsCatalogimgs(getCatalogImgList(search));
			if (CommonUtil.isNotEmpty(catalog.getDmsCatalogimgs())) {
				for (DmsCatalogimg catalogImg : catalog.getDmsCatalogimgs()) {
					search.setCatalogImgNo(catalogImg.getCatalogImgNo().toString());
					
					// 카탈로그 상품 삭제					
					catalogImg.setDmsCatalogproducts(getCatalogProductList(search));
					if (CommonUtil.isNotEmpty(catalogImg.getDmsCatalogproducts())) {
						catalogImg.getDmsCatalogproducts().get(0).setCatalogId(search.getCatalogId());
						catalogImg.getDmsCatalogproducts().get(0).setCatalogImgNo(catalogImg.getCatalogImgNo());
						deleteCatalogProduct(catalogImg.getDmsCatalogproducts());
					}
				}
				
				// 카탈로그 이미지 삭제
				deleteCatalogImg(catalog.getDmsCatalogimgs());
			}
			
			// 카탈로그 삭제
			catalog.setStoreId(storeId);
			dao.deleteOneTable(catalog);
		}
		
	}
	
	/**
	 * 
	 * @Method Name : deleteCatalogImg
	 * @author : stella
	 * @date : 2016. 7. 6.
	 * @description : 카탈로그 이미지 삭제
	 *
	 * @param catalogImgList
	 * @return
	 */
	public void deleteCatalogImg(List<DmsCatalogimg> catalogImgList) throws Exception {	
		String storeId = SessionUtil.getStoreId();
		
		for (DmsCatalogimg catalogImg : catalogImgList) {
			catalogImg.setStoreId(storeId);
			
			DmsCatalogSearch search = new DmsCatalogSearch();
			search.setStoreId(storeId);
			search.setCatalogId(catalogImg.getCatalogId());
			search.setCatalogImgNo(catalogImg.getCatalogImgNo().toString());
			catalogImg.setDmsCatalogproducts(getCatalogProductList(search));
			
			// 카탈로그 상품 삭제
			if (CommonUtil.isNotEmpty(catalogImg.getDmsCatalogproducts())) {
				catalogImg.getDmsCatalogproducts().get(0).setCatalogId(catalogImg.getCatalogId());
				catalogImg.getDmsCatalogproducts().get(0).setCatalogImgNo(catalogImg.getCatalogImgNo());
				deleteCatalogProduct(catalogImg.getDmsCatalogproducts());
			}
			
			// 카탈로그 이미지 삭제
			dao.deleteOneTable(catalogImg);
		}
		
	}
	
	/**
	 * 
	 * @Method Name : deleteCatalogProduct
	 * @author : stella
	 * @date : 2016. 7. 6.
	 * @description : 카탈로그 상품 삭제
	 *
	 * @param catalogProductList
	 * @return
	 */
	public void deleteCatalogProduct(List<DmsCatalogproduct> catalogProductList) throws Exception {
		String storeId = SessionUtil.getStoreId();
		
		for (DmsCatalogproduct catalogProduct : catalogProductList) {
			catalogProduct.setStoreId(storeId);
			catalogProduct.setCatalogId(catalogProductList.get(0).getCatalogId());
			catalogProduct.setCatalogImgNo(catalogProductList.get(0).getCatalogImgNo());
			
			dao.deleteOneTable(catalogProduct);
		}
		
	}

	/**
	 * 
	 * @Method Name : getCatalogAllItem
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : LOOKBOOK, COORDIBOOK 이미지 배너 및 상품까지 조회
	 *
	 * @param catalogProductList
	 * @return
	 */
	public DmsCatalog getCatalogAllItem(DmsCatalogSearch search) throws Exception {
		return (DmsCatalog) dao.selectOne("dms.catalog.getCatalogItem", search);
	}

	/**
	 * 
	 * @Method Name : getBrandCatalogSeasonCd
	 * @author : stella
	 * @date : 2016. 10. 15.
	 * @description : 브랜드 LOOKBOOK, COORDIBOOK 시즌코드만 조회
	 *
	 * @param catalogProductList
	 * @return
	 */
	public List<CcsCode> getBrandCatalogSeasonCd(DmsCatalogSearch search) throws Exception {
		return (List<CcsCode>) dao.selectList("dms.catalog.getBrandCatalogSeasonCd", search);
	}

	/**
	 * 
	 * @Method Name : moveCatalogImage
	 * @author : stella
	 * @date : 2016. 6. 29.
	 * @description : 이미지 복사( temp to real )
	 *
	 * @param catalogProductList
	 * @return
	 */
	private String moveCatalogImage(String tempImgUrl) {	
		String realImgUrl = FileUploadUtil.moveTempToReal(tempImgUrl);

		return realImgUrl;
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
}
