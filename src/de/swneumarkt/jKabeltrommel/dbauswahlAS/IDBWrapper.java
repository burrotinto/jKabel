package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.entytis.*;

import java.util.List;

/**
 * Created by derduke on 19.05.2016.
 */
public interface IDBWrapper {
    List<KabeltypE> getAllKabeltypen();

    List<TrommelE> getTrommelnForTyp(KabeltypE kabeltyp);

    List<StreckeE> getStreckenForTrommel(TrommelE trommel);

    List<LieferantE> getAllLieferanten();

    GeliefertE getLiefer(TrommelE trommel);

    LieferantE getLieferant(GeliefertE liefert);

    KabeltypE getTyp(TrommelE trommel);

    boolean update(KabeltypE kabeltyp);

    boolean create(KabeltypE kabeltyp);

    boolean update(StreckeE strecke);

    boolean create(StreckeE strecke);

    boolean update(TrommelE trommel);

    boolean create(TrommelE trommel, LieferantE lieferantE, GeliefertE geliefert);

    boolean remove(StreckeE strecke);

    boolean create(LieferantE lieferant);

    boolean update(LieferantE lieferantE);

    boolean update(GeliefertE geliefert);
}
