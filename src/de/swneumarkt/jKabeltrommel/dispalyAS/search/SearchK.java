package de.swneumarkt.jKabeltrommel.dispalyAS.search;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IKabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.IStreckeE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ITrommelE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by derduke on 25.05.16.
 */
class SearchK {
    private final IDBWrapper db;

    public SearchK(IDBWrapper db) {
        this.db = db;
    }

    public List<ITrommelE> getAllTrommelWithBA(int ba, IKabeltypE typ) {
        List<ITrommelE> list = new ArrayList<>();
        for (ITrommelE t : db.getTrommelnForTyp(typ)) {
            for (IStreckeE s : db.getStreckenForTrommel(t)) {
                if (s.getBa() == ba) {
                    list.add(t);
                    break;
                }
            }
        }
        return list;
    }

    public List<IStreckeE> getAllStrecken(ITrommelE trommel) {
        return db.getStreckenForTrommel(trommel);
    }

    public Vector<IKabeltypE> getAllKAbelTypen() {
        return new Vector<>(db.getAllKabeltypen());
    }
}
