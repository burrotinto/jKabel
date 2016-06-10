package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.config.Reader;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBServer;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.OnlyOneUserExeption;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus.IStatusClient;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus.StatusClient;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.serverStatus.StatusServer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

/**
 * Startet den Akteursanwendungsfall der DB auswahl.
 * Created by derduke on 19.05.2016.
 */
public class DBAuswahlAAS {
    private final DBAuswahlK kontroll;
    private String pfad;
    private HSQLDBServer server = null;
    private InetAddress serverIP = null;

    public DBAuswahlAAS() {
        kontroll = new DBAuswahlK();
    }

    private String choosePath(String pfad) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(pfad));
        chooser.setDialogTitle("DB Pfad");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION ? null : chooser.getSelectedFile().getPath() + File.separator;

    }

    /**
     * Gibt eine Datenbank zurück.
     *
     * @return eine DB
     */
    public IDBWrapper getDBWrapper() {
        IDBWrapper db = null;

        pfad = de.swneumarkt.jKabeltrommel.config.Reader.getInstance().getPath();
        if (pfad == null) {
            pfad = choosePath(Reader.SWNPFAD);
            try {
                Reader.getInstance().savePath(pfad);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            server = getServer();
            server.start();
            try {
                new Thread(new StatusServer()).start();
            } catch (IOException ioE) {
                ioE.printStackTrace();
                //todo
            }

        } catch (Exception e) {
            // Server kann nur gestartet werden wenn er der einzige ist der auf die DB dateien zugreift
        }
        db = connectRemoteDB();
        return db;
    }

    /**
     * Versucht sich mit der entfernten Datenbank zu verbinden
     *
     * @param ip Adresse der DB
     * @return Wrapper zur angeforderten DB, Null wenn keine Verbindung möglich war.
     */
    private IDBWrapper connectRemoteDB(InetAddress ip) {
        try {
            if (ip.isReachable(100))
                try {
                    HSQLDBWrapper idbWrapper = new HSQLDBWrapper(ip);
                    serverIP = ip;
                    return idbWrapper;
                } catch (Exception e) {
                    return null;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IStatusClient getStatusClient() throws IOException {
        return new StatusClient(new Socket(serverIP, StatusServer.PORT));
    }

    private IDBWrapper connectRemoteDB() {
        IDBWrapper db = null;
        List<InetAddress> inetAddressList = kontroll.getAllIPfromFile(new File(pfad + "serverIp.txt"));
        int i = 0;
        while (i < inetAddressList.size() && db == null) {
            db = connectRemoteDB(inetAddressList.get(i++));
        }
        return db;
    }

    private HSQLDBServer getServer() throws IOException, OnlyOneUserExeption, SQLException, ClassNotFoundException {
        HSQLDBServer server = new HSQLDBServer(pfad);
        kontroll.handleIPFile(new File(pfad + "serverIp.txt"));

        return server;
    }

    public boolean hasServer() {
        return server != null;
    }

    public InetAddress getServerIP() {
        return serverIP;
    }
}
