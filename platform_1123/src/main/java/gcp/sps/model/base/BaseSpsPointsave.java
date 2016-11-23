package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsOrderproduct;
import gcp.sps.model.SpsPointsavelang;
import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsControl;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsPointsave extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String pointSaveId; //포인트적립ID		[primary key, primary key, primary key, not null]
	private String name; //포인트적립명		[not null]
	private String pointSaveTypeCd; //포인트적립유형코드		[not null]
	private BigDecimal pointValue; //포인트적립적용값		[not null]
	private BigDecimal applyNo; //적용대상번호		[null]
	private BigDecimal controlNo; //적용제어번호		[null]
	private String startDt; //포인트적립시작일자		[not null]
	private String endDt; //포인트적립종료일자		[not null]
	private String pointSaveStateCd; //포인트적립상태코드		[not null]

	private List<OmsOrderproduct> omsOrderproducts;
	private List<SpsPointsavelang> spsPointsavelangs;
	private CcsApply ccsApply;
	private CcsControl ccsControl;

	public String getPointSaveTypeName(){
			return CodeUtil.getCodeName("POINT_SAVE_TYPE_CD", getPointSaveTypeCd());
	}

	public String getPointSaveStateName(){
			return CodeUtil.getCodeName("POINT_SAVE_STATE_CD", getPointSaveStateCd());
	}
}