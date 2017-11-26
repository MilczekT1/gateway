package pl.konradboniecki.Budget.core;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    
    public static String hashString(String string){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashArray = digest.digest(string.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hashArray);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }
    public static String hashPassword(String password){
        return hashString(password);
    }
    
    public static String createActivationCode(String email){
        String hash = Utils.hashString(email).toLowerCase();
        return hash.substring(hash.length()/2,(hash.length()/3)*2);
    }
}
