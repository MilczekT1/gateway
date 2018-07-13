package pl.konradboniecki.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Data
@Component
public class TokenGenerator {

    private String algorithm;

    public TokenGenerator() {
        setAlgorithm("SHA-256");
    }

    public String hashPassword(String password){
        return hashString(password);
    }

    private String hashString(String string){
        try {
            MessageDigest digest = MessageDigest.getInstance(getAlgorithm());
            byte[] hashArray = digest.digest(string.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hashArray);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public String createUUIDToken(){
        return UUID.randomUUID().toString();
    }
}
