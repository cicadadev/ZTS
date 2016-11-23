package gcp.frontpc.controller.view.ccs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsCodegroup;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.CodeService;
import gcp.ccs.service.CommonService;
import gcp.ccs.service.NoticeService;
import gcp.ccs.service.OffshopService;
import gcp.ccs.service.PopupNoticeService;
import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.CategoryService;
import gcp.dms.service.CornerService;
import gcp.dms.service.ExhibitService;
import gcp.dms.service.SpecialService;
import gcp.external.kainess.vo.GviaDistrictVo;
import gcp.external.kainess.vo.GviaPostSearchNewVo;
import gcp.external.model.search.AddressSearch;
import gcp.external.service.AddressService;
import gcp.external.service.RecobelRecommendationService;
import gcp.frontpc.common.contants.Constants;
import gcp.mms.model.MmsInterestoffshop;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsStyle;
import gcp.mms.model.search.MmsChildrenSearch;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.model.search.MmsStyleSearch;
import gcp.mms.service.MemberService;
import gcp.mms.service.StyleService;
import gcp.oms.model.OmsPosorder;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.model.search.OmsRegularSearch;
import gcp.oms.service.OrderService;
import gcp.oms.service.RegularService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsCategory;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsReview;
import gcp.pms.model.custom.PmsOffpickupProduct;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.model.search.PmsCategorySearch;
import gcp.pms.model.search.PmsProductReviewSearch;
import gcp.pms.service.BrandService;
import gcp.pms.service.ProductReviewService;
import gcp.pms.service.StandardCategoryService;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsEventgift;
import gcp.sps.model.search.SpsDealProductSearch;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.service.DealService;
import gcp.sps.service.EventService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("commonViewController")
@RequestMapping("/ccs/common")
public class CommonController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private OffshopService				offshopService;
	@Autowired
	private AddressService				addressService;
	@Autowired
	private OrderService				orderService;
	@Autowired
	private EventService				eventService;
	@Autowired
	private MemberService				memberService;
	@Autowired
	private PopupNoticeService			popupService;
	@Autowired
	private ProductReviewService		productReviewService;
	@Autowired
	private CommonService				commonService;
	@Autowired
	private StandardCategoryService		standardCategoryService;
	@Autowired
	private CategoryService				displayCategoryService;
	@Autowired
	private CodeService					codeService;
	@Autowired
	private BrandService 				brandService;
	@Autowired
	private CornerService				cornerService;
	@Autowired
	private DealService					dealService;
	@Autowired
	private NoticeService 				noticeService;
	@Autowired
	private SpecialService				specialService;
	@Autowired
	private StyleService			styleService;
	@Autowired
	private RegularService			regularService;
	@Autowired
	private ExhibitService			exhibitService;	
	@Autowired
	private RecobelRecommendationService 	recobelRecommendationService;


	@RequestMapping(value = "/error")
	public ModelAndView error(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) throws Exception {
		if (FoSessionUtil.isMobile()) {
			return mainMo(request);//new
			//return mainPc(request);//old
		} else {
			return mainPc(request);//old
		}
	}

	public ModelAndView mainMo(HttpServletRequest request) throws Exception {

		/***************************************************************************
		 * 1. 메인
		 ***************************************************************************/
		//공통변수
		ModelAndView model = new ModelAndView();
		String url = CommonUtil.makeJspUrl(request);
		logger.info("## return View :: " + url + ".mobile");
		//Return View
		model.setViewName(url + ".mobile");

		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);

		/***************************************************************************
		 * 1-1. 메인 공통 > 공지사항 팝업 노출
		 ***************************************************************************/
		String loginYn = CommonUtil.replaceNull(request.getParameter("login"), "");
		String returnUrl = CommonUtil.replaceNull(request.getParameter("returnUrl"), "");

		if ("Y".equals(loginYn)) {
			model.addObject("login", loginYn);
			model.addObject("returnUrl", CommonUtil.isNotEmpty(returnUrl) ? returnUrl : "");
		}

		/***************************************************************************
		 * 1-2. 메인 공통 > 메인 화면 코너 아이템 조회
		 ***************************************************************************/
		model.addObject("cornerMap", cornerService.getMainCornerMap(search));

		/***************************************************************************
		 * 1-3. 메인 공통 > 전체 카테고리 목록 조회
		 ***************************************************************************/
		List<DmsDisplaycategory> categoryList = displayCategoryService.getAllCategoryList(search);
		List<DmsDisplaycategory> ctgList = displayCategoryService.getDepthCategoryList(search, categoryList);
		model.addObject("ctgList", ctgList);
//
		/***************************************************************************
		 * 1-4. 메인 공통 > 월령 코드 목록 조회 - 상품의 월령코드 목록만 조회함.
		 ***************************************************************************/
		CcsCodeSearch codeSearch = new CcsCodeSearch();
		codeSearch.setCdGroupCd("AGE_TYPE_CD");
		codeSearch.setStoreId(SessionUtil.getStoreId());
		CcsCodegroup ageCode = codeService.getFrontCodeList(codeSearch);
		model.addObject("ageCodeList", ageCode.getCcsCodes());
//
		/***************************************************************************
		 * 1-5. 메인 공통 > 브랜드 목록 조회
		 ***************************************************************************/
		BrandSearch brandSearch = new BrandSearch();
		List<PmsBrand> brandCode = brandService.getBrandCodeList(brandSearch);
		model.addObject("brandCodeList", brandCode);
//
		/***************************************************************************
		 * 1-7. 메인 공통 > 공지사항
		 ***************************************************************************/
		CcsNoticeSearch notiSearch = new CcsNoticeSearch();
		notiSearch.setStoreId(SessionUtil.getStoreId());
		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String yesterDay = date.getAddDate(date.FORMAT_2, currentDate, new BigDecimal(-1));
		notiSearch.setNewDt(yesterDay);
		notiSearch.setLastRow(1);
		List<CcsNotice> list = noticeService.getNoticeListSearch(notiSearch);
		model.addObject("noticeList", list);

		model.addObject("mainStr", "main");
		return model;

	}

	/**
	 * 메인 화면_ pc
	 * 
	 * @Method Name : main
	 * @author : emily
	 * @date : 2016. 8. 18.
	 * @description :
	 *
	 * @param request
	 * @return
	 */
	public ModelAndView mainPc(HttpServletRequest request) throws Exception {
		
		/***************************************************************************
		 * 1. 메인
		 ***************************************************************************/
		//공통변수
		ModelAndView model = new ModelAndView();
		boolean isMoblie = FoSessionUtil.isMobile(request);
		String url = CommonUtil.makeJspUrl(request);
		
		//Return View
		if(isMoblie){
			model.setViewName(url+".mobile");
		}else{
			model.setViewName(url+".pc");
		}
		
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		search.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		
		/***************************************************************************
		 * 1-1. 메인 공통 > 공지사항 팝업 노출
		 ***************************************************************************/
		String loginYn = CommonUtil.replaceNull(request.getParameter("login"), "");
		String returnUrl = CommonUtil.replaceNull(request.getParameter("returnUrl"), "");
		
		if("Y".equals(loginYn)){
			model.addObject("login", loginYn);
			model.addObject("returnUrl", CommonUtil.isNotEmpty(returnUrl)?returnUrl:"");
		}
		
		/*
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		float periodTime = (endTime - startTime) / 1000.0f;
		logger.info("### 공지사항 팝업 노출 :  " + periodTime + "Sec   ");
		*/
		
		/***************************************************************************
		 * 1-2. 메인 공통 > 메인 화면 코너 아이템 조회
		 ***************************************************************************/
		model.addObject("cornerMap", cornerService.getMainCornerMap(search));
		
		/***************************************************************************
		 * 1-3. 메인 공통 > 전체 카테고리 목록 조회 
		 ***************************************************************************/
		List<DmsDisplaycategory> categoryList = displayCategoryService.getAllCategoryList(search);
		List<DmsDisplaycategory> ctgList =  displayCategoryService.getDepthCategoryList(search, categoryList);
		model.addObject("ctgList", ctgList);
		
		/***************************************************************************
		 * 1-4. 메인 공통 > 월령 코드 목록 조회 - 상품의 월령코드 목록만 조회함. 
		 ***************************************************************************/
		CcsCodeSearch codeSearch = new CcsCodeSearch();
		codeSearch.setCdGroupCd("AGE_TYPE_CD");
		codeSearch.setStoreId(SessionUtil.getStoreId());
		CcsCodegroup ageCode = codeService.getFrontCodeList(codeSearch);
		model.addObject("ageCodeList", ageCode.getCcsCodes());
		
		/***************************************************************************
		 * 1-5. 메인 공통 > 브랜드 목록 조회  
		 ***************************************************************************/
		BrandSearch brandSearch = new BrandSearch(); 
		List<PmsBrand> brandCode = brandService.getBrandCodeList(brandSearch);
		model.addObject("brandCodeList", brandCode);
		
		/***************************************************************************
		 * 1-6. 메인 공통 > 최근클릭추천상품 목록을 위한 쿠키 정보  
		 ***************************************************************************/
		String RB_PCID = CookieUtil.getCookieValue(request,"RB_PCID");
		model.addObject("RB_PCID", RB_PCID);
		
		/***************************************************************************
		 * 1-7. 메인 공통 > 공지사항  
		 ***************************************************************************/
		CcsNoticeSearch notiSearch = new CcsNoticeSearch();
		notiSearch.setStoreId(SessionUtil.getStoreId());
		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String yesterDay = date.getAddDate(date.FORMAT_2, currentDate, new BigDecimal(-1));
		notiSearch.setNewDt(yesterDay);
		notiSearch.setLastRow(1);
		List<CcsNotice> list = noticeService.getNoticeListSearch(notiSearch);
		model.addObject("noticeList", list);
		
		/**************************************************************************
		 * 1-8. 메인 공통 > 쇼킹제로
		 **************************************************************************/
		SpsDealProductSearch darlSearch = new SpsDealProductSearch();
		darlSearch.setStoreId(SessionUtil.getStoreId());
		if (isMoblie) {
			darlSearch.setPageSize(20 * darlSearch.getCurrentPage());
		} else {
			darlSearch.setFirstRow(1);
			darlSearch.setSortType("popular");
			darlSearch.setPagingYn("Y");
			darlSearch.setPageSize(3);
		}
		List<SpsDealproduct> shockingProducts = dealService.getShockingzeroProductList(darlSearch);
		model.addObject("shockingProducts", shockingProducts);
			
		/**************************************************************************
		 * 1-9. 메인PC > 매장픽업관 상품목록 조회
		 **************************************************************************/
		List<PmsProduct> pickList  = specialService.getMainPickupPrdList(search);
		model.addObject("pickup",pickList);
		
		int pickMaxPage = pickList.size()/2;
		if (pickList.size() % 2 != 0) {
			pickMaxPage++;
		}
		model.addObject("pickupTotal",pickMaxPage);

		/**************************************************************************
		 * 1-10. 메인PC > giftShop 상품목록 조회
		 **************************************************************************/
		search.setPageSize(6);
		List<PmsProduct> themeProductList = specialService.getThemeProductList(search);
		model.addObject("giftList", themeProductList);
		
		int giftMaxPage = themeProductList.size()/2;
		if (themeProductList.size() % 2 != 0) {
			giftMaxPage++;
		}
		model.addObject("giftTotal", giftMaxPage);
		
		return model;
		
	}

	/**
	 * 개인정보 취급방침
	 * @Method Name : personalInfo
	 * @author : emily
	 * @date : 2016. 8. 18.
	 * @description : 
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/privacy")
	public ModelAndView privacy(HttpServletRequest request){
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;
	}
	
	/**
	 * 이용약관
	 * @Method Name : agreement
	 * @author : emily
	 * @date : 2016. 8. 18.
	 * @description : 
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/service")
	public ModelAndView agreement(HttpServletRequest request){
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;
	}
	
	/**
	 * 입점제휴안내
	 * 
	 * @Method Name : alliance
	 * @author : emily
	 * @date : 2016. 8. 18.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/alliance")
	public ModelAndView alliance(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		return mv;
	}

	/**
	 * 입점제휴안내 카테고리 셀렉터 ajax
	 * 
	 * @Method Name : getAllianceSearch
	 * @author : roy
	 * @date : 2016. 9. 20.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/alliance/select/ajax", method = RequestMethod.GET)
	public ModelAndView getAllianceSearch(PmsCategorySearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/inner/allianceSelector");

		// 셀럭터 선택값
		mv.addObject("search", search);

		search.setStoreId(SessionUtil.getStoreId());

		List<PmsCategory> list = standardCategoryService.getCategory(search);

		// 전체 표준카테고리 리스트
		mv.addObject("category", list);

		return mv;
	}

	/**
	 * 매장 픽업 선택 레이어
	 * 
	 * @Method Name : pickupLayer
	 * @author : eddie
	 * @date : 2016. 9. 2.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pickup/layer", method = RequestMethod.POST)
	public ModelAndView pickupLayer(@RequestBody List<CcsOffshopSearch> searchList, HttpServletRequest request) {
		List<PmsOffpickupProduct> pickupProducts = (List<PmsOffpickupProduct>) offshopService
				.getSaleproductPickupshopInfo(searchList);

		ModelAndView mv = new ModelAndView("/ccs/common/layer/pickupLayer");
		mv.addObject("saleproductList", pickupProducts);
		return mv;
	}

	@RequestMapping(value = "/deliveryAddress/layer", method = RequestMethod.POST)
	public ModelAndView deliveryAddressLayer(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/deliveryAddressLayer");
		return mv;
	}

	@RequestMapping(value = "/searchAddress/layer", method = RequestMethod.POST)
	public ModelAndView searchAddressLayer(HttpServletRequest request) {

		//시도 목록 조회
		AddressSearch search = new AddressSearch();
		List<GviaDistrictVo> list = addressService.getDistrictInfo(search);
		ModelAndView mv = new ModelAndView("/ccs/common/layer/searchAddressLayer");
		mv.addObject("districtList", list);
		return mv;
	}

	@RequestMapping(value = "/offlineOrder/layer", method = RequestMethod.POST)
	public ModelAndView offlineOrder(HttpServletRequest request, @RequestBody OmsOrderSearch search) {
		search.setMemberNo(SessionUtil.getMemberNo());
		OmsPosorder detail = memberService.getMemberOffOrderDetail(search);
		ModelAndView mv = new ModelAndView("/ccs/common/layer/offlineOrderLayer");
		mv.addObject("orderDetail", detail);
		return mv;
	}

	/**
	 * 
	 * @Method Name : searchAddressAjax
	 * @author : intune
	 * @date : 2016. 9. 29.
	 * @description : 주소 검색
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchAddress/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView searchAddressAjax(HttpServletRequest request, AddressSearch search)
			throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/inner/addressList");

		search.setPageSize(10);

		List<GviaPostSearchNewVo> addressList = addressService.getSearchAddress(search);

		mv.addObject("addressList", addressList);
		mv.addObject("search", search);
		if (addressList != null && addressList.size() > 0) {
			mv.addObject("totalCount", addressList.get(0).getTotrecord());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}
	
	/**
	 * 주문 상품 선택 레이어
	 * 
	 * @Method Name : orderProductLayer
	 * @author : roy
	 * @date : 2016. 9. 8.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/searchOrderProduct/layer", method = RequestMethod.POST)
	public ModelAndView orderProductLayer(HttpServletRequest request) throws Exception {

		OmsOrderSearch search = new OmsOrderSearch();
				
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setStoreId(SessionUtil.getStoreId());
		
		// startDate, endDate, memberNo
		List<?> orderList = orderService.selectList("oms.order.select.productList", search);
		
		ModelAndView mv = new ModelAndView("/ccs/common/layer/searchOrderProductLayer");
		
		mv.addObject("list", orderList);
		
		return mv;
	}
	
	
	@RequestMapping(value = "/orderProduct/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView searchOrderProductAjax(HttpServletRequest request, OmsOrderSearch search)
			throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/inner/orderProductList");
		
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setStoreId(SessionUtil.getStoreId());
		
		// startDate, endDate, memberNo
		List<?> orderList = orderService.selectList("oms.order.select.productList", search);
		
		mv.addObject("list", orderList);
		
		return mv;
	}
	
	/**
	 * 당첨자 목록 선택 레이어
	 * 
	 * @Method Name : orderProductLayer
	 * @author : roy
	 * @date : 2016. 9. 8.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/searchWinnerList/layer", method = RequestMethod.POST)
	public ModelAndView winnerListLayer(HttpServletRequest request, @RequestBody SpsEventSearch search) throws Exception {

		ModelAndView mv = new ModelAndView("/ccs/common/layer/searchWinnerListLayer");
		
		mv.addObject("search", search);
		
		return mv;
		
	}
	
	
	@RequestMapping(value = "/winnerList/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView searchOrderProductAjax(HttpServletRequest request, @RequestBody SpsEventSearch search)
			throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/inner/winnerList");

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		List<SpsEventgift> list = eventService.getEventWinnerList(search);

		mv.addObject("list", list);

		return mv;
	}
	

	@RequestMapping(value = "/deposit/refund/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView refundPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/depositRefundLayer");
		
		MmsMemberZts member = memberService.getMemberDetail(SessionUtil.getMemberNo());
		mv.addObject("member", member);

		return mv;
	}

	/**
	 * 공지사항 팝업
	 * 
	 * @Method Name : popupNotice
	 * @author : roy
	 * @date : 2016. 9. 12.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/notice", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView popupNotice(HttpServletRequest request) throws Exception {
//		ModelAndView mv = new ModelAndView("/ccs/common/layer/noticePopupLayer");

		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

//		search.setStoreId(SessionUtil.getStoreId());
		CcsPopup popup = new CcsPopup();
//
//		mv.addObject("popup", popup);

		popup.setPopupNo(new BigDecimal(request.getParameter("popupNo")));
		popup.setStoreId(SessionUtil.getStoreId());
		
		mv.addObject("popup", popupService.selectOne(popup));
		mv.addObject("cookieKey", request.getParameter("cookieKey"));

		return mv;

	}

	/**
	 * 상품평 등록 레이어
	 * 
	 * @Method Name : reviewInsertLayer
	 * @author : roy
	 * @date : 2016. 9. 12.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reviewLayer/layer", method = RequestMethod.POST)
	public ModelAndView reviewInsertLayer(HttpServletRequest request, @RequestBody PmsProductReviewSearch search)
			throws Exception {

		ModelAndView mv = new ModelAndView("/ccs/common/layer/reviewLayer");

		search.setMemberNo(SessionUtil.getMemberNo());
		search.setStoreId(SessionUtil.getStoreId());

		// 리뷰 업데이트
		if (CommonUtil.isNotEmpty(search.getReviewNo())) {
			PmsReview review = new PmsReview();
			review.setStoreId(SessionUtil.getStoreId());
			review.setReviewNo(search.getReviewNo());
			review.setProductId(search.getProductId());
			mv.addObject("review", productReviewService.getReviewDetail(review));
		}
		else { // 리뷰 등록
			// productName, saleproductName, orderId
			mv.addObject("review", search);
		}

		mv.addObject("rating", productReviewService.getReviewIdList(search));

		return mv;

	}

	@RequestMapping(value = "/memberAddress/layer", method = RequestMethod.POST)
	public ModelAndView memberAddressLayer(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/memberAddressLayer");
		MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
		CcsUtil.setSessionLoginInfo(mmsMemberSearch);
		mmsMemberSearch.setMemberNo(SessionUtil.getMemberNo());
		mv.addObject("memberInfo", memberService.getMemberDetail(mmsMemberSearch.getMemberNo()));
		//배송주소록
		mv.addObject("memberAddressList", memberService.getMemberAddressList(mmsMemberSearch));

		// 최근 배송 주소록
		mv.addObject("memberRecentAddressList", memberService.getRecentOrderDeliveyAddress(mmsMemberSearch));

		return mv;
	}

	/**
	 * 게이트웨이
	 * 
	 * @Method Name : gateway
	 * @author : intune
	 * @date : 2016. 9. 19.
	 * @description : 외부 채널로 접속했을때 채널별/디바이스별 해당 url로 redirect한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gateway", method = RequestMethod.GET)
	public ModelAndView gateway(HttpServletRequest request) throws Exception {
		String channelId = request.getParameter("channelId");
		ModelAndView mv = new ModelAndView();

		// 세션 생성
		SessionUtil.setSession(request, Constants.SESSION_KEY_CHANNEL, channelId);

		CcsChannel c = new CcsChannel();
		c.setStoreId(SessionUtil.getStoreId());
		c.setChannelId(channelId);
		CcsChannel channel = (CcsChannel) commonService.selectOneTable(c);
		String redirectUrl = null;
		if(channel==null){
			redirectUrl = "/ccs/common/main";
		}
		boolean isMobile = FoSessionUtil.isMobile(request);
		
		if (isMobile) {
			redirectUrl = channel.getMobileUrl();
		} else {
			redirectUrl = channel.getPcUrl();
		}
		mv.setViewName("redirect:" + redirectUrl);
		return mv;
	}

	/**
	 * 모바일앱 설치 Layer
	 * 
	 * @Method Name : mobileAppInstallLayer
	 * @author : roy
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobileApp/layer", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mobileAppInstallLayer(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/mobileAppInstallLayer");

		return mv;

	}
	
	/**
	 * 관심정보 Layer
	 * 
	 * @Method Name : interestInfoLayer
	 * @author : stella
	 * @date : 2016. 9. 22.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/interestInfo/layer", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView interestInfoLayer(@RequestBody boolean compulsion, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/interestInfoLayer");
		
		CookieUtil cookie = new CookieUtil();
		
		MmsMemberSearch memberSearch = new MmsMemberSearch();
		memberSearch.setMemberNo(SessionUtil.getMemberNo());
		
		// 시/도
		CcsOffshopSearch offshopSearch = new CcsOffshopSearch();
		mv.addObject("area1List", offshopService.getOffshopAreaDiv1List(offshopSearch));
		
		// 멤버쉽관 인증 고객 정보는 자동 입력
		MmsMemberZts memberInfo = memberService.getMemberZts(memberSearch);
		if ("Y".equals(memberInfo.getMembershipYn())) {
			mv.addObject("memberInfo", memberInfo);
			mv.addObject("startIndex", "2");
		}
		
		// 아이 생일 년도 SELECTBOX
		mv.addObject("currentYear", DateUtil.getCurrentDate(DateUtil.FORMAT_5));
		
		// 관심정보 설정 외부 링크 통해서 들어온 경우
		cookie.createCookie(response, "linkYn", "N");
		
		// 브랜드 목록
		BrandSearch brandSearch = new BrandSearch(); 
		List<PmsBrand> brandCode = brandService.getBrandCodeList(brandSearch);
		mv.addObject("brandCodeList", brandCode);
		
		return mv;
	}
	
	/**
	 * 관심정보 Layer - 관심 카테고리 조회
	 * 
	 * @Method Name : interestInfoLayer
	 * @author : stella
	 * @date : 2016. 9. 22.
	 * @description :
	 * 					카테고리만 따로 조회, 레이어 팝업 뜨는 속도 감소
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/interest/category", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getInterestCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/inner/interestInfoCategory");
		
		
		// 관심 카테고리
		DmsDisplaySearch search = new DmsDisplaySearch();
		search.setStoreId(SessionUtil.getStoreId());
		// 전시공통카테고리의  root카테고리ID 추가 
		search.setRootCategoryId(Config.getString("root.display.category.id"));
		
		List<DmsDisplaycategory> categoryList = displayCategoryService.getDepth2CategoryList(search);
		List<DmsDisplaycategory> ctgList =  displayCategoryService.getDepthCategoryList(search, categoryList);
		mv.addObject("categoryList", ctgList);
		
		return mv;
	}
	
	/**
	 * 관심정보 Layer
	 * 
	 * @Method Name : interestInfoLayer
	 * @author : stella
	 * @date : 2016. 9. 22.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/interestInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView interestInfoLayerLink(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/interestInfoLinkLayer");
		
		if (SessionUtil.isMemberLogin()) {
			CookieUtil cookie = new CookieUtil();
 			cookie.createCookie(response, "linkYn", "Y");
		};
		
		return mv;
	}

	/**
	 * 
	 * @Method Name : styleDetailLayer
	 * @author : intune
	 * @date : 2016. 10. 9.
	 * @description : 스타일 상세 레이어
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/styleDetail/layer", method = RequestMethod.POST)
	public ModelAndView styleDetailLayer(HttpServletRequest request, @RequestBody MmsStyleSearch search) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/styleDetailLayer");

		if (search.getMemberNo() == null) {
			search.setMemberNo(SessionUtil.getMemberNo());
		}
		MmsStyle styleDetail = styleService.getMemberStyleDetail(search);
		styleDetail.setIdShowYn(search.getIdShowYn());
		if (styleDetail != null) {
			String hashTag = "";
			if (StringUtils.isNotEmpty(styleDetail.getGenderTypeCd())) {
				hashTag += " #" + styleDetail.getGenderTypeName();
			}
			if (StringUtils.isNotEmpty(styleDetail.getThemeCd())) {
				hashTag += " #" + styleDetail.getThemeName();
				styleDetail.setThemeCdName(styleDetail.getThemeName());
			}
			if (StringUtils.isNotEmpty(styleDetail.getBrandId())) {
				hashTag += " #" + styleDetail.getBrandName();
			}
			styleDetail.setHashTag(hashTag.trim());

		}
		mv.addObject("styleDetail", styleDetail);
		return mv;
	}

	/**
	 * 
	 * @Method Name : cardAuthLayer
	 * @author : roy
	 * @date : 2016. 10. 14.
	 * @description : 카드 인증 레이어
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cardAuth/layer", method = RequestMethod.POST)
	public ModelAndView cardAuthLayer(HttpServletRequest request, @RequestBody MmsChildrenSearch search) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/cardAuthLayer");

//		if (search.getMemberNo() == null) {
//			search.setMemberNo(SessionUtil.getMemberNo());
//		}
//		MmsStyle styleDetail = styleService.getMemberStyleDetail(search);
//		styleDetail.setIdShowYn(search.getIdShowYn());
//		String detail = styleDetail.getDetail();
//		if (StringUtils.isNotEmpty(detail)) {
//			int index = styleDetail.getDetail().indexOf("\r\n");
//			styleDetail.setHashTag(detail.substring(0, index).replaceAll("\\^\\|", " ").replaceAll("1", "").replaceAll("2", "")
//					.replaceAll("3", ""));
//			styleDetail.setDetail(detail.substring(index, detail.length()));
//		}
		mv.addObject("search", search);
		return mv;
	}

	/**
	 * 
	 * @Method Name : regularDetailLayer
	 * @author : roy
	 * @date : 2016. 10. 18.
	 * @description : 정기배송 상세 레이어
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/regularDetail/layer", method = RequestMethod.POST)
	public ModelAndView regularDetailLayer(HttpServletRequest request, @RequestBody OmsRegularSearch regularSearch) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/regularDetailLayer");

		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		regularSearch.setStoreId(SessionUtil.getStoreId());
		regularSearch.setOrdererType("number");
		regularSearch.setOrderer(String.valueOf(SessionUtil.getMemberNo()));
		// regularSearch.setPageSize(1000);

		regularSearch.setSearchId("oms.regular.select.deliveryList");
//		regularSearch.setEndDate(currentDate);
		regularSearch.setDeliveryScheduleStateCd("'DELIVERY_SCHEDULE_STATE_CD.ORDER','DELIVERY_SCHEDULE_STATE_CD.CANCEL'");
		regularSearch.setDeliveryProductStateCd("'DELIVERY_PRODUCT_STATE_CD.CANCEL'");

		List<?> regularList = regularService.selectList(regularSearch.getSearchId(), regularSearch);
		mv.addObject("regularList", regularList);
		List<?> shceduleList = regularService.getRegulardeliveryschedule(regularSearch);
		mv.addObject("shceduleList", shceduleList);

		return mv;
	}

	/**
	 * 
	 * @Method Name : noticePopupLayer
	 * @author : roy
	 * @date : 2016. 10. 18.
	 * @description : 공지사항 팝업 레이어
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/noticePopup/layer", method = RequestMethod.POST)
	public ModelAndView noticePopupLayer(HttpServletRequest request, @RequestBody CcsPopup search) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/noticePopupLayer");
		search.setStoreId(SessionUtil.getStoreId());
		CcsPopup popup = popupService.selectOne(search);
		mv.addObject("search", search);
		mv.addObject("popup", popup);
		return mv;
	}
	
	@RequestMapping(value = "/videoPlay/layer", method = RequestMethod.POST)
	public ModelAndView videoPlayLayer(@RequestBody Map<String, String> map, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/ccs/common/layer/brandVideoLayer");
		
		mv.addObject("videoUrl", map.get("url"));
		
		return mv;
	}
	
	/**
	 * 제휴사 접근
	 * 
	 * @Method Name : affiliate
	 * @author : intune
	 * @date : 2016. 11. 2.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/affiliate", method = RequestMethod.GET)
	public ModelAndView affiliate(HttpServletRequest request) throws Exception {
		
		return new ModelAndView(CommonUtil.makeJspUrl(request));
	 }
	
	/**
	 * 샵온 메인 화면 조회(mo)
	 * 
	 * @Method Name : getShopOnMainMo
	 * @author : intune
	 * @date : 2016. 11. 12.
	 * @description :
	 *
	 * @return
	 */
	@RequestMapping(value = "/getShopOnMainMo", method = RequestMethod.GET)
	public ModelAndView getShopOnMainMo() {
		ModelAndView model = new ModelAndView("/ccs/common/inner/displayShopOn.mainCon");
		// 회원 관심 매장 리스트 조회
		if (SessionUtil.getMemberNo() != null) {
			CcsOffshopSearch ccsOffshopSearch = new CcsOffshopSearch();
			ccsOffshopSearch.setStoreId(SessionUtil.getStoreId());
			ccsOffshopSearch.setMemberNo(SessionUtil.getMemberNo().toString());
			ccsOffshopSearch.setPagingYn(BaseConstants.YN_Y);
			List<MmsInterestoffshop> interestOffshopList = offshopService.getShopOnOffShopInfoList(ccsOffshopSearch);
			model.addObject("interestOffshopList", interestOffshopList);
		}

		// 오프라인 기획전 조회
		DmsExhibitSearch offExhibitSearch = new DmsExhibitSearch();
		offExhibitSearch.setStoreId(SessionUtil.getStoreId());
		List<DmsExhibit> shopOnExhibitList = exhibitService.getOffshopExhibitList(offExhibitSearch);
		model.addObject("shopOnExhibitList", shopOnExhibitList);

		// 매장 픽업 브랜드 리스트
		DmsDisplaySearch shopOnSearch = new DmsDisplaySearch();
		shopOnSearch.setStoreId(SessionUtil.getStoreId());
		shopOnSearch.setRootCategoryId(Config.getString("root.display.category.id"));
		List<PmsBrand> shopOnbrandList = specialService.getPickupBrandList(shopOnSearch);
		model.addObject("shopOnbrandList", shopOnbrandList);

		// 브랜드의 카테고리 및 상품 리스트 조회
		if (shopOnbrandList != null && shopOnbrandList.size() > 0) {
			List<DmsDisplaycategory> brandCategoryList = specialService.getPickupInfoList(shopOnSearch);
			model.addObject("brandCategoryList", brandCategoryList);
		}

		CcsOffshopSearch offshopSearch = new CcsOffshopSearch();
		offshopSearch.setPickupYn("Y");
		model.addObject("area1List", offshopService.getOffshopAreaDiv1List(offshopSearch));
		return model;
	}

	/**
	 * 스타일 메인 화면 조회(mo)
	 * 
	 * @Method Name : getStyleMainMo
	 * @author : intune
	 * @date : 2016. 11. 12.
	 * @description :
	 *
	 * @return
	 */
	@RequestMapping(value = "/getStyleMainMo", method = RequestMethod.GET)
	public ModelAndView getStyleMainMo() {
		ModelAndView model = new ModelAndView("/ccs/common/inner/displayStyleMain.mainCon");
		String styleBannerId = Config.getString("corner.style.banner.img.1");
		DmsDisplaySearch styleDisplaySearch = new DmsDisplaySearch();
		styleDisplaySearch.setStoreId(SessionUtil.getStoreId());
		styleDisplaySearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		styleDisplaySearch.setDisplayId(styleBannerId);
		List<DmsDisplay> bannerList = cornerService.getCommonCornerList(styleDisplaySearch);
		model.addObject("styleBannerList", bannerList);
		return model;
	}

	/**
	 * 기획전 메인 화면 조회(mo)
	 * 
	 * @Method Name : getStyleMainMo
	 * @author : intune
	 * @date : 2016. 11. 12.
	 * @description :
	 *
	 * @return
	 */
	@RequestMapping(value = "/getExhibitionMainMo", method = RequestMethod.GET)
	public ModelAndView getExhibitionMainMo() {
		ModelAndView model = new ModelAndView("/ccs/common/inner/exhibitMain.mainCon");

		// MOBILE 일 경우 메인의 빅배너 사용
		String mobileBigBanner = Config.getString("corner.main.banner.img.6");
		DmsDisplaySearch displaySearch = new DmsDisplaySearch();
		displaySearch.setStoreId(SessionUtil.getStoreId());
		displaySearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		displaySearch.setDisplayId(mobileBigBanner);
		List<DmsDisplay> mbBigBannerList = cornerService.getCommonCornerList(displaySearch);
		model.addObject("mbBigBannerList", mbBigBannerList);

		// 상단 이미지 배너 - 기획전 코너 조회
		String exhibitCornerId = Config.getString("corner.exhibit.exhibit.exhibit.1");
		DmsDisplaySearch exhibitSearch = new DmsDisplaySearch();
		exhibitSearch.setStoreId(SessionUtil.getStoreId());
		exhibitSearch.setDisplayTypeCd(BaseConstants.DISPLAY_TYPE_CD_COMMON);
		exhibitSearch.setDisplayId(exhibitCornerId);
		List<DmsDisplay> exhibitCornerList = cornerService.getCommonCornerList(exhibitSearch);

		// 브랜드 리스트 조회
		List<PmsBrand> exhibitBrandList = exhibitService.getExhibitBrandList();

		model.addObject("exhibitCornerList", exhibitCornerList);
		model.addObject("exhibitBrandList", exhibitBrandList);

		return model;
	}

	/**
	 * 쇼킹제로 메인
	 * 
	 * @Method Name : getShockingzeroMainMo
	 * @author : intune
	 * @date : 2016. 11. 15.
	 * @description :
	 *
	 * @return
	 */
	@RequestMapping(value = "/getShockingzeroMainMo", method = RequestMethod.GET)
	public ModelAndView getShockingzeroMainMo() {
		ModelAndView model = new ModelAndView("/ccs/common/inner/shockingzeroMain.mainCon");
		return model;
	}

	/**
	 * 이벤트 메인
	 * 
	 * @Method Name : getEventMainMo
	 * @author : intune
	 * @date : 2016. 11. 15.
	 * @description :
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getEventMainMo", method = RequestMethod.GET)
	public ModelAndView getEventMainMo(HttpServletRequest request) throws Exception {
		ModelAndView model = new ModelAndView("/ccs/common/inner/eventMain.mainCon");

		SpsEventSearch eventSearch = new SpsEventSearch();
		eventSearch.setStoreId(SessionUtil.getStoreId());
		eventSearch.setPagingYn("N");

		if (FoSessionUtil.isApp(request)) {
			eventSearch.setControlDeviceTypeCd("DEVICE_TYPE_CD.APP");
		} else if (FoSessionUtil.isMobile(request)) {
			eventSearch.setControlDeviceTypeCd("DEVICE_TYPE_CD.MW");
		} else {
			eventSearch.setControlDeviceTypeCd("DEVICE_TYPE_CD.PC");
		}

		List<SpsEvent> eventList = eventService.getRunAllEventList(eventSearch);
		model.addObject("eventList", eventList);

		List<SpsEvent> expEventList = eventService.getExpEventList(eventSearch);
		model.addObject("expEventList", expEventList);

		return model;
	}
	
	/**
	 * 
	 * @Method Name : brandFaqLayer
	 * @author : stella
	 * @date : 2016. 11. 15.
	 * @description : 브랜드관 FAQ 레이어
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/brand/faq/layer", method = RequestMethod.POST)
	public ModelAndView brandFaqLayer(HttpServletRequest request, @RequestBody BrandSearch search) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		String brandId = search.getBrandId();
		String templateId = search.getTemplateId();
		
		if (CommonUtil.isNotEmpty(templateId) && "A".equals(templateId)) {
			mv.setViewName("/dms/template/layer/templateA_faqLayer");
		}
		if (CommonUtil.isNotEmpty(brandId)) {
			mv.setViewName("/dms/template/layer/" + brandId + "_faqLayer");
		}
		
		return mv;
	}

}
