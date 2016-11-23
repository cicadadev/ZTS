package intune.gsf.common.utils;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import intune.gsf.model.Result;


/**
 * 파일을 사용자측(Web Browser)로 전송하는 역활을 수행. 웹서버의 기능을 이용할수
 * 없을때, 또는 mime-type을 octet-stream 으로 지정해야 할때 사용한다.
 *
 * @author Kwonki Yoon(Ryan)
 * @version 1.0
 *
 */
public class FileDownloader {

	private static Logger log = LoggerFactory.getLogger(FileDownloader.class);

	static final String DOWNLOAD_ERROR_MESSAGE = "Download failed.";
	static final String FILE_NOT_FOUND = "File not found.";
	static final String ILLEGAL_PATH = "The file path is not allowed.";
	static final String IS_NOT_FILE = "The source is not a file.";

	/**
	 * 파일을 다운로드 한다.
	 *
	 * @param response HttpServletResponse
	 * @param sourcePath 다운로드 파일의 풀경로.("/some/path/filname.extention")
	 * @param filename 사용자에게 보여줄 이름.(ex. wordfile.doc)
	 * @return
	 */
	public static Result download(HttpServletResponse response, String sourcePath, String filename) {
		return download(response, sourcePath, filename, "1");
	}

	public static Result download(HttpServletResponse response, String sourcePath) {
		String filename = StringUtils.substringAfterLast(sourcePath, "/");
		return download(response, sourcePath, filename, "1");
	}
	
	/**
	 * 파일을 다운로드 한다.
	 *
	 * @param response HttpServletResponse
	 * @param sourcePath 다운로드 파일의 풀경로.("/some/path/filname.extention")
	 * @param filename 사용자에게 보여줄 이름.(ex. wordfile.doc)
	 * @param ieFlag 브라우저별 다국어 처리용.( 1 = IE , 0 = 기타 브라우저)
	 * @return
	 */
	public static Result download(HttpServletResponse response, String sourcePath, String filename, String ieFlag) {
		log.info("Requested download file : {} as filename: {}", sourcePath, filename);
		Result result = new Result();

		File file = new File(sourcePath);

//		if (isAllowedPath(file) == false) {
//			log.warn(ILLEGAL_PATH);
//			result.setFail(ILLEGAL_PATH);
//			return result;
//		}
//
//		if (!file.exists()) {
//			log.warn(FILE_NOT_FOUND);
//			result.setFail(FILE_NOT_FOUND);
//			return result;
//		}
//
//		if (file.isDirectory()) {
//			log.warn(IS_NOT_FILE);
//			result.setFail(IS_NOT_FILE);
//			return result;
//		}
//
//		if (StringUtils.isEmpty(filename)) {
//			log.debug("Requested filename is empty. use 'noname' instead");
//			filename = "noname";
//		}

		// 한글 언어표현을 위해 추가.
		/*
		if (StringUtil.containsKoreanLanguage(filename)) {
			try {
				filename = new String(filename.getBytes("EUC-KR"), "8859_1");
			}
			catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}
		*/

		// 다국어 언어표현을 위해 추가.
		try {
			if ( ieFlag.equals("1") ) {
				filename = URLEncoder.encode(filename, "UTF-8");
				// 공백  ->  "+"(URLEncoder)  ->  %20 순으로 변환
				filename = filename.replaceAll("\\+", "%20");
			}
			else {
				filename = new String(filename.getBytes("UTF-8"), "8859_1");
			}
		}
		catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}

		/* set header */
		// response.setContentType("application/octet-stream");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Length", "" + file.length());

		int length = 0;
		byte[] byteBuffer = new byte[1024];
		FileInputStream fileInputStream = null;
		DataInputStream inputStream = null;
		BufferedOutputStream outStream = null;

		try {
			fileInputStream = new FileInputStream(file);
			inputStream = new DataInputStream(fileInputStream);
			outStream = new BufferedOutputStream(response.getOutputStream(), 1024);
			while ((inputStream != null) && ((length = inputStream.read(byteBuffer)) != -1)) {
				outStream.write(byteBuffer, 0, length);
			}
			outStream.flush();
		} catch (IOException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage(), e);
			}
			result.setFail(DOWNLOAD_ERROR_MESSAGE);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (Exception e) {
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Exception e) {
				}
			}
		}

		return result;
	}

	/**
	 * 요청 받은 경로가 보안상 허가된 경로인지를 반환한다.
	 *
	 * @param file
	 * @return
	 */
	static boolean isAllowedPath(File file) {
		String cfg = "허용하지 않는 경로입니다.";
		log.debug("download-allow: {}", cfg);
		if (cfg == null) {
			return true;
		}

		try {
			String path = file.getCanonicalPath();
			log.debug("request file absolute path: {}", path);
			String[] allowPaths = cfg.split("\n");
			for (String allowPath : allowPaths) {
				allowPath = allowPath.trim();
				if ("".equals(allowPath)) {
					continue;
				}

				if (path.startsWith(allowPath)) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}
