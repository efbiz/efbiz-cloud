/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.efbiz.oauth.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of PasswordEncoder using message digest. Can accept any
 * message digest that the JDK can accept, including MD5 and SHA1. Returns the
 * equivalent Hash you would get from a Perl digest.
 * 
 * @author Scott Battaglia
 * @author Stephen More
 * @version $Revision$ $Date$
 * @since 3.1
 */
@Component
public final class PasswordEncoder {

	public static final Charset UTF8 = Charset.forName("UTF-8");
	
	private final String encodingAlgorithm = "SHA";
	
	public String encode(String password, String daPassword){
		if (daPassword.startsWith("{")) {
            return doCompareTypePrefix(encodingAlgorithm, password);
        } else if (daPassword.startsWith("$")) {
            return doComparePosix(password, daPassword);
        }else{
        	return doCompareBare(encodingAlgorithm, password);
        }
	}
	
	private String doComparePosix(String password,String daPassword){
		int typeEnd = daPassword.indexOf("$", 1);
		int saltEnd = daPassword.indexOf("$", typeEnd + 1);
		String salt = daPassword.substring(typeEnd + 1, saltEnd);
		return cryptUTF8(encodingAlgorithm,salt,password);
	}
	
	private String doCompareTypePrefix(String encodingAlgorithm,String password){
		return digestHash64UTF8(encodingAlgorithm,password);
	}
	
	private String doCompareBare(String encodingAlgorithm,String password){
		return encodeBase64URLSafeStringUTF8(encodingAlgorithm,password);
	}

    private static String getCryptedBytes(String hashType, String salt, byte[] bytes) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance(hashType);
            messagedigest.update(salt.getBytes(UTF8));
            messagedigest.update(bytes);
            return Base64.encodeBase64URLSafeString(messagedigest.digest()).replace('+', '.');
        } catch (NoSuchAlgorithmException e) {
        	return "";
        }
    }
    
    private static String cryptBytes(String hashType, String salt, byte[] bytes) {
        if (hashType == null) {
            hashType = "SHA";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$").append(hashType).append("$").append(salt).append("$");
        sb.append(getCryptedBytes(hashType, salt, bytes));
        return sb.toString();
    }
    
    private static String cryptUTF8(String hashType, String salt, String value) {
        return value != null ? cryptBytes(hashType, salt, value.getBytes(UTF8)) : null;
    }
    
    private static String digestHash64(String hashType, byte[] bytes) {
        if (hashType == null) {
            hashType = "SHA";
        }
        try {
            MessageDigest messagedigest = MessageDigest.getInstance(hashType);
            messagedigest.update(bytes);
            byte[] digestBytes = messagedigest.digest();
            char[] digestChars = Hex.encodeHex(digestBytes);
            String checkCrypted = new String(digestChars);
            StringBuilder sb = new StringBuilder();
            sb.append("{").append(hashType).append("}");
            sb.append(checkCrypted);
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        	return "";
        }
    }
    
    private static String digestHash64UTF8(String hashType,String value) {
    	return value != null ? digestHash64(hashType, value.getBytes(UTF8)) : null;
    }
    
    private static String encodeBase64URLSafeString(String hashType, byte[] bytes) {
        if (hashType == null) {
            hashType = "SHA";
        }
        try {
            MessageDigest messagedigest = MessageDigest.getInstance(hashType);
            messagedigest.update(bytes);
            byte[] digestBytes = messagedigest.digest();
            return Base64.encodeBase64URLSafeString(digestBytes).replace('+', '.');
        } catch (NoSuchAlgorithmException e) {
        	return "";
        }
    }
	
    private static String encodeBase64URLSafeStringUTF8(String hashType,String value){
    	return value != null ? encodeBase64URLSafeString(hashType, value.getBytes(UTF8)) : null;
    }
}
