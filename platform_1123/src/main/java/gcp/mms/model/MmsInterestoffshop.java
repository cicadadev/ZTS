package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsInterestoffshop;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsInterestoffshop extends BaseMmsInterestoffshop {
	private String interestYn;
	private String offshopName;
}