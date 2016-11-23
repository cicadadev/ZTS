package gcp.external.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class TmsQueue {
	
	private BigDecimal 	seq;		// 자동생성 [not null] 
	private String		msgCode;	// 메시지 코드(템플릿 코드) [not null]
	private String		toId;		// 대상 ID			  [not null]
	private String		toName;		// 대상 이름 (받는 사람)
	private String		toEmail;	// 대상 이메일 (받는 이메일)
	private String		toPhone;	// 대상 휴대전화번호 (SMS)
	private String		toToken;	// 대상 Push
	private String		fromName;	// 보내는 사람 이름
	private String		fromEmail;	// 보내는 사람 이메일주소
	private String		fromPhone;	// 보내는 사람 전화번호
	private String		securePwd;	// 보안메일 비밀번호
	
	private String		targetFlag;	// 타겟 여부 플래그	[not null][default : N]
	private String		targetDate;	// 타겟 일자
	private String		regDate;	// 등록 일자
	private String		udtDate;	// 수정 일자
	private String		subject;	// 제목
	private String		mapContent;	// 메시지 내용 
	private String		mapContent2;	// 메시지 내용2 
	
	// 매핑 1 ~ 20
	private String		map1;
	private String		map2;
	private String		map3;
	private String		map4;
	private String		map5;
	private String		map6;
	private String		map7;
	private String		map8;
	private String		map9;
	private String		map10;
	private String		map11;
	private String		map12;
	private String		map13;
	private String		map14;
	private String		map15;
	private String		map16;
	private String		map17;
	private String		map18;
	private String		map19;
	private String		map20;

}
