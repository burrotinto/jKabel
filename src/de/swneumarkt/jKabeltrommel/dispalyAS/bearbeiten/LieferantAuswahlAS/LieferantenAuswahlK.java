package de.swneumarkt.jKabeltrommel.dispalyAS.bearbeiten.LieferantAuswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.IDBWrapper;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.ILieferantE;

import java.util.Vector;

/**
 * Created by derduke on 24.05.16.
 */
class LieferantenAuswahlK {

    private final IDBWrapper db;

    public LieferantenAuswahlK(IDBWrapper db) {
        this.db = db;
    }

    public Vector<ILieferantE> getLieferanten() {
        return new Vector<ILieferantE>(db.getAllLieferanten());
    }
}
