package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsSite;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsUploadconf extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String siteId; //사이트ID		[primary key, primary key, primary key, not null]
	private String zipCd; //우편번호		[not null]
	private String address1; //주소1		[not null]
	private String address2; //주소2		[null]
	private String phone1; //전화번호1		[not null]
	private String phone2; //전화번호2		[null]
	private String saleproductId1; //단품ID1		[not null]
	private String saleproductId2; //단품ID2		[null]
	private String saleproductId3; //단품ID3		[null]
	private String saleproductId4; //단품ID4		[null]
	private String saleproductId5; //단품ID5		[null]
	private String orderQty; //주문수량		[not null]
	private String note; //배송메모		[null]
	private String salePrice; //판매가		[null]
	private String siteOrderId; //외부몰주문ID		[not null]
	private String name; //수령인명		[not null]
	private BigDecimal titleRow; //제목행		[not null]
	private String lpNo; //중국주문LP번호		[null]
	private String currencyCd; //통화코드		[null]
	private String currencyPrice; //외화금액		[null]
	private BigDecimal dataRow; //데이터행		[null]
	private String localDelivery; //현지배송여부		[null]

	private CcsSite ccsSite;

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}

	public String getCurrencyName(){
			return CodeUtil.getCodeName("CURRENCY_CD", getCurrencyCd());
	}
}