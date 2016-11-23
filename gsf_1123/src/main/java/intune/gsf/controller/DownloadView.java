package intune.gsf.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import intune.gsf.common.exceptions.ServiceException;

public class DownloadView extends AbstractView {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public DownloadView() {
		setContentType("application/download;charset=utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		File file = (File) model.get("downloadFile");
		
		logger.debug("download filename : {}", file.getName());
		
		if(!file.exists()) {
			throw new ServiceException("fileNotExists");
		}
		
		response.setContentType(getContentType());
		response.setContentLength((int)file.length());
		
		String fileName = URLEncoder.encode(file.getName(), "UTF-8");
		
		response.setHeader("Content-Disposition",  "attachment;filename=\""+fileName+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		OutputStream os = response.getOutputStream();
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, os);
		} catch(Exception e) {
			throw new ServiceException("file.download.error", e.getMessage());
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		os.flush();
	}

}
