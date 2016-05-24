package de.swneumarkt.jKabeltrommel.dispalyAS.StreckenAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.*;

import java.util.*;

/**
 * Created by derduke on 22.05.16.
 */
class StreckenK {
    private final IDBWrapper db;
    private KabeltypE typ = null;

    public StreckenK(IDBWrapper db) {
        this.db = db;
    }

    public List<StreckeE> getStreckenForTrommel(TrommelE trommel){
       List<StreckeE> strecken = db.getStreckenForTrommel(trommel);

        Collections.sort(strecken, new Comparator<StreckeE>() {
            @Override
            public int compare(StreckeE o1, StreckeE o2) {
                return (int) (-o2.getVerlegedatum() + o1.getVerlegedatum());
            }
        });
        return strecken;
    }
    public KabeltypE getTyp(TrommelE trommel){
        return db.getTyp(trommel);
    }

    public void eintragen(StreckeE strecke) {
    db.create(strecke);
    }

    public void setTyp(KabeltypE typ) {
        this.typ = typ;
    }

    public void update(StreckeE s) {
        db.update(s);
    }

    public void update(TrommelE trommel) {
        db.update(trommel);
    }

    public void update(KabeltypE typ) {
        db.update(typ);
    }

    String getTimeString(long t){
        StringBuilder sb = new StringBuilder();
        Date d = new Date(t);
        sb.append(d.getDate()).append(".").append((d.getMonth()+1)).append(".").append(d.getYear()+1900);
        return sb.toString();
    }

    public void remove(StreckeE strecke) {
        db.remove(strecke);
    }

    public boolean richtigeRichtung(TrommelE trommel, int start, int ende) {
        List<StreckeE> list = getStreckenForTrommel(trommel);
        if(list.size() == 0){
            return true;
        } else {
            int x = list.get(0).getStart()-list.get(0).getEnde();
            int y = start - ende;
            return (x <= 0 && y <= 0) || (x>= 0 && y >= 0);
        }

    }
    public GeliefertE getLiefer(TrommelE trommel){
        return db.getLiefer(trommel);
    }

    public long getLieferDate(TrommelE trommel) {
        return getLiefer(trommel).getDatum();
    }

    public LieferantE getLieferant(TrommelE trommel) {
        return db.getLieferant(getLiefer(trommel));
    }

    public String getLieferscheinNR(TrommelE trommel){
        return getLiefer(trommel).getLieferscheinNr();
    }

    public Vector<LieferantE> getLieferanten() {
        return new Vector<LieferantE>(db.getAllLieferanten());
    }

    public void update(GeliefertE g) {
        db.update(g);
    }
}
