package gcp.admin.controller.rest.dms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.model.base.BaseDmsTemplate;
import gcp.dms.model.base.BaseDmsTemplateDisplay;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.DisplayCommonService;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/dms/common")
public class DisplayCommonController {

	@Autowired
	private DisplayCommonService displayCommonService;

	/**
	 * 템플릿 타입으로 템플릿 목록조회
	 * 
	 * @Method Name : getTemplateListByType
	 * @author : eddie
	 * @date : 2016. 5. 3.
	 * @description :
	 *
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public List<BaseDmsTemplate> getTemplateListByType(DmsDisplaySearch search) throws Exception {
		search.setStoreId(SessionUtil.getStoreId());
		return displayCommonService.getTemplateListByType(search);
	}


	
	
	/**
	 * Template Id로 전시코너 목록 조회
	 * @Method Name : getCornerListByTemplateId
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/template/corner/list", method = RequestMethod.POST)
	public List<BaseDmsTemplateDisplay> getTemplateCornerMapList(@RequestBody DmsDisplaySearch search){
		search.setStoreId(SessionUtil.getStoreId());
		return displayCommonService.getTemplateCornerMapList(search);
	}
	
	
	/**
	 * 템플릿-코너 매핑 정보 저장
	 * @Method Name : saveTemplateCornerMap
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param search
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/template/corner/save", method = RequestMethod.POST)
	public void saveTemplateCornerMap(@RequestBody List<BaseDmsTemplateDisplay> list) throws Exception{
		displayCommonService.saveTemplateCornerMap(list);
	}
	
	/**
	 * 템플릿-코너 매핑 정보 삭제
	 * @Method Name : deleteTemplateCornerMap
	 * @author : eddie
	 * @date : 2016. 5. 26.
	 * @description : 
	 *
	 * @param search
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/template/corner/delete", method = RequestMethod.POST)
	public void deleteTemplateCornerMap(@RequestBody List<BaseDmsTemplateDisplay> list) throws Exception{
		displayCommonService.deleteTemplateCornerMap(list);
	}
	
}
