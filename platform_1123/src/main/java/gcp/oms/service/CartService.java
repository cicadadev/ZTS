package gcp.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsPolicy;
import gcp.ccs.service.BaseService;
import gcp.ccs.service.PolicyService;
import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.mms.model.custom.FoLoginInfo;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsCart;
import gcp.oms.model.search.OmsCartSearch;
import gcp.pms.model.PmsOffshopstock;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.custom.PmsOptimalprice;
import gcp.pms.service.PriceService;
import gcp.pms.service.ProductService;
import gcp.sps.service.DealService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : CartService.java
 * @author : dennis
 * @date : 2016. 4. 20.
 * @description : Cart Service
 */
@Service
public class CartService extends BaseService {

	private static final Logger	logger	= LoggerFactory.getLogger(CartService.class);

	@Autowired
	private PolicyService policyService;

	@Autowired
	private ProductService	productService;

	@Autowired
	private PriceService		priceService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private DealService		dealService;

	/**
	 * 
	 * @Method Name : getCartCnt
	 * @author : dennis
	 * @date : 2016. 7. 4.
	 * @description : 장바구니 개수 조회
	 *
	 * @param omsCartSearch
	 * @return
	 */
	public OmsCart getCartCnt(OmsCartSearch omsCartSearch) {
		return (OmsCart) dao.selectOne("oms.cart.getCartCnt", omsCartSearch);
	}

	/**
	 * 
	 * @Method Name : getCartList
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 조회
	 *
	 * @param omsCartSearch
	 * @return
	 * @throws Exception
	 */
	public List<OmsCart> getCartList(OmsCartSearch omsCartSearch) throws Exception {
		BigDecimal memberNo = omsCartSearch.getMemberNo();
		String deviceTypeCd = omsCartSearch.getDeviceTypeCd();

		List<String> memberTypeCds = new ArrayList();
		memberService.getMemberTypeInfo(memberTypeCds);

		String memGradeCd = (String) FoSessionUtil.getMemGradeCd();	//회원등급

		List<String> dealTypeList = dealService.convertMemberTypeToDealType(memberTypeCds);
		String dealTypeCds = CommonUtil.convertInParam(dealTypeList);	//deal 유형코드

		omsCartSearch.setMemGradeCd(memGradeCd);
		omsCartSearch.setDealTypeCds(dealTypeCds);
		omsCartSearch.setChannelId(SessionUtil.getChannelId());	//제휴채널로 들어왔을때.

		List<OmsCart> cartList = (List<OmsCart>) dao.selectList("oms.cart.getCartList", omsCartSearch);
		if ("CART_TYPE_CD.GENERAL".equals(omsCartSearch.getCartTypeCd())) {
			int cnt = 1;
			for (OmsCart cart : cartList) {
				PmsOptimalprice price = new PmsOptimalprice();
				price.setMemberNo(memberNo);
				price.setProductId(cart.getProductId());
				price.setSaleproductId(cart.getSaleproductId());
				price.setSalePrice(cart.getSalePrice());
				price.setAddSalePrice(cart.getAddSalePrice());
				price.setTargetAmt(cart.getOrgTotalSalePrice());
				price.setStoreId(cart.getStoreId());
				price.setChannelId(cart.getChannelId());
				price.setDeviceTypeCd(deviceTypeCd);
				price.setMemGradeCd(memGradeCd);
				price.setMemberTypeCds(memberTypeCds);
				price.setCommissionRate(cart.getCommissionRate());
				PmsOptimalprice opt = priceService.optimalPrice(price, cart);
//				List<PmsOptimalprice> priceList = productService.optimalPriceList(price);
//
//				cart.setOptimalprices(priceList);
				cart.setOptimalprice(opt);

				//상품의 배송비무료여부가 N일때 딜걸려있으면 딜배송비무료여부 세팅.
				if ("N".equals(cart.getPolicyDeliveryFeeFreeYn()) && CommonUtil.isNotEmpty(opt.getDealId())) {
					cart.setPolicyDeliveryFeeFreeYn(opt.getDeliveryFeeFreeYn());
				}
				
				// mypage 메인 카트 목록 6개 제한
				if(CommonUtil.isNotEmpty(omsCartSearch.getIsMypage())) {
					if(cnt > 6) {
						cnt++;
					} else {
						break;
					}
				}
			}

			//장바구니에서는 상품간 check필요없음.
			//calcOptimalprice(cartList);
		}

		return cartList;
	}

	private void calcOptimalprice(List<OmsCart> cartList) {

		List<PmsOptimalprice> confirmList = new ArrayList<PmsOptimalprice>();

		logger.debug("============ calcOptimalprice start");
		int idx = 0;
		for (OmsCart cart : cartList) {
			PmsOptimalprice opt = cart.getOptimalprice();
			if (opt != null) {
				BigDecimal cartProductNo = cart.getCartProductNo();

//				logger.debug("productName : " + cart.getProductName());
//				logger.debug("productId : " + cart.getProductId());
//				logger.debug("productNo : " + cartProductNo);

				BigDecimal totalSalePrice = opt.getTotalSalePrice();
				BigDecimal couponDcAmt = opt.getCouponDcAmt();
				BigDecimal optSalePrice = totalSalePrice.subtract(couponDcAmt);
				String couponId = opt.getCouponId();
				String dealId = opt.getDealId();
//				logger.debug("totalSalePrice : " + totalSalePrice);
//				logger.debug("couponDcAmt : " + couponDcAmt);

				if (couponId != null) {

					int subIdx = 0;
					for (OmsCart subProduct : cartList) {

						if (idx < subIdx) {
							PmsOptimalprice subOpt = subProduct.getOptimalprice();
							if (subOpt != null) {
								String subCouponId = subOpt.getCouponId();
								String subDealId = subOpt.getDealId();

								boolean eq = false;
								if (couponId.equals(subCouponId)) {
									eq = true;
								}

								//같은 할인적용 쿠폰이있을때.
								if (eq && cartProductNo.compareTo(subProduct.getCartProductNo()) != 0) {
									selectNextOptimalprice(subProduct, confirmList);
								}
							}
						}
						subIdx++;
					}
				}
			}

			idx++;

//			logger.debug("================= CART ====================");
//			logger.debug(cart.toString());
//			logger.debug("================= CART ====================");
		}

		logger.debug("============ calcOptimalprice end");
	}

	private void selectNextOptimalprice(OmsCart product, List<PmsOptimalprice> confirmList) {

		PmsOptimalprice opt = null;

		for (PmsOptimalprice price : product.getOptimalprices()) {
			BigDecimal seq = price.getSeq();

			boolean ex = false;
			for (PmsOptimalprice confirmPrice : confirmList) {
				BigDecimal cseq = confirmPrice.getSeq();
				if (seq.compareTo(cseq) == 0) {
					ex = true;
				}
			}
			if (!ex) {
				if (opt == null) {
					opt = price;
				} else {
					BigDecimal totalSalePrice = opt.getTotalSalePrice();
					BigDecimal couponDcAmt = opt.getCouponDcAmt();
					BigDecimal calcAmt = totalSalePrice.subtract(couponDcAmt);

					BigDecimal totalSalePrice2 = price.getTotalSalePrice();
					BigDecimal couponDcAmt2 = price.getCouponDcAmt();
					BigDecimal calcAmt2 = totalSalePrice2.subtract(couponDcAmt2);

					if (calcAmt.compareTo(calcAmt2) < 0) {
						opt = price;
					}
				}
			}
		}
		if (opt != null) {
			confirmList.add(opt);
			product.setOptimalprice(opt);
			priceService.setOptimalField(product, opt);
		} else {
			product.setOptimalprice(null);
		}

	}

	public Map<String, String> saveCartList(OmsCartSearch omsCartSearch) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		String[] cartProductNos = omsCartSearch.getCartProductNos().split(",");
		String storeId = omsCartSearch.getStoreId();
		boolean priceChange = omsCartSearch.isPriceChange();

		if (priceChange) {
			List<OmsCart> cartList = getCartList(omsCartSearch);
			for (OmsCart cart : cartList) {
				for (String cartProductNo : cartProductNos) {
					if (cart.getCartProductNo().compareTo(new BigDecimal(cartProductNo)) == 0) {
						dao.update("oms.cart.updateOmsCartBySalepriceChange", cart);
					}
				}
			}
		}
		
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	/**
	 * 
	 * @Method Name : saveCart
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 저장
	 *
	 * @param omsCart
	 * @return Map<String, String>
	 * @throws Exception
	 */
	public Map<String, String> saveCart(OmsCart omsCart) throws Exception {
		boolean cart = omsCart.isCartTF();
		String storeId = omsCart.getStoreId();
		String cartId = omsCart.getCartId();
		String cartTypeCd = omsCart.getCartTypeCd();
		String channelId = SessionUtil.getChannelId();
//		String dealId = omsCart.getDealId();
		String productId = omsCart.getProductId();		
		String saleproductId = omsCart.getSaleproductId();
		String newSaleproductId = omsCart.getNewSaleproductId();
		String deviceTypeCd = omsCart.getDeviceTypeCd();



		BigDecimal qty = omsCart.getQty();
		if (CommonUtil.isEmpty(channelId)) {
			channelId = null;
			omsCart.setChannelId("-1");
		}
//		if (CommonUtil.isEmpty(dealId)) {
//			dealId = null;
//			omsCart.setDealId("-1");
//		}

		//product id에 해당하는 cart data를 조회.	
		OmsCartSearch omsCartSearch = new OmsCartSearch();
		omsCartSearch.setMemberNo(omsCart.getMemberNo());
		omsCartSearch.setDeviceTypeCd(omsCart.getDeviceTypeCd());
		omsCartSearch.setProductId(productId);
		omsCartSearch.setCartId(cartId);
		omsCartSearch.setCartTypeCd(cartTypeCd);
		omsCartSearch.setDeviceTypeCd(deviceTypeCd);
		if ("CART_TYPE_CD.PICKUP".equals(cartTypeCd) && !cart) {
			omsCartSearch.setOrgOffshopId(omsCart.getOrgOffshopId());
		}
		List<OmsCart> selOmsCarts = getCartList(omsCartSearch);

		OmsCart selOmsCart = null;	//단품변경시 기존에 존재하는 cart
		OmsCart orgOmsCart = null;	//변경하려는 cart

		if (selOmsCarts != null && selOmsCarts.size() > 0) {

			//해당상품이 맞는지 찾는다.
			int orgIdx = cartSearchIdx(omsCart, selOmsCarts, false);
			if (orgIdx > -1) {
				orgOmsCart = selOmsCarts.get(orgIdx);
				selOmsCarts.remove(orgIdx);
			}

			//상품 1개이상일때는 해당상품과 단품변경시 중복되는 상품을 찾는다.
			if (selOmsCarts.size() > 0) {
				//단품 변경시 중복되는 데이터
				int selIdx = cartSearchIdx(omsCart, selOmsCarts, true);
				if (selIdx > -1) {
					selOmsCart = selOmsCarts.get(selIdx);
				}
			}

		}

		if (orgOmsCart != null) {
			setChangeData(omsCart, orgOmsCart, cart);
			return updateCart(orgOmsCart, selOmsCart);
		} else {
			return insertCart(omsCart);
		}

	}

	/**
	 * 
	 * @Method Name : setChangeData
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 변경data 조회된 data에 setting
	 *
	 * @param omsCart
	 * @param orgCart
	 * @param cart
	 */
	private void setChangeData(OmsCart omsCart, OmsCart orgCart, boolean cart) {

		String cartProdctTypeCd = orgCart.getCartProductTypeCd();

		BigDecimal chgQty = omsCart.getQty();
		if (cart) {	//장바구니에서 수량변경.

		} else {	//장바구니 아닌곳에서 추가시 수량 증가.
			chgQty = orgCart.getQty().add(chgQty);
			BigDecimal maxStockQty = orgCart.getMaxStockQty();
			if (maxStockQty.compareTo(chgQty) < 0) {	//변경수량이 재고를 넘으면 재고만큼만 담음.
				chgQty = maxStockQty;
			}
		}

		orgCart.setQty(chgQty);

		//정기배송변경정보 추가
		orgCart.setDeliveryCnt(omsCart.getDeliveryCnt());
		orgCart.setDeliveryPeriodCd(omsCart.getDeliveryPeriodCd());
		orgCart.setDeliveryPeriodValue(omsCart.getDeliveryPeriodValue());

		//매장픽업변경정보 추가
		orgCart.setOffshopId(omsCart.getOffshopId());

		//new 단품번호 세팅.
		orgCart.setNewSaleproductId(omsCart.getNewSaleproductId());

		if(BaseConstants.CART_PRODUCT_TYPE_CD_SET.equals(cartProdctTypeCd)){
			for(OmsCart subCart : omsCart.getOmsCarts()){
				int idx = 0;
				for(OmsCart subOrgCart : orgCart.getOmsCarts()){
					if(subCart.getSaleproductId().equals(subOrgCart.getSaleproductId())){
						String newSaleproductId = subCart.getNewSaleproductId();
						if (CommonUtil.isNotEmpty(newSaleproductId)) {
							//orgCart.getOmsCarts().get(idx).setNewSaleproductId(newSaleproductId);
							subOrgCart.setNewSaleproductId(newSaleproductId);
						}
						//orgCart.getOmsCarts().get(idx).setQty(chgQty);
						subOrgCart.setQty(chgQty);
					}
					idx++;
				}
			}
		}
	}

	/**
	 * 
	 * @Method Name : cartSearchIdx
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 data check, 중복 data check
	 *
	 * @param omsCart
	 * @param selOmsCarts
	 * @param compareNew
	 * @return
	 */
	private int cartSearchIdx(OmsCart omsCart, List<OmsCart> selOmsCarts, boolean compareNew) {
		String cartProdctTypeCd = selOmsCarts.get(0).getCartProductTypeCd();

		BigDecimal paramQty = omsCart.getQty();

		int matchIdx = -1;
		int idx = 0;
		for (OmsCart selCart : selOmsCarts) {

			BigDecimal chgQty = selCart.getQty().add(paramQty);
			String cartTypeCd = selCart.getCartTypeCd();
			
			boolean deliveryFlag = false;
			BigDecimal selDeliveryCnt = selCart.getDeliveryCnt();
			BigDecimal orgDeliveryCnt = omsCart.getDeliveryCnt();
			String selDeliveryPeriodCd = selCart.getDeliveryPeriodCd();
			String orgDeliveryPeriodCd = omsCart.getDeliveryPeriodCd();
			
			String selOffshopId = selCart.getOffshopId();
			String orgOffshopId = omsCart.getOffshopId();

			if (compareNew) {
				//변경된것 찾을때
				if ("CART_TYPE_CD.REGULARDELIVERY".equals(cartTypeCd)) {
					if (selDeliveryCnt.compareTo(orgDeliveryCnt) == 0 && selDeliveryPeriodCd.equals(orgDeliveryPeriodCd)) {
						deliveryFlag = true;
					}
				} else if ("CART_TYPE_CD.PICKUP".equals(cartTypeCd)) {
					if (selOffshopId.equals(orgOffshopId)) {
						deliveryFlag = true;
					}
				} else {
					deliveryFlag = true;
				}
			} else {
				//변경된것이 아닐때
				deliveryFlag = true;
			}

			if (BaseConstants.CART_PRODUCT_TYPE_CD_GENERAL.equals(cartProdctTypeCd)) {

				String selSaleproductId = selCart.getSaleproductId();
				String orgSaleproductId = omsCart.getSaleproductId();
				String newSaleproductId = omsCart.getNewSaleproductId();
				if (compareNew && CommonUtil.isNotEmpty(newSaleproductId)) {
					orgSaleproductId = newSaleproductId;
				}


				if (selSaleproductId.equals(orgSaleproductId) && deliveryFlag) {
					if (compareNew) {
						//selOmsCarts.get(idx).setQty(chgQty);
						BigDecimal maxStockQty = selCart.getMaxStockQty();
						if (maxStockQty.compareTo(chgQty) < 0) {	//변경수량이 재고를 넘으면 재고만큼만 담음.
							chgQty = maxStockQty;
						}
						selCart.setQty(chgQty);
					}
					matchIdx = idx;
					break;
				}


			} else if (BaseConstants.CART_PRODUCT_TYPE_CD_SET.equals(cartProdctTypeCd)) {

				int matchCnt = 0;
				int subIdx = 0;
				for (OmsCart selSubCart : selCart.getOmsCarts()) {					
					
					String selSaleproductId = selSubCart.getSaleproductId();
					
					for (OmsCart sub : omsCart.getOmsCarts()) {
						String subSId = sub.getSaleproductId();
						String newSId = sub.getNewSaleproductId();
						
						if (compareNew && CommonUtil.isNotEmpty(newSId)) {
							subSId = newSId;
						}

						if (selSaleproductId.equals(subSId) && deliveryFlag) {
							if (compareNew) {
								//selOmsCarts.get(idx).getOmsCarts().get(subIdx).setQty(chgQty);
								sub.setQty(chgQty);
							}
							matchCnt++;
						}

					}
					subIdx++;
				}
				if (matchCnt == selCart.getOmsCarts().size()) {
					if (compareNew) {
						//selOmsCarts.get(idx).setQty(chgQty);
						selCart.setQty(chgQty);
					}
					matchIdx = idx;
					break;
				}

			}
			idx++;

		}
		return matchIdx;
	}

	/**
	 * 
	 * @Method Name : updateCart
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 수정
	 *
	 * @param omsCart
	 * @param selOmsCart
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> updateCart(OmsCart omsCart, OmsCart selOmsCart) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String cartProductTypeCd = omsCart.getCartProductTypeCd();

//		String storeId = omsCart.getStoreId();
//		String productId = omsCart.getProductId();
//		BigDecimal cartProductNo = omsCart.getCartProductNo();
//		String saleproductId = omsCart.getSaleproductId();
//		String cartTypeCd = omsCart.getCartTypeCd();
//		String dealId = omsCart.getDealId();
//		BigDecimal qty = omsCart.getQty();
//		String cartId = omsCart.getCartId();
//		String channelId = omsCart.getChannelId();
//		String newSaleproductId = omsCart.getNewSaleproductId();

		if (selOmsCart != null) {

			//기존꺼 삭제.
			if (BaseConstants.CART_PRODUCT_TYPE_CD_SET.equals(cartProductTypeCd)) {
				for (OmsCart subOmsCart : omsCart.getOmsCarts()) {
					subOmsCart.setCartStateCd(BaseConstants.CART_STATE_CD_DEL);
					dao.update("oms.cart.updateOmsCart", subOmsCart);
				}
				omsCart.setCartStateCd(BaseConstants.CART_STATE_CD_DEL);
				dao.update("oms.cart.updateOmsCart", omsCart);
			} else {
				omsCart.setCartStateCd(BaseConstants.CART_STATE_CD_DEL);
				dao.update("oms.cart.updateOmsCart", omsCart);
			}

			//UPDATE
			omsCart.setCartStateCd(BaseConstants.CART_STATE_CD_REG);

			dao.update("oms.cart.updateOmsCart", selOmsCart);

			if (BaseConstants.CART_PRODUCT_TYPE_CD_SET.equals(cartProductTypeCd)) {
				for (OmsCart subOmsCart : selOmsCart.getOmsCarts()) {
					subOmsCart.setCartStateCd(BaseConstants.CART_STATE_CD_REG);
					dao.update("oms.cart.updateOmsCart", subOmsCart);
				}
			}

		} else {

			dao.update("oms.cart.updateOmsCart", omsCart);
			if (BaseConstants.CART_PRODUCT_TYPE_CD_SET.equals(cartProductTypeCd)) {
				for (OmsCart subOmsCart : omsCart.getOmsCarts()) {
					dao.update("oms.cart.updateOmsCart", subOmsCart);
				}
			}

		}

		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);

		return result;
	}

	/**
	 * 
	 * @Method Name : insertCart
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 장바구니 생성
	 *
	 * @param omsCart
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> insertCart(OmsCart omsCart) throws Exception {


		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);


		String storeId = omsCart.getStoreId();
		String productId = omsCart.getProductId();
		String saleproductId = omsCart.getSaleproductId();
		String cartTypeCd = omsCart.getCartTypeCd();
		String cartProductTypeCd = null;
//		String dealId = omsCart.getDealId();
		BigDecimal qty = omsCart.getQty();
		String cartId = omsCart.getCartId();
		String channelId = SessionUtil.getChannelId();
		String offshopId = omsCart.getOffshopId();
		String deviceTypeCd = omsCart.getDeviceTypeCd();

		BigDecimal deliveryCnt = omsCart.getDeliveryCnt();
		BigDecimal deliveryPeriodValue = new BigDecimal(2);
		String deliveryPeriodCd = omsCart.getDeliveryPeriodCd();

		BigDecimal styleNo = omsCart.getStyleNo();

		if (CommonUtil.isEmpty(deliveryCnt)) {
			deliveryCnt = new BigDecimal(0);
		}
		if (CommonUtil.isEmpty(deliveryPeriodCd)) {
			deliveryPeriodCd = "DELIVERY_PERIOD_CD.2WEEK";
		}

		BigDecimal memberNo = omsCart.getMemberNo();
		List<String> memberTypeCds = new ArrayList();
		memberService.getMemberTypeInfo(memberTypeCds);

		String memGradeCd = FoSessionUtil.getMemGradeCd();	//회원등급

		List<String> dealTypeList = dealService.convertMemberTypeToDealType(memberTypeCds);
		String dealTypeCds = CommonUtil.convertInParam(dealTypeList);	//deal 유형코드

		omsCart.setMemGradeCd(memGradeCd);
		omsCart.setDealTypeCds(dealTypeCds);

		//장바구니 유효기간 정책.
		CcsPolicy ccsPolicy = new CcsPolicy();
		ccsPolicy.setStoreId(storeId);
		ccsPolicy.setPolicyId(BaseConstants.POLICY_ID_CART_END_DT);
		ccsPolicy.setPolicyTypeCd(BaseConstants.POLICY_TYPE_CD_ORDER);
		ccsPolicy = policyService.getPolicy(ccsPolicy);

		BigDecimal endDtValue = new BigDecimal(ccsPolicy.getValue());
		String endDt = DateUtil.getAddDate(DateUtil.FORMAT_1, DateUtil.getCurrentDate(DateUtil.FORMAT_1), endDtValue);

		//상품정보
		PmsProduct pmsProduct = (PmsProduct) dao.selectOne("oms.cart.getCartProductInfo", omsCart);


		//단품정보
		PmsSaleproduct pmsSaleproduct = (PmsSaleproduct) dao.selectOne("oms.cart.getCartSaleproductInfo", omsCart);
		String msg = pmsProduct.getName() + "-" + pmsSaleproduct.getName();

		String productTypeCd = pmsProduct.getProductTypeCd();

		if (BaseConstants.PRODUCT_TYPE_CD_SET.equals(productTypeCd)) {
			cartProductTypeCd = BaseConstants.CART_PRODUCT_TYPE_CD_SET;
		} else if (BaseConstants.PRODUCT_TYPE_CD_GENERAL.equals(productTypeCd)) {
			cartProductTypeCd = BaseConstants.CART_PRODUCT_TYPE_CD_GENERAL;
		} else {
			result.put(BaseConstants.RESULT_MESSAGE, msg + "은 일반상품 or SET상품이 아닙니다.");
			return result;
		}

		//정기배송 가능유무 CHECK
		if (BaseConstants.CART_TYPE_CD_REGULARDELIVERY.equals(cartTypeCd)) {
			if (!BaseConstants.YN_Y.equals(pmsProduct.getRegularDeliveryYn())) {
				result.put(BaseConstants.RESULT_MESSAGE, msg + "은 정기배송이 불가능한 상품입니다.");
				return result;
			}
//			if (qty.compareTo(pmsProduct.getRegularDeliveryMaxQty()) > 0) {
//				result.put(BaseConstants.RESULT_MESSAGE, "정기배송 최대수량을 초과하였습니다.");
//				return result;
//			}
		}
		
		//매장PICKUP CHECK
		String offshopPickupYn = pmsProduct.getOffshopPickupYn();
		if(BaseConstants.CART_TYPE_CD_PICKUP.equals(cartTypeCd)){
			if (!BaseConstants.YN_Y.equals(offshopPickupYn)) {
				result.put(BaseConstants.RESULT_MESSAGE, msg + "은 매장픽업이 불가능한 상품입니다.");
				return result;
			}

			PmsOffshopstock pmsOffshopstock = new PmsOffshopstock();
			pmsOffshopstock.setStoreId(storeId);
			pmsOffshopstock.setSaleproductId(saleproductId);
			pmsOffshopstock.setOffshopId(offshopId);
			//매장재고조회
			pmsOffshopstock = productService.getOffshopstock(pmsOffshopstock);

//			msg = msg + "(" + pmsOffshopstock.getOffshopName() + ")";

			boolean pickup = false;

			if (pmsOffshopstock != null) {
				BigDecimal realStockQty = pmsOffshopstock.getRealStockQty();
				if (realStockQty.compareTo(new BigDecimal(0)) > 0) {
					pickup = true;
				} else {
					result.put(BaseConstants.RESULT_MESSAGE, msg + "은 구성단품이 품절입니다.");
					return result;
				}
			}

			if (!pickup) {
				result.put(BaseConstants.RESULT_MESSAGE, pmsOffshopstock.getName() + "은 픽업이 불가능한 매장입니다.");
				return result;
			}
		} else {

			//		if (!"-1".equals(dealId) && CommonUtil.isNotEmpty(dealId) && !dealId.equals(pmsSaleproduct.getDealId())) {
			//			result.put(BaseConstants.RESULT_MESSAGE, "Deal 적용 단품이없습니다.");
			//			return result;
			//		}
			if (pmsSaleproduct == null) {
				result.put(BaseConstants.RESULT_MESSAGE, msg + "는 구성단품이 없습니다.");
					return result;
			} else {
				if (!BaseConstants.PRODUCT_TYPE_CD_SET.equals(productTypeCd)) {	//set 아닐때만 check
					String saleproductStateCd = pmsSaleproduct.getSaleproductStateCd();
					BigDecimal realStockQty = pmsSaleproduct.getRealStockQty();
					if (!(realStockQty.compareTo(new BigDecimal(0)) > 0)
							|| "SALEPRODUCT_STATE_CD.SOLDOUT".equals(saleproductStateCd)) {
						result.put(BaseConstants.RESULT_MESSAGE, msg + "은 구성단품이 품절입니다.");
						return result;
					}
					if ("SALEPRODUCT_STATE_CD.STOP".equals(saleproductStateCd)) {
						result.put(BaseConstants.RESULT_MESSAGE, "구성단품이 판매중지 되었습니다.");
						return result;
					}

				}
				}
		}


		//CART 상품
		OmsCart insertOmsCart1 = new OmsCart();
		insertOmsCart1.setStoreId(storeId);
		insertOmsCart1.setUpperCartProductNo(null);
		insertOmsCart1.setSetQty(qty);
		insertOmsCart1.setCartTypeCd(cartTypeCd);
		insertOmsCart1.setCartProductTypeCd(cartProductTypeCd);
		insertOmsCart1.setCartId(cartId);
		if ("-1".equals(channelId)) {
			channelId = null;
		}
//		if ("-1".equals(dealId)) {
//			dealId = null;
//		}
		insertOmsCart1.setChannelId(channelId);
		insertOmsCart1.setProductId(productId);
		insertOmsCart1.setSaleproductId(pmsSaleproduct.getSaleproductId());

//		BigDecimal totalSalePrice = pmsProduct.getSalePrice().add(pmsSaleproduct.getAddSalePrice());

		BigDecimal salePrice = omsCart.getSalePrice();
		BigDecimal addSalePrice = omsCart.getAddSalePrice();
		BigDecimal totalSalePrice = salePrice.add(addSalePrice);

		if (BaseConstants.CART_TYPE_CD_REGULARDELIVERY.equals(cartTypeCd)) {
			totalSalePrice = pmsProduct.getRegularDeliveryPrice();
			insertOmsCart1.setDeliveryCnt(deliveryCnt);
			insertOmsCart1.setDeliveryPeriodCd(deliveryPeriodCd);
			insertOmsCart1.setDeliveryPeriodValue(deliveryPeriodValue);
		}
		insertOmsCart1.setRegularDeliveryPrice(pmsProduct.getRegularDeliveryPrice());
		insertOmsCart1.setTotalSalePrice(totalSalePrice);

//		PmsOptimalprice price = new PmsOptimalprice();
//		price.setMemberNo(memberNo);
//		price.setProductId(productId);
//		price.setSaleproductId(saleproductId);
//		price.setSalePrice(pmsProduct.getSalePrice());
//		price.setAddSalePrice(pmsSaleproduct.getAddSalePrice());
//		price.setTargetAmt(totalSalePrice);
//		price.setStoreId(storeId);
//		price.setChannelId(channelId);
//		price.setDeviceTypeCd(deviceTypeCd);
//		price.setMemGradeCd(memGradeCd);
//		price.setMemberTypeCds(memberTypeCds);
//		price.setCommissionRate(pmsProduct.getCommissionRate());
//		price = productService.optimalPrice(price);

		String dealId = omsCart.getDealId();
		String couponId = omsCart.getCouponId();
//
//		insertOmsCart1.setCouponId(price.getCouponId());
//		insertOmsCart1.setSalePrice(price.getSalePrice());
//		insertOmsCart1.setAddSalePrice(price.getAddSalePrice());
//		insertOmsCart1.setTotalSalePrice(price.getTotalSalePrice().subtract(price.getCouponDcAmt()));
//		insertOmsCart1.setDealId(price.getDealId());

		insertOmsCart1.setCouponId(couponId);
		insertOmsCart1.setDealId(dealId);


		insertOmsCart1.setSalePrice(salePrice);
		insertOmsCart1.setAddSalePrice(addSalePrice);
		insertOmsCart1.setTotalSalePrice(totalSalePrice);

		insertOmsCart1.setQty(qty);
		insertOmsCart1.setCartStateCd(BaseConstants.CART_STATE_CD_REG);
		insertOmsCart1.setEndDt(endDt);

		insertOmsCart1.setKeepYn(BaseConstants.YN_N);
		insertOmsCart1.setOffshopId(offshopId);
//		insertOmsCart1.setCouponId(couponId);

		insertOmsCart1.setStyleNo(styleNo);

//		dao.insert("oms.cart.insertOmsCart", insertOmsCart1);
		dao.insertOneTable(insertOmsCart1);
		BigDecimal upperCartProductNo = insertOmsCart1.getCartProductNo();

		//Set상품일때
		if (BaseConstants.PRODUCT_TYPE_CD_SET.equals(productTypeCd)) {	//SET
			
			for (PmsSetproduct pmsSetproduct : pmsProduct.getPmsSetproducts()) {
				
				String subProductId = pmsSetproduct.getSubProductId();
				String subSaleproductId = null;
				for (OmsCart subCart : omsCart.getOmsCarts()) {
					if (subCart.getProductId().equals(subProductId)) {
						subSaleproductId = subCart.getSaleproductId();	//선택된 set 단품ID		
					}
				}


				//set 구성상품 정보
				PmsProduct subPmsProduct = new PmsProduct();
				subPmsProduct.setStoreId(storeId);
				subPmsProduct.setProductId(subProductId);				
				subPmsProduct = (PmsProduct)dao.selectOneTable(subPmsProduct);
				
				//set 구성단품 정보
				PmsSaleproduct pmsSetSaleproduct = new PmsSaleproduct();
				pmsSetSaleproduct.setProductId(subProductId);
				pmsSetSaleproduct.setSaleproductId(subSaleproductId);
				pmsSetSaleproduct.setDealId(dealId);
				pmsSetSaleproduct = (PmsSaleproduct) dao.selectOne("oms.cart.getCartSaleproductInfo", pmsSetSaleproduct);

				if (pmsSetSaleproduct == null) {
					throw new ServiceException("SET 구성단품이 없습니다.");
				}
//				if (!"-1".equals(dealId) && CommonUtil.isNotEmpty(dealId) && !dealId.equals(pmsSetSaleproduct.getDealId())) {
//					throw new ServiceException("Deal 적용 단품이 없습니다.");
//				}

				OmsCart insertOmsCart2 = new OmsCart();
				insertOmsCart2.setStoreId(storeId);
				insertOmsCart2.setUpperCartProductNo(upperCartProductNo);
				insertOmsCart2.setSetQty(pmsSetproduct.getQty());
				insertOmsCart2.setCartTypeCd(cartTypeCd);
				insertOmsCart2.setCartProductTypeCd(BaseConstants.CART_PRODUCT_TYPE_CD_SUB);
				insertOmsCart2.setCartId(cartId);
				insertOmsCart2.setChannelId(channelId);
				insertOmsCart2.setProductId(pmsSetSaleproduct.getProductId());
				insertOmsCart2.setSaleproductId(pmsSetSaleproduct.getSaleproductId());
				insertOmsCart2.setDealId(dealId);
				insertOmsCart2.setSalePrice(pmsSetproduct.getPmsProduct().getSalePrice());
				insertOmsCart2.setAddSalePrice(pmsSetSaleproduct.getAddSalePrice());
				insertOmsCart2
						.setTotalSalePrice(pmsSetproduct.getPmsProduct().getSalePrice().add(pmsSetSaleproduct.getAddSalePrice()));
				//TODO 값설정해야함.
				insertOmsCart2.setRegularDeliveryPrice(pmsSetproduct.getPmsProduct().getRegularDeliveryPrice());
				insertOmsCart2.setQty(qty);
				insertOmsCart2.setCartStateCd(BaseConstants.CART_STATE_CD_REG);
				insertOmsCart2.setEndDt(endDt);
				
				insertOmsCart2.setKeepYn(BaseConstants.YN_N);
				insertOmsCart2.setOffshopId(offshopId);
				insertOmsCart2.setCouponId(couponId);

				insertOmsCart2.setStyleNo(styleNo);

//				BigDecimal subSetCartProductNo = new BigDecimal((Integer) dao.insert("oms.cart.insertOmsCart", insertOmsCart2));
				dao.insertOneTable(insertOmsCart2);
			}

		}

		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;

	}
	
	/**
	 * 
	 * @Method Name : keepCartOmsCart
	 * @author : dennis
	 * @date : 2016. 7. 5.
	 * @description : 카트보관
	 *
	 * @param omsCart
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> saveKeep(OmsCart omsCart) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String endDt = DateUtil.getAddDate(DateUtil.FORMAT_2, DateUtil.getCurrentDate(DateUtil.FORMAT_2), new BigDecimal(30));
		if (BaseConstants.YN_Y.equals(omsCart.getKeepYn())) {
			omsCart.setEndDt("99991231");
		} else {
			omsCart.setEndDt(endDt);
		}
		dao.updateOneTable(omsCart);
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}
	
	/**
	 * 
	 * @Method Name : saveCartTransfer
	 * @author : dennis
	 * @date : 2016. 8. 29.
	 * @description : 장바구니 유형변경 (택배로받기)
	 *
	 * @param omsCart
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> saveCartTransfer(OmsCart omsCart) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

//		String endDt = DateUtil.getAddDate(DateUtil.FORMAT_2, DateUtil.getCurrentDate(DateUtil.FORMAT_2), new BigDecimal(30));

		OmsCart chgCart = new OmsCart();
		chgCart.setStoreId(omsCart.getStoreId());
		chgCart.setCartProductNo(omsCart.getCartProductNo());
		chgCart.setCartStateCd("CART_STATE_CD.DEL");
//		chgCart.setEndDt(endDt);
//		chgCart.setOffshopId("");
		dao.updateOneTable(chgCart);

		omsCart.setCartTF(false);
		omsCart.setCartProductNo(null);
		omsCart.setCartTypeCd("CART_TYPE_CD.GENERAL");
		omsCart.setOffshopId("");
		saveCart(omsCart);

		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
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
	 * @param storeId
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> deleteCart(List<OmsCart> omsCartList, String storeId, String cartId) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		for (OmsCart omsCart : omsCartList) {
			OmsCart delCart = new OmsCart();
			delCart.setCartProductNo(omsCart.getCartProductNo());
			delCart.setStoreId(storeId);
			delCart.setCartId(cartId);
			delCart.setCartStateCd("CART_STATE_CD.DEL");
			dao.update("oms.cart.updateUpperCart", delCart);
			dao.updateOneTable(delCart);
		}
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}


	/**
	 * 
	 * @Method Name : mergeCartData
	 * @author : dennis
	 * @date : 2016. 8. 4.
	 * @description : 비회원 장바구니 merge
	 *
	 * @param nonMemberLoginInfo
	 * @param sessionLoginInfo
	 * @throws Exception
	 */
	public Map<String, String> mergeCartData(FoLoginInfo nonMemberLoginInfo, FoLoginInfo sessionLoginInfo,
			String deviceTypeCd) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		OmsCartSearch omsCartSearch = new OmsCartSearch();
		CcsUtil.setSessionLoginInfo(omsCartSearch);
		omsCartSearch.setCartId(nonMemberLoginInfo.getLoginId());
		omsCartSearch.setCartTypeCd("CART_TYPE_CD.GENERAL");
		omsCartSearch.setDeviceTypeCd(deviceTypeCd);
		List<OmsCart> nonMemberCartList = this.getCartList(omsCartSearch);

		for (OmsCart omsCart : nonMemberCartList) {
			if(CommonUtil.isNotEmpty(sessionLoginInfo)){
				omsCart.setCartId(sessionLoginInfo.getLoginId());
				result = this.saveCart(omsCart);
			}
		}

		//비회원장바구니 삭제
		result = this.deleteCart(nonMemberCartList, nonMemberLoginInfo.getStoreId(), nonMemberLoginInfo.getLoginId());

		return result;
	}

	/**
	 * 
	 * @Method Name : deleteCart
	 * @author : dennis
	 * @date : 2016. 10. 7.
	 * @description : 장바구니 삭제
	 *
	 * @param cartProductNos
	 */
	public void deleteCartOrder(String cartProductNos, String storeId) {
		if (CommonUtil.isNotEmpty(cartProductNos)) {
			String[] arrcartProductNos = cartProductNos.split(",");
			for (String cartProductNo : arrcartProductNos) {
				OmsCart omsCart = new OmsCart();
				omsCart.setStoreId(storeId);
				omsCart.setCartProductNo(new BigDecimal(cartProductNo));
				dao.update("oms.cart.updateCartDel", omsCart);
			}
		}
	}
	
	public List<OmsCart> getCartListByMyPage(OmsCartSearch omsCartSearch) throws Exception {
		BigDecimal memberNo = omsCartSearch.getMemberNo();
		String deviceTypeCd = omsCartSearch.getDeviceTypeCd();

		List<String> memberTypeCds = new ArrayList();
		memberService.getMemberTypeInfo(memberTypeCds);

		String memGradeCd = (String) FoSessionUtil.getMemGradeCd();	//회원등급

		List<String> dealTypeList = dealService.convertMemberTypeToDealType(memberTypeCds);
		String dealTypeCds = CommonUtil.convertInParam(dealTypeList);	//deal 유형코드

		omsCartSearch.setMemGradeCd(memGradeCd);
		omsCartSearch.setDealTypeCds(dealTypeCds);
		omsCartSearch.setChannelId(SessionUtil.getChannelId());	//제휴채널로 들어왔을때.

		List<OmsCart> cartList = (List<OmsCart>) dao.selectList("oms.cart.getCartList", omsCartSearch);
		if ("CART_TYPE_CD.GENERAL".equals(omsCartSearch.getCartTypeCd())) {
			int cnt = 1;
			for (OmsCart cart : cartList) {
				PmsOptimalprice price = new PmsOptimalprice();
				price.setMemberNo(memberNo);
				price.setProductId(cart.getProductId());
				price.setSaleproductId(cart.getSaleproductId());
				price.setSalePrice(cart.getSalePrice());
				price.setAddSalePrice(cart.getAddSalePrice());
				price.setTargetAmt(cart.getOrgTotalSalePrice());
				price.setStoreId(cart.getStoreId());
				price.setChannelId(cart.getChannelId());
				price.setDeviceTypeCd(deviceTypeCd);
				price.setMemGradeCd(memGradeCd);
				price.setMemberTypeCds(memberTypeCds);
				price.setCommissionRate(cart.getCommissionRate());
				PmsOptimalprice opt = priceService.optimalPrice(price, cart);
//				List<PmsOptimalprice> priceList = productService.optimalPriceList(price);
//
//				cart.setOptimalprices(priceList);
				cart.setOptimalprice(opt);

				//상품의 배송비무료여부가 N일때 딜걸려있으면 딜배송비무료여부 세팅.
				if ("N".equals(cart.getPolicyDeliveryFeeFreeYn()) && CommonUtil.isNotEmpty(opt.getDealId())) {
					cart.setPolicyDeliveryFeeFreeYn(opt.getDeliveryFeeFreeYn());
				}

			}

			//장바구니에서는 상품간 check필요없음.
			//calcOptimalprice(cartList);
		}

		return cartList;
	}
	
}