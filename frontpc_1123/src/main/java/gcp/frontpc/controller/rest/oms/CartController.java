package gcp.frontpc.controller.rest.oms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.oms.model.OmsCart;
import gcp.oms.model.search.OmsCartSearch;
import gcp.oms.service.CartService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CookieUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	/**
	 * 
	 * @Method Name : saveCart
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 저장
	 *
	 * @param omsCart
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Map<String, String> saveCart(@ModelAttribute OmsCart omsCart, HttpServletRequest request) throws Exception {
		Map<String, String> result = new HashMap<String,String>();
		result.put(BaseConstants.RESULT_FLAG,BaseConstants.RESULT_FLAG_FAIL);
		CcsUtil.setSessionLoginInfo(omsCart, true);
		String cartTypeCd = omsCart.getCartTypeCd();
		omsCart.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		if ("CART_TYPE_CD.REGULARDELIVERY".equals(cartTypeCd) && !FoSessionUtil.isMemberLogin()) {
			result.put(BaseConstants.RESULT_MESSAGE, MessageUtil.getMessage("oms.cart.save.nonmember", new String[] { "정기배송" }));
		} else if ("CART_TYPE_CD.PICKUP".equals(cartTypeCd) && !FoSessionUtil.isMemberLogin()) {
			result.put(BaseConstants.RESULT_MESSAGE, MessageUtil.getMessage("oms.cart.save.nonmember", new String[] { "매장픽업" }));
		}else{
			result = cartService.saveCart(omsCart);
		}
		return result;
	}

	@RequestMapping(value = "/saveMulti", method = RequestMethod.POST)
	public Map<String, String> saveCartMulti(@RequestBody List<OmsCart> omsCartList, HttpServletRequest request)
			throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

//		List<OmsCart> cartList = (List<OmsCart>) omsCartList.get("cartList");
		for (OmsCart omsCart : omsCartList) {
			CcsUtil.setSessionLoginInfo(omsCart, true);
			String cartTypeCd = omsCart.getCartTypeCd();
			omsCart.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
			if ("CART_TYPE_CD.REGULARDELIVERY".equals(cartTypeCd) && !FoSessionUtil.isMemberLogin()) {
				result.put(BaseConstants.RESULT_MESSAGE,
						MessageUtil.getMessage("oms.cart.save.nonmember", new String[] { "정기배송" }));
			} else if ("CART_TYPE_CD.PICKUP".equals(cartTypeCd) && !FoSessionUtil.isMemberLogin()) {
				result.put(BaseConstants.RESULT_MESSAGE,
						MessageUtil.getMessage("oms.cart.save.nonmember", new String[] { "매장픽업" }));
			} else {
				result = cartService.saveCart(omsCart);
			}
			if (!BaseConstants.RESULT_FLAG_SUCCESS.equals(result.get(BaseConstants.RESULT_FLAG))) {
				return result;
			}
		}

		return result;
	}

	@RequestMapping(value = "/keep", method = RequestMethod.POST)
	public Map<String, String> keepCart(@RequestBody OmsCart omsCart) throws Exception {
		CcsUtil.setSessionLoginInfo(omsCart, true);
		Map<String, String> result = cartService.saveKeep(omsCart);
		return result;
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public Map<String, String> transferCart(@ModelAttribute OmsCart omsCart, HttpServletRequest request) throws Exception {
		CcsUtil.setSessionLoginInfo(omsCart, true);
		omsCart.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		Map<String, String> result = cartService.saveCartTransfer(omsCart);
		return result;
	}

	@RequestMapping(value = "/saveList", method = RequestMethod.POST)
	public Map<String, String> saveCartList(@RequestBody OmsCartSearch omsCartSearch) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		CcsUtil.setSessionLoginInfo(omsCartSearch, true);
		String cartTypeCd = omsCartSearch.getCartTypeCd();
		if ("CART_TYPE_CD.REGULARDELIVERY".equals(cartTypeCd) && !FoSessionUtil.isMemberLogin()) {
			result.put(BaseConstants.RESULT_MESSAGE, MessageUtil.getMessage("oms.cart.save.nonmember", new String[] { "정기배송" }));
		} else if ("CART_TYPE_CD.PICKUP".equals(cartTypeCd) && !FoSessionUtil.isMemberLogin()) {
			result.put(BaseConstants.RESULT_MESSAGE, MessageUtil.getMessage("oms.cart.save.nonmember", new String[] { "매장픽업" }));
		} else {
			result = cartService.saveCartList(omsCartSearch);
		}
		return result;
	}

	/**
	 * 
	 * @Method Name : deleteCart
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 삭제
	 *
	 * @param omsCartList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, String> deleteCart(@RequestBody List<OmsCart> omsCartList) throws Exception {
		OmsCart omsCart = new OmsCart();
		CcsUtil.setSessionLoginInfo(omsCart, true);
		Map<String, String> result = cartService.deleteCart(omsCartList, omsCart.getStoreId(), omsCart.getCartId());
		return result;
	}

	/**
	 * 
	 * @Method Name : getCartCnt
	 * @author : dennis
	 * @date : 2016. 8. 2.
	 * @description : 장바구니 개수
	 *
	 * @return
	 */
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public OmsCart getCartCnt(HttpServletRequest request) {
		OmsCartSearch omsCartSearch = new OmsCartSearch();
		CcsUtil.setSessionLoginInfo(omsCartSearch, true);
		return (OmsCart) cartService.getCartCnt(omsCartSearch);
	}

	/**
	 * 
	 * @Method Name : mergeCart
	 * @author : dennis
	 * @date : 2016. 8. 4.
	 * @description : 장바구니 비회원 -> 회원 merge
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/merge", method = RequestMethod.POST)
	public Map<String, String> mergeCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		FoLoginInfo nonMemberLoginInfo = CcsUtil.getNonMemberInfo(request);
		FoLoginInfo sessionLoginInfo = (FoLoginInfo) SessionUtil.getLoginInfo(request);
		Map result = cartService.mergeCartData(nonMemberLoginInfo, sessionLoginInfo, FoSessionUtil.getDeviceTypeCd(request));
		if (BaseConstants.RESULT_FLAG_SUCCESS.equals(result.get(BaseConstants.RESULT_FLAG))) {
			CookieUtil.removeCookie(response, BaseConstants.COOKIE_NON_MEMBER_INFO);
		}
		return result;
	}
}
