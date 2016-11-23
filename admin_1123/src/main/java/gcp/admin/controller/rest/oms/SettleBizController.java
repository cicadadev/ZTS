package gcp.admin.controller.rest.oms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.common.util.BoSessionUtil;
import gcp.oms.model.OmsSettle;
import gcp.oms.model.OmsSettleBiz;
import gcp.oms.model.search.OmsSettleSearch;
import gcp.oms.service.SettleBizService;
import intune.gsf.common.utils.CommonUtil;

@RestController
@RequestMapping("api/oms/settlebiz")
public class SettleBizController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private SettleBizService	settleBizService;

	/**
	 * @Method Name : getSettleBizList
	 * @author : peter
	 * @date : 2016. 10. 18.
	 * @description : 업체별 정산금액
	 *
	 * @param oss
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsSettleBiz> getSettleBizList(@RequestBody OmsSettleSearch oss) throws Exception {
		logger.debug("businessId: " + BoSessionUtil.getBusinessId());

		//PO 로그인일경우 business id 값 세팅
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			oss.setBusinessId(BoSessionUtil.getBusinessId());
		}

		return settleBizService.getSettleBizList(oss);
	}

	/**
	 * @Method Name : getSettleBizDetailList
	 * @author : peter
	 * @date : 2016. 10. 18.
	 * @description : 업체별 상세 정산내역
	 *
	 * @param oss
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/detail/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsSettle> getSettleBizDetailList(@RequestBody OmsSettleSearch oss) throws Exception {
		return settleBizService.getSettleBizDetailList(oss);
	}
}
