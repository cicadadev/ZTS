package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsAttributevalue;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsAttributevalue extends BasePmsAttributevalue {
}