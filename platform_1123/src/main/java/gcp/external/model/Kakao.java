package gcp.external.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class Kakao {
	private String prType;
	private String MID;
	private String merchantTxnNum;
	private String channelType;
	private String GoodsName;
	private String Amt;
	private String ServiceAmt;
	private String SupplyAmt;
	private String GoodsVat;
	private String currency;
	private String returnUrl;
	private String merchantEncKey;
	private String merchantHashKey;
	private String requestDealApproveUrl;
	private String certifiedFlag;
	private String requestorName;
	private String requestorTel;
	private String offerPeriod;
	private String offerPeriodFlag;
	private String possiCard;
	private String fixedInt;
	private String maxInt;
	private String noIntYN;
	private String noIntOpt;
	private String pointUseYn;
	private String blockCard;
	private String blockBin;
	
	private String hash_string;
	private String encryptData;
	private String msgName;
	private String webPath;
	private String EdiDate;
	
	private String resultCode;
	private String resultMsg;
	private String txnId;	
	private String prDt;
	
	private String SPU;
	private String SPU_SIGN_TOKEN;
	private String MPAY_PUB;
	private String NON_REP_TOKEN;
	
	private String requestParam;
	
	private String cancelAmt;	//취소금액
	private String cancelMsg;	//취소사유.
	private String selectKey;	//결제변경인지 결제 추가인지 구분플래그
	private String orderId;	
	private String claimNo;	
	
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
