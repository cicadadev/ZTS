package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsExchange;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsExchange extends BaseCcsExchange {
}