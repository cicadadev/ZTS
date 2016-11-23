package gcp.ccs.service;

import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsPolicy;

/**
 * 
 * @Pagckage Name : gcp.ccs.service
 * @FileName : PolicyService.java
 * @author : dennis
 * @date : 2016. 6. 29.
 * @description : policy service
 */
@Service
public class PolicyService extends BaseService {

	/**
	 * 
	 * @Method Name : getPolicy
	 * @author : dennis
	 * @date : 2016. 6. 29.
	 * @description : 정책 조회
	 *
	 * @param ccsPolicy
	 * @return
	 */
	public CcsPolicy getPolicy(CcsPolicy ccsPolicy) {
		return (CcsPolicy) dao.selectOne("ccs.policy.getPolicy", ccsPolicy);
	}
}
