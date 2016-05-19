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
    Statement stmnt = null;
    HSQLDBWrapper(String path) throws ClassNotFoundException, SQLException {
        Class.forName( "org.hsqldb.jdbcDriver" );
        con = DriverManager.getConnection("jdbc:hsqldb:file:"+path+"jKabeltrommelDB;shutdown=true", "sa", "" );
        stmnt = con.createStatement();
        try {
            stmnt.execute("create table kabeltyp(name VARCHAR (64) NOT NULL PRIMARY KEY , materialnummer integer not null);");
            stmnt.executeUpdate("create table trommel(id IDENTITY, trommelnummer VARCHAR(64) NOT NULL, gesamtlaenge INTEGER, lieferdatum DATE); ");
            stmnt.executeUpdate("create TABLE strecke(id IDENTITY, ba INTEGER, ort VARCHAR(64), verlegedatum DATE , start INTEGER , ende INTEGER );");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stmnt.executeUpdate("INSERT INTO kabeltyp (name, materialnummer) VALUES('4x40',102008 )");
    }

    @Override
    public List<KabeltypE> getAllKabeltypen() {
        ArrayList<KabeltypE> list = new ArrayList<>();
        try {
            ResultSet rs = stmnt.executeQuery("Select * FROM kabeltyp;");
            while(rs.next()){
                list.add(new KabeltypE(rs.getString(1),rs.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
