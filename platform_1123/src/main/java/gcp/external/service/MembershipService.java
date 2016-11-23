package gcp.external.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gcp.ccs.service.BaseService;
import gcp.external.model.membership.MemberPoint;
import gcp.external.model.membership.MembershipBase;
import gcp.external.model.membership.MembershipPointRes;
import gcp.external.model.membership.MembershipPointSearchReq;
import gcp.external.model.membership.MembershipPointSearchRes;
import gcp.external.model.membership.MembershipSavePointReq;
import gcp.external.model.membership.MembershipUsePointReq;
import gcp.oms.model.OmsPointif;
import gcp.oms.model.base.BaseOmsPointif;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.JsonUtil;
import intune.gsf.common.utils.SessionUtil;

/**
 * 통합 멤버십 서비스
 * 
 * @Pagckage Name : gcp.external.service
 * @FileName : MembershipService.java
 * @author : eddie
 * @date : 2016. 8. 20.
 * @description :
 */
@Service("membershipService")
public class MembershipService extends BaseService {

	private static final Logger	logger				= LoggerFactory.getLogger(MembershipService.class);

//	private static String		API_DOMAIN		= Config.getString("membership.domain");


	private static String		endpoint_use	= "/maeilmembership/useMemberPoint";
	private static String		endpoint_save	= "/maeilmembership/saveMemberPoint";
	private static String		endpoint_search	= "/maeilmembership/getMemberPoint";

	/**
	 * 포인트 사용/사용취소 ( 매일 멤버십 API )
	 * 
	 * @Method Name : updateMemberPoint
	 * @author : eddie
	 * @date : 2016. 8. 20.
	 * @description :
	 *
	 * @param req
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public MembershipPointRes useMemberPoint(MembershipUsePointReq req) throws JsonProcessingException {

		req.setTrscTypCd("300");
		req.setTrscOrgnDvCd("10");// 거래발생구분코드 : 정상
		req.setBrndCd("3010");
		req.setStorNo("3010");

		ObjectMapper mapper = new ObjectMapper();
		String jsonParam = mapper.writeValueAsString(req);

//		MembershipPointRes res = (MembershipPointRes) JsonUtil.Json2ObjectUrl(MembershipPointRes.class, jsonParam,
//				Config.getString("membership.domain") + "/" + endpoint_save);

		MembershipPointRes res = (MembershipPointRes) JsonUtil.Json2ObjectUrl(MembershipPointRes.class, jsonParam,
				Config.getString("membership.domain") + "/" + endpoint_use);
		try {
			// 포인트 거래정보 db insert
			BaseOmsPointif saveif = new BaseOmsPointif();
			saveif.setTrscBizDvCd(req.getTrscBizDvCd());
			saveif.setTrscTypCd(req.getTrscTypCd());
			saveif.setMmbCertDvCd(req.getMmbCertDvCd());
			saveif.setMmbCertDvVlu(req.getMmbCertDvVlu());
			saveif.setPintUseTypCd(req.getPintUseTypCd());
			saveif.setUsePint(req.getUsePint());
			saveif.setUniqRcgnNo(req.getUniqRcgnNo());
//		saveif.setPintAcmlTypCd(req.getPintAcmlTypCd());
			saveif.setOrgApvDt(res.getApvDt());
			saveif.setOrgApvNo(res.getApvNo());
			saveif.setOrgUniqRcgnNo(req.getUniqRcgnNo());
			saveif.setTotSelAmt(req.getTotSelAmt());
//		saveif.setAcmlTgtAmt(req.getAcmlTgtAmt());
//		saveif.setAcmlPint(req.getAcmlPint());
			saveif.setTrscDt(req.getTrscDt());
			saveif.setTrscHr(req.getTrscHr());
			saveif.setPointIfReqData(jsonParam);

			saveif.setUpdId(CommonUtil.isNotEmpty(SessionUtil.getMemberNo()) ? SessionUtil.getMemberNo().toString() : "system");

			String jsonResult = mapper.writeValueAsString(res);
			saveif.setPointIfReturnData(jsonResult);

			dao.insertOneTable(saveif);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;

	}

	/**
	 * 포인트 적립/적립취소 ( 매일 멤버십 API )
	 * 
	 * @Method Name : updateMemberPoint
	 * @author : eddie
	 * @date : 2016. 8. 20.
	 * @description : resCd 가 00000 이면 성공
	 * @param req
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public MembershipPointRes saveMemberPoint(MembershipSavePointReq req) throws JsonProcessingException {

		req.setTrscTypCd("200");

		req.setValdTrmDvCd("1");//유효기간구분(1:개월수)
		req.setValdTrm("24");//유효기간(24개월고정)
		req.setTrscOrgnDvCd("10");// 거래발생구분코드 : 정상

		req.setBrndCd("3010");
		req.setStorNo("3010");

		ObjectMapper mapper = new ObjectMapper();
		String jsonParam = mapper.writeValueAsString(req);

//		MembershipPointRes res = (MembershipPointRes) JsonUtil.Json2ObjectUrl(MembershipPointRes.class, jsonParam,
//				Config.getString("membership.domain") + "/" + endpoint_save);

		MembershipPointRes res = (MembershipPointRes) JsonUtil.Json2ObjectUrl(MembershipPointRes.class, jsonParam,
				Config.getString("membership.domain") + "/" + endpoint_save);

		try {
			// 포인트 거래정보 db insert
			OmsPointif saveif = new OmsPointif();
			saveif.setTrscBizDvCd(req.getTrscBizDvCd());
			saveif.setTrscTypCd(req.getTrscTypCd());
			saveif.setMmbCertDvCd(req.getMmbCertDvCd());
			saveif.setMmbCertDvVlu(req.getMmbCertDvVlu());
			saveif.setPintUseTypCd(req.getPintUseTypCd());
			saveif.setUsePint(req.getUsePint());
			saveif.setUniqRcgnNo(req.getUniqRcgnNo());
			saveif.setPintAcmlTypCd(req.getPintAcmlTypCd());
			saveif.setOrgApvDt(res.getApvDt());
			saveif.setOrgApvNo(res.getApvNo());
			saveif.setOrgUniqRcgnNo(req.getUniqRcgnNo());
			saveif.setTotSelAmt(req.getTotSelAmt());
			saveif.setAcmlTgtAmt(req.getAcmlTgtAmt());
			saveif.setAcmlPint(req.getAcmlPint());
			saveif.setTrscDt(req.getTrscDt());
			saveif.setTrscHr(req.getTrscHr());
			saveif.setPointIfReqData(jsonParam);

			saveif.setUpdId(CommonUtil.isNotEmpty(SessionUtil.getMemberNo()) ? SessionUtil.getMemberNo().toString() : "system");

			String jsonResult = mapper.writeValueAsString(res);
			saveif.setPointIfReturnData(jsonResult);

//			dao.insertOneTable(saveif);
			dao.insert("oms.pointif.insertPointIf", saveif);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * 회원 포인트 정보 조회( 매일 멤버십 API )
	 * 
	 * @Method Name : getMemberPoint
	 * @author : eddie
	 * @date : 2016. 8. 22.
	 * @description :
	 *
	 * @param req
	 * @return
	 * @throws JsonProcessingException
	 */
	public MemberPoint getMemberPoint(MembershipPointSearchReq req) throws JsonProcessingException {

		req.setTrscTypCd("100"); // 100 : 포인트 조회 요청 
		req.setTrscBizDvCd("11");// 11 : 포인트 조회
		req.setBrndCd("3010");
		req.setStorNo("3010");

		MembershipPointSearchRes res = new MembershipPointSearchRes();
		MemberPoint result = new MemberPoint();
		result.setRmndPint("0");
		try {
//			setCommonParam(req);

			ObjectMapper mapper = new ObjectMapper();

			String jsonParam = mapper.writeValueAsString(req);

//			System.out.println(jsonParam);


//			res = (MembershipPointSearchRes) JsonUtil.Json2ObjectUrl(MembershipPointSearchRes.class, jsonParam,
//					Config.getString("membership.domain") + endpoint_search);

			res = (MembershipPointSearchRes) JsonUtil.Json2ObjectUrl(MembershipPointSearchRes.class, jsonParam,
					Config.getString("membership.domain") + endpoint_search);

			List<MemberPoint> results = res.getResults();

			if (results != null && results.size() > 0) {
				result = results.get(0);
			}

		} catch (Exception e) {

			e.printStackTrace();

			res = new MembershipPointSearchRes();
			res.setResCd("00023");

		}

		return result;
	}

	private void setCommonParam(MembershipBase req) {




	}

	private void cancelPointSet(MembershipSavePointReq req) {
		// 취소공통
		req.setTrscBizDvCd("22");// 21:포인트적립거래, 22:포인트적립거래취소

		// 취소 데이터
		req.setMmbCertDvCd("2");// 조회 유형 회원번호(필수)
		req.setMmbCertDvVlu("1000000098");// 회원번호(필수)
		req.setPintUseTypCd("11");//포인트사용유형코드, 일반구매(옵션)
		req.setUniqRcgnNo("00000000001");//고유관리번호, 주문번호/취소번호등(필수)
		req.setPintAcmlTypCd("10");//포인트적립유형코드(필수, 10’- 일반, ‘20’- 이벤트) 

		// 원거래정보
		req.setOrgApvDt("20161013");//원통합승인일자
		req.setOrgApvNo("7000000469");//원통합승인번호
		req.setOrgUniqRcgnNo("11111111113");//원관계사고유식별번호

//		req.setTotSelAmt("500");//총매출금액 ( 구매금액 )(필수)
//		req.setAcmlTgtAmt("55");//TODO 포인트적립대상금액 (정책확인)
//		req.setAcmlPint("3533");//적립포인트(필수)

		req.setTrscDt("20161013");//관계사기준 매출/결제일자(YYYYMMDD)(필수)
		req.setTrscHr("212524");//관계사기준 매출/결제시간(HH24MISS)(필수)
	}

	private void savePointSet(MembershipSavePointReq req) {
		// 적립공통
		req.setTrscBizDvCd("21");// 21:포인트적립거래, 22:포인트적립거래취소

		//적립 데이터

		req.setTrscRsnCd("RS01");//거래사유코드 RS01:0to7 쇼핑몰 상품구매,RS02:상품평 작성(0to7), RS07:첫구매 상품평 작성(0to7)
		req.setMmbCertDvCd("2");// 조회 유형 회원번호(필수)
		req.setMmbCertDvVlu("1000000005");// 회원번호(필수)
		req.setTotSelAmt("30000");//총매출금액 ( 구매금액 )(필수)
		req.setTotDcAmt("0");// 총할인금액 (쿠폰할인금액)(옵션)
		req.setMbrshDcAmt("0");// 멤버십할인금액(옵션)
		req.setAcmlTgtAmt("0");//TODO 포인트적립대상금액 (정책확인)
		req.setMmbBabySeqNo(null);//아기일련번호(옵션)
		req.setPrdctCd(null);//분유제품코드(옵션)
		req.setAftacmlYn("N");//사후적립여부(필수:N)
		req.setMbrshpintSetlYn("N");//멤버십포인트결제여부(필수:N)
		req.setPintUseTypCd("11");//포인트사용유형코드, 일반구매(옵션)
		req.setUsePint("0");//사용포인트 (한거래를 사용요청 후 적립요청을 할 경우 Setting)(옵션)
		req.setUniqRcgnNo("11111111113");//고유관리번호, 주문번호/취소번호등(필수)

		req.setPintAcmlTypCd("10");//포인트적립유형코드(필수, 10’- 일반, ‘20’- 이벤트) 
		req.setAcmlPint("9999999");//적립포인트(필수)
		req.setRmk(null);//비고(옵션)
	}

	// 포인트 사용
	private void usePointSet(MembershipUsePointReq req) {
		// 사용공통
		req.setTrscBizDvCd("31");// 21:포인트사용거래, 22:포인트사용거래취소

		//사용 데이터
		req.setTrscRsnCd("CS01");//거래사유코드 CS01:0to7 쇼핑몰 상품구매
		req.setMmbCertDvCd("2");// 조회 유형 회원번호(필수)
		req.setMmbCertDvVlu("1000000098");// 회원번호(필수)
		req.setTotSelAmt("30000");//총매출금액 ( 구매금액 )(필수)
		req.setTotDcAmt("0");// 총할인금액 (쿠폰할인금액)(옵션)
		req.setMbrshDcAmt("0");// 멤버십할인금액(옵션)
		req.setPintUseTypCd("11");//포인트사용유형코드, 일반구매(옵션)
		req.setUsePint("0");//사용포인트 (한거래를 사용요청 후 적립요청을 할 경우 Setting)(옵션)
		req.setUniqRcgnNo("11111111113");//고유관리번호, 주문번호/취소번호등(필수)

		req.setPintUseTypCd("11");//포인트사용유형코드(필수, 11’- 일반, ‘21’- 이벤트) 
		req.setUsePint("300");//사용포인트(필수)
		req.setRmk(null);//비고(옵션)
	}

	public static void main(String[] args) {

		MembershipService s = new MembershipService();

		//포인트 조회
//		MembershipPointSearchReq req = new MembershipPointSearchReq();

		// 데이터 
//		req.setSrchDv("I");//조회구분‘I’- 통합회원번호, ‘C’- 멤버십카드번호 ‘T’- 휴대전화번호
//		req.setSrchDvVlu("1000000005");//조회구분값(test1)
//
//		try {
//
//			MemberPoint res = s.getMemberPoint(req);
//			System.out.println(res);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}

		// 포이트 적립/취소
		MembershipSavePointReq req = new MembershipSavePointReq();
		s.savePointSet(req);

		try {

			MembershipPointRes res = s.saveMemberPoint(req);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		s.cancelPointSet(req);

		//포인트사용
//		MembershipUsePointReq req = new MembershipUsePointReq();
//		s.usePointSet(req);
//		try {
//
//			MembershipPointRes res = s.useMemberPoint(req);
//			System.out.println(res);
//
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}
