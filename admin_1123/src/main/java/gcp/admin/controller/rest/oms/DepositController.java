package gcp.admin.controller.rest.oms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.mms.model.MmsDeposit;
import gcp.mms.model.search.MmsMemberPopupSearch;
import gcp.mms.service.MemberService;

@RestController
@RequestMapping("api/oms/deposit")
public class DepositController {

	@Autowired
	private MemberService memberService;

	@RequestMapping(value="/list",method = { RequestMethod.GET, RequestMethod.POST })
	public List<MmsDeposit> getdepositList(@RequestBody MmsMemberPopupSearch depositSearch) {
		
		List<MmsDeposit> resultList = new ArrayList<MmsDeposit>();
		List<MmsDeposit> list = memberService.getDepositList(depositSearch);
		for (MmsDeposit deposit : list) {
			if (deposit.getMmsMember() != null) {
				if (StringUtils.isNotEmpty(deposit.getMmsMember().getMemberName())) {
					deposit.setMemberInfo(
							deposit.getMmsMember().getMemberName() + " (" + deposit.getMmsMember().getMemberId() + ")");
				}
			}
			resultList.add(deposit);
		}
		
		return resultList;
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public String save(@RequestBody MmsDeposit deposit) throws Exception {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = memberService.saveMemberDeposit(deposit);
		
		return (String) resultMap.get("resultMsg"); 
	}
	
	@RequestMapping(value = "/amt" ,method = { RequestMethod.GET })
	public Object selectOne(@RequestParam String memberNo) throws Exception {
		MmsMemberPopupSearch search = new MmsMemberPopupSearch();
		search.setMemberNo(new BigDecimal(memberNo));
		Object obj = memberService.getDepositBalanceAmt(search);
		return obj;
	}

}
