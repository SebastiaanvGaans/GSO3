/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RemoteObserver;

import java.beans.*;
import java.rmi.*;
import java.util.*;


public interface RemotePropertyListener
    extends EventListener, Remote  {

    /**
     * op basis van de informatie die evt overdraagt kan deze listener er voor
     * zorgen dat de bijbehorende observer gesynchroniseerd wordt
     * @param evt PropertyChangeEvent @see java.beans.PropertyChangeEvent
     * @throws RemoteException
     */
    void propertyChange(PropertyChangeEvent evt) throws RemoteException;
}

