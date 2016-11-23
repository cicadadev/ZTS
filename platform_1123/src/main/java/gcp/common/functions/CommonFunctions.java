package gcp.common.functions;

import java.util.ArrayList;
import java.util.List;

import gcp.ccs.model.CcsCode;
import gcp.common.util.CodeUtil;
import intune.gsf.common.utils.CommonUtil;

public class CommonFunctions {

	// private static final Logger logger = Logger.getLogger(CommonFunctions.class);

	public static List<CcsCode> getCodeList(String groupCode) {
		return CodeUtil.getCodeList(groupCode);
	}

	public static List<CcsCode> getCodeList(String groupCode, List<Long> exceptions) {
		List<CcsCode> codeList = CodeUtil.getCodeList(groupCode);
		List<CcsCode> retList = new ArrayList<CcsCode>();
		if (exceptions != null && exceptions.size() > 0) {
			for (CcsCode code : codeList) {
				if (!exceptions.contains(code.getSortNo())) {
					retList.add(code);
				}
			}
		}
		return retList;
	}

	public static String getCodeName(String code) {
		List<CcsCode> codeList = CodeUtil.getCodeList(code.split(".")[0]);

		for (CcsCode c : codeList) {
			if (code.equals(c.getCd())) {
				return c.getName();
			}
		}
		return "";
	}

	public static String getPrice(String value, String sign) {
		if (!CommonUtil.isEmpty(value)) {
			try {
				String money = CommonUtil.formatMoney(value);
				if (!CommonUtil.isEmpty(sign)) {
					if (!"0".equals(money)) {
						return sign + " " + money;
					} else {
						return money;
					}
				} else {
					return money;
				}
			} catch (Exception e) {
			}
		}
		return "0";
	}
}
