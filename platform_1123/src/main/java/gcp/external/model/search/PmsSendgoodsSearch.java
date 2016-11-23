package gcp.external.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class PmsSendgoodsSearch extends BaseSearchCondition {
	//상품ID
	private String	productId;
	//카테고리ID
	private String	categoryId;
	//전송여부
	private String	outSendYn;
	//담당MD ID
	private String	mdId;
	//업체ID
	private String	businessId;
	//브랜드ID
	private String	brandId;
	//제조사
	private String	makerName;

	//판매상태
	private String	saleStateCds;
	//상품유형
	private String	productTypeCds;
	//상품ERP 코드
	private String erpProductIds;

	//날짜검색 종류
	private String	dateType;
	//상품검색 종류
	private String	infoType;
	
	//상품IDs
	private String productIds;
}
