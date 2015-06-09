/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RemoteObserver;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Bas
 */
public class portsAndIps {

    public final static String IP = "192.168.177.1";
    public final static int defaultServerPortChat = 1099;
    private static int lastPort = 1100;
    
    private static boolean isPortInUse(int port) throws IOException {
        // Assume no connection is possible.
        boolean result = false;

        try {
            (new Socket(IP, port)).close();
            result = true;
        } catch (SocketException e) {
            // Could not connect.
        }

        return result;
    }
    
    public static int getNewPort() throws IOException
    {
        while(isPortInUse(lastPort)){
            System.out.println(lastPort+" is in gebruik");
            lastPort++;}
        return lastPort;
    }
}
