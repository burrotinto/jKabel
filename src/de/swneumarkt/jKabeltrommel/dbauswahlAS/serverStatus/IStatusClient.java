package de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus;

import java.io.IOException;

/**
 * Created by derduke on 08.06.16.
 */
public interface IStatusClient {
    boolean isConnected();

    int getAnzahlClients() throws IOException;

    void sendObject(Object o) throws IOException;
}
