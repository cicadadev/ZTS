package gcp.external.model;

import java.util.List;

import gcp.pms.model.PmsProductimg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class EpNaver {
	//상품자료
	private String productId;	//상품ID
	private String productName;		//상품명
	private int salePrice;		//판매가
	private int listPrice;		//정상가
	private String categoryName1;	//전시카테고리대분류
	private String categoryName2;	//전시카테고리중분류
	private String categoryName3;	//전시카테고리소분류
	private String overseasPurchaseYn;	//해외구매대행여부
	private String brandName;	//브랜드명
	private String maker;	//제조사
	private String origin;	//원산지
	private int deliveryFee;	//배송비
	private String gender;	//성별
	private String dataType;	//데이터종류(I:신규,U:업데이트,D:품절)
	private List<PmsProductimg> imageList;		//이미지

	//주문자료
	private int	sumOrderQty;	//주문수량합계
	private long	sumSalePrice;	//판매가합계
}
