package gcp.admin.controller.rest.ccs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsCodegroup;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.service.CodeService;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.BaseEntity;

@RestController
@RequestMapping("api/ccs/code")
public class CodeController {

	@Autowired
	private CodeService codeService;

	/**
	 * 
	 * @Method Name : selectCodeGrpList
	 * @author : roy
	 * @date : 2016. 6. 13.
	 * @description : 코드그룹관리 목록 조회
	 *
	 * @param codegroupSerch
	 * @return
	 */
	@RequestMapping(value = "/codegroup/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsCodegroup> selectCodeGrpList(@RequestBody CcsCodeSearch codegroupSearch) {

		/*if (StringUtils.isNotEmpty(qnaSerch.getQnaIds())) {
			String[] ids = qnaSerch.getQnaIds().split(",");
			if (ids != null && ids.length > 0) {
				Long[] idArray = new Long[ids.length];
		
				for (int i = 0; i < ids.length; i++) {
					idArray[i] = Long.parseLong(ids[i].replaceAll("'", ""));
				}
				qnaSerch.setQnaIdList(idArray);
			}
		}*/
		codegroupSearch.setStoreId(SessionUtil.getStoreId());
		List<CcsCodegroup> list = codeService.selectCodegroupList(codegroupSearch);

		/*if (list != null && list.size() > 0) {
			for (CcsCode ccsCodeInfo : list) {
				ccsCodeInfo.setMemberInfo(ccsCodeInfo.getMmsMember().getName1() + "(" + ccsCodeInfo.getMmsMember().getMemId() + ")");
			}
		}*/
		return list;
	}

	/**
	 * 코드 그룹 등록
	 * 
	 * @Method Name : saveCodeGroup
	 * @author : roy
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/codegroup/save", method = { RequestMethod.POST })
	public BaseEntity saveCodeGroup(@RequestBody List<CcsCodegroup> reserves) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			codeService.saveCodeGroup(reserves);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}

	/**
	 * 코드 그룹 삭제
	 * 
	 * @Method Name : deleteCodeGroup
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/codegroup/delete", method = { RequestMethod.POST })
	public void deleteCodeGroup(@RequestBody List<CcsCodegroup> reserves) throws Exception {
		codeService.deleteCodeGroup(reserves);
	}

	/**
	 * 코드 그룹 엑셀 다운로드
	 * 
	 * @Method Name : getCodeGroupListExcel
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param codegroupSearch
	 * @throws Exception
	 */
	/*	@RequestMapping(value = "/codegroup/excel", method = { RequestMethod.GET, RequestMethod.POST })
		public String getCodeGroupListExcel(@RequestBody CcsCodeSearch codegroupSearch) {
	
			List<CcsCodegroup> list = new ArrayList<CcsCodegroup>();
			codegroupSearch.setStoreId(SessionUtil.getStoreId());
			list = codeService.selectCodegroupList(codegroupSearch);
	
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("cdGroupCd", list.get(i).getCdGroupCd());
				map.put("name", list.get(i).getName());
				map.put("insDt", list.get(i).getInsDt());
				map.put("insId", list.get(i).getInsId());
				map.put("updDt", list.get(i).getUpdDt());
				map.put("updId", list.get(i).getUpdId());
				dataList.add(map);
			}
			String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) codegroupSearch, dataList);
	
			return msg;
		}*/

	/**
	 * 
	 * @Method Name : selectCodeList
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description : 코드 목록 조회
	 *
	 * @param codeSearch
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsCode> selectCodeList(@RequestBody CcsCodeSearch codeSearch) {

		/*if (StringUtils.isNotEmpty(qnaSerch.getQnaIds())) {
			String[] ids = qnaSerch.getQnaIds().split(",");
			if (ids != null && ids.length > 0) {
				Long[] idArray = new Long[ids.length];
		
				for (int i = 0; i < ids.length; i++) {
					idArray[i] = Long.parseLong(ids[i].replaceAll("'", ""));
				}
				qnaSerch.setQnaIdList(idArray);
			}
		}*/
		codeSearch.setStoreId(SessionUtil.getStoreId());
		List<CcsCode> list = codeService.selectCodeList(codeSearch);

		/*if (list != null && list.size() > 0) {
			for (CcsCode ccsCodeInfo : list) {
				ccsCodeInfo.setMemberInfo(ccsCodeInfo.getMmsMember().getName1() + "(" + ccsCodeInfo.getMmsMember().getMemId() + ")");
			}
		}*/
		return list;
	}

	/**
	 * 코드 등록
	 * 
	 * @Method Name : saveCode
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param reserves
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public BaseEntity saveCode(@RequestBody List<CcsCode> reserves) throws Exception {
		BaseEntity base = new BaseEntity();
		try {

			codeService.saveCode(reserves);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}

		return base;
	}

	/**
	 * 코드 삭제
	 * 
	 * @Method Name : deleteCode
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param reserves
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public void deleteCode(@RequestBody List<CcsCode> reserves) throws Exception {
		codeService.deleteCode(reserves);
	}

	/**
	 * 코드 엑셀 다운로드
	 * 
	 * @Method Name : getCodeListExcel
	 * @author : roy
	 * @date : 2016. 6. 15.
	 * @description :
	 *
	 * @param codeSearch
	 * @throws Exception
	 */
	/*	@RequestMapping(value = "/excel", method = { RequestMethod.GET, RequestMethod.POST })
		public String getCodeListExcel(@RequestBody CcsCodeSearch codeSearch) {
	
			List<CcsCode> list = new ArrayList<CcsCode>();
			codeSearch.setStoreId(SessionUtil.getStoreId());
			list = codeService.selectCodeList(codeSearch);
	
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("cd", list.get(i).getCd());
				map.put("cdGroupCd", list.get(i).getCdGroupCd());
				map.put("name", list.get(i).getName());
				map.put("sortNo", String.valueOf(list.get(i).getSortNo()));
				map.put("useYn", list.get(i).getUseYn());
				map.put("insDt", list.get(i).getInsDt());
				map.put("insId", list.get(i).getInsId());
				map.put("updDt", list.get(i).getUpdDt());
				map.put("updId", list.get(i).getUpdId());
				dataList.add(map);
			}
			String msg = ExcelUtil.excelDownlaod((BaseSearchCondition) codeSearch, dataList);
	
			return msg;
		}*/
}
