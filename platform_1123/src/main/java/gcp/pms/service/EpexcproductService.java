package gcp.pms.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.pms.model.PmsEpexcproduct;
import gcp.pms.model.search.PmsEpexcproductSearch;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;

@Service
public class EpexcproductService extends BaseService {
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : getEpexcproductList
	 * @author : peter
	 * @date : 2016. 6. 9.
	 * @description : 외부 비노출 상품 목록
	 *
	 * @param pes
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<PmsEpexcproduct> getEpexcproductList(PmsEpexcproductSearch pes) throws ServiceException {
		// 상품 번호 검색시 다른 조건 무시: 상품은 이 기능 제거
//		if (CommonUtil.isNotEmpty(pes.getProductIds())) {
//			String temp = pes.getProductIds();
//			pes = new PmsEpexcproductSearch();
//			pes.setProductIds(temp);
//		}
		pes.setStoreId(SessionUtil.getStoreId());
		return (List<PmsEpexcproduct>) dao.selectList("pms.epexcproduct.getEpexcproductList", pes);
	}

	/**
	 * @Method Name : getExistCount
	 * @author : peter
	 * @date : 2016. 6. 20.
	 * @description : 업체등록이나 상품등록시 DB에 기 존재하는지 조회
	 *
	 * @param pes
	 * @return
	 * @throws ServiceException
	 */
	public String getExistCount(PmsEpexcproductSearch pes) throws ServiceException {
		String existYn = "N";
		int dataCnt = (int) dao.selectOne("pms.epexcproduct.getExistCount", pes);
		if (dataCnt > 0) {
			existYn = "Y";
		}
		return existYn;
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
	public void insertEpexcproduct(PmsEpexcproduct pep) throws Exception {
		dao.insertOneTable(pep);
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
	public void deleteEpexcproduct(List<PmsEpexcproduct> peList) throws Exception {
		for (PmsEpexcproduct peOne : peList) {
			peOne.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(peOne);
		}
	}
}
