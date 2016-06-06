package de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.SQLException;

/**
 * Ein einfacher HSQLDBServer
 * Created by derduke on 03.06.16.
 */
public class HSQLDBServer {

    public final int SERVERPORT = 9001;

    private Server server;

    /**
     * Constuctor zum Starten und gleichzeitigen Verbinden mit HSQLDB.
     *
     * @param path Pfad wo die DB liegt/liegen soll
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws OnlyOneUserExeption
     * @throws IOException
     */
    public HSQLDBServer(String path) throws ClassNotFoundException, SQLException, OnlyOneUserExeption, IOException {
        File lck = new File(path + "lock.lck");
        if (lck.exists()) {
            throw new OnlyOneUserExeption();
        } else {
            lck.createNewFile();
            FileWriter fw = new FileWriter(lck);
            fw.write(InetAddress.getByName(InetAddress.getLocalHost().getHostName()).toString());
            fw.flush();
            fw.close();
            lck.deleteOnExit();

            HsqlProperties p = new HsqlProperties();
            p.setProperty("server.database.0", "file:" + path + "jKabeltrommelHSQLDB");
            p.setProperty("server.dbname.0", "jKabeltrommelHSQLDB");
            p.setProperty("server.port", SERVERPORT + "");
            server = new Server();
            server.setProperties(p);
            server.setLogWriter(new PrintWriter(System.out));  // can use custom writer
            server.setErrWriter(new PrintWriter(System.err));  // can use custom writer

        }
    }

    public void start() {
        server.start();
    }
}
