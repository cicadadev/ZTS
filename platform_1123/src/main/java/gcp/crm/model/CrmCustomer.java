package gcp.crm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.crm.model.base.BaseCrmCustomer;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CrmCustomer extends BaseCrmCustomer {
}