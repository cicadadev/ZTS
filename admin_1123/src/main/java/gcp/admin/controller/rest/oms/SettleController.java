package gcp.admin.controller.rest.oms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsChannel;
import gcp.oms.model.OmsSettle;
import gcp.oms.model.search.OmsSettleSearch;
import gcp.oms.service.SettleService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/settle")
public class SettleController {
	private final Log	logger	= LogFactory.getLog(getClass());

	@Autowired
	private SettleService	settleService;

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<OmsSettle> getSettleList(@RequestBody OmsSettleSearch oss) throws Exception {
		return settleService.getSettleList(oss);
	}

	@RequestMapping(value = "/channelList", method = { RequestMethod.GET, RequestMethod.POST })
	public List<CcsChannel> getAllChannelList() throws Exception {
		return settleService.getAllChannelList(SessionUtil.getStoreId());
	}
}
