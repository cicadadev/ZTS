package intune.gsf.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class Download extends AbstractView {
	@Override
	protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		File file = (File)map.get("fileName");
        System.out.println("DownloadView --> file.getPath() : " + file.getPath());
        System.out.println("DownloadView --> file.getName() : " + file.getName());
         
        res.setContentType(getContentType());
        res.setContentLength((int)file.length());
         
        String userAgent = req.getHeader("User-Agent");
         
        boolean ie = userAgent.indexOf("MSIE") > -1;
         
        String fileName = null;
         
        if(ie){
             
            fileName = URLEncoder.encode(file.getName(), "utf-8");
                         
        } else {
             
            fileName = new String(file.getName().getBytes("utf-8"));
             
        }// end if;
 
         
		res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
         
		res.setHeader("Content-Transfer-Encoding", "binary");
         
		OutputStream out = res.getOutputStream();
         
        FileInputStream fis = null;
         
        try {
             
            fis = new FileInputStream(file);
             
            FileCopyUtils.copy(fis, out);
             
             
        } catch(Exception e){
             
            e.printStackTrace();
             
        }finally{
             
            if(fis != null){
                 
                try{
                    fis.close();
                }catch(Exception e){}
            }
             
        }// try end;
         
        out.flush();
         
    }// render() end;
}