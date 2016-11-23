package gcp.sps.model;

import java.math.BigDecimal;

import gcp.sps.model.base.BaseSpsPointsave;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsPointsave extends BaseSpsPointsave {
	private String controlType;	//제한 유형
	
	private String insName;		// 등록자
	private String updName;		// 수정자
	
	private BigDecimal basicPoint;//기본적립포인트
	private BigDecimal addPoint;//추가적립포인트(개당)
	private BigDecimal totalPoint;//총적립포인트(개당)

}