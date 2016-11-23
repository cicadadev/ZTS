package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

import gcp.mms.model.base.BaseMmsQuickmenu;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsQuickmenu extends BaseMmsQuickmenu {
	private String memberNo;
	private BigDecimal myMenuSortNo;
}