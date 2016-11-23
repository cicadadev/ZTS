package gcp.frontpc.controller.view.dms;

import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsMessage;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.ccs.service.CommonService;
import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplayitem;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.DmsExhibitcoupon;
import gcp.dms.model.DmsExhibitmainproduct;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.CornerService;
import gcp.dms.service.ExhibitService;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsBrand;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("exhibitViewController")
@RequestMapping("dms/exhibit")
public class ExhibitController {


	@Autowired
	private ExhibitService	exhibitService;
	
	@Autowired
	private CornerService	cornerService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private CommonService	commonService;

	/**
	 * 
	 * @Method Name : getMain
	 * @author : intune
	 * @date : 2016. 9. 24.
	 * @description : 기획전 메인
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main")
	public ModelAndView getMain(@ModelAttribute("dmsDisplaySearch") DmsExhibitSearch search, HttpServletRequest request)throws Exception {
		ModelAndView mv = new ModelAndView();
		
		String mobileType = CommonUtil.replaceNull(request.getParameter("type"), "");
		
		if(CommonUtil.equals("mainCon", mobileType)){
			mv.setViewName("/dms/exhibit/inner/exhibitMain.mainCon");
		}else{
			mv.setViewName(CommonUtil.makeJspUrl(request));
		}

		if (FoSessionUtil.isMobile()) {

			// MOBILE 일 경우 메인의 빅배너 사용
			String mobileBigBanner = Config.getString("corner.main.banner.img.6");
			DmsDisplaySearch displaySearch = new DmsDisplaySearch();
			displaySearch.setStoreId(SessionUtil.getStoreId());
			displaySearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
			displaySearch.setDisplayId(mobileBigBanner);
			List<DmsDisplay> mbBigBannerList = cornerService.getCommonCornerList(displaySearch);
			mv.addObject("mbBigBannerList", mbBigBannerList);
		}

		// 상단 이미지 배너 - 기획전 코너 조회
		String exhibitCornerId = Config.getString("corner.exhibit.exhibit.exhibit.1");
		List<DmsDisplayitem> itemList = new ArrayList<DmsDisplayitem>();
		DmsDisplaySearch displaySearch = new DmsDisplaySearch();
		displaySearch.setStoreId(SessionUtil.getStoreId());
		displaySearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		displaySearch.setDisplayId(exhibitCornerId);
		List<DmsDisplay> cornerList = cornerService.getCommonCornerList(displaySearch);

		// 브랜드 리스트 조회
		List<PmsBrand> brandList = exhibitService.getExhibitBrandList();

		mv.addObject("cornerList", cornerList);
		mv.addObject("brandList", brandList);
		
		return mv;
	}

	/**
	 * 
	 * @Method Name : deliveryAddressAjax
	 * @author : intune
	 * @date : 2016. 9. 24.
	 * @description : 기획전 리스트 조회
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list/ajax", method = RequestMethod.GET)
	public ModelAndView deliveryAddressAjax(HttpServletRequest request, DmsExhibitSearch search) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/exhibit/inner/exhibitList");

		if (FoSessionUtil.isMobile()) {
			search.setMobileYn(BaseConstants.YN_Y);
			search.setCurrentPage(search.getCurrentPage() + 1);
		} else {
			if (search.getCurrentPage() == 0) {
				search.setCurrentPage(1);
			}
			search.setMobileYn(BaseConstants.YN_N);
		}
		search.setPageSize(30);
		search.setStoreId(SessionUtil.getStoreId());
		List<DmsExhibit> exhibitList = exhibitService.getMainExhibitList(search);
		mv.addObject("exhibitList", exhibitList);
		mv.addObject("search", search);
		if (exhibitList != null && exhibitList.size() > 0) {
			mv.addObject("totalCount", exhibitList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}

	/**
	 * 
	 * @Method Name : exhibitDetail
	 * @author : intune
	 * @date : 2016. 9. 24.
	 * @description :
	 *
	 * @param request
	 * @param search
	 * @return 기획전 상세
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ModelAndView exhibitDetail(HttpServletRequest request, HttpServletResponse response, DmsExhibitSearch search) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		CcsMessage ccsMessage = new CcsMessage();
		String groupNo = "";
		try {
			search.setSystem(BaseConstants.SYSTEM_FO);
			search.setStoreId(SessionUtil.getStoreId());
			ccsMessage.setSuccess(true);

			search.setPagingYn(BaseConstants.YN_N);
			DmsExhibit exhibitInfo = exhibitService.getFrontExhibitInfo(search);
			if (!FoSessionUtil.isMobile(request)) {
				if (StringUtils.isNotEmpty(search.getGroupNo())) {
					groupNo = search.getGroupNo();
					search.setGroupNo("");
				}
			}
			DmsExhibit exhibitDetail = exhibitService.getFrontExhibitDetail(search);

			if (exhibitInfo != null) {

				// 허용 설정 체크
				if (CommonUtil.isNotEmpty(exhibitInfo.getControlNo())) {
					String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);
					String memGradeCd = FoSessionUtil.getMemGradeCd();

					CcsControlSearch control = new CcsControlSearch();
					List<String> memberTypeCds = new ArrayList();
					memberService.getMemberTypeInfo(memberTypeCds);

					control.setExceptionFlag(false);
					control.setMemberTypeCds(memberTypeCds);
					control.setStoreId(SessionUtil.getStoreId());
					control.setChannelId(SessionUtil.getChannelId());
					control.setControlNo(exhibitInfo.getControlNo());
					control.setDeviceTypeCd(deviceTypeCd);
					if (SessionUtil.getMemberNo() != null) {
						control.setMemGradeCd(memGradeCd);
					} else {
						control.setMemGradeCd(BaseConstants.MEM_GRADE_CD_WELCOME);
					}

					control.setMemberNo(SessionUtil.getMemberNo());
					control.setControlType("EXHIBIT");

					if (!commonService.checkControl(control)) {
						throw new ServerException("진행중인 기획전이 아닙니다.");
					}
				}

				if (BaseConstants.EXHIBIT_TYPE_CD_COUPON.equals(exhibitInfo.getExhibitTypeCd())) {
					// 쿠폰 기획전 쿠폰 정보 조회
					List<DmsExhibitcoupon> couponList = exhibitService.getExhibitCouponList(search);
					mv.addObject("couponList", couponList);
				} else if (BaseConstants.EXHIBIT_TYPE_CD_ONEDAY.equals(exhibitInfo.getExhibitTypeCd())) {
					// 원데이 대표상품 조회
					List<DmsExhibitmainproduct> mainProductList = exhibitService.getExhibitMainProductList(search);
					mv.addObject("mainProductList", mainProductList);
				}
				mv.addObject("exhibitInfo", exhibitInfo);
				mv.addObject("exhibitDetail", exhibitDetail);
				search.setGroupNo(groupNo);
				mv.addObject("search", search);

				if (exhibitDetail != null && exhibitDetail.getTotalCount() > 0) {
					mv.addObject("totalCount", exhibitDetail.getTotalCount());
				} else {
					mv.addObject("totalCount", 0);
				}
				
				if(FoSessionUtil.isMobile(request)) {
					String temp = "EXHIBIT," + exhibitInfo.getExhibitId();
					CookieUtil.createCookieString(request, response, "moHistory", temp, CookieUtil.SECONDS_OF_10YEAR, new BigDecimal(3650));
				}
				
			} else {
				throw new ServerException("진행중인 기획전이 아닙니다.");
			}

		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
			mv.addObject(ccsMessage);
		}


		return mv;
	}

	/**
	 * 
	 * @Method Name : productList
	 * @author : intune
	 * @date : 2016. 9. 28.
	 * @description :
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/list/ajax", method = RequestMethod.POST)
	public ModelAndView productList(HttpServletRequest request, @RequestBody DmsExhibitSearch search) throws Exception {
		ModelAndView mv = new ModelAndView("/dms/exhibit/inner/exhibitProductList");
		search.setStoreId(SessionUtil.getStoreId());
		search.setPagingYn(BaseConstants.YN_N);
		DmsExhibit exhibitDetail = exhibitService.getFrontExhibitDetail(search);
		mv.addObject("exhibitDetail", exhibitDetail);
		mv.addObject("search", search);

		return mv;
	}
	
	/**
	 * 브랜드관 템플릿 exhibit 조회(모바일용)
	 * 
	 * @Method Name : getBrandCornerList
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
	@RequestMapping(value = "/brand/list", method = { RequestMethod.GET, RequestMethod.POST } )
	public ModelAndView getBrandCornerList(@RequestBody DmsDisplaySearch search , HttpServletRequest request) throws Exception{
		ModelAndView model = new ModelAndView("/dms/template/inner/template" + search.getTemplateTypeCd() + "/brandExhibit_mo");
		
		/**********************************************************************************
		 * 리스트 조회 - EXHIBIT
		 **********************************************************************************/
		DmsExhibitSearch exhibitSearch = new DmsExhibitSearch();
		exhibitSearch.setStoreId(SessionUtil.getStoreId());
		exhibitSearch.setBrandId(search.getBrandId());
		exhibitSearch.setPagingYn("N");
		
		List<DmsExhibit> exhibitList = exhibitService.getBrandExhibitInfo(exhibitSearch);
		model.addObject("exhibitList", exhibitList);
		
		return model;
	}

}
