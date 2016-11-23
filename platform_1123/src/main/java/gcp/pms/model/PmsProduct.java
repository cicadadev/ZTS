package gcp.pms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import gcp.ccs.model.CcsApplytarget;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsUser;
import gcp.dms.model.DmsDisplaycategory;
import gcp.dms.model.DmsExhibit;
import gcp.pms.model.base.BasePmsProduct;
import gcp.pms.model.custom.PmsOptimalprice;
import gcp.pms.model.custom.PmsOptionvalue;
import gcp.sps.model.SpsCardpromotion;
import gcp.sps.model.SpsDeal;
import gcp.sps.model.SpsDealproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsProduct extends BasePmsProduct {
	private String					brandName;
	private String					sortNo;
	private String					exhibitId;
	private String					groupNo;
	private int						saleProductTotalStock;
	private String					dmsCategoryName;				// 전시카테고리 path
	private String					productGubun;					// 상품구분
	private String					productCopyReg;					// 복사등록

	private String					saveType;
	private String					imgInsert;

	private String					businessInfo;					//업체정보(ID_name)

	private PmsSaleproduct			pmsSaleproduct;
	private PmsProductimg			pmsProductimg;
	private List<CcsApplytarget>	ccsApplyTarget;
	private BigDecimal				realStockQty;					// 실재고수량
	private BigDecimal             pickupMaxQty;
	private String					insName;						// 등록자
	private String					updName;						// 수정자

	// 승인 이후 상태인지
	private String					approvalYn;

	// 단품 변경 여부
	private String					changeSaleproductYn;

	// 담당 MD
	private CcsUser					ccsUserMd;

	// erp연동 상품 여부(직매입)
	private String					erpProductYn;

	public String					detail1;
	public String					detail2;
	public String					detail3;
	public String					detail4;
	public String					detail5;
	public String					detail6;
	public String					detail7;
	public String					detail8;
	public String					detail9;
	public String					detail10;
	public String					detail11;
	public String					detail12;
	public String					detail13;
	public String					detail14;
	public String					detail15;
	public String					detail16;
	public String					detail17;
	public String					detail18;
	public String					detail19;
	public String					detail20;
	
	private SpsDeal 				spsDeal;
	private CcsNotice 				ccsNotice;
	private List<DmsExhibit>       	exhibits;
	
	private List<PmsOptionvalue> 	optionValues;
	private String 					saleproductId;
	
	private String 					newIconYn;
	private String 					presentYn;
	private String 					couponYn;
	private String 					pointSaveYn;
	private String					freeDeliveryYn;
	private String 					imgUrl;
	private String					imgText;
	
	private PmsOptimalprice 		optimalprice;
	private PmsProductprice			pmsProductprice;
	private SpsCardpromotion 		spsCardpromotion;
	private String mainProductImg;

	private SpsDealproduct spsDealproduct;
	
	private String upperDealGroupNo;
	private String dealGroupNo;
	private String wishlistNo;
	
	private BigDecimal totalPoint;//적립 포인트액
	
	private int deliveryFee; 		// 배송비
	private int minDeliveryFreeAmt; // 배송비무료최소주문금액
	private String displayCategoryIdPath;
	private List<List<DmsDisplaycategory>> cateList= new ArrayList<List<DmsDisplaycategory>>();
	
	//배스트 상품 rank
	private Long bestRank;
}