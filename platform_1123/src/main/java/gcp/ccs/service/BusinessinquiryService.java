package gcp.ccs.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsBusinessinquiry;
import gcp.ccs.model.CcsBusinessinquirycategory;
import gcp.ccs.model.search.CcsBusinessinquirySearch;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;

/**
 * 
 * @Pagckage Name : gcp.ccs.service
 * @FileName : BusinessinquiryService.java
 * @author : roy
 * @date : 2016. 8. 11.
 * @description : businessinquiry service
 */
@Service
public class BusinessinquiryService extends BaseService {
	/**
	 * 
	 * @Method Name : getBusinessInquiryListInfo
	 * @author : roy
	 * @date : 2016. 8. 2.
	 * @description : 입점상담관리 목록 조회
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CcsBusinessinquiry> getBusinessInquiryListInfo(CcsBusinessinquirySearch search){
		return (List<CcsBusinessinquiry>) dao.selectList("ccs.businessinquiry.getBusinessInquiryListInfo", search);
	}
	

	/**
	 * 
	 * @Method Name : getBusinessInquiryDetail
	 * @author : roy
	 * @date : 2016. 8. 2.
	 * @description : 입점상담관리 정보 상세조회
	 *
	 * @param business
	 * @return
	 */
	public CcsBusinessinquiry getBusinessInquiryDetail(CcsBusinessinquiry businessInquiry) {
		return (CcsBusinessinquiry) dao.selectOne("ccs.businessinquiry.getBusinessInquiryDetail", businessInquiry);
	}

	/**
	 * 
	 * @Method Name : insert
	 * @author : roy
	 * @date : 2016. 9. 20.
	 * @description : 입점상담 등록
	 *
	 * @param business
	 * @return
	 * @throws Exception
	 */
	public BigDecimal insert(CcsBusinessinquiry businessInquiry) throws Exception {
		// 이미지 temp to real
		if (CommonUtil.isNotEmpty(businessInquiry.getImg())) {
			businessInquiry.setImg(FileUploadUtil.moveTempToReal(businessInquiry.getImg()));
		}
		dao.insertOneTable(businessInquiry);
		return (BigDecimal) dao.selectOne("ccs.businessinquiry.getBusinessInquiryNo", businessInquiry);
	}

	/**
	 * 
	 * @Method Name : insertCategory
	 * @author : roy
	 * @date : 2016. 9. 20.
	 * @description : 입점상담 카테고리 등록
	 *
	 * @param business
	 * @return
	 * @throws Exception
	 */
	public void insertCategory(CcsBusinessinquirycategory category) throws Exception {
		dao.insertOneTable(category);
	}

}