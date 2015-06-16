/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.centraleBank;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.*;
import bank.bankieren.Money;
import fontys.util.NumberDoesntExistException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastiaan
 */
public class CentraleBank implements ICentraleBank {

    private int nextRekNr = 100000000;
    private ArrayList<IBank> banken;

    public CentraleBank() {
        banken = new ArrayList<>();
    }

    @Override
    public boolean maakOver(int zender, int ontvanger, Money bedrag) {

                
        IBank zenderBank = null;
        IBank ontvangerBank = null;
        for(IBank bank: banken){
            if(bank.getRekening(zender) != null)
                zenderBank = bank;
            if(bank.getRekening(ontvanger) != null)
                ontvangerBank = bank;
        }
        if(zenderBank == null || ontvangerBank == null )
            return false;

        Money negative = Money.difference(new Money(0, bedrag.getCurrency()), bedrag);
        boolean zenderGelukt = zenderBank.muteerRekening(zender, negative);
        boolean ontvangerGelukt = ontvangerBank.muteerRekening(ontvanger, bedrag);

        if (zenderGelukt && ontvangerGelukt) {
            return true;
        } else if (!zenderGelukt && ontvangerGelukt) {
            ontvangerBank.muteerRekening(ontvanger, negative);
            return false;
        } else if (zenderGelukt && !ontvangerGelukt) {
            zenderBank.muteerRekening(zender, bedrag);
            return false;
        } else {
            return false;
        }

    }

    @Override
    public int nextBankNr() {
        return nextRekNr++;
    }

    @Override
    public boolean addBank(IBank bank) {
        if (bank != null) {
            banken.add(bank);
            return true;
        }
        return false;
    }

    @Override
    public IRekening getRekening(int rekenNr) {

        for (IBank bank : banken) {
            IRekening rekening = bank.getRekening(rekenNr);
            if (rekening != null) {
                return rekening;
            }
        }
        return null;
    }

}
