package gcp.admin.controller.imapi;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.open.api.sdk.internal.JSON.JSON;

import gcp.mms.model.MmsMemberbaby;
import gcp.mms.model.MmsMembersns;
import gcp.mms.model.base.BaseMmsMember;
import gcp.mms.service.MemberService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("imapi")
public class ImRxController {

	public static String	SUCCESS_MSG		= "성공적으로 수행 되었습니다";

	public static String	COOPCO_CD		= "7020";
	public static String	SITE_CD		= "7020";
	public static String	SUCCESS_CD	= "S001" + SITE_CD;
	public static String	FAIL_CD_E001	= "E001" + SITE_CD;																		//결과값 없음
	public static String	FAIL_CD_E007	= "E007" + SITE_CD;																								//params누락
	public static String	FAIL_CD_E009	= "E009" + SITE_CD;																		//알수없는 에러

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private MemberService	memberService;

	private ModelMap convertMapToJson(BaseMmsMember member) throws ParseException {
		
		
		ModelMap resMap = new ModelMap();
		resMap.put("UNFY_MMB_NO", member.getMemberNo().toString());
		resMap.put("CUST_NO", member.getCustomerNo());
		resMap.put("MMB_NM", member.getMemberName());
		resMap.put("MMB_ID", member.getMemberId());
		resMap.put("FRNR_DV_CD", "Y".equals(member.getForeignerYn()) ? "2" : "1");

		if (member.getBirthday() != null) {
			resMap.put("BTDY", DateUtil.convertFormat(member.getBirthday(), DateUtil.FORMAT_10, DateUtil.FORMAT_3));
		} else {
			resMap.put("BTDY", null);
		}

		resMap.put("BTDY_LUCR_SOCR_DV_CD", "Y".equals(member.getLunarYn()) ? "2" : "1");

		if ("GENDER_CD.MALE1".equals(member.getGenderCd())) {
			resMap.put("GNDR_DV_CD", "1");
		} else if ("GENDER_CD.MALE3".equals(member.getGenderCd())) {
			resMap.put("GNDR_DV_CD", "3");
		} else if ("GENDER_CD.FEMALE2".equals(member.getGenderCd())) {
			resMap.put("GNDR_DV_CD", "2");
		} else if ("GENDER_CD.FEMALE4".equals(member.getGenderCd())) {
			resMap.put("GNDR_DV_CD", "4");
		}
		
		resMap.put("SEF_CERT_DI", member.getDiscrhash());
		resMap.put("SEF_CERT_CI_VER", member.getCiversion());
		resMap.put("SEF_CERT_CI", member.getCiscrhash());
		resMap.put("SEF_CERT_DV_CD", member.getCertDivCd());


		if ("DEVICE_TYPE_CD.PC".equals(member.getDeviceTypeCd())) {
			resMap.put("NTRY_CHNL_CD", "3");
		} else if ("DEVICE_TYPE_CD.MW".equals(member.getDeviceTypeCd())) {
			resMap.put("NTRY_CHNL_CD", "41");
		} else if ("DEVICE_TYPE_CD.APP".equals(member.getDeviceTypeCd())) {
			resMap.put("NTRY_CHNL_CD", "42");
		}

		resMap.put("NTRY_PATH", member.getRegChannelUrl());

		if ("MEMBER_STATE_CD.NORMAL".equals(member.getMemberStateCd())) {
			resMap.put("MMB_ST_CD", "1");
		} else if ("MEMBER_STATE_CD.SLEEP".equals(member.getMemberStateCd())) {
			resMap.put("MMB_ST_CD", "9");
		} else if ("MEMBER_STATE_CD.WITHDRAW".equals(member.getMemberStateCd())) {
			resMap.put("MMB_ST_CD", "0");
		}

		resMap.put("SOC_ID", member.getSnsId());

		if ("SNS_CHANNEL_CD.KAKAO".equals(member.getSnsChannelCd())) {
			resMap.put("SOC_KIND_CD", "K");
		} else if ("SNS_CHANNEL_CD.NAVER".equals(member.getSnsChannelCd())) {
			resMap.put("SOC_KIND_CD", "N");
		} else if ("SNS_CHANNEL_CD.FACEBOOK".equals(member.getSnsChannelCd())) {
			resMap.put("SOC_KIND_CD", "F");
		}

		resMap.put("SOC_MMB_YN", member.getSnsMemberYn());
		resMap.put("PREM_MMB_YN", member.getPremiumYn());
		resMap.put("PREM_MMB_NTRY_DTM", member.getPremiumRegDt());

		if ("N".equals(member.getEmployeeYn())) {
			resMap.put("STFF_DV_CD", "0");
		} else if ("Y".equals(member.getEmployeeYn())) {
			resMap.put("STFF_DV_CD", "1");
		}

		resMap.put("STFF_EML_ADDR", member.getEmployeeEmail());
		resMap.put("STFF_CERT_DT", member.getEmployeeRegDt());

		if ("REG_TYPE_CD.NEW".equals(member.getRegTypeCd())) {
			resMap.put("NTRY_TYP_CD", "10");
		} else if ("REG_TYPE_CD.SWITCH".equals(member.getRegTypeCd())) {
			resMap.put("NTRY_TYP_CD", "11");
		}

		resMap.put("COOPCO_MMB_ID", member.getSiteMemberId());
		resMap.put("WRLS_TEL_NO", member.getPhone2());
		
		resMap.put("CBL_TEL_NO", member.getPhone1());
		resMap.put("EML_ADDR", member.getEmail());
		resMap.put("CRD_NO", member.getCardIssueNo());

		if (member.getCardIssueDt() != null) {
			resMap.put("CRD_REG_DTM", DateUtil.convertFormat(member.getCardIssueDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10));
		} else {
			resMap.put("CRD_REG_DTM", null);
		}


		if (member.getRegDt() != null) {
			resMap.put("REG_DTM", DateUtil.convertFormat(member.getRegDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10));
		} else {
			resMap.put("REG_DTM", null);
		}

		resMap.put("ASCT_MMB_NO", member.getOffshopMemberNo());
		resMap.put("STOR_CD", member.getOffshopId());
		resMap.put("STOR_CRD_NO", member.getOffshopCardNo());
		
		resMap.put("STOR_PINT_SWT_YN", member.getOffshopPointSwitchYn());
//		resMap.put("ZIP_NO", member.getMemberNo().toString());
//		resMap.put("ZIP_SEQ", member.getMemberNo().toString());
//		resMap.put("ZIP_ZONE_NO", member.getMemberNo().toString());
//		resMap.put("ZIP_BASE_ADDR", member.getMemberNo().toString());
//		resMap.put("ZIP_DTLS_ADDR", member.getMemberNo().toString());
//		resMap.put("ROZIP_NO", member.getMemberNo().toString());
//		
//		resMap.put("ROZIP_SEQ", member.getMemberNo().toString());
//		resMap.put("ROZIP_ZONE_NO", member.getMemberNo().toString());
//		resMap.put("ROZIP_BASE_ADDR", member.getMemberNo().toString());
//		resMap.put("ROZIP_DTLS_ADDR", member.getMemberNo().toString());
//		resMap.put("ROZIP_REFN_ADDR", member.getMemberNo().toString());
//		resMap.put("ROZIP_BLD_NO", member.getMemberNo().toString());
//		resMap.put("ROZIP_PNU", member.getMemberNo().toString());
//		resMap.put("STTR_DONG_CD", member.getMemberNo().toString());
//		resMap.put("ADMST_DONG_CD", member.getMemberNo().toString());
//		resMap.put("PSN_X_CORD", member.getMemberNo().toString());
//		resMap.put("PSN_Y_CORD", member.getMemberNo().toString());
		
		resMap.put("AGRM_YN", member.getAgreeYn());

		if (member.getAgreeDt() != null) {
			resMap.put("AGRM_DTHR", DateUtil.convertFormat(member.getAgreeDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10));
		} else {
			resMap.put("AGRM_DTHR", null);
		}

		if (member.getAgreeWithdrawDt() != null) {
			resMap.put("AGRM_END_DTHR",
					DateUtil.convertFormat(member.getAgreeWithdrawDt(), DateUtil.FORMAT_1, DateUtil.FORMAT_10));
		}else{
			resMap.put("AGRM_END_DTHR", null);
		}
		if ("Y".equals(member.getEmailYn())) {
			resMap.put("EML_RECV_DV_CD", "1");
		} else {
			resMap.put("EML_RECV_DV_CD", "0");
		}
		
		if ("Y".equals(member.getSmsYn())) {
			resMap.put("SMS_RECV_DV_CD", "1");
		} else {
			resMap.put("SMS_RECV_DV_CD", "0");
		}

		if ("Y".equals(member.getAppPushYn())) {
			resMap.put("APP_PUSH_RECV_DV_CD", "1");
		} else {
			resMap.put("APP_PUSH_RECV_DV_CD", "0");
		}

		List<ModelMap> babyInfos = new ArrayList<ModelMap>();
		if (member.getMmsMemberbabys() != null) {
			for (MmsMemberbaby baby : member.getMmsMemberbabys()) {
				ModelMap babyInfo = new ModelMap();

				babyInfo.put("SEQ_NO", baby.getBabyNo());
				babyInfo.put("BABY_NM", baby.getBabyName());
				babyInfo.put("BABY_SEQ", baby.getBabyOrder());
				babyInfo.put("TWIN_DV_CD", baby.getTwinYn());

				if (baby.getBirthday() != null) {
					babyInfo.put("BTDY", DateUtil.convertFormat(baby.getBirthday(), DateUtil.FORMAT_10, DateUtil.FORMAT_3));
				} else {
					babyInfo.put("BTDY", null);
				}

				babyInfo.put("BTDY_LUCR_SOCR_DV_CD", "Y".equals(baby.getLunarYn()) ? "2" : "1");

				if ("BABY_GENDER_CD.BOY".equals(baby.getBabyGenderCd())) {
					babyInfo.put("BABY_GNDR_DV_CD", "1");
				} else if ("BABY_GENDER_CD.GIRL".equals(baby.getBabyGenderCd())) {
					babyInfo.put("BABY_GNDR_DV_CD", "3");
				} else if ("BABY_GENDER_CD.UNKNOWN".equals(baby.getBabyGenderCd())) {
					babyInfo.put("BABY_GNDR_DV_CD", "2");
				}

				if ("FEED_TYPE_CD.MILK".equals(baby.getFeedTypeCd())) {
					babyInfo.put("FEDG_TYP_CD", "001");
				} else if ("FEED_TYPE_CD.BABYFOOD".equals(baby.getFeedTypeCd())) {
					babyInfo.put("FEDG_TYP_CD", "002");
				} else if ("FEED_TYPE_CD.MOTHER".equals(baby.getFeedTypeCd())) {
					babyInfo.put("FEDG_TYP_CD", "003");
				} else if ("FEED_TYPE_CD.MIX".equals(baby.getFeedTypeCd())) {
					babyInfo.put("FEDG_TYP_CD", "004");
				} else if ("FEED_TYPE_CD.END".equals(baby.getFeedTypeCd())) {
					babyInfo.put("FEDG_TYP_CD", "007");
				}

				babyInfo.put("USE_YN", baby.getUseYn());

				babyInfos.add(babyInfo);
			}
		}
		resMap.put("MMB_BABY", babyInfos);

		List<ModelMap> socInfos = new ArrayList<ModelMap>();
		if (member.getMmsMembersnss() != null) {

			for (MmsMembersns memberSns : member.getMmsMembersnss()) {
				ModelMap socInfo = new ModelMap();

				socInfo.put("SOC_UNFY_MMB_NO", memberSns.getSnsMemberNo().toString());

				if ("SNS_CHANNEL_CD.KAKAO".equals(memberSns.getSnsChannelCd())) {
					socInfo.put("SOC_KIND_CD", "K");
				} else if ("SNS_CHANNEL_CD.NAVER".equals(memberSns.getSnsChannelCd())) {
					socInfo.put("SOC_KIND_CD", "N");
				} else if ("SNS_CHANNEL_CD.FACEBOOK".equals(memberSns.getSnsChannelCd())) {
					socInfo.put("SOC_KIND_CD", "F");
				}

				socInfo.put("USE_YN", memberSns.getUseYn());

				socInfos.add(socInfo);

			}
		}
		resMap.put("SOC_INFO", socInfos);

		return resMap;
	}

	private BaseMmsMember convertJsonToMap(HashMap<String, Object> reqMap) {

		BaseMmsMember member = new BaseMmsMember();

		HashMap<String, Object> rs = (HashMap<String, Object>) reqMap.get("IM_SBC");

		member.setMemberNo(new BigDecimal((String) rs.get("UNFY_MMB_NO")));
		member.setCustomerNo((String) rs.get("CUST_NO"));

		if ("1".equals((String) rs.get("MMB_ST_CD"))) {
			member.setMemberStateCd("MEMBER_STATE_CD.NORMAL");
		} else if ("9".equals((String) rs.get("MMB_ST_CD"))) {
			member.setMemberStateCd("MEMBER_STATE_CD.SLEEP");
		} else if ("0".equals((String) rs.get("MMB_ST_CD"))) {
			member.setMemberStateCd("MEMBER_STATE_CD.WITHDRAW");
		}

		member.setMemberId((String) rs.get("MMB_ID"));

		// sns_member_yn = 'Y'일경우
		if ("Y".equals((String) rs.get("SOC_MMB_YN"))) {
			member.setMemberId((String) rs.get("SOC_ID"));
		}

		member.setMemberName((String) rs.get("MMB_NM"));
		member.setRegChannelUrl((String) rs.get("NTRY_PATH"));
		member.setCardIssueNo((String) rs.get("CRD_NO"));
		member.setCardIssueDt((String) rs.get("CRD_REG_DTM"));
		member.setRegDt((String) rs.get("REG_DTM"));

		if ("1".equals((String) rs.get("FRNR_DV_CD"))) {//내국인
			member.setForeignerYn("N");
		} else if ("2".equals((String) rs.get("FRNR_DV_CD"))) {//외국인
			member.setForeignerYn("N");
		} else {
			member.setForeignerYn(null);
		}

		if (rs.get("BTDY") != null && !rs.get("BTDY").toString().equals("null")) {
			member.setBirthday((String) rs.get("BTDY") + "000000");
		}

		if ("1".equals((String) rs.get("BTDY_LUCR_SOCR_DV_CD"))) {
			member.setLunarYn("N");//양력
		} else if ("2".equals((String) rs.get("BTDY_LUCR_SOCR_DV_CD"))) {
			member.setLunarYn("Y");//음력
		}

		if ("1".equals((String) rs.get("GNDR_DV_CD"))) {
			member.setGenderCd("GENDER_CD.MALE1");
		} else if ("3".equals((String) rs.get("GNDR_DV_CD"))) {
			member.setGenderCd("GENDER_CD.MALE3");
		} else if ("2".equals((String) rs.get("GNDR_DV_CD"))) {
			member.setGenderCd("GENDER_CD.FEMALE2");
		} else if ("4".equals((String) rs.get("GNDR_DV_CD"))) {
			member.setGenderCd("GENDER_CD.FEMALE4");
		}

		member.setDiscrhash((String) rs.get("SEF_CERT_DI"));
		member.setCiversion((String) rs.get("SEF_CERT_CI_VER"));
		member.setCiscrhash((String) rs.get("SEF_CERT_CI"));

		if ("0".equals((String) rs.get("SEF_CERT_DV_CD"))) {
			member.setCertDivCd("CERT_DIV_CD.NOTCERT");
		} else if ("1".equals((String) rs.get("SEF_CERT_DV_CD"))) {
			member.setCertDivCd("CERT_DIV_CD.PHONE");
		} else if ("2".equals((String) rs.get("SEF_CERT_DV_CD"))) {
			member.setCertDivCd("CERT_DIV_CD.IPIN");
		}

		member.setPremiumYn((String) rs.get("PREM_MMB_YN"));
		member.setPremiumRegDt((String) rs.get("PREM_MMB_NTRY_DTM"));

		if ("0".equals((String) rs.get("STFF_DV_CD"))) {
			member.setEmployeeYn("N");
		} else if ("1".equals((String) rs.get("STFF_DV_CD"))) {
			member.setEmployeeYn("Y");
		}

		member.setPhone2((String) rs.get("WRLS_TEL_NO"));
		member.setPhone1((String) rs.get("CBL_TEL_NO"));
		member.setEmail((String) rs.get("EML_ADDR"));

		if ("1".equals((String) rs.get("EML_RECV_DV_CD"))) {
			member.setEmailYn("Y");
		} else {
			member.setEmailYn("N");
		}
		if ("1".equals((String) rs.get("SMS_RECV_DV_CD"))) {
			member.setSmsYn("Y");
		} else {
			member.setSmsYn("N");
		}
		if ("1".equals((String) rs.get("APP_PUSH_RECV_DV_CD"))) {
			member.setAppPushYn("Y");
		} else {
			member.setAppPushYn("N");
		}

		member.setSnsId((String) rs.get("SOC_ID"));

		if ("K".equals((String) rs.get("SOC_KIND_CD"))) {
			member.setSnsChannelCd("SNS_CHANNEL_CD.KAKAO");
		} else if ("N".equals((String) rs.get("SOC_KIND_CD"))) {
			member.setSnsChannelCd("SNS_CHANNEL_CD.NAVER");
		} else if ("F".equals((String) rs.get("SOC_KIND_CD"))) {
			member.setSnsChannelCd("SNS_CHANNEL_CD.FACEBOOK");
		}

		member.setOffshopId((String) rs.get("STOR_CD"));

		if ("3".equals((String) rs.get("NTRY_CHNL_CD"))) {
			member.setDeviceTypeCd("DEVICE_TYPE_CD.PC");
		} else if ("41".equals((String) rs.get("NTRY_CHNL_CD"))) {
			member.setDeviceTypeCd("DEVICE_TYPE_CD.MW");
		} else if ("42".equals((String) rs.get("NTRY_CHNL_CD"))) {
			member.setDeviceTypeCd("DEVICE_TYPE_CD.APP");
		}

		member.setSnsMemberYn((String) rs.get("SOC_MMB_YN"));
		member.setEmployeeEmail((String) rs.get("STFF_EML_ADDR"));
		member.setEmployeeRegDt((String) rs.get("STFF_CERT_DT"));

		if ("10".equals((String) rs.get("NTRY_TYP_CD"))) {
			member.setRegTypeCd("REG_TYPE_CD.NEW");
		} else if ("11".equals((String) rs.get("NTRY_TYP_CD"))) {
			member.setRegTypeCd("REG_TYPE_CD.SWITCH");
		}

		member.setAgreeYn((String) rs.get("AGRM_YN"));
		member.setAgreeDt((String) rs.get("AGRM_DTHR"));
		member.setAgreeWithdrawDt((String) rs.get("AGRM_END_DTHR"));
		member.setOffshopCardNo((String) rs.get("STOR_CRD_NO"));
		member.setOffshopPointSwitchYn((String) CommonUtil.replaceNull(rs.get("STOR_PINT_SWT_YN"), "N"));
		member.setSiteMemberId(CommonUtil.replaceNull(rs.get("COOPCO_MMB_ID")));

		member.setOffshopMemberNo((String) rs.get("ASCT_MMB_NO"));

		// 아이정보
		List<HashMap<String, Object>> babys = (List<HashMap<String, Object>>) rs.get("MMB_BABY");

		if (babys != null && babys.size() > 0) {

			List<MmsMemberbaby> babyList = new ArrayList<MmsMemberbaby>();
			for (HashMap<String, Object> b : babys) {

				MmsMemberbaby baby = new MmsMemberbaby();
				baby.setBabyNo(new BigDecimal((String) b.get("SEQ_NO")));
				baby.setBabyOrder((String) b.get("BABY_SEQ"));
				baby.setBabyName((String) b.get("BABY_NM"));
				baby.setTwinYn((String) b.get("TWIN_DV_CD"));

				if (rs.get("BTDY") != null && !rs.get("BTDY").toString().equals("null")) {
					baby.setBirthday((String) b.get("BTDY") + "000000");
				}

				if ("1".equals((String) b.get("BTDY_LUCR_SOCR_DV_CD"))) {
					baby.setLunarYn("N");//양력
				} else if ("2".equals((String) b.get("BTDY_LUCR_SOCR_DV_CD"))) {
					baby.setLunarYn("Y");//음력
				}

				if ("1".equals((String) b.get("BABY_GNDR_DV_CD"))) {
					baby.setBabyGenderCd("BABY_GENDER_CD.BOY");
				} else if ("2".equals((String) b.get("BABY_GNDR_DV_CD"))) {
					baby.setBabyGenderCd("BABY_GENDER_CD.GIRL");
				} else if ("3".equals((String) b.get("BABY_GNDR_DV_CD"))) {
					baby.setBabyGenderCd("BABY_GENDER_CD.UNKNOWN");
				}

				//수유형태
				if ("001".equals((String) b.get("FEDG_TYP_CD"))) {
					baby.setFeedTypeCd("FEED_TYPE_CD.MILK");
				} else if ("002".equals((String) b.get("FEDG_TYP_CD"))) {
					baby.setFeedTypeCd("FEED_TYPE_CD.BABYFOOD");
				} else if ("003".equals((String) b.get("FEDG_TYP_CD"))) {
					baby.setFeedTypeCd("FEED_TYPE_CD.MOTHER");
				} else if ("004".equals((String) b.get("FEDG_TYP_CD"))) {
					baby.setFeedTypeCd("FEED_TYPE_CD.MIX");
				} else if ("007".equals((String) b.get("FEDG_TYP_CD"))) {
					baby.setFeedTypeCd("FEED_TYPE_CD.END");
				}

				baby.setUseYn((String) b.get("USE_YN"));

				babyList.add(baby);
			}
			member.setMmsMemberbabys(babyList);

		}

		List<HashMap<String, Object>> socInfos = (List<HashMap<String, Object>>) rs.get("SOC_INFO");

		if (socInfos != null && socInfos.size() > 0) {

			List<MmsMembersns> socInfoList = new ArrayList<MmsMembersns>();

			for (HashMap<String, Object> s : socInfos) {

				MmsMembersns sns = new MmsMembersns();

				sns.setSnsMemberNo(new BigDecimal((String) s.get("SOC_UNFY_MMB_NO")));

				if ("F".equals((String) s.get("SOC_KIND_CD"))) {
					sns.setSnsChannelCd("SNS_CHANNEL_CD.FACEBOOK");
				} else if ("K".equals((String) s.get("SOC_KIND_CD"))) {
					sns.setSnsChannelCd("SNS_CHANNEL_CD.KAKAO");
				} else if ("N".equals((String) s.get("SOC_KIND_CD"))) {
					sns.setSnsChannelCd("SNS_CHANNEL_CD.NAVER");
				}
				sns.setUseYn((String) s.get("USE_YN"));
				socInfoList.add(sns);
			}
			member.setMmsMembersnss(socInfoList);
		}

		return member;
	}


	/**
	 * 
	 * @Method Name : RXCreateUser
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 신규 회원의 공통프로파일을 관계사 사이트로 배포한다. 관계사 사이트는 공통프로파일을 저장한다. 관계사 별로 전달되는 컬럼이 달라진다.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXCreateUser", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXCreateUser(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, Object> rs = new HashMap<String, Object>();

		String memberNo = null;
		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;
		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			BaseMmsMember member = convertJsonToMap(rs);

			memberService.createRxMember(member);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}


		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);

	}

	private HashMap<String, Object> preProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException, JsonParseException, JsonMappingException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");

		String data = request.getParameter("params");
		data = new String(data.getBytes("iso-8859-1"), "utf-8");

		Enumeration<String> headers = request.getHeaderNames();


		logger.info("-----------------------------------------------------------------------------------------------");
		logger.info("## RxRequest [URL]" + request.getRequestURL());
		logger.info("-----------------------------------------------------------------------------------------------");
		while (headers.hasMoreElements()) {
			String headerName = (String) headers.nextElement();
			logger.info("- [" + headerName + "] : " + request.getHeader(headerName));
		}
		logger.info("-----------------------------------------------------------------------------------------------");
		logger.info("## RxRequest [data]: " + data);
		logger.info("-----------------------------------------------------------------------------------------------");

		if (data == null || CommonUtil.isEmpty(data)) {
			throw new ServiceException("mms.imrx." + FAIL_CD_E007);
		}
		
		HashMap<String, Object> rs = new ObjectMapper().readValue(data, HashMap.class);

		return rs;
	}

	private String postProcess(String resultCd, String resultMsg, HashMap<String, Object> rs, ModelMap imSbc) {
//		// Dummy
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("RES_CD", resultCd.replace("mms.imrx.", ""));
		modelMap.addAttribute("OPN_MD", rs.get("OPN_MD"));
		modelMap.addAttribute("SST_CD", rs.get("SST_CD"));// 서비스사이트 코드(제로투세븐)
		modelMap.addAttribute("RES_MSG", resultMsg);
		modelMap.addAttribute("TRS_NO", rs.get("TRS_NO"));

		modelMap.addAttribute("IM_SBC", imSbc);

		logger.info("-----------------------------------------------------------------------------------------------");
		logger.info("## RxResponse : " + JSON.toString(modelMap));
		logger.info("-----------------------------------------------------------------------------------------------");

		return JSON.toString(modelMap);
	}

	private String getMemberNoInReq(HashMap<String, Object> rs) {

		HashMap<String, Object> imSbc = (HashMap<String, Object>) rs.get("IM_SBC");
		String memberNo = imSbc.get("UNFY_MMB_NO").toString();

		return memberNo;

	}
	/**
	 * 
	 * @Method Name : RXISDeleteUserRegister
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 관계사 사이트는 IM서버로부터 받은 통합포탈에서 탈퇴한 통합ID를 탈퇴 처리한다. (통합회원 상태코드:탈퇴)
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXISDeleteUserRegister", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXISDeleteUserRegister(HttpServletRequest request, HttpServletResponse response) {

		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		String memberNo = "";
		HashMap<String, Object> rs = new HashMap<String, Object>();
		try {

			rs = preProcess(request, response);

			memberNo = getMemberNoInReq(rs);

			memberService.deleteRxMember(memberNo);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}

		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);
	}

	/**
	 * 
	 * @Method Name : RXAgreeUser
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : IM서버에서 약관이 동의된 관계사 사이트로 공통프로파일을 배포한다. 관계사 별로 전달되는 컬럼이 달라진다.
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXAgreeUser", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXAgreeUser(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, Object> rs = new HashMap<String, Object>();

		String memberNo = null;
		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			BaseMmsMember member = convertJsonToMap(rs);
			memberService.updateRXAgreeUser(member);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;

		}

		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);

	}

	/**
	 * 
	 * @Method Name : RXISDisagreeAvailableSST
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : IM서버에서 관계사 사이트로 통합 회원 탈퇴 가능 여부 확인 요청한다.
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXISDisagreeAvailableSST", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXISDisagreeAvailableSST(HttpServletRequest request, HttpServletResponse response) {

		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		String memberNo = "";
		HashMap<String, Object> rs = new HashMap<String, Object>();
		Map<String, String> availableMap = new HashMap<String, String>();
		try {

			rs = preProcess(request, response);

			memberNo = getMemberNoInReq(rs);

			availableMap = memberService.getDisagreeAvailable(memberNo);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}

		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);
		imSbc.addAttribute("COOPCO_CD", memberNo);

		if (resultCd.equals(SUCCESS_CD)) {
			imSbc.addAttribute("ISDEL_YN", availableMap.get("available"));
			imSbc.addAttribute("ISDEL_MSG", availableMap.get("msg"));
		}
		return postProcess(resultCd, resultMsg, rs, imSbc);
	}

	/**
	 * 
	 * @Method Name : RXDisagreeUser
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 이용해지 된 관계사 사이트로 해지 요청을 한다. 관계사 사이트는 이용해지 처리한다
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXDisagreeUser", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXDisagreeUser(HttpServletRequest request, HttpServletResponse response) {


		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		String memberNo = "";
		HashMap<String, Object> rs = new HashMap<String, Object>();
		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			memberService.deleteRxMember(memberNo);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}


		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);
	}

	/**
	 * 
	 * @Method Name : RXUpdateUserInfo
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 변경된 공통프로파일을 관계사 사이트로 배포한다. 관계사 사이트는 변경된 공통프로파일을 저장한다. 관계사 별로 전달되는 컬럼이 달라진다
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXUpdateUserInfo", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXUpdateUserInfo(HttpServletRequest request, HttpServletResponse response) {


		HashMap<String, Object> rs = new HashMap<String, Object>();

		String memberNo = null;
		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			BaseMmsMember member = convertJsonToMap(rs);
			memberService.updateRxMember(member);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}

		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);

	}

	/**
	 * 
	 * @Method Name : RXUpdateUserPwd
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 변경된 비밀번호를 관계사 사이트로 배포한다. 관계사 사이트는 변경된 비밀번호를 저장한다.
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXUpdateUserPwd", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXUpdateUserPwd(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, Object> rs = new HashMap<String, Object>();

		String memberNo = null;
		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			BaseMmsMember member = convertJsonToMap(rs);
			memberService.updateRxMember(member);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}

		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);

	}

	/**
	 * 
	 * @Method Name : RXUpdateUserName
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 실명 변경 정보를 관계사 사이트로 배포한다. 관계사 사이트는 실명정보를 저장한다 .
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXUpdateUserName", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXUpdateUserName(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, Object> rs = new HashMap<String, Object>();

		String memberNo = null;
		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			BaseMmsMember member = convertJsonToMap(rs);
			memberService.updateRxMember(member);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}
		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);

	}

	/**
	 * 
	 * @Method Name : RXUpdateUserStatus
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 법정대리인 동의 정보를 관계사 사이트로 배포한다. 관계사 사이트는 해당 정보를 저장한다.
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXUpdateUserStatus", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXUpdateUserStatus(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, Object> rs = new HashMap<String, Object>();

		String memberNo = null;
		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;

		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			BaseMmsMember member = convertJsonToMap(rs);
			memberService.updateRxMember(member);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}
		ModelMap imSbc = new ModelMap();
		imSbc.addAttribute("UNFY_MMB_NO", memberNo);

		return postProcess(resultCd, resultMsg, rs, imSbc);

	}

	/**
	 * 
	 * @Method Name : RXGetUserInfo
	 * @author : intune
	 * @date : 2016. 9. 13.
	 * @description : 관계사 사이트로 공통프로파일을 요청한다. (대사처리용도) 관계사 별로 리턴해주는 컬럼이 달라진다.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/RXGetUserInfo", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	public String RXGetUserInfo(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, Object> rs = new HashMap<String, Object>();

		String memberNo = null;
		String resultMsg = SUCCESS_MSG;
		String resultCd = SUCCESS_CD;
		ModelMap imSbc = new ModelMap();
		try {

			rs = preProcess(request, response);
			memberNo = getMemberNoInReq(rs);

			BaseMmsMember search = new BaseMmsMember();
			search.setMemberNo(new BigDecimal(memberNo));
			BaseMmsMember member = memberService.getMmsMemberForImRx(search);

			imSbc = convertMapToJson(member);

		} catch (ServiceException e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = e.getMessageCd();
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			resultCd = FAIL_CD_E009;
		}

		return postProcess(resultCd, resultMsg, rs, imSbc);

	}

}
