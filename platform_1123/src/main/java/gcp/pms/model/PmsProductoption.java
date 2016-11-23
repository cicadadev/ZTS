package gcp.pms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import gcp.pms.model.base.BasePmsProductoption;
import gcp.pms.model.custom.PmsOptionvalue;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsProductoption extends BasePmsProductoption {
	
	private List<PmsOptionvalue> optionValues;
}