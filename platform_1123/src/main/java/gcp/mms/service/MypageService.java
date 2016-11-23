package gcp.mms.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.service.BaseService;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsMember;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsWishlist;
import gcp.mms.model.search.MmsMemberPopupSearch;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductqna;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.search.SpsCouponIssueSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.MessageUtil;

@Service
public class MypageService extends BaseService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MemberService memberService;
	
	/**
	 * @Method Name : getMypageMain
	 * @author : ian
	 * @date : 2016. 7. 20.
	 * @description : mypage 메인, 회원정보, 당근, 예치금, 쿠폰
	 *
	 * @param search
	 * @return
	 */
	public MmsMemberZts getMypageMemberInfo(MmsMemberSearch search) {
		return (MmsMemberZts) dao.selectOne("mms.mypage.getMypageMemberInfo", search);
	}

	/**
	 * @Method Name : getCarrotInfo, getCarrotList
	 * @author : ian
	 * @date : 2016. 7. 20.
	 * @description : getCarrotInfo : 당근 보유/소멸예정, getCarrotList : 당근내역
	 *
	 * @param search
	 * @return
	 */
	public MmsCarrot getCarrotInfo(MmsMemberSearch search) {
		return (MmsCarrot) dao.selectOne("mms.mypage.getCarrotInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<MmsCarrot> getCarrotList(MmsMemberSearch search) {
		List<MmsCarrot> list = (List<MmsCarrot>) dao.selectList("mms.mypage.getCarrotList", search);
		return list;
	}

	/**
	 * @Method Name : getCouponInfo, getCouponList
	 * @author : ian
	 * @date : 2016. 7. 21.
	 * @description : getCouponInfo : 쿠폰 보유/소멸예정 쿠폰, getCouponList : 쿠폰내역
	 *
	 * @param search
	 * @return
	 */
	public SpsCouponissue getCouponInfo(SpsCouponIssueSearch search) {
		return (SpsCouponissue) dao.selectOne("mms.mypage.getCouponInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<SpsCouponissue> getCouponList(SpsCouponIssueSearch search) {
		List<SpsCouponissue> list = (List<SpsCouponissue>) dao.selectList("mms.mypage.getCouponList", search);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<SpsCouponissue> getCouponListOffline(SpsCouponIssueSearch search) {
		List<SpsCouponissue> list = (List<SpsCouponissue>) dao.selectList("mms.mypage.getCouponListOffline", search);
		return list;
	}

	/**
	 * @Method Name : getDepositInfo, getDepositList
	 * @author : ian
	 * @date : 2016. 9. 2.
	 * @description : 예치금 보유, 예치금 내역
	 *
	 * @param search
	 * @return
	 */
	public MmsDeposit getDepositInfo(MmsMemberSearch search) {
		return (MmsDeposit) dao.selectOne("mms.mypage.getDepositInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<MmsDeposit> getDepositList(MmsMemberSearch search) {
		List<MmsDeposit> list = (List<MmsDeposit>) dao.selectList("mms.mypage.getDepositList", search);
		return list;
	}

	/**
	 * @Method Name : getMyQaInfo, getMyQaList, getProductQnaInfo, getProductQnaList
	 * @author : ian
	 * @date : 2016. 8. 22.
	 * @description : 내가 문의한글 (1:1문의 / 상품QNA) 정보, 내역
	 *
	 * @param search
	 * @return
	 */
	public CcsInquiry getMyQaInfo(MmsMemberSearch search) {
		return (CcsInquiry) dao.selectOne("mms.mypage.getMyQaInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<CcsInquiry> getMyQaList(MmsMemberSearch search) {
		List<CcsInquiry> list = (List<CcsInquiry>) dao.selectList("mms.mypage.getMyQaList", search);
		return list;
	}

	public PmsProductqna getProductQnaInfo(MmsMemberSearch search) {
		return (PmsProductqna) dao.selectOne("mms.mypage.getProductQnaInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<PmsProductqna> getProductQnaList(MmsMemberSearch search) {
		List<PmsProductqna> list = (List<PmsProductqna>) dao.selectList("mms.mypage.getProductQnaList", search);
		return list;
	}

	/**
	 * @Method Name : deleteProductQna
	 * @author : ian
	 * @date : 2016. 9. 2.
	 * @description : 상품Q&A 삭제
	 *
	 * @param qna
	 * @throws Exception
	 */
	public void deleteProductQna(PmsProductqna qna) throws Exception {
		dao.deleteOneTable(qna);
	}

	/**
	 * @Method Name : deleteMyQa
	 * @author : ian
	 * @date : 2016. 9. 2.
	 * @description : 1:1 문의 삭제
	 *
	 * @param inquiry
	 * @throws Exception
	 */
	public void deleteMyQa(CcsInquiry inquiry) throws Exception {
		dao.deleteOneTable(inquiry);
	}
	
	/**
	 * @Method Name : setDepositRefundData
	 * @author : ian
	 * @date : 2016. 9. 9.
	 * @description : 예치금 환불 데이터 세팅 및 예치금 차감
	 *
	 * @param payment
	 * @return
	 */
	public Map<String, String> insertDepositRefundData(OmsPayment payment) throws Exception {

		Map<String, String> resultMap = new HashMap<String, String>();

		MmsMember member = new MmsMember();
		member.setMemberNo(payment.getMemberNo());
		member = (MmsMember) dao.selectOneTable(member);

//		if (CommonUtil.isNotEmpty(member.getMemberName()) && payment.getAccountHolderName().equals(member.getMemberName())) {
			
			BigDecimal balanceAmt = BigDecimal.ZERO;
			if (payment.getPaymentAmt() != null && (payment.getPaymentAmt().abs()).compareTo(BigDecimal.ZERO) > 0) {
				balanceAmt = payment.getPaymentAmt();
			} else {
				MmsMemberPopupSearch search = new MmsMemberPopupSearch();
				search.setMemberNo(payment.getMemberNo());
				balanceAmt = memberService.getDepositBalanceAmt(search);
			}

			// TODO 최소 환불 예치금 확인
			if ((balanceAmt.abs()).compareTo(BigDecimal.ZERO) == 1) {
				// 예치금 삭감
				MmsDeposit deposit = new MmsDeposit();
				deposit.setMemberNo(payment.getMemberNo());
				deposit.setDepositAmt(balanceAmt.negate());
				deposit.setDepositTypeCd("DEPOSIT_TYPE_CD.CASHREFUND");
				
				if(BaseConstants.REFUND_REASON_CD_REFUNDDEPOSIT.equals(payment.getRefundReasonCd()) 
						&& BaseConstants.PAYMENT_STATE_CD_REFUND_CANCEL.equals(payment.getPaymentStateCd())) {
					deposit.setNote("환불취소");
				} else {
					deposit.setNote("마이페이지 환불신청");
				}
				
				// 환불 예치금 등록
				dao.insertOneTable(deposit);

				resultMap.put("code", BaseConstants.DEPOSIT_REFUND_REQ_RESULT_SUCCESS);
				resultMap.put("depositNo", (deposit.getDepositNo()).toString());
				resultMap.put("balanceAmt", (balanceAmt).toString());

			} else {
				logger.error("\t 예치금 잔액 > " + balanceAmt);

				resultMap.put("code", BaseConstants.DEPOSIT_REFUND_REQ_RESULT_LACK);
				resultMap.put("msg", MessageUtil.getMessage("mms.deposit.lack.amt"));
			}
//		} else {
//			logger.error("\t 환불 요청 예금주명 : " + payment.getAccountHolderName());
//			logger.error("\t 환불 요청 은행정보 : " + payment.getPaymentBusinessCd() + ", " + payment.getPaymentBusinessNm());
//			logger.error("\t 환불 요청 계좌번호 : " + payment.getRefundAccountNo());
//
//			resultMap.put("code", BaseConstants.DEPOSIT_REFUND_REQ_RESULT_LACK);
//			resultMap.put("msg", MessageUtil.getMessage("mms.account.holder.name.wrong"));
//		}

		return resultMap;
	}
	
	/**
	 * @Method Name : regRefundAccount
	 * @author : ian
	 * @date : 2016. 9. 10.
	 * @description : 환불계좌 등록 
	 *
	 * @param omsPayment
	 * @throws Exception
	 */
	public String updateRegRefundAccount (OmsPayment omsPayment) throws Exception {
		
		MmsMember member = new MmsMember();
		member.setMemberNo(omsPayment.getMemberNo());
		member = (MmsMember) dao.selectOneTable(member);
		
//		if(CommonUtil.isNotEmpty(member.getMemberName()) && omsPayment.getAccountHolderName().equals(member.getMemberName())) {
			MmsMemberZts mmsMemberZts = new MmsMemberZts();
			mmsMemberZts.setMemberNo(omsPayment.getMemberNo());
			mmsMemberZts.setAccountHolderName(omsPayment.getAccountHolderName());
			mmsMemberZts.setAccountNo(omsPayment.getRefundAccountNo());
			mmsMemberZts.setBankCd(omsPayment.getPaymentBusinessCd());
			mmsMemberZts.setBankName(omsPayment.getPaymentBusinessNm());
			mmsMemberZts.setUpdId(member.getMemberId());
			mmsMemberZts.setAccountAuthDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
			
			dao.update("mms.member.updateAccount", mmsMemberZts);
			
			return BaseConstants.RESULT_FLAG_SUCCESS;
//		} else {
//			logger.error("\t 요청 예금주명 : " + omsPayment.getAccountHolderName());
//			logger.error("\t 요청 은행정보 : " + omsPayment.getPaymentBusinessCd() + ", "+ omsPayment.getPaymentBusinessNm());
//			logger.error("\t 요청 계좌번호 : " + omsPayment.getRefundAccountNo());
//			logger.debug(MessageUtil.getMessage("mms.account.holder.name.wrong"));
//			return MessageUtil.getMessage("mms.account.holder.name.wrong");
//		}
		
	}
	
	/**
	 * @Method Name : getReceiptList
	 * @author : ian
	 * @date : 2016. 9. 12.
	 * @description : 영수증 조회
	 *
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrder> getReceiptList (MmsMemberSearch search) {
		return (List<OmsOrder>) dao.selectList("mms.mypage.getReceiptList", search);
	}
	
	/**
	 * @Method Name : getShoppingWishlist
	 * @author : stella
	 * @date : 2016. 9. 12.
	 * @description : 쇼핑찜 조회
	 *
	 * @param payment
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProduct> getShoppingWishlist(MmsMemberSearch search) throws Exception {
		List<PmsProduct> productList = (List<PmsProduct>) dao.selectList("mms.mypage.getShoppingWishlist", search);
		
		for(PmsProduct product : productList) {
			String grade = CommonUtil.isEmpty(search.getMemGradeCd()) ? "" : search.getMemGradeCd();
			switch (grade) {
				case BaseConstants.MEMBER_GRADE_VIP :
					product.setSalePrice(product.getPmsProductprice().getVipSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getVipPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getVipCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getVipDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_GOLD :
					product.setSalePrice(product.getPmsProductprice().getGoldSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getGoldPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getGoldCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getGoldDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_SILVER :
					product.setSalePrice(product.getPmsProductprice().getSilverSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getSilverPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getSilverCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getSilverDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_FAMILY :
					product.setSalePrice(product.getPmsProductprice().getFamilySalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getFamilyPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getFamilyCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getFamilyDeliveryFeeFreeYn());
					break;
				case BaseConstants.MEMBER_GRADE_WELCOME :
					product.setSalePrice(product.getPmsProductprice().getWelcomeSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getWelcomePointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getWelcomeCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getWelcomeDeliveryFeeFreeYn());
					break;
				default  :
					product.setSalePrice(product.getPmsProductprice().getSalePrice()); 
					product.setPointSaveYn(product.getPmsProductprice().getPointYn());
					product.setCouponYn(CommonUtil.isEmpty(product.getPmsProductprice().getCouponId()) ? "N" : "Y");
					product.setFreeDeliveryYn(product.getPmsProductprice().getDeliveryFeeFreeYn());
					break;
			}
		}
		return productList;
	}
	

	/**
	 * 
	 * @Method Name : deleteWishList
	 * @author : stella
	 * @date : 2016. 9. 9.
	 * @description : 쇼핑찜 목록 삭제
	 *
	 * @param MmsWishlist
	 * @return
	 * @throws Exception
	 */
	public void deleteWishList(MmsWishlist wishlist) throws Exception {
		dao.delete("mms.mypage.deleteWishlist", wishlist);
	}

	/**
	 * @Method Name : getDeliveryStep
	 * @author : ian
	 * @date : 2016. 10. 11.
	 * @description : 마이페이지 메인 주문/배송현황
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrder> getDeliveryStep (MmsMemberSearch search) {
		return (List<OmsOrder>) dao.selectList("mms.mypage.getDeliveryStep", search);
	}
	
	/**
	 * @Method Name : getLatestOrderList
	 * @author : ian
	 * @date : 2016. 10. 27.
	 * @description : 마이페이지 최근 7일 주문 내역
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrder> getLatestOrderList (OmsOrderSearch search) {
		return (List<OmsOrder>) dao.selectList("mms.mypage.getLatestOrderList", search);
	}
	
}