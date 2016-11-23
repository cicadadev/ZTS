package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsAddress;
import gcp.mms.model.MmsBlacklist;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsChildrencard;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsInterest;
import gcp.mms.model.MmsInterestoffshop;
import gcp.mms.model.MmsLoginhistory;
import gcp.mms.model.MmsMembermenu;
import gcp.mms.model.MmsMemberZtsHistory;
import gcp.mms.model.MmsOffshopcouponissue;
import gcp.mms.model.MmsStyle;
import gcp.mms.model.MmsStylelike;
import gcp.mms.model.MmsWishlist;
import gcp.mms.model.MmsAddress;
import gcp.sps.model.SpsDeal;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsMemberZts extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String memGradeCd; //회원등급코드		[not null]
	private String membershipYn; //멤버십여부		[not null]
	private String membershipRegDt; //멤버십가입일시		[null]
	private String b2eYn; //타사B2E여부		[not null]
	private String b2eRegDt; //B2E가입일시		[null]
	private String regNo; //사업자번호		[null]
	private String childrenYn; //다자녀카드인증여부		[not null]
	private String childrenAccountNo; //다자녀카드번호		[null]
	private String childrenDealId; //다자녀카드딜ID		[null]
	private String childrenRegDt; //다자녀카드가입일시		[null]
	private BigDecimal addressNo; //기본배송지번호		[null]
	private String bankName; //은행명		[null]
	private String bankCd; //은행코드		[null]
	private String accountHolderName; //계좌주명		[null]
	private String accountNo; //계좌번호		[null]
	private String accountAuthDt; //환불계좌인증일시		[null]
	private String jobCd; //직업코드		[null]
	private String marriageYn; //혼인여부		[null]
	private String spouseBirthday; //배우자생일		[null]
	private String weddingDay; //결혼기념일		[null]
	private String babyYnCd; //자녀여부코드		[null]
	private String babyGenderCd; //자녀성별코드		[null]
	private String babyBirthday; //자녀생년월일		[null]
	private String appPushYn; //APP푸쉬동의여부		[not null]
	private String billingKey; //정기결제빌링키		[null]
	private String regularPaymentBusinessCd; //정기결제결제사코드		[null]
	private String regularPaymentBusinessNm; //정기결제결제사명		[null]
	private String deviceTypeCd; //정기결제빌링키디바이스유형코드		[null]
	private String paymentMethodCd; //기본결제수단코드		[null]
	private String paymentBusinessCd; //기본결제사코드		[null]
	private String affiliatecardCd; //제휴카드코드		[null]
	private String affiliatecardNo; //제휴카드인증번호		[null]
	private String affiliatecardAuthDt; //제휴카드인증일시		[null]
	private String affiliatecardExpireDt; //제휴카드만료일시		[null]

	private List<MmsAddress> mmsAddresss;
	private List<MmsBlacklist> mmsBlacklists;
	private List<MmsCarrot> mmsCarrots;
	private List<MmsChildrencard> mmsChildrencards;
	private List<MmsDeposit> mmsDeposits;
	private List<MmsInterest> mmsInterests;
	private List<MmsInterestoffshop> mmsInterestoffshops;
	private List<MmsLoginhistory> mmsLoginhistorys;
	private List<MmsMembermenu> mmsMembermenus;
	private List<MmsMemberZtsHistory> mmsMemberZtsHistorys;
	private List<MmsOffshopcouponissue> mmsOffshopcouponissues;
	private List<MmsStyle> mmsStyles;
	private List<MmsStylelike> mmsStylelikes;
	private List<MmsWishlist> mmsWishlists;
	private MmsAddress mmsAddress;
	private SpsDeal spsDeal;

	public String getMemGradeName(){
			return CodeUtil.getCodeName("MEM_GRADE_CD", getMemGradeCd());
	}

	public String getBankName(){
			return CodeUtil.getCodeName("BANK_CD", getBankCd());
	}

	public String getJobName(){
			return CodeUtil.getCodeName("JOB_CD", getJobCd());
	}

	public String getBabyYnName(){
			return CodeUtil.getCodeName("BABY_YN_CD", getBabyYnCd());
	}

	public String getBabyGenderName(){
			return CodeUtil.getCodeName("BABY_GENDER_CD", getBabyGenderCd());
	}

	public String getRegularPaymentBusinessName(){
			return CodeUtil.getCodeName("REGULAR_PAYMENT_BUSINESS_CD", getRegularPaymentBusinessCd());
	}

	public String getDeviceTypeName(){
			return CodeUtil.getCodeName("DEVICE_TYPE_CD", getDeviceTypeCd());
	}

	public String getPaymentMethodName(){
			return CodeUtil.getCodeName("PAYMENT_METHOD_CD", getPaymentMethodCd());
	}

	public String getPaymentBusinessName(){
			return CodeUtil.getCodeName("PAYMENT_BUSINESS_CD", getPaymentBusinessCd());
	}

	public String getAffiliatecardName(){
			return CodeUtil.getCodeName("AFFILIATECARD_CD", getAffiliatecardCd());
	}
}