package gcp.frontpc.controller.view.ccs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gcp.common.util.FoSessionUtil;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.search.OmsClaimSearch;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.model.search.OmsPaymentSearch;
import gcp.oms.service.DeliveryService;
import gcp.oms.service.OrderService;
import gcp.pms.service.ProductService;

@Controller("ccsGuestViewController")
@RequestMapping("ccs/guest")
public class GuestController {

	// private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ProductService productService;

	@Autowired
	private DeliveryService deliveryService;

	/**
	 * 비회원 주문조회
	 * 
	 * @Method Name : nonmemberorder
	 * @author : eddie
	 * @date : 2016. 9. 21.
	 * @description :
	 *
	 * @param omsOrder
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order" ,method = RequestMethod.GET)
	public String guestOrder(Model model, HttpServletRequest request) throws Exception {
		FoLoginInfo loginInfo = (FoLoginInfo) FoSessionUtil.getLoginInfo(request);
		String orderId = loginInfo.getLoginId();

		OmsOrderSearch orderSearch = new OmsOrderSearch();
		orderSearch.setOrderId(orderId);

		this.selectOne(model, orderSearch);
		return "/ccs/guest/order/orderGuest";
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

	// @RequestMapping(value = { "/cancel" ,"/exchange" ,"/return" } ,method = { /* RequestMethod.GET , */RequestMethod.POST })
//	@RequestMapping(value = "/order/{module}" ,method = { /* RequestMethod.GET , */RequestMethod.POST })
//	public String claim(Model model, @RequestBody OmsOrderSearch orderSearch, @PathVariable String module) throws Exception {
//		model.addAttribute("cancelAll", StringUtils.isEmpty(orderSearch.getOrderProductNo()) ? true : false);
//		this.selectOne(model, orderSearch);
//
//		return "/ccs/guest/order/layer/order" + StringUtils.capitalize(module);
//	}

}
