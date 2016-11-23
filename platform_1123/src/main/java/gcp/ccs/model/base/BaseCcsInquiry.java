package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsInquirytransfer;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsStore;
import gcp.mms.model.MmsMember;
import gcp.oms.model.OmsOrder;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsInquiry extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal inquiryNo; //문의번호		[primary key, primary key, primary key, not null]
	private String inquiryChannelCd; //문의채널코드		[not null]
	private String inquiryTypeCd; //문의유형코드		[not null]
	private String orderId; //주문ID		[null]
	private String productId; //주문상품ID		[null]
	private String saleproductId; //주문단품ID		[null]
	private String inquiryStateCd; //문의상태코드		[not null]
	private String img; //이미지		[null]
	private String title; //제목		[not null]
	private String detail; //내용		[not null]
	private String emailYn; //이메일수신여부		[not null]
	private String email; //이메일		[null]
	private String smsYn; //SMS수신여부		[not null]
	private String phone; //전화번호		[null]
	private BigDecimal memberNo; //회원번호		[null]
	private String customerName; //고객명		[null]
	private String businessId; //담당업체ID		[null]
	private String confirmDt; //확인일시		[null]
	private String confirmId; //확인자		[null]
	private String answer; //답변		[null]
	private String answerDt; //답변일시		[null]
	private String answerId; //답변자		[null]
	private String brandId; //브랜드ID		[null]

	private List<CcsInquirytransfer> ccsInquirytransfers;
	private CcsBusiness ccsBusiness;
	private CcsStore ccsStore;
	private MmsMember mmsMember;
	private OmsOrder omsOrder;
	private PmsBrand pmsBrand;
	private PmsProduct pmsProduct;
	private PmsSaleproduct pmsSaleproduct;

	public String getInquiryChannelName(){
			return CodeUtil.getCodeName("INQUIRY_CHANNEL_CD", getInquiryChannelCd());
	}

	public String getInquiryTypeName(){
			return CodeUtil.getCodeName("INQUIRY_TYPE_CD", getInquiryTypeCd());
	}

	public String getInquiryStateName(){
			return CodeUtil.getCodeName("INQUIRY_STATE_CD", getInquiryStateCd());
	}
}