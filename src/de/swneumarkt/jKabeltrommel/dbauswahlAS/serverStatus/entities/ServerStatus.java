package de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus.entities;

/**
 * Created by derduke on 08.06.16.
 */
public class ServerStatus implements IServerStatus {
    private final int anzahl;

    public ServerStatus(int anzahl) {
        this.anzahl = anzahl;
    }

    @Override
    public int anzahlClients() {
        return anzahl;
    }
}
