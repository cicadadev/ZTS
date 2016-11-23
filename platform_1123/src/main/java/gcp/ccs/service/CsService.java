package gcp.ccs.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsInquiry;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.pms.model.PmsProductqna;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.search.SpsCouponIssueSearch;

@Service
public class CsService extends BaseService {

	protected final Log logger = LogFactory.getLog(getClass());

//	/**
//	 * @Method Name : getMypageMain
//	 * @author : ian
//	 * @date : 2016. 7. 20.
//	 * @description : mypage 메인
//	 *
//	 * @param search
//	 * @return
//	 */
//	public MmsMember getMypageMain(MmsMemberSearch search) {
//		return (MmsMember) dao.selectOne("mms.mypage.getMypageMain", search);
//	}

	/**
	 * @Method Name : getCarrotInfo, getCarrotList
	 * @author : ian
	 * @date : 2016. 7. 20.
	 * @description : 당근 보유/소멸예정 포인트, 당근내역
	 *
	 * @param search
	 * @return
	 */
	public MmsCarrot getCarrotInfo(MmsMemberSearch search) {
		return (MmsCarrot) dao.selectOne("mms.mypage.getCarrotInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<MmsCarrot> getCarrotList(MmsMemberSearch search) {
		List<MmsCarrot> resultList = new ArrayList<MmsCarrot>();
		List<MmsCarrot> list = (List<MmsCarrot>) dao.selectList("mms.mypage.getCarrotList", search);
		for (MmsCarrot carrot : list) {
			carrot.setCarrotTypeCd(carrot.getCarrotTypeName());
			resultList.add(carrot);
		}
		return resultList;
	}

	/**
	 * @Method Name : getCouponInfo, getCouponList
	 * @author : ian
	 * @date : 2016. 7. 21.
	 * @description : 쿠폰 보유/소멸예정 쿠폰, 쿠폰내역
	 *
	 * @param search
	 * @return
	 */
	public SpsCouponissue getCouponInfo(SpsCouponIssueSearch search) {
		return (SpsCouponissue) dao.selectOne("mms.mypage.getCouponInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<SpsCouponissue> getCouponList(SpsCouponIssueSearch search) {
		List<SpsCouponissue> resultList = new ArrayList<SpsCouponissue>();
		List<SpsCouponissue> list = (List<SpsCouponissue>) dao.selectList("mms.mypage.getCouponList", search);
		for (SpsCouponissue coupon : list) {
			logger.debug(coupon.getSpsCoupon());
//			resultList.add(coupon);
		}
		return list;
	}

	public MmsDeposit getDepositInfo(MmsMemberSearch search) {
		return (MmsDeposit) dao.selectOne("mms.mypage.getDepositInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<MmsDeposit> getDepositList(MmsMemberSearch search) {
		List<MmsDeposit> resultList = new ArrayList<MmsDeposit>();
		List<MmsDeposit> list = (List<MmsDeposit>) dao.selectList("mms.mypage.getDepositList", search);
		for (MmsDeposit deposit : list) {
			deposit.setDepositTypeCd(deposit.getDepositTypeName());
			resultList.add(deposit);
		}
		return list;
	}

	/**
	 * @Method Name : getMyQaList, getProductQnaList
	 * @author : ian
	 * @date : 2016. 8. 22.
	 * @description : 내가 문의한글 (1:1문의 / 상품QNA)
	 *
	 * @param search
	 * @return
	 */
	public CcsInquiry getMyQaInfo(MmsMemberSearch search) {
		return (CcsInquiry) dao.selectOne("mms.mypage.getMyQaInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<CcsInquiry> getMyQaList(MmsMemberSearch search) {
		List<CcsInquiry> resultList = new ArrayList<CcsInquiry>();
		List<CcsInquiry> list = (List<CcsInquiry>) dao.selectList("mms.mypage.getMyQaList", search);
		for (CcsInquiry inquiry : list) {
			inquiry.setInquiryTypeCd(inquiry.getInquiryTypeName());
			inquiry.setInquiryStateCd(inquiry.getInquiryStateName());
			resultList.add(inquiry);
		}
		return list;
	}

	public PmsProductqna getProductQnaInfo(MmsMemberSearch search) {
		return (PmsProductqna) dao.selectOne("mms.mypage.getProductQnaInfo", search);
	}

	@SuppressWarnings("unchecked")
	public List<PmsProductqna> getProductQnaList(MmsMemberSearch search) {
		List<PmsProductqna> resultList = new ArrayList<PmsProductqna>();
		List<PmsProductqna> list = (List<PmsProductqna>) dao.selectList("mms.mypage.getProductQnaList", search);
		for (PmsProductqna qna : list) {
			qna.setProductQnaTypeCd(qna.getProductQnaTypeName());
			qna.setProductQnaStateCd(qna.getProductQnaStateName());
			resultList.add(qna);
		}
		return list;
	}

}
