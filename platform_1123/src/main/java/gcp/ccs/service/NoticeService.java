package gcp.ccs.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsNoticeBrand;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.sps.model.SpsEvent;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class NoticeService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<CcsNotice> selectList(CcsNoticeSearch ccsNoticeSearch) {
		return (List<CcsNotice>) dao.selectList("ccs.notice.list", ccsNoticeSearch);
	}

	@SuppressWarnings("unchecked")
	public List<CcsNotice> selectPoList(CcsNoticeSearch ccsNoticeSearch) {
		return (List<CcsNotice>) dao.selectList("ccs.notice.polist", ccsNoticeSearch);
	}

	/**
	 * 프론트 일반 공지사항 상세 조회
	 * 
	 * @Method Name : selectOne
	 * @author : intune
	 * @date : 2016. 10. 17.
	 * @description :
	 *
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public CcsNotice selectOne(CcsNotice notice) throws Exception {
		CcsNotice noticeInfo = (CcsNotice) dao.selectOne("ccs.notice.getNoticeDetail", notice);

		// readCnt 증가
		notice.setReadCnt(noticeInfo.getReadCnt().add(new BigDecimal("1")));
		updateNoticeReadCnt(notice);
		notice.setReadCnt(notice.getReadCnt());

		return noticeInfo;
	}

	/**
	 * MD 공지 조회 ( 어드민 관리용)
	 * 
	 * @Method Name : selectMdNotice
	 * @author : intune
	 * @date : 2016. 10. 17.
	 * @description :
	 *
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public CcsNotice selectMdNotice(CcsNotice notice) throws Exception {
		CcsNotice noticeInfo = (CcsNotice) dao.selectOne("ccs.notice.getNoticeDetail", notice);
		return noticeInfo;
	}

	public int insert(CcsNotice notice) throws Exception {
		int result = dao.insertOneTable(notice);
		BigDecimal noticeNo = (BigDecimal) dao.selectOne("ccs.notice.getNoticeNo", notice);
		notice.setStoreId(SessionUtil.getStoreId());
		
		// CCS_NOTICE_BRAND 생성, FO 공지(일반, 이벤트)일때
		if(BaseConstants.NOTICE_TYPE_CD_FRONT.equals(notice.getNoticeTypeCd()) || 
				BaseConstants.NOTICE_TYPE_CD_EVENT.equals(notice.getNoticeTypeCd())){
			for (int i = 0; i < notice.getCcsBrands().length; i++) {
				CcsNoticeBrand brand = new CcsNoticeBrand();
				if (CommonUtil.isNotEmpty(notice.getCcsBrands()[i])) {
					brand.setStoreId(SessionUtil.getStoreId());
					brand.setNoticeNo(noticeNo);
					brand.setBrandId(notice.getCcsBrands()[i]);
					
					dao.insertOneTable(brand);
				}
			}
		}
		return result;
	}

	public int update(CcsNotice notice) throws Exception {
		
		
		// CCS_NOTICE_BRAND 생성, FO 일반 공지일때
		if(notice.getNoticeTypeCd().equals("NOTICE_TYPE_CD.FRONT")){
			// CCS_NOTICE_BRAND 삭제
			dao.delete("ccs.notice.deleteNoticeBrand", notice);
			
			for (int i = 0; i < notice.getCcsBrands().length; i++) {
				CcsNoticeBrand brand = new CcsNoticeBrand();
				if (CommonUtil.isNotEmpty(notice.getCcsBrands()[i])) {
					brand.setStoreId(SessionUtil.getStoreId());
					brand.setNoticeNo(notice.getNoticeNo());
					brand.setBrandId(notice.getCcsBrands()[i]);
					
					dao.insertOneTable(brand);
				}
			}
		}
		notice.setUpdId(SessionUtil.getLoginId());
		notice.setStoreId(SessionUtil.getStoreId());
		return dao.updateOneTable(notice);
	}
	
	/**
	 * 이벤트 존재 여부 체크
	 * 
	 * @Method Name : checkEvent
	 * @author : roy
	 * @date : 2016. 9. 5.
	 * @description :
	 *
	 * @param SpsEvent
	 * @return
	 */
	public int checkEvent(SpsEvent event) throws Exception {
		return (int) dao.selectOne("sps.event.checkExistEvent", event);
	}

	public int delete(CcsNotice notice) throws Exception {
		return dao.deleteOneTable(notice);
	}
	
	/**
	 * 상품의 MD공지 조회
	 * 
	 * @Method Name : getProductMdNotice
	 * @author : eddie
	 * @date : 2016. 8. 25.
	 * @description :
	 *
	 * @param noticeSearch
	 * @return
	 */
	public CcsNotice getProductMdNotice(CcsNoticeSearch noticeSearch) {
		return (CcsNotice) dao.selectOne("ccs.notice.getProductMdNotice", noticeSearch);

	}
	
//	FO
	
	// FO 고객센터 메인
	/**
	 * FO 고객센터 메인 공지사항
	 * 
	 * @Method Name : selectCsNoticeList
	 * @author : roy
	 * @date : 2016. 09. 05.
	 * @description :
	 *
	 * @param noticeSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsNotice> selectCsNoticeList(CcsNoticeSearch ccsNoticeSearch) {
		return (List<CcsNotice>) dao.selectList("ccs.notice.noticelist", ccsNoticeSearch);
	}
	
	/**
	 * FO 고객센터 메인 공지사항
	 * 
	 * @Method Name : selectCsEventList
	 * @author : roy
	 * @date : 2016. 09. 05.
	 * @description :
	 *
	 * @param noticeSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsNotice> selectCsEventList(CcsNoticeSearch ccsNoticeSearch) {
		return (List<CcsNotice>) dao.selectList("ccs.notice.eventlist", ccsNoticeSearch);
	}
	
	/**
	 * 공지사항 목록 조회
	 * 
	 * @Method Name : getNoticeListSearch
	 * @author : roy
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @return
	 */
	public List<CcsNotice> getNoticeListSearch(CcsNoticeSearch search) {
		return (List<CcsNotice>) dao.selectList("ccs.notice.getNoticeListSearch", search);
	}
	
	/**
	 * 당첨자 발표 목록 조회
	 * 
	 * @Method Name : getEventNoticeListSearch
	 * @author : roy
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @return
	 */
	public List<CcsNotice> getEventNoticeListSearch(CcsNoticeSearch search) {
		return (List<CcsNotice>) dao.selectList("ccs.notice.getEventNoticeListSearch", search);
	}
	
	/**
	 * 공지사항 조회수 증가
	 * 
	 * @Method Name : updateNoticeReadCnt
	 * @author : roy
	 * @date : 2016. 9. 2.
	 * @description :
	 *
	 * @return
	 * @throws Exception 
	 */
	public void updateNoticeReadCnt(CcsNotice notice) throws Exception {
		notice.setUpdId(SessionUtil.getLoginId());
		notice.setStoreId(SessionUtil.getStoreId());
		dao.updateOneTable(notice);
	}

	/**
	 * FO 브랜드관 템플릿 메인 최근 공지사항
	 * 
	 * @Method Name : getBrandNotice
	 * @author : stella
	 * @date : 2016. 10. 5.
	 * @description :
	 *
	 * @return
	 * @throws Exception 
	 */
	public CcsNotice getBrandNotice(CcsNoticeSearch search) throws Exception {
		return (CcsNotice) dao.selectOne("ccs.notice.getBrandNotice", search);
	}
	
	/**
	 * FO 브랜드관 템플릿 메인 브랜드별 공지사항 리스트
	 * 
	 * @Method Name : getBrandNoticeList
	 * @author : stella
	 * @date : 2016. 10. 5.
	 * @description :
	 *
	 * @return
	 * @throws Exception 
	 */
	public List<CcsNotice> getBrandNoticeList(CcsNoticeSearch search) throws Exception {
		return (List<CcsNotice>) dao.selectList("ccs.notice.getBrandNoticeList", search);
	}
}
