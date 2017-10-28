package pl.konradoniecki.Budget.models;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.Account;
import pl.konradboniecki.Budget.models.AccountForm;

public class AccountTest {
    
    private static AccountForm accountForm;
    
    @Before
    public void createAccountForm(){
        accountForm = new AccountForm();
        accountForm.setEmail("1234@hotmail.com");
        accountForm.setFirstName("John");
        accountForm.setLastName("Kowalsky");
        accountForm.setPhoneNumber("123456765");
        accountForm.setPassword("123456");
        accountForm.setRepeatedPassword("123456");
    }
    
    @AfterClass
    public static void release(){
        accountForm = null;
    }
    
    @Test
    public void singlePasswordHashingUsingAccountFormInConstructorTest(){
        Account account = new Account(accountForm);
        
        if (account.getPassword().equals(Utils.hashPassword(accountForm.getPassword()))){
            return;
        }
        Assert.fail("Account's password should have been hashed 1 time.");
    }
}
