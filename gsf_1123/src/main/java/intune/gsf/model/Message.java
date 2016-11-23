package intune.gsf.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString(callSuper=true)
@Data
public class Message{
	
	private String msgCd;
	private String msgTypeCd;
	private String msg;

	private List<Messagelang> messagelangs;
	
}