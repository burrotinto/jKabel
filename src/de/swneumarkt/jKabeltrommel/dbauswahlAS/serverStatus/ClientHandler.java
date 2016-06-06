package de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by derduke on 05.06.16.
 */
class ClientHandler implements Runnable {
    private final Socket socket;
    private final StatusServer statusServer;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket socket, StatusServer statusServer) throws IOException {
        this.socket = socket;
        this.statusServer = statusServer;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        objectInputStream = new ObjectInputStream(socket.getInputStream());

    }

    @Override
    public void run() {
        Object o = null;
        try {
            while ((o = objectInputStream.readObject()) != null) {
                if (o instanceof String) {
                    String string = (String) o;
                    switch (string) {
                        case "Clients":
                            objectOutputStream.writeObject("" + statusServer.getAnzahlClients());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
