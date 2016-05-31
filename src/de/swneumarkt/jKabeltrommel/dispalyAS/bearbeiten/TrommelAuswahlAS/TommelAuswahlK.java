package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.TrommelAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IStreckeE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ITrommelE;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by derduke on 22.05.16.
 */
class TommelAuswahlK {
    private final IDBWrapper db;
    private Comparator<ITrommelE> comperator = null;


    public TommelAuswahlK(IDBWrapper db) {
        this.db = db;
        comperator = new Comparator<ITrommelE>() {
            @Override
            public int compare(ITrommelE o1, ITrommelE o2) {
                return db.getLiefer(o2).getDatum() < db.getLiefer(o2).getDatum() ? -1 : 1;
            }
        };
    }

    public List<ITrommelE> getAllTrommelForMatNr(IKabeltypE typ) {
        List<ITrommelE> list = db.getTrommelnForTyp(typ);
        Collections.sort(list, comperator);
        return list;
    }

    public int getRestMeter(ITrommelE trommel) {
        int laenge = trommel.getGesamtlaenge();
        for (IStreckeE s : db.getStreckenForTrommel(trommel)) {
            laenge -= s.getMeter();
        }
        return laenge;
    }

    public boolean isAusserHaus(ITrommelE t) {
        return getBaustelle(t) != null;
    }

    public String getBaustelle(ITrommelE t) {
        for (IStreckeE s : db.getStreckenForTrommel(t)) {
            if (s.getEnde() < 0 || s.getStart() < 0) {
                return s.getOrt();
            }
        }
        return null;
    }

    public void setSortierReihenfolge(Comparator<ITrommelE> comperator) {
        if (comperator != null) {
            this.comperator = comperator;
        }
    }
}
