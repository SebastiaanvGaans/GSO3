package bank.bankieren;

import RemoteObserver.BasicPublisher;
import RemoteObserver.RemotePropertyListener;
import RemoteObserver.RemotePublisher;
import bank.centraleBank.ICentraleBank;
import fontys.util.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Bank implements IBank{

    /**
     *
     */
    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer, IRekeningTbvBank> accounts;
    private Collection<IKlant> clients;
    private int nieuwReknr;
    private String name;
    private ICentraleBank centrale;
    BasicPublisher bp;

    public Bank(String name, ICentraleBank centrale) {
        accounts = new HashMap<Integer, IRekeningTbvBank>();
        clients = new ArrayList<IKlant>();
        nieuwReknr = 100000000;
        this.name = name;
        this.centrale = centrale;
        bp =   new BasicPublisher(new String[]{"Balie"});
    }

    public synchronized int openRekening(String name, String city) {
        if (name.equals("") || city.equals("")) {
            return -1;
        }

        nieuwReknr = centrale.nextBankNr();

        IKlant klant = getKlant(name, city);
        IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
        accounts.put(nieuwReknr, account);
        nieuwReknr++;
        return nieuwReknr - 1;
    }

    private IKlant getKlant(String name, String city) {
        for (IKlant k : clients) {
            if (k.getNaam().equals(name) && k.getPlaats().equals(city)) {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        clients.add(klant);
        return klant;
    }

    public IRekening getRekening(int nr) {
        return accounts.get(nr);
    }

    public boolean maakOver(int source, int destination, Money money)
            throws NumberDoesntExistException {
        if (source == destination) {
            throw new RuntimeException(
                    "cannot transfer money to your own account");
        }
        if (!money.isPositive()) {
            throw new RuntimeException("money must be positive");
        }

        IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
        if (source_account == null) {
            throw new NumberDoesntExistException("account " + source
                    + " unknown at " + name);
        }

        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);

        if (dest_account == null) {
            dest_account = (IRekeningTbvBank) centrale.getRekening(destination);
            if (dest_account == null) {
                throw new NumberDoesntExistException("account " + destination
                        + " unknown at " + name);
            } else {
                return centrale.maakOver(source, destination, money);
            }
        }

        Money negative = Money.difference(new Money(0, money.getCurrency()),
                money);
        boolean success = source_account.muteer(negative);
        if (!success) {
            return false;
        }

        success = dest_account.muteer(money);

        if (!success) // rollback
        {
            source_account.muteer(money);
        }
        return success;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean muteerRekening(int rekeningNummer, Money bedrag) {
        IRekeningTbvBank rekening = (IRekeningTbvBank) getRekening(rekeningNummer);
        bp.inform(this,"Balie" , null, rekeningNummer);
        return rekening.muteer(bedrag);

    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        bp.removeProperty(property);
    }

}
