package gcp.mms.model;

import java.math.BigDecimal;
import java.util.List;

import gcp.common.util.CodeUtil;
import gcp.mms.model.base.BaseMmsStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsStyle extends BaseMmsStyle {
	String memberName;
	String hashTag; 
	String memberId;
	String likeYn;
	String idShowYn;
	String themeCdName;
	BigDecimal styleLikeCnt;
	
	List<MmsStyleproduct> mmsStyleproducts;
	
	public String getBrandName(){
		return CodeUtil.getCodeName("BRAND_CD", getBrandId());
	}
}