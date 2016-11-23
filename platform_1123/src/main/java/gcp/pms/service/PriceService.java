package gcp.pms.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.common.util.FoSessionUtil;
import gcp.pms.model.custom.PmsOptimalprice;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.search.SpsCouponSearch;
import gcp.sps.model.search.SpsDealSearch;
import gcp.sps.service.CouponService;
import gcp.sps.service.DealService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service("priceService")
public class PriceService extends BaseService {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DealService		dealService;

	@Autowired
	private CouponService	couponService;

	public List<PmsOptimalprice> optimalPriceList(PmsOptimalprice optimalprice) throws Exception {
		List<PmsOptimalprice> resultList = new ArrayList<PmsOptimalprice>();

		////parameter
		logger.debug("=========== 최적가 parameter =============");
		logger.debug(optimalprice.toString());
		logger.debug("=========== 최적가 parameter =============");
		BigDecimal memberNo = optimalprice.getMemberNo();
		String productId = optimalprice.getProductId();
		BigDecimal targetAmt = optimalprice.getTargetAmt();
		String storeId = optimalprice.getStoreId();
		String channelId = optimalprice.getChannelId();
		String deviceTypeCd = optimalprice.getDeviceTypeCd();
		String memGradeCd = optimalprice.getMemGradeCd();
		List<String> memberTypeCds = optimalprice.getMemberTypeCds();
		BigDecimal listPrice = optimalprice.getListPrice();
		BigDecimal salePrice = optimalprice.getSalePrice();
		BigDecimal addSalePrice = optimalprice.getAddSalePrice();
		BigDecimal commissionRate = optimalprice.getCommissionRate();
		String controlType = optimalprice.getControlType();
		////parameter		

		//순번
		BigDecimal seq = new BigDecimal(1);

		//딜조회
		SpsDealSearch spsDealSearch = new SpsDealSearch();
		spsDealSearch.setProductId(productId);
		spsDealSearch.setMemberNo(memberNo);
		List<SpsDeal> dealList = dealService.getApplyDealList(spsDealSearch);

		//쿠폰조회
		SpsCouponSearch spsCouponSearch = new SpsCouponSearch();
		spsCouponSearch.setStoreId(storeId);
		spsCouponSearch.setChannelId(channelId);
		spsCouponSearch.setMemberNo(memberNo);
		spsCouponSearch.setDeviceTypeCd(deviceTypeCd);
		spsCouponSearch.setMemberTypeCds(memberTypeCds);
		spsCouponSearch.setMemGradeCd(memGradeCd);
		spsCouponSearch.setProductId(productId);
		spsCouponSearch.setTargetAmt(targetAmt);
		spsCouponSearch.setCouponTypeCd("COUPON_TYPE_CD.PRODUCT");
		spsCouponSearch.setDownShowYn("Y");
		spsCouponSearch.setCommissionRate(commissionRate);
		spsCouponSearch.setControlType(controlType);
		List<SpsCoupon> couponList = couponService.getApplyCouponList(spsCouponSearch);

		//최적가 대상쿠폰 정보담음.
		for (SpsCoupon coupon : couponList) {
			PmsOptimalprice result = new PmsOptimalprice();
			result.setCouponId(coupon.getCouponId());
			result.setDealId(coupon.getDealId());
			if (CommonUtil.isNotEmpty(coupon.getDealId())) { //딜 걸린 쿠폰
				result.setListPrice(coupon.getDealListPrice());
				result.setSalePrice(coupon.getDealSalePrice());
				result.setAddSalePrice(coupon.getDealAddSalePrice());
				result.setTotalSalePrice(coupon.getTargetAmt());
				result.setDeliveryFeeFreeYn(coupon.getDeliveryFeeFreeYn());
				result.setPointSaveRate(coupon.getPointSaveRate());
				result.setDealTypeCd(coupon.getDealTypeCd());	//딜유형코드.
				result.setDealStockQty(coupon.getDealStockQty());	//딜수량
			} else {
				result.setListPrice(listPrice);
				result.setSalePrice(salePrice);
				result.setAddSalePrice(addSalePrice);
				result.setTotalSalePrice(targetAmt);
			}

			result.setDcApplyTypeCd(coupon.getDcApplyTypeCd());
			result.setDcValue(coupon.getDcValue());
			result.setCouponDcAmt(coupon.getApplyDcAmt());
			result.setSeq(seq);
			seq = seq.add(new BigDecimal(1));
			resultList.add(result);
		}

		//최적가 대상딜 정보담음.		
		for (SpsDeal deal : dealList) {
			PmsOptimalprice result = new PmsOptimalprice();
			result.setCouponId(null);
			result.setDealId(deal.getDealId());
			result.setDeliveryFeeFreeYn(deal.getSpsDealproducts().get(0).getDeliveryFeeFreeYn());
			result.setPointSaveRate(deal.getSpsDealproducts().get(0).getPointSaveRate());
			result.setListPrice(deal.getListPrice());
			result.setSalePrice(deal.getSalePrice());
			result.setAddSalePrice(deal.getAddSalePrice());
			result.setTotalSalePrice(deal.getTotalSalePrice());
			result.setCouponDcAmt(new BigDecimal(0));
			result.setSeq(seq);
			result.setDealTypeCd(deal.getDealTypeCd());	//딜유형코드.
			result.setDealStockQty(deal.getSpsDealproducts().get(0).getDealStockQty());	//딜수량
			seq = seq.add(new BigDecimal(1));
			resultList.add(result);
		}

		//일반가담음.
		PmsOptimalprice result1 = new PmsOptimalprice();
		result1.setCouponId(null);
		result1.setDealId(null);
		result1.setListPrice(listPrice);
		result1.setSalePrice(salePrice);
		result1.setAddSalePrice(addSalePrice);
		result1.setTotalSalePrice(targetAmt);
		result1.setCouponDcAmt(new BigDecimal(0));
		result1.setSeq(seq);
		seq = seq.add(new BigDecimal(1));
		resultList.add(result1);

		return resultList;
	}

	private PmsOptimalprice checkPrice(List<PmsOptimalprice> priceList) {
		PmsOptimalprice result = null;
		logger.debug("=========== 가격비교 시작 =================");
		/**
		 * 가격이 동일할 경우 처리룰 공유합니다.
		 * 
		 * 프리미엄멤버십 | 다자녀 | B2E > 쇼킹제로 > 멤버십 > 임직원 > 일반
		 * 
		 * [설명] - 프리미엄멤버십가, 다자녀가, B2E가는 가격이 낮건 높건 무조건 우선 적용 - 쇼킹제로가, 멤버십가, 임직원가, 일반가가 동일 할 경우 '쇼킹제로
		 * > 멤버십 > 임직원 > 일반' 우선순위로 적용
		 */

		for (PmsOptimalprice price : priceList) {
			logger.debug("\n====================================================================");
			String dealTypeCd = price.getDealTypeCd();
			if (result == null) {
				if ("DEAL_TYPE_CD.CHILDREN".equals(dealTypeCd)) {
					logger.debug("최적가가 프리미엄,다자녀 아니고 현재가 다자녀이면 setting");
					if (price.getDealId().equals(FoSessionUtil.getChildrenDealId())) {
						result = price;
					}
				} else {
					result = price;
				}
			} else {
				String optimalDealTypeCd = result.getDealTypeCd();

				logger.debug("최적가 dealTypeCd : " + optimalDealTypeCd);
				logger.debug("현재 dealTypeCd : " + dealTypeCd);

				if (!"DEAL_TYPE_CD.PREMIUM".equals(optimalDealTypeCd) && "DEAL_TYPE_CD.PREMIUM".equals(dealTypeCd)) {
					logger.debug("최적가가 프리미엄 아니고 현재가 프리미엄이면 setting");
					result = price;
				} else if (!"DEAL_TYPE_CD.PREMIUM".equals(optimalDealTypeCd) && "DEAL_TYPE_CD.CHILDREN".equals(dealTypeCd)) {
					logger.debug("최적가가 프리미엄 아니고 현재가 다자녀이면 setting");
					if (price.getDealId().equals(FoSessionUtil.getChildrenDealId())) {
						result = price;
					}
				} else if (!"DEAL_TYPE_CD.PREMIUM".equals(optimalDealTypeCd) && !"DEAL_TYPE_CD.CHILDREN".equals(optimalDealTypeCd)
						&& "DEAL_TYPE_CD.B2E".equals(dealTypeCd)) {
					logger.debug("최적가가 프리미엄,다자녀가가 아니고 현재가 B2E이면 setting");
					result = price;
				} else {
					logger.debug("최적가가 프리미엄이고 현재도 프리미엄이면 비교.");
					logger.debug("최적가가 다자녀이고 현재도 다자녀이면 비교.");
					logger.debug("최적가가 B2E이고 현재도 B2E이면 비교.");
					logger.debug("최적가가 프리미엄, 다자녀, B2E도 아니면 비교.");

					BigDecimal totalSalePrice = result.getTotalSalePrice();
					BigDecimal couponDcAmt = result.getCouponDcAmt();
					BigDecimal comPrice = totalSalePrice.subtract(couponDcAmt);
					logger.debug("optimal dealId : " + result.getDealId());
					logger.debug("optimal couponId : " + result.getCouponId());
					logger.debug("optimal totalSalePrice : " + totalSalePrice);
					logger.debug("optimal couponDcAmt : " + couponDcAmt);
					logger.debug("optimal comPrice : " + comPrice);

					BigDecimal priceTotalSalePrice = price.getTotalSalePrice();
					BigDecimal priceCouponDcAmt = price.getCouponDcAmt();
					BigDecimal comPrice2 = priceTotalSalePrice.subtract(priceCouponDcAmt);
					logger.debug("price dealId : " + price.getDealId());
					logger.debug("price couponId : " + price.getCouponId());
					logger.debug("price totalSalePrice : " + priceTotalSalePrice);
					logger.debug("price couponDcAmt : " + priceCouponDcAmt);
					logger.debug("price comPrice2 : " + comPrice2);

					if (comPrice.compareTo(comPrice2) > 0) {
						result = price;
					}
				}
			}
		}
		logger.debug("=========== 최저가 =================");
		logger.debug(result.toString());
		logger.debug("=========== 최저가 =================");
		logger.debug("=========== 가격비교 끝 =================");
		return result;
	}

	public PmsOptimalprice optimalPrice(PmsOptimalprice optimalprice) throws Exception {
		List<PmsOptimalprice> priceList = optimalPriceList(optimalprice);
		String productId = optimalprice.getProductId();
		String saleproductId = optimalprice.getSaleproductId();
		PmsOptimalprice optPrice = checkPrice(priceList);


		//단품정보있을때. deal addSaleprice, coupon 다시 계산.
		if (CommonUtil.isNotEmpty(saleproductId)) {
			optPrice.setProductId(productId);
			optPrice.setSaleproductId(saleproductId);
			optimalSaleprice(optPrice);
		}

		return optPrice;
	}

	/**
	 * 
	 * @Method Name : optimalSaleprice
	 * @author : dennis
	 * @date : 2016. 10. 18.
	 * @description : 단품 추가판매가 및 쿠폰 계산금액 적용.
	 *
	 * @param optPrice
	 * 
	 *            dealId couponId productId saleproductId memberNo
	 * 
	 * 
	 * @throws Exception
	 */
	public void optimalSaleprice(PmsOptimalprice optPrice) throws Exception {
		String dealId = optPrice.getDealId();
		String couponId = optPrice.getCouponId();

		String productId = optPrice.getProductId();
		String saleproductId = optPrice.getSaleproductId();
		BigDecimal memberNo = optPrice.getMemberNo();

		if (CommonUtil.isEmpty(optPrice.getCouponDcAmt())) {
			optPrice.setCouponDcAmt(new BigDecimal(0));
		}

		if (CommonUtil.isNotEmpty(dealId)) {
			//딜조회
			SpsDealSearch spsDealSearch = new SpsDealSearch();
			spsDealSearch.setProductId(productId);
			spsDealSearch.setSaleproductId(saleproductId);
			spsDealSearch.setMemberNo(memberNo);
			spsDealSearch.setDealId(dealId);
			SpsDeal deal = dealService.getApplyDealOne(spsDealSearch);

			if (deal != null) {
				optPrice.setDealId(deal.getDealId());
				optPrice.setDeliveryFeeFreeYn(deal.getSpsDealproducts().get(0).getDeliveryFeeFreeYn());
				optPrice.setPointSaveRate(deal.getSpsDealproducts().get(0).getPointSaveRate());
				optPrice.setListPrice(deal.getListPrice());
				optPrice.setSalePrice(deal.getSalePrice());
				optPrice.setAddSalePrice(deal.getAddSalePrice());
				optPrice.setTotalSalePrice(deal.getTotalSalePrice());
				optPrice.setDealTypeCd(deal.getDealTypeCd());	//딜유형코드.
			}

		}

		if (CommonUtil.isNotEmpty(couponId)) {
			SpsCoupon coupon = new SpsCoupon();
			coupon.setCouponId(couponId);
			coupon.setStoreId(SessionUtil.getStoreId());

			coupon = (SpsCoupon) dao.selectOneTable(coupon);

			if (coupon != null) {
				coupon.setTargetAmt(optPrice.getTotalSalePrice());
				couponService.calcCoupnDcAmt(coupon);

				optPrice.setCouponId(couponId);
				optPrice.setSingleApplyYn(coupon.getSingleApplyYn());
				optPrice.setCouponDcAmt(coupon.getApplyDcAmt());
			}
		}

		logger.debug("=========== 단품 적용가 계산 =================");
		logger.debug("deal id : " + dealId);
		logger.debug("coupon id : " + couponId);
		logger.debug(optPrice.toString());
		logger.debug("=========== 단품 적용가 계산 =================");
	}

	/**
	 * 
	 * @Method Name : optimalPrice
	 * @author : dennis
	 * @date : 2016. 10. 18.
	 * @description : 최적가격 계산해서 object에 setting
	 *
	 * @param optimalprice
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public PmsOptimalprice optimalPrice(PmsOptimalprice optimalprice, Object obj) throws Exception {
		String productId = optimalprice.getProductId();
		String saleproductId = optimalprice.getSaleproductId();
		PmsOptimalprice optPrice = optimalPrice(optimalprice);

		//단품정보있을때. deal addSaleprice, coupon 다시 계산.
		if (CommonUtil.isNotEmpty(saleproductId)) {
			optPrice.setProductId(productId);
			optPrice.setSaleproductId(saleproductId);
			optimalSaleprice(optPrice);
		}
		setOptimalField(obj, optPrice);
		return optPrice;
	}

	public void setOptimalField(Object obj, PmsOptimalprice result) {
		if (obj != null) {
			Class cls = obj.getClass();
			try {
				setPriceField(cls, obj, result);
				while (cls.getSuperclass() != null) {
					cls = cls.getSuperclass();
					setPriceField(cls, obj, result);
				}
			} catch (Exception e) {
				//logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void setPriceField(Class cls, Object obj, PmsOptimalprice result) throws Exception {
		BigDecimal salePrice = result.getSalePrice();
		BigDecimal addSalePrice = result.getAddSalePrice();
		BigDecimal totalSalePrice = result.getTotalSalePrice();
		BigDecimal couponDcAmt = result.getCouponDcAmt();
		String dealId = result.getDealId();
		String couponId = result.getCouponId();
		String singleApplyYn = result.getSingleApplyYn();

		Field[] fields = cls.getDeclaredFields();
//		logger.debug("class name : " + cls.getName());
		for (Field field : fields) {
			field.setAccessible(true);

//			logger.debug("field : " + field.getName());

			try {
				if ("salePrice".equals(field.getName())) {
					field.set(obj, salePrice);
				} else if ("addSalePrice".equals(field.getName())) {
					field.set(obj, addSalePrice);
//					logger.debug("addSalePrice : " + addSalePrice);
				} else if ("totalSalePrice".equals(field.getName())) {
					field.set(obj, totalSalePrice);
//					logger.debug("totalSalePrice : " + totalSalePrice);
				} else if ("dealId".equals(field.getName())) {
					field.set(obj, dealId);
//					logger.debug("dealId : " + dealId);
				} else if ("couponId".equals(field.getName())) {
					field.set(obj, couponId);
//					logger.debug("couponId : " + couponId);
				} else if ("couponDcAmt".equals(field.getName())) {
					field.set(obj, couponDcAmt);
//					logger.debug("couponDcAmt : " + couponDcAmt);
				} else if ("singleApplyYn".equals(field.getName())) {
					field.set(obj, singleApplyYn);
//					logger.debug("singleApplyYn : " + singleApplyYn);
				}
			} catch (UsernameNotFoundException u) {
				//logger.error(u.getMessage(), u);
			}
		}
	}
}
