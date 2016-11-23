package gcp.admin.controller.rest.oms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsSite;
import gcp.oms.model.OmsUploadconf;
import gcp.oms.model.search.OmsUploadexcelSearch;
import gcp.oms.service.UploadConfService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/uploadconf")
public class UploadConfController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private UploadConfService	uploadConfService;

	/**
	 * @Method Name : getExternalSiteList
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 외부몰, 중국몰 사이트 정보
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/siteList", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsSite> getExternalSiteList() throws Exception {
		return uploadConfService.getExternalSiteList(SessionUtil.getStoreId());
	}

	/**
	 * @Method Name : getUploadConfList
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 업로드 설정 조회
	 *
	 * @param ous
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public List<OmsUploadconf> getUploadConfList(@RequestBody OmsUploadexcelSearch ous) throws Exception {
		ous.setStoreId(SessionUtil.getStoreId());
		List<OmsUploadconf> list = uploadConfService.getUploadConfList(ous);

		return list;
	}

	/**
	 * @Method Name : deleteEvent
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 업로드 설정 삭제
	 *
	 * @param confList
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteEvent(@RequestBody List<OmsUploadconf> confList) throws Exception {
		uploadConfService.deleteUploadconf(confList);
	}

	/**
	 * @Method Name : getUploadConfDetail
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 업로드 설정 상세
	 *
	 * @param ous
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/detail/{siteId}", method = RequestMethod.GET)
	public OmsUploadconf getUploadConfDetail(@PathVariable("siteId") String siteId) throws Exception {
		logger.debug("siteId: " + siteId);
		OmsUploadexcelSearch ous = new OmsUploadexcelSearch();
		ous.setStoreId(SessionUtil.getStoreId());
		ous.setSiteId(siteId);

		return uploadConfService.getUploadConfDetail(ous);
	}

	/**
	 * @Method Name : updateUploadconf
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 업로드 설정 변경
	 *
	 * @param ouc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/update", method = RequestMethod.POST)
	public String updateUploadconf(@RequestBody OmsUploadconf ouc) throws Exception {
		ouc.setStoreId(SessionUtil.getStoreId());

		return uploadConfService.updateUploadconf(ouc);
	}

	/**
	 * @Method Name : getExistCount
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 업로드 설정 중복 체크
	 *
	 * @param ouc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/checkDup", method = RequestMethod.POST)
	public String getExistCount(@RequestBody OmsUploadconf ouc) throws Exception {
		ouc.setStoreId(SessionUtil.getStoreId());

		return uploadConfService.getExistCount(ouc);
	}

	/**
	 * @Method Name : insertUploadconf
	 * @author : peter
	 * @date : 2016. 10. 15.
	 * @description : 업로드 설정 신규 등록
	 *
	 * @param ouc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/insert", method = RequestMethod.POST)
	public String insertUploadconf(@RequestBody OmsUploadconf ouc) throws Exception {
		ouc.setStoreId(SessionUtil.getStoreId());

		return uploadConfService.insertUploadconf(ouc);
	}
}
