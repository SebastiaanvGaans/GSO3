/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.centraleBank.CentraleBank;
import bank.internettoegang.Bankiersessie;
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
public class IBankierSessieTest {
    IBank bank;
    int rekeningNummer;
    IBankiersessie sessie;
    
    public IBankierSessieTest() {
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
        rekeningNummer = bank.openRekening("testNaam", "testStad");
        try {
            sessie = new Bankiersessie(rekeningNummer, bank);
        } catch (RemoteException ex) {
            Logger.getLogger(IBankierSessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }
    @Test
    public void isGeldigTest(){
        try{
        if(!sessie.isGeldig())
            fail("Sessie is ongeldig terwijl de geldigheidsduur niet verstreken is");
        }
        catch(Exception e){
            fail("Kan sessie niet ophalen");
        }
        try {
            Thread.sleep(600000);
            if(sessie.isGeldig())
                fail("Sessie is geldig terwijl de geldigheidsduur verstreken is");
        } catch (Exception ex) {
            fail("Kan sessie niet ophalen");
        }
    }
    
    @Test
    public void maakOverTest(){
        Money money1 = new Money(10 , Money.EURO);
        
        try{
            sessie.maakOver(rekeningNummer, money1);
            fail("Eigen nummer mag niet bestemming zijn");
        }catch(Exception e){
            
        }
    }
    
    @Test
    public void logUitTest(){
        try {
            sessie.logUit();
            if(sessie.isGeldig())
                fail("Sessie is niet beeindigd");
        } catch (RemoteException ex) {
            fail("failed to end session");
            Logger.getLogger(IBankierSessieTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void getRekeningTest(){
        try{
            IRekening rekening = sessie.getRekening();
            assertNotNull("bestaande rekening onbekend", rekening);
        }catch(Exception e){
            fail("kan rekening niet ophalen bij bank");
        }
    }
}
