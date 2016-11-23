package gcp.common.tags;

import java.math.BigDecimal;

import javax.servlet.jsp.JspWriter;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import gcp.common.util.PmsUtil;

public class DcRatePrice extends RequestContextAwareTag {
	private static final long serialVersionUID = 7595181584158469126L;
	
	private String				dcRate;
	private String				price;
	private String				format;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDcRate() {
		return dcRate;
	}

	public void setDcRate(String dcRate) {
		this.dcRate = dcRate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	protected int doStartTagInternal() throws Exception {

		BigDecimal result = PmsUtil.applyDcRateToPrice(new BigDecimal(price), new BigDecimal(dcRate));
		JspWriter out = pageContext.getOut();
		if (Boolean.parseBoolean(format)) {
			out.write(String.format("%,d", result.longValue()));
		} else {
			out.write(result.toString());
		}

		return EVAL_BODY_INCLUDE;
	}

}
