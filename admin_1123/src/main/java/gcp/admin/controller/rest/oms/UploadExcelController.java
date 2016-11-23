package gcp.admin.controller.rest.oms;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gcp.ccs.model.CcsExcelUploadResult;
import gcp.oms.model.excel.UploadExcelOrder;
import gcp.oms.model.search.OmsUploadexcelSearch;
import gcp.oms.service.UploadExcelService;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/uploadexcel")
public class UploadExcelController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private UploadExcelService	uploadExcelService;

	/**
	 * @Method Name : bulkUpload
	 * @author : peter
	 * @date : 2016. 10. 13.
	 * @description : 엑셀 주문파일 업로드
	 *
	 * @param siteId
	 * @param mRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bulk/{id}", method = RequestMethod.POST)
	public CcsExcelUploadResult bulkUpload(@PathVariable("id") String siteId, MultipartHttpServletRequest mRequest,
			Model model) {
		OmsUploadexcelSearch ous = new OmsUploadexcelSearch();
		ous.setStoreId(SessionUtil.getStoreId());
		ous.setSiteId(siteId);

		String uploadTempPath = Config.getString("upload.excel.path");
		Iterator<String> files = mRequest.getFileNames();
		List<UploadExcelOrder> orderList = null;
		CcsExcelUploadResult summary = new CcsExcelUploadResult();
		try {
			MultipartFile mfile = null;
			while (files.hasNext()) {
				mfile = mRequest.getFile(files.next());
			}

			String fileName = mfile.getOriginalFilename();
			uploadTempPath = uploadTempPath + File.separator + DateUtil.getCurrentDate(DateUtil.FORMAT_7);
			//경로에 디렉토리가 없으면 생성
			File dirPath = new File(uploadTempPath);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			//엑셀파일 업로드
			File excelFile = new File(uploadTempPath + File.separator + fileName);
			logger.debug("Excel File: " + excelFile.getAbsolutePath());
			excelFile.setReadable(true);
			mfile.transferTo(excelFile);

			//엑셀파일 읽기
			orderList = uploadExcelService.readExcelFile(excelFile, ous);
			summary.setResultList(orderList);
			if (null == orderList) {
				summary.setTotalCnt(0);
			} else {
				summary.setTotalCnt(orderList.size());
//				summary.setFailCnt(failCnt);
//				summary.setSuccessCnt(totalCnt - failCnt);
//				summary.setErrorList(errorList);
			}
		} catch (Exception e) {
			logger.error("##### grid bulk 에러 : " + e.getMessage());
			summary.setResultList(null);
			summary.setTotalCnt(-1);
			e.printStackTrace();
		}

		return summary;
	}

	/**
	 * @Method Name : insertExcelOrder
	 * @author : peter
	 * @date : 2016. 10. 13.
	 * @description : 선택된 항목 주문데이터 생성
	 *
	 * @param excelOrderList
	 * @return
	 */
	@RequestMapping(value = "/insertOrder", method = RequestMethod.POST)
	public String insertExcelOrder(@RequestBody List<UploadExcelOrder> excelOrderList) {
		String result = "";

		try {
			result = uploadExcelService.insertExcelOrder(excelOrderList);
		} catch (Exception e) {
			result = "오류: " + e.getMessage();
			e.printStackTrace();
		}

		return result;
	}
}
