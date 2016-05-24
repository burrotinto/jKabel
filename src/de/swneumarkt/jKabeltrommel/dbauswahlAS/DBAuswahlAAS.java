package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;

import javax.swing.*;
import java.io.File;
import java.sql.SQLException;
import java.util.function.Consumer;

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        KabeltypE k = null;
        IDBWrapper db =    new DBAuswahlAAS().getDBWrapper();

//        db.create(new KabeltypE(102035,"4*150"));

        db.getAllKabeltypen().forEach(new Consumer<KabeltypE>() {
            @Override
            public void accept(KabeltypE kabeltypE) {
                System.out.println(kabeltypE.toString());
            }
        });

        k=db.getAllKabeltypen().get(0);
        db.update(k);

//        db.create(new TrommelE(k,"100030002",0,500));
        db.getTrommelnForTyp(k).forEach(new Consumer<TrommelE>() {
            @Override
            public void accept(TrommelE kabeltypE) {
                System.out.println(kabeltypE.toString());
            }
        });
    }
}
