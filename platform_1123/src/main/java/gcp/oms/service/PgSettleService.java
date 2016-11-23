package gcp.oms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.oms.model.OmsPaymentsettle;
import gcp.oms.model.search.OmsPgSettleSearch;

@Service
public class PgSettleService extends BaseService {
	@SuppressWarnings("unchecked")
	public List<OmsPaymentsettle> getPgSettleList(OmsPgSettleSearch opss) {
		return (List<OmsPaymentsettle>) dao.selectList("oms.pgsettle.getPgSettleList", opss);
	}
}
