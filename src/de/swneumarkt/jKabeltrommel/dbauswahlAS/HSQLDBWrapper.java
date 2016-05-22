package de.swneumarkt.jKabeltrommel.dbauswahlAS;


import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.StreckeE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derduke on 19.05.2016.
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
        } catch (SQLException e) {
            // Create DB
            stmnt.execute("create table kabeltyp(materialnummer integer not null PRIMARY KEY , typ VARCHAR (64) );");
            stmnt.execute("create table trommel(id IDENTITY, materialnummer integer not null, trommelnummer VARCHAR(64) NOT NULL, gesamtlaenge INTEGER, lieferdatum BIGINT, FOREIGN KEY(materialnummer) REFERENCES kabeltyp(materialnummer) ); ");
            stmnt.execute("create TABLE strecke(id IDENTITY, trommelid integer not null, ba INTEGER, ort VARCHAR(64), verlegedatum BIGINT , start INTEGER , ende INTEGER , FOREIGN KEY(trommelid) REFERENCES trommel(id));");
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
            ResultSet rs = stmnt.executeQuery("Select id,trommelnummer, gesamtlaenge, lieferdatum FROM kabeltyp JOIN trommel ON kabeltyp.materialnummer = trommel.materialnummer Where kabeltyp.materialnummer = " + kabeltyp.getMaterialNummer() + ";");
            while (rs.next()) {
                //KabeltypE kabelTyp, int id, String trommelnummer, long date, int gesamtlaenge
                list.add(new TrommelE(kabeltyp, rs.getInt("id"), rs.getString("trommelnummer"), rs.getLong("lieferdatum"), rs.getInt("gesamtlaenge")));
            }
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
            while (rs.next()) {
                //KabeltypE kabelTyp, int id, String trommelnummer, long date, int gesamtlaenge
                list.add(new StreckeE(rs.getInt("strecke.id") , rs.getInt("strecke.ba"),rs.getString("strecke.ort"), rs.getLong("strecke.verlegedatum"),  rs.getInt("strecke.start"),  rs.getInt("strecke.ende"),trommel));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
        //!TODO
        return false;
    }

    @Override
    public boolean create(StreckeE strecke) {
        return execute("INSERT INTO strecke(id, trommelid, ba, ort,verlegedatum,start,ende) VALUES(NULL," + strecke.getTrommelID() + "," + strecke.getBa() + ",'" + strecke.getOrt() + "'," + strecke.getVerlegedatum() + "," + strecke.getStart() + "," + strecke.getEnde() + " )");
    }

    @Override
    public boolean update(TrommelE trommel) {
//!TODO
        return false;
    }

    @Override
    public boolean create(TrommelE trommel) {
        return execute("insert into trommel(id, materialnummer,trommelnummer,gesamtlaenge,lieferdatum) VALUES(NULL," + trommel.getMaterialNummer() + ",'" + trommel.getTrommelnummer() + "', " + trommel.getGesamtlaenge() + "," + trommel.getDate() + "); ");

    }
}
