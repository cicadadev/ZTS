package intune.gsf.model;

/**
 * Result class.<br/>
 * 성공/실패 여부, 결과 코드 및 메시지를 가지는 클래스.
 * 
 */
public class Result {

	private boolean success;
	
	private String code;
	
	private String message;
	
	public Result() {
		success = true;
		message = null;
	}
	
	public Result(boolean success, String code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess() {
		success = true;
	}
	
	public void setSuccess(String message) {
		setSuccess();
		this.message = message;
	}
	
	public void setSuccess(String code, String message) {
		setSuccess(message);
		this.code = code;
	}
	
	public void setFail() {
		success = false;
	}
	
    public void setFail(String message) {
        setFail();
        this.message = message;
    }
    
	public void setFail(String code, String message) {
		setFail(message);
		this.code = code;
	}
	
	public String getCode() {
	    return code;
	}

	public String getMessage() {
		return message;
	}
}
