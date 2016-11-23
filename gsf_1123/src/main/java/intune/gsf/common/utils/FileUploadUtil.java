package intune.gsf.common.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.model.FileInfo;
import intune.gsf.model.FileMeta;


public class FileUploadUtil {
//	@Value("#{config['upload.format']}")
//	private String fileFormatList;
//
//	@Value("#{config['upload.maxSize']}")
//	private long maxUploadSize;
//
//	@Value("#{config['system.language']}")
//	private String language;
//	
//	@Value("#{config['upload.width']}")
//	private String width;
//	
//	@Value("#{config['upload.height']}")
//	private String height;
//	
//	@Value("#{config['upload.sizeName']}")
//	private String sizeName;

//	@Value("#{config['upload.path']}")
//	private static String uploadPath = "c:\\ZTS\\upload";

	protected static final Log logger = LogFactory.getLog(FileUploadUtil.class);

	public static String SEPARATOR = "/";
	
	public static String TEMP_PATH = "temp";
	
	/**
	 * 업로드 이미지 파일명 생성
	 * 
	 * @Method Name : makeImageFileName
	 * @author : eddie
	 * @date : 2016. 5. 29.
	 * @description :
	 *
	 * @return
	 */
	public static String makeImageFileName(String task, String extension) {
		return task + "_" + Calendar.getInstance().getTimeInMillis() + "." + extension;
	}

	public static LinkedList<FileMeta> uploadFile(MultipartHttpServletRequest request, String uploadPath) throws Exception {
		LinkedList<FileMeta> fileList = new LinkedList<FileMeta>();
		FileMeta fileMeta = null;

		Iterator<String> it = request.getFileNames();
		MultipartFile mfile = null;

		try {
			while (it.hasNext()) {
				mfile = request.getFile(it.next());

				if (fileList.size() > 10) {
					fileList.pop();
				}

				String fileName = mfile.getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
					String lowCase = StringUtils.lowerCase(fileFormat);
					String formatList = Config.getString("upload.format");
					long maxSize = Config.getLong("upload.maxSize");
					long fileSize = mfile.getSize();

					if (!formatList.contains(lowCase)) {
						throw new ServiceException("ccs.common.file.not.support");
					}

					if (fileSize <= 0) {
						throw new ServiceException("ccs.common.file.size.zero");
					}

					if (fileSize > maxSize) {
						Long[] args = { maxSize };
						throw new ServiceException("ccs.common.file.not.found", args);
					}

					fileMeta = new FileMeta();
					fileMeta.setFileName(fileName);
					fileMeta.setFileSize(mfile.getSize() / 1024 + " kb");
					fileMeta.setFileType(mfile.getContentType());
					fileMeta.setBytes(mfile.getBytes());

					fileList.add(fileMeta);

					saveFile(mfile, uploadPath);

				}
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage());
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessageCd(), e.getArgs());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return fileList;
	}

	public static File saveFile(MultipartFile mfile, String savePath) throws IllegalStateException, IOException {
		String fileName = mfile.getOriginalFilename();
		savePath += FileUploadUtil.SEPARATOR + DateUtil.getCurrentDate(DateUtil.FORMAT_7);

		File dirPath = new File(savePath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		File file = new File(savePath + "/" + fileName);
		file.setReadable(true);
		if (!file.exists()) {
			mfile.transferTo(file);
		} else {
			mfile.transferTo(file);
			// throw new ServiceException("fileAlreadyExists");
		}
		return file;
	}
	
	/**
	 * 파일 경로, 파일명 으로 부터 확장자를 추출한다.
	 * @Method Name : getFileExtension
	 * @author :eddie
	 * @date : 2016. 6. 17.
	 * @description : 
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileExtension(String filePath) {
		String fileFormat = filePath.substring(filePath.lastIndexOf(".") + 1);
		String extension = StringUtils.lowerCase(fileFormat);
		return extension;
	}

	/**
	 * 파일 full path에서 dir 가져오기
	 * 
	 * @Method Name : getFileDir
	 * @author : eddie
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param filePath
	 * @return
	 */
	public static String getFileDir(String filePath) {
		String dir = filePath.substring(0, filePath.lastIndexOf(FileUploadUtil.SEPARATOR));
		return dir;
	}
	/**
	 * 파일 풀 경로에서 파일명 변경
	 * 
	 * @Method Name : changeFileName
	 * @author : eddie
	 * @date : 2016. 6. 17.
	 * @description :
	 *
	 * @param original
	 * @param fileNameToChange
	 * @return
	 */
	public static String changeFileName(String original, String fileNameToChange) {

		int fileNameIndex = original.lastIndexOf(FileUploadUtil.SEPARATOR);

		//System.out.println("fileNameIndex:::::::::::" + fileNameIndex);

		String dir = original.substring(0, fileNameIndex);

		String newPath = dir + FileUploadUtil.SEPARATOR + fileNameToChange + "." + FileUploadUtil.getFileExtension(original);

		return newPath;
	}
	

	/**
	 * 이미지 업로드 ( 다중건 처리 )
	 * 
	 * @Method Name : uploadImages
	 * @author : eddie
	 * @date : 2016. 5. 29.
	 * @description : 이미지를 업로드하고 fileInfo.reSize=true 이면 config에 설정된 정보로 리사이징 한다.
	 * @param request
	 * @param fileInfo systemName-시스템명, task-업무명, reSize-리사이즈 여부, isTempUpload-temp 경로에 upload여부
	 * @return
	 * @throws Exception
	 */
	public static LinkedList<FileMeta> uploadImages(MultipartHttpServletRequest request, FileInfo fileInfo)
			throws Exception {
		LinkedList<FileMeta> fileList = new LinkedList<FileMeta>();

		FileMeta fileMeta = null;

		Iterator<String> it = request.getFileNames();
		MultipartFile mfile = null;

		//referer 로 부터 system, biz 명을 추출
		parseReferer(fileInfo);

		try {
			while (it.hasNext()) {
				mfile = request.getFile(it.next());

				if (fileList.size() > 10) {
					fileList.pop();
				}

				String extension = FileUploadUtil.getFileExtension(mfile.getOriginalFilename());
				
				String formatList = Config.getString("upload.format");

				long maxmega = Config.getLong("upload.maxSize");
				long maxSize = maxmega * 1024l * 1024l;
				logger.info("## maxSize : " + maxSize);
				long fileSize = mfile.getSize();

				
				//logger.debug("@@fileInfo.getSystem()::" + fileInfo.getSystem());
				//logger.debug("@@extension::" + extension);
				
				// pms.product는 jpg만 허용함
				if ("pms.product".equals(fileInfo.getSystem() + "." + fileInfo.getBiz())) {
					String pmsFormats = Config.getString("upload.product.format");

					//logger.debug("pmsFormats::" + pmsFormats);

					if (!pmsFormats.contains(extension)) {
						String [] ext = new String[]{"jpg"};
						throw new ServiceException("file.image.pattern", ext);
					}
				}
				if (!formatList.contains(extension)) {
					throw new ServiceException("NoImageFile");
				}

				if (fileSize <= 0) {
					throw new ServiceException("fileSizeZero");
				}

				if (fileSize > maxSize) {
					Long[] args = { maxmega };
					throw new ServiceException("fileSizeExceed", args);
				}

				String timestmapPath = DateUtil.getCurrentDate(DateUtil.FORMAT_5) + FileUploadUtil.SEPARATOR
						+ DateUtil.getCurrentDate(DateUtil.FORMAT_6);
				


				String saveFilePathTemp = (fileInfo.isTempUpload() ? Config.getString("upload.tempimage.path")
						: Config.getString("upload.image.path")) + FileUploadUtil.SEPARATOR + fileInfo.getSystem()
						+ FileUploadUtil.SEPARATOR + fileInfo.getBiz() + FileUploadUtil.SEPARATOR + timestmapPath;


				String uploadFilePathTemp = CommonUtil.replace(saveFilePathTemp, Config.getString("upload.base.path"), "");

				//logger.debug("#saveFilePathTemp::" + saveFilePathTemp);
				//logger.debug("#uploadFilePathTemp::" + uploadFilePathTemp);

				File dirPath = new File(saveFilePathTemp);

				if (!dirPath.exists()) {
					dirPath.mkdirs();
				}

				// orginal file create
				String uploadFileName = fileInfo.getBiz() + "_" + Calendar.getInstance().getTimeInMillis();
				String fullPathName = saveFilePathTemp + FileUploadUtil.SEPARATOR + uploadFileName;

				//logger.debug("#uploadFileName::" + uploadFileName);
				//logger.debug("#fullPathName::" + fullPathName);

				String tempUploadPath = fullPathName + "." + extension;

				String dbSavePath = uploadFilePathTemp + FileUploadUtil.SEPARATOR + uploadFileName + "." + extension;

				//logger.debug("#tempUploadPath :: " + tempUploadPath);
				//logger.debug("#dbSavePath :: " + dbSavePath);
				
				File orginFile = new File(tempUploadPath);



				fileMeta = new FileMeta();
				fileMeta.setFileName(mfile.getOriginalFilename());
				fileMeta.setFileSize(mfile.getSize() / 1024 + " kb");
				fileMeta.setFileType(mfile.getContentType());
//				fileMeta.setBytes(mfile.getBytes());
				fileMeta.setUploadedFilePath(dbSavePath);
				fileList.add(fileMeta);

				mfile.transferTo(orginFile);

				try {
					BufferedImage bi = ImageIO.read(orginFile);
					fileMeta.setWidth(bi.getWidth());
					fileMeta.setHeight(bi.getHeight());
				} catch (IIOException e) {
//					JPG CMYK -> RGB로 변환

					//Find a suitable ImageReader
					Iterator readers = ImageIO.getImageReadersByFormatName("JPEG");
					ImageReader reader = null;
					while (readers.hasNext()) {
						reader = (ImageReader) readers.next();
						if (reader.canReadRaster()) {
							break;
						}
					}

					//Stream the image file (the original CMYK image)
					ImageInputStream input = ImageIO.createImageInputStream(orginFile);
					reader.setInput(input);

					//Read the image raster
					Raster raster = reader.readRaster(0, null);

					//Create a new RGB image
					BufferedImage bi = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

					//Fill the new image with the old raster
					bi.getRaster().setRect(raster);
					fileMeta.setWidth(bi.getWidth());
					fileMeta.setHeight(bi.getHeight());
				}

//
//				logger.debug("## width :::: " + bi.getWidth());
//				logger.debug("## height :::: " + bi.getHeight());


				//resize 가 true일때 property 정보로 resize한다.
				if (fileInfo.isResize()) {

					String[] imageSizes = Config.getString("upload.sizeName").split(",");
					String[] imageWidths = Config.getString("upload.width").split(",");
					String[] imageHeighs = Config.getString("upload.height").split(",");

					for (int i = 0; i < imageSizes.length; i++) {
						String resizeFile = fullPathName + "_" + imageSizes[i] + "." + extension;
						File file = new File(resizeFile);
						resize(orginFile, file, Integer.parseInt(imageWidths[i]), Integer.parseInt(imageHeighs[i]));
					}
				}

			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());

		} catch (ServiceException e) {
			throw new ServiceException(e.getMessageCd(), e.getArgs());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return fileList;

	}

	/**
	 * 바이너리를 임시경로에서 실제경로로 복사
	 * 
	 * @Method Name : copyTempToReal
	 * @author : eddie
	 * @date : 2016. 5. 30.
	 * @description : 이미지 경로가 /temp를 포함하면 /temp를 삭제한 경로로 이동 한다.<br/>
	 * @return realPath
	 */
	public static String moveTempToReal(String tempPath) {
		String realPath = tempPath;
		
		if (CommonUtil.isNotEmpty(tempPath) && tempPath.indexOf(FileUploadUtil.TEMP_PATH) > 0) {
			realPath = tempPath.replace(FileUploadUtil.SEPARATOR + TEMP_PATH, "");
			FileUploadUtil.moveFile(tempPath, realPath);
		}
		return realPath;
	}

	/**
	 * 파일 이동
	 * 
	 * @Method Name : moveFile
	 * @author : eddie
	 * @date : 2016. 6. 17.
	 * @description : source 에서 target 으로 파일을 이동한다.
	 *
	 * @param target
	 * @param source
	 */
	public static void moveFile(String source, String target) {

		source = Config.getString("upload.base.path") + source;
		target = Config.getString("upload.base.path") + target;

		//logger.debug("#source::" + source);
		//logger.debug("#target::" + target);
		String dir = FileUploadUtil.getFileDir(target);

		File dirPath = new File(dir);

		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		
		File file = new File(source);

		File fileToMove = new File(target);

		file.renameTo(fileToMove);

	}
	

	public static void deleteFile(String fullPath) {

		fullPath = Config.getString("upload.base.path") + fullPath;

		File file = new File(fullPath);
		if (!file.exists()) {
			throw new ServiceException("ccs.common.file.not.found");
		} else {
			file.delete();
		}
	}

	public static void deleteFile(String path, String fileName) {
		File file = new File(path + fileName);
		if (!file.exists()) {
			throw new ServiceException("ccs.common.file.not.found");
		} else {
			file.delete();
		}
	}

	/**
	 * 이미지 리사이징 처리
	 * @Method Name : resize
	 * @author : eddie
	 * @date : 2016. 6. 17.
	 * @description : 
	 *
	 * @param src
	 * @param dest
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public static void resize(File src, File dest, int width, Integer height) throws IOException {
		// File src = new File(srcStr);
		Image srcImg = null;
		String suffix = src.getName().substring(src.getName().lastIndexOf('.') + 1).toLowerCase();
		if (suffix.equals("bmp") || suffix.equals("png") || suffix.equals("gif")) {
			srcImg = ImageIO.read(src);
		} else {
			// BMP가 아닌 경우 ImageIcon을 활용해서 Image 생성
			// 이렇게 하는 이유는 getScaledInstance를 통해 구한 이미지를
			// PixelGrabber.grabPixels로 리사이즈 할때
			// 빠르게 처리하기 위함이다.
			srcImg = new ImageIcon(src.toURL()).getImage();
		}
		srcImg = ImageIO.read(src);
		Image imgTarget = srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		int pixels[] = new int[width * height];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, width, height, pixels, 0, width);
		try {
			pg.grabPixels(); // JEPG 포맷의 경우 오랜 시간이 걸린다.
		} catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
		BufferedImage destImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		destImg.setRGB(0, 0, width, height, pixels, 0, width);

		// File dest = new File(destStr);
//		logger.debug("======file====dest=====\n"+dest);
		ImageIO.write(destImg, "jpg", dest);

	}

	/**
	 * request의 referer 에서 system, biz 명을 추출한다.
	 * 
	 * @Method Name : parseReferer
	 * @author : Administrator
	 * @date : 2016. 5. 30.
	 * @description :
	 *
	 * @param info
	 */
	public static void parseReferer(FileInfo info) {
		String referer = info.getReferer();
//		String referer = "http://local.0to7.com:7001/dms/corner/popup/imageInsert";
		referer = referer.replace("http://", "").replace("https://", "");

		int fst = referer.indexOf("/");
		int sec = referer.indexOf("/", fst + 1);
		int thrd = referer.indexOf("/", sec + 1);

		String system = referer.substring(fst + 1, sec);
		String biz = referer.substring(sec + 1, thrd);
		//logger.debug("#system::" + system);
		//logger.debug("#biz::" + biz);
		info.setSystem(system);
		info.setBiz(biz);
	}

	/**
	 * ZIP 압축 업로드
	 * 
	 * @Method Name : uploadZip
	 * @author : roy
	 * @date : 2016. 11. 02.
	 * @description : ZIP 압축파일을 업로드하고 fileInfo.reSize=true 이면 config에 설정된 정보로 리사이징 한다.
	 * @param request
	 * @param fileInfo systemName-시스템명, task-업무명, reSize-리사이즈 여부, isTempUpload-temp 경로에 upload여부
	 * @return
	 * @throws Throwable
	 */
	public static List<FileInfo> uploadZip(MultipartHttpServletRequest request, FileInfo fileInfo) throws Throwable {
		LinkedList<FileMeta> fileList = new LinkedList<FileMeta>();

		List<FileInfo> list = new ArrayList<FileInfo>();
		
		FileMeta fileMeta = null;

		Iterator<String> it = request.getFileNames();
		MultipartFile mfile = null;

		//referer 로 부터 system, biz 명을 추출
		parseReferer(fileInfo);

		try {
			while (it.hasNext()) {

				mfile = request.getFile(it.next());

//				if (fileList.size() > 10) {
//					fileList.pop();
//				}

				String extension = FileUploadUtil.getFileExtension(mfile.getOriginalFilename());

				String formatList = "zip";
				long maxSize = Config.getLong("upload.maxSize");
				long fileSize = mfile.getSize();

				//logger.debug("@@fileInfo.getSystem()::" + fileInfo.getSystem());
				//logger.debug("@@extension::" + extension);

//				zip 파일만 허용
				if (!formatList.contains(extension)) {
					String[] ext = new String[] { "zip" };
					throw new ServiceException("file.image.pattern", ext);
				}
//				if (fileSize <= 0) {
//					throw new ServiceException("fileSizeZero");
//				}
//
//				if (fileSize > maxSize) {
//					Long[] args = { maxSize };
//					throw new ServiceException("fileSizeExceed", args);
//				}

				String timestmapPath = DateUtil.getCurrentDate(DateUtil.FORMAT_5) + FileUploadUtil.SEPARATOR
						+ DateUtil.getCurrentDate(DateUtil.FORMAT_6);

				String saveFilePathTemp = (fileInfo.isTempUpload() ? Config.getString("upload.tempimage.path")
						: Config.getString("upload.image.path")) + FileUploadUtil.SEPARATOR + fileInfo.getSystem()
						+ FileUploadUtil.SEPARATOR + fileInfo.getBiz() + FileUploadUtil.SEPARATOR  + timestmapPath;

				String uploadFilePathTemp = Config.getString("upload.base.path");

				//logger.debug("#saveFilePathTemp::" + saveFilePathTemp);
				//logger.debug("#uploadFilePathTemp::" + uploadFilePathTemp);

				File dirPath = new File(saveFilePathTemp);

				if (!dirPath.exists()) {
					dirPath.mkdirs();
				}

				// orginal file create
				String uploadFileName = fileInfo.getBiz() + "_" + Calendar.getInstance().getTimeInMillis();
//				String fullPathName = saveFilePathTemp + FileUploadUtil.SEPARATOR + uploadFileName;
				String fullPathName = saveFilePathTemp;

				logger.debug("#uploadFileName::" + uploadFileName);
				logger.debug("#fullPathName::" + fullPathName);

				String tempUploadPath = fullPathName + "." + extension;

				String dbSavePath = uploadFilePathTemp + FileUploadUtil.SEPARATOR + uploadFileName + "." + extension;

				String realFullPathName = CommonUtil.replace(fullPathName, Config.getString("upload.base.path"), "");

				logger.debug("#tempUploadPath :: " + tempUploadPath);
				logger.debug("#dbSavePath :: " + dbSavePath);

				File orginFile = new File(tempUploadPath);

				fileMeta = new FileMeta();
				fileMeta.setFileName(mfile.getOriginalFilename());
				fileMeta.setFileSize(mfile.getSize() / 1024 + " kb");
				fileMeta.setFileType(mfile.getContentType());
				fileMeta.setUploadedFilePath(dbSavePath);

				fileList.add(fileMeta);

				mfile.transferTo(orginFile);

//				파일 지우기 추가 반영 TODO
				list = decompress(tempUploadPath, realFullPathName, fileInfo);

			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());

		} catch (ServiceException e) {
			throw new ServiceException(e.getMessageCd(), e.getArgs());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return list;

	}

	/**
	 * 압축풀기 메소드
	 * 
	 * @param zipFileName 압축파일
	 * @param directory 압축 풀 폴더
	 */
	public static List<FileInfo> decompress(String zipFileName, String directory, FileInfo info) throws Throwable {
		List<FileInfo> list = new ArrayList<FileInfo>();

		File zipFile = new File(zipFileName);
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zipentry = null;
		String realDirectory = Config.getString("upload.base.path") + directory;
		try {
			//파일 스트림
			fis = new FileInputStream(zipFile);
			//Zip 파일 스트림
			zis = new ZipInputStream(fis);
			//entry가 없을때까지 뽑기
			while ((zipentry = zis.getNextEntry()) != null) {
				String filename = zipentry.getName();
				File file = new File(realDirectory, filename);
				//entiry가 폴더면 폴더 생성
				if (zipentry.isDirectory()) {
//					file.mkdirs();
				} else {
					//파일이면 파일 만들기
					FileInfo tempFile = new FileInfo();
					tempFile.setFileName(file.getName());

					createFile(file, zis);
					String uploadFileName = info.getBiz() + "_" + Calendar.getInstance().getTimeInMillis();
					String extension = FileUploadUtil.getFileExtension(file.getName());

					String realSavePath = directory + FileUploadUtil.SEPARATOR + uploadFileName + "." + extension;
					File fileToMove = new File(Config.getString("upload.base.path") + realSavePath);

					file.renameTo(fileToMove);

					tempFile.setTempFullPath(realSavePath);
					list.add(tempFile);
				}
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			if (zis != null)
				zis.close();
			if (fis != null)
				fis.close();
		}

		return list;
	}

	/**
	 * 파일 만들기 메소드
	 * 
	 * @param file 파일
	 * @param zis Zip스트림
	 */
	private static void createFile(File file, ZipInputStream zis) throws Throwable {
		//디렉토리 확인
		File parentDir = new File(file.getParent());
		//디렉토리가 없으면 생성하자
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		//파일 스트림 선언
		try (FileOutputStream fos = new FileOutputStream(file)) {
			byte[] buffer = new byte[256];
			int size = 0;
			//Zip스트림으로부터 byte뽑아내기
			while ((size = zis.read(buffer)) > 0) {
				//byte로 파일 만들기
				fos.write(buffer, 0, size);
			}
		} catch (Throwable e) {
			throw e;
		}
	}
}
