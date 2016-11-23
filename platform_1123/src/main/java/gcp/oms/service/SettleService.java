package gcp.oms.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsChannel;
import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsSettle;
import gcp.oms.model.search.OmsSettleSearch;
import intune.gsf.common.utils.CommonUtil;

@Service
public class SettleService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : getAllChannelList
	 * @author : peter
	 * @date : 2016. 10. 28.
	 * @description : 전체 채널정보 조회
	 *
	 * @param storeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsChannel> getAllChannelList(String storeId) {
		return (List<CcsChannel>) dao.selectList("oms.settle.getAllChannelList", storeId);
	}

	/**
	 * @Method Name : getSettleList
	 * @author : peter
	 * @date : 2016. 10. 12.
	 * @description : 매출원장 조회
	 *
	 * @param oss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsSettle> getSettleList(OmsSettleSearch oss) {
		//주문구분 설정
		String orderType = oss.getOrderType();
		if ("ALL".equals(orderType)) { //전체
			oss.setOrderType("");
			oss.setDealTypeCd("");
		} else if ("GENERAL".equals(orderType)) { //일반가
			oss.setOrderType("GNR");
			oss.setDealTypeCd("GNR");
		} else if ("B2B".equals(orderType)) { //B2B가
			oss.setOrderType("B2B");
			oss.setDealTypeCd("B2B");
		} else { //그 외 딜가격
			oss.setDealTypeCd("DEAL_TYPE_CD." + orderType);
			if ("B2E".equals(orderType)) {
				oss.setOrderType("B2E");
			} else {
				oss.setOrderType("DEAL");
			}
		}
		logger.debug("orderType: " + oss.getOrderType() + ", dealType: " + oss.getDealTypeCd());

		//정산유형 설정
		String settleType = oss.getSettleType();
		if (null != settleType && !"".equals(settleType)) {
			String[] types = settleType.split(",");
			int ruleValue = 0;
			for (int i = 0, size = types.length; i < size; i++) {
				ruleValue += Integer.parseInt(types[i]);
			}
			logger.debug("value: " + ruleValue);
			if (ruleValue == 7) { //전체 체크인 경우
				oss.setSettleTypeValue("");
			} else {
				oss.setSettleTypeValue(String.valueOf(ruleValue));
			}
		} else { //아무것도 체크 안한 경우
			oss.setSettleTypeValue("");
		}
		logger.debug("settleType: " + oss.getSettleType() + ", settleTypeValue: " + oss.getSettleTypeValue());

		List<OmsSettle> settleList = (List<OmsSettle>) dao.selectList("oms.settle.getSettleList", oss);
		for (OmsSettle stlOne : settleList) {
			//고객결제금액=판매금액-매일포인트-예치금사용액-자사쿠폰금액-업체쿠폰금액-플러스쿠폰금액-장바구니쿠폰금액
			int paymentAmt = stlOne.getSaleAmt().intValue() - stlOne.getMaeilPoint().intValue()
					- stlOne.getDepositAmt().intValue() - stlOne.getOwnCouponAmt().intValue()
					- stlOne.getBizCouponAmt().intValue() - stlOne.getPlusCouponAmt().intValue()
					- stlOne.getBasketCouponAmt().intValue();
			stlOne.setPaymentAmt(new BigDecimal(paymentAmt));

			//위탁 회계매출(위탁)=판매금액*수수료율
			if ("SALE_TYPE_CD.CONSIGN".equals(stlOne.getSaleTypeCd()) && "N".equals(stlOne.getPurchaseYn())) {
				long consignSales = Math
						.round(stlOne.getSaleAmt().doubleValue() * (stlOne.getCommissionRate().doubleValue() / 100.0));
//				logger.debug(
//						"a: " + Math.round(stlOne.getSaleAmt().doubleValue() * (stlOne.getCommissionRate().doubleValue() / 100.0))
//								+ ", b: " + consignSales);
				stlOne.setConsignSales(new BigDecimal(consignSales));
				//수수료율: 위탁인 경우만 노출(% 포함)
//				stlOne.setStrCommissionRate(stlOne.getCommissionRate() + "%");
				//수수료율: 위탁인 경우만 노출(% 제거)
				if (CommonUtil.isNotEmpty(stlOne.getCommissionRate())) {
					stlOne.setStrCommissionRate(stlOne.getCommissionRate().toString());
				}
			}
			//사입 회계매출(사입 & 위탁사입)=판매금액-자사쿠폰금액
			else {
				int purchaseSales = stlOne.getSaleAmt().intValue() - stlOne.getOwnCouponAmt().intValue();
				stlOne.setPurchaseSales(new BigDecimal(purchaseSales));
				//수수료율: 위탁이 아닌 경우는 공백
				stlOne.setStrCommissionRate("");
			}
		}

		return settleList;
	}
}
