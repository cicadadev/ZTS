package intune.gsf.model;

import lombok.Data;

/**
 * 
 * @Pagckage Name : intune.gsf.model
 * @FileName : ValidationModel.java
 * @author : eddie
 * @date : 2016. 4. 21.
 * @description :
 */
@Data
public class FieldVaidation {

	private String	fieldCd;
	private Object	values;
	private boolean	isValid;
	private String	message;
	
	
}
