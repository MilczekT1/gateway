package pl.konradoniecki.Budget.models;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import pl.konradboniecki.Budget.models.account.AccountForm;

public class AccountFormTest {
    
    private static AccountForm accountForm;
    @AfterClass
    public static void release(){
        accountForm = null;
    }
    
    @Test
    public void checkRepeatedPasswordWhenSetTheSameTest(){
        accountForm = new AccountForm();
        accountForm.setEmail("1234@hotmail.com");
        accountForm.setFirstName("John");
        accountForm.setLastName("Kowalsky");
        accountForm.setPhoneNumber("123456765");
        accountForm.setPassword("123456");
        accountForm.setRepeatedPassword("123456");
        
        Assert.assertTrue("Password and repeated password should be the same in AccountForm using checkRepeatedPassword()",accountForm.checkRepeatedPassword());
    }
    @Test
    public void checkRepeatedPasswordWhenNotSetTheSameTest(){
        accountForm = new AccountForm();
        accountForm.setEmail("1234@hotmail.com");
        accountForm.setFirstName("John");
        accountForm.setLastName("Kowalsky");
        accountForm.setPhoneNumber("123456765");
        accountForm.setPassword("123456");
        accountForm.setRepeatedPassword("fsjdkhgfliskv324");
        
        Assert.assertFalse("Password and repeated password should not be the same in AccountForm using checkRepeatedPassword()",accountForm.checkRepeatedPassword());
    }
}
