/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
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
        bank = new Bank("TestBank");
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
        //TODO
        //HOWTODOTHIS?
    }
    
    @Test
    public void maakOverTest(){
        Money money1 = new Money(0 , Money.EURO);
        
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
