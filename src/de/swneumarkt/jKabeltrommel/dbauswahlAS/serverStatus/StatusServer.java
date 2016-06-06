package de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by derduke on 03.06.16.
 */
public class StatusServer implements Runnable {
    public static int PORT = 9089;
    private ServerSocket ssO;
    private List<ClientHandler> sockets = new LinkedList<>();
    private Set<IConnectionListner> connectionListners = new HashSet<IConnectionListner>();


    public StatusServer() throws IOException {
        ssO = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        while (!isStoppt()) {
            try {
                ClientHandler cH = new ClientHandler(ssO.accept(), this);
                synchronized (ssO) {
                    sockets.add(cH);
                }
                new Thread(cH).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void addIConnectionListner(IConnectionListner iCL) {
        connectionListners.add(iCL);
    }

    private boolean isStoppt() {
        return false;
    }

    public int getAnzahlClients() {
        List<ClientHandler> l = new LinkedList<>();
        synchronized (ssO) {
            for (ClientHandler s : sockets) {
                if (!s.isClosed()) {
                    l.add(s);
                }
            }
            sockets = l;
        }
        return l.size();
    }
}
