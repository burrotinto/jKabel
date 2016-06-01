package de.swneumarkt.jKabeltrommel.config;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by derduke on 01.06.16.
 */
public class Reader {
    private static final String SWNPFAD = "O:\\KFM-Verwaltung\\Materialwirtschaft\\Lager\\jKabelDB\\";
    private static final String DBPFADPROP = "DB.PFAD";
    private static Reader instance = new Reader();


    private Reader() {
    }

    public static Reader getInstance() {
        return instance;
    }

    public String getPath() {
        String path = SWNPFAD;
        try {
            Properties prop = new Properties();
            System.out.println(System.getProperty("user.home") + File.separator + "jKabel.prop");
            File propFile = new File(System.getProperty("user.home") + File.separator + "jKabel.prop");
            if (!propFile.exists()) propFile.createNewFile();
            prop.load(new FileReader(propFile));


            if (prop.getProperty(DBPFADPROP) == null) {
                path = choosePath(path);
                prop.setProperty(DBPFADPROP, path);
                prop.store(new FileWriter(propFile), "Konfigurationsdatei des JKabel");
            } else {
                path = prop.getProperty(DBPFADPROP);
            }


        } catch (IOException e) {
            path = choosePath(".");
        }
        return path;
    }

    private String choosePath(String pfad) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(pfad));
        chooser.setDialogTitle("DB Pfad");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION ? null : chooser.getSelectedFile().getPath() + File.separator;

    }
}
