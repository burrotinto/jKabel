package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

import java.io.Serializable;

/**
 * Created by derduke on 25.05.16.
 */
public interface IGeliefertE extends Serializable {
    long getDatum();

    String getLieferscheinNr();

    ITrommelE getTrommel();

    ILieferantE getLieferant();

    void setLieferantID(ILieferantE lieferant);
}
