package de.swneumarkt.jKabeltrommel.dbauswahlAS;


import de.swneumarkt.jKabeltrommel.entytis.KabeltypE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derduke on 19.05.2016.
 */
class HSQLDBWrapper implements IDBWrapper {
    Connection con = null;

    HSQLDBWrapper(String path) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        con = DriverManager.getConnection("jdbc:hsqldb:file:" + path + "jKabeltrommelHSQLDB;shutdown=true", "sa", "");
        Statement stmnt = con.createStatement();

        try {
            stmnt.execute("SET WRITE_DELAY 0;");
            stmnt.executeQuery("Select * FROM kabeltyp;");
            stmnt.executeQuery("Select * FROM trommel;");
            stmnt.executeQuery("Select * FROM strecke;");
        } catch (SQLException e) {
            e.printStackTrace();
            // Create DB
            stmnt.execute("create table kabeltyp(materialnummer integer not null PRIMARY KEY , typ VARCHAR (64) );");
            stmnt.execute("create table trommel(id IDENTITY, trommelnummer VARCHAR(64) NOT NULL, gesamtlaenge INTEGER, lieferdatum DATE); ");
            stmnt.execute("create TABLE strecke(id IDENTITY, ba INTEGER, ort VARCHAR(64), verlegedatum DATE , start INTEGER , ende INTEGER );");
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
    public void update(KabeltypE kabeltyp) {
        try {
            Statement stmnt = con.createStatement();
            stmnt.executeUpdate("UPDATE kabeltyp SET typ='" + kabeltyp.getTyp() + "' WHERE materialnummer=" + kabeltyp.getMaterialNummer() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
