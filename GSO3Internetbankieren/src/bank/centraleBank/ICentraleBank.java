/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centraleBank;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;

/**
 *
 * @author Sebastiaan
 */
public interface ICentraleBank {
    
    
    /**
     * Maakt een bedrag over van de bron rekening bij bank 1 naar de doel rekening bij bank 2,
     * alleen als de doel rekening door het afschrijven van het bedrag niet lager wordt dan het kredietlimiet
     * 
     * @param zender bron rekening
     * @param ontvanger doel rekening
     * @param bedrag het over te maken bedrag
     * @return true als het gelukt is, false als gefaald
     */
    public boolean maakOver(int zender, int ontvanger, Money bedrag);
    
    
    /**
     * Berekend het eerst volgende vrije bankrekeningnummer
     * @return het eerst volgende vrije bankrekeningnummer
     */
    public int nextBankNr();
    
    /**
     * Voegt een bank toe aan de centrale bank
     * @param bank de toe te voegen bank
     * @return true als het gelukt is, false als het gefaald heeft
     */
    public boolean addBank(IBank bank);
    
    /**
     * Haalt de rekenning op van het gegeven rekening nummer
     * @param rekenNr het nummer van de gevraagde rekening
     * @return de rekening die bij het rekeningnummer hoort
     */
    public IRekening getRekening(int rekenNr);
}
