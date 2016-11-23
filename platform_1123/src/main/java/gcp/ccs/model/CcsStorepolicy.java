package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsStorepolicy;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsStorepolicy extends BaseCcsStorepolicy {
}