package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.streckenAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.*;

import java.util.*;

/**
 * Created by derduke on 22.05.16.
 */
class StreckenK {
    private final IDBWrapper db;
    private IKabeltypE typ = null;

    StreckenK(IDBWrapper db) {
        this.db = db;
    }

    public List<IStreckeE> getStreckenForTrommel(ITrommelE trommel) {
        List<IStreckeE> strecken = db.getStreckenForTrommel(trommel);

        Collections.sort(strecken, new Comparator<IStreckeE>() {
            @Override
            public int compare(IStreckeE o1, IStreckeE o2) {
                return (int) (-o2.getVerlegedatum() + o1.getVerlegedatum());
            }
        });
        return strecken;
    }

    public IKabeltypE getTyp(ITrommelE trommel) {
        return db.getTyp(trommel);
    }

    public void setTyp(IKabeltypE typ) {
        this.typ = typ;
    }

    public void update(IStreckeE s) {
        db.update(s);
    }

    public void update(ITrommelE trommel) {
        db.update(trommel);
    }

    public void update(IKabeltypE typ) {
        db.update(typ);
    }

    String getTimeString(long t){
        StringBuilder sb = new StringBuilder();
        Date d = new Date(t);
        sb.append(d.getDate()).append(".").append((d.getMonth()+1)).append(".").append(d.getYear()+1900);
        return sb.toString();
    }

    public void remove(IStreckeE strecke) {
        db.remove(strecke);
    }

    public boolean richtigeRichtung(ITrommelE trommel, int start, int ende) {
        List<IStreckeE> list = getStreckenForTrommel(trommel);
        if(list.size() == 0){
            return true;
        } else {
            int x = list.get(0).getStart()-list.get(0).getEnde();
            int y = start - ende;
            return (x <= 0 && y <= 0) || (x>= 0 && y >= 0);
        }

    }

    public IGeliefertE getLiefer(ITrommelE trommel) {
        return db.getLiefer(trommel);
    }

    public long getLieferDate(ITrommelE trommel) {
        return getLiefer(trommel).getDatum();
    }

    public ILieferantE getLieferant(ITrommelE trommel) {
        return db.getLieferant(getLiefer(trommel));
    }

    public String getLieferscheinNR(ITrommelE trommel) {
        return getLiefer(trommel).getLieferscheinNr();
    }

    public Vector<ILieferantE> getLieferanten() {
        return new Vector<ILieferantE>(db.getAllLieferanten());
    }

    public void update(IGeliefertE g) {
        db.update(g);
    }

    public void eintragenStrecke(int ba, String text, long l, int start, int ende, ITrommelE trommel) {
        db.createStrecke(ba, text, l, start, ende, trommel);
    }

    public String getTextForBA(int ba) {
        List<String> list = db.getAllTexteForBA(ba);
        return list == null ? "" : list.get(0);
    }

    public boolean istAusserHaus(ITrommelE trommel) {
        for (IStreckeE s : getStreckenForTrommel(trommel)) {
            if (s.getEnde() < 0 || s.getStart() < 0) {
                return true;
            }
        }
        return false;
    }

    public int getRestMeter(ITrommelE trommel) {
        int laenge = trommel.getGesamtlaenge();
        for (IStreckeE s : db.getStreckenForTrommel(trommel)) {
            laenge -= s.getMeter();
        }
        return laenge;
    }
}
