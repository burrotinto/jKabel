/*
 * jKabel - Ein hochperfomantes, extremstanpassungsfähiges Mehrbenutzersystem zur erfassung von Kabelstrecken
 *
 * Copyright (C) 2016 Florian Klinger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.burrotinto.jKabel.config;

import de.burrotinto.jKabel.config.trommelSort.*;
import de.burrotinto.jKabel.config.typSort.AbstractTypeSort;
import de.burrotinto.jKabel.config.typSort.TypNameSort;
import de.burrotinto.jKabel.config.typSort.TypeFrequenzSort;
import de.burrotinto.jKabel.config.typSort.TypeMatNrSort;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ITrommelE;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Einfacher Propertysreader. ISt ein Singelton
 * Created by derduke on 01.06.16.
 */
public class ConfigReader {
    public static final String SWNPFAD = "O:\\KFM-Verwaltung\\Materialwirtschaft\\Lager\\jKabelDB\\";
    private static final String DBPFADPROP = "DB.PFAD";
    private static final String SORTTYP = "SORT.TYP";
    private static final String SORTTYPINORDER = "SORT.TYP.INORDER";
    private static final String SORTTROMMELINORDER = "SORT.TROMMEL.INORDER";
    private static final String SORTTROMMEL = "SORT.TROMMEL";
    private static ConfigReader instance = new ConfigReader();
    private static Logger log = Logger.getLogger(ConfigReader.class);
    private final File propFile = new File(System.getProperty("user.home") + File.separator + "jKabel.prop");
    private Properties prop = null;

    private HashMap<String, AbstractTypeSort> alleTypSortierer = null;
    private HashMap<String, AbstractTrommelSort> alleTrommelSortierer = null;

    private ConfigReader() {
    }

    /**
     * Gibt die ConfigReader Instanz zurück
     *
     * @return den ConfigReader
     */
    public static ConfigReader getInstance() {
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
    }

    private void save() throws IOException {
        prop.store(new FileWriter(propFile), "Konfigurationsdatei des JKabel");
        log.info("save:");
        for (Map.Entry<Object, Object> set : getProperties().entrySet()) {
            log.info(set.getKey() + " " + set.getValue());
        }
    }

    public void setTypSort(String typSort) throws IOException {
        prop.setProperty(SORTTYP, typSort);
        save();
    }

    public void setTypeInOrder(boolean inOrder) throws IOException {
        prop.setProperty(SORTTYPINORDER, Boolean.toString(inOrder));
        save();
    }

    public Comparator<? super IKabeltypE> getKabeltypSort() {
        try {
            //Default Wert speichern
            if (getProperties().getProperty(SORTTYP) == null) {
                try {
                    setTypSort("matnr");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            AbstractTypeSort c = alleTypSortierer.get(getProperties().getProperty(SORTTYP));
            if (c == null) c = new TypeMatNrSort();
            c.setInOrder(isTypeInOrder());
            return c;

        } catch (Exception e) {
            return new TypeMatNrSort();
        }
    }

    public Boolean isTypeInOrder() {
        try {
            return getProperties().getProperty(SORTTYPINORDER) == null || Boolean.parseBoolean(getProperties().getProperty(SORTTYPINORDER));
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    public Comparator<ITrommelE> getTrommelSort() {
        try {
            //Default Wert speichern
            if (getProperties().getProperty(SORTTROMMEL) == null) {
                try {
                    setTrommelSort("intelligent");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            AbstractTrommelSort c = alleTrommelSortierer.get(prop.getProperty(SORTTROMMEL));
            if (c == null) {
                c = new TrommelIntelligentSort();
            }
            c.setInOrder(isTrommelInOrder());

            return c;

        } catch (Exception e) {
            return new TrommelIntelligentSort();
        }
    }

    public void setTrommelSort(String typSort) throws IOException {
        prop.setProperty(SORTTROMMEL, typSort);
        save();
    }

    private boolean isTrommelInOrder() {
        try {
            return getProperties().getProperty(SORTTROMMELINORDER) == null || Boolean.parseBoolean(getProperties().getProperty(SORTTROMMELINORDER));
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void setTrommelInOrder(boolean inOrder) throws IOException {
        prop.setProperty(SORTTROMMELINORDER, Boolean.toString(inOrder));
        save();
    }

    public Collection<AbstractTypeSort> getAllTypSort() {
        if (alleTypSortierer == null) {
            alleTypSortierer = new HashMap<>();
            alleTypSortierer.put(TypeFrequenzSort.class.getName(), new TypeFrequenzSort());
            alleTypSortierer.put(TypeMatNrSort.class.getName(), new TypeMatNrSort());
            alleTypSortierer.put(TypNameSort.class.getName(), new TypNameSort());
        }
        return alleTypSortierer.values();
    }

    public Collection<AbstractTrommelSort> getAllTrommelSort() {
        if (alleTrommelSortierer == null) {
            alleTrommelSortierer = new HashMap<String, AbstractTrommelSort>();
            alleTrommelSortierer.put(TrommelIntelligentSort.class.getName(), new TrommelIntelligentSort());
            alleTrommelSortierer.put(TrommelDatumSort.class.getName(), new TrommelDatumSort());
            alleTrommelSortierer.put(TrommelFuellstand.class.getName(), new TrommelFuellstand());
            alleTrommelSortierer.put(TrommelNummerSort.class.getName(), new TrommelNummerSort());
        }
        return alleTrommelSortierer.values();
    }
}
