package de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB;


import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.*;

import java.io.File;
import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper für eine HSQLDB
 */
public class HSQLDBWrapper extends UnicastRemoteObject implements IDBWrapper {
    private final String path;
    private Statement stmnt = null;

    public HSQLDBWrapper(String path) throws ClassNotFoundException, SQLException, OnlyOneUserExeption, IOException {
        File lck = new File(path + "lock.lck");
        if (lck.exists()) {
            throw new OnlyOneUserExeption();
        } else {
            lck.createNewFile();
            lck.deleteOnExit();
        }
        this.path = path;
        Statement stmnt = getStatement();

        try {
            stmnt.execute("SET WRITE_DELAY 0;");
            stmnt.executeQuery("Select * FROM kabeltyp;");
            stmnt.executeQuery("Select * FROM trommel;");
            stmnt.executeQuery("Select * FROM strecke;");

            //Update zu Lagerplätze
            try {
                stmnt.execute("ALTER TABLE trommel ADD lagerplatz VARCHAR(32);");
            } catch (SQLException e) {
            }
            //Update zu Lagerplätze
            try {
                stmnt.execute("ALTER TABLE trommel ADD kabelstart INTEGER;");
            } catch (SQLException e) {
            }
            //Update zu freimeldung
            try {
                stmnt.execute("ALTER TABLE trommel ADD freigemeldet BOOLEAN;");
            } catch (SQLException e) {
            }
        } catch (SQLException e) {
            // Create DB
            stmnt.execute("create table kabeltyp(materialnummer integer not null PRIMARY KEY , typ VARCHAR (64) );");

            stmnt.execute("CREATE TABLE lieferant(hid IDENTITY,name VARCHAR(64) );");

            stmnt.execute("create table trommel(id IDENTITY, materialnummer integer not null, trommelnummer VARCHAR(64) NOT NULL, gesamtlaenge INTEGER,lagerplatz VARCHAR(32),kabelstart INTEGER ,  FOREIGN KEY(materialnummer) REFERENCES kabeltyp(materialnummer) ); ");

            stmnt.execute("CREATE TABLE geliefert(lid IDENTITY,hid INTEGER, id INTEGER, datum BIGINT,lieferschein VARCHAR(64), FOREIGN KEY(hid) REFERENCES lieferant(hid) , FOREIGN KEY(id) REFERENCES trommel(id));");

            stmnt.execute("create TABLE strecke(sid IDENTITY, trommelid integer not null, ba INTEGER, ort VARCHAR(64), verlegedatum BIGINT , start INTEGER , ende INTEGER , FOREIGN KEY(trommelid) REFERENCES trommel(id));");
        }
//        try {
//            java.rmi.registry.LocateRegistry.createRegistry(1099);
//            Naming.rebind("HSQLDBWrapper",this);
//            System.out.println("RMI registry ready.");
//            IDBWrapper w = (IDBWrapper) Naming.lookup("rmi://127.0.0.1:1099/HSQLDBWrapper");
//            System.out.println(w.getAllKabeltypen().get(0).toString());
//        } catch (Exception e) {
//            System.out.println("Exception starting RMI registry:");
//            e.printStackTrace();
//        }
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

    Statement getStatement() {
        if (stmnt == null) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                Connection con = DriverManager.getConnection("jdbc:hsqldb:file:" + path + "jKabeltrommelHSQLDB;shutdown=true", "sa", "");
                stmnt = con.createStatement();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return stmnt;
    }

    @Override
    public List<IKabeltypE> getAllKabeltypen() {
        ArrayList<IKabeltypE> list = new ArrayList<>();
        try {
            Statement stmnt = getStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM kabeltyp;");
            while (rs.next()) {
                list.add(new KabeltypE(rs.getString(2), rs.getInt(1)));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ITrommelE> getTrommelnForTyp(IKabeltypE kabeltyp) {
        ArrayList<ITrommelE> list = new ArrayList<>();
        try {
            Statement stmnt = getStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM kabeltyp JOIN trommel ON kabeltyp.materialnummer = trommel.materialnummer Where kabeltyp.materialnummer = " + kabeltyp.getMaterialNummer() + ";");
            while (rs.next()) {
                list.add(new TrommelE(kabeltyp, rs.getInt("id"), rs.getString("trommelnummer"), rs.getInt("gesamtlaenge"), rs.getString("lagerplatz"), rs.getInt("kabelstart"), rs.getBoolean("freigemeldet")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    @Override
    public List<IStreckeE> getStreckenForTrommel(ITrommelE trommel) {
        ArrayList<IStreckeE> list = new ArrayList<>();
        try {
            Statement stmnt = getStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM trommel JOIN strecke ON strecke.trommelid = trommel.id Where strecke.trommelid = " + trommel.getId() + ";");
//            System.out.println(getResultSetAsStringTable(rs));
//                System.out.println(getResultSetAsStringTable(rs));
            while (rs.next()) {
                //KabeltypE kabelTyp, int id, String trommelnummer, long date, int gesamtlaenge
                list.add(new StreckeE(rs.getInt("sid"), rs.getInt("ba"), rs.getString("ort"), rs.getLong("verlegedatum"), rs.getInt("start"), rs.getInt("ende"), trommel));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ILieferantE> getAllLieferanten() {
        ArrayList<ILieferantE> list = new ArrayList<>();
        try {
            Statement stmnt = getStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM lieferant;");
            while (rs.next()) {
                list.add(new LieferantE(rs.getInt("hid"), rs.getString("name")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<String> getAllTexteForBA(int ba) {
        ArrayList<String> list = new ArrayList<>();
        try {
            Statement stmnt = getStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM strecke Where ba= " + ba + ";");
            while (rs.next()) {
                //KabeltypE kabelTyp, int id, String trommelnummer, long date, int gesamtlaenge
                list.add(rs.getString("ort"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public IGeliefertE getLiefer(ITrommelE trommel) {
        try {
            ResultSet rs = getStatement().executeQuery("SELECT * FROM geliefert JOIN trommel ON trommel.id = geliefert.id WHERE id =" + trommel.getId() + ";");
            if (rs.next()) {
                ILieferantE l = getLieferantByID(rs.getInt("hid"));
                return new GeliefertE(rs.getLong("datum"), rs.getString("lieferschein"), l, trommel);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ILieferantE getLieferantByID(int id) {
        try {
            ResultSet rs = getStatement().executeQuery("SELECT * FROM lieferant WHERE hid =" + id + ";");
            if (rs.next()) {
                return new LieferantE(rs.getInt("hid"), rs.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LieferantE getLieferant(IGeliefertE liefert) {
        try {
            ResultSet rs = getStatement().executeQuery("SELECT * FROM geliefert JOIN lieferant ON lieferant.hid = geliefert.hid WHERE hid =" + liefert.getLieferant().getId() + ";");
            if (rs.next()) {
                return new LieferantE(rs.getInt("lid"), rs.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public KabeltypE getTyp(ITrommelE trommel) {
        try {
            ResultSet rs = getStatement().executeQuery("SELECT * FROM trommel JOIN kabeltyp ON kabeltyp.materialnummer = trommel.materialnummer WHERE id =" + trommel.getId() + ";");
            if (rs.next()) {
                return new KabeltypE(rs.getInt("materialnummer"), rs.getString("typ"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean execute(String ex) {
        try {
            getStatement().executeUpdate(ex);
        } catch (SQLException e) {
            e.printStackTrace();
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
        boolean out = true;
        try {
            ResultSet rs = getStatement().executeQuery("insert into trommel(id, materialnummer,trommelnummer,gesamtlaenge,lagerplatz,kabelstart) VALUES(NULL," + kabelTyp.getMaterialNummer() + ",'" + trommelnummer + "', " + gesamtlaenge + ",'" + lagerPlatz + "'," + start + "); CALL IDENTITY()");
            rs.next();
            int trommelID = rs.getInt(1);

            //  geliefert(lid IDENTITY,hid INTEGER, id INTEGER, datum BIGINT,lieferschein VARCHAR(64),
            out = execute("INSERT INTO geliefert(lid,hid,id,datum,lieferschein) VALUES(NULL," + lieferantE.getId() + "," + trommelID + "," + lieferdatum + ",'" + lieferscheinNr + "' );");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return out;
        }
        return true;
    }

    @Override
    public boolean remove(IStreckeE strecke) {
        return execute("DELETE FROM strecke WHERE sid=" + strecke.getId() + ";");
    }

    @Override
    public boolean createLieferant(String name) {
        return execute("INSERT INTO lieferant(hid,name) VALUES(NULL,'" + name + "')");
    }

    @Override
    public boolean update(ILieferantE lieferantE) {
        return execute("UPDATE lieferant SET name='" + lieferantE.getName() + "' WHERE hid=" + lieferantE.getId() + ";");
    }
}
