package gcp.admin.controller.rest.dms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.model.DmsCatalog;
import gcp.dms.model.DmsCatalogimg;
import gcp.dms.model.DmsCatalogproduct;
import gcp.dms.model.search.DmsCatalogSearch;
import gcp.dms.service.CatalogService;
import intune.gsf.common.utils.SessionUtil;

@RestController("CatalogController")
@RequestMapping("api/dms/catalog")
public class CatalogController {
	
	@Autowired
	CatalogService catalogService;
	
	
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
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<DmsCatalog> getCatalogList(@RequestBody DmsCatalogSearch search) {
		List<DmsCatalog> catalogList = new ArrayList<>();
		
		try {
			search.setStoreId(SessionUtil.getStoreId());
			catalogList = catalogService.getCatalogList(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return catalogList;
	}

	@RequestMapping(value = "/{catalogId}", method = RequestMethod.POST)
	public DmsCatalog getCatalogDetail(@PathVariable("catalogId") String catalogId) {
		DmsCatalog dmsCatalog = null;
		try {
			dmsCatalog = catalogService.getCatalogDetail(catalogId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dmsCatalog;
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
	@RequestMapping(value = "/img/list")
	public List<DmsCatalogimg> getCatalogImgList(@RequestBody DmsCatalogSearch search) {
		List<DmsCatalogimg>  catalogImgList = new ArrayList<>();
		
		try {
			search.setStoreId(SessionUtil.getStoreId());
			catalogImgList = catalogService.getCatalogImgList(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return catalogImgList;
	}
	
	@RequestMapping(value = "/img/catalogImgDetail", method = RequestMethod.POST)
	public DmsCatalogimg getCatalogImgDetail(@RequestBody DmsCatalogSearch search) {
		DmsCatalogimg dmsCatalogimg = null;
		try {
			dmsCatalogimg = catalogService.getCatalogImgDetail(search);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dmsCatalogimg;
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
	@RequestMapping(value = "/product/list")
	public List<DmsCatalogproduct> getCatalogProductList(@RequestBody DmsCatalogSearch search) {
		List<DmsCatalogproduct>  catalogImgList = new ArrayList<>();
		
		try {
			search.setStoreId(SessionUtil.getStoreId());
			catalogImgList = catalogService.getCatalogProductList(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return catalogImgList;
	}

	/**
	 * 
	 * @Method Name : updateCatalog
	 * @author : stella
	 * @date : 2016. 6. 28.
	 * @description : 카탈로그 정보 수정
	 *
	 * @param catalogList
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String updateCatalog(@RequestBody List<DmsCatalog> catalogList) {
		try {
			catalogService.updateCatalog(catalogList);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return Integer.toString(catalogList.size());
	}
	
	/**
	 * 
	 * @Method Name : updateCatalogImg
	 * @author : stella
	 * @date : 2016. 6. 30.
	 * @description : 카탈로그 이미지 정보 수정
	 *
	 * @param catalogList
	 * @return
	 */
	@RequestMapping(value = "/img/save", method = RequestMethod.POST)
	public String updateCatalogImg(@RequestBody List<DmsCatalogimg> catalogImgList) {
		try {
			catalogService.updateCatalogImg(catalogImgList);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return Integer.toString(catalogImgList.size());
	}
	
	@RequestMapping(value = "/img/insert", method = RequestMethod.POST)
	public void insertCatalogImg(@RequestBody DmsCatalogimg catalogImg) {
		try {
			catalogService.insertCatalogImg(catalogImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Method Name : insertCatalogImgs
	 * @author : roy
	 * @date : 2016. 11. 07.
	 * @description : 카탈로그 일괄 이미지 정보 저장
	 *
	 * @param catalogList
	 * @return
	 */
	@RequestMapping(value = "/multi/img/save", method = RequestMethod.POST)
	public String updateCatalogImgs(@RequestBody List<DmsCatalogimg> catalogImgList) {
		try {
			catalogService.insertCatalogImgs(catalogImgList);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

		return Integer.toString(catalogImgList.size());
	}

	/**
	 * 
	 * @Method Name : updateCatalogProduct
	 * @author : stella
	 * @date : 2016. 6. 30.
	 * @description : 카탈로그 상품 정보 수정
	 *
	 * @param catalogList
	 * @return
	 */
	@RequestMapping(value = "/product/save", method = RequestMethod.POST)
	public String updateCatalogProduct(@RequestBody List<DmsCatalogproduct> catalogProductList) {
		try {
			catalogService.updateCatalogProduct(catalogProductList);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return Integer.toString(catalogProductList.size());
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
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insertCatalog(@RequestBody DmsCatalog catalog) {
		try {		
			catalog.setStoreId(SessionUtil.getStoreId());
			catalogService.insertCatalog(catalog);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return catalog.getCatalogId();
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
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteCatalog(@RequestBody List<DmsCatalog> catalogList) {
		try {
			catalogService.deleteCatalog(catalogList);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return Integer.toString(catalogList.size());
	}
	
	/**
	 * 
	 * @Method Name : deleteCatalogImg
	 * @author : stella
	 * @date : 2016. 7. 4.
	 * @description : 카탈로그 이미지 삭제
	 *
	 * @param catalogImgList
	 * @return
	 */
	@RequestMapping(value = "/img/delete", method = RequestMethod.POST)
	public String deleteCatalogImg(@RequestBody List<DmsCatalogimg> catalogImgList) {
		try {
			catalogService.deleteCatalogImg(catalogImgList);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return Integer.toString(catalogImgList.size());
	}
	
	/**
	 * 
	 * @Method Name : deleteCatalogProduct
	 * @author : stella
	 * @date : 2016. 7. 4.
	 * @description : 카탈로그 상품 삭제
	 *
	 * @param catalogProductList
	 * @return
	 */
	@RequestMapping(value = "/product/delete", method = RequestMethod.POST)
	public String deleteCatalogProduct(@RequestBody List<DmsCatalogproduct> catalogProductList) {
		try {
			catalogService.deleteCatalogProduct(catalogProductList);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return Integer.toString(catalogProductList.size());
	}

}
