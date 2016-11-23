package gcp.common.tags;

import java.math.BigDecimal;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import gcp.common.util.PmsUtil;
import intune.gsf.common.utils.CommonUtil;

public class DcRate extends RequestContextAwareTag {

	private static final long serialVersionUID = -9201262118146697591L;

	private String stdPrice;
	private String dcPrice;
	private String loc;
	private String mode;

	private String tagYn;
	private String var;

	public String getStdPrice() {
		return stdPrice;
	}

	public String getDcPrice() {
		return dcPrice;
	}

	public String getLoc() {
		return loc;
	}

	public String getMode() {
		return mode;
	}

	public void setStdPrice(String stdPrice) {
		this.stdPrice = stdPrice;
	}

	public void setDcPrice(String dcPrice) {
		this.dcPrice = dcPrice;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

		if (CommonUtil.isNotEmpty(dcPrice)) {
			if(CommonUtil.isEmpty(loc)) {
				loc = "0";
			}
			if(CommonUtil.isEmpty(mode)) {
				mode = "2";
			}
			
			
			BigDecimal result = PmsUtil.getDcRate(new BigDecimal(stdPrice), new BigDecimal(dcPrice), Integer.parseInt(loc), Integer.parseInt(mode));

			if (CommonUtil.isNotEmpty(var)) {
				pageContext.setAttribute(var, result);
			} else {
				JspWriter out = pageContext.getOut();
				out.write(result.toString());
			}
			
			

			return EVAL_BODY_INCLUDE;
		} else {
			return EVAL_BODY_INCLUDE;
		}
		
	}

}
