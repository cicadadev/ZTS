package gcp.mms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.mms.model.MmsBlacklist;
import gcp.mms.model.search.MmsBlacklistSearch;


@Service
public class BlacklistService extends BaseService {

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
	public List<MmsBlacklist> getMemberBlacklist(MmsBlacklistSearch mmsBlacklistSearch) {
		String memberType[];
		memberType = mmsBlacklistSearch.getMemberType().split(",");
		for (int i = 0; i < memberType.length; i++) {
			if ("MEMBER_TYPE_CD.MEMBERSHIP".equals(memberType[i])) {
				mmsBlacklistSearch.setMembershipYn("Y");
			}
			if ("MEMBER_TYPE_CD.PREMIUM".equals(memberType[i])) {
				mmsBlacklistSearch.setMembershipYn("Y");
			}
			if ("MEMBER_TYPE_CD.EMPLOYEE".equals(memberType[i])) {
				mmsBlacklistSearch.setMembershipYn("Y");
			}
			if ("MEMBER_TYPE_CD.CHILDREN".equals(memberType[i])) {
				mmsBlacklistSearch.setMembershipYn("Y");
			}
			if ("MEMBER_TYPE_CD.B2E".equals(memberType[i])) {
				mmsBlacklistSearch.setMembershipYn("Y");
			}
		}

		return (List<MmsBlacklist>) dao.selectList("mms.blacklist.getMemberBlacklist", mmsBlacklistSearch);
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
	public void saveBlacklist(MmsBlacklist mmsBlacklist) throws Exception {
		dao.insertOneTable(mmsBlacklist);
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
	public void updateBlacklist(List<MmsBlacklist> mmsBlacklists) throws Exception {
		for (MmsBlacklist mmsBlacklist : mmsBlacklists) {
			dao.updateOneTable(mmsBlacklist);
		}
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
	public MmsBlacklist getBlacklistDetail(String blacklistNo) throws Exception {
		MmsBlacklist mmsBlacklist = new MmsBlacklist();
		mmsBlacklist.setBlacklistNo(new BigDecimal(blacklistNo));
		return (MmsBlacklist) dao.selectOne("mms.blacklist.getBlacklistDetail", mmsBlacklist);
	}
}
