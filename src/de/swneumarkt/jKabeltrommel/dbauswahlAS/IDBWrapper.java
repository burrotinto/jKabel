package de.swneumarkt.jKabeltrommel.dbauswahlAS;

import de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys.*;

import java.util.List;

/**
 * Regelt wie eine DB zu benutzen ist. Die Methodennamen sollten selbserklärend sein
 * Created by derduke on 19.05.2016.
 */
public interface IDBWrapper {
    List<IKabeltypE> getAllKabeltypen();

    List<ITrommelE> getTrommelnForTyp(IKabeltypE kabeltyp);

    List<IStreckeE> getStreckenForTrommel(ITrommelE trommel);

    List<ILieferantE> getAllLieferanten();

    List<String> getAllTexteForBA(int ba);

    IGeliefertE getLiefer(ITrommelE trommel);

    ILieferantE getLieferant(IGeliefertE liefert);

    IKabeltypE getTyp(ITrommelE trommel);


    boolean update(IKabeltypE kabeltyp);

    boolean createKabeltyp(String name, int materialnummer);

    boolean update(IStreckeE strecke);

    boolean createStrecke(int ba, String ort, long verlegedatum, int start, int ende, ITrommelE trommel);

    boolean update(ITrommelE trommel);

    boolean createTrommel(IKabeltypE kabelTyp, String trommelnummer, int gesamtlaenge, String lagerPlatz, int start, ILieferantE lieferantE, long Lieferdatum, String lieferscheinNr);

    boolean remove(IStreckeE strecke);

    boolean createLieferant(String name);

    boolean update(ILieferantE lieferantE);

    boolean update(IGeliefertE geliefert);

}
