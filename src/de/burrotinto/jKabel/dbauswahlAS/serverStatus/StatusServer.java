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

package de.burrotinto.jKabel.dbauswahlAS.serverStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by derduke on 03.06.16.
 */
public class StatusServer implements Runnable {
    public static int PORT = 12345;
    private ServerSocket ssO;
    private List<ClientHandler> sockets = new LinkedList<>();
    private Set<IConnectionListner> connectionListners = new HashSet<IConnectionListner>();


    public StatusServer() throws IOException {
        ssO = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        while (!isStoppt()) {
            try {
                ClientHandler cH = new ClientHandler(ssO.accept(), this);
                synchronized (ssO) {
                    sockets.add(cH);
                }
                new Thread(cH).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void addIConnectionListner(IConnectionListner iCL) {
        connectionListners.add(iCL);
    }

    private boolean isStoppt() {
        return false;
    }

    public int getAnzahlClients() {
        List<ClientHandler> l = new LinkedList<>();
        synchronized (ssO) {
            for (ClientHandler s : sockets) {
                if (!s.isClosed()) {
                    l.add(s);
                }
            }
            sockets = l;
        }
        return l.size();
    }
}
