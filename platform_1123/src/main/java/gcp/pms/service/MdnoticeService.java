
package gcp.pms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsNoticeproduct;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.service.BaseService;
import gcp.pms.model.PmsProduct;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.pms.mdnotice
 * @FileName : MdnoticeService.java
 * @author : roy
 * @date : 2016. 6. 20.
 * @description : Mdnotice Service
 */

@Service("mdnoticeService")
public class MdnoticeService extends BaseService {

	/**
	 * 
	 * @Method Name : selectNoticeList
	 * @author : roy
	 * @date : 2016. 6. 20.
	 * @description : MD공지 목록 조회
	 * 
	 * @param ccsNoticeSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsNotice> selectNoticeList(CcsNoticeSearch ccsNoticeSearch) {
		// 상품 번호 검색시 다른 조건 무시
		if (CommonUtil.isNotEmpty(ccsNoticeSearch.getNoticeNos())) {
			String temp = ccsNoticeSearch.getNoticeNos();
			ccsNoticeSearch = new CcsNoticeSearch();
			ccsNoticeSearch.setNoticeNos(temp);
		}
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());

		return (List<CcsNotice>) dao.selectList("pms.mdnotice.list", ccsNoticeSearch);
	}

	/**
	 * 
	 * @Method Name : selectNotice
	 * @author : roy
	 * @date : 2016. 6. 20.
	 * @description : MD공지 조회
	 * 
	 * @param notice
	 * @return
	 */
	public CcsNotice selectNotice(CcsNotice notice) throws Exception {
		return (CcsNotice) dao.selectOneTable(notice);
	}

	/**
	 * MD공지 등록
	 * 
	 * @Method Name : insertNotice
	 * @author : roy
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param notice
	 * @throws Exception
	 */
	public int insertNotice(CcsNotice notice) throws Exception {
		return dao.insertOneTable(notice);
	}

	/**
	 * MD공지 수정
	 * 
	 * @Method Name : updateNotice
	 * @author : roy
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param notice
	 * @throws Exception
	 */
	public int updateNotice(CcsNotice notice) throws Exception {
		return dao.updateOneTable(notice);
	}

	/**
	 * 
	 * @Method Name : selectNoticeProductList
	 * @author : roy
	 * @date : 2016. 6. 21.
	 * @description : MD공지상품 조회
	 * 
	 * @param ccsNoticeSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProduct> selectNoticeProductList(CcsNoticeSearch ccsNoticeSearch) {
		return (List<PmsProduct>) dao.selectList("pms.mdnotice.productlist", ccsNoticeSearch);
	}

	/**
	 * MD공지상품 수정
	 * 
	 * @Method Name : saveNoticeProduct
	 * @author : roy
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	public void saveNoticeProduct(CcsNoticeSearch ccsNoticeSearch) throws Exception {
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());

		if (CommonUtil.isEmpty(ccsNoticeSearch.getNoticeNos())) {

			String noticeNo = (String) dao.selectOne("pms.mdnotice.selectNoticeNo", ccsNoticeSearch);
			ccsNoticeSearch.setNoticeNos(noticeNo);
		}

		dao.delete("pms.mdnotice.deleteNoticeproduct", ccsNoticeSearch);

		for (int i = 0; i < ccsNoticeSearch.getProductIds().length; i++) {

			CcsNoticeproduct notice = new CcsNoticeproduct();
			notice.setNoticeNo(new BigDecimal(ccsNoticeSearch.getNoticeNos()));
			notice.setStoreId(SessionUtil.getStoreId());

			if (CommonUtil.isNotEmpty(ccsNoticeSearch.getProductIds()[i])) {
				notice.setProductId(ccsNoticeSearch.getProductIds()[i]);
			}

			String exists1 = (String) dao.selectOne("pms.mdnotice.selectOne", notice);

			if (CommonUtil.isEmpty(exists1)) {
				notice.setInsId(SessionUtil.getLoginId());
				notice.setUpdId(SessionUtil.getLoginId());
				dao.insertOneTable(notice);
			}
		}
	}

	/**
	 * MD공지 삭제 전 체크
	 * 
	 * @Method Name : checkNoticeProduct
	 * @author : roy
	 * @date : 2016. 6. 21.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	public void checkNoticeProduct(CcsNoticeSearch ccsNoticeSearch) throws Exception {
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		String exists1 = (String) dao.selectOne("pms.mdnotice.selectProductList", ccsNoticeSearch);
		if (CommonUtil.isNotEmpty(exists1)) {
			throw new ServiceException("pms.mdnotice.notice");
		}
	}

	/**
	 * MD 공지 상품 삭제
	 * 
	 * @Method Name : deleteNoticeProduct
	 * @author : roy
	 * @date : 2016. 6. 21.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	public void deleteNoticeProduct(CcsNoticeSearch ccsNoticeSearch) throws Exception {
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		dao.delete("pms.mdnotice.deleteMdnoticeproduct", ccsNoticeSearch);

		CcsNotice c = new CcsNotice();
		c.setStoreId(SessionUtil.getStoreId());
		c.setNoticeNo(new BigDecimal(ccsNoticeSearch.getNoticeNos()));
		dao.deleteOneTable(c);
	}
}
