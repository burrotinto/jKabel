package de.swneumarkt.jKabeltrommel.dbauswahlAS.enitys;

/**
 * Created by derduke on 25.05.16.
 */
public interface IGeliefertE {
    long getDatum();

    String getLieferscheinNr();

    ITrommelE getTrommel();

    ILieferantE getLieferant();

    void setLieferantID(ILieferantE lieferant);
}
