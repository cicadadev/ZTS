package gcp.external.model.recobell;

import lombok.Data;

@Data
public class RecobellProduct {
	private String	itemId;
	private String	itemName;
	private String	itemImage;
	private String	originalPrice;
	private String	salePrice;
	private String	category1;
	private String	category2;
	private String	category3;
	private String	regDate;
	private String	updateDate;
	private String	expireDate;
	private String	stock;
	private String	state;
	private String	description;
	private String	extraImage;
	private String	locale;
}
