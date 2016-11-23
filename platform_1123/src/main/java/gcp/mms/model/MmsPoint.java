package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsPoint;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsPoint extends BaseMmsPoint {
}