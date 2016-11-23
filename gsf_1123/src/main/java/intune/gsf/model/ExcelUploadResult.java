package intune.gsf.model;

import java.util.List;

import lombok.Data;

/**
 * 
 * @Pagckage Name : intune.gsf.model
 * @FileName : ExcelUploadResult.java
 * @author : allen
 * @date : 2016. 5. 31.
 * @description :
 */
@Data
public class ExcelUploadResult {

	private int	successCnt;
	private int	failCnt;
	private int totalCnt;
	private String excelPath;
	
	private List successList;
	
}
