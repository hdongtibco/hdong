package com.tibco.tct.fom.common;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class EncrypterDecryptorUtil {

	public static String CIPHER_ALGORITHM = "PBEWithMD5AndDES";
	private Cipher ecipher;
	private Cipher dcipher;
	private static EncrypterDecryptorUtil cipher;
	private static final String ENC = "ENC";
	private byte[] salt = { -88, -97, -51, 49, 86, 53, -29, 3 };
	private int iterationCount = 23;

	public static EncrypterDecryptorUtil getCipher() throws Exception {
		if (cipher == null)
			cipher = new EncrypterDecryptorUtil("euc!1d");
		return cipher;
	}

	private EncrypterDecryptorUtil(String passPhrase) throws Exception {

		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), this.salt,
				this.iterationCount);
		SecretKey key = SecretKeyFactory.getInstance(CIPHER_ALGORITHM)
				.generateSecret(keySpec);
		this.ecipher = Cipher.getInstance(CIPHER_ALGORITHM);
		this.dcipher = Cipher.getInstance(CIPHER_ALGORITHM);
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(this.salt,
				this.iterationCount);
		this.ecipher.init(1, key, paramSpec);
		this.dcipher.init(2, key, paramSpec);
	}

	public String encrypt(String str) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
			String tem = decrypt(str);
			if (!tem.equals(str))
				return str;
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = this.ecipher.doFinal(utf8);
			String encryptedData = new String(Base64.encodeBase64(enc), "UTF-8");
			return "ENC(" + encryptedData + ")";
	}

	public String decrypt(String str) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		if ((str == null) || (str.trim().equals("")))
			return str;
		if (!(str.startsWith("ENC(")))
			return str;
		str = str.substring("ENC(".length(), str.length() - 1);
		byte[] dec = Base64.decodeBase64(str.getBytes());
		byte[] utf8 = this.dcipher.doFinal(dec);
		return new String(utf8, "UTF8");
	}

	public String getAlgorithm() {
		return this.ecipher.getAlgorithm();
	}

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a string");
		String text = in.nextLine();
		System.out.println("Encryption(1)/Decryption(2): ");
		int a = in.nextInt();
		if (a == 1)
			System.out.println("Encrypted: " + getCipher().encrypt(text));
		else
			System.out.println("Decrypted: " + getCipher().decrypt(text));
	}
}
