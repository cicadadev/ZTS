package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsReviewrating;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsReviewrating extends BasePmsReviewrating {
	private String ratingName;
}