package gcp.external.searchApi.vo;

import gcp.common.util.FoSessionUtil;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import lombok.Data;

@Data
public class SearchProduct {
	private String	storeId;
	private String	productId;
	private String	brandId;
	private String	brandName;
	private String	productName;
	private String	keyword;
	private String	adCopy;
	private String	businessId;
	private String	ageTypeCd;
	private String	date;
	private String	genderTypeCd;
	private String	displayCategoryId;
	private String	categoryName;
	private String	imgUrl;
	//private String	reviewCount;
	private String	offshopPickupYn;
	private String	regularDeliveryYn;
	private String	newIconYn;
	private String	color;
	private String	material;
	private String	material_1;
	
	private String	salePrice; //판매가 
	private String	presentYn; //사은품
	//private String	pointSaveYn; //포인트여부
	//private String	deliveryFreeYn; //배송비무료여부
	private String	couponYn; //쿠폰여부
	
	private String	oriSalePrice;

	private String	pointYn;
	private String	deliveryFeeFreeYn;
	
	private String	prestigeSalePrice;
	private String	prestigePointYn;
	private String	prestigeDeliveryFeeFreeYn;
	private String	prestigeCouponYn;
	
	private String	vipSalePrice;
	private String	vipPointYn;
	private String	vipDeliveryFeeFreeYn;
	private String	vipCouponYn;
	
	private String	goldSalePrice;
	private String	goldPointYn;
	private String	goldDeliveryFeeFreeYn;
	private String	goldCouponYn;
	
	private String	silverSalePrice;
	private String	silverPointYn;
	private String	silverDeliveryFeeFreeYn;
	private String	silverCouponYn;
	
	private String	familySalePrice;
	private String	familyPointYn;
	private String	familyDeliveryFeeFreeYn;
	private String	familyCouponYn;
	
	private String	welcomeSalePrice;
	private String	welcomePointYn;
	private String	welcomeDeliveryFeeFreeYn;
	private String	welcomeCouponYn;
	
	private String	searchExcYn;
	private String	saleStartDt;
	private String	saleEndDt;
	private String	displayYn;
	private String	saleStateCd;
	private String	orderQty;
	private String	rating;
	private String	modifiedDate;
	private String	regularDeliveryPrice;
	private String dealListPrice;
	
	public String getSalePrice(){
		String grade = FoSessionUtil.getMemberGradeForPrice();
		if(CommonUtil.isEmpty(grade)){
			return this.salePrice;	
		}else{
			if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_VIP, grade)){
				setSalePrice(vipSalePrice);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_GOLD, grade)){
				setSalePrice(goldSalePrice);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_SILVER, grade)){
				setSalePrice(silverSalePrice);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_FAMILY, grade)){
				setSalePrice(familySalePrice);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_WELCOME, grade)){
				setSalePrice(welcomeSalePrice);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_PRESTAGE,grade)){
				setSalePrice(prestigeSalePrice);
			}
		}
		return salePrice;
	}
	
	public String getOriSalePrice() {

		String grade = FoSessionUtil.getMemberGradeForPrice();

		double salePrice = Double.parseDouble(this.salePrice);

		if (CommonUtil.isEmpty(grade)) {
			return this.oriSalePrice;
			
		} else {
			if (CommonUtil.equals(BaseConstants.MEMBER_GRADE_VIP, grade)) {

				double price = Double.parseDouble(this.vipSalePrice);

				if (salePrice >= price) {
					oriSalePrice = getDealListPrice();
				}

			} else if (CommonUtil.equals(BaseConstants.MEMBER_GRADE_GOLD, grade)) {
				double price = Double.parseDouble(this.goldSalePrice);
				if (salePrice >= price) {
					oriSalePrice = getDealListPrice();
				}
			} else if (CommonUtil.equals(BaseConstants.MEMBER_GRADE_SILVER, grade)) {
				double price = Double.parseDouble(this.silverSalePrice);

				if (salePrice >= price) {
					oriSalePrice = getDealListPrice();
				}
			} else if (CommonUtil.equals(BaseConstants.MEMBER_GRADE_FAMILY, grade)) {
				double price = Double.parseDouble(this.familySalePrice);

				if (salePrice >= price) {
					oriSalePrice = getDealListPrice();
				}
			} else if (CommonUtil.equals(BaseConstants.MEMBER_GRADE_WELCOME, grade)) {
				double price = Double.parseDouble(this.welcomeSalePrice);

				if (salePrice >= price) {
					oriSalePrice = getDealListPrice();
				}
			} else if (CommonUtil.equals(BaseConstants.MEMBER_GRADE_PRESTAGE, grade)) {
				double price = Double.parseDouble(this.prestigeSalePrice);

				if (salePrice >= price) {
					oriSalePrice = getDealListPrice();
				}
			}
		}

		return oriSalePrice;
	}
	
	public String getPointYn(){
		
		String grade = FoSessionUtil.getMemberGradeForPrice();
		if(CommonUtil.isEmpty(grade)){
			return this.pointYn;	
		}else{
			if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_VIP, grade)){
				setPointYn(vipPointYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_GOLD, grade)){
				setPointYn(goldPointYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_SILVER, grade)){
				setPointYn(silverPointYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_FAMILY, grade)){
				setPointYn(familyPointYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_WELCOME, grade)){
				setPointYn(welcomePointYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_PRESTAGE,grade)){
				setPointYn(prestigePointYn);
			}
		}
		return pointYn;
	}
	
	public String getDeliveryFeeFreeYn(){
		
		String grade = FoSessionUtil.getMemberGradeForPrice();
		if(CommonUtil.isEmpty(grade)){
			return this.deliveryFeeFreeYn;	
		}else{
			if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_VIP, grade)){
				setDeliveryFeeFreeYn(vipDeliveryFeeFreeYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_GOLD, grade)){
				setDeliveryFeeFreeYn(goldDeliveryFeeFreeYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_SILVER, grade)){
				setDeliveryFeeFreeYn(silverDeliveryFeeFreeYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_FAMILY, grade)){
				setDeliveryFeeFreeYn(familyDeliveryFeeFreeYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_WELCOME, grade)){
				setDeliveryFeeFreeYn(welcomeDeliveryFeeFreeYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_PRESTAGE,grade)){
				setDeliveryFeeFreeYn(prestigeDeliveryFeeFreeYn);
			}
		}
		return deliveryFeeFreeYn;
	}
	
	
	public String getCouponYn(){
		String grade = FoSessionUtil.getMemberGradeForPrice();
		if(CommonUtil.isEmpty(grade)){
			return this.couponYn;	
		}else{
			if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_VIP, grade)){
				setCouponYn(vipCouponYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_GOLD, grade)){
				setCouponYn(goldCouponYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_SILVER, grade)){
				setCouponYn(silverCouponYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_FAMILY, grade)){
				setCouponYn(familyCouponYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_WELCOME, grade)){
				setCouponYn(welcomeCouponYn);
			}else if(CommonUtil.equals(BaseConstants.MEMBER_GRADE_PRESTAGE,grade)){
				setCouponYn(prestigeCouponYn);
			}
		}
		return couponYn;
	}
	
	
	
}
