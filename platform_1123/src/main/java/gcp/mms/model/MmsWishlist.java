package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsWishlist;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsWishlist extends BaseMmsWishlist {
}