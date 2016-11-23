package gcp.sps.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.sps.model.base.BaseSpsDiscount;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsDiscount extends BaseSpsDiscount {
}