package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsOffshop;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsInterestoffshop extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String offshopId; //매장ID		[primary key, primary key, primary key, not null]
	private BigDecimal sortNo; //정렬순서		[null]
	private String topYn; //대표매장여부		[not null]

	private CcsOffshop ccsOffshop;
	private MmsMemberZts mmsMemberZts;
}