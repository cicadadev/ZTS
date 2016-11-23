package gcp.ccs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.search.CcsInquirySearch;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.model.search.CcsPopupNoticeSearch;
import gcp.ccs.model.search.CcsUserSearch;
import gcp.common.util.BoSessionUtil;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.search.PmsProductQnaSearch;
import gcp.pms.model.search.PmsProductSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class MainService extends BaseService {
	@SuppressWarnings("unchecked")
	public List<CcsNotice> getSimpleNoticeList(CcsNoticeSearch search) throws Exception {
		return (List<CcsNotice>) dao.selectList("ccs.main.getNoticeList", search);
	}
	
	/**
	 * 
	 * @Method Name : getProductQnaList
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description : 상품QnA 목록 조회
	 *
	 * @param storeId
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProductqna> getProductQnaList(PmsProductQnaSearch search) throws Exception {
		return (List<PmsProductqna>) dao.selectList("ccs.main.getProductQnaList", search);
	}
	
	/**
	 * 
	 * @Method Name : getProductQnaState
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description : 상품QnA 현황 조회
	 *
	 * @param search
	 */
	
	public PmsProductqna getProductQnaState(PmsProductQnaSearch search) throws Exception {
		PmsProductqna productQnaState = new PmsProductqna();
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			productQnaState = (PmsProductqna) dao.selectOne("ccs.main.getProductQnaState", search);
		} else {
			productQnaState = (PmsProductqna) dao.selectOne("ccs.main.getBoProductQnaState", search);
		}
		
		return productQnaState;
	}
	
	/**
	 * 
	 * @Method Name : getInquiryList
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description : 문의사항 목록 조회
	 *
	 * @param storeId
	 */
	@SuppressWarnings("unchecked")
	public List<CcsInquiry> getInquiryList(CcsInquirySearch search) throws Exception {
		return (List<CcsInquiry>) dao.selectList("ccs.main.getInquiryList", search);
	}
	
	/**
	 * 
	 * @Method Name : getInquiryState
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description : 문의사항 현황 조회
	 *
	 * @param search
	 */
	public CcsInquiry getInquiryState(CcsInquirySearch search) throws Exception {

		CcsInquiry qnaInfoState = new CcsInquiry();
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			qnaInfoState = (CcsInquiry) dao.selectOne("ccs.main.getInquiryState", search);
		} else {
			qnaInfoState = (CcsInquiry) dao.selectOne("ccs.main.getBoInquiryState", search);
		}
		return qnaInfoState;
	}
	
	/**
	 * 
	 * @Method Name : getOrderState
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description : 주문 현황 조회
	 *
	 * @param search
	 */
	public OmsOrder getOrderState(OmsOrderSearch search) throws Exception {

		OmsOrder orderInfoState = (OmsOrder) dao.selectOne("ccs.main.getOrderState", search);
		
		return orderInfoState;
	}
	
	/**
	 * 
	 * @Method Name : getProductList
	 * @author : roy
	 * @date : 2016. 8. 5.
	 * @description : 품절 임박 리스트 조회
	 *
	 * @param search
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProduct> getProductList(PmsProductSearch search) {
		search.setStoreId(SessionUtil.getStoreId());

		return (List<PmsProduct>) dao.selectList("ccs.main.getProductList", search);
	}

	/**
	 * 
	 * @Method Name : getMdList
	 * @author : roy
	 * @date : 2016. 10. 4.
	 * @description : 업무 담당자 조회
	 *
	 * @param search
	 */
	@SuppressWarnings("unchecked")
	public List<CcsUser> getMdList(CcsUserSearch search) {
		search.setStoreId(SessionUtil.getStoreId());

		return (List<CcsUser>) dao.selectList("ccs.main.getMdList", search);
	}

	/**
	 * 
	 * @Method Name : getPopupList
	 * @author : roy
	 * @date : 2016. 10. 4.
	 * @description : PO 팝업 조회
	 *
	 * @param search
	 */
	@SuppressWarnings("unchecked")
	public List<CcsPopup> getPopupList(CcsPopupNoticeSearch search) {
		search.setStoreId(SessionUtil.getStoreId());

		return (List<CcsPopup>) dao.selectList("ccs.main.getPopupList", search);
	}

}
