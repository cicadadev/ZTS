package gcp.ccs.model.search;

import java.util.List;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsNoticeproductSearch extends BaseSearchCondition{
	private String	noticeNo;
	private String  productId;
	List<String> noticeNos;
	
	private String 	insSearchId;
	private String 	insSearchName;
}