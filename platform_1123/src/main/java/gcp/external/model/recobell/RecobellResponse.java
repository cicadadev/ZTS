package gcp.external.model.recobell;

import java.util.List;

import lombok.Data;
@Data
public class RecobellResponse {
	private String	recType;
	private String	iids;
	private String	cids;
	private String	exiids;
	private String	excids;
	private List<RecobellProduct>		products;
	private List<RecobellProductResult>	results;
}
