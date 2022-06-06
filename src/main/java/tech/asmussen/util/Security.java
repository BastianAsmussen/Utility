package tech.asmussen.util;

import com.lambdaworks.crypto.SCryptUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import lombok.Data;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * A utility class for all security systems on used by Asmussen Technology.
 * @author Bastian Almar Wolsgaard Asmussen (BastianA)
 * @author Casper Agerskov Madsen (consoleBeep)
 * @version 1.0.6
 * @since 1.0.0
 * @see #MAX_PASSWORD_LENGTH
 * @see #MIN_PASSWORD_LENGTH
 * @see #generateKeyPair()
 * @see #generateKeyPair(int, String)
 * @see #generateCipher(PublicKey)
 * @see #generateCipher(PublicKey, String)
 * @see #encryptMessage(Cipher, byte[])
 * @see #encryptMessage(Cipher, String)
 * @see #decryptMessage(PrivateKey, byte[])
 * @see #decryptMessage(PrivateKey, String)
 * @see #generateHash(String)
 * @see #generateHash(String, int, int, int)
 * @see #compareHash(String, String)
 * @see #generateRandomPassword()
 * @see #generatePassword(int, boolean, boolean, boolean, boolean)
 * @see #hasInternet()
 * @see #hasInternet(URL)
 * @see #generate2FA()
 * @see #validate2FA(String, String)
 * @see #validate2FA(String, int)
 * @see #validatePassword(String)
 * @see #validateEmail(String)
 * @see #validateCreditCard(String)
 */
@Data
public final class Security {
	
	/**
	 * The maximum length of a password.
	 *
	 * @since 1.0.2
	 */
	public static final int MAX_PASSWORD_LENGTH = 128;
	
	/**
	 * The minimum length of a password.
	 *
	 * @since 1.0.2
	 */
	public static final int MIN_PASSWORD_LENGTH = 8;
	
	/**
	 * Generate a keypair of size 2,048 and return it.
	 *
	 * @return A keypair of size 2,048 to be used along with other methods.
	 * @throws NoSuchAlgorithmException If the algorithm is not supported throw this exception.
	 * @see #generateKeyPair(int, String)
	 * @since 1.0.0
	 */
	public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		
		return generateKeyPair(2_048, "RSA");
	}
	
	/**
	 * Generate a keypair of size n and return it.
	 *
	 * @param keySize   Size of keypair, note: the bigger the key size the longer the generation time.
	 * @param algorithm The algorithm to use for the keypair generation.
	 * @return A keypair of size n to be used along with other methods.
	 * @throws NoSuchAlgorithmException If the algorithm is not supported throw this exception.
	 * @since 1.0.0
	 */
	public KeyPair generateKeyPair(int keySize, String algorithm) throws NoSuchAlgorithmException {
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
		
		keyPairGenerator.initialize(keySize);
		
		return keyPairGenerator.generateKeyPair();
	}
	
	/**
	 * Generate a cipher from the given public key and return it as Cipher.
	 *
	 * @param publicKey Used for cipher generation.
	 * @return A cipher used for encryption of a message.
	 * @throws NoSuchPaddingException   If the cipher is not supported throw this exception.
	 * @throws NoSuchAlgorithmException If the cipher is not supported throw this exception.
	 * @throws InvalidKeyException      If the given public key is invalid throw this exception.
	 * @see #generateCipher(PublicKey, String)
	 * @since 1.0.0
	 */
	public Cipher generateCipher(PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		
		return generateCipher(publicKey, "RSA/ECB/PKCS1Padding");
	}
	
	/**
	 * Generate a cipher from the given public key and return it as Cipher.
	 *
	 * @param publicKey Used for cipher generation.
	 * @param algorithm The algorithm used for cipher generation.
	 * @return A cipher used for encryption of a message.
	 * @throws NoSuchPaddingException   If the cipher is not supported throw this exception.
	 * @throws NoSuchAlgorithmException If the cipher is not supported throw this exception.
	 * @throws InvalidKeyException      If the given public key is invalid throw this exception.
	 * @since 1.0.0
	 */
	public Cipher generateCipher(PublicKey publicKey, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		return cipher;
	}
	
	/**
	 * Encrypt the given input with the given cipher.
	 *
	 * @param cipher The cipher for encrypting the message.
	 * @param input  The input used to encrypt as a String.
	 * @return The encrypted input as a byte array.
	 * @throws IllegalBlockSizeException If the input is too large throw this exception.
	 * @see #encryptMessage(Cipher, byte[])
	 * @since 1.0.0
	 */
	public byte[] encryptMessage(Cipher cipher, String input) throws IllegalBlockSizeException, BadPaddingException {
		
		return encryptMessage(cipher, input.getBytes());
	}
	
	/**
	 * Encrypt the given input with the given cipher.
	 *
	 * @param cipher The cipher for encrypting the message.
	 * @param input  The input used to encrypt as a byte array.
	 * @return The encrypted input as a byte array.
	 * @throws IllegalBlockSizeException If the input is too large throw this exception.
	 * @throws BadPaddingException       If the padding is invalid throw this exception.
	 * @since 1.0.0
	 */
	public byte[] encryptMessage(Cipher cipher, byte[] input) throws IllegalBlockSizeException, BadPaddingException {
		
		cipher.update(input);
		
		return cipher.doFinal();
	}
	
	/**
	 * Decrypt the given input with the given private key.
	 *
	 * @param privateKey Private key used for decryption of message.
	 * @param input      Input message as a byte array.
	 * @return The decrypted message as a byte array.
	 * @throws NoSuchAlgorithmException  If the algorithm is not supported throw this exception.
	 * @throws NoSuchPaddingException    If the padding is not supported throw this exception.
	 * @throws InvalidKeyException       If the key is invalid throw this exception.
	 * @throws IllegalBlockSizeException If the block size is invalid throw this exception.
	 * @throws BadPaddingException       If the padding is invalid throw this exception.
	 * @see #decryptMessage(PrivateKey, byte[])
	 * @since 1.0.0
	 */
	public byte[] decryptMessage(PrivateKey privateKey, String input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		return decryptMessage(privateKey, input.getBytes());
	}
	
	/**
	 * Decrypt the given input with the given private key.
	 *
	 * @param privateKey Private key used for decryption of message.
	 * @param input      Input message as a byte array.
	 * @return The decrypted message as a byte array.
	 * @throws NoSuchAlgorithmException  If the algorithm is not supported throw this exception.
	 * @throws NoSuchPaddingException    If the padding scheme is not supported throw this exception.
	 * @throws InvalidKeyException       If the key is invalid throw this exception.
	 * @throws IllegalBlockSizeException If the size of the input is not a multiple of the block size throw this exception.
	 * @throws BadPaddingException       If the padding bytes are incorrect throw this exception.
	 * @since 1.0.0
	 */
	public byte[] decryptMessage(PrivateKey privateKey, byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		return cipher.doFinal(input);
	}
	
	/**
	 * Generate a hashed password from plain text and return it.
	 *
	 * @param str Plain text token / password.
	 * @return Returns the hashed password from the plain text password.
	 * @see #generateHash(String, int, int, int)
	 * @since 1.0.0
	 */
	public String generateHash(String str) {
		
		return generateHash(str, 16, 16, 16);
	}
	
	/**
	 * Generate a hashed password from plain text and return it.
	 *
	 * @param str Plain text token / password.
	 * @param n   CPU cost parameter.
	 * @param r   Memory cost parameter.
	 * @param p   Parallelization parameter.
	 * @return Returns the hashed password from the plain text password.
	 * @since 1.0.0
	 */
	public String generateHash(String str, int n, int r, int p) {
		
		return SCryptUtil.scrypt(str, n, r, p);
	}
	
	/**
	 * Compare the given string against the given hash and return the match as boolean.
	 *
	 * @param str  The plain text token / password from the user.
	 * @param hash The hash from the database.
	 * @return True if the string matches the hash otherwise false.
	 * @since 1.0.0
	 */
	public boolean compareHash(String str, String hash) {
		
		return SCryptUtil.check(str, hash);
	}
	
	/**
	 * Generate a 2FA secret key and return it.
	 *
	 * @return The generated secret key.
	 * @since 1.0.0
	 */
	public String generate2FA() {
		
		return new GoogleAuthenticator().createCredentials().getKey();
	}
	
	/**
	 * Validate the code with the secret key and return the match as boolean.
	 *
	 * @param secretKey The secret key used for generating the code.
	 * @param code      The 6-digit code used for validation.
	 * @return True if the code matches the secret key otherwise false.
	 * @see #validate2FA(String, int)
	 * @since 1.0.0
	 */
	public boolean validate2FA(String secretKey, String code) {
		
		if (!code.matches("\\d{6}$")) return false;
		
		return validate2FA(secretKey, Integer.parseInt(code));
	}
	
	/**
	 * Validate the code with the secret key and return the match as boolean.
	 *
	 * @param secretKey The secret key used for generating the code.
	 * @param code      The 6-digit code used for validation.
	 * @return True if the code matches the secret key otherwise false.
	 * @since 1.0.0
	 */
	public boolean validate2FA(String secretKey, int code) {
		
		return new GoogleAuthenticator().authorize(secretKey, code);
	}
	
	/**
	 * Generate a password of a random length with random parameters.
	 *
	 * @return The randomly generated password.
	 * @see #generatePassword(int, boolean, boolean, boolean, boolean)
	 * @since 1.0.0
	 */
	public String generateRandomPassword() {
		
		SecureRandom random = new SecureRandom();
		
		int length = random.nextInt(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
		
		boolean useLowerCaseLetters = random.nextBoolean();
		boolean useUpperCaseLetters = random.nextBoolean();
		boolean useDigits = random.nextBoolean();
		boolean useSymbols = random.nextBoolean();
		
		return generatePassword(length, useLowerCaseLetters, useUpperCaseLetters, useDigits, useSymbols);
	}
	
	/**
	 * Generate a password of length n.
	 *
	 * @param length              How long should the password be?
	 * @param useLowerCaseLetters Should the password contain lowercase letters?
	 * @param useUpperCaseLetters Should the password contain uppercase letters?
	 * @param useDigits           Should the password contain digits?
	 * @param useSymbols          Should the password contain symbols
	 * @return The generated password.
	 * @since 1.0.0
	 */
	public String generatePassword(int length, boolean useLowerCaseLetters, boolean useUpperCaseLetters, boolean useDigits, boolean useSymbols) {
		
		StringBuilder password = new StringBuilder();
		String characters = "";
		
		if (!(length >= MIN_PASSWORD_LENGTH) || !(length <= MAX_PASSWORD_LENGTH) || !useLowerCaseLetters && !useUpperCaseLetters && !useDigits && !useSymbols)
			
			return generateRandomPassword();
		
		if (useLowerCaseLetters) characters += "abcdefghijklmnopqrstuvwxyz";
		if (useUpperCaseLetters) characters += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (useDigits) characters += "0123456789";
		if (useSymbols) characters += "@$!%*?&";
		
		for (int i = 0; i < length; i++)
			
			password.append(characters.charAt(new SecureRandom().nextInt(characters.length())));
		
		return password.toString();
	}
	
	/**
	 * Checks if the application has a connection to the internet.
	 *
	 * @return True if the application has a connection to the internet otherwise it returns false.
	 * @throws IOException If an error occurs while checking the connection throw this exception.
	 * @see #hasInternet(URL)
	 * @since 1.0.0
	 */
	public boolean hasInternet() throws IOException {
		
		return hasInternet(new URL("https://www.google.com"));
	}
	
	/**
	 * Checks if the application has a connection to the internet.
	 *
	 * @param url The url to check the connection against.
	 * @return True if the application has a connection to the internet otherwise it returns false.
	 * @throws IOException              If an error occurs while checking the connection throw this exception.
	 * @throws IllegalArgumentException If the url parameter is null throw this exception.
	 * @since 1.0.0
	 */
	public boolean hasInternet(URL url) throws IOException {
		
		if (url == null)
			
			throw new IllegalArgumentException("The URL cannot be null.");
		
		final URLConnection CONNECTION = url.openConnection();
		
		CONNECTION.connect();
		CONNECTION.getInputStream().close();
		
		return true;
	}
	
	/**
	 * Match the plain text password against a regular expression and return the result.
	 * <p>
	 * Password must contain one lowercase letter. (a-z)
	 * Password must contain one uppercase letter. (A-Z)
	 * Password must contain one digit. (0-9)
	 * Password must contain a symbol. (@, $, !, %, *, ?, &)
	 * Password must be between {@code MIN_LENGTH_PASSWORD} and {@code MAX_PASSWORD_LENGTH} characters.
	 *
	 * @param plainTextPassword The password of the user.
	 * @return True if it is a valid password otherwise it returns false.
	 * @see #MAX_PASSWORD_LENGTH
	 * @see #MIN_PASSWORD_LENGTH
	 * @since 1.0.0
	 */
	public boolean validatePassword(String plainTextPassword) {
		
		return plainTextPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{" + MIN_PASSWORD_LENGTH + "," + MAX_PASSWORD_LENGTH + "}$");
	}
	
	/**
	 * Match the given email against a regular expression and return the result.
	 * Email must contain only one @.
	 * Email cannot start with the @ sign.
	 * Email must end with an address such as '.com'
	 *
	 * @param email The email of the user.
	 * @return True if it is a valid email otherwise it returns false.
	 * @since 1.0.0
	 */
	public boolean validateEmail(String email) {
		
		return email.matches("^[a-zA-Z\\d_+&*-]+(?:\\.[a-zA-Z\\d_+&*-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,7}$");
	}
	
	/**
	 * Validate a given credit card number using the Luhn algorithm.
	 * Input must be an integer.
	 * Input must have a remainder of 0 after sum is divided by 10.
	 * Input must be between 13 and 16 digits.
	 *
	 * @param creditCard The credit card number to validate.
	 * @return True if it is a valid credit card number otherwise it returns false.
	 * @since 1.0.0
	 */
	public boolean validateCreditCard(String creditCard) {
		
		final boolean[] flag = {(creditCard.length() & 1) == 1};
		
		return Arrays.stream(creditCard.split(""))
				.map(Integer::parseInt)
				.mapToInt(value -> value)
				.map(i -> ((flag[0] ^= true) ? (i * 2 - 1) % 9 + 1 : i))
				.sum() % 10 == 0;
	}
}
