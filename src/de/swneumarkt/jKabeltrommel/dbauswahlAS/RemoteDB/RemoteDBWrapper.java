package de.swneumarkt.jKabeltrommel.dbauswahlAS.RemoteDB;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derduke on 29.05.16.
 */
public class RemoteDBWrapper implements IDBWrapper {
    private final String host;
    private final int port;
    private Socket so = null;
    private BufferedWriter bw = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream oi = null;

    public RemoteDBWrapper(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    private Object makeServerRequset(String r, Object parameter) {
        List<Object> l = new ArrayList<>();
        l.add(parameter);
        return makeServerRequset(r, l);
    }

    private Object makeServerRequset(String r, List<? extends Object> parameters) {
        try {
            if (so == null) {
                so = new Socket(host, port);
                bw = new BufferedWriter(new OutputStreamWriter(so.getOutputStream()));
                objectOutputStream = new ObjectOutputStream(so.getOutputStream());
                oi = new ObjectInputStream(so.getInputStream());
            }
            bw.write(r + " " + parameters.size() + System.lineSeparator());
            bw.flush();

            // Parameter nachsenden
            for (Object o : parameters) {
                objectOutputStream.writeObject(o);
            }
            objectOutputStream.flush();

            // Rückgabe warten
            Object rO = oi.readObject();
            System.out.println("Retrive " + rO.toString());
            so.close();
            so = null;
            return rO;

        } catch (Exception e) {
            e.printStackTrace();
            so = null;
            return null;
        }
    }

    @Override
    public List<IKabeltypE> getAllKabeltypen() {
        return (List<IKabeltypE>) makeServerRequset("getAllKabeltypen", new ArrayList<>());
    }

    @Override
    public List<ITrommelE> getTrommelnForTyp(IKabeltypE kabeltyp) {
        List<Object> list = new ArrayList<>();
        list.add(kabeltyp);
        return (List<ITrommelE>) makeServerRequset("getTrommelnForTyp", list);
    }

    @Override
    public List<IStreckeE> getStreckenForTrommel(ITrommelE trommel) {
        List<Object> list = new ArrayList<>();
        list.add(trommel);
        return (List<IStreckeE>) makeServerRequset("getStreckenForTrommel", list);
    }

    @Override
    public List<ILieferantE> getAllLieferanten() {
        return (List<ILieferantE>) makeServerRequset("getAllLieferanten", new ArrayList<>());
    }

    @Override
    public List<String> getAllTexteForBA(int ba) {
        //todo

        return new ArrayList<>();
    }

    @Override
    public IGeliefertE getLiefer(ITrommelE trommel) {
        List<Object> list = new ArrayList<>();
        list.add(trommel);
        return (IGeliefertE) makeServerRequset("getLiefer", list);
    }

    @Override
    public ILieferantE getLieferant(IGeliefertE liefert) {
        return (ILieferantE) makeServerRequset("getLieferant", liefert);
    }

    @Override
    public IKabeltypE getTyp(ITrommelE trommel) {
        return (IKabeltypE) makeServerRequset("getTyp", trommel);
    }

    @Override
    public boolean update(IKabeltypE kabeltyp) {
        return (Boolean) makeServerRequset("updateKabeltyp", kabeltyp);
    }

    @Override
    public boolean createKabeltyp(String name, int materialnummer) {

        List<Object> list = new ArrayList<>();
        list.add(name);
        list.add(materialnummer);
        return (Boolean) makeServerRequset("createKabeltyp", list);
    }

    @Override
    public boolean update(IStreckeE strecke) {
        return (Boolean) makeServerRequset("updateStrecke", strecke);
    }

    @Override
    public boolean createStrecke(int ba, String ort, long verlegedatum, int start, int ende, ITrommelE trommel) {
        List<Object> list = new ArrayList<>();
        list.add(ba);
        list.add(ort);
        list.add(verlegedatum);
        list.add(start);
        list.add(ende);
        list.add(trommel);
        return (Boolean) makeServerRequset("createStrecke", list);
    }

    @Override
    public boolean update(ITrommelE trommel) {
        return (Boolean) makeServerRequset("updateTrommel", trommel);
    }

    @Override
    public boolean createTrommel(IKabeltypE kabelTyp, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start, ILieferantE lieferantE, long Lieferdatum, String lieferscheinNr) {
        List<Object> list = new ArrayList<>();
        list.add(kabelTyp);
        list.add(trommelnummer);
        list.add(gesamtlaenge);
        list.add(lagerPlatz);
        list.add(start);
        list.add(lieferantE);
        list.add(Lieferdatum);
        list.add(lieferscheinNr);
        return (Boolean) makeServerRequset("createTrommel", list);
    }

    @Override
    public boolean remove(IStreckeE strecke) {
        return (Boolean) makeServerRequset("remove", strecke);
    }

    @Override
    public boolean createLieferant(String name) {
        return (Boolean) makeServerRequset("createLieferant", name);
    }


    @Override
    public boolean update(ILieferantE lieferantE) {
        return (Boolean) makeServerRequset("updateLieferant", lieferantE);
    }

    @Override
    public boolean update(IGeliefertE geliefert) {
        return (Boolean) makeServerRequset("updateGeliefert", geliefert);
    }
}
