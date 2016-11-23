package gcp.mms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.mms.model.MmsChildrencard;
import gcp.mms.model.search.MmsChildrenSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.SessionUtil;


@Service
public class ChildrenService extends BaseService {


	public List<MmsChildrencard> getChildrenCardList(MmsChildrenSearch mmsChildrenSearch) {

		return (List<MmsChildrencard>) dao.selectList("mms.children.getChildrenCardList", mmsChildrenSearch);
	}
	
	/**
	 * 
	 * @Method Name : childrenCardAuth
	 * @author : intune
	 * @date : 2016. 10. 27.
	 * @description : 다자녀카드 정보 조회
	 *
	 * @param childrencard
	 * @return
	 */
	public MmsChildrencard childrenCardAuth(MmsChildrenSearch search) {
		return (MmsChildrencard) dao.selectOne("mms.children.getChildrenCardInfo", search);
	}

	/**
	 * 
	 * @Method Name : updateChildrencardInfo
	 * @author : intune
	 * @date : 2016. 10. 27.
	 * @description : 다자녀 카드 회원번호 업데이트
	 *
	 * @param mmsChildrencard
	 */
	public void updateChildrencardInfo(MmsChildrencard mmsChildrencard) {
		dao.update("mms.children.updateChildrencardInfo", mmsChildrencard);
	}

	/**
	 * 
	 * @Method Name : insertChildrencard
	 * @author : allen
	 * @date : 2016. 11. 2.
	 * @description : 다자녀 카드 등록
	 *
	 * @param mmsChildrencardList
	 * @throws Exception
	 */
	public void saveChildrencard(List<MmsChildrencard> mmsChildrencardList) throws Exception {

		for (MmsChildrencard mmsChildrencard : mmsChildrencardList) {
			mmsChildrencard.setInsId(SessionUtil.getLoginId());
			mmsChildrencard.setUpdId(SessionUtil.getLoginId());

			if (BaseConstants.CRUD_TYPE_CREATE.equals(mmsChildrencard.getCrudType())) {
				dao.insert("mms.children.insertChildrencard", mmsChildrencard);
			} else {
				dao.update("mms.children.updateChildrencard", mmsChildrencard);
			}
		}
	}
}
