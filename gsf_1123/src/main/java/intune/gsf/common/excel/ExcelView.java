package intune.gsf.common.excel;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class ExcelView extends AbstractXlsView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub


		List<String[]> dataList = (List<String[]>) model.get("dataList");
		String[] header = (String[]) model.get("header");
		String[] dataType = (String[]) model.get("dataType");
		String excelName = (String) model.get("fileName");
		//create a wordsheet

//		String[] header = dataList.get(0);

		int PER_SHEET = 60000;

		int sheetCnt = 0;

		if (dataList == null || dataList.size() == 0) {
			List<String[]> sheetData = new ArrayList<String[]>();
			sheetCnt++;
			if(dataType != null){
				sheetData.add(dataType);
			}
			sheetData.add(header);
			String[] noData = { "조회된 데이터가 없습니다." };
			sheetData.add(noData);
			makeRow(workbook, sheetData, sheetCnt, dataType);
			
		} else {
			for (int i = 0; i < dataList.size(); i = i + PER_SHEET) {
				List<String[]> sheetData = new ArrayList<String[]>();
				sheetCnt++;
				int last = dataList.size() <= PER_SHEET * sheetCnt ? dataList.size() : i + PER_SHEET;
				if(dataType != null){
					sheetData.add(dataType);
				}
				sheetData.add(header);
				sheetData.addAll(dataList.subList(i, last));
				makeRow(workbook, sheetData, sheetCnt, dataType);
			}
		}
		
		if(excelName != null && excelName != ""){
			response.setContentType("Application/Msexcel");
			response.setHeader("Content-Disposition", "ATTachment; Filename="+URLEncoder.encode(excelName, "UTF-8")+".xls");
		}
	}

	private void makeRow(Workbook workbook, List<String[]> dataList, int sheetCnt, String[] dataType) {

		Sheet sheet = workbook.createSheet("Sheet" + sheetCnt);

		sheet.createFreezePane(0, 1);

		sheet.setDefaultColumnWidth(12);
	    
	    if(dataType != null){
	    	DataFormat fmt = workbook.createDataFormat();
	    	CellStyle textStyle = workbook.createCellStyle();
	    	textStyle.setDataFormat(fmt.getFormat("TEXT"));
//	    	textStyle.setWrapText(true);
	    	
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

			CellStyle floatStyle = workbook.createCellStyle();
			floatStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));

	    	for(int i=0; i < dataType.length; i++){
//	    		숫자형 셀 설정
				if (dataType[i].equals("숫자")) {
					sheet.setDefaultColumnStyle(i, numberStyle);
				} else if (dataType[i].equals("퍼센트")) {
					sheet.setDefaultColumnStyle(i, floatStyle);
				} else {
					sheet.setDefaultColumnStyle(i, textStyle);
				}
				sheet.setColumnWidth(i, 4900);
	    	}
	    }
		
		int rowNum = 0;
		for (String[] arrRow : dataList) {

			Row row = sheet.createRow(rowNum++);

			int cellCnt = 0;
			for (String cellData : arrRow) {
				//create the row data
				Cell cell = row.createCell(cellCnt++);
				if (rowNum != 1 && rowNum != 2 && dataType != null) {
					try {
						if (dataType[cellCnt - 1].equals("숫자")) {
							cell.setCellValue(Long.parseLong(cellData));
						} else if (dataType[cellCnt - 1].equals("퍼센트")) {
							cell.setCellValue(Double.parseDouble(cellData) / 100);
						} else {
							cell.setCellValue(cellData);
						}
					} catch (Exception e) {
						cell.setCellValue(cellData);
					}
				} else {
					cell.setCellValue(cellData);
				}

				if ((rowNum == 1 && dataType == null) || ((rowNum == 1 || rowNum == 2) && dataType != null)) {// 헤더 스타일 적용
					Font font = workbook.createFont();
					font.setColor(HSSFColor.BLACK.index);
					font.setBold(true);
					CellStyle style = workbook.createCellStyle();
					style.setAlignment(CellStyle.ALIGN_CENTER);
					style.setFont(font);
					cell.setCellStyle(style);
				}

			}

		}
	}
}