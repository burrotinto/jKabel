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

import de.burrotinto.jKabel.dbauswahlAS.serverStatus.entities.IServerStatus;
import de.burrotinto.jKabel.dbauswahlAS.serverStatus.entities.ServerStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by derduke on 05.06.16.
 */
class ClientHandler implements Runnable {
    private final Socket socket;
    private final StatusServer statusServer;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket socket, StatusServer statusServer) throws IOException {
        this.socket = socket;
        this.statusServer = statusServer;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        objectInputStream = new ObjectInputStream(socket.getInputStream());

    }

    @Override
    public void run() {
        Object o = null;
        try {
            while ((o = objectInputStream.readObject()) != null) {
                if (o instanceof String) {
                    String string = (String) o;
                    switch (string) {
                        case "Status":
                            IServerStatus iss = new ServerStatus(statusServer.getAnzahlClients());
                            objectOutputStream.writeObject(iss);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}

