package gcp.external.model;

import lombok.Data;

@Data
public class RecobellData {
	//상품ID
	private String	productId;
	//ERP상품ID
	private String	erpProductId;
	//브랜드ID
	private String	brandId;
	//정상가
	private String	listPrice;
	//판매가
	private String	salePrice;
	//전시카테고리ID1
	private String displayCategoryId1;
	//전시카테고리ID2
	private String displayCategoryId2;
	//전시카테고리ID3
	private String displayCategoryId3;
}
