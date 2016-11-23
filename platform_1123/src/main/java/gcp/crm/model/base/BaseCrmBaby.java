package gcp.crm.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import java.math.BigDecimal;
import java.sql.Date;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCrmBaby extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String customerno; //고객번호		[primary key, not null]
	private String babyno; //아기번호		[primary key, not null]
	private String babyname; //아기이름		[null]
	private String babysequence; //몇째아이		[null]
	private String twindist; //쌍둥이여부		[null]
	private String solardist; //생일양음구분		[null]
	private String babysex; //아기성별		[null]
	private String childbirthdate; //아기생일		[null]
	private String firstclasscode; //FIRSTCLASSCODE		[null]
	private String secondclasscode; //SECONDCLASSCODE		[null]
	private String thirdclasscode; //THIRDCLASSCODE		[null]
	private String mediahospitalcode; //MEDIAHOSPITALCODE		[null]
	private String collector; //COLLECTOR		[null]
	private String employeecode; //EMPLOYEECODE		[null]
	private String hospitalTc; //HOSPITAL_TC		[null]
	private String bornhospitalcode; //BORNHOSPITALCODE		[null]
	private String hospitalMethod; //HOSPITAL_METHOD		[null]
	private String hospitalComp; //HOSPITAL_COMP		[null]
	private String hospitalpowdercode; //HOSPITALPOWDERCODE		[null]
	private String carecentercode; //CARECENTERCODE		[null]
	private String carecenterMethod; //CARECENTER_METHOD		[null]
	private String carecenterComp; //CARECENTER_COMP		[null]
	private String carecenterpowdercode; //CARECENTERPOWDERCODE		[null]
	private String nowMethod; //NOW_METHOD		[null]
	private String nowComp; //NOW_COMP		[null]
	private String nowfeedpowdercode; //NOWFEEDPOWDERCODE		[null]
	private String nowbabymeal; //NOWBABYMEAL		[null]
	private String specialpowdercode; //SPECIALPOWDERCODE		[null]
	private String babystatusflag; //BABYSTATUSFLAG		[null]
	private String incmanageendflag; //INCMANAGEENDFLAG		[null]
	private String incmanageenddate; //INCMANAGEENDDATE		[null]
	private String incmanageendreason; //INCMANAGEENDREASON		[null]
	private String dmPermissionflag; //DM_PERMISSIONFLAG		[null]
	private String dmStep; //DM_STEP		[null]
	private String useflag; //USEFLAG		[null]
	private BigDecimal uriiCustno; //URII_CUSTNO		[null]
	private BigDecimal uriiBabyno; //URII_BABYNO		[null]
	private String register; //등록자		[null]
	private Date registdate; //등록일		[null]
	private String editor; //수정자		[null]
	private Date editdate; //수정일		[null]
	private String breastend; //BREASTEND		[null]
	private String firstfeed; //FIRSTFEED		[null]
	private String firstmeal; //FIRSTMEAL		[null]
	private String dmreceiptflag; //DMRECEIPTFLAG		[null]
	private String babybirthdate; //BABYBIRTHDATE		[null]
	private String bloodtype; //BLOODTYPE		[null]
	private String usemail; //USEMAIL		[null]
	private String powmilkcomp; //POWMILKCOMP		[null]
	private String powmilkprodname; //분유제품명		[null]
	private String powmilkprodcode; //분유제품코드		[null]
	private String hospitalcode; //HOSPITALCODE		[null]
	private String hospitalname; //HOSPITALNAME		[null]
	private String lactation; //수유형태		[null]
	private String onlineflag; //ONLINEFLAG		[null]
	private String updateChannel; //수정Site		[null]
	private String updateDate; //정보수정일		[null]

}