package gcp.admin.controller.rest.oms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.oms.model.OmsPaymentsettle;
import gcp.oms.model.search.OmsPgSettleSearch;
import gcp.oms.service.PgSettleService;

@RestController
@RequestMapping("api/oms/pgsettle")
public class PgSettleController {
	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	PgSettleService		pgsettleService;

	/**
	 * @Method Name : getPgSettleList
	 * @author : peter
	 * @date : 2016. 10. 8.
	 * @description : PG사 정산대사 자료 조회
	 *
	 * @param opss
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsPaymentsettle> getPgSettleList(@RequestBody OmsPgSettleSearch opss) throws Exception {
		return pgsettleService.getPgSettleList(opss);
	}
}
