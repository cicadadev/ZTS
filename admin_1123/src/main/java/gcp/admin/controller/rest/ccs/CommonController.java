package gcp.admin.controller.rest.ccs;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gcp.admin.common.IPMask;
import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsApplytarget;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsBusinessholiday;
import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsControlchannel;
import gcp.ccs.model.CcsControldevice;
import gcp.ccs.model.CcsControlmembergrade;
import gcp.ccs.model.CcsControlmembertype;
import gcp.ccs.model.CcsExcelUploadResult;
import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.base.BaseCcsCode;
import gcp.ccs.model.base.BaseCcsControl;
import gcp.ccs.model.base.BaseCcsControlchannel;
import gcp.ccs.model.base.BaseCcsControldevice;
import gcp.ccs.model.base.BaseCcsControlmembergrade;
import gcp.ccs.model.base.BaseCcsControlmembertype;
import gcp.ccs.model.base.BaseCcsField;
import gcp.ccs.model.search.CcsApplySearch;
import gcp.ccs.model.search.CcsChannelSearch;
import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.CommonService;
import gcp.ccs.service.OffshopService;
import gcp.ccs.service.UserService;
import gcp.common.util.CodeUtil;
import gcp.common.util.ValidationUtil;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.service.CategoryService;
import gcp.external.kainess.vo.GviaDistrictVo;
import gcp.external.kainess.vo.GviaPostSearchNewVo;
import gcp.external.model.TmsQueue;
import gcp.external.model.search.AddressSearch;
import gcp.external.service.AddressService;
import gcp.external.service.TmsService;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsChildrencard;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsMember;
import gcp.mms.model.search.MmsChildrenSearch;
import gcp.mms.service.ChildrenService;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.service.BrandService;
import gcp.pms.service.PresentService;
import gcp.pms.service.ProductService;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.service.CouponService;
import gcp.sps.service.EventService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.FileDownloader;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseSearchCondition;
import intune.gsf.model.ExcelUploadResult;
import intune.gsf.model.FieldVaidation;
import intune.gsf.model.FileInfo;
import intune.gsf.model.FileMeta;
import intune.gsf.model.Result;

@RestController
@RequestMapping("api/ccs/common")
public class CommonController {

	private static final Logger	logger	= LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CommonService		commonService;

	@Autowired
	private ProductService		productService;

	@Autowired
	private BrandService		brandService;

	@Autowired
	private CategoryService		displayCategoryService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private CouponService		couponService;

	@Autowired
	private PresentService		presentService;

	@Autowired
	private OffshopService		offshopService;

	@Autowired
	private AddressService		addressService;
	
	@Autowired
	private EventService		eventService;

	@Autowired
	private TmsService			tmsService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ChildrenService		childrenService;

	@RequestMapping(value = "/validations", method = RequestMethod.POST)
	public List<FieldVaidation> checkValidationList(@RequestBody List<FieldVaidation> fieldValues) throws Exception {

		for (FieldVaidation m : fieldValues) {
			ValidationUtil.isValid(m);
		}

		return fieldValues;
	}

	@RequestMapping(value = "/validation", method = RequestMethod.POST)
	public FieldVaidation checkValidation(@RequestBody FieldVaidation fieldValue) throws Exception {

		ValidationUtil.isValid(fieldValue);

		return fieldValue;
	}

	/**
	 * validation 체크를 위한 filed 정보 조회
	 * 
	 * @Method Name : field
	 * @author : eddie
	 * @date : 2016. 6. 24.
	 * @description :
	 *
	 * @param fieldValues
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/field", method = RequestMethod.POST)
	public Map<String, BaseCcsField> field(@RequestBody List<FieldVaidation> fieldValues) throws Exception {
		Map<String, BaseCcsField> result = new HashMap<String, BaseCcsField>();

		for (FieldVaidation f : fieldValues) {

			BaseCcsField field = ValidationUtil.getCcsField(f.getFieldCd());
			if (!CommonUtil.isEmpty(field)) {
				result.put(field.getFieldCd(), field);
			}

		}

		return result;
	}

	@RequestMapping(value = "/messages", method = RequestMethod.POST)
	public Map<String, String> getMessages(@RequestBody String[] codes) throws Exception {
		return MessageUtil.getMessages(codes);
	}

	/**
	 * message key 로 message 조회
	 * 
	 * @Method Name : getGridField
	 * @author : Administrator
	 * @date : 2016. 6. 24.
	 * @description :
	 *
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grid/field", method = RequestMethod.POST)
	public Map<String, String> getGridField(@RequestBody String[] codes) throws Exception {
		Map<String, String> fields = MessageUtil.getMessages(codes);
		return fields;
	}

	@RequestMapping(value = "/code/value/{cd}", method = RequestMethod.GET)
	public String codegroup(@PathVariable("cd") String cd) {
		return CodeUtil.getCodeName(cd);
	}

	@RequestMapping(value = "/codegroup", method = RequestMethod.GET)
	public List<CcsCode> codegroup(BaseCcsCode code) {
		return CodeUtil.getCodeList(code.getCdGroupCd());
	}

	/**
	 * 
	 * @Method Name : getChannelList
	 * @author : allen
	 * @date : 2016. 5. 10.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/channel/list", method = RequestMethod.POST)
	public List<CcsChannel> getChannelList(HttpServletRequest request, @RequestBody CcsChannelSearch chSearch) throws Exception {
		chSearch.setStoreId(SessionUtil.getStoreId());
		return commonService.getChannelList(chSearch);
	}

	/**
	 * 
	 * @Method Name : getChannelList
	 * @author : allen
	 * @date : 2016. 5. 10.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/channelList", method = RequestMethod.POST)
	public List<CcsChannel> getSimpleChannelList(HttpServletRequest request) throws Exception {
//		return commonService.getSimpleChannelList(((LoginInfo) SessionUtil.getLoginInfo(request)).getStoreId());
		return commonService.getSimpleChannelList(SessionUtil.getStoreId());
	}

	/**
	 * 
	 * @Method Name : getChannelDetail
	 * @author : allen
	 * @date : 2016. 5. 10.
	 * @description :
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/channelDetail", method = RequestMethod.GET)
	public CcsChannel getChannelDetail(HttpServletRequest request, @RequestParam("channelId") String channelId) throws Exception {

		CcsChannel ccsChannel;
		ccsChannel = commonService.getChannelDetail(channelId);
		if (ccsChannel != null) {
			ccsChannel.setGateUrl(Config.getString("channel.gate.url"));
		}
		return ccsChannel;
	}

	@RequestMapping(value = "/channelBusinessList", method = RequestMethod.GET)
	public List<CcsBusiness> getChannelBusinessList(HttpServletRequest request) throws Exception {
		return commonService.getChannelBusinessList(SessionUtil.getStoreId());
	}

	@RequestMapping(value = "/channel/insert", method = RequestMethod.POST)
	public void createChannel(HttpServletRequest request, @RequestBody CcsChannel ccsChannel) throws Exception {
		ccsChannel.setStoreId(SessionUtil.getStoreId());
		commonService.createChannel(ccsChannel);
	}

	@RequestMapping(value = "/channel/save", method = RequestMethod.POST)
	public void updateChannel(HttpServletRequest request, @RequestBody List<CcsChannel> ccsChannels) throws Exception {
		commonService.updateChannel(ccsChannels);
	}

	@RequestMapping(value = "/channel/delete", method = RequestMethod.POST)
	public void deleteCorner(HttpServletRequest request, @RequestBody List<CcsChannel> ccsChannels) throws Exception {
		commonService.deleteChannel(ccsChannels);
	}

	@RequestMapping(value = "/channel/updateChannelStatus", method = RequestMethod.POST)
	public void updateChannelStatus(HttpServletRequest request, @RequestBody CcsChannel ccsChannel) throws Exception {
		commonService.updateChannelStatus(ccsChannel);
	}

	/**
	 * @Method Name : getControlInfo
	 * @author : peter
	 * @date : 2016. 5. 24.
	 * @description : 제어정보
	 *
	 * @param cctl
	 * @return
	 */
	@RequestMapping(value = "/control/popup/detail", method = RequestMethod.POST)
	public BaseCcsControl getControlInfo(@RequestBody CcsControl cctl) {
		//cctl.setStoreId(SessionUtil.getStoreId());
		cctl.setStoreId("1001");
		//logger.debug("hak: " + cctl.getControlNo());
		BaseCcsControl bcc = commonService.getControlInfo(cctl);
		return bcc;
	}

	/**
	 * @Method Name : getControlMemberList
	 * @author : peter
	 * @date : 2016. 5. 24.
	 * @description : 회원 제어정보
	 *
	 * @param ccm
	 * @return
	 */
	@RequestMapping(value = "/control/popup/memberType", method = RequestMethod.POST)
	public List<BaseCcsControlmembertype> getControlMemberTypeList(@RequestBody CcsControlmembertype ccmt) {
		//ccm.setStoreId(SessionUtil.getStoreId());
		ccmt.setStoreId("1001");
		logger.debug("hak_member: " + ccmt.getControlNo());
		List<BaseCcsControlmembertype> bccmtlist = commonService.getControlMemberTypeList(ccmt);
		return bccmtlist;
	}

	@RequestMapping(value = "/control/popup/memberGrade", method = RequestMethod.POST)
	public List<BaseCcsControlmembergrade> getControlMemberGradeList(@RequestBody CcsControlmembergrade ccmg) {
		//ccm.setStoreId(SessionUtil.getStoreId());
		ccmg.setStoreId("1001");
		logger.debug("hak_member: " + ccmg.getControlNo());
		List<BaseCcsControlmembergrade> bccmglist = commonService.getControlMemberGradeList(ccmg);
		return bccmglist;
	}

	/**
	 * @Method Name : getControlChannel
	 * @author : peter
	 * @date : 2016. 5. 24.
	 * @description : 채널 제어정보
	 *
	 * @param ccc
	 * @return
	 */
	@RequestMapping(value = "/control/popup/channel", method = RequestMethod.POST)
	public List<BaseCcsControlchannel> getControlChannel(@RequestBody CcsControlchannel ccc) {
		//ccc.setStoreId(SessionUtil.getStoreId());
		ccc.setStoreId("1001");
		logger.debug("hak_channel: " + ccc.getControlNo());
		List<BaseCcsControlchannel> bccclist = commonService.getControlChannelList(ccc);
		return bccclist;
	}

	/**
	 * @Method Name : getControlDevice
	 * @author : peter
	 * @date : 2016. 5. 24.
	 * @description : 디바이스 제어정보
	 *
	 * @param ccd
	 * @return
	 */
	@RequestMapping(value = "/control/popup/device", method = RequestMethod.POST)
	public List<BaseCcsControldevice> getControlDevice(@RequestBody CcsControldevice ccd) {
		//ccd.setStoreId(SessionUtil.getStoreId());
		ccd.setStoreId("1001");
		logger.debug("hak_device: " + ccd.getControlNo());
		List<BaseCcsControldevice> bccdlist = commonService.getControlDeviceList(ccd);
		return bccdlist;
	}

	@RequestMapping(value = "/apply/insert", method = RequestMethod.POST)
	public int insertApply(@RequestBody CcsApply apply) throws Exception {
		apply.setStoreId(SessionUtil.getStoreId());
		return commonService.insertApply(apply);
	}

	@RequestMapping(value = "/applyTarget/insert", method = RequestMethod.POST)
	public int insertApplyTarget(@RequestBody CcsApplytarget ccsApplytarget) throws Exception {
		ccsApplytarget.setStoreId(SessionUtil.getStoreId());
		return commonService.insertApplyTarget(ccsApplytarget);
	}

	/**
	 * @Method Name : imgFileUpload
	 * @author : peter
	 * @date : 2016. 5. 26.
	 * @description : 이미지 upload
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/imgFileUpload", method = RequestMethod.POST)
	public String imgFileUpload(MultipartHttpServletRequest req) throws Exception {

		String referer = req.getHeader("referer");

		FileInfo fileInfo = new FileInfo();
		fileInfo.setReferer(referer);
		String uploadedPath = null;

		try {
			uploadedPath = commonService.imgFileUpload(req, fileInfo);
		} catch (Exception e) {
			// TODO 파일업로드 AJAX호출하는 소스에서 exception 발생시 메세지 처리 불가하여 'error' 문자열로 에러 구분함
			e.printStackTrace();
			return "[업로드 실패]" + e.getMessage();
		}
		return uploadedPath;
	}

	/**
	 * @Method Name : imgFileUpload2
	 * @author : peter
	 * @date : 2016. 5. 26.
	 * @description : 이미지 upload
	 *                  FileMeta를 리턴
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/imgFileUpload2", method = RequestMethod.POST)
	public FileMeta imgFileUpload2(MultipartHttpServletRequest req) throws Exception {

		String referer = req.getHeader("referer");

		FileInfo fileInfo = new FileInfo();
		fileInfo.setReferer(referer);
		FileMeta uploadInfo = new FileMeta();
		try {
			uploadInfo = commonService.imgFileUpload2(req, fileInfo);
		} catch (Exception e) {
			// TODO 파일업로드 AJAX호출하는 소스에서 exception 발생시 메세지 처리 불가하여 'error' 문자열로 에러 구분함
			e.printStackTrace();
			uploadInfo.setErrorMsg("[업로드 실패]" + e.getMessage());
		}
		return uploadInfo;
	}

	/**
	 * @Method Name : zipFileUpload
	 * @author : roy
	 * @date : 2016. 11. 02.
	 * @description : 압축 upload FileMeta를 리턴
	 *
	 * @param req
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/zipFileUpload", method = RequestMethod.POST)
	public List<FileInfo> zipFileUpload(MultipartHttpServletRequest req) throws Throwable {

		String referer = req.getHeader("referer");

		FileInfo fileInfo = new FileInfo();
		fileInfo.setReferer(referer);
//		String result = null;
		List<FileInfo> result = new ArrayList<FileInfo>();
		try {
			result = commonService.zipFileUpload(req, fileInfo);
		} catch (Exception e) {
			// TODO 파일업로드 AJAX호출하는 소스에서 exception 발생시 메세지 처리 불가하여 'error' 문자열로 에러 구분함
//			result = "[업로드 실패]" + e.getMessage();
		}
		return result;
	}

	@RequestMapping(value = "/ckUpload", method = RequestMethod.POST)
	@ResponseBody
	public void ckUpload(MultipartHttpServletRequest req, HttpServletResponse response,
			@RequestParam MultipartFile upload) throws Exception {

		String callback = req.getParameter("CKEditorFuncNum");

		// referer 조회: upload 경로 생성을 위함.
		String referer = req.getHeader("referer");
		FileInfo fileInfo = new FileInfo();
		fileInfo.setReferer(referer);
		fileInfo.setTempUpload(false);// temp 경로에 업로드하지 않음.

		String basePath =  Config.getString("upload.base.path");
		String fileUrl = basePath + commonService.imgFileUpload(req, fileInfo);
		String replaceUrl = fileUrl.replaceAll("\\\\", "/");
		logger.debug(" \t url >> " + replaceUrl);

		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter outs = response.getWriter();

		
		replaceUrl = Config.getString("image.domain") + replaceUrl.replace(basePath, "");
		
		outs.println("<script type='text/javascript'>");
		outs.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ", '" + replaceUrl + "', '이미지 업로드 완료')");
		outs.println("</script>");

		outs.flush();

//		View view = new AbstractView() {
//			@Override
//			protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
//					throws Exception {
//
//				//TODO jdk1.7 down 주석
//
////				logger.info(" \t url in over method >> " + replaceUrl);
//
//				response.setContentType("text/html; charset=UTF-8");
//				response.setCharacterEncoding("UTF-8");
//				PrintWriter outs = response.getWriter();
//
//				outs.println("<script type='text/javascript'>");
//				outs.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ", '" + replaceUrl + "', '이미지 업로드 완료')");
//				outs.println("</script>");
//
//				outs.flush();
//			}
//		};
	}

	/**
	 * 
	 * @Method Name : downTemplate
	 * @author : allen
	 * @date : 2016. 5. 30.
	 * @description : 엑셀 템플릿 다운로드
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downTemplate", method = RequestMethod.GET)
	public Result downTemplate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("templateName") String templateName) throws Exception {
//		Result rs = FileDownloader.download(response, Config.getString("download.template.path") + "/" + templateName);
		Result rs = FileDownloader.download(response, templateName);
		return rs;
	}

	@RequestMapping(value = "/gridbulkupload/{id}", method = RequestMethod.POST) // insert/update/reserve/price
	public ExcelUploadResult bulkupload(@PathVariable("id") String id, MultipartHttpServletRequest multipartRequest,
			Model model) {
		Iterator<String> files = multipartRequest.getFileNames();

		List resultList = null;
		int totalCnt = 0;
		int failCnt = 0;
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();

		try {
			while (files.hasNext()) {

				MultipartFile mfile = multipartRequest.getFile(files.next());

				Map result = readExcelFile(mfile, id);
				resultList = (List) result.get("resultList");
				totalCnt = (int) result.get("totalCnt");
				errorList = (List<Map<String, String>>) result.get("errorList");
				failCnt = errorList.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("##### grid bulk 에러 : " + e.getMessage());
		}

		if(id.indexOf("coupon") != -1) {
			String[] temp = id.split(",");
			id = temp[0];
		}
		
		CcsExcelUploadResult summary = new CcsExcelUploadResult();
		summary.setResultList(resultList);
		summary.setTotalCnt(totalCnt);
		summary.setFailCnt(failCnt);
		summary.setSuccessCnt(totalCnt - failCnt);
		summary.setErrorList(errorList);

		if (failCnt > 0) {
			List<String> headerNmList = new ArrayList<String>();
			headerNmList.add("에러 데이터");
			headerNmList.add("에러 내용");

			String errFileName = "상품 업로드 에러";
			if ("category".equals(id)) {
				errFileName = "카테고리 업로드 에러";
			} else if ("brand".equals(id)) {
				errFileName = "브랜드 업로드 에러";
			} else if ("coupon".equals(id)) {
				errFileName = "쿠폰 발급 업로드 에러";
			} else if ("holiday".equals(id)) {
				errFileName = "휴일 업로드 에러";
			} else if ("present".equals(id)) {
				errFileName = "사은품 업로드 에러";
			} else if ("carrot".equals(id)) {
				errFileName = "당근 업로드 에러";
			} else if ("deposit".equals(id)) {
				errFileName = "예치금 업로드 에러";
			} else if ("offshop".equals(id)) {
				errFileName = "오프라인매장 업로드 에러";
			} else if ("eventWinner".equals(id)) {
				errFileName = "당첨자 업로드 에러";
			} else if ("childrencard".equals(id)) {
				errFileName = "다자녀카드 업로드 에러";
			} 

			String uploadPath = Config.getString("excel.upload.path.error");
			String excelPath = ExcelUtil.createExcelFile(headerNmList, errorList, uploadPath, errFileName);
			logger.debug(">>>>>> error path : " + excelPath);
			summary.setExcelPath(excelPath);

		}

		return summary;
	}

	/**
	 * 파일 삭제
	 * 
	 * @Method Name : deleteFile
	 * @author : eddie
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param imagepath
	 */
	@RequestMapping(value = "/file/delete", method = { RequestMethod.POST })
	public void deleteFile(@RequestBody FileInfo fileInfo) {

		try {
			FileUploadUtil.deleteFile(fileInfo.getFullPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isVersion2003(MultipartFile file) {
		return file.getName().endsWith(".xls");
	}

	private Workbook createWorkbook(MultipartFile file) throws IOException {
		if (isVersion2003(file)) {
			return new HSSFWorkbook(file.getInputStream());
		} else { // 2007+
			return new XSSFWorkbook(file.getInputStream());
		}
	}

	private Map<String, Object> readExcelFile(MultipartFile file, String id) throws Exception {
		
		// couponId
		String couponId = "";
		if(id.indexOf("coupon") != -1) {
			String[] temp = id.split(",");
			id = temp[0];
			couponId = temp[1];
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();

		String storeId = SessionUtil.getStoreId();

		Workbook wb = createWorkbook(file);

		Sheet sheet = wb.getSheetAt(0);

//		int totalCnt = sheet.getPhysicalNumberOfRows();
		
		int columnCnt = sheet.getRow(1).getPhysicalNumberOfCells();
		
		Iterator<Row> itr = sheet.iterator();
		
	    List<String> dupList = new ArrayList<String>();
		while (itr.hasNext()) {
			Row row = itr.next();
			if (row != null) {
				int rownum = row.getRowNum();
				
				if (rownum > 1) {
					String msg = "";
					Iterator<Cell> celItr = row.iterator();
					// 헤더 타이틀 개수로 컬럼값 정의
					String[] column = new String[columnCnt];
					
					int i = 0;
					while (celItr.hasNext()) {
						Cell c1 = celItr.next();
						if (!CommonUtil.isEmpty(c1)) {
							logger.debug("type : " + c1.getCellType());
							if (Cell.CELL_TYPE_STRING == c1.getCellType()) {
								column[i] = (c1.getStringCellValue()).replaceAll("\\p{Z}", "");
							} else if (Cell.CELL_TYPE_NUMERIC == c1.getCellType()) {
								if (HSSFDateUtil.isCellDateFormatted(c1)) {
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
									column[i] = (formatter.format(c1.getDateCellValue())).replaceAll("\\p{Z}", "");
								} else {
									// 숫자 7자리수 지수변환 문제로 인해 수정
									DataFormatter fmt = new DataFormatter(); 
									column[i] = (fmt.formatCellValue(c1).toString()).replaceAll("\\p{Z}", "");
								}
							}
						}
						i++;
					}
					
					Map obj = null;
					if(CommonUtil.isNotEmpty(column[0])) {
						
						/***********************************
						 * 중복제거 대상 업로드 : product, brand, category, offshop, present
						 ***********************************/ 
						if ("product".equals(id) || "brand".equals(id) || "category".equals(id) || "present".equals(id)
								|| "offshop".equals(id) || "childrencard".equals(id)) {
							if(dupList.contains(column[0])){
								msg = "중복된 등록 대상이 존재합니다.";
							} else {
								dupList.add(column[0]);
								
								if ("product".equals(id)) {
									PmsProduct pmsProduct = new PmsProduct();
									pmsProduct.setStoreId(storeId);
									pmsProduct.setProductId(column[0]);
									pmsProduct.setSaleStateCd(BaseConstants.SALE_STATE_CD_SALE);
									pmsProduct = (PmsProduct) productService.getProductBulk(pmsProduct);
									if (pmsProduct != null && !pmsProduct.getProductTypeCd().equals(BaseConstants.PRODUCT_TYPE_CD_PRESENT)) {
										obj = new HashMap<String, Object>();
										obj.put("pmsProduct", pmsProduct);
									}
								} else if ("brand".equals(id)) {
									PmsBrand pmsBrand = new PmsBrand();
									pmsBrand.setBrandId(column[0]);
									pmsBrand.setStoreId(storeId);
									pmsBrand = (PmsBrand) brandService.selectOneTable(pmsBrand);
									if (pmsBrand != null) {
										obj = new HashMap<String, Object>();
										obj.put("pmsBrand", pmsBrand);
									}
								} else if ("category".equals(id)) {
									DmsDisplaycategory dmsDisplaycategory = new DmsDisplaycategory();
									dmsDisplaycategory.setDisplayCategoryId(column[0]);
									dmsDisplaycategory.setStoreId(storeId);
									dmsDisplaycategory = (DmsDisplaycategory) displayCategoryService.selectOneTable(dmsDisplaycategory);
									if (dmsDisplaycategory != null) {
										obj = new HashMap<String, Object>();
										obj.put("dmsDisplaycategory", dmsDisplaycategory);
									}
								} else if ("present".equals(id)) {
									PmsProductSearch search = new PmsProductSearch();
									search.setStoreId(storeId);
									search.setProductId(column[0]);
									search.setSaleStateCd(BaseConstants.SALE_STATE_CD_SALE);
									
									PmsProduct pmsProduct = (PmsProduct) presentService.getPresentDetail(search);
									
									if (pmsProduct != null && pmsProduct.getProductTypeCd().equals(BaseConstants.PRODUCT_TYPE_CD_PRESENT)) {
										obj = new HashMap<String, Object>();
										obj.put("pmsProduct", pmsProduct);
									}
									
								} else if ("offshop".equals(id)) {
									CcsOffshopSearch search = new CcsOffshopSearch();
									search.setStoreId(storeId);
									search.setOffshopId(column[0]);
									CcsOffshop ccsOffshop = offshopService.getOffshopInfo(search);
									
									if (ccsOffshop != null) {
										obj = new HashMap<String, Object>();
										obj.put("ccsOffshop", ccsOffshop);
									}
								} else if ("childrencard".equals(id)) {
									MmsChildrenSearch search = new MmsChildrenSearch();
									search.setStoreId(storeId);
									search.setAccountNo(column[0]);
									MmsChildrencard card = childrenService.childrenCardAuth(search);
									
									if (card == null) {
										obj = new HashMap<String, Object>();
										MmsChildrencard childrencard = new MmsChildrencard();
										childrencard.setChildrencardTypeCd(column[0]);
										childrencard.setName(column[1]);
										childrencard.setAccountNo(column[2]);
										obj.put("childrencard", childrencard);
									}
								}
							}
						} 
						/***********************************
						 * 중복제거 미 대상 : coupon, holiday, deposit, carrot, eventWinner
						 ***********************************/ 
						else {
							if ("coupon".equals(id)) {
								MmsMember member = new MmsMember();
								
								try {
									member = (MmsMember) memberService.getMemberInfoByMemberId(column[0]);
								} catch (Exception e) {
									e.getStackTrace();
									msg = "중복된 회원 번호가 존재합니다.";
								}
								
								if (member != null) {
									SpsCouponissue issue = new SpsCouponissue();
									issue.setStoreId(storeId);
									issue.setCouponId(couponId);
									issue.setMemberNo(member.getMemberNo());
									issue.setControllCheckType("BO");//허용설정 체크타입
									Map<String, String> resultMap = couponService.issueCoupon(issue, false);
									msg = resultMap.get("resultMsg");
									String code = resultMap.get("resultCode"); 
									if ("0000".equals(code)) {
										obj = new HashMap<String, Object>();
										obj.put("issueCoupon", msg);
									}
								} else {
									msg = "유효한 회원이 아닙니다.";
								}
							} else if ("holiday".equals(id)) {
								if (StringUtils.isNotEmpty(column[0])) {
									CcsBusinessholiday holiday = new CcsBusinessholiday();
									holiday.setStoreId(storeId);
									holiday.setHoliday(column[0]);
									Date date = null;
									try{
										SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
										date = formatter.parse(column[0]);
										if(column[0].equals(formatter.format(date))){
											obj = new HashMap<String, Object>();
											obj.put("ccsBusinessholiday", holiday);
										}
									}
									catch(ParseException ex){
										ex.printStackTrace();
										msg = "형식에 오류가 있습니다.";
									}
									
								} else {
									msg = "없는 휴일";
								}
							} else if ("carrot".equals(id)) {
								MmsMember member = new MmsMember();
								try {
									member = (MmsMember) memberService.getMemberInfoByMemberId(column[0]);
								} catch (Exception e) {
									e.getStackTrace();
									msg = "중복된 회원 번호가 존재합니다.";
								}
								
								if (member != null) {
									MmsCarrot carrot = new MmsCarrot();
									carrot.setMemberNo(member.getMemberNo());						//회원번호
									try {
										carrot.setCarrot(new BigDecimal(column[1]));					//조정당근
										String code = column[2];
										code = code.toUpperCase();
										if("CS".equals(code) || "EVENT".equals(code)) {
											if("CS".equals(code)) {											//당근 유형 : 엑셀 업로드시 cs, event 만
												carrot.setCarrotTypeCd(BaseConstants.CARROT_TYPE_CD_CS);
											} else if("EVENT".equals(code)) {
												carrot.setCarrotTypeCd(BaseConstants.CARROT_TYPE_CD_EVENT);
											}
											carrot.setNote(column[3]);										//사유
											
											Map<String, String> resultMap = memberService.updateCarrot(carrot);
											msg = resultMap.get("resultMsg");
											
											if ("success".equals(msg)) {
												obj = new HashMap<String, Object>();
												obj.put("adjustCarrot", msg);
											}
										} else {
											if (CommonUtil.isEmpty(code)) {
												msg = "조정 당근의 코드값이 누락되었습니다.";
											} else {
												msg = "CS, EVENT 유형만 등록하실 수 있습니다.";
											}
										}
									} catch (NumberFormatException e) {
										e.printStackTrace();
										msg = "조정 당근이 누락되었습니다.";
									}
								} else {
									msg = "유효한 회원이 아닙니다.";
								}
							} else if ("deposit".equals(id)) {
								
								MmsMember member = new MmsMember();
								try {
									member = (MmsMember) memberService.getMemberInfoByMemberId(column[0]);
								} catch (Exception e) {
									e.getStackTrace();
									msg = "중복된 회원 번호가 존재합니다.";
								}
								
								if (member != null) {
									MmsDeposit deposit = new MmsDeposit();
									deposit.setMemberNo(member.getMemberNo());						//회원번호
									try {
										deposit.setDepositAmt(new BigDecimal(column[1]));					//조정예치금
										deposit.setNote(column[2]);											//사유
										deposit.setDepositTypeCd(BaseConstants.DEPOSIT_TYPE_CD_MANAGER);	//예치금유형:관리자조정
										
										Map<String, String> resultMap = memberService.saveMemberDeposit(deposit);
										msg = resultMap.get("resultMsg");
										
										if ("success".equals(msg)) {
											obj = new HashMap<String, Object>();
											obj.put("adjustDeposit", msg);
										}
									} catch (NumberFormatException e) {
										e.printStackTrace(); 
										msg = "조정 예치금이 누락되었습니다.";
									}
								} else {
									msg = "유효한 회원이 아닙니다.";
								}
								
							} else if ("eventWinner".equals(id)) {
								if (rownum > 1 && CommonUtil.isNotEmpty(column[0])) {
									SpsEventjoin join = new SpsEventjoin();							
									join = eventService.getWinnerDetail(column[1]);	
									
									join.setEventId(column[0]);
									join.setMemberNo(new BigDecimal(column[1]));													
									join.setNaverblogUrl(column[2]);
									join.setInstagramUrl(column[3]);
									join.setFacebookUrl(column[4]);
									join.setKakaostoryUrl(column[5]);
									join.setUrl(column[6]);
									join.setWinYn(column[7]);
									join.setGiftName(column[8]);				
									
									obj = new HashMap<String, Object>();
									obj.put("winnerList", join);
								}						
							}
						}
						
						// 엑셀 1 Row 처리
						if (obj == null) {
							Map<String, Object> excelError = new HashMap<String, Object>();
							excelError.put(rownum + 1 + "번째 : " + column[0], msg);
							errorList.add(excelError);
						} else {
							list.add(obj);
						}
						
						
					} 
					// 엑셀 ROW 데이터 누락
					else {
						Map<String, Object> excelError = new HashMap<String, Object>();
						excelError.put(rownum + 1 + "번째 : ", "누락된 데이터가 존재합니다.");
						errorList.add(excelError);						
					}
				}
			}
		}

		map.put("totalCnt", (int) list.size() + (int) errorList.size());
		map.put("resultList", list);
		map.put("errorList", errorList);
		return map;
	}

	@RequestMapping(value = "/gridbulkupload/{id}/errordown", method = RequestMethod.POST)
	public String getErrorListExcel(@PathVariable("id") String id, @RequestBody List<Map<String, String>> errorList) {
		BaseSearchCondition search = new BaseSearchCondition();
		List<String> filed = new ArrayList<String>();
		List<String> headerNm = new ArrayList<String>();
		search.setFileNm("errorList");
		filed.add("excelId");
		filed.add("msg");
		if ("product".equals(id)) {
			headerNm.add("상품 ID");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("brand".equals(id)) {
			headerNm.add("브랜드 ID");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("category".equals(id)) {
			headerNm.add("전시카테고리 ID");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("coupon".equals(id)) {
			headerNm.add("회원ID");
			headerNm.add("발급실패사유");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("holiday".equals(id)) {
			headerNm.add("휴일");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("offshop".equals(id)) {
			headerNm.add("오프라인 매장");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("carrot".equals(id)) {
			headerNm.add("회원ID");
			headerNm.add("조정실패사유");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("deposit".equals(id)) {
			headerNm.add("회원ID");
			headerNm.add("조정실패사유");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("eventWinner".equals(id)) {
			headerNm.add("회원번호");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		} else if ("childrencard".equals(id)) {
			headerNm.add("카드번호");
			search.setField(filed);
			search.setHeaderNm(headerNm);
		}

		String msg = ExcelUtil.excelDownlaod(search, errorList);
		return msg;
	}

	/**
	 * @Method Name : checkIdDuplicate
	 * @author : ian
	 * @date : 2016. 6. 24.
	 * @description : 적용 대상 중복 검사
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkIdDuplicate", method = RequestMethod.POST)
	public List<String> getExistsList(@RequestBody CcsApplySearch search) throws Exception {

		List<String> resultList = new ArrayList<String>();

		// 중복 id list
		List<String> dupList = commonService.getExistsList(search);
		boolean flag = true;
		for (int i = 0; i < search.getIdArray().length; i++) {
			for (int j = 0; j < dupList.size(); j++) {
				if (search.getIdArray()[i].equals(dupList.get(j))) {
					flag = false;
					break;
				}
			}
			if (flag) {
				String id = search.getIdArray()[i];
				resultList.add(id);
			}
			flag = true;
		}

		return resultList;
	}

	@RequestMapping(value = "/configuration", method = RequestMethod.POST)
	public String readConfiguration(@RequestParam("key") String key) throws Exception {
		return Config.getString(key);
	}

	/**
	 * 
	 * @Method Name : checkExternalNetwork
	 * @author : allen
	 * @date : 2016. 8. 11.
	 * @description : 외부망 체크
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkExternalNetwork", method = RequestMethod.POST)
	public Boolean checkExternalNetwork(HttpServletRequest request) throws Exception {

		IPMask ipmask;
		String[] allowIpArray = Config.getString("allow.ip").split(",");
		boolean checkFlag = true;

//		for (String allowIp : allowIpArray) {
//			ipmask = IPMask.getIPMask(allowIp.trim());
//
//			String ipAddress = request.getHeader("X-FORWARDED-FOR");
//
//			if (StringUtils.isEmpty(ipAddress)) {
//				ipAddress = request.getRemoteAddr();
//			}
//			if (!ipmask.matches(ipAddress)) {
//				checkFlag = false;
//				break;
//			}
//		}

		return checkFlag;
	}

	@RequestMapping(value = "/sendAuthSms", method = RequestMethod.POST)
	public void sendAuthSms(HttpServletRequest request, @RequestBody CcsUser ccsUser) throws Exception {
		// 인증번호 생성
		String authNumber = CommonUtil.makeRandomNumber(6);
		logger.debug("#### authNumber = " + authNumber);
		// 임시 세션 생성
		request.getSession().setAttribute("authNumber", authNumber);

		ccsUser.setPwd("");
		ccsUser = userService.getUserByBoLogin(ccsUser);

		//문자 발송
		TmsQueue tmsQueue = new TmsQueue();
		tmsQueue.setMsgCode("133");
		tmsQueue.setMap1(authNumber);
		tmsQueue.setToPhone(ccsUser.getPhone2());
		tmsService.sendTmsSmsQueue(tmsQueue);

	}

	@RequestMapping(value = "/checkAuthSms", method = RequestMethod.POST)
	public boolean checkAuthSms(HttpServletRequest request, @RequestParam("authNumber") String authNumber) throws Exception {
		// 인증번호 체크
		boolean authResult = false;
		if (StringUtils.isNotEmpty(authNumber)) {
			String sessionAuthNumber = (String) request.getSession().getAttribute("authNumber");
			if (StringUtils.isNotEmpty(sessionAuthNumber)) {
				if (sessionAuthNumber.equals(authNumber)) {
					authResult = true;
				}
			}
		}
		return authResult;
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
}
