package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsCountry;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsCountry extends BaseCcsCountry {
}