package gcp.pms.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.pms.model.PmsAttribute;
import gcp.pms.model.PmsAttributevalue;
import gcp.pms.model.search.PmsAttributeSearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;

@Service
public class AttributeService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : getAttributeList
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 속성 목록
	 *
	 * @param pas
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<PmsAttribute> getAttributeList(PmsAttributeSearch pas) throws ServiceException {
		List<PmsAttribute> list = (List<PmsAttribute>) dao.selectList("pms.attribute.getAttributeList", pas);
		return list;
	}

	/**
	 * @Method Name : getAttributeDetail
	 * @author : peter
	 * @date : 2016. 6. 1.
	 * @description : 속성 상세정보
	 *
	 * @param pas
	 * @return
	 * @throws ServiceException
	 */
	public PmsAttribute getAttributeDetail(PmsAttributeSearch pas) throws ServiceException {
		if (null == pas.getAttributeId() || "".equals(pas.getAttributeId())) {
			throw new ServiceException("pms.attribute.error");
		}

		return (PmsAttribute) dao.selectOne("pms.attribute.getAttributeDetail", pas);
	}

	/**
	 * 속성 수정
	 * 
	 * @Method Name : saveAttriButes
	 * @author : roy
	 * @date : 2016. 10. 11.
	 * @description :
	 *
	 * @param reserve
	 * @throws Exception
	 */
	public void saveAttributes(List<PmsAttribute> reserve) throws Exception {
		for (PmsAttribute r : reserve) {
			r.setUpdId(SessionUtil.getLoginId());
			r.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(r);
		}
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
	public String updateAttribute(PmsAttribute pat) throws Exception {
		String storeId = pat.getStoreId();
		String attributeId = pat.getAttributeId();

		//속성테이블 update
		pat.setSortNo(null);
		dao.updateOneTable(pat);

		PmsAttributevalue pav = new PmsAttributevalue();
		pav.setStoreId(storeId);
		pav.setAttributeId(attributeId);
		pav.setUseYn(pat.getUseYn());

		//속성값 테이블 기존데이터 사용여부 update: 해당 속성ID 전체 update
		dao.update("pms.attribute.updateAttributevalue", pav);

		//추가된 속성값이 있으면 속성값 테이블에 insert
		int addValCnt = pat.getPmsAttributevalues().size();
		if (addValCnt > 0) {
			int nextSortNo = pat.getLastSortNo();
			for (PmsAttributevalue pavOne : pat.getPmsAttributevalues()) {
				if (null != pavOne.getAttributeValue() && !"".equals(pavOne.getAttributeValue())) {
					pavOne.setStoreId(storeId);
					pavOne.setAttributeId(attributeId);
					pavOne.setUseYn(pat.getUseYn());
					nextSortNo++;
					BigDecimal bd = new BigDecimal(nextSortNo);
					pavOne.setSortNo(bd);

					dao.insertOneTable(pavOne);
				}
			}
		}

		return attributeId;
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
	public String insertAttribute(PmsAttribute pat) throws Exception {
		String storeId = pat.getStoreId();
		//속성테이블 insert
		pat.setSortNo(null);
		dao.insert("pms.attribute.insertAttribute", pat);
		String attributeId = pat.getAttributeId();

		//속성값 테이블 insert: 속성유형이 입력형이 아닌 경우만 처리
		if (!"ATTRIBUTE_TYPE_CD.INPUT".equals(pat.getAttributeTypeCd())) {
			int addValCnt = pat.getPmsAttributevalues().size();
			if (addValCnt > 0) {
				int sortNo = 0;
				for (PmsAttributevalue pavOne : pat.getPmsAttributevalues()) {
					if (null != pavOne.getAttributeValue() && !"".equals(pavOne.getAttributeValue())) {
						pavOne.setStoreId(storeId);
						pavOne.setAttributeId(attributeId);
						pavOne.setUseYn(pat.getUseYn());
						sortNo++;
						BigDecimal bd = new BigDecimal(sortNo);
						pavOne.setSortNo(bd);

						dao.insertOneTable(pavOne);
					}
				}
			}
		}

		return attributeId;
	}

	/**
	 * 표준카테고리 번호에 해당하는 attribute 목록 조회
	 * 
	 * @Method Name : selectAttributeListByCategoryId
	 * @author : eddie
	 * @date : 2016. 6. 14.
	 * @description :
	 *
	 * @param search
	 * @return
	 */
	public List<PmsAttribute> selectAttributeListByCategoryId(PmsAttributeSearch search) {
		return (List<PmsAttribute>) dao.selectList("pms.attribute.selectAttributeListByCategoryId", search);
	}
	
	/**
	 * attribute value 사용여부 수정
	 * 
	 * @Method Name : updateAttributevalue
	 * @author : stella
	 * @date : 2016. 6. 28.
	 * @description :
	 *
	 * @param pav
	 */
	public void updateAttributevalue(List<PmsAttributevalue> pav) throws Exception {
		for (PmsAttributevalue attributevalue : pav) {
			attributevalue.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(attributevalue);
		}
	}
	
}
