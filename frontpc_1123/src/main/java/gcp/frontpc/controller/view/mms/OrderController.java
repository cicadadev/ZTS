package gcp.frontpc.controller.view.mms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.FoSessionUtil;
import gcp.mms.model.MmsAddress;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.search.OmsClaimSearch;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.model.search.OmsPaymentSearch;
import gcp.oms.service.OrderService;
import gcp.pms.model.PmsProductoption;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("mmsOrderViewController")
@RequestMapping({ "mms/mypage/order" ,"ccs/guest/order" })
public class OrderController {

	// private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ProductService productService;

	@RequestMapping(value = { "/history" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public String makeView(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("tabIdx", CommonUtil.replaceNull(request.getParameter("tabIdx"), "0"));
		return CommonUtil.makeJspUrl(request);
	}

	@RequestMapping(value = "/inner/{tabType}" ,method = { RequestMethod.POST })
	public String selectList(Model model, HttpServletRequest request, @ModelAttribute OmsOrderSearch orderSearch, @PathVariable String tabType)
			throws Exception {
		orderSearch.setStoreId(SessionUtil.getStoreId());
		orderSearch.setMemberNo(SessionUtil.getMemberNo());
		orderSearch.setSearchId("oms.order.select.foList");

		if ("pickup".equals(tabType)) {
			// 1. 지역,지점 검색
			orderSearch.setOrderer(SessionUtil.getMemberNo().toString());
			orderSearch.setOrdererType("number");

			orderSearch.setSearchId("oms.pickup.selectAreaList");
			List<?> pickupAreaList = orderService.selectList(orderSearch.getSearchId(), orderSearch);

			model.addAttribute("pickupAreaList", pickupAreaList);
			model.addAttribute("orderSearch", orderSearch);

			// 2. 픽업상품 검색
			orderSearch.setSearchId("oms.pickup.selectFoList");
		} else if ("regular".equals(tabType)) {
			orderSearch.setOrderTypeCds("ORDER_TYPE_CD.REGULARDELIVERY");
		} else if ("general".equals(tabType)) {
		}
		List<?> orderList = orderService.selectList(orderSearch.getSearchId(), orderSearch);
		model.addAttribute("orderList", orderList);

		return CommonUtil.makeJspUrl(request);
	}

	/**
	 * 주문상세 조회
	 * 
	 * @Method Name : selectOne
	 * @author : victor
	 * @date : 2016. 10. 6.
	 * @description : 주문상세 페이지에서 필요한 각종 주문 데이터 조회
	 *
	 * @param model
	 * @param orderSearch
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void selectOne(Model model, OmsOrderSearch orderSearch) throws Exception {
		Object omsOrder = orderService.selectOne("oms.order.select.fodetail", orderSearch);
		Object omsDelivery = orderService.selectOne("oms.delivery.selectAmt", orderSearch.getOrderId());
		List<?> couponList = orderService.selectList("oms.order.select.coupon", orderSearch.getOrderId());
		// List<?> couponList = orderService.selectList("oms.order.select.couponList", orderSearch);

		OmsClaimSearch claimSearch = new OmsClaimSearch();
		claimSearch.setOrderId(orderSearch.getOrderId());
		List<OmsPayment> paymentList = (List<OmsPayment>) orderService.selectList("oms.payment.selectList", claimSearch);
		String partCancelYn = "Y";
		if (paymentList != null) {
			for (OmsPayment omsPayment : paymentList) {
				if ("N".equals(omsPayment.getPartialCancelYn())) {
					partCancelYn = "N";
				}
			}
		}

		model.addAttribute("partCancelYn", partCancelYn);

		model.addAttribute("order", omsOrder);
		model.addAttribute("delivery", omsDelivery);
		model.addAttribute("couponList", couponList);
		model.addAttribute("paymentList", paymentList);
	}

	@RequestMapping(value = "/{orderType}/{orderId}" ,method = RequestMethod.GET)
	public String order(Model model, @PathVariable String orderType, @PathVariable String orderId) throws Exception {
		OmsOrderSearch orderSearch = new OmsOrderSearch();
		orderSearch.setOrderId(orderId);
		if ("pickup".equals(orderType)) {
			orderSearch.setStoreId(SessionUtil.getStoreId());
			orderSearch.setSearchId("oms.pickup.selectFoList");

			List<?> orderList = orderService.selectList(orderSearch.getSearchId(), orderSearch);
			model.addAttribute("pickup", orderList.get(0));
		} else {
			this.selectOne(model, orderSearch);
		}
		return "/mms/mypage/order/order" + StringUtils.capitalize(orderType);
	}

	@RequestMapping(value = { "/cancel" ,"/exchange" ,"/return" } ,method = { /* RequestMethod.GET , */RequestMethod.POST })
	public String claim(Model model, HttpServletRequest request, @ModelAttribute OmsOrderSearch orderSearch) throws Exception {
		model.addAttribute("cancelAll", StringUtils.isEmpty(orderSearch.getOrderProductNo()) ? true : false);
		this.selectOne(model, orderSearch);

		return CommonUtil.makeJspUrl(request);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/layer/{module}" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public String layer(Model model, HttpServletRequest request, @PathVariable String module, @RequestBody OmsPaymentSearch omsPaymemtSearch,
			@RequestBody OmsOrderSearch orderSearch) throws Exception {
		if (module.equals("payment")) {
			model.addAttribute("omsPaymemtSearch", omsPaymemtSearch);
		} else if (module.equals("deliveryTracking")) {
			List<OmsLogistics> logisticss = (List<OmsLogistics>) orderService.selectList("oms.delivery.tracking", omsPaymemtSearch);
			model.addAttribute("logistics", logisticss != null && logisticss.size() > 0 ? logisticss.get(0) : null);
		} else {
			model.addAttribute("cancelAll", StringUtils.isEmpty(orderSearch.getOrderProductNo()) ? true : false);
			this.selectOne(model, orderSearch);
		}
		// return CommonUtil.makeJspUrl(request);
		return "/mms/mypage/order/layer/layer" + StringUtils.capitalize(module);
	}

	/**
	 * 
	 * @Method Name : orderGiftCert
	 * @author : intune
	 * @date : 2016. 9. 23.
	 * @description : 선물함 인증
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/giftCert" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public ModelAndView orderGiftCert(HttpServletRequest request, MmsMemberSearch search) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		if (!FoSessionUtil.isMobile(request)) {
			// TODO : 에러페이지
		}

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "받은 선물함");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}

	/**
	 * 
	 * @Method Name : orderGift
	 * @author : allen
	 * @date : 2016. 9. 12.
	 * @description : 선물함 관리
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gift" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public ModelAndView orderGift(HttpServletRequest request, MmsMemberSearch search) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		if (SessionUtil.isMemberLogin()) {
			FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo();
			// search.setGiftName(loginInfo.getLoginName());
			search.setGiftPhone(loginInfo.getPhone2());
		}

		MmsMemberSearch searchResult = memberService.getMemberGiftOrderCnt(search);

		mv.addObject("giftTotalCnt", searchResult.getGiftTotalCnt());
		mv.addObject("notReceiveCnt", searchResult.getNotReceiveCnt());
		mv.addObject("search", search);
		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "선물함");
		mv.addObject("pageNavi", pageMap);
		return mv;
	}

	/**
	 * 
	 * @Method Name : orderGiftDetail
	 * @author : intune
	 * @date : 2016. 9. 23.
	 * @description : 선물 상세
	 *
	 * @param request
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/giftDetail" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public ModelAndView orderGiftDetail(HttpServletRequest request, MmsMemberSearch search) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		MmsAddress basicAddress = new MmsAddress();
		OmsOrder giftOrderDetail = memberService.getGiftOrderDetail(search);

		if (FoSessionUtil.isMemberLogin()) {
			// 기본배송지 조회
			search.setMemberNo(FoSessionUtil.getMemberNo());
			basicAddress = memberService.getMemberBasicAddress(search);
			mv.addObject("basicAddress", basicAddress);
		}

		// 단품 옵션 정보
		if (giftOrderDetail != null) {
			PmsProductSearch productSearch = new PmsProductSearch();
			productSearch.setProductId(giftOrderDetail.getOmsOrderproducts().get(0).getProductId());
			productSearch.setSaleproductId(giftOrderDetail.getOmsOrderproducts().get(0).getSaleproductId());

			Map<String, Object> optionMap = productService.getOptionList(productSearch);

			List<PmsProductoption> optionList = (List<PmsProductoption>) optionMap.get("optionList");
			Map<String, String> selectedOptionMap = (Map<String, String>) optionMap.get("selectedOptionMap");

			mv.addObject("optionList", optionList);
			mv.addObject("selected", selectedOptionMap);

		}
		mv.addObject("giftOrderDetail", giftOrderDetail);

		Map<String, String> pageMap = new HashMap<String, String>();
		pageMap.put("name", "받은 선물");
		mv.addObject("pageNavi", pageMap);

		return mv;
	}

	/**
	 * 
	 * @Method Name : giftOrderAjax
	 * @author : intune
	 * @date : 2016. 9. 23.
	 * @description :
	 *
	 * @param request
	 * @param mmsMemberSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gift/list/ajax" ,method = RequestMethod.POST)
	public ModelAndView giftOrderAjax(HttpServletRequest request, @RequestBody MmsMemberSearch mmsMemberSearch) throws Exception {
		ModelAndView mv = new ModelAndView("/mms/mypage/order/inner/giftOrderList");

		mmsMemberSearch.setMemberNo(SessionUtil.getMemberNo());
		mmsMemberSearch.setPageSize(5);
		mmsMemberSearch.setStoreId(SessionUtil.getStoreId());
		if (FoSessionUtil.isMobile(request)) {
			mmsMemberSearch.setMobileYn("Y");
		} else {
			mmsMemberSearch.setMobileYn("N");
		}

		if (SessionUtil.isMemberLogin()) {
			FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo();
			// mmsMemberSearch.setGiftName(loginInfo.getLoginName());
			mmsMemberSearch.setGiftPhone(loginInfo.getPhone2());
		}

		List<OmsOrder> list = memberService.getMemberGiftOrderList(mmsMemberSearch);

		mv.addObject("giftOrderList", list);
		mv.addObject("search", mmsMemberSearch);
		if (list != null && list.size() > 0) {
			mv.addObject("totalCount", list.get(0).getTotalCount());
		} else {
			mv.addObject("totalCount", 0);
		}
		return mv;
	}

}
