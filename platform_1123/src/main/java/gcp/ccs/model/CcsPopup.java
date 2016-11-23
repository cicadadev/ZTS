package gcp.ccs.model;

import gcp.ccs.model.base.BaseCcsPopup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsPopup extends BaseCcsPopup {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	String	channelName;
	private String  insName;
	private String  updName;
	
	private String[] ccsPcUrls;
	private String[] ccsMoUrls;	
	
	// FO 
	private String 	url;  // URL
	private String 	cookieKey; // 쿠키값
}