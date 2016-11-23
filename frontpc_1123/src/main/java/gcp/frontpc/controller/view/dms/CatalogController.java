package gcp.frontpc.controller.view.dms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsCode;
import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsCatalog;
import gcp.dms.model.DmsCatalogimg;
import gcp.dms.model.DmsCatalogproduct;
import gcp.dms.model.search.DmsCatalogSearch;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CatalogService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller("catalogViewController")
@RequestMapping("dms/catalog")
public class CatalogController {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private CatalogService catalogService;
	
	/**
	 * 브랜드관 템플릿 Lookbook & CoordiLook 조회
	 * 
	 * @Method Name : getCatalogueList
	 * @author : stella
	 * @date : 2016. 9. 30.
	 * @description : 
	 * 				BRAND ID 필수
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getCatalogList(@RequestBody DmsDisplaySearch search , HttpServletRequest request) throws Exception{
		
		String url = "/dms/template/inner/templateA/brandCatalogue";
		if(CommonUtil.isNotEmpty(search.getSalesAssist())) {
			url = "/dms/template/inner/salesAssist/salesCatalogue";
		}
		ModelAndView model = new ModelAndView(url);
		
		List<DmsCatalog> lookbookList = new ArrayList<DmsCatalog>();
		List<DmsCatalog> coordibookList = new ArrayList<DmsCatalog>();
		
		if (CommonUtil.isNotEmpty(search.getDirectYn()) && "Y".equals(search.getDirectYn())) {
			model.addObject("lookbookList", lookbookList);
			model.addObject("coordibookList", coordibookList);
			
			/**********************************************************************************
			 * 시즌 코드 조회
			 **********************************************************************************/
			DmsCatalogSearch catalogSearch = new DmsCatalogSearch();
			catalogSearch.setStoreId(SessionUtil.getStoreId());
			catalogSearch.setBrandId(search.getBrandId());
			
			List<CcsCode> codeList= catalogService.getBrandCatalogSeasonCd(catalogSearch);
			model.addObject("codeList", codeList);
		} else {
			/**********************************************************************************
			 * 리스트 조회 - LOOKBOOK
			 **********************************************************************************/
			DmsCatalogSearch catalogSearch = new DmsCatalogSearch();
			catalogSearch.setStoreId(SessionUtil.getStoreId());
			catalogSearch.setBrandId(search.getBrandId());
			catalogSearch.setSeasonCd(search.getCatalogueSeason());
			catalogSearch.setCatalogTypeCdCds("'CATALOG_TYPE_CD.LOOKBOOK'");
			catalogSearch.setPagingYn("N");
			
			lookbookList = catalogService.getCatalogList(catalogSearch);
			model.addObject("lookbookList", lookbookList);
			
			/**********************************************************************************
			 * 리스트 조회 - COORDI BOOK
			 **********************************************************************************/		
			catalogSearch.setCatalogTypeCdCds("'CATALOG_TYPE_CD.COORDILOOK'");
			
			coordibookList = catalogService.getCatalogList(catalogSearch);
			model.addObject("coordibookList", coordibookList);

			/**********************************************************************************
			 * 시즌 코드 조회
			 **********************************************************************************/		
			List<CcsCode> codeList= catalogService.getBrandCatalogSeasonCd(catalogSearch);
			model.addObject("codeList", codeList);
		}

		return model;		
	}	
	
	/**
	 * 브랜드관 템플릿 룩북 리스트
	 * 
	 * @Method Name : getCatalogDetailList
	 * @author : stella
	 * @date : 2016. 10. 4.
	 * @description : 
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail/list", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getCatalogDetailList(@RequestBody DmsCatalogSearch search , HttpServletRequest request) throws Exception{
		
		String url = "/dms/template/inner/template" + search.getTemplateType() + "/brandCatalogueDetail";
		if(CommonUtil.isNotEmpty(search.getSalesAssist())) {
			url = "/dms/template/inner/salesAssist/salesCatalogueDetail";
		}
		ModelAndView mv = new ModelAndView(url);

		search.setStoreId(BaseConstants.STORE_ID);
		DmsCatalog catalogItem = catalogService.getCatalogAllItem(search);
		
		mv.addObject("catalogItem", catalogItem);
		
		String directYn = CommonUtil.replaceNull(search.getDirectYn());
		String catalogId = CommonUtil.replaceNull(search.getCatalogId());
		String catalogImgNo = CommonUtil.replaceNull(search.getCatalogImgNo());
		
		mv.addObject("directYn", directYn);
		mv.addObject("catalogImgNo", catalogImgNo);
		
		String imgPath = "";
		boolean isMobile = FoSessionUtil.isMobile();
		if (CommonUtil.isNotEmpty(catalogId) && CommonUtil.isNotEmpty(catalogImgNo)) {
			DmsCatalogimg catalogItemImg = catalogService.getCatalogImgDetail(search);

			if (CommonUtil.isNotEmpty(catalogItemImg)) {
				if (isMobile) {
					imgPath = catalogItemImg.getImg2();
				} else {
					imgPath = catalogItemImg.getImg1();
				}

				mv.addObject("ogTagTitle", catalogItemImg.getName());
				mv.addObject("ogTagImage", Config.getString("image.domain") + imgPath);
				mv.addObject("ogTagUrl",
						Config.getString("front.domain.url") + "/dms/common/templateDisplay?brandId=" + catalogItem.getBrandId()
								+ "&direct=catalogDetail&catalogId=" + catalogId + "&catalogImgNo=" + catalogImgNo);
			}
		} else {
			if (catalogItem.getDmsCatalogimgs().size() > 0) {
				DmsCatalogimg catalogImg = catalogItem.getDmsCatalogimgs().get(0);

				if (isMobile) {
					imgPath = catalogImg.getImg2();
				} else {
					imgPath = catalogImg.getImg1();
				}

				mv.addObject("ogTagTitle", catalogImg.getName());
				mv.addObject("ogTagImage", Config.getString("image.domain") + imgPath);
				mv.addObject("ogTagUrl",
						Config.getString("front.domain.url") + "/dms/common/templateDisplay?brandId=" + catalogItem.getBrandId()
								+ "&direct=catalogDetail&catalogId=" + catalogImg.getCatalogId() + "&catalogImgNo="
								+ catalogImg.getCatalogImgNo());
			}
		}
				
		return mv;
	}
	
	/**
	 * 브랜드관 템플릿 룩북 리스트
	 * 
	 * @Method Name : getCatalogDetailList
	 * @author : stella
	 * @date : 2016. 10. 4.
	 * @description : 
	 * 
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail/product/list", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getCatalogDetailProductList(@RequestBody DmsCatalogSearch search , HttpServletRequest request) throws Exception{
		
		String url = "/dms/template/inner/template" + search.getTemplateType() + "/brandCatalogueProduct";
		if(CommonUtil.isNotEmpty(search.getSalesAssist())) {
			url = "/dms/template/inner/salesAssist/salesCatalogueProduct";
		}
		ModelAndView mv = new ModelAndView(url);

		search.setStoreId(BaseConstants.STORE_ID);	
		DmsCatalog catalogItem = new DmsCatalog();
		
		List<DmsCatalogimg> catalogImgList = new ArrayList<DmsCatalogimg>();
		DmsCatalogimg catalogImg = new DmsCatalogimg();
		catalogImg.setCatalogId(search.getCatalogId());
		catalogImg.setCatalogImgNo(new BigDecimal(search.getCatalogImgNo()));
		
		search.setCatalogImgNo(search.getCatalogImgNo());
		search.setPagingYn("N");
		
		List<DmsCatalogproduct> catalogProductList = catalogService.getCatalogProductList(search);
		if (catalogProductList.size() > 0){
			mv.addObject("totalCount", catalogProductList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		catalogImg.setDmsCatalogproducts(catalogProductList);

		catalogImgList.add(catalogImg);
		catalogItem.setDmsCatalogimgs(catalogImgList);

		mv.addObject("catalogItem", catalogItem);
		mv.addObject("search", search);
		
		return mv;
	}
	
}
