package org.project.encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Md5Base64Encoder implements IEncoder {
    @Override
    public String encode(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(url.getBytes());
            String base64Hash = Base64.getUrlEncoder().encodeToString(hashBytes);
            return base64Hash.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
