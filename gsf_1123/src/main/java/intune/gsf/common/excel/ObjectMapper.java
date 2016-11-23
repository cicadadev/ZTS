/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package intune.gsf.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.Maps;

import intune.gsf.common.excel.annotation.Excel;
import intune.gsf.common.excel.util.EachFieldCallback;
import intune.gsf.common.excel.util.ReflectionUtils;
import intune.gsf.common.exceptions.EtcException;

/**
 * @author redcrow
 */
public class ObjectMapper {

	private static final Logger logger = LoggerFactory.getLogger(ObjectMapper.class);

	private final File excelFile;
	private Class clazz;

	private ObjectMapper(File excelFile) {
		this.excelFile = excelFile;
	}

	public static ObjectMapper readfile(File excelFile) {
		return new ObjectMapper(excelFile);
	}

	public ObjectMapper convert(Class clazz) {
		this.clazz = clazz;
		return this;
	}
	
	private Object getValueByName(String annoName, Row row, Map<String, Integer> headerMap) {
		if (headerMap.get(annoName) == null) {
			return null;
		}

		Cell cell = row.getCell(headerMap.get(annoName));
		return getCellValue(cell);
	}

	private void mapName2Index(String annoName, Row row, Map<String, Integer> headerMap) {
		int index = findIndexCellByName(annoName, row);
		if (index != -1) {
			headerMap.put(annoName, index);
		}
	}

	private int findIndexCellByName(String annoName, Row row) {
		Iterator<Cell> iterator = row.cellIterator();
		while (iterator.hasNext()) {
			Cell cell = iterator.next();
			String headerName = ((String) getCellValue(cell)).trim();
			String[] annoList = StringUtils.split(annoName, ",");
			for (String name : annoList) {
				if (headerName.startsWith(name)) {
					return cell.getColumnIndex();
				}
			}
		}
		return -1;
	}

	private void readExcelHeader(final Row row, final Map<String, Integer> headerMap) throws Exception {
		ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

			@Override
			public void each(Field field, String annoName) throws Exception {
				mapName2Index(annoName, row, headerMap);
			}
		});
	}

	private Object readExcelContent(final Row row, final Map<String, Integer> headerMap) throws Exception {
		final Object instance = clazz.newInstance();
		ReflectionUtils.eachFields(clazz, new EachFieldCallback() {

			@Override
			public void each(Field field, String annoName) throws Exception {
				Object value = getValueByName(annoName, row, headerMap);
				if (!ObjectUtils.isEmpty(value)) {
					ReflectionUtils.setValueOnField(instance, field, value);
				}
			}
		});

		return instance;
	}
	
	private boolean isVersion2003(File file) {
		return file.getName().endsWith(".xls");
	}

	private Workbook createWorkbook(InputStream inputStream) throws IOException {
		if (isVersion2003(excelFile)) {
			return new HSSFWorkbook(inputStream);
		} else { // 2007+
			return new XSSFWorkbook(inputStream);
		}
	}

	// public <T> List<T> map() throws Exception {
	// public <T> Map<String, Object> map() throws Exception {
	@SuppressWarnings("unchecked")
	public <T> Map<String, Object> map(String type) throws Exception {
		InputStream inputStream = null;
		List<T> dataList = new LinkedList<>();
		Map<String, Object> resultMap = Maps.newHashMap();
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
		int totalCount = 0;

		try {
			Iterator<Row> rowAll;
			inputStream = new FileInputStream(excelFile);
			Workbook workbook = createWorkbook(inputStream);
//			int sheetCount = workbook.getNumberOfSheets();
			int sheetCount = 1;	// hardcoding read first excel sheet

			for (int index = 0; index < sheetCount; index++) { // sheet
				Sheet sheet = workbook.getSheetAt(index);
				rowAll = sheet.iterator();
				// totalCount = sheet.getLastRowNum();
				totalCount = sheet.getPhysicalNumberOfRows();

				Map<String, Integer> headerMap = new HashMap<>();
				while (rowAll.hasNext()) { // row
					Row row = rowAll.next();
					if (row != null) {
						int rownum = row.getRowNum();

						try {
							// column
							if ("name".equals(type)) {
								if (rownum == 1) {
									readExcelHeader(row, headerMap);
								} else if (rownum > 1) {
									dataList.add((T) readExcelContent(row, headerMap));
								}
							} else if ("idx".equals(type)) {
								if (rownum > 1) {

									int cellCnt = row.getPhysicalNumberOfCells();

									Field[] fields = clazz.getDeclaredFields();
//									List<T> innerList = new LinkedList<>();
									Object instance = clazz.newInstance();
									for (int i = 0; i < cellCnt; i++) {
										Object obj = getCellValue(row.getCell(i));
										for (Field field : fields) {
											Excel excel = field.getAnnotation(Excel.class);
											if (excel != null) {
												if (excel.name().equals(String.valueOf(i))) {
													ReflectionUtils.setValueOnField(instance, field, obj);
												}
											}
										}
									}
									dataList.add((T) instance);
								}
							}

						} catch (Exception e) {
							logger.debug(">>> excel read error, " + (rownum - 1) + " 번째 행 : " + e.getMessage());
							Map<String, String> errmap = Maps.newHashMap();
							errmap.put((rownum - 1) + " 번째 행", e.getMessage());
							errorList.add(errmap);
						}
					}
				}
			}
		} catch (FileNotFoundException e) { // 파일없음
			logger.debug("FileNotFoundException", e.getMessage());
		} catch (IOException e) { // 파일 import 에러
			logger.debug("IOException", e.getMessage());
		} catch (Exception e) {
			logger.debug("##### excel file object mapping exception : ", e.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		resultMap.put("totalCount", totalCount - 2);
		resultMap.put("dataList", dataList);
		resultMap.put("errorList", errorList);

		// return items;
		return resultMap;
	}

	// private String getCellValue(Cell cell) {
	private Object getCellValue(Cell cell) {
		Object value = null;

		if (cell != null) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						value = transFormat.format(cell.getDateCellValue());
					} else {
						value = new BigDecimal(cell.getNumericCellValue()).setScale(2, RoundingMode.HALF_UP);
					}
					break;
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_FORMULA:
					value = cell.getCellFormula();
					break;
				case Cell.CELL_TYPE_BLANK:
					value = null;
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					value = String.valueOf(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_ERROR:
					// list.add(cell.getCellFormula());
					break;
				default:
					value = cell.getStringCellValue();
					break;
			}
		}
		return value;
	}
}
