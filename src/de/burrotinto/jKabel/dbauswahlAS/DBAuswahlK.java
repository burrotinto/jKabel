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

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Kontrollklasse
 * Created by derduke on 10.06.16.
 */
class DBAuswahlK {


    /**
     * Gibt eine Liste aller IP Adressen aus die in der Dateui gefunden wurden
     *
     * @param file
     * @return IPadressenliste
     */
    List<InetAddress> getAllIPfromFile(File file) {
        List<InetAddress> inetAddressList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String next = null;
            while ((next = br.readLine()) != null) {
                try {
                    inetAddressList.add(InetAddress.getByName(next));
                } catch (Exception e) {

                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inetAddressList;
    }

    /**
     * Erweitert im endeffekt die IPliste um die eigenen IP
     *
     * @param file
     * @throws IOException
     */
    void handleIPFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        // Datei einlesen
        BufferedReader br = new BufferedReader(new FileReader(file));
        Set<InetAddress> inetAddressList = new HashSet<>(getAllIPfromFile(file));

        BufferedWriter fw = new BufferedWriter(new FileWriter(file));
        for (InetAddress ia : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
            inetAddressList.add(ia);
        }

        for (InetAddress ia : inetAddressList) {
            fw.write(ia.getHostAddress());
            fw.write(System.lineSeparator());
        }
        fw.flush();
        fw.close();
    }
}
