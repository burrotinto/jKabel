package de.swneumarkt.jKabeltrommel.dispalyAS.LieferantAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.LieferantE;

import java.util.Vector;

/**
 * Created by derduke on 24.05.16.
 */
class LieferantenAuswahlK {

    private final IDBWrapper db;

    public LieferantenAuswahlK(IDBWrapper db) {
        this.db = db;
    }

    public Vector<LieferantE> getLieferanten() {
        return new Vector<LieferantE>(db.getAllLieferanten());
    }
}
