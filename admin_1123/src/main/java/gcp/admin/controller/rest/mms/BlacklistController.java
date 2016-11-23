package gcp.admin.controller.rest.mms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.mms.model.MmsBlacklist;
import gcp.mms.model.search.MmsBlacklistSearch;
import gcp.mms.service.BlacklistService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/mms/blacklist")
public class BlacklistController {

	@Autowired
	private BlacklistService blacklistService;

	/**
	 * 
	 * @Method Name : getMemberBlacklist
	 * @author : intune
	 * @date : 2016. 7. 11.
	 * @description : 블랙리스트 조회
	 *
	 * @param mmsBlacklistSearch
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<MmsBlacklist> getMemberBlacklist(@RequestBody MmsBlacklistSearch mmsBlacklistSearch) {
		mmsBlacklistSearch.setStoreId(SessionUtil.getStoreId());
		return blacklistService.getMemberBlacklist(mmsBlacklistSearch);
	}

	/**
	 * 
	 * @Method Name : saveBlacklist
	 * @author : intune
	 * @date : 2016. 7. 11.
	 * @description : 블랙리스트 저장
	 *
	 * @param mmsBlacklist
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveBlacklist(@RequestBody MmsBlacklist mmsBlacklist) throws Exception {
		blacklistService.saveBlacklist(mmsBlacklist);
	}

	/**
	 * 
	 * @Method Name : updateBlacklist
	 * @author : intune
	 * @date : 2016. 7. 11.
	 * @description : 블랙리스트 수정
	 *
	 * @param mmsBlacklist
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void updateBlacklist(@RequestBody List<MmsBlacklist> mmsBlacklists) throws Exception {
		blacklistService.updateBlacklist(mmsBlacklists);
	}

	/**
	 * 
	 * @Method Name : getBlacklistDetail
	 * @author : allen
	 * @date : 2016. 7. 14.
	 * @description : 블랙리스트 상세 조회
	 *
	 * @param blacklistNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail/{blacklistNo}", method = RequestMethod.GET)
	public MmsBlacklist getBlacklistDetail(@PathVariable("blacklistNo") String blacklistNo) throws Exception {
		MmsBlacklist mmsBlacklistDetail = blacklistService.getBlacklistDetail(blacklistNo);
		return mmsBlacklistDetail;
	}
}
