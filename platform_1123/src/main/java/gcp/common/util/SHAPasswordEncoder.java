package gcp.common.util;

import java.security.NoSuchAlgorithmException;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class SHAPasswordEncoder extends MessageDigestPasswordEncoder {
	private Object saltValue = "0to7";

	public SHAPasswordEncoder() {
		this(256);
	}

	public SHAPasswordEncoder(int sha) {
		super("SHA-" + sha);
	}

	public void setSalt(Object salt) {
		this.saltValue = salt;
	}

	@Override
	public String encodePassword(String data, Object salt) {
		try {
			
			byte[] bSalt = null;
			if( salt != null ) {
				bSalt = ((String) salt).getBytes();
			} else {
				bSalt = ((String) saltValue).getBytes();
			}
			
			byte[] encrypt = MessageDigestEx.encrypt(data.getBytes(), bSalt, "SHA-256");
			
			StringBuffer buff = new StringBuffer();
			for( int i = 0; i < encrypt.length; i++ ) {
				String hex = Integer.toHexString( encrypt[i] & 0xFF ).toUpperCase();
				if( hex.length() == 1 ) {
					buff.append("0");
				}
				buff.append(hex);
			}
			return buff.toString();
			
		} catch(NoSuchAlgorithmException e) {
			// Never
			return null;
		}
	}

//	public static void main(String[] args) {
//		Object salt = "0to7";
//		SHAPasswordEncoder encode = new SHAPasswordEncoder(256);
//		System.out.println(encode.encodePassword("1111", salt));
//	}
}
