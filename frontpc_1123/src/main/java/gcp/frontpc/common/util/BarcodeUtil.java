package gcp.frontpc.common.util;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class BarcodeUtil{
	
	private static int dpi = 150;
	
	private static String format = "png";
	
	private static boolean antiAliasing = false;
	
	private static AbstractBarcodeBean barcodeBean;
	
	
	public static void createBarcode(HttpServletResponse response,String data) throws Exception{
		barcodeBean = new Code128Bean();
		response.setContentType("image/gif");
		OutputStream out = response.getOutputStream();

		try {
			
			if(StringUtils.isNotEmpty(data)){
				 
				String mimeType = MimeTypes.expandFormat(format);
				int imageType   = BufferedImage.TYPE_BYTE_BINARY;
				BitmapCanvasProvider canvas = new BitmapCanvasProvider(
												out, 
												mimeType, 
												dpi, 
												imageType, 
												antiAliasing, 
												0);
				barcodeBean.doQuietZone(true);
				barcodeBean.setBarHeight(10); 
				barcodeBean.setModuleWidth(1);
				barcodeBean.setFontSize(4);
				barcodeBean.generateBarcode(canvas, data);
	
				canvas.finish();
				
				log.info("create image success.");
			}

		} catch (Exception e) {
			log.error("create image fail!");
			throw new RuntimeException(e.getMessage());
		}finally{
			try{
				out.close();
			}catch(IOException io){
				throw new RuntimeException(io.getMessage());
			}
		}
	}	
}