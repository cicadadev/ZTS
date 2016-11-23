package gcp.admin.controller.rest.mms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.mms.model.MmsChildrencard;
import gcp.mms.model.search.MmsChildrenSearch;
import gcp.mms.service.ChildrenService;

@RestController
@RequestMapping("api/mms/childrencard")
public class childrenController {

	@Autowired
	private ChildrenService childrenService;

	/**
	 * 
	 * @Method Name : getChildrenMemberlist
	 * @author : allen
	 * @date : 2016. 7. 18.
	 * @description : 다자녀 회원 리스트 조회
	 *
	 * @param mmsChildrenSearch
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<MmsChildrencard> getChildrenMemberlist(@RequestBody MmsChildrenSearch mmsChildrenSearch) {
		return childrenService.getChildrenCardList(mmsChildrenSearch);
	}

	/**
	 * 
	 * @Method Name : saveChildrenCard
	 * @author : allen
	 * @date : 2016. 11. 2.
	 * @description : 다자녀 카드 등록
	 *
	 * @param mmsChildrencardList
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveChildrenCard(@RequestBody List<MmsChildrencard> mmsChildrencardList) throws Exception {
		childrenService.saveChildrencard(mmsChildrencardList);
	}
}
