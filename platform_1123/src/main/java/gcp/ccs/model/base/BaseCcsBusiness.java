package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsBusinessholiday;
import gcp.ccs.model.CcsBusinesslang;
import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsCommission;
import gcp.ccs.model.CcsDeliverypolicy;
import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsUser;
import gcp.pms.model.PmsEpexcproduct;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.SpsCoupon;
import gcp.ccs.model.CcsStore;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsBusiness extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String businessId; //업체ID		[primary key, primary key, primary key, not null]
	private String name; //업체명		[not null]
	private String erpBusinessId; //ERP업체ID		[null]
	private String repName; //대표자명		[null]
	private String regNo; //사업자번호		[null]
	private String businessCondition; //업태		[null]
	private String businessType; //업종		[null]
	private String businessTaxTypeCd; //업체과세유형코드		[null]
	private String supplyItem; //공급품목		[null]
	private String businessTypeCd; //업체유형코드		[not null]
	private String saleTypeCd; //매입유형코드		[null]
	private String purchaseYn; //위탁매입여부		[not null]
	private String overseasPurchaseYn; //해외구매대행여부		[not null]
	private String note; //비고		[null]
	private String zipCd; //우편번호		[null]
	private String address1; //주소1		[null]
	private String address2; //주소2		[null]
	private String address3; //주소3		[null]
	private String address4; //주소4		[null]
	private String phone; //전화번호		[null]
	private String fax; //팩스번호		[null]
	private String memo; //메모		[null]
	private String reqUserId; //신청계정ID		[null]
	private String reqUserPwd; //신정패스워드		[null]
	private String salesName; //영업담당자명		[null]
	private String salesPhone1; //영업담당자전화번호1		[null]
	private String salesPhone2; //영업담당자전화번호2		[null]
	private String salesEmail; //영업담당자이메일		[null]
	private String managerName; //운영자명		[null]
	private String managerPhone1; //운영자전화번호1		[null]
	private String managerPhone2; //운영자전화번호2		[null]
	private String managerEmail; //운영자이메일		[null]
	private String userId; //담당MDID		[null]
	private String bankName; //은행명		[null]
	private String depositorName; //예금주명		[null]
	private String accountNo; //계좌번호		[null]
	private String contractStartDt; //계약시작일		[null]
	private String contractEndDt; //계약종료일		[null]
	private String businessStateCd; //업체상태코드		[not null]

	private List<CcsBusinessholiday> ccsBusinessholidays;
	private List<CcsBusinesslang> ccsBusinesslangs;
	private List<CcsChannel> ccsChannels;
	private List<CcsCommission> ccsCommissions;
	private List<CcsDeliverypolicy> ccsDeliverypolicys;
	private List<CcsInquiry> ccsInquirys;
	private List<CcsUser> ccsUsers;
	private List<PmsEpexcproduct> pmsEpexcproducts;
	private List<PmsProduct> pmsProducts;
	private List<SpsCoupon> spsCoupons;
	private CcsStore ccsStore;

	public String getBusinessTaxTypeName(){
			return CodeUtil.getCodeName("BUSINESS_TAX_TYPE_CD", getBusinessTaxTypeCd());
	}

	public String getBusinessTypeName(){
			return CodeUtil.getCodeName("BUSINESS_TYPE_CD", getBusinessTypeCd());
	}

	public String getSaleTypeName(){
			return CodeUtil.getCodeName("SALE_TYPE_CD", getSaleTypeCd());
	}

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}

	public String getBusinessStateName(){
			return CodeUtil.getCodeName("BUSINESS_STATE_CD", getBusinessStateCd());
	}
}