package gcp.oms.model.excel;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class UploadExcelOrder {
	//사이트ID
	private String siteId;
	//사이트명
	private String siteName;
	//제목행
	private BigDecimal titleRow;
	//데이터행
	private BigDecimal dataRow;
	//우편번호
	private String zipCd;
	//주소1
	private String address1;
	//주소2
	private String address2;
	//전화번호1
	private String phone1;
	//전화번호2
	private String phone2;
	//단품ID1
	private String saleproductId1;
	//단품ID2
	private String saleproductId2;
	//단품ID3
	private String saleproductId3;
	//단품ID4
	private String saleproductId4;
	//단품ID5
	private String saleproductId5;
	//주문수량
	private BigDecimal orderQty = BigDecimal.ZERO;
	//배송메모
	private String note;
	//판매가 또는 판매단가
	private BigDecimal salePrice = BigDecimal.ZERO;
	//외부몰주문ID
	private String siteOrderId;
	//수취인명
	private String name;
	//중국주문LP번호
	private String lpNo;
	//통화코드
	private String currencyCd;
	//외화금액
	private BigDecimal currencyPrice = BigDecimal.ZERO;
	//보세여부
	private String localDeliveryYn;

	//상점ID
	private String storeId;
	//로그인ID
	private String loginId;
	//사이트유형코드
	private String siteTypeCd;
}
