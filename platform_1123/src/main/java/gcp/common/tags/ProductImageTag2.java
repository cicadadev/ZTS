package gcp.common.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.ImageUrlUtil;
import lombok.Data;

@Data
public class ProductImageTag2 extends TagSupport{
	private final Log logger	= LogFactory.getLog(getClass());
	private static final long serialVersionUID = 7595181584158469126L;
	
	private String productId;
	private String seq;
	private String size;
	private String style;
	private String alt;
	private String className;
	private String var;
	
	
	@Override
	public int doStartTag() throws JspException {
		
		try {
			String returnUrl = ImageUrlUtil.productMakeURL(productId, size, seq, (HttpServletRequest) pageContext.getRequest());

//			if (CommonUtil.isEmpty(var)) {
//				pageContext.getOut().print(returnUrl);
//			} else {
//				pageContext.setAttribute(var, returnUrl);
//			}
			String altStr = "";
			if(CommonUtil.isNotEmpty(alt)){
				altStr="alt='"+alt+"'";
			}
			String styleStr = "";
			if(CommonUtil.isNotEmpty(style)){
				styleStr="style='"+style+"'";
			}
			String classStr = "";
			if(CommonUtil.isNotEmpty(className)){
				classStr="class='"+className+"'";
			}
					
					
			StringBuffer sb = new StringBuffer();
			JspWriter out = pageContext.getOut();
			
			String noimagesize = "";
			
			if(CommonUtil.isEmpty(size)){
				noimagesize = "326";
			}else{
				noimagesize = size;
			}
			
			sb.append("<img src='"+returnUrl+"' "+styleStr+" "+altStr+" "+classStr+" onError=\"this.src='/resources/img/noimage/no_image_"+noimagesize+".png';\" />");
			out.write(sb.toString());
			
			return SKIP_BODY;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage(), e);
			return SKIP_BODY;
		}
	}
	
}
