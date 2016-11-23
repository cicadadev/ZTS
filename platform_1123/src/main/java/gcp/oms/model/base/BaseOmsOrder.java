package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsInquiry;
import gcp.mms.model.MmsDeposit;
import gcp.oms.model.OmsClaim;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrdercoupon;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.pms.model.PmsReview;
import gcp.ccs.model.CcsChannel;
import gcp.mms.model.MmsMember;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsOrder extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String orderTypeCd; //주문유형코드		[not null]
	private String deviceTypeCd; //디바이스유형코드		[not null]
	private String channelId; //채널ID		[null]
	private String siteId; //사이트ID		[null]
	private String siteTypeCd; //사이트유형코드		[not null]
	private String siteOrderId; //외부몰주문ID		[null]
	private String orderPwd; //비회원주문비밀번호		[null]
	private String siteMemId; //외부몰주문자ID		[null]
	private BigDecimal memberNo; //회원번호		[null]
	private String memberId; //회원ID		[null]
	private String memGradeCd; //회원등급코드		[null]
	private String name1; //주문자명1		[null]
	private String name2; //주문자명2		[null]
	private String name3; //주문자명3		[null]
	private String name4; //주문자명4		[null]
	private String countryNo; //국가국번		[null]
	private String phone1; //전화번호1		[null]
	private String phone2; //전화번호2		[null]
	private String phone3; //전화번호3		[null]
	private String email; //이메일		[null]
	private String zipCd; //우편번호		[null]
	private String address1; //주소1		[null]
	private String address2; //주소2		[null]
	private String address3; //주소3		[null]
	private String address4; //주소4		[null]
	private String giftName; //기프티콘대상자명		[null]
	private String giftPhone; //기프티콘대상전화번호		[null]
	private String giftImgTypeCd; //기프티콘이미지유형코드		[null]
	private String giftMsg; //기프티콘메세지		[null]
	private BigDecimal orderAmt; //주문금액||전체주문		[not null]
	private BigDecimal dcAmt; //할인금액||전체주문		[not null]
	private BigDecimal paymentAmt; //결제금액||전체주문, 주문금액-할인금액		[not null]
	private String orderDt; //주문일시		[not null]
	private String cancelDt; //주문취소일시		[null]
	private String orderStateCd; //주문상태코드		[not null]
	private String orderDeliveryStateCd; //배송상태코드		[not null]
	private String okcashbagNo; //OK캐쉬백번호		[null]

	private List<CcsInquiry> ccsInquirys;
	private List<MmsDeposit> mmsDeposits;
	private List<OmsClaim> omsClaims;
	private List<OmsDeliveryaddress> omsDeliveryaddresss;
	private List<OmsOrdercoupon> omsOrdercoupons;
	private List<OmsOrderproduct> omsOrderproducts;
	private List<OmsPayment> omsPayments;
	private List<PmsReview> pmsReviews;
	private CcsChannel ccsChannel;
	private MmsMember mmsMember;

	public String getOrderTypeName(){
			return CodeUtil.getCodeName("ORDER_TYPE_CD", getOrderTypeCd());
	}

	public String getDeviceTypeName(){
			return CodeUtil.getCodeName("DEVICE_TYPE_CD", getDeviceTypeCd());
	}

	public String getSiteTypeName(){
			return CodeUtil.getCodeName("SITE_TYPE_CD", getSiteTypeCd());
	}

	public String getMemGradeName(){
			return CodeUtil.getCodeName("MEM_GRADE_CD", getMemGradeCd());
	}

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}

	public String getGiftImgTypeName(){
			return CodeUtil.getCodeName("GIFT_IMG_TYPE_CD", getGiftImgTypeCd());
	}

	public String getOrderStateName(){
			return CodeUtil.getCodeName("ORDER_STATE_CD", getOrderStateCd());
	}

	public String getOrderDeliveryStateName(){
			return CodeUtil.getCodeName("ORDER_DELIVERY_STATE_CD", getOrderDeliveryStateCd());
	}
}