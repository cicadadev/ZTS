package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsLoginhistory;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsLoginhistory extends BaseMmsLoginhistory {
}