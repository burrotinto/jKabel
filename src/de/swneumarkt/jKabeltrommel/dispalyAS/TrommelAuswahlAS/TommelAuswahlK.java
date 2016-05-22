package de.swneumarkt.jKabeltrommel.dispalyAS.TrommelAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.StreckeE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by derduke on 22.05.16.
 */
class TommelAuswahlK {
    private final IDBWrapper db;
    public TommelAuswahlK(IDBWrapper db) {
        this.db = db;
    }

    public List<TrommelE> getAllTrommelForMatNr(KabeltypE typ){
        List<TrommelE> list = db.getTrommelnForTyp(typ);
        Collections.sort(list, new Comparator<TrommelE>() {
            @Override
            public int compare(TrommelE o1, TrommelE o2) {
                return getRestMeter(o2) - getRestMeter(o1);
            }
        });
        return list;
    }

    public int getRestMeter(TrommelE trommel){
        int laenge = trommel.getGesamtlaenge();
        for(StreckeE s: db.getStreckenForTrommel(trommel)){
           laenge-= s.getMeter();
        }
        return  laenge;
    }
}
