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

public class CodeCheckBoxList extends TagSupport {

	private static final long	serialVersionUID	= -1774048226116492277L;
	private static final Logger	logger				= Logger.getLogger(CodeCheckBoxList.class);
	private String				var;
	private String				codeGroupCd;
	private String				value;
	private String				ngModel;
	private String				langCd;
	private String				ngChecked;

	public String getNgChecked() {
		return ngChecked;
	}

	public void setNgChecked(String ngChecked) {
		this.ngChecked = ngChecked;
	}

	public String getLangCd() {
		return langCd;
	}

	public void setLangCd(String langCd) {
		this.langCd = langCd;
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
			String tags = "";
			int i = 0;
			for (BaseCcsCode code : codeList) {
				String codeName =code.getName();

				if (CommonUtil.isNotEmpty(langCd)) {
					List<CcsCodelang> langs = code.getCcsCodelangs();
					if (langs != null) {
						for (BaseCcsCodelang lang : langs) {
							if (lang.getLangCd().equals(langCd)) {
								codeName = lang.getName();
								break;
							}
						}
					}
				}
				String model = "";
				if (CommonUtil.isNotEmpty(ngModel)) {
					model = "ng-model=\"" + ngModel + ".val" + i++ + "\"";
				}

				String ngCheckedStr = "";
				if (CommonUtil.isNotEmpty(ngChecked)) {
					ngCheckedStr = "ng-checked=\"" + ngChecked + "\"";
				}
				
				tags += "<input type=\"checkbox\" " + model + " " + ngCheckedStr + " ng-true-value=\"'" + code.getCd()
						+ "'\" ><label>" + codeName
						+ "</label>";
			}

			out.append(tags);

		} catch (EtcException | IOException e) {

		}
		return SKIP_BODY;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
