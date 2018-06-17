package pl.konradboniecki.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TokenGenerator {

    public static String hashPassword(String password){
        return hashString(password);
    }

    private static String hashString(String string){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashArray = digest.digest(string.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hashArray);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String createUUIDToken(){
        return UUID.randomUUID().toString();
    }
}
