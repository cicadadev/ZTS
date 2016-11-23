package gcp.external.searchApi.vo;

import java.util.List;

import gcp.ccs.model.CcsCode;
import lombok.Data;

@Data
public class SearchData {
	private String						query;
	private int							totalCount;
	private int							viewCount;
	private String						suggestedQuery;
	private List<SearchCategoryList>	categoryList;
	private List<SearchBrandList>		brandList;
	private List<SearchColorList>		colorList;
	private List<SearchMaterialList>	materialList;
	private List<SearchMaterial1List>	material1List;
	private List<SearchProduct>			productList;
	private List<CcsCode> 				ageCodeList;
	private List<CcsCode>				genderCodeList;
	
	//카테고리 총 카운트
	private int							catgtotalCount;
	//브랜드총카운트
	private int							brdtotalCount;
	private int							colorTotalCount;
	private int							materTotalCount;
}
