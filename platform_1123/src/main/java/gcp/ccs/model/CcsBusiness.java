package gcp.ccs.model;

import java.util.List;

import gcp.ccs.model.base.BaseCcsBusiness;
import gcp.pms.model.PmsCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsBusiness extends BaseCcsBusiness {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productNoticeType;
	private String userName;
	private String manageMds;
	
	private String insName;	  	// 등록자
	private String updName;		// 수정자
	
	private List<PmsCategory> pmsCategorys;
}