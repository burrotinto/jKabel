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

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.SQLException;

/**
 * Ein einfacher HSQLDBServer
 * Created by Florian Klinger on 03.06.16.
 */
public class HSQLDBServer {

    public static final String DBPREFIX = "jKabelHSQLDB";
    public static final int SERVERPORT = 9001;
public static final String LOCK_FILENAME = "lock.lck";
    private final InetAddress ip;

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
    public HSQLDBServer(String path) throws ClassNotFoundException, SQLException, OnlyOneUserExeption, IOException, ServerAcl.AclFormatException {
        if (portInUse(SERVERPORT)) {
            throw new IOException();
        }
        // Lockfile
        File lck = new File(path + LOCK_FILENAME);
        if (!lck.createNewFile()) {
            throw new OnlyOneUserExeption();
        } else {
            ip = InetAddress.getByName(InetAddress.getLocalHost().getHostName());

            // Lockfile beschreiben
            BufferedWriter fw = new BufferedWriter(new FileWriter(lck));
            fw.write("Lock created by: ");
            fw.write(System.getProperty("user.name"));
            fw.newLine();
            fw.write("From adress: ");
            fw.write(getIP().toString());
            fw.flush();
            fw.close();
            lck.deleteOnExit();

            //HSQLDB initialisieren
            HsqlProperties p = new HsqlProperties();
            p.setProperty("server.database.0", "file:" + path + HSQLDBServer.DBPREFIX);
            p.setProperty("server.dbname.0", HSQLDBServer.DBPREFIX);
            p.setProperty("server.port", SERVERPORT + "");
            server = new Server();
            server.setProperties(p);
            server.setLogWriter(new PrintWriter(System.out));
            server.setErrWriter(new PrintWriter(System.err));

        }
    }

    /**
     * Startet die HSQLDB Server Datenbank
     */
    public void start() {
        server.start();
    }

    /**
     * Rückgabe der Inetadresse deses Servers
     *
     * @return ip
     */
    public InetAddress getIP() {
        return ip;
    }

    /**
     * Testet ob der Port schon belegt ist
     *
     * @param port zu testend
     * @return false wenn belegt, true sonst
     */
    private boolean portInUse(int port) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return false;
        } catch (IOException e) {
        } finally {

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }
}
