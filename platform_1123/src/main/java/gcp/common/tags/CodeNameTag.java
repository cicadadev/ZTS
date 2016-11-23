package gcp.common.tags;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import gcp.common.util.CodeUtil;
import intune.gsf.common.utils.CommonUtil;

public class CodeNameTag extends RequestContextAwareTag {
	private final Log	logger	= LogFactory.getLog(getClass());
	private static final long serialVersionUID = 7595181584158469126L;
	
	private String 		code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	protected int doStartTagInternal() throws Exception {

		String codeName = null;
		try {
			codeName = CodeUtil.getCodeName(getCode());
		} catch (Exception e) {
			codeName = "";
		}
		if (CommonUtil.isEmpty(codeName)) {
			codeName = "";
		}
		JspWriter out = pageContext.getOut();
		out.write(codeName);

		return EVAL_BODY_INCLUDE;
	}
	
}
