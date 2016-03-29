package com.tibco.tct.fom.common;

import com.tibco.customwizard.config.IPasswordObfuscator;

public class PasswordObfuscator implements IPasswordObfuscator {

	public String encrypt(String value) throws Exception {
		if (value != null) {
			value = new String(EncrypterDecryptorUtil.getCipher().encrypt(value));
		}
		return value;
	}

	public String decrypt(String value) throws Exception {
		if (value != null) {
			value = new String(EncrypterDecryptorUtil.getCipher().decrypt(value));
		}
		return value;
	}
}
