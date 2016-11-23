package intune.gsf.model;

import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
@Data
public class Messagelang {
	private String msgCd; 
	private String langCd;
	private String msg;

	private Message message;
}