package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.config.Reader;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBServer;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.OnlyOneUserExeption;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.sql.SQLException;

/**
 * Startet den Akteursanwendungsfall der DB auswahl.
 * Created by derduke on 19.05.2016.
 */
public class DBAuswahlAAS {
    public final int REMOTEPORT = 4242;
    private String pfad;
    private HSQLDBServer server = null;
    private InetAddress serverIP = null;

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
        } catch (Exception e) {
            // Server kann nur gestartet werden wenn er der einzige ist der auf die DB dateien zugreift
            db = null;
        }
        db = connectRemoteDB();


        return db;
    }

    private IDBWrapper connectRemoteDB() {
        IDBWrapper db = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pfad + "serverIp.txt")));
            String next = null;
            while ((next = br.readLine()) != null && db == null) {
                try {
                    InetAddress ip = InetAddress.getByName(next);
                    db = new HSQLDBWrapper(ip);
                    serverIP = ip;
                } catch (Exception e) {
                    db = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    private HSQLDBServer getServer() throws IOException, OnlyOneUserExeption, SQLException, ClassNotFoundException {
        File ip = new File(pfad + "serverIp.txt");
        if (!ip.exists()) {
            ip.createNewFile();
            ip.deleteOnExit();
        }
        BufferedWriter fw = new BufferedWriter(new FileWriter(ip));
        for (InetAddress ia : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
            fw.write(ia.getHostAddress());
            fw.write(System.lineSeparator());
        }
        fw.flush();
        fw.close();
        return new HSQLDBServer(pfad);
    }

    public boolean hasServer() {
        return server != null;
    }

    public InetAddress getServerIP() {
        return serverIP;
    }
}
