package gcp.admin.controller.rest.pms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.pms.model.PmsProduct;
import gcp.pms.service.MdnoticeService;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.ExcelUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;
import intune.gsf.model.BaseSearchCondition;

@RestController
@RequestMapping("api/pms/mdnotice")
public class MdnoticeController {

	@Autowired
	private MdnoticeService mdnoticeService;

	/**
	 * 
	 * @Method Name : selectNoticeList
	 * @author : roy
	 * @date : 2016. 6. 20.
	 * @description : MD공지 목록 조회
	 *
	 * @param ccsNoticeSearch
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsNotice> selectNoticeList(@RequestBody CcsNoticeSearch ccsNoticeSearch) throws Exception {
		List<CcsNotice> noticeList = mdnoticeService.selectNoticeList(ccsNoticeSearch);
		return noticeList;
	}

	/**
	 * MD공지 등록
	 * 
	 * @Method Name : insertNotice
	 * @author : roy
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param notice
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.POST })
	public String insertNotice(@RequestBody CcsNotice notice) throws Exception {
		notice.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isEmpty(notice.getReadCnt())) {
			notice.setReadCnt(new BigDecimal("0"));
		}
		return String.valueOf(mdnoticeService.insertNotice(notice));
	}

	/**
	 * MD공지 수정
	 * 
	 * @Method Name : udpateNotice
	 * @author : roy
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param notice
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.PUT })
	public String udpateNotice(@RequestBody CcsNotice notice) throws Exception {
		notice.setStoreId(SessionUtil.getStoreId());
		return String.valueOf(mdnoticeService.updateNotice(notice));
	}

	/**
	 * 공지 엑셀 다운로드
	 * 
	 * @Method Name : getMdnoticeListExcel
	 * @author : roy
	 * @date : 2016. 6. 21.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	@RequestMapping(value = "/excel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getMdnoticeListExcel(@RequestBody CcsNoticeSearch ccsNoticeSearch) {

		List<CcsNotice> list = new ArrayList<CcsNotice>();
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		list = mdnoticeService.selectNoticeList(ccsNoticeSearch);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("noticeNo", list.get(i).getNoticeNo().toString());
			map.put("noticeTypeName", list.get(i).getNoticeTypeName());
			map.put("title", list.get(i).getTitle());
			map.put("detail", list.get(i).getDetail());
			map.put("perid", list.get(i).getPeriod());
			map.put("insDt", list.get(i).getInsDt());
			map.put("insId", list.get(i).getInsId());
			map.put("updDt", list.get(i).getUpdDt());
			map.put("updId", list.get(i).getUpdId());
			dataList.add(map);
		}
		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) ccsNoticeSearch, dataList);

		return msg;
	}

	/**
	 * 
	 * @Method Name : selectNoticeProductList
	 * @author : roy
	 * @date : 2016. 6. 20.
	 * @description : MD공지상품 목록 조회
	 *
	 * @param ccsNoticeSearch
	 * @return
	 */
	@RequestMapping(value = "/product/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<PmsProduct> selectNoticeProductList(@RequestBody CcsNoticeSearch ccsNoticeSearch)
			throws Exception {
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		List<PmsProduct> noticeProdcutList = mdnoticeService.selectNoticeProductList(ccsNoticeSearch);
		return noticeProdcutList;
	}

	/**
	 * MD공지상품 등록
	 * 
	 * @Method Name : saveNoticeproduct
	 * @author : roy
	 * @date : 2016. 6. 22.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/save", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseEntity saveNoticeproduct(@RequestBody CcsNoticeSearch ccsNoticeSearch) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			mdnoticeService.saveNoticeProduct(ccsNoticeSearch);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}

	/**
	 * 상품 엑셀 다운로드
	 * 
	 * @Method Name : getProductListExcel
	 * @author : roy
	 * @date : 2016. 6. 21.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/excel", method = { RequestMethod.GET, RequestMethod.POST })
	public String getProductListExcel(@RequestBody CcsNoticeSearch ccsNoticeSearch) {

		List<PmsProduct> list = new ArrayList<PmsProduct>();
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		list = mdnoticeService.selectNoticeProductList(ccsNoticeSearch);

		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("productId", list.get(i).getProductId());
			map.put("name", list.get(i).getName());
			map.put("insDt", list.get(i).getInsDt());
			map.put("insId", list.get(i).getInsId());
			map.put("updDt", list.get(i).getUpdDt());
			map.put("updId", list.get(i).getUpdId());
			dataList.add(map);
		}
		String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) ccsNoticeSearch, dataList);

		return msg;
	}

	/**
	 * MD공지 상품 삭제 전 체크
	 * 
	 * @Method Name : checkNoticeproduct
	 * @author : roy
	 * @date : 2016. 6. 21.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/check", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseEntity checkNoticeproduct(@RequestBody CcsNoticeSearch ccsNoticeSearch) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			mdnoticeService.checkNoticeProduct(ccsNoticeSearch);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}

	/**
	 * MD공지 삭제
	 * 
	 * @Method Name : deleteNoticeproduct
	 * @author : roy
	 * @date : 2016. 6. 21.
	 * @description :
	 *
	 * @param ccsNoticeSearch
	 * @throws Exception
	 */
	@RequestMapping(value = "/product/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public BaseEntity deleteNoticeproduct(@RequestBody CcsNoticeSearch ccsNoticeSearch) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			mdnoticeService.deleteNoticeProduct(ccsNoticeSearch);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}
}

