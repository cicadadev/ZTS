package gcp.oms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.search.OmsOrderSearch;

@Service
public class DeliveryService extends BaseService {
	// private static final Logger logger = LoggerFactory.getLogger(PickupService.class);

	@SuppressWarnings("unchecked")
	public List<?> selectList(String queryId, Object omsOrderSearch) throws Exception {
		return dao.selectList(queryId, omsOrderSearch);
	}

	public OmsDeliveryaddress selectOne(OmsDeliveryaddress omsDeliveryaddress) throws Exception {
		return (OmsDeliveryaddress) dao.selectOneTable(omsDeliveryaddress);
	}

	public Object selectOne(String queryId, Object object) throws Exception {
		return dao.selectOne(queryId, object);
	}

	public int insert(Object object) throws Exception {
		return dao.insertOneTable(object);
	}

	public int update(Object object) throws Exception {
		return dao.updateOneTable(object);
	}

	public int delete(Object object) throws Exception {
		return dao.deleteOneTable(object);
	}

	// public int updatePickupStatus(List<OmsPickupproduct> omsPickupproducts) {
	// return dao.update("oms.delivery.updatePickupStatus", omsPickupproducts);
	// }

}
