package gcp.oms.model.excel;

import lombok.Data;

@Data
public class UploadExcelInfo {
	//업로드설정 title
	private String confName;
	//엑셀 title
	private String	titleName;
	//엑셀값
	private String	titleValue;
	//엑셀 색인
	private int		index;
}
