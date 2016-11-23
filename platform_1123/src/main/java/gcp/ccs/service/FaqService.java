package gcp.ccs.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsFaq;
import gcp.ccs.model.search.CcsFaqSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Service
public class FaqService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<CcsFaq> selectList(CcsFaqSearch ccsFaqSearch) {
//		// FAQ 번호 검색시 다른 조건 무시
//		if (CommonUtil.isNotEmpty(ccsFaqSearch.getFaqNos())) {
//			String temp = ccsFaqSearch.getFaqNos();
//			ccsFaqSearch = new CcsFaqSearch();
//			ccsFaqSearch.setFaqNos(temp);
//		}
//		ccsFaqSearch.setStoreId(SessionUtil.getStoreId());

		return (List<CcsFaq>) dao.selectList("ccs.faq.list", ccsFaqSearch);
	}

	public CcsFaq selectOne(CcsFaq faq) throws Exception {

		return (CcsFaq) dao.selectOne("ccs.faq.getFaqDetail", faq);
//		return (CcsFaq) dao.selectOneTable(faq);
	}

	public int insert(CcsFaq faq) throws Exception {
		if (CommonUtil.isEmpty(faq.getSortNo())) {
			faq.setSortNo(new BigDecimal("9999"));
		}

		CcsFaq clob = new CcsFaq();
		clob.setDetail(faq.getDetail());
		clob.setStoreId(SessionUtil.getStoreId());

		faq.setDetail(" ");
		faq.setUpdId(SessionUtil.getLoginId());
		faq.setStoreId(SessionUtil.getStoreId());
		int faqNo = dao.insertOneTable(faq);

		clob.setFaqNo((BigDecimal) dao.selectOne("ccs.faq.getFaqNo", faq));
		dao.update("ccs.faq.updateClob", clob);
		return faqNo;
	}

	public int update(CcsFaq faq) throws Exception {
		faq.setUpdId(SessionUtil.getLoginId());
		faq.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isEmpty(faq.getSortNo())) {
			faq.setSortNo(new BigDecimal("9999"));
		}

		CcsFaq clob = new CcsFaq();
		clob.setDetail(faq.getDetail());
		clob.setStoreId(SessionUtil.getStoreId());
		clob.setFaqNo(faq.getFaqNo());

		faq.setDetail(" ");
		faq.setUpdId(SessionUtil.getLoginId());
		faq.setStoreId(SessionUtil.getStoreId());
		int faqNo = dao.updateOneTable(faq);
		dao.update("ccs.faq.updateClob", clob);

		return faqNo;
	}

	public int delete(CcsFaq faq) throws Exception {
		return dao.deleteOneTable(faq);
	}
	
	@SuppressWarnings("unchecked")
	public List<CcsFaq> selectCsList(CcsFaqSearch ccsFaqSearch) {
		return (List<CcsFaq>) dao.selectList("ccs.faq.cslist", ccsFaqSearch);
	}
	
//	FO
	
	/**
	 * FAQ 목록 조회
	 * 
	 * @Method Name : getFaqListSearch
	 * @author : roy
	 * @date : 2016. 8. 30.
	 * @description :
	 *
	 * @return
	 */
	public List<CcsFaq> getFaqListSearch(CcsFaqSearch search) {
		return (List<CcsFaq>) dao.selectList("ccs.faq.getFaqListSearch", search);
	}
}
