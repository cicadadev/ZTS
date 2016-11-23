package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsBusinessinquiry;
import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsFaq;
import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsOffshop;
import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.CcsRole;
import gcp.ccs.model.CcsSite;
import gcp.ccs.model.CcsStorelang;
import gcp.ccs.model.CcsStorelanguage;
import gcp.ccs.model.CcsStorepolicy;
import gcp.ccs.model.CcsUser;
import gcp.dms.model.DmsCatalog;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.DmsTemplate;
import gcp.pms.model.PmsAttribute;
import gcp.pms.model.PmsCategory;
import gcp.pms.model.PmsReviewpermit;
import gcp.pms.model.PmsStyleproduct;
import gcp.sps.model.SpsCardpromotion;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsEvent;
import gcp.ccs.model.CcsCountry;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsStore extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String name; //상점명		[not null]
	private String countryCd; //국가코드		[not null]
	private String langCd; //언어코드		[not null]
	private String url; //상점URL		[null]
	private String zipCd; //우편번호		[null]
	private String address1; //주소1		[null]
	private String address2; //주소2		[null]
	private String address3; //주소3		[null]
	private String address4; //주소4		[null]
	private String phone; //전화번호		[null]
	private String managerName; //운영자명		[null]
	private String managerPhone; //운영자전화번호		[null]
	private String managerEmail; //운영자이메일		[null]
	private String storeStateCd; //상점상태코드		[not null]

	private List<CcsApply> ccsApplys;
	private List<CcsBusiness> ccsBusinesss;
	private List<CcsBusinessinquiry> ccsBusinessinquirys;
	private List<CcsChannel> ccsChannels;
	private List<CcsControl> ccsControls;
	private List<CcsFaq> ccsFaqs;
	private List<CcsInquiry> ccsInquirys;
	private List<CcsNotice> ccsNotices;
	private List<CcsOffshop> ccsOffshops;
	private List<CcsPopup> ccsPopups;
	private List<CcsRole> ccsRoles;
	private List<CcsSite> ccsSites;
	private List<CcsStorelang> ccsStorelangs;
	private List<CcsStorelanguage> ccsStorelanguages;
	private List<CcsStorepolicy> ccsStorepolicys;
	private List<CcsUser> ccsUsers;
	private List<DmsCatalog> dmsCatalogs;
	private List<DmsDisplay> dmsDisplays;
	private List<DmsTemplate> dmsTemplates;
	private List<PmsAttribute> pmsAttributes;
	private List<PmsCategory> pmsCategorys;
	private List<PmsReviewpermit> pmsReviewpermits;
	private List<PmsStyleproduct> pmsStyleproducts;
	private List<SpsCardpromotion> spsCardpromotions;
	private List<SpsCoupon> spsCoupons;
	private List<SpsEvent> spsEvents;
	private CcsCountry ccsCountry;

	public String getCountryName(){
			return CodeUtil.getCodeName("COUNTRY_CD", getCountryCd());
	}

	public String getLangName(){
			return CodeUtil.getCodeName("LANG_CD", getLangCd());
	}

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}

	public String getStoreStateName(){
			return CodeUtil.getCodeName("STORE_STATE_CD", getStoreStateCd());
	}
}