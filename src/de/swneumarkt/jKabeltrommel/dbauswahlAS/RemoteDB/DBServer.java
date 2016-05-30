package de.swneumarkt.jKabeltrommel.dbauswahlAS.RemoteDB;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by derduke on 28.05.16.
 */
public class DBServer implements Runnable {
    private final IDBWrapper db;
    private boolean stopp = false;
    private ServerSocket serverSocket;

    public DBServer(IDBWrapper db, int port) throws IOException {
        this.db = db;
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (!stopp) {
            try {
                new Thread(new ClientHandler(db, serverSocket.accept())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
