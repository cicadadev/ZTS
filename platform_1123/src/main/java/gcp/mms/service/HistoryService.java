package gcp.mms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.mms.model.MmsReadhistory;
import gcp.mms.model.search.MmsMemberSearch;

@Service
public class HistoryService extends BaseService {

	/**
	 * 
	 * @Method Name : getMemberBlackList
	 * @author : allen
	 * @date : 2016. 7. 11.
	 * @description :
	 *
	 * @param mmsBlacklistSearch
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MmsReadhistory> getReadHistorylist(MmsMemberSearch mmsMemberSearch) {

		return (List<MmsReadhistory>) dao.selectList("mms.history.getReadHistorylist", mmsMemberSearch);
	}

	/**
	 * 
	 * @Method Name : saveReadHistory
	 * @author : allen
	 * @date : 2016. 11. 7.
	 * @description : 개인정보조회 이력 저장
	 *
	 * @param mmsReadhistory
	 * @throws Exception
	 */
	public void saveReadHistory(MmsReadhistory mmsReadhistory) throws Exception {
		dao.insert("mms.history.insertReadHistory", mmsReadhistory);
	}
}
