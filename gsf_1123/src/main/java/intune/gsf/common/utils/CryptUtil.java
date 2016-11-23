package intune.gsf.common.utils;

import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

/**
 * CryptUtil Class
 * <pre>
 * 선택한 암호화 알고리즘에 따라서 문자열을 암/복호화 처리한다
 * 
 * 사용예)
 * 암호화 : CryptUtil.getInstance(ALGORITHM_AES).Encrypt(암호화할 문자열)
 * 복호화 : CryptUtil.getInstance(ALGORITHM_AES).Decrypt(복호화할 문자열)
 * </pre>
 * @author ditto
 *
 */
public class CryptUtil {
	
	/**
	 * AES 128bit
	 */
	private static final String ALGORITHM_AES = "AES";
	
	/**
	 * DES 64bit
	 */
	private static final String ALGORITHM_DES = "DES";
	
	/**
	 * DESede 192bit
	 */
	private static final String ALGORITHM_DESede = "DESede"; 
	
	private final static String secretKey = "53aebe359ee8efb196e34b0aa0e162bd";
	private final static String secretKey_des = "15524c73d6b361a7";
	private final static String secretKey_desede = "196891970dfe0dd99b38589da8152326ab9db99b9b318694";
	
	private static volatile CryptUtil INSTANCE;
	private static Key key = null;
	private static String encMode = "";
	
	/**
	 * Crypt instance for sigleton
	 * @param algorithm 암호화 모드
	 * @return
	 */
	public static CryptUtil getInstance(String algorithm) {
		if(INSTANCE == null) {
			synchronized(CryptUtil.class) {
				if(INSTANCE == null) {
					INSTANCE = new CryptUtil(algorithm);
				}
			}
		}
		
		return INSTANCE;
	}
	
	/**
	 * CryptUtil 생성자
	 * @param algorithm 암호화 모드
	 */
	private CryptUtil(String algorithm) {
		if(ALGORITHM_DES.equals(algorithm)) {
			key = generateKey(algorithm, ByteUtil.toBytes(secretKey_des, 16));
		} else if(ALGORITHM_DESede.equals(algorithm)) {
			key = generateKey(algorithm, ByteUtil.toBytes(secretKey_desede, 16));
		} else if(ALGORITHM_AES.equals(algorithm)) {
			key = generateKey(algorithm, ByteUtil.toBytes(secretKey, 16));
		}
		
		encMode = algorithm;
	}
	
	/**
	 * Key Generate
	 * @param algorithm 암호화 모드
	 * @return Secrect key
	 */
	public static Key generateKey(String algorithm) {
		SecretKey key = null;
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
			key = keyGen.generateKey();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return key;
	}
	
	/**
	 * Key Generate with keyData
	 * @param algorithm 암호화 모드
	 * @param keyData key data
	 * @return
	 */
	public static Key generateKey(String algorithm, byte[] keyData) {
		String upper = StringUtils.upperCase(algorithm);
		SecretKey secretKey = null;
		KeySpec keySpec = null;
		
		try {
			if("DES".equals(upper)) {
				keySpec = new DESKeySpec(keyData);
			} else if("DESede".equals(upper) || "TripleDES".equals(upper)) {
				keySpec = new DESedeKeySpec(keyData);
			} else {
				SecretKeySpec skeySpec = new SecretKeySpec(keyData, algorithm);
				return skeySpec;
			}
			
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			secretKey = secretKeyFactory.generateSecret(keySpec);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return secretKey;
	}
	
	/**
	 * 암호화
	 * @param str 암호화할 문자열
	 * @return 암호화된 문자열
	 */
	public static String Encrypt(String str) {
		String enStr = null;
		
		try {
			String transformation = null;
			if(ALGORITHM_DES.equals(encMode)) {
				transformation = "DES/ECB/NoPadding";
			} else if(ALGORITHM_DESede.equals(encMode)) {
				transformation = "DESede/ECB/PKCS5Padding";
			} else if(ALGORITHM_AES.equals(encMode)) {
				transformation = "AES/ECB/PKCS5Padding";
			}
			
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			byte[] plain = str.getBytes();
			byte[] encrypt = cipher.doFinal(plain);
			enStr = ByteUtil.toHexString(encrypt);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return enStr;
	}
	
	/**
	 * 복호화
	 * @param str 복호화할 문자
	 * @return 복호화된 문자열
	 */
	public static String Decrypt(String str) {
		String deStr = null;
		
		try {
			String transformation = null;
			if(ALGORITHM_DES.equals(encMode)) {
				transformation = "DES/ECB/NoPadding";
			} else if(ALGORITHM_DESede.equals(encMode)) {
				transformation = "DESede/ECB/PKCS5Padding";
			} else if(ALGORITHM_AES.equals(encMode)) {
				transformation = "AES/ECB/PKCS5Padding";
			}
			
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			byte[] decrypt = cipher.doFinal(ByteUtil.toBytes(str, 16));
			deStr = new String(decrypt, "UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return deStr;
	}
	
}
