package gcp.common.tags;

import java.util.List;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import gcp.ccs.model.CcsCode;
import gcp.common.util.CodeUtil;
import intune.gsf.common.utils.CommonUtil;

public class CodeListTag extends RequestContextAwareTag {
	private final Log	logger	= LogFactory.getLog(getClass());
	private static final long serialVersionUID = 7595181584158469126L;
	
	private String 		code;
	private String				optionHead;
	private String				tagYn;
	private String				var;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOptionHead() {
		return optionHead;
	}

	public void setOptionHead(String optionHead) {
		this.optionHead = optionHead;
	}

	public String getTagYn() {
		return tagYn;
	}

	public void setTagYn(String tagYn) {
		this.tagYn = tagYn;
	}


	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	protected int doStartTagInternal() throws Exception {

		List<CcsCode> codeList = null;
		try {
			codeList = CodeUtil.getCodeList(getCode());
		} catch (Exception e) {
			
		}
		
		if (CommonUtil.isNotEmpty(tagYn) && "N".equals(tagYn)) {
			pageContext.setAttribute(var, codeList);
		} else {
			StringBuffer sb = new StringBuffer();
			JspWriter out = pageContext.getOut();

			sb.append("<select onChange='chgSelect(this)'>");

			if (optionHead != null && optionHead != "") {
				sb.append("<option value=\"\">");
				sb.append(optionHead);
				sb.append("</option>");
			} else {
				sb.append("<option value=\"\">");
				sb.append("전체");
				sb.append("</option>");
			}

			for (int i = 0; i < codeList.size(); i++) {
				sb.append("<option value=\"" + codeList.get(i).getCd() + "\" name=\"" + codeList.get(i).getName() + "\"");
				if (codeList.get(i).getName().equals(var)) {
					sb.append("selected=selected");
				}
				sb.append(">" + codeList.get(i).getName() + "</option>");
			}

			sb.append("</select>");
			out.write(sb.toString());
		}
		return EVAL_BODY_INCLUDE;
	}
	
}
