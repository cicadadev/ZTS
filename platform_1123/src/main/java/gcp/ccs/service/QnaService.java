package gcp.ccs.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.search.CcsInquirySearch;
import gcp.external.model.TmsQueue;
import gcp.external.service.TmsService;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.search.PmsProductQnaSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 
 * @Pagckage Name : gcp.mms.service
 * @FileName : QnaService.java
 * @author : dennis
 * @date : 2016. 4. 20.
 * @description : Qna Service
 */
@Service
public class QnaService extends BaseService {

	@Autowired
	private TmsService tmsService;

	/**
	 * 
	 * @Method Name : selectQnaList
	 * @author : emily
	 * @date : 2016. 6. 1.
	 * @description : 문의관리 목록 조회
	 * 
	 * @param qnaSerch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsInquiry> selectQnaList(CcsInquirySearch qnaSerch) {
		return (List<CcsInquiry>) dao.selectList("ccs.qna.getQnaList", qnaSerch);
	}

	/**
	 * 
	 * @Method Name : selectQna
	 * @author : emily
	 * @date : 2016. 6. 3.
	 * @description : 문의 상세 조회
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	public CcsInquiry selectInquiry(CcsInquiry qna) throws Exception {

		CcsInquiry qnaInfo = (CcsInquiry) dao.selectOne("ccs.qna.getInquiryDetail",qna);
		
		return qnaInfo;
	}


	/**
	 * 
	 * @Method Name : updateQna
	 * @author : emily
	 * @date : 2016. 6. 3.
	 * @description : 문의 등록 & 수정
	 *
	 * @param qna
	 * @return
	 */
	public String saveInquiry(CcsInquiry qna) throws Exception {
		if(CommonUtil.isNotEmpty(qna.getInquiryNo())){
//			 dao.update("ccs.qna.updateInquiry", qna);
			// clob 설정
			CcsInquiry clob = new CcsInquiry();
			clob.setDetail(qna.getDetail());
			clob.setAnswer(qna.getAnswer());
			clob.setStoreId(SessionUtil.getStoreId());
			clob.setInquiryNo(qna.getInquiryNo());

			qna.setDetail(" ");
			qna.setAnswer(" ");
			qna.setUpdId(SessionUtil.getLoginId());
			qna.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(qna);
			dao.update("ccs.qna.updateClob", clob);
		}else{
			// clob 설정
			CcsInquiry clob = new CcsInquiry();
			clob.setDetail(qna.getDetail());
			clob.setAnswer(qna.getAnswer());
			clob.setStoreId(SessionUtil.getStoreId());

			qna.setDetail(" ");
			qna.setAnswer(" ");
			dao.insertOneTable(qna);

			clob.setInquiryNo((BigDecimal) dao.selectOne("ccs.qna.getInquiryNo", qna));
			dao.update("ccs.qna.updateClob", clob);
		}
		return String.valueOf(qna.getInquiryNo());
	}
	
	/**
	 * 문의 답변 등록
	 * @Method Name : saveAnswer
	 * @author : emily
	 * @date : 2016. 7. 14.
	 * @description : 
	 *
	 * @param qna
	 * @throws Exception
	 */
	public void saveAnswer(CcsInquiry qna) throws Exception{
//		dao.update("ccs.qna.updateAnswer", qna);

		// clob 설정
		CcsInquiry clob = new CcsInquiry();
		clob.setAnswer(qna.getAnswer());
		clob.setStoreId(SessionUtil.getStoreId());
		clob.setInquiryNo(qna.getInquiryNo());

		qna.setAnswer(" ");
		qna.setAnswerDt(BaseConstants.SYSDATE);
		qna.setAnswerId(SessionUtil.getLoginId());
		qna.setInquiryStateCd("INQUIRY_STATE_CD.COMPLETE");// 답변완료
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		dao.updateOneTable(qna);
		dao.update("ccs.qna.updateClob", clob);

		CcsInquiry qnaDetail = (CcsInquiry) dao.selectOne("ccs.qna.getInquiryDetail", qna);

		TmsQueue tmsQueue = new TmsQueue();
		if (qnaDetail.getSmsYn().equals("Y")) {
			tmsQueue.setToId(qnaDetail.getInsId());
			String phone = qnaDetail.getPhone().replaceAll("-", ""); // 하이픈 제거
			tmsQueue.setMsgCode("129");
			tmsQueue.setToPhone(phone);
			tmsQueue.setSubject("문의완료 되었습니다.");

			tmsService.sendTmsSmsQueue(tmsQueue);

		}
		if (qnaDetail.getEmailYn().equals("Y")) {
			tmsQueue.setMsgCode("015");
			tmsQueue.setToEmail(qnaDetail.getEmail());
			tmsQueue.setSubject("문의완료 되었습니다.");
			tmsQueue.setMap1(qnaDetail.getMemberName());
			tmsQueue.setMap2(qnaDetail.getInquiryTypeName());
			tmsQueue.setMap3(qnaDetail.getTitle());
			tmsQueue.setMap4(DateUtil.convertFormat(qnaDetail.getInsDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_7));
			tmsQueue.setMapContent2(qnaDetail.getDetail());
			tmsQueue.setMapContent(clob.getAnswer());
			tmsQueue.setMap7(DateUtil.convertFormat(qnaDetail.getAnswerDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_7));
//			tmsQueue.setMapContent("문의 완료 되었습니다.");

			tmsService.sendTmsEmailQueue(tmsQueue);
		}


	}
	
	/**
	 * 문의 확인
	 * @Method Name : updateConfirm
	 * @author : emily
	 * @date : 2016. 7. 14.
	 * @description : 
	 *
	 * @param qna
	 * @throws Exception
	 */
	public void updateConfirm(CcsInquiry qna) throws Exception{

		qna.setConfirmId(SessionUtil.getLoginId()); // 확인자
		qna.setConfirmDt(BaseConstants.SYSDATE);// 확인일
		qna.setInquiryStateCd("INQUIRY_STATE_CD.ANSWER");// 답변중
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		dao.updateOneTable(qna);

//		dao.update("ccs.qna.updateConfirm",qna );
	}
	
//	/**
//	 * 
//	 * @Method Name : updateInquiryState
//	 * @author : emily
//	 * @date : 2016. 7. 14.
//	 * @description : 문의 상태 수정 
//	 *
//	 * @param qna
//	 * @throws Exception
//	 */
//	public void updateInquiryState(CcsInquiry qna) throws Exception{
//		dao.update("ccs.qna.updateInquiryState",qna );
//	}
	
	/**
	 * 
	 * @Method Name : getProductQnaList
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품QNA 목록 조회
	 *
	 * @param qnaSerch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProductqna> getProductQnaList(PmsProductQnaSearch search) {
		return (List<PmsProductqna>) dao.selectList("ccs.qna.getProductQnaList", search);
	}

	/**
	 * 
	 * @Method Name : getProductQnaDetail
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품QNA 상세 조회
	 *
	 * @param qna
	 * @return
	 */
	public PmsProductqna getProductQnaDetail(PmsProductqna qna) {
		PmsProductqna detail = (PmsProductqna) dao.selectOne("ccs.qna.getProductQnaDetail", qna); 
		
/*		if(detail != null){
			MmsMember param = new MmsMember();
			param.setMemberNo(detail.getMemberNo());
			MmsMember mem = (MmsMember) dao.selectOne("mms.member.getMemberInfo",param);
			detail.setMmsMember(mem);
		}*/
		return detail;
	}

	/**
	 * 
	 * @Method Name : updateProductQna
	 * @author : emily
	 * @date : 2016. 6. 12.
	 * @description : 상품문의 수정
	 *
	 * @param qna
	 * @return
	 * @throws Exception
	 */
	public void updatePrdQna(PmsProductqna qna) throws Exception {
		// clob 설정
		PmsProductqna clob = new PmsProductqna();
		clob.setDetail(qna.getDetail());
		clob.setAnswer(qna.getAnswer());
		clob.setStoreId(SessionUtil.getStoreId());
		clob.setProductQnaNo(qna.getProductQnaNo());

		qna.setConfirmId(SessionUtil.getLoginId()); // 확인자
		qna.setConfirmDt(BaseConstants.SYSDATE);// 확인일
		qna.setDetail(" ");
		qna.setAnswer(" ");
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		dao.updateOneTable(qna);

		dao.update("ccs.qna.updatePrdClob", clob);
	}

	/**
	 * 
	 * @Method Name : saveQnaConfirm
	 * @author : emily
	 * @date : 2016. 7. 15.
	 * @description : 상품QnA 문의 확인
	 *
	 * @param qna
	 * @throws Exception
	 */
	public void updatePrdQnaConfirm(PmsProductqna qna) throws Exception {

		qna.setConfirmDt(BaseConstants.SYSDATE);
		qna.setConfirmId(SessionUtil.getLoginId());
		qna.setProductQnaStateCd("PRODUCT_QNA_STATE_CD.ANSWER");//답변중
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());
		dao.updateOneTable(qna);

	}

	/**
	 * 
	 * @Method Name : updatePrdQnaState
	 * @author : emily
	 * @date : 2016. 7. 15.
	 * @description : 상품QnA답변완료
	 *
	 * @param qna
	 * @throws Exception
	 */
	public void updatePrdQnaAnswer(PmsProductqna qna) throws Exception {

		// clob 설정
		PmsProductqna clob = new PmsProductqna();
		clob.setAnswer(qna.getAnswer());
		clob.setStoreId(SessionUtil.getStoreId());
		clob.setProductQnaNo(qna.getProductQnaNo());

		qna.setAnswerDt(BaseConstants.SYSDATE);
		qna.setAnswerId(SessionUtil.getLoginId());
		qna.setProductQnaStateCd("PRODUCT_QNA_STATE_CD.COMPLETE");// 답변완료
		qna.setAnswer(" ");
		qna.setUpdId(SessionUtil.getLoginId());
		qna.setStoreId(SessionUtil.getStoreId());

		dao.updateOneTable(qna);

		dao.update("ccs.qna.updatePrdClob", clob);

	}

	
	/**
	 * 
	 * @Method Name : saveQna
	 * @author : roy
	 * @date : 2016. 8. 24.
	 * @description : 1:1 문의 저장
	 *
	 * @param ccsQna
	 * @return Map<String, String>
	 * @throws Exception
	 */
	public Map<String, String> saveQna(CcsInquiry qna) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		try{
			// clob 설정
			CcsInquiry clob = new CcsInquiry();
			clob.setDetail(qna.getDetail());
			clob.setAnswer(qna.getAnswer());
			clob.setStoreId(SessionUtil.getStoreId());

			qna.setDetail(" ");
			qna.setAnswer(" ");

			// 이미지 temp to real
			if (CommonUtil.isNotEmpty(qna.getImg())) {
				qna.setImg(FileUploadUtil.moveTempToReal(qna.getImg()));
			}

			dao.insertOneTable(qna);

			clob.setInquiryNo((BigDecimal) dao.selectOne("ccs.qna.getInquiryNo", qna));
			dao.update("ccs.qna.updateClob", clob);
			
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		}catch(Exception e){
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			return result;
		}
		return result;

	}

	/**
	 * 상품 문의 등록
	 * 
	 * @Method Name : insertProductQna
	 * @author : eddie
	 * @date : 2016. 8. 26.
	 * @description :
	 *
	 * @param qna
	 * @throws Exception
	 */
	public void insertProductQna(PmsProductqna qna) throws Exception {

		// clob 설정
		PmsProductqna clob = new PmsProductqna();
		clob.setDetail(qna.getDetail());
		clob.setAnswer(qna.getAnswer());
		clob.setStoreId(SessionUtil.getStoreId());

		// 이미지 temp to real
		if (CommonUtil.isNotEmpty(qna.getImg())) {
			qna.setImg(FileUploadUtil.moveTempToReal(qna.getImg()));
		}

		qna.setDetail(" ");
		qna.setAnswer(" ");
		dao.insertOneTable(qna);

		clob.setProductQnaNo((BigDecimal) dao.selectOne("ccs.qna.getProductQnaNo", qna));
		dao.update("ccs.qna.updatePrdClob", clob);
	}

	/**
	 * 상품상세 상품문의 목록 조회
	 * 
	 * @Method Name : getProductQnaListByProductId
	 * @author : eddie
	 * @date : 2016. 8. 29.
	 * @description :
	 *
	 * @return
	 */
	public List<PmsProductqna> getProductQnaListByProductId(PmsProductQnaSearch search) {
		return (List<PmsProductqna>) dao.selectList("ccs.qna.getProductQnaListByProductId", search);
	}
}
