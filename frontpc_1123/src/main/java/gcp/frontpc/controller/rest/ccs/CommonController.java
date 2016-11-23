package gcp.frontpc.controller.rest.ccs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gcp.ccs.model.CcsBusinessinquiry;
import gcp.ccs.model.CcsBusinessinquirycategory;
import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.BusinessinquiryService;
import gcp.ccs.service.CommonService;
import gcp.ccs.service.OffshopService;
import gcp.ccs.service.PopupNoticeService;
import gcp.common.util.FoSessionUtil;
import gcp.external.kainess.vo.GviaDistrictVo;
import gcp.external.kainess.vo.GviaPostSearchNewVo;
import gcp.external.model.TmsQueue;
import gcp.external.model.search.AddressSearch;
import gcp.external.service.AddressService;
import gcp.external.service.TmsService;
import gcp.frontpc.common.util.BarcodeUtil;
import gcp.mms.service.ChildrenService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.FileInfo;
import intune.gsf.model.FileMeta;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;


@RestController
@RequestMapping("api/ccs/common")
public class CommonController {

	private static final Logger	logger	= LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CommonService		commonService;

	@Autowired
	private OffshopService		offshopService;
	
	@Autowired
	private AddressService		addressService;

	@Autowired
	private PopupNoticeService	popupService;

	@Autowired
	private BusinessinquiryService	businessinquiryService;

	@Autowired
	private ChildrenService			childrenService;

	@Autowired
	private TmsService				tmsService;

	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 오프매장 시/도정보 조회
	 * 
	 * @Method Name : getOffshopAreaDiv2List
	 * @author : eddie
	 * @date : 2016. 8. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pickup/areadiv1", method = RequestMethod.POST)
	public List<CcsOffshop> getOffshopAreaDiv1List(@RequestBody CcsOffshopSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return offshopService.getOffshopAreaDiv1List(search);
	}

	/**
	 * 오프매장 시/도 정보로 시/군/구정보 조회
	 * 
	 * @Method Name : getOffshopAreaDiv2List
	 * @author : eddie
	 * @date : 2016. 8. 30.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pickup/areadiv2", method = RequestMethod.POST)
	public List<CcsOffshop> getOffshopAreaDiv2List(@RequestBody CcsOffshopSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return offshopService.getOffshopAreaDiv2List(search);
	}

	/**
	 * 픽업매장 단품ID, 시도/시군구 정보로 지점 목록 조회
	 * 
	 * @Method Name : getOffshopList2
	 * @author : eddie
	 * @date : 2016. 9. 2.
	 * @description :
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pickup/shoplist", method = RequestMethod.POST)
	public List<CcsOffshop> getOffshopList2(@RequestBody CcsOffshopSearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return offshopService.getOffshopList2(search);
	}

	/**
	 * 
	 * @Method Name : getDistrictList
	 * @author : allen
	 * @date : 2016. 8. 24.
	 * @description : 시도 / 시군군 검색
	 *
	 * @param request
	 * @param search searchFlag - A:시도, B:시군구 searchStr - '0000000000' 시도 전체 조회
	 * @return
	 */
	@RequestMapping(value = "/districtList", method = RequestMethod.POST)
	public List<GviaDistrictVo> getDistrictList(HttpServletRequest request, @RequestBody AddressSearch search) {
		List<GviaDistrictVo> gviaDistrictVoList = addressService.getDistrictInfo(search);
		return gviaDistrictVoList;
	}

	/**
	 * 
	 * @Method Name : getAddressList
	 * @author : allen
	 * @date : 2016. 8. 24.
	 * @description : 주소 검색
	 *
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/addressList", method = RequestMethod.POST)
	public List<GviaPostSearchNewVo> getAddressList(HttpServletRequest request, @RequestBody AddressSearch search) {
		List<GviaPostSearchNewVo> list = addressService.getSearchAddress(search);
		return list;
	}

	/**
	 * 바코드
	 * 
	 * @param bacodeTag
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/barcode/{bacodeTag}", method = RequestMethod.GET)
	public void barcodeProcess(@PathVariable String bacodeTag, HttpServletResponse response) throws Exception {
		BarcodeUtil.createBarcode(response, bacodeTag);
	}

	/**
	 * 
	 * @Method Name : popupList
	 * @author : allen
	 * @date : 2016. 8. 24.
	 * @description : 주소 검색
	 *
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/popupList", method = RequestMethod.POST)
	public List<CcsPopup> popupList(HttpServletRequest request, @RequestBody CcsPopup search) {
		search.setPopupTypeCd("POPUP_TYPE_CD.FRONT");
		search.setStoreId(SessionUtil.getStoreId());

		if (FoSessionUtil.isMobile(request)) { 		// 모바일 팝업
			search.setMobileDisplayYn("Y");
		} else {								// PC 팝업
			search.setPcDisplayYn("Y");
		}

		List<CcsPopup> list = popupService.selectOneFo(search);
		return list;
	}

	/**
	 * 
	 * @Method Name : businessInquiryInsert
	 * @author : roy
	 * @date : 2016. 9. 20.
	 * @description : 업체 상담 신청서 등록
	 *
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/alliance/insert", method = RequestMethod.POST)
	public Map<String, String> businessInquiryInsert(@RequestBody CcsBusinessinquiry businessInquiry, HttpServletRequest request)
			throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		businessInquiry.setStoreId(SessionUtil.getStoreId());

		if (CommonUtil.isEmpty(businessInquiry.getDetail())) {
			result.put(BaseConstants.RESULT_MESSAGE, "내용을 입력하지 않으셨습니다.");
		} else if (CommonUtil.isEmpty(businessInquiry.getName())) {
			result.put(BaseConstants.RESULT_MESSAGE, "업체명을 입력하지 않으셨습니다.");
		} else {
			try {
				BigDecimal BusinessInquiryNo = businessinquiryService.insert(businessInquiry);

				// 입정 상담 카테고리 등록
				CcsBusinessinquirycategory category = new CcsBusinessinquirycategory();
				category.setStoreId(SessionUtil.getStoreId());
				category.setBusinessInquiryNo(BusinessInquiryNo);
				for (CcsBusinessinquirycategory list : businessInquiry.getCcsBusinessinquirycategorys()) {
					category.setCategoryId(list.getCategoryId());
					businessinquiryService.insertCategory(category);
				}
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
			} catch (Exception e) {
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
			}
		}

		return result;
	}
	

	@RequestMapping(value = "/uploadImage")
	@ResponseBody
	public FileInfo imgFileUpload(MultipartHttpServletRequest req) throws Exception {

		String referer = req.getHeader("referer");

		FileInfo fileInfo = new FileInfo();
		fileInfo.setReferer(referer);
		String uploadedPath = null;
		FileMeta uploadInfo = null;
		try {
			uploadInfo = commonService.imgFileUpload2(req, fileInfo);
		} catch (Exception e) {
			e.printStackTrace();

		}
		fileInfo.setFullPath(uploadInfo.getUploadedFilePath());

		return fileInfo;
	}

	/**
	 * 
	 * @Method Name : mobileAppSend
	 * @author : roy
	 * @date : 2016. 11. 03.
	 * @description : 모바일 앱 링크 받기
	 *
	 * @param request
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/mobileApp/send/{phone}", method = RequestMethod.GET)
	public Map<String, String> mobileAppSend(@PathVariable String phone, HttpServletResponse response) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);


		try {
			if (CommonUtil.isNotEmpty(phone)) {
				TmsQueue tmsQueue = new TmsQueue();

				tmsQueue.setMsgCode("135");
				tmsQueue.setToPhone(phone.replaceAll("-", ""));
				tmsQueue.setSubject("모바일 앱 링크 발송.");
				tmsQueue.setMap1(Config.getString("mobile.app.url"));

				tmsService.sendTmsSmsQueue(tmsQueue);
				result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
			}
			
		} catch (Exception e) {
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		}
		return result;
	}
	
	
	/**
	 * 캐쉬 삭제
	 * @Method Name : deleteCache
	 * @author : intune
	 * @date : 2016. 11. 10.
	 * @description : 
	 *
	 * @param request
	 */
	@RequestMapping(value="/delete/cache", method = RequestMethod.GET)
	public void deleteCache(HttpServletRequest request){
	
		logger.info("======================================");
		logger.info("cache_name:"+request.getParameter("name"));
		logger.info("======================================");

		Cache cache = cacheManager.getCache(request.getParameter("name"));
		cache.clearStatistics();
	}
}
