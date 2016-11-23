package gcp.frontpc.controller.view.oms;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsCode;
import gcp.ccs.service.CodeService;
import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.external.model.membership.MemberPoint;
import gcp.external.model.membership.MembershipPointSearchReq;
import gcp.external.service.MembershipService;
import gcp.external.service.PgService;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.search.MmsMemberPopupSearch;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPickup;
import gcp.oms.model.OmsRegulardelivery;
import gcp.oms.service.OrderService;
import gcp.oms.service.PickupService;
import gcp.oms.service.RegularService;
import gcp.sps.model.search.SpsCardPromotionSearch;
import gcp.sps.service.CouponService;
import gcp.sps.service.DiscountService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@Controller("orderViewController")
@RequestMapping("oms/order")
public class OrderController {

	private static final Logger	logger	= LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private MembershipService	membershipService;

	@Autowired
	private CouponService	couponService;

	@Autowired
	private DiscountService		discountService;

	@Autowired
	private RegularService		regularService;

	@Autowired
	private PickupService		pickupService;

	@Autowired
	private CodeService			codeService;

	@Autowired
	private PgService			pgService;

	@RequestMapping(value = "/sheet", method = RequestMethod.POST)
	public ModelAndView orderSheet(@ModelAttribute OmsOrder omsOrder, HttpServletRequest request) throws Exception {

//		boolean orderTF = omsOrder.getOrderTF();
		String orderStat = omsOrder.getOrderStat();
		String selectPresent = omsOrder.getSelectPresent();

//		String checkOrder = (String) SessionUtil.getSession(request, BaseConstants.SESSION_KEY_CHECK_ORDER);
//		if (CommonUtil.isEmpty(checkOrder) || !"true".equals(checkOrder)) {
//			throw new ServiceException("AbstractAccessDecisionManager.accessDenied");
//		}

		String orderLoginReturn = omsOrder.getOrderLoginReturn();
		if ("true".equals(orderLoginReturn)) {
			OmsOrder omsOrderSession = (OmsOrder) SessionUtil.getSession(request, BaseConstants.SESSION_KEY_MOBILE_ORDER);
			if (CommonUtil.isNotEmpty(omsOrderSession)) {
				omsOrder = omsOrderSession;
				omsOrder.setOrderStat(orderStat);
				omsOrder.setSelectPresent(selectPresent);
			}
		}

		CcsUtil.setSessionLoginInfo(omsOrder, true);
		BigDecimal memberNo = omsOrder.getMemberNo();

		String paramCartProductNos = omsOrder.getCartProductNos();
		String paramSelectPresent = omsOrder.getSelectPresent();

		if (CommonUtil.isNotEmpty(paramCartProductNos)) {
			String cartProductNos = CommonUtil.convertInParam(paramCartProductNos);
			OmsCart omsCart = new OmsCart();
			omsCart.setCartProductNos(cartProductNos);
			omsOrder = orderService.getOmsOrderByCart(omsCart);

			omsOrder.setCartProductNos(paramCartProductNos);
			omsOrder.setOrderStat(orderStat);
			omsOrder.setSelectPresent(paramSelectPresent);
		} else {
			/*
			 * omsOrder - omsOrderproducts - omsOrderproducts
			 */
		}

		omsOrder.setMemberNo(memberNo);

		return order(omsOrder, request);
	}

	@RequestMapping(value = "/inner/presentList", method = RequestMethod.POST)
	public ModelAndView presentList(OmsOrder omsOrder, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();

		if (FoSessionUtil.isMobile(request)) {
			mv.setViewName("/oms/order/inner/presentList_mo");
		} else {
			mv.setViewName("/oms/order/inner/presentList");
		}

		String orderLoginReturn = omsOrder.getOrderLoginReturn();
		if ("true".equals(orderLoginReturn)) {
			omsOrder = (OmsOrder) SessionUtil.getSession(request, BaseConstants.SESSION_KEY_MOBILE_ORDER);
		}
		CcsUtil.setSessionLoginInfo(omsOrder, true);
		BigDecimal memberNo = omsOrder.getMemberNo();

		String paramCartProductNos = omsOrder.getCartProductNos();
		String orderStat = omsOrder.getOrderStat();
		String paramSelectPresent = omsOrder.getSelectPresent();

		if (CommonUtil.isNotEmpty(paramCartProductNos)) {
			String cartProductNos = CommonUtil.convertInParam(paramCartProductNos);
			OmsCart omsCart = new OmsCart();
			omsCart.setCartProductNos(cartProductNos);
			omsOrder = orderService.getOmsOrderByCart(omsCart);

			omsOrder.setCartProductNos(paramCartProductNos);
			omsOrder.setOrderStat(orderStat);
			omsOrder.setSelectPresent(paramSelectPresent);
		} else {
			/*
			 * omsOrder - omsOrderproducts - omsOrderproducts
			 */
		}

		omsOrder.setMemberNo(memberNo);

		omsOrder = orderService.getOrderList(omsOrder);

		omsOrder.setCartProductNos(paramCartProductNos);
		omsOrder.setSelectPresent(paramSelectPresent);

		mv.addObject("omsOrder", omsOrder);

		return mv;
	}

	private ModelAndView order(OmsOrder omsOrder, HttpServletRequest request) throws Exception {
		boolean mobilecheck = FoSessionUtil.isMobile(request);
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request, mobilecheck));

		String paramCartProductNos = omsOrder.getCartProductNos();
		String paramSelectPresent = omsOrder.getSelectPresent();
		String orderStat = omsOrder.getOrderStat();

		//OmsOrder omsOrder = new OmsOrder();
		BigDecimal memberNo = omsOrder.getMemberNo();
		omsOrder.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		omsOrder = orderService.getOrderList(omsOrder);
		omsOrder.setCartProductNos(paramCartProductNos);
		omsOrder.setSelectPresent(paramSelectPresent);

		mv.addObject("omsOrder", omsOrder);
		if ("PRESENT".equals(orderStat)) {	//사은품이 있으면 사은품 선택 페이지로 간다.
			if (mobilecheck) {
				mv.setViewName("/oms/order/presentList_mo");
			} else {
				mv.setViewName("/oms/order/presentList");
			}
		} else {
			MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
			mmsMemberSearch.setMemberNo(memberNo);

			if (CommonUtil.isNotEmpty(memberNo)) {
				//회원정보
				mv.addObject("memberInfo", memberService.getMemberDetail(memberNo));
				//배송주소록
				mv.addObject("memberAddressList", memberService.getMemberAddressList(mmsMemberSearch));
				//예치금
				MmsMemberPopupSearch mmsMemberPopupSearch = new MmsMemberPopupSearch();
				mmsMemberPopupSearch.setMemberNo(memberNo);
				MmsDeposit mmsDeposit = new MmsDeposit();
				BigDecimal balanceAmt = memberService.getDepositBalanceAmt(mmsMemberPopupSearch);
				if (balanceAmt != null) {
					mmsDeposit.setBalanceAmt(balanceAmt);
				} else {
					mmsDeposit.setBalanceAmt(new BigDecimal(0));
				}
				mv.addObject("memberDeposit", mmsDeposit);

				//보유쿠폰개수
				mv.addObject("memberCouponTotal", couponService.getCouponCountByMemberNo(omsOrder));

				//포인트
				if (SessionUtil.isMemberLogin()) {
					try {

						MembershipPointSearchReq req = new MembershipPointSearchReq();
						req.setSrchDv("I");//조회구분‘I’- 통합회원번호, ‘C’- 멤버십카드번호 ‘T’- 휴대전화번호
						req.setSrchDvVlu(memberNo.toString());//회원번호
						MemberPoint point = membershipService.getMemberPoint(req);
						mv.addObject("memberPoint", point);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				//카드사할인정보
				SpsCardPromotionSearch search = new SpsCardPromotionSearch();
				search.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
				search.setStoreId(SessionUtil.getStoreId());
				mv.addObject("cardPromotionList", discountService.getApplyCardPromotion(search));
				
				//제휴사일때.
				String channelId = omsOrder.getChannelId();
				if(CommonUtil.isNotEmpty(channelId)){
					CcsCode ccsCode = new CcsCode();
					ccsCode.setCdGroupCd("PAYMENT_BUSINESS_CD");
					ccsCode.setNote(channelId);
					ccsCode = codeService.getCode(ccsCode);
					if (CommonUtil.isNotEmpty(ccsCode)) {
						mv.addObject("staticPaymentCard", ccsCode.getCd());
					}

					mv.addObject("channelId", channelId);
				}

				//다자녀 check
				String childrenDealId = FoSessionUtil.getChildrenDealId();
				logger.debug("children deal id : " + childrenDealId);
				if (CommonUtil.isNotEmpty(childrenDealId)) {
					int childrenCnt = 0;
					for (OmsOrderproduct product : omsOrder.getOmsOrderproducts()) {
						String productDealId = product.getDealId();
						logger.debug("product id : " + product.getProductId());
						logger.debug("deal id : " + product.getDealId());
						if (CommonUtil.isNotEmpty(productDealId) && childrenDealId.equals(productDealId)) {
							childrenCnt++;
						}
					}
					logger.debug("children cnt : " + childrenCnt);
					if (childrenCnt > 0) {
						if (childrenCnt != omsOrder.getOmsOrderproducts().size()) {
							throw new ServiceException("oms.order.childrenDealError");
						}
						String staticPaymentCard = Config.getString("card.type.children." + childrenDealId);
						if (CommonUtil.isNotEmpty(staticPaymentCard)) {
							mv.addObject("staticPaymentCard", staticPaymentCard);
							mv.addObject("childrenDealYn", "Y");
						}
					}
				}

			}

			//비원원유무
			mv.addObject("memberYn", FoSessionUtil.isMemberLogin() ? "Y" : "N");

		}

		//카드사 무이자정보 interface
		pgService.getInterest(omsOrder.getOrderTypeCd(), FoSessionUtil.getDeviceTypeCd());
		
		return mv;
	}

	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public ModelAndView complete(@ModelAttribute OmsOrder omsOrder, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request, FoSessionUtil.isMobile(request)));

		CcsUtil.setSessionLoginInfo(omsOrder, true);
		mv.addObject("omsOrder", orderService.getOrderComplete(omsOrder));
		SessionUtil.removeSession(request, BaseConstants.SESSION_KEY_CHECK_ORDER);
		return mv;
	}


	@RequestMapping(value = "/regulardelivery", method = RequestMethod.POST)
	public ModelAndView regulardelivery(@ModelAttribute OmsRegulardelivery omsRegulardelivery, HttpServletRequest request)
			throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request, FoSessionUtil.isMobile(request)));

		CcsUtil.setSessionLoginInfo(omsRegulardelivery);
		String paramCartProductNos = omsRegulardelivery.getCartProductNos();
		omsRegulardelivery.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		omsRegulardelivery.setStoreId(SessionUtil.getStoreId());

		if (CommonUtil.isNotEmpty(paramCartProductNos)) {
			String cartProductNos = CommonUtil.convertInParam(paramCartProductNos);
			OmsCart omsCart = new OmsCart();
			omsCart.setCartProductNos(cartProductNos);
			omsCart.setMemberNo(omsRegulardelivery.getMemberNo());
			omsRegulardelivery = regularService.getOmsRegulardeliveryByCart(omsCart);

			omsRegulardelivery.setCartProductNos(paramCartProductNos);
		} else {
			/*
			 * omsOrder - omsOrderproducts - omsOrderproducts
			 */
		}
		
		omsRegulardelivery = regularService.getRegulardeliveryOrderList(omsRegulardelivery);
		mv.addObject("regular", omsRegulardelivery);

		//회원정보
		mv.addObject("memberInfo", memberService.getMemberDetail(omsRegulardelivery.getMemberNo()));
		MmsMemberSearch mmsMemberSearch = new MmsMemberSearch();
		mmsMemberSearch.setMemberNo(omsRegulardelivery.getMemberNo());
		//배송주소록
		mv.addObject("memberAddressList", memberService.getMemberAddressList(mmsMemberSearch));

		//카드사할인정보
		SpsCardPromotionSearch search = new SpsCardPromotionSearch();
		search.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		search.setStoreId(SessionUtil.getStoreId());
		mv.addObject("cardPromotionList", discountService.getApplyCardPromotion(search));

		return mv;
	}

	@RequestMapping(value = "/regularComplete", method = RequestMethod.POST)
	public ModelAndView regularComplete(@ModelAttribute OmsRegulardelivery omsRegulardelivery, HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request, FoSessionUtil.isMobile(request)));
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));

		CcsUtil.setSessionLoginInfo(omsRegulardelivery, true);
		mv.addObject("omsRegulardelivery", regularService.getOrderRegularComplete(omsRegulardelivery));
		return mv;
	}

	@RequestMapping(value = "/pickupComplete", method = RequestMethod.POST)
	public ModelAndView pickupComplete(@ModelAttribute OmsPickup omsPickup, HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request, FoSessionUtil.isMobile(request)));
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		CcsUtil.setSessionLoginInfo(omsPickup, true);

		mv.addObject("omsPickup", pickupService.getOrderPickupComplete(omsPickup));
		return mv;
	}

	@RequestMapping(value = "/login/return", method = RequestMethod.GET)
	public ModelAndView loginReturn(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/oms/order/orderLoginReturn");
		return mv;
	}

}
