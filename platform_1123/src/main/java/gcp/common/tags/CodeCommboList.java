package gcp.common.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsCodelang;
import gcp.ccs.model.base.BaseCcsCode;
import gcp.ccs.model.base.BaseCcsCodelang;
import gcp.common.util.CodeUtil;
import intune.gsf.common.exceptions.EtcException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.MessageUtil;

public class CodeCommboList extends TagSupport {

	private static final long	serialVersionUID	= -1774048226116492277L;
	private static final Logger	logger				= Logger.getLogger(CodeCommboList.class);
	private String				var;
	private String				codeGroupCd;
	private String				value;
	private String				ngModel;
	private String				langCd;
	private String				className;

	public String getLangCd() {
		return langCd;
	}

	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getNgModel() {
		return ngModel;
	}

	public void setNgModel(String ngModel) {
		this.ngModel = ngModel;
	}

	public String getCodeGroupCd() {
		return codeGroupCd;
	}

	public void setCodeGroupCd(String codeGroupCd) {
		this.codeGroupCd = codeGroupCd;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getVar() {
		return var;
	}

	public int doStartTag() throws JspException {
//		String codeGroup = (String) ExpressionEvaluatorManager.evaluate("codeGroup", codeGroupExp, String.class, this, pageContext);
		try {
			List<CcsCode> codeList = CodeUtil.getCodeList(codeGroupCd);
			JspWriter out = pageContext.getOut();

			String options = "<option value=''>" + MessageUtil.getMessage("common.search.commbo.default") + "</option>";
			String ngModelStr = "";
			if (CommonUtil.isNotEmpty(ngModel)) {
				ngModelStr = " ng-model='" + ngModel + "' ";
			}

			String classStr = "";
			if (CommonUtil.isNotEmpty(className)) {
				classStr = "class='" + className + "' ";
			}

			String tags = "<select " + ngModelStr + classStr;
			String selected = "";
			for (BaseCcsCode ccsCode : codeList) {
				if (CommonUtil.isNotEmpty(value) && ccsCode.getCd().equals(value)) {
					selected = " ng-init=\"" + ngModel + "='" + value + "'\"";
				}

				String codeName = ccsCode.getName();

				if (CommonUtil.isNotEmpty(langCd)) {
					List<CcsCodelang> langs = ccsCode.getCcsCodelangs();
					if (langs != null) {
						for (BaseCcsCodelang lang : langs) {
							if (lang.getLangCd().equals(langCd)) {
								codeName = lang.getName();
								break;
							}
						}
					}
				}
				options += "<option value='" + ccsCode.getCd() + "'>" + codeName + "</option>";
			}
			tags = tags + selected + ">" + options + "</select>";
			logger.debug("tags:" + tags);

			out.append(tags);

		} catch (EtcException | IOException e) {
//			logger.error(StringUtils.abbreviate("codeListTag error for <var='" + var + "', codeGroup='" + codeGroupCd + "'> : "
//					+ StackTraceUtil.getStackTrace(e), 2000));
		}
		return SKIP_BODY;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
