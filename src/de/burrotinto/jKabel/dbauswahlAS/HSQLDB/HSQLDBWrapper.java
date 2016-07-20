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

package de.burrotinto.jKabel.dbauswahlAS.HSQLDB;


import de.burrotinto.jKabel.dbauswahlAS.IDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.enitys.*;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Wrapper für eine HSQLDB startet gleichzeitig als Server und setzt eine Sperrdatei in das Verzeichniss der DB.
 */
public class HSQLDBWrapper implements IDBWrapper {
    private static Logger log = Logger.getLogger(HSQLDBWrapper.class);
    protected Statement stmnt = null;
    private String connectionString;

    /**
     * Wrapper verbindet sich mitt einer "Mem" Datenbank. Somit ist dies nicht persistent
     */
    public HSQLDBWrapper(String memDBname) {
        connectionString = "jdbc:hsqldb:mem:" + memDBname + ";shutdown=true;default_schema=true;";
        getStatement();
        try {
            initDB();
        } catch (SQLException e) {
            log.error(e.getStackTrace());

        }
    }

    /**
     * Verbindet sich mit einem HypersqlDB server.
     *
     * @param ip
     * @throws SQLException
     */
    public HSQLDBWrapper(InetAddress ip) throws SQLException {
        connectionString = "jdbc:hsqldb:hsql://" + ip.getHostAddress() + "/" + HSQLDBServer.DBPREFIX + ";shutdown=true;default_schema=true;";
        if (getStatement() == null) throw new SQLException();
        initDB();
    }

    public static String getResultSetAsStringTable(ResultSet rs) throws SQLException {
        if (rs != null) {
            StringBuilder sb = new StringBuilder();
            ResultSetMetaData md = rs.getMetaData();
            //Tabellen Kopf erzeugen
            for (int i = 1; i <= md.getColumnCount(); i++) {
                sb.append(md.getTableName(i) + "." + md.getColumnName(i) + " || ");
            }
            sb.append("\n");
            // Tabbeleneintäge
            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    sb.append(rs.getString(i) + " || ");
                }
                sb.append("\n");
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    private void initDB() throws SQLException {
        try {
            stmnt.execute("SET WRITE_DELAY 0;");
            stmnt.executeQuery("Select * FROM kabeltyp;");
            stmnt.executeQuery("Select * FROM trommel;");
            stmnt.executeQuery("Select * FROM strecke;");

        } catch (SQLException e) {
            log.warn("Create DB");
            // Create DB
            stmnt.execute("create table kabeltyp(materialnummer integer not null PRIMARY KEY , typ VARCHAR (64) );");

            stmnt.execute("CREATE TABLE lieferant(hid IDENTITY,name VARCHAR(64) );");

            stmnt.execute("create table trommel(id IDENTITY, materialnummer integer not null, trommelnummer VARCHAR(64) NOT NULL, gesamtlaenge INTEGER,lagerplatz VARCHAR(32),kabelstart INTEGER ,  FOREIGN KEY(materialnummer) REFERENCES kabeltyp(materialnummer) ); ");

            stmnt.execute("CREATE TABLE geliefert(lid IDENTITY,hid INTEGER, id INTEGER, datum BIGINT,lieferschein VARCHAR(64), FOREIGN KEY(hid) REFERENCES lieferant(hid) , FOREIGN KEY(id) REFERENCES trommel(id));");

            stmnt.execute("create TABLE strecke(sid IDENTITY, trommelid integer not null, ba INTEGER, ort VARCHAR(64), verlegedatum BIGINT , start INTEGER , ende INTEGER , FOREIGN KEY(trommelid) REFERENCES trommel(id));");
        }
    }

    protected Statement getStatement() {
        if (stmnt == null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                Connection con = DriverManager.getConnection(connectionString, "sa", "");
                stmnt = con.createStatement();
                log.info("Create Statement - " + stmnt.toString());
            } catch (Exception e) {
                e.printStackTrace();
                stmnt = null;
            }
        }

        return stmnt;
    }

    private ResultSet executeQuery(String query) throws SQLException {
        log.info(query);
        return stmnt.executeQuery(query);
    }

    @Override
    public List<IKabeltypE> getAllKabeltypen() {

        ArrayList<IKabeltypE> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("Select * FROM kabeltyp;");
            while (rs.next()) {
                list.add(new KabeltypE(rs.getString(2), rs.getInt(1), this));
            }
            rs.close();
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
        }

        return list;
    }


    List<ITrommelE> getTrommelnForTyp(IKabeltypE kabeltyp) {
        ArrayList<ITrommelE> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("Select * FROM kabeltyp JOIN trommel ON kabeltyp.materialnummer = trommel.materialnummer Where kabeltyp.materialnummer = " + kabeltyp.getMaterialNummer() + ";");
            while (rs.next()) {
                TrommelE trommel = new TrommelE(kabeltyp, rs.getInt("id"), rs.getString("trommelnummer"), rs.getInt("gesamtlaenge"), rs.getString("lagerplatz"), rs.getInt("kabelstart"), rs.getBoolean("freigemeldet"), this);
                log.info("getTrommelnForTyp() - " + kabeltyp + " " + trommel);
                list.add(trommel);
            }
            rs.close();
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
        }
        return list;

    }


    List<IStreckeE> getStreckenForTrommel(ITrommelE trommel) {
        ArrayList<IStreckeE> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("Select * FROM trommel JOIN strecke ON strecke.trommelid = trommel.id Where strecke.trommelid = " + trommel.getId() + ";");
            while (rs.next()) {
                list.add(new StreckeE(rs.getInt("sid"), rs.getInt("ba"), rs.getString("ort"), rs.getLong("verlegedatum"), rs.getInt("start"), rs.getInt("ende"), trommel));
            }
            rs.close();
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ILieferantE> getAllLieferanten() {
        ArrayList<ILieferantE> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("Select * FROM lieferant;");
            while (rs.next()) {
                list.add(new LieferantE(rs.getInt("hid"), rs.getString("name")));
            }
            rs.close();
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<String> getAllTexteForBA(int ba) {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet rs = executeQuery("Select * FROM strecke Where ba= " + ba + ";");
            while (rs.next()) {
                list.add(rs.getString("ort"));
            }
            rs.close();
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public IKabeltypE getTypByMaterialnummer(int materialNummer) {
        try {
            ResultSet rs = executeQuery("Select * FROM kabeltyp Where materialnummer= " + materialNummer + ";");
            if (!rs.next()) {
                return null;
            } else {
                return new KabeltypE(rs.getString("typ"), rs.getInt("materialnummer"), this);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ITrommelE getTrommelByID(int id) {
        try {
            ResultSet rs = executeQuery("Select * FROM kabeltyp JOIN trommel ON kabeltyp.materialnummer = trommel.materialnummer Where trommel.id = " + id + ";");
            if (!rs.next()) {
                return null;
            } else {
                return new TrommelE(new KabeltypE(rs.getString("typ"), rs.getInt("materialnummer"), this), rs.getInt("id"), rs.getString("trommelnummer"), rs.getInt("gesamtlaenge"), rs.getString("lagerplatz"), rs.getInt("kabelstart"), rs.getBoolean("freigemeldet"), this);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    IGeliefertE getLiefer(ITrommelE trommel) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM geliefert JOIN trommel ON trommel.id = geliefert.id WHERE id =" + trommel.getId() + ";");
            if (rs.next()) {
                return new GeliefertE(rs.getLong("datum"), rs.getString("lieferschein"), getLieferantByID(rs.getInt("hid")), trommel);
            } else {
                return null;
            }
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
            return null;
        }
    }

    private ILieferantE getLieferantByID(int id) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM lieferant WHERE hid =" + id + ";");
            if (rs.next()) {
                return new LieferantE(rs.getInt("hid"), rs.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
            return null;
        }
    }


//    LieferantE getLieferant(IGeliefertE liefert) {
//        try {
//            ResultSet rs = executeQuery("SELECT * FROM geliefert JOIN lieferant ON lieferant.hid = geliefert.hid WHERE hid =" + liefert.getLieferant().getId() + ";");
//            if (rs.next()) {
//                return new LieferantE(rs.getInt("lid"), rs.getString("name"));
//            } else {
//                return null;
//            }
//        } catch (SQLException e) {
//            stmnt = null;
//            e.printStackTrace();
//            return null;
//        }
//    }


    KabeltypE getTyp(ITrommelE trommel) {
        try {
            ResultSet rs = executeQuery("SELECT * FROM trommel JOIN kabeltyp ON kabeltyp.materialnummer = trommel.materialnummer WHERE id =" + trommel.getId() + ";");
            if (rs.next()) {
                return new KabeltypE(rs.getString("typ"), rs.getInt("materialnummer"), this);
            } else {
                return null;
            }
        } catch (SQLException e) {
            stmnt = null;
            e.printStackTrace();
            return null;
        }
    }

    private boolean execute(String ex) {
        try {
            log.info(ex);
            getStatement().executeUpdate(ex);
        } catch (SQLException e) {
            stmnt = null;
            return false;
        }
        return true;
    }

    @Override
    public boolean update(IKabeltypE kabeltyp) {
        return execute("UPDATE kabeltyp SET typ='" + kabeltyp.getTyp() + "' WHERE materialnummer=" + kabeltyp.getMaterialNummer() + ";");
    }

    @Override
    public boolean createKabeltyp(String name, int materialnummer) {
        return execute("INSERT INTO kabeltyp (materialnummer, typ) VALUES(" + materialnummer + ", '" + name + "');");
    }

    @Override
    public boolean update(IStreckeE strecke) {
        return execute("UPDATE strecke SET ba=" + strecke.getBa() + ",ort='" + strecke.getOrt() + "',start=" + strecke.getStart() + ",ende=" + strecke.getEnde() + " WHERE sid=" + strecke.getId() + ";");
    }

    @Override
    public boolean createStrecke(int ba, String ort, long verlegedatum, int start, int ende, ITrommelE trommel) {
        return execute("INSERT INTO strecke(sid, trommelid, ba, ort,verlegedatum,start,ende) VALUES(NULL," + trommel.getId() + "," + ba + ",'" + ort + "'," + verlegedatum + "," + start + "," + ende + " )");
    }

    @Override
    public boolean update(ITrommelE trommel) {
        return execute("UPDATE trommel SET trommelnummer='" + trommel.getTrommelnummer() + "',gesamtlaenge=" + trommel.getGesamtlaenge() + ",lagerplatz='" + trommel.getLagerPlatz() + "', kabelstart=" + trommel.getStart() + ",freigemeldet=" + trommel.isFreigemeldet() + "  WHERE id=" + trommel.getId() + ";");
    }

    public boolean update(IGeliefertE geliefert) {
        execute("UPDATE geliefert SET hid=" + geliefert.getLieferant().getId() + " WHERE id=" + geliefert.getTrommel().getId() + ";");
        return true;
    }

    @Override
    public boolean createTrommel(IKabeltypE kabelTyp, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start, ILieferantE lieferantE, long lieferdatum, String lieferscheinNr) {
        try {
            if (!(kabelTyp != null && trommelnummer != null && lagerPlatz != null && lieferantE != null && lieferscheinNr != null && gesamtlaenge >= 0)) {
                log.debug("create Trommel nicht möglich NULL werte ");
                return false;
            } else {
                ResultSet liefer = executeQuery("SELECT * FROM lieferant WHERE hid=" + lieferantE.getId() + " AND name='" + lieferantE.getName() + "';");
                // Lieferant nicht enthalten
                if (!liefer.next()) {
                    log.debug("Lieferant nicht enthalten - " + lieferantE);
                    return false;
                } else {
                    liefer.close();
                    ResultSet rs = executeQuery("insert into trommel(id, materialnummer,trommelnummer,gesamtlaenge,lagerplatz,kabelstart) VALUES(NULL," + kabelTyp.getMaterialNummer() + ",'" + trommelnummer + "', " + gesamtlaenge + ",'" + lagerPlatz + "'," + start + "); CALL IDENTITY()");
                    if (!rs.next()) {
                        return false;
                    } else {
                        execute("INSERT INTO geliefert(lid,hid,id,datum,lieferschein) VALUES(NULL," + lieferantE.getId() + "," + rs.getInt(1) + "," + lieferdatum + ",'" + lieferscheinNr + "' );");
                    }
                    rs.close();
                }
            }
        } catch (SQLException e) {
            stmnt = null;
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(IStreckeE strecke) {
        return execute("DELETE FROM strecke WHERE sid=" + strecke.getId() + ";");
    }

    @Override
    public boolean createLieferant(String name) {

        try {
            ResultSet rs = executeQuery("SELECT * FROM lieferant WHERE name='" + name + "' ;");
            // Name bereits enthalten
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
        return execute("INSERT INTO lieferant(hid,name) VALUES(NULL,'" + name + "')");
    }

    @Override
    public boolean update(ILieferantE lieferantE) {
        return execute("UPDATE lieferant SET name='" + lieferantE.getName() + "' WHERE hid=" + lieferantE.getId() + ";");
    }

    @Override
    public boolean isClosed() {
        try {
            return stmnt == null || DriverManager.getConnection(connectionString, "sa", "").isClosed();
        } catch (SQLException e) {
            return true;
        }
    }


}
