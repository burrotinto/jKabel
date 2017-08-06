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

package de.burrotinto.jKabel.dbauswahlAS;

import de.burrotinto.jKabel.config.ConfigReader;
import de.burrotinto.jKabel.dbauswahlAS.HSQLDB.HSQLDBServer;
import de.burrotinto.jKabel.dbauswahlAS.HSQLDB.HSQLDBWrapper;
import de.burrotinto.jKabel.dbauswahlAS.HSQLDB.OnlyOneUserExeption;
import de.burrotinto.jKabel.dbauswahlAS.serverStatus.IStatusClient;
import de.burrotinto.jKabel.dbauswahlAS.serverStatus.StatusClient;
import de.burrotinto.jKabel.dbauswahlAS.serverStatus.StatusServer;
import org.hsqldb.server.ServerAcl;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

/**
 * Startet den Akteursanwendungsfall der DB auswahl.
 * Created by derduke on 19.05.2016.
 */
@Configuration
public class DBAuswahlAAS {
    private final ConfigReader configReader;
    private final DBAuswahlK kontroll;
    private String pfad;
    private HSQLDBServer server = null;
    private InetAddress serverIP = null;

    public DBAuswahlAAS(ConfigReader configReader) {
        this.configReader = configReader;
        kontroll = new DBAuswahlK();
    }

    @Nullable
    private String choosePath(String pfad) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(pfad));
        chooser.setDialogTitle("DB Pfad");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION ? null : chooser.getSelectedFile().getPath() + File.separator;

    }

    /**
     * Gibt eine Datenbank zurück.
     *
     * @return eine DB
     */
    @Bean
    public IDBWrapper getDBWrapper() {
        IDBWrapper db = null;

        pfad = configReader.getPath();
        if (pfad == null) {
            pfad = choosePath(ConfigReader.Companion.getSWNPFAD());
            try {
                configReader.savePath(pfad);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            server = getServer();
            server.start();
            try {
                new Thread(new StatusServer()).start();
            } catch (IOException ioE) {
                ioE.printStackTrace();
                //todo
            }

        } catch (Exception e) {
            // Server kann nur gestartet werden wenn er der einzige ist der auf die DB dateien zugreift
        }
        db = connectRemoteDB();
        return db;
    }

    /**
     * Versucht sich mit der entfernten Datenbank zu verbinden
     *
     * @param ip Adresse der DB
     * @return Wrapper zur angeforderten DB, Null wenn keine Verbindung möglich war.
     */
    private IDBWrapper connectRemoteDB(InetAddress ip) {
        try {
            if (ip.isReachable(100))
                try {
                    HSQLDBWrapper idbWrapper = new HSQLDBWrapper(ip);
                    serverIP = ip;
                    return idbWrapper;
                } catch (Exception e) {
                    serverIP = null;
                    return null;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public IStatusClient getStatusClient() throws IOException {
        return new StatusClient(new Socket(serverIP, StatusServer.PORT));
    }

    private IDBWrapper connectRemoteDB() {
        IDBWrapper db = null;
        List<InetAddress> inetAddressList = kontroll.getAllIPfromFile(new File(pfad + "serverIp.txt"));
        int i = 0;
        while (i < inetAddressList.size() && db == null) {
            db = connectRemoteDB(inetAddressList.get(i++));
        }
        if (db == null) {
            new File(pfad + HSQLDBServer.LOCK_FILENAME).delete();
            db = getDBWrapper();
        }

        return db;
    }

    private HSQLDBServer getServer() throws IOException, OnlyOneUserExeption, SQLException, ClassNotFoundException, ServerAcl.AclFormatException {
        HSQLDBServer server = new HSQLDBServer(pfad);
        kontroll.handleIPFile(new File(pfad + "serverIp.txt"));

        return server;
    }

    public boolean hasServer() {
        return server != null;
    }

    public InetAddress getServerIP() {
        return serverIP;
    }
}
