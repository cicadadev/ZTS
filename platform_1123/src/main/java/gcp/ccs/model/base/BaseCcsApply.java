package gcp.ccs.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.ccs.model.CcsApplytarget;
import gcp.ccs.model.CcsExcproduct;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsDiscount;
import gcp.sps.model.SpsPointsave;
import gcp.sps.model.SpsPresent;
import gcp.ccs.model.CcsStore;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseCcsApply extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private BigDecimal applyNo; //적용대상번호		[primary key, primary key, primary key, not null]
	private String targetTypeCd; //적용대상유형코드		[not null]

	private List<CcsApplytarget> ccsApplytargets;
	private List<CcsExcproduct> ccsExcproducts;
	private List<SpsCoupon> spsCoupons;
	private List<SpsDiscount> spsDiscounts;
	private List<SpsPointsave> spsPointsaves;
	private List<SpsPresent> spsPresents;
	private CcsStore ccsStore;

	public String getTargetTypeName(){
			return CodeUtil.getCodeName("TARGET_TYPE_CD", getTargetTypeCd());
	}
}