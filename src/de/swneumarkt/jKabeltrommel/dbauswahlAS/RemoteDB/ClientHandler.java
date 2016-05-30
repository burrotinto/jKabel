package de.swneumarkt.jKabeltrommel.dbauswahlAS.RemoteDB;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.HSQLDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.HSQLDB.OnlyOneUserExeption;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by derduke on 28.05.16.
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final IDBWrapper db;

    public ClientHandler(IDBWrapper db, Socket socket) {
        this.db = db;
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, OnlyOneUserExeption, SQLException {
        DBServer s = new DBServer(new HSQLDBWrapper("./"), 4242);
        new Thread(s).start();

        RemoteDBWrapper rDB = new RemoteDBWrapper("localhost", 4242);
        System.out.println(rDB.getTrommelnForTyp(rDB.getAllKabeltypen().get(0)));

    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oOS = new ObjectOutputStream(socket.getOutputStream());
            String befehl = null;
//            while ((befehl = br.readLine()) != null) {
//                Scanner scanner = new Scanner(befehl);
//                String b = scanner.next();
//                int anzahl = scanner.nextInt();
//                List<Object> list = new ArrayList<>();
//                for (int i = 0; i < anzahl; i++) {
//                    list.add(in.readObject());
//                    System.out.println("Read " + (i + 1) + "/" + anzahl + " " + list.get(i).toString());
//                }
//                oOS.writeObject(getObject(b, list));
//                oOS.flush();
//            }
            befehl = br.readLine();
            Scanner scanner = new Scanner(befehl);
            String b = scanner.next();
            int anzahl = scanner.nextInt();
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < anzahl; i++) {
                list.add(in.readObject());
                System.out.println("Read " + (i + 1) + "/" + anzahl + " " + list.get(i).toString());
            }
            oOS.writeObject(getObject(b, list));
            oOS.flush();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Object getObject(String r, List<Object> list) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        System.out.println(r);
        Object ret = null;
        switch (r) {
            case "getTrommelnForTyp":
                ret = db.getTrommelnForTyp((IKabeltypE) list.get(0));
                break;
            case "getStreckenForTrommel":
                ret = db.getStreckenForTrommel((ITrommelE) list.get(0));
                break;
            case "getLiefer":
                ret = db.getLiefer((ITrommelE) list.get(0));
                break;
            case "getLieferant":
                ret = db.getLieferant((IGeliefertE) list.get(0));
                break;
            case "getTyp":
                ret = db.getTyp((ITrommelE) list.get(0));
                break;
            case "createKabeltyp":
                ret = db.createKabeltyp((String) list.get(0), (Integer) list.get(1));
                break;
            case "createStrecke":
                ret = db.createStrecke((Integer) list.get(0), (String) list.get(1), (Long) list.get(2), (Integer) list.get(3), (Integer) list.get(4), (ITrommelE) list.get(5));
                break;
            case "createTrommel":
                ret = db.createTrommel((IKabeltypE) list.get(0), (String) list.get(1), (Integer) list.get(2), (String) list.get(3), (Integer) list.get(4), (ILieferantE) list.get(5), (Long) list.get(6), (String) list.get(7));
                break;
            case "createLieferant":
                ret = db.createLieferant((String) list.get(0));
                break;
            case "updateTyp":
                ret = db.update((IKabeltypE) list.get(0));
                break;
            case "updateStrecke":
                ret = db.update((IStreckeE) list.get(0));
                break;
            case "updateTrommel":
                ret = db.update((ITrommelE) list.get(0));
                break;
            case "updateLieferant":
                ret = db.update((ILieferantE) list.get(0));
                break;
            case "updateGeliefert":
                ret = db.update((IKabeltypE) list.get(0));
                break;
            case "remove":
                ret = db.remove((IStreckeE) list.get(0));
                break;
            default:
                //Für ohne Parameter benutzen wir die Reflections
                ret = db.getClass().getMethod(r).invoke(db);
        }
        System.out.println(ret.toString());
        return ret;
    }
}