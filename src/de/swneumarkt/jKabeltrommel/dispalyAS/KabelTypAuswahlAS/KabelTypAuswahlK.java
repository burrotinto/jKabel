package de.swneumarkt.jKabeltrommel.dispalyAS.KabelTypAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by derduke on 21.05.16.
 */
class KabelTypAuswahlK {
    public final IDBWrapper db;

    public KabelTypAuswahlK(IDBWrapper db) {
        this.db = db;
    }

    public HashMap<Integer,String> getTypenMap() {
        HashMap<Integer,String> map= new HashMap<>();
        db.getAllKabeltypen().forEach(new Consumer<KabeltypE>() {
            @Override
            public void accept(KabeltypE kabeltypE) {
                map.put(kabeltypE.getMaterialNummer(),kabeltypE.getTyp());
            }
        });
        return map;
    }

    public KabeltypE getTyp(Integer integer) {
        for(KabeltypE k : db.getAllKabeltypen()){
            if(k.getMaterialNummer() == integer){
                return k;
            }
        }
        return null;
    }
}

