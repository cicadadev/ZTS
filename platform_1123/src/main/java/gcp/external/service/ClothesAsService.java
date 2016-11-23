package gcp.external.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.ClothesAfterService;
import gcp.mms.model.search.MmsMemberSearch;

@Service("clothesAsService")
public class ClothesAsService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(ClothesAsService.class);
	
	/**
	 * @Method Name : getClothesAfterServiceList
	 * @author : ian
	 * @date : 2016. 11. 9.
	 * @description : as의류 현황 
	 *
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ClothesAfterService> getClothesAfterServiceList(MmsMemberSearch search) {
		return (List<ClothesAfterService>) pos.selectList("external.pos.getClothesAfterServiceList", search);
	}
}

