package gcp.external.searchApi.vo;

import lombok.Data;

@Data
public class SearchCategoryList {
	private String	categoryName;
	private String	categoryId;
	private int		categoryCount;

}
