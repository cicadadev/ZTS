package gcp.oms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsSettle;
import gcp.oms.model.OmsSettleBiz;
import gcp.oms.model.search.OmsSettleSearch;

@Service
public class SettleBizService extends BaseService {
	/**
	 * @Method Name : getSettleBizList
	 * @author : peter
	 * @date : 2016. 10. 18.
	 * @description : 정산내역(위탁) 조회
	 *
	 * @param oss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsSettleBiz> getSettleBizList(OmsSettleSearch oss) {
		String saleDate = oss.getSaleYear() + "-" + String.format("%02d", Integer.parseInt(oss.getSaleMonth()));
		oss.setSaleDate(saleDate);
		return (List<OmsSettleBiz>) dao.selectList("oms.settlebiz.getSettleBizList", oss);
	}

	/**
	 * @Method Name : getSettleBizDetailList
	 * @author : peter
	 * @date : 2016. 10. 18.
	 * @description : 정산내역(위탁) 업체 상세정보 조회
	 *
	 * @param oss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsSettle> getSettleBizDetailList(OmsSettleSearch oss) {
		String saleDate = oss.getSaleYear() + "-" + String.format("%02d", Integer.parseInt(oss.getSaleMonth()));
		oss.setSaleDate(saleDate);

		List<OmsSettle> bizDetailList = (List<OmsSettle>) dao.selectList("oms.settlebiz.getSettleBizDetailList", oss);
		for (OmsSettle stlOne : bizDetailList) {
			//수수료율 % 포함
			stlOne.setStrCommissionRate(stlOne.getCommissionRate() + "%");
			//고객결제금액=판매금액-기타금액(매일포인트+예치금-현금환불-예치금환불)-자사쿠폰금액(상품쿠폰+플러스쿠폰+장바구니쿠폰)-업체쿠폰금액
			int paymentAmt = stlOne.getSaleAmt().intValue() - stlOne.getEtcAmt().intValue() - stlOne.getOwnCouponAmt().intValue()
					- stlOne.getBizCouponAmt().intValue();
			stlOne.setPaymentAmt(new BigDecimal(paymentAmt));
		}

		return bizDetailList;
	}
}