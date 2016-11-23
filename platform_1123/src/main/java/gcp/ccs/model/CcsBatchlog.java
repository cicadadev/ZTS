package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsBatchlog;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsBatchlog extends BaseCcsBatchlog {
}