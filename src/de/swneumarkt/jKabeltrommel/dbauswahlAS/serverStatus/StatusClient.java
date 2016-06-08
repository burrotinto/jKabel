package de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus.entities.IServerStatus;

import java.io.*;
import java.net.Socket;

/**
 * Created by derduke on 07.06.16.
 */
public class StatusClient implements IStatusClient {

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
        sendObject("Status");
        try {
            return ((IServerStatus) objectInputStream.readObject()).anzahlClients();
        } catch (ClassNotFoundException e) {
            return -1;
        }
    }

    @Override
    public void sendObject(Object o) throws IOException {
        objectOutputStream.writeObject(o);
        objectOutputStream.flush();
    }
}
