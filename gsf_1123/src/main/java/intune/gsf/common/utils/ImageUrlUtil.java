package intune.gsf.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * 이미지 URL 생성을 위한 유틸
 * 
 * @Pagckage Name : intune.gsf.common.utils
 * @FileName : ProductImageUrlUtil.java
 * @author : emily
 * @date : 2016. 8. 5.
 * @description :
 */
public class ImageUrlUtil {

	private static final Logger	logger	= Logger.getLogger(ImageUrlUtil.class);
	
	/**
	 * 상품 이미지URL을 생성한다.
	 * 
	 * @Method Name : productMakeURL
	 * @author : intune
	 * @date : 2016. 8. 5.
	 * @description :
	 *
	 * @param productId
	 * @param no
	 * @param url
	 * @param scheme
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static String productMakeURL(String productId, String size, String seq, HttpServletRequest req) {

		String returnUrl = null;

		try {
			String relUrl = Config.getString("image.domain");
			if (req != null && req.isSecure()) {
				relUrl = Config.getString("image.ssl.domain");
			}

			if (CommonUtil.isEmpty(seq)) {
				seq = "0";
			}

			String productImagePath = Config.getString("product.image.path");
			String sizeStr = "";
			if (CommonUtil.isNotEmpty(size)) {
				sizeStr = seq + "_" + size;
			} else {
				sizeStr = seq;
			}
//			String urlStr = relUrl + productImagePath + "/" + productId + "/" + productId + "_" + sizeStr + ".jpg?d="
//					+ DateUtil.getCurrentDate(DateUtil.FORMAT_10);
			String urlStr = relUrl + productImagePath + "/" + productId + "/" + productId + "_" + sizeStr + ".jpg";
			returnUrl = removeTrippleSlash(urlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnUrl;
	}

	/**
	 * URL Scheme Format 
	 * @Method Name : removeTrippleSlash
	 * @author : intune
	 * @date : 2016. 8. 5.
	 * @description : 
	 *
	 * @param url
	 * @return
	 */
	private static String removeTrippleSlash(String url) {
		
		if (url.contains("://")) {
			String scheme = url.substring(0, url.indexOf("://") + 3);
			String remains = url.substring(url.indexOf("://") + 3);
			String removed = scheme + removeDoubleSlash(remains.replaceAll("//", "/"));
			return removed;
		}

		return url.replaceAll("///", "/").replaceAll("//", "/");
	}

	private static String removeDoubleSlash(String url) {
		return url.replaceAll("//", "/");
	}
}
