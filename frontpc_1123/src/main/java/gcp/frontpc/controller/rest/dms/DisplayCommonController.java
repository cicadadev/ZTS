package gcp.frontpc.controller.rest.dms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.FoSessionUtil;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CornerService;
import gcp.dms.service.DisplayCommonService;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.pms.model.PmsProduct;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/dms/common")
public class DisplayCommonController {

	protected final Log				logger	= LogFactory.getLog(getClass());

	@Autowired
	private DisplayCommonService	displayCommonService;
	
	@Autowired
	private CornerService cornerService;

	/**
	 * 프론트 공통 GNB
	 * 
	 * @Method Name : getGbnInfo
	 * @author : emily
	 * @date : 2016. 7. 25.
	 * @description :
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gbnInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public JSONObject getGbnInfo(HttpServletRequest request) throws Exception {

		JSONObject returnVal = new JSONObject();
		boolean isMobile = FoSessionUtil.isMobile(request);//PC or Mobile
		
		/****************************************************************************
		 * 전체카테고리 월령 코드 목록 조회(공통)
		 ****************************************************************************/
		returnVal.putAll(displayCommonService.getGnbInfo());

		/****************************************************************************
		 * 코너 조회(PC전용)
		 ****************************************************************************/
		if (!isMobile) {
			DmsDisplaySearch search1 = new DmsDisplaySearch();
			search1.setDisplayId(Config.getString("corner.common.ctg.img"));
			search1.setStoreId(SessionUtil.getStoreId());
			returnVal.put("corner", displayCommonService.getGnbCorner(search1));
		}
		
		//logger.debug("############: "+returnVal.toJSONString());
		return returnVal;
	}

	/**
	 * SkyScraper
	 * @Method Name : getCommonInfo
	 * @author : emily
	 * @date : 2016. 8. 2.
	 * @description : 
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "/skyScraper", method = RequestMethod.GET)
	public ModelAndView getSkyScraper(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/gsf/layout/page/inner/skyScraperList");
		
		/*****************************************************************************
		 * 찜한상품
		 *****************************************************************************/	
		MmsMemberSearch mmsSearch = new MmsMemberSearch();

		mmsSearch.setStoreId(SessionUtil.getStoreId());
		FoLoginInfo loginInfo = (FoLoginInfo) SessionUtil.getLoginInfo(request);
		if (loginInfo != null) {
			mmsSearch.setMemberId(SessionUtil.getLoginId());
			mmsSearch.setMemberNo(SessionUtil.getMemberNo());
//			mmsSearch.setMemberNo(new BigDecimal(3955857));
			mmsSearch.setMemberShipYn(FoSessionUtil.getMembershipYn());
			mmsSearch.setMemGradeCd(FoSessionUtil.getMemGradeCd());
		}
		
		List<PmsProduct> wishList = displayCommonService.getWishList(mmsSearch);
		mv.addObject("wishCnt", wishList.size());
		mv.addObject("wishList", wishList);
		
		/*****************************************************************************
		 * 내지갑 _회원 정보 
		 *****************************************************************************/	
		//MmsMemberZts wallet = memberService.getMemberWalletInfo(mmsSearch);
		//mv.put("wallet", wallet);
		
		/*****************************************************************************
		 * 추천상품
		 *****************************************************************************/
		
		/*****************************************************************************
		 * 최근본 상품
		 *****************************************************************************/	
		CookieUtil cookie = new CookieUtil();
		String productIds = cookie.getCookieList(request, response, "latestProduct", 20);
		logger.debug("\t pp >> " + productIds);
		if(CommonUtil.isNotEmpty(productIds)) {
			mmsSearch.setProductIds(productIds);
			
			List<PmsProduct> latestProductList = (List<PmsProduct>) displayCommonService.getLatestProductList(mmsSearch, "SKY_SCRAPER");
			mv.addObject("latestProductListSize", latestProductList.size());
			mv.addObject("latestProductList", latestProductList);
		}
		
		
		
		return mv;
	}
	
}
