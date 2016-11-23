package gcp.sps.model;

import java.util.List;

import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsSaleproduct;
import gcp.sps.model.base.BaseSpsPresentproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsPresentproduct extends BaseSpsPresentproduct {
	private List<SpsPresentproduct>	spsPresentproducts;
	private String					saveType;																//U,I

	private PmsProduct				pmsProduct;
	private PmsSaleproduct			pmsSaleproduct;
	
	private String insName;		// 등록자
	private String updName;		// 수정자

}