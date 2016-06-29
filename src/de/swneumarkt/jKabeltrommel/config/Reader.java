package de.swneumarkt.jKabeltrommel.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Einfacher Propertysreader. ISt ein Singelton
 * Created by derduke on 01.06.16.
 */
public class Reader {
    public static final String SWNPFAD = "O:\\KFM-Verwaltung\\Materialwirtschaft\\Lager\\jKabelDB\\";
    private static final String DBPFADPROP = "DB.PFAD";
    private static Reader instance = new Reader();

    private final File propFile = new File(System.getProperty("user.home") + File.separator + "jKabel.prop");
    private Properties prop = null;


    private Reader() {
    }

    /**
     * Gibt die Reader Instanz zurück
     *
     * @return den Reader
     */
    public static Reader getInstance() {
        return instance;
    }

    /**
     * Gibt den Pfad zur DB zurück
     *
     * @return Pfad zu der zu benutzenden DB ODER NULL  wenn nicht gestetzt
     */
    public String getPath() {
        String path = null;
        try {
            Properties prop = getProperties();
            if (prop.getProperty(DBPFADPROP) != null) {
                path = prop.getProperty(DBPFADPROP);
            }
        } catch (IOException e) {
            return null;
        }
        return path;
    }

    private Properties getProperties() throws IOException {
        // lazy implementation
        if (prop == null) {
            prop = new Properties();
            if (!propFile.exists()) propFile.createNewFile();
            prop.load(new FileReader(propFile));
        }
        return prop;
    }

    /**
     * Zum Speichern des Pfades
     *
     * @param path wo die DB liegt
     * @throws IOException
     */
    public void savePath(String path) throws IOException {
        prop.setProperty(DBPFADPROP, path);
        prop.store(new FileWriter(propFile), "Konfigurationsdatei des JKabel");
    }


}
