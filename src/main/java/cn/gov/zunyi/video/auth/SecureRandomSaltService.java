package cn.gov.zunyi.video.auth;

import java.security.SecureRandom;

public class SecureRandomSaltService {

	/**
	 * Instance of SecureRandom for generating the salt.
	 */
	private static SecureRandom secureRandom = new SecureRandom();

	public static byte[] generateSalt() {
		byte[] salt = new byte[32];
		secureRandom.nextBytes(salt);
		return salt;
	}

}
