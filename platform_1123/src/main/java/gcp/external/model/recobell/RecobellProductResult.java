package gcp.external.model.recobell;

import lombok.Data;

@Data
public class RecobellProductResult {

	private String			itemId;
	private String			itemName;
	private String			egoryId;
	private String			rank;
	private String			score;
	private String			isCompensation;

	private RecobellProduct	product;

}
