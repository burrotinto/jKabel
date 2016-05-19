package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.entytis.KabeltypE;

import java.util.List;

/**
 * Created by derduke on 19.05.2016.
 */
public interface IDBWrapper {
    List<KabeltypE> getAllKabeltypen();
    void update(KabeltypE kabeltyp);
}
