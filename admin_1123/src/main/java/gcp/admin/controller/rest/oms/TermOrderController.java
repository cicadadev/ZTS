package gcp.admin.controller.rest.oms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsSite;
import gcp.external.model.OmsReceiveordermapping;
import gcp.external.service.ChinaReceiveOrderService;
import gcp.external.service.SbnReceiveOrderService;
import gcp.oms.model.search.OmsTermorderSearch;
import gcp.oms.service.TermOrderService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms/termorder")
public class TermOrderController {
	private final Log			logger	= LogFactory.getLog(getClass());

	@Autowired
	private TermOrderService	termOrderService;

	@Autowired
	private SbnReceiveOrderService		sbnRoService;

	@Autowired
	private ChinaReceiveOrderService	chinaRoService;

	/**
	 * @Method Name : getDomesticSiteList
	 * @author : peter
	 * @date : 2016. 10. 4.
	 * @description : 국내외부몰 사이트 정보 조회(중국몰 제외)
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/siteList", method = { RequestMethod.POST })
	public List<CcsSite> getExternalSiteList() throws Exception {
		List<CcsSite> siteList = termOrderService.getExternalSiteList(SessionUtil.getStoreId());

		return siteList;
	}

	/**
	 * @Method Name : getUploadConfList
	 * @author : peter
	 * @date : 2016. 10. 8.
	 * @description : 사방넷 주문 미처리 내역 조회
	 *
	 * @param ots
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public List<OmsReceiveordermapping> getUnHandleReceiveOrderList(@RequestBody OmsTermorderSearch ots) throws Exception {
		ots.setStoreId(SessionUtil.getStoreId());
		List<OmsReceiveordermapping> list = termOrderService.getUnHandleReceiveOrderList(ots);

		return list;
	}

	/**
	 * @Method Name : deleteOrderList
	 * @author : peter
	 * @date : 2016. 10. 8.
	 * @description : 미처리 주문 삭제
	 *
	 * @param orList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public String deleteOrderList(@RequestBody List<OmsReceiveordermapping> orList) throws Exception {
		String result = "Y";
		try {
			termOrderService.deleteOrderList(orList, SessionUtil.getStoreId());
		} catch (Exception e) {
			result = "N";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Method Name : updateOrderList
	 * @author : peter
	 * @date : 2016. 10. 8.
	 * @description : 미처리 주문 변경
	 *
	 * @param orList
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public void updateOrderList(@RequestBody List<OmsReceiveordermapping> orList) throws Exception {
		termOrderService.updateOrderList(orList, SessionUtil.getStoreId());
	}

	/**
	 * @Method Name : receiveSbnOrder
	 * @author : peter
	 * @date : 2016. 10. 8.
	 * @description : 사방넷 주문자료 터미널로 수집
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveSbn", method = { RequestMethod.POST })
	public String receiveSbnOrder(@RequestBody OmsTermorderSearch ots) throws Exception {
		String rtnValue = "";
		try {
			rtnValue = termOrderService.receiveSbnOrder(ots);
		} catch (Exception e) {
			logger.error("사방넷 통신 Error: " + e.toString());
			rtnValue = "F";
			e.printStackTrace();
		}

		return rtnValue;
	}

	/**
	 * @Method Name : insertReceiveOrder
	 * @author : peter
	 * @date : 2016. 10. 8.
	 * @description : 사방넷 미처리 주문자료 주문데이터 생성
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertSbnOrder", method = { RequestMethod.POST })
	public String insertSbnReceiveOrder() throws Exception {
		return sbnRoService.processSbnReceiveOrder(SessionUtil.getStoreId(), SessionUtil.getLoginId());
	}

	/**
	 * @Method Name : receiveTmallOrder
	 * @author : peter
	 * @date : 2016. 11. 15.
	 * @description :
	 *
	 * @param ots
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveTmall", method = { RequestMethod.POST })
	public String receiveTmallOrder(@RequestBody OmsTermorderSearch ots) throws Exception {
		String rtnValue = "";
		ots.setStoreId(SessionUtil.getStoreId());
		try {
			int orderCnt = termOrderService.receiveTmallOrder(ots);
			rtnValue = "Tmall 주문수집: " + orderCnt + "건";
		} catch (Exception e) {
			logger.error("Tmall 주문수집 Error: " + e.toString());
			rtnValue = "F";
			e.printStackTrace();
		}

		return rtnValue;
	}

	/**
	 * @Method Name : insertTmallReceiveOrder
	 * @author : peter
	 * @date : 2016. 11. 15.
	 * @description : Tmall 미처리 주문자료 주문데이터 생성
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertTmallOrder", method = { RequestMethod.POST })
	public String insertTmallReceiveOrder() throws Exception {
		return chinaRoService.processChinaReceiveOrder(SessionUtil.getStoreId(), SessionUtil.getLoginId(), "TMALL");
	}
}
