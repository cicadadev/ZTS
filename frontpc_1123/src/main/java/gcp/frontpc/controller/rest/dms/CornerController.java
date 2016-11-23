package gcp.frontpc.controller.rest.dms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.dms.model.DmsDisplay;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.dms.service.CornerService;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/dms/corner")
public class CornerController {
	
	private final Log	logger	= LogFactory.getLog(getClass());
	
	@Autowired
	private CornerService	cornerService;


	/**
	 * 프론트 Layout 코너 목록 조회
	 * @Method Name : getLayoutConnerList
	 * @author : emily
	 * @date : 2016. 7. 26.
	 * @description : 
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/connerList", method = RequestMethod.POST)
	public Map<String, DmsDisplay> getLayoutConnerList(HttpServletRequest request) throws Exception {
		
		DmsDisplaySearch seach = new DmsDisplaySearch();
		Map<String, DmsDisplay> returnMap = new HashMap<String, DmsDisplay>();
		
		//공통영역 코너 
		seach.setStoreId(SessionUtil.getStoreId());
		seach.setDisplayId(Config.getString("corner.common.banner.img"));
		List<DmsDisplay> comConerList = cornerService.getChildCornerList(seach);
		if(comConerList != null && comConerList.size() > 0){
			for (DmsDisplay comdisp : comConerList) {
				returnMap.put(comdisp.getDisplayId(), comdisp);			
			}
		}
		
		//월령 코너
		seach.setDisplayId(Config.getString("corner.common.ctg.img.5"));
		List<DmsDisplay> ageConerList = cornerService.getCommonCornerList(seach);
		if(ageConerList != null && ageConerList.size() > 0){
			DmsDisplay corner =  ageConerList.get(0);
			returnMap.put(corner.getDisplayId(), corner);	
		}
		
		seach.setDisplayId(Config.getString("corner.etc.search"));
		List<DmsDisplay> etcList = cornerService.getCommonCornerList(seach);
		if(etcList != null && etcList.size() > 0){
			DmsDisplay corner1 =  etcList.get(0);
			returnMap.put(corner1.getDisplayId(), corner1);	
		}
		
		//debug
		/*Iterator<String> keys2 = returnMap.keySet().iterator();
		while(keys2.hasNext()){
			String key = keys2.next();
			logger.debug("key: "+key);
			logger.debug(", value: "+returnMap.get(key)+'\n');
		}*/
		
		return returnMap;
	}

}
