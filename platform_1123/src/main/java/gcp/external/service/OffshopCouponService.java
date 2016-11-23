package gcp.external.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.service.BaseService;
import gcp.external.model.coupon.UvMmsOffshopcouponissue;
import gcp.mms.model.search.MmsMemberSearch;
import intune.gsf.common.utils.CommonUtil;

/**
 * @Pagckage Name : gcp.external.service
 * @FileName : OffshopCouponService.java
 * @author : ian
 * @date : 2016. 10. 28.
 * @description : 
 */
@Service("offshopCouponService")
public class OffshopCouponService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(OffshopCouponService.class);

	@SuppressWarnings("unchecked")
	public Map<String,String> getOffshopCouponInfo(MmsMemberSearch search) {
		Map<String,String> map = new HashMap<String,String>();
		List<UvMmsOffshopcouponissue> offshopIssue = (List<UvMmsOffshopcouponissue>) pos
				.selectList("external.pos.getOffshopCouponInfo", search);
		for(UvMmsOffshopcouponissue issue : offshopIssue) {
			if(CommonUtil.isEmpty(issue.getUseCnt())) {
				issue.setUseCnt(0);
			}
			if(CommonUtil.isEmpty(issue.getUseCnt())) {
				issue.setEndCnt(0);
			}
			
			map.put("USE", String.valueOf(issue.getUseCnt()) );
			map.put("END", String.valueOf(issue.getEndCnt()) );
		}
		return map; 
	}
	
	@SuppressWarnings("unchecked")
	public List<UvMmsOffshopcouponissue> getOffshopCouponList(MmsMemberSearch search) {
		return (List<UvMmsOffshopcouponissue>) pos.selectList("external.pos.getOffshopCouponList", search);
	}

	/**
	 * 오프라인 쿠폰 발급 여부
	 * 
	 * @Method Name : isCouponIssued
	 * @author : intune
	 * @date : 2016. 10. 31.
	 * @description : 회원 등급별 오프라인 쿠폰을 기 다운로드 했는지 여부 체크
	 * @param memberNo
	 * @param couponType
	 * @return true : 기발급, false : 미발급
	 */
	public boolean isCouponIssued(String memberNo, String couponType) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("memberNo", memberNo);
		param.put("couponId", couponType);
		String result = (String) pos.selectOne("external.pos.checkIssueCoupon", param);
		return CommonUtil.isEmpty(result) ? false : true;
	}
}

