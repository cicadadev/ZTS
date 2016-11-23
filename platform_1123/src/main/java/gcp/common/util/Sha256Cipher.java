package gcp.common.util;

import java.security.NoSuchAlgorithmException;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * SHA-256 algorithm to encrypt or decrypt the data is the class that provides the ability to.
 * @author devhome.tistory.com
 *
 */
public class Sha256Cipher extends ShaPasswordEncoder {

	/**
	 * SHA-256 algorithm to encrypt the data.
	 * @param data Target Data
	 * @param salt Masterkey
	 * @return Encrypted data
	 */
	public static String encrypt(String data) {
		try {
			String salt = "0to7";
			byte[] bSalt = null;
			if( salt != null ) {
				bSalt = salt.getBytes();
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
	/*
	public static void main(String args[]) {
		
		System.out.println( Sha256Cipher.encrypt("1234", null) );
		System.out.println( Sha256Cipher.encrypt("1234", "12345678") );
	}*/
}
