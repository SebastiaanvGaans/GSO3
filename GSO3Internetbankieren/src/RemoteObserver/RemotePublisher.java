/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RemoteObserver;

import java.rmi.*;


public interface RemotePublisher extends Remote {

    /**
     * listener abonneert zich op PropertyChangeEvent's zodra property is 
     * gewijzigd
     * @param listener
     * @param property mag null zijn, dan abonneert listener zich op alle
     * properties; property moet wel een eigenschap zijn waarop je je kunt
     * abonneren
     */
    void addListener(RemotePropertyListener listener, String property)
            throws RemoteException;

    /**
     * het abonnement van listener voor PropertyChangeEvent's mbt property wordt
     * opgezegd
     * @param listener PropertyListener
     * @param property mag null zijn, dan worden alle abonnementen van listener
     * opgezegd
     */
    void removeListener(RemotePropertyListener listener, String property)
            throws RemoteException;

}

