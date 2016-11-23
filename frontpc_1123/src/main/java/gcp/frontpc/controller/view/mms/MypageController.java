package gcp.frontpc.controller.view.mms;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsOffshop;
import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsExhibit;
import gcp.dms.service.CategoryService;
import gcp.dms.service.DisplayCommonService;
import gcp.dms.service.ExhibitService;
import gcp.external.model.ClothesAfterService;
import gcp.external.model.ShoppingAlarm;
import gcp.external.model.coupon.UvMmsOffshopcouponissue;
import gcp.external.model.membership.MemberPoint;
import gcp.external.model.membership.MembershipPointSearchReq;
import gcp.external.service.ClothesAsService;
import gcp.external.service.MembershipService;
import gcp.external.service.OffshopCouponService;
import gcp.external.service.TmsService;
import gcp.mms.model.MmsAddress;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsInterestoffshop;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsQuickmenu;
import gcp.mms.model.MmsStyle;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.model.search.MmsStyleSearch;
import gcp.mms.service.MemberService;
import gcp.mms.service.MypageService;
import gcp.mms.service.StyleService;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPosorder;
import gcp.oms.model.search.OmsCartSearch;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.service.CartService;
import gcp.oms.service.OrderService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsReview;
import gcp.pms.model.search.PmsProductReviewSearch;
import gcp.pms.service.BrandService;
import gcp.pms.service.ProductReviewService;
import gcp.pms.service.ProductService;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.search.SpsCouponIssueSearch;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.service.EventService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("mypageViewController")
@RequestMapping("mms/mypage")
public class MypageController {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MypageService			mypageService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private EventService			eventService;

	@Autowired
	private ProductReviewService	productReviewService;
	
	@Autowired
	private DisplayCommonService	displayCommonService;

	@Autowired
	private ProductService			productService;

	@Autowired
	private StyleService			styleService;
	
	@Autowired
	private OrderService			orderService;
	
	@Autowired
	private MembershipService		membershipService;

	@Autowired
	private CartService 			cartService;
	
	@Autowired
	private OffshopCouponService 	offshopCouponService;
	
	@Autowired
	private TmsService 				tmsService;
	
	@Autowired
	private ClothesAsService 		clothesAsService;
	
	@Autowired
	private CategoryService 		categoryService;
	
	@Autowired
	private BrandService 			brandService;
	
	@Autowired
	private ExhibitService 			exhibitService;
	
	//@Autowired
	//private ErpService erpService;

	/**
	 * @Method Name : defaultSetDate
	 * @author : ian
	 * @date : 2016. 8. 12.
	 * @description : mypage 기본 조회기간 세팅 (현재 월 -1 ~ 현재 월)
	 *
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	private Map<String, String> defaultSetDate() throws Exception {
		
		Map<String, String> map = new HashMap<String, String>();
		
		// 날짜 세팅
		DateUtil date = new DateUtil();
		String currentDate = date.getCurrentDate(date.FORMAT_2);
		String editDate = date.getAddMonth(date.FORMAT_2, currentDate, new BigDecimal(-1));

		map.put("startDate", editDate);
		map.put("endDate", currentDate);
		
		return map;
	}

	/**
	 * @Method Name : mainPage
	 * @author : ian
	 * @date : 2016. 7. 5.
	 * @description : mypage 메인
	 *
	 * @param request
	 * @return
	 */

	@RequestMapping(value = "/main", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mainPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		MmsMemberSearch search = new MmsMemberSearch();

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());

		// 데이터 호출 START
		
		//멤버쉽 포인트 호출
		MembershipPointSearchReq req = new MembershipPointSearchReq();
		req.setSrchDv("I");//조회구분‘I’- 통합회원번호, ‘C’- 멤버십카드번호 ‘T’- 휴대전화번호
		req.setSrchDvVlu(SessionUtil.getMemberNo().toString());//회원번호
		MemberPoint point = membershipService.getMemberPoint(req);

		mv.addObject("memberpoint", point);
		
		/********************************************************
		 * 쇼핑 알림함 보관함 메세지 카운팅 (모바일 앱)
		 * *****************************************************/
		if (FoSessionUtil.isMobile(request)) {
			search.setSiteKey(Config.getString("tms.site.key"));
			BigDecimal count = tmsService.getMemberShoppingAlarmCount(search);
			if(count != null) {
				mv.addObject("alarmCnt", count);
			} else {
				mv.addObject("alarmCnt", BigDecimal.ZERO);
			}
		}

		/********************************************************
		 * 회원정보, 당근, 예치금, 쿠폰
		 * *****************************************************/ 
		MmsMemberZts member = mypageService.getMypageMemberInfo(search);
		mv.addObject("member", member);

		/********************************************************
		 * 주문배송현황
		 * *****************************************************/
		List<OmsOrder> deliveryStepList = (List<OmsOrder>) mypageService.getDeliveryStep(search);
		mv.addObject("delivery", deliveryStepList);
		
		// 데이터 호출 END
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "마이쇼핑");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}
	
	/**
	 * @Method Name : mainLatestOrder
	 * @author : ian
	 * @date : 2016. 10. 27.
	 * @description : 마이페이지 메인 - 최근 7일 주문내역
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/order/ajax", method = { RequestMethod.GET })
	public ModelAndView mainLatestOrder(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/inner/mainLatestOrder");

		/********************************************************
		 * 최근주문내역
		 * *****************************************************/
		OmsOrderSearch orderSearch = new OmsOrderSearch();
		orderSearch.setStoreId(SessionUtil.getStoreId());
		orderSearch.setMemberNo(SessionUtil.getMemberNo());

		List<OmsOrder> orderList = mypageService.getLatestOrderList(orderSearch);
		mv.addObject("orderList", orderList);

		return mv;
	}
	
	/**
	 * @Method Name : mainWishList
	 * @author : ian
	 * @date : 2016. 10. 27.
	 * @description : 마이페이지 메인 쇼핑찜 list
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/wish/ajax", method = { RequestMethod.GET })
	public ModelAndView mainWishList(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/inner/mainWishList");
		MmsMemberSearch search = new MmsMemberSearch();

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		
		List<PmsProduct> wishList = mypageService.getShoppingWishlist(search);
		mv.addObject("wishList", wishList);
		
		return mv;
	}
	
	/**
	 * @Method Name : mainCartList
	 * @author : ian
	 * @date : 2016. 10. 27.
	 * @description : 마이페이지 메인 장바구니
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/main/cart/ajax", method = { RequestMethod.GET })
	public ModelAndView mainCartList(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/inner/mainCartList");
		OmsCartSearch omsCartSearch = new OmsCartSearch();
		CcsUtil.setSessionLoginInfo(omsCartSearch, true);
		
//		OmsCart cartCnt = cartService.getCartCnt(omsCartSearch);
		List<OmsCart> cartList = null;
		omsCartSearch.setCartTypeCd(BaseConstants.CART_TYPE_CD_GENERAL);
		omsCartSearch.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		omsCartSearch.setIsMypage("Y");
		
		cartList = cartService.getCartList(omsCartSearch);
//		if (cartCnt != null && cartCnt.getGeneralCnt() > 0) {
//		}
//		
//		mv.addObject("cartCnt", cartCnt);
		mv.addObject("cartList", cartList);
		
		return mv;
	}

	/**
	 * @Method Name : eventPage
	 * @author : ian
	 * @date : 2016. 9. 2.
	 * @description : 나의 활동 (이벤트)
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/activity/event", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView eventPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		SpsEventSearch search = new SpsEventSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		
		if (CommonUtil.isEmpty(search.getStartDate())) {
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}
		
		mv.addObject("joinCntInfo", eventService.getEventjoinCntInfo(search));

		return mv;
	}

	/**
	 * 
	 * @Method Name : getEventjoinHistoryAjax
	 * @author : stella
	 * @date : 2016. 9. 10.
	 * @description : 이벤트 참여내역 조회
	 *
	 * @param search
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/eventjoin/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getEventjoinHistoryAjax(SpsEventSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/eventJoinList");

		if (CommonUtil.isEmpty(search.getStartDate())) {
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setPageSize(20);

		mv.addObject("joinCntInfo", eventService.getEventjoinCntInfo(search));
		mv.addObject("joinEventList", eventService.getEventjoinHistory(search));
		mv.addObject("search", search);

		return mv;
	}

	/**
	 * @Method Name : inquiryPage
	 * @author : ian
	 * @date : 2016. 9. 1.
	 * @description : 내가 문의한 글 (1:1 / 상품 Q&A)
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/activity/inquiry", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView inquiryPage(@ModelAttribute("mmsMemberSearch") MmsMemberSearch search, HttpServletRequest request)
			throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		mv.addObject("pageType", search.getPageType());
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "나의문의");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}

	/**
	 * @Method Name : getInquiryListAjax
	 * @author : ian
	 * @date : 2016. 9. 1.
	 * @description : 내가 문의한 글 (1:1 / 상품 Q&A) ajax List 
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/inquiry/product/list/ajax", method = { RequestMethod.GET })
	public ModelAndView getInquiryProductListAjax(MmsMemberSearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView();
		
		if (FoSessionUtil.isMobile(request) && BaseConstants.YN_Y.equals(search.getIsScroll())) {
			mv.setViewName("/mms/mypage/activity/inner/mobileQnaInnerList");
		} else {
			mv.setViewName("/mms/mypage/activity/inner/productQnaList");
		}
		
		if (CommonUtil.isEmpty(search.getStartDate())) {
			// 기본세팅 날짜 : (현재 월 -1 ~ 현재 월)
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		
		search.setPageSize(10);
		
		if ("COMPLETE".equals(search.getSaveType())) {
			search.setProductQnaStateCd(BaseConstants.PRODUCT_QNA_STATE_CD_COMPLETE);
		}

		PmsProductqna qnaInfo = mypageService.getProductQnaInfo(search);
		List<PmsProductqna> list = mypageService.getProductQnaList(search);
		
		mv.addObject("info", qnaInfo);
		mv.addObject("productQnaList", list);
		
		// paging
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		mv.addObject("search", search);
		mv.addObject("pageType", search.getPageType());
		return mv;
	}
	
	@RequestMapping(value = "/inquiry/myqa/list/ajax", method = { RequestMethod.GET })
	public ModelAndView getInquiryMyQaListAjax(MmsMemberSearch search, HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		if (FoSessionUtil.isMobile(request) && BaseConstants.YN_Y.equals(search.getIsScroll())) {
			mv.setViewName("/mms/mypage/activity/inner/mobileQnaInnerList");
		} else {
			mv.setViewName("/mms/mypage/activity/inner/myQaList");
		}
		
		if (CommonUtil.isEmpty(search.getStartDate())) {
			// 기본세팅 날짜 : (현재 월 -1 ~ 현재 월)
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}
		
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		
		search.setPageSize(10);
		
		if ("COMPLETE".equals(search.getSaveType())) {
			search.setInquiryStateCd(BaseConstants.INQUIRY_STATE_CD_COMPLETE);
		}
		
		CcsInquiry myQaInfo = mypageService.getMyQaInfo(search);
		List<CcsInquiry> list = mypageService.getMyQaList(search);
		
		mv.addObject("info", myQaInfo);
		mv.addObject("myQaList", list);
		
		// paging
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		
		mv.addObject("search", search);
		mv.addObject("pageType", search.getPageType());
		return mv;
	}

	@RequestMapping(value = "/activity/gift", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView giftPage(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;
	}

	@RequestMapping(value = "/activity/history", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView historyPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		// 히스토리는 모바일 전용
		if (!FoSessionUtil.isMobile(request)) {
			return (ModelAndView) new ModelAndView("redirect:/ccs/common/main");
		}
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "히스토리");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/history/list/ajax", method = { RequestMethod.GET })
	public ModelAndView getHistoryListAjax(MmsMemberSearch search, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// TODO 브랜드, 기획전 이벤트, 날짜
		String storeId = SessionUtil.getStoreId();
		
		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/historyList");
		search.setStoreId(storeId);
		search.setMemberShipYn(FoSessionUtil.getMembershipYn());
		search.setMemGradeCd(FoSessionUtil.getMemGradeCd());
		
		CookieUtil cookie = new CookieUtil();
		List<Map<String,String>> moHistory = cookie.getMoHistoryCookie(request, response);
		for(Map<String,String> map : moHistory) {
			try {
				if("SEARCH".equals(map.get("type"))) {
					map.put("id", URLDecoder.decode(map.get("id"), "UTF-8"));
				} else {
					if("PRODUCT".equals(map.get("type"))) {
						search.setMoHistoryId(map.get("id"));
						PmsProduct product = (PmsProduct) displayCommonService.getMoHistoryProduct(search);
						map.put("name", product.getName());
						map.put("price", (product.getSalePrice()).toString());
					} 
					else {
						if("CATEGORY".equals(map.get("type"))) {
							DmsDisplaycategory category = new DmsDisplaycategory();
							category.setStoreId(storeId);
							category.setDisplayCategoryId(map.get("id"));
							category = categoryService.getCurrentCategory(category);
							
							map.put("name", category.getName());
						}
						else if("BRAND".equals(map.get("type"))) {
							PmsBrand brand = new PmsBrand();
							brand.setStoreId(storeId);
							brand.setBrandId(map.get("id"));
							brand = brandService.getBrand(brand);
							
							map.put("name", brand.getName());
						}
						else if("EXHIBIT".equals(map.get("type"))) {
							DmsExhibit exhibit = new DmsExhibit();
							exhibit.setStoreId(storeId);
							exhibit.setExhibitId(map.get("id"));
							exhibit = exhibitService.getExhibit(exhibit);
							
							map.put("name", exhibit.getName());
							String date = DateUtil.convertFormat(exhibit.getStartDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10);
							date += " ~ " + DateUtil.convertFormat(exhibit.getEndDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10);
							map.put("date", date);
						}
						else if("EVENT".equals(map.get("type"))) {
							SpsEvent event = new SpsEvent();
							event.setStoreId(storeId);
							event.setEventId(map.get("id"));
							event = eventService.getEvent(event);
							
							map.put("name", event.getName());
							String date = DateUtil.convertFormat(event.getEventStartDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10);
							date += " ~ " + DateUtil.convertFormat(event.getEventEndDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10);
							map.put("date", date);
						}
						
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		mv.addObject("moHistory", moHistory);

		return mv;
	}
	
	@RequestMapping(value = "/activity/review", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView reviewPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "상품평");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}
	
	/**
	 * @Method Name : latestProductPage
	 * @author : ian
	 * @date : 2016. 9. 21.
	 * @description : 나의 활동 (최근 본 상품)
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/activity/latestProduct", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView latestProductPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "최근 본 상품");
		mv.addObject("pageNavi", pageMap);
		
		return mv;
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/latestProduct/list/ajax", method = { RequestMethod.GET })
	public ModelAndView getLatestProductListAjax(MmsMemberSearch search, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/latestProductList");
		List<PmsProduct> latestProductList = new ArrayList<PmsProduct>();
		
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberShipYn(FoSessionUtil.getMembershipYn());
		search.setMemGradeCd(FoSessionUtil.getMemGradeCd());
		
		search.setPageSize(5);
		
		CookieUtil cookie = new CookieUtil();
		String productIds = cookie.getCookieList(request, response, "latestProduct", 0);
		if(CommonUtil.isNotEmpty(productIds)) {
			search.setProductIds(productIds);
			latestProductList = (List<PmsProduct>) displayCommonService.getLatestProductList(search, "MY_PAGE");
		}
		
		mv.addObject("latestProductListSize", latestProductList.size());
		mv.addObject("latestProductList", latestProductList);
		
		// paging
		if (latestProductList != null && latestProductList.size() > 0) {
			mv.addObject("totalCount", latestProductList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		mv.addObject("search", search);

		return mv;
	}
	
	/**
	 * @Method Name : carrotPage
	 * @author : ian
	 * @date : 2016. 8. 31.
	 * @description : 나의 혜택(당근)
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/benefit/carrot", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView carrotPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		MmsMemberSearch search = new MmsMemberSearch();
		search.setMemberNo(SessionUtil.getMemberNo());

		MmsCarrot carrotInfo = mypageService.getCarrotInfo(search);

		mv.addObject("carrotInfo", carrotInfo);

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "당근");
		mv.addObject("pageNavi", pageMap);
		
		return mv;
	}

	/**
	 * @Method Name : getCarrotListAjax
	 * @author : ian
	 * @date : 2016. 8. 31.
	 * @description : 당근 페이징
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/carrot/list/ajax", method = { RequestMethod.GET })
	public ModelAndView getCarrotListAjax(MmsMemberSearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/mms/mypage/benefit/inner/carrotList");

		if (CommonUtil.isEmpty(search.getStartDate())) {
			// 기본세팅 날짜 : (현재 월 -1 ~ 현재 월)
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}
		search.setMemberNo(SessionUtil.getMemberNo());
		
		if (!FoSessionUtil.isMobile(request)) {
			search.setPageSize(20);
		} else {
			search.setPageSize(10);
		}

		MmsCarrot carrotInfo = mypageService.getCarrotInfo(search);
		List<MmsCarrot> carrotList = mypageService.getCarrotList(search);

		mv.addObject("carrotInfo", carrotInfo);
		mv.addObject("carrotList", carrotList);
		mv.addObject("search", search);

		// paging
		if (carrotList != null && carrotList.size() > 0) {
			mv.addObject("totalCount", carrotList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		return mv;
	}

	/**
	 * @Method Name : depositPage
	 * @author : ian
	 * @date : 2016. 8. 31.
	 * @description : 나의 혜택(예치금)
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/benefit/deposit", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView depositPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		MmsMemberSearch search = new MmsMemberSearch();
		search.setMemberNo(SessionUtil.getMemberNo());

		MmsDeposit depositInfo = mypageService.getDepositInfo(search);

		mv.addObject("depositInfo", depositInfo);

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "예치금");
		mv.addObject("pageNavi", pageMap);
		
		return mv;
	}

	/**
	 * @Method Name : getDepositListAjax
	 * @author : ian
	 * @date : 2016. 8. 31.
	 * @description : 예치금 페이징
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deposit/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getDepositListAjax(MmsMemberSearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/mms/mypage/benefit/inner/depositList");

		search.setMemberNo(SessionUtil.getMemberNo());
		if (CommonUtil.isEmpty(search.getStartDate())) {
			// 기본세팅 날짜 : (현재 월 -1 ~ 현재 월)
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}

		if (!FoSessionUtil.isMobile(request)) {
			search.setPageSize(20);
		} else {
			search.setPageSize(10);
		}

		MmsDeposit depositInfo = mypageService.getDepositInfo(search);
		List<MmsDeposit> depositList = mypageService.getDepositList(search);

		mv.addObject("depositInfo", depositInfo);
		mv.addObject("depositList", depositList);
		mv.addObject("search", search);

		// paging
		if (depositList != null && depositList.size() > 0) {
			mv.addObject("totalCount", depositList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		return mv;
	}

	/**
	 * @Method Name : couponPage
	 * @author : ian
	 * @date : 2016. 8. 31.
	 * @description : 나의 혜택 (쿠폰)
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/benefit/coupon", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView couponPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		SpsCouponIssueSearch search = new SpsCouponIssueSearch();
		search.setMemberNo(SessionUtil.getMemberNo());
		SpsCouponissue couponInfo = mypageService.getCouponInfo(search);

		mv.addObject("couponInfo", couponInfo);
		
		MmsMemberSearch memSearch = new MmsMemberSearch();
		memSearch.setStoreId(SessionUtil.getStoreId());
		memSearch.setMemberNo(SessionUtil.getMemberNo());
		
		Map<String,String> offshopCoupon = offshopCouponService.getOffshopCouponInfo(memSearch);
		mv.addObject("offshopUseCoupon", offshopCoupon.get("USE"));
		mv.addObject("offshopEndCoupon", offshopCoupon.get("END"));
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "쿠폰");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}

	@RequestMapping(value = "/coupon/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getCouponListAjax(SpsCouponIssueSearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/mms/mypage/benefit/inner/couponList");
		
		if (FoSessionUtil.isMobile(request)) {
			search.setPageSize(10);
			search.setPagingYn(BaseConstants.YN_Y);
		} else {
			search.setPagingYn(BaseConstants.YN_N);
		}
		
		search.setMemberNo(SessionUtil.getMemberNo());

		SpsCouponissue couponInfo = mypageService.getCouponInfo(search);
		mv.addObject("couponInfo", couponInfo);
		
		MmsMemberSearch memSearch = new MmsMemberSearch();
		memSearch.setStoreId(SessionUtil.getStoreId());
		memSearch.setMemberNo(SessionUtil.getMemberNo());
		
		Map<String,String> offshopCoupon = offshopCouponService.getOffshopCouponInfo(memSearch);
		mv.addObject("offshopUseCoupon", offshopCoupon.get("USE"));
		mv.addObject("offshopEndCoupon", offshopCoupon.get("END"));
		
		mv.addObject("eachCount", 0);
		// online/offline
		if("ONLINE".equals(search.getUsePlace())) {
			List<SpsCouponissue> couponList = mypageService.getCouponList(search);
			mv.addObject("couponList", couponList);
			
			if (couponList != null && couponList.size() > 0) {
				mv.addObject("eachCount", couponList.get(0).getTotalCount());
			}
		} else if("OFFLINE".equals(search.getUsePlace())) {
			List<UvMmsOffshopcouponissue> offshopCouponList = (List<UvMmsOffshopcouponissue>) offshopCouponService.getOffshopCouponList(memSearch);
			mv.addObject("offshopCouponList", offshopCouponList);
			
			if (offshopCouponList != null && offshopCouponList.size() > 0) {
				mv.addObject("eachCount", offshopCouponList.get(0).getTotalCount());
			}
		}

		return mv;
	}

	/**
	 * @Method Name : editPage, proofPage
	 * @author : ian
	 * @date : 2016. 7. 18.
	 * @description : 정보관리(회원정보 수정, 멤버쉽관 고객인증)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/info/edit", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView editPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;
	}

	@RequestMapping(value = "/info/proof", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView proofPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;
	}

	/**
	 * 
	 * @Method Name : deliveryAddressPage
	 * @author : allen
	 * @date : 2016. 8. 30.
	 * @description : 배송지 관리
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/info/deliveryAddress", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deliveryAddressPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
		mmsMemberSearch.setMemberNo(SessionUtil.getMemberNo());
		MmsAddress basicAddress = memberService.getMemberBasicAddress(mmsMemberSearch);
		mv.addObject("basicAddress", basicAddress);

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "배송지관리");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}

	@RequestMapping(value = "/deliveryAddress/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deliveryAddressAjax(HttpServletRequest request, MmsMemberSearch mmsMemberSearch) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/info/inner/addressList");

		//TODO : 회원 배송지 정보 조회
		mmsMemberSearch.setMemberNo(SessionUtil.getMemberNo());
		mmsMemberSearch.setPageSize(5);
		List<MmsAddress> addressList = memberService.getMemberAddressList(mmsMemberSearch);

		mv.addObject("addressList", addressList);
		mv.addObject("search", mmsMemberSearch);
		if (addressList != null && addressList.size() > 0) {
			mv.addObject("totalCount", addressList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}

	/**
	 * @Method Name : offlinePage, addr
	 * @author : ian
	 * @date : 2016. 7. 5.
	 * @description : 주문/배송 (오프라인 구매내역, 배송지관리)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order/offline", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView offlinePage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		MmsMemberSearch search = new MmsMemberSearch();
		search.setMemberNo(SessionUtil.getMemberNo());
		List<CcsOffshop> list = memberService.getMemberOffOrderRegionList(search);
		mv.addObject("regionList", list);
		return mv;
	}

	@RequestMapping(value = "/offlineOrder/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView offlineOrderAjax(HttpServletRequest request, @RequestBody MmsMemberSearch mmsMemberSearch)
			throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/order/inner/offlineOrderList");

		mmsMemberSearch.setMemberNo(SessionUtil.getMemberNo());
		mmsMemberSearch.setPageSize(5);
		List<OmsPosorder> list = memberService.getMemberOffOrderProductListForFront(mmsMemberSearch);

		mv.addObject("orderList", list);
		mv.addObject("search", mmsMemberSearch);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.size());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}

	@RequestMapping(value = "/order/addr", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView addrPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		return mv;
	}

	/**
	 * @Method Name : offshopPage
	 * @author : stella
	 * @date : 2016. 9. 7.
	 * @description : 관심매장 설정
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/info/offshop", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView offshopPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		MmsMemberSearch search = new MmsMemberSearch();
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		
		search.setTopOffshopYn("Y");
		List<MmsInterestoffshop> topOffshop = memberService.getInterestOffshopList(search);
//		for (MmsInterestoffshop offshop : topOffshop) {
//			offshop.getCcsOffshop().setMainBrand(erpService.getOffshopMainBrand(offshop.getOffshopId()));
//		}
		mv.addObject("topOffshop", topOffshop);
		
		search.setTopOffshopYn("N");
		List<MmsInterestoffshop> offshopList = memberService.getInterestOffshopList(search);
//		for (MmsInterestoffshop offshop : offshopList) {
//			offshop.getCcsOffshop().setMainBrand(erpService.getOffshopMainBrand(offshop.getOffshopId()));
//		}
		mv.addObject("offshopList", offshopList);
		
		mv.addObject("totalCnt", offshopList.size() + topOffshop.size());

		return mv;
	}
	
	/**
	 * @Method Name : refundPage
	 * @author : ian
	 * @date : 2016. 9. 8.
	 * @description : 환불 계좌 관리
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/info/refund", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView refundPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		MmsMemberZts member = memberService.getMemberDetail(SessionUtil.getMemberNo());
		mv.addObject("member", member);
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "환불계좌 관리");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}

	/**
	 * 
	 * @Method Name : myMenu
	 * @author : intune
	 * @date : 2016. 9. 9.
	 * @description : 마이메뉴
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/info/myMenu", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myMenu(HttpServletRequest request) throws Exception {

		MmsMemberSearch search = new MmsMemberSearch();
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setQuickmenuTypeCd(BaseConstants.QUICKMENU_TYPE_CD_MENU);
		List<MmsQuickmenu> menuList = memberService.getQuickmenuList(search);

		search.setQuickmenuTypeCd(BaseConstants.QUICKMENU_TYPE_CD_DEAL);
		List<MmsQuickmenu> dealList = memberService.getQuickmenuList(search);

		List<MmsQuickmenu> memberMenuList = memberService.getMemberQuickmenuList(search);

		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		mv.addObject("menuList", menuList);
		mv.addObject("dealList", dealList);
		mv.addObject("memberMenuList", memberMenuList);
		return mv;
	}
	
	/**
	 * @Method Name : receiptPage
	 * @author : ian
	 * @date : 2016. 9. 12.
	 * @description : 영수증 조회
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/info/receipt", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView receiptPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "영수증 조회");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}
	
	@RequestMapping(value = "/receipt/list/ajax", method = { RequestMethod.GET })
	public ModelAndView getReceiptListAjax(MmsMemberSearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/mms/mypage/info/inner/receiptList");

		if (CommonUtil.isEmpty(search.getStartDate())) {
			// 기본세팅 날짜 : (현재 월 -1 ~ 현재 월)
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		
		if (!FoSessionUtil.isMobile(request)) {
			search.setPageSize(20);
		} else {
			search.setPageSize(10);
		}

		List<OmsOrder> orderList = (List<OmsOrder>) mypageService.getReceiptList(search);

		mv.addObject("orderList", orderList);
		mv.addObject("search", search);

		// paging
		if (orderList != null && orderList.size() > 0) {
			mv.addObject("totalCount", orderList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		return mv;
	}

	/**
	 * @Method Name : wishlistPage
	 * @author : stella
	 * @date : 2016. 9. 2.
	 * @description : 쇼핑찜
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/activity/wishlist", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView wishlistPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "마이쇼핑|쇼핑찜");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}
	
	/**
	 * @Method Name : wishListAjax
	 * @author : ian
	 * @date : 2016. 9. 22.
	 * @description : 쇼핑찜 ajax list 
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/wishlist/ajax", method = { RequestMethod.GET })
	public ModelAndView wishListAjax(MmsMemberSearch search, HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/wishList");
		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setMemGradeCd(FoSessionUtil.getMemGradeCd());
		search.setPageSize(20);
		
		List<PmsProduct> wishList = mypageService.getShoppingWishlist(search);
		mv.addObject("wishList", wishList);
		if (wishList.size() > 0) {
			mv.addObject("totalCount", wishList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		
		mv.addObject("search", search);
		
		return mv;
	}

	/**
	 * 주문 상품 목록 조회
	 * 
	 * @Method Name : getProductListSearch
	 * @author : roy
	 * @date : 2016. 9. 12.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getProductListSearch(PmsProductReviewSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/productList");

		logger.debug("/product/list/ajax:" + search);

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setImgNo(new BigDecimal("0"));
		search.setPageSize(10);

		List<OmsOrderproduct> list = productReviewService.getReviewAbleProductlist(search);
		
		mv.addObject("list", list);
		mv.addObject("search", search);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;

	}

	/**
	 * 상품평 목록 조회
	 * 
	 * @Method Name : getReviewListSearch
	 * @author : roy
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/review/list/ajax", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getReviewListSearch(PmsProductReviewSearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/reviewList");

		search.setStoreId(SessionUtil.getStoreId());
		search.setMemberNo(SessionUtil.getMemberNo());

		search.setPageSize(10);

		if (CommonUtil.isEmpty(search.getStartDate())) {
			// 날짜 세팅
			DateUtil date = new DateUtil();
			String currentDate = date.getCurrentDate(date.FORMAT_2);
			String editDate = date.getAddMonth(date.FORMAT_2, currentDate, new BigDecimal(-1));

			search.setStartDate(editDate);
			search.setEndDate(currentDate);
		}

		List<PmsReview> list = productReviewService.getProductReviewListByMemberNo(search);

		PmsProductReviewSearch reviewSearch = new PmsProductReviewSearch();
		reviewSearch.setStartDate(search.getStartDate());
		reviewSearch.setEndDate(search.getEndDate());

		PmsReview listCount = productReviewService.getProductReviewCountByMemberNo(search);

		mv.addObject("list", list);
		mv.addObject("listCount", listCount);
		mv.addObject("search", search);

		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		return mv;

	}

	/**
	 * 
	 * @Method Name : styleManagement
	 * @author : allen
	 * @date : 2016. 10. 8.
	 * @description : 스타일 관리
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/activity/style", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView styleManagement(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/activity/activityStyle_list");

		MmsStyleSearch search = new MmsStyleSearch();
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setDisplayYn(BaseConstants.YN_Y);
		int styleTotalCnt = styleService.getMemberStyleTotalCnt(search);
		mv.addObject("styleTotalCnt", styleTotalCnt);

		return mv;
	}

	/**
	 * 
	 * @Method Name : getMemberStyleList
	 * @author : intune
	 * @date : 2016. 10. 8.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/activity/style/list/ajax", method = RequestMethod.POST)
	public ModelAndView getMemberStyleList(HttpServletRequest request, @RequestBody MmsStyleSearch search) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/styleProductList");

		search.setMemberNo(SessionUtil.getMemberNo());
		search.setDisplayYn(BaseConstants.YN_Y);
		search.setPageSize(20);
		List<MmsStyle> memberStyleList = styleService.getMemberStyleList(search);

		if (memberStyleList != null) {
			for (MmsStyle mmsStyle : memberStyleList) {
				String hashTag = "";
				if (StringUtils.isNotEmpty(mmsStyle.getGenderTypeCd())) {
					hashTag += " #" + mmsStyle.getGenderTypeName();
				}
				if (StringUtils.isNotEmpty(mmsStyle.getThemeCd())) {
					hashTag += " #" + mmsStyle.getThemeName();
					mmsStyle.setThemeCdName(mmsStyle.getThemeName());
				}
				if (StringUtils.isNotEmpty(mmsStyle.getBrandId())) {
					hashTag += " #" + mmsStyle.getBrandName();
				}
				mmsStyle.setHashTag(hashTag.trim());
			}
		}

		mv.addObject("memberStyleList", memberStyleList);
		mv.addObject("search", search);

		// paging
		if (memberStyleList != null && memberStyleList.size() > 0) {
			mv.addObject("totalCount", memberStyleList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		return mv;
	}
	
	/**
	 * @Method Name : shoppingAlarmPage
	 * @author : ian
	 * @date : 2016. 11. 7.
	 * @description : 쇼핑알림 보관함
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shoppingAlarm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView shoppingAlarmPage(MmsMemberSearch search, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		
		// 쇼핑알림 보관함은 모바일 전용
		if (!FoSessionUtil.isMobile(request)) {
			return (ModelAndView) new ModelAndView("redirect:/ccs/common/main");
		}
		
		search.setMemberNo(SessionUtil.getMemberNo());
		search.setSiteKey(Config.getString("tms.site.key"));
		
		List<ShoppingAlarm> alarmList = tmsService.getMemberShoppingAlarm(search);
		mv.addObject("alarmList", alarmList);
		
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "쇼핑알림 보관함");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}
	
	@RequestMapping(value = "/activity/clothesAs", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView clothesAsPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		MmsMemberSearch search = new MmsMemberSearch();

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "의류AS현황");
		mv.addObject("pageNavi", pageMap);
		
		return mv;
	}


	/**
	 * @Method Name : getClothesAsListAjax
	 * @author : ian
	 * @date : 2016. 11. 9.
	 * @description : 의류 as 현황 list 
	 *
	 * @param search
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/clothesAs/list/ajax", method = { RequestMethod.GET })
	public ModelAndView getClothesAsListAjax(MmsMemberSearch search, HttpServletRequest request) throws Exception {

		ModelAndView mv = new ModelAndView("/mms/mypage/activity/inner/clothesAsList");

		if (CommonUtil.isEmpty(search.getStartDate())) {
			// 기본세팅 날짜 : (현재 월 -1 ~ 현재 월)
			Map<String, String> map = defaultSetDate();
			search.setStartDate(map.get("startDate"));
			search.setEndDate(map.get("endDate"));
		}
		//search.setOrderId("e050661");	// test 임시 id
		search.setMemberNo(SessionUtil.getMemberNo());
		
		if (FoSessionUtil.isMobile(request)) {
			search.setPageSize(10);
		} else {
			/** PC 페이징 안함*/
			search.setFirstRow(0);
		}

		List<ClothesAfterService> clothesAsList = clothesAsService.getClothesAfterServiceList(search);

		mv.addObject("clothesAsList", clothesAsList);
		mv.addObject("search", search);

		// paging
		if (clothesAsList != null && clothesAsList.size() > 0) {
			mv.addObject("totalCount", clothesAsList.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}

		return mv;
	}

}
