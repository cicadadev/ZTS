package gcp.crm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.crm.model.base.BaseCrmMember;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CrmMember extends BaseCrmMember {
}