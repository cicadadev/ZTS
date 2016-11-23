package gcp.admin.controller.rest.pms;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.pms.model.PmsAttribute;
import gcp.pms.model.PmsAttributevalue;
import gcp.pms.model.base.BasePmsAttribute;
import gcp.pms.model.search.PmsAttributeSearch;
import gcp.pms.service.AttributeService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/pms/attribute")
public class AttributeController {
	private final Log			logger	= LogFactory.getLog(getClass());

	@Autowired
	private AttributeService attrService;

	/**
	 * @Method Name : getAttributeList
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 속성 목록
	 *
	 * @param pas
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<PmsAttribute> getAttributeList(@RequestBody PmsAttributeSearch pas) {
		pas.setStoreId(SessionUtil.getStoreId());
		List<PmsAttribute> list = attrService.getAttributeList(pas);
		
		return list;
	}

	/**
	 * 그리드 데이터 일괄변경(사용여부)
	 * 
	 * @Method Name : saveGrid
	 * @author : roy
	 * @date : 2016. 10. 11.
	 * @description : 일괄저장한다.
	 *
	 * @param attribut
	 * @return int (변경된 그리드 row 수)
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public void saveGrid(@RequestBody List<PmsAttribute> list) throws Exception {
		attrService.saveAttributes(list);
	}

	/**
	 * @Method Name : getAttributeDetail
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 속성 상세정보
	 *
	 * @param pas
	 * @return
	 */
	@RequestMapping(value = "/popup/detail", method = RequestMethod.POST)
	public BasePmsAttribute getAttributeDetail(@RequestBody PmsAttributeSearch pas) {
		BasePmsAttribute bpa = new BasePmsAttribute();

		try {
			pas.setStoreId(SessionUtil.getStoreId());
			bpa = attrService.getAttributeDetail(pas);
		} catch (ServiceException se) {
			// TODO Auto-generated catch block
			bpa = new BasePmsAttribute();
			bpa.setSuccess(false);
			bpa.setResultCode(se.getMessageCd());
			bpa.setResultMessage(se.getMessage());
		}

		return bpa;
	}

	/**
	 * @Method Name : updateAttribute
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 속성, 속성값 update
	 *
	 * @param pat
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/update", method = RequestMethod.POST)
	public String updateAttribute(@RequestBody PmsAttribute pat) throws Exception {
		pat.setStoreId(SessionUtil.getStoreId());
		return attrService.updateAttribute(pat);
	}

	/**
	 * @Method Name : insertAttribute
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 속성, 속성값 insert
	 *
	 * @param pat
	 * @throws Exception
	 */
	@RequestMapping(value = "/popup/insert", method = RequestMethod.POST)
	public String insertAttribute(@RequestBody PmsAttribute pat) throws Exception {
		pat.setStoreId(SessionUtil.getStoreId());
		return attrService.insertAttribute(pat);
	}
	
	
	/**
	 * 표준카테고리 번호에 해당하는 attribute 목록 조회
	 * 
	 * @Method Name : selectAttributeListByCategoryId
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param categoryId, attributeTypeCd ( ATTR : 단품옵션 , SALEPRODUCT : 복합,단일,입력형 )
	 */
	@RequestMapping(value = "/list/category", method = { RequestMethod.POST })
	public List<PmsAttribute> selectAttributeListByCategoryId(@RequestBody PmsAttributeSearch search) {

		search.setStoreId(SessionUtil.getStoreId());
		return attrService.selectAttributeListByCategoryId(search);
	}

	/**
	 * attribute value 사용여부 수정
	 * 
	 * @Method Name : updateAttributevalue
	 * @author : stella
	 * @date : 2016. 6. 28.
	 * @description :
	 *
	 * @param attributevalue
	 */
	@RequestMapping(value = "/value/update", method = RequestMethod.POST)
	public void updateAttributevalue(@RequestBody List<PmsAttributevalue> pav) throws Exception {
		attrService.updateAttributevalue(pav);
	}
	
}
