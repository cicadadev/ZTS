package intune.gsf.model;

import java.io.File;

public class FileInfo extends BaseEntity{

    /** The path. */
    private String dir;

    /** The original file name. */
    private String fileName;

    private String organizedFileName;

    private String fullPath;
    private String tempFullPath;
    private Long fileSize;

    private File file;
    
    private String system;
    private String biz;
    
    private String referer;
    
    private boolean isSuccess = true;
    
    private boolean isResize = false;
    
    private boolean isTempUpload = true;
    
    
    
	public boolean isTempUpload() {
		return isTempUpload;
	}
	public void setTempUpload(boolean isTempUpload) {
		this.isTempUpload = isTempUpload;
	}
	public boolean isResize() {
		return isResize;
	}
	public void setResize(boolean isResize) {
		this.isResize = isResize;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	
	
	public String getTempFullPath() {
		return tempFullPath;
	}
	public void setTempFullPath(String tempFullPath) {
		this.tempFullPath = tempFullPath;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getBiz() {
		return biz;
	}
	public void setBiz(String biz) {
		this.biz = biz;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOrganizedFileName() {
		return organizedFileName;
	}
	public void setOrganizedFileName(String organizedFileName) {
		this.organizedFileName = organizedFileName;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}
