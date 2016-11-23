package gcp.mms.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.mms.model.MmsStyle;
import gcp.mms.model.MmsStylelike;
import gcp.mms.model.MmsStyleproduct;
import gcp.mms.model.search.MmsStyleSearch;
import intune.gsf.common.utils.SessionUtil;

@Service
public class StyleService extends BaseService {
	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 
	 * FO 브랜드관 스타일 조회
	 * 
	 * @Method Name : deleteWishList
	 * @author : stella
	 * @date : 2016. 9. 9.
	 * @description :
	 *
	 * @param MmsWishlist
	 * @return
	 * @throws Exception
	 */
	public List<MmsStyle> getStyleList(MmsStyleSearch search) throws Exception {
		return (List<MmsStyle>) dao.selectList("mms.style.getStyleList", search);
	}

	/**
	 * 
	 * @Method Name : getMemberStyleTotalCnt
	 * @author : allen
	 * @date : 2016. 10. 8.
	 * @description : 회원의 스타일 개수 조회
	 *
	 * @param search
	 * @return
	 */
	public int getMemberStyleTotalCnt(MmsStyleSearch search) {
		return dao.selectCount("mms.style.getMemberStyleTotalCnt", search);
	}

	/**
	 * 
	 * @Method Name : getMemberStyleList
	 * @author : allen
	 * @date : 2016. 10. 8.
	 * @description : 회원 스타일 리스트 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsStyle> getMemberStyleList(MmsStyleSearch search) {
		return (List<MmsStyle>) dao.selectList("mms.style.getMemberStyleList", search);
	}

	/**
	 * 
	 * @Method Name : deleteStyle
	 * @author : allen
	 * @date : 2016. 10. 9.
	 * @description : 회원 스타일 삭제
	 *
	 * @param search
	 * @throws Exception
	 */
	public void deleteStyle(MmsStyleSearch search) throws Exception {
		// 회원 스타일 상품 삭제
		MmsStyleproduct product = new MmsStyleproduct();
		product.setStoreId(SessionUtil.getStoreId());
		product.setStyleNo(new BigDecimal(search.getStyleNo()));
//		product.setMemberNo(SessionUtil.getMemberNo());
		dao.delete("mms.style.deleteMemberStyleProduct", product);

		// 회원 스타일 삭제
		MmsStyle style = new MmsStyle();
		style.setStyleNo(new BigDecimal(search.getStyleNo()));
		style.setMemberNo(SessionUtil.getMemberNo());
		dao.deleteOneTable(style);
	}

	/**
	 * 
	 * @Method Name : getMemberStyleDetail
	 * @author : allen
	 * @date : 2016. 10. 9.
	 * @description : 회원 스타일 상세
	 *
	 * @param search
	 * @return
	 */
	public MmsStyle getMemberStyleDetail(MmsStyleSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
//		search.setMemberNo(SessionUtil.getMemberNo());
		return (MmsStyle) dao.selectOne("mms.style.getMemberStyleDetail", search);
	}

	/**
	 * 
	 * @Method Name : getMainStyleList
	 * @author : allen
	 * @date : 2016. 10. 9.
	 * @description : 스타일 메인의 리스트
	 *
	 * @param search
	 * @return
	 */
	public List<MmsStyle> getMainStyleList(MmsStyleSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return (List<MmsStyle>) dao.selectList("mms.style.getMainStyleList", search);
	}

	/**
	 * 
	 * @Method Name : updateStyleLikeCnt
	 * @author : allen
	 * @date : 2016. 10. 9.
	 * @description : 스타일 좋아요 Cnt 업데이트
	 *
	 * @param mmsStyle
	 */
	public void updateStyleLikeCnt(MmsStyle mmsStyle) {
		dao.update("mms.style.updateStyleLikeCnt", mmsStyle);
	}

	public void insertMemberStyle(MmsStyle mmsStyle) throws Exception {
		dao.insertOneTable(mmsStyle);
	}

	public void updateMemberStyle(MmsStyle mmsStyle) throws Exception {
		dao.updateOneTable(mmsStyle);
	}

	public void deleteMemberStyleproduct(MmsStyleproduct mmsStyleproduct) throws Exception {
		dao.delete("mms.style.deleteMemberStyleproduct", mmsStyleproduct);
	}

	public void insertMemberStyleproduct(List<MmsStyleproduct> mmsStyleproduct) throws Exception {
		for (MmsStyleproduct mmsStyleproduct2 : mmsStyleproduct) {
			mmsStyleproduct2.setInsId(SessionUtil.getLoginId());
			dao.insert("mms.style.insertMmsStyleProduct", mmsStyleproduct2);
		}
	}

	/**
	 * 
	 * @Method Name : insertStyleLike
	 * @author : allen
	 * @date : 2016. 10. 31.
	 * @description : 스타일 좋아요 저장
	 *
	 * @param mmsStyle
	 * @throws Exception
	 */
	public void insertStyleLike(MmsStylelike mmsStylelike) throws Exception {
		dao.insertOneTable(mmsStylelike);
	}
	
	/**
	 * 
	 * @Method Name : deleteStyleLike
	 * @author : allen
	 * @date : 2016. 10. 31.
	 * @description : 스타일좋아요 삭제
	 *
	 * @param mmsStylelike
	 * @throws Exception
	 */
	public void deleteStyleLike(MmsStylelike mmsStylelike) throws Exception {
		dao.deleteOneTable(mmsStylelike);
	}
}
