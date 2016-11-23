package gcp.sa.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.sa.model.base.BaseSaFeedback;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SaFeedback extends BaseSaFeedback {
}