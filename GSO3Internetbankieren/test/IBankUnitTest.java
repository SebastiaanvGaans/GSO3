/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bank.bankieren.*;
import bank.centraleBank.CentraleBank;
import fontys.util.NumberDoesntExistException;
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
public class IBankUnitTest {
    IBank bank;
    
    
    public IBankUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        bank = new Bank("TestBank", new CentraleBank());
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void RekeningTest(){
        int rekeningNummer = bank.openRekening("testNaam", "testStad");
        if(rekeningNummer == -1){
            fail("combinatie van naam en plaats niet uniek/leeg");
        }
        
        IRekening rekening = bank.getRekening(rekeningNummer);
        IRekening rekening2 = bank.getRekening( 0);
        
        assertNotNull("bestaande rekening onbekend", rekening);
        assertNull("niet bestaande rekening retourneerd geen null", rekening2);
        
        assertEquals("Opgehaalde rekening heeft ander nummer",rekeningNummer, rekening.getNr());
        assertEquals("Opgehaalde rekening's eigenaar heeft een andere naam", "testNaam",rekening.getEigenaar().getNaam());
        assertEquals("Opgehaalde rekening's eigenaar heeft een andere stad", "testStad",rekening.getEigenaar().getPlaats());
    }
    
    @Test
    public void maakOverTest() {
        int rekeningNummer1 = bank.openRekening("testNaam", "testStad");
        int rekeningNummer2 =  bank.openRekening("NameTest", "StadTest");
        Money money1 = new Money(1000, Money.EURO);
        Money money2 = new Money(-1000, Money.EURO);
        Money money3 = new Money(100000000, Money.EURO);
        
        try{
            bank.maakOver(rekeningNummer1, rekeningNummer1, money1);
            fail("Source and destination must be different");   
        }catch(Exception e){}
        
        try{
            bank.maakOver(rekeningNummer1, rekeningNummer2, money2);
            fail("Amount to be transferred may not be negative");
        }catch(Exception e){}
        
        try{
            bank.maakOver(rekeningNummer1, rekeningNummer2, money3);
            fail("Amount to be transferred may not be more than KREDIETLIMIET");
        }catch(Exception e){}
        
        try {
            long vorigSaldoRekening1 = bank.getRekening(rekeningNummer1).getSaldo().getCents() - 1000;
            long vorigSaldoRekening2 = bank.getRekening(rekeningNummer2).getSaldo().getCents() + 1000;
            
            
            assertTrue("Cant make a valid transaction", bank.maakOver(rekeningNummer1, rekeningNummer2, money1));
            assertEquals("Saldo source niet goed bijgewerkt",vorigSaldoRekening1 ,bank.getRekening(rekeningNummer1).getSaldo().getCents());
            assertEquals("Saldo destination niet goed bijgewerkt",vorigSaldoRekening2 ,bank.getRekening(rekeningNummer2).getSaldo().getCents());            
            
        } catch (NumberDoesntExistException ex) {
            fail("Cant make a valid transaction");
        }
    }
    
    @Test
    public void getNaam(){
        assertEquals("naam van bank niet correct opgehaald","TestBank", bank.getName());
    }
}
