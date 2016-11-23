package gcp.pms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsOrderproduct;
import gcp.pms.model.PmsCategoryrating;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsReviewrating;
import gcp.pms.model.search.PmsProductReviewSearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

@Service("productReviewService")
public class ProductReviewService extends BaseService {
	/**
	 * 
	 * @Method Name : getReviewList
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품평 목록 조회
	 *
	 * @param search
	 * @return
	 */
	public List<PmsReview> getReviewList(PmsProductReviewSearch search) {
		return (List<PmsReview>) dao.selectList("pms.review.getProductReviewList", search);
	}

	/**
	 * 
	 * @Method Name : getReviewDetail
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품평 상세 조회
	 *
	 * @param dto
	 * @return
	 */
	public PmsReview getReviewDetail(PmsReview dto) {
		PmsReview detail = (PmsReview) dao.selectOne("pms.review.getPrdReviewDetail", dto);

		/*if(detail != null){
			MmsMember param = new MmsMember();
			param.setMemberNo(detail.getMemberNo());
			MmsMember mem = (MmsMember) dao.selectOne("mms.member.getMemberInfo",param);
			detail.setMmsMember(mem);
		}*/
		return detail;
	}

	public void updateReview(List<PmsReview> review) throws Exception {
		for (PmsReview pmsReview : review) {
			pmsReview.setUpdId(SessionUtil.getLoginId());
			dao.updateOneTable(pmsReview);
		}
	}

	/**
	 * 상품평 상세 수정
	 * 
	 * @Method Name : update
	 * @author : roy
	 * @date : 2016. 8. 3.
	 * @description : 상품평 상세 내용을 수정한다.
	 *
	 * @param review
	 * @throws Exception
	 */
	public int updatePmsReview(PmsReview review) throws Exception {
		// clob 설정
		PmsReview clob = new PmsReview();
		clob.setDetail(review.getDetail());
		clob.setStoreId(SessionUtil.getStoreId());
		clob.setReviewNo(review.getReviewNo());
		clob.setProductId(review.getProductId());

		review.setDetail(" ");

		// 이미지 temp to real
		if (CommonUtil.isNotEmpty(review.getImg1())) {
			review.setImg1(FileUploadUtil.moveTempToReal(review.getImg1()));
		}
		if (CommonUtil.isNotEmpty(review.getImg2())) {
			review.setImg2(FileUploadUtil.moveTempToReal(review.getImg2()));
		}
		if (CommonUtil.isNotEmpty(review.getImg3())) {
			review.setImg3(FileUploadUtil.moveTempToReal(review.getImg3()));
		}

		dao.updateOneTable(review);

		dao.update("pms.review.updateClob", clob);

		return 1;
	}

	/**
	 * 상품상세 상품평 평점정보 조회
	 * 
	 * @Method Name : getProductReviewRagingAvgByProductId
	 * @author : eddie
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @return
	 */
	public PmsReview getProductReviewRagingAvgByProductId(PmsProductReviewSearch search) {
		
		PmsReview review = new PmsReview();
		
		// 평점정보(총평점, 품질, 포장, 배송 ) 
		List<PmsReview> ratingAvgList = (List<PmsReview>) dao.selectList("pms.review.getProductReviewAvgByRatingId", search);

		review.setRatingAvgList(ratingAvgList);

		//전체평점
		int total = (Integer) dao.selectOne("pms.review.getProductReviewAvg", search);
		review.setTotalRatingAvg(new BigDecimal(total));
		return review;
	}

	/**
	 * 상품의 상품평 목록 조회
	 * 
	 * @Method Name : getProductReviewListByProductId
	 * @author : eddie
	 * @date : 2016. 9. 7.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsReview> getProductReviewListByProductId(PmsProductReviewSearch search) {

		List<PmsReview> reviewList = (List<PmsReview>) dao.selectList("pms.review.getProductReviewListByProductId", search);
		return reviewList;
	}

	/**
	 * 상품의 상품평 가능한 목록 조회
	 * 
	 * @Method Name : getReviewAbleProductlist
	 * @author : roy
	 * @date : 2016. 9. 12.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsOrderproduct> getReviewAbleProductlist(PmsProductReviewSearch search) {

		List<OmsOrderproduct> productList = (List<OmsOrderproduct>) dao.selectList("pms.review.getReviewAbleProductlist", search);
		return productList;
	}

	/**
	 * 상품평 등록
	 * 
	 * @Method Name : insert
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평을 등록한다.
	 *
	 * @param review
	 * @throws Exception
	 */
	public BigDecimal insertPmsReview(PmsReview review) throws Exception {

		// clob 설정
		PmsReview clob = new PmsReview();
		clob.setDetail(review.getDetail());
		clob.setStoreId(SessionUtil.getStoreId());
		clob.setProductId(review.getProductId());

		review.setDetail(" ");

		// 이미지 temp to real
		if (CommonUtil.isNotEmpty(review.getImg1())) {
			review.setImg1(FileUploadUtil.moveTempToReal(review.getImg1()));
		}
		if (CommonUtil.isNotEmpty(review.getImg2())) {
			review.setImg2(FileUploadUtil.moveTempToReal(review.getImg2()));
		}
		if (CommonUtil.isNotEmpty(review.getImg3())) {
			review.setImg3(FileUploadUtil.moveTempToReal(review.getImg3()));
		}

		dao.insertOneTable(review);

		clob.setReviewNo((BigDecimal) dao.selectOne("pms.review.getReviewNo", review));

		dao.update("pms.review.updateClob", clob);

		return clob.getReviewNo();
	}

	/**
	 * 상품평 삭제
	 * 
	 * @Method Name : delete
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평을 삭제한다.
	 *
	 * @param review
	 * @throws Exception
	 */
	public int deletePmsReview(PmsReview review) throws Exception {
		// 상품평 별점 삭제
		PmsReviewrating reviewRating = new PmsReviewrating();
		reviewRating.setStoreId(SessionUtil.getStoreId());
		reviewRating.setReviewNo(review.getReviewNo());
		reviewRating.setProductId(review.getProductId());
		dao.delete("pms.review.deleteReviewrating", reviewRating);

		return dao.deleteOneTable(review);
	}

	/**
	 * 상품평 별점 등록
	 * 
	 * @Method Name : insertPmsReviewrating
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평 점수를 등록한다.
	 *
	 * @param reviewRating
	 * @throws Exception
	 */
	public void insertPmsReviewrating(PmsReviewrating reviewRating) throws Exception {
		dao.insertOneTable(reviewRating);
	}

	/**
	 * 상품평 별점 수정
	 * 
	 * @Method Name : updatePmsReviewrating
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평 점수를 수정한다.
	 *
	 * @param reviewRating
	 * @throws Exception
	 */
	public void updatePmsReviewrating(PmsReviewrating reviewRating) throws Exception {
		dao.updateOneTable(reviewRating);
	}

	/**
	 * 상품평 별점 삭제
	 * 
	 * @Method Name : deletePmsReviewrating
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평 점수를 삭제한다.
	 *
	 * @param reviewRating
	 * @throws Exception
	 */
	public void deletePmsReviewrating(PmsReviewrating reviewRating) throws Exception {
		dao.delete("pms.review.deleteReviewrating", reviewRating);
	}
	
	/**
	 * 상품 평점 카테고리 리스트 조회
	 * 
	 * @Method Name : updatePmsReviewrating
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description : 상품평 점수를 수정한다.
	 *
	 * @param reviewRating
	 * @throws Exception
	 */
	public List<PmsCategoryrating> getReviewIdList(PmsProductReviewSearch search) throws Exception {
		return (List<PmsCategoryrating>) dao.selectList("pms.review.getReviewIdList", search);
	}

	/**
	 * 회원의 상품평 목록 조회
	 * 
	 * @Method Name : getProductReviewListByMemberNo
	 * @author : roy
	 * @date : 2016. 9. 13.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsReview> getProductReviewListByMemberNo(PmsProductReviewSearch search) {

		List<PmsReview> reviewList = (List<PmsReview>) dao.selectList("pms.review.getProductReviewListByMemberNo", search);
		return reviewList;
	}

	/**
	 * 회원의 상품평 목록 COUNT
	 * 
	 * @Method Name : getProductReviewCountByMemberNo
	 * @author : roy
	 * @date : 2016. 9. 19.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public PmsReview getProductReviewCountByMemberNo(PmsProductReviewSearch search) {

		PmsReview review = (PmsReview) dao.selectOne("pms.review.getProductReviewCountByMemberNo", search);
		return review;
	}

	/**
	 * 상품평 최초 등록 여부 판단
	 * 
	 * @Method Name : getFirstReview
	 * @author : roy
	 * @date : 2016. 9. 19.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public Boolean getFirstReview(PmsReview search) {

		int first = (int) dao.selectOne("pms.review.getFirstReview", search);
		if (first > 0) {
			return true;
		} else {
			return false;
		}

	}

	public List<PmsReview> selectPointTarget(String storeId) {
		return (List<PmsReview>) dao.selectList("pms.review.selectPointTarget", storeId);
	}

	public List<PmsReview> gerPointTargetFirstReviews(String storeId) {
		return (List<PmsReview>) dao.selectList("pms.review.gerPointTargetFirstReviews", storeId);
	}
}
