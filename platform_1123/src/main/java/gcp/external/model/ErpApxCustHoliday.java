package gcp.external.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class ErpApxCustHoliday {
	//매장의 키값
	private String	accountNum;
	//수정일시
	private Date	modifiedDateTime;
	//휴일
	private String	holiday;
}
