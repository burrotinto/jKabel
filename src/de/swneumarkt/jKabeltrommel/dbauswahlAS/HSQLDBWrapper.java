package de.swneumarkt.jKabeltrommel.dbauswahlAS;


import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper für eine HSQLDB
 */
class HSQLDBWrapper implements IDBWrapper {
    private Connection con;
    private Statement stmnt;

    HSQLDBWrapper(String path) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        con = DriverManager.getConnection("jdbc:hsqldb:file:" + path + "jKabeltrommelHSQLDB;shutdown=true", "sa", "");
        stmnt = con.createStatement();

        try {
            stmnt.execute("SET WRITE_DELAY 0;");
            stmnt.executeQuery("Select * FROM kabeltyp;");
            stmnt.executeQuery("Select * FROM trommel;");
            stmnt.executeQuery("Select * FROM strecke;");

            //Update zu Lagerplätze
            try{
                stmnt.execute("ALTER TABLE trommel ADD lagerplatz VARCHAR(32);");
            } catch ( SQLException e){
            }
        } catch (SQLException e) {
            // Create DB
            stmnt.execute("create table kabeltyp(materialnummer integer not null PRIMARY KEY , typ VARCHAR (64) );");

            stmnt.execute("CREATE TABLE lieferant(hid IDENTITY,name VARCHAR(64) );");

            stmnt.execute("create table trommel(id IDENTITY, materialnummer integer not null, trommelnummer VARCHAR(64) NOT NULL, gesamtlaenge INTEGER,lagerplatz VARCHAR(32),  FOREIGN KEY(materialnummer) REFERENCES kabeltyp(materialnummer) ); ");

            stmnt.execute("CREATE TABLE geliefert(lid IDENTITY,hid INTEGER, id INTEGER, datum BIGINT,lieferschein VARCHAR(64), FOREIGN KEY(hid) REFERENCES lieferant(hid) , FOREIGN KEY(id) REFERENCES trommel(id));");

            stmnt.execute("create TABLE strecke(sid IDENTITY, trommelid integer not null, ba INTEGER, ort VARCHAR(64), verlegedatum BIGINT , start INTEGER , ende INTEGER , FOREIGN KEY(trommelid) REFERENCES trommel(id));");
        }
    }

    @Override
    public List<KabeltypE> getAllKabeltypen() {
        ArrayList<KabeltypE> list = new ArrayList<>();
        try {
            Statement stmnt = con.createStatement();
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
    public List<TrommelE> getTrommelnForTyp(KabeltypE kabeltyp) {
        ArrayList<TrommelE> list = new ArrayList<>();
        try {
            Statement stmnt = con.createStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM kabeltyp JOIN trommel ON kabeltyp.materialnummer = trommel.materialnummer Where kabeltyp.materialnummer = " + kabeltyp.getMaterialNummer() + ";");
            while (rs.next()) {
                list.add(new TrommelE(kabeltyp, rs.getInt("id"), rs.getString("trommelnummer"), rs.getInt("gesamtlaenge"),rs.getString("lagerplatz")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    @Override
    public List<StreckeE> getStreckenForTrommel(TrommelE trommel) {
        ArrayList<StreckeE> list = new ArrayList<>();
        try {
            Statement stmnt = con.createStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM trommel JOIN strecke ON strecke.trommelid = trommel.id Where strecke.trommelid = " + trommel.getId() + ";");
//            System.out.println(getResultSetAsStringTable(rs));
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
    public List<LieferantE> getAllLieferanten() {
        ArrayList<LieferantE> list = new ArrayList<>();
        try {
            Statement stmnt = con.createStatement();
            ResultSet rs = stmnt.executeQuery("Select * FROM lieferant;");
            while (rs.next()) {
                list.add(new LieferantE(rs.getInt("hid"),rs.getString("name")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public GeliefertE getLiefer(TrommelE trommel) {
        try {
            ResultSet rs = stmnt.executeQuery("SELECT * FROM geliefert JOIN trommel ON trommel.id = geliefert.id WHERE id ="+ trommel.getId()+";");
            if(rs.next()){
                return new GeliefertE(rs.getLong("datum"),rs.getString("lieferschein"),rs.getInt("hid"),rs.getInt("id"));
            } else {
                return  null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LieferantE getLieferant(GeliefertE liefert) {
        try {
            ResultSet rs = stmnt.executeQuery("SELECT * FROM geliefert JOIN lieferant ON lieferant.hid = geliefert.hid WHERE hid ="+ liefert.getLieferantID()+";");
//            ResultSet rs = stmnt.executeQuery("SELECT * FROM (geliefert JOIN lieferant ON lieferant.hid = geliefert.hid) JOIN trommel on trommel.id == geliefert.id WHERE hid ="+ liefert.getLieferantID()+";");
            if(rs.next()){
                return new LieferantE(rs.getInt("lid"),rs.getString("name"));
            } else {
                return  null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public KabeltypE getTyp(TrommelE trommel) {
        try {
            ResultSet rs = stmnt.executeQuery("SELECT * FROM trommel JOIN kabeltyp ON kabeltyp.materialnummer = trommel.materialnummer WHERE id ="+ trommel.getId()+";");
            if(rs.next()){
                return new KabeltypE(rs.getInt("materialnummer"),rs.getString("typ"));
            } else {
                return  null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private boolean execute(String ex) {
        try {
            stmnt.executeUpdate(ex);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(KabeltypE kabeltyp) {
        return execute("UPDATE kabeltyp SET typ='" + kabeltyp.getTyp() + "' WHERE materialnummer=" + kabeltyp.getMaterialNummer() + ";");
    }

    @Override
    public boolean create(KabeltypE kabeltyp) {
        return execute("INSERT INTO kabeltyp (materialnummer, typ) VALUES(" + kabeltyp.getMaterialNummer() + ", '" + kabeltyp.getTyp() + "');");
    }

    @Override
    public boolean update(StreckeE strecke) {
        return execute("UPDATE strecke SET ba=" + strecke.getBa() + ",ort='" + strecke.getOrt() + "',start=" + strecke.getStart() + ",ende=" + strecke.getEnde() + " WHERE sid=" + strecke.getId() + ";");
    }

    @Override
    public boolean create(StreckeE strecke) {
        return execute("INSERT INTO strecke(sid, trommelid, ba, ort,verlegedatum,start,ende) VALUES(NULL," + strecke.getTrommelID() + "," + strecke.getBa() + ",'" + strecke.getOrt() + "'," + strecke.getVerlegedatum() + "," + strecke.getStart() + "," + strecke.getEnde() + " )");
    }

    @Override
    public boolean update(TrommelE trommel) {
        // materialnummer integer not null, trommelnummer VARCHAR(64) NOT NULL, gesamtlaenge INTEGER, lieferdatum BIGINT,
        return execute("UPDATE trommel SET trommelnummer='" + trommel.getTrommelnummer() + "',gesamtlaenge=" + trommel.getGesamtlaenge() + ",lagerplatz='"+trommel.getLagerPlatz()+"' WHERE id=" + trommel.getId() + ";");
    }

    @Override
    public boolean create(TrommelE trommel, LieferantE lieferant,GeliefertE geliefert) {
        //TODO geliefert
        System.out.println(trommel.toString());
        boolean out = true;
        try {
            ResultSet rs = stmnt.executeQuery("insert into trommel(id, materialnummer,trommelnummer,gesamtlaenge,lagerplatz) VALUES(NULL," + trommel.getMaterialNummer() + ",'" + trommel.getTrommelnummer() + "', " + trommel.getGesamtlaenge() + ",'"+trommel.getLagerPlatz() + "'); CALL IDENTITY()");
            rs.next();
            int trommelID = rs.getInt(1);

            //  geliefert(lid IDENTITY,hid INTEGER, id INTEGER, datum BIGINT,lieferschein VARCHAR(64),
            out = execute("INSERT INTO geliefert(lid,hid,id,datum,lieferschein) VALUES(NULL,"+lieferant.getId()+","+trommelID+","+geliefert.getDatum()+",'"+geliefert.getLieferscheinNr()+"');");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return out;
        }
    return true;
    }

    @Override
    public boolean remove(StreckeE strecke) {
        return execute("DELETE FROM strecke WHERE sid=" + strecke.getId() + ";");
    }

    @Override
    public boolean create(LieferantE lieferant) {
        return execute("INSERT INTO lieferant(hid,name) VALUES(NULL,'"+ lieferant.getName() +"')");
    }

    @Override
    public boolean update(LieferantE lieferantE) {
        return execute("UPDATE lieferant SET name='" + lieferantE.getName() + "' WHERE hid="+ lieferantE.getId()+";" );
    }

    public static String getResultSetAsStringTable(ResultSet rs) throws SQLException {
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
    }
}
