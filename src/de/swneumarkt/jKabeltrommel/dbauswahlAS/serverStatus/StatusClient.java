package de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus;

import java.io.*;
import java.net.Socket;

/**
 * Created by derduke on 07.06.16.
 */
public class StatusClient {

    private final Socket socket;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public StatusClient(Socket socket) throws IOException {
        this.socket = socket;
        objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }


    public boolean isConnected() {
        return !socket.isClosed();
    }

    public int getAnzahlClients() throws IOException {
        objectOutputStream.writeObject("CLIENTS");
        objectOutputStream.flush();
        try {
            return Integer.parseInt((String) objectInputStream.readObject());
        } catch (ClassNotFoundException e) {
            return -1;
        }
    }
}
