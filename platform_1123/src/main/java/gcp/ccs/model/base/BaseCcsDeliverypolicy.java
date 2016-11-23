package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsDeliveryfee;
import gcp.ccs.model.CcsDeliverypolicylang;
import gcp.oms.model.OmsOrderproduct;
import gcp.pms.model.PmsProduct;
import gcp.ccs.model.CcsBusiness;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsDeliverypolicy extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal deliveryPolicyNo; //배송정책번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String businessId; //업체ID		[null]
	private String name; //배송정책명		[not null]
	private String deliveryServiceCd; //택배사코드		[not null]
	private String deliveryFeeTypeCd; //배송비유형코드		[not null]
	private BigDecimal deliveryFee; //배송비		[null]
	private BigDecimal minDeliveryFreeAmt; //배송비무료최소주문금액		[null]
	private String zipCd; //반품배송지우편번호		[null]
	private String address1; //반품배송지주소1		[null]
	private String address2; //반품배송지주소2		[null]
	private String address3; //반품배송지주소3		[null]
	private String address4; //반품배송지주소4		[null]
	private String deliveryInfo; //배송정보안내		[null]

	private List<CcsDeliveryfee> ccsDeliveryfees;
	private List<CcsDeliverypolicylang> ccsDeliverypolicylangs;
	private List<OmsOrderproduct> omsOrderproducts;
	private List<PmsProduct> pmsProducts;
	private CcsBusiness ccsBusiness;

	public String getDeliveryServiceName(){
			return CodeUtil.getCodeName("DELIVERY_SERVICE_CD", getDeliveryServiceCd());
	}

	public String getDeliveryFeeTypeName(){
			return CodeUtil.getCodeName("DELIVERY_FEE_TYPE_CD", getDeliveryFeeTypeCd());
	}

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}
}