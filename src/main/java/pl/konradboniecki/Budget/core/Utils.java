package pl.konradboniecki.Budget.core;

import pl.konradboniecki.Budget.models.account.Account;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    
    public static String hashPassword(String password){
        return hashString(password);
    }
    
    public static String createActivationCode(String email){
        String hash = Utils.hashString(email).toLowerCase();
        return hash.substring(hash.length()/2,(hash.length()/3)*2);
    }
    public static String createNewPasswordConfirmationCode(String hashedPassword){
        String partOfHashedPassword = hashedPassword.substring( 10,30);
        String hash = Utils.hashString(partOfHashedPassword).toLowerCase();
        return hash.substring(hash.length()/2,(hash.length()/3)*2);
    }
    public static String createInvitationCode(String email){
        String hash = Utils.hashString(email).toLowerCase();
        return hash;
    }
    public static boolean isActivationCodeValid(Account acc, String activationCodeFromUrl){
        return Utils.createActivationCode(acc.getEmail()).equals(activationCodeFromUrl) ? true : false;
    }
    
    public static boolean isInvitationCodeValid(Account acc, String invitationCode){
        return Utils.createInvitationCode(acc.getEmail()).equals(invitationCode) ? true : false;
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
}
