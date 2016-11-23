package gcp.admin.controller.rest.pms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsBusiness;
import gcp.pms.model.PmsEpexcproduct;
import gcp.pms.model.search.PmsEpexcproductSearch;
import gcp.pms.service.EpexcproductService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/pms/epexcproduct")
public class EpexcproductController {
	private final Log			logger	= LogFactory.getLog(getClass());

	@Autowired
	private EpexcproductService	epexcService;

	/**
	 * @Method Name : getEpexcproductList
	 * @author : peter
	 * @date : 2016. 6. 20.
	 * @description : 외부 비노출 상품목록
	 *
	 * @param pes
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<PmsEpexcproduct> getEpexcproductList(@RequestBody PmsEpexcproductSearch pes) {
		return epexcService.getEpexcproductList(pes);
	}

	/**
	 * @Method Name : getBusinessInfo
	 * @author : peter
	 * @date : 2016. 6. 20.
	 * @description : 공급업체 정보 조회
	 *
	 * @param businessId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{businessId}", method = RequestMethod.GET)
	public CcsBusiness getBusinessInfo(@PathVariable("businessId") String businessId) throws Exception {
		CcsBusiness biz = new CcsBusiness();
		biz.setStoreId(SessionUtil.getStoreId());
		biz.setBusinessId(businessId);
		return (CcsBusiness) epexcService.selectOneTable(biz);
	}

	/**
	 * @Method Name : getExistCount
	 * @author : peter
	 * @date : 2016. 6. 21.
	 * @description : 외부 비노출 자료 중복 체크
	 *
	 * @param pes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkDup", method = RequestMethod.POST)
	public String getExistCount(@RequestBody PmsEpexcproductSearch pes) throws Exception {
		pes.setStoreId(SessionUtil.getStoreId());
		return epexcService.getExistCount(pes);
	}

	/**
	 * @Method Name : insertAttribute
	 * @author : peter
	 * @date : 2016. 6. 21.
	 * @description : 신규 외부 비노출 항목 등록
	 *
	 * @param peList
	 * @throws Exception
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void insertEpexcproduct(@RequestBody PmsEpexcproduct pep) throws Exception {
		pep.setStoreId(SessionUtil.getStoreId());
		epexcService.insertEpexcproduct(pep);
	}

	/**
	 * @Method Name : deleteEpexcproduct
	 * @author : peter
	 * @date : 2016. 7. 5.
	 * @description : 외부 비노출 항목 삭제
	 *
	 * @param peList
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteEpexcproduct(@RequestBody List<PmsEpexcproduct> peList) throws Exception {
		epexcService.deleteEpexcproduct(peList);
	}
}
