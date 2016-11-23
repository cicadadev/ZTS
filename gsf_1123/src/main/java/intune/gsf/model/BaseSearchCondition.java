package intune.gsf.model;

import java.util.List;

import intune.gsf.common.utils.CommonUtil;
import lombok.Data;

@Data
public class BaseSearchCondition extends BaseEntity {

	private static final long serialVersionUID = -8913690495665575999L;
	
	/* search */
	private String		searchId;
	private String		storeId;
	private String		langCd;
	private String		searchKeyword;
	private String		startDate;	//시작일
	private String 		endDate;	//종료일

	/* excel download */
	private List<String> field;
	private List<String> headerNm;
	private String 		fileNm;
	
	/* paging */
	private int			firstRow ;
	private int			lastRow;
	private String		sort;
	private String		direction;
	private int 		currentPage 	= 1;
	private int 		pageSize 		= 40;
	private String 		pagingYn;
	private int 		searchFirstRow; //검색엔진의 시작페이지
	
	public int getFirstRow() {
		return (firstRow > 0)?firstRow:((currentPage-1)*pageSize)+1;
	}
	
	public int getSearchFirstRow(){
		return (searchFirstRow > 0)?searchFirstRow:(currentPage-1)*pageSize;
	}

	public int getlastRow() {
		return (lastRow > 0)?lastRow:currentPage*pageSize;
	}
	
	
//	public Timestamp getStartTimestamp() throws ParseException {
//		String startDate = getStartDate();
//		Timestamp timeStamp = null;
//		if(!CommonUtil.isEmpty(startDate)){
//			if(startDate.length() == 10){
//				startDate = startDate + " 00:00:00";
//			}
//			timeStamp = DateUtil.convertStringToTimestamp(startDate, DateUtil.FORMAT_1);
//		}
//		return timeStamp;
//	}

	public String getEndDate(){
		if(!CommonUtil.isEmpty(endDate)){
			if(endDate.length() == 10){
				endDate = endDate + " 23:59:59";
			}			
		}
		return endDate;
	}

}
