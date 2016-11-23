package gcp.external.searchApi.vo;

import lombok.Data;

@Data
public class SearchBrandList {
	private String	brandName;
	private String	brandId;
	private int		brandCount;
}
