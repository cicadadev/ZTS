package gcp.external.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class IfLabel {

	private String	oDay;
	private String	ordNo;
	private String	custNm;
	private String	itemcd;
	private String	itemNm;
	private String	ordQty;
	private String	batch;
	private String	batchNo;
	private String	invNo;
	private String	status;
	private String	utime;
	private String	sndSt;
	private String	rowNo;
	private String	maxRow;
	private String seqNo;
	
}
