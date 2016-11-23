package gcp.sa.model.base;

import java.math.BigDecimal;

import gcp.sa.model.SaFeedback;
import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSaFeedbackImg extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal fbNo; //피드백번호		[primary key, primary key, primary key, not null]
	private BigDecimal imgNo; //이미지번호		[primary key, primary key, primary key, not null]
	private String imgNm; //이미지명		[null]
	private String imgPath; //이미지경로		[null]
	private BigDecimal fileSize; //파일사이즈		[null]

	private SaFeedback saFeedback;
}