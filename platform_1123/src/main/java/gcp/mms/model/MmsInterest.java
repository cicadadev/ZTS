package gcp.mms.model;

import gcp.mms.model.base.BaseMmsInterest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsInterest extends BaseMmsInterest {
	private String brandName;
	private String displayCategoryName;

}