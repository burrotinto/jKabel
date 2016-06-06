package de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus;

import java.net.InetAddress;

/**
 * Created by derduke on 05.06.16.
 */
public interface IConnectionListner {
    void newClient(InetAddress adress);

    void clientLeave(InetAddress adress);

}
