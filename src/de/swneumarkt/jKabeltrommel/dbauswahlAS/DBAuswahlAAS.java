package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.OnlyOneUserExeption;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;

/**
 * Created by derduke on 19.05.2016.
 */
public class DBAuswahlAAS {
    public final int REMOTEPORT = 4242;
    private String pfad = "O:\\KFM-Verwaltung\\Materialwirtschaft\\Lager\\jKabelDB\\";

    public DBAuswahlAAS() {
    }

    private String getPath(String pfad) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(pfad));
        chooser.setDialogTitle("DB Pfad");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION ? null : chooser.getSelectedFile().getPath()+ File.separator;

    }

    /**
     * Gibt eine Datenbank zurück
     *
     * @return
     */
    public IDBWrapper getDBWrapper() {
        IDBWrapper db = null;
        try {
            pfad = getPath(pfad);
            db = new HSQLDBWrapper(pfad, "localhost");
            startServer(db);
        } catch (OnlyOneUserExeption oOUE) {
            db = connectRemoteDB(pfad);
        } catch (Exception e){
            return null;
        }
        return db;
    }

    private IDBWrapper connectRemoteDB(String pfad) {
        IDBWrapper db = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pfad + "serverIp.txt")));
            String next = null;
            while ((next = br.readLine()) != null && db == null) {
                try {
                    db = new HSQLDBWrapper(pfad, next);
                } catch (IOException e) {
                    db = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    private void startServer(IDBWrapper db) throws IOException {
        File ip = new File(pfad + "serverIp.txt");
        if (!ip.exists()) {
            ip.createNewFile();
            ip.deleteOnExit();
        }
        BufferedWriter fw = new BufferedWriter(new FileWriter(ip));
        for (InetAddress ia : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
            System.out.println(ia.getHostAddress());
            fw.write(ia.getHostAddress());
            fw.write(System.lineSeparator());
        }
        fw.flush();
        fw.close();
    }
}
