package gcp.pms.model.excel;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import gcp.common.util.ValidationUtil;
import intune.gsf.common.excel.annotation.Excel;
import lombok.Data;

@Data
public class PmsProductBulk {

	/* 1. 상품(PmsProduct) */
	@NotEmpty(groups = { ValidationUtil.Insert.class, ValidationUtil.Update.class })
	@Size(min = 0, max = 20)
	@Pattern(regexp = "[0-9]*")
	@Excel(name = "상품번호", target = "gcp.pms.model.PmsProduct")
	private String productId; // 상점ID [primary key, not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Size(min = 9, max = 10)
	@Excel(name = "상품명", target = "gcp.pms.model.PmsProduct")
	private String name; // 상품명 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "표준카테고리", target = "gcp.pms.model.PmsProduct")
	private String categoryId; // 카테고리ID [not null]

	@Excel(name = "브랜드", target = "gcp.pms.model.PmsProduct")
	private String brandId; // 브랜드ID [null]

	@Excel(name = "업체번호", target = "gcp.pms.model.PmsProduct")
	private String businessId; // 업체ID [null]

	@Excel(name = "업체상품번호", target = "gcp.pms.model.PmsProduct")
	private String businessProductId; // 업체상품ID [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "상품유형", target = "gcp.pms.model.PmsProduct", codegroup = "PRODUCT_TYPE_CD")
	private String productTypeCd; // 상품유형코드 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "월령유형", target = "gcp.pms.model.PmsProduct", codegroup = "AGE_TYPE_CD")
	private String ageTypeCd; // 월령유형코드 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "판매상태", target = "gcp.pms.model.PmsProduct", codegroup = "SALE_STATE_CD")
	private String saleStateCd; // 판매상태코드 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "판매시작일시", target = "gcp.pms.model.PmsProduct", pattern = "yyyy/MM/dd HH:mm:ss")
	private String saleStartDt; // 판매시작일시 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "판매종료일시", target = "gcp.pms.model.PmsProduct", pattern = "yyyy/MM/dd HH:mm:ss")
	private String saleEndDt; // 판매종료일시 [not null]

	@Excel(name = "판매제어번호", target = "gcp.pms.model.PmsProduct")
	@NumberFormat(style = Style.NUMBER)
	private BigDecimal controlNo; // 판매제어번호 [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "전시여부", target = "gcp.pms.model.PmsProduct")
	private String displayYn; // 전시여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "과세구분", target = "gcp.pms.model.PmsProduct", codegroup = "TAX_TYPE_CD")
	private String taxTypeCd; // 과세구분코드 [not null]

	@NotNull(groups = { ValidationUtil.Insert.class })
	@NumberFormat(style = Style.NUMBER)
	@Excel(name = "낱개수량", target = "gcp.pms.model.PmsProduct")
	private BigDecimal unitQty; // 낱개수량 [not null]

	@NotNull(groups = { ValidationUtil.Insert.class })
	@NumberFormat(style = Style.NUMBER)
	@Excel(name = "주문가능최소수량", target = "gcp.pms.model.PmsProduct")
	private BigDecimal minQty; // 주문가능최소수량 [not null]
	// private BigDecimal maxQty; // 주문가능최대수량 [not null]

	@NumberFormat(style = Style.CURRENCY)
	@NotNull(groups = { ValidationUtil.Insert.class })
	@Excel(name = "정상가", target = "gcp.pms.model.PmsProduct")
	private BigDecimal listPrice; // 정상가 [not null]

	@NumberFormat(style = Style.CURRENCY)
	@NotNull(groups = { ValidationUtil.Insert.class })
	@Excel(name = "판매가", target = "gcp.pms.model.PmsProduct")
	private BigDecimal salePrice; // 판매가 [not null]

	@NumberFormat(style = Style.CURRENCY)
	@NotNull(groups = { ValidationUtil.Insert.class })
	@Excel(name = "정기배송가", target = "gcp.pms.model.PmsProduct")
	private BigDecimal regularDeliveryPrice; // 정기배송가 [not null]

	@NumberFormat(style = Style.CURRENCY)
	@NotNull(groups = { ValidationUtil.Insert.class })
	@Excel(name = "공급가", target = "gcp.pms.model.PmsProduct")
	private BigDecimal supplyPrice; // 공급가 [not null]

	@NumberFormat(style = Style.PERCENT)
	@NotNull(groups = { ValidationUtil.Insert.class })
	@Excel(name = "수수료율", target = "gcp.pms.model.PmsProduct")
	private BigDecimal commissionRate; // 수수료율 [not null]

	@NumberFormat(style = Style.PERCENT)
	@NotNull(groups = { ValidationUtil.Insert.class })
	@Excel(name = "포인트적립율", target = "gcp.pms.model.PmsProduct")
	private BigDecimal pointSaveRate; // 포인트적립율 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "옵션여부", target = "gcp.pms.model.PmsProduct")
	private String optionYn; // 옵션여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "텍스트옵션여부", target = "gcp.pms.model.PmsProduct")
	private String textOptionYn; // 텍스트옵션여부 [not null]

	@Excel(name = "텍스트옵션명", target = "gcp.pms.model.PmsProduct")
	private String textOptionName; // 텍스트옵션명 [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "예약판매여부", target = "gcp.pms.model.PmsProduct")
	private String reserveYn; // 예약판매여부 [not null]

	@Excel(name = "예약배송가능일시", target = "gcp.pms.model.PmsProduct")
	private String reserveDeliveryDt; // 예약배송가능일시 [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "매장픽업가능여부", target = "gcp.pms.model.PmsProduct")
	private String offshopPickupYn; // 매장픽업가능여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "정기배송가능여부", target = "gcp.pms.model.PmsProduct")
	private String regularDeliveryYn; // 정기배송가능여부 [not null]

	@Excel(name = "정기배송비무료여부", target = "gcp.pms.model.PmsProduct")
	private String regularDeliveryFeeFreeYn; // 정기배송무료배송여부 [null]

	@Excel(name = "정기배송포인트적립여부", target = "gcp.pms.model.PmsProduct")
	private String regularDeliveryPointSaveYn; // 정기배송포인트적립여부 [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "지정일배송가능여부", target = "gcp.pms.model.PmsProduct")
	private String fixedDeliveryYn; // 지정일배송가능여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "기프티콘가능여부", target = "gcp.pms.model.PmsProduct")
	private String giftYn; // 기프티콘가능여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "선물포장가능여부", target = "gcp.pms.model.PmsProduct")
	private String wrapYn; // 선물포장가능여부 [not null]

	@NotNull(groups = { ValidationUtil.Insert.class })
	@NumberFormat(style = Style.NUMBER)
	@Excel(name = "포장부피", target = "gcp.pms.model.PmsProduct")
	private BigDecimal wrapVolume; // 포장부피 [not null]
//	private String wrapVolume; // 포장부피 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "해외구매대행여부", target = "gcp.pms.model.PmsProduct")
	private String overseasPurchaseYn; // 해외구매대행여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "박스배송여부", target = "gcp.pms.model.PmsProduct")
	private String boxDeliveryYn; // 박스배송여부 [not null]

	@Excel(name = "박스구성단위", target = "gcp.pms.model.PmsProduct")
	private String boxUnitCd; // 박스구성단위 [null]

	@Excel(name = "박스구성수량", target = "gcp.pms.model.PmsProduct")
	@NumberFormat(style = Style.NUMBER)
	private BigDecimal boxUnitQty; // 박스구성수량 [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "재고제어유형코드", target = "gcp.pms.model.PmsProduct", codegroup = "STOCK_CONTROL_TYPE_CD")
	private String stockControlTypeCd; // 재고제어유형코드 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "사용여부", target = "gcp.pms.model.PmsProduct")
	private String useYn; // 사용여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "배송비무료여부", target = "gcp.pms.model.PmsProduct")
	private String deliveryFeeFreeYn; // 배송비무료여부 [not null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "배송정책번호", target = "gcp.pms.model.PmsProduct")
	private String deliveryPolicyNo; // 배송정책번호 [not null]

	@Excel(name = "배송정보안내", target = "gcp.pms.model.PmsProduct")
	private String deliveryInfo; // 배송정보안내 [null]

	@Excel(name = "교환반품환불안내", target = "gcp.pms.model.PmsProduct")
	private String claimInfo; // 교환반품환불안내 [null]

	@Excel(name = "반려사유", target = "gcp.pms.model.PmsProduct")
	private String rejectReason; // 반려사유 [null]

	@Excel(name = "원산지", target = "gcp.pms.model.PmsProduct")
	private String origin; // 원산지 [null]

	@Excel(name = "제조사", target = "gcp.pms.model.PmsProduct")
	private String maker; // 제조사 [null]

	@Excel(name = "키워드", target = "gcp.pms.model.PmsProduct")
	private String keyword; // 키워드||복수 [null]

	@Excel(name = "상품홍보문구", target = "gcp.pms.model.PmsProduct")
	private String adCopy; // 상품홍보문구 [null]

	@Excel(name = "상세설명", target = "gcp.pms.model.PmsProduct")
	private String detail; // 상세설명 [null]

	@NotEmpty(groups = { ValidationUtil.Insert.class })
	@Excel(name = "상품고시유형", target = "gcp.pms.model.PmsProduct", codegroup = "PRODUCT_NOTICE_TYPE_CD")
	private String productNoticeTypeCd; // 상품고시유형코드 [not null]

	@Excel(name = "등록일", target = "gcp.pms.model.PmsProduct")
	protected String insDt;

	@Excel(name = "등록자", target = "gcp.pms.model.PmsProduct")
	protected String insId;

	@Excel(name = "수정일", target = "gcp.pms.model.PmsProduct")
	private String updDt;

	@Excel(name = "수정자", target = "gcp.pms.model.PmsProduct")
	private String updId;

	/* 2. 상품고시(PmsProductnotice) */
	@Excel(name = "상품고시항목1", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail1; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목2", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail2; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목3", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail3; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목4", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail4; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목5", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail5; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목6", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail6; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목7", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail7; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목8", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail8; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목9", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail9; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목10", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail10; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목11", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail11; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목12", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail12; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목13", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail13; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목14", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail14; // 상품고시항목내용 [primary key, not null]
	@Excel(name = "상품고시항목15", target = "gcp.pms.model.PmsProductnotice")
	private String productNoticeFieldDetail15; // 상품고시항목내용 [primary key, not null]

	/* 3. 단품(PmsSaleproduct) */
	@Excel(name = "단품(", target = "gcp.pms.model.PmsSaleproduct")
	private String pmsSaleProductArray; // 단품

	// @Excel(name = "단품명", target = "gcp.pms.model.PmsSaleproduct")
	// private String saleproductNmList; // 업체단품명 [null]
	// @Excel(name = "추가판매가", target = "gcp.pms.model.PmsSaleproduct")
	// private String addSalePriceList; // 추가판매가 [not null]
	// @Excel(name = "단품상태코드", target = "gcp.pms.model.PmsSaleproduct")
	// private String saleproductStateCdList; // 단품상태코드 [not null]
	// // private BigDecimal sortNo; //정렬순서 [null]
	// @Excel(name = "업체단품번호", target = "gcp.pms.model.PmsSaleproduct")
	// private String businessSaleproductIdList; // 업체단품번호

	/* 3.1. 단품옵션(PmsSaleproductoptionvalue) */
	@Excel(name = "단품옵션(", target = "gcp.pms.model.PmsSaleproductoptionvalue")
	private String pmsSaleProductOptionArray; // 단품옵션
	// @Excel(name = "단품옵션명", target =
	// "gcp.pms.model.PmsSaleproductoptionvalue")
	// private String optionNameList; // 옵션명 [primary key, not null]
	// @Excel(name = "단품옵션값", target =
	// "gcp.pms.model.PmsSaleproductoptionvalue")
	// private String optionValueList; // 옵션값 [null]

	/* 4. 상품속성(PmsProductattribute) */
	@Excel(name = "상품속성(", target = "gcp.pms.model.PmsProductattribute")
	private String pmsProductAttributeArray; // 상품속성
	// @Excel(name = "상품속성번호", target = "gcp.pms.model.PmsProductattribute")
	// private String attributeIdList; // 매핑속성ID [null]
	// @Excel(name = "상품속성값", target = "gcp.pms.model.PmsProductattribute")
	// private String attributeValueList; // 매핑속성값 [null]

	/* 5. 전시카테고리(DmsDisplaycategoryproduct) */
	@Excel(name = "전시카테고리(", target = "gcp.dms.model.DmsDisplaycategoryproduct")
	private String dmsDisplayCategoryArray; // 전시카테고리

}
