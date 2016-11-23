package intune.gsf.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.model.BaseSearchCondition;

/**
 * @Pagckage Name : intune.gsf.common.utils
 * @FileName : ExcelUtil.java
 * @author : ian
 * @date : 2016. 5. 20.
 * @description :
 */
public class ExcelUtil {

	public static String excelDownlaod(BaseSearchCondition search, List<Map<String, String>> dataList) {
		
		
		List<String> fieldList = search.getField();
		List<String> headerNmList = search.getHeaderNm();
		String fileNm = search.getFileNm();
		
		//1차로 workbook을 생성 
		XSSFWorkbook workbook = new XSSFWorkbook();
		//2차는 sheet생성 
		XSSFSheet sheet = workbook.createSheet("Sheet1");
		//엑셀의 행 
		XSSFRow row = sheet.createRow(0);
		//엑셀의 셀 
		XSSFCell cell = null;

		// 헤더값 컬럼 저장
		for (int i = 0; i < headerNmList.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerNmList.get(i));
		}

		// 본문 저장
		for (int i = 0; i < dataList.size(); i++) {
			row = sheet.createRow(i + 1);
			int k = 0;
			for (int j = 0; j < fieldList.size(); j++) {
				for (String key : dataList.get(i).keySet()) {
					if (StringUtils.equals(fieldList.get(j), key)) {
						String value = dataList.get(i).get(key);
						if (value == null) {
							cell = row.createCell(k);
							cell.setCellValue("");
						} else {
							cell = row.createCell(k);
							cell.setCellValue(String.valueOf(value));
						}
						k++;
					}
				}

			}

		}

		String path = "C:\\excel\\";

		File dirPath = new File(path);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		String date = DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		String msg = "";
		String result = path + fileNm + "_" + date.replaceAll(":", "") + ".xlsx";
		File file = new File(result);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			//파일을 쓴다
			workbook.write(fos);

			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			msg = "error: FileNotFound";
			return msg;
		} catch (IOException ie) {
			ie.printStackTrace();
			msg = "error: IOExcepiton";
			return msg;
		} finally {
			try {
				//필수로 닫아주어야함
				if (workbook != null) {
					workbook.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				msg = "error: Finally _ IOExcepiton";
				e.printStackTrace();
				return msg;
			}
		}
	}

	public static List<List<Object>> readExcelFile(MultipartHttpServletRequest req) throws Exception {
		List<List<Object>> objList = new ArrayList<List<Object>>();
		Iterator<String> it = req.getFileNames();
		MultipartFile mfile = null;

		try {
			while (it.hasNext()) {
				mfile = req.getFile(it.next());
				Workbook book = WorkbookFactory.create(mfile.getInputStream());

				// 0번째 sheet
				Sheet sheet = book.getSheetAt(0);

				int rowCnt = sheet.getPhysicalNumberOfRows();
				Row firstRow = sheet.getRow(0);
				int cellCnt = firstRow.getPhysicalNumberOfCells();
				
				for (int i = 1; i < rowCnt; i++) {
					List<Object> list = new ArrayList<Object>();
					Row row = sheet.getRow(i);
					for (int j = 0; j < cellCnt; j++) {
						Cell cell = row.getCell(j);
						if (cell == null) {
							list.add("");
						} else {
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_BOOLEAN:
								list.add(cell.getBooleanCellValue());
							break;
							case Cell.CELL_TYPE_NUMERIC:
								list.add((int) cell.getNumericCellValue());
							break;
							case Cell.CELL_TYPE_STRING:
								list.add(cell.getStringCellValue());
							break;
							case Cell.CELL_TYPE_BLANK:
								list.add("");
							break;
							case Cell.CELL_TYPE_FORMULA:
								list.add(cell.getCellFormula());
							break;
							}
						}
					}
					objList.add(list);
				}
				book.close();
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage());
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessageCd(), e.getArgs());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return objList;
	}

//	public static String createExcelFile(BaseSearchCondition search, List<Map<String, String>> dataList) {
	public static String createExcelFile(List<String> headerNmList, List<Map<String, String>> errorList, String uploadPath, String fileName) {

		// 1차로 workbook을 생성
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 2차는 sheet생성
		XSSFSheet sheet = workbook.createSheet("Sheet1");
		// 엑셀의 행
		XSSFRow row = sheet.createRow(0);
		// 엑셀의 셀
		XSSFCell cell = null;

		// 헤더값 컬럼 저장
		for (int i = 0; i < headerNmList.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerNmList.get(i));
		}

		// 본문 저장
		int errrow = 1; // 1부터
		for (Map<String, String> err : errorList) {
			row = sheet.createRow(errrow);

			for (String key : err.keySet()) {
				cell = row.createCell(0);
				cell.setCellValue(key);

				cell = row.createCell(1);
				cell.setCellValue(err.get(key));
			}
			errrow++;
		}

		// 파일 저장
//		String uploadPath = Config.getString("excel.download.path.template.temp");
//		String uploadPath = Config.getString("excel.upload.path.error");

		uploadPath += DateUtil.getCurrentDate(DateUtil.FORMAT_7);
		File dirPath = new File(uploadPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		String msg = "";
//		String fullpath = uploadPath + File.separator + fileName + "_" + DateUtil.getCurrentDate("yyyyMMddHHmmss") + ".xlsx";
		String fullpath = uploadPath + "/" + fileName + "_" + DateUtil.getCurrentDate("yyyyMMddHHmmss") + ".xlsx";
		File file = new File(fullpath);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			// 파일을 쓴다
			workbook.write(fos);

			return fullpath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			msg = "error: FileNotFound";
			return msg;
		} catch (IOException ie) {
			ie.printStackTrace();
			msg = "error: IOExcepiton";
			return msg;
		} finally {
			try {
				// 필수로 닫아주어야함
				if (workbook != null) {
					workbook.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				msg = "error: Finally _ IOExcepiton";
				e.printStackTrace();
				return msg;
			}
		}
	}

}
