package cn.gov.zunyi.video.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * Provides a SHA-256 based implementation of the password encryption
 * functionality.
 * 
 * @author James Muehlner
 */
public class SHA256PasswordEncryptionService {

	public static byte[] createPasswordHash(String password, byte[] salt) {

		try {

			// Build salted password, if a salt was provided
			StringBuilder builder = new StringBuilder();
			builder.append(password);

			if (salt != null)
				builder.append(DatatypeConverter.printHexBinary(salt));

			// Hash UTF-8 bytes of possibly-salted password
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(builder.toString().getBytes("UTF-8"));
			return md.digest();

		}

		// Throw hard errors if standard pieces of Java are missing
		catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("Unexpected lack of UTF-8 support.", e);
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException("Unexpected lack of SHA-256 support.", e);
		}

	}

}
