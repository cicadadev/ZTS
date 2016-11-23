/**
 * 
 */
package gcp.common.util;

import java.util.List;
import java.util.Map;

import gcp.ccs.model.CcsCode;
import intune.gsf.common.utils.CommonUtil;

/**
 * 
 * @Pagckage Name : gcp.common.util
 * @FileName : CodeUtil.java
 * @author : victor
 * @date : 2016. 4. 20.
 * @description :
 */
public class CodeUtil {

	static private Map<String, List<CcsCode>>	codeListMap;
//	static private List<CcsCodegroup>			codeListAll;

	public static List<CcsCode> getCodeList(String codeGroupCd) {
		List<CcsCode> codeList = codeListMap.get(codeGroupCd);
		
		return codeList;
	}

	/**
	 * 코드그룹, 코드값으로 코드명 조회
	 * @Method Name : getCodeName
	 * @author : eddie
	 * @date : 2016. 6. 24.
	 * @description : 
	 *
	 * @param codeGroupCd
	 * @param codeCd
	 * @return
	 */
	public static String getCodeName(String codeGroupCd, String codeCd) {
		List<CcsCode> codeList = codeListMap.get(codeGroupCd);

		if (codeList != null) {
			for (CcsCode code : codeList) {
				if (code.getCd().equals(codeCd)) {
					return code.getName();
				}
			}
		}
		return null;
	};

	/**
	 * 코드로 코드값 조회
	 * 
	 * @Method Name : getCodeName
	 * @author : eddie
	 * @date : 2016. 6. 24.
	 * @description :
	 *
	 * @param codeCd
	 * @return
	 */
	public static String getCodeName(String codeCd) {
		return getCodeName(codeCd.split("\\.")[0], codeCd);
	};
	
	
	/**
	 * 코드로 코드값 조회
	 * 
	 * @Method Name : getCodeName
	 * @author : eddie
	 * @date : 2016. 6. 24.
	 * @description :
	 *
	 * @param codeCd
	 * @return
	 */
	public static String getCodeNote(String codeCd) {
		if (CommonUtil.isNotEmpty(codeCd)) {
			String codeGroupCd = codeCd.split("\\.")[0];

			List<CcsCode> codeList = codeListMap.get(codeGroupCd);

			if (codeList != null) {
				for (CcsCode code : codeList) {
					if (code.getCd().equals(codeCd)) {
						return code.getNote();
					}
				}
			}
			return "";
		} else {
			return "";
		}
	};
	
	/**
	 * 
	 * @Method Name : getCodeValueByNote
	 * @author : dennis
	 * @date : 2016. 11. 8.
	 * @description : note로 cd return
	 *
	 * @param codeGroupCd
	 * @param note
	 * @return
	 */
	public static String getCodeValueByNote(String codeGroupCd, String note) {
		if (CommonUtil.isNotEmpty(note)) {

			List<CcsCode> codeList = codeListMap.get(codeGroupCd);

			if (codeList != null) {
				for (CcsCode code : codeList) {
					if (CommonUtil.isNotEmpty(code.getNote()) && code.getNote().equals(note)) {
						return code.getCd();
					}
				}
			}
			return "";
		} else {
			return "";
		}

	}


//	public static String getCodeLangName(String codeGroupCd, String codeCd, String langCd) {
//		for (CcsCodegroup codegroup : codeListAll) {
//			for (BaseCcsCode code : codegroup.getBaseCcsCodes()) {
//				List<BaseCcsCodelang> codeLangList = code.getBaseCcsCodelangs();
//				for (BaseCcsCodelang codeLang : codeLangList) {
//					if (codeLang.getLangCd().equals(langCd) && codeLang.getCd().equals(codeCd)) {
//						return codeLang.getName();
//					}
//				}
//
//				if (code.getCdGroupCd().equals(codeGroupCd) && code.getCd().equals(codeCd)) {
//					return code.getName();
//				}
//			}
//		}
//		return null;
//	}

//	public static String getCodeValues(String codeGroup) {
//		LinkedMap lm = (LinkedMap) codeListMap.get(codeGroup);
//		// if (lm != null)
//		// return ((CodeItem) lm.get(code)).getName();
//		// else
//		// return "NA";
//
//		// List codeList = CodeUtil.getCodeList(codeGroup);
//		// pageContext.setAttribute(var, codeList);
//
//		return null;
//	}
//
//	public static String getCodeValue(String codeGroup, String code) {
//		LinkedMap lm = (LinkedMap) codeListMap.get(codeGroup);
//		// if (lm != null)
//		// return ((CodeItem) lm.get(code)).getName();
//		// else
//		// return "NA";
//
//		// List codeList = CodeUtil.getCodeList(codeGroup);
//		// pageContext.setAttribute(var, codeList);
//
//		return null;
//	}

	public static Map<String, List<CcsCode>> getCodeListMap() {
		return codeListMap;
	}

	public static void setCodeListMap(Map<String, List<CcsCode>> codeListMap) {
		CodeUtil.codeListMap = codeListMap;
	}

//	public static List<CcsCodegroup> getCodeListAll() {
//		return codeListAll;
//	}
//
//	public static void setCodeListAll(List<CcsCodegroup> codeListAll) {
//		CodeUtil.codeListAll = codeListAll;
//	}

}
