package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsOffshopbrand;
import gcp.ccs.model.CcsOffshopholiday;
import gcp.ccs.model.CcsOffshoplang;
import gcp.ccs.model.CcsUser;
import gcp.dms.model.DmsExhibitoffshop;
import gcp.mms.model.MmsInterestoffshop;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsPickupproduct;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsOffshop extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String offshopId; //매장ID		[primary key, primary key, primary key, not null]
	private String offshopTypeCd; //매장유형코드		[not null]
	private String name; //매장명		[not null]
	private String offshopAffiliation; //매장계열명		[null]
	private String addressInfo; //매장위치안내		[null]
	private String areaDiv1; //매장지역구분1		[null]
	private String areaDiv2; //매장지역구분2		[null]
	private String holidayInfo; //휴일안내		[null]
	private String zipCd; //우편번호		[null]
	private String address1; //주소1		[null]
	private String address2; //주소2		[null]
	private String address3; //주소3		[null]
	private String address4; //주소4		[null]
	private String managerPhone; //운영자전화번호		[null]
	private String offshopPickupYn; //매장픽업가능여부		[not null]
	private BigDecimal latitude; //좌표위도		[null]
	private BigDecimal longitude; //좌표경도		[null]
	private String offshopStateCd; //매장상태코드		[not null]
	private String offshopPhone; //매장전화번호		[null]

	private List<CcsOffshopbrand> ccsOffshopbrands;
	private List<CcsOffshopholiday> ccsOffshopholidays;
	private List<CcsOffshoplang> ccsOffshoplangs;
	private List<CcsUser> ccsUsers;
	private List<DmsExhibitoffshop> dmsExhibitoffshops;
	private List<MmsInterestoffshop> mmsInterestoffshops;
	private List<OmsCart> omsCarts;
	private List<OmsPickupproduct> omsPickupproducts;
	private CcsStore ccsStore;

	public String getOffshopTypeName(){
			return CodeUtil.getCodeName("OFFSHOP_TYPE_CD", getOffshopTypeCd());
	}

	public String getZipName(){
			return CodeUtil.getCodeName("ZIP_CD", getZipCd());
	}

	public String getOffshopStateName(){
			return CodeUtil.getCodeName("OFFSHOP_STATE_CD", getOffshopStateCd());
	}
}