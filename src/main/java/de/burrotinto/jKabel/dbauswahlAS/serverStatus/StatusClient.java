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

import java.io.*;
import java.net.Socket;

/**
 * Created by derduke on 07.06.16.
 */
public class StatusClient implements IStatusClient {

    private final Socket socket;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public StatusClient(Socket socket) throws IOException {
        this.socket = socket;
        objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }


    public boolean isConnected() {
        return !socket.isClosed();
    }

    public int getAnzahlClients() throws IOException {
        sendObject("Status");
        try {
            return ((IServerStatus) objectInputStream.readObject()).anzahlClients();
        } catch (ClassNotFoundException e) {
            return -1;
        }
    }

    @Override
    public void sendObject(Object o) throws IOException {
        objectOutputStream.writeObject(o);
        objectOutputStream.flush();
    }
}
