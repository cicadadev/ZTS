package gcp.admin.controller.rest.mms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.mms.model.MmsReadhistory;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.service.HistoryService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/mms/history")
public class HistoryController {

	@Autowired
	private HistoryService historyService;

	/**
	 * 
	 * @Method Name : getMemberHistorylist
	 * @author : intune
	 * @date : 2016. 10. 22.
	 * @description : 개인정보 조회 이력 리스트
	 *
	 * @param mmsHistorylistSearch
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<MmsReadhistory> getMemberBlacklist(@RequestBody MmsMemberSearch mmsMemberSearch) {
		mmsMemberSearch.setStoreId(SessionUtil.getStoreId());
		return historyService.getReadHistorylist(mmsMemberSearch);
	}

}
