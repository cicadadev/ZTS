package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsTemplate;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsTemplate extends BaseDmsTemplate {
}