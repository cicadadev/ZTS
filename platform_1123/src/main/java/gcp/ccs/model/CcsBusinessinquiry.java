package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.ccs.model.base.BaseCcsBusinessinquiry;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsBusinessinquiry extends BaseCcsBusinessinquiry {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String 	mdInfo;			// MD 정보  [이름(ID), 이름(ID)]
	private String 	mdNames;		// MD 이름  [이름1, 이름2]
	private String 	categoryNames;	// 카테고리 명  [카테고리1, 카테고리2]
	private String  address;		// 주소
	
	
	// FO
	
	private String tel1;
	private String tel2;
	private String phone1;
	private String phone2;
	private String email1;
	private String email2;
}