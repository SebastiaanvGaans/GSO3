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

        IRekeningTbvBank rekeningZender = (IRekeningTbvBank) getRekening(zender);
        IRekeningTbvBank rekeningOntvanger = (IRekeningTbvBank) getRekening(ontvanger);

        if (rekeningZender == null || rekeningOntvanger == null) {
            return false;
        }

        Money negative = Money.difference(new Money(0, bedrag.getCurrency()), bedrag);
        boolean zenderGelukt = rekeningZender.muteer(negative);
        boolean ontvangerGelukt = rekeningOntvanger.muteer(bedrag);

        if (zenderGelukt && ontvangerGelukt) {
            return true;
        } else if (!zenderGelukt && ontvangerGelukt) {
            rekeningOntvanger.muteer(negative);
            return false;
        } else if (zenderGelukt && !ontvangerGelukt) {
            rekeningZender.muteer(bedrag);
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
