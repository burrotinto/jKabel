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

package de.burrotinto.jKabel.tests;

import de.burrotinto.jKabel.dbauswahlAS.HSQLDB.HSQLDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.IKabeltypE;
import de.burrotinto.jKabel.dbauswahlAS.enitys.ILieferantE;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by derduke on 22.06.16.
 */
public class HSQLDBWrapperTest {

    private HSQLDBWrapper getWrapper() {
        HSQLDBWrapper client = new HSQLDBWrapper("" + new Random().nextLong());
        return client;
    }

    @Test
    public void createKabeltyp() throws Exception {
        HSQLDBWrapper db = getWrapper();
        assertTrue(db.createKabeltyp("Kabel 1", 123456));
        assertTrue(db.createKabeltyp("Kabel 2", 123457));
        assertTrue(db.createKabeltyp("Kabel 3", 123458));
        assertTrue("Gleiche Namen erlaubt", db.createKabeltyp("Kabel 1", 123459));
        assertTrue("Kabeltyp schon enthalten", !db.createKabeltyp("Kabel 1", 123456));
        assertTrue("Materialnummer schon enthalten", !db.createKabeltyp("Kabel 4", 123456));
    }

    @Test
    public void createLieferant() throws Exception {
        HSQLDBWrapper db = getWrapper();
        assertTrue(db.createLieferant("L1"));
        assertTrue(db.createLieferant("L2"));
        assertTrue(db.createLieferant("l1"));
        assertTrue("Lieferant darf nicht  enthalten sein", !db.createLieferant("L1"));
    }

    @Test
    public void createTrommel() throws Exception {
        HSQLDBWrapper db = getWrapper();
        db.createKabeltyp("Kabel 1", 123456);
        db.createLieferant("HANS");
        IKabeltypE kabeltypE = db.getAllKabeltypen().get(0);
        ILieferantE lieferant = db.getAllLieferanten().get(0);
        ILieferantE fakeLiefer = new ILieferantE() {
            @Override
            public int getId() {
                return 50;
            }

            @Override
            public String getName() {
                return "HANS";
            }

            @Override
            public void setName(String name) {

            }
        };
        IKabeltypE fakeTyp = new IKabeltypE() {
            @Override
            public String getTyp() {
                return "ADA";
            }

            @Override
            public void setTyp(String typ) {

            }

            @Override
            public int getMaterialNummer() {
                return 42;
            }
        };
        assertTrue(db.createTrommel(kabeltypE, "123", 500, "PL 00-00-00", 500, lieferant, System.currentTimeMillis(), "LA12345"));
        assertTrue("Lieferant gibt es nicht", !db.createTrommel(kabeltypE, "123", 500, "PL 00-00-00", 500, fakeLiefer, System.currentTimeMillis(), "LA12346"));
        assertTrue("Kabeltyp gibt es nicht", !db.createTrommel(fakeTyp, "123", 500, "PL 00-00-00", 500, lieferant, System.currentTimeMillis(), "LA12347"));
        assertTrue("Laenge muss >= 0 sein", !db.createTrommel(kabeltypE, "123", -1, "PL 00-00-00", 500, lieferant, System.currentTimeMillis(), "LA12347"));
        assertTrue("NULL nicht erlaubt", !db.createTrommel(null, null, 10, null, 500, null, System.currentTimeMillis(), null));
    }

    @Test
    public void getAllKabeltypen() throws Exception {
        HSQLDBWrapper db = getWrapper();
        assertTrue("Leere DB nicht erkannt", db.getAllKabeltypen().size() == 0);
        db.createKabeltyp("Kabel 1", 123456);
        db.createKabeltyp("Kabel 2", 123457);
        db.createKabeltyp("Kabel 3", 123458);
        assertTrue(db.getAllKabeltypen().size() == 3);
        assertTrue(!db.getAllKabeltypen().get(0).equals(db.getAllKabeltypen().get(1)));
        assertTrue(!db.getAllKabeltypen().get(1).equals(db.getAllKabeltypen().get(2)));
        assertTrue(!db.getAllKabeltypen().get(2).equals(db.getAllKabeltypen().get(0)));

    }

    @Test
    public void getTrommelnForTyp() throws Exception {
        HSQLDBWrapper db = getWrapper();
        db.createKabeltyp("Kabel 1", 123456);
        db.createKabeltyp("Kabel 2", 123457);
        db.createKabeltyp("Kabel 3", 123458);

    }

    @Test
    public void getStreckenForTrommel() throws Exception {

    }

    @Test
    public void getAllLieferanten() throws Exception {

    }

    @Test
    public void getAllTexteForBA() throws Exception {

    }

    @Test
    public void getLiefer() throws Exception {

    }

    @Test
    public void getLieferant() throws Exception {

    }

    @Test
    public void getTyp() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void update1() throws Exception {

    }


    @Test
    public void update2() throws Exception {

    }

    @Test
    public void update3() throws Exception {

    }


    @Test
    public void update4() throws Exception {

    }

    @Test
    public void createStrecke() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

}