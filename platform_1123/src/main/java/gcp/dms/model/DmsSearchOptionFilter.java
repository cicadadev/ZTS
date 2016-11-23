package gcp.dms.model;

import java.util.List;

import gcp.ccs.model.CcsCode;
import lombok.Data;

@Data
public class DmsSearchOptionFilter {
	
	private List<CcsCode> 	ageCodeList;
	private List<CcsCode>	genderCodeList;
}
