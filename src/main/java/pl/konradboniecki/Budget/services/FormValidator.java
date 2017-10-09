package pl.konradboniecki.Budget.services;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class FormValidator {
    
    /*
     * error codes:
     * firstName   0
     * lastName    1
     * age         2
     * email       3
     * phoneNumber 4
     */
    
    
    public Map<String, String> validateRegisterAccountForm(Account account){
        HashMap<String, String> errorCodesMap = new HashMap<>();
        ArrayList<Integer> errorCodes = new ArrayList<>(5);
        
        Optional<String> valueToCheck = Optional.ofNullable(account.getFirstName());
        
        if (!Pattern.matches("[a-zA-Z]{5,}",valueToCheck.orElse("")))
            errorCodesMap.put("firstNameFailure", "komunikat");
        
        valueToCheck = Optional.ofNullable(account.getLastName());
        if (!Pattern.matches("[a-zA-Z]{3,}",valueToCheck.orElse("")))
            errorCodes.add(1);
    
        valueToCheck = Optional.ofNullable(account.getAge());
        if (!Pattern.matches("",valueToCheck.orElse("")))
            errorCodes.add(2);
        
        valueToCheck = Optional.ofNullable(account.getEmail());
        if (!Pattern.matches("\\w+@\\w+.[a-zA-Z]+",valueToCheck.orElse(""))) {
            errorCodes.add(3);
        }
    
        valueToCheck = Optional.ofNullable(account.getPhoneNumber());
        if (!Pattern.matches("",valueToCheck.orElse(""))) {
            errorCodes.add(4);
        }
        
        return errorCodesMap;
    }
}
