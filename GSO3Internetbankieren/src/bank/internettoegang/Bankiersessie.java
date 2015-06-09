package bank.internettoegang;

import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;

public class Bankiersessie extends UnicastRemoteObject implements
        IBankiersessie, RemoteObserver.RemotePublisher {

    private static final long serialVersionUID = 1L;
    private long laatsteAanroep;
    private int reknr;
    private IBank bank;

    BasicPublisher bp;

    public Bankiersessie(int reknr, IBank bank) throws RemoteException {
        laatsteAanroep = System.currentTimeMillis();
        this.reknr = reknr;
        this.bank = bank;

        bp = new BasicPublisher(new String[]{"Rekeningen"});
    }

    public boolean isGeldig() {
        return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
    }

    @Override
    public boolean maakOver(int bestemming, Money bedrag)
            throws NumberDoesntExistException, InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        if (reknr == bestemming) {
            throw new RuntimeException(
                    "source and destination must be different");
        }
        if (!bedrag.isPositive()) {
            throw new RuntimeException("amount must be positive");
        }

        return bank.maakOver(reknr, bestemming, bedrag);
    }

    private void updateLaatsteAanroep() throws InvalidSessionException {
        if (!isGeldig()) {
            throw new InvalidSessionException("session has been expired");
        }

        laatsteAanroep = System.currentTimeMillis();
    }

    @Override
    public IRekening getRekening() throws InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        return bank.getRekening(reknr);
    }

    @Override
    public void logUit() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addProperty(property);
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeProperty(property);
        bp.removeListener(listener, property);
    }

}
