package gcp.external.searchApi.vo;

import lombok.Data;

@Data
public class SearchSmallCategoryList {
	private String	smallCategoryName;
	private String	smallCategoryId;
	private int		smallCategoryCount;
}
