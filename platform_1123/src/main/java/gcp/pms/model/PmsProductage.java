package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.pms.model.base.BasePmsProductage;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsProductage extends BasePmsProductage {
}