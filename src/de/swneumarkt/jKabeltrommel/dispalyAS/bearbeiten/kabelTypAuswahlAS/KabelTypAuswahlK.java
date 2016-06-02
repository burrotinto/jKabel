package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.kabelTypAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;

import java.util.List;

/**
 * Created by derduke on 21.05.16.
 */
class KabelTypAuswahlK {
    public final IDBWrapper db;

    public KabelTypAuswahlK(IDBWrapper db) {
        this.db = db;
    }

    public List<IKabeltypE> getTypen() {

        return db.getAllKabeltypen();
    }

    public IKabeltypE getTyp(Integer integer) {
        for (IKabeltypE k : db.getAllKabeltypen()) {
            if(k.getMaterialNummer() == integer){
                return k;
            }
        }
        return null;
    }
}

