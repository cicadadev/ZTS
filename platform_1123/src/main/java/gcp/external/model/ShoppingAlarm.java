package gcp.external.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ShoppingAlarm {

	private BigDecimal pushId;		
	private String expireDate;		// PUSH 메세지 유효기간(7일)
	private int appGrpId;         
	private BigDecimal deviceId;   
	private String custId;         // 회원번호
	private String reqUid;         
	private String readYn;         // 회원 읽음 여부
	private String msgUid;         
	private String pushType;       
	private String pushTitle;      
	private String pushMsg;        
	private String pushValue;      
	private String pushKey;        
	private String msgType;        
	private String inappMsg;       
	private String pushImg;        
	private String etc1;            
	private String etc2;            
	private String etc3;            
	private String etc4;            
	private String etc5;            
	private String etc6;            
	private String regDate;        
	private String uptDate;        

	private String isNew;           
}
