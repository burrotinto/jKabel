package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.KabeltypE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.StreckeE;
import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.TrommelE;

import java.util.List;

/**
 * Created by derduke on 19.05.2016.
 */
public interface IDBWrapper {
    List<KabeltypE> getAllKabeltypen();
    List<TrommelE> getTrommelnForTyp(KabeltypE kabeltyp);

    boolean update(KabeltypE kabeltyp);

    boolean create(KabeltypE kabeltyp);

    boolean update(StreckeE strecke);

    boolean create(StreckeE strecke);

    boolean update(TrommelE trommel);

    boolean create(TrommelE trommel);
}
