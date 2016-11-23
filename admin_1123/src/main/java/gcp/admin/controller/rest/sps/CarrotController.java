package gcp.admin.controller.rest.sps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.mms.model.MmsCarrot;
import gcp.mms.model.search.MmsMemberPopupSearch;
import gcp.mms.service.MemberService;

@RestController
@RequestMapping("api/sps/carrot")
public class CarrotController {

	@Autowired
	private MemberService memberService;

	@RequestMapping(value = "/summry", method = { RequestMethod.GET, RequestMethod.POST })
	public List<MmsCarrot> getCarrotSummry(@RequestBody MmsMemberPopupSearch carrotSearch) {
		return memberService.getCarrotSummry(carrotSearch);
	}

	@RequestMapping(value="/list",method = { RequestMethod.GET, RequestMethod.POST })
	public List<MmsCarrot> getCarrotList(@RequestBody MmsMemberPopupSearch carrotSearch) {
		
		List<MmsCarrot> resultList = new ArrayList<MmsCarrot>();
		List<MmsCarrot> list = memberService.getCarrotList(carrotSearch);
		for (MmsCarrot carrot : list) {
			if (carrot.getMmsMember() != null) {
				if (StringUtils.isNotEmpty(carrot.getMmsMember().getMemberName())) {
					carrot.setMemberInfo(
							carrot.getMmsMember().getMemberName() + " (" + carrot.getMmsMember().getMemberId() + ")");
				}
			}
			resultList.add(carrot);
		}
		
		return resultList;
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public Map<String, String> save(@RequestBody MmsCarrot carrot) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		return resultMap = memberService.updateCarrot(carrot);
	}
//
//	@RequestMapping(value = "/{pointSaveId}", method = { RequestMethod.GET, RequestMethod.POST })
//	public SpsPointsave detail(@PathVariable("pointSaveId") String pointSaveId) throws Exception {
//		SpsPointSearch spsPointSearch = new SpsPointSearch();
//		spsPointSearch.setStoreId(SessionUtil.getStoreId());
//		spsPointSearch.setPointSaveId(pointSaveId);
//		return (SpsPointsave) pointService.getPointDetail(spsPointSearch);
//	}
}
