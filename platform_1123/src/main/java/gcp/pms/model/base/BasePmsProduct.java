package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsExcproduct;
import gcp.ccs.model.CcsInquiry;
import gcp.dms.model.DmsCatalogproduct;
import gcp.dms.model.DmsDisplaycategoryproduct;
import gcp.dms.model.DmsExhibitmainproduct;
import gcp.dms.model.DmsExhibitproduct;
import gcp.mms.model.MmsStyleproduct;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsOrderproduct;
import gcp.pms.model.PmsBarcode;
import gcp.pms.model.PmsEpexcproduct;
import gcp.pms.model.PmsOptionproduct;
import gcp.pms.model.PmsPricereserve;
import gcp.pms.model.PmsProductage;
import gcp.pms.model.PmsProductattribute;
import gcp.pms.model.PmsProductimg;
import gcp.pms.model.PmsProductlang;
import gcp.pms.model.PmsProductnotice;
import gcp.pms.model.PmsProductoption;
import gcp.pms.model.PmsProductprice;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsProductreserve;
import gcp.pms.model.PmsProductsummary;
import gcp.pms.model.PmsRecommendproduct;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsReviewpermit;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsSetproduct;
import gcp.pms.model.PmsStyleproduct;
import gcp.sps.model.SpsDealproduct;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsPresentproduct;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsDeliverypolicy;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsCategory;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProduct extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private String categoryId; //표준카테고리ID		[null]
	private String brandId; //브랜드ID		[null]
	private String name; //상품명		[not null]
	private String keyword; //키워드||복수		[null]
	private String icon; //상품아이콘||복수		[null]
	private String adCopy; //상품홍보문구		[null]
	private String detail; //상세설명		[null]
	private String sellingPoint; //셀링포인트		[null]
	private String mdNotice; //MD공지		[null]
	private String attribute1; //추가속성1		[null]
	private String attribute2; //추가속성2		[null]
	private String attribute3; //추가속성3		[null]
	private String productTypeCd; //상품유형코드		[not null]
	private String businessId; //공급업체ID		[null]
	private String erpProductId; //ERP상품ID		[null]
	private String exportErpProductId; //수출용ERP상품ID		[null]
	private String businessProductId; //업체상품ID		[null]
	private String genderTypeCd; //성별유형코드		[null]
	private String themeCd; //테마코드		[null]
	private String productNoticeTypeCd; //상품고시유형코드		[null]
	private String offshopImg; //매장컷이미지||상품ID_OFFSHOP		[null]
	private String saleStateCd; //판매상태코드		[not null]
	private String rejectReason; //반려사유		[null]
	private String origin; //원산지||국가		[null]
	private String maker; //제조사		[null]
	private BigDecimal controlNo; //판매제어번호		[null]
	private String displayYn; //전시여부		[not null]
	private BigDecimal unitQty; //낱개수량		[not null]
	private BigDecimal minQty; //주문가능최소수량		[not null]
	private BigDecimal personQty; //1인구매제한수량		[null]
	private String taxTypeCd; //과세구분코드		[not null]
	private BigDecimal listPrice; //정상가		[not null]
	private BigDecimal salePrice; //판매가		[not null]
	private BigDecimal regularDeliveryPrice; //정기배송가		[not null]
	private BigDecimal supplyPrice; //공급가		[not null]
	private BigDecimal commissionRate; //수수료율		[not null]
	private BigDecimal pointSaveRate; //포인트적립율		[not null]
	private String deliveryFeeFreeYn; //배송비무료여부		[not null]
	private String priceApplyDt; //가격적용일		[null]
	private BigDecimal deliveryPolicyNo; //배송정책번호		[not null]
	private String deliveryInfo; //배송정보안내		[null]
	private String claimInfo; //교환반품환불안내		[null]
	private String saleStartDt; //판매시작일시		[null]
	private String saleEndDt; //판매종료일시		[null]
	private String group1; //그룹코드1		[null]
	private String group2; //그룹코드2		[null]
	private String group3; //그룹코드3		[null]
	private String searchExcYn; //검색제외여부		[not null]
	private String optionYn; //옵션여부		[not null]
	private String textOptionYn; //텍스트옵션여부		[not null]
	private String textOptionName; //텍스트옵션명		[null]
	private String reserveYn; //예약판매여부		[not null]
	private String reserveDeliveryDt; //예약배송가능일시		[null]
	private String offshopPickupYn; //매장픽업가능여부		[not null]
	private BigDecimal offshopPickupDcRate; //매장픽업할인율		[not null]
	private String regularDeliveryYn; //정기배송가능여부		[not null]
	private String regularDeliveryFeeFreeYn; //정기배송무료배송여부		[null]
	private String regularDeliveryPointSaveYn; //정기배송포인트적립여부		[null]
	private BigDecimal regularDeliveryMinCnt; //정기배송최소횟수		[null]
	private BigDecimal regularDeliveryMaxCnt; //정기배송최대횟수		[null]
	private BigDecimal regularDeliveryMaxQty; //정기배송최대수량		[null]
	private String fixedDeliveryYn; //지정일배송가능여부		[not null]
	private String giftYn; //기프티콘가능여부		[not null]
	private String wrapYn; //선물포장가능여부		[not null]
	private BigDecimal wrapVolume; //포장부피		[not null]
	private String overseasPurchaseYn; //해외구매대행여부		[not null]
	private String boxDeliveryYn; //박스배송여부		[not null]
	private String boxUnitCd; //박스구성단위코드		[null]
	private BigDecimal boxUnitQty; //박스구성수량		[null]
	private String stockControlTypeCd; //재고제어유형코드		[null]
	private String noticeConfirmYn; //품목정보확인여부		[null]
	private String noticeConfirmDt; //품목정보확인일시		[null]
	private String noticeConfirmId; //품목정보확인자		[null]
	private String outSendYn; //사방넷전송여부		[not null]
	private String outSendDt; //사방넷전송일시		[null]
	private String useYn; //사용여부		[not null]
	private String dcDisplayYn; //세일아이콘표시여부		[not null]

	private List<CcsExcproduct> ccsExcproducts;
	private List<CcsInquiry> ccsInquirys;
	private List<DmsCatalogproduct> dmsCatalogproducts;
	private List<DmsDisplaycategoryproduct> dmsDisplaycategoryproducts;
	private List<DmsExhibitmainproduct> dmsExhibitmainproducts;
	private List<DmsExhibitproduct> dmsExhibitproducts;
	private List<MmsStyleproduct> mmsStyleproducts;
	private List<OmsCart> omsCarts;
	private List<OmsOrderproduct> omsOrderproducts;
	private List<PmsBarcode> pmsBarcodes;
	private List<PmsEpexcproduct> pmsEpexcproducts;
	private List<PmsOptionproduct> pmsOptionproducts;
	private List<PmsPricereserve> pmsPricereserves;
	private List<PmsProductage> pmsProductages;
	private List<PmsProductattribute> pmsProductattributes;
	private List<PmsProductimg> pmsProductimgs;
	private List<PmsProductlang> pmsProductlangs;
	private List<PmsProductnotice> pmsProductnotices;
	private List<PmsProductoption> pmsProductoptions;
	private List<PmsProductprice> pmsProductprices;
	private List<PmsProductqna> pmsProductqnas;
	private List<PmsProductreserve> pmsProductreserves;
	private List<PmsProductsummary> pmsProductsummarys;
	private List<PmsRecommendproduct> pmsRecommendproducts;
	private List<PmsReview> pmsReviews;
	private List<PmsReviewpermit> pmsReviewpermits;
	private List<PmsSaleproduct> pmsSaleproducts;
	private List<PmsSetproduct> pmsSetproducts;
	private List<PmsStyleproduct> pmsStyleproducts;
	private List<SpsDealproduct> spsDealproducts;
	private List<SpsEvent> spsEvents;
	private List<SpsPresentproduct> spsPresentproducts;
	private CcsBusiness ccsBusiness;
	private CcsControl ccsControl;
	private CcsDeliverypolicy ccsDeliverypolicy;
	private PmsBrand pmsBrand;
	private PmsCategory pmsCategory;

	public String getProductTypeName(){
			return CodeUtil.getCodeName("PRODUCT_TYPE_CD", getProductTypeCd());
	}

	public String getGenderTypeName(){
			return CodeUtil.getCodeName("GENDER_TYPE_CD", getGenderTypeCd());
	}

	public String getThemeName(){
			return CodeUtil.getCodeName("THEME_CD", getThemeCd());
	}

	public String getProductNoticeTypeName(){
			return CodeUtil.getCodeName("PRODUCT_NOTICE_TYPE_CD", getProductNoticeTypeCd());
	}

	public String getSaleStateName(){
			return CodeUtil.getCodeName("SALE_STATE_CD", getSaleStateCd());
	}

	public String getTaxTypeName(){
			return CodeUtil.getCodeName("TAX_TYPE_CD", getTaxTypeCd());
	}

	public String getBoxUnitName(){
			return CodeUtil.getCodeName("BOX_UNIT_CD", getBoxUnitCd());
	}

	public String getStockControlTypeName(){
			return CodeUtil.getCodeName("STOCK_CONTROL_TYPE_CD", getStockControlTypeCd());
	}
}