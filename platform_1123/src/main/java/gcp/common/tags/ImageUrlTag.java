package gcp.common.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.ImageUrlUtil;
import lombok.Data;

@Data
public class ImageUrlTag extends TagSupport{
	private final Log logger	= LogFactory.getLog(getClass());
	private static final long serialVersionUID = 7595181584158469126L;
	
	private String productId;
	private String path;
	private String size;
	private String var;
	
	
	@Override
	public int doStartTag() throws JspException {
		
		try {
			String returnUrl = ImageUrlUtil.productMakeURL(productId, size, path, (HttpServletRequest) pageContext.getRequest());

			if (CommonUtil.isEmpty(var)) {
				pageContext.getOut().print(returnUrl);
			} else {
				pageContext.setAttribute(var, returnUrl);
			}
			
			return SKIP_BODY;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
			return SKIP_BODY;
		}
	}
	
}
