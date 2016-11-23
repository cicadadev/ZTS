package gcp.pms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsProductBulkUpdate {

//	상품정보 일괄변경: 상품정보 일괄 변경(즉시반영)-> 
//	1. 상품명 
//	2. 상품 강조문구 
//	3. 세일뱃지 노출 여부 
//	4. 성별 
//	5. 선물테마 
//	6. 검색어태그 
//	7. 정기배송여부 / 신청차수 범위 
//	8. 픽업여부 / 픽업상품 할인율
//	9. 선물포장여부 / 부피 
//	10. 기프티콘 여부 
//	11. 해외구매대행 여부 
//	12. 상품상태   @Pattern(regexp = "[0-9 ]")
//	13. 무료배송여부 
//  14. 브랜드코드
	
	/*@Pattern(regexp = ".*([a-zA-Z0-9]{4}$)")*/
	/* 1. 상품(PmsProduct) */
	@NotNull(groups = { ValidationUtil.Update.class })
	@Size(min = 0, max = 20)
	@Excel(name = "상품번호", target = "gcp.pms.model.PmsProduct")
	private String productId; // 상점ID [primary key, not null]

	/* 2. 상품명(PmsProduct) */
	@Size(min = 9, max = 10)
	@Excel(name = "상품명", target = "gcp.pms.model.PmsProduct")
	private String name; // 상품명 [not null]

	/* 3. 상품 강조문구(PmsProduct) */
	@Excel(name = "상품홍보문구", target = "gcp.pms.model.PmsProduct")
	private String adCopy; // 상품홍보문구 [null]
	
	/* 4. 세일뱃지 노출 여부(PmsProduct) */
	@Excel(name = "세일뱃지노출여부", target = "gcp.pms.model.PmsProduct")
	private String dcDisplayYn; // 세일뱃지노출여부 [null]
	
	/* 5. 성별(PmsProduct) */
	@Excel(name = "성별", target = "gcp.pms.model.PmsProduct", codegroup = "GENDER_TYPE_CD")
	private String genderTypeCd; // 성별 [null]
	
	/* 6. 선물테마(PmsProduct) */
	@Excel(name = "선물테마", target = "gcp.pms.model.PmsProduct", codegroup = "THEME_CD")
	private String themeCd; // 선물테마 [null]
	
	/* 7. 검색어태그(PmsProduct) */
	@Excel(name = "검색어태그", target = "gcp.pms.model.PmsProduct")
	private String keyword; // 검색어태그 [null]
	
	/* 7. 정기배송여부(PmsProduct) */
	@Excel(name = "정기배송가능여부", target = "gcp.pms.model.PmsProduct")
	private String regularDeliveryYn; // 정기배송가능여부 [not null]
	
	/* 8. 신청차수최소(PmsProduct) */
	@NumberFormat(style = Style.NUMBER)
	@Excel(name = "신청차수최소", target = "gcp.pms.model.PmsProduct")
	private BigDecimal regularDeliveryMinCnt; // 신청차수최소 [not null]
	
	/* 9. 신청차수최대(PmsProduct) */
	@NumberFormat(style = Style.NUMBER)
	@Excel(name = "신청차수최대", target = "gcp.pms.model.PmsProduct")
	private BigDecimal regularDeliveryMaxCnt; // 신청차수최대 [not null]
	
	/* 10. 매장픽업가능여부(PmsProduct) */
	@Excel(name = "매장픽업가능여부", target = "gcp.pms.model.PmsProduct")
	private String offshopPickupYn; // 매장픽업가능여부 [not null]
	
	/* 11. 매장픽업 할인율(PmsProduct) */
	@NumberFormat(style = Style.NUMBER)
	@Excel(name = "매장픽업할인율", target = "gcp.pms.model.PmsProduct")
	private BigDecimal offshopPickupDcRate; // 매장픽업할인율 [not null]

	/* 12. 선물포장여부(PmsProduct) */
	@Excel(name = "선물포장가능여부", target = "gcp.pms.model.PmsProduct")
	private String wrapYn; // 선물포장가능여부 [not null]

	/* 13. 포장부피(PmsProduct) */
	@NumberFormat(style = Style.NUMBER)
	@Excel(name = "포장부피", target = "gcp.pms.model.PmsProduct")
	private BigDecimal wrapVolume; // 포장부피 [not null]
//	private String wrapVolume; // 포장부피 [not null]
	
	/* 14. 기프티콘 여부 (PmsProduct) */
	@Excel(name = "기프티콘가능여부", target = "gcp.pms.model.PmsProduct")
	private String giftYn; // 기프티콘가능여부 [not null]

	/* 15. 해외구매대행 여부 (PmsProduct) */
	@Excel(name = "해외구매대행여부", target = "gcp.pms.model.PmsProduct")
	private String overseasPurchaseYn; // 해외구매대행 여부 [not null]
	
	/* 16. 상품상태 (PmsProduct) */
	@Excel(name = "상품상태", target = "gcp.pms.model.PmsProduct", codegroup = "SALE_STATE_CD")
	private String saleStateCd; // 상품상태 [not null]
	
	/* 17. 무료배송 (PmsProduct) */
	@Excel(name = "무료배송", target = "gcp.pms.model.PmsProduct")
	private String deliveryFeeFreeYn; // 배송비 무료 여부
	
	/* 18. 브랜드코드 (PmsProduct) */
	@Excel(name = "브랜드코드", target = "gcp.pms.model.PmsProduct")
	private String brandId; // 배송비 무료 여부	
}

