package gcp.admin.controller.rest.oms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.oms.model.OmsClaim;
import gcp.oms.model.OmsClaimWrapper;
import gcp.oms.model.OmsClaimproduct;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.search.OmsClaimSearch;
import gcp.oms.service.ClaimService;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms")
public class ClaimController {
	// private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);

	@Autowired
	private ClaimService claimService;

	@RequestMapping(value = { "/claim/list", "/claim/target/list" }, method = { RequestMethod.GET, RequestMethod.POST })
	public List<?> selectList(@RequestBody OmsClaimSearch claimSearch) throws Exception {
		claimSearch.setStoreId(SessionUtil.getStoreId());
		List<?> claimList = claimService.selectList(claimSearch.getSearchId(), claimSearch);
		return claimList;
	}
	
	@RequestMapping(value = "/claim", method = RequestMethod.GET)
	public Object selectOne(@RequestParam("orderId") String orderId) throws Exception {
		return claimService.selectOne(orderId);
	}

	@RequestMapping(value = "/claim" ,method = RequestMethod.POST)
	public CcsMessage claim(@RequestBody OmsClaimWrapper omsClaimWrapper) {
		omsClaimWrapper.setClaimPath("BO");
		CcsMessage result = new CcsMessage();
		try {
			result = claimService.insertClaimData(omsClaimWrapper, SessionUtil.getLoginId());
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResultCode("-999");
			result.setResultMessage(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/claim", method = RequestMethod.PUT)
	public CcsMessage update(@RequestBody OmsClaimWrapper omsClaimWrapper) throws Exception {
		CcsMessage result = new CcsMessage();
		try {
			result = claimService.updateClaimData(omsClaimWrapper);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResultCode("-999");
			result.setResultMessage(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/claim/complete" ,method = RequestMethod.POST)
	public CcsMessage complete(@RequestBody OmsClaimWrapper omsClaimWrapper) throws Exception {
		CcsMessage result = new CcsMessage();
		try {
			if ("CLAIM_TYPE_CD.RETURN".equals(omsClaimWrapper.getClaimTypeCd())) {
				result = claimService.updateReturnComplete(omsClaimWrapper, SessionUtil.getLoginId());
			} else if ("CLAIM_TYPE_CD.EXCHANGE".equals(omsClaimWrapper.getClaimTypeCd())) {
				result = claimService.updateExchangeComplete(omsClaimWrapper, SessionUtil.getLoginId());
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResultCode("-999");
			result.setResultMessage(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/claim/save", method = { RequestMethod.POST })
	public int saveGrid(@RequestBody List<OmsClaimproduct> omsClaimproducts) throws Exception {
		int updateCnt = 0;
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		for (OmsClaimproduct product : omsClaimproducts) {
			OmsClaim claim = product.getOmsClaim();
			claim.setUpdDt(currentDate);
			claim.setUpdId(userId);
			updateCnt += claimService.update(claim);
		}
		return updateCnt;
	}
}
