/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gebruiker
 */
public class IBalieTest {
    IBank bank;
    IBalie balie;
    
    public IBalieTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        bank = new Bank("TestBank");
        try {
            balie = new Balie(bank);
        } catch (RemoteException ex) {
            Logger.getLogger(IBalieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void BalieTest(){
        String wachtwoord = "1234";
        String foutWachtwoord1 = "12";
        String foutWachtwoord2 = "123456789";
        String accountNaam = "";
        
        
        try {
            assertNull("Naam mag niet null zijn",balie.openRekening("", "stad", wachtwoord));
            assertNull("Stad mag niet null zijn",balie.openRekening("naam", "", wachtwoord));
            assertNull("Wachtwoord mag niet null zijn",balie.openRekening("naam", "stad", ""));
            
            assertNull("Wachtwoord moet langer dan 4 tekens zijn", balie.openRekening("naam", "stad", foutWachtwoord1));
            assertNull("Wachtwoord moet korter dan 8 tekens zijn", balie.openRekening("naam", "stad", foutWachtwoord2));
            
            accountNaam = balie.openRekening("naam", "stad", wachtwoord);
        
        
            assertNull("Wachtwoord moet gecontroleerd worden", balie.logIn(accountNaam, ""));
            assertNull("Accountnaam moet bestaan", balie.logIn("GeenNaam", wachtwoord));
            
            IBankiersessie sessie = balie.logIn(accountNaam, wachtwoord);
            
            if(sessie == null){
                fail("Correcte accountnaam + wachtwoord geeft null terug");
            }
        
        } catch (RemoteException ex) {
            Logger.getLogger(IBalieTest.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
