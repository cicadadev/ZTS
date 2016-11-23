package gcp.frontpc.common.tiles;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;

import gcp.common.util.FoSessionUtil;

public class TilesPreparer implements ViewPreparer {

	@Override
	public void execute(Request request, AttributeContext attrContext) {

		String layoutPath = "/WEB-INF/views/gsf/layout";

		/*attrContext.putAttribute("commonScript", new Attribute(layoutPath + "/common/commonScript.jsp"));*/

		String deviceType = "";
		if (FoSessionUtil.isMobile()) {
			deviceType = "mo";

			attrContext.putAttribute("left_mo", new Attribute(layoutPath + "/page/mo/left_mo.jsp"));
			attrContext.putAttribute("right_mo", new Attribute(layoutPath + "/page/mo/right_mo.jsp"));
			//attrContext.putAttribute("bottom", new Attribute(layoutPath + "/page/mo/bottom_mo.jsp"));
			attrContext.putAttribute("commonLayer_mo", new Attribute(layoutPath + "/page/mo/commonLayer.jsp"));

		} else {
			deviceType = "pc";

			attrContext.putAttribute("sky_pc", new Attribute(layoutPath + "/page/pc/sky_pc.jsp"));
			/*좌측 윙배너*/
			attrContext.putAttribute("adBanner_pc", new Attribute(layoutPath + "/page/pc/adBanner_pc.jsp"));
			attrContext.putAttribute("commonLayer_pc", new Attribute(layoutPath + "/page/pc/commonLayer.jsp"));
		}
		attrContext.putAttribute("commonCss", new Attribute(layoutPath + "/common/commonCss_" + deviceType + ".jsp"));
		attrContext.putAttribute("commonHead", new Attribute(layoutPath + "/common/commonHead_" + deviceType + ".jsp"));
		//attrContext.putAttribute("header", new Attribute(layoutPath + "/page/" + deviceType + "/header_" + deviceType + ".jsp"));
//		attrContext.putAttribute("footer", new Attribute(layoutPath + "/page/" + deviceType + "/footer_" + deviceType + ".jsp"));

	}
}
