package gcp.admin.controller.rest.sps;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.sps.model.SpsPresent;
import gcp.sps.model.SpsPresentproduct;
import gcp.sps.model.search.SpsPresentSearch;
import gcp.sps.service.PresentPromotionService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.controller.BaseController;

@RestController("presentPromotionController")
@RequestMapping("api/sps/present")
public class PresentPromotionController extends BaseController {
//	private final Log				logger	= LogFactory.getLog(getClass());

	@Autowired
	private PresentPromotionService	presentService;

	/**
	 * @Method Name : getPresentPromotion
	 * @author : ian
	 * @date : 2016. 5. 3.
	 * @description : 사은품 프로모션 관리
	 *
	 * @param sps
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<SpsPresent> getPresentPromotion(@RequestBody SpsPresentSearch sps) {
		List<SpsPresent> list = presentService.getPresentPromotion(sps);
		return list;
	}

	/**
	 * @Method Name : deletePresentPromotion
	 * @author : ian
	 * @date : 2016. 8. 4.
	 * @description : 대기상태인 사은품 프로모션 삭제
	 *
	 * @param present
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deletePresentPromotion(@RequestBody List<SpsPresent> present) throws Exception {
		presentService.deletePresentPromotion(present);
	}

	/**
	 * @Method Name : updatePresent
	 * @author : ian
	 * @date : 2016. 5. 26.
	 * @description : 사은품 프로모션 등록/수정
	 *
	 * @param sp
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/update", method = RequestMethod.POST)
	public String updatePresent(@RequestBody SpsPresent present, HttpServletRequest request) throws Exception {
		String id = null;
		present.setInsId(SessionUtil.getLoginId());
		present.setUpdId(SessionUtil.getLoginId());
		if (CommonUtil.isEmpty(present.getPresentId())) {
			id = presentService.insertPresentPromotion(present);
		} else {
			id = presentService.updatePresentPromotion(present);
		}
		return id;
	}

	@RequestMapping(value = "/updatePresentState", method = RequestMethod.POST)
	public String updatePresentState(@RequestBody SpsPresent present) throws Exception {
		return presentService.updatePresentState(present);
	}

	/**
	 * @Method Name : getPresentPromotionProduct
	 * @author : ian
	 * @date : 2016. 5. 27.
	 * @description : 사은품 프로모션 _ 사은품 목록
	 *
	 * @param presentProduct
	 * @return
	 */
	@RequestMapping(value = "/popup/presentPromotionProduct/list", method = RequestMethod.POST)
	public List<SpsPresentproduct> getPresentPromotionProduct(@RequestBody SpsPresentproduct presentProduct) {

		presentProduct.setStoreId(SessionUtil.getStoreId());
		List<SpsPresentproduct> list = presentService.getPresentPromotionProduct(presentProduct);

		return list;
	}

	/**
	 * @Method Name : getPresentPromotionDetail
	 * @author : ian
	 * @date : 2016. 5. 17.
	 * @description : 사은품 Row Data
	 *
	 * @param sp
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/popup/detail", method = RequestMethod.POST)
	public SpsPresent getPresentPromotionDetail(@RequestBody SpsPresent present) {
		return presentService.getPresentPromotionDetail(present);
	}

	/**
	 * @Method Name : checkIdDuplicatePresent
	 * @author : ian
	 * @date : 2016. 6. 24.
	 * @description : 제공사은품 중복 검사
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkIdDuplicatePresent", method = RequestMethod.POST)
	public List<String> getExistsList(@RequestBody SpsPresentSearch search) throws Exception {

		List<String> resultList = new ArrayList<String>();

		// 중복 id list
		List<String> dupList = presentService.getExistsList(search);
		boolean flag = true;
		for (int i = 0; i < search.getIdArray().length; i++) {
			for (int j = 0; j < dupList.size(); j++) {
				if (search.getIdArray()[i].equals(dupList.get(j))) {
					flag = false;
					break;
				}
			}
			if (flag) {
				String id = search.getIdArray()[i];
				resultList.add(id);
			}
			flag = true;
		}

		return resultList;
	}

}
