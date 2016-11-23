package gcp.external.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class ErpApxSellingBrand {
	//매장의 키값
	private String	accountNum;
	//브랜드
	private String	brand;
}
