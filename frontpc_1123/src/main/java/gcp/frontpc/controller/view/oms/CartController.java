package gcp.frontpc.controller.view.oms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.oms.model.OmsCart;
import gcp.oms.model.search.OmsCartSearch;
import gcp.oms.service.CartService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("cartViewController")
@RequestMapping("oms/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	/**
	 * 
	 * @Method Name : cart
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView cart(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(CommonUtil.makeJspUrl(request));
		OmsCartSearch omsCartSearch = new OmsCartSearch();

		CcsUtil.setSessionLoginInfo(omsCartSearch, true);
		omsCartSearch.setChannelId(SessionUtil.getChannelId());
		OmsCart cartCnt = cartService.getCartCnt(omsCartSearch);

		List<OmsCart> cartList = null;
		String cartTypeCd = request.getParameter("cartTypeCd");
		if (CommonUtil.isEmpty(cartTypeCd) || cartTypeCd.indexOf("undefined") > -1) {
			cartTypeCd = BaseConstants.CART_TYPE_CD_GENERAL;
		}
		omsCartSearch.setCartTypeCd(cartTypeCd);
		omsCartSearch.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));

//		if (cartCnt != null) {
//			cartCnt.setCartTypeCd(cartTypeCd);
//
//			if (BaseConstants.CART_TYPE_CD_GENERAL.equals(cartTypeCd) && cartCnt.getGeneralCnt() > 0) {
//				cartList = cartService.getCartList(omsCartSearch);
//			} else if (BaseConstants.CART_TYPE_CD_PICKUP.equals(cartTypeCd) && cartCnt.getPickupCnt() > 0) {
//				cartList = cartService.getCartList(omsCartSearch);
//			} else if (BaseConstants.CART_TYPE_CD_REGULARDELIVERY.equals(cartTypeCd) && cartCnt.getRegulardeliveryCnt() > 0) {
//				cartList = cartService.getCartList(omsCartSearch);
//			}
//		}
		cartCnt.setCartTypeCd(cartTypeCd);
		cartList = cartService.getCartList(omsCartSearch);

		mv.addObject("cartCnt", cartCnt);
		mv.addObject("cartList", cartList);
		mv.addObject("memberYn", FoSessionUtil.isMemberLogin() ? "Y" : "N");

		return mv;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public ModelAndView cartInsert(HttpServletRequest request) {
		return new ModelAndView(CommonUtil.makeJspUrl(request));
	}

}
