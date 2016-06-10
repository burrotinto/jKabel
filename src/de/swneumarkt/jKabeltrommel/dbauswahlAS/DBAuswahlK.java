package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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

    void handleIPFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        // Datei einlesen
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<InetAddress> inetAddressList = getAllIPfromFile(file);

        BufferedWriter fw = new BufferedWriter(new FileWriter(file));
        for (InetAddress ia : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
            if (!inetAddressList.contains(ia)) {
                fw.write(ia.getHostAddress());
                fw.write(System.lineSeparator());
            }
        }
        for (InetAddress ia : inetAddressList) {
            fw.write(ia.getHostAddress());
            fw.write(System.lineSeparator());
        }
        fw.flush();
        fw.close();
    }
}
