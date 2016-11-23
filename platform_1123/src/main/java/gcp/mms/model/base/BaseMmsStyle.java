package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsStylelike;
import gcp.mms.model.MmsStyleproduct;
import gcp.mms.model.MmsMemberZts;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsStyle extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal styleNo; //스타일번호		[primary key, primary key, primary key, not null]
	private BigDecimal memberNo; //회원번호		[not null]
	private String styleInfo; //스타일저작정보		[null]
	private String styleImg; //스타일이미지		[null]
	private String title; //제목		[null]
	private String detail; //설명		[null]
	private BigDecimal likeCnt; //좋아요수		[null]
	private String themeCd; //스타일테마코드		[null]
	private String genderTypeCd; //성별코드		[null]
	private String brandId; //브랜드ID		[null]
	private String styleStateCd; //스타일상태코드||FO		[null]
	private String displayYn; //전시여부||BO		[not null]

	private List<MmsStylelike> mmsStylelikes;
	private List<MmsStyleproduct> mmsStyleproducts;
	private MmsMemberZts mmsMemberZts;

	public String getThemeName(){
			return CodeUtil.getCodeName("THEME_CD", getThemeCd());
	}

	public String getGenderTypeName(){
			return CodeUtil.getCodeName("GENDER_TYPE_CD", getGenderTypeCd());
	}

	public String getStyleStateName(){
			return CodeUtil.getCodeName("STYLE_STATE_CD", getStyleStateCd());
	}
}