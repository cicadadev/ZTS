package gcp.pms.model;

import gcp.pms.model.base.BasePmsCategoryrating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsCategoryrating extends BasePmsCategoryrating {
	private String rating; // 별점
}