package gcp.dms.model;

import java.util.List;

import gcp.dms.model.base.BaseDmsDisplaycategory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsDisplaycategory extends BaseDmsDisplaycategory {
	
	private String depth;					//카테고리depth
	private String depthFullName;			//카테고리 depth명
	private String depthDisplayCategoryId;	//카테고리 depth 카테고리ID
	private List<DmsTemplate> dmsTemplates;	//템플릿 정보
	private String lastNodeYn;				// 트리의 최하위 노드
	private String insName;					//등록자명
	private String updName;					//수정자명
	private List<DmsDisplayitem> dmsDisplayitems;	//카테고리별 코너아이템목록
	private int pageSize;				//하위카테고리rownum
	
	//검색API property
	private String categoryName;
	private String categoryId;
	private String categoryCount;
	
}