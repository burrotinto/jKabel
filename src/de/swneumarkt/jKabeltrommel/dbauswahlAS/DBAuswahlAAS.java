package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBWrapper;

import javax.swing.*;
import java.io.File;

/**
 * Created by derduke on 19.05.2016.
 */
public class DBAuswahlAAS {
    private final String selectedPath = ".";

    public DBAuswahlAAS() {
    }

    private String getPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("DB Pfad");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION ? null : chooser.getSelectedFile().getPath()+ File.separator;

    }

    public IDBWrapper getDBWrapper() {
        try {
            if(new File("O:\\KFM-Verwaltung\\Materialwirtschaft\\Lager\\jKabelDB").exists()){
                return new HSQLDBWrapper("O:\\KFM-Verwaltung\\Materialwirtschaft\\Lager\\jKabelDB\\");
            } else {
                return new HSQLDBWrapper(getPath());
            }
        } catch (Exception e){
            return null;
        }
    }
}
