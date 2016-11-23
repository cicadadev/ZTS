package gcp.frontpc.controller.rest.pms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.service.QnaService;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.custom.PmsOptionvalue;
import gcp.pms.model.search.PmsProductReviewSearch;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.ProductReviewService;
import gcp.pms.service.ProductService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/pms/product")
public class ProductController {
	
	
	private final Log		logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private ProductService productService;

	@Autowired
	private QnaService		qnaService;

	@Autowired
	private ProductReviewService	productReviewService;

	/**
	 * 상품 문의 등록
	 * 
	 * @Method Name : saveProductQna
	 * @author : eddie
	 * @date : 2016. 8. 26.
	 * @description : 상품 상세에서 상품문의를 등록한다.
	 *
	 * @param qna
	 * @throws Exception
	 */
	@RequestMapping(value = "/qna/save", method = RequestMethod.POST)
	public void saveProductQna(@ModelAttribute PmsProductqna qna) throws Exception {

		qna.setMemberNo(SessionUtil.getMemberNo());
		qna.setStoreId(SessionUtil.getStoreId());
		qna.setProductQnaTypeCd("PRODUCT_QNA_TYPE_CD.PRODUCT");// 유형 : 상품문의
		qna.setProductQnaStateCd("PRODUCT_QNA_STATE_CD.ACCEPT");// 상태 : 요청
		qna.setEmailYn("Y");
		qna.setSmsYn("Y");
		qna.setDisplayYn("Y");

		if (CommonUtil.isEmpty(qna.getSecretYn())) {
			qna.setSecretYn("N");//기본값 : 공개글
		}

		qnaService.insertProductQna(qna);
	}

	@RequestMapping(value = "/optionvalue/list", method = RequestMethod.POST)
	public List<PmsOptionvalue> getOptionValueList(@RequestBody PmsProductSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return productService.getOptionValueList(search);
	}

	/**
	 * 선택한 옵션들로 단품번호 가져오기
	 * 
	 * @Method Name : getSaleproductByOptions
	 * @author : eddie
	 * @date : 2016. 8. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saleproduct", method = RequestMethod.POST)
	public PmsSaleproduct getSaleproductByOptions(@RequestBody PmsProductSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return productService.getSaleproductByOptions(search);
	}

	/**
	 * 상품 상세 상품평 평점 조회
	 * 
	 * @Method Name : getProductReviewRagingAvgByProductId
	 * @author : eddie
	 * @date : 2016. 9. 7.
	 * @description :
	 *
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/review/rating/ajax", method = RequestMethod.GET)
	public PmsReview getProductReviewRagingAvgByProductId(PmsProductReviewSearch search, HttpServletRequest request) {

		search.setStoreId(SessionUtil.getStoreId());
		PmsReview review = productReviewService.getProductReviewRagingAvgByProductId(search);
		return review;
	}
}
