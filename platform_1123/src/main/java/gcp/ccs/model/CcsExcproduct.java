package gcp.ccs.model;

import java.util.List;

import gcp.ccs.model.base.BaseCcsExcproduct;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsExcproduct extends BaseCcsExcproduct {

	private List<CcsExcproduct>	ccsExcproducts;
	private String				saveType;										//U,I
	private String				type;

	private PmsProduct			pmsProduct;
	private PmsBrand			pmsBrand;
	
	private String insName;		// 등록자
	private String updName;		// 수정자

}