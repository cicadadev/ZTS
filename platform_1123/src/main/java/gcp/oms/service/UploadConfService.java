package gcp.oms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsSite;
import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsUploadconf;
import gcp.oms.model.search.OmsUploadexcelSearch;
import intune.gsf.common.exceptions.ServiceException;

@Service
public class UploadConfService extends BaseService {
	/**
	 * @Method Name : getExternalSiteList
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 외부몰, 중국몰 사이트 정보
	 *
	 * @param storeId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CcsSite> getExternalSiteList(String storeId) throws Exception {
		return (List<CcsSite>) dao.selectList("oms.uploadconf.getExternalSiteList", storeId);
	}

	/**
	 * @Method Name : getUploadConfList
	 * @author : peter
	 * @date : 2016. 9. 23.
	 * @description : 주문업로드 설정 정보 조회
	 *
	 * @param storeId
	 * @param siteId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OmsUploadconf> getUploadConfList(OmsUploadexcelSearch ous) throws Exception {
		return (List<OmsUploadconf>) dao.selectList("oms.uploadconf.getUploadConfList", ous);
	}

	/**
	 * @Method Name : deleteUploadconf
	 * @author : peter
	 * @date : 2016. 9. 27.
	 * @description : 업로드설정 그리스 선택Row 삭제
	 *
	 * @param confList
	 * @throws Exception
	 */
	public void deleteUploadconf(List<OmsUploadconf> confList) throws Exception {
		for (OmsUploadconf confOne : confList) {
			//업로드설정 삭제
			dao.deleteOneTable(confOne);
		}
	}

	/**
	 * @Method Name : getUploadConfDetail
	 * @author : peter
	 * @date : 2016. 9. 27.
	 * @description : 업로드설정 상세
	 *
	 * @param ous
	 * @return
	 * @throws Exception
	 */
	public OmsUploadconf getUploadConfDetail(OmsUploadexcelSearch ous) throws Exception {
		return (OmsUploadconf) dao.selectOne("oms.uploadconf.getUploadConfDetail", ous);
	}

	/**
	 * @Method Name : updateUploadconf
	 * @author : peter
	 * @date : 2016. 9. 27.
	 * @description : 업로드설정 변경
	 *
	 * @param ouc
	 * @return
	 * @throws Exception
	 */
	public String updateUploadconf(OmsUploadconf ouc) throws Exception {
		String siteId = ouc.getSiteId().substring(0, ouc.getSiteId().indexOf("("));
		ouc.setSiteId(siteId);
		dao.updateOneTable(ouc);

		return siteId;
	}

	/**
	 * @Method Name : getExistCount
	 * @author : peter
	 * @date : 2016. 9. 27.
	 * @description : 업로드설정 중복체크
	 *
	 * @param ouc
	 * @return
	 * @throws ServiceException
	 */
	public String getExistCount(OmsUploadconf ouc) throws ServiceException {
		String existYn = "N";
		int dataCnt = (int) dao.selectOne("oms.uploadconf.getExistCount", ouc);
		if (dataCnt > 0) {
			existYn = "Y";
		}
		return existYn;
	}

	/**
	 * @Method Name : insertUploadconf
	 * @author : peter
	 * @date : 2016. 9. 27.
	 * @description : 업로드설정 신규등록
	 *
	 * @param ouc
	 * @return
	 * @throws Exception
	 */
	public String insertUploadconf(OmsUploadconf ouc) throws Exception {
		//속성테이블 insert
		dao.insertOneTable(ouc);

		return ouc.getSiteId();
	}
}
