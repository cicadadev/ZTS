package gcp.admin.controller.rest.ccs;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.search.CcsOffshopSearch;
import gcp.ccs.service.OffshopService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/ccs/offshop")
public class OffshopController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private OffshopService	offshopService;

	/**
	 * 
	 * @Method Name : getOffshopList
	 * @author : emily
	 * @date : 2016. 6. 23.
	 * @description : 매장 검색 팝업
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/popup/list", method = RequestMethod.POST)
	public List<CcsOffshop> getOffshopList(@RequestBody CcsOffshopSearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		return offshopService.getOffshopList(search);
	}

}
