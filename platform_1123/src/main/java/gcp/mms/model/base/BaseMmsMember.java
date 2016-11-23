package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsInquiry;
import gcp.mms.model.MmsMemberbaby;
import gcp.mms.model.MmsMembersns;
import gcp.mms.model.MmsPoint;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsRegulardelivery;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsReviewpermit;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsEventjoin;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsMember extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private String customerNo; //고객번호		[null]
	private String memberStateCd; //회원상태코드		[not null]
	private String regTypeCd; //가입유형코드		[null]
	private String memberId; //회원ID		[null]
	private String memberName; //고객명		[null]
	private String deviceTypeCd; //가입디바이스유형코드		[null]
	private String regChannelUrl; //회원가입채널URL		[null]
	private String cardIssueNo; //멤버십카드번호		[null]
	private String cardIssueDt; //멤버십카드번호등록일		[null]
	private String regDt; //회원가입일시		[null]
	private String foreignerYn; //외국인여부		[null]
	private String birthday; //생년월일		[null]
	private String lunarYn; //생일음력여부		[null]
	private String genderCd; //성별코드		[null]
	private String discrhash; //중복가입확인정보		[null]
	private String civersion; //CI연계버전		[null]
	private String ciscrhash; //CI연계값		[null]
	private String certDivCd; //실명구분코드		[null]
	private String premiumYn; //프리미엄 회원여부		[null]
	private String premiumRegDt; //프리미엄 회원 가입일		[null]
	private String employeeYn; //임직원여부		[null]
	private String employeeEmail; //임직원이메일		[null]
	private String employeeRegDt; //임직원등록일시		[null]
	private String phone2; //휴대전화번호		[null]
	private String phone1; //유선전화번호		[null]
	private String email; //이메일		[null]
	private String emailYn; //이메일수신동의		[null]
	private String smsYn; //SMS수신동의		[null]
	private String appPushYn; //APP푸쉬동의		[null]
	private String agreeYn; //제3자정보제공동의여부		[not null]
	private String agreeDt; //제3자정보제공동의일시		[null]
	private String agreeWithdrawDt; //제3자정보제공동의철회일시		[null]
	private String siteMemberId; //제휴사회원ID		[null]
	private String offshopId; //가입매장ID		[null]
	private String offshopCardNo; //매장카드번호		[null]
	private String offshopPointSwitchYn; //매장포인트전환여부		[not null]
	private String snsId; //SNS회원ID		[null]
	private String snsChannelCd; //SNS채널코드		[null]
	private String snsMemberYn; //SNS회원여부		[not null]
	private String offshopMemberNo; //준회원번호		[null]

	private List<CcsInquiry> ccsInquirys;
	private List<MmsMemberbaby> mmsMemberbabys;
	private List<MmsMembersns> mmsMembersnss;
	private List<MmsPoint> mmsPoints;
	private List<OmsOrder> omsOrders;
	private List<OmsRegulardelivery> omsRegulardeliverys;
	private List<PmsProductqna> pmsProductqnas;
	private List<PmsReview> pmsReviews;
	private List<PmsReviewpermit> pmsReviewpermits;
	private List<SpsCouponissue> spsCouponissues;
	private List<SpsEventjoin> spsEventjoins;

	public String getMemberStateName(){
			return CodeUtil.getCodeName("MEMBER_STATE_CD", getMemberStateCd());
	}

	public String getRegTypeName(){
			return CodeUtil.getCodeName("REG_TYPE_CD", getRegTypeCd());
	}

	public String getDeviceTypeName(){
			return CodeUtil.getCodeName("DEVICE_TYPE_CD", getDeviceTypeCd());
	}

	public String getGenderName(){
			return CodeUtil.getCodeName("GENDER_CD", getGenderCd());
	}

	public String getCertDivName(){
			return CodeUtil.getCodeName("CERT_DIV_CD", getCertDivCd());
	}

	public String getSnsChannelName(){
			return CodeUtil.getCodeName("SNS_CHANNEL_CD", getSnsChannelCd());
	}
}